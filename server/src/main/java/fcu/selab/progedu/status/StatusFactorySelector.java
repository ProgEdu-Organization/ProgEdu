package fcu.selab.progedu.status;

import fcu.selab.progedu.project.ProjectTypeEnum;

public class StatusFactorySelector {
  /**
   * getStatusFactory
   * 
   * @param projectType project type
   */
  public static StatusFactory getStatusFactory(ProjectTypeEnum projectType) {
    switch (projectType) {
      case JAVAC: {
        return new JavacStatusFactory();
      }
      case MAVEN: {
        return new MavenStatusFactory();
      }
      case WEB: {
        return new WebStatusFactory();
      }
      case APP: {
        return null;
      }
      default: {
        return null;
      }
    }
  }
}
