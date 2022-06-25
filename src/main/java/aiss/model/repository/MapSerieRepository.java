package aiss.model.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import aiss.model.Chapter;
import aiss.model.Season;
import aiss.model.Serie;

public class MapSerieRepository implements SerieRepository {
	
	Map<String, Serie> serieMap;
	Map<String, Season> seasonMap;
	Map<String, Chapter> chapterMap;
	private static MapSerieRepository instance=null;
	private int index=0;			// Index to create playlists and songs' identifiers.
	
	public static MapSerieRepository getInstance() {
		
		if (instance==null) {
			instance = new MapSerieRepository();
			instance.init();
		}
		return instance;
	}
	
	
	public void init() {
		serieMap = new HashMap<String, Serie>();
		seasonMap = new HashMap<String, Season>();
		chapterMap = new HashMap<String, Chapter>();
		
		/*
		 * Create chapters
		 */
		Chapter maja = new Chapter();
		maja.setDuration("45");
		maja.setTitle("1. Maja");
		addChapter(maja);
		
		Chapter bajoCustodia = new Chapter();
		bajoCustodia.setDuration("47");
		bajoCustodia.setTitle("2. Bajo custodia");
		addChapter(bajoCustodia);
		
		Chapter elFuneral = new Chapter();
		elFuneral.setDuration("45");
		elFuneral.setTitle("3. El funeral");
		addChapter(elFuneral);
		
		Chapter laReconstruccion = new Chapter();
		laReconstruccion.setDuration("41");
		laReconstruccion.setTitle("4. La reconstrucción");
		addChapter(laReconstruccion);
		
		Chapter elJuicio = new Chapter();
		elJuicio.setDuration("49");
		elJuicio.setTitle("5. El juicio");
		addChapter(elJuicio);
		
		Chapter losTestigos = new Chapter();
		losTestigos.setDuration("45");
		losTestigos.setTitle("6. Los testigos");
		addChapter(losTestigos);
		
		// -----------------------------
		
		Chapter episodioUnoCollateral = new Chapter();
		episodioUnoCollateral.setDuration("56");
		episodioUnoCollateral.setTitle("Episodio 1");
		addChapter(episodioUnoCollateral);
		
		Chapter episodioDosCollateral = new Chapter();
		episodioDosCollateral.setDuration("59");
		episodioDosCollateral.setTitle("Episodio 2");
		addChapter(episodioDosCollateral);
		
		Chapter episodioTresCollateral = new Chapter();
		episodioTresCollateral.setDuration("58");
		episodioTresCollateral.setTitle("Episodio 3");
		addChapter(episodioTresCollateral);
		
		Chapter episodioCuatroCollateral = new Chapter();
		episodioCuatroCollateral.setDuration("59");
		episodioCuatroCollateral.setTitle("Episodio 4");
		addChapter(episodioCuatroCollateral);
		
		
		/*
		 * Create seasons
		 */
		Season temporadaUnoArenasMovedizas = new Season();
		temporadaUnoArenasMovedizas.setName("Temporada 1");
		temporadaUnoArenasMovedizas.setYear("2019");
		temporadaUnoArenasMovedizas.setDescription("Después de que una tragedia en un centro escolar conmocione a un barrio rico de Estocolmo, una adolescente aparentemente equilibrada acaba acusada de asesinato.");
		addSeason(temporadaUnoArenasMovedizas);
		
		Season temporadaUnoCollateral = new Season();
		temporadaUnoCollateral.setName("Temporada 1");
		temporadaUnoCollateral.setYear("2018");
		temporadaUnoCollateral.setDescription("Mientras investiga el asesinato de un repartidor de pizzas, una detective destapa una conspiración en la que están implicados narcotraficantes, contrabandistas y espías.");
		addSeason(temporadaUnoCollateral);
		
		/*
		 * Add chapters to season
		 */
		addChapter(temporadaUnoArenasMovedizas.getId(), maja.getId());
		addChapter(temporadaUnoArenasMovedizas.getId(), bajoCustodia.getId());
		addChapter(temporadaUnoArenasMovedizas.getId(), elFuneral.getId());
		addChapter(temporadaUnoArenasMovedizas.getId(), laReconstruccion.getId());
		addChapter(temporadaUnoArenasMovedizas.getId(), elJuicio.getId());
		addChapter(temporadaUnoArenasMovedizas.getId(), losTestigos.getId());
		
		addChapter(temporadaUnoCollateral.getId(), episodioUnoCollateral.getId());
		addChapter(temporadaUnoCollateral.getId(), episodioDosCollateral.getId());
		addChapter(temporadaUnoCollateral.getId(), episodioTresCollateral.getId());
		addChapter(temporadaUnoCollateral.getId(), episodioCuatroCollateral.getId());
		/*
		 * Create series
		 */
		Serie arenasMovedizas = new Serie();
		arenasMovedizas.setName("Arenas movedizas");
		addSerie(arenasMovedizas);
		
		Serie collateral = new Serie();
		collateral.setName("Collateral");
		addSerie(collateral);
		
		/*
		 * Add seasons to serie
		 */
		addSeason(arenasMovedizas.getId(), temporadaUnoArenasMovedizas.getId());
		
		addSeason(collateral.getId(), temporadaUnoCollateral.getId());
	}
	
