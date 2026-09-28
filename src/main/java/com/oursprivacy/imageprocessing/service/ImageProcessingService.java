package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.model.ProcessedImage;
import org.springframework.stereotype.Service;

@Service
public class ImageProcessingService {

    private final ImageDownloadService imageDownloadService;
    private final ImageProcessor imageProcessor;

    public ImageProcessingService(
            ImageDownloadService imageDownloadService,
            ImageProcessor imageProcessor) {

        this.imageDownloadService = imageDownloadService;
        this.imageProcessor = imageProcessor;
    }

    public ProcessedImage process(
            String url,
            Integer width,
            Integer height) {

        ProcessedImage image = imageDownloadService.download(url);

        if (width != null && height != null) {
            image = imageProcessor.resize(
                    image,
                    width,
                    height);
        }

        return image;
    }
}