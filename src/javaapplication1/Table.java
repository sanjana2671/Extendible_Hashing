/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaApplication1;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


/**
 *
 * @author Sanjana
 */
public class Table {
    	 int globalDepth,bfr;
         String result  = "";
         String exists = "";
         static String sho = "";
    ArrayList<Bucket> buckets=new ArrayList<Bucket> (0);  //buckets will be storing individual buckets of the directory
    Table(int globalDepth,int bfr){
        this.globalDepth=globalDepth;
        this.bfr=bfr;

        //initially we are creating only 2 buckets corresponding to 0 and 1
        for(int i=0;i<(1<<globalDepth);++i){
            buckets.add(new Bucket(globalDepth,bfr));
        }
    }

    int pairIndex(int bucketNumber,int depth){
        return (bucketNumber^(1<<(depth-1)));
    }

    void expandDirectory(){
        //this will double the buckets
        for(int i=0;i<(1<<globalDepth);++i){
            buckets.add(buckets.get(i));
        }
        ++globalDepth;
    }

    void splitOverflown(int bucketNumber){
        int localDepth,pairIndex,index_diff,dir_size,i;
        HashSet<Integer> temp = new HashSet<Integer>();

        localDepth=buckets.get(bucketNumber).increaseDepth();

        //Directory needs to be expanded if the local depth is exceeding global depth
        if(localDepth>globalDepth){
            expandDirectory();
        }
        pairIndex=pairIndex(bucketNumber,localDepth);
        buckets.set(pairIndex,new Bucket(localDepth,bfr));
        temp=buckets.get(bucketNumber).copy();
        buckets.get(bucketNumber).clear();
        index_diff = 1<<localDepth;
        dir_size = 1<<globalDepth;
        for(i=pairIndex-index_diff;i>=0;i-=index_diff){
            buckets.set(i,buckets.get(pairIndex));
        }
        for(i=pairIndex+index_diff;i<dir_size;i+=index_diff){
            buckets.set(i,buckets.get(pairIndex));
        }
        Iterator<Integer> it = temp.iterator();
        while(it.hasNext()){
            insert(it.next(),true);
        }
    }

    //In search function firstly the bucket where key needs to be inserted is found and then we check that bucket
    void search(int key){
        int bucketNumber=hashFunction(key);
        result = "";
        System.out.println("Searching key "+key+" in bucket "+bucketID(bucketNumber));
        if(buckets.get(bucketNumber).search(key) == 1){
            result = result +"KEY: "+ key + " is found in the Bucket: " + bucketID(bucketNumber);
            System.out.println("KEY:"+ key + " is found in the Bucket: " + bucketID(bucketNumber));
        }
        
    }
    int hashFunction(int n){
        return (n&((1<<globalDepth)-1));
    }

    //bucketID is a function which will be converting number to binary string
    String bucketID(int num){
        int d;
        String str;
        d=buckets.get(num).getDepth();
        str="";
        while(num>0 && d>0)
        {
            str = (num%2==0?"0":"1")+str;
            num/=2;
            d--;
        }
        while(d>0)
        {
            str = "0"+str;
            d--;
        }
        return str;
    }

    void insert(int key,boolean reinserted){
        exists = "";
        int bucketNumber=hashFunction(key);
        int status=buckets.get(bucketNumber).insert(key);
        if(status==1){
            if(!reinserted){
                System.out.println("Inserted "+key+" in bucket "+bucketID(bucketNumber));
            }
            else{
                System.out.println("Moved "+key+" to bucket "+bucketID(bucketNumber));
            }
        }
        else if(status==0){
            splitOverflown(bucketNumber);
            insert(key, reinserted);
        }
        else{
            System.out.println("Key "+key+" already exists in bucket "+bucketID(bucketNumber));
            exists = exists + "Key "+key+" already exists in bucket "+bucketID(bucketNumber);
        }
    }

    //duplicates will be used to incase the bucket is not split and we then want to print it as different or not
    void show_table(boolean duplicates){
        String str;
        sho = "Current Directory Structure:\n";
        //this set is used to mark if a particular value has been printed before
        //this will have no significance if we are printing duplicates
        HashSet<String> displayed = new HashSet<String>();
        System.out.println("Global depth is : "+globalDepth);
        sho = sho + "Global depth is : "+globalDepth + "\n\n";
        for(int i=0;i<buckets.size();++i){
            int extra=buckets.get(i).getDepth();
            str=bucketID(i);
            //sho = sho + bucketID(i);
            if(duplicates || !displayed.contains(str)){
                displayed.add(str);
                //will leave space while printing buckets whose local depth is less than the global depth
                for(int j=extra;j<=globalDepth;++j){
                    System.out.print(" ");
                    sho = sho + " ";
                }
                System.out.print(str+" :: ");
                sho = sho + str +" (Local Depth : "+ str.length() + ") "  + " : ";
                buckets.get(i).display();
                sho = sho + "\n\n";
            }
            System.out.println();
            //sho = sho + "\n";
        }
    }
}
