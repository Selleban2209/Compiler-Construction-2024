package syntaxtree;  // This is just an inspirational example, probably 

import errors.TypeError;
import tools.SymbolTable;

// there will be no class declaration class, as 
                     // compila 24 does not support class declarations.
public class ClassDecl {

    String name;
    
    public ClassDecl (String name) {
        this.name = name;
    }

    public String printAst() {
        return "(CLASS_DECL (NAME " + name + "))";
    }

    

}
