package com.framework.utils;

import java.io.File;
import java.net.URL;

public class DriverFile {

	public String getPath(String resource) {
		return getFile(resource).getAbsolutePath();
	}
	
	public File getFile(String resource) {
		URL resolvedResource = this.getClass().getResource(resource);
		
		if(resolvedResource == null ) 
			throw new IllegalStateException(String.format("Could not load resource [%s]", resource));
		
		String path = resolvedResource.getPath();
		File file = new File(path);
		
		if(!file.exists() ) 			
			throw new IllegalStateException(String.format("Could not find file [%s][%s]", resource, path));
		
		return file;
	}
	
}
