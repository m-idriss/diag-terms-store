package com.dime.term;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/terms")
@Produces(MediaType.APPLICATION_JSON)
public class TermResource {

    @Inject
    TermProducer producer;

    @POST
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
    public Response getAll() {
        return Response.ok(producer.getAllTerms()).build();
    }
}