package com.community.aemlab.core.services.sample.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.crypto.CryptoSupport;
import com.community.aemlab.core.services.sample.impl.CryptoServiceImpl.CryptoServiceConfig;

/**
 * @author arunpatidar02
 *
 */
@Component(service = CryptoService.class, immediate = true)
@Designate(ocd = CryptoServiceConfig.class)
public class CryptoServiceImpl implements CryptoService {

	@Reference
	private CryptoSupport cryptoSupport;

	private String protectedText;
	private String unProtectedText;

	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoServiceImpl.class);

	@Override
	public String getProtectedText() {
		return protectedText;
	}

	@Override
	public String getUnprotectedText() {
		return unProtectedText;
	}

	@Activate
	@Modified
	protected void activate(final CryptoServiceConfig config) {
		this.protectedText = config.secret();

		try {
			this.unProtectedText = cryptoSupport.isProtected(protectedText) ? cryptoSupport.unprotect(protectedText)
					: protectedText;
		} catch (CryptoException e) {
			LOGGER.error("Error while decrypting the text", e);
		}

		LOGGER.trace("Protected - {} | Unprotected - {}", protectedText, unProtectedText);

	}

	@ObjectClassDefinition(name = "Annotation Demo Crypto Service - OSGi", description = "Sample Crypto services")
	public @interface CryptoServiceConfig {

		@AttributeDefinition(name = "Plain Text", description = "Sample String property", type = AttributeType.STRING)
		String secret() default "";

	}
}