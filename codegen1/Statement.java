package codegen1;

public abstract class Statement {
  public abstract void accept(Visitor visitor);
}
