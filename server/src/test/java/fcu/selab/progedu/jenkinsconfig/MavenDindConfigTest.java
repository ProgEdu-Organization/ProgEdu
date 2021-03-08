package fcu.selab.progedu.jenkinsconfig;

import org.junit.Test;

import static org.junit.Assert.*;

public class MavenDindConfigTest {

    @Test
    public void readPipeline() {
        MavenDindConfig mavenDindConfig = new MavenDindConfig("franky-test", "franky-test", "franky-test", "franky-test");
        System.out.println(mavenDindConfig.getXmlConfig());
    }

    @Test
    public void createPipelineString() {
        MavenDindConfig mavenDindConfig = new MavenDindConfig("franky-test", "franky-test", "franky-test", "franky-test");
        mavenDindConfig.createPipelineString("franky-test", "franky-test", "franky-test", "franky-test");
    }
}