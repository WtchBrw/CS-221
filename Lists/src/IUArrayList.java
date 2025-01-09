import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;


/**
 * 
 * @author
 *
 * Its an array-based useage of the IndexedUnsortedList
 * 
 * This is just all the gunk necessary to get the ArrayList functionality
 * of the ListTester to work.
 * 
 * @param <T> type of data stored in the list
 */

public class IUArrayList<T> implements IndexedUnsortedList<T> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private T[] array;
	private int rear;
	private int modCount;
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param arrayListSize
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int arrayListSize) {
		array = (T[])(new Object[arrayListSize]);
		rear = 0;
		modCount = 0;
	}
	
	/** Double the capacity of array */
	private void expandCapacity() {
		array = Arrays.copyOf(array, array.length*2);
	}

	@Override
	public void addToFront(T element) {
		if (isEmpty()) {
			array[0] = element;
		} else {
			if(rear >= array.length){
				expandCapacity();
			}
			for(int i = rear; i > 0; i--){
				array[i] = array[i-1];
			}
			array[0] = element;
		}
		rear++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		add(element);
	}

	@Override
	public void add(T element) {
		if(rear >= array.length){
			expandCapacity();
		}
		array[rear] = element;
		rear++;
		modCount++;		
	}

	@Override
	public void addAfter(T element, T target) {
		for (int i = 0; i < rear; i++) {
			if (array[i].equals(target)) {
				add(i+1, element);
				return;
			}
		}
		throw new NoSuchElementException();	
	}

	@Override
	public void add(int index, T element) {
		if(index>rear) {
			throw new IndexOutOfBoundsException();
		}
		if(rear >= array.length){
			expandCapacity();
		}
		for(int i = rear; i > index; i--){
			array[i] = array[i-1];
		}
		array[index] = element;
		rear++;
		modCount++;		
	}

	@Override
	public T removeFirst() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		T val = array[0];
		for(int i = 0; i < rear-1; i++){
			array[i] = array[i+1];
		}
		rear--;
		modCount++;
		return val;
	}

	@Override
	public T removeLast() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}		
		T retVal = array[rear - 1];
		rear--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		//shift elements
		for (int i = index; i < rear-1; i++) {
			array[i] = array[i+1];
		}
		rear--;
		modCount++;
		return element;
	}

	@Override
	public T remove(int index) {
		if (index<0 || index>=rear) {
			throw new IndexOutOfBoundsException();
		}
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		T val = array[index];
		for(int i = index; i < rear-1; i++){
			array[i] = array[i+1];
		}
		rear--;
		modCount++;
		return val;
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index >= rear){
			throw new IndexOutOfBoundsException();
		}
		array[index] = element;
		modCount++;		
	}

	@Override
	public T get(int index) {
		if(index < 0 || index >= rear){
			throw new IndexOutOfBoundsException();
		}
		T value = array[index];
		return value;
	}

	@Override
	public int indexOf(T element) {
		int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		return index;
	}

	@Override
	public T first() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		T value = array[0];
		return value;
	}

	@Override
	public T last() {
		if(isEmpty()){
			throw new NoSuchElementException();
		}
		T value = array[rear-1];
		return value;
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) != NOT_FOUND);
	}

	@Override
	public boolean isEmpty() {
		return rear==0;
	}

	@Override
	public int size() {
		return rear;
	}
	
	public void sort() {
		// create a new, temp list of same size
		Integer[] sortArray;
		try {
			sortArray = (Integer[])array;
		}
		catch (Exception e) {
			System.out.println("The Sort method only work on Integers.");
			return;
		}
		
		int temp;

		for (int i=0;i<sortArray.length;i++) {
			for (int j=0;j<sortArray.length;i++) {
				if (sortArray[i]>sortArray[j]) {
					// switches values of i and j
					temp=sortArray[i];
					sortArray[i]=sortArray[j];
					sortArray[j]=temp;
				}
			}
		}
		array = (T[])sortArray;
	}
		
	
	public void reverseSort() {
		// create a new, temp list of same size
		Integer[] sortArray;
		try {
			sortArray = (Integer[])array;
		}
		catch (Exception e) {
			System.out.println("The Reverse Sort method only work on Integers.");
			return;
		}
		
		int temp;

		for (int i=0;i<sortArray.length;i++) {
			for (int j=0;j<sortArray.length;i++) {
				if (sortArray[i]<sortArray[j]) {
					// switches values of i and j
					temp=sortArray[i];
					sortArray[i]=sortArray[j];
					sortArray[j]=temp;
				}
			}
		}
		array = (T[])sortArray;
	}
	
	@Override
	public String toString() {
		String retVal = "[";
		for (int i = 0; i < rear - 1; i++) {
			retVal += "" + array[i] + ",";
		}
		if (rear != 0) {
			retVal += "" + array[rear-1];
		}
		retVal += "]";
		return retVal;
	}

	@Override
	public Iterator<T> iterator() {
		return new arrayListIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
	

	/** This thingy is all just an iterator for IUArrayList */
	private class arrayListIterator implements Iterator<T> {
		private int nextIndex;
		private int iteratorModulusCounter;
		
		private boolean next;
		private boolean remove;
		
		public arrayListIterator() {
			nextIndex = 0;
			iteratorModulusCounter = modCount;
		}

		@Override
		public boolean hasNext() {
			boolean value = false;
			if(iteratorModulusCounter != modCount){
				throw new ConcurrentModificationException();
			}
			if(nextIndex < rear){
				value = true;
			}
			return value;
		}

		@Override
		public T next() {
			T value;
			if(iteratorModulusCounter != modCount){
				throw new ConcurrentModificationException();
			}
			if(nextIndex < rear){
				value = array[nextIndex];
				nextIndex++;
			}else{
				throw new NoSuchElementException();
			}
			next = true;
			remove = false;
			return value;
		}
		
		@Override
		public void remove() {
			if(iteratorModulusCounter != modCount){
				throw new ConcurrentModificationException();
			}
			if(next = false || remove == true){
				throw new IllegalStateException();
			}
			if(nextIndex > rear || nextIndex <= 0){
				throw new IllegalStateException();
			}
			for(int i = nextIndex - 1; i < rear; i++){
				array[i] = array[i+1];
			}
			
			//this is quite possibly my least faforite looking pice of code in this entire project lol
			rear--;
			nextIndex--;
			modCount++;
			iteratorModulusCounter++;
			next = false;
			remove = true;
		}
		
	}
}
