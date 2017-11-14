import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictInfo {
	
	@JsonProperty("TagId")
	private String tagId;
	
	@JsonProperty("Tag")
	private String tag;
	
	@JsonProperty("Probability")
	private double probability;
	
	public PredictInfo(String tagId, String tag, double probability){
		this.tagId = tagId;
		this.tag = tag;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	
}