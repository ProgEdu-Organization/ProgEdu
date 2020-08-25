package fcu.selab.progedu.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
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
   *  get all category
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
   *  get metrics by specific category
   *
   * @param category category
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

  /**
   *  insert new category
   *
   * @param name name
   * @param metrics metrics
   */
  @POST
  @Path("category/create")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createCategory(@QueryParam("name") String name,
                                 @QueryParam("metrics") String metrics) {
    reviewCategoryDbManager.insertReviewCategory(name, metrics);
    return Response.ok().build();
  }

  /**
   *  edit category by specific id
   *
   * @param id id
   * @param metrics metrics
   */
  @PUT
  @Path("category/edit")
  @Produces(MediaType.APPLICATION_JSON)
  public Response editCategory(@QueryParam("id") int id,
                               @QueryParam("metrics") String metrics) {
    reviewCategoryDbManager.editReviewCategoryById(id, metrics);
    return Response.ok().build();
  }

  /**
   * delete category by specific id
   *
   * @param id id
   */
  @DELETE
  @Path("category/delete")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteCategory(@QueryParam("id") int id) {
    reviewCategoryDbManager.deleteReviewCategoryById(id);
    return Response.ok().build();
  }

  /**
   *  insert new metrics into specific category
   *
   * @param category category
   * @param mode mode
   * @param metrics metrics
   * @param description description
   * @param link link
   */
  @POST
  @Path("metrics/create")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createMetrics(@QueryParam("category") int category,
                                 @QueryParam("mode") int mode,
                                @QueryParam("metrics") String metrics,
                                @QueryParam("description") String description,
                                @QueryParam("link") String link) {
    reviewMetricsDbManager.insertReviewMetrics(category, mode, metrics, description, link);
    return Response.ok().build();
  }

  /**
   *  edit metrics by specific id
   *
   * @param id id
   * @param mode mode
   * @param description description
   * @param link link
   */
  @PUT
  @Path("metrics/edit")
  @Produces(MediaType.APPLICATION_JSON)
  public Response editMetrics(@QueryParam("id") int id,
                              @QueryParam("mode") int mode,
                              @QueryParam("description") String description,
                              @QueryParam("link") String link) {
    reviewMetricsDbManager.editReviewMetricsById(id, mode, description, link);
    return Response.ok().build();
  }

  /**
   *  delete metrics by specific id
   *
   * @param id id
   */
  @DELETE
  @Path("metrics/delete")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteMetrics(@QueryParam("id") int id) {
    reviewMetricsDbManager.deleteReviewMetrics(id);
    return Response.ok().build();
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
