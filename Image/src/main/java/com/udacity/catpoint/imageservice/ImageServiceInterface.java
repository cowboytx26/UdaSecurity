package com.udacity.catpoint.imageservice;

import java.awt.image.BufferedImage;

public interface ImageServiceInterface {
    //public AwsImageService();
    public boolean imageContainsCat(BufferedImage image, float confidenceThreshhold);
    //private void logLabelsForFun(DetectLabelsResponse response);
}
