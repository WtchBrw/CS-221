import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * Double-linked node implementation of IndexedUnsortedList.
 * 
 * @author
 * 
 * @param <T> type to store
 * 
 * I honestly think I should get sponsored by a coffee or tea company
 * or something with how much I drink while I chug away on this stuff. Anyways,
 * this took a while to get right but it finally functions as expected and
 * I'm proud of it. '~'
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	/**
	 * Adds a T element to the front of the list
	 * @param element to add to front of list
	 */
	@Override
	public void addToFront(T element) {
		Node<T> newNode = new Node<T>(element);
		if (isEmpty()) {
			head = newNode;
			tail = newNode;
		} else {
			newNode.setNext(head);
			head.setPrevious(newNode);
			head = newNode;
		}
		size++;
		modCount++;
	}

	/**
	 * Adds a T element to the end of the list by just "adding" it normally - calls other function
	 * @param element to add to end of list
	 */
	@Override
	public void addToRear(T element) {
		add(element);
	}

	/**
	 * Adds a T element to the end of a list
	 * @param element to add to end of list
	 */
	@Override
	public void add(T element) {
		Node<T> newNode = new Node<T>(element);
		if(isEmpty())
		{
			head = newNode;
			tail = newNode;
		}
		else {
			tail.setNext(newNode);
			newNode.setPrevious(tail);
			tail = newNode;
		}
		size++;
		modCount++;	
	}

	/**
	 * adds element after a specified target element
	 * @param element to be inserted
	 * @param element where the insertion needs to occur
	 * @throws NoSuchElementException
	 */
	@Override
	public void addAfter(T element, T target) {
		// locate target Node
		Node<T> targetNode = head;
		boolean found = false;
		while(targetNode != null && !found) {
			if (targetNode.getElement().equals(target)) {
				found = true;
			}
			else {
				targetNode = targetNode.getNext();
			}
		}
		// throw NoSuchElementException if not found
		if (!found) {
			throw new NoSuchElementException();
		}
		Node<T> newNode = new Node<T>(element);
		if(targetNode == tail)	{
			addToRear(element);
		}
		else if(targetNode == head)
		{
			add(1, element);
		}
		else
		{
			newNode.setPrevious(targetNode);
			newNode.setNext(targetNode.getNext());
			targetNode.getNext().setPrevious(newNode);
			targetNode.setNext(newNode);
			
			size++;
			modCount++;
		}
	}

	/**
	 * Adds an element after a specified index
	 * @param int index to add element in
	 * @param T element to add in specified index
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public void add(int index, T element) {
		Node<T> newNode = new Node<T>(element);
		if (index<0 || index>size) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			addToFront(element);
		} 
		else if (index == size)
		{
			tail.setNext(newNode);
			newNode.setPrevious(tail);
			tail = newNode;
			size++;
			modCount++;
		}
		else
		{
			Node<T> current = head;
			for(int i = 0; i < index; i++) {
				current = current.getNext();
			}
			Node<T> previous = current.getPrevious();
			previous.setNext(newNode);
			current.setPrevious(newNode);
			newNode.setNext(current);
			newNode.setPrevious(previous);	
			size++;
			modCount++;
		}
	}

	/**
	 * Removes first element in a list
	 * @throws NoSuchElementException
	 */
	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		if (head == tail) { 
			head = null;
			tail = null;
		} else { 
			head = head.getNext(); 
			head.setPrevious(null);
		}
		size--;
		modCount++;
		return retVal;
	}

	/**
	 * Removes last element in a list
	 * @throws NoSuchElementException
	 */
	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = null;
		if (size == 1) {
			retVal = head.getElement();
			head = null;
			tail = null;
		} 
		else {
			retVal = tail.getElement();
			tail = tail.getPrevious();
		}
		size--;
		modCount++;
		return retVal;
	}
	
	/**
	 * Removes specified element
	 * @param T element which is what will be removed
	 * @throws NoSuchElementException if element not found
	 */
	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		boolean found = false;
		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		Node<T> previous = current.getPrevious();
		Node<T> next = current.getNext();
		
		if (current == head) { //first node
			if (size() == 1) { //only node
				head = tail = null;
			}
			else {
				head = next;
				head.setPrevious(null);
			}
			
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(next);
			next.setPrevious(previous);
		}
		size--;
		modCount++;
		
		return current.getElement();
	}

	/**
	 * Removes element at a specified index
	 * @param index of element to be removed
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public T remove(int index) {
		Node<T> current = head;
		T retVal = null;
		if (index<0||index>=size) {
			throw new IndexOutOfBoundsException();
		}
		if (size == 0) {
			throw new IndexOutOfBoundsException();
		}
		if (index==0) {
			retVal = head.getElement();
			head = current.getNext();
		} 
		else {
			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}
			retVal = current.getElement(); 
			if (current == tail) {
				current.getPrevious().setNext(null); 
				tail = current.getPrevious();
			} else if (current == head) {
				current = current.getNext();
				current.setPrevious(null); 
				head = current;
			} else { 
				current.getPrevious().setNext(current.getNext());
				current.getNext().setPrevious(current.getPrevious());
			}
		}
		size--;
		modCount++;
		return retVal;
	}

	/**
	 * Changes element at a specified index to another element
	 * @param index to be altered
	 * @param element to be changed to
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public void set(int index, T element) {
		Node<T> current = head;
		if (index<0 || index>=size()) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			head.setElement(element);
		} 
		else {
			for (int i=0;i<index;i++) {
				current = current.getNext();
			}
			current.setElement(element);
		}
		modCount++;
	}

	/**
	 * Returns element at a specified index
	 * @param index of element to be found
	 * @return element at specified index
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public T get(int index) {
		Node<T> current = head;
		@SuppressWarnings("unused")
		T retVal = null;
		if (index<0 || index>=size()) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			retVal = head.getElement();
		} 
		else {
			for (int i=0;i<index;i++) {
				current = current.getNext();
			}
			retVal = current.getElement();
		}
		
		return current.getElement();		
	}

	/**
	 * Finds the index of a specified element
	 * @param element to be found
	 * @returns index of specified element, -1 if not found
	 */
	@Override
	public int indexOf(T element) {
		int index = -1;
		Node<T> current = head;
		int currentIndex = 0;
		while (current != null && index < 0) {
			if (current.getElement().equals(element)) {
				index = currentIndex;
			}
			else {
				current = current.getNext();
				currentIndex++;
			}
		}
		return index;
	}

	/**
	 * Returns first element in the list
	 * @returns T element first one in list
	 * @throws NoSuchElementException
	 */
	@Override
	public T first() {
		T retVal = null;
		if (isEmpty()) {
			throw new NoSuchElementException();
		} else {
			retVal = head.getElement();
		}
		return retVal;
	}

	/**
	 * Returns last element in the list
	 * @returns T element last one in list
	 * @throws NoSuchElementException
	 */
	@Override
	public T last() {
		T retVal = null;
		if (isEmpty()) {
			throw new NoSuchElementException();
		} else {
			retVal = tail.getElement();
		}
		return retVal;
	}
	
	/**
	 * Return String
	 */
	public String toString() {
		if (size == 0) {
			return "[]";
		}
		String retVal = "[";
		Node<T> current = head;
		while (current.getNext() != null) {
			retVal += "" + current.getElement() + ",";
			current = current.getNext();
		}
		retVal += current.getElement();
		retVal += "]";
		return retVal;
	}

	/**
	 * Determines if a list contains a specified target element
	 * @return boolean if element is in the list or not
	 */
	@Override
	public boolean contains(T target) {
		return indexOf(target) != -1;
	}

	/**
	 * Checks if list is empty
	 * @return boolean true if empty, false if not empty
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/** 
	 * Determines size of list
	 * @return size of list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Constructs new iterator
	 */
	@Override
	public Iterator<T> iterator() {
		return new DLLIterator();
	}

	/**
	 * Constructs new iterator
	 */
	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	/**
	 * Constructs new iterator based on starting index
	 */
	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}
	
	/** Iterator for IUDoubleLinkedList */
	private class DLLIterator implements ListIterator<T> {
		private Node<T> nextNode;
		private int iterModCount, nextIndex;
		private Node<T> last;
	
	/** Creates a new iterator for the list */
		public DLLIterator() {
			nextNode = head;
			iterModCount = modCount;
			nextIndex = 0;
			last = null;
		}
		
		/**
		 * Constructs a new iterator at index specified
		 * @param index - position to start at
		 */
		public DLLIterator(int index) {
			if (index < 0 || index > size) {
				throw new IndexOutOfBoundsException();
			}
			nextNode = head;
			for (int i = 0; i < index; i++) {
				nextNode = nextNode.getNext();
			}
			nextIndex = index;
			last = null;
			iterModCount = modCount;
		}
		
		/**
		 * Finds next index for list
		 * @return next index
		 * @throws ConcurrentModificationException
		 */
		public int nextIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex;
		}

		/**
		 * Finds previous index for list
		 * @return previous index
		 * @throws ConcurrentModificationException
		 */
		public int previousIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex - 1;
		}

		/**
		 * Returns next element in the list
		 * @return element T
		 * @throws NoSuchElementException if at end of list
		 * @throws ConcurrentModificationException
		 */
		@Override
		public T next() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			last = nextNode;
			nextNode = nextNode.getNext();
			nextIndex++;
			return last.getElement();
		}
		
		/**
		 * @return boolean of whether or not there is more elements in the list
		 * @throws ConcurrentModificationException
		 */
		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextNode != null;
		}
		
		/**
		 * @return previous element in list after a certain spot
		 * @throws ConcurrentModificationException
		 * @throws NoSuchElementException
		 */
		public T previous() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (nextNode == null) {
				nextNode = tail;
			} else {
				nextNode = nextNode.getPrevious();
			}
			last = nextNode;
			nextIndex--;
			return last.getElement();
		}
		
		/**
		 * boolean of whether or not there is an element before the one in the list
		 * @returns true if has previous, false if not
		 * @throws ConcurrentModificationException
		 */
		public boolean hasPrevious() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (nextNode == null) {
				return !isEmpty();
			}
			return nextNode.getPrevious() != null;
		}		
		
		/**
		 * Adds element to end of list
		 * @param element
		 * @throws ConcurrentModificationException
		 */
		public void add(T element) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			Node<T> newNode = new Node<T>(element);
			
			if (size == 0) {
				head = newNode;
				tail = newNode;
			} else if (nextIndex == size) {
				tail.setNext(newNode);
				newNode.setPrevious(tail);
				tail = newNode;
			} else if (nextIndex == 0) {
				head.setPrevious(newNode);
				newNode.setNext(head);
				head = newNode;
			} else {
				nextNode.getPrevious().setNext(newNode);
				newNode.setNext(nextNode);
				newNode.setPrevious(nextNode.getPrevious());
				nextNode.setPrevious(newNode);
			}
			iterModCount++;
			modCount++;
			size++;
			nextIndex++;
			last = null;
		}
		
		/**
		 * Removes element from current position
		 * @throws ConcurrentModificationException
		 * @throws IllegalStateException
		 */
		public void remove() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (last == null){
				throw new IllegalStateException();
			}
			if (size == 1) {
				head = tail = null;
				nextIndex = 0;
			}
			else if (last != nextNode) {
				if (last == head) {
					head = nextNode;
					head.setPrevious(null);
				} else if (last == tail) {
					tail = tail.getPrevious();
					tail.setNext(null);
				} else {
					last.getPrevious().setNext(nextNode);
					nextNode.setPrevious(last.getPrevious());
				}
				nextIndex--;
			}
			else {
				if (last == head) {
					head = nextNode.getNext();
					head.setPrevious(null);
				} else if (last == tail) {
					tail = tail.getPrevious();
					tail.setNext(null);
				} else {
					last.getPrevious().setNext(nextNode.getNext());
					nextNode.getNext().setPrevious(last.getPrevious());
				}
			}
			size--;
			modCount++;
			iterModCount++;	
			last = null;
		}
		
		/**
		 * Changes last returned element to new element T
		 * @param element
		 * @throws ConcurrentModificationException
		 * @throws IllegalStateException
		 */
		public void set(T element) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (last == null) {
				throw new IllegalStateException();
			}
			last.setElement(element);
			modCount++;
			iterModCount++;
		}
	}
}
