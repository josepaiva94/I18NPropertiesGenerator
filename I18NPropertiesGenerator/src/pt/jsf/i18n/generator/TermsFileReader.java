package pt.jsf.i18n.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a file with terms
 * 
 * @author josepaiva
 */
public class TermsFileReader {
	private static final Charset ENCODING = StandardCharsets.UTF_8;

	private BufferedReader reader = null;
	private LineNumberReader lineReader = null;
	
	public TermsFileReader(String filePath) throws IOException {
		Path resourcesPath = new File(filePath).getAbsoluteFile()
				.toPath();
		reader = Files.newBufferedReader(resourcesPath,
				ENCODING);
		lineReader = new LineNumberReader(reader);
	}
	
	/**
	 * Get a list of terms from a file
	 * @return
	 * @throws IOException 
	 */
	public List<String> getTermsList() throws IOException {
		
		List<String> termsList = new ArrayList<>();
		String term = null;
		while ((term = getNextTerm()) != null) {
			termsList.add(term);
		}
		
		return termsList;
	}
	
	/**
	 * Get a list of keys from a file
	 * @return
	 * @throws IOException 
	 */
	public List<String> getKeysList() throws IOException {
		
		List<String> keysList = new ArrayList<>();
		String key = null;
		while ((key = getNextKey()) != null) {
			keysList.add(key);
		}
		
		return keysList;
	}
	
	/**
	 * Get next term from file
	 * @return next term from file
	 * @throws IOException 
	 */
	private String getNextTerm() throws IOException {
		String line = lineReader.readLine();
		
		if (line == null)
			return null;
		
		return encodeNativeToASCII(line);
	}
	
	/**
	 * Get next key from file
	 * @return next key from file
	 * @throws IOException 
	 */
	private String getNextKey() throws IOException {
		String line = lineReader.readLine();
		
		if (line == null)
			return null;
		
		return normalizeKey(line);
	}
	
	/**
	 * Encode string to ASCII
	 * @param term in native language
	 * @return encoded string to ASCII
	 */
	private String encodeNativeToASCII(String term) {
		
	    final CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
	    final StringBuilder result = new StringBuilder();
	    for (final Character character : term.toCharArray()) {
	        if (asciiEncoder.canEncode(character)) {
	            result.append(character);
	        } else {
	            result.append("\\u");
	            result.append(Integer.toHexString(0x10000 | character).substring(1).toUpperCase());
	        }
	    }
	    
	    return result.toString();
	}
	
	/**
	 * Normalizes a string to a valid property key
	 * @return
	 */
	private String normalizeKey(String key) {
		
		return Normalizer
			           .normalize(key, Normalizer.Form.NFD)
			           .replaceAll("\\s", "\\\\ ")
			           .replaceAll("[^\\p{ASCII}]", "");
	}
	
	/**
	 * Close the file reader
	 * @throws IOException 
	 */
	public void close() throws IOException {
		reader.close();
		lineReader.close();
	}
}
