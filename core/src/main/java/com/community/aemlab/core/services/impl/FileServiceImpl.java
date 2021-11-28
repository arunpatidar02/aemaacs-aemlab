package com.community.aemlab.core.services.impl;

import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import com.community.aemlab.core.services.FileService;
import com.community.aemlab.core.services.config.FileServiceFactoryConfig;

@Component(service = FileService.class, immediate = true)
@Designate(ocd = FileServiceFactoryConfig.class, factory = true)

public class FileServiceImpl implements FileService {
	private String data;

	@Override
	public String getFileData() {
		return "File data from Service:" + this.data;
	}

	@Activate
	@Modified
	protected void activate(final FileServiceFactoryConfig config) {
		this.data = PropertiesUtil.toString(config.file_type() + " - " + config.max_size(), "No Config found");
	}
}