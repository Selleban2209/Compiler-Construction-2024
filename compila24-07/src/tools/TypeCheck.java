package tools;

import bytecode.CodeFile;
import bytecode.CodeProcedure;
import bytecode.instructions.*;
import bytecode.type.*;
import errors.CodeGenerationError;
import syntaxtree.DataType;
import bytecode.type.CodeType;
import bytecode.type.IntType;
import errors.CodeGenerationError;
import syntaxtree.DataType;

public class TypeCheck {

    public static boolean isTypeCompatible(DataType type1, DataType type2){
        
        if(type1.retTypeString().equals(type2.retTypeString())){
            if(type1.isStruct() && type2.isStruct()){
                return type1.retTypeString().equals(type2.retTypeString());
            }
            return true;
        } 
        if((type1.retTypeString()=="int" && type2.retTypeString() =="float")||
        (type1.retTypeString()=="float" && type2.retTypeString() =="int")) {
            return true;
        }
        return false;

    }
    public static CodeType typeToByteType(DataType dataType, CodeFile codeFile) throws CodeGenerationError{

     //   System.out.println("type tobyte we got in here YES");

        switch (dataType.retTypeString()) {
            case "int": 
                return IntType.TYPE;    
            case "bool": 
                return BoolType.TYPE;
            case "float":
                return FloatType.TYPE;
            case "string":
                return StringType.TYPE;
            case " ":
                return VoidType.TYPE;
            case "void":
                return VoidType.TYPE;
            default: break;
        }

        if(dataType.isStruct()){
        
            return new RefType(codeFile.structNumber(dataType.retTypeString()));
        }
        
        throw new CodeGenerationError("Coudnt generate the correct return type");

    }


    public static CodeType typeToByteType(DataType dataType, CodeProcedure codeProc) throws CodeGenerationError{

        switch (dataType.retTypeString()) {
            case "int": 
                return IntType.TYPE;    
            case "bool": 
                return BoolType.TYPE;
            case "float":
                return FloatType.TYPE;
            case "string":
                return StringType.TYPE;
            case " ":
                return VoidType.TYPE;
            case "void":
                return VoidType.TYPE;
            default: break;
        }

        if(dataType.isStruct()){
            return new RefType(codeProc.structNumber(dataType.retTypeString()));
        }
        
        throw new CodeGenerationError("Coudnt generate the correct return type");

    }

    public static Instruction relOperatorToInstruc(String op, CodeProcedure codeProc) throws CodeGenerationError{
           switch (op) {
            case ">": codeProc.addInstruction(new GT());
            case "<": codeProc.addInstruction(new LT());
            case ">=": codeProc.addInstruction(new GTEQ());
            case "<=": codeProc.addInstruction(new LTEQ());
            case "=": codeProc.addInstruction(new EQ());
            case "<>": codeProc.addInstruction(new NEQ());
        
            default: throw new CodeGenerationError("Coudnt generate the correct relational operation");
        }
        
    }

    public static Instruction logOperatorToInstruc(String op, CodeProcedure codeProc) throws CodeGenerationError{

        switch (op) {
            case "||": return new OR();                
            case "&&": return new AND();                
           
            default: throw new CodeGenerationError("Coudnt generate the correct logical operation");
        }
    }

    public static Instruction arithOperatorInstruction( String op, CodeProcedure codeProc) throws CodeGenerationError {


        switch (op) {
            case "+": return new ADD();
            case "-": return new SUB();
            case "*": return new MUL();
            case "/": return new DIV();
            case "^": return new EXP();

            default:
               throw new CodeGenerationError("Coudnt generate the correct arithmetical operation");
        }    
        
    }
}
