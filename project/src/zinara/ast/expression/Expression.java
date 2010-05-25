package zinara.ast.expression;

import zinara.ast.ASTNode;
import zinara.ast.type.Type;
import zinara.code_generator.Genx86;
import zinara.exceptions.TypeClashException;

import java.io.IOException;

public abstract class Expression extends ASTNode {
    public Type type;
    //@ ensures \result != null
    public abstract Type getType() throws TypeClashException;
    public abstract String toString();
    public abstract String tox86(Genx86 generate) throws IOException;
}
