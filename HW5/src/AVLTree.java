/* The following implementation of AVLTree was referenced
and modified from the lecture notes and textbook */

public class AVLTree<K extends Comparable<K>, V> {
    private MyAVLNode<K, V> root;
    static final MyAVLNode NIL = new MyAVLNode(null, null, null, null, 0); //sentinel

    public AVLTree(){
        root = NIL;
    }

    public MyAVLNode<K, V> getRoot(){
        return root;
    }

    // Print Preorder Traversal //
    public void printPreorderFormatted(MyAVLNode<K, V> root){
        if (root != NIL){
            System.out.print(" " + root.key);
            printPreorderFormatted(root.left);
            printPreorderFormatted(root.right);
        }
    }

    public void printPreorder(MyAVLNode<K, V> root){
        if (root != NIL){
            System.out.print(root.key);
            printPreorderFormatted(root.left);
            printPreorderFormatted(root.right);
            System.out.println();
        }
    }

    // Search //
    public MyAVLNode<K, V> search(K item){
        return searchItem(root, item);
    }

    private MyAVLNode<K, V> searchItem(MyAVLNode<K, V> tNode, K item){
        if (tNode == NIL){
            return NIL;
        } else if (item.compareTo(tNode.key) == 0){
            return tNode;
        } else if (item.compareTo(tNode.key) < 0){
            return searchItem(tNode.left, item);
        } else {
            return searchItem(tNode.right, item);
        }
    }

    // Insert //
    public void insert(MyAVLNode<K, V> newNode){
        root = insertItem(root, newNode);
    }

    private MyAVLNode<K, V> insertItem(MyAVLNode<K, V> tNode, MyAVLNode<K, V> newNode){
        int type;
        if (tNode == NIL){
            tNode = newNode;
         } else if (newNode.key.compareTo(tNode.key) < 0){
            tNode.left = insertItem(tNode.left, newNode);
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            type = needBalance(tNode);
            if (type != NO_NEED){
                tNode = balanceAVLTree(tNode, type);
            }
        } else{
            tNode.right = insertItem(tNode.right, newNode);
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            type = needBalance(tNode);
            if (type != NO_NEED){
                tNode = balanceAVLTree(tNode, type);
            }
        }
        return tNode;
    }

    // Delete //
    public void delete(K item){
        root = findAndDelete(root, item);
    }

    private MyAVLNode<K, V> findAndDelete(MyAVLNode<K, V> parentNode, K item){
        if (parentNode == NIL){
            return NIL;
        } else {
            if (item.compareTo(parentNode.key) == 0){
                parentNode = deleteNode(parentNode);
            } else if (item.compareTo(parentNode.key) < 0){
                parentNode.left = findAndDelete(parentNode.left, item);
                parentNode.height = 1 + Math.max(parentNode.right.height, parentNode.left.height);
                int type = needBalance(parentNode);
                if (type != NO_NEED){
                    parentNode = balanceAVLTree(parentNode, type);
                }
            } else{
                parentNode.right = findAndDelete(parentNode.right, item);
                parentNode.height = 1 + Math.max(parentNode.right.height, parentNode.left.height);
                int type = needBalance(parentNode);
                if (type != NO_NEED){
                    parentNode = balanceAVLTree(parentNode, type);
                }
            }
            return parentNode;
        }
    }

    private MyAVLNode<K, V> deleteNode(MyAVLNode<K, V> parentNode){
        if ((parentNode.left == NIL) && (parentNode.right == NIL)){
            return NIL;
        } else if (parentNode.left == NIL){
            return parentNode.right;
        } else if (parentNode.right == NIL){
            return parentNode.left;
        } else {
            returnPair rPair = deleteMinItem(parentNode.right);
            parentNode.key = rPair.item;
            parentNode.right = rPair.node;

            parentNode.height = 1 + Math.max(parentNode.right.height, parentNode.left.height);
            int type = needBalance(parentNode);
            if (type != NO_NEED){
                parentNode = balanceAVLTree(parentNode, type);
            }
            return parentNode;
        }
    }

    private returnPair deleteMinItem(MyAVLNode<K, V> parentNode){
        if (parentNode.left == NIL){
            return new returnPair(parentNode.key, parentNode.right);
        } else{
            returnPair rPair = deleteMinItem(parentNode.left);
            parentNode.left = rPair.node;

            parentNode.height = 1 + Math.max(parentNode.right.height, parentNode.left.height);
            int type = needBalance(parentNode);
            if (type != NO_NEED){
                parentNode = balanceAVLTree(parentNode, type);
            }

            rPair.node = parentNode;
            return rPair;
        }
    }

    private class returnPair{
        K item;
        MyAVLNode<K, V> node;

        private returnPair(K item, MyAVLNode<K, V> node){
            this.item = item;
            this.node = node;
        }
    }

    // Balancing //
    private final int LL = 1, LR = 2, RR = 3, RL = 4, NO_NEED = 0, ILLEGAL = -1;
    private int needBalance(MyAVLNode<K, V> node){
        int type = ILLEGAL;
        if (node.left.height + 1 < node.right.height){ // type R
            if (node.right.left.height <= node.right.right.height){
                type = RR;
            } else{
                type = RL;
            }
        } else if (node.left.height > node.right.height+1){ // type L
            if (node.left.left.height >= node.left.right.height){
                type = LL;
            } else{
                type = LR;
            }
        } else{
            type = NO_NEED;
        }
        return type;
    }

    private MyAVLNode<K, V> balanceAVLTree(MyAVLNode<K, V> rotateNode, int type){
        MyAVLNode<K, V> returnNode = NIL;
        if (type == LL){
            returnNode = rightRotate(rotateNode);
        }else if (type == LR){
            rotateNode.left = leftRotate(rotateNode.left);
            returnNode = rightRotate(rotateNode);
        }else if (type == RR){
            returnNode = leftRotate(rotateNode);
        }else if (type == RL){
            rotateNode.right = rightRotate(rotateNode.right);
            returnNode = leftRotate(rotateNode);
        }
        return returnNode;
    }

    private MyAVLNode<K, V> leftRotate(MyAVLNode<K, V> rotateNode){
        MyAVLNode<K, V> RChild = rotateNode.right;

        if (RChild == NIL) return NIL;

        MyAVLNode<K, V> RLChild = RChild.left;
        RChild.left = rotateNode;
        rotateNode.right = RLChild;

        rotateNode.height = Math.max(rotateNode.left.height, rotateNode.right.height) + 1;
        RChild.height = Math.max(RChild.left.height, RChild.right.height) + 1;
        return RChild;
    }

    private MyAVLNode<K, V> rightRotate(MyAVLNode<K, V> rotateNode){
        MyAVLNode<K, V> LChild = rotateNode.left;

        if (LChild == NIL) return NIL;

        MyAVLNode<K, V> LRChild = LChild.right;
        LChild.right = rotateNode;
        rotateNode.left = LRChild;

        rotateNode.height = Math.max(rotateNode.left.height, rotateNode.right.height) + 1;
        LChild.height = Math.max(LChild.left.height, LChild.right.height) + 1;
        return LChild;
    }


}
