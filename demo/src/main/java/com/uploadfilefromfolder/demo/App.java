package com.uploadfilefromfolder.demo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

public class App  {
    public static void main(String[] args) {
        String bucketName = "uploadfiletoei";
        String folderPath = "C:\\Users\\win10 pro\\Desktop\\UploadFile";
        String backupFolderPath = "C:\\Users\\win10 pro\\Desktop\\BackupFile";
        String senderEmail = "narongchai.takunpan@gmail.com";
        List<String> recipientEmails = Arrays.asList("narongchai.takunpan@gmail.com",
                "toey1234567891234@gmail.com");

        while (true) {
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
            if (files != null) {
                Region region = Region.AP_SOUTHEAST_1;
                S3Client s3Client = S3Client.builder().region(region)
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("AKIAXPNJMWDMCKIAXXGD", "8gEkFIr+tpguHtUQ99LBvIItxK6m/Tf/XbZtLVg2")))
                        .build();	
                for (File file : files) {
                    if (file.isFile()) {
                        String key = file.getName();
                        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key)
                                .build();
                        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                                RequestBody.fromFile(file));
                        if (putObjectResponse != null) {
                            System.out.println("File uploaded successfully!");
                        } else {
                            System.out.println("File upload failed!");
                        }
                        String backupFilePath = backupFolderPath + "\\" + key;
                        Path sourcePath = Paths.get(file.getAbsolutePath());
                        Path destinationPath = Paths.get(backupFilePath);

                        if (backupFilePath != null) {
                            System.out.println("File backup successfully!");
/*
                            RequestClass request = new RequestClass(key); // 
                            S3UploadHandler s3UploadHandler = new S3UploadHandler();
                            ResponseClass response = s3UploadHandler.handleRequest(request, null);*/
                           
                            // Send SMS
                         /*   SnsClient snsClient = SnsClient.builder().region(region)
                                    .credentialsProvider(StaticCredentialsProvider.create(
                                            AwsBasicCredentials.create("AKIAXPNJMWDMCKIAXXGD", "8gEkFIr+tpguHtUQ99LBvIItxK6m/Tf/XbZtLVg2")))
                                    .build();
                            String message = "File backup and upload completed!\n\nFile Name: " + key;
                            String topicArn = "arn:aws:sns:ap-southeast-1:514140713176:SMSbackup";
                            PublishRequest publishRequest = PublishRequest.builder().topicArn(topicArn)
                                    .message(message).build();
                            PublishResponse publishResponse = snsClient.publish(publishRequest);

                            if (publishResponse != null) {
                                System.out.println("SMS sent successfully!");
                            } else {
                                System.out.println("Failed to send SMS!");
                            }
*/
                            // Send Email
                            SesClient sesClient = SesClient.builder().region(region)
                                    .credentialsProvider(StaticCredentialsProvider.create(
                                            AwsBasicCredentials.create("AKIAXPNJMWDMCKIAXXGD ", "8gEkFIr+tpguHtUQ99LBvIItxK6m/Tf/XbZtLVg2")))
                                    .build();

                            String subject = "File Backup and Upload Completed";
                            String body = "The file backup and upload process has been completed successfully.";

                            sendEmail(sesClient, senderEmail, recipientEmails, subject, body, key);

                            try {
                                Files.move(sourcePath, destinationPath);
                            } catch (IOException e) {
                                e.printStackTrace();
                                continue;
                            }
                        } else {
                            System.out.println("File backup failed!");
                        }
                        try {
                            Thread.sleep(6000);
                        } 
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        file.delete();
                    } else {
                        System.out.println("File backup failed!");
                    }
                }
            } else {
                System.out.println("Folder is empty or does not exist.");
            }
            
        }
        
    }

    private static void sendEmail(SesClient client, String senderEmail, List<String> recipientEmails, String subject, String body, String fileName) {
        for (String recipientEmail : recipientEmails) {
            String emailBody = body + "\n\n" + "File Name: " + fileName;

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .source(senderEmail)
                    .destination(builder -> builder.toAddresses(recipientEmail))
                    .message(builder -> builder
                            .subject(builder2 -> builder2.data(subject))
                            .body(builder2 -> builder2.text(builder3 -> builder3.data(emailBody))))
                    .build();

            client.sendEmail(emailRequest);

            System.out.println("Email sent successfully to " + recipientEmail);
        }
    }
}
