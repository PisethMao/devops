package devoffff.site.samplerestfulapi.restcontroller;


import devoffff.site.samplerestfulapi.services.IFileUploadService;
import devoffff.site.samplerestfulapi.utils.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file-upload")
public class FileUploadRestController {
    private final IFileUploadService fileUploadService;

    @Autowired
    public FileUploadRestController(IFileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }


    private String constructImageUrl(HttpServletRequest request, String fileName) {
        // Assuming images are served from '/images'
        return String.format("%s://%s:%d/images/%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                fileName);
    }

    @PostMapping(value = "/single-file", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload Single File")
    public BaseResponse<?> uploadSingleFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return BaseResponse.<Object>builder()
                .payload(constructImageUrl(request, fileUploadService.uploadSingleFile(file)))
                .message("Successfully Upload File")
                .status(200)
                .build();
    }

    @PostMapping(value = "/multiple-files", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Multiple Files")
    public BaseResponse<?> uploadMultipleFiles(
            @RequestPart("files") MultipartFile[] files,
            HttpServletRequest request)
            throws IOException {


        return BaseResponse.<Object>builder()
                // convert the List to Arrays since we need arrays
                .payload(fileUploadService.uploadMultipleFiles(files))
                .message("Successfully Upload Files")
                .status(200)
                .build();
    }


    @DeleteMapping("/delete-file")
    public BaseResponse<?> deleteFile(
            @RequestParam String fileName,
            @RequestParam String fileType) throws IOException {
        return BaseResponse.<Object>builder()
                .payload(fileUploadService.deleteFile(fileName, fileType))
                .message("Successfully Deleted File")
                .status(200)
                .build();
    }
}
