package codegen1;

public class False extends Condition {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
