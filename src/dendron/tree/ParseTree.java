package dendron.tree;

import dendron.machine.Machine;


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

  Nodes.aNode mainNode;
  static Map<String, Integer> sysTab = new HashMap<>();

  /**
   * Parse the entire list of program tokens. The program is a
   * sequence of actions (statements), each of which modifies something
   * in the program's set of variables. The resulting parse tree is
   * stored internally.
   *
   * @param program the token list (Strings)
   */
  public ParseTree(List<String> program) {


  }

  /**
   * Parse the next action (statement) in the list.
   * (This method is not required, just suggested.)
   *
   * @param program the list of tokens
   * @return a parse tree for the action
   */
  private ActionNode parseAction(List<String> program) {
    for(int i = program.size() - 1; i > 0; i--) {
      String val = program.get(i);
      if(isInt(val)) {

      }

    }


    return null;
  }

  public static boolean isInt(String s) {
    char[] chars = s.toCharArray();

    for(char c : chars) {
      if(!Character.isDigit(c))
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
  private ExpressionNode parseExpr(List<String> program) {
    return null;
  }

  /**
   * Print the program the tree represents in a more typical
   * infix style, and with one statement per line.
   *
   * @see dendron.tree.ActionNode#infixDisplay()
   */
  public void displayProgram() {
  }

  /**
   * Run the program represented by the tree directly
   *
   * @see dendron.tree.ActionNode#execute(Map)
   */
  public void interpret() {
    mainNode.execute(sysTab);
  }

  /**
   * Build the list of machine instructions for
   * the program represented by the tree.
   *
   * @return the Machine.Instruction list
   * @see Machine.Instruction#execute()
   */
  public List<Machine.Instruction> compile() {
    return null;
  }

}
