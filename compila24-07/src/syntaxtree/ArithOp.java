package syntaxtree;

import bytecode.CodeProcedure;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;
import tools.TypeCheck;

public class ArithOp extends Exp{

    Exp left;
    Exp right;
    String op;
    public ArithOp(Exp leftExp, String op, Exp rightExp){
        this.left= leftExp;
        this.right= rightExp;
        this.op = op;   

    }

    public String printAst(){  // "pretty" printing 

        //System.out.println("we got to artihOp");
        StringBuilder sb = new StringBuilder();
        sb.append(left.printAst());

        sb.append(op);
        sb.append(right.printAst());
    
    
        return sb.toString();      
    }

    public DataType retDataType(){
        if(left.retDataType().retTypeString().equals("int") &&
        right.retDataType().retTypeString().equals("int "))return new DataType("int");

        return new DataType("float");
    }

    public void typeCheck (SymbolTable symbolTable) throws TypeError{
      
        left.typeCheck(symbolTable);
        right.typeCheck(symbolTable);
        //System.out.println("l" + left.retDataType().retTypeString());
        //System.out.println("r" + right.retDataType().retTypeString());
        
        if(!(left.retDataType().retTypeString()== "int" || left.retDataType().retTypeString()== "float")
        ||!(right.retDataType().retTypeString()== "float" || right.retDataType().retTypeString()== "int")){
            throw new TypeError("Invalid type for arithmetic expression, should be int or float");
        }

        
        if(!( op=="+"|| op=="-" || op=="*"|| op=="/"|| op=="^")) throw new TypeError("Invalid arithmetic operator sign");
    }



    @Override
    public void generateCode(CodeProcedure codeProc) throws CodeGenerationError {
        left.generateCode(codeProc);
        right.generateCode(codeProc);
        codeProc.addInstruction(TypeCheck.arithOperatorInstruction(op, codeProc));


    }

}
