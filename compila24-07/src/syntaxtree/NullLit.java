package syntaxtree;

import bytecode.CodeProcedure;
import bytecode.instructions.PUSHNULL;
import errors.CodeGenerationError;

public class NullLit extends Literal {

    public NullLit() {

    }


    @Override
    public String printAst() {
        StringBuilder sb = new StringBuilder();
        sb.append("(NULL_LITERAL");

        sb.append(" )");
	    return sb.toString();

	}


    public DataType retDataType(){
        return null;
    }


    
    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        codeProc.addInstruction(new PUSHNULL());
    }

    
}
