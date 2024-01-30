/** 
 * A class to keep track of the relationship between StrieNode's and their children,
 * by storing this StrieNode's Character and the other StrieNodes it is connected to in a HashMap.
 * Used to construct a Strie.  
 * @author btate4
**/
public class StrieNode{

	
	/**A HashMap that stores Key-Value pairings of Characters and child StrieNodes for this class.**/
	private HashMap<Character, StrieNode> children; 

	/**Marks the end of a word.**/
	private boolean endMarker;  
	
	/**Can used as an optional marker for dummy Node deletion.**/
	private boolean flag;  	
	
	/**Default capacity of the HashMap.**/
	private static final int INIT_MAP_LENGTH = 5; 

	/**number of children for this StrieNode.**/
	private int numChildren; 

	/** 
	* Constructor to initialize a StrieNode.
	* This classes' HashMap is initialized with a default capacity of 5.
	* O(1)
	**/
	public StrieNode(){

		this.children = new HashMap<Character, StrieNode>(INIT_MAP_LENGTH);
		this.endMarker = false;
		this.flag = false;
		this.numChildren = 0;
	}

	/** 
	* Retrieves the number of children for this StrieNode.
	* O(1)
	* @return The number of child StrieNodes for this StrieNode.
	**/
	public int getNumChildren() {return this.numChildren;}


	/** 
	* Retrieves a HashMap containing the all of children for this StrieNode.
	* O(1)
	* @return A HashMap of this StrieNode's children.
	**/
	public HashMap<Character, StrieNode> getAllChildren() {return this.children;}


	/**
	* Sets the marker for this StrieNode to be true, signifying the end of a word.
	* O(1) 
	**/
	public void setEnd() {this.endMarker = true;}
	

	/**
	* Sets the endMarker for this StrieNode to be false, indicating that it is no longer the end of a word.
	* O(1) 
	**/
	public void unsetEnd() {this.endMarker = false;}
	

	/** 
	* Determines whether this StrieNode marks the end of a given word.
 	* O(1)
	* @return true if this StrieNode is the end of a word, false otherwise. 
	**/
	public boolean isEnd() {return this.endMarker == true;}
	
	/**
	* Determines whether or not one of this StrieNode's children
	* holds the provided character as a data portion.
	* O(1)
	* @param ch A character the user is searching for in the Strie.
	* @return True if ch is contained in one of this StrieNode's children, false otherwise.
	**/
	public boolean containsChild(char ch) {return children.contains(ch);}


	/**
	* Retrieves the child node associated with the provided character.
	* O(1)  
	* @param ch A character contained within a StrieNode.
	* @return The child StrieNode that contains the provided character as a data portion or null if no such Node exists.   
	**/
	public StrieNode getChild(char ch) {return children.getValue(ch);}

	/**
	* Checks to see if the provided character is in the HashMap.
	* If it is not, a pair containing the character and the StrieNode is added to the HashMap.
	* If the provided character is already in the HashMap, that character is re-mapped to reference new node.  
	* O(1)
	* @param ch A character to be added to the HashMap or re-mapped to a new StrieNode.
	* @param node The StrieNode that ch is being mapped to. 
	**/
	public void putChild(char ch, StrieNode node){
	
		if(children.contains(ch)) {/*if ch is in this HashMap*/ 

			children.update(ch, node);	/*change ch to map to the provided Node*/			
			return;
		
		}

		children.add(ch, node); /*add the <ch, node> pair to this HashMap.*/ 		
		this.numChildren++; /*increment this StrieNode's number of children*/
	}

	/** 
	* Removes the child StrieNode associated with the specified character.
	* Returns true if a corresponding Node was found, false otherwise.
	* O(1)
	* @param ch A character Key.
	* @return true if the child StrieNode could be removed, false otherwise.
	**/
	public boolean removeChild(char ch) {
		
		this.numChildren--; /*decrement number of children*/
		return children.remove(ch);	
	}


	/** 
	* Mutator method that sets this StrieNode's flag to be true.
	* O(1)
	**/
	public void setFlag() {this.flag = true;}


	/** 
	* Mutator method that sets this StrieNode's flag to be false.
	* O(1)
	**/
	public void unSetFlag() {this.flag = false;}
	

	/** 
	* Accessor method to retrieve the flag value for this StrieNode.
	* O(1)
	* @return True if this Node is flagged and false if it is not.
	**/
	public boolean checkFlag() {return this.flag;}
	
}