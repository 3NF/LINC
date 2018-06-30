package Models;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class UploadedAssignment implements Iterable{

    HashMap<String, String> data;

    public void addAssignment(String fileName, String content) {
        data.put(fileName, content);
    }

    @Override
    public Iterator iterator() {
        Set<String> keys = data.keySet();
        return new Iterator() {
            @Override
            public boolean hasNext() {
                return !keys.isEmpty();
            }

            @Override
            public Object next() {
                return null;
            }
        };
    }
}
