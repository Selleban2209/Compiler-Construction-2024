package syntaxtree;

import javax.xml.crypto.Data;

public class DataType {
    public String name;
    public Type type;
    public boolean isStruct;

    public DataType(){

    }

    public DataType(Type type){
        this.type = type;
        this.name = type.retName();
    }

    public DataType(String name){
        this.name = name;
    }


    public String printAst(){  // "pretty" printing 

    StringBuilder sb = new StringBuilder();
    if(this.type!= null) {
            sb.append("(TYPE ");
            sb.append(this.retTypeString());
        }
        else {
            sb.append("(NAME ");
            sb.append(this.name);

        }
        sb.append(" )"); 
        return sb.toString();      
    }

    public boolean isStruct(){
        return (this.name!= "bool" || this.name!= "int" || this.name!= "float" || this.name!= "string" || this.name!=" " || this.name!="void");
    }

    public String retTypeString(){
        return this.name;
    }
    
}
