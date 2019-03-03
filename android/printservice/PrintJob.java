package android.printservice;

import android.content.Context;
import android.os.RemoteException;
import android.print.PrintJobId;
import android.print.PrintJobInfo;
import android.util.Log;

public final class PrintJob
{
  private static final String LOG_TAG = "PrintJob";
  private PrintJobInfo mCachedInfo;
  private final Context mContext;
  private final PrintDocument mDocument;
  private final IPrintServiceClient mPrintServiceClient;
  
  PrintJob(Context paramContext, PrintJobInfo paramPrintJobInfo, IPrintServiceClient paramIPrintServiceClient)
  {
    mContext = paramContext;
    mCachedInfo = paramPrintJobInfo;
    mPrintServiceClient = paramIPrintServiceClient;
    mDocument = new PrintDocument(mCachedInfo.getId(), paramIPrintServiceClient, paramPrintJobInfo.getDocumentInfo());
  }
  
  private boolean isInImmutableState()
  {
    int i = mCachedInfo.getState();
    boolean bool;
    if ((i != 5) && (i != 7) && (i != 6)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean setState(int paramInt, String paramString)
  {
    try
    {
      if (mPrintServiceClient.setPrintJobState(mCachedInfo.getId(), paramInt, paramString))
      {
        mCachedInfo.setState(paramInt);
        mCachedInfo.setStatus(paramString);
        return true;
      }
    }
    catch (RemoteException localRemoteException)
    {
      paramString = new StringBuilder();
      paramString.append("Error setting the state of job: ");
      paramString.append(mCachedInfo.getId());
      Log.e("PrintJob", paramString.toString(), localRemoteException);
    }
    return false;
  }
  
  public boolean block(String paramString)
  {
    PrintService.throwIfNotCalledOnMainThread();
    int i = getInfo().getState();
    if ((i != 3) && (i != 4)) {
      return false;
    }
    return setState(4, paramString);
  }
  
  public boolean cancel()
  {
    
    if (!isInImmutableState()) {
      return setState(7, null);
    }
    return false;
  }
  
  public boolean complete()
  {
    
    if (isStarted()) {
      return setState(5, null);
    }
    return false;
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
    return mCachedInfo.getId().equals(mCachedInfo.getId());
  }
  
  public boolean fail(String paramString)
  {
    
    if (!isInImmutableState()) {
      return setState(6, paramString);
    }
    return false;
  }
  
  public int getAdvancedIntOption(String paramString)
  {
    PrintService.throwIfNotCalledOnMainThread();
    return getInfo().getAdvancedIntOption(paramString);
  }
  
  public String getAdvancedStringOption(String paramString)
  {
    PrintService.throwIfNotCalledOnMainThread();
    return getInfo().getAdvancedStringOption(paramString);
  }
  
  public PrintDocument getDocument()
  {
    PrintService.throwIfNotCalledOnMainThread();
    return mDocument;
  }
  
  public PrintJobId getId()
  {
    PrintService.throwIfNotCalledOnMainThread();
    return mCachedInfo.getId();
  }
  
  public PrintJobInfo getInfo()
  {
    
    if (isInImmutableState()) {
      return mCachedInfo;
    }
    Object localObject1 = null;
    try
    {
      localObject2 = mPrintServiceClient.getPrintJobInfo(mCachedInfo.getId());
      localObject1 = localObject2;
    }
    catch (RemoteException localRemoteException)
    {
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Couldn't get info for job: ");
      ((StringBuilder)localObject2).append(mCachedInfo.getId());
      Log.e("PrintJob", ((StringBuilder)localObject2).toString(), localRemoteException);
    }
    if (localObject1 != null) {
      mCachedInfo = localObject1;
    }
    return mCachedInfo;
  }
  
  public String getTag()
  {
    PrintService.throwIfNotCalledOnMainThread();
    return getInfo().getTag();
  }
  
  public boolean hasAdvancedOption(String paramString)
  {
    PrintService.throwIfNotCalledOnMainThread();
    return getInfo().hasAdvancedOption(paramString);
  }
  
  public int hashCode()
  {
    return mCachedInfo.getId().hashCode();
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
  
  public void setProgress(float paramFloat)
  {
    
    try
    {
      mPrintServiceClient.setProgress(mCachedInfo.getId(), paramFloat);
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error setting progress for job: ");
      localStringBuilder.append(mCachedInfo.getId());
      Log.e("PrintJob", localStringBuilder.toString(), localRemoteException);
    }
  }
  
  public void setStatus(int paramInt)
  {
    
    try
    {
      mPrintServiceClient.setStatusRes(mCachedInfo.getId(), paramInt, mContext.getPackageName());
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error setting status for job: ");
      localStringBuilder.append(mCachedInfo.getId());
      Log.e("PrintJob", localStringBuilder.toString(), localRemoteException);
    }
  }
  
  public void setStatus(CharSequence paramCharSequence)
  {
    
    try
    {
      mPrintServiceClient.setStatus(mCachedInfo.getId(), paramCharSequence);
    }
    catch (RemoteException localRemoteException)
    {
      paramCharSequence = new StringBuilder();
      paramCharSequence.append("Error setting status for job: ");
      paramCharSequence.append(mCachedInfo.getId());
      Log.e("PrintJob", paramCharSequence.toString(), localRemoteException);
    }
  }
  
  public boolean setTag(String paramString)
  {
    
    if (isInImmutableState()) {
      return false;
    }
    try
    {
      boolean bool = mPrintServiceClient.setPrintJobTag(mCachedInfo.getId(), paramString);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      paramString = new StringBuilder();
      paramString.append("Error setting tag for job: ");
      paramString.append(mCachedInfo.getId());
      Log.e("PrintJob", paramString.toString(), localRemoteException);
    }
    return false;
  }
  
  public boolean start()
  {
    PrintService.throwIfNotCalledOnMainThread();
    int i = getInfo().getState();
    if ((i != 2) && (i != 4)) {
      return false;
    }
    return setState(3, null);
  }
}
