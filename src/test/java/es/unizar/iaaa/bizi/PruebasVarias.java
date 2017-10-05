package es.unizar.iaaa.bizi;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.apache.log4j.Logger;

import com.google.inject.spi.Message;

public class PruebasVarias {

//	final static Logger logger = Logger.getLogger(PruebasVarias.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		System.out.println(System.getProperty("file.separator"));
//		
//		System.out.println(System.getProperty("user.dir"));
		
		// Prueba para la obtencion de fechas
		
//		Date hoy = new Date();
//		Date ayer = new Date( hoy.getTime()-86400000);
//		System.out.println("Ayer fue: " + new SimpleDateFormat("dd-MM-yyyy").format(ayer));
		
//		LocalDate date = LocalDate.of(2017, 01, 01); //1989-11-11
//		date = date.plusDays(-1);
//		LocalDate date = LocalDate.now().plusDays(-1);
//		System.out.println(date.getYear());
//		System.out.println(date.getMonth());
//		System.out.println(date.getDayOfMonth());
//		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		String formattedString = date.format(formatter);
//		System.out.println(formattedString);
//		
//		String fecha = "20/03/2017";
//		String[] fechaSeparada = fecha.split("/");
//		System.out.println(fechaSeparada[0]);
//		System.out.println(fechaSeparada[1]);
//		System.out.println(fechaSeparada[2]);
//		
//		String nuevaFecha = MessageFormat.format("{0}-{1}-{2}",fechaSeparada[2],fechaSeparada[1], fechaSeparada[0]);
//		
//		System.out.println(nuevaFecha);
		
//		System.out.println(FileSystems.getDefault().getPath("/home/dani/git/UNIZAR-TFG-BIZI/csvFiles/3.1-Usos de las estaciones01102017_02102017.csv"));
		
//		System.out.println(System.getProperty("user.dir"));
		
//		try{
//			System.out.println(logger.getEffectiveLevel());
//			System.out.println(logger.isTraceEnabled());
//			int i= 10/0;
//			
//		}catch(ArithmeticException ex){
//			logger.error("Sorry, something wrong!");
//			logger.trace("AQUI");
//			logger.debug("Sorry, something wrong!");
//			logger.fatal("FATAAAAAAAAAAAAAAAAAAAAL");
//		}
	}

}
