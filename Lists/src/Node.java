public class Node<T> 
{
	private Node<T> next;
	private Node<T> previous;
	private T element;
	
	/**
	 * @param element;
	 */
	public Node(T element)
	{
		setElement(element);
		setNext(null);
		setPrevious(null);
	}

	/**
	 * @return reference to the next code
	 */
	public Node<T> getNext()
	{
		return next;
	}
	
	/**
	 * @param next - reference to the next node
	 */
	public void setNext(Node<T> next) 
	{
		this.next = next;		
	}
	
	/**
	 * @return reference to the previous code
	 */
	public Node<T> getPrevious()
	{
		return previous;
		
	}
	/**
	 * @param previous - reference to the previous node
	 */
	public void setPrevious(Node<T> previous) {
		this.previous = previous;
	}

	/**
	 * @return reference to element stored in code
	 */
	public T getElement()
	{
		return element;
	}
	
	/**
	 * @param element - reference to node stored in node
	 */
	public void setElement(T element) 
	{
		this.element = element;
	}

}