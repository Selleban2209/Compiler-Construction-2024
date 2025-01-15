package bytecode;

import java.util.ArrayList;
import java.util.List;
import bytecode.instructions.Instruction;
import bytecode.instructions.JMP;
import bytecode.instructions.JMPFALSE;
import bytecode.instructions.JMPTRUE;
import bytecode.type.CodeType;


/**
 * This class is conceptually analogous to the class CodeFile for the whole
 * program (and to CodeStruct, responsible for records). An instance of
 * this class is to contain a representation of the code of a procedure.
 * Compared to CodeFile, the class CodeProcedure is more complex. A
 * procedure can contain more kinds of code pieces. In particular, all
 * instructions are contained inside procedure.
*/


public class CodeProcedure {
    private String name;
    private CodeType returnType;
    private CodeFile codeFile;
    private List<String> parameterNames = new ArrayList<String>();
    private List<CodeType> parameterTypes = new ArrayList<CodeType>();
    private List<String> variableNames = new ArrayList<String>();
    private List<CodeType> variableTypes = new ArrayList<CodeType>();
    private List<Instruction> instructions = new ArrayList<Instruction>();

    /**
     * The constructor gives back the initially (mostly) empty data
     * structure to store the definition of a procedure. Provided initially
     * as part of the constructore are the name of the procedure (which
     * must be unique) and the return type. Note that the types of the
     * parameters are not initially given, they have to be added later one
     * one after the other, when adding the parameters together with their
     * types. The last parameter is the reference to the "code file",
     * representing the main program. The object requires access to that
     * for some of its tasks (in some call-back-like pattern).
     * @param name of the procedure
     * @param returnType specifies the return type of the procedure
     * @param codeFile reference to the representation of the main program
     */
    
    public CodeProcedure(String name, CodeType returnType, CodeFile codeFile) {
	this.name = name;
	this.returnType = returnType;
	this.codeFile = codeFile;
    }

    /** 
	@param name of the parameter of the procedure
	@param type of the parameter
     */
    public void addParameter(String name, CodeType type) {
	this.parameterNames.add(name);
	this.parameterTypes.add(type);
    }
    /** 
	Adding a local variable (declaration) to the procedure. The names
	of local variables have to be unique per procedure, and that
	includes the parameters as well; in that respect parameters count
	among the local variables. Internally, local variables are referred
	to by an index. That's analogous to the treatment of global
	variables in a  CodeFile object.
	@param name of the local variable 
	@param type of the local variable 
     */    
    public void addLocalVariable(String name, CodeType type) {
	this.variableNames.add(name);
	this.variableTypes.add(type);
    }

    /** 
	Adding a instruction. The classes representing the instructions are
	provided by the subpackage bytecode/instructions*. The procedure
	returns the index of the instruction. That can be used, for
	instance, to replace the instruction. 
	@param instruction being added
	@return index of the instruction
     */    

    
    public int addInstruction(Instruction instruction) {
	this.instructions.add(instruction);
	return this.instructions.size()-1;
    }

    /** 
	Replacing an instruction at some place specified by its index.
	@param place i.e., index of the instruction to be replaced
	@param instruction that replaces the old one. An situation where a
	replacement could come in handy are jumps.  First, one adds a no-op
	(NOP) instruction as a placeholder, which is later replaced by the
	correct instruction, a jump. Instead of remove and add,
	instructions.set should do the same.
     */    
    
    public void replaceInstruction(int place, Instruction instruction) {
	this.instructions.remove(place);  
	this.instructions.add(place, instruction);
    }
    /**
     * The method corresponds to the corresponding method in the code file,
     * and indeed an invocation simply delegates the task to the method of
     * the code file.
     * @param value representing the string constant
     * @return the index of the constant.
     */
    public int addStringConstant(String value) {
	return this.codeFile.addStringConstant(value);
    }
    
    /**
     * Determine the index (an integer) of a local variable or procedure
     * parameter declared earlier. Note that local variables and parameters
     * are stored in separate arrays. That means the official "index" of a
     * local variable is not identical to the array index in the array for
     * local variables. For parameters, there is no such mismatch.
     * @return the so-called index of a variable (incl. formal parameter).
     * @param name of the variable (incl. formal parameter).
     */        
    public int variableNumber(String name) {
	for (int i=0; i<parameterNames.size(); i++){
	    if(name.equals(parameterNames.get(i))){
		return i;
	    }
	}
	for (int i=0; i<this.variableNames.size(); i++){
	    if(name.equals(this.variableNames.get(i))){
		return this.parameterNames.size() + i;
	    }
	}
	return -1;
    }

    /**
     * The method corresponds to the corresponding method in the code file,
     * and indeed an invocation simply delegates the task to the method of
     * the code file.
     * @param name of the global variable
     * @return index of the global variable
     */    
    public int globalVariableNumber(String name) {
	return this.codeFile.globalVariableNumber(name);
    }
    /**
     * The method corresponds to the corresponding method in the code file,
     * and indeed an invocation simply delegates the task to the method of
     * the code file.
     * @param name of the procedure.
     * @return index of the procedure.
     */    
    
