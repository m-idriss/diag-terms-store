package com.dime.term;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("api/v1/terms")
@Produces(MediaType.APPLICATION_JSON)
public class TermResource {

  @Inject
  private TermConsumer consumer;

  @GET
  public Response getAllTerms() {
    Log.infof("Resource : Retrieving all terms from MongoDB");
    return Response.ok(consumer.getAllTerms()).build();
  }

}