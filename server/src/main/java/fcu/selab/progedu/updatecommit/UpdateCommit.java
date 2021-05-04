package fcu.selab.progedu.updatecommit;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import fcu.selab.progedu.jenkinsjob2status.JenkinsJob2StatusFactory;
import fcu.selab.progedu.jenkinsjob2status.JenkinsJobStatus;

import org.json.JSONObject;

import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentTypeDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.UserDbManager;
import fcu.selab.progedu.data.ProjectTypeEnum;
import fcu.selab.progedu.status.StatusEnum;

@Path("update/")
public class UpdateCommit {

  /**
   * update user assignment commit record to DB.
   *
   * @param username       username
   * @param assignmentName assignment name
   * @throws ParseException (to do)
   */
  @POST
  @Path("commits")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateCommitResult(@FormParam("user") String username,
                                     @FormParam("proName") String assignmentName)
          throws ParseException {
    // Todo 所有 assignment 相關的都要改掉 現在沒有 assignment

    JSONObject ob = new JSONObject();

    AssignmentDbManager assignmentDb = AssignmentDbManager.getInstance();
    AssignmentUserDbManager auDb = AssignmentUserDbManager.getInstance();
    UserDbManager userDb = UserDbManager.getInstance();
    CommitRecordDbManager db = CommitRecordDbManager.getInstance();
    AssignmentTypeDbManager atDb = AssignmentTypeDbManager.getInstance();

    int auId = auDb.getAuid(assignmentDb.getAssignmentIdByName(assignmentName),
            userDb.getUserIdByUsername(username));
    int commitNumber = db.getCommitCount(auId) + 1;
    Date date = new Date();
    DateFormat time = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    date = time.parse(time.format(Calendar.getInstance().getTime()));


    int assignmentId = assignmentDb.getAssignmentTypeId(assignmentName);
    ProjectTypeEnum projectTypeEnum = atDb.getTypeNameById(assignmentId);
    JenkinsJobStatus jobStatus = JenkinsJob2StatusFactory.createJenkinsJobStatus(projectTypeEnum);

    String jobName = username + "_" + assignmentName;
    StatusEnum statusEnum = jobStatus.getStatus(jobName, commitNumber);


    db.insertCommitRecord(auId, commitNumber, statusEnum, date);

    ob.put("auId", auId);
    ob.put("commitNumber", commitNumber);
    ob.put("time", time);
    ob.put("status", statusEnum.getType());

    return Response.ok().entity(ob.toString()).build();
  }

}
