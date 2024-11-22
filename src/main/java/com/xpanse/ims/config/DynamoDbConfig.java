package com.xpanse.ims.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {
	
	/** dynamodb region field */
	private final String region;

	/** DynamoDbConfig constructor */
	public DynamoDbConfig(@Value("${aws.region}") final String region) {
		this.region = region;

	}


   @Bean
   public DynamoDbClient dynamoDbClient() {
       return DynamoDbClient.builder()
               .region(Region.of(region))  // Use the appropriate region
               .build();
   }

}
