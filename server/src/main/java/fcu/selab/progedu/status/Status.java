package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public interface Status {
  public String extractFailureMsg(String consoleText);

  public ArrayList<FeedBack> formatExamineMsg(String consoleText);

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
      e.printStackTrace();
      return "JsonGenerationException Error";
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return "JsonMappingException Error";
    } catch (IOException e) {
      e.printStackTrace();
      return "IOException Error";
    }
  }

}
