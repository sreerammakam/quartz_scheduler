package com.lca.eps.common.controller;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;

@Path("reference")
public class ReferenceController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		logger.info("Ping successfully...");
		return Response.status(Response.Status.OK).entity("Success : " + new Date()).build();
	}

	@GET
	@Path("jsp")
	@Produces(MediaType.TEXT_HTML)
	public Viewable renderJSP() {
		return new Viewable("/EPSJobs.jsp");
	}
}
