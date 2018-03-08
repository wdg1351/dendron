package dendron.tree;

import dendron.Errors;
import dendron.machine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Nodes {

  public static class Assignment implements ActionNode {
    String name;
    ExpressionNode val;

    public Assignment(String name, ExpressionNode val) {
      this.name = name;
      this.val = val;
    }

    @Override
    public void execute(Map<String, Integer> symTab) {
      symTab.put(name, val.evaluate(symTab));
    }

    @Override
    public void infixDisplay() {
      System.out.print(name + " := ");
      val.infixDisplay();
    }

    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>(val.emit());
      i.add(new Machine.Store(name));
      return i;
    }
  }

  public static class Print implements ActionNode {
    ExpressionNode val;

    public Print(ExpressionNode val) {
      this.val = val;
    }

    @Override
    public void execute(Map<String, Integer> symTab) {
      System.out.println("=== " + val.evaluate(symTab));
    }

    @Override
    public void infixDisplay() {
      System.out.print("Print ");
      val.infixDisplay();
    }

    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>(val.emit());
      i.add(new Machine.Print());
      return i;
    }
  }

  public static class Constant implements ExpressionNode {
    private int val;

    public Constant(int val) {
      this.val = val;
    }

    @Override
    public int evaluate(Map<String, Integer> symTab) {
      return val;
    }

    @Override
    public void infixDisplay() {

      System.out.print(val);
    }

    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>(0);
      i.add(new Machine.PushConst(val));
      return i;
    }
  }

  public static class Load implements ExpressionNode {
    private String name;

    public Load(String name) {
      this.name = name;
    }

    @Override
    public int evaluate(Map<String, Integer> symTab) {
      return symTab.get(name);
    }

    @Override
    public void infixDisplay() {
      System.out.print(name);
    }

    @Override
    public List<Machine.Instruction> emit() {
      ArrayList<Machine.Instruction> i = new ArrayList<>(0);
      i.add(new Machine.Load(name ));
      return i;
    }
  }

  public static class BinaryOperation implements ExpressionNode {

    String o;
    ExpressionNode c1;
    ExpressionNode c2;

    public BinaryOperation(String o, ExpressionNode c1, ExpressionNode c2) {
      this.o = o;
      this.c1 = c1;
      this.c2 = c2;
    }

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
          return c1.evaluate(symTab) / c2.evaluate(symTab);
        default:
          Errors.report(Errors.Type.ILLEGAL_VALUE, o);
          return 0;
      }
    }

    @Override
    public void infixDisplay() {
      System.out.print("( ");
      c1.infixDisplay();
      System.out.print(" " + o + " ");
      c2.infixDisplay();
      System.out.print(" )");

    }

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

  public static class UnaryOperation implements ExpressionNode {

    String o;
    ExpressionNode c;

    public UnaryOperation(String o, ExpressionNode c) {
      this.o = o;
      this.c = c;
    }

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

    @Override
    public void infixDisplay() {
      System.out.print(o);
      c.infixDisplay();
      System.out.print(" ");
    }

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
