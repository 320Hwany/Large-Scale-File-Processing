package large_scale_image_processing.presentation;

import large_scale_image_processing.application.StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StorageController {

    private final StorageService storageService;

    public StorageController(final StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public void upload(@RequestParam final MultipartFile file) {
        storageService.store(file);
    }
}
