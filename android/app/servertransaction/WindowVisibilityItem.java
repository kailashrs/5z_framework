package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Trace;

public class WindowVisibilityItem
  extends ClientTransactionItem
{
  public static final Parcelable.Creator<WindowVisibilityItem> CREATOR = new Parcelable.Creator()
  {
    public WindowVisibilityItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WindowVisibilityItem(paramAnonymousParcel, null);
    }
    
    public WindowVisibilityItem[] newArray(int paramAnonymousInt)
    {
      return new WindowVisibilityItem[paramAnonymousInt];
    }
  };
  private boolean mShowWindow;
  
  private WindowVisibilityItem() {}
  
  private WindowVisibilityItem(Parcel paramParcel)
  {
    mShowWindow = paramParcel.readBoolean();
  }
  
  public static WindowVisibilityItem obtain(boolean paramBoolean)
  {
    WindowVisibilityItem localWindowVisibilityItem1 = (WindowVisibilityItem)ObjectPool.obtain(WindowVisibilityItem.class);
    WindowVisibilityItem localWindowVisibilityItem2 = localWindowVisibilityItem1;
    if (localWindowVisibilityItem1 == null) {
      localWindowVisibilityItem2 = new WindowVisibilityItem();
    }
    mShowWindow = paramBoolean;
    return localWindowVisibilityItem2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (WindowVisibilityItem)paramObject;
      if (mShowWindow != mShowWindow) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public void execute(ClientTransactionHandler paramClientTransactionHandler, IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    Trace.traceBegin(64L, "activityShowWindow");
    paramClientTransactionHandler.handleWindowVisibility(paramIBinder, mShowWindow);
    Trace.traceEnd(64L);
  }
  
  public int hashCode()
  {
    return 17 + true * mShowWindow;
  }
  
  public void recycle()
  {
    mShowWindow = false;
    ObjectPool.recycle(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WindowVisibilityItem{showWindow=");
    localStringBuilder.append(mShowWindow);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBoolean(mShowWindow);
  }
}
