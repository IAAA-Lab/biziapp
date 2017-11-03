//package es.unizar.iaaa.bizi;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class MapToJson {
//
//	@Test
//	public void testMapToJson() throws JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, String> testMap = new HashMap<String, String>();
//		testMap.put("hola", "5");
//		testMap.put("adios", "564");
//		String json = mapper.writeValueAsString(testMap);
//		
//		File fil = new File (System.getProperty("user.dir")+System.getProperty("file.separator")+"prueba.log");
//		FileWriter fw;
//		try {
//			fw = new FileWriter(fil, true);
//			fw.write(json);
//			fw.append("\n");
//			fw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//}
