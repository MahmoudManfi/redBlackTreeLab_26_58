package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T> , V > implements IRedBlackTree<T, V> {

    private INode<T, V> root;
    private INode<T, V> nullNode;

    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';

    private int compare;

    RedBlackTree () {

        nullNode = new Node<>();
        root = nullNode;

    }

    private void refresh(){

        root.setParent(null);
        nullNode.setParent(null);

    }

    @Override
    public INode<T, V> getRoot() {
        return root;
    }

    @Override
    public boolean isEmpty() {
        return root.isNull();
    }

    @Override
    public void clear() {

        root = nullNode;

    }

    private INode<T,V> find(INode<T,V> node, T key) {

        if (getRoot().isNull()) { return nullNode;}

        compare = key.compareTo(node.getKey());

        if (compare < 0 && !node.getLeftChild().isNull()) return find(node.getLeftChild(),key);
        else if (compare > 0 && !node.getRightChild().isNull()) return find(node.getRightChild(),key);

        return node;

    }

    @Override
    public V search(T key) {

        threwException(key);

        V value = find(getRoot(),key).getValue();
        return (compare == 0) ? value : null;

    }

    @Override
    public boolean contains(T key) {

        return (search(key) != null);

    }

    private void threwException(Object key){

        if (key == null) throw new RuntimeErrorException(null);

    }

    private INode<T, V> newNode(INode<T,V> parent, T key, V value) {

        INode<T,V> node = new Node<>(key,value);
        node.setLeftChild(nullNode);
        node.setRightChild(nullNode);

        if (!parent.isNull()) {

            if (compare > 0) {
                putNode(RIGHT,parent,node);
            } else {
                putNode(LEFT,parent,node);
            }
            node.setColor(INode.RED);
        }

        return node;

    }

    @Override
    public void insert(T key, V value) {

        threwException(key);
        threwException(value);

        if (isEmpty()) {

            root = newNode(getRoot(),key,value);  return;

        }

        INode<T,V> node = find(getRoot(),key); // i search for the place where should the new node be

        if (compare == 0) { // i search for the node and find the same key so i upgrade its value

            node.setValue(value);

        } else {

            node = newNode(node,key,value);
            recolorAndRotation(node);

        }

    }

    @Override
    public boolean delete(T key) {

        threwException(key);

        INode <T, V> node = find(getRoot(),key);

        if (compare != 0) return false;

        node = searchForDelete(node);

        if (node == getRoot()) {
            root = nullNode;
            return true;
        }

        INode<T, V> parent = node.getParent();

        if (parent.getLeftChild() == node){
            putNode(LEFT,parent,nullNode);
        } else {
            putNode(RIGHT,parent,nullNode);
        }

        if (node.getColor() == INode.RED) return true;

        playWithDB(nullNode);
        refresh();
        return true;

    }

    private void swapColors(INode<T,V> node1, INode<T,V> node2) {

        boolean temp = node1.getColor(); node1.setColor(node2.getColor()); node2.setColor(temp);

    }

    private void case5And6(INode<T,V> dp, INode<T,V> parent, INode<T,V> child) {

        INode<T,V> dpParent = dp.getParent();
        swapColors(parent,child);
        swapNode(child,parent);
        dp.setParent(dpParent);
        playWithDB(dp);

    }

    private void case3(INode<T,V> parent,INode<T,V> sibling) {

        sibling.setColor(INode.RED);
        playWithDB(parent);

    }

    private void playWithDB(INode<T, V> dp) {

        if (dp == getRoot()) return;

        if (dp.getColor() == INode.RED) {

            dp.setColor(INode.BLACK);
            return;

        }

        INode<T, V> parent = dp.getParent();
        INode<T, V> sibling = getSibling(parent,dp);

        if (sibling.getColor() == INode.RED) {

            case5And6(dp,parent,sibling); return;

        }

        compare = parent.getKey().compareTo(sibling.getKey());

        INode<T, V> siblingLeft = sibling.getLeftChild();
        INode<T, V> siblingRight = sibling.getRightChild();

        if (compare < 0) {

            if (siblingRight.getColor() == INode.RED) {

                case5And6(siblingRight,parent,sibling);

            } else if (siblingLeft.getColor() == INode.RED) {

                case5And6(dp,sibling,siblingLeft);

            } else {

                case3(parent,sibling);

            }

        } else {

            if (siblingLeft.getColor() == INode.RED) {

                case5And6(siblingLeft,parent,sibling);

            } else if (siblingRight.getColor() == INode.RED) {

                case5And6(dp,sibling,siblingRight);

            } else {

                case3(parent,sibling);

            }

        }

    }

    private INode<T,V> searchForDelete(INode<T, V> node) {

        INode<T, V> newNode;

        if (!node.getRightChild().isNull()) {

            newNode = node.getRightChild();

            while (!newNode.getLeftChild().isNull()) newNode = newNode.getLeftChild();

        } else if (!node.getLeftChild().isNull()) {

            newNode = node.getLeftChild();

            while (!newNode.getRightChild().isNull()) newNode = newNode.getRightChild();

        } else {
            return node;
        }

        node.setKey(newNode.getKey());
        node.setValue(newNode.getValue());
        return searchForDelete(newNode);

    }

    private void recolorAndRotation(INode <T, V> node) {

        INode<T,V> parent = node.getParent();  INode<T,V> grandParent = parent.getParent();

        if (grandParent == null || parent.getColor() == INode.BLACK) return;

        INode<T,V> parentSibling = getSibling(grandParent,parent);

        if (parentSibling.getColor() == INode.RED) {

            parent.setColor(INode.BLACK);
            parentSibling.setColor(INode.BLACK);

            if (grandParent != getRoot()) {

                grandParent.setColor(INode.RED);
                recolorAndRotation(grandParent);

            }

        } else {

            rotation(grandParent,parent,node);

        }

    }

    private void putNode(char direction, INode<T, V> parent, INode<T,V>  child) {

        child.setParent(parent);

        if (direction == RIGHT) {
            parent.setRightChild(child);
        } else {
            parent.setLeftChild(child);
        }

    }

    private void swapNode(INode<T,V>  parent, INode<T,V> child) {

        compare = parent.getKey().compareTo(child.getKey());

        INode<T,V> grandParent = child.getParent();

        if (compare > 0) {
            INode<T, V> leftChild = parent.getLeftChild();
            putNode(LEFT,parent,child);
            putNode(RIGHT,child,leftChild);
        } else {
            INode<T, V> rightChild = parent.getRightChild();
            putNode(RIGHT,parent,child);
            putNode(LEFT,child,rightChild);
        }

        if (grandParent != null) {

            if (grandParent.getKey().compareTo(parent.getKey()) > 0) {
                putNode(LEFT,grandParent,parent);
            } else {
                putNode(RIGHT,grandParent,parent);
            }

        } else {

            root = parent;

        }

    }

    private INode<T,V> getSibling (INode <T, V> parent, INode <T, V> child) {

        return (parent.getLeftChild() == child) ? parent.getRightChild() : parent.getLeftChild() ;

    }

    private void rotation(INode<T,V> grandParent, INode<T,V> parent, INode<T,V> node) {

        compare = grandParent.getKey().compareTo(parent.getKey()); // to compare the grandparent with parent

        grandParent.setColor(INode.RED);

        if (compare != parent.getKey().compareTo(node.getKey())) {

            node.setColor(INode.BLACK);
            swapNode(node,parent);
            swapNode(node,grandParent);

        } else {

            parent.setColor(INode.BLACK);
            swapNode(parent,grandParent);

        }

        refresh();
    }

}
