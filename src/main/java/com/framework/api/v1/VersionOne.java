package com.framework.api.v1;

import com.framework.api.v1.data.Data;

public class VersionOne {
    public static final String BASE_URL = "https://www4.v1host.com/AllegisGroup/rest-1.v1";
    public static final String AUTH_TOKEN = "1.lZHZU8pRYYwcH0QdCi5sgfimU9Y=";
    
    private VersionOne() {
    	
    }

    public static Data data() {
        return new Data();
    }
}
