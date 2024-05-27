package hellyeah;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private Map<String, Integer> variableValues = new HashMap<>();

    public int getVariableValue(String name) {
        if (variableValues.containsKey(name)) {
            return variableValues.get(name);
        } else {
            throw new RuntimeException("Undefined variable: " + name);
        }
    }

    public void setVariableValue(String name, int value) {
        System.out.println("context put worked!");
        variableValues.put(name, value);
    }

    @Override
    public String toString() {
        return variableValues.toString();
    }
}