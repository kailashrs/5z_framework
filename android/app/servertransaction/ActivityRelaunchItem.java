package android.app.servertransaction;

import android.app.ActivityThread.ActivityClientRecord;
import android.app.ClientTransactionHandler;
import android.app.ResultInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;
import android.util.MergedConfiguration;
import com.android.internal.content.ReferrerIntent;
import java.util.List;
import java.util.Objects;

public class ActivityRelaunchItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<ActivityRelaunchItem> CREATOR = new Parcelable.Creator()
  {
    public ActivityRelaunchItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ActivityRelaunchItem(paramAnonymousParcel, null);
    }
    
    public ActivityRelaunchItem[] newArray(int paramAnonymousInt)
    {
      return new ActivityRelaunchItem[paramAnonymousInt];
    }
  };
  private static final String TAG = "ActivityRelaunchItem";
  private ActivityThread.ActivityClientRecord mActivityClientRecord;
  private MergedConfiguration mConfig;
  private int mConfigChanges;
  private List<ReferrerIntent> mPendingNewIntents;
  private List<ResultInfo> mPendingResults;
  private boolean mPreserveWindow;
  
  private ActivityRelaunchItem() {}
  
  private ActivityRelaunchItem(Parcel paramParcel)
  {
    mPendingResults = paramParcel.createTypedArrayList(ResultInfo.CREATOR);
    mPendingNewIntents = paramParcel.createTypedArrayList(ReferrerIntent.CREATOR);
    mConfigChanges = paramParcel.readInt();
    mConfig = ((MergedConfiguration)paramParcel.readTypedObject(MergedConfiguration.CREATOR));
    mPreserveWindow = paramParcel.readBoolean();
  }
  
  public static ActivityRelaunchItem obtain(List<ResultInfo> paramList, List<ReferrerIntent> paramList1, int paramInt, MergedConfiguration paramMergedConfiguration, boolean paramBoolean)
  {
    ActivityRelaunchItem localActivityRelaunchItem1 = (ActivityRelaunchItem)ObjectPool.obtain(ActivityRelaunchItem.class);
    ActivityRelaunchItem localActivityRelaunchItem2 = localActivityRelaunchItem1;
    if (localActivityRelaunchItem1 == null) {
      localActivityRelaunchItem2 = new ActivityRelaunchItem();
    }
    mPendingResults = paramList;
    mPendingNewIntents = paramList1;
    mConfigChanges = paramInt;
    mConfig = paramMergedConfiguration;
    mPreserveWindow = paramBoolean;
    return localActivityRelaunchItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (ActivityRelaunchItem)paramObject;
      if ((!Objects.equals(mPendingResults, mPendingResults)) || (!Objects.equals(mPendingNewIntents, mPendingNewIntents)) || (mConfigChanges != mConfigChanges) || (!Objects.equals(mConfig, mConfig)) || (mPreserveWindow != mPreserveWindow)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    if (mActivityClientRecord == null) {
      return;
    }
    Trace.traceBegin(64L, "activityRestart");
    paramClientTransactionHandler.handleRelaunchActivity(mActivityClientRecord, paramPendingTransactionActions);
    Trace.traceEnd(64L);
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * 17 + Objects.hashCode(mPendingResults)) + Objects.hashCode(mPendingNewIntents)) + mConfigChanges) + Objects.hashCode(mConfig)) + mPreserveWindow;
  }
  
  public void postExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    paramClientTransactionHandler.reportRelaunch(paramIBinder, paramPendingTransactionActions);
  }
  
  public void preExecute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder)
  {
    mActivityClientRecord = paramClientTransactionHandler.prepareRelaunchActivity(paramIBinder, mPendingResults, mPendingNewIntents, mConfigChanges, mConfig, mPreserveWindow);
  }
  
  public void recycle()
  {
    mPendingResults = null;
    mPendingNewIntents = null;
    mConfigChanges = 0;
    mConfig = null;
    mPreserveWindow = false;
    mActivityClientRecord = null;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ActivityRelaunchItem{pendingResults=");
    localStringBuilder.append(mPendingResults);
    localStringBuilder.append(",pendingNewIntents=");
    localStringBuilder.append(mPendingNewIntents);
    localStringBuilder.append(",configChanges=");
    localStringBuilder.append(mConfigChanges);
    localStringBuilder.append(",config=");
    localStringBuilder.append(mConfig);
    localStringBuilder.append(",preserveWindow");
    localStringBuilder.append(mPreserveWindow);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(mPendingResults, paramInt);
    paramParcel.writeTypedList(mPendingNewIntents, paramInt);
    paramParcel.writeInt(mConfigChanges);
    paramParcel.writeTypedObject(mConfig, paramInt);
    paramParcel.writeBoolean(mPreserveWindow);
  }
}
