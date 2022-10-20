package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBHandler extends Configs {
	
	Connection dbconnection;
	
	public Connection getConnection()
	{
	
	String connectionString = "jdbc:mysql://" + Configs.dbhost + ":" + Configs.dbport + "/" + Configs.dbname + "?autoReconnect=true&useSSL=false";     

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
		dbconnection = DriverManager.getConnection(connectionString,Configs.dbuser,Configs.dbpass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dbconnection;
}
}