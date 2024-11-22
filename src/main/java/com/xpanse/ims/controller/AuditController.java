package com.xpanse.ims.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xpanse.ims.model.AppResponse;
import com.xpanse.ims.model.AuditRequest;
import com.xpanse.ims.service.DynamoDbService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
	private static final Logger logger = LoggerFactory.getLogger(AuditController.class);

	private final DynamoDbService dynamoDbService;

	public AuditController(DynamoDbService dynamoDbService) {
		this.dynamoDbService = dynamoDbService;
	}

	@PostMapping("/insert-record")
	public ResponseEntity<AppResponse> insertRecord(@RequestBody @Valid AuditRequest record) {
		try {
			if (!dynamoDbService.doesTableExist("AuditRecords")) {
				dynamoDbService.createAuditRecordsTableIfNotExists("AuditRecords");
			}
			return new ResponseEntity<>(new AppResponse(dynamoDbService.insertRecord(record, "AuditRecords")), HttpStatus.CREATED);

		} catch (Exception ex) {
			logger.error("Error while doing audit transaction {} " + ex.getMessage());
			return new ResponseEntity<>(new AppResponse(ex.getMessage()), HttpStatus.CONFLICT);

		}
	}
}
