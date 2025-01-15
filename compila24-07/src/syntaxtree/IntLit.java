package syntaxtree;

import bytecode.CodeProcedure;
import bytecode.instructions.PUSHINT;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class IntLit extends Literal {
    Integer literal;
    
    public IntLit(Integer literal){
        this.literal= literal;

    }


    public String printAst(){  // "pretty" printing 

   
        StringBuilder sb = new StringBuilder();
        sb.append("(INT_LITERAL ");
        sb.append(this.literal.toString());  
        sb.append(")"); 
        return sb.toString();      
    }



    public DataType retDataType(){
        return new DataType("int");
    }
    public void typeCheck (SymbolTable symbolTable) throws TypeError{}


    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        codeProc.addInstruction(new PUSHINT(literal));
    }
}
