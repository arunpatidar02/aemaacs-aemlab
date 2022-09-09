package com.community.aemlab.core.utils;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class PathHelper {

  private static final String XF_PATH_SEGMENT = "experience-fragments";

  private static final int SITE_ROOT_SEGMENTS_LENGTH = 4;

  private static final int LANGUAGE_ROOT_SEGMENTS_LENGTH = 5;

  private String[] pathSegments;

  public PathHelper(String path) {
    this.pathSegments = getPathSegments(path);
  }

  public String getSiteRootPath() {
    return ArrayUtils.getLength(pathSegments) >= SITE_ROOT_SEGMENTS_LENGTH
        ? "/" + StringUtils.join(pathSegments, '/', 0, SITE_ROOT_SEGMENTS_LENGTH)
        : "";
  }

  public String getLanguageRootPath() {
    return ArrayUtils.getLength(pathSegments) >= LANGUAGE_ROOT_SEGMENTS_LENGTH
        ? "/" + StringUtils.join(pathSegments, '/', 0, LANGUAGE_ROOT_SEGMENTS_LENGTH)
        : "";
  }

  private String[] getPathSegments(String path) {
    String[] segments = StringUtils.split(path, '/');
    return Arrays.stream(segments)
                 .filter(s -> !StringUtils.equals(s, XF_PATH_SEGMENT))
                 .toArray(String[]::new);
  }
}
