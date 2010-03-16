package zinara;

import zinara.ast.Program;
import zinara.exceptions.*;
import zinara.lexer.*;
import zinara.parser.*;

import java.io.*;

public class Main {
    public static final boolean debug_parsing = false;

    public static void main(String argv[]) {
	try {
	    parser p = new parser(new Scanner(new FileReader(argv[0])));
	    Program root;
	    if (debug_parsing)
		root = (Program)p.debug_parse().value;
	    else
		root = (Program)p.parse().value;
	    System.out.println("AST:      " + root + "\n");
	    System.out.println("SYMTABLE: " + root.getSymTable() + "\n");
	} 
	catch (ClassCastException e) {
	    System.out.println("oops..., classcastE: " + e.toString());
	    e.printStackTrace();
	}
	catch (IdentifierAlreadyDeclaredException e) { System.out.println(e.getMessage()); }
	catch (IdentifierNotDeclaredException e) { System.out.println(e.getMessage()); }
	catch (InvalidAssignationException e) { System.out.println(e.getMessage()); }
	catch (TypeClashException e) { System.out.println(e.getMessage()); }
	catch (Exception e) {
	    System.out.println("oops...");
	    e.printStackTrace();
	    System.out.println(e.getMessage());
	}
    }
}
