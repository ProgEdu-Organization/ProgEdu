package fcu.selab.progedu.service;

import java.sql.Connection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.IDatabase;
import fcu.selab.progedu.db.MySqlDatabase;
import fcu.selab.progedu.db.AssignmentDbManager;

@Path("commits/record/")
public class CommitRecordService {
  CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  AssignmentDbManager pdb = AssignmentDbManager.getInstance();

  /**
   * get counts by different color
   * 
   * @param color color
   * @return counts
   */
  @GET
  @Path("color")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCounts(@QueryParam("color") String color) {
    List<Integer> array = commitRecordDb.getCounts(color);
    JSONObject ob = new JSONObject();
    ob.put("data", array);
    ob.put("name", color);
    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * get Count Group By Hw And Time
   * 
   * @param hw hw number
   * @return records
   */
  @GET
  @Path("records")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCountGroupByHwAndTime(@QueryParam("hw") String hw) {
    JSONObject ob = new JSONObject();
    JSONArray records = commitRecordDb.getCountGroupByHwAndTime(hw);
    String deadline = pdb.getAssignmentByName(hw).getDeadline();
    ob.put("records", records);
    ob.put("title", hw);
    ob.put("deadline", deadline);
    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * delete build record of hw
   * 
   * @param hw hw
   */
  public void deleteRecord(String hw) {
    IDatabase database = new MySqlDatabase();
    Connection connection = database.getConnection();
    commitRecordDb.deleteRecord(hw);
  }

}
