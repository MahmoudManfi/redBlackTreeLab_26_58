package eg.edu.alexu.csd.filestructure.redblacktree;

public class Node <T extends Comparable<T>, V> implements INode <T,V> {

    private INode<T, V> parent;
    private INode<T, V> leftChild;
    private INode<T, V> rightChild;
    private boolean color = RED;
    private T key;
    private V value;

    @Override
    public void setParent(INode<T, V> parent) {

        this.parent = parent;

    }

    @Override
    public INode<T, V> getParent() {
        return parent;
    }

    @Override
    public void setLeftChild(INode<T, V> leftChild) {

        this.leftChild = leftChild;

    }

    @Override
    public INode<T, V> getLeftChild() {
        return leftChild;
    }

    @Override
    public void setRightChild(INode<T, V> rightChild) {

        this.rightChild = rightChild;

    }

    @Override
    public INode<T, V> getRightChild() {
        return rightChild;
    }

    @Override
    public T getKey() {
        return key;
    }

    @Override
    public void setKey(T key) {

        this.key = key;

    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public void setValue(V value) {

        this.value = value;

    }

    @Override
    public boolean getColor() {
        return color;
    }

    @Override
    public void setColor(boolean color) {

        this.color = color;

    }

    @Override
    public boolean isNull() {
        return (key == null);
    }
}
