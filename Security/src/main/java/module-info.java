module com.udacity.catpoint.service.Security {
    requires com.udacity.catpoint.imageservice.Image;
    requires java.desktop;
    requires com.google.common;
    requires com.google.gson;
    requires java.prefs;
    requires com.miglayout.swing;
    opens com.udacity.catpoint.data to com.google.gson;
    //opens com.udacity.catpoint.service;
}