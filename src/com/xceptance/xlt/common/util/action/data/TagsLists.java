package com.xceptance.xlt.common.util.action.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TagsLists {
	
	protected HashMap<String, List<String>> mappy = new HashMap<>(); 
	private List<String> root;
	private List<String> requestTags;
	private List<String> responseTags;
	private List<String> subRequestTags;
	private List<String> actionTags;
	
	public TagsLists() 
	{
		
		root = new ArrayList<String>();
		root.add("Action");
		root.add("Store");
		root.add("Name");
		root.add("Httpcode");
		root.add("Url");
		root.add("Method");
		root.add("Xhr");
		root.add("Encode-Parameters");
		root.add("Encode-Body");
		root.add("Headers");
		root.add("Parameters");
		root.add("Cookies");
		root.add("Variables");
		root.add("Header");
		root.add("Encoded");
		
		actionTags = new ArrayList<String>();
		actionTags.add("Name");
		actionTags.add("Request");
		actionTags.add("Response");
		actionTags.add("Subrequests");
		
		requestTags = new ArrayList<String>();
		requestTags.add("Url");
		requestTags.add("Method");
		requestTags.add("Xhr");
		requestTags.add("Encode-Parameters");
		requestTags.add("Parameters");
		requestTags.add("Headers");
		requestTags.add("Body");
		requestTags.add("Encode-Body");
		
		responseTags = new ArrayList<String>();
		responseTags.add("Httpcode");
		responseTags.add("Validate");
		responseTags.add("Store");
		
		subRequestTags = new ArrayList<String>();
		subRequestTags.add("Xhr");
		subRequestTags.add("Static");		
	
		mappy.put("root", root);
		mappy.put("actionTags", actionTags);
		mappy.put("requestTags", requestTags);
		mappy.put("responseTags", responseTags);
		mappy.put("subRequestTags", subRequestTags);
	}
	


	
}
