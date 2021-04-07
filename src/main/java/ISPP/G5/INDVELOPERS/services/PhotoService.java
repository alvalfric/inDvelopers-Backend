package ISPP.G5.INDVELOPERS.services;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ISPP.G5.INDVELOPERS.models.Photo;
import ISPP.G5.INDVELOPERS.repositories.PhotoRepository;

@Service
public class PhotoService {
	
	@Autowired
    private PhotoRepository photoRepository;

	
	public String addPhoto(String title, MultipartFile file) throws IOException { 
        Photo photo = new Photo(); 
        photo.setTitle(title);
        photo.setImage(
          new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
        photo = photoRepository.insert(photo);
        return photo.getId(); 
    }

    public Photo getPhoto(String id) { 
        return photoRepository.findById(id).get(); 
    }
}
