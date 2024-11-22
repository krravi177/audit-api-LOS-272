# Use Amazon Corretto as the base image
FROM amazoncorretto:17

# Copy the built JAR file from the target directory
COPY target/audit-api-0.1.0.jar audit-api-0.1.0.jar


# Set default environment variables (update these with actual values or keep them empty)
ENV APP_PORT=8080
ENV AWS_SQS_REGION=us-east-1
# Run the application
ENTRYPOINT ["java", "-jar", "audit-api-0.1.0.jar"]
