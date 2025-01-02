package com.project.project;

public class LinkedList<T> implements Cloneable{
    private Node<T> start;
    private Node<T> end;
    private int size;
    private final int NOT_FOUND = -1;

    public void add(T element) {
        Node<T> newNode = new Node<T>(element);
        if (size == 0) {
            start = newNode;
        } else {
            this.end.setNext(newNode);
        }

        this.end = newNode;
        this.size++;
    }

    public int getSize() {
        return this.size;
    }

    public Node<T> getStart() {
        return this.start;
    }


    // CLEARS THE LIST IN AN OPTIMIZED WAY
    public void clearAll() {
        Node<T> current = this.start.getNext();

        while (current != null) {
            Node<T> next = current.getNext();
            current.setElement(null);
            current.setNext(null);

            current = next;
        }

        this.start = null;
        this.end = null;
        this.size = 0;
    }

    private Node<T> findNodeByPosition(int pos) {
        if (pos < 0 || pos > this.size - 1) {
            throw new IllegalArgumentException("THIS POSITION DOES NOT EXIST!!!");
        }

        int count = 0;
        Node<T> current = start;
        while (current.getNext() != null) {
            if (count == pos)
                return current;

            count++;

            current = current.getNext();
        }

        return current;
    }


    public T findByPosition(int pos){
        return findNodeByPosition(pos).getElement();
    }


    public int findByElement(T element) {
        Node<T> currentNode = this.start;

        for (int i = 0; i < this.size; i++) {
            if (currentNode.getElement().equals(element)) {
                return i;
            }

            currentNode = currentNode.getNext();
        }

        return NOT_FOUND;
    }


    public void insertAtStart(T element){

        // 2 SITUATIONS
        Node<T> newNode = new Node<T>(element);

        // 1ST SITUATION - SIZE = 0
        if (this.size == 0) {
            this.start = newNode;
            this.end = newNode;
        }
        // 2ND SITUATION - SIZE > 0
        else {
            newNode.setNext(start);
            this.start = newNode;
        }

        size++;
    }

    public void insertAtPosition(int pos, T element) {
        if (pos < 0 || pos > size) {
            throw new IllegalArgumentException("POSITION DOES NOT EXIST!!!");
        }

        // 3 CASES

        // 1ST CASE - INSERT AT POSITION 0
        if (pos == 0) {
            insertAtStart(element);
        }
        // 2ND CASE - INSERT AT THE END
        else if (pos == this.size) {
            add(element);
        }
        // 3RD CASE - INSERT IN THE MIDDLE
        else {
            Node<T> nodeAtPos = findNodeByPosition(pos);
            Node<T> previousNode = findNodeByPosition(pos - 1);

            Node<T> nodeToInsert = new Node<T>(element);

            nodeToInsert.setNext(nodeAtPos);
            previousNode.setNext(nodeToInsert);

            this.size++;
        }
    }

    public T removeFromStart() {
        if (this.size == 0)
            throw new RuntimeException("THERE ARE NO ELEMENTS TO REMOVE!!!");

        T removedElement = start.getElement();
        Node<T> newStart = start.getNext();
        start.setElement(null);
        start.setNext(null);

        start = newStart;
        this.size--;

        if (this.size == 0)
            this.end = null;

        return removedElement;
    }



    public T removeFromEnd() {
        if (this.size == 0)
            throw new RuntimeException("THERE ARE NO ELEMENTS TO REMOVE!!!");

        T removedElement = this.end.getElement();

        if (this.size == 1) {
            this.start = null;
            this.end = null;
            this.size = 0;
            return removedElement;
        }

        Node<T> previous = findNodeByPosition(this.size - 2);
        end.setElement(null);
        previous.setNext(null);
        end = previous;
        this.size--;

        return removedElement;
    }



    public T removeByPosition(int pos) {
        if (this.size == 0)
            throw new RuntimeException("NO ELEMENTS TO REMOVE!!!");

        if (pos < 0 || pos > this.size - 1)
            throw new IllegalArgumentException("POSITION DOES NOT EXIST!!!");

        if (this.size == 1 || pos == this.size - 1)
            return removeFromEnd();
        else if (pos == 0)
            return removeFromStart();


        Node<T> previousNode = findNodeByPosition(pos - 1);
        Node<T> nodeToRemove = findNodeByPosition(pos);
        Node<T> nextNode = findNodeByPosition(pos + 1);

        T returnElement = nodeToRemove.getElement();
        nodeToRemove.setElement(null);
        nodeToRemove.setNext(null);

        previousNode.setNext(nextNode);
        this.size--;

        return returnElement;
    }

    @Override
    public LinkedList<T> clone() {
        LinkedList<T> clonedList = new LinkedList<>();
        Node<T> current = this.start;

        while (current != null) {
            clonedList.add(current.getElement());
            current = current.getNext();
        }

        return clonedList;
    }



    @Override
    public String toString() {
        if (this.size == 0) return "[]";


        String output = "[";
        Node<T> current = start;

        output +=  current.toString();

        while (current.getNext() != null) {
            current = current.getNext();
            output += ", " + current.toString();
        }

        return output + "]";
    }
}
