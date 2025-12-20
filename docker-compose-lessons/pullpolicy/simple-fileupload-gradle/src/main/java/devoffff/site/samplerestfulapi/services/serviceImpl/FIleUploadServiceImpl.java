package devoffff.site.samplerestfulapi.services.serviceImpl;

import devoffff.site.samplerestfulapi.services.IFileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Service
public class FIleUploadServiceImpl implements IFileUploadService {
    @Value("${file.upload-file-dir}")
    private String fileUploadDirectory;
    @Value("${file.upload-img-dir}")
    private String imageUploadDirectory;
    public static boolean isImageFile(MultipartFile file) {
        // Get the content type of the file
        String contentType = file.getContentType();

        // List of common image content types
        String[] imageContentTypes = {
                "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp",
                "image/svg+xml", "image/tiff", "image/vnd.microsoft.icon"
        };

        for (String type : imageContentTypes) {
            if (type.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isNormalFile(MultipartFile file) {
        // Get the content type of the file
        String contentType = file.getContentType();
        // List of common safe file content types
        String[] safeFileContentTypes = {
                "application/json", "text/plain", "application/xml", "application/pdf",
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/vnd.ms-word", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        };

        // Check if the content type is a normal file type
        for (String type : safeFileContentTypes) {
            if (type.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
    // allow user to upload the images and files where they want
    private Path getUploadPath(String fileType) {
        Path uploadPath;
        if (fileType.equals("image")) {
            uploadPath = Path.of(imageUploadDirectory);
        } else if (fileType.equals("file")) {
            uploadPath = Path.of(fileUploadDirectory);
        } else throw new IllegalArgumentException("Invalid file type");
// create the directory if it does not exist
        if (!uploadPath.toFile().exists()) {
            uploadPath.toFile().mkdir();
        }
        return uploadPath;
    }



    private String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("FIle should not be empty");
        }
        String originalExtension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() +originalExtension;  // StringUtils
        String fileType = isImageFile(file) ? "image" : isNormalFile(file) ? "file": "whatever";
        Path uploadPath = getUploadPath(fileType);
        if(!Files.exists(uploadPath)){
          Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Could not save file: " + fileName, e);
        }
        return fileName;
    }

    @Override
    public String uploadSingleFile(MultipartFile file) throws IOException {
        return uploadFile(file);
    }

    @Override
    public List<String> uploadMultipleFiles(MultipartFile[] files) throws IOException {

        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            fileNames.add(uploadFile(file));
        }
        return fileNames;
    }

    @Override
    public boolean deleteFile(String fileName, String fileType) throws IOException {
        Path filePath = getUploadPath(fileType).resolve(fileName);
        System.out.println("This is the location of filePath: "+filePath);
        return  Files.deleteIfExists(filePath);

    }
}
