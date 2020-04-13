package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        System.out.println("hello world");

        TreeMap<Integer,Object> Map = new TreeMap<>();

        RedBlackTree<Integer,Object> tree = new RedBlackTree<>();
        Random r = new Random();
        for (int i = 0; i < 1; i++) {
            Random random = new Random();
            for (int j = 0; j < 200000; j++) {
                int key = random.nextInt(200000);
                tree.insert(key, "Manfi" + key);
            }
            for (int k = 0; k < 400; k++) {
                int n = r.nextInt(20000);
                long start = System.currentTimeMillis();
                for (int j = 0; j < n; j++) {
                    int  key = random.nextInt(200000);
                    tree.search(key);
                }
                long end = System.currentTimeMillis();
                Map.put(n,(end - start));
            }

        }

        Set<java.util.Map.Entry<Integer, Object>> set = Map.entrySet();
        int counter = 0;
        for (Map.Entry<Integer, Object> entry : set){
            if (counter%20 == 0) {
                System.out.println(entry.getKey()+"   "+entry.getValue());
            }
            counter++;
        }

    }

}
