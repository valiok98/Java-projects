package codegen;

public class Number extends Expression {
  private int value;
  
  public int getValue() {
    return value;
  }
  
  public Number(int value) {
    super();
    this.value = value;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
  
  @Override
  public String toString() {
	  return ""+value;
  }
  
}
