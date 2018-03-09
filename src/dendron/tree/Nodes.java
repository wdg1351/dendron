package dendron.tree;

import dendron.Errors;
import dendron.machine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Nodes {

  /**
   * Creates the Action for assignment
   * This action is created when an action tree starts with the character ":="
   */
  public static class Assignment implements ActionNode {
    String name;
    ExpressionNode val;

    /**
     * @param name The key for the symbol table
     * @param val The expression that when evaluated will be put
     *            in to the symbol table
     */
    public Assignment(String name, ExpressionNode val) {
      this.name = name;
      this.val = val;
    }

    /**
     * Puts the evaluated value into the symbol table stored under
     * the given name found in the constructor.
     * @param symTab the table where variable values are stored
     */
    @Override
    public void execute(Map<String, Integer> symTab) {
      symTab.put(name, val.evaluate(symTab));
    }

    /**
     * Will print out the passed in key value followed by :=
     * Followed by the infix display of the passed in expression
     * ex
     * "x := " + val.infixDisplay()
     */
    @Override
    public void infixDisplay() {
      System.out.print(name + " := ");
      val.infixDisplay();
    }

    /**
     * Creates Instructions for the dendron machine
     *
     * @return Instructions for the dendron machine to run
     */
    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>(val.emit());
      i.add(new Machine.Store(name));
      return i;
    }
  }

  /**
   * Creates the Action for printing
   * This action is created when an action tree starts with the character "@"
   */
  public static class Print implements ActionNode {
    ExpressionNode val;

    /**
     *
     * @param val The expression that when evaluated will be printed
     */
    public Print(ExpressionNode val) {
      this.val = val;
    }

    /**
     * Prints the evaluated output of the given expression
     * @param symTab the table where variable values are stored
     */
    @Override
    public void execute(Map<String, Integer> symTab) {
      System.out.println("=== " + val.evaluate(symTab));
    }

    /**
     * Will print out the passed in key value followed by Print
     * Followed by the infix display of the passed in expression
     * ex
     * "Print " + val.infixDisplay()
     */
    @Override
    public void infixDisplay() {
      System.out.print("Print ");
      val.infixDisplay();
    }

    /**
     * Creates Instructions for the dendron machine
     *
     * @return Instructions for the dendron machine to run
     */
    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>(val.emit());
      i.add(new Machine.Print());
      return i;
    }
  }

  /**
   * Creates a constant then when evaluated returns the given constant
   */
  public static class Constant implements ExpressionNode {
    private int val;

    /**
     *
     * @param val the constant to hold
     */
    public Constant(int val) {
      this.val = val;
    }

    /**
     *
     * @param symTab symbol table, if needed, to fetch variable values
     * @return The constant value passed in
     */
    @Override
    public int evaluate(Map<String, Integer> symTab) {
      return val;
    }

    @Override
    public void infixDisplay() {

      System.out.print(val);
    }

    /**
     * Creates Instructions for the dendron machine
     *
     * @return Instructions for the dendron machine to run
     */
    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>(0);
      i.add(new Machine.PushConst(val));
      return i;
    }
  }

  /**
   * Loads a given key from a table
   */
  public static class Load implements ExpressionNode {
    private String name;

    /**
     *
     * @param name the key to load from the table
     */
    public Load(String name) {
      this.name = name;
    }

    /**
     *
     * @param symTab symbol table, if needed, to fetch variable values
     * @return the value of the passed in key from the symbol table
     */
    @Override
    public int evaluate(Map<String, Integer> symTab) {
      try {
        return symTab.get(name);
      }
      catch (Exception e) {
        Errors.report(Errors.Type.UNINITIALIZED, name);
        return 0;
      }
    }

    /**
     * Will print out the passed in key value
     * ex
     * "x"
     */
    @Override
    public void infixDisplay() {
      System.out.print(name);
    }

    /**
     * Creates Instructions for the dendron machine
     *
     * @return Instructions for the dendron machine to run
     */
    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>(0);
      i.add(new Machine.Load(name ));
      return i;
    }
  }

  /**
   * Creates a binary operator for addition, subtraction, multiplication, and division
   */
  public static class BinaryOperation implements ExpressionNode {

    String o;
    ExpressionNode c1;
    ExpressionNode c2;

    /**
     *
     * @param o the operator passed in aloud operators are "+ - * /"
     * @param c1 first part of an expression (numerator for division) (first for subtraction)
     * @param c2 second part of an expression (denominator for division) (second for subtraction)
     */
    public BinaryOperation(String o, ExpressionNode c1, ExpressionNode c2) {
      this.o = o;
      this.c1 = c1;
      this.c2 = c2;
    }

    /**
     *
     * @param symTab symbol table, if needed, to fetch variable values
     * @return result of a given operation and the 2 passed in expressions
     */
    @Override
    public int evaluate(Map<String, Integer> symTab) {
      switch (o) {
        case "+":
          return c1.evaluate(symTab) + c2.evaluate(symTab);
        case "-":
          return c1.evaluate(symTab) - c2.evaluate(symTab);
        case "*":
          return c1.evaluate(symTab) * c2.evaluate(symTab);
        case "/":
          int n = c1.evaluate(symTab);
          int d = c2.evaluate(symTab);
          if(d == 0) Errors.report(Errors.Type.DIVIDE_BY_ZERO, n + " / " + d);
          return  n / d;
        default:
          Errors.report(Errors.Type.ILLEGAL_VALUE, o);
          return 0;
      }
    }

    /**
     * Will print out the an expression based on the operator with
     * the infix display of the passed in expressions on either side of the operator
     * ex
     * "( c1.infix * c2.infix )"
     */
    @Override
    public void infixDisplay() {
      System.out.print("( ");
      c1.infixDisplay();
      System.out.print(" " + o + " ");
      c2.infixDisplay();
      System.out.print(" )");

    }

    /**
     * Creates Instructions for the dendron machine
     *
     * @return Instructions for the dendron machine to run
     */
    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>();
      i.addAll(c1.emit());
      i.addAll(c2.emit());
      switch (o) {
        case "+":
          i.add(new Machine.Add());
          break;
        case "-":
          i.add(new Machine.Subtract());
          break;
        case "*":
          i.add(new Machine.Multiply());
          break;
        case "/":
          i.add(new Machine.Divide());
          break;
        default:
          Errors.report(Errors.Type.ILLEGAL_VALUE, o);
      }

      return i;
    }

  }

  /**
   * Creates a unary operator for negation, and taking square roots
   */
  public static class UnaryOperation implements ExpressionNode {

    String o;
    ExpressionNode c;

    /**
     *
     * @param o the operator passed in aloud operators are "_ #"
     * @param c the expression that is being operated on
     */
    public UnaryOperation(String o, ExpressionNode c) {
      this.o = o;
      this.c = c;
    }

    /**
     *
     * @param symTab symbol table, if needed, to fetch variable values
     * @return result of a given operation and the passed in expression
     */
    @Override
    public int evaluate(Map<String, Integer> symTab) {
      switch (o) {
        case "_":
          return -c.evaluate(symTab);
        case "#":
          return (int) Math.sqrt(c.evaluate(symTab));
        default:
          Errors.report(Errors.Type.ILLEGAL_VALUE, o);
          return 0;
      }
    }

    /**
     * Will print out the an expression based on the operator with
     * the infix display of the passed in expression will be displayed after
     * ex
     * "_c1.infix"
     */
    @Override
    public void infixDisplay() {
      System.out.print(o);
      c.infixDisplay();
      System.out.print(" ");
    }

    /**
     * Creates Instructions for the dendron machine
     *
     * @return Instructions for the dendron machine to run
     */
    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>();
      i.addAll(c.emit());
      switch (o) {
        case "_":
          i.add(new Machine.Negate());
          break;
        case "#":
          i.add(new Machine.SquareRoot());
          break;
        default:
          Errors.report(Errors.Type.ILLEGAL_VALUE, o);
      }
      return i;
    }

  }
}
