package com.oursprivacy.imageprocessing.service;

import com.oursprivacy.imageprocessing.exception.ImageDownloadException;
import com.oursprivacy.imageprocessing.model.ProcessedImage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class ImageDownloadService {

    private final RestClient restClient;

    public ImageDownloadService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public ProcessedImage download(String url) {

        try {
            ResponseEntity<byte[]> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(byte[].class);

            MediaType contentType = response.getHeaders().getContentType();

            if (contentType == null) {
                throw new ImageDownloadException(
                        "Remote server did not provide a Content-Type");
            }

            if (!"image".equalsIgnoreCase(contentType.getType())) {
                throw new ImageDownloadException(
                        "URL does not point to an image");
            }

            byte[] data = response.getBody();

            if (data == null || data.length == 0) {
                throw new ImageDownloadException(
                        "Downloaded image is empty");
            }

            return new ProcessedImage(
                    data,
                    contentType.toString());

        } catch (ImageDownloadException e) {
            throw e;

        } catch (RestClientException | IllegalArgumentException e) {
            throw new ImageDownloadException(
                    "Failed to download image", e);
        }
    }
}