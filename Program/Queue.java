package Program;
import java.util.ArrayList;

public class Queue<T> {
    private ArrayList<T> myList;

    public Queue() { myList = new ArrayList<T>(); }

    public Queue(T t) {
        this();
        myList.add(t);
    }

    public void enqueue(T t) { myList.add(t); }

    public T dequeue() {
        if (myList.size() == 0) return null;
        T temp = myList.get(0);
        myList.remove(0);
        return temp;
    }

    public T peek() {
        if (myList.size() == 0) return null;
        return myList.get(0);
    }

    public void print() { for (var item : myList) System.out.println(item); }
    public ArrayList<T> getList() { return myList; }
    public boolean contains(T t) { return myList.contains(t); }
    public void clear() { myList.clear(); }
    public int size() { return myList.size(); }
    public boolean isEmpty() { return (this.size() == 0); }
}
