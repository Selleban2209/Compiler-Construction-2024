package syntaxtree;

import bytecode.CodeProcedure;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class LogOp extends Exp {

    Exp left;
    Exp right;
    String op;
    public LogOp(Exp leftExp, String op, Exp rightExp){
        this.left= leftExp;
        this.right= rightExp;
        this.op = op;   


    }

    public String printAst(){  // "pretty" printing 
        StringBuilder sb = new StringBuilder();
       
        sb.append(left.printAst());

        sb.append(op);
        sb.append(right.printAst());
        
        return sb.toString();  
    }


    public DataType retDataType(){
        return new DataType("bool");
    }
        public void typeCheck (SymbolTable symbolTable) throws TypeError{
        left.typeCheck(symbolTable);
        right.typeCheck(symbolTable);

        if(!(left.retDataType().retTypeString()== "bool") && !(right.retDataType().retTypeString()== "bool")) throw new TypeError("Invalid logical comparison");

        if(!( op=="&&"|| op=="||" )) throw new TypeError("Invalid logical operator sign");
    }

    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        left.generateCode(codeProc);
        codeProc.addInstruction(TypeCheck.logOperatorToInstruc(op, codeProc));
        right.generateCode(codeProc);
        
    }
}
