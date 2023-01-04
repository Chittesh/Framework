package com.framework.utils.dataHelpers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateMapper {

	private static String[][] stateNameToCodeArray = new String[][] 
	{
		{"alberta", "AB"},
		{"american samoa", "AS"},
		{"arizona", "AZ"},
		{"arkansas", "AR"},
		{"armed forces (ae)", "AE"},
		{"armed forces americas", "AA"},
		{"armed forces pacific", "AP"},
		{"british columbia", "BC"},
		{"california", "CA"},
		{"colorado", "CO"},
		{"connecticut", "CT"},
		{"delaware", "DE"},
		{"district of columbia", "DC"},
		{"florida", "FL"},	
		{"georgia", "GA"},
		{"guam", "GU"},
		{"hawaii", "HI"},
		{"idaho", "ID"},
		{"illinois", "IL"},
		{"indiana", "IN"},
		{"iowa", "IA"},
		{"kansas", "KS"},
		{"kentucky", "KY"},
		{"louisiana", "LA"},
		{"maine", "ME"},
		{"manitoba", "MB"},
		{"maryland", "MD"},
		{"massachusetts", "MA"},
		{"michigan", "MI"},
		{"minnesota", "MN"},
		{"mississippi", "MS"},
		{"missouri", "MO"},
		{"montana", "MT"},
		{"nebraska", "NE"},
		{"nevada", "NV"},
		{"new brunswick", "NB"},
		{"new hampshire", "NH"},
		{"new jersey", "NJ"},
		{"new mexico", "NM"},
		{"new york", "NY"},
		{"newfoundland", "NF"},
		{"north carolina", "NC"},
		{"north dakota", "ND"},
		{"northwest territories", "NT"},
		{"nova scotia", "NS"},
		{"nunavut", "NU"},
		{"ohio", "OH"},
		{"oklahoma", "OK"},
		{"ontario", "ON"},
		{"oregon", "OR"},
		{"pennsylvania", "PA"},
		{"prince Edward Island", "PE"},
		{"puerto Rico", "PR"},
		{"quebec", "PQ"},
		{"rhode island", "RI"},
		{"saskatchewan", "SK"},
		{"south carolina", "SC"},
		{"south dakota", "SD"},
		{"tennessee", "TN"},
		{"texas", "TX"},
		{"utah", "UT"},
		{"vermont", "VT"},
		{"virgin islands", "VI"},
		{"virginia", "VA"},
		{"washington", "WA"},
		{"west cirginia", "WV"},
		{"wisconsin", "WI"},
		{"wyoming", "WY"},
		{"yukon territory", "YT"}
	};
	
	private static String[][] stateCodeToNameArray = new String[][]
	{
		{"AL", "Alabama"},
		{"AK", "Alaska"},
		{"AB", "Alberta"},
		{"AZ", "Arizona"},
		{"AR", "Arkansas"},
		{"BC", "British Columbia"},
		{"CA", "California"},
		{"CO", "Colorado"},
		{"CT", "Connecticut"},
		{"DE", "Delaware"},
		{"DC", "District Of Columbia"},
		{"FL", "Florida"},
		{"GA", "Georgia"},
		{"GU", "Guam"},
		{"HI", "Hawaii"},
		{"ID", "Idaho"},
		{"IL", "Illinois"},
		{"IN", "Indiana"},
		{"IA", "Iowa"},
		{"KS", "Kansas"},
		{"KY", "Kentucky"},
		{"LA", "Louisiana"},
		{"ME", "Maine"},
		{"MB", "Manitoba"},
		{"MD", "Maryland"},
		{"MA", "Massachusetts"},
		{"MI", "Michigan"},
		{"MN", "Minnesota"},
		{"MS", "Mississippi"},
		{"MO", "Missouri"},
		{"MT", "Montana"},
		{"NE", "Nebraska"},
		{"NV", "Nevada"},
		{"NB", "New Brunswick"},
		{"NH", "New Hampshire"},
		{"NJ", "New Jersey"},
		{"NM", "New Mexico"},
		{"NY", "New York"},
		{"NF", "Newfoundland"},
		{"NC", "North Carolina"},
		{"ND", "North Dakota"},
		{"NT", "Northwest Territories"},
		{"NS", "Nova Scotia"},
		{"NU", "Nunavut"},
		{"OH", "Ohio"},
		{"OK", "Oklahoma"},
		{"ON", "Ontario"},
		{"OR", "Oregon"},
		{"PA", "Pennsylvania"},
		{"PE", "Prince Edward Island"},
		{"PR", "Puerto Rico"},
		{"QC", "Quebec"},
		{"RI", "Rhode Island"},
		{"SK", "Saskatchewan"},
		{"SC", "South Carolina"},
		{"SD", "South Dakota"},
		{"TN", "Tennessee"},
		{"TX", "Texas"},
		{"UT", "Utah"},
		{"VT", "Vermont"},
		{"VI", "Virgin Islands"},
		{"VA", "Virginia"},
		{"WA", "Washington"},
		{"WV", "West Virginia"},
		{"WI", "Wisconsin"},
		{"WY", "Wyoming"},
		{"YT", "Yukon Territory"}
	};
																
	private StateMapper() {
		
	}
	
	private static Map<String, String> stateNameToCode = new HashMap<>();
	private static Map<String, String> stateCodeToName = new HashMap<>();


	public static String getStateCode(String name) {
		populateCodeMap();
		return stateNameToCode.get(name.toLowerCase());
	}

	
	public static String getStateName(String code) {
		populateNameMap();
		return stateCodeToName.get(code.toUpperCase());
	}

	private static void populateCodeMap() {
		stateNameToCode = Stream.of(stateNameToCodeArray)
								.collect(Collectors.toMap(data -> data[0], data -> data[1]));
	}

	private static void populateNameMap() {
		stateCodeToName = Stream.of(stateCodeToNameArray)
								.collect(Collectors.toMap(data -> data[0], data -> data[1]));
	}
	
}
