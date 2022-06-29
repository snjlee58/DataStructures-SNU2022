public class MyHashTable<K, V> {
    private V table[];
    int numItems;

    public MyHashTable(int n) {
        table = (V[]) new Object[n];
        numItems = 0;
    }

    private int hash(K x) {
        int hashResult = x.hashCode() % table.length;
        return hashResult;
    }

    public V getSlot(K key) {
        int slot = hash(key);
        return table[slot];
    }

    public V getSlot(int index){
        return table[index];
    }

    public boolean insert(K substring, V item) {
        int slot = hash(substring);
        if (numItems == table.length) return false;
        if (table[slot] == null) {
            table[slot] = item;
            numItems++;
            return true;
        }
        else{

        }
        return false;
    }

}