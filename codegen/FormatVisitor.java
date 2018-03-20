package codegen;

import java.util.ArrayList;
import java.util.HashMap;

public class FormatVisitor extends Visitor {
	private int current_priority = 0;
	private int tab = 0;
	private String result = "";

	public String getResult() {
		return result;
	}

	/*
	 * Expression
	 */

	@Override
	public void visit(Number number) {
		current_priority = 0;
		result += number.toString();

	}

	@Override
	public void visit(Variable variable) {
		current_priority = 0;
		result += variable.toString();
	}

	@Override
	public void visit(Type type) {

	}

	@Override
	public void visit(Unary unary) {
		switch (unary.getOperator()) {
		case Minus:
			boolean brackets = false;
			result += "-";
			if (unary.getOperand() instanceof Binary) {
				Binary tmp = (Binary) unary.getOperand();
				if (tmp.getOperator() == Binop.Plus || tmp.getOperator() == Binop.Minus) {
					brackets = true;
				}
			}
			if (brackets)
				result += "(";
			unary.getOperand().accept(this);
			if (brackets)
				result += ")";
			current_priority = 3;
			break;
		}
	}

	@Override
	public void visit(Binary binary) {
		boolean brackets = false;
		String prev;
		switch (binary.getOperator()) {
		case Minus:

			brackets = false;
			if (!(binary.getLhs() instanceof Number) && !(binary.getLhs() instanceof Variable))
				brackets = true;
			if (binary.getLhs() instanceof Binary) {
				Binary tmp = (Binary) binary.getLhs();
				if (tmp.getOperator() == Binop.Plus)
					brackets = false;
			}
			if (brackets)
				result += "(";
			binary.getLhs().accept(this);
			if (brackets)
				result += ")";
			brackets = false;
			result += " - ";
			if (!(binary.getRhs() instanceof Number) && !(binary.getRhs() instanceof Variable))
				brackets = true;
			if (brackets)
				result += "(";
			binary.getRhs().accept(this);
			if (brackets)
				result += ")";
			current_priority = 5;
			break;
		case Plus:

			binary.getLhs().accept(this);
			result += " + ";
			binary.getRhs().accept(this);
			current_priority = 5;
			break;
		case MultiplicationOperator:

			brackets = false;
			prev = result;
			binary.getLhs().accept(this);
			if (current_priority > 4) {
				result = prev;
				result += "(";
				binary.getLhs().accept(this);
				result += ")";
			}
			result += " * ";
			prev = result;
			binary.getRhs().accept(this);
			result = prev;
			if (current_priority >= 4) {
				brackets = true;
			}
			if (binary.getRhs() instanceof Binary) {
				Binary tmp = (Binary) binary.getRhs();
				if (tmp.getOperator() == Binop.MultiplicationOperator)
					brackets = false;
			}
			if (brackets)
				result += "(";
			binary.getRhs().accept(this);
			if (brackets)
				result += ")";
			current_priority = 4;
			break;
		case DivisionOperator:
			brackets = false;
			prev = result;
			binary.getLhs().accept(this);
			if (current_priority > 4) {
				result = prev;
				result += "(";
				binary.getLhs().accept(this);
				result += ")";
			}
			result += " / ";
			prev = result;
			binary.getRhs().accept(this);
			if (current_priority >= 4)
				brackets = true;
			result = prev;
			if (brackets)
				result += "(";
			binary.getRhs().accept(this);
			if (brackets)
				result += ")";
			current_priority = 4;
			break;
		case Modulo:
			brackets = false;
			binary.getLhs().accept(this);
			result += " % ";
			if (!(binary.getRhs() instanceof Number) && !(binary.getRhs() instanceof Variable))
				brackets = true;
			if (brackets)
				result += "(";
			binary.getRhs().accept(this);
			if (brackets)
				result += ")";
			current_priority = 4;
			break;
		}
	}

	@Override
	public void visit(Call call) {
		//for(int i=0;i<2*tab;i++)result+=" ";
		result += call.getFunctionName();
		result += "(";
		String prev = result;
		for (Expression e : call.getArguments()) {
			e.accept(this);
			prev = result;
			result += ", ";
		}
		result = prev;
		result += ")";
		current_priority = 0;
	}

	/*
	 * Statement
	 */

	@Override
	public void visit(Read read) {
		for(int i=0;i<2*tab;i++)result+=" ";
		result += read.toString();
	}

	@Override
	public void visit(Write write) {
		for(int i=0;i<2*tab;i++)result+=" ";
		result += "write( ";
		write.getExpression().accept(this);
		result += " );";
	}

	@Override
	public void visit(Assignment assignment) {
		for(int i=0;i<2*tab;i++)
			result+=" ";
		result += assignment.getName();
		result += " = ";
		int tmp_tab =tab;
		tab = 0;
		assignment.getExpression().accept(this);;
		tab=tmp_tab;
		result+=";\n";
		current_priority = 4;
	}

