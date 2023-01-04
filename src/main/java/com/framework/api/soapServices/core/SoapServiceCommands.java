package com.framework.api.soapServices.core;

@SuppressWarnings("unused")
public class SoapServiceCommands {
	
	private SoapServiceCommands() {
		
	}
	
	public static final String ADD_NODE = "fx:addnode; Node:";
	public static final String ADD_ATTIRBUTE = "fx:addattribute; Attribute:";
	public static final String ADD_NAMESPACE = "fx:addnamespace; Namespace:";
	public static final String GET_DATE_TIME = "fx:getdatetime; DaysOut:";
	public static final String GET_DATE = "fx:getdate; DaysOut:";
	public static final String REMOVE_NODE = "fx:removenode";
	public static final String REMOVE_ATTRIBUTE = "fx:removeattribute";
	public static final String RANDOM_NUMBER = "fx:randomnumber; node:";
	public static final String RANDOM_STRING = "fx:randomstring; node:";
	public static final String RANDOM_ALPHANUMBERIC = "fx:randomalphanumeric; node:";
}