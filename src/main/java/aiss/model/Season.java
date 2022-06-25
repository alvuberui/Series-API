package aiss.model;

import java.util.ArrayList;
import java.util.List;

public class Season {
	
	private String id;
	private String name;
	private String description;
	private String year;
	private List<Chapter> chapters;
	
	
	/*
	 * Constructores 
	 */
	
	public Season() {} 
	
	public Season(String name) {
		this.name = name;
	}
	
	public Season(String name, String description, String year) { 
		this.name = name;
		this.description = description;
		this.year = year;
	}

	public Season(String name, String description, String year, List<Chapter> chapters) { 
		this.name = name;
		this.description = description;
		this.year = year;
		this.chapters = chapters;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	protected void setChapters(List<Chapter> s) { 
		this.chapters = s;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public List<Chapter> getChapters() {
		return this.chapters;
	}
	
	public Chapter getChapter(String id) {
		if(chapters == null) {
			return null;
		}
		Chapter chapter = null;
		for(Chapter s : chapters) {
			if(s.getId().equals(id)) {
				chapter = s;
				break;
			}
		}
		return chapter;
	}
	
	/*
	 * Derivadas
	 */
	public void addChapter(Chapter s) {
		if(chapters == null) {
			chapters = new ArrayList<Chapter>();
		}
		chapters.add(s);
	}
	
	public void deleteChapter(Chapter s) {
		chapters.remove(s);
	}
	
	public void deleteChapter(String id) {
		Chapter s = getChapter(id);
		if(s!=null) {
			chapters.remove(s);
		}
	}
}
