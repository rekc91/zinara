package zinara.lexer;
/* JFlex example: part of Java language lexer specification */

import java_cup.runtime.*;

import zinara.parser.*;

/**
 * This class is a simple example lexer.
 */
%%

%class Scanner
%public
%unicode
%cup
%cupdebug
%line
%column

%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\n]
WhiteSpace     = [ \t\f]
EmptyLine      = [\ ]* "\n"
Letter         = [a-zA-Z]
Digit          = [0-9]
Alphanumeric   = [a-zA-Z0-9]

Comment = {MultipleComment} | {SimpleComment}

MultipleComment   = "/\." [^\.] ~"\./" | "/\." "\."+ "\./" "\ "* "\n"?
SimpleComment     = "//" {InputCharacter}* {LineTerminator}

Identifier  = {Letter} {Alphanumeric}*
Number      = {Digit}+


%%

//{Comment}                        {}
{WhiteSpace}                     {}
^{EmptyLine}                     {}

 "main" "\ "* "\n"               { return symbol(sym.MAIN); }
 "endmain" "\ "* "\n"            { return symbol(sym.ENDMAIN); }

 {LineTerminator}                { return symbol(sym.SEMI); }

 "("                             { return symbol(sym.LPAREN); }
 ")"                             { return symbol(sym.RPAREN); }
 "["                             { return symbol(sym.LBRACKET); }
 "]"                             { return symbol(sym.RBRACKET); }
 "{"                             { return symbol(sym.LBRACET);}
 "}"                             { return symbol(sym.RBRACET);}

 "Int"                           { return symbol(sym.INTEGER); }
 "Float"                         { return symbol(sym.FLOAT); }
 "Char"                          { return symbol(sym.CHAR); }
 "String"                        { return symbol(sym.STRING); }
 "Bool"                          { return symbol(sym.BOOL); }
 "var"                           { return symbol(sym.VAR); }
 "def"                           { return symbol(sym.DEF); }
 ":" "\ "* "\n"                  { return symbol(sym.DOUBLEDOT); }
 "copy"                          { return symbol(sym.COPY); }
 ","                             { return symbol(sym.COMMA);}

 "+"                             { return symbol(sym.PLUS);}
 "-"                             { return symbol(sym.MINUS);}
 "*"                             { return symbol(sym.TIMES); }
 "/"                             { return symbol(sym.DIVIDE);}
 "%"                             { return symbol(sym.MOD);}
 "^"                             { return symbol(sym.PLUS);}

 ">"                             { return symbol(sym.GT);}
 "<"                             { return symbol(sym.LT);}
 ">="                            { return symbol(sym.GTE);}
 "<="                            { return symbol(sym.LTE);}
 "=="                            { return symbol(sym.SHEQ);}
 "<=>"                           { return symbol(sym.DEEQ);}
 "AND"                           { return symbol(sym.AND);}
 "OR"                            { return symbol(sym.OR);}
 "&"                             { return symbol(sym.SAND);}
 "|"                             { return symbol(sym.SOR);}
 "!"                             { return symbol(sym.NOEQ);}

 "for"                           { return symbol(sym.FOR);}
 "in"                            { return symbol(sym.IN);}
 "endfor"                        { return symbol(sym.ENDFOR);}
 "def"                           { return symbol(sym.DEF);}
 "as"                            { return symbol(sym.AS); }
 "enddef"                        { return symbol(sym.ENDDEF);}
 "while"                         { return symbol(sym.WHILE);}
 "endwhile"                      { return symbol(sym.ENDWHILE);}
 "cycle"                         { return symbol(sym.CYCLE);}
 "on"                            { return symbol(sym.ON);}
 "do"                            { return symbol(sym.DO);}
 "endcycle"                      { return symbol(sym.ENDCYCLE);}
 "if"                            { return symbol(sym.IF);}
 "elif"                          { return symbol(sym.ELIF);}
 "else"                          { return symbol(sym.ELSE);}
 "endif"                         { return symbol(sym.ENDIF);}
 "end"                           { return symbol(sym.END); }

 "<<"                            { return symbol(sym.LTLT);}
 ">>"                            { return symbol(sym.GTGT);}
 "++"                            { return symbol(sym.PLUSPLUS);}
 "="                             { return symbol(sym.ASIG);}
 "return"                        { return symbol(sym.RETURN);}
 "returns"                       { return symbol(sym.RETURNS);}
 "print"                         { return symbol(sym.PRINT);}
 "read"                          { return symbol(sym.READ);}

 "true"                          { return symbol(sym.TRUE); }
 "false"                         { return symbol(sym.FALSE); }
 [0-9]+\.[0-9]*                  { return symbol(sym.FLOAT_V); }
 [0-9]*\.[0-9]+                  { return symbol(sym.FLOAT_V); }
 [0-9]+                          { return symbol(sym.INTEGER_V); }
 [A-Za-z] [a-zA-Z\'_0-9]*         { return symbol(sym.IDENTIFIER); }


// .*     { throw new Error("Illegal character <"+yytext()+">"); }