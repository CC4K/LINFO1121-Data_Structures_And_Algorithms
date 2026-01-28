package Part1_Fundamentals;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Author: Pierre Schaus
 *
 * Functional Programming is an increasingly important programming paradigm.
 * In this programming paradigm, data structures are immutable.
 * We are interested here in the implementation of an immutable list
 * called FList allowing to be used in a functional framework.
 * Look at the main method for a small example using this functional list
 *
 * Complete the implementation to pass the tests
 *
 * @param <A>
 */
public abstract class FList<A> implements Iterable<A> {

    public static void main(String[] args) {
        FList<Integer> list = FList.nil();

        for (int i = 0; i < 10; i++) {
            list = list.cons(i);
        }
        for (Integer i: list) {
            System.out.print(i+",");    // 9, 8, 7, 6, 5, 4, 3, 2, 1, 0
        }
        System.out.println("nil");

        list = list.map(i -> i+1);
        for (Integer i: list) {
            System.out.print(i+",");    // 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
        }
        System.out.println("nil");

        list = list.filter(i -> i%2 == 0);
        for (Integer i: list) {
            System.out.print(i+",");    // 10, 8, 6, 4, 2
        }
        System.out.println("nil");
    }

    // return true if the list is not empty, false otherwise
    public final boolean isNotEmpty() {
        return (this instanceof Cons);
    }

    // return true if the list is empty (Nill), false otherwise
    public final boolean isEmpty() {
        return (this instanceof Nil);
    }

    // return the length of the list
    public final int length() {
        // TODO
        if (isEmpty()) return 0;
        return 1 + tail().length();
    }

    // return the head element of the list
    public abstract A head();

    // return the tail of the list
    public abstract FList<A> tail();

    // creates an empty list
    public static <A> FList<A> nil() {
        return (Nil<A>) Nil.INSTANCE;
    }

    public final FList<A> cons(final A a) {
        return new Cons(a, this);
    }

    // return a list on which each element has been applied function f
    public final <B> FList<B> map(Function<A,B> f) {
        // TODO
        if (isNotEmpty()) {
            return tail().map(f).cons(f.apply(head())); // apply f on head and rerun map on remaining tail
        }
        else {
            return nil(); // reached end => add a NIL to stop
        }
    }

    // return a list on which only the elements that satisfies predicate are kept
    public final FList<A> filter(Predicate<A> f) {
        // TODO
        if (isNotEmpty()) {
            if (f.test(head())) { // test head with f
                return tail().filter(f).cons(head()); // if ok => keep the head
            }
            else {
                return tail().filter(f); // else ignore the head and continue
            }
        }
        else {
            return nil(); // reached end => add a NIL to stop
        }
    }

    // return an iterator on the element of the list
    public Iterator<A> iterator() {
        return new Iterator<A>() {
            // complete this class

            FList<A> current = FList.this;

            public boolean hasNext() {
                // TODO
                return current.isNotEmpty();
            }

            public A next() {
                // TODO
                A ret = current.head();
                current = current.tail();
                return ret;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    private static final class Nil<A> extends FList<A> {
        public static final Nil<Object> INSTANCE = new Nil();

        @Override
        public A head() {
            // TODO NOPE
            throw new IllegalArgumentException();
        }

        @Override
        public FList<A> tail() {
            // TODO NOPE
            throw new UnsupportedOperationException();
        }
    }


    private static final class Cons<A> extends FList<A> {

        // TODO add instance variables
        A a;
        FList<A> tail;

        Cons(A a, FList<A> tail) {
            this.a = a;
            this.tail = tail;
        }

        @Override
        public A head() {
            // TODO
            return a;
        }

        @Override
        public FList<A> tail() {
            // TODO
            return tail;
        }
    }


}
