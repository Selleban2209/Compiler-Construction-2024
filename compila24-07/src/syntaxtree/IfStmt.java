package syntaxtree;

import java.util.List;

import bytecode.CodeProcedure;
import bytecode.instructions.Instruction;
import bytecode.instructions.JMP;
import bytecode.instructions.JMPFALSE;
import bytecode.instructions.NOP;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.SymbolTable;

public class IfStmt extends Statement {

    Exp exp;
    List<Statement> ifStmt;
    List<Statement> elseStmt;
    
    public IfStmt(Exp exp, List<Statement> ifStmt){
        this.exp = exp;
        this.ifStmt= ifStmt;
    }
    public IfStmt(Exp exp, List<Statement> ifStmt, List<Statement> elseStmt){
        this.exp = exp;
        this.ifStmt= ifStmt;
        this.elseStmt= elseStmt;
    }

    public String printAst(){
        StringBuilder sb = new StringBuilder();

        sb.append("(IF ");

        sb.append(exp.printAst());

        for (Statement ifState : ifStmt) {
            sb.append("\t " + ifState.printAst());
        }
        sb.append(")");
        if(elseStmt!=null){
            sb.append("(ELSE ");
            for (Statement elseState : elseStmt){
                sb.append("\t " + elseState.printAst());
                
            }
        }
        sb.append(")");

        sb.append(" \n\t )");

        
        return sb.toString();
    }


    public void typeCheck (SymbolTable symbolTable) throws TypeError{
        if(this.exp!=null)exp.typeCheck(symbolTable);

        for (Statement statement : ifStmt) {
            statement.typeCheck(symbolTable);
        }
        
        if(this.elseStmt!=null){
            for (Statement statement : elseStmt) {
                statement.typeCheck(symbolTable);
            }
        }
    }
    
    @Override
    public void generateProcedureCode(CodeProcedure procedure) throws CodeGenerationError {
        if(this.exp!= null)this.exp.generateCode(procedure);
        
        int beforeIfStmt = procedure.addInstruction(new NOP());
        
        for (Statement statement : ifStmt) {
            statement.generateProcedureCode(procedure);
        }
        int afterIfStmt = procedure.addInstruction(new NOP());
        
        
        if(this.elseStmt!=null){
            
            int beforeElseStmt = procedure.addInstruction(new NOP());
            
            for (Statement statement : elseStmt) {
                statement.generateProcedureCode(procedure);
            }
            int afterElseStmt = procedure.addInstruction(new NOP());


            procedure.replaceInstruction(afterIfStmt, new JMP(afterElseStmt));
            procedure.replaceInstruction(beforeElseStmt, new JMPFALSE(beforeIfStmt));
        }
        procedure.replaceInstruction(beforeIfStmt, new JMPFALSE(afterIfStmt));
        procedure.addInstruction(new NOP());
    }
    
    
}
