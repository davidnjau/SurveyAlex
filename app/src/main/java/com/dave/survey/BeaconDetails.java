package com.dave.survey;

public class BeaconDetails {

    private String beaconName;
    private String beaconDetails;
    private String additionalDetails;
    private String imageUrl;

    private String latitude;
    private String longitude;

    public BeaconDetails() {
    }

    public BeaconDetails(String beaconName, String beaconDetails, String additionalDetails, String imageUrl, String latitude, String longitude) {
        this.beaconName = beaconName;
        this.beaconDetails = beaconDetails;
        this.additionalDetails = additionalDetails;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
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
