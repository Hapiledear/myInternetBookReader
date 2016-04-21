package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnection {
	private static final String DBDRIVER="com.mysql.jdbc.Driver";
	private static final String DBURL="jdbc:mysql://127.0.0.1:3306/e_book_db?&relaxAutoCommit=true";
	private static final String DBUSER="root";
	private static final String DBPASSWORD="root";
	private Connection conn=null;
	public DatabaseConnection()throws Exception{
		Class.forName(DBDRIVER);
		this.conn=DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);
	}
	public Connection getConnection(){
		return this.conn;
	}
	public void close() throws Exception{
		if(this.conn!=null){
			this.conn.close();
		}
	}
}
