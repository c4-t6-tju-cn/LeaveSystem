package cn.edu.tju.t6.c4.base;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DBOperator {

	private String driver;

	private String url;
	
	private String user;
	
	private String password;
	
	private Connection conn;
	
	public DBOperator(){
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("/config/jdbc.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		driver = prop.getProperty("jdbc.driverClassName");
		url = prop.getProperty("jdbc.url");
		user = prop.getProperty("jdbc.username");
		password = prop.getProperty("jdbc.password");
	}
	
	public DBOperator(String driver, String url, String user, String password){
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public Connection connection() throws SQLException{
		try {
			Class.forName(driver);
			
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		}
		catch(SQLException | ClassNotFoundException e){
			throw new SQLException("SQL connection can't connect!");
		}
	}
	private String head(String sql){
		String str = "sql @ [" + CommonConst.getCurrentTime() +  "]>\n" + sql + "\n\n";
		return str;
	}
	public ResultSet select(String sql){
		try {
			if(conn == null || conn.isClosed())	conn = connection();
			if(conn == null)	return null;
			Statement statement = conn.createStatement();
			System.out.print(head(sql));
			return statement.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("SQLException: "+sql+" NOT RIGHT!");
			return null;
		}
	}
	
	public boolean add(String sql) {
		try {
			Connection conn = connection();
		
			if(conn == null)	return false;
		
			Statement statement = conn.createStatement();
			System.out.print(head(sql));
			return statement.execute(sql);
		} catch (SQLException e) {
			System.out.println("SQLException: "+sql+" NOT RIGHT!");
			//return null;
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("SQL connection can't close!");
			}
		}
		return false;
	}
	public String getItemResult(ResultSet rs, String item){
		String result = null;
		try{
			result = rs.getString(item);
		}catch (Exception e){
			System.out.println("No this item : " + item);
		}
		return result;
	}
	public boolean close() throws SQLException{
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			System.out.println("SQL connection can't close!");
		}
		return false;
	}
	//same to add
	public boolean update(String sql) throws SQLException{
		return add(sql);
	}

	//same to add
	public boolean delete(String sql) throws SQLException{
		return add(sql);
	}
}
