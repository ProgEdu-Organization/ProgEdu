package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public interface Status {
  public String extractFailureMsg(String consoleText);

  public ArrayList<FeedBack> formatExamineMsg(String consoleText);

  static final Logger LOGGER = LoggerFactory.getLogger(Status.class);

  /**
   *
   * @param arrayList all feedbacks
   * @return jsonString
   */
  default String tojsonArray(ArrayList<FeedBack> arrayList) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
              .writeValueAsString(arrayList);
      return jsonString;
    } catch (JsonGenerationException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return "JsonGenerationException Error";
    } catch (JsonMappingException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return "JsonMappingException Error";
    } catch (IOException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
      return "IOException Error";
    }
  }

}
