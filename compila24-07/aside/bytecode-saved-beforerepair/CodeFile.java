package bytecode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bytecode.type.CodeType;

/**
 * An instance of this class contains a representation of the code of a
 * complete program (and to generate the corresponding bytes). Typically,
 * per compilation, there is only one instance of that class, i.e., the
 * typical usage is that of a singleton class.
*/
public class CodeFile {
    private int main=-1;
    private List<String> variableNames = new ArrayList<String>();
    private Map<Integer, CodeType> variableTypes = new HashMap<Integer, CodeType>();
    
    private List<String> procedureNames = new ArrayList<String>();
    private Map<Integer, CodeProcedure> procedures = new HashMap<Integer, CodeProcedure>();
    
    private List<String> structNames = new ArrayList<String>();
    private Map<Integer, CodeStruct> structs = new HashMap<Integer, CodeStruct>();
    
    private List<String> stringConstants = new ArrayList<String>();

    /**
     * Prepare a global variable "declaration", i.e. initialize it in
     * adding the name of the variable. Internally, the variable is known
     * via its index, which is not returned by the method.
     * @param name of the variable. The variable name must be unique. 
     */    
    public void addVariable(String name) {
	this.variableNames.add(name);
    }

    /**
     * Declare the type for a global variable (which must have been added
     * before), Add a global variable "declaration".  Internally, the
     * variable is managed via its index, which is not returned by the
     * method.
     * @param name of the variable. 
     * @param type of the variable. 
     */        
    public void updateVariable(String name, CodeType type) {
		for(int i=0;i<this.variableNames.size();i++){
			if(name.equals(this.variableNames.get(i))){
				this.variableTypes.put(new Integer(i), type);
			}
		}
	}

    /**
     * Prepare for a procedure declaration and definition. Internally, the
     * procedure is is managed via its index, which, however, is not
     * returned by the method.
     * @param name of the variable. 
     */        
    public void addProcedure(String name) {
	this.procedureNames.add(name);  // append entry.
    }

    /**
     * Finalize the definition of a procedure (whose name has to be added before).
     * @param codeProcedure: the "code" of the procedure, which includes
     * parameters and their types.
     */            
    
    public void updateProcedure(CodeProcedure codeProcedure) {
	for(int i=0;i<this.procedureNames.size();i++){
	    if(codeProcedure.getName().equals(this.procedureNames.get(i))){
		this.procedures.put(new Integer(i), codeProcedure);
	    }
	}
    }

    /**
     * Prepare the definition of a named record type (struct type).
     * Internally, the (name of) the record type is is managed via its
     * index, which, however, is not returned by the method.
     * @param name of the record type
     */                
    
    public void addStruct(String name) {
	this.structNames.add(name);
    }
    
    /**
     * Finalize the definition of a named record type (struct type), by
     * providing the "code" object for the record type.
     * The codeStruct object carries its own name 
     * @param codeStruct of the record type
     */                

    public void updateStruct(CodeStruct codeStruct) {
	for(int i=0;i<this.structNames.size();i++){
	    if(codeStruct.getName().equals(this.structNames.get(i))){
		this.structs.put(new Integer(i), codeStruct);
		
	    }
	}
    }

    
    /**
     * Add a string constant. Unlike other add-methods, the index of the
     * added string constant is returned to the caller.
     * @param value is the string constant 
     * @return the index under which the the constant is stored
     */                
    
    public int addStringConstant(String value) {
	this.stringConstants.add(value);
	return this.stringConstants.size()-1;
    }

    /**
     * Determine the index (an integer) of a global variable declared earlier
     * @return the so-called index of a global variable.
     * @param name of the variable
     */    
    public int globalVariableNumber(String name) {
	for(int i=0; i<this.variableNames.size(); i++){
	    if(name.equals(this.variableNames.get(i))){
		return i;
	    }
	}
	return -1;
    }

    /** 
     * Look up the index of a procedure.
     * @param name of the procedure
     * @return the index of the procedure.
     */
    public int procedureNumber(String name) {
	for(int i=0; i<this.procedureNames.size();i++){
	    if(this.procedureNames.get(i).equals(name)){
		return i;
	    }
	}
	return -1;
    }

    /** 
     * Look up the index of a record type.
     * @param name of the record type
     * @return the index of the record type.
     */    

    public int structNumber(String name) {
	for(int i=0; i<this.structNames.size();i++){
	    if(name.equals(this.structNames.get(i))){
		return i;
	    }
	}
	return -1;
    }
    
    /** 
     * Look up the index of a record type.
     * @param structName name of the record type
     * @param varName name of the field
     * @return the index of a field inside a record type.
     */    

    public int fieldNumber(String structName, String varName) {
	for(int i=0; i<this.structs.size();i++){
	    CodeStruct codeStruct = this.structs.get(i);
	    if(structName.equals(codeStruct.getName())){
		return codeStruct.fieldNumber(varName);
	    }
	}
	return -1;
    }

