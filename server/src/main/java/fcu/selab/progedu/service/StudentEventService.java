package fcu.selab.progedu.service;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


import java.util.Calendar;
import java.util.Date;

import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import fcu.selab.progedu.db.service.MongoDbService;
import org.json.JSONObject;
import org.json.JSONArray;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.InsertOneResult;

import javax.ws.rs.Produces;
import org.bson.Document;
import org.glassfish.jersey.media.multipart.FormDataParam;
import javax.servlet.http.HttpServletRequest;


@Path("student_events/")
public class StudentEventService {
  MongoDbService mongoDb = MongoDbService.getInstance();
  Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
  static String ipAddress;

  /**
   * @param collection Collection
   * @return student events
   */
  @GET
  @Path("getStudentEvents")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCollection(@QueryParam("collection") String collection) {
    JSONObject ob = new JSONObject();
    JSONArray array = new JSONArray();
    MongoCollection<Document> documents = mongoDb.getCollection(collection);
    FindIterable<Document> findIterable = documents.find();  
    MongoCursor<Document> mongoCursor = findIterable.iterator();  
    while (mongoCursor.hasNext()) {  
      array.put(mongoCursor.next());
    }  
    ob.put("studentEvents", array);
    
    return Response.ok().entity(ob.toString()).build();
  }

    /**
   * Student Event Log
   * @return Response
   */
  @POST
  @Path("logStudentEvent")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response logStudentEvent(@Context HttpServletRequest request,
                                  @FormDataParam("username") String username,
                                  @FormDataParam("ip") String ip,
                                  @FormDataParam("page") String page,
                                  @FormDataParam("name") String eventName,
                                  @FormDataParam("event") String event) {
    getClientIpAddress(request);
    Date nowDate = new Date();
    cal.setTime(nowDate);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int minute = cal.get(Calendar.MINUTE);
    int second = cal.get(Calendar.SECOND);
    Document eventJson = Document.parse(event);
    String nowTime = (year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
    Document doc = new Document("username", username)
        .append("name", eventName)
        .append("ip", ipAddress)
        .append("page", page)
        .append("time", nowTime)
        .append("event", eventJson);

    String collectionName = eventName.substring(eventName.indexOf('.') + 1, eventName.length());
    InsertOneResult result = mongoDb.getCollection(collectionName).insertOne(doc);
    JSONObject ob = new JSONObject();
    ob.put("result", result.toString());
    return Response.ok().entity(ob.toString()).build();
  }

  /**
   * Get student ip address
   * @return null
   */
  private static void getClientIpAddress(HttpServletRequest request) {
    ipAddress = request.getHeader("X-FORWARDED-FOR");
    if (ipAddress == null) {
      ipAddress = request.getRemoteAddr();
    }
  }

}
