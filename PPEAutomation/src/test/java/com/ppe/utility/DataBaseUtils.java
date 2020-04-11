package com.ppe.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import com.tavant.base.WebPage;
import com.tavant.utils.PropertiesUtil;

public class DataBaseUtils extends WebPage{


	private static DataBaseUtils _instance = null;
	private Connection con = null;
	private final String DB_PROPS_FILE =System.getProperty("user.dir")+"\\"+ "db.properties";
	private static Properties dbprops = new Properties();
	
	public void loadDBProperties() throws IOException {
       /* ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream file = loader.getResourceAsStream(DB_PROPS_FILE);*/
		FileInputStream fileInput = new FileInputStream(DB_PROPS_FILE);
		dbprops.load(fileInput);        
}


	/*
	 * Initiating the Data base connection by using the property values from
	 * resource package.
	 */
	
	public Connection init()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		String statusIntegratedAuthentication;
		String url;
		
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		

			loadDBProperties();
			//String env = getEnvironment();
			statusIntegratedAuthentication=dbprops.getProperty("IntegratedAuthentication");
			
			String env = dbprops.getProperty("Env").trim();
			String dbserver = dbprops.getProperty("DBserver_"+env).trim();
			String dbname = dbprops.getProperty("DBName_"+env).trim();
			String dbuname = dbprops.getProperty("DBusername_"+env).trim();
			String dbpass = dbprops.getProperty("DBpassword_"+env).trim();
			
			if(statusIntegratedAuthentication.equalsIgnoreCase("yes")) {
				url = "jdbc:sqlserver://"+dbserver+";databaseName="+dbname+";integratedSecurity=true";
				this.con = DriverManager.getConnection(url);
			}
			else {
				url = "jdbc:sqlserver://"+dbserver+";databaseName="+dbname;
				this.con = DriverManager.getConnection(url, dbuname, dbpass);
			}			
			
			
			return con;

		
	}
	public Connection initEncompassDatabase()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


			//loadDBProperties();
			Properties props = new Properties();		    
			String propFilePath = System.getProperty("user.dir")+"\\"+"db.properties";
			FileInputStream fileInput = new FileInputStream(propFilePath);
			props.load(fileInput);
			
			
						
			String env =props.getProperty("Env").trim();
			String dbserver = props.getProperty("DBserver_"+env).trim();
			//dbserver=dbserver.replace("-","\\");
			String dbname = props.getProperty("DBName_"+env+"_Encompass").trim();
			String dbuname = props.getProperty("DBusername_"+env).trim();
			String dbpass = props.getProperty("DBpassword_"+env).trim();
			String url = "jdbc:sqlserver://"+dbserver+";databaseName="+dbname;
			System.out.println("url>>" + url);
			return this.con = DriverManager.getConnection(url, dbuname, dbpass);

		
	}
	
	/**
	 * Initialize DB for a specific db name. Pass parameter dbname
	 * @return 
	 * 
	 */
	public Connection initForDbName(String dbname)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


		loadDBProperties();
			String env = dbprops.getProperty("Env").trim();
			String dbserver = dbprops.getProperty("DBserver_"+env).trim();
