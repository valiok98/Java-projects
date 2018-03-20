package codegen;

import java.awt.List;
import java.util.ArrayList;
import java.util.Scanner;


public class ParsyMcParseface {
	
	private int from;
	private  static String[] program;
	private boolean legal_binary_expression = true;
	private boolean legal_binary_condition = true;
	
	
	
	public ParsyMcParseface(String code) {
		from = 0;
		program = lex(code);
	}
	
	public static String readProgramConsole() {
		@SuppressWarnings("resource")
		Scanner sin = new Scanner(System.in);
		StringBuilder builder = new StringBuilder();
		while (true) {
			String nextLine = sin.nextLine();
			if (nextLine.equals("")) {
				nextLine = sin.nextLine();
				if (nextLine.equals(""))
					break;
			}
			if (nextLine.startsWith("//"))
				continue;
			builder.append(nextLine);
			builder.append('\n');
		}
		return builder.toString();
	}

	private  static final int PADDING = 3;

	public static String[] lex(String input) {
		ArrayList<String> tokens = new ArrayList<>();
		int index = 0;
		while (index < input.length()) {
			char next = input.charAt(index);
			if (next == ' ' || next == '\n' || next == '\r' || next == '\t') {
				index++;
				continue;
			}
			switch (next) {
			case '{':
			case '}':
			case '(':
			case ')':
			case '+':
			case '-':
			case '/':
			case '*':
			case '%':
			case ';':
				index++;
				tokens.add("" + next);
				continue;

			}
			StringBuilder tokenBuilder = new StringBuilder();
			compBinAssLoop: while (index < input.length()) {
				char nextOp = input.charAt(index);
				switch (nextOp) {
				case '=':
				case '!':
				case '<':
				case '>':
				case '&':
				case '|':
					tokenBuilder.append(nextOp);
					break;
				default:
					break compBinAssLoop;
				}
				index++;
			}
			if (tokenBuilder.length() == 0) {
				while (index < input.length()) {
					char nextLetterNumber = input.charAt(index);
					if (nextLetterNumber >= 'a' && nextLetterNumber <= 'z'
							|| nextLetterNumber >= 'A' && nextLetterNumber <= 'Z'
							|| nextLetterNumber >= '0' && nextLetterNumber <= '9')
						tokenBuilder.append(nextLetterNumber);
					else
						break;
					index++;
				}
			}
			if (tokenBuilder.length() > 0)
				tokens.add(tokenBuilder.toString());
			else {
				index++;
				tokens.add("" + next);
			}
		}
		// Padding
		for (int i = 0; i < PADDING; i++)
			tokens.add("");
		program = tokens.toArray(new String[0]);
		return program;
	}

	private Number parseNumber() {
		for (int i = 0; i < program[from].length(); i++) {
			char next = program[from].charAt(i);
			if (!(next >= '0' && next <= '9')) {
				return null;
			}
		}
		from++;
		return new Number(Integer.parseInt(program[from-1]));
	}

	public static boolean isKeyword(String token) {
		switch (token) {
		case "true":
		case "false":
		case "int":
		case "while":
		case "if":
		case "else":
		case "read":
		case "write":
			return true;
		default:
			return false;
		}
	}

	private String parseName() {
		if (isKeyword(program[from]))
			return null;
		if (program[from].length() == 0)
			return null;
		char first = program[from].charAt(0);
		if (!(first >= 'a' && first <= 'z') && !(first >= 'A' && first <= 'Z'))
			return null;
		for (int i = 1; i < program[from].length(); i++) {
			char next = program[from].charAt(i);
			if (!(next >= 'a' && next <= 'z') && !(next >= 'A' && next <= 'Z') && !(next >= '0' && next <= '9')) {
				return null;
			}
		}
		from++;
		return new String(program[from-1]);
	}

	private Type parseType() {
		if (program[from].equals("int")) {
			from++;
			return new Type("int");
		}
		return null;
	}

