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

    private void newNode(INode<T,V> node, T key, V value) {

        node.setKey(key);
        node.setValue(value);
        node.setLeftChild(new Node<>());
        node.setRightChild(new Node<>());

    }

    @Override
    public void insert(T key, V value) {

        if (isEmpty()) {

            newNode(getRoot(),key,value);  return;

        }

        INode<T,V> node = find(getRoot(),key); // i search for the place where should the new node be

        if (!node.isNull()) { // i search for the node and find the same key so i upgrade its value

            node.setValue(value);

        } else {

            newNode(node,key,value);

            node.setColor(INode.RED);

            recolorAndRotation(node);

        }

    }

    @Override
    public boolean delete(T key) {
        return false;
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

    private INode<T,V> getSibling (INode <T, V> parent, INode <T, V> child) {

        return (parent.getLeftChild() == child) ? parent.getRightChild() : parent.getLeftChild() ;

    }

    private void rotation(INode<T,V> grandParent, INode<T,V> parent, INode<T,V> node) {

        int compare = grandParent.getKey().compareTo(parent.getKey()); // to compare the grandparent with parent

        if (compare == parent.getKey().compareTo(node.getKey())) {

            parent.setParent(grandParent.getParent());

            grandParent.setColor(INode.RED);
            parent.setColor(INode.BLACK);

            INode<T, V> sibling = getSibling(parent,node);

            sibling.setParent(grandParent);
            grandParent.setParent(parent);

            if (compare < 0) {

                grandParent.setRightChild(sibling);

                parent.setLeftChild(grandParent);


            } else {

                grandParent.setLeftChild(sibling);

                parent.setRightChild(grandParent);

            }

        } else {

            node.setParent(grandParent.getParent());

            INode<T, V> leftChild = node.getLeftChild();
            INode<T, V> rightChild = node.getRightChild();

            if (compare < 0) {

                node.setLeftChild(grandParent);
                node.setRightChild(parent);

                grandParent.setLeftChild(rightChild);

                parent.setRightChild(leftChild);

            } else {

                node.setRightChild(grandParent);
                node.setLeftChild(parent);

                grandParent.setRightChild(rightChild);

                parent.setLeftChild(leftChild);

            }

        }

    }

}
