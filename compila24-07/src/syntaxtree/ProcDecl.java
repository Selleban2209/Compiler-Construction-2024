package syntaxtree;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import bytecode.CodeFile;
import bytecode.CodeProcedure;
import bytecode.instructions.RETURN;
import bytecode.type.CodeType;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class ProcDecl extends Decl {

    
    List<ParamDecl> proc_params;
    List<Statement> stmtLi; 
    List<Decl> decls; 
    DataType dataType;
    Boolean hasRetStmt = false;
   
    public ProcDecl(String name, List<ParamDecl> proc_params, List<Statement> stmtLi ){
        super(name);
        this.proc_params= proc_params;
        this.stmtLi= stmtLi;
    

    }
    public ProcDecl(String name, List<ParamDecl> proc_params, List<Statement> stmtLi , List<Decl> decls){
        super(name);
        this.proc_params= proc_params;
        this.stmtLi= stmtLi;
        this.decls= decls;
      

    }
    public ProcDecl(String name, List<ParamDecl> proc_params, List<Statement> stmtLi , List<Decl> decls, DataType dataType){
        super(name);
        this.proc_params= proc_params;
        this.stmtLi= stmtLi;
        this.decls= decls;
        this.dataType= dataType;

    }
    public ProcDecl(String name, List<ParamDecl> proc_params, List<Statement> stmtLi , DataType dataType){
        super(name);
        this.proc_params= proc_params;
        this.stmtLi= stmtLi;
        this.dataType= dataType;
    }

    //For library declerations
    public ProcDecl(String name, List<ParamDecl> proc_params,DataType dataType){
        super(name);
        this.proc_params= proc_params;
        this.dataType= dataType;


    }

    public String printAst(){  // "pretty" printing 
        StringBuilder sb = new StringBuilder();
        sb.append("(PROCEDURE_DECL ");
        sb.append("(NAME  ");
        sb.append(this.retName());
        if(dataType!= null){
            sb.append(" : ");
            sb.append(this.dataType.retTypeString());
        }
        sb.append(" )");

     
        if(proc_params!= null){
            for(ParamDecl pd : proc_params) {
                sb.append("\n");
                sb.append( "\t"+ pd.printAst());    
            }
        }
        if(decls!=null){
            for (Decl decl : decls) {
                sb.append("\n" +"\t"+ decl.printAst()); 
            }
        }
        if(stmtLi!=null){
            for (Statement state : stmtLi){
                sb.append("\n" +"\t"+ state.printAst());   
            }
        }
        sb.append(")");
        return sb.toString();      
    }

    public DataType retDataType(){
        if (this.dataType== null ) return new DataType("void");
        else return this.dataType;
    }
    public List<ParamDecl> retParameters(){
        return this.proc_params;
    }

    public void typeCheck(SymbolTable symbolTable) throws TypeError{
       //boolean hasRetStmt = false;
       SymbolTable procSymbolTable = symbolTable.createNestedTable();
       for(ParamDecl pd : proc_params) {
            pd.parentProc = this.name;
            
            procSymbolTable.insertDecl(pd);
           pd.typeCheck(procSymbolTable);
           
        }
        
        for (Decl decl : decls) {
            if(!(decl instanceof VarDecl|| decl instanceof ProcDecl)){
                throw new TypeError("Only variable and or procedure declerations  allowed within a procedure");
            }
            decl.parentProc = this.name;
            
            procSymbolTable.insertDecl(decl);
            decl.typeCheck(procSymbolTable);
        }
    
        for (Statement state : stmtLi){
            state.typeCheck(procSymbolTable);
            
            if((stmtLi.indexOf(state)== stmtLi.size()-1)  && state instanceof RetStmt){
                hasRetStmt= true;
                if(!(TypeCheck.isTypeCompatible(state.retDataType(), this.dataType))){   
                    throw new TypeError("Return type dosent match proceduretype");
                }
            }
            
        }
        procSymbolTable.printSymbolTable();
    }

    public void generateCode (CodeFile codeFile) throws CodeGenerationError{
       // System.out.println("procedure name: " + this.name  + " type " + this.retDataType().retTypeString());
        
        CodeType returnType  = TypeCheck.typeToByteType(this.retDataType(), codeFile);

        codeFile.addProcedure(this.name);
        CodeProcedure procedure =  new CodeProcedure(this.name, returnType, codeFile);

        for(ParamDecl pd : proc_params) {      
            CodeType paramType = TypeCheck.typeToByteType(pd.retDataType(), codeFile);
            procedure.addParameter(pd.retName(), paramType);

        }

        for (Decl decl : decls) {
            CodeType declType = TypeCheck.typeToByteType(decl.retDataType(), codeFile);
            procedure.addLocalVariable(decl.retName(), declType);          
        }

        for (Statement state : stmtLi){
            state.generateProcedureCode(procedure);
        }

        if(!hasRetStmt){
            procedure.addInstruction(new RETURN());
        }

        codeFile.updateProcedure(procedure);
    }

    
}
