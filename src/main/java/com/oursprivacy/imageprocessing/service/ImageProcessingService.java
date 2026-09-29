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

        ProcessedImage original = imageDownloadService.download(options.getUrl());
        boolean processingRequested = options.getWidth() != null ||
                options.getFormat() != null ||
                options.getQuality() != null ||
                options.getCrop() != null;

        if (!processingRequested) {
            return original;
        }
        
        original = imageProcessor.process(
                original,
                options.getWidth(),
                options.getHeight(),
                options.getCrop(),
                options.getFormat(),
                options.getQuality());

        return original;
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

        if (options.getQuality() != null &&
                (options.getQuality() < 1 ||
                        options.getQuality() > 100)) {

            throw new InvalidProcessingOptionsException(
                    "Quality must be between 1 and 100");
        }

        if (options.getFormat() != null) {

            String format = options.getFormat().toLowerCase();

            if (!format.equals("jpeg") &&
                    !format.equals("jpg") &&
                    !format.equals("png") &&
                    !format.equals("webp")) {

                throw new InvalidProcessingOptionsException(
                        "Unsupported format: " + options.getFormat());
            }
        }

        if (options.getQuality() != null &&
                options.getFormat() == null) {

            throw new InvalidProcessingOptionsException(
                    "Quality requires an output format");
        }
    }
}