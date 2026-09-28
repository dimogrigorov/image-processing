package com.oursprivacy.imageprocessing.model;

public final class ImageProcessingOptions {

    private final String url;
    private final Integer width;
    private final Integer height;
    private final String crop;

    public ImageProcessingOptions(
            String url,
            Integer width,
            Integer height,
            String crop) {

        this.url = url;
        this.width = width;
        this.height = height;
        this.crop = crop;
    }

    public String getUrl() {
        return url;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getCrop() {
        return crop;
    }
}