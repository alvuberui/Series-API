package aiss.model.repository;

import java.util.Collection;

import aiss.model.Chapter;
import aiss.model.Season;
import aiss.model.Serie;

public interface SerieRepository {
	
	
	/*
	 * Chapters
	 */
	public void addChapter(Chapter s);
	public Collection<Chapter> getAllChapters();
	public Chapter getChapter(String chapterId);
	public void updateChapter(Chapter s);
	public void deleteChapter(String chapterId);
	
	/*
	 * Seasons
	 */
	public void addSeason(Season p);
	public Collection<Season> getAllSeasons();
	public Season getSeason(String seasonId);
	public void updateSeason(Season p);
	public void deleteSeason(String seasonId);
	public Collection<Chapter> getAllChapters(String seasonId);
	public void addChapter(String seasonId, String  chapterId);
	public void removeChapter(String seasonId, String chapterId); 
	
	/*
	 * Series
	 */
	public void addSerie(Serie p);
	public Collection<Serie> getAllSeries();
	public Serie getSerie(String serieId);
	public void updateSerie(Serie p);
	public void deleteSerie(String serieId);
	public Collection<Season> getAllSeasons(String serieId);
	public void addSeason(String serieId, String seasonId);
	public void removeSeason(String serieId, String seasonId); 
}
