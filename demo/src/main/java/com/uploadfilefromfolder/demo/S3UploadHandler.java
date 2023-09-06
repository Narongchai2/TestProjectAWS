package com.uploadfilefromfolder.demo;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

public class S3UploadHandler implements RequestHandler<RequestClass, ResponseClass> {

    @Override
    public ResponseClass handleRequest(RequestClass request, Context context) {
     
        boolean isSuccess = true; 
        
        String status = isSuccess ? "success" : "fail";
        String tableName = "MyTable";
        String fileKey = request.getFileKey(); // 
        String keyName = "Narongchai";
        String keyValue = "Toei Toey";
        AttributeValue keyAttributeValue = AttributeValue.builder().s(keyValue).build();
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("AKIAXPNJMWDMCKIAXXGD", "8gEkFIr+tpguHtUQ99LBvIItxK6m/Tf/XbZtLVg2")))
                .build();

        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("fileKey", AttributeValue.builder().s(fileKey).build());
        itemValues.put("status", AttributeValue.builder().s(status).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        PutItemResponse putItemResponse = dynamoDbClient.putItem(putItemRequest);

        
        return new ResponseClass();
    }
}