    /** 
     * Determine the "main" procedure .
     * @param name of the main procedure
     */    
    
    public void setMain(String name) {
	for(int i=0; i<this.procedures.size();i++){
	    if(this.procedures.get(i).getName().equals(name)){
		this.main = i;
	    }
	}
    }


    /** 
     * Returning the resulting bytecode as the overall result of the byte code generation.
     * @return the byte code as array of bytes.
     */    
    
    public byte[] getBytecode() {
	int totalSize = 0;
	byte[][] variableNamesBytes = new byte[this.variableNames.size()][];  
	for(int i=0; i<this.variableNames.size(); i++){                       
	    variableNamesBytes[i] = this.variableNames.get(i).getBytes();     
	    totalSize += variableNamesBytes[i].length + 2;  //  extra 2
	}
	byte[][] variableTypesBytes = new byte[this.variableTypes.size()][];  
	for(int i=0; i<this.variableTypes.size(); i++){
	    variableTypesBytes[i] = this.variableTypes.get(new Integer(i)).getBytecode();
	    totalSize += variableTypesBytes[i].length;
	}
	byte[][] proceduresBytes = new byte[this.procedures.size()][];
	for(int i=0;i<this.procedures.size();i++){
	    proceduresBytes[i] = this.procedures.get(new Integer(i)).getBytecode();
	    totalSize += proceduresBytes[i].length + 2;
	}
	byte[][] structsBytes = new byte[this.structs.size()][];
	for(int i=0;i<this.structs.size();i++){
	    structsBytes[i] = this.structs.get(new Integer(i)).getBytecode();
	    totalSize += structsBytes[i].length + 2;
	}
	byte[][] stringConstantsBytes = new byte[this.stringConstants.size()][];
	for(int i=0; i<this.stringConstants.size(); i++){
	    stringConstantsBytes[i] = this.stringConstants.get(i).getBytes();
	    totalSize += stringConstantsBytes[i].length + 2;
	}
	
	// Add main (4), counters (4*2) => 12  (main = 2? => 10?)
	totalSize += 12;
	
        byte[] bytes = new byte[totalSize];
//        NumberConversion.shortToByteArray(bytes, 0, (short) 0xCABE);
        NumberConversion.shortToByteArray(bytes,  0, (short) this.main);
        NumberConversion.shortToByteArray(bytes,  2, (short) this.variableNames.size());
        NumberConversion.shortToByteArray(bytes,  4, (short) this.procedures.size());
        NumberConversion.shortToByteArray(bytes,  6, (short) this.structs.size());
        NumberConversion.shortToByteArray(bytes,  8, (short) this.stringConstants.size());

        int index = 10;
        // Variables
        // First the sizes
        for(int i=0;i<variableNamesBytes.length;i++){
        	NumberConversion.shortToByteArray(bytes, index, (short) variableNamesBytes[i].length);
        	index += 2;
        }
        // Then the values
        for(int i=0;i<variableNamesBytes.length;i++){
        	insert(bytes, variableNamesBytes[i], index);
        	index += variableNamesBytes[i].length;
        }
	// Then the types
        for(int i=0;i<variableTypesBytes.length;i++){
        	insert(bytes, variableTypesBytes[i], index);
        	index += variableTypesBytes[i].length;
        }

        // Procedures
        // First the sizes
        for(int i=0;i<proceduresBytes.length;i++){
        	NumberConversion.shortToByteArray(bytes, index, (short) proceduresBytes[i].length);
        	index += 2;
        }
        // Then the values
        for(int i=0;i<proceduresBytes.length;i++){
        	insert(bytes, proceduresBytes[i], index);
        	index+=proceduresBytes[i].length;
        }

        // Structs
        // First the sizes
        for(int i=0;i<structsBytes.length;i++){
        	NumberConversion.shortToByteArray(bytes, index, (short) structsBytes[i].length);
        	index += 2;
        }
        // Then the values
        for(int i=0;i<structsBytes.length;i++){
        	insert(bytes, structsBytes[i], index);
        	index+=structsBytes[i].length;
        }

        // Constants
        // First the sizes
        for(int i=0;i<stringConstantsBytes.length;i++){
        	NumberConversion.shortToByteArray(bytes, index, (short) stringConstantsBytes[i].length);
        	index += 2;
        }
        // Then the values
        for(int i=0;i<stringConstantsBytes.length;i++){
        	insert(bytes, stringConstantsBytes[i], index);
        	index+=stringConstantsBytes[i].length;
        }

        return bytes;
	}

    private void insert(byte[] bytes, byte[] insert, int index) {
	for(int i=0;i<insert.length;i++){
	    bytes[index + i] = insert[i];
	}
    }
    
}
