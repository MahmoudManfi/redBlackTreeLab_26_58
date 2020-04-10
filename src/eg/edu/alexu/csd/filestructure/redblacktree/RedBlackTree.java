package eg.edu.alexu.csd.filestructure.redblacktree;

public class RedBlackTree<T extends Comparable<T> , V > implements IRedBlackTree<T, V> {

    private INode<T, V> root;

    RedBlackTree () {

        root = new Node<>();

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

        root = new Node<>();

    }

    private INode<T,V> find(INode<T,V> node, T key) {

        if (node.isNull()) return node;

        else {

            int compareInt = key.compareTo(node.getKey());

            if (compareInt == 0) return node;

            else if (compareInt > 0) return find(node.getRightChild(),key);

            else return find(node.getLeftChild(),key);

        }



    }

    @Override
    public V search(T key) {

        return find(getRoot(),key).getValue();

    }

    @Override
    public boolean contains(T key) {

        return (!find(getRoot(),key).isNull());

    }

    private INode<T,V> getParentSibling (INode <T, V> parent, INode <T, V> child) {

        return (parent.getLeftChild() == child) ? parent.getRightChild() : parent.getLeftChild() ;

    }

    private void newNode(INode<T,V> node, T key, V value) {

        node.setKey(key);
        node.setValue(value);
        node.setLeftChild(new Node<>());
        node.setRightChild(new Node<>());

    }

    private void recolorAndRotation(INode <T, V> node) {

        INode<T,V> parent = node.getParent();  INode<T,V> grandParent = parent.getParent();

        if (grandParent == null || parent.getColor() == INode.BLACK) return;

        INode<T,V> parentSibling = getParentSibling(grandParent,parent);

        if (parentSibling.getColor() == INode.RED) {

            parent.setColor(INode.BLACK);
            parentSibling.setColor(INode.BLACK);

            if (grandParent != getRoot()) {

                grandParent.setColor(INode.RED);
                recolorAndRotation(grandParent);

            }

        } else {

            // 7amza ana mesh mot5yl hn3mel eh hena fmstnyk tshr7ly
            // hena el mfrod el rotation

        }

    }

    @Override
    public void insert(T key, V value) {

        if (isEmpty()) {

            newNode(getRoot(),key,value);
            getRoot().setColor(INode.BLACK); return;

        }

        INode<T,V> node = find(getRoot(),key); // i search for the place where should the new node be

        if (!node.isNull()) { // i search for the node and find the same key so i upgrade its value

            node.setValue(value);

        } else {

            recolorAndRotation(node);

        }

    }

    @Override
    public boolean delete(T key) {
        return false;
    }
}
