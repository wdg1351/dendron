package dendron.tree;

import dendron.Errors;
import dendron.machine.Machine;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Operations that are done on a Dendron code parse tree.
 * <p>
 * THIS CLASS IS UNIMPLEMENTED. All methods are stubbed out.
 *
 * @author YOUR NAME HERE
 */
public class ParseTree {

  private Map<String, Integer> sysTab;
  private List<String> cAction;
  ArrayList<ActionNode> allActions;

  /**
   * Parse the entire list of program tokens. The program is a
   * sequence of actions (statements), each of which modifies something
   * in the program's set of variables. The resulting parse tree is
   * stored internally.
   *
   * @param program the token list (Strings)
   */
  public ParseTree(List<String> program) {
    cAction = new ArrayList<>(0);
    allActions = new ArrayList<>(0);
    sysTab = new HashMap<>();

    ArrayList<String> currentAction = new ArrayList<>(0);

    for(int i = 0; i < program.size(); i++) {
      if(program.get(i).equals(":=") || program.get(i).equals("@")) {
        if(currentAction.isEmpty()) {
          currentAction.add(program.get(i));
        }
        else {
          allActions.add(parseAction(currentAction));
          currentAction.clear();
          currentAction.add(program.get(i));
        }
      }
      else {
        currentAction.add(program.get(i));
      }
    }
    allActions.add(parseAction(currentAction));

  }

  /**
   * Parse the next action (statement) in the list.
   * (This method is not required, just suggested.)
   *
   * @param program the list of tokens
   * @return a parse tree for the action
   */
  private ActionNode parseAction(List<String> program) {
    switch (program.get(0)){
      case ":=":
          this.cAction = program.subList(2, program.size());
          return new Nodes.Assignment(program.get(1), parseExpr());
      case "@":
          this.cAction = program.subList(1, program.size());
        return new Nodes.Print(parseExpr());
      default:
        Errors.report(Errors.Type.ILLEGAL_VALUE, program.get(0));
        return null;
    }

  }

  public static boolean isInt(String s) {
    char[] chars = s.toCharArray();

    if(!Character.isDigit(chars[0]))
      if(!Character.isLetter( '-'))
        return false;

    for(int i = 1; i < chars.length; i++) {
      if(!Character.isDigit(chars[i]))
        return false;
    }
    return true;
  }


  /**
   * Parse the next expression in the list.
   * (This method is not required, just suggested.)
   *
   * @param program the list of tokens
   * @return a parse tree for this expression
   */
  private ExpressionNode parseExpr() {
    ActionNode pAction;
    switch (this.cAction.get(0)){
      case "+":
      case "-":
      case "*":
      case "/":
        return new Nodes.BinaryOperation(this.cAction.remove(0), parseExpr(), parseExpr() );
      case "_":
      case "#":
        return new Nodes.UnaryOperation(this.cAction.remove(0), parseExpr());
    }

    if(isInt(this.cAction.get(0))) {
      return new Nodes.Constant(Integer.parseInt(this.cAction.remove(0)));
    }
    return new Nodes.Load(this.cAction.remove(0));

  }

  /**
   * Print the program the tree represents in a more typical
   * infix style, and with one statement per line.
   *
   * @see dendron.tree.ActionNode#infixDisplay()
   */
  public void displayProgram() {
    System.out.println("The program, with expressions in infix notation: \n");
    for(ActionNode a : allActions) {
      a.infixDisplay();
      System.out.println("");
    }
    System.out.println();
  }

  /**
   * Run the program represented by the tree directly
   *
   * @see dendron.tree.ActionNode#execute(Map)
   */
  public void interpret() {
    System.out.println("Interpreting the parse tree...");
    for(ActionNode a : allActions) {
      a.execute(this.sysTab);
    }
    System.out.println("Interpretation complete.");
    System.out.println();
    Errors.dump(this.sysTab);
  }

  /**
   * Build the list of machine instructions for
   * the program represented by the tree.
   *
   * @return the Machine.Instruction list
   * @see Machine.Instruction#execute()
   */
  public List<Machine.Instruction> compile() {
    ArrayList<Machine.Instruction> i = new ArrayList<>();
    for(ActionNode a : allActions) {
      i.addAll(a.emit());
    }
    return i;
  }

}
