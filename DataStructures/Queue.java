public class Queue {

  private static class Node {
    private int data;
    private Node next;
    private Node(int data) {
      this.data = data;
    }
  }

  private Node head; 
  private Node tail;

  public boolean isEmpty() {
    return head == null;
  }

  public int peek() {
    return head.data;
  }

  public void add(int data) {
    Node n = new Node(data);
    if(tail != null) {
      tail.next = n;
    }
    tail = n;

    if(head == null) {
      head = n;
    }
  }

  public int remove() {
    int data = head.data;
    head = head.next;
    if(head == null) {
      tail = null;
    }
    return data;
  }
}
