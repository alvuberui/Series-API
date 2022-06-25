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

@Path("/chapters")
public class ChapterResource {
	
	/*
	 * Singleton
	 */
	private static ChapterResource _instance=null;
	SerieRepository repository;
	
	private ChapterResource() {
		repository = MapSerieRepository.getInstance();
	}
	
	public static ChapterResource getInstance() {
		if(_instance == null) {
			_instance = new ChapterResource();
		}
		return _instance;
	}
	
	@GET
	@Produces("application/json")
	public Collection<Chapter> getChapters(@QueryParam("isEmpty") Boolean isEmpty, @QueryParam("order") String order, @QueryParam("name") String name,@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {
		List<Chapter> result = new ArrayList<Chapter>();
		for(Chapter chapter: repository.getAllChapters()) {
			if(name == null ||chapter.getTitle().equals(name)) {
				if(isEmpty == null
						|| (isEmpty && (chapter.getTitle() == null))
						|| (!isEmpty && (chapter.getTitle() != null))) {
							result.add(chapter);
						}
			}
		}
		if(order != null) {
			if(order.equals("name")) {
				Collections.sort(result, new ComparatorNameChapter());
			} else if (order.equals("-name")) {
				Collections.sort(result, new ComparatorNameChapterReversed());
			}else {
				throw new BadRequestException("The order paramenter must be name or -name" );
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
	public Chapter get(@PathParam("id") String id)
	{
		Chapter list = repository.getChapter(id);
		
		if (list == null) {
			throw new NotFoundException("The chapter with id="+ id +" was not found");			
		}
		return list;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addChapter(@Context UriInfo uriInfo, Chapter chapter) {
		if (chapter.getTitle() == null || "".equals(chapter.getTitle()))
			throw new BadRequestException("The title of the chapter must not be null");
		
		if (chapter.getDuration() == null || "".equals(chapter.getDuration()))
			throw new BadRequestException("The duration of the chapter must not be null");

		repository.addChapter(chapter);

		// Builds the response. Returns the playlist the has just been added.
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(chapter.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(chapter);			
		return resp.build();
	}
	
	@PUT
	@Consumes("application/json")
	public Response updateChapter(Chapter chapter) {
		Chapter oldchapter = repository.getChapter(chapter.getId());
		if (oldchapter == null) {
			throw new NotFoundException("The chapter with id="+ chapter.getId() +" was not found");			
		}
		
		// Update name
		if (chapter.getTitle()!=null)
			oldchapter.setTitle(chapter.getTitle());
		
		if(chapter.getDuration() != null) {
			oldchapter.setDuration(chapter.getDuration());
		}
		
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response removeChapter(@PathParam("id") String id) {
		Chapter toberemoved = repository.getChapter(id);
		if (toberemoved == null)
			throw new NotFoundException("The chapter with id="+ id +" was not found");
		else
			repository.deleteChapter(id);
		
		return Response.noContent().build();
	}
}
