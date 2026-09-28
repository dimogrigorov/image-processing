package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.exception.InvalidProcessingOptionsException;
import com.oursprivacy.imageprocessing.model.ImageProcessingOptions;
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

    public ProcessedImage process(ImageProcessingOptions options) {

        validate(options);

        ProcessedImage image = imageDownloadService.download(options.getUrl());

        if (options.getWidth() != null) {

            if ("fill".equalsIgnoreCase(options.getCrop())) {
                image = imageProcessor.resizeAndCrop(
                        image,
                        options.getWidth(),
                        options.getHeight());
            } else {
                image = imageProcessor.resize(
                        image,
                        options.getWidth(),
                        options.getHeight());
            }
        }

        return image;
    }

    private void validate(ImageProcessingOptions options) {

        if (options.getUrl() == null ||
                options.getUrl().isBlank()) {

            throw new InvalidProcessingOptionsException(
                    "Image URL is required");
        }

        boolean widthProvided = options.getWidth() != null;
        boolean heightProvided = options.getHeight() != null;

        if (widthProvided != heightProvided) {
            throw new InvalidProcessingOptionsException(
                    "Width and height must be provided together");
        }

        if (widthProvided &&
                (options.getWidth() <= 0 ||
                        options.getHeight() <= 0)) {

            throw new InvalidProcessingOptionsException(
                    "Width and height must be greater than zero");
        }

        if (options.getCrop() != null &&
                !"fill".equalsIgnoreCase(options.getCrop())) {

            throw new InvalidProcessingOptionsException(
                    "Unsupported crop mode: " + options.getCrop());
        }

        if (options.getCrop() != null && !widthProvided) {
            throw new InvalidProcessingOptionsException(
                    "Crop requires width and height");
        }
    }
}