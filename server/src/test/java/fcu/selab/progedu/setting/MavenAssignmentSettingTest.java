package fcu.selab.progedu.setting;

import java.util.*;

import org.apache.maven.model.*;
import org.apache.maven.plugin.lifecycle.Execution;
import org.junit.Test;

public class MavenAssignmentSettingTest extends PatternSet implements java.io.Serializable {

  @Test
  public void createAssignmentSettingTest() {
    List<String> order = new ArrayList<String>();
    order.add("Compile Failure");
    order.add("Coding Style Failure");
    order.add("Unit Test Failure");
    Model model = MavenAssignmentSetting.createAssignmentSettingTest(order, "test");
    System.out.println("modelVersion {" + model.getModelVersion() + "}");
    System.out.println("groupId {" + model.getGroupId() + "}");
    System.out.println("artifactId {" + model.getArtifactId() + "}");
    System.out.println("packaging {" + model.getPackaging() + "}");
    System.out.println("version {" + model.getVersion() + "}");
    System.out.println("name {" + model.getName() + "}");
    System.out.println("url {" + model.getUrl() + "}");

    List<Dependency> dependencyList = model.getDependencies();
    for (Dependency dependency : dependencyList) {
      System.out.println(dependency.toString());
    }
    Build build = model.getBuild();
    List<Plugin> pluginList = model.getBuild().getPlugins();
    for (Plugin plugin : pluginList) {
      System.out.println(plugin.getGroupId());
      System.out.println(plugin.getArtifactId());
      System.out.println(plugin.getVersion());
    }

  }

}