package com.uploadfilefromfolder.demo;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBExample {
    private static final String TABLE_NAME = "MyTable";

    public static void main(String[] args) {
        // Create DynamoDB client
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.AP_SOUTHEAST_1)
              
                .build();

        // Create a map of attributes for the item
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("id", AttributeValue.builder().s("123").build());
        itemValues.put("name", AttributeValue.builder().s("Toei Toey").build());

        // Create a PutItemRequest
        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(itemValues)
                .build();

        // Insert the item into the table
        try {
            PutItemResponse response = client.putItem(request);
            System.out.println("Insertion successful");

            // Perform any further processing or log related data after successful insertion
        } catch (DynamoDbException e) {
            System.err.println("Insertion failed");
            e.printStackTrace();

            // Perform any further processing or log related data after failed insertion
        }

        // Close the connection
        client.close();
    }
}

