import java.util.Iterator;
import java.util.NoSuchElementException;

/** 
 * A singly-linked list of Nodes of generic type T.
 * Implements the Iterator interface to override the 'iterate' method.
 * Provides methods for removing and adding Nodes to a singly-linked List.
 * Also contains a get() method to determine whtether given value is in the list.
 * @author btate4
 * @param <T> a generic Object
**/

class SimpleList<T> implements Iterable<T> {
	
	
	/** 
	 * Allows user to create Nodes containing an generic
	 * object as a value portion.  Nodes only have a 'next' link
	 * to other Nodes since this is a singly-linked list.
	 * @param <T> An Object of a generic type
	**/
	private class Node<T> {
		
		/**A generic data portion held by this Node.**/
		T value;
		
		/**A reference to the next node in this list.**/
		Node<T> next;	
		
		/**
		 * Constructor method to initialize a Node with the provided value.
		 * @param value An Object of a generic type. 
		**/
		public Node(T value){
			this.value = value;
		}
	}
	
	/** First node, not dummy. **/
	private Node<T> head;  	

	/** Last node, not dummy. **/
	private Node<T> tail;

	/** The number of Nodes in this list. **/
	private int size;


	/** 
	 * Constructor to initialize an empty list of Nodes.
	 * O(1)
	**/
	public SimpleList(){}
	

	/** 
	 * Accessor method that allows others classes to retrieve the number of Nodes in this list.
	 * O(1)
	 * @return The number of Nodes in this list 
	 * 
	**/
	public int size(){return this.size;}


	/** 
	 * Appends a Node containing the provided value to the end of this linked list.
	 * O(1)
	 * @param value The Object the last Node in this list will contain.
	 * @throws IllegalArgumentException If the user tries to add a null element
	**/
	public void addLast(T value){

		if(value == null) 
			throw new IllegalArgumentException("Cannot add null value!");

		Node<T> newNode = new Node<T>(value);

		if(this.size == 0)
			this.head = newNode; /*Size is 0 --> set head and tail equal to the new Node*/
		else 
			this.tail.next = newNode; /*Otherwise, add new Node to the end and have tail reference the new Node*/
			
		this.tail = newNode;		
		this.size++; /*Increment size*/
	}
	

	/** 
	 * Handles deletions from the front of this linked list (simulates Queue).
	 * O(1)
	 * @return The value held by the Node that was deleted (Node at front)
	**/
	public T removeFirst(){
		
		if(this.size == 0)
			return null; /*If the size of this list is 0, return null*/

		T deletedValue = head.value; /*Store the value being deleted into a variable*/
		this.head = head.next; /*Have head reference its successor Node*/

		if(size == 1)
			this.tail = this.head; /*If size is one, we want head and tail to be set to null (head is already null since it was set to predecessor)*/

		this.size--; /*Decrement size*/

		return deletedValue;

	}

	/** 
	 * Removes the first Node containing the provided value from the list.
	 * O(N) where N is the number of nodes in the list
	 * @param value The value to be removed from this list.
	 * @return True if removal was successful, false otherwise.
	**/	
	public boolean remove(T value){
		
		if(value == null || this.size() == 0)
			return false; /*Can't remove a null Node or Node from an empty List!*/

		else if(value.equals(this.head.value)){ /*If value being removed is at the head of this list, call removeFirst()*/
			removeFirst(); 
			return true;
		}
			
		Node<T> current = this.head; /*Create a copy of head to traverse the list*/

		while(current.next != null) { /*Iterate through the list (starting at the second first Node cause we already checked head)*/
	
			if(current.next.value.equals(value)){ /*If we find the value, remove the Node containing it*/
					
				if(current.next.equals(this.tail)) /*If Node being removed is the tail --> tail must now reference the second-to-last Node in the list*/
					this.tail = current;
						
				current.next = current.next.next; /*De-link Node that is being deleted*/
				this.size--; /*Decrement size*/
				return true; /*Successful removal*/
			}
			current = current.next;	
		}		
			
		return false; /*return false if a Node containing the value was not found*/
	}
	

	/** 
	 * Searches this list for a given value and returns it.
	 * Null is returned if the value is not found.
	 * O(N) where N is the number of Nodes in this list.
	 * @param value The value to be retrieved from this list.
	 * @return The provided value if it was found or null if the provided value is null or wasn't found in the list
	**/
	public T get(T value){
		
		if(value == null) /*Return null for null value*/
			return null;
		else if(value == this.head.value) /*Return true if value is equal to value at the head of this list*/
			return value;
			
		Node<T> currentNode = this.head;

		while(currentNode.next != null) {

			if(currentNode.next.value == value) /*Iterate through list, checking if the current Node contains the value*/
				return value; 

			currentNode = currentNode.next;	
		}
	
		return null; /*Value was not found*/
	}


	/** 
	 * A basic iterator that allows the user to quickly test and traverse this singly-linked list.
	 * @return An Iterator class to traverse over this SimpleList.
	**/
	public Iterator<T> iterator(){
		// return a basic iterator of T
		// Note that this method uses your linked list!
		// so if the iterator doesn't work, that's on you...
		return new Iterator<>(){
			private Node<T> current = head;
			
			/** 
			* Checks if there is a non-null object after the current Node.
			* @return true if the current Node has a Node after it or false otherwise
			**/
			public boolean hasNext(){			
				return (current!=null);
			}
			
			/** 
			* Retrieves the successor Node for the current Node in this singly-linked list.
			* @return The next Node in this singly-linked list.
			* @throws NoSuchElementException if the next Node is null. 
			**/
			public T next(){
				if(!hasNext()) {
					throw new NoSuchElementException();
				}
				T toReturn = current.value;
				current = current.next;
				return toReturn;
			}
		};
	}
	

	/** 
	 * Produces a string representation of the values held by Nodes in this
	 * singly-linked list, beginning at the head value and ending at the tail value.
	 * @return A string of all the object held by this singly-linked list.
	**/
	@Override
	public String toString(){
		// list all values from head to tail
		StringBuilder s = new StringBuilder("[");
		Node<T> current = head;
		String prefix="";
		while (current!=null){
			s.append(prefix);
			s.append(current.value);
			prefix=",";
			current = current.next;
		}
		s.append("]");
		return s.toString();

	}
	
}