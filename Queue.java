package com.project.project;

public class Queue<T> {
    private LinkedList<T> list;

    public Queue() {
        this.list = new LinkedList<>();
    }

    // GUARD AN ITEM IN THE QUEUE
    public void enqueue(T element) throws Exception {
        if (element == null)
            throw new Exception("MISSING ITEM TO ADD");
        list.add(element);
    }

    // RETRIEVE THE FIRST ITEM IN THE QUEUE
    public T peek() throws Exception {
        if (list.getSize() == 0)
            throw new Exception("NO ITEM TO RETRIEVE");

        return list.findByPosition(0);
    }

    // REMOVE THE FIRST ITEM IN THE QUEUE
    public void dequeue() throws Exception {
        if (list.getSize() == 0)
            throw new Exception("NO ITEM TO REMOVE");

        list.removeFromStart();
    }

    // CHECK IF THE QUEUE IS EMPTY
    public boolean isEmpty() {
        return list.getSize() == 0;
    }

    // GET THE SIZE OF THE QUEUE
    public int size() {
        return this.list.getSize();
    }

    // STRING REPRESENTATION OF THE QUEUE
    @Override
    public String toString() {
        return list.toString();
    }

    // EQUALITY CHECK BETWEEN TWO QUEUES
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Queue<?> otherQueue = (Queue<?>) obj;

        if (this.size() != otherQueue.size()) return false;

        Node<?> current = this.list.getStart();
        Node<?> currentOther = otherQueue.list.getStart();

        while (current != null) {
            if (!current.getElement().equals(currentOther.getElement())) {
                return false;
            }
            current = current.getNext();
            currentOther = currentOther.getNext();
        }
        return true;
    }

    // HASH CODE FOR THE QUEUE
    @Override
    public int hashCode() {
        return list.hashCode();
    }

    // COPY CONSTRUCTOR TO CREATE A QUEUE BASED ON ANOTHER ONE
    public Queue(Queue<T> template) throws Exception {
        if (template == null)
            throw new Exception("MISSING TEMPLATE");
        this.list = new LinkedList<>();
        for (int i = 0; i < template.list.getSize(); i++) {
            this.list.add(template.list.findByPosition(i));
        }
    }

    // CLONE THE QUEUE
    public Queue<T> clone() {
        Queue<T> clonedQueue = new Queue<>();
        clonedQueue.list = this.list.clone();
        return clonedQueue;
    }

}
