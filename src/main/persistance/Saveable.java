package persistance;

import org.json.JSONObject;

/**
 * Interface that represents a class that can be converted to Json
 */
public interface Saveable {
    
    JSONObject toJson();
}
