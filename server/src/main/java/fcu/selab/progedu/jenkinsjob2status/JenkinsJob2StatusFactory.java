package fcu.selab.progedu.jenkinsjob2status;

import fcu.selab.progedu.project.ProjectTypeEnum;

public class JenkinsJob2StatusFactory {

  public static JenkinsJobStatus createJenkinsJobStatus(ProjectTypeEnum projectTypeEnum) {

    switch(projectTypeEnum) {
      case JAVAC:
        return new JavaJenkinsJobStatus();
      case MAVEN:
        return new MavenJenkinsJobStatus();
      case WEB:
        return new WebJenkinsJobStatus();
      case ANDROID:
        return new AndroidJenkinsJobStatus();
      default:
        return null;
    }

  }



}
