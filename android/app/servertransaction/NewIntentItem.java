package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;
import com.android.internal.content.ReferrerIntent;
import java.util.List;
import java.util.Objects;

public class NewIntentItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<NewIntentItem> CREATOR = new Parcelable.Creator()
  {
    public NewIntentItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NewIntentItem(paramAnonymousParcel, null);
    }
    
    public NewIntentItem[] newArray(int paramAnonymousInt)
    {
      return new NewIntentItem[paramAnonymousInt];
    }
  };
  private List<ReferrerIntent> mIntents;
  private boolean mPause;
  
  private NewIntentItem() {}
  
  private NewIntentItem(Parcel paramParcel)
  {
    mPause = paramParcel.readBoolean();
    mIntents = paramParcel.createTypedArrayList(ReferrerIntent.CREATOR);
  }
  
  public static NewIntentItem obtain(List<ReferrerIntent> paramList, boolean paramBoolean)
  {
    NewIntentItem localNewIntentItem1 = (NewIntentItem)ObjectPool.obtain(NewIntentItem.class);
    NewIntentItem localNewIntentItem2 = localNewIntentItem1;
    if (localNewIntentItem1 == null) {
      localNewIntentItem2 = new NewIntentItem();
    }
    mIntents = paramList;
    mPause = paramBoolean;
    return localNewIntentItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (NewIntentItem)paramObject;
      if ((mPause != mPause) || (!Objects.equals(mIntents, mIntents))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityNewIntent");
    paramClientTransactionHandler.handleNewIntent(paramIBinder, mIntents, mPause);
    Trace.traceEnd(64L);
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + mPause) + mIntents.hashCode();
  }
  
  public void recycle()
  {
    mIntents = null;
    mPause = false;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NewIntentItem{pause=");
    localStringBuilder.append(mPause);
    localStringBuilder.append(",intents=");
    localStringBuilder.append(mIntents);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(mPause);
    paramParcel.writeTypedList(mIntents, paramInt);
  }
}
