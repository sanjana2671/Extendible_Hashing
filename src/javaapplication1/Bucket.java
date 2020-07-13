/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaApplication1;
import java.util.*;


/**
 *
 * @author Sanjana
 */
public class Bucket {
    int depth,size;
    HashSet<Integer> values = new HashSet<Integer>();
    Bucket(int depth,int size){
        this.depth=depth;
        this.size=size;
    }
    //this function will be used to get a status whether the key is already there / bucket has to be split /add key
    int insert(int key){
        if(values.contains(key)){
            return -1;
        }
        if(isFull()){
            return 0;
        }
        values.add(key);
        return 1;
    }
    HashSet<Integer> copy(){
        HashSet<Integer> temp = new HashSet<Integer>();
        Iterator<Integer> it =values.iterator();
        while(it.hasNext()) {
            temp.add(it.next());
        }
        return temp;
    }
    //Display the particular bucket
    void display(){
        Iterator<Integer> it =values.iterator();
        while(it.hasNext()){
            int temp = it.next();
            System.out.print(temp+" ");
            Table.sho = Table.sho + temp + " ";
        }
    }
    //To check if a particular bucket is full or not
    boolean isFull(){
        if(values.size()==size){
            return true;
        }
        else{
            return false;
        }
    }
    //get depth of that particular bucket
    int getDepth()
    {
        return depth;
    }
    //increase depth while splitting
    int increaseDepth() {
        ++depth;
        return depth;
    }
    //clear that bucket
    void clear(){
        values.clear();
    }
    //searching in that bucket
    int search(int key){
        Iterator<Integer> it =values.iterator();
        if(values.contains(key)){
            System.out.println("Key exists in a bucket");
            return 1;
        }
        else{
            System.out.println("This key does not exists");
            return 0;
        }
    }
}
