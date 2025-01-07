package com.dime.term;

import com.dime.exceptions.GenericError;
import com.dime.model.TermRecord;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/v1/terms")
@Tag(name = "Terms", description = "Manage Terms")
public class TermResource {

  @Inject
  private TermService termService;

  /*
   * curl -X GET http://localhost:8080/api/v1/terms/word
   */
  @GET
  @Path("{word: [a-zA-Z-]+}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get term by word")
  public TermRecord getTermByWord(@PathParam("word") String word) {
    String wordLower = word.toLowerCase();
    TermEntity entity = termService.findByWord(wordLower)
        .orElseThrow(() -> GenericError.WORD_NOT_FOUND.exWithArguments(Map.of("word", word)));
    return TermMapper.INSTANCE.toRecord(entity);
  }

  /*
   * curl -X GET http://localhost:8080/api/v1/terms/1
   */
  @GET
  @Path("/{id: \\d+}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Get term by id")
  public TermRecord getTermById(@PathParam("id") int id) {
    TermEntity entity = termService.findById((long) id)
        .orElseThrow(() -> GenericError.TERM_NOT_FOUND.exWithArguments(Map.of("id", id)));
    return TermMapper.INSTANCE.toRecord(entity);
  }

  /*
   * curl -X GET http://localhost:8080/api/v1/terms
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "List all terms")
  public List<TermRecord> listAllTerms() {
    List<TermEntity> terms = termService.listAll();
    return terms.stream().map(TermMapper.INSTANCE::toRecord).toList();
  }

  /*
   * curl -X POST http://localhost:8080/api/v1/terms -H 'Content-Type: application/json' -d '{"word":"example","synonyms":["synonym1","synonym2"]}'
   */
  @POST
  @Path("/")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  @Operation(summary = "Create term")
  public Response createTerm(TermRecord termRecord) {
    TermEntity entity = TermMapper.INSTANCE.toEntity(termRecord);
    TermEntity termSaved = termService.create(entity);
    TermRecord termRecordSaved = TermMapper.INSTANCE.toRecord(termSaved);
    return Response.status(Response.Status.CREATED).entity(termRecordSaved).build();
  }

  /*
   * curl -X DELETE http://localhost:8080/api/v1/terms/word
   */
  @DELETE
  @Path("/{word}")
  @Transactional
  @Operation(summary = "Delete term by word")
  public Response deleteTerm(@PathParam("word") String word) {
    String wordLower = word.toLowerCase();
    return termService.deleteByWord(wordLower)
        .map(deleted -> Response.noContent().build())
        .orElseThrow(() -> GenericError.WORD_NOT_FOUND.exWithArguments(Map.of("word", word)));
  }

  /*
   * curl -X DELETE http://localhost:8080/api/v1/terms/1
   */
  @DELETE
  @Path("/{id: \\d+}")
  @Transactional
  @Operation(summary = "Delete term by id")
  public Response deleteTermById(@PathParam("id") int id) {
    return termService.deleteById((long) id)
        .map(deleted -> Response.noContent().build())
        .orElseThrow(() -> GenericError.TERM_NOT_FOUND.exWithArguments(Map.of("id", id)));
  }
}
