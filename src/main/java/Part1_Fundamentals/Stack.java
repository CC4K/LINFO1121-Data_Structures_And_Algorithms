package Part1_Fundamentals;

import java.util.EmptyStackException;

/**
 * Author: Pierre Schaus
 * <p>
 * You have to implement the interface using
 * <p>
 * - a simple linkedList as internal structure
 * <p>
 * - a growing array as internal structure
 */
public interface Stack<E> {

    /**
     * Looks at the object at the top of this stack
     * without removing it from the stack
     */
    public boolean empty();

    /**
     * Returns the first element of the stack, without removing it from the stack
     *
     * @throws EmptyStackException if the stack is empty
     */
    public E peek() throws EmptyStackException;

    /**
     * Remove the first element of the stack and returns it
     *
     * @throws EmptyStackException if the stack is empty
     */
    public E pop() throws EmptyStackException;

    /**
     * Adds an element to the stack
     *
     * @param item the item to add
     */
    public void push(E item);

}

/**
 * Implement the Stack interface above using a simple linked list.
 * You should have at least one constructor without argument.
 * You are not allowed to use classes from java.util
 * @param <E>
 */
class LinkedStack<E> implements Stack<E> {

    private Node<E> top;    // the node on the top of the stack
    private int size;       // size of the stack

    // helper linked list class
    private static class Node<E> {
        private final E item;
        private final Node<E> next;

        public Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }

    @Override
    public boolean empty() {
        // TODO Implement empty method
        return top == null;
    }

    @Override
    public E peek() throws EmptyStackException {
        // TODO Implement peek method
        if (empty()) throw new EmptyStackException();
        return top.item;
    }

    @Override
    public E pop() throws EmptyStackException {
        // TODO Implement pop method
        if (empty()) throw new EmptyStackException();
        // [top]->[newTop]->[]->[low]
        E ret = top.item;
        top = top.next;
        size--;
        return ret;
    }

    @Override
    public void push(E item) {
        // TODO Implement push method
        // [(new)top]->[oldTop]->[]->[low]
        Node<E> oldTop = top;
        top = new Node<>(item, oldTop);
        size++;
    }

    public static void main(String[] args) {
        Stack<Integer> nodeStack = new LinkedStack<>();
        System.out.println(nodeStack.empty());
        nodeStack.push(1);
        nodeStack.push(2);
        nodeStack.push(3);
        System.out.println(nodeStack.empty());
        System.out.println(nodeStack.peek());
        System.out.println(nodeStack.pop());
        System.out.println(nodeStack.peek());
    }
}


/**
 * Implement the Stack interface above using an array as internal representation
 * The capacity of the array should double when the number of elements exceed its length.
 * You should have at least one constructor without argument.
 * You are not allowed to use classes from java.util
 * @param <E>
 */
class ArrayStack<E> implements Stack<E> {

    private E[] array;      // array storing the elements on the stack
    private int size;       // size of the stack

    public ArrayStack() {
        array = (E[]) new Object[10];
    }

    @Override
    public boolean empty() {
        // TODO Implement empty method
        return size == 0;
    }

    @Override
    public E peek() throws EmptyStackException {
        // TODO Implement peek method
        if (empty()) throw new EmptyStackException();
        return array[size-1];
    }

    @Override
    public E pop() throws EmptyStackException {
        // TODO Implement pop method
        if (empty()) throw new EmptyStackException();
        E ret = array[size-1];
        array[size] = null;
        size--;
        return ret;
    }

    @Override
    public void push(E item) {
        // TODO Implement push method
        if (size == array.length) {
            // double size and copy
            E[] newArray = (E[]) new Object[array.length*2];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size++] = item;
    }

    public static void main(String[] args) {
        Stack<Integer> arrayStack = new ArrayStack<>();
        System.out.println(arrayStack.empty());
        arrayStack.push(1);
        arrayStack.push(2);
        arrayStack.push(3);
        System.out.println(arrayStack.empty());
        System.out.println(arrayStack.peek());
        System.out.println(arrayStack.pop());
        System.out.println(arrayStack.peek());
        arrayStack.push(3);
        arrayStack.push(4);
        arrayStack.push(5);
        arrayStack.push(6);
        arrayStack.push(7);
        arrayStack.push(8);
        arrayStack.push(9);
        arrayStack.push(10);
        arrayStack.push(11);
        arrayStack.push(12);
    }
}
