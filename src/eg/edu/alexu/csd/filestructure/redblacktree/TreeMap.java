package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TreeMap <T extends Comparable<T> , V > implements ITreeMap<T,V> {

    private IRedBlackTree<T, V> redBlackTree;
    private int size;

    TreeMap () {

        redBlackTree = new RedBlackTree<>();
        size = 0;

    }

    private void throwException(Object value) {
        if (value == null) throw new RuntimeErrorException(null);
    }

    private INode<T,V> search(char how,T key) {

        throwException(key);
        INode<T,V> node = redBlackTree.getRoot(),ceilingNode = null,floorNode = null;;

        while (!node.isNull()) {

            int compare = key.compareTo(node.getKey());

            if (compare == 0) {
                ceilingNode = floorNode = node; break;
            } else if(compare < 0) {
                ceilingNode = node; node = node.getLeftChild();
            } else {
                floorNode = node; node = node.getRightChild();
            }

        }

        if (how == 'F') {
            return floorNode;
        }
        return ceilingNode;

    }

    private Map.Entry<T,V> buildEntry(INode<T,V> node) {
        if (node == null) return null;
        return new AbstractMap.SimpleEntry<>(node.getKey(), node.getValue());
    }

    private T individualKey (Map.Entry<T,V> entry) {

        if (entry == null) return null;
        return entry.getKey();

    }

    private Map.Entry<T,V> fiLaEntry(char how) {

        if (size() == 0) return null;

        INode<T,V> node = redBlackTree.getRoot();

        if (how == 'F') {
            while(!node.getLeftChild().isNull()) {
                node = node.getLeftChild();
            }
        } else {
            while(!node.getRightChild().isNull()) {
                node = node.getRightChild();
            }
        }

        return buildEntry(node);

    }

    @Override
    public Map.Entry<T, V> ceilingEntry(T key) {
        return buildEntry(search('C',key));
    }

    @Override
    public T ceilingKey(T key) {
        return individualKey(ceilingEntry(key));
    }

    @Override
    public void clear() {
        redBlackTree.clear(); size = 0;
    }

    @Override
    public boolean containsKey(T key) {
        return redBlackTree.search(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        throwException(value);
        Collection<Map.Entry<T,V>> entries = new ArrayList<>();
        traverse(redBlackTree.getRoot(),entries);
        Iterator<Map.Entry<T,V>> iterator  = entries.iterator();

        while (iterator.hasNext()) {
            if (value.equals(iterator.next().getValue())) return true;
        }
        return false;
    }

    private void traverse(INode<T,V> node,Collection<Map.Entry<T,V>> entries) {

        if (node.isNull()) return;

        traverse(node.getLeftChild(),entries);
        entries.add(buildEntry(node));
        traverse(node.getRightChild(),entries);

    }

    @Override
    public Set<Map.Entry<T, V>> entrySet() {
        Set<Map.Entry<T,V>> entries = new LinkedHashSet<>(size());
        traverse(redBlackTree.getRoot(),entries);
        return entries;
    }

    @Override
    public Map.Entry<T, V> firstEntry() {
        return fiLaEntry('F');
    }

    @Override
    public T firstKey() {
        return individualKey(firstEntry());
    }

    @Override
    public Map.Entry<T, V> floorEntry(T key) {
        return buildEntry(search('F',key));
    }

    @Override
    public T floorKey(T key) {
        return individualKey(floorEntry(key));
    }

    @Override
    public V get(T key) {
        return redBlackTree.search(key);
    }

    @Override
    public ArrayList<Map.Entry<T, V>> headMap(T toKey) {
        return headMap(toKey,false);
    }

    @Override
    public ArrayList<Map.Entry<T, V>> headMap(T toKey, boolean inclusive) {

        ArrayList<Map.Entry<T,V>> entries = new ArrayList<>();
        Collection<Map.Entry<T,V>> entries1 = new ArrayList<>();
        traverse(redBlackTree.getRoot(),entries1);

        Iterator<Map.Entry<T,V>> itr = entries1.iterator();
        while (itr.hasNext())
        {
            Map.Entry<T,V> entry = itr.next(); int compare = toKey.compareTo(entry.getKey());
            if(compare > 0){
                entries.add(entry);
            } else if (inclusive && compare == 0){
                entries.add(entry); break;
            } else break;
        }
        return entries;

    }

    @Override
    public Set<T> keySet() {

        Set<T> set = new LinkedHashSet<>(size());
        Collection<Map.Entry<T,V>> entries = new ArrayList<>();
        traverse(redBlackTree.getRoot(),entries);
        Iterator<Map.Entry<T,V>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            set.add(iterator.next().getKey());
        }
        return set;
    }

    @Override
    public Map.Entry<T, V> lastEntry() {
        return fiLaEntry('L');
    }

    @Override
    public T lastKey() {
        return individualKey(lastEntry());
    }

    private Map.Entry<T, V> poll(char how) {
        if (size() == 0) return null;
        Map.Entry<T,V> entry;

        if (how == 'F') {
            entry = firstEntry();
        } else {
            entry = lastEntry();
        }
        remove(entry.getKey());
        return entry;
    }

    @Override
    public Map.Entry<T, V> pollFirstEntry() {
        return poll('F');
    }

    @Override
    public Map.Entry<T, V> pollLastEntry() {
        return poll('L');
    }

    @Override
    public void put(T key, V value) {
        if (!containsKey(key)) size++;
        redBlackTree.insert(key,value);
    }

    @Override
    public void putAll(Map<T, V> map) {
        throwException(map);
        for(Map.Entry<T,V> entry : map.entrySet()) {
            put(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public boolean remove(T key) {
        if (redBlackTree.delete(key)) {
            size --; return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Collection<V> values() {
        Collection<V>  collections = new ArrayList<>(size());
        Collection<Map.Entry<T,V>> entries = new ArrayList<>();
        traverse(redBlackTree.getRoot(),entries);
        Iterator<Map.Entry<T,V>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            collections.add(iterator.next().getValue());
        }
        return collections;
    }
}
