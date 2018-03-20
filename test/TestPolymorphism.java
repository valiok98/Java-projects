package test;

public class TestPolymorphism extends TestPoly{
	
	private int x= 10;
	public int getX() {
		//super.getX();
		return x;
	}
	public void PrintMessage() {
		System.out.println("This should cause an error !");
	}
	public void Interesting1(int inter) {
		System.out.println("thats interesting " + inter);
	}
	public void Interesting1(double inter) {
		System.out.println("thats interesting , a double" + inter);
	}
}
