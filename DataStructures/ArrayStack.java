// ref: https://courses.cs.washington.edu/courses/cse373/13wi/lectures/01-23/

// This class defines an implementation of the stack ADT using an array as the internal data structure

import java.util.Arrays;
import java.util.EmptyStackException;

public class ArrayStack<E> implements Stack<E> {
	// TODO: why not use ArrayList?
	private E[] elements;
	private int size;

	// Constructor
	public ArrayStack() {
		elements = (E[]) (new Object[5]);
		size = 0;
	}
	
	// Add element to top of stack
	public void push(E value) {
		// check if need to double internal array
		if(size == elements.length) {
			// copy into an array twice as large
			elements = Arrays.copyOf(elements, 2 * size);
		}
		elements[size] = value;
		size++;
	}

	// Removes and returns the top element of stack
	// Throws EmptyStackException if empty
	public E pop() {
		if(size == 0) {
			throw new EmptyStackException();
		}
		E top = elements[size - 1];
		elements[size - 1] = null;
		size--;
		return top;
	}

	// Returns the top element of this stack without removing it
	// Throws EmptyStackException if empty
	public E peek() {
		if(size == 0) {
			throw new EmptyStackException();
		}	
		return elements[size - 1];
	}

	// Returns true if stack is empty
	public boolean isEmpty() {
		return size == 0;
	}

	// Returns number elements in stack
	public int size() {
		return size;
	}
}

