import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Recognizer {
	
	public PredictInfo execute(File recogImage, String propertiesPath) throws ClientProtocolException, URISyntaxException, IOException{
		
		String recogInfo = getRecogInfo(recogImage, propertiesPath);
		if(recogInfo.equals("error")) return (new PredictInfo("", "error", 0.0));
		
		// JSON file to Java object
		ObjectMapper om = new ObjectMapper();
		RecogEntity recogEntity = om.readValue(recogInfo, RecogEntity.class);
		PredictInfo[] infos = recogEntity.getPredictions();
		
		// get PredictInfo instance that has maximum probability
		double max = 0.0;
		int cnt = 0;
		int infoIdx = -1;
		for(PredictInfo info : infos){
			double prob = info.getProbability();
			if(max < prob){
				max = prob;
				infoIdx = cnt;
			}
			cnt++;
		}
		
		if(infoIdx == -1) return (new PredictInfo("", "rejected", 0.0));
		
		PredictInfo result = infos[infoIdx];
		return result;
	}
	
	
	private String getRecogInfo(File recogImage, String propertiesPath) throws URISyntaxException, ClientProtocolException, IOException{
		
		// Load properties
		Properties properties = PropertiesLoader.load(propertiesPath);
		
		// Create http client
		HttpClient httpclient = HttpClients.createDefault();
		String projectId = properties.getProperty("projectId");
		String httpUrl = "https://southcentralus.api.cognitive.microsoft.com/"
						+ "customvision/v2.0/Prediction/" + projectId + "/image";
		URIBuilder builder = new URIBuilder(httpUrl);

//        builder.setParameter("iterationId", "{string}");
//        builder.setParameter("application", "{string}");

		// Request header
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);
		String predictionKey = properties.getProperty("predictionKey");
		request.setHeader("Content-Type", "application/octet-stream");
		request.setHeader("Prediction-key", predictionKey);

		// Request body
		FileEntity reqEntity = new FileEntity(recogImage);
		request.setEntity(reqEntity);

		// Get response
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
        
		if (entity != null){
			String entityString = EntityUtils.toString(entity);
			return entityString;
		}
		return "error";
	}
}
