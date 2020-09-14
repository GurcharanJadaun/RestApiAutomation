package com.TestRetrieveTaskApi;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Utility.ResponseKeyFileManager;
import com.Utility.WorkbookHelper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RetrieveApi {
	List<String> listOfKeys = new ArrayList();
	
	Map<String, Object> projectList=new HashMap<String,Object>();
	Set<String> existingRecords=new HashSet<String>();
	String filePath="/target/TestOutput/Output.xls";

	@BeforeClass
	public void setupResourcesForTest() {

		ResponseKeyFileManager keyManager = new ResponseKeyFileManager();
		listOfKeys = keyManager.getKeys();
		if (listOfKeys.size() == 0) {
			Assert.assertTrue(false);
		}
		
		
	}

	@Test()
	public void getDataFromServer() {
		
		WorkbookHelper book = new WorkbookHelper();
		existingRecords=book.getRecordIdList(filePath);
		
		RequestSpecification requestSpec = new RequestSpecBuilder().build();
		requestSpec.baseUri("https://data.sfgov.org");
		requestSpec.basePath("resource/p4e4-a5a7.json");
		Response res = given().spec(requestSpec).get();
		System.out.println("Status Code : " + res.getStatusCode());
		JsonPath path = new JsonPath(res.body().asString());

		List<Object> list = path.getList("");
		HashMap<String, String> map = new HashMap<String, String>();

		Iterator obj = list.iterator();
		while (obj.hasNext()) {
			map = (HashMap<String, String>) obj.next();
			String record_id=map.get("record_id");
			//Check whether the data exists in output excel file or not,
			//if data isn't present then enter it into the list for entry
			if (!checkRecordWithExistingRecords(record_id)) {
				Map<String, String> createMapKeys = new HashMap<String, String>();
				Iterator keyIterator = listOfKeys.iterator();
				
				while (keyIterator.hasNext()) {
					String key = keyIterator.next().toString();
					createMapKeys.put(key, (map.get(key)!=null?map.get(key):""));
					// System.out.println(key+":>> "+map.get(key));
				}
				projectList.put(record_id, createMapKeys);
			}
			
			}
		}

	
	private boolean checkRecordWithExistingRecords(String target) {
		boolean result=false;
		Iterator move=existingRecords.iterator();
		while(move.hasNext() && !result) {
			result=move.next().toString().equals(target);
		}
		
		return result;
	}
	@AfterClass
	public void exportResults() {
		boolean result=true;
		if(projectList.size()>0) {		
			WorkbookHelper book = new WorkbookHelper();
			existingRecords=book.getRecordIdList(filePath);
		result=book.addProjectInList(filePath, projectList);
		
		//System.out.println("Output File : " + book.getRecordIdList(filePath));
		}
		System.out.println("No of records added : "+projectList.size());
		Assert.assertTrue(result);
		
	}

}
