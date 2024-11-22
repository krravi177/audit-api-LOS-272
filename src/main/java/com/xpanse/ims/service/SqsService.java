package com.xpanse.ims.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.sqs.model.Message;


@Service
public class SqsService {

	private static final Logger logger = LoggerFactory.getLogger(SqsService.class);

	/** Rest Template */
	private final RestTemplate restTemplate;

	/** audit-api URL field */
	private final String auditURL;

	/** SqsService constructor */
	public SqsService(final RestTemplate restTemplate,
			@Value("${audit.api.url}") final String auditURL) {
		this.restTemplate = restTemplate;
		this.auditURL = auditURL;

	}

	/** post message to audit-api */
	public void postMessage(Message message) {
		logger.info("Message received: {}", message.body());

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> request = new HttpEntity<>(message.body(), headers);

			ResponseEntity<String> response = restTemplate
					.postForEntity(auditURL, request, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				logger.info("Message successfully forwarded to Audit API");
			} else {
				logger.error("Failed to forward message: {}", response.getStatusCode());
			}

		} catch (Exception e) {
			logger.error("Error processing message: {}", e.getMessage(), e);
		}

	}

}
