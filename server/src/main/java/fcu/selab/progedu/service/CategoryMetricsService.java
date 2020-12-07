package fcu.selab.progedu.service;

import java.sql.SQLException;
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

import fcu.selab.progedu.utils.ExceptionUtil;
import fcu.selab.progedu.db.ReviewCategoryDbManager;
import fcu.selab.progedu.db.ReviewMetricsDbManager;
import fcu.selab.progedu.data.ReviewCategory;
import fcu.selab.progedu.data.ReviewMetrics;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("categoryMetrics/")
public class CategoryMetricsService {
  private ReviewCategoryDbManager reviewCategoryDbManager = ReviewCategoryDbManager.getInstance();
  private ReviewMetricsDbManager reviewMetricsDbManager = ReviewMetricsDbManager.getInstance();

  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryMetricsService.class);

  /**
   *  get all category
   */
  @GET
  @Path("category")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCategory() {
    Response response = null;

    try {
      JSONArray array = new JSONArray();
      JSONObject result = new JSONObject();
      List<ReviewCategory> reviewCategoryList = getAllCategory();
      for (ReviewCategory reviewCategory : reviewCategoryList) {
        JSONObject object = new JSONObject();
        object.put("id", reviewCategory.getId());
        object.put("name", reviewCategory.getName());
        object.put("metrics", reviewCategory.getMetrics());
        array.put(object);
      }
      result.put("allCategory", array);

      response = Response.ok().entity(result.toString()).build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }

    return response;
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
    Response response = null;

    try {
      JSONArray array = new JSONArray();
      JSONObject result = new JSONObject();
      List<ReviewMetrics> reviewMetricsList = getAllMetrics(category);
      for (ReviewMetrics reviewMetrics : reviewMetricsList) {
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

      response = Response.ok().entity(result.toString()).build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }

    return response;
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
    Response response = null;

    try {
      reviewCategoryDbManager.insertReviewCategory(name, metrics);
      response = Response.ok().build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }
    return response;
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
    Response response = null;

    try {
      reviewCategoryDbManager.editReviewCategoryById(id, metrics);
      response = Response.ok().build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }
    return response;
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
    Response response = null;

    try {
      reviewCategoryDbManager.deleteReviewCategoryById(id);
      response = Response.ok().build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }
    return response;
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
    Response response = null;

    try {
      reviewMetricsDbManager.insertReviewMetrics(category, mode, metrics, description, link);
      response = Response.ok().build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }
    return response;
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
    Response response = null;

    try {
      reviewMetricsDbManager.editReviewMetricsById(id, mode, description, link);
      response = Response.ok().build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }
    return response;
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
    Response response = null;

    try {
      reviewMetricsDbManager.deleteReviewMetrics(id);
      response = Response.ok().build();
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      response = Response.serverError().entity(e.getMessage()).build();
    }
    return response;
  }

  public List<ReviewCategory> getAllCategory() throws SQLException {
    List<ReviewCategory> reviewCategoryList = reviewCategoryDbManager.getReviewCategory();
    return reviewCategoryList;
  }

  public List<ReviewMetrics> getAllMetrics(int category) throws SQLException {
    List<ReviewMetrics> reviewMetricsList = reviewMetricsDbManager.getReviewMetricsList(category);
    return reviewMetricsList;
  }

}
