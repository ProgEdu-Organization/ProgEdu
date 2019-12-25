package fcu.selab.progedu.service;

import fcu.selab.progedu.data.CommitRecord;
import fcu.selab.progedu.db.AssignmentDbManager;
import fcu.selab.progedu.db.AssignmentUserDbManager;
import fcu.selab.progedu.db.CommitRecordDbManager;
import fcu.selab.progedu.db.CommitStatusDbManager;
import fcu.selab.progedu.status.StatusEnum;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("chart/")
public class ChartService {
    private AssignmentDbManager assignmentDbManager = AssignmentDbManager.getInstance();
    private AssignmentService assignmentService = AssignmentService.getInstance();
    private AssignmentUserDbManager assignmentUserDbManager = AssignmentUserDbManager.getInstance();
    private CommitStatusDbManager commitStatusDbManager = CommitStatusDbManager.getInstance();
    private CommitRecordDbManager commitRecordDbManager = CommitRecordDbManager.getInstance();

    @GET
    @Path("allCommitRecord")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCommitRecords(){
        // Step1  find all assignment's name.
        List<String> assignmentNames =  assignmentService.getAllAssignmentNames();
        // Step2  find all commitRecord id by using assignment's id.
        JSONObject ob = new JSONObject();
        JSONArray assignment = new JSONArray();
        assignmentNames.forEach(name -> {
            JSONObject commits = new JSONObject();
            List<Integer> assignmentIds = new ArrayList<>();
            assignmentIds.add(assignmentDbManager.getAssignmentIdByName(name));
            assignmentIds.forEach(id ->{
                List<Integer> auIds = assignmentUserDbManager.getAuids(id);
                JSONArray array = new JSONArray();
                auIds.forEach(auId->{
                    List<CommitRecord> commitRecords = commitRecordDbManager.getCommitRecord(auId);
                    for (CommitRecord commitRecord : commitRecords) {
                        try {
                            int number = commitRecord.getNumber();
                            Date time = commitRecord.getTime();
                            String status = commitRecord.getStatus().getType();
                            JSONObject temp = new JSONObject();
                            temp.put("number", number);
                            temp.put("status", status.toUpperCase());
                            temp.put("time", time);
                            array.put(temp);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(array.toString());
                });
                commits.put("name", name);
                commits.put("commits", array);
            });
            assignment.put(commits);
        });
        ob.put("allCommitRecord", assignment);
        return Response.ok().entity(ob.toString()).build();
    }
}
