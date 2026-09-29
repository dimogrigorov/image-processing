package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.exception.ImageProcessingException;
import com.oursprivacy.imageprocessing.model.ProcessedImage;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageProcessor {

    public ProcessedImage process(
            ProcessedImage image,
            Integer width,
            Integer height,
            String crop,
            String format,
            Integer quality) {

        try {
            ByteArrayInputStream input = new ByteArrayInputStream(image.getData());

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            String outputFormat = format != null
                    ? normalizeFormat(format)
                    : getFormat(image.getContentType());

            Thumbnails.Builder<?> builder = Thumbnails.of(input);

            if (width != null && height != null) {

                builder.size(width, height);

                if ("fill".equalsIgnoreCase(crop)) {
                    builder.crop(Positions.CENTER);
                }
            } else {
                builder.scale(1.0);
            }

            builder.outputFormat(outputFormat);

            if (quality != null) {
                builder.outputQuality(quality / 100.0);
            }

            builder.toOutputStream(output);

            return new ProcessedImage(
                    output.toByteArray(),
                    getContentType(outputFormat));

        } catch (IOException e) {
            throw new ImageProcessingException(
                    "Failed to process image", e);
        }
    }

    private String getContentType(String format) {

        return switch (format.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            default -> throw new ImageProcessingException(
                    "Unsupported output format: " + format);
        };
    }

    private String getFormat(String contentType) {

        return switch (contentType.toLowerCase()) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new ImageProcessingException(
                    "Unsupported image format: " + contentType);
        };
    }

    private String normalizeFormat(String format) {

        if ("jpeg".equalsIgnoreCase(format)) {
            return "jpg";
        }

        return format.toLowerCase();
    }
}