import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PredictInfo {
	
	@JsonProperty("tagId")
	private String tagId;
	
	@JsonProperty("tagName")
	private String tagName;
	
	@JsonProperty("probability")
	private double probability;
	
	public PredictInfo(String tagId, String tagName, double probability){
		this.tagId = tagId;
		this.tagName = tagName;
		this.probability = probability;
	}
	
	public PredictInfo(){
		
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	
}
