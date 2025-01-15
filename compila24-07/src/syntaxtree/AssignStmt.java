package syntaxtree;

import bytecode.CodeFile;
import bytecode.CodeProcedure;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class AssignStmt  extends Statement{

    VarExp varExp;
    Exp exp;
    DerefVar derefVar;


    public AssignStmt(VarExp varExp, Exp exp ){
        this.varExp= varExp;
        this.exp= exp;

    }
    public AssignStmt(DerefVar derefVar, Exp exp ){
        this.derefVar= derefVar;
        this.exp= exp;

    }

    public String printAst(){  // "pretty" printing 
        //System.out.println("we get here");
        StringBuilder sb = new StringBuilder();
        sb.append("(ASSIGN ");
        if(varExp!= null){
            sb.append(varExp.printAst());
            
        } else sb.append(derefVar.printAst());
         
        sb.append(" := ");

        sb.append(exp.printAst());
        sb.append(" )"); 
        return sb.toString();      
    }

    public void typeCheck (SymbolTable symbolTable) throws TypeError{
        if(this.varExp != null){
            varExp.typeCheck(symbolTable);
            exp.typeCheck(symbolTable);
            //System.out.println("assignment " + exp.retDataType().retTypeString() );
            if(!TypeCheck.isTypeCompatible(varExp.retDataType(), exp.retDataType())){
                System.out.println("ok");
                throw new  TypeError("Assignment type "+ exp.retDataType().retTypeString() + " Does not match variable type "+  varExp.retDataType().retTypeString());
            }
        }
        
        if(this.derefVar!= null) {
            this.derefVar.typeCheck(symbolTable);
            this.exp.typeCheck(symbolTable);
            if(!TypeCheck.isTypeCompatible(derefVar.retDataType(), exp.retDataType())){
                throw new  TypeError("Deref Assignment type "+ derefVar.retDataType().retTypeString() + " Does not match variable type "+  derefVar.retDataType().retTypeString());
            }

        if(this.derefVar == null && this.varExp ==null)throw new TypeError("Wrong use of assignment");
        }      

    }

    public void generateProcedureCode (CodeProcedure procedure) throws CodeGenerationError{
        this.exp.generateCode(procedure);

        if(this.varExp!= null) this.varExp.storeCode(procedure);
        else this.derefVar.generateCode(procedure);

    }

    
}
