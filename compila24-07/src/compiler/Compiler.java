package compiler;

import java.io.*;

import bytecode.CodeFile;
import errors.TypeError;
import tools.SymbolTable;
import syntaxtree.*;

import parser.*;
//import oblig1parser.*;

// That's the version for Oblig 2, it's extended compared to the version of
// oblig 1.  It's again mainly for INSPIRATION. It probably needs
// adaptation to a group's local situation and is not meant to be usable
// without changes.

public class Compiler {
    private String inFilename = null;
    private String astFilename = null;
    private String binFilename = null;
    private SymbolTable symbolTable =null;
    private CodeFile codeFile = null;
    public String syntaxError;
    public String error;
    public Compiler(String inFilename, String astFilename, String binFilename){
        this.inFilename = inFilename;
        this.astFilename = astFilename;
        this.binFilename = binFilename;
        this.symbolTable = new SymbolTable();
    }
    public int compile() throws Exception {
        InputStream inputStream = null;
        inputStream = new FileInputStream(this.inFilename);
        Lexer lexer = new Lexer(inputStream);
        parser parser = new parser(lexer);
        Program program;
        try {
            program = (Program)parser.parse().value;
            writeAST(program);
          
        } catch (Exception e) {
            // Do something here?
            e.printStackTrace();
            return 1;// Or something.
        }

        if(program== null) throw new Exception("Something went wrong");
        // Check semanics.
        /* */
        try {
            program.typeCheck(symbolTable);
            writeAST(program);
        } catch (TypeError e) {
            e.printStackTrace();
            return 1;
            // TODO: handle exception
        }

        try {
            generateCode(program);            
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }

        return 0;  
            
    }
    private void writeAST(Program program) throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.astFilename));
        bufferedWriter.write(program.printAst());
        bufferedWriter.close();
    }
    private void generateCode(Program program) throws Exception {
        CodeFile codeFile = new CodeFile();
        program.generateCode(codeFile);
        byte[] bytecode = codeFile.getBytecode();
        DataOutputStream stream = new DataOutputStream(new FileOutputStream (this.binFilename));
        stream.write(bytecode);
        stream.close();
    }
    public static void main(String[] args) {
        System.out.println("args length"+ args[2]);
        Compiler compiler = new Compiler(args[0], args[1], args[2]);
        int result;
        try {
            result = compiler.compile();
            if(result == 1){
                System.out.println(compiler.syntaxError);
            } else if(result == 2){
                System.out.println(compiler.error);
            }
            
            System.exit(result);
        } catch (Exception e) {

            System.out.println("ERROR: " + e);
            // If unknown error.
            System.exit(3);
        }
    }
    public static String indent(int indent){
        String result = "";
        for(int i=0;i<indent; i++){
            result+=" ";
        }
        return result;
    }
}
