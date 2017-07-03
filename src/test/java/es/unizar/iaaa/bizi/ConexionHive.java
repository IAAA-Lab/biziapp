package es.unizar.iaaa.bizi;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.DriverManager;


public class ConexionHive {
	
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		try {
			Class.forName(driverName);
		}catch (ClassNotFoundException e){
			e.printStackTrace();
			System.exit(1);
		}
		//replace "hive" here with the name of the user the queries should run as
	    Connection con = DriverManager.getConnection("jdbc:hive2://172.18.0.3:10000/default", "hive", "hive");
	    Statement stmt = con.createStatement();
	    String tableName = "testHiveDriverTable";
	    stmt.execute("drop table if exists " + tableName);
//	    stmt.execute("create table " + tableName + " (key int, value string)");
	    // show tables
	    // String sql = "show tables '" + tableName + "'";
	    String sql = ("show tables");
	    
//	    ResultSet res = stmt.executeQuery(sql);
//	    while (res.next()){
//	    	System.out.println(res.getString(1));
//	    }
	    
	    //Prueba de generacion de tabla propia y carga de datos
	    String nombre = "prueba";
	    String createTable = "create table " + nombre + " (id String, nombre String, apellido String)"
	    		+ "row format delimited fields terminated by \",\" lines terminated by \"\n\" "
	    		+ "tblproperties(\"skip.header.line.count\"=\"1\")";
//	    stmt.execute("drop table if exists " + nombre);
//	    stmt.execute(createTable);
	    
	    ResultSet res = stmt.executeQuery(sql);
	    while (res.next()){
	    	System.out.println(res.getString(1));
	    }
	    
	    // Desde HDFS
	    String loadData = "LOAD DATA INPATH '/user/root/data/hola.csv' INTO TABLE "+ nombre;
	    String loadData2 = "LOAD DATA INPATH '/user/root/data/adios.csv' INTO TABLE "+ nombre;
//	    stmt.execute(loadData);
//	    stmt.execute(loadData2);
	    String loadDataOverwrite = "LOAD DATA INPATH '/user/root/data/mono.csv' INTO TABLE "+ nombre;
//	    stmt.execute(loadDataOverwrite);
	    
	    // Desde sistema de fichero local
	    String loadDataLOCAL = "LOAD DATA LOCAL INPATH '/home/hola.csv' INTO TABLE "+ nombre;
//	    stmt.execute(loadDataLOCAL);
	    String loadDataLOCAL2 = "LOAD DATA LOCAL INPATH '/home/mono.csv' INTO TABLE "+ nombre;
//	    stmt.execute(loadDataLOCAL2);
	    String loadDataLOCAL3 = "LOAD DATA LOCAL INPATH '/home/adios.csv' INTO TABLE "+ nombre;
//	    stmt.execute(loadDataLOCAL3);
	    
	    String sqlSelectAll = "SELECT * FROM " + nombre + " ORDER BY id";
	    res = stmt.executeQuery(sqlSelectAll);
	    
	    
	    while (res.next()){
	    	System.out.println(res.getString(1) + " " + res.getString(2) + " " + res.getString(3));
	    }
	}
}
