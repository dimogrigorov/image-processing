package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.model.ProcessedImage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageProcessingServiceTest {

    @Test
    void shouldDownloadImage() {

        ImageDownloadService downloadService =
                mock(ImageDownloadService.class);

        ImageProcessingService service =
                new ImageProcessingService(downloadService);

        String url = "https://example.com/image.jpg";

        ProcessedImage expected =
                new ProcessedImage(
                        new byte[]{1, 2, 3},
                        "image/jpeg");

        when(downloadService.download(url))
                .thenReturn(expected);

        ProcessedImage actual =
                service.process(url);

        assertSame(expected, actual);

        verify(downloadService).download(url);
    }
}