package com.oursprivacy.imageprocessing;

import com.oursprivacy.imageprocessing.model.ProcessedImage;
import com.oursprivacy.imageprocessing.service.ImageProcessor;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImageProcessorTest {

    private final ImageProcessor imageProcessor =
            new ImageProcessor();

    @Test
    void shouldResizeImage() throws IOException {

        ProcessedImage original =
                createTestImage(1200, 600, "jpg");

        ProcessedImage result =
                imageProcessor.process(
                        original,
                        500,
                        300,
                        null,
                        null,
                        null);

        BufferedImage resultImage =
                ImageIO.read(
                        new ByteArrayInputStream(result.getData()));

        assertNotNull(resultImage);
        assertEquals(500, resultImage.getWidth());
        assertEquals(250, resultImage.getHeight());
        assertEquals("image/jpeg", result.getContentType());
    }

    @Test
    void shouldResizeAndCropToExactDimensions() throws IOException {

        ProcessedImage original =
                createTestImage(1200, 600, "jpg");

        ProcessedImage result =
                imageProcessor.process(
                        original,
                        500,
                        300,
                        "fill",
                        null,
                        null);

        BufferedImage resultImage =
                ImageIO.read(
                        new ByteArrayInputStream(result.getData()));

        assertNotNull(resultImage);
        assertEquals(500, resultImage.getWidth());
        assertEquals(300, resultImage.getHeight());
    }

    @Test
    void shouldConvertJpegToPng() throws IOException {

        ProcessedImage original =
                createTestImage(800, 600, "jpg");

        ProcessedImage result =
                imageProcessor.process(
                        original,
                        null,
                        null,
                        null,
                        "png",
                        null);

        assertEquals("image/png", result.getContentType());

        BufferedImage resultImage =
                ImageIO.read(
                        new ByteArrayInputStream(result.getData()));

        assertNotNull(resultImage);
        assertEquals(800, resultImage.getWidth());
        assertEquals(600, resultImage.getHeight());

        // Verify that the actual bytes contain the PNG signature
        byte[] data = result.getData();

        assertEquals((byte) 0x89, data[0]);
        assertEquals((byte) 0x50, data[1]);
        assertEquals((byte) 0x4E, data[2]);
        assertEquals((byte) 0x47, data[3]);
    }

    private ProcessedImage createTestImage(
            int width,
            int height,
            String format) throws IOException {

        BufferedImage image =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.BLUE);
        graphics.fillRect(0, 0, width, height);

        graphics.dispose();

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        ImageIO.write(image, format, output);

        String contentType =
                switch (format) {
                    case "jpg", "jpeg" -> "image/jpeg";
                    case "png" -> "image/png";
                    default -> throw new IllegalArgumentException(
                            "Unsupported test format: " + format);
                };

        return new ProcessedImage(
                output.toByteArray(),
                contentType);
    }
}