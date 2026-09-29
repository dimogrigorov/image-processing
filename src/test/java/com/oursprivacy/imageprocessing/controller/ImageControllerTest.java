package com.oursprivacy.imageprocessing.controller;

import com.oursprivacy.imageprocessing.model.ProcessedImage;
import com.oursprivacy.imageprocessing.service.ImageProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ImageProcessingService imageProcessingService;

    @Test
    void shouldReturnProcessedImage() throws Exception {

        ProcessedImage image =
                new ProcessedImage(
                        new byte[]{1, 2, 3},
                        "image/jpeg");

        when(imageProcessingService.process(any()))
                .thenReturn(image);

        mockMvc.perform(get("/process")
                        .param("url", "https://example.com/image.jpg")
                        .param("width", "500")
                        .param("height", "300"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));
    }

    @Test
    void shouldReturnBadRequestWhenUrlIsMissing() throws Exception {

        mockMvc.perform(get("/process"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldPassProcessingParametersToService() throws Exception {

        ProcessedImage image =
                new ProcessedImage(
                        new byte[]{1, 2, 3},
                        "image/webp");

        when(imageProcessingService.process(any()))
                .thenReturn(image);

        mockMvc.perform(get("/process")
                        .param("url", "https://example.com/image.jpg")
                        .param("width", "800")
                        .param("height", "600")
                        .param("crop", "fill")
                        .param("format", "webp")
                        .param("quality", "80"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/webp"));
    }
}