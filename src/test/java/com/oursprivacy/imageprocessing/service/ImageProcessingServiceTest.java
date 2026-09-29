package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.model.ImageProcessingOptions;
import com.oursprivacy.imageprocessing.model.ProcessedImage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageProcessingServiceTest {

    @Test
    void shouldReturnOriginalImageWhenNoProcessingRequested() {

        ImageDownloadService downloadService =
                mock(ImageDownloadService.class);

        ImageProcessor imageProcessor =
                mock(ImageProcessor.class);

        ImageProcessingService service =
                new ImageProcessingService(
                        downloadService,
                        imageProcessor);

        String url = "https://example.com/image.jpg";

        ProcessedImage expected =
                new ProcessedImage(
                        new byte[]{1, 2, 3},
                        "image/jpeg");

        when(downloadService.download(url))
                .thenReturn(expected);

        ImageProcessingOptions options =
                new ImageProcessingOptions(
                        url,
                        null,
                        null,
                        null,
                        null,
                        null);

        ProcessedImage actual =
                service.process(options);

        assertSame(expected, actual);

        verify(downloadService).download(url);
    }

    @Test
    void shouldResizeImageWhenDimensionsProvided() {

        ImageDownloadService downloadService =
                mock(ImageDownloadService.class);

        ImageProcessor imageProcessor =
                mock(ImageProcessor.class);

        ImageProcessingService service =
                new ImageProcessingService(
                        downloadService,
                        imageProcessor);

        String url = "https://example.com/image.jpg";

        ProcessedImage original =
                new ProcessedImage(
                        new byte[]{1, 2, 3},
                        "image/jpeg");

        ProcessedImage resized =
                new ProcessedImage(
                        new byte[]{4, 5, 6},
                        "image/jpeg");

        when(downloadService.download(url))
                .thenReturn(original);

        when(imageProcessor.process(
                original,
                500,
                300,
                null,
                null,
                null))
                .thenReturn(resized);

        ImageProcessingOptions options =
                new ImageProcessingOptions(
                        url,
                        500,
                        300,
                        null,
                        null,
                        null);

        ProcessedImage actual =
                service.process(options);

        assertSame(resized, actual);

        verify(downloadService).download(url);

        verify(imageProcessor).process(
                original,
                500,
                300,
                null,
                null,
                null);
    }

    @Test
    void shouldResizeAndCropWhenCropIsFill() {

        ImageDownloadService downloadService =
                mock(ImageDownloadService.class);

        ImageProcessor imageProcessor =
                mock(ImageProcessor.class);

        ImageProcessingService service =
                new ImageProcessingService(
                        downloadService,
                        imageProcessor);

        String url = "https://example.com/image.jpg";

        ProcessedImage original =
                new ProcessedImage(
                        new byte[]{1, 2, 3},
                        "image/jpeg");

        ProcessedImage cropped =
                new ProcessedImage(
                        new byte[]{4, 5, 6},
                        "image/jpeg");

        when(downloadService.download(url))
                .thenReturn(original);

        when(imageProcessor.process(
                original,
                500,
                300,
                "fill",
                null,
                null))
                .thenReturn(cropped);

        ImageProcessingOptions options =
                new ImageProcessingOptions(
                        url,
                        500,
                        300,
                        "fill",
                        null,
                        null);

        ProcessedImage actual =
                service.process(options);

        assertSame(cropped, actual);

        verify(downloadService).download(url);

        verify(imageProcessor).process(
                original,
                500,
                300,
                "fill",
                null,
                null);
    }

    @Test
    void shouldConvertImageFormat() {

        ImageDownloadService downloadService =
                mock(ImageDownloadService.class);

        ImageProcessor imageProcessor =
                mock(ImageProcessor.class);

        ImageProcessingService service =
                new ImageProcessingService(
                        downloadService,
                        imageProcessor);

        String url = "https://example.com/image.png";

        ProcessedImage original =
                new ProcessedImage(
                        new byte[]{1, 2, 3},
                        "image/png");

        ProcessedImage converted =
                new ProcessedImage(
                        new byte[]{4, 5, 6},
                        "image/jpeg");

        when(downloadService.download(url))
                .thenReturn(original);

        when(imageProcessor.process(
                original,
                null,
                null,
                null,
                "jpeg",
                80))
                .thenReturn(converted);

        ImageProcessingOptions options =
                new ImageProcessingOptions(
                        url,
                        null,
                        null,
                        null,
                        "jpeg",
                        80);

        ProcessedImage actual =
                service.process(options);

        assertSame(converted, actual);

        verify(downloadService).download(url);

        verify(imageProcessor).process(
                original,
                null,
                null,
                null,
                "jpeg",
                80);
    }
}