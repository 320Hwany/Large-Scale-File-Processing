package large_scale_image_processing.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.concurrent.*;


@Service
public class StorageService {

    @Value("${storage.location}")
    private Path rootLocation;

    // 파일 한 번에 업로드
    public void store(final MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    // 파일 쪼개서 업로드
    public void storeChunk(final MultipartFile file, final String fileName, final int chunkIndex) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file chunk.");
            }
            Path chunkFile = this.rootLocation.resolve(Paths.get(fileName + ".part" + chunkIndex))
                    .normalize().toAbsolutePath();
            if (!chunkFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new RuntimeException("Cannot store file chunk outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, chunkFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file chunk.", e);
        }
    }

    // 서버에서 합치기
    public void combineChunks(final String fileName, final int totalChunks) {
        Path finalFile = this.rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

        try (OutputStream outputStream = Files.newOutputStream(finalFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (int chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
                Path chunkFile = this.rootLocation.resolve(Paths.get(fileName + ".part" + chunkIndex)).normalize().toAbsolutePath();
                if (!chunkFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                    // This is a security check
                    throw new RuntimeException("Cannot read file chunk outside current directory.");
                }
                try (InputStream inputStream = Files.newInputStream(chunkFile, StandardOpenOption.READ)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read file chunk: " + chunkFile, e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to combine file chunks.", e);
        }
    }
}