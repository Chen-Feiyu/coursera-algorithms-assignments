import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] itemArray;
    private int capacity;
    private int size;
    
    private class RQIterator implements Iterator<Item> {
        private final int[] iterateOrder;
        private int position; // the position of current entry in iterateOrder

        public RQIterator() {
            iterateOrder = new int[size];
            for (int i = 0; i < size; i++) iterateOrder[i] = i;
            StdRandom.shuffle(iterateOrder);
            position = 0;
        }
        public boolean hasNext() {
            return position < size;
        }
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("randomized queue is empty");

            return itemArray[iterateOrder[position++]];
        }
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

    public RandomizedQueue() {
        // construct an empty randomized
        capacity = 1;
        size = 0;
        itemArray = (Item[]) new Object[capacity];
    }

    public boolean isEmpty() {
        // is the randomized queue empty?
        return size == 0;
    }

    public int size() {
        // return the number of items on the randomized queue
        return size;
    }

    public void enqueue(Item item) {
        // add the item
        if (item == null)
            throw new IllegalArgumentException("can't add null item");

        if (size == capacity) resize(capacity*2);
        itemArray[size++] = item;
    }

    private void resize(int capacityNew) {
        // helper function: resize the itemArray to capacityNew
        Item[] copyArray = (Item[]) new Object[capacityNew];
        for (int i = 0; i < size; i++) copyArray[i] = itemArray[i];
        itemArray = copyArray;
        capacity = capacityNew;
    }

    public Item dequeue() {
        // remove and return a random item
        if (size == 0) 
            throw new NoSuchElementException("randomized queue is empty");
        
        int position = StdRandom.uniform(size);
        Item item = itemArray[position];
        if (position != size-1) {
            // fill the last item into to randomly popped position
            // then update the position of the last item
            itemArray[position] = itemArray[size-1];
            itemArray[size-1] = null;
        }
        size--;
        
        if (size > 0 && size == capacity/4) resize(capacity/2);
        
        return item;    
    }

    public Item sample() {
        // return a random item (but do not remove it)
        if (size == 0)
            throw new NoSuchElementException("randomized queue is empty");
        int position = StdRandom.uniform(size);
        Item item = itemArray[position];
        return item;    
    }

    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RQIterator();
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
                rq.enqueue(22);
                rq.size();
                rq.dequeue();
                rq.isEmpty();
                rq.enqueue(454);
                rq.dequeue();
                rq.enqueue(222);
                rq.enqueue(125);
                rq.dequeue();
    }
}
