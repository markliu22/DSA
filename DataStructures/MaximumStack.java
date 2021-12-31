class MaximumStack {
	Stack<Integer> stack;
	Stack<Integer> maxStack;

 	public MaxStack() {
      		stack = new Stack<>();
      		maxStack = new Stack<>();
  	}
  
  	// push WILL PUSH ONTO BOTH STACKS !!! stack & maxStack WILL ALWAYS HAVE THE SAME NUMBER OF ELEMENTS
  	public void push(int x) {
      		stack.push(x);
      		int max = maxStack.isEmpty() ? x : maxStack.peek();
      		maxStack.push(max > x ? max : x);
  	}
  
  	// pop WILL POP FROM BOTH STACKS !!! stack & maxStack WILL ALWAYS HAVE THE SAME NUMBER OF ELEMENTS
  	public int pop() {
      		maxStack.pop(); 
      		return stack.pop();
  	}
  
  	public int top() {
      		return stack.peek();
  	}
  
  	public int peekMax() {
      		return maxStack.peek();
  	}
  
  	public int popMax() {
      		int max = peekMax();
      		Stack<Integer> temp = new Stack<>();
      
      		// keep popping off from stack while we not at the max yet
      		// put the numbers from stack into temp
      		while(top() != max) {
          		temp.push(pop());
      		}
      
      		pop(); // at the max now, pop again
      
      		// add the numbers from stack INTO BOTH stack & maxStack
      		while(!temp.isEmpty()) {
          		push(temp.pop());
      		}
      
      		return max;
  	}
}
