package large_scale_image_processing.global;

public record CompleteUploadRequest(
        String fileName,
        int totalChunks
) {
}
