package syntaxtree;

import java.util.List;

import javax.xml.crypto.Data;

import bytecode.CodeProcedure;
import bytecode.instructions.CALL;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class CallExp extends Exp {

    String name;
    List<Exp> expList;
    public DataType dataType;
    public CallExp(String name, List<Exp> expList){
        this.name=name;
        this.expList= expList;
    }
    
    public CallExp(String name){
        this.name=name;
        
    }
    

    public String printAst(){
        StringBuilder sb = new StringBuilder();    
        sb.append("(CALL_EXP ");
        sb.append(name);

        if(expList!=null){
            for (Exp exp : expList) {  
                sb.append( "\t"+ exp.printAst());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public DataType retDataType(){
        return this.dataType;
    }
        
    public void typeCheck (SymbolTable symbolTable) throws TypeError{
      
      ProcDecl procedure = symbolTable.retProcByName(this.name);

      this.dataType = procedure.dataType;
       //No need to typecheck if call lacks arguments
      if(expList == null) return;

      //System.out.println("expression list size "+ expList.size() + ", procedure param size "+ procedure.retParameters().size());
      
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


    public void generateCode (CodeProcedure procedure) throws CodeGenerationError{
        if(this.expList!= null){
            for (Exp exp : expList) {
                exp.generateCode(procedure);
            }
        }
        int procNumber = procedure.procedureNumber(this.name);
        procedure.addInstruction(new CALL(procNumber));
    }

}
