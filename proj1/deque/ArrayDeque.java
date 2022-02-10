package deque;

public class ArrayDeque<T> {
    private static final int DEFAULT_CAPACITY = 8;

    private int size;
    private T[] array;

    public ArrayDeque(){
        size = 0;
        array = (T[]) new Object[DEFAULT_CAPACITY];
    }

    public void addFirst(T item){

    }

    public void addLast(T item){

    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        for (int i = 0; i < size - 1; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.print(array[size]);
    }

    public T removeFirst(){

    }

    public T removeLast(){

    }

    public T get(int index){
        return array[index];
    }
}
