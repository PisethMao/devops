package devoffff.site.samplerestfulapi.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileUploadService {

    String uploadSingleFile(MultipartFile file) throws IOException;

    List<String> uploadMultipleFiles(MultipartFile[] files) throws IOException;
     public boolean  deleteFile(String fileName, String fileType) throws IOException;
}
