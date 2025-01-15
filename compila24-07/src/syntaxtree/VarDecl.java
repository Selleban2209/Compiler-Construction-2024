package syntaxtree;

import bytecode.CodeFile;
import bytecode.type.CodeType;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class VarDecl extends Decl {

    
    public DataType dataType;
    public Exp exp;

    //To allow duplicate variabels, as long as not nested
    public String parentProc;
   

    public VarDecl(String name, DataType dataType, Exp exp  ){
        super(name);
        this.dataType= dataType;
        this.exp= exp;
    }
    public VarDecl(String name, Exp exp ){
        super(name);
        this.exp= exp;

    }
    public VarDecl(String name, DataType dataType ){
        super(name);
        this.dataType= dataType;

    }


    @Override
    public String printAst() {
        StringBuilder sb = new StringBuilder();

        sb.append("(VAR_DECL ");
        sb.append(" (NAME ");
        
        //sb.append(this.retName());    
        if(dataType!=null){
            sb.append("\n" +"\t"+this.dataType.printAst());
            sb.append(" : ");

            if(exp!=null){
                sb.append(":=");
                sb.append(this.exp.printAst());
            }
        } else {
            sb.append(":=");
            sb.append(this.exp.printAst());

        }
        sb.append(this.retName());
        sb.append(")");
        sb.append("\n )");

	    return sb.toString();

	}

    public DataType retDataType(){
        return this.dataType;
    }

    @Override
    public void typeCheck (SymbolTable symbolTable) throws TypeError{
    
        if(this.dataType==null){
            VarDecl var = symbolTable.retVarByName(this.retName());

            if(var == null ) throw new TypeError("Coudnt find variable type");
        

            this.dataType = var.retDataType();

        }

        if(this.exp != null)this.exp.typeCheck(symbolTable);
    }


    public void generateCode(CodeFile codeFile) throws CodeGenerationError {

        codeFile.addVariable(this.name);


        CodeType varType =  TypeCheck.typeToByteType(this.dataType, codeFile);

        codeFile.updateVariable(this.name, varType);

    }

   

    
}
