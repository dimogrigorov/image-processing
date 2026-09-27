package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.model.ProcessedImage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ImageDownloadService {
    
    // synchronous HTTP client.
    private final RestClient restClient;

    public ImageDownloadService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public ProcessedImage download(String url) {

        return restClient.get()
                .uri(url)
                .exchange((request, response) -> {

                    byte[] data = response.getBody().readAllBytes();

                    String contentType = response.getHeaders()
                            .getContentType()
                            .toString();

                    return new ProcessedImage(data, contentType);
                });
    }
}