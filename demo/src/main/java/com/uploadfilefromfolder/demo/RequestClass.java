package com.uploadfilefromfolder.demo;
public class RequestClass {
    private String fileKey;

    public RequestClass() {
    }

    public RequestClass(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}

