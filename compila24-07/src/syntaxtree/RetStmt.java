package syntaxtree;

import bytecode.CodeProcedure;
import bytecode.instructions.RETURN;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class RetStmt extends Statement  {
    
    Exp exp;



    public RetStmt(Exp exp){
        this.exp= exp;
    }
    public RetStmt(){
        
    }

    

    public String printAst(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("(RETURN_STMT  ");
        if(exp!=null)sb.append(exp.printAst());
        sb.append(")");
        return sb.toString();
    }


    public DataType retDataType() throws TypeError{    
       if(exp.retDataType()== null){
        throw new TypeError("Missing data type on return statement");
        } else return exp.retDataType();
    }

    public void typeCheck (SymbolTable symbolTable) throws TypeError{
        //if(this.exp instanceof ArithOp) System.out.println("this is avariable expression");
        if(this.exp!=null)this.exp.typeCheck(symbolTable);
        
    }
   
    public void generateProcedureCode(CodeProcedure procedure) throws CodeGenerationError {
        if(this.exp!= null)this.exp.generateCode(procedure);
        procedure.addInstruction(new RETURN());
    }
}
    



