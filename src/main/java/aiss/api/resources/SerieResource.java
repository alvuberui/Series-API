package aiss.api.resources;

import javax.ws.rs.Path;


import aiss.model.Season;
import aiss.model.Serie;
import aiss.model.repository.MapSerieRepository;
import aiss.model.repository.SerieRepository;

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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;


@Path("/series")
public class SerieResource {
	
	/*
	 * Singleton
	 */
	private static SerieResource _instance=null;
	SerieRepository repository;
	
	private SerieResource() {
		repository = MapSerieRepository.getInstance();
	}
	
	public static SerieResource getInstance() {
		if(_instance == null) {
			_instance = new SerieResource();
		}
		return _instance;
	}
	
	@GET
	@Produces("application/json") 
	public Collection<Serie> getAllSeriesFiltered(@QueryParam("isEmpty") Boolean isEmpty, @QueryParam("order") String order, @QueryParam("name") String name,@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {
		List<Serie> result = new ArrayList<Serie>();
		
		for(Serie serie : repository.getAllSeries()) {
			if(name == null || serie.getName().equals(name)) {
				if(isEmpty == null 
						|| (isEmpty && (serie.getSeasons() == null || serie.getSeasons().size() == 0))
						|| (!isEmpty && (serie.getSeasons() != null || serie.getSeasons().size() > 0))) {
							result.add(serie);
						}
			}
		}
		if(order != null) {
			if(order != null) {
				if(order.equals("name")) {
					Collections.sort(result, new ComparatorNameSerie());
				} else if (order.equals("-name")) {
					Collections.sort(result, new ComparatorNameSerieReversed());
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
	@Path("/{offset}/{limit}")
	@Produces("application/json") 
	public Collection<Serie> getFilter(@QueryParam("isEmpty") Boolean isEmpty, @QueryParam("order") String order, @QueryParam("name") String name,@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {
List<Serie> result = new ArrayList<Serie>();
		
		for(Serie serie : repository.getAllSeries()) {
			if(isEmpty == null 
					|| (isEmpty && (serie.getSeasons() == null || serie.getSeasons().size() == 0))
					|| (isEmpty && (serie.getSeasons() != null || serie.getSeasons().size() > 0))) {
						result.add(serie);
					}
		}
		if(order != null) {
			if(order != null) {
				if(order.equals("name")) {
					Collections.sort(result, new ComparatorNameSerie());
				} else if (order.equals("-name")) {
					Collections.sort(result, new ComparatorNameSerieReversed());
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
	public Serie getSerieId(@PathParam("id") String id)
	{
		Serie list = repository.getSerie(id);
		
		if (list == null) {
			throw new NotFoundException("The serie id="+ id +" was not found");			
		}
		
		return list;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addSerie(@Context UriInfo uriInfo, Serie serie) {
		if (serie.getName() == null || "".equals(serie.getName()))
			throw new BadRequestException("The name of the serie must not be null");
		
		if (serie.getSeasons()!=null)
			throw new BadRequestException("The seasons property is not editable.");

		repository.addSerie(serie);

		// Builds the response. Returns the serie the has just been added.
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(serie.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(serie);			
		return resp.build();
	}
	
	@PUT
	@Consumes("application/json")
	public Response updateSerie(Serie serie) {
		Serie oldserie = repository.getSerie(serie.getId());
		if (oldserie == null) {
			throw new NotFoundException("The serie with id="+ serie.getId() +" was not found");			
		}
		
		if (serie.getSeasons()!=null)
			throw new BadRequestException("The seasons property is not editable.");
		
		// Update name
		if (serie.getName()!=null)
			oldserie.setName(serie.getName());
		
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response removeSerie(@PathParam("id") String id) {
		Serie toberemoved=repository.getSerie(id);
		if (toberemoved == null)
			throw new NotFoundException("The serie with id="+ id +" was not found");
		else
			repository.deleteSerie(id);
		
		return Response.noContent().build();
	}
	
	
	@POST	
	@Path("/{serieId}/{seasonId}")
	@Consumes("text/plain")
	@Produces("application/json")
	public Response addSeasonToSerie(@Context UriInfo uriInfo,@PathParam("serieId") String serieId, @PathParam("seasonId") String seasonId){				
		Serie serie = repository.getSerie(serieId);
		Season season = repository.getSeason(seasonId);
		
		if (serie == null)
			throw new NotFoundException("The serie with id=" + serieId + " was not found");
		
		if (season == null)
			throw new NotFoundException("The season with id=" + seasonId + " was not found");
		
		if (serie.getSeason(seasonId)!=null)
			throw new BadRequestException("The season is already included in the serie.");
			
		repository.addSeason(serieId, seasonId);		

		// Builds the response
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(serieId);
		ResponseBuilder resp = Response.created(uri);
		resp.entity(serie);			
		return resp.build();
	}
	
	
	@DELETE
	@Path("/{serieId}/{seasonId}")
	public Response removeSeasonFromSerie(@PathParam("serieId") String serieId, @PathParam("seasonId") String seasonId) {
		Serie serie = repository.getSerie(serieId);
		Season season = repository.getSeason(seasonId);
		
		if (serie == null)
			throw new NotFoundException("The serie with id=" + serieId + " was not found");
		
		if (season == null)
			throw new NotFoundException("The season with id=" + seasonId + " was not found");
		
		
		repository.removeSeason(serieId, seasonId);		
		
		return Response.noContent().build();
	}
}
