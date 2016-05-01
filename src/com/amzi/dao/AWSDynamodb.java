package com.amzi.dao;
import java.io.File;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.simpledb.model.Item;


public class AWSDynamodb {
	static AmazonDynamoDBClient client;
	static String tableName = "pictures";
	 static DynamoDB dynamoDB;
	 static String replyTableName = "Reply";
	public void createClient() {

		client = new AmazonDynamoDBClient(new ProfileCredentialsProvider()); 
		dynamoDB = new DynamoDB(client);
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		//createTable("pictures", 10L, 5L, "pictureid", "S");
	}
	public void insertintotable(String desc, String price, String name, String image, String category) {
		createClient();	

		Random rand = new Random();
		int randomNum = rand.nextInt((100 - 10) + 1) + 10;
		String pictureId = "P"+ randomNum;
		if(desc==null || desc.equals(""))
		{
			desc= "abc";
		}
		if(name==null || name.equals(""))
		{
			name= "abc";
		}
		if(price==null || price.equals(""))
		{
			price= "abc";
		}
		if(image==null || image.equals(""))
		{
			image = "abc";
		}
		Map<String,AttributeValue> item1 = new HashMap<String,AttributeValue>();
		item1.put("pictureid", new AttributeValue().withS(pictureId));
		item1.put("desc", new AttributeValue().withS(desc));
		item1.put("price", new AttributeValue().withS(price));
		item1.put("username", new AttributeValue().withS(name));
		item1.put("image", new AttributeValue().withS(image));
		item1.put("category", new AttributeValue().withS(category));


		PutItemRequest putItemRequest1 = new PutItemRequest(tableName,item1);
		PutItemResult putItemResult1 = client.putItem(putItemRequest1);

	}


	//Simple s3 upload(any file)
	public void s3upload(File file)
	{
		String existingBucketName  = "s3cart"; 
		String keyName = file.getName();
		AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());        
		List<PartETag> partETags = new ArrayList<PartETag>();
		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new 
				InitiateMultipartUploadRequest(existingBucketName, keyName);
		InitiateMultipartUploadResult initResponse = 
				s3Client.initiateMultipartUpload(initRequest);

		long contentLength = file.length();
		long partSize = 5242880; // Set part size to 5 MB.

		try {
			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
				// Last part can be less than 5 MB. Adjust part size.
				partSize = Math.min(partSize, (contentLength - filePosition));

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest()
				.withBucketName(existingBucketName).withKey(keyName)
				.withUploadId(initResponse.getUploadId()).withPartNumber(i)
				.withFileOffset(filePosition)
				.withFile(file)
				.withPartSize(partSize);

				// Upload part and add response to our list.
				partETags.add(
						s3Client.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;
			}

			// Step 3: Complete.
			CompleteMultipartUploadRequest compRequest = new 
					CompleteMultipartUploadRequest(
							existingBucketName, 
							keyName, 
							initResponse.getUploadId(), 
							partETags);

			s3Client.completeMultipartUpload(compRequest);
		} catch (Exception e) {
			s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
					existingBucketName, keyName, initResponse.getUploadId()));
		}
		s3Client.setObjectAcl(existingBucketName, keyName, CannedAccessControlList.PublicRead);

	}
	//This part is to scan(fetch all items from dynamodb).
	public ScanResult dynamo(String tableName) {
		createClient();
		ScanRequest scanRequest = new ScanRequest().withTableName(tableName);
		ScanResult result = client.scan(scanRequest);
		return result;
	}
	//THis part is not required
	public File S3Download(String imageName)
	{
		AmazonS3Client s3Client = new AmazonS3Client();

		File localFile = new File(imageName);
		GetObjectRequest getObjectRequest =  new GetObjectRequest("webcloudproject3", imageName);
		s3Client.getObject(getObjectRequest, localFile);
		return localFile;
	}
	//Use similar code if user table is in dyanamo db(for quiz) 
	public void validateCredentials(String userName, String password) {
		createClient();
		Map<String,AttributeValue> item1 = new HashMap<String,AttributeValue>();
		item1.put("id", new AttributeValue().withS(userName));

		GetItemRequest getItemRequest = new GetItemRequest(tableName,item1 );
		GetItemResult getItemResult = client.getItem(getItemRequest);
		if(getItemResult.getItem()!=null)
		{
			Map<String,AttributeValue> item2 = getItemResult.getItem();

			if(userName.equals(item2.get("name").getS()) && password.equals(item2.get("password").getS())){
				System.out.println("login success");
			}
			else
				System.out.println("incorrect password");
		}
		else
			System.out.println("incorrect username");
	}
	private static void createTable(
	        String tableName, long readCapacityUnits, long writeCapacityUnits, 
	        String partitionKeyName, String partitionKeyType) {

	        createTable(tableName, readCapacityUnits, writeCapacityUnits,
	            partitionKeyName, partitionKeyType, null, null);
	    }

	    private static void createTable(
	        String tableName, long readCapacityUnits, long writeCapacityUnits, 
	        String partitionKeyName, String partitionKeyType, 
	        String sortKeyName, String sortKeyType) {

	        try {

	            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
	            keySchema.add(new KeySchemaElement()
	                .withAttributeName(partitionKeyName)
	                .withKeyType(KeyType.HASH)); //Partition key
	            
	            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
	            attributeDefinitions.add(new AttributeDefinition()
	                .withAttributeName(partitionKeyName)
	                .withAttributeType(partitionKeyType));

	            if (sortKeyName != null) {
	                keySchema.add(new KeySchemaElement()
	                    .withAttributeName(sortKeyName)
	                    .withKeyType(KeyType.RANGE)); //Sort key
	                attributeDefinitions.add(new AttributeDefinition()
	                    .withAttributeName(sortKeyName)
	                    .withAttributeType(sortKeyType));
	            }

	            CreateTableRequest request = new CreateTableRequest()
	                    .withTableName(tableName)
	                    .withKeySchema(keySchema)
	                    .withProvisionedThroughput( new ProvisionedThroughput()
	                        .withReadCapacityUnits(readCapacityUnits)
	                        .withWriteCapacityUnits(writeCapacityUnits));

	            // If this is the Reply table, define a local secondary index
	            if (replyTableName.equals(tableName)) {
	                
	                attributeDefinitions.add(new AttributeDefinition()
	                    .withAttributeName("PostedBy")
	                    .withAttributeType("S"));

	                ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
	                localSecondaryIndexes.add(new LocalSecondaryIndex()
	                    .withIndexName("PostedBy-Index")
	                    .withKeySchema(
	                        new KeySchemaElement().withAttributeName(partitionKeyName).withKeyType(KeyType.HASH),  //Partition key
	                        new KeySchemaElement() .withAttributeName("PostedBy") .withKeyType(KeyType.RANGE))  //Sort key
	                    .withProjection(new Projection() .withProjectionType(ProjectionType.KEYS_ONLY)));

	                request.setLocalSecondaryIndexes(localSecondaryIndexes);
	            }

	            request.setAttributeDefinitions(attributeDefinitions);

	            System.out.println("Issuing CreateTable request for " + tableName);
	            Table table = dynamoDB.createTable(request);
	            System.out.println("Waiting for " + tableName
	                + " to be created...this may take a while...");
	            table.waitForActive();

	        } catch (Exception e) {
	            System.err.println("CreateTable request failed for " + tableName);
	            System.err.println(e.getMessage());
	        }
	    }
}
