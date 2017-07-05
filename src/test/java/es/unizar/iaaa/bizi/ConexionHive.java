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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// replace "hive" here with the name of the user the queries should run
		// as
		// Connection con =
		// DriverManager.getConnection("jdbc:hive2://172.18.0.3:10000/default",
		// "hive", "hive");
		Connection con = DriverManager.getConnection("jdbc:hive2://155.210.155.122:10000/default", "hive", "hive");
		Statement stmt = con.createStatement();

		// Prueba de generacion de tabla propia y carga de datos
		String nombre = "prueba";
//		String createTable = "create table " + nombre + " (id String, "
//				+ "nombre String, apellido String)row format delimited" + " fields terminated by \",\" "
//				+ "lines terminated by \"\n\" " + "tblproperties(\"skip.header.line.count\"=\"1\")";
		// stmt.execute("drop table if exists " + nombre);
		// stmt.execute(createTable);

		// ResultSet res = stmt.executeQuery(sql);
		// while (res.next()){
		// System.out.println(res.getString(1));
		// }

		// Desde HDFS
		// String loadData = "LOAD DATA INPATH '/user/root/data/hola.csv' INTO
		// TABLE "+ nombre;
		// String loadData2 = "LOAD DATA INPATH '/user/root/data/adios.csv' INTO
		// TABLE "+ nombre;
		// stmt.execute(loadData);
		// stmt.execute(loadData2);
		// String loadDataOverwrite = "LOAD DATA INPATH
		// '/user/root/data/mono.csv' INTO TABLE "+ nombre;
		// stmt.execute(loadDataOverwrite);

		// Desde sistema de fichero local
//		 String loadDataLOCAL = "LOAD DATA LOCAL INPATH '/home/hola.csv' OVERWRITE INTO TABLE "+ nombre;
//		stmt.execute(loadDataLOCAL);
		// String loadDataLOCAL2 = "LOAD DATA LOCAL INPATH '/home/mono.csv' INTO
		// TABLE "+ nombre;
		// stmt.execute(loadDataLOCAL2);
		// String loadDataLOCAL3 = "LOAD DATA LOCAL INPATH '/home/adios.csv'
		// INTO TABLE "+ nombre;
		// stmt.execute(loadDataLOCAL3);

		// String sqlSelectAll = "SELECT * FROM " + nombre + " ORDER BY id";
		// res = stmt.executeQuery(sqlSelectAll);
		//
		// while (res.next()){
		// System.out.println(res.getString(1) + " " + res.getString(2) + " " +
		// res.getString(3));
		// }

		// Prueba de creación e insercion de datos desde máquina exterior:
		
		String nombreTablaUsoEstaciones = "usoEstaciones";
		String createTableUsoEstaciones = "create table " + nombreTablaUsoEstaciones
				+ "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso String,"
				+ "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
				+ "retiradasMedia float, neto float, total float, fechaObtencionDatos String, ficheroCSV  String,"
				+ "ficheroXLS  String) "
				+ "row format delimited fields terminated by \",\" "
				+ "lines terminated BY \'\n\' "
				+ "tblproperties(\"skip.header.line.count\"=\"1\")";
		
		stmt.execute("drop table if exists " + nombreTablaUsoEstaciones);
		stmt.execute(createTableUsoEstaciones);
		
		String loadDataCSV = "LOAD DATA LOCAL INPATH '/compartida/usoEstacion_13062017.csv' INTO TABLE " + nombreTablaUsoEstaciones;
		stmt.execute(loadDataCSV);
		
		// Trasladar los datos de la tabla anterior a una table con formato de almacenamiento ORC
		String nombreTablaUsoEstacionesORC = "usoEstacionesORC";
		String createTableUsoEstacionesORC = "create table " + nombreTablaUsoEstacionesORC
				+ "(nombreCompleto String, idEstacion int, nombreEstacion String, fechaDeUso String,"
				+ "intervaloDeTiempo String, devolucionTotal int,devolucionMedia float,retiradasTotal int,"
				+ "retiradasMedia float, neto float, total float, fechaObtencionDatos String, ficheroCSV  String,"
				+ "ficheroXLS  String) STORED AS ORC";
		
		stmt.execute("drop table if exists " + nombreTablaUsoEstacionesORC);
		stmt.execute(createTableUsoEstacionesORC);
		
		String insertDataORC = "INSERT INTO TABLE " + nombreTablaUsoEstacionesORC + " select * from " + nombreTablaUsoEstaciones;
		stmt.execute(insertDataORC);
		
	}
}
