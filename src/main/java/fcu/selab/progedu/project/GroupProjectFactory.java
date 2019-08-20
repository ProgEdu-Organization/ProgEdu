package fcu.selab.progedu.project;

public class GroupProjectFactory {
  /**
   * get the type of project
   * 
   * @param projectType assignment type
   */
  public static GroupProjectType getGroupProjectType(String projectType) {
    ProjectTypeEnum type = ProjectTypeEnum.getProjectTypeEnum(projectType);

    switch (type) {
      case JAVAC: {
        return null;
      }
      case MAVEN: {
        return new MavenGroupProject();
      }
      case WEB: {
        return new WebGroupProject();
      }
      case APP: {
        return null;
      }
      default:
        return null;
    }
  }
}
