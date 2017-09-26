package es.unizar.iaaa.bizi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DescargaUnica {
	
//	private static String registerPath = System.getProperty("user.dir")+System.getProperty("file.separator")+ "register";

	public static void main(String[] args) throws IOException {

//		// Comprobar que la carpeta donde se generan los logs existe
//		File registerDirectory = new File(registerPath);
//		if (!registerDirectory.exists()) {
//			// Si no existe se crea
//			registerDirectory.mkdir();
//		}

		// Desde aqui se llamará a las descargas diarias de ficheros, indicando la fecha
		// a descargar
		// La respuesta obtenida desde la gestion de la descarga será almacenada en un
		// fichero log.
		// En caso de que el fichero log contenga entradas de descargas fallidas, se
		// intentaran descargar
		// una vez descargada la del día actual

		// Llamar a metodo de descarga y recoger lo que devuelve
		UsoEstaciones usoEstacion = new UsoEstaciones();
		String fecha = obtenerFecha();
		int result = usoEstacion.descargar(fecha);
		System.out.println(result);

//		// incluir linea en fichero log
//		if (result == 1) {
//			// incluir exito
//			fichero = fichero.concat("download.log");
//			entrada = MessageFormat.format(
//					"\"timestamp\":\"{0}\", \"estado\":\"{1}\" \"fichero\":\"{2}\", \"categoria\":\"Y\","
//							+ " \"subcategoria\":\"X\", \"intentos\":\"x\"",
//					timestamp.toString(), Estado.SUCCESSDOWNLOAD, fecha);
//			System.out.println("{" + entrada + "}");
//			entrada = "{" + entrada + "}\n";
//		} else if (result == -1) {
//			fichero = fichero.concat("error.log");
//			entrada = MessageFormat
//					.format("\"timestamp\":\"{0}\", \"estado\":\"{1}\" \"fichero\":\"{2}\", \"categoria\":\"Y\","
//							+ " \"subcategoria\":\"X\", \"intentos\":\"x\"", timestamp.toString(), Estado.ERROR, fecha);
//			System.out.println("{" + entrada + "}");
//			entrada = "{" + entrada + "}\n";
//		}
//
//		File archivo = new File(fichero);
//		if (archivo.exists()) { // incluye la nueva linea
//			System.out.println("if");
//			FileWriter fw = new FileWriter(archivo, true);
//			fw.write(entrada);
//			fw.close();
//		} else { // Crea el fichero si no existe e incluye la linea
//			System.out.println("else");
//			FileWriter fw = new FileWriter(archivo);
//			fw.write(entrada);
//			fw.close();
//		}

		// Lectura del fichero log en busca de alguna descarga fallida anterior

		// Si existe, realizar descarga

		// Si la descarga es realizada, modificar fichero log
	}

	/**
	 * Obtener fecha del dia anterior (según la fecha del sistema) Hace de manera
	 * correcta la resta, incluso en años bisiesto.
	 * 
	 * @return fecha en formato "dd/MM/yyyy"
	 */
	private static String obtenerFecha() {
		String fecha = null;
		// Obtener fecha del dia anterior
		LocalDate date = LocalDate.now().plusDays(-1);
		// Dar formato de salida
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		fecha = date.format(formatter);
		return fecha;
	}
}
