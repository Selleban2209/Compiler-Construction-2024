package tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import errors.TypeError;
import syntaxtree.Decl;
import syntaxtree.ParamDecl;
import syntaxtree.VarDecl;
import syntaxtree.ProcDecl;
import syntaxtree.RecDecl;

public class SymbolTable {
    

    Map<String, VarDecl> variables;
    Map<String,String> parameters;
    Map<String, ProcDecl> procedures;
    Map<String, RecDecl> records;
    Map<String, String> handleDuplicateVars;
    ArrayList<SymbolTable> nestedTable;
   
    public SymbolTable(){
        this.variables = new HashMap<>();
        this.parameters = new HashMap<>();
        this.procedures = new HashMap<>();
        this.records = new HashMap<>();
        this.nestedTable = new ArrayList<>();
    }

    public void insertDecl(Decl decl) throws TypeError{
        if(decl instanceof ParamDecl){  
            insertParameter((ParamDecl)decl);
        } else if( decl instanceof VarDecl){
            insertVariable((VarDecl)decl);   
        }else if (decl instanceof ProcDecl){
            insertProcedure((ProcDecl)decl);
        } else if (decl instanceof RecDecl){   
            insertRecord((RecDecl)decl);
        } else {
            throw new TypeError("Wrong decleration somehow");
        }
    }

    public void insertVariable(VarDecl var) throws TypeError{
        if(this.variables.containsKey(var.retName())){
            //System.out.println("paramdecelaration parent " + var.parentProc);
            throw new TypeError("Variable already exists: " + var.retName() + " duplicate variable decleration.");
        }
        variables.put(var.retName(), var);
    }
    public void insertParameter(ParamDecl param) throws TypeError{
         
        if(parameters.containsKey(param.retName())){
            if(this.variables.containsKey(param.retName())){
                
                System.out.println("already exists");
                /*
                variables.put(param.retName()   , param);
                 * 
                 parameters.put(param.retName(), param.parentProc);
                 VarDecl  pdl =variables.get(param.retName());
                 parameters.put(pdl.retName(), pdl.parentProc  );
                 parameters.put(param.retName(), param.parentProc);
                 variables.put(param.retName()  , param);
                 return;
                 */
            }
            throw new TypeError("Paramter already exists: " + param.retName() + " duplicate variable decleration.");
        }
        //System.out.println("PARAMTER INSERT");
        parameters.put(param.retName(), param.parentProc);
        variables.put(param.retName()  , param);
    }
    
    public void insertProcedure(ProcDecl proc) throws TypeError{
        if(procedures.containsKey(proc.retName())){
            throw new TypeError("Procedure already exists");
        }
        procedures.put(proc.retName(), proc);
    }

    public void insertRecord(RecDecl rec) throws TypeError{
        if(records.containsKey(rec.retName())){
            throw new TypeError("Record already exists");
        }
        records.put(rec.retName(), rec);
    }



    public ProcDecl retProcByName(String name){
        return procedures.get(name);
    }
    public RecDecl retRecByName(String name){
        return records.get(name);
    }
    public String retParamByName(String name){
        return parameters.get(name);
    }
    public VarDecl retVarByName(String name){
        return variables.get(name);
    }

    public ArrayList<SymbolTable> getChildTables() {
        return this.nestedTable;
    }
    public SymbolTable createNestedTable(){

        SymbolTable newNestedTable = new SymbolTable();
        newNestedTable.procedures.putAll(this.procedures);
        newNestedTable.variables.putAll(this.variables);
        newNestedTable.records.putAll(this.records);
        this.getChildTables().add(newNestedTable);
        return newNestedTable;
    }
    //function for debugging, pritns the symboltable
    public void printSymbolTable(){
        System.out.println("-------------------[SymbolTable debug]--------------");
        int x =0;
        System.out.println("printing variable names");
        for (String variable : variables.keySet()) {
            System.out.println("Variable name: "+  variable+ ", Index = " + x);
            x++;
        }
        x=0;
        System.out.println("\nPrinting Procedure names");
        for (String procedure : procedures.keySet()) {
            System.out.println("Procedure name: "+  procedure+ ", Index = " + x);
            x++;
        }
        x=0;
        System.out.println("\nPrinting Record names");
        for (String record : records.keySet()) {
            System.out.println("Record name: "+  record+ ", Index = " + x);
            x++;
        }
        
        System.out.println("------------------------------------------------------");
        

    }
















}
