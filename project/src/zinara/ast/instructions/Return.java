package zinara.ast.instructions;

import zinara.ast.expression.Expression;

class Return extends Instruction{
    private Expression expr;

    public Return(Expression ex){
	this.expr = ex;
    }

    public Expression getExpression(){
	return this.expr;
    }
}