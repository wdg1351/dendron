package dendron.tree;

import dendron.machine.Machine;

import java.util.List;
import java.util.Map;

public class Nodes {
  public class aNode implements ActionNode {

    DendronNode c1;
    DendronNode c2;
    String operator;

    @Override
    public void execute(Map<String, Integer> symTab) {
      switch (operator) {
        case ":=":
          symTab.put("x");
          break;
      }
    }

    @Override
    public void infixDisplay() {

    }

    @Override
    public List<Machine.Instruction> emit() {
      return null;
    }
  }

  public class eNode implements ExpressionNode {

    @Override
    public int evaluate(Map<String, Integer> symTab) {
      return 0;
    }

    @Override
    public void infixDisplay() {

    }

    @Override
    public List<Machine.Instruction> emit() {
      return null;
    }
  }
}
