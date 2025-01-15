package syntaxtree;

import bytecode.CodeProcedure;
import bytecode.instructions.EQ;
import bytecode.instructions.GT;
import bytecode.instructions.GTEQ;
import bytecode.instructions.LT;
import bytecode.instructions.LTEQ;
import bytecode.instructions.NEQ;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class RelOp extends Exp {


   
    Exp left;
    Exp right;
    String op;
    
    public RelOp(Exp leftExp, String op, Exp rightExp){
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

        if(!(left.retDataType().retTypeString()== "int"|| left.retDataType().retTypeString()== "float") && !(right.retDataType().retTypeString()== "int"|| right.retDataType().retTypeString()== "float")) throw new TypeError("Invalid Relational comparison");

        if(!( op=="<"|| op==">" || op=="<=" ||op==">=" || op=="=" || op=="<>")) throw new TypeError("Invalid operator sign");
    }


    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        left.generateCode(codeProc);
        right.generateCode(codeProc);
       // System.out.println(" operator: " + op);
        switch (op) {
            case ">": 
                codeProc.addInstruction(new GT());
                return;
            case "<": 
                codeProc.addInstruction(new LT());
                return;
            case ">=": 
                codeProc.addInstruction(new GTEQ());
                return;
            case "<=": 
                codeProc.addInstruction(new LTEQ());
                return;
            case "=": 
                codeProc.addInstruction(new EQ());
                return;
            case "<>": 
                codeProc.addInstruction(new NEQ());
                return;
        
            default: throw new CodeGenerationError("Coudnt generate the correct arithmetic operation");
        }

    }

    
}
