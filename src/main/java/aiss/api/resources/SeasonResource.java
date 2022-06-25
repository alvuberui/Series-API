package aiss.api.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import aiss.model.Chapter;
import aiss.model.Season;
import aiss.model.Serie;
import aiss.model.repository.MapSerieRepository;
import aiss.model.repository.SerieRepository;

@Path("/seasons")
public class SeasonResource {
	/*
	 * Singleton
	 */
	private static SeasonResource _instance=null;
	SerieRepository repository;
	
	private SeasonResource() {
		repository = MapSerieRepository.getInstance();
	}
	
	public static SeasonResource getInstance() {
		if(_instance == null) {
			_instance = new SeasonResource();
		}
		return _instance;
	}
	
	@GET
	@Produces("application/json") 
	public Collection<Season> getAllSeasonsFiltered(@QueryParam("isEmpty") Boolean isEmpty, @QueryParam("order") String order, @QueryParam("name") String name,@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {
		List<Season> result = new ArrayList<Season>();
		
		for(Season season : repository.getAllSeasons()) {
			if(name == null || season.getName().equals(name)) {
				if(isEmpty == null 
						|| (isEmpty && (season.getChapters() == null || season.getChapters().size() == 0))
						|| (!isEmpty && (season.getChapters() != null || season.getChapters().size() > 0))) {
							result.add(season);
						}
			}
		}
		if(order != null) {
			if(order != null) {
				if(order.equals("name")) {
					Collections.sort(result, new ComparatorNameSeason());
				} else if (order.equals("-name")) {
					Collections.sort(result, new ComparatorNameSeasonReversed());
				}else {
					throw new BadRequestException("The order paramenter must be name or -name" );
				}
			}
		}
		if(offset != null) {
			if(offset < result.size()-1) {
				result = result.subList(offset, result.size()-1);
			}else {
				throw new BadRequestException(String.format("The offset must be < %d", result.size()-1));
			}
		}
		if(limit != null) {
			if(limit <= result.size()) {
				result = result.subList(0, limit-1);				
			}else {
				throw new BadRequestException(String.format("The limit must be < %d", result.size()));
			}
		}
		return result;
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Season getSeasonId(@PathParam("id") String id)
	{
		Season list = repository.getSeason(id);
		
		if (list == null) {
			throw new NotFoundException("The season with id="+ id +" was not found");			
		}
		
		return list;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addSeason(@Context UriInfo uriInfo, Season season) {
		if (season.getName() == null || "".equals(season.getName()))
			throw new BadRequestException("The name of the season must not be null");
		
		if (season.getChapters()!=null)
			throw new BadRequestException("The chapters property is not editable.");

		repository.addSeason(season);

		// Builds the response. Returns the season the has just been added.
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(season.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(season);			
		return resp.build();
	}
	
	@PUT
	@Consumes("application/json")
	public Response updateSeason(Season season) {
		Season oldseason = repository.getSeason(season.getId());
		if (oldseason == null) {
			throw new NotFoundException("The season with id="+ season.getId() +" was not found");			
		}
		
		if (season.getChapters()!=null)
			throw new BadRequestException("The chapters property is not editable.");
		
		// Update name
		if (season.getName()!=null)
			oldseason.setName(season.getName());
		
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response removeSeason(@PathParam("id") String id) {
		Season toberemoved=repository.getSeason(id);
		if (toberemoved == null)
			throw new NotFoundException("The season with id="+ id +" was not found");
		else
			repository.deleteSeason(id);
		
		return Response.noContent().build();
	}
	
	
	@POST	
	@Path("/{seasonId}/{chapterId}")
	@Consumes("text/plain")
	@Produces("application/json")
	public Response addChapterToSeason(@Context UriInfo uriInfo,@PathParam("seasonId") String seasonId, @PathParam("chapterId") String chapterId){				
		Season season = repository.getSeason(seasonId);
		Chapter chapter = repository.getChapter(chapterId);
		
		if (season == null)
			throw new NotFoundException("The season with id=" + seasonId + " was not found");
		 
		if (chapter == null)
			throw new NotFoundException("The chapter with id=" + chapterId + " was not found");
		
		if (season.getChapter(chapterId)!=null)
			throw new BadRequestException("The chapter is already included in the serie.");
			
		repository.addSeason(seasonId, chapterId);		

		// Builds the response
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(seasonId);
		ResponseBuilder resp = Response.created(uri);
		resp.entity(season);			
		return resp.build();
	}
	
	
	@DELETE
	@Path("/{seasonId}/{chapterId}")
	public Response removeChapterFromSeason(@PathParam("seasonId") String seasonId, @PathParam("chapterId") String chapterId) {
		Season season = repository.getSeason(seasonId);
		Chapter chapter = repository.getChapter(chapterId);
		
		if (season == null)
			throw new NotFoundException("The season with id=" + seasonId + " was not found");
		
		if (chapter == null)
			throw new NotFoundException("The chapter with id=" + chapterId + " was not found");
		
		
		repository.removeSeason(seasonId, chapterId);		
		
		return Response.noContent().build();
	}
}
