package syntaxtree;

import bytecode.CodeProcedure;
import bytecode.instructions.PUSHBOOL;
import errors.CodeGenerationError;

public class BoolLit extends Literal {

    Boolean literal;
    
    public BoolLit(Boolean literal){
        this.literal= literal;

    }


    public String printAst(){  // "pretty" printing 

   
        StringBuilder sb = new StringBuilder();
        sb.append("(BOOLEAN_LITERAL ");
        sb.append(this.literal.toString());  
        sb.append(")"); 
        return sb.toString();      
    }

    public DataType retDataType(){
        return new DataType("bool");
    }


    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        codeProc.addInstruction(new PUSHBOOL(literal));
    }
}