	private Declaration parseDecl() {
		ArrayList<String> arguments = new ArrayList<String>();
		Type type = parseType();
		String name = parseName();
		
		if(type != null && name != null) {
			arguments.add(name);
			if (program[from].equals(";")) {
				from += 1;
				return new Declaration(name);
			}else {
				while(program[from].equals(",")){
					from++;
					String name1 = parseName();
					if(name1 != null) {
						arguments.add(name1);
						if (program[from].equals(";")) {
							from += 1;
							return new Declaration(arguments.stream().toArray(String[]::new));
						}
					}else break;
				}
			}
			return null;
		}
		return null;
	}

	private Unop parseUnop() {
		if (program[from].equals("-")) {
			from++;
			return Unop.Minus;
		}
		return null;
	}

	private Binop parseBinop() {
		switch (program[from]) {
		case "+":
			from++;
			return Binop.Plus;
		case "-":
			from++;
			return Binop.Minus;
		case "*":
			from++;
			return Binop.MultiplicationOperator;
		case "/":
			from++;
			return Binop.DivisionOperator;
		case "%":
			from++;
			return Binop.Modulo;
		default:
			return null;
		}
	}

	private Comp parseComp() {
		switch (program[from]) {
		case "==":
			from++;
			return Comp.Equals;
		case "!=":
			from++;
			return Comp.NotEquals;
		case "<=":
			from++;
			return Comp.LessEqual;
		case "<":
			from++;
			return Comp.Less;
		case ">=":
			from++;
			return Comp.GreaterEqual;
		case ">":
			from++;
			return Comp.Greater;
		default:
			return null;
		}
	}

	private Expression parseExpressionNoBinop() {
		// <number>
		Number num = parseNumber();
		if(num != null)
			return num;
		String name = parseName();
		if(name != null) {
			int before = from;
			if(program[from].equals("(")) {
				from++;
				Expression[] test_call = parseCall();
				if(test_call == null)
					from = before;
				else
					return new Call(name,test_call);
			}
			
			return new Variable(name);
		}
			else
			from--;
		
		
		if (program[from].equals("(")) {
			from += 1;
			Expression tmp = parseExpression();
			if (tmp == null)
				return null;
			if (program[from].equals(")")) {
				from++;
				return tmp;
			}
			else
				return null;
		}
		Unop tmp = parseUnop();
		if (tmp != null) {
			Expression to_parse = parseExpression();
			if(to_parse == null)
				return null;
			return new Unary(tmp,to_parse);
		}
		else
			return null;
	}

	private Expression parseExpression() {
		Expression tmp = parseExpressionNoBinop();
		if (tmp == null)
			return null;
		Binop binop_tmp = parseBinop();
		if(binop_tmp == null)
			return tmp;
		Expression noBinop = parseExpressionNoBinop();
		Expression to_return  = generateBinary(tmp,binop_tmp,noBinop);
		if(to_return != null && legal_binary_expression) {
			from++;
		return to_return ;
	}
		return null;
	}
	
	private Expression generateBinary(Expression tmp, Binop binop_tmp, Expression noBinop) {
		if(binop_tmp != null && noBinop == null) {
			legal_binary_expression = false;
			return null;
		}
		if(binop_tmp == null && noBinop == null)
			return tmp;
		
		return generateBinary(new Binary(tmp,binop_tmp,noBinop),parseBinop(),parseExpression());
	}
	
	private Expression[] parseCall() {
		
		ArrayList<Expression> args = new ArrayList<Expression>();
		Expression tmp_expr = parseExpression();
		if(tmp_expr != null)
			args.add(tmp_expr);
		while(!program[from].equals(")")) {
			if(program[from].equals(","))
				from++;
			tmp_expr = parseExpression();
			if(tmp_expr != null)
				args.add(tmp_expr);
			else
				throw new RuntimeException("Fehler bei Argumenten");
		}
		from++;
		return args.stream().toArray(Expression[]::new);
	}

	
	private Bbinop parseBbinop() {
		switch (program[from]) {
		case "&&":
			from++;
			return Bbinop.And;
		case "||":
			from++;
			return Bbinop.Or;
		default:
			return null;
		}
	}
	
	
	private Bunop parseBunop() {
		if (program[from].equals("!")) {
			from++;
			return Bunop.Not;
		}
		return null;
	}

