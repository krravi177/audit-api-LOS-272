package com.xpanse.ims.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xpanse.ims.model.AuditRequest;
import com.xpanse.ims.util.DateUtil;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Service
public class DynamoDbService {
	private static final Logger logger = LoggerFactory.getLogger(DynamoDbService.class);
	private final DynamoDbClient dynamoDbClient;

	public DynamoDbService(DynamoDbClient dynamoDbClient) {
		this.dynamoDbClient = dynamoDbClient;
	}

	public boolean doesTableExist(String tableName) {
		try {
			DescribeTableRequest describeTableRequest = DescribeTableRequest.builder().tableName(tableName).build();
			dynamoDbClient.describeTable(describeTableRequest);
			return true;
		} catch (ResourceNotFoundException e) {
			return false;
		} catch (DynamoDbException ex) {
			logger.error("Error checking table existence: {}", ex.getMessage());
			return false;
		}
	}

	public void createAuditRecordsTableIfNotExists(String tableName) {

		if (!doesTableExist(tableName)) {
			try {
				CreateTableRequest request = CreateTableRequest.builder().tableName(tableName)
						.keySchema(KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH) // Partition
								// key
								.build())
						.attributeDefinitions(AttributeDefinition.builder().attributeName("id")
								.attributeType(ScalarAttributeType.S).build())
						.provisionedThroughput(
								ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build())
						.build();

				dynamoDbClient.createTable(request);
				DescribeTableRequest tableRequest = DescribeTableRequest.builder().tableName(tableName).build();

				DynamoDbWaiter dbWaiter = dynamoDbClient.waiter();
				// Wait until the Amazon DynamoDB table is created.
				dbWaiter.waitUntilTableExists(tableRequest);

				logger.debug("Table {} created successfully. ", tableName);

			} catch (DynamoDbException ex) {
				logger.error("Error creating table: {}", ex.getMessage());
				throw ex;

			}
		} else {
			logger.debug("Table {} already exists. Creation skipped. ", tableName);

		}
	}

	public String insertRecord(AuditRequest auditRecord, String tableName) throws ParseException {
		try {
			DateUtil du = new DateUtil();
			Map<String, AttributeValue> item = new HashMap<>();
			item.put("id", AttributeValue.builder().s(UUID.randomUUID().toString()).build());
			item.put("applicationName", AttributeValue.builder().s(auditRecord.getApplicationName()).build());
			item.put("action", AttributeValue.builder().s(auditRecord.getAction()).build());
			item.put("actionPayload", AttributeValue.builder().s(auditRecord.getActionPayload()).build());
			item.put("auditDate", AttributeValue.builder()
					.s(du.getSqlFormat().format(du.getAuditFormat().parse(auditRecord.getAuditDate()))).build());

			PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(item).build();

			PutItemResponse putItemResponse = dynamoDbClient.putItem(request);

			logger.debug("Record inserted successfully into {} ", tableName);
			return putItemResponse.responseMetadata().requestId();

		} catch (ParseException ex) {
			logger.error("Parse Error while inserting record: {}", ex.getMessage());
			throw ex;
		} catch (DynamoDbException ex) {
			logger.error("Error inserting record: {}", ex.getMessage());
			throw ex;
		}
	}

}
