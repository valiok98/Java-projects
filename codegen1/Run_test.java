package codegen1;

public class Run_test {
	public static void main(String args[]) {


		Test2 a= new Test2();
		 Test1 b = new Test1();
		 Test3 c = new Test3();
		
		 a.m((Test2) b); // Statement 1
			a.m((Test1) c); // Statement 2
			 a.m(c); // Statement 3
			 b.m(a); // Statement 4
			 b.m((Test2) b); // Statement 5
			 b.m((Test2) c); // Statement 6
			// b.m((Test1) a); // Statement 7
			 b.m(c); // Statement 8
		
	
	}
	}
