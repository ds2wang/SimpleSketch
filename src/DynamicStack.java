import java.util.LinkedList;
import java.util.NoSuchElementException;
    
    public class DynamicStack <MyShape> { //creates a MyShape Dynamic stack
    private LinkedList <MyShape> list=new LinkedList <MyShape>();
    
    //contructor class
    public DynamicStack(){
        list=new LinkedList<MyShape>();
    }
    //returns if stack is empty
    public boolean isEmpty(){
        return list.isEmpty();
    }
    //push an object into the stack
    public void push(MyShape x){
        list.addFirst(x);
    }
    //clears the stack
    public void makeEmpty(){
        list.clear();
    }
    //pops an item from the stack
    public MyShape dequeue(){
        if(!list.isEmpty()){
            return list.removeFirst();
        }else{
        return null;
        }
    }
    public MyShape remove(int i){
    	return list.remove(i);
    }
    public MyShape pop(){
        if(!list.isEmpty()){
            return list.pop();
        }else{
        	return null;
        }
    }
    //sees most recent item of the stack
    public MyShape peek(){
        return list.peek();
    }
    //gets size of the stack
    public int getSize(){
        return list.size();
    }
    //gets the element at the specified index/position
    public MyShape get(int i){
        return list.get(i);
    }
}