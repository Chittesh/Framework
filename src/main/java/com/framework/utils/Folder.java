package com.framework.utils;

import java.io.File;
import java.io.IOException;

public class Folder {
	
	private String path;

	public Folder(String path) {
		this.path = path;
	}
	
	public void deleteFiles(String ... keywords) throws IOException {
		
		File folder  = new File(this.path);
		File[] files = folder.listFiles();
		
		for(File file : files){
			for (String keyword : keywords)
				if (file.getName().toLowerCase().contains(keyword))
					java.nio.file.Files.delete(file.toPath());
		}
		
	}

}
