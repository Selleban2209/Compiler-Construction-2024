package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import syntaxtree.DataType;
import syntaxtree.ParamDecl;
import syntaxtree.ProcDecl;

public class StandardLibrary {

    List<ProcDecl> StandardLibraryProcs;

    public StandardLibrary(){
        //print library
        ProcDecl printInt = new ProcDecl("printint", Arrays.asList(new ParamDecl("pri", new DataType("int"))), new DataType("void"));
       
        ProcDecl printFloat = new ProcDecl("printfloat", Arrays.asList(new ParamDecl("prf", new DataType("float"))), new DataType("void"));
        
        ProcDecl printStr = new ProcDecl("printstr", Arrays.asList(new ParamDecl("prs", new DataType("string"))), new DataType("void"));
        
        ProcDecl printLine = new ProcDecl("printline", Arrays.asList(new ParamDecl("prl", new DataType("string"))), new DataType("void"));
        
        //read library 
        
        ProcDecl readInt = new ProcDecl("readint", Arrays.asList(new ParamDecl("rint", new DataType("int"))), new DataType("int"));
        ProcDecl readFloat = new ProcDecl("readfloat", Arrays.asList(new ParamDecl("rfloat", new DataType("float"))), new DataType("float"));


        ProcDecl readChar = new ProcDecl("readchar", Arrays.asList(new ParamDecl("rchar", new DataType("string"))), new DataType("int"));
        ProcDecl readString = new ProcDecl("readstring", Arrays.asList(new ParamDecl("rstr", new DataType("string"))), new DataType("string"));
        ProcDecl readLine = new ProcDecl("readline", Arrays.asList(new ParamDecl("rline", new DataType("string"))), new DataType("string"));



        //ProcDecl printFloat = new ProcDecl("printfloat", Arrays.asList(new ParamDecl("prf", new DataType("float"))), new DataType("float"));
        
        StandardLibraryProcs = new ArrayList<ProcDecl>();
        

        StandardLibraryProcs.add(printInt); 
        StandardLibraryProcs.add(printFloat);
        StandardLibraryProcs.add(printStr);
        StandardLibraryProcs.add(printLine);
        StandardLibraryProcs.add(readInt);
        StandardLibraryProcs.add(readFloat);
        StandardLibraryProcs.add(readChar);
        StandardLibraryProcs.add(readString);
        StandardLibraryProcs.add(readLine);
      
    }


    public List<ProcDecl> retStandardLibraryProcedures(){
        return StandardLibraryProcs;
    }
    
}
