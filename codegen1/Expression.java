package codegen1;

public abstract class Expression {
  public abstract void accept(Visitor visitor);
}
