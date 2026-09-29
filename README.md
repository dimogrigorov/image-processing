# Image Processing Service

A Spring Boot service for downloading and processing remote images.

The API supports image resizing, cropping, format conversion, and output quality configuration.

## Requirements

For local development:

- Java 21
- Maven 3.8+

For containerized execution:

- Docker

## Running the Application

Clone the repository and run:

```bash
mvn clean test
mvn spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

## Running with Docker

First build the application JAR:

```bash
mvn clean package
```

Build the Docker image:

```bash
docker build -t image-processing-service .
```

Run the container:

```bash
docker run --rm -p 8080:8080 image-processing-service
```

The API is then available at:

```text
http://localhost:8080
```

Test the running container:

```bash
curl \
"http://localhost:8080/process?url=https://fileinfo.com/img/ss/xl/jpg_44-2.jpg&width=500&height=300&crop=fill" \
-o docker-test.jpg
```

Verify the generated image:

```bash
file docker-test.jpg
```

Stop the container with `Ctrl+C`.

## API

### Process Image

```http
GET /process
```

### Parameters

| Parameter | Required | Description |
|---|---|---|
| `url` | Yes | URL of the source image |
| `width` | No | Target width |
| `height` | No | Target height |
| `format` | No | Output format: `jpeg`, `jpg`, `png`, or `webp` |
| `quality` | No | Output quality from `1` to `100` |
| `crop` | No | Crop mode. Currently supports `fill` |

`width` and `height` must be supplied together.

`quality` requires an output `format`.

## Examples

### Download an Image Without Transformation

```bash
curl \
"http://localhost:8080/process?url=https://example.com/image.jpg" \
-o image.jpg
```

### Resize an Image

```bash
curl \
"http://localhost:8080/process?url=https://example.com/image.jpg&width=500&height=300" \
-o resized.jpg
```

Resize preserves the original aspect ratio. The resulting image fits within the requested dimensions.

For example, a `1200x600` image resized to `500x300` results in `500x250`.

### Resize and Crop

```bash
curl \
"http://localhost:8080/process?url=https://example.com/image.jpg&width=500&height=300&crop=fill" \
-o cropped.jpg
```

`crop=fill` preserves the aspect ratio while cropping the center of the image to produce the exact requested dimensions.

### Convert Image Format

```bash
curl \
"http://localhost:8080/process?url=https://example.com/image.png&format=jpeg&quality=80" \
-o converted.jpg
```

### Combined Processing

```bash
curl \
"http://localhost:8080/process?url=https://example.com/image.jpg&width=800&height=600&format=webp&crop=fill&quality=80" \
-o processed.webp
```

## Error Handling

Invalid requests return an appropriate HTTP status and a JSON response.

Example:

```json
{
  "status": 400,
  "error": "Quality must be between 1 and 100"
}
```

Examples of invalid requests include:

- Invalid dimensions
- Width without height or height without width
- Unsupported output format
- Invalid quality
- Unsupported crop mode
- Non-image remote resources
- Failed remote image downloads

## Design

The application separates HTTP handling, orchestration, downloading, and image manipulation:

```text
ImageController
       |
       v
ImageProcessingService
       |
       +---- ImageDownloadService
       |
       +---- ImageProcessor
```

### ImageController

Handles the HTTP API and converts request parameters into processing options.

### ImageProcessingService

Validates processing options and coordinates image downloading and processing.

### ImageDownloadService

Retrieves remote image data and validates the remote content type.

### ImageProcessor

Performs resizing, cropping, format conversion, and output quality configuration.

Image manipulation is implemented using Thumbnailator, with additional ImageIO support for WebP.

## Testing

Run all tests with:

```bash
mvn clean test
```

The test suite includes:

- Service orchestration tests using Mockito
- Image processing tests using real generated images
- Controller/API tests using MockMvc

The image processing tests verify actual output dimensions and image format rather than only mocked interactions.

## Supported Formats

The service currently supports:

- JPEG
- PNG
- WebP

## Technology

- Java 21
- Spring Boot
- Spring MVC
- Thumbnailator
- ImageIO WebP
- JUnit 5
- Mockito
- Maven
- Docker
