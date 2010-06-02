package zinara.ast.expression;

import zinara.ast.type.BoolType;
import zinara.ast.type.FloatType;
import zinara.ast.type.IntType;
import zinara.ast.type.ListType;
import zinara.ast.type.Type;
import zinara.code_generator.Genx86;
import zinara.symtable.*;

import java.io.IOException;

public class Identifier extends LValue {
    private String identifier;
    private SymTable symtable;

    public Identifier (String id, SymTable st) {
	identifier = id;
	symtable = st;
    }

    public String getIdentifier() { return identifier; }
    public SymTable getSymTable() { return symtable; }
    public SymValue getSymValue(){
	return symtable.getSymbol(identifier);
    }

    public Type getType() {
	if (type != null) return type;
	type = symtable.getSymValueForId(identifier).getType();
	return type;
    }
    public String toString() { return identifier; }

    public void tox86(Genx86 generator) throws IOException {
	generator.write(generator.movAddr(generator.regName(register),
					  generator.global_offset()+
					  "+"+
					  Integer.toString(getSymValue().getOffset())));
	if (isExpression()) {
	    if (isBool())
		writeBooleanExpression(generator);
	    else
		writeExpression(generator);
	}	
    }
    
    public String currentDirection(Genx86 generator) {
	return generator.global_offset() + "+" + getSymValue().getOffset();
    }

    public void writeExpression(Genx86 generator) throws IOException{
	String reg = generator.regName(register);
	//Si es un tipo numerico o boleano, se copian los contenidos
	if (type.getType() instanceof IntType) {
	    generator.write(generator.movInt(reg,  "[" + reg + "]"));
	    generator.write("; writing  " + type.getType());
	}
	else if (type.getType() instanceof FloatType)
	    generator.write(generator.movReal(reg, "[" + reg + "]"));
	else if (type.getType() instanceof BoolType)
	    generator.write(generator.movBool(reg, "[" + reg +  "]"));
	else
	    generator.write("Identificador para el tipo "+type.getType().toString()+" no implementado\n");	    
    }

    public boolean isStaticallyKnown() {
	SymValue sv = symtable.getSymbol(identifier);
	if (sv.isVariable()) return false;
	// Recursively check the content of the expression
	else return ((Constant)sv.getStatus()).getExpression().isStaticallyKnown();
    }

    public Object staticValue() {
	SymValue sv = symtable.getSymbol(identifier);
	return ((Constant)sv.getStatus()).getExpression().staticValue();
    }
}
