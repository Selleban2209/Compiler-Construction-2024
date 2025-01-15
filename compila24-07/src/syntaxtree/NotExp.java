package syntaxtree;

import bytecode.CodeProcedure;
import bytecode.instructions.NOT;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class NotExp extends Exp {

    Exp exp;

    public NotExp(Exp exp){
        this.exp =exp;
    }

    public String printAst(){  // "pretty" printing 
        StringBuilder sb = new StringBuilder();
        sb.append("(not ");
        sb.append(exp.printAst());

        sb.append(")");


        return sb.toString();      
    }

    public DataType retDataType(){
        return new DataType("bool");
    }
    
    public void typeCheck (SymbolTable symbolTable) throws TypeError{
        if(this.exp!=null)exp.typeCheck(symbolTable);   
    }
    

    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        if(this.exp != null)this.generateCode(codeProc);
        codeProc.addInstruction(new NOT());

    }
}
