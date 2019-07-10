package fcu.selab.progedu.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.CommitRecordStateDbManager;
import fcu.selab.progedu.db.IDatabase;
import fcu.selab.progedu.db.MySqlDatabase;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.status.StatusEnum;

@Path("commits/")
public class CommitRecordStateService {
  CommitRecordStateDbManager commitRecordStateDb = CommitRecordStateDbManager.getInstance();
  CommitRecordDbManager commitRecordDb = CommitRecordDbManager.getInstance();
  AssignmentDbManager projectDb = AssignmentDbManager.getInstance();

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

  /**
   * <<<<<<< HEAD update state table ======= update data in Commit_Record_State
   * table >>>>>>> b77d964bf49ff866ed65195bcda4931ca40c49f0
   */
  public void updateCommitRecordState() {

    List<String> lsNames = projectDb.listAllAssignmentNames();

    for (String name : lsNames) {

      Map<String, Integer> map = commitRecordDb.getCommitRecordStateCounts(name);

      int bs = 0;
      int ini = 0;
      int utf = 0;
      int csf = 0;
      int cpf = 0;

      if (map.containsKey(StatusEnum.BUILD_SUCCESS.getTypeName())) {
        bs = map.get(StatusEnum.BUILD_SUCCESS.getTypeName());
      }

      if (map.containsKey(StatusEnum.INITIALIZATION.getTypeName())) {
        ini = map.get(StatusEnum.INITIALIZATION.getTypeName());
      }

      if (map.containsKey(StatusEnum.UNIT_TEST_FAILURE.getTypeName())) {
        utf = map.get(StatusEnum.UNIT_TEST_FAILURE.getTypeName());
      }

      if (map.containsKey(StatusEnum.CHECKSTYLE_FAILURE.getTypeName())) {
        csf = map.get(StatusEnum.CHECKSTYLE_FAILURE.getTypeName());
      }

      if (map.containsKey(StatusEnum.COMPILE_FAILURE.getTypeName())) {
        cpf = map.get(StatusEnum.COMPILE_FAILURE.getTypeName());
      }

      int ccs = 0;
      ccs = bs + utf + csf + cpf;

      commitRecordStateDb.addCommitRecordState(name, bs, csf, cpf, utf, ini, ccs);

    }

  }

  /**
   * delete build record state of hw
   * 
   * @param hw
   *          hw
   */
  public void deleteRecordState(String hw) {
    IDatabase database = new MySqlDatabase();
    Connection connection = database.getConnection();
    commitRecordStateDb.deleteRecordState(hw);
  }

}
