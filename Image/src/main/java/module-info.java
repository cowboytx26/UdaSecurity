module com.udacity.catpoint.imageservice.Image{
    exports com.udacity.catpoint.imageservice;
    requires software.amazon.awssdk.services.rekognition;
    requires software.amazon.awssdk.core;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.auth;
    requires java.desktop;
    requires org.slf4j;
}