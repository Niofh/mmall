import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Test {

    @org.junit.Test
    public void test(){
        HashMap<String, String> params = new HashMap<>();
        params.put("a","aaaaaaaaaa");
        params.put("brwer","bbbbbbbbbb");
        params.put("dfwefwf","bbbbbbbbbb");
        params.put("c123","bbbbbbbbbb");
        params.put("c","bbbbbbbbbb");


        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);

        for(int i = 0; i < keys.size(); ++i) {
            String key = (String)keys.get(i);
            String value = (String)params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }

        System.out.println(content.toString());
    }
}
