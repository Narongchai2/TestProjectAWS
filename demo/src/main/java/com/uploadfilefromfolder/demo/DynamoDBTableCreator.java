package com.uploadfilefromfolder.demo;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

public class DynamoDBTableCreator {
    public static void main(String[] args) {
        String tableName = "LambdaTable";
        String partitionKey = "Narongchai";
        String sortKey = "Takunpan";

        // Create the DynamoDB client
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("AKIAXPNJMWDMCKIAXXGD", "8gEkFIr+tpguHtUQ99LBvIItxK6m/Tf/XbZtLVg2")))
                .build();

        // Define the attribute definitions
        AttributeDefinition partitionKeyDefinition = AttributeDefinition.builder()
                .attributeName(partitionKey)
                .attributeType(ScalarAttributeType.S)
                .build();

        AttributeDefinition sortKeyDefinition = AttributeDefinition.builder()
                .attributeName(sortKey)
                .attributeType(ScalarAttributeType.S)
                .build();

        // Define the key schema
        KeySchemaElement partitionKeySchema = KeySchemaElement.builder()
                .attributeName(partitionKey)
                .keyType(KeyType.HASH)
                .build();

        KeySchemaElement sortKeySchema = KeySchemaElement.builder()
                .attributeName(sortKey)
                .keyType(KeyType.RANGE)
                .build();

        // Create the table request
        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .tableName(tableName)
                .attributeDefinitions(partitionKeyDefinition, sortKeyDefinition)
                .keySchema(partitionKeySchema, sortKeySchema)
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(5L)
                        .writeCapacityUnits(5L)
                        .build())
                .build();

        // Create the table
        CreateTableResponse createTableResponse = dynamoDbClient.createTable(createTableRequest);

        // Wait for the table to become active
        dynamoDbClient.waiter().waitUntilTableExists(b -> b.tableName(tableName));

        System.out.println("Table created successfully: " + createTableResponse.tableDescription().tableName());
    }
}
