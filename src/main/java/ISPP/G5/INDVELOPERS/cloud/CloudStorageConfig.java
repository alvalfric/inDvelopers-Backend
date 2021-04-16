package ISPP.G5.INDVELOPERS.cloud;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class CloudStorageConfig {

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secret-key-encoded}")
	private String secretKeyEncoded;
	
	@Value("${cloud.aws.region.static}")
	private String region;
	
	@Bean
	@Primary
	public AmazonS3 generateS3client() {
		byte[] decodedBytes = Base64.getDecoder().decode(secretKeyEncoded);
		String secretKey = new String(decodedBytes);
		AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region).build();
	}
}