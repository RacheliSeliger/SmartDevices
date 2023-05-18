package net.codejava.javaee;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.bson.types.ObjectId;
import org.json.JSONObject;


import org.bson.types.ObjectId;
import org.json.JSONObject;


public class SingletonCommandFactory {
	
    private final static SingletonCommandFactory instance = new SingletonCommandFactory();
    
    private Map<String, Function<JSONObject, ObjectId>> commands = new HashMap<>();;

    private SingletonCommandFactory() {
    }


    public void addCommand(String key, Function<JSONObject, ObjectId> command)
    {
    	commands.put(key, command);
    }

    public ObjectId doCommand(String key, JSONObject params)
    {
    	   Function<JSONObject, ObjectId> creator = commands.get(key);

           if (creator == null) {
               throw new IllegalArgumentException("No creator for key " + key);
           }
           return creator.apply(params);    
     }
    
    public static SingletonCommandFactory getFactory() {
    	return instance; 
    }
}