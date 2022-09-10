package com.community.aemlab.oneweb.core.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.adobe.cq.wcm.core.components.models.Image;

/**
 * @author arun.patidar
 *
 */
public class ImageUtils {
	
	private static final String WDITH_PLACEHOLDER_REGEX = "\\{.width\\}";
	private static final String DOT = ".";
	
	/**
	 * Constructor
	 */
	ImageUtils(){}
	
	/**
	 * Get srcset based on policy 
	 * 
	 * @param image
	 * @return
	 */
	public static String getSrcSet(Image image) {

			String srcUriTemplate = image.getSrcUriTemplate();
			int[] widths = image.getWidths();
			Set<String> set = new HashSet<>();
			
			for(int w: widths) {
				StringBuilder sb = new StringBuilder();
				String width = String.valueOf(w);
				String srcset = srcUriTemplate.replaceAll(WDITH_PLACEHOLDER_REGEX, DOT + width);
				sb.append(srcset).append(" ").append(width).append("w");
				set.add(sb.toString());
			}
			
			return set.stream().collect(Collectors.joining(","));
			
		}

}
