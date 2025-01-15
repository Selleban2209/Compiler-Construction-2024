package syntaxtree;

import java.util.List;

import bytecode.CodeProcedure;
import bytecode.instructions.JMP;
import bytecode.instructions.JMPFALSE;
import bytecode.instructions.NOP;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class WhileStmt  extends Statement{


    Exp exp;
    List<Statement> whStmt;

    
    public WhileStmt(Exp exp, List<Statement> whStmt){
        this.exp = exp;
        this.whStmt= whStmt;
    }



    public String printAst(){
        StringBuilder sb = new StringBuilder();
        sb.append("(WHILE ");

        sb.append(exp.printAst());

        for (Statement state : whStmt) {
            sb.append("\n");
            sb.append("\t" + state.printAst());
        }



        sb.append(")");
        return sb.toString();
    }
     public void typeCheck(SymbolTable symbolTable) throws TypeError{
        if(this.exp!=null) typeCheck(symbolTable);

        for (Statement statement : whStmt) {
            statement.typeCheck(symbolTable);  
        }
    }

    @Override
    public void generateProcedureCode(CodeProcedure procedure) throws CodeGenerationError {
        int beforeExp = procedure.addInstruction(new NOP());
        
        if(this.exp!=null) this.exp.generateCode(procedure);
        
        int afterExpBeforeWhile = procedure.addInstruction(new NOP());
        
        for (Statement statement : whStmt) {
            statement.generateProcedureCode(procedure);
        }
        int afterWhileStmt  = procedure.addInstruction(new NOP());
        int whileJmp = procedure.addInstruction(new JMP(beforeExp));

        procedure.replaceInstruction(afterExpBeforeWhile, new JMPFALSE(afterWhileStmt));
        
    }
    
  
}
