package syntaxtree;

import java.util.List;

import bytecode.CodeProcedure;
import bytecode.instructions.CALL;
import bytecode.instructions.Instruction;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class CallStmt extends Statement {

    String name;
    List<Exp> expList;

    public CallStmt(String name, List<Exp> expList){
        this.name=name;
        this.expList= expList;
    }
    
    public CallStmt(String name){
        this.name=name;
        
    }
    

    public String printAst(){
        StringBuilder sb = new StringBuilder();    
        sb.append("(CALL_STMT ");
        sb.append(name);

        if(expList!=null){
            for (Exp exp : expList) {
                
                sb.append( "\t"+ exp.printAst());
            }
        }
        sb.append(")");
        return sb.toString();
    }


    
    public void typeCheck (SymbolTable symbolTable) throws TypeError{

        //symbolTable.printSymbolTable(); 
       
        ProcDecl procedure = symbolTable.retProcByName(this.name);
        
        //System.out.println("expression list size "+ expList.size() + ", procedure param size "+ procedure.retParameters().size());
        
        //No need to typecheck if call lacks arguments
        if(expList == null) return;

        for (Exp exp : expList) {
            exp.typeCheck(symbolTable);
        }
        
        if(expList.size()!= procedure.retParameters().size() ){
            new TypeError("Number arguments in definition and call for procedure"+ this.name+ " Arent matching");
        } 
       
        
        ParamDecl curParam = null;
        Exp curExp = null;
        for (int i = 0; i < procedure.retParameters().size(); i++) {
            curParam = procedure.retParameters().get(i);
            curExp = expList.get(i);
            
            if(!(TypeCheck.isTypeCompatible(curExp.retDataType(), curParam.retDataType()))){   
                throw new TypeError("Call argument  type dosent defined type: " + curParam.retDataType().retTypeString());
            }
            
        }

    }

    
    public void generateProcedureCode (CodeProcedure procedure) throws CodeGenerationError{
        if(this.expList!= null){
            for (Exp exp : expList) {
                exp.generateCode(procedure);
            }
        }
        int procNumber = procedure.procedureNumber(this.name);
        procedure.addInstruction(new CALL(procNumber));
    }
      
    
}
