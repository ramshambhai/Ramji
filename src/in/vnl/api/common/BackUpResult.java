package in.vnl.api.common;

import java.io.Serializable;

public class BackUpResult implements Serializable {
  public static final String SUCCESS = "Success";
  public static final String FAIL = "Failed";
  private String status = FAIL;
  private String backupName = "";
  private String reason = "";

  public BackUpResult() {
  }

  public String getBackupName() {
    return backupName;
  }

  public void setBackupName(String backupName) {
    this.backupName = backupName;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String toString() {
    return "Status:: " + status + " BackUp Name:: " + backupName + " Reason:: " + reason;
  }
}

