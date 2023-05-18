package net.codejava.javaee;

import org.bson.types.ObjectId;
import org.json.JSONObject;

public interface IMongoCrud {

	public ObjectId create(JSONObject json, String collectionName);
	
	public ObjectId read(JSONObject json, String collectionName);
	
	public ObjectId update(JSONObject json, String collectionName);
	
	public ObjectId delete(JSONObject json, String collectionName);
}
