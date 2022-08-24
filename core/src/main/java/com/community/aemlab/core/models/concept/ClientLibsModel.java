package com.community.aemlab.core.models.concept;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.clientlibs.ClientLibrary;
import com.adobe.granite.ui.clientlibs.HtmlLibraryManager;
import com.adobe.granite.ui.clientlibs.LibraryType;



/**
 * @author arunpatidar02
 *
 *
 *         Sightly Code
 *
 *         <sly data-sly-use.jsObj="${'com.community.aemlab.core.models.concept.ClientLibsModel' @category='aemlab.base'}" data-sly-list="${jsObj.cssFiles}">
 *          <link rel="stylesheet" href="${item}" type="text/css" async>
 *         </sly>
 *         <sly data-sly-use.jsObj="${'com.community.aemlab.core.models.concept.ClientLibsModel' @category='aemlab.base'}" data-sly-list="${jsObj.jsFiles}">
 *          <script async type="text/javascript" src="${item}"></script>
 *         </sly>
 *
 *
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ClientLibsModel {

	@RequestAttribute(name = "category")
	private String category;

	@Reference
	HtmlLibraryManager clientlibmanager;

	private List<String> jsFiles = new ArrayList<>();
	private List<String> cssFiles = new ArrayList<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientLibsModel.class);

	private static final String APPS_REGEX = "^/apps/";
	private static final String APPS_REPLACE = "/etc.clientlibs/";

	@PostConstruct
	protected void init() {

		try {
			if (clientlibmanager != null) {
				String[] categoryArray = { category };
				java.util.Collection<ClientLibrary> libs = clientlibmanager.getLibraries(categoryArray, LibraryType.JS,
						false, false);
				for (ClientLibrary lib : libs) {
					String libPath = getClientLibPath(lib, LibraryType.JS);
					jsFiles.add(libPath);
				}

				libs = clientlibmanager.getLibraries(categoryArray, LibraryType.CSS, false, false);
				for (ClientLibrary lib : libs) {
					String libPath = getClientLibPath(lib, LibraryType.CSS);
					cssFiles.add(libPath);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while processing clienlibs for category {} ", category, e);
		}

	}

	/**
	 * @param lib
	 * @param type
	 * @return
	 */
	private String getClientLibPath(ClientLibrary lib, LibraryType type) {
		String libPath = lib.getIncludePath(type);
		if (lib.allowProxy()) {
			libPath = libPath.replaceFirst(APPS_REGEX, APPS_REPLACE);
		}
		return libPath;
	}

	/**
	 * @return
	 */
	public List<String> getJsFiles() {
		return jsFiles;
	}

	/**
	 * @return
	 */
	public List<String> getCSSFiles() {
		return cssFiles;
	}

}
