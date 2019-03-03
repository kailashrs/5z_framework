package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;

public class SpellCheckSpan
  implements ParcelableSpan
{
  private boolean mSpellCheckInProgress;
  
  public SpellCheckSpan()
  {
    mSpellCheckInProgress = false;
  }
  
  public SpellCheckSpan(Parcel paramParcel)
  {
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mSpellCheckInProgress = bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getSpanTypeId()
  {
    return getSpanTypeIdInternal();
  }
  
  public int getSpanTypeIdInternal()
  {
    return 20;
  }
  
  public boolean isSpellCheckInProgress()
  {
    return mSpellCheckInProgress;
  }
  
  public void setSpellCheckInProgress(boolean paramBoolean)
  {
    mSpellCheckInProgress = paramBoolean;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelInternal(paramParcel, paramInt);
  }
  
  public void writeToParcelInternal(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSpellCheckInProgress);
  }
}
