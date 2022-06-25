package aiss.model;

public class Chapter {
	
	private String id;
	private String title;
	private String duration;
	
	/*
	 * Constructores
	 */
	public Chapter() {
	}

	public Chapter(String title, String duration) {
		this.title = title;
		this.duration = duration;
	}
	
	public Chapter(String id, String title, String duration) {
		this.id=id;
		this.title = title;
		this.duration = duration;
	}
	
	
	/*
	 * GETTERS AND SETTERS
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
}
