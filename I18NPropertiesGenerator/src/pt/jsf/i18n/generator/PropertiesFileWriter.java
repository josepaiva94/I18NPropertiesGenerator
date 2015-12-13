package pt.jsf.i18n.generator;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Writes properties to a file named i18n_{locale}.properties
 * 
 * @author josepaiva
 */
public class PropertiesFileWriter {
	public final static String OUTPUT_FOLDER = "resources/output/";
	
	private List<String> keys = null;
	
	public PropertiesFileWriter(List<String> keys) {
		this.keys = keys; 
	}
	
	/**
	 * Writes a properties file for a locale
	 * @param locale
	 * @param terms
	 * @throws Exception 
	 */
	public void write(String locale, List<String> terms) throws Exception {
		
		if (terms.size() != keys.size())
			throw new Exception("Number of terms and keys does not match!");
		
		// create output directory
		new File(OUTPUT_FOLDER).mkdirs();
		
		PrintWriter writer = new PrintWriter(OUTPUT_FOLDER + "i18n_" + locale + ".properties", "UTF-8");
		
		for (int i = 0; i < keys.size(); i++) {
			writer.println(keys.get(i) + " = " + terms.get(i));
		}
		
		writer.close();
	}
}
