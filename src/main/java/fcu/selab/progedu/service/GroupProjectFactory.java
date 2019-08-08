package fcu.selab.progedu.service;

import fcu.selab.progedu.project.MavenGroupProject;
import fcu.selab.progedu.project.ProjectTypeEnum;

public class GroupProjectFactory {
  /**
   * get the type of project
   * 
   * @param projectType assignment type
   */
  public static GroupProject getGroupProjectType(String projectType) {
    ProjectTypeEnum type = ProjectTypeEnum
        .getStatusProjectTypeEnum(projectType);

    switch (type) {
      case JAVAC: {
        return null;
      }
      case MAVEN: {
        return new MavenGroupProject();
      }
      case WEB: {
        return null;
      }
      case APP: {
        return null;
      }
      default:
        return null;
    }
  }
}
