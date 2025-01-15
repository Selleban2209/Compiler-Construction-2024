package syntaxtree;
import java.util.List;

import JFlex.sym;
import bytecode.CodeFile;
import bytecode.CodeProcedure;
import bytecode.type.CodeType;
import errors.CodeGenerationError;
import errors.TypeError;
import tools.StandardLibrary;
import tools.SymbolTable;
import tools.TypeCheck;

public class Program {

    List<Decl> decls;
    String name;
    StandardLibrary stlp;
    boolean mainExists= false;
    public Program(String name, List<Decl> decls) {
        this.decls = decls;
        this.name = name;
        this.stlp = new StandardLibrary();
    }

    public String printAst(){  // "pretty" printing 
      
        StringBuilder sb = new StringBuilder();
        sb.append("(PROGRAM ");
        sb.append("(NAME ");
        sb.append(this.name);
        sb.append(")\n");
        for (Decl decl : decls) {
            sb.append("\t" + decl.printAst());
            sb.append("\n");
        }
        sb.append(")");
        return sb.toString();
        
    }

    public void typeCheck(SymbolTable symbolTable) throws TypeError{

        for (int i =0; i< stlp.retStandardLibraryProcedures().size(); i++) {
        }
        for (ProcDecl LibDecl : stlp.retStandardLibraryProcedures()) {
            symbolTable.insertDecl(LibDecl);
            //System.out.println("Library variable name " + LibDecl.retName());
            
        }
        System.out.println("decleration size " + decls.size());
        for (Decl decl : decls) {
            symbolTable.insertDecl(decl);
            decl.typeCheck(symbolTable);
                
            if(decl.retName().equals("main")){
                if(!mainExists){
                    mainExists = true;
                } else throw new TypeError("Main procedure already exists");
            }
        }
        if(!mainExists){
            
            throw new TypeError("No main procedure found");
        } 
        System.out.println("SUCSESSFULLY TYPE CHECKED PROGRAM");
    }


    public void generateCode (CodeFile codeFile) throws CodeGenerationError{
        //Adding standard library declerations
        for (ProcDecl LibDecl : stlp.retStandardLibraryProcedures()) {
            //System.out.println("Library type " + LibDecl.retName());
            CodeType libRetType  = TypeCheck.typeToByteType(LibDecl.retDataType(), codeFile);
            codeFile.addProcedure(LibDecl.retName());
            CodeProcedure libProc =  new CodeProcedure(LibDecl.retName(), libRetType, codeFile);

            if(LibDecl.retParameters()!=null){
                for (ParamDecl pd : LibDecl.retParameters()) {
                    CodeType paramType = TypeCheck.typeToByteType(pd.retDataType(), codeFile);
                    libProc.addParameter(pd.retName(), paramType);
                }
            }
            codeFile.updateProcedure(libProc);
        }
        for (Decl decl : decls) {
            decl.generateCode(codeFile);
        }

        if(mainExists)codeFile.setMain("main");
        else throw new CodeGenerationError("Coudnt generate program, missing main procedure");


        System.out.println("COMPLETED CODE GENERATION!");
    }
}