	private UnaryCondition parseConditionNoBinary() {
		
		
		Bunop bunop = parseBunop();
		if (bunop != null) {
			if (program[from].equals("(")) {
				from += 1;
				Condition tmp = parseCondition();
				if (tmp == null)
					return null;
				if (program[from].equals(")")) {
					from++;
					return new UnaryCondition(bunop,tmp);
				}
				else
					return null;
			}
		}
		return null;
		
	}
	
	private Condition generateBinaryCondition(Condition tmp, Bbinop bbinop_tmp, Condition rhs) {
		
		if(bbinop_tmp != null && rhs == null) {
			legal_binary_condition = false;
			return null;
		}
		if(bbinop_tmp == null && rhs != null){
			legal_binary_condition = false;
			return null;
		}
		if(bbinop_tmp == null && rhs == null)
			return tmp;
		
		return generateBinaryCondition(new BinaryCondition(tmp,bbinop_tmp,rhs),parseBbinop(),parseCondition());
		
		
	}
	
	private Condition parseBinaryCondition() {
		
		Condition first = parseSpecialBinary();
		if (first == null)
			return null;

		Condition result = generateBinaryCondition(first,parseBbinop(),parseCondition());
		if(result != null && legal_binary_condition == true)
			return result;
		return null;
	}
	
	private Comparison parseComparison() {
		
		// <expr> <comp> <expr>
				Expression expr = parseExpression();
				if (expr != null) {
					Comp tmp_comp = parseComp();
					if (tmp_comp == null)
						return null;
					Expression rhs = parseExpression();
					if(rhs == null)
						return null;
					return new Comparison(expr,tmp_comp,rhs);
				}
				return null;
		
	}
	
	private Condition parseSpecialBinary() {
		int from_before = from;
		UnaryCondition unary_tmp = parseConditionNoBinary();
		if(unary_tmp == null)
			from = from_before;
		else
			return unary_tmp;
		Comparison comp_tmp = parseComparison();
		if(comp_tmp == null)
			from = from_before;
		else
			return comp_tmp;
		return null;
	}
	
	private Condition parseCondition() {
		if (program[from].equals("true")) {
			from++;
			return new True();
		}
		//false
		if (program[from].equals("false")) {
			from++;
			return new False();
		}
		// (<cond>)
				if (program[from].equals("(")) {
					from += 1;
					Condition tmp = parseCondition();
					if (tmp == null)
						return null;
					if (program[from].equals(")")) {
						from++;
						return tmp;
					}
					else
						return null;
				}
			int from_before = from;
			Condition binary_tmp = parseBinaryCondition();
			if(binary_tmp == null)
				from = from_before;
			else
				return binary_tmp;
			UnaryCondition unary_tmp = parseConditionNoBinary();
			if(unary_tmp == null)
				from = from_before;
			else
				return unary_tmp;
			Comparison comp_tmp = parseComparison();
			if(comp_tmp == null)
				from = from_before;
			else
				return comp_tmp;
			
			
			return null;
			
	}

	private Assignment parseAssigment() {
		String Name = parseName();
		if(Name == null)
			return null;
		Variable name = new Variable(Name);
		if (!program[from].equals("="))
			return null;
		from += 1;
		if (from + 2 < program.length && (program[from] + program[from + 1] + program[from + 2]).equals("read()")) {
			Read to_parse = new Read(name.getName());
			return new Assignment(name.getName(),new Variable(to_parse.getName()));
		}
			return new Assignment(name.getName(),parseExpression());
	}

	private IfThen parseIte() {
		if (!program[from].equals("if"))
			return null;
		from++;
		Condition tmp_cond = parseCondition();
		if (tmp_cond == null)
			return null;
		Statement tmp_state = parseStatement();
		if (tmp_state != null)
			return new IfThen(tmp_cond,tmp_state);
		return null;
	}
	
	private IfThenElse parseIteEls() {
		
		if (!program[from].equals("if"))
			return null;
		from++;
		Condition tmp_cond = parseCondition();
		if (tmp_cond == null)
			return null;
		Statement tmp_state = parseStatement();
		if (tmp_state == null)
			return null;
		if (from < program.length && program[from].equals("else")) {
			from++;
			Statement else_state = parseStatement();
			return new IfThenElse(tmp_cond,tmp_state,else_state);
		}
		return null;
		
	}

