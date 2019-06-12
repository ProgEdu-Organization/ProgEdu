package fcu.selab.progedu.service;

public class GroupProjectFactory {
  /**
   * get the type of project
   * 
   * @param projectType assignmentType
   */
  public static GroupProject getGroupProjectType(String projectType) {
    GroupProjectType groupProjectType = GroupProjectType.getGroupProjectType(projectType);

    switch (groupProjectType) {
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
