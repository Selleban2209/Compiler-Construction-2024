package syntaxtree;

import bytecode.CodeProcedure;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class RefVar extends Exp {
    
    VarExp var;


    public RefVar(VarExp var){
        this.var= var;

    }
    

    public String printAst(){  // "pretty" printing 
        StringBuilder sb = new StringBuilder();
        sb.append("(REF_VAR ");
        sb.append(var.printAst());

        sb.append(" )");

        return sb.toString();      
    }

    public DataType retDataType(){
        //System.out.println("refrence datat type"+ this.var.retDataType().retTypeString());
        return this.var.retDataType();
    }

    
    public void typeCheck (SymbolTable symbolTable) throws TypeError{
        if(this.var!=null)var.typeCheck(symbolTable);
        
    }

    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        if(this.var!=null)this.var.generateCode(codeProc);
    }
}