	private Statement parseStatement() {
		switch (program[from]) {
		case ";":
			from++;
			return null;
		case "{":
			from++;
			ArrayList<Statement> result = new ArrayList<Statement>();
			int before = from;
			Statement tmp_state = parseStatement();
			while (tmp_state != null) {
				result.add(tmp_state);
				before = from;
				tmp_state = parseStatement();
				if(tmp_state == null) {
					from = before;
					if(program[from].equals(";")) {
						from++;
						tmp_state = parseStatement();
					}else
						break;
				}
			}
			from = before;
			if(program[from].equals(";"))
				from++;
			if (program[from].equals("}")) {
				from++;
				return new Composite(result.stream().toArray(Statement[]::new));
			}else
				return null;
		case "while":
			from++;
			if (program[from].equals("("))
				from++;
			else
				return null;
			Condition cond_tmp = parseCondition();
			if (cond_tmp == null)
				return null;
			if (!program[from].equals(")"))
				return null;
			from++;
			
			return new While(cond_tmp,parseStatement());
		case "if":
			int before_test = from;
			IfThenElse tmp_else = parseIteEls();
			if(tmp_else == null)
				from = before_test;
			else
				return tmp_else;
			IfThen tmp_if = parseIte();
			if(tmp_if != null)
				return tmp_if;
			return null;
		
		case "write":
			if (!program[from + 1].equals("("))
				return null;
			from+=2;
			Expression tmp_expr = parseExpression();
			if (from < 0 || !program[from].equals(")"))
				return null;
			if (!program[from + 1].equals(";"))
				return null;
			return new Write(tmp_expr);
			
		case "return":
			from++;
			Expression expr = parseExpression();
			
			if(expr == null)
				return null;
			return new Return(expr);
			
		default: {
			Assignment ass = parseAssigment();
			if (ass != null) {
				if (program[from].equals(";")) {
					return ass;
				}
				else
					return null;
			}
			return null;
		}
		}
	}
	
	private Function parseFunction() {
		
		Type type = parseType();
		if(type == null)
			return null;
		String name = parseName();
		if(name == null)
			return null;
		if(!program[from].equals("(")) 
			return null;
		from++;
		boolean error = false;
		boolean first_iter = false;
		ArrayList<String> params = new ArrayList<String>();
		
		while(true) {
			if(program[from].equals(")")) {
				from++;
				break;
			}
			if(first_iter && !program[from].equals(",")) {
				
				error = true;
				break;
			}else if(first_iter) {
				from++;
			}
			if(parseType() != null) {
				
				String name_par = parseName();
				if(name_par != null) {
				
					first_iter = true;
					params.add(name_par);
				}else
					return null;
			}else
				return null;
		}
		
		if(error)
			return null;
		if(!program[from].equals("{"))
			return null;
		from++;
		ArrayList<Declaration> decl = new ArrayList<Declaration>();
		int before = from;
		Declaration decl_tmp = parseDecl();
		while(decl_tmp != null) {
			decl.add(decl_tmp);
			before = from;
			decl_tmp = parseDecl();
		}
		from = before;
		ArrayList<Statement> state = new ArrayList<Statement>();
		before = from;
		Statement state_tmp = parseStatement();
		while(state_tmp != null) {
			state.add(state_tmp);
			before = from;
			state_tmp = parseStatement();
			if(state_tmp == null) {
				from = before;
				if(program[from].equals(";")) {
					from++;
					state_tmp = parseStatement();
				}else
					break;
			}
		}
		from = before;
		if(program[from].equals(";"))
			from++;
		if(!program[from].equals("}"))
			return null;
		from++;
		return new Function(name,params.stream().toArray(String[]::new),decl.stream().toArray(Declaration[]::new),state.stream().toArray(Statement[]::new));
	}
	
	private Program parse() {
		ArrayList<Function> funcs = new ArrayList<Function>();
		Function func = parseFunction();
		if(func == null)
			return null;
		while(func != null) {
			funcs.add(func);
			func = parseFunction();
		}
		return new Program(funcs.stream().toArray(Function[]::new));
	}
	
	public Program liefert() {
		return parse();
	}
	

	

	
	
}
