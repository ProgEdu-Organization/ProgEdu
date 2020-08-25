package fcu.selab.progedu.service;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import fcu.selab.progedu.db.ReviewCategoryDbManager;
import fcu.selab.progedu.db.ReviewMetricsDbManager;
import fcu.selab.progedu.data.ReviewCategory;
import fcu.selab.progedu.data.ReviewMetrics;

@Path("categoryMetrics/")
public class CategoryMetricsService {
  private ReviewCategoryDbManager reviewCategoryDbManager = ReviewCategoryDbManager.getInstance();
  private ReviewMetricsDbManager reviewMetricsDbManager = ReviewMetricsDbManager.getInstance();

  /**
   *
   */
  @GET
  @Path("category")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCategory() {
    JSONArray array = new JSONArray();
    JSONObject result = new JSONObject();
    List<ReviewCategory> reviewCategoryList = getAllCategory();
    for (ReviewCategory reviewCategory: reviewCategoryList) {
      JSONObject object = new JSONObject();
      object.put("id", reviewCategory.getId());
      object.put("name", reviewCategory.getName());
      object.put("metrics", reviewCategory.getMetrics());
      array.put(object);
    }
    result.put("allCategory", array);

    return Response.ok().entity(result.toString()).build();
  }

  /**
   *
   */
  @GET
  @Path("metrics")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMetrics(@QueryParam("category") int category) {
    JSONArray array = new JSONArray();
    JSONObject result = new JSONObject();
    List<ReviewMetrics> reviewMetricsList = getAllMetrics(category);
    for (ReviewMetrics reviewMetrics: reviewMetricsList) {
      JSONObject object = new JSONObject();
      object.put("id", reviewMetrics.getId());
      object.put("category", reviewMetrics.getCategory());
      object.put("mode", reviewMetrics.getMode());
      object.put("metrics", reviewMetrics.getMetrics());
      object.put("description", reviewMetrics.getDescription());
      object.put("link", reviewMetrics.getLink());
      array.put(object);
    }
    result.put("allMetrics", array);

    return Response.ok().entity(result.toString()).build();
  }

  public List<ReviewCategory> getAllCategory() {
    List<ReviewCategory> reviewCategoryList = reviewCategoryDbManager.getReviewCategory();
    return reviewCategoryList;
  }

  public List<ReviewMetrics> getAllMetrics(int category) {
    List<ReviewMetrics> reviewMetricsList = reviewMetricsDbManager.getReviewMetrics(category);
    return reviewMetricsList;
  }

}
