package pt.jsf.i18n.generator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class for creating JSF i18n properties files from 
 * files with terms
 * 
 * Files with terms should be named terms_{locale}(.txt| )
 * The files with keys should be named keys(.txt| ). If you don't specify a file
 * with the keys, the english terms file will be used to generate the keys. If there
 * is also no file with english terms, a "random" is selected
 * 
 * @author josepaiva
 */
public class I18NGenerator {

	public static final String REGEX_LANGUAGE_TERMS_FILE = "terms\\_([^\\.]+)(\\.txt)?$";
	public static final String REGEX_KEYS_FILE = "keys(\\.txt)?$";
	public static final Pattern PATTERN_LANGUAGE_TERMS_FILE = Pattern.compile(REGEX_LANGUAGE_TERMS_FILE);
	
	public static void main(String[] args) throws Exception {
		
		File[] termsFiles = getTermsFiles();
		
		if (termsFiles.length == 0)
			throw new Exception("Not a single file with terms was found!");
		
		File keysFile = getKeysFile();
		TermsFileReader keysFileReader = new TermsFileReader(keysFile.getPath());
		List<String> keys = keysFileReader.getKeysList();
		keysFileReader.close();
		
		PropertiesFileWriter fileWriter = new PropertiesFileWriter(keys);
		
		for (File file : termsFiles) {

			String filePath = file.getPath();
			TermsFileReader termsFileReader = new TermsFileReader(filePath);
			List<String> terms = termsFileReader.getTermsList();
			termsFileReader.close();
			
			Matcher matcher = PATTERN_LANGUAGE_TERMS_FILE.matcher(file.getName());
			if (matcher.matches())
				fileWriter.write(matcher.group(1),
						terms);
		}
		
		System.out.println("Files generated to resources/output folder");
	}

	/**
	 * Get the file which contains the keys for the properties file
	 * @return the file which contains the keys for the properties file
	 */
	private static File getKeysFile() {
		File resourcesDir = new File("resources");
	    File[] keysFiles = resourcesDir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.matches(REGEX_KEYS_FILE);
	        }
	    });
	    
	    if (keysFiles.length == 1)
	    	return keysFiles[0];
	    
	    keysFiles = resourcesDir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.matches(REGEX_LANGUAGE_TERMS_FILE);
	        }
	    });
	    
	    if (keysFiles.length == 1)
	    	return keysFiles[0];
	    
	    for (File file : keysFiles) {
			Matcher matcher = PATTERN_LANGUAGE_TERMS_FILE.matcher(file.getName());
			
			if (matcher.matches())
				if (matcher.group(1).indexOf("en") != -1)
					return file;
		}
	    
    	return keysFiles[0];
	}

	/**
	 * Get all files with terms present in resources directory
	 * @return an array of all files with terms present in resources directory
	 * @throws IOException 
	 */
	private static File[] getTermsFiles() throws IOException {

		File resourcesDir = new File("resources");
	    File[] termsFiles = resourcesDir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.matches(REGEX_LANGUAGE_TERMS_FILE);
	        }
	    });
		return termsFiles;
	}
}
