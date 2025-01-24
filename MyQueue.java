/**
 * MyQueue is a generic queue implementation using a linked list.
 * It supports typical queue operations such as add, remove, check if empty, and get size.
 *
 * @param <T> The type of elements held in this queue
 */
public class MyQueue<T> {
    private Node<T> front; // Front node of the queue
    private Node<T> rear;  // Rear node of the queue
    private int size;      // Current size of the queue

    /**
     * Node class represents each element in the queue.
     * It contains data and a reference to the next node in the queue.
     *
     * @param <T> The type of the data stored in the node
     */
    private static class Node<T> {
        T data;       // Data stored in the node
        Node<T> next; // Reference to the next node in the queue

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Constructs an empty MyQueue.
     */
    public MyQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Adds an element to the end of the queue.
     *
     * @param data The data to be added to the queue
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data); // Create a new node with the given data
        if (rear != null) {
            rear.next = newNode; // Link the current rear to the new node
        }
        rear = newNode; // Update rear to the new node
        if (front == null) {
            front = newNode; // If the queue was empty, set front to the new node
        }
        size++; // Increment the size of the queue
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return The data at the front of the queue, or null if the queue is empty
     */
    public T remove() {
        if (isEmpty()) {
            return null; // Return null if the queue is empty
        }
        T data = front.data; // Get the data from the front node
        front = front.next;  // Move the front to the next node
        if (front == null) {
            rear = null; // If the queue is now empty, set rear to null as well
        }
        size--; // Decrement the size of the queue
        return data; // Return the removed data
    }

    /**
     * Checks if the queue is empty.
     *
     * @return True if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return The number of elements in the queue
     */
    public int size() {
        return size;
    }
}