//			String dbname = dbprops.getProperty("DBName_"+env).trim();
			String dbuname = dbprops.getProperty("DBusername_"+env).trim();
			String dbpass = dbprops.getProperty("DBpassword_"+env).trim();
			String url = "jdbc:sqlserver://"+dbserver+";databaseName="+dbname;
			System.out.println("url>>" + url);
			return this.con = DriverManager.getConnection(url, dbuname, dbpass);

		
	}
	
	public String getEnvironment() {
		String url = PropertiesUtil.getProperty("url");
		
		String env = null;
		if (url.contains("qch2o"))
			env = "UAT";
		if (url.contains("uat2h2o"))
			env = "UAT2";
		if(url.contains("cnvd"))
			env = "SIT";
		if(url.contains("uat3h2o"))
			env = "UAT3";
		
		System.out.println("ENVIRONMENT == "+env);
		return env;
	}
	
	public String getDatabaseEnvironment() {
		String url = PropertiesUtil.getProperty("url");
		
		String env = null;
		if (url.contains("qch2o"))
			env = "UAT1";
		if (url.contains("uat2h2o"))
			env = "UAT2";
		if(url.contains("cnvd"))
			env = "SIT";
		if(url.contains("uat3h2o"))
			env = "UAT3";
		
		System.out.println("ENVIRONMENT == "+env);
		return env;
	}
	String getSingleDBValue(Connection con,String query) throws SQLException
	{
	String val = null;
	Statement st = null;
	ResultSet rs = null;
	try{
			if (con != null) 
			{
				st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);			
			}
			if(rs.next()) 
			{
				val=rs.getString(1);
			}
		}
	catch(Exception e)
	{
	System.out.println(e);
	}
	finally
	{
		st.close();
		rs.close();		
	}

	return val;
	}
	List<String> getResultList(Connection con,String query) throws SQLException
	{
		Statement st = null;
		ResultSet rs = null;
		ResultSetMetaData mt = null;
		
		List<String> ls=new ArrayList<String>();
		try{
			if (con != null) 
			{
				st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
				mt=rs.getMetaData();
			}
			if(rs.next()) 
			{
				for(int i=1;i<=4;i++)
					ls.add(rs.getString(i));				
			}
		}
	catch(Exception e)
	{
	System.out.println(e);
	}
	finally
	{
		st.close();
		rs.close();		
	}
		return ls;
	}
	public static void main(String args[]) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		DataBaseUtils objDb=new DataBaseUtils();
		Connection con=objDb.init();
		ArrayList<LinkedHashMap<String, String>> listOfMap=objDb.getResultSetAsMap(con, "Web_Request_QuickQuote");
		LinkedHashMap<String, String> map=listOfMap.get(0);
		System.out.println(map);
		
	}
	ArrayList<LinkedHashMap<String, String>> getResultSetAsMap(Connection con,String TableName) throws SQLException
	{
		
		ArrayList<String> listColumnName=getColumnListFromTableName(con, TableName);
		ArrayList<LinkedHashMap<String, String>> list_map_RowsOfTable=new ArrayList<LinkedHashMap<String, String>>();
		
		LinkedHashMap<String,String>  map;
		//String arrColumnList[]="WebRequestLogId,LoanTypesId,LoanTypes,AmortizationTypeId,AmortizationType,AmortizationTermId,AmortizationTerm,DocumentationTypeId,DocumentationType,LoanProgramId,LoanProgram,LoanPurposeId,LoanPurpose,OccupancyPurposeId,OccupancyPurpose,NumberOfUnits,StateId,State,CountyId,County,PropertyTypeId,PropertyType,BaseLoanAmount,MIPFundingPercent,MIPFundingCash,IsLock,AmountPaidInCash,MIPFundingFeeFinanced,TotalLoanAmount,EstimatedAppraised,PurchasePrice,LTVPercent,CLTVPercent,SecondLien,CreditScoreUsed,DebtToIncomePercent,AUSSystemId,AUSSystem,Rate,LockPeriodId,LockPeriod,WaiveEscrow".split(",");
		ResultSet rs;
		Statement st=con.createStatement();
		
		String queryOFTable="Select * from "+TableName;
		rs=st.executeQuery(queryOFTable);
		String cellValue;
		while(rs.next())
		{
			map=new LinkedHashMap<String,String>();
			for(String tableColumn:listColumnName)
			{
				cellValue=rs.getString(tableColumn);
				map.put(tableColumn,cellValue);				
			}
			list_map_RowsOfTable.add(map);			
		}
		return list_map_RowsOfTable;
		
	}
	private ArrayList<String> getColumnListFromTableName(Connection con,String tableName) throws SQLException {
		ArrayList<String> list=new ArrayList<String>();
		String query="SELECT name FROM sys.columns WHERE object_id = OBJECT_ID('"+tableName+"')";
		ResultSet rs;
		Statement st=con.createStatement();
		rs=st.executeQuery(query);
		String columnName;
		while(rs.next())
		{
			columnName=rs.getString(1);
			list.add(columnName);
		}
				
		
		return list;
	}


	@Override
	public void checkPage() {
		// TODO Auto-generated method stub
		
	}

}
