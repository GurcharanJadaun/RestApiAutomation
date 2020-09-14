package com.Utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ResponseKeyFileManager {

	public List<String> getKeys() {
		// String output="";
		List<String> keys = new ArrayList<String>();
		try {
			JSONParser parser = new JSONParser();
			JSONObject object = (JSONObject) parser
					.parse(new FileReader("src/test/resources/config/" + "RetrieveFields.json"));
			Map<String, String> mapKeys = (Map<String, String>) object.get("KeySet1");
			Iterator i = mapKeys.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry tmp = (Map.Entry) i.next();
				if (tmp.getValue() != null) {
					keys.add(tmp.getValue().toString());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Excpetion:>>" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return keys;
	}

}
