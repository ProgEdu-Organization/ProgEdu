package fcu.selab.progedu.status;

public abstract class StatusFactory {
  /**
   * 
   * @param statusType is the status type
   * @return Status status
   */
  public abstract Status getStatus(String statusType);
}


