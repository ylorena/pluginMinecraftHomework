package minecrafters.minecraftersplugin;

public class Matheuzinho<T> {
    Node<T> current;

    public Matheuzinho() {
        this.current = null;
    }

    public void setNext(T stuff){
        if(this.current==null){
            this.current = generateNode(stuff);
            return;
        }
        Node<T> node = generateNode(stuff);
        getLast().setNext(node);
    }

    public Node<T> getLast(){
      Node<T> current = this.current;
      while(current.getNext()!=null){
          current = current.getNext();
      }
      return current;
    }

    public void removeNode(){
        if(this.current.getNext()==null){
            this.current = null;
            return;
        }
        this.current = this.current.getNext();
    }

    public Node<T> generateNode(T stuff){
        return new Node<T>(stuff);
    }
}
