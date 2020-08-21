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
  @Path("allCategoryMetrics")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllCategoryMetrics() {
    JSONArray array = new JSONArray();
    JSONObject result = new JSONObject();
    List<ReviewCategory> reviewCategoryList = getAllCategory();
    for (ReviewCategory reviewCategory: reviewCategoryList) {
      JSONObject object = new JSONObject();
      JSONArray metricsArray = new JSONArray();
      List<ReviewMetrics> reviewMetricsList = getAllMetrics(reviewCategory.getId());
      for (ReviewMetrics reviewMetrics: reviewMetricsList) {
        JSONObject metricsObject = new JSONObject();
        metricsObject.put("id", reviewMetrics.getId());
        metricsObject.put("category", reviewCategory.getId());
        metricsObject.put("mode", reviewMetrics.getMode());
        metricsObject.put("metrics", reviewMetrics.getMetrics());
        metricsObject.put("description", reviewMetrics.getDescription());
        metricsArray.put(metricsObject);
      }
      object.put("id", reviewCategory.getId());
      object.put("name", reviewCategory.getName());
      object.put("metrics", reviewCategory.getMetrics());
      object.put("metricsList", metricsArray);
      array.put(object);
    }
    result.put("allCategoryMetrics", array);

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