	@Override
	public void visit(Composite composite) {
		for (Statement s : composite.getStatements())
			s.accept(this);
	}

	@Override
	public void visit(IfThenElse ifThenElse) {
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="if (";
		ifThenElse.getCond().accept(this);
		result+=")\n";
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="{\n";
		tab++;
		ifThenElse.getThenBranch().accept(this);
		tab--;
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="}\n";
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="else\n";
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="{\n";
		tab++;
		ifThenElse.getElseBranch().accept(this);
		tab--;
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="}\n";
		
	}

	@Override
	public void visit(IfThen ifThen) {
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="if (";
		ifThen.getCond().accept(this);
		result+=")\n";
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="{\n";
		tab++;
		ifThen.getThenBranch().accept(this);
		tab--;
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="}\n";
	}

	@Override
	public void visit(While while_) {
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="while (";
		while_.getCond().accept(this);
		result+=")\n";
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="{\n";
		tab++;
		while_.getBody().accept(this);
		tab--;
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="}\n";
	}

	@Override
	public void visit(Return return_) {
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="return ";
		return_.getExpression().accept(this);
		result+=";\n";
	}

	@Override
	public void visit(EmptyStatement emptyStatement) {
	}

	/*
	 * Condition
	 */

	@Override
	public void visit(True true_) {
		result += true_.toString();
		current_priority = 0;
	}

	@Override
	public void visit(False false_) {
		result += false_.toString();
		current_priority = 0;
	}

	@Override
	public void visit(Comparison comparison) {

		comparison.getLhs().accept(this);
		switch (comparison.getOpeator()) {
		case Equals:
			result += " == ";
			break;
		case NotEquals:
			result += " != ";
			break;
		case Greater:
			result += " > ";
			break;
		case GreaterEqual:
			result += " >= ";
			break;
		case Less:
			result += " < ";
			break;
		case LessEqual:
			result += " <= ";
			break;
		}
		comparison.getRhs().accept(this);
		current_priority = 4;
	}

	@Override
	public void visit(UnaryCondition unaryCondition) {

		switch (unaryCondition.getOperator()) {
		case Not:
			result += "!";
			boolean brackets = false;
			String tmp = result;
			unaryCondition.getOperand().accept(this);
			if (current_priority >= 3)
				brackets = true;
			result = tmp;
			if (brackets)
				result += "(";
			unaryCondition.getOperand().accept(this);
			if (brackets)
				result += ")";
			break;
		}
		current_priority = 3;
	}

	@Override
	public void visit(BinaryCondition binaryCondition) {
		String tmp;
		boolean brackets = false;
		switch (binaryCondition.getOperator()) {
		case And:
			brackets = false;
			tmp = result;
			binaryCondition.getLhs().accept(this);
			if (current_priority == 13)
				brackets = true;
			result = tmp;
			if (brackets)
				result += "(";
			binaryCondition.getLhs().accept(this);
			if (brackets)
				result += ")";
			brackets = false;
			result += " && ";
			tmp = result;
			binaryCondition.getRhs().accept(this);
			if (current_priority == 13)
				brackets = true;
			result = tmp;
			if (brackets)
				result += "(";
			binaryCondition.getRhs().accept(this);
			if (brackets)
				result += ")";
			current_priority = 12;
			break;
		case Or:

			binaryCondition.getLhs().accept(this);

			result += " || ";

			binaryCondition.getRhs().accept(this);
			current_priority = 13;
			break;
		}
	}

	/*
	 * Rest
	 */

	@Override
	public void visit(Declaration declaration) {
		for(int i=0;i<2*tab;i++)result+=" ";
		result += "int ";
		String tmp = result;
		;
		String[] names = declaration.getNames();
		for (int i = 0; i < names.length; i++) {
			result += names[i];
			tmp = result;
			result += ", ";
		}
		result = tmp;
		result += ";\n";

	}

	@Override
	public void visit(Function function) {
		result+="int ";
		String[] parameters = function.getParameters();
		result+=function.getName();
		result+="(";
		String tmp = result;
		for (int i = 0; i < parameters.length; i++) {
			result+="int ";
			result+=parameters[i];
			tmp = result;
			result+=", ";
		}
		result = tmp;
		result+=")";
		result+="\n";
		result+="{\n";
		tab++;
		for (Declaration d : function.getDeclarations()) {
			d.accept(this);
		}
		
		for (Statement s : function.getStatements())
			s.accept(this);
		tab--;
		for(int i=0;i<2*tab;i++)result+=" ";
		result+="}\n";
		result+="\n";
	}

	@Override
	public void visit(Program program) {
		for (Function f : program.getFunctions())
			f.accept(this);
		

	}
}
