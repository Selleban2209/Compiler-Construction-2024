package parser;
import java_cup.runtime.*;
%%

%class Lexer
%unicode
%cup
%line
%column
%public
%state STRING
%{
 StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

%}
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Identifier = [:jletter:] [:jletterdigit:]*
SingleLineComment = "//"[^LineTerminator]*LineTerminator
MultiLineComment = "(*" [^*] ~"*)"

IntLiteral = [0-9]+
FloatLiteral = [0-9]+ "." [0-9]+

//^[0-9]*[1-9][0-9]*$
//*$^-?\d+$
//^([+-]?[0-9]\d*|0)$


%%
<YYINITIAL>{
  {WhiteSpace}                          {  }
        "program"                       { return symbol(sym.PROGRAM); }
        "class"                         { return symbol(sym.CLASS); }
        "procedure"                     { return symbol(sym.PROCEDURE); }
        "begin"                         { return symbol(sym.BEGIN); }
        "end"                           { return symbol(sym.END); }
        "("                             { return symbol(sym.LPAR); }
        ")"                             { return symbol(sym.RPAR); }
        "{"                             { return symbol(sym.LCURL); }
        "}"                             { return symbol(sym.RCURL); }
        ","                             { return symbol(sym.COMMA); }
        "."                             { return symbol(sym.DOT); }   
        ";"                             { return symbol(sym.SEMI); }
        ":"                             { return symbol(sym.COLON); }
        "not"                           { return symbol(sym.NOT); }
        "new"                           { return symbol(sym.NEW); }

        "if"                            { return symbol(sym.IF); }
        "else"                          { return symbol(sym.ELSE); }
        "then"                          { return symbol(sym.THEN); }
        "return"                        { return symbol(sym.RETURN); }
        "while"                            { return symbol(sym.WHILE); }
        "do"                            { return symbol(sym.DO); }
        "od"                            { return symbol(sym.OD); }
        "fi"                            { return symbol(sym.FI); }
        


        "="                            { return symbol(sym.EQ); }
        ":="                            { return symbol(sym.ASSIGN); }
        "var"                           { return symbol(sym.VAR); }
        "ref"                           { return symbol(sym.REF); }
        "deref"                         { return symbol(sym.DEREF); }
        "struct"                        { return symbol(sym.STRUCT); }
        "new"                           { return symbol(sym.NEW); }
        "not"                           { return symbol(sym.NEW); }
        "in"                           { return symbol(sym.IN); }
        "null"                           { return symbol(sym.NULL); }
        
        "&&"                            { return symbol(sym.AND); }
        "||"                            { return symbol(sym.OR); }
        ">"                             { return symbol(sym.GT); }
        "<"                             { return symbol(sym.LT); }
        ">="                            { return symbol(sym.GEQ); }
        "<="                            { return symbol(sym.LEQ); }
        "<> "                           { return symbol(sym.ABR); }
       
        "+"                             { return symbol(sym.ADD); }
        "-"                             { return symbol(sym.SUB); }
        "/"                             { return symbol(sym.DIV); }
        "*"                             { return symbol(sym.MULT); }
        "^"                             { return symbol(sym.POW); }



        \"                              {string.setLength(0); yybegin(STRING);}


/*
"float" | "int" | "string" | "bool" | NAME
| "ref" "(" TYPE ")"
*/
        "int"                           { return symbol(sym.INT_TYPE); }
        "float"                         { return symbol(sym.FLOAT_TYPE); }
        "bool"                          { return symbol(sym.BOOL_TYPE); }
        "string"                        { return symbol(sym.STRING_TYPE); }

        "false"                         { return symbol(sym.BOOL_TYPE, new Boolean(false)); }
        "true"                          { return symbol(sym.BOOL_TYPE, new Boolean(true)); }
        

        
        {Identifier}                    { return symbol(sym.ID,yytext()); }
        {IntLiteral}                { return symbol(sym.INT_LITERAL, new Integer(yytext())); }
        {FloatLiteral}                  { return symbol(sym.FLOAT_LITERAL, new Float(yytext())); }
        {SingleLineComment}             { }
        {MultiLineComment}              { }
}

<STRING> {
      \"                             { yybegin(YYINITIAL);return symbol(sym.STRING_LITERAL,string.toString()); }
      [^\n\r\t\"\\]+                   { string.append( yytext() ); }
      \\t                            { }
      \\n                            {  }

      \\r                            { }
      \\\"                           { string.append('\"'); }
      \\                             { }
    }

.                           { throw new Error("Illegal character '" + yytext() + "' at line " + yyline + ", column " + yycolumn + "."); }
