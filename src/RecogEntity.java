import com.fasterxml.jackson.annotation.JsonProperty;

public class RecogEntity {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("project")
	private String project;
	
	@JsonProperty("iteration")
	private String iteration;
	
	@JsonProperty("created")
	private String created;
	
	@JsonProperty("predictions")
	private PredictInfo[] predictions;
	

	@Override
	public String toString(){
		String ret = "";
		ret += "id: " + id + "\n"
			 + "project: " + project + "\n"
			 + "iteration: " + iteration + "\n"
			 + "created: " + created + "\n";
		for(PredictInfo info : predictions){
			ret += "[tagId: " + info.getTagId() + "\n"
				 + " tagName: " + info.getTagName() + "\n"
				 + " probability: " + info.getProbability() + "]\n";
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
