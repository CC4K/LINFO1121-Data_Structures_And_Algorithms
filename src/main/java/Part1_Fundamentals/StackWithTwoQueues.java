package Part1_Fundamentals;

import java.util.ArrayDeque;
import java.util.EmptyStackException;
import java.util.Queue;

/**
 * Author: Pierre Schaus and Auguste Burlats
 * Implement the abstract data type stack using two queues
 * You are not allowed to modify or add the instance variables,
 * only the body of the methods
 */
public class StackWithTwoQueues<E> {

    Queue<E> queue1;
    Queue<E> queue2;

    /**
     * use queue1 normally for peeking and popping, use queue2 to invert the queue for pushing
     * -> 1 2 3 ->
     * osef de queue2 ici
     * push(4)
     * -> 1 2 3 -> pop it all and push it all back in queue2
     * -> 4 -> (queue2.push(4))
     * -> 4 3 2 1 -> pop queue1 on queue2
     * swap queue1 and queue2
     * pop()
     * -> 1 2 3 -> (queue1.pop())
     * osef de queue2 ici
     */
    public StackWithTwoQueues() {
        queue1 = new ArrayDeque<>();
        queue2 = new ArrayDeque<>();
    }

    /**
     * Looks at the object at the top of this stack
     * without removing it from the stack
     */
    public boolean empty() {
        // just use queue1
        return queue1.isEmpty();
    }

    /**
     * Returns the first element of the stack, without removing it from the stack
     *
     * @throws EmptyStackException if the stack is empty
     */
    public E peek() throws EmptyStackException {
        // just use queue1
        if (queue1.isEmpty()) throw new EmptyStackException();
        return queue1.peek();
    }

    /**
     * Remove the first element of the stack and returns it
     *
     * @throws EmptyStackException if the stack is empty
     */
    public E pop() throws EmptyStackException {
        // just use queue1
        if (queue1.isEmpty()) throw new EmptyStackException();
        return queue1.poll();
    }

    /**
     * Adds an element to the stack
     *
     * @param item the item to add
     */
    public void push(E item) {
        // that's where it gets ugly
        // add item to empty queue2
        queue2.add(item);
        // empty queue1 into queue2
        while (!queue1.isEmpty()) {
            E elem = queue1.poll();
            queue2.add(elem);
        }
        // swap queue1 and queue2 so that queue2 becomes new "stack" and queue1 empty queue
        Queue<E> temp = queue2;
        queue2 = queue1;
        queue1 = temp;

        /*
        System.out.print("queue1: ");
        for (E elem : queue1) {
            System.out.print(elem+"\t");
        }
        System.out.println();
        System.out.print("queue2: ");
        for (E elem : queue2) {
            System.out.print(elem+"\t");
        }
        System.out.println();
        */
    }

    public static void main(String[] args) {
        StackWithTwoQueues<Integer> stack = new StackWithTwoQueues<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.peek()); // 3
        System.out.println(stack.pop()); // 3
    }

}
