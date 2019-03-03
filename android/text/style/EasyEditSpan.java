package android.text.style;

import android.app.PendingIntent;
import android.os.Parcel;
import android.text.ParcelableSpan;

public class EasyEditSpan
  implements ParcelableSpan
{
  public static final String EXTRA_TEXT_CHANGED_TYPE = "android.text.style.EXTRA_TEXT_CHANGED_TYPE";
  public static final int TEXT_DELETED = 1;
  public static final int TEXT_MODIFIED = 2;
  private boolean mDeleteEnabled;
  private final PendingIntent mPendingIntent;
  
  public EasyEditSpan()
  {
    mPendingIntent = null;
    mDeleteEnabled = true;
  }
  
  public EasyEditSpan(PendingIntent paramPendingIntent)
  {
    mPendingIntent = paramPendingIntent;
    mDeleteEnabled = true;
  }
  
  public EasyEditSpan(Parcel paramParcel)
  {
    mPendingIntent = ((PendingIntent)paramParcel.readParcelable(null));
    int i = paramParcel.readByte();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mDeleteEnabled = bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PendingIntent getPendingIntent()
  {
    return mPendingIntent;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 22;
  }
  
  public boolean isDeleteEnabled()
  {
    return mDeleteEnabled;
  }
  
  public void setDeleteEnabled(boolean paramBoolean)
  {
    mDeleteEnabled = paramBoolean;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mPendingIntent, 0);
    paramParcel.writeByte((byte)mDeleteEnabled);
  }
}
