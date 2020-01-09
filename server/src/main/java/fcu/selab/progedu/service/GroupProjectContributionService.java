package fcu.selab.progedu.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("groups/{groupName}/projects/{projectName}/contribution")
public class GroupProjectContributionService {
  @GET
  @Path("/loc")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response lineOfCode() {
    return null;
  }
}
