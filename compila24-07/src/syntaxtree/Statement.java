package syntaxtree;

import bytecode.CodeFile;
import bytecode.CodeProcedure;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class Statement {

    
    

    public String printAst(){  // "pretty" printing 
        StringBuilder sb = new StringBuilder();


        return sb.toString();      
    }


    public  DataType retDataType() throws TypeError{
        return new DataType("none");
    }

    
    public void typeCheck (SymbolTable symbolTable) throws TypeError{}

    public void generateProcedureCode(CodeProcedure codeProc) throws CodeGenerationError {}
}
