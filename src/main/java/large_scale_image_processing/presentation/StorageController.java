package large_scale_image_processing.presentation;

import large_scale_image_processing.application.StorageService;
import large_scale_image_processing.global.CompleteUploadRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class StorageController {

    private final StorageService storageService;

    public StorageController(final StorageService storageService) {
        this.storageService = storageService;
    }

    // 파일 한 번에 업로드
    @PostMapping("/upload")
    public void upload(@RequestParam final MultipartFile file) {
        storageService.store(file);
    }

    // 파일 쪼개서 업로드
    @PostMapping("/uploadChunk")
    public void uploadChunk(@RequestParam final MultipartFile file,
                            @RequestParam final String fileName,
                            @RequestParam final int chunkIndex) {
        storageService.storeChunk(file, fileName, chunkIndex);
    }

    // 서버에서 합치기
    @PostMapping("/completeUpload")
    public void completeUpload(@RequestBody final CompleteUploadRequest request) {
        storageService.combineChunks(request.fileName(), request.totalChunks());
    }
}
