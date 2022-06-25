package aiss.model;

import java.util.ArrayList;
import java.util.List;

public class Serie {

	private String id;
	private String name;
	private List<Season> seasons;
	
	
	/*
	 * Constructores 
	 */
	
	public Serie() {} 
	
	public Serie(String name) {
		this.name = name;
	}
	
	public Serie(String name, List<Season> seasons) { 
		this.name = name;
		this.seasons = seasons;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	protected void setSeasons(List<Season> s) { 
		this.seasons = s;
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
	
	public List<Season> getSeasons() {
		return this.seasons;
	}
	
	public Season getSeason(String id) {
		if(seasons == null) {
			return null;
		}
		Season season = null;
		for(Season s : seasons) {
			if(s.getId().equals(id)) {
				season = s;
				break;
			}
		}
		return season;
	}
	
	/*
	 * Derivadas
	 */
	public void addSeason(Season s) {
		if(seasons == null) {
			seasons = new ArrayList<Season>();
		}
		seasons.add(s);
	}
	
	public void deleteSeason(Season s) {
		seasons.remove(s);
	}
	
	public void deleteSeason(String id) {
		Season s = getSeason(id);
		if(s!=null) {
			seasons.remove(s);
		}
	}
}
