package com.dave.survey;

public class BeaconData {

    private String key;
    private String beaconName;
    private String beaconDetails;
    private String additionalDetails;
    private String imageUrl;

    private String latitude;
    private String longitude;

    public BeaconData(String key, String beaconName, String beaconDetails, String additionalDetails, String imageUrl, String latitude, String longitude) {
        this.key = key;
        this.beaconName = beaconName;
        this.beaconDetails = beaconDetails;
        this.additionalDetails = additionalDetails;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BeaconData() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public String getBeaconDetails() {
        return beaconDetails;
    }

    public void setBeaconDetails(String beaconDetails) {
        this.beaconDetails = beaconDetails;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
