package syntaxtree;

import errors.TypeError;
import tools.SymbolTable;

public class ParamDecl extends VarDecl {

    public DataType dataType;
    public String name;
    public String parentRecName;
    public ParamDecl(String name, DataType dataType){
        super(name, dataType);
        this.name =name;
        this.dataType= dataType;
    
    }
    public ParamDecl( DataType dataType){
        super(dataType.retTypeString(),dataType);
        this.dataType= dataType;
    
    }
    

    @Override
    public String printAst() {
        StringBuilder sb = new StringBuilder();
        sb.append("(PARAM_DECL");
        sb.append(" " +this.dataType.printAst());
        sb.append(this.retName());
        sb.append(" : ");
        sb.append(this.dataType.retTypeString());

        sb.append(")");
       

	    return sb.toString();

	}

    public DataType retDataType(){
     
        return this.dataType;
    }

    public String retName(){
        return this.name;
    }
    public void typeCheck(SymbolTable symbolTable) throws TypeError{ }
  
}
