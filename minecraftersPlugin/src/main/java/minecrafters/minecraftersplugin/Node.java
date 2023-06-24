package minecrafters.minecraftersplugin;

public class Node<T> {
    private T data;
    private Node<T> next;

    public Node(T data){
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext(){
        return this.next;
    }

    public void setNext(Node<T> node){
        this.next = node;
    }



}