// Stack is usually implemented using array or linked list. 
// This is the linked list implementation from Cracking the Coding Interview

public class Stack {

  private static class Node {
    private int data;
    private Node next;
    private Node(int data) {
      this.data = data;
    }
  }

  private Node top;

  public boolean isEmpty() {
    return top == null;
  }

  public int peek() {
    return top.data;
  }

  public void push(int data) {
    Node n = new Node(data);
    n.next = top;
    top = n;
  }

  public int pop() {
    int data = top.data;
    top = top.next;
    return data;
  }
}
