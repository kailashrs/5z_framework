package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.app.ResultInfo;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;
import java.util.List;
import java.util.Objects;

public class ActivityResultItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<ActivityResultItem> CREATOR = new Parcelable.Creator()
  {
    public ActivityResultItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ActivityResultItem(paramAnonymousParcel, null);
    }
    
    public ActivityResultItem[] newArray(int paramAnonymousInt)
    {
      return new ActivityResultItem[paramAnonymousInt];
    }
  };
  private List<ResultInfo> mResultInfoList;
  
  private ActivityResultItem() {}
  
  private ActivityResultItem(Parcel paramParcel)
  {
    mResultInfoList = paramParcel.createTypedArrayList(ResultInfo.CREATOR);
  }
  
  public static ActivityResultItem obtain(List<ResultInfo> paramList)
  {
    ActivityResultItem localActivityResultItem1 = (ActivityResultItem)ObjectPool.obtain(ActivityResultItem.class);
    ActivityResultItem localActivityResultItem2 = localActivityResultItem1;
    if (localActivityResultItem1 == null) {
      localActivityResultItem2 = new ActivityResultItem();
    }
    mResultInfoList = paramList;
    return localActivityResultItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (ActivityResultItem)paramObject;
      return Objects.equals(mResultInfoList, mResultInfoList);
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityDeliverResult");
    paramClientTransactionHandler.handleSendResult(paramIBinder, mResultInfoList, "ACTIVITY_RESULT");
    Trace.traceEnd(64L);
  }
  
  public int hashCode()
  {
    return mResultInfoList.hashCode();
  }
  
  public void recycle()
  {
    mResultInfoList = null;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ActivityResultItem{resultInfoList=");
    localStringBuilder.append(mResultInfoList);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(mResultInfoList, paramInt);
  }
}
