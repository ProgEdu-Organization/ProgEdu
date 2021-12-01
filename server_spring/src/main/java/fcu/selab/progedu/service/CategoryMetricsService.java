package fcu.selab.progedu.service;

import fcu.selab.progedu.data.ReviewCategory;
import fcu.selab.progedu.data.ReviewMetrics;
import fcu.selab.progedu.db.ReviewCategoryDbManager;
import fcu.selab.progedu.db.ReviewMetricsDbManager;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.minidev.json.JSONObject;

import javax.ws.rs.QueryParam;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value ="/categoryMetrics")
public class CategoryMetricsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryMetricsService.class);
  private ReviewCategoryDbManager reviewCategoryDbManager = ReviewCategoryDbManager.getInstance();
  private ReviewMetricsDbManager reviewMetricsDbManager = ReviewMetricsDbManager.getInstance();

  @GetMapping("category")
  public ResponseEntity<Object> getCategory() {

    HttpHeaders headers = new HttpHeaders();
    //

    try {
      List<JSONObject> array = new ArrayList<>();
      JSONObject result = new JSONObject();
      List<ReviewCategory> reviewCategoryList = getAllCategory();
      for (ReviewCategory reviewCategory : reviewCategoryList) {
        JSONObject object = new JSONObject();
        object.put("id", reviewCategory.getId());
        object.put("name", reviewCategory.getName());
        object.put("metrics", reviewCategory.getMetrics());
        array.add(object);
      }
      result.put("allCategory", array);

      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public List<ReviewCategory> getAllCategory() throws SQLException {
    List<ReviewCategory> reviewCategoryList = reviewCategoryDbManager.getReviewCategory();
    return reviewCategoryList;
  }

  @GetMapping("metrics")
  public ResponseEntity<Object> getMetrics(@QueryParam("category") int category) {

    HttpHeaders headers = new HttpHeaders();
    //

    try {
      List<JSONObject> array = new ArrayList<>();

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
        array.add(object);
      }
      result.put("allMetrics", array);

      return new ResponseEntity<Object>(result, headers, HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  public List<ReviewMetrics> getAllMetrics(int category) throws SQLException {
    List<ReviewMetrics> reviewMetricsList = reviewMetricsDbManager.getReviewMetricsList(category);
    return reviewMetricsList;
  }

  @PostMapping("category/create")
  public ResponseEntity<Object> createCategory(@QueryParam("name") String name,
                                 @QueryParam("metrics") String metrics) {
    HttpHeaders headers = new HttpHeaders();
    //

    try {
      reviewCategoryDbManager.insertReviewCategory(name, metrics);
      return new ResponseEntity<Object>(headers, HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }


  @CrossOrigin(origins = "*")
  @PutMapping("category/edit")
  public ResponseEntity<Object> editCategory(@QueryParam("id") int id,
                               @QueryParam("metrics") String metrics) {

    try {
      reviewCategoryDbManager.editReviewCategoryById(id, metrics);
      return new ResponseEntity<Object>(HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

  }

  @CrossOrigin(origins = "*")
  @DeleteMapping("category/delete")
  public ResponseEntity<Object> deleteCategory(@QueryParam("id") int id) {

    try {
      reviewCategoryDbManager.deleteReviewCategoryById(id);
      return new ResponseEntity<Object>(HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("metrics/create")
  public ResponseEntity<Object> createMetrics(@QueryParam("category") int category,
                                @QueryParam("mode") int mode,
                                @QueryParam("metrics") String metrics,
                                @QueryParam("description") String description,
                                @QueryParam("link") String link) {

    HttpHeaders headers = new HttpHeaders();
    //

    try {
      reviewMetricsDbManager.insertReviewMetrics(category, mode, metrics, description, link);
      return new ResponseEntity<Object>(headers, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @CrossOrigin(origins = "*")
  @PutMapping("metrics/edit")
  public ResponseEntity<Object> editMetrics(@QueryParam("id") int id,
                              @QueryParam("metrics") String metrics,
                              @QueryParam("mode") int mode,
                              @QueryParam("description") String description,
                              @QueryParam("link") String link) {

    try {
      reviewMetricsDbManager.editReviewMetricsById(id, metrics, mode, description, link);
      return new ResponseEntity<Object>(HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("metrics/delete")
  public ResponseEntity<Object> deleteMetrics(@QueryParam("id") int id) {

    try {
      reviewMetricsDbManager.deleteReviewMetrics(id);
      return new ResponseEntity<Object>(HttpStatus.OK);

    } catch (Exception e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}
