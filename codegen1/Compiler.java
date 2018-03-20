package codegen1;

import static org.junit.Assert.assertEquals;

import interpreter.Interpreter;


public class Compiler {

	
	public static int[] compile(String code) {
		
		ParsyMcParseface parser = new ParsyMcParseface(code);
		Program program = parser.liefert();
		if(program == null)
			throw new RuntimeException("Exception ist afgetretten");
		CodeGenerationVisitor cgv = new CodeGenerationVisitor();
		program.accept(cgv);
	    return cgv.getProgram();
		//System.out.println(Interpreter.programToString(cgv.getProgram()));
	    //System.out.println("-----");
	
	}
	

	
}