	/*
	 * Chapters related operations
	 */
	@Override
	public void addChapter(Chapter s) {
		String id = "s" + index++;
		s.setId(id);
		chapterMap.put(id, s);
	}

	@Override
	public Collection<Chapter> getAllChapters() {
		return chapterMap.values();
	}

	@Override
	public Chapter getChapter(String chapterId) {
		return chapterMap.get(chapterId);
	}

	@Override
	public void updateChapter(Chapter s) {
		Chapter chapter = chapterMap.get(s.getId());
		chapter.setDuration(s.getDuration());
		chapter.setTitle(s.getTitle());
	}

	@Override
	public void deleteChapter(String chapterId) {
		chapterMap.remove(chapterId);
		
	}
	
	/*
	 * Seasons related operations
	 */
	@Override
	public void addSeason(Season p) {
		String id = "p" + index++;
		p.setId(id);
		seasonMap.put(id, p);
	}

	@Override
	public Collection<Season> getAllSeasons() {
		return seasonMap.values();
	}

	@Override
	public Season getSeason(String seasonId) {
		return seasonMap.get(seasonId);
	}

	@Override
	public void updateSeason(Season p) {
		seasonMap.put(p.getId(), p);
	}

	@Override
	public void deleteSeason(String seasonId) {
		seasonMap.remove(seasonId);
		
	}

	@Override
	public Collection<Chapter> getAllChapters(String seasonId) {
		return getSeason(seasonId).getChapters();
	}

	@Override
	public void addChapter(String seasonId, String chapterId) {
		Season season = getSeason(seasonId);
		season.addChapter(chapterMap.get(chapterId));
	}

	@Override
	public void removeChapter(String seasonId, String chapterId) {
		getSeason(seasonId).deleteChapter(chapterId);
	}
	
	/*
	 * Series related operations
	 */
	@Override
	public void addSerie(Serie p) {
		String id = "p" + index++;
		p.setId(id);
		serieMap.put(id, p);
	}

	@Override
	public Collection<Serie> getAllSeries() {
		return serieMap.values();
	}

	@Override
	public Serie getSerie(String serieId) {
		return serieMap.get(serieId);
	}

	@Override
	public void updateSerie(Serie p) {
		serieMap.put(p.getId(), p);
	}

	@Override
	public void deleteSerie(String serieId) {
		serieMap.remove(serieId);
	}

	@Override
	public Collection<Season> getAllSeasons(String serieId) {
		return getSerie(serieId).getSeasons();
	}

	@Override
	public void addSeason(String serieId, String seasonId) {
		Serie serie = getSerie(serieId);
		serie.addSeason(seasonMap.get(seasonId));	
	}

	@Override
	public void removeSeason(String serieId, String seasonId) {
		getSerie(serieId).deleteSeason(seasonId);
	}

}
