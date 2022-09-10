package com.community.aemlab.core.models;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.community.aemlab.core.services.sample.FileService;

/**
 * @author arunpatidar02
 *
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FileServiceModel {

	@OSGiService(filter = "(file.type=xml)")
	private FileService fs1;

	private String fileData1 = "";
	private String fileData2 = "";

	@PostConstruct
	protected void init() {
		BundleContext bundleContext = FrameworkUtil.getBundle(FileServiceModel.class).getBundleContext();
		Collection<ServiceReference<FileService>> serviceList = null;
		try {
			serviceList = bundleContext.getServiceReferences(FileService.class, getFilter("pdf", "10"));
			Iterator<ServiceReference<FileService>> it = serviceList.iterator();
			if (it.hasNext()) {
				FileService fs2 = bundleContext.getService(it.next());
				if (fs2 != null) {
					fileData2 = fs2.getFileData();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get the message
	 * 
	 * @return message
	 */
	public String getMessage() {
		return fileData1 + fileData2;
	}

	/**
	 * Get the factory Filter
	 * 
	 * @param type
	 * @param maxSize
	 * @return service factory ldap expression
	 */
	private String getFilter(String type, String maxSize) {
		return "(&(max.size=" + maxSize + ")(file.type=" + type + "))";
	}

}
