package es.unizar.iaaa.biziapp.tareas;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase que contiene distintas herramientas a utilizar en varios de los procesos:
 * Tratamiento de ficheros.
 * Generacion de entradas en logs.
 */
public class Herramientas {

	public Herramientas(){}

	/**
	 * Renombrar el fichero anadiendole la fecha de la información que contiene
	 *
	 * @param downloadPath
	 *            donde se encuentra el fichero a renombrar
	 * @param nombreFichero
	 *            nombre que tiene el fichero a renombrar
	 * @param fecha
	 *            fecha que se quiere anadir al nombre (formato "dd/MM/yyyy")
	 * @return Ruta absoluta del fichero renombrado
	 */
	public String renameFile(String downloadPath, String nombreFichero, String fecha) {
		// Obtener path completo del fichero
		String path = downloadPath + System.getProperty("file.separator") + nombreFichero;
		File fichero = new File(path);
		String result = null;

		// Comprobar si existe el fichero
		if (fichero.exists()) {
			// Obtener nombre sin extension
			String nombreSinExt = nombreFichero.substring(0, nombreFichero.lastIndexOf("."));
			// Anadir al nombre la fecha y de nuevo la extension
			String nuevoNombre = nombreSinExt + fecha.replaceAll("/", "") + ".xls";
			// Renombrar
			File dest = new File(downloadPath + System.getProperty("file.separator") + nuevoNombre);
			fichero.renameTo(dest);
			result = dest.getAbsolutePath();
		}
		return result;
	}

    public int comprobarFichero(String pathCompleto) {
        int result;
        File fichero = new File(pathCompleto);
        System.out.println(pathCompleto);
        if (fichero.exists()) {
            result = 1;
        } else {
            result = -1;
        }
        return result;
    }

    /**
     * Encripta una cadena con SHA-256
     * @param cadena
     * @return cadena encriptada
     */
    public String generarHash(String cadena){

        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(cadena.getBytes("UTF-8"));
            byte[] digest = sha256.digest();
            StringBuffer sb=new StringBuffer();
            for (byte aDigest : digest) {
                sb.append(String.format("%02x", aDigest));
            }
            String hash = sb.toString();
            return hash;

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

}
