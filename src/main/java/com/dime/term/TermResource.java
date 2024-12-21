package com.dime.term;

import io.micrometer.core.annotation.Counted;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("api/v1/terms")
@Produces(MediaType.APPLICATION_JSON)
public class TermResource {

  @Inject
  TermProducer producer;

  @POST
  @Counted(value = "addTerm")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addTerm(Term term) {
    if (term == null) {
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    Log.info("Received a new term: " + term.description);
    try {
      producer.sendToKafka(term);
      return Response.status(Response.Status.CREATED).entity(term).build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Error storing term: " + e.getMessage())
          .build();
    }
  }

  @GET
  @Counted(value = "getAllTerms")
  public Response getAll() {
    return Response.ok(producer.getAllTerms()).build();
  }
}