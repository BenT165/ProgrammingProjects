/**
 * A tree of Strings.
 * Allows for insertion and removal of Strings.
 * Also provides a contains method to check if a given word is in the Strie.
 * There is a levelOrderTraversal method to represent this tree in level order
 * and a getStrieWords method to retrieve a SimpleList of the words in this
 * Strie.
 * 
 * @author btate4
 **/
public class Strie {

	/** The root of a strie. **/
	private StrieNode root;

	/** number of words represented by the strie. **/
	private int numWords = 0;

	/**
	 * Initializes this classes' fields. Root is set to be an empty Node.
	 * O(1)
	 **/
	public Strie() {
		this.root = new StrieNode();
	}

	/**
	 * Retrieves the number of words in this Strie.
	 * O(1)
	 * 
	 * @return The total number of different words in this Strie.
	 **/
	public int numWords() {
		return this.numWords;
	}

	/**
	 * Retrieves the root Node from this Strie.
	 * O(1)
	 * 
	 * @return Th root of this Strie.
	 **/
	public StrieNode getRoot() {
		return this.root;
	}

	/**
	 * Adds a word to this Strie.
	 * O(n) where n is the number of characters in word.
	 * 
	 * @param word The word that is being added to this Strie.
	 **/
	public void insert(String word) {

		if (word == null) /* don't add null entries */
			return;

		StrieNode current = this.root; /* start at root */

		for (int i = 0; i < word.length(); i++) { /* for each character in the word */

			if (!current.containsChild(word.charAt(i))) { /* if Strie doesn't contain current character in word */
				current.putChild(word.charAt(i), new StrieNode()); /*
																	 * create a new child for this level node and link
																	 * the current character to it
																	 */
			}

			current = current.getChild(word.charAt(i)); /* Go down to the next level */
		}

		current.setEnd(); /* mark the end of the word and increment number of words in the Strie */
		this.numWords++; /* Increment number of words in this Strie */
	}

	/**
	 * Determines if this Strie has the provided word in it.
	 * O(N) where n is the number of characters in word.
	 * 
	 * @param word The word the user is searching for in the Strie.
	 * @return True if the given word is in the tree and has an endMarker, false
	 *         other wise.
	 **/
	public boolean contains(String word) {

		if (word.length() == 0)
			return false; /* Return false for null arguments */

		StringBuilder myString = new StringBuilder(); /* create empty stringbuilder */
		StrieNode current = this.root; /* Start at the root */

		for (int i = 0; i < word.length(); i++) { /* Iterate through characters in word + 1 index (for endmarker) */

			if (!current.containsChild(word.charAt(i)))
				return false; /* if current StrieNode doesn't contain the current character, return false */

			myString.append(word.charAt(i)); /* otherwise, append current character to myString */
			current = current.getChild(word.charAt(i)); /* then update current to be current's child StrieNode */
		}

		if (!current.isEnd())
			return false; /* if word doesn't have an endmarker --> return false */

		return myString.toString().equals(word);
	}

	/**
	 * Removes the provided word from the Strie.
	 * If no letters from the word can be removed, only the endmarked is removed.
	 * 
	 * @param current The current StrieNode (our location in the Strie) being
	 *                processed for deletion.
	 * @param word    The word being deleted.
	 * @param index   current index of the word.
	 **/

	private void removeWord(StrieNode current, String word, int index) {

		/* BASE CASE word is done being processed when we have passed the last index */
		if (index == word.length()) {
			current.unsetEnd(); /* remove the endmarker */
			return;
		}

		/*
		 * recursive call with this StrieNodes child, the word being removed, and the
		 * index incremented
		 */
		removeWord(current.getChild(word.charAt(index)), word, index + 1);

		/*
		 * Process characters starting from the last character in the word and ending at
		 * the first character.
		 * If currentStrieNode's child has zero children and no endmarker --> remove it
		 * from the Strie.
		 */
		if (current.getChild(word.charAt(index)).getNumChildren() == 0 && !current.getChild(word.charAt(index)).isEnd())
			current.removeChild(word.charAt(index));
	}

	/**
	 * Check if the given word can be deleted, then calls recursive helper method to
	 * handle deletion.
	 * O(N) where N is the number of characters in the word.
	 *
	 * @param word The word being deleted from this Strie
	 * @return true if the word can be deleted from this Strie, false otherwise.
	 **/
	public boolean remove(String word) {

		if (!this.contains(word) || word.length() == 0) /* return false if Strie doesn't contain the given word */
			return false;

		/*
		 * call helper method on the given word, starting at the root, and beginning of
		 * word (so we account for root w/o out of bounds)
		 */
		removeWord(this.root, word, 0);

		this.numWords--; /* decrement number of words */

		return true;
	}

