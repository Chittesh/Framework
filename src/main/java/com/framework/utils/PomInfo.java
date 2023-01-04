package com.framework.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Profile;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import com.framework.exception.AutomationException;

public class PomInfo {

	private PomInfo() {
		
	}
	
	public static String getProjectId() {
		return getGroupId() + ":" + getArtifactId();
	}
	
	public static String getArtifactId() {
		return getModel().getArtifactId();
	}

	public static String getGroupId() {
		return getModel().getGroupId();
	}
	
	public static String getFrameworkVersion() {
    	Model model = getModel();
    	return model.getParent().getVersion();
    }
	
	public static String getSonarSkipPom() {
    	return getProperty("sonar.skip");
    }
	
	public static String getSonarSkipCommandLine() {
    	return getProperty("sonar.skip.status");
    }
	
	public static Boolean hasDependencies() {
		List<Dependency> dependencies = getModel().getDependencies();
		
		return Boolean.FALSE.equals(dependencies.isEmpty());
	}
	
	public static Boolean hasProfileDependencies() {
		for (Profile profile : getModel().getProfiles())
			if (Boolean.FALSE.equals(profile.getDependencies().isEmpty()))
				return true;
			
		return false;
	}
	
	public static Boolean hasPlugins() {
		Build build = getModel().getBuild();
		
		if (build == null)
			return false;
		
		List<Plugin> plugins = build.getPlugins();
		
		return Boolean.FALSE.equals(plugins.isEmpty());
	}
	
	public static Boolean hasProfilePlugins() {
		for (Profile profile : getModel().getProfiles()) {
			BuildBase build = profile.getBuild();
		
			if (build == null)
				return false;
		
			List<Plugin> plugins = build.getPlugins();
		
			if (Boolean.FALSE.equals(plugins.isEmpty()))
				return true;
		}
		
		return false;
	}
	
	public static Boolean hasProperties() {
		Properties properties = getModel().getProperties();
		
		return Boolean.FALSE.equals(properties.isEmpty());
	}

	private static String getProperty(String name) {
    	Properties properties = getModel().getProperties();
    	
    	if (properties.isEmpty())
    		return "";
    	
    	String value = properties.getProperty(name);
    	
    	return value != null ? value : "";
    }

	private static Model getModel() {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		try {
			return reader.read(new FileReader("pom.xml"));
		} catch (IOException | XmlPullParserException e) {
			throw new AutomationException("cannot read pom.xml!");
		}
	}
	
}
