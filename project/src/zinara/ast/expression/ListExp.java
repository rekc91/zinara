package zinara.ast.expression;
import zinara.code_generator.*;

import java.util.ArrayList;

import zinara.ast.type.IntType;
import zinara.ast.type.ListType;
import zinara.ast.type.DictType;
import zinara.ast.type.StringType;
import zinara.ast.type.TupleType;
import zinara.ast.type.Type;
import zinara.exceptions.TypeClashException;
import zinara.exceptions.InvalidCodeException;

import java.io.IOException;

// invariant: every element of the value has the same type
public class ListExp extends Expression {
    public ArrayList value; // arraylist of expressions

    public ListExp(ArrayList v) throws TypeClashException {
	value = v;
	Type t = ((Expression)value.get(0)).getType();
	boolean consistency = true;
	for (int i = 1; i < value.size(); i++)
	    consistency = consistency && (t.equals(((Expression)value.get(i)).getType()));
	if (!consistency)
	    throw new TypeClashException("La lista " + toString() + " tiene inconsistencia de tipos");
    }

    public ListExp(Expression e1, Expression e2) throws TypeClashException {
	if (!(e1.getType() instanceof IntType))
	    throw new TypeClashException("La expresion " + e1 + " debe ser del tipo <INT>");
	if (!e1.isStaticallyKnown())
	    throw new TypeClashException("La expresion " + e1 + " debe ser conocida a tiempo de compilacion");
	if (!(e2.getType() instanceof IntType))
	    throw new TypeClashException("La expresion " + e2 + " debe ser del tipo <INT>");
	if (!e2.isStaticallyKnown())
	    throw new TypeClashException("La expresion " + e2 + " debe ser conocida a tiempo de compilacion");

	Object i1 = e1.staticValue(), i2 = e2.staticValue();
	if (!(i1 instanceof Integer))
	    throw new TypeClashException("La expresion " + e1 + " no pudo ser reducida a Integer");
	if (!(i2 instanceof Integer))
	    throw new TypeClashException("La expresion " + e2 + " no pudo ser reducida a Integer");

	value = new ArrayList();
	for (int i = ((Integer)i1).intValue(); i < ((Integer)i2).intValue(); i++)
	    value.add(new IntegerExp(i));
    }

    public ListExp() { value = new ArrayList(); }

    public Type getType() throws TypeClashException { 
	if (type != null) return type;
	type = (value.size() > 0 ? new ListType(((Expression)value.get(0)).getType(), value.size()) : new ListType(null,0));
	return type;
    }

    public String toString() {
	String ret = "[";
	for (int i = 0; i < value.size(); i++)
	    ret += (Expression)value.get(i) + ", ";
	return (value.size() == 0 ? ret : ret.substring(0, ret.length()-2)) + "]";
    }

    public void tox86(Genx86 generator)
	throws IOException, InvalidCodeException{
	Expression expr;
	Type listType =  ((ListType)type).getInsideType();
	String reg = generator.regName(register,listType);
	String regAddr = generator.addrRegName(register);

	for (int i = value.size()-1; i >= 0 ; i--) {
	    //Se genera el valor
	    expr = (Expression)value.get(i);
	    expr.register = register;

	    if (expr instanceof BooleanExp){
		String ret = generator.newLabel();
		boolValue(generator,expr,ret,reg);
		generator.writeLabel(ret);
	    }
	    else
		expr.tox86(generator);
	    
	    
	    //Se pushea en la pila
	    if (!(expr instanceof ListExp)&&
		!(expr instanceof DictExp)&&
		!(expr instanceof TupleExp)&&
		!(expr instanceof StringExp))
		generator.write(generator.push(reg,listType.size()));
	}

	//Por ultimo, devuelvo la direccion donde comienza la lista
	generator.write(generator.mov(regAddr,generator.stack_pointer()));
    }

    public boolean isStaticallyKnown() {
	boolean isk = true;
	Expression v;
	for (int i = 0; i < value.size(); i++) {
	    v = (Expression)value.get(i);
	    isk = isk && v.isStaticallyKnown();
	}
	return isk;
    }

    public Object staticValue() {
	ArrayList result = new ArrayList();
	for (int i = 0; i < value.size(); i++)
	    result.add(((Expression)value.get(i)).staticValue());
	return result;
    }
}