package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.exception.ImageProcessingException;
import com.oursprivacy.imageprocessing.model.ProcessedImage;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageProcessor {

    public ProcessedImage resize(
            ProcessedImage image,
            int width,
            int height) {

        try {
            ByteArrayInputStream input =
                    new ByteArrayInputStream(image.getData());

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();

            Thumbnails.of(input)
                    .size(width, height)
                    .outputFormat(getFormat(image.getContentType()))
                    .toOutputStream(output);

            return new ProcessedImage(
                    output.toByteArray(),
                    image.getContentType());

        } catch (IOException e) {
            throw new ImageProcessingException(
                    "Failed to resize image", e);
        }
    }

    private String getFormat(String contentType) {

        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            default -> throw new ImageProcessingException(
                    "Unsupported image format: " + contentType);
        };
    }
}