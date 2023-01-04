package com.framework.utils;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.Platform;
import org.springframework.stereotype.Component;

@Component
public class GridPlatformByOs {

	private final Map<String, Platform> map;

	/*
	 * Constructor for GridPlatformByOs
	 */
	public GridPlatformByOs() {
		map = new HashMap<>();

		map.put("android", Platform.ANDROID);

		map.put("windows", Platform.WINDOWS);
		map.put("w", Platform.WINDOWS);
		map.put("win", Platform.WINDOWS);

		map.put("win7", Platform.VISTA);
		map.put("windows 7", Platform.VISTA);

		map.put("windows 8", Platform.WIN8);
		map.put("win8", Platform.WIN8);

		map.put("windows 8.1", Platform.WIN8_1);
		map.put("win8.1", Platform.WIN8_1);

		map.put("win10", Platform.WIN10);
		map.put("windows 10", Platform.WIN10);
		map.put("windows10", Platform.WIN10);

		map.put("xp", Platform.XP);

		map.put("linux", Platform.LINUX);
		map.put("l", Platform.LINUX);

		map.put("mac", Platform.MAC);

		map.put("el capitan", Platform.EL_CAPITAN);
		map.put("el_capitan", Platform.EL_CAPITAN);

		map.put("mavericks", Platform.MAVERICKS);

		map.put("mountain lion", Platform.MOUNTAIN_LION);
		map.put("mountain_lion", Platform.MOUNTAIN_LION);

		map.put("snow leopard", Platform.SNOW_LEOPARD);
		map.put("snow_leopard", Platform.SNOW_LEOPARD);

		map.put("yosemite", Platform.YOSEMITE);
	}

	public Platform getGridPlatformByOS(String os) {
		return map.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase(os)).map(Map.Entry::getValue).findFirst()
				.orElse(Platform.ANY);

	}
}
