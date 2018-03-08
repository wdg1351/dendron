package dendron.machine;

import dendron.Errors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * An abstraction of a computing machine that reads instructions
 * and executes them. It has an instruction set, a symbol table
 * for variables (instead of general-purpose memory), and a
 * value stack on which calculations are performed.
 * <p>
 * (Everything is static to avoid the need to master the subtleties
 * of nested class instantiation or to pass the symbol table and
 * stack into every instruction when it executes.)
 * <p>
 * THIS CLASS IS INCOMPLETE. The student must add code to it.
 *
 * @author James Heliotis
 * @author YOUR NAME HERE
 */
public class Machine {

  /**
   * Do not instatiate this class.
   */
  private Machine() {
  }

  public static interface Instruction {
    /**
     * Run this instruction on the Machine, using the Machine's
     * value stack and symbol table.
     */
    void execute();

    /**
     * Show the instruction using text so it can be understood
     * by a person.
     *
     * @return a short string describing what this instruction will do
     */
    @Override
    String toString();
  }

  private static Map<String, Integer> table = null;
  private static Stack<Integer> stack = null;

  /**
   * Reset the Machine to a pristine state.
   *
   * @see Machine#execute
   */
  private static void reset() {
    stack = new Stack<>();
    table = new HashMap<>();
  }

  /**
   * Generate a listing of a program on standard output by
   * calling the toString() method on each instruction
   * contained therein, in order.
   *
   * @param program the list of instructions in the program
   */
  public static void displayInstructions(
      List<Machine.Instruction> program) {
    System.out.println("\nCompiled code:");
    for (Machine.Instruction instr : program) {
      System.out.println(instr);
    }
    System.out.println();
  }

  /**
   * Run a "compiled" program by executing in order each instruction
   * contained therein.
   * Report on the final size of the stack (should normally be empty)
   * and the contents of the symbol table.
   *
   * @param program a list of Machine instructions
   */
  public static void execute(List<Instruction> program) {
    reset();
    System.out.println("Executing compiled code...");
    for (Instruction instr : program) {
      instr.execute();
    }
    System.out.println("Machine: execution ended with " +
        stack.size() + " items left on the stack.");
    System.out.println();
    Errors.dump(table);
  }

  /**
   * The ADD instruction
   */
  public static class Add implements Instruction {
    /**
     * Run the microsteps for the ADD instruction.
     */
    @Override
    public void execute() {
      int op2 = stack.pop();
      int op1 = stack.pop();
      stack.push(op1 + op2);
    }

    /**
     * Show the ADD instruction as plain text.
     *
     * @return "ADD"
     */
    @Override
    public String toString() {
      return "ADD";
    }
  }

  /**
   * The STORE instruction
   */
  public static class Store implements Instruction {
    /**
     * stores name of target variable
     */
    private String name;

    /**
     * Create a STORE instruction
     *
     * @param ident the name of the target variable
     */
    public Store(String ident) {
      this.name = ident;
    }

    /**
     * Run the microsteps for the STORE instruction.
     */
    @Override
    public void execute() {
      table.put(this.name, stack.pop());
    }

    /**
     * Show the STORE instruction as plain text.
     *
     * @return "STORE" followed by the target variable name
     */
    @Override
    public String toString() {
      return "STORE\t" + this.name;
    }
  }


  public static class PushConst implements Instruction {
    int c;

    /**
     * Create a PUSH instruction
     *
     * @param i the value of int to push to the stack
     */
    public PushConst(int i) {
      c = i;
    }

    /**
     * Pushes c to the stack
     */
    @Override
    public void execute() {
      stack.push(c);
    }

    /**
     *  Show the PUSH instruction as plain text.
     *
     * @return "PUSH"
     */
    @Override
    public String toString() {
      return "PUSH\t" + c;
    }
  }

  public static class Load implements Instruction {
    String v;
    /**
     * Create a PUSH instruction
     *
     * @param v the key to load from the table
     */
    public Load(String v) {
      this.v = v;
    }

    /**
     * Pushes the value of v to the stack
     */
    @Override
    public void execute() {
      stack.push(table.get(v));
    }

    /**
     *  Show the LOAD instruction as plain text.
     *
     * @return "LOAD"
     */
    @Override
    public String toString() {
      return "LOAD\t" + v;
    }
  }

  public static class Subtract implements Instruction {
    /**
     * Run the microsteps for the SUBTRACT instruction.
     */
    @Override
    public void execute() {
      int op2 = stack.pop();
      int op1 = stack.pop();
      stack.push(op1 - op2);
    }

    /**
     *  Show the SUBTRACT instruction as plain text.
     *
     * @return "SUBTRACT"
     */
    @Override
    public String toString() {
      return "SUB";
    }
  }


  public static class Multiply implements Instruction {

    @Override
    public void execute() {
      int op2 = stack.pop();
      int op1 = stack.pop();
      stack.push(op1 * op2);
    }

    /**
     *  Show the MULTIPLY instruction as plain text.
     *
     * @return "MULTIPLY"
     */
    @Override
    public String toString() {
      return "MUL";
    }
  }

  public static class Divide implements Instruction {
    /**
     * Run the microsteps for the DIVIDE instruction.
     */
    @Override
    public void execute() {
      int op2 = stack.pop();
      int op1 = stack.pop();
      if(op2 == 0) {
        Errors.report(Errors.Type.DIVIDE_BY_ZERO, (op1 + " / " + op2));
      }

      stack.push(op1 / op2);
    }

    /**
     *  Show the DIVIDE instruction as plain text.
     *
     * @return "DIVIDE"
     */
    @Override
    public String toString() {
      return "DIVIDE";
    }
  }

  public static class Negate implements Instruction {
    /**
     * Run the microsteps for the Negate instruction.
     */
    @Override
    public void execute() {
      int op1 = stack.pop();
      stack.push(-op1);
    }

    /**
     *  Show the NEGATE instruction as plain text.
     *
     * @return "NEGATE"
     */
    @Override
    public String toString() {
      return "NEGATE";
    }
  }


  public static class SquareRoot implements Instruction {
    /**
     * Run the microsteps for the SQRT instruction.
     */
    @Override
    public void execute() {
      int op1 = stack.pop();
      stack.push((int) Math.sqrt(op1));
    }

    /**
     *  Show the SQRT instruction as plain text.
     *
     * @return "SQRT"
     */
    @Override
    public String toString() {
      return "SQRT";
    }
  }


  public static class Print implements Instruction {

    /**
     * Prints out the top of the stack
     */
    @Override
    public void execute() {
      int op1 = stack.pop();

      System.out.println("*** " + op1);
    }

    @Override
    public String toString() {
      return "PRINT";
    }
  }
}
