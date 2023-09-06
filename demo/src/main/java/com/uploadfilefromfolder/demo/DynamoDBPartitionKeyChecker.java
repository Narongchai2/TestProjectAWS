package com.uploadfilefromfolder.demo;
import java.util.List;
import java.util.Optional;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

	public class DynamoDBPartitionKeyChecker {
	    public static void main(String[] args) {
	    	  String tableName = "MyTable"; 
	    	  
	          DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
	                  .region(Region.AP_SOUTHEAST_1)
	                  .credentialsProvider(StaticCredentialsProvider.create(
	                          AwsBasicCredentials.create("AKIAXPNJMWDMCKIAXXGD", "8gEkFIr+tpguHtUQ99LBvIItxK6m/Tf/XbZtLVg2")))
	                  .build();

	          DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
	                  .tableName(tableName)
	                  .build();

	          DescribeTableResponse describeTableResponse = dynamoDbClient.describeTable(describeTableRequest);
	          TableDescription tableDescription = describeTableResponse.table();

	          List<KeySchemaElement> keySchema = tableDescription.keySchema();
	          Optional<KeySchemaElement> partitionKey = keySchema.stream()
	                  .filter(element -> element.keyType().equals(KeyType.HASH))
	                  .findFirst();

	          String partitionKeyAttribute = partitionKey.map(KeySchemaElement::attributeName)
	                  .orElseThrow(() -> new IllegalArgumentException("No partition key found in the table."));

	          System.out.println("Partition Key Attribute: " + partitionKeyAttribute);
	      }
	  }