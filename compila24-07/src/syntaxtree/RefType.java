package syntaxtree;

import errors.TypeError;
import tools.SymbolTable;

public class RefType extends DataType {

    private DataType dataType;
    public RefType(DataType dataType){

        this.dataType= dataType;

    }


    
    @Override
    public String printAst() {
        StringBuilder sb = new StringBuilder();
        sb.append("(REF_TYPE");
        sb.append("\n" +"\t"+this.dataType.printAst());
        sb.append(")");
	    return sb.toString();

	}
    
    public String retTypeString(){
        return this.dataType.name;
    }
    
	public void typeCheck(SymbolTable symbolTable) throws TypeError{}
    

}
