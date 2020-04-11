package com.ppe.utility;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;

public class ExtractDatabaseTable {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		String tableName="Web_Request_QuickQuote";
		//String tableName="Web_Request_Log";
		//String tableName="Web_Request_LoanCompare";
		ExtractDatabaseTable objExtractDatabaseTable=new ExtractDatabaseTable();
		ArrayList<LinkedHashMap<String, String>> mapOfaTableRow;
		Connection conn = null;
		
		DataBaseUtils objDb=new DataBaseUtils();
		
		try {
		conn=objDb.init();
		
		mapOfaTableRow=objDb.getResultSetAsMap(conn, tableName);
				
		objExtractDatabaseTable.generateJsonFileFromDatabaseTable(tableName,mapOfaTableRow);
		System.out.println("Json file extraction from database is complete");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
	}

	
	void generateJsonFileFromDatabaseTable(String tableName,ArrayList<LinkedHashMap<String, String>> mapOfaTableRow)
	{
		String outputJsonFile=tableName+".json";
		ObjectMapper objectMapper = new ObjectMapper();
			
		 try {
			objectMapper.writeValue(new File(outputJsonFile), mapOfaTableRow);
		} catch (JsonGenerationException e) {			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
