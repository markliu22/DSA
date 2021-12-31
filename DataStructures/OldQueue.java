// Queue is usually implemented using array or linked list. 
// This is the linked list implementation from Cracking the Coding Interview

package OldDataStructures;
// Queue class
// A queue can also be implemented with a linked list. Essentially they are the same thing, as long as items are added and removed fro opposite sides
public class Queue {

  // Inner node class
  private static class Node {
    private int data;
    private Node next;
    // Constructor
    private Node(int data) {
      this.data = data;
    }
  }

  // Unlike a stack which just needs a top, a queue needs a head and a tail
  private Node head; // FIFO so remove from the head
  private Node tail; // FIFO so add things to the tail

  // Is empty function, return boolean
  public boolean isEmpty() {
    // If head is null, the queue is empty, otherwise it's not
    return head == null;
  }

  // Peek function, returns int
  public int peek() {
    return head.data;
  }

  // Unlike stack which has push and pop, queue has add and remove
  // Add function (ADDS TO THE TAIL), returns nothing, takes data as parameter
  public void add(int data) {
    // First actually create the new node
    Node n = new Node(data);
    if(tail != null) {
      // Set tail to point to this new node
      tail.next = n;
    }
    // Set this new node as the new tail
    tail = n;

    // Case where the queue is completely empty:
    if(head == null) {
      // Set head to this new node
      head = n;
    }
  }

  // Remove function(REMOVES FROM THE HEAD), retuns int <<(the head's data)
  public int remove() {
    // Save the data of the head in the variable data
    int data = head.data;
    // Set head to it's next node
    head = head.next;
    // CHECK IF HEAD IS NOW NULL:
    if(head == null) {
      tail = null;
    }
    // Return old head's data
    return data;
  }

}
