import java.util.Iterator;


/**
* A Dynamic array that uses seperate chaining to store generic objects.
* Provides methods to add elements to this HashSet and methods 
* to retrieve and delete elements from this HashSet. 
* 
* @param <T> a generic Object held by this HashSet
* @author btate4
**/
class ThreeTenHashSet<T> {
	
	/** 
	* An array of generic objects used to represent the key value mappings for this hashSet.
	**/
	private SimpleList<T>[] table;


	/** 
	* The number of items that this hashSet contains.
	**/
	private int size;

	/** 
	* The number of items that this hashSet can hold.
	**/
	private int capacity;

	/** 
	* How full this table is (i.e. size/capacity)
	**/
	private float load;


	/**
	* Constructor method that intiailizes table to be a an array with a capacity of initLength.
	* Length of table is assumed to be greater than or equal to 2 
	* O(1)
	* @param initLength The initial length of this hash table.
	**/	
	@SuppressWarnings("unchecked")
	public ThreeTenHashSet(int initLength){
	
		this.table = (SimpleList<T>[]) new SimpleList[initLength]; 	/*initialize table with length of initLength */
		this.capacity = table.length; /*set capacity for this table*/			
	}

	/** 
	* Accessor method to get number of elements that this classes' table can hold.
	* O(1)
	* @return The length of this classes' table array.
	**/
	public int capacity() {return this.capacity;}

	/**
	* Accessor method to retrieve the number of elements stored in table. 
	* O(1)
	* @return The number of non-null items in the table 
	**/
	public int size() {return this.size;} 
	

	/** 
	* Inserts an item into an index of this hashTable corresponding to the item's hashCode.
	* If this table is full, its current members are rehashed into a larger array
	* to accomodate the new entry. 
	* are rehashed.
	* O(N) Amortized worst case where N is total number of values in the table
	* O(N/M) Amortized average case where N is the number of values in table and M is the table length
	* @param value The Object being added to this hashSet
	* @return true if the value was successfully added and false if it was not
	**/
	public boolean add(T value) {

		if(this.load >= 2.0)
			rehash(this.table.length * 2); /*If load of table is at or above 2.0, rehash() to double the length.*/

		int initialSize = this.size; /*keep track of the original size of the table*/
		
		/*have to make sure hashIndex is not negative,
		then we map the value to the appropriate hashIndex*/		
		int hashIndex  = Math.abs(value.hashCode()) % table.length;
		
		if(table[hashIndex] == null) /*If the hashIndex is null, create a SimpleList at that index*/
			table[hashIndex] = new SimpleList<T>();


		Iterator<T> it = table[hashIndex].iterator(); /*Create an iterator for the generated hashIndex*/

		while(it.hasNext()) { /*Go through SimpleList at hashIndex and check if it already contains the value being added*/

			if(it.next().equals(value))
				return false; /*if it does, return false and don't add*/
		}

		table[hashIndex].addLast(value); /*Append the value to the tail of the linked list at the generated hashIndex*/
		size++; /*Increment number of elements*/
		load =  ((float) size / capacity); /*Adjust load*/

		
		return this.size > initialSize; /*return whether or not the element was added*/
	}
	

	/** 
	 * Deletes a value from this classes' table.  Null values and values that are not in the table
	 * cannot be removed.
	 * O(N) worst case, where N is the number of values in table
	 * O(N/M) average case where N is the number of values in table and M is the table length
	 * @param value The value to be removed from the table.
	 * @return True if the size of the table decreased, false if the item is null or not in the table.
	**/
	public boolean remove(T value) {

		/*have to make sure hashIndex is not negative,
		then we map the value to the appropriate hashIndex*/	
		int hashIndex = Math.abs(value.hashCode()) % table.length;

		/*If value can be removed (SimpleList index is not empty and does not already contain the value)*/
		if(table[hashIndex] != null && table[hashIndex].remove(value)) { 
			size--; /*Decrement size*/
			
			if(table[hashIndex].size() == 0) /*delete the SimpleList at hashIndex if it no longer has any elements*/
				table[hashIndex] = null;

			load = ((float) size/capacity); /*Adjust load*/
			return true; 
		}

		return false; /*Value couldn't be removed*/
	}
	

	/** 
	 * Checks to see if a value is in this ThreeTenHashSet.
	 * O(N) worst case, where N is the number of values in table
	 * O(N/M) average case where N is the number of values in table and M is the table length
	 * @param value The value being searched for.
	 * @return True if the value is in this set, false if the value is not in this set.
	**/
	public boolean contains(T value) {
		
		if(value == null)
			return false; /*return false if the value is null*/

		int hashIndex  = Math.abs(value.hashCode()) % table.length; /*generate hashIndex for the value*/

		/*If there is a value in the SimpleList at the generated hashIndex*/
		if(table[hashIndex] != null) {
			Iterator<T> it = table[hashIndex].iterator(); /*create an iterator to traverse the SimpleList at table[hashIndex]*/
		
			/* Go through the list at hashIndex, checking if each Node contains the given value.
			If the value is found, return true. */
			while(it.hasNext()){
			
				if(it.next().equals(value)) 
					return true;
			}
		}

		return false; /*return false if value not found*/
	}


