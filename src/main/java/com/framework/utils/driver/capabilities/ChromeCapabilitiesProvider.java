package com.framework.utils.driver.capabilities;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChromeCapabilitiesProvider extends BaseChromeCapabilityProvider implements CapabilitiesProvider {

	@Value("${user.dir}")
	private String userDir;
	
	@Override
	protected void modifiyOptions(ChromeOptions options, String mobileName, List<String> extensions) {
		addExtensions(options, extensions);
	}

	private void addExtensions(ChromeOptions options, List<String> extensions) {
		List<File> extensionsAvailable = Stream
				.of(new File(userDir, "/src/test/resources/extensions"))
				.filter(File::exists)
				.flatMap(this::extensionList)
				.filter(extension -> extensionEnabled(extension, extensions))
				.collect(toList());

		options.addExtensions(extensionsAvailable);
	}

	private Stream<File> extensionList(File directory) {
		return FileUtils.listFiles(directory, new String[] { "crx" }, false).stream();
	}

	private boolean extensionEnabled(File file, List<String> extensions) {
		return extensions.stream()
				.map(extension -> extension.endsWith(".crx") ? extension : extension + ".crx")
				.map(String::toLowerCase)
				.anyMatch(extension -> file.getName().endsWith(extension));
	}

	@Override
	public boolean canCreate(String browserUnderTest, String browserVersion, String operatingSystem, String mobileName) {
		return "chrome".equalsIgnoreCase(browserUnderTest) 
				|| "headless".equalsIgnoreCase(browserUnderTest);
	}

	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}
}
