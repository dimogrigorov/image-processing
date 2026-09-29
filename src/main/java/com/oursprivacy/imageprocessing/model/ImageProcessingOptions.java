package com.oursprivacy.imageprocessing.model;

public final class ImageProcessingOptions {

    private final String url;
    private final Integer width;
    private final Integer height;
    private final String crop;
    private final String format;
    private final Integer quality;

    public ImageProcessingOptions(
            String url,
            Integer width,
            Integer height,
            String crop,
            String format,
            Integer quality) {

        this.url = url;
        this.width = width;
        this.height = height;
        this.crop = crop;
        this.format = format;
        this.quality = quality;
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

    public String getFormat() {
        return format;
    }

    public Integer getQuality() {
        return quality;
    }
}