	/** 
	* Retrieves a value from this ThreeTenHashSet or null if the value is null or not in this ThreeTenHashSet.
	* O(N) worst case, where N is the number of values in table.
	* O(N/M) average case where N is the number of values in table and M is the table length.
	* @param value The data item to be retrieved
	* @return The provided value from this ThreeTenHashSet if it is present or null if the value is in not in this hash set.
	**/
	public T get(T value) {

		if(value == null)
			return null; /*return null if the value is null*/
		
		int hashIndex  = Math.abs(value.hashCode()) % table.length; /*generate hashIndex for the value*/	
		T current; /*current value in the table*/

		/*If there is a value in the SimpleList at the generated hashIndex*/
		if(table[hashIndex] != null) {
			Iterator<T> it = table[hashIndex].iterator(); /*create an iterator to traverse the SimpleList at table[hashIndex]*/
		
			/* Go through the list at hashIndex, checking if each Node contains the given value.
			If the value is found, return true. */
			while(it.hasNext()){
				
				current = it.next(); /*update current to be next Node in list*/
				if(current.equals(value)) /*check if current is equal to the value we are trying to retrieve*/
					return current;
			}
		}

		return null; /*return false if value not found*/	
	}
	

	/** 
	 * Checks if size is less than newCapacity. 
	 * If size is greater than or equal to newCapacity, return false.
	 * If newCapacity is greater than size, create a new array with length equal to newCapacity
	 * and copy all existing elements over from table.
	 * Assumes newCapacity is less than Integer.MAX_VALUE -50.
	 * O(N+M) where N is the number of values in table and M is the table size
	 * @param newCapacity The capacity that the user is setting for this table.
	 * @return True if the table needed to be rehashed, false otherwise
	**/
	@SuppressWarnings("unchecked")
	public boolean rehash(int newCapacity) {
	
		/*If the new capacity is no greater than the current number of values in this classes' table, do not rehash and return false;*/
		if(size >= newCapacity) 
			return false;
		
		SimpleList<T>[] newArray = (SimpleList<T>[]) new SimpleList[newCapacity]; /*create new array with length newCapacity*/				
			
		int hashIndex; /*index where objects in the current array will be placed in the new array*/
		T current; /*value held by the current Node in SimpleList*/
		Iterator<T> it; /*Iterator to traverse over each simple list in the current table*/
		
		for(int i = 0; i < this.table.length; i++) { /*go through current array*/

			if(table[i] != null){  /*If SimpleList at current index of current Array is not null, we want to copy its values to newArray */
				it = table[i].iterator(); /*assign a new iterator to 'it' if linked list at current index*/
				
				while(it.hasNext()){ /*go through SimpleList at index i*/

					current = it.next(); /*get the value held by the current Node*/
					hashIndex = Math.abs(current.hashCode()) % newCapacity; /*generate new hashCode for that value (used to map value to newArray)*/

					if(newArray[hashIndex] == null) /*If the hashIndex of the newArray is null, create a SimpleList at that index*/
						newArray[hashIndex] = new SimpleList<T>();
					
					newArray[hashIndex].addLast(current); /*append a Node containing current to the SimpleList at newArray[hashIndex]*/						
				}
			}
		}

		this.table = newArray; /*have current array reference new array*/
		this.capacity = newCapacity; /*update capacity for the rehashed table*/
		this.load = ((float) size / capacity); /*update load for the rehashed table*/

		return true; /*return true if table was able to be resized*/				
	}
	
	/**
	 * Produces a formatted String of all the Objects in this HashSet, excluding empty entries. 
	 * @return A String representation of this HashSet. 
	**/
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("ThreeTenHashSet (non-empty entries):\n");
		for(int i = 0; i < table.length; i++) {
			if(table[i] != null) {
				s.append(i);
				s.append(" :");
				s.append(table[i]);
				s.append("\n");
			}
		}
		return s.toString().trim();
	}
	
	/**
	 * Produces a formatted String of all the objects in this HashSet, inlcuding empty entries. 
	 * @return A String representation of this HashSet. 
	**/
	public String toStringDebug() {
		StringBuilder s = new StringBuilder("ThreeTenHashSet (all entries):\n");
		for(int i = 0; i < table.length; i++) {
			s.append(i);
			s.append(" :");
			s.append(table[i]);
			s.append("\n");
		}
		return s.toString().trim();
	}

	/**
	 * Retrieves a singly-linked-list of the objects in this HashSet.
	 * @return A SimpleList representation of this HashSet. 
	**/
	public SimpleList<T> allValues(){
		// return all items in set as a list
		SimpleList<T> all = new SimpleList<>();
		for(int i = 0; i < table.length; i++) {
			if (table[i]!=null){
				for (T value: table[i])
					all.addLast(value);
			}
		}
		return all;
	}
		
}