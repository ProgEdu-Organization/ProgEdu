package fcu.selab.progedu.status;

public class StatusFactory {
  /**
   * 
   * @param type type
   * @return Status status
   */
  public static Status getStatus(String type) {
    switch (type) {
      case "NB": {
        return new NotBuilt();
      }
      case "CPF": {
        return new CompileFailure();
      }
      case "CSF": {
        return new CheckstyleFailure();
      }
      case "CTF": {
        return new UnitTestFailure();
      }
      case "S": {
        return new BuildSuccess();
      }

      default: {
        return null;
      }

    }
  }
}
