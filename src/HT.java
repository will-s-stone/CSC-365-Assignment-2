import java.io.*;
import java.util.ArrayList;
import java.util.List;

class HT implements java.io.Serializable {
    static final class Node implements java.io.Serializable{
        Object key;
        Node next;
        // int count;

        Object value;
        Node(Object k, Object v, Node n) { key = k; value = v; next = n; }
    }
    Node[] table = new Node[8]; // always a power of 2
    int size = 0;
    //Modified to transient for serialization process
    ArrayList<Object> keySet = new ArrayList<>();
    ArrayList<Object> values = new ArrayList<>();

    boolean contains(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }


    double getDouble(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return (double) e.value;
        }
        return -999999999;
    }


    Object get(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return e.value;
        }
        return -999999999;
    }

    void add(Object key, Object value) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next ) {
            if (key.equals(e.key)) {
                // Update the value if the key already exists
                e.value = value;
                return;
            }
        }
        // Create a new node for the key-value pair
        table[i] = new Node(key, value, table[i]);
        //Add new key to the keySet
        keySet.add(key);
        values.add(value);
        ++size;
        if ((float)size/table.length >= 0.75f)
            resize();
    }

    ArrayList<Object> getKeySet(){
        return keySet;
    }
    ArrayList<Object> getValues() {return values;}
    List<HT> getValuesTable() {
        List<HT> valTable = new ArrayList<>();
        for (int i = 0; i < keySet.size(); i++) {
            valTable.add((HT) this.get(keySet.get(i)));
        }
        return valTable;

    }

    void resize() {
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; ++i) {
            for (Node e = oldTable[i]; e != null; e = e.next) {
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                newTable[j] = new Node(e.key, e.value, newTable[j]);
            }
        }
        table = newTable;
    }
    void resizeV2() { // avoids unnecessary creation
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; ++i) {
            Node e = oldTable[i];
            while (e != null) {
                Node next = e.next;
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                e.next = newTable[j];
                newTable[j] = e;
                e = next;
            }
        }
        table = newTable;
    }
    void remove(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        Node e = table[i], p = null;
        while (e != null) {
            if (key.equals(e.key)) {
                if (p == null)
                    table[i] = e.next;
                else
                    p.next = e.next;
                break;
            }
            p = e;
            e = e.next;
        }
    }
    void printAll() {
        for (int i = 0; i < table.length; ++i)
            for (Node e = table[i]; e != null; e = e.next)
                System.out.println(e.key + ": " + e.value);
    }


    @Serial
    private void writeObject(ObjectOutputStream s) throws Exception {
        s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                s.writeObject(e.key);
                s.writeObject(e.value);
            }
        }
    }

    @Serial
    private void readObject(ObjectInputStream s) throws Exception {
        s.defaultReadObject();
        int n = s.readInt();
        for (int i = 0; i < n; ++i) {
            //Might have to change this after
            add(s.readObject(), s.readObject());
        }
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < table.length; ++i){
            for (Node e = table[i]; e != null; e = e.next){
                s.append(e.key).append(":").append(e.value);
                s.append("\n");
            }
        }
        return s.toString();
    }
}

