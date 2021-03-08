package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MavenPipelineConfigTest {

    @Test
    public void createPipelineString() {
        MavenPipelineConfig mavenPipelineConfig = new MavenPipelineConfig("franky-test", "franky-test", "franky-test", "franky-test");
        String test = mavenPipelineConfig.createPipeline("franky-test", "franky-test",
                "franky-test", "franky-test");
        System.out.println(test);
    }

    @Test
    public void setAll() {
        MavenPipelineConfig mavenPipelineConfig = new MavenPipelineConfig("franky-test", "franky-test", "franky-test", "franky-test");
        String pipeline = mavenPipelineConfig.getXmlConfig();
        System.out.printf(pipeline);
    }


}