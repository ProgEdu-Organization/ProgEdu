package fcu.selab.progedu.service;

public class GroupProjectFactory {
  /**
   * get the type of project
   * 
   * @param projectType assignment type
   */
  public static GroupProject getGroupProjectType(String projectType) {
    AssignmentTypeEnum type = AssignmentTypeEnum
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
