package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.servlet.Servlet;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

/*
 * ------ POM Dependencies for email --------
 * 
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-email</artifactId>
	<version>1.5</version>
	<scope>provided</scope>
</dependency>
			
<dependency>
	<groupId>commons-lang</groupId>
	<artifactId>commons-lang</artifactId>
	<version>2.6</version>
	<scope>provided</scope>
</dependency>
 * 
 * -------------- Email Template and mail output ---------
 * https://github.com/arunpatidar02/aem63app-repo/tree/master/demo/email
 * 
 */

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Template Email Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/email1" })
public class SimpleEmailServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private static final String EMAIL_TEMPLATE = "/apps/aemlab/oneweb/concept/utils/email-template.txt";
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEmailServlet.class);

	private static final String EMAIL_TITLE = "Demo Email";
	private static final String EMAIL_SUBJECT = "AEM - Demo Email for Templated email";
	private static final String EMAIL_TO = "noreply.aem@gmail.com";

	@Reference
	private transient MessageGatewayService messageGatewayService;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
		try {
			resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
			PrintWriter pw = resp.getWriter();
			Node templateNode = req.getResourceResolver().getResource(EMAIL_TEMPLATE).adaptTo(Node.class);
			final Map<String, String> parameters = new HashMap<>();
			parameters.put("title", EMAIL_TITLE);
			parameters.put("name", "arunpatidar02");
			parameters.put("id", "0001");
			parameters.put("host.prefix", "http://localhost");
			parameters.put("faqpath", "/content/aemlab/oneweb/en/faq");
			final MailTemplate mailTemplate = MailTemplate.create(EMAIL_TEMPLATE, templateNode.getSession());
			HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(parameters), HtmlEmail.class);
			email.setSubject(EMAIL_SUBJECT);
			email.addTo(EMAIL_TO);
			MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
			messageGateway.send(email);
			LOGGER.trace("email sent");
			pw.write("email sent");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while sending an email", e);
			resp.getWriter().write(e.getMessage());
		}
	}
}