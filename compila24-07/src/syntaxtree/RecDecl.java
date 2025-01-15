package syntaxtree;

import java.util.List;

import bytecode.CodeFile;
import bytecode.CodeStruct;
import bytecode.type.CodeType;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class RecDecl extends Decl {

    public List<ParamDecl> pds;

    public RecDecl(String name, List<ParamDecl> pds) {
        super(name);
        this.pds = pds;

    }

    @Override
    public String printAst() {
        StringBuilder sb = new StringBuilder();
        sb.append("(STRUCT ");
        sb.append( "(NAME ");
        sb.append(this.retName());

        
        sb.append(")");
        if(!pds.isEmpty()){
            for(ParamDecl pd: pds)
                sb.append("\n" +"\t"+ "\t"+ pd.printAst());             
        }
        sb.append(")");
        sb.append(")");

	    return sb.toString();

	}  

    public List<ParamDecl> retParameters(){
        return pds;
    }

    public void typeCheck(SymbolTable symbolTable) throws TypeError{
        //System.out.println(" we in this h");
        for (ParamDecl paramDecl : pds) {
          
            symbolTable.insertDecl(paramDecl);
            paramDecl.typeCheck(symbolTable);
        }  
    
    }
    
    public void generateCode(CodeFile codeFile) throws CodeGenerationError {
        codeFile.addStruct(this.name);

        CodeStruct struct = new CodeStruct(this.retName());

        for (ParamDecl param : pds) {
            param.generateCode(codeFile);
            CodeType paramType = TypeCheck.typeToByteType(param.retDataType(), codeFile);
            struct.addVariable(param.retName(), paramType);
        }

        codeFile.updateStruct(struct);
    }
    
}
