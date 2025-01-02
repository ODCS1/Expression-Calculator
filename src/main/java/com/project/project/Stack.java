package com.project.project;

public class Stack<T> implements Cloneable {
    private LinkedList<T> elements;

    public Stack() {
        this.elements = new LinkedList<>();
    }

    public void push(T item) throws Exception {
        if (item == null)
            throw new Exception("Missing item to push");

        this.elements.add(item);
    }

    public T peek() throws Exception {
        if (this.elements.getSize() == 0)
            throw new Exception("Nothing to pop");

        return this.elements.findByPosition(this.elements.getSize() - 1);
    }

    public void pop() throws Exception {
        if (this.elements.getSize() == 0)
            throw new Exception("Nothing to remove");

        this.elements.removeFromEnd();
    }

    public boolean isEmpty() {
        return this.elements.getSize() == 0;
    }

    public int size() {
        return this.elements.getSize();
    }

    @Override
    public String toString() {
        return "Stack with " + this.elements.getSize() + " element(s): " + this.elements.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Stack<?> otherStack = (Stack<?>) obj;

        if (this.size() != otherStack.size()) return false;

        Node<?> current = this.elements.getStart();
        Node<?> otherCurrent = otherStack.elements.getStart();

        while (current != null) {
            if (!current.getElement().equals(otherCurrent.getElement())) {
                return false;
            }
            current = current.getNext();
            otherCurrent = otherCurrent.getNext();
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.elements.hashCode();
    }

    @Override
    public Stack<T> clone() {
        Stack<T> clonedStack = new Stack<>();
        clonedStack.elements = this.elements.clone();
        return clonedStack;
    }
}
