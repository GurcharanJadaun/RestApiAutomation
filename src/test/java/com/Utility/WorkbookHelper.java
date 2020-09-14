package com.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
public class WorkbookHelper {

	public boolean addProjectInList(String filePath,Map<String,Object> listOfProjects) {
		
		boolean result=false;
		try {
			String fileName=System.getProperty("user.dir")+filePath;
			File file=new File(fileName);
			FileInputStream fis=new FileInputStream(file);
			HSSFWorkbook repository=new HSSFWorkbook(fis);
			HSSFSheet workSheet=repository.getSheet("Sheet1");
			int rowNumber=workSheet.getLastRowNum();
			
			Iterator iterateProjectId=listOfProjects.entrySet().iterator();
			while(iterateProjectId.hasNext()) {
				Map.Entry entry=(Entry) iterateProjectId.next();
				String recordId=(String) entry.getKey();
				
				Map<String,String> tmp=(Map<String, String>) entry.getValue();
				Iterator iterateList=tmp.entrySet().iterator();
				//Enter data into Excel
				while(iterateList.hasNext()) {
					Map.Entry data=(Entry) iterateList.next();
					rowNumber++;
					Row row=workSheet.createRow(rowNumber);
					row.createCell(0).setCellValue(recordId);
					row.createCell(1).setCellValue(data.getKey().toString());
					row.createCell(2).setCellValue(data.getValue().toString());
				}
			}
			fis.close();
			Thread.sleep(500);
			FileOutputStream fos=new FileOutputStream(fileName);
			repository.write(fos);
			fos.close();
			result=true;
			
		} catch (Exception ex) {
			System.out.println("<<Exception>>");
			ex.printStackTrace();
		}
		return result;

	}
	
	public Set<String> getRecordIdList(String filePath) {
		
		 Set<String> listOfRecordId = new HashSet<String>(); 
		try {
			File file=new File(System.getProperty("user.dir")+filePath);
			FileInputStream fis=new FileInputStream(file);
			HSSFWorkbook repository=new HSSFWorkbook(fis);
			HSSFSheet workSheet=repository.getSheet("Sheet1");
			Iterator it=workSheet.rowIterator();
			
			it.next();
			while(it.hasNext()) {
				Row row=(Row) it.next();
				String recordId=row.getCell(0).getStringCellValue();
				
				if(recordId!=null) {
					listOfRecordId.add(recordId.trim());
				}
			}
			fis.close();
		}
			catch(Exception ex) {
				System.out.println("Work book Exception : "+ex.getStackTrace());
			}
	
		return listOfRecordId;}

}
