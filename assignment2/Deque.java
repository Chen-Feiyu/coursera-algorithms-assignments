import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first;
    private Node last;
    private int size;
    
    private class Node {
        Item item;
        Node next; 
        Node prev; 
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() {
            return current != null;
        }
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("deque is empty");
            Item item = current.item;
            current = current.next;
            return item;
        }
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }
    
    public Deque() {
        // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }
    
    public boolean isEmpty() {
        // is the deque empty?
        return (first == null);
    }
    
    public int size() {
        // return the number of items on the deque
        return size;
    }
    
    public void addFirst(Item item) {
        // add the item to the front
        if (item == null)
            throw new IllegalArgumentException("null item");
        
        if (first == null) { // create a node if no node exists
            first = new Node();
            first.item = item;
            last = first;
        } else { // add to the head
            Node firstOld = first;
            Node firstNew = new Node();
            firstNew.item = item;
            firstNew.next = firstOld;
            firstOld.prev = firstNew;
            first = firstNew;
        }
        size++;
    }
    
    public void addLast(Item item) {
        // add the item to the end
        if (item == null)
            throw new IllegalArgumentException("can't add null item");

        if (last == null) { // create a node if no node exists
            last = new Node();
            last.item = item;
            first = last;
        } else { // add to the tail
            Node lastOld = last;
            Node lastNew = new Node();
            lastNew.item = item;
            lastNew.prev = lastOld;
            lastOld.next = lastNew;
            last = lastNew;
        }
        size++;
    }
    
    public Item removeFirst() {
        // remove and return the item from the front
        if (size == 0) 
            throw new NoSuchElementException("deque is empty");
        
        Item item = first.item;
        if (first.next != null) {
            first.next.prev = null;
            first = first.next;
        } else {
            first = null;
            last = null;
        }
        size--;
        
        return item;
    }
    
    public Item removeLast() {
        // remove and return the item from the end
        if (size == 0) 
            throw new NoSuchElementException("deque is empty");
        
        Item item = last.item;
        if (last.prev != null) {
            last.prev.next = null;
            last = last.prev;
        } else {
            last = null;
            first = null;
        }
        size--;
        
        return item;
    }
    
    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }

}