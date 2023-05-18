package net.codejava.javaee;

import java.util.Arrays;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


public class mongoDBHendler implements IMongoCrud{
	
	MongoDatabase database = null;

	public mongoDBHendler(MongoDatabase database) {
		this.database = database;
	}

	@Override
	public ObjectId create(JSONObject json, String collectionName) {
		
	    MongoCollection<Document> collection = database.getCollection(collectionName);
	    Document doc = Document.parse(json.toString());

	    String[] data = {}; 
	    doc.append("Data", Arrays.asList(data));

	    collection.insertOne(doc);
	    return doc.getObjectId("_id");
	}
	



	@Override
	public ObjectId update(JSONObject json, String collectionName) {
		
	    MongoCollection<Document> collection = database.getCollection(collectionName);

	    String deviceDataId = json.getString("iot_id");

	    collection.updateOne(
	            Filters.eq("iot_id", deviceDataId),
	            Updates.push("Data", json.getString("data"))
	    );

	    Document filter = new Document("iot_id", deviceDataId);
	    Document result = collection.find(filter).first();
	    ObjectId objectId = result.getObjectId("_id");

	    return objectId;
		
	}
	
	@Override
	public ObjectId read(JSONObject json, String collectionName) {
		
		return null;
	}

	@Override
	public ObjectId delete(JSONObject json, String collectionName) {
		return null;
	}
	
}
