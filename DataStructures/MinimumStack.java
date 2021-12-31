class MinimumStack {
	// Custom node class that stores node VAL & current MIN VAL
	class ListNode {
		int val;
		int min;
		ListNode next = null;
		public ListNode(int val, int min) {
			this.val = val;
		  	this.min = min;
	      	}
  	}

  	ListNode top;
  	public MinStack() {
      		top = null;
  	}
  
  	// O(1) Create new node with min = Min(curr's val, top's min)
  	public void push(int x) {
      		if(top == null) {
          		top = new ListNode(x, x);
      		} else {
          		ListNode curr = new ListNode(x, Math.min(x, top.min));
          		curr.next = top;
          		top = curr;
      		}
  	}
  
  	// O(1)
  	public void pop() {
      		ListNode curr = top;
      		top = top.next;
      		curr.next = null; // disconnect yourself
  	}
  
  	// O(1)
	// requires stack not empty
  	public int top() {
      		return top.val;
  	}
  
  	// O(1)
  	public int getMin() {
      		return top.min;
  	}
}

