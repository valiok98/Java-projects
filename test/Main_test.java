package test;

public class Main_test {
	
	public static void main(String args[]) {
		
		TestPoly A = new TestPoly();
		TestPolymorphism B = new TestPolymorphism();
		TestPoly C = new TestPolymorphism();
		System.out.println(A.getX());
		System.out.println(B.getX());
		C.Interesting1(3);
		//C.Interesting1(3.5);
	}
}

