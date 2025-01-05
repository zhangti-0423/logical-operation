package logic.core;

import java.util.HashMap;
import java.util.Map;

// 参数
public class Context {

    private final Map<String, Object> facts = new HashMap<>();

    public void addFact(String key, Object value) {
        facts.put(key, value);
    }

    public Object getFact(String key) {
        return facts.get(key);
    }

    @Override
    public String toString() {
        return "Context{" +
                "facts=" + facts +
                '}';
    }
}


