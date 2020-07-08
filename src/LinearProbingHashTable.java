import java.io.IOException;
import java.util.Map;


public class LinearProbingHashTable<K,V> {

    private static class Entry<K,V>{
        K key;
        V value;
        boolean isDeleted;

        Entry(){
            key = null;
            value = null;
            isDeleted = false;
        }
        Entry(K newKey, V newValue){
            key = newKey;
            value = newValue;
            isDeleted = false;
        }
    }

    int size;
    int numElements;
    Entry<K,V> table[];

    LinearProbingHashTable(){
        size = 4;
        table = new Entry[size];
    }
    LinearProbingHashTable(int hashSize){
        size = hashSize;
        table = new Entry[size];
    }

    public boolean insert(K key, V value){
        if(key == null || value == null)
            throw new NullPointerException("Key or value cannot be null");

        Entry<K,V> newEntry = new Entry<K,V>(key, value);
        if(numElements >= size/2)
            rehash();

        for(int i = getHashValue(key); i < size; i=(i+1)%size){
            if(table[i] != null) {  //if there is already something in that position
                if (table[i].value == value && table[i].key == key) {   //Check if duplicate and return if it is
                    System.out.println(value + " is already in the table.");
                    return false;
                }
                if (table[i].key == key && table[i].isDeleted)    //For the case of the spot being a deleted element
                {
                    numElements++;
                    table[i].value = value;
                    table[i].isDeleted = false;
                    return true;
                }
            }
                else{   //Nothing is in spot on table, so we can insert
                    numElements++;
                    table[i] = newEntry;
                    return true;
                }
        }
        return false;
    }

    public V find(K key){
        for(int i = getHashValue(key); table[i] != null; i=(i+1)%size){ //checks through every spot in table
            if(table[i].key == key && !table[i].isDeleted) //If keys match, we found it, unless it is deleted, then keep going
                return table[i].value;
        }
        return null;
    }

    public boolean delete(K key){
        if(find(key) == null)   //can't delete what doesn't exist
            return false;
        int i = getHashValue(key);
        while(key.equals(table[i].key) == false)
            i = (i+1) % size;

        table[i].isDeleted = true;  //don't actually delete it, just mark it as such
        numElements--;
        return true;
    }

    private int getHashValue(K key){
        return (key.hashCode()) % size;
    }

    private void rehash() {
        LinearProbingHashTable<K,V> temp;
        if(numElements > size/2)    //If size too small, double size
            temp = new LinearProbingHashTable<K,V>(size*2);
        else if(numElements < size/4)   //If size too big, 1/2 size
            temp = new LinearProbingHashTable<K,V>(size/2);
        else   //If size is right, keep same
            temp = new LinearProbingHashTable<K,V>(size);
        for(int i = 0; i < size; i++){
            if(table[i] != null)
                temp.insert(table[i].key, table[i].value);
        }
        size = temp.size;
        table = temp.table;
    }

    public int getLocation(K key){
        for(int i = 0; i < size; i++){
            if(table[i] != null)
                if(table[i].key == key)
                    return i;
        }
        return -1;
    }

    public String toString(){
        String result = "";

        for(int i = 0; i < size; i++){
            result += i + " ";
            if(table[i]!=null) {
                result += table[i].key + ", " + table[i].value + " ";
                if(table[i].isDeleted)
                    result += "deleted";
            }

            result += "\n";
        }
        return result;
    }

    public static void main(String[] args){
        LinearProbingHashTable<Integer, String> hashTable = new LinearProbingHashTable<>();
        //insert test
        hashTable.insert(6, "John");
        hashTable.insert(28, "Michael");
        hashTable.insert(99, "Michelle");
        hashTable.insert(11, "Sam");
        hashTable.insert(27, "Sarah");
        hashTable.insert(24, "Bill");
        hashTable.insert(22, "Marie");

        hashTable.insert(99, "Michelle"); //Will not insert since it is a duplicate

        //toString test
        System.out.println("\nInitial table: \n" + hashTable.toString()); //Print out the table so far (minus the duplicate

        //find test
        System.out.println("The value at key \"28\" is " + hashTable.find(28) + "\n"); //should be Michael
        System.out.println("The value at key \"64\" is " + hashTable.find(64) + "\n"); //should be null since 64 is not a key

        //delete test
        hashTable.delete(11); //not gone from the list, but "marked" as deleted
        System.out.println("Table with key \"11\" deleted:\n" + hashTable.toString());

        //rehash can be seen tested on line 42 inside the insert function

        //getHashValue test
        System.out.println("The hash value for key \"22\" is " + hashTable.getHashValue(22) + "\n");

        //getLocation test
        System.out.println("The location for key \"22\" is " + hashTable.getLocation(22) +"\n");


    }

}
