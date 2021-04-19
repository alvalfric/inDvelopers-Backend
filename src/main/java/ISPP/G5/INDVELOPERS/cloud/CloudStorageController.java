package ISPP.G5.INDVELOPERS.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/file")
public class CloudStorageController {

    @Autowired
    private CloudStorageService cloudStorageService;
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(cloudStorageService.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = cloudStorageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        if(this.cloudStorageService.canDownloadGame(fileName)) {
            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        }else {
            return ResponseEntity
            		.status(HttpStatus.FORBIDDEN)
                    .contentLength(data.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(null);
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        if(this.cloudStorageService.isOwnerOfTheFile(fileName)) {
        	System.out.println("entraaborrar");
            return new ResponseEntity<>(cloudStorageService.deleteFile(fileName), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You are not the owner of the file", HttpStatus.FORBIDDEN);

        }
    }
}