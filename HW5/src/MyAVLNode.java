public class MyAVLNode<K extends Comparable<K>, V> {
    public K key;
    public MyAVLNode<K, V> left, right;
    public int height;
    public V item;

    public MyAVLNode(K key, V item){
        this.key = key;
        this.item = item;
        left = right = AVLTree.NIL;
        height = 1;
    }

    public MyAVLNode(K key, V item, MyAVLNode<K, V> leftChild, MyAVLNode<K, V> rightChild, int h){
        this.key = key;
        this.item = item;
        left = leftChild;
        right = rightChild;
        height = h;
    }

}
