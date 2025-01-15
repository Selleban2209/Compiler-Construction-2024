package syntaxtree;

import bytecode.CodeProcedure;
import bytecode.instructions.PUSHSTRING;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class StringLit extends Literal {


    String stringLiteral;
    
    public StringLit(String stringliteral){
        this.stringLiteral=  stringliteral;

    }
    
    public String printAst(){  // "pretty" printing 

   
        StringBuilder sb = new StringBuilder();
        sb.append("(STRING ");
        sb.append(this.stringLiteral.toString());  
        sb.append(")"); 
        return sb.toString();      
    }
    public DataType retDataType(){
        return new DataType("string");
    }


    public void typeCheck(SymbolTable symbolTable) throws TypeError{}

    @Override
    public void generateCode(CodeProcedure procedure) throws CodeGenerationError {
       procedure.addInstruction(new PUSHSTRING(procedure.addStringConstant(stringLiteral))); 
    }
}
