// ref: https://courses.cs.washington.edu/courses/cse373/13wi/lectures/01-23/
// Note: Generics are the closest thing Java has to C++ templates
// ref: https://stackoverflow.com/questions/2159338/what-is-the-java-equivalent-of-cs-templates
/*
Difference: 
In C++, what happens is that the compiler creates a compiled version of the template for all instances of the template used in code. 
In Java, the compiler creates only one compiled version of the code regardless of how many times it is used with different type parameters.
*/

// This interface defines a Stack abstract data type (ADT) allowing adding/removing items in FIFO from the "top" of the stack
// Recall abstract classes cannot be instantiated. We initialize an ArrayStack instead of a Stack

public interface Stack<E> {
	public void push(E value);
	public E pop();
	public E peek();
	public boolean isEmpty();
	public int size();
}
