package fcu.selab.progedu.setting;

import java.util.*;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PatternSet;
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
    Iterator<Dependency> iterator = model.getDependencies().listIterator();

    while(iterator.hasNext()) {
      System.out.println(iterator.next());
    }
    /*
    List<Dependency> dependencyList = model.getDependencies();
    for (Dependency dependency : dependencyList) {
      System.out.println(dependency.toString());
    }
    List<Plugin> pluginList = model.getBuild().getPlugins();
    for (Plugin plugin : pluginList) {
      System.out.println(plugin.getGroupId());
      System.out.println(plugin.getArtifactId());
      System.out.println(plugin.getVersion());
    }*/
  }

}