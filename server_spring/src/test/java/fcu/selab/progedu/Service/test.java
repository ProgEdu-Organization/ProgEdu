package fcu.selab.progedu.Service;

import fcu.selab.progedu.db.AssignmentTimeDbManager;
import fcu.selab.progedu.service.AssignmentService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class test {
    public AssignmentService assignmentService = new AssignmentService();

    @Test
    public void test() throws ParseException {
        String time = "{\n" +
                "  \"0\": {\n" +
                "    \n" +
                "    \"releaseTime\":\"2021-08-17\",\n" +
                "    \"deadline\":\"2021-08-20\",\n" +
                "    \"reviewStart\":\"2021-08-20\",\n" +
                "    \"reviewEnd\":\"2021-08-20\"\n" +
                "  }, \n" +
                "  \"1\":{\n" +
                "    \"releaseTime\":\"2021-08-17\",\n" +
                "    \"deadline\":\"2021-08-20\",\n" +
                "    \"reviewStart\":\"2021-08-20\",\n" +
                "    \"reviewEnd\":\"2021-08-20\"\n" +
                "  }\n" +
                "}";
        MultipartFile file = new MockMultipartFile("sourceFile.txt", "Hello World".getBytes());
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(time);
        System.out.println(jsonObject.get("0"));
        AssignmentService a = new AssignmentService();
        a.createPeerReview("HW20","","3", file,2,"",time,2);
    }
}
