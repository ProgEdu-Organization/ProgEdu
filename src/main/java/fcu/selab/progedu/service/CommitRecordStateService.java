package fcu.selab.progedu.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
<<<<<<< HEAD
=======
import fcu.selab.progedu.db.CommitRecordStateDbManager;
>>>>>>> #45CommitStatus

@Path("commits/")
public class CommitRecordStateService {
  CommitRecordStateDbManager commitRecordStateDb = CommitRecordStateDbManager.getInstance();
  CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();

  /**
   * get counts by different state
   * 
   * @param state
   * 
   * @return counts
   */
  @GET
  @Path("state/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommitRecordStateCounts(@QueryParam("state") String state) {
    List<Integer> array = commitRecordStateDb.getCommitRecordStateCounts(state);
    JSONObject ob = new JSONObject();
    ob.put("data", array);
    ob.put("name", state);
    return Response.ok().entity(ob.toString()).build();
  }

}
