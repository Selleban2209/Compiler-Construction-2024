package syntaxtree;

import java.util.List;

import bytecode.CodeFile;
import bytecode.CodeProcedure;
import bytecode.instructions.GETFIELD;
import bytecode.instructions.Instruction;
import bytecode.instructions.LOADGLOBAL;
import bytecode.instructions.LOADLOCAL;
import bytecode.instructions.PUTFIELD;
import bytecode.instructions.STOREGLOBAL;
import bytecode.instructions.STORELOCAL;
import errors.CodeGenerationError;
import errors.TypeError;
import java_cup.production;
import tools.SymbolTable;

public class VarExp extends Exp{

    Exp exp;
    String name;
    DataType dataType;
    public VarExp(String name){
        this.name= name;
    }
    
    public VarExp(String name, Exp exp){
        this.name= name;
        this.exp= exp;
    }
    
    public VarExp(String name, Exp exp, DataType dataType){
        this.name= name;
        this.exp= exp;
        this.dataType= dataType; 
    }
    public VarExp(String name,DataType dataType){
        this.name= name;
        this.dataType= dataType; 
    }


    public String printAst(){  // "pretty" printing 
        StringBuilder sb = new StringBuilder();

        if(exp!= null){
            sb.append(exp.printAst());
            sb.append(".");
            sb.append(name);

            sb.append(")");
        }else {
            
            sb.append("(NAME ");
    
            sb.append(this.name);
        }

        sb.append(")");
        
        return sb.toString();      
    }

    public DataType retDataType(){

        return this.dataType;
    }

    public void typeCheck(SymbolTable symbolTable) throws TypeError{
        
        
        if(this.exp == null){
            
            //symbolTable.printSymbolTable();
    
            DataType varDataType = symbolTable.retVarByName(this.name).retDataType();
            this.dataType = varDataType;
            
        } else { 
            this.exp.typeCheck(symbolTable);
            
            if(this.exp.retDataType().isStruct()){
                //System.out.println("found type from struct "+ this.exp.retDataType().retTypeString());

                RecDecl rec = symbolTable.retRecByName(this.exp.retDataType().retTypeString());

                List<ParamDecl> params = rec.retParameters();

                for (ParamDecl paramDecl : params) {
                    if(this.name.equals(paramDecl.retName())){

                        this.dataType = paramDecl.retDataType();
                        
                    }
                }
            }

        }  
    }

    
    public void generateCode (CodeProcedure procedure) throws CodeGenerationError{

        Instruction  instruction;
        if (this.exp == null ){
            if(procedure.variableNumber(this.name) ==-1){
                instruction = new LOADGLOBAL(procedure.globalVariableNumber(this.name));   
            } else instruction = new LOADLOCAL(procedure.variableNumber(this.name));

        } else {
           // System.out.println("expression data type GETFIELD " +this.exp.retDataType().retTypeString());
            this.exp.generateCode(procedure);
            instruction = new GETFIELD(procedure.fieldNumber(this.exp.retDataType().retTypeString(), this.name), procedure.structNumber(this.exp.retDataType().retTypeString()));
        }
        procedure.addInstruction(instruction);
    }


    public void storeCode (CodeProcedure procedure) throws CodeGenerationError{
        Instruction instruction;
        if(this.exp == null){

                if(procedure.variableNumber(this.name )==-1)  instruction = new STOREGLOBAL(procedure.globalVariableNumber(this.name));
                 else instruction = new STORELOCAL(procedure.variableNumber(this.name));

        }else {
            this.exp.generateCode(procedure);
            instruction = new PUTFIELD(procedure.fieldNumber(this.exp.retDataType().retTypeString(), this.name), procedure.structNumber(this.exp.retDataType().retTypeString()));

        }
        procedure.addInstruction(instruction);
    }
    
}
