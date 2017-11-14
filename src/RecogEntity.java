import com.fasterxml.jackson.annotation.JsonProperty;

public class RecogEntity {
	
	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("Project")
	private String project;
	
	@JsonProperty("Iteration")
	private String iteration;
	
	@JsonProperty("Created")
	private String created;
	
	@JsonProperty("Predictions")
	private PredictInfo[] predictions;
	

	@Override
	public String toString(){
		String ret = "";
		ret += "Id: " + id + "\n"
			 + "Project: " + project + "\n"
			 + "Iteration: " + iteration + "\n"
			 + "Created: " + created + "\n";
		for(PredictInfo info : predictions){
			ret += "[TagId: " + info.getTagId() + "\n"
				 + " Tag: " + info.getTag() + "\n"
				 + " Probability: " + info.getProbability() + "]\n";
		}
		return ret;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIteration() {
		return iteration;
	}

	public void setIteration(String iteration) {
		this.iteration = iteration;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public PredictInfo[] getPredictions() {
		return predictions;
	}

	public void setPredictions(PredictInfo[] predictions) {
		this.predictions = predictions;
	}
}
