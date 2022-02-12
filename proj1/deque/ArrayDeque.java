package deque;

public class ArrayDeque<T> {
    private static final int DEFAULT_CAPACITY = 8;

    private int size;
    private T[] array;
    private int front;
    private int rear;

    public ArrayDeque(){
        size = 0;
        array = (T[]) new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = 0;
    }

    public void addFirst(T item){
        size++;
        if (size > DEFAULT_CAPACITY)
            resize(size * 2);

        if (size != 0){
            front = (front - 1 + array.length) % array.length;
        }

        array[front] = item;
    }

    public void addLast(T item){
        size++;
        if (size > DEFAULT_CAPACITY)
            resize(size * 2);

        array[rear] = item;
        rear = (rear + 1 + array.length) % array.length;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

//    public String printDeque(){
//        String s = "";
//        if (!isEmpty()){
//            for (int i = front; i != rear; i = (i+1) % array.length) {
//                s = s + array[i];
//                if ((i+1) % array.length != rear)
//                    s = s + " -> ";
//            }
//        }else{
//            s = "队列为空";
//        }
//        return s;
//    }

    public void printDeque(){
        if (!isEmpty()){
            for (int i = front; i != rear; i = (i+1) % array.length) {
                System.out.print(array[i]);
                if ((i+1) % array.length != rear)
                    System.out.println(" -> ");
            }
        }else{
            System.out.println("队列为空");
        }
    }

    public T removeFirst(){
        if (isEmpty())
            return null;

        size--;
        T x = array[front];
        front = (front + 1 + array.length) % array.length;
        return x;
    }

    public T removeLast(){
        if (isEmpty())
            return null;
        size--;
        T x = array[rear];
        rear = (rear - 1 + array.length) % array.length;
        return x;
    }

    public T get(int index){
        return array[index];
    }

    public void resize(int capacity){
        T[] a1 = (T[]) new Object[capacity];
        System.arraycopy(array, 0, a1, 0, size);
        array = a1;
    }
}
