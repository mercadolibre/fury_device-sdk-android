package com.mercadolibre.android.device.sdk.domain;

import java.io.Serializable;

/**
 * VendorSpecificAttributes represents all the attributes and features for the device.
 *
 */
@SuppressWarnings({
        "unused",
        "PMD.SingularField",
        "CPD-START"
})
public class VendorSpecificAttributes implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean featureCamera;
    private boolean featureFlash;
    private boolean featureFrontCamera;
    private String product;
    private String device;
    private String platform;
    private String brand;
    private boolean featureAccelerometer;
    private boolean featureBluetooth;
    private boolean featureCompass;
    private boolean featureGps;
    private boolean featureGyroscope;
    private boolean featureMicrophone;
    private boolean featureNfc;
    private boolean featureTelephony;
    private boolean featureTouchScreen;
    private String manufacturer;
    private float screenDensity;

    /**
     * isFeatureCamera get device's latitude
     *
     * @return if feature camera is enabled
     */
    public boolean isFeatureCamera() {
        return featureCamera;
    }

    /**
     * setFeatureCamera sets device's feature camera
     *
     * @param featureCamera device's feature camera
     */
    public void setFeatureCamera(boolean featureCamera) {
        this.featureCamera = featureCamera;
    }

    /**
     * isFeatureFlash gets device's feature flash
     *
     * @return if device has flash
     */
    public boolean isFeatureFlash() {
        return featureFlash;
    }

    /**
     * setFeatureFlash sets device's featureFlash
     *
     * @param featureFlash device's featureFlash
     */
    public void setFeatureFlash(boolean featureFlash) {
        this.featureFlash = featureFlash;
    }

    /**
     * isFeatureFrontCamera gets device's FrontCamera
     *
     * @return if device has front camera
     */
    public boolean isFeatureFrontCamera() {
        return featureFrontCamera;
    }

    /**
     * setFeatureFrontCamera sets device's featureFrontCamera
     *
     * @param featureFrontCamera device's featureFrontCamera
     */
    public void setFeatureFrontCamera(boolean featureFrontCamera) {
        this.featureFrontCamera = featureFrontCamera;
    }

    /**
     * getProduct gets device's product
     *
     * @return device product
     */
    public String getProduct() {
        return product;
    }

    /**
     * setProduct sets device's product
     *
     * @param product device's product
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * getDevice gets device's
     *
     * @return device name
     */
    public String getDevice() {
        return device;
    }

    /**
     * setDevice sets device's device
     *
     * @param device device's device
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * getPlatform gets device's platform
     *
     * @return device platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * setPlatform sets device's platform
     *
     * @param platform device's platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * getBrand gets device's brand
     *
     * @return device brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * setBrand sets device's brand
     *
     * @param brand device's brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * isFeatureAccelerometer gets device's Accelerometer
     *
     * @return if device has accelerometer
     */
    public boolean isFeatureAccelerometer() {
        return featureAccelerometer;
    }

    /**
     * setFeatureAccelerometer sets device's featureAccelerometer
     *
     * @param featureAccelerometer device's featureAccelerometer
     */
    public void setFeatureAccelerometer(boolean featureAccelerometer) {
        this.featureAccelerometer = featureAccelerometer;
    }

    /**
     * isFeatureBluetooth gets device's Bluetooth
     *
     * @return if device has bluetooth
     */
    public boolean isFeatureBluetooth() {
        return featureBluetooth;
    }

    /**
     * setFeatureBluetooth sets device's featureBluetooth
     *
     * @param featureBluetooth device's featureBluetooth
     */
    public void setFeatureBluetooth(boolean featureBluetooth) {
        this.featureBluetooth = featureBluetooth;
    }

    /**
     * isFeatureCompass gets device's Compass
     *
     * @return if device has compass
     */
    public boolean isFeatureCompass() {
        return featureCompass;
    }

    /**
     * setFeatureCompass sets device's featureCompass
     *
     * @param featureCompass device's featureCompass
     */
    public void setFeatureCompass(boolean featureCompass) {
        this.featureCompass = featureCompass;
    }

    /**
     * isFeatureGps gets device's Gps
     *
     * @return if device has gps
     */
    public boolean isFeatureGps() {
        return featureGps;
    }

    /**
     * setFeatureGps sets device's featureGps
     *
     * @param featureGps device's featureGps
     */
    public void setFeatureGps(boolean featureGps) {
        this.featureGps = featureGps;
    }

    /**
     * isFeatureGyroscope gets device's Gyroscope
     *
     * @return if device has gyroscope
     */
    public boolean isFeatureGyroscope() {
        return featureGyroscope;
    }

    /**
     * setFeatureGyroscope sets device's featureGyroscope
     *
     * @param featureGyroscope device's featureGyroscope
     */
    public void setFeatureGyroscope(boolean featureGyroscope) {
        this.featureGyroscope = featureGyroscope;
    }

    /**
     * isFeatureMicrophone gets device's latitude
     *
     * @return if device has microphone
     */
    public boolean isFeatureMicrophone() {
        return featureMicrophone;
    }

    /**
     * setFeatureMicrophone sets device's featureMicrophone
     *
     * @param featureMicrophone device's featureMicrophone
     */
    public void setFeatureMicrophone(boolean featureMicrophone) {
        this.featureMicrophone = featureMicrophone;
    }

    /**
     * isFeatureNfc gets device's featureNfc
     *
     * @return if device has nfc
     */
    public boolean isFeatureNfc() {
        return featureNfc;
    }

    /**
     * setFeatureNfc sets device's featureNfc
     *
     * @param featureNfc device's featureNfc
     */
    public void setFeatureNfc(boolean featureNfc) {
        this.featureNfc = featureNfc;
    }

    /**
     * isFeatureTelephony gets device's feature telephony
     *
     * @return if device has telephony
     */
    public boolean isFeatureTelephony() {
        return featureTelephony;
    }

    /**
     * setFeatureTelephony sets device's featureTelephony
     *
     * @param featureTelephony device's featureTelephony
     */
    public void setFeatureTelephony(boolean featureTelephony) {
        this.featureTelephony = featureTelephony;
    }

    /**
     * isFeatureTouchScreen gets device's feature touch screen
     *
     * @return if device has touch screen
     */
    public boolean isFeatureTouchScreen() {
        return featureTouchScreen;
    }

    /**
     * setFeatureTouchScreen sets device's feature touch screen
     *
     * @param featureTouchScreen device's feature touch screen
     */
    public void setFeatureTouchScreen(boolean featureTouchScreen) {
        this.featureTouchScreen = featureTouchScreen;
    }

    /**
     * getManufacturer get device's manufacturer
     *
     * @return device's manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * setManufacturer sets device's manufacturer
     *
     * @param manufacturer device's manufacturer
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * getScreenDensity gets device's screen density
     *
     * @return device's screen density
     */
    public float getScreenDensity() {
        return screenDensity;
    }

    /**
     * setScreenDensity sets device's screen density
     *
     * @param screenDensity device's screen density
     */
    public void setScreenDensity(float screenDensity) {
        this.screenDensity = screenDensity;
    }
}
