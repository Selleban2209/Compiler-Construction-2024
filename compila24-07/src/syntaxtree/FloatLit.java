package syntaxtree;

import bytecode.CodeProcedure;
import bytecode.instructions.PUSHFLOAT;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class FloatLit extends Literal{

    Float floatLiteral;
    
    public FloatLit(Float floatLiteral){
        this.floatLiteral= floatLiteral;

    }


    public String printAst(){  // "pretty" printing 

   
        StringBuilder sb = new StringBuilder();
        sb.append("(FLOAT_LITERAL ");
        sb.append(this.floatLiteral.toString());  
        sb.append(")"); 
        return sb.toString();      
    }

    public DataType retDataType(){
        //System.out.println("reached float literal ret type ");
        return new DataType("float");
    }

     public void typeCheck (SymbolTable symbolTable) throws TypeError{}
    
     @Override
     public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
         codeProc.addInstruction(new PUSHFLOAT(floatLiteral));
     }
}
