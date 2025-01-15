package bytecode;

import java.util.ArrayList;
import java.util.List;
import bytecode.type.CodeType;



/**
 * This class is conceptually analogous to the class CodeFile for the whole
 * program (and to CodeProcedure, responsible for procedures). An instance
 * of this class is to contain a representation of the "code" of a struct.
*/

public class CodeStruct {
	private String name;
	private List<String> names = new ArrayList<String>();
	private List<CodeType> types = new ArrayList<CodeType>();

    /**
     * This class is conceptually analogous to the class CodeFile for the
     * whole program (and to CodeProcedure, responsible for procedures). An
     * instance of this class is to contain a representation of the "code"
     * of a struct (resp. can compute it). 
     * @param name of the introduced record type. 
     */
    public CodeStruct(String name) {
	this.name = name;
    }

    /**
     * Add a field to a record type and fix its type.
     * @param name of the field. 
     * @param type of the field. 
     */
    public void addVariable(String name, CodeType type) {
	this.names.add(name);
	this.types.add(type);
    } 

    /**
     * Determine the index (an integer) of a field of a struct.
     * @return the so-called index of a variable (incl. formal parameter).
     * @param name of the variable (incl. formal parameter)
     */            
    public int fieldNumber(String name) {
	for(int i=0;i<this.names.size();i++){
	    if(name.equals(this.names.get(i))) return i;
	}
	return -1;
    }
    /**
     * A simple get-ter method for the name of the struct.
     * @return name of the struct.
     */    
    public String getName() {
	return name;
    }
    /** 
     * Returning the resulting bytecode of the struct.
     * @return the byte code as array of bytes.
     */    
    
    public byte[] getBytecode() {
	int totalSize = 0;
	byte[][] typesBytes = new byte[this.types.size()][];  // make an empty array of array of indiv. bytes
	for(int i=0; i<this.types.size(); i++){               // go through all types
	    typesBytes[i] =  this.types.get(i).getBytecode(); // fill one slot
	    totalSize     += typesBytes[i].length;            // keep tally 
	}
	byte[] nameBytes = this.name.getBytes();              // method from String class
	totalSize+=nameBytes.length;
	
	// Add size of name (2) and counters (2) => 4
	totalSize += 4;
	
	byte[] bytes = new byte[totalSize];
        NumberConversion.shortToByteArray(bytes,  0, (short) nameBytes.length);
        NumberConversion.shortToByteArray(bytes,  2, (short) this.types.size());
	
        int index = 4;
        insert(bytes, nameBytes, index);
	
	index += nameBytes.length;
	
        // Types
        // Only the values.
        for(int i=0;i<typesBytes.length;i++){
	    insert(bytes, typesBytes[i], index);
	    index += typesBytes[i].length;
        }
	
        return bytes;
    }
    
    private void insert(byte[] bytes, byte[] insert, int index) {
	for(int i=0;i<insert.length;i++){
	    bytes[index + i] = insert[i];
	}
    }
}
