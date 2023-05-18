package net.codejava.javaee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bson.types.ObjectId;
import org.json.JSONObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;



public class GenericIotUtils {
	

	static SingletonCommandFactory commandFactory  = SingletonCommandFactory.getFactory();
	private static ThreadPool<Object> threadPool = new ThreadPool<>(10);
	

	public static JSONObject CreateJsonObj(HttpServletRequest request) throws IOException {
		
        InputStream inputStream = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
    
        String jsonStr = stringBuilder.toString();
        return (new JSONObject(jsonStr));
        
    }
	
	public static void GetCommandFromJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
        Callable taskCallable = new Callable() {
        	   @Override
            public Object call() throws Exception {
				String jsonData = (String) request.getAttribute("jsonData");
				JSONObject jsonObject = new JSONObject(jsonData);
				String command = jsonObject.getString("command");
				ObjectId rows = commandFactory.doCommand(command, jsonObject);
				return rows;
        	}
        };
        threadPool.submit(taskCallable);
	}
	
}
