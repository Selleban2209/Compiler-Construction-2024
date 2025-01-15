package syntaxtree;

import bytecode.CodeProcedure;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class DerefVar extends Exp{
    VarExp var;
    DerefVar derefVar;
    public DerefVar(VarExp var){
        this.var= var;

    }
    public DerefVar(DerefVar derefVar){
        this.derefVar= derefVar;

    }

    public String printAst(){  // "pretty" printing 
        StringBuilder sb = new StringBuilder();
        sb.append("(DEREF_VAR ");
        sb.append(var.printAst());

        sb.append(" )");

        return sb.toString();         
    }

    public DataType retDataType(){
        //System.out.println("deref var ret type" + this.var.retDataType().retTypeString());
        return this.var.retDataType();
    }

    public void typeCheck(SymbolTable symbolTable) throws TypeError{
        //System.out.println("we made it to deref");
        if(this.var!=null) var.typeCheck(symbolTable);

    }

    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        if(this.var!=null){
            var.storeCode(codeProc);

        }else if(this.derefVar!=null) {
            derefVar.generateCode(codeProc);
        }else {
            throw new CodeGenerationError("Coudnt generate derfrence");
        }
    }
}
    
