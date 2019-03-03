package android.print;

import java.util.Objects;

public final class PrintJob
{
  private PrintJobInfo mCachedInfo;
  private final PrintManager mPrintManager;
  
  PrintJob(PrintJobInfo paramPrintJobInfo, PrintManager paramPrintManager)
  {
    mCachedInfo = paramPrintJobInfo;
    mPrintManager = paramPrintManager;
  }
  
  private boolean isInImmutableState()
  {
    int i = mCachedInfo.getState();
    boolean bool;
    if ((i != 5) && (i != 7)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void cancel()
  {
    int i = getInfo().getState();
    if ((i == 2) || (i == 3) || (i == 4) || (i == 6)) {
      mPrintManager.cancelPrintJob(mCachedInfo.getId());
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (PrintJob)paramObject;
    return Objects.equals(mCachedInfo.getId(), mCachedInfo.getId());
  }
  
  public PrintJobId getId()
  {
    return mCachedInfo.getId();
  }
  
  public PrintJobInfo getInfo()
  {
    if (isInImmutableState()) {
      return mCachedInfo;
    }
    PrintJobInfo localPrintJobInfo = mPrintManager.getPrintJobInfo(mCachedInfo.getId());
    if (localPrintJobInfo != null) {
      mCachedInfo = localPrintJobInfo;
    }
    return mCachedInfo;
  }
  
  public int hashCode()
  {
    PrintJobId localPrintJobId = mCachedInfo.getId();
    if (localPrintJobId == null) {
      return 0;
    }
    return localPrintJobId.hashCode();
  }
  
  public boolean isBlocked()
  {
    boolean bool;
    if (getInfo().getState() == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCancelled()
  {
    boolean bool;
    if (getInfo().getState() == 7) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCompleted()
  {
    boolean bool;
    if (getInfo().getState() == 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isFailed()
  {
    boolean bool;
    if (getInfo().getState() == 6) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isQueued()
  {
    boolean bool;
    if (getInfo().getState() == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStarted()
  {
    boolean bool;
    if (getInfo().getState() == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void restart()
  {
    if (isFailed()) {
      mPrintManager.restartPrintJob(mCachedInfo.getId());
    }
  }
}
