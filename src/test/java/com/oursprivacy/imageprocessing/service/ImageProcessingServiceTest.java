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
        void shouldReturnOriginalImageWhenNoResizeRequested() {

                ImageDownloadService downloadService = mock(ImageDownloadService.class);

                ImageProcessor imageProcessor = mock(ImageProcessor.class);

                ImageProcessingService service = new ImageProcessingService(
                                downloadService,
                                imageProcessor);

                String url = "https://example.com/image.jpg";

                ProcessedImage expected = new ProcessedImage(
                                new byte[] { 1, 2, 3 },
                                "image/jpeg");

                when(downloadService.download(url))
                                .thenReturn(expected);
                ImageProcessingOptions ipo = new ImageProcessingOptions(url, null, null, url)
                ProcessedImage actual = service.process(ipo);

                assertSame(expected, actual);

                verify(downloadService).download(url);
        }

        @Test
        void shouldResizeImageWhenDimensionsProvided() {

                ImageDownloadService downloadService = mock(ImageDownloadService.class);

                ImageProcessor imageProcessor = mock(ImageProcessor.class);

                ImageProcessingService service = new ImageProcessingService(
                                downloadService,
                                imageProcessor);

                String url = "https://example.com/image.jpg";

                ProcessedImage original = new ProcessedImage(
                                new byte[] { 1, 2, 3 },
                                "image/jpeg");

                ProcessedImage resized = new ProcessedImage(
                                new byte[] { 4, 5, 6 },
                                "image/jpeg");

                when(downloadService.download(url))
                                .thenReturn(original);

                when(imageProcessor.resize(
                                original, 500, 300))
                                .thenReturn(resized);

                ImageProcessingOptions ipo = new ImageProcessingOptions(url, 500, 300, null)

                ProcessedImage actual = service.process(ipo);

                assertSame(resized, actual);

                verify(downloadService).download(url);

                verify(imageProcessor)
                                .resize(original, 500, 300);
        }

        @Test
        void shouldResizeAndCropWhenCropIsFill() {

                ImageDownloadService downloadService = mock(ImageDownloadService.class);

                ImageProcessor imageProcessor = mock(ImageProcessor.class);

                ImageProcessingService service = new ImageProcessingService(
                                downloadService,
                                imageProcessor);

                String url = "https://example.com/image.jpg";

                ProcessedImage original = new ProcessedImage(
                                new byte[] { 1, 2, 3 },
                                "image/jpeg");

                ProcessedImage cropped = new ProcessedImage(
                                new byte[] { 4, 5, 6 },
                                "image/jpeg");

                when(downloadService.download(url))
                                .thenReturn(original);

                when(imageProcessor.resizeAndCrop(
                                original,
                                500,
                                300))
                                .thenReturn(cropped);

                ImageProcessingOptions options = new ImageProcessingOptions(
                                url,
                                500,
                                300,
                                "fill");

                ProcessedImage actual = service.process(options);

                assertSame(cropped, actual);

                verify(imageProcessor)
                                .resizeAndCrop(original, 500, 300);
        }
}