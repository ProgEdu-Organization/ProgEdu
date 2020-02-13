package fcu.selab.progedu.conn;
import fcu.selab.progedu.utils.ExceptionUtil;
import org.apache.commons.logging.Log;
import org.gitlab.api.models.GitlabProject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.gitlab.api.GitlabAPI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GitlabServiceTest {

    @Test
    void test_createUserProject_Username_is_root() {
        String actual_name = null;
        String hostUrl = null;
        String apiToken = null;

        Properties props = new Properties();
        try{
            props.load(new FileInputStream("./src/main/resources/config/gitlab_config.properties")); // ./ = ProgEdu/server
            hostUrl = (String)props.getProperty("GITLAB_HOST_URL");
            apiToken = (String)props.getProperty("GITLAB_API_TOKEN");
        } catch(Exception e){
            System.out.println(e);
        }

        try {
            GitlabAPI gitlabAPI = GitlabAPI.connect(hostUrl,apiToken);
            GitlabProject project = gitlabAPI.createUserProject(1, "testProject");
            actual_name = project.getOwner().getUsername();
            gitlabAPI.deleteProject(project.getId());
        } catch (IOException e) {
            System.out.println(e);
        }
        assertEquals("root", actual_name);
    }

}
