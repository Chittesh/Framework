package com.framework.utils.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.framework.utils.TestReporter;

public class FileLoader {
	
	private FileLoader() {
		
	}
	
	public static String loadFileFromProjectAsString(String filePath) throws IOException {

		InputStream resource = FileLoader.class.getResourceAsStream(filePath);
		
		if (resource == null) {
			resource = FileLoader.class.getResourceAsStream("/" + filePath);
			if (resource == null) {
				throw new FileNotFoundException("Failed to find resource [ " + filePath + " ]");
			}
		}

		String fileContents = "";
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))){
			
			String line;
			StringBuilder sb = new StringBuilder();

			try {
				
				while ((line = br.readLine()) != null) {
					sb.append(line.trim());
				}
				
				fileContents = sb.toString();
				
			} catch (IOException e) {
				throw new IOException("Failed to read from file [ " + filePath + " ]", e);
			}
		}
		
		return fileContents;
		
	}
}