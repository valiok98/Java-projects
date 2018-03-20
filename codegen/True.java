package codegen;

public class True extends Condition {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
  public String toString() {
	  return "true";
  }

}
