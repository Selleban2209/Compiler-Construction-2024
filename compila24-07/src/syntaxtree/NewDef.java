package syntaxtree;

import javax.xml.crypto.Data;

import bytecode.CodeProcedure;
import bytecode.instructions.NEW;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class NewDef extends Exp {

    DataType type;

    public NewDef(DataType type){
        this.type= type;
    }


   
    public String printAst(){  // "pretty" printing 

        StringBuilder sb = new StringBuilder();
        sb.append("new");
        sb.append(this.type.printAst());
        sb.append(this.type.retTypeString());
        
        return sb.toString();      
    }

    public DataType retDataType(){
        return this.type;
    }

    
    public void typeCheck (SymbolTable symbolTable) throws TypeError{

        //System.out.println("type "+ this.type.retTypeString());
        if(!this.type.isStruct() ){
            throw new TypeError("New expression only usable to define struct");
        }
    }

    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        codeProc.addInstruction(new NEW(codeProc.structNumber(this.retDataType().retTypeString())));
    }
    
}