    public int procedureNumber(String name) {
	return this.codeFile.procedureNumber(name);
    }
    /**
     * The method corresponds to the corresponding method in the code file,
     * and indeed an invocation simply delegates the task to the method of
     * the code file.
     * @param name of the struct.
     * @return index of the struct.
     */    
    
    public int structNumber(String name) {
	return this.codeFile.structNumber(name);
    }

    /**
     * The method corresponds to the corresponding method in the code file,
     * and indeed an invocation simply delegates the task to the method of
     * the code file.
     * @param structName name of the struct.
     * @param varName name of the field
     * @return index of the field.
     */    
    
    public int fieldNumber(String structName, String varName) {
	return this.codeFile.fieldNumber(structName, varName);
    }


    /**
     * A simple get-ter method for the name of the procedure.
     * @return name of the procedure
     */
    public String getName() {
	return name;
    }


    /**
     * Auxiliary method used by getBytecode. 
     */
    
    private void moveJmps() {
	List<Instruction> newInstructions = new ArrayList<Instruction>();
	for(int i=0; i<this.instructions.size(); i++){
	    Instruction instruction = this.instructions.get(i);
	    if(instruction instanceof JMP){
		newInstructions.add(i, new JMP(findSize(((JMP)instruction).getJumpTo())));
	    } else if(instruction instanceof JMPFALSE){
		newInstructions.add(i, new JMPFALSE(findSize(((JMPFALSE)instruction).getJumpTo())));
	    } else if(instruction instanceof JMPTRUE){
		newInstructions.add(i, new JMPTRUE(findSize(((JMPTRUE)instruction).getJumpTo())));
	    } else {
		newInstructions.add(instruction);
	    }
	}
	this.instructions = newInstructions;
    }
    
    private int findSize(int num) {
	int pos = 0;
	for(int i=0; i<num; i++){
	    pos += this.instructions.get(i).size();
	}
	return pos;
    }
    /**
     * Extract the actual bytecode as an array of bytes.
     * @return the byte code of the procedure.
     */
    public byte[] getBytecode() {
	
	moveJmps();
	
	int totalSize = 0;
	byte[][] parameterTypesBytes = new byte[this.parameterTypes.size()][];
	for(int i=0; i<this.parameterTypes.size(); i++){
	    parameterTypesBytes[i] = this.parameterTypes.get(i).getBytecode();  
	    totalSize += parameterTypesBytes[i].length;
	}
	byte[][] variableTypesBytes = new byte[this.variableTypes.size()][];
	for(int i=0; i<this.variableTypes.size(); i++){
	    variableTypesBytes[i] = this.variableTypes.get(i).getBytecode();
	    totalSize += variableTypesBytes[i].length;
	}
	byte[][] instructionsBytes = new byte[this.instructions.size()][];
	for(int i=0; i<this.instructions.size(); i++){
	    instructionsBytes[i] = this.instructions.get(i).getBytecode();
	    totalSize += instructionsBytes[i].length;
	}

	// header information: 4 shorts
 	// Add size of name (2) counters (3*2) => 8
	totalSize += 8;
	byte[] nameBytes = this.name.getBytes();
	totalSize+=nameBytes.length;
	byte[] typeBytes = this.returnType.getBytecode();
	totalSize+=typeBytes.length;
        byte[] bytes = new byte[totalSize];
        NumberConversion.shortToByteArray(bytes,  0, (short) nameBytes.length);
        NumberConversion.shortToByteArray(bytes,  2, (short) this.parameterTypes.size());
	NumberConversion.shortToByteArray(bytes,  4, (short) this.variableTypes.size());
	NumberConversion.shortToByteArray(bytes,  6, (short) this.instructions.size());
	
        int index = 8;
        insert(bytes, nameBytes, index);
	index+=nameBytes.length;
	insert(bytes, typeBytes, index);   // not referenced in the header (not needed)
	index+=typeBytes.length; 
	
		
        // Parameters
        // Only the values
        for(int i=0;i<parameterTypesBytes.length;i++){
	    insert(bytes, parameterTypesBytes[i], index);
	    index+=parameterTypesBytes[i].length;
        }
        // Variables
        // Only the values
        for(int i=0;i<variableTypesBytes.length;i++){
	    insert(bytes, variableTypesBytes[i], index);
	    index+=variableTypesBytes[i].length;
        }
        // Instructions
        // Only the values
        for(int i=0;i<instructionsBytes.length;i++){
	    insert(bytes, instructionsBytes[i], index);
	    index+=instructionsBytes[i].length;
        }
	
	return bytes;
    }
    
    private void insert(byte[] bytes, byte[] insert, int index) {
	for(int i=0;i<insert.length;i++){
	    bytes[index + i] = insert[i];
	}
    }
}