	/**
	 * Produces a String containing the characters in this Strie in level order.
	 * Enqueues the root of this Strie, then follows the following steps until the
	 * Queue is empty.
	 * Dequeues the StrieNode and enqueue all of its children.
	 * Append the character value of the current StrieNode to a return string.
	 * O(N) where N is the number of StrireNodes in this Strie.
	 *
	 * @return A level order String representation of this Strie.
	 **/
	public String levelOrderTraversal() {

		if (this.numWords == 0) {
			return "";
		} /* If tree is empty, return empty string */

		SimpleList<StrieNode> strieQueue = new SimpleList<>(); /*
																 * Queue to keeps track of order of StrieNodes that are
																 * being processed in this Strie
																 */
		StringBuilder levelOrderString = new StringBuilder(); /* Stringbuilder to create a return string */
		SimpleList<Character> keyQueue = new SimpleList<>(); /*
																 * Queue to keep track of order in which current
																 * StrieNode's children are being processed
																 */
		StrieNode current; /* StrieNode that is currently being operated on */

		strieQueue.addLast(this.root); /* add root to the end of the StireNodeQueue */

		/* while strieQueue is not empty (more nodes in Strie left to process) */
		while (strieQueue.size() != 0) {
			current = strieQueue.removeFirst(); /*
												 * dequeue first StrieNode in strieQueue and store it into a current
												 * variable
												 */
			keyQueue = current.getAllChildren().getKeys(); /*
															 * retrieve the Key values for all of current StrieNodes
															 * children
															 */

			/* for each of current StrieNodes children */
			for (Character element : keyQueue) {

				strieQueue.addLast(current.getChild(element)); /* enqueue current StrieNode's children left-to-right */
				levelOrderString.append(keyQueue.removeFirst() + " "); /*
																		 * add corresponding StrieNode characters to
																		 * return String left-to-right with space after
																		 */
			}

			// while(it.hasNext()) {
			// strieQueue.addLast(current.getChild(it.next()));
			//
			// }
		}

		return levelOrderString.toString().substring(0, levelOrderString.length() - 1); /*
																						 * return formatted string with
																						 * last space removed
																						 */
	}

	/**
	 * Helper method to add all words in this Strie to the designated SimpleList
	 * (wordsFound) by using recursion to perform a depth-first traversal.
	 * 
	 * @param current     The current StrieNode being processed (where we are in the
	 *                    Strie)
	 * @param wordsFound  A list of the words that have been found in this Strie so
	 *                    far.
	 * @param currentWord the string up until this currentNode + currentNode.next
	 *                    letter
	 **/
	public void getAllWords(StrieNode current, SimpleList<String> wordsFound, StringBuilder currentWord) {

		/* BASE CASE can't go down if there are no StrieNodes below */
		if (current.getNumChildren() == 0) {
			return;
		}

		SimpleList<Character> queueChildren = current.getAllChildren().getKeys();

		/* iterate through currentNode's children */
		for (Character element : queueChildren) {

			currentWord.append(element); /*
											 * append current character in iterator to currentWord (check ahead, we will
											 * have to remove it later)
											 */

			if (current.getChild(element).isEnd()) { /*
														 * if current StrieNode's child (for current iterator element)
														 * has an endmarker
														 */
				wordsFound.addLast(currentWord.toString()); /* add currentWord to list of words seen */
			}

			getAllWords(current.getChild(element), wordsFound, currentWord); /* recursive call */
			currentWord.deleteCharAt((currentWord.length() - 1)); /*
																	 * remove last character from the StringBuilder (so
																	 * currentWord is up-to-date)
																	 */
		}
	}

	/**
	 * Retrieves all of the words contained within this Strie.
	 * 
	 * @return A SimpleList representing all of the words that are currently stored
	 *         in Strie.
	 **/
	public SimpleList<String> getStrieWords() {

		// If Strie has no words, return null.
		if (this.numWords == 0)
			return null;

		SimpleList<String> strieWords = new SimpleList<String>(); /*
																	 * create an empty list of Strings to represent the
																	 * words in this Strie
																	 */

		/* pass root, empty list, and empty stringbuilder to helper */
		getAllWords(this.root, strieWords, new StringBuilder());

		return strieWords;

	}

}