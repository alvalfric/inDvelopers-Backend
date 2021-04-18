package ISPP.G5.INDVELOPERS.cloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;
import ISPP.G5.INDVELOPERS.services.OwnedGameService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CloudStorageService {

	@Value("${application.bucket.name}")
	private String bucketName;
	
	@Autowired
	private AmazonS3 s3Client;
    @Autowired
    private DeveloperService developerService;
    @Autowired
    private GameService gameService;
    @Autowired
    private OwnedGameService ownedGameService;
    
	public String uploadFile(MultipartFile multipartFile) {
        File file = convertMultiPartFileToFile(multipartFile);
		String fileName = System.currentTimeMillis()+"_"+multipartFile.getOriginalFilename();
				
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
		file.delete();
		return fileName;
	}
	
	public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
        	byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}
	
    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed";
    }
	
    private File convertMultiPartFileToFile(MultipartFile multipartFile) {
        File convertedFile = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
    
    public boolean isOwnerOfTheFile(String fileName) {
    	boolean modifyAccess = false;
        Developer currentDeveloper = this.developerService.findCurrentDeveloper();
        List<Game> createdGames = this.gameService.findByMyGames(currentDeveloper.getId());
        
        for(Game myGame: createdGames) {
        	if(fileName == myGame.getIdCloud()) {
        		modifyAccess = true;
        	}
        }
        
        return modifyAccess;
    }
    
    public boolean canDownloadGame(String fileName) {
    	boolean downloadAccess = false;
        Developer currentDeveloper = this.developerService.findCurrentDeveloper();
        List<Game> purchasedGames = this.ownedGameService.findAllMyOwnedGames(currentDeveloper);
        if(currentDeveloper.getRoles().contains(UserRole.ADMIN)) {
        	downloadAccess=true;
        }else {
        for(Game myGame: purchasedGames) {
        	if(fileName.equals(myGame.getIdCloud())) {
        		downloadAccess = true;
        		break;
        	}
        }
        }
        
        return downloadAccess;
    }
}
