package syntaxtree;

import javax.xml.crypto.Data;

import bytecode.CodeFile;
import bytecode.CodeProcedure;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class Exp {

    public Exp(){

    }
    
    public String printAst(){  // "pretty" printing 
        StringBuilder sb = new StringBuilder();

        return sb.toString();      
    }
    public DataType retDataType(){
        return new DataType("none");
    }
    
    public void typeCheck(SymbolTable symbolTable) throws TypeError{};
    

    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {}
    
}
