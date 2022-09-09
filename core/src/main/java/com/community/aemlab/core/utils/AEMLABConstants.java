package com.community.aemlab.core.utils;

import com.day.cq.dam.api.DamConstants;

/**
 * @author arunpatidar02
 *
 */
public class AEMLABConstants {

	AEMLABConstants() {

	}

	public static final String CONTENTTYPE_TXT_HTML = "text/html";
	public static final String EXTENSION_HTML = ".html";
	public static final String EXTENSION_JSON = ".json";
	
	
	public static final String COMMA = ",";
	public static final String SPACE = " ";

	public static final String HYPHEN = "-";
	public static final String DOT = ".";
	public static final String HASH = "#";

	public static final String FORWARD_SLASH = "/";
	public static final String DOUBLE_SLASH = FORWARD_SLASH + FORWARD_SLASH;

	public static final String SEARCH_SEQ = "../";
	
	public static final String DAM_PATH_PREFIX = DamConstants.MOUNTPOINT_ASSETS + FORWARD_SLASH;
	
	public static final String AEMLAB_CONTENT_ROOT = "/content/aemlab";
	
	public static final String AEMLAB_DAM_ROOT = "/content/dam/aemlab";
	
	public static final String AEMLAB_CONTENT_XF_ROOT = "/content/experience-fragments/aemlab";
	
	public static final int LEVEL_CONTENT_ROOT = 0;
	public static final int LEVEL_MARKETING_ROOT = 1;
	public static final int LEVEL_TENANT_ROOT = 2;
	public static final int LEVEL_SITE_ROOT = 3;
	public static final int LEVEL_LANGUAGE_ROOT = 4;
	public static final int LEVEL_TARGETGROUP_ROOT = 5;
	
	

}
