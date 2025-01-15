package syntaxtree;

import bytecode.CodeFile;
import bytecode.CodeProcedure;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class Decl {
    public String name;
    public String parentProc;

    public Decl(String name){
        this.name = name;
        
    }

    public String printAst() {
        return " ";
    }
    

    public String retName(){
        return this.name;
    }

    public DataType retDataType() {
        return new DataType("void");
    }
    public void typeCheck(SymbolTable symbolTable) throws TypeError{
        
    }

    public void generateCode(CodeFile codeFile) throws CodeGenerationError {}

    

}
