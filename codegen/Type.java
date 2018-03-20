package codegen;

public class Type {
	
	private String type;
	
	public Type(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
