package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.model.ProcessedImage;
import org.springframework.stereotype.Service;

@Service
public class ImageProcessingService {

    private final ImageDownloadService imageDownloadService;

    public ImageProcessingService(ImageDownloadService imageDownloadService) {
        this.imageDownloadService = imageDownloadService;
    }

    public ProcessedImage process(String url) {
        return imageDownloadService.download(url);
    }
}