package android.telephony;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class VoLteServiceState
  implements Parcelable
{
  public static final Parcelable.Creator<VoLteServiceState> CREATOR = new Parcelable.Creator()
  {
    public VoLteServiceState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VoLteServiceState(paramAnonymousParcel);
    }
    
    public VoLteServiceState[] newArray(int paramAnonymousInt)
    {
      return new VoLteServiceState[paramAnonymousInt];
    }
  };
  private static final boolean DBG = false;
  public static final int HANDOVER_CANCELED = 3;
  public static final int HANDOVER_COMPLETED = 1;
  public static final int HANDOVER_FAILED = 2;
  public static final int HANDOVER_STARTED = 0;
  public static final int INVALID = Integer.MAX_VALUE;
  private static final String LOG_TAG = "VoLteServiceState";
  public static final int NOT_SUPPORTED = 0;
  public static final int SUPPORTED = 1;
  private int mSrvccState;
  
  public VoLteServiceState()
  {
    initialize();
  }
  
  public VoLteServiceState(int paramInt)
  {
    initialize();
    mSrvccState = paramInt;
  }
  
  public VoLteServiceState(Parcel paramParcel)
  {
    mSrvccState = paramParcel.readInt();
  }
  
  public VoLteServiceState(VoLteServiceState paramVoLteServiceState)
  {
    copyFrom(paramVoLteServiceState);
  }
  
  private void initialize()
  {
    mSrvccState = Integer.MAX_VALUE;
  }
  
  private static void log(String paramString)
  {
    Rlog.w("VoLteServiceState", paramString);
  }
  
  public static VoLteServiceState newFromBundle(Bundle paramBundle)
  {
    VoLteServiceState localVoLteServiceState = new VoLteServiceState();
    localVoLteServiceState.setFromNotifierBundle(paramBundle);
    return localVoLteServiceState;
  }
  
  private void setFromNotifierBundle(Bundle paramBundle)
  {
    mSrvccState = paramBundle.getInt("mSrvccState");
  }
  
  protected void copyFrom(VoLteServiceState paramVoLteServiceState)
  {
    mSrvccState = mSrvccState;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    try
    {
      VoLteServiceState localVoLteServiceState = (VoLteServiceState)paramObject;
      if (paramObject == null) {
        return false;
      }
      if (mSrvccState == mSrvccState) {
        bool = true;
      }
      return bool;
    }
    catch (ClassCastException paramObject) {}
    return false;
  }
  
  public void fillInNotifierBundle(Bundle paramBundle)
  {
    paramBundle.putInt("mSrvccState", mSrvccState);
  }
  
  public int getSrvccState()
  {
    return mSrvccState;
  }
  
  public int hashCode()
  {
    return mSrvccState * 31;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("VoLteServiceState: ");
    localStringBuilder.append(mSrvccState);
    return localStringBuilder.toString();
  }
  
  public void validateInput() {}
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSrvccState);
  }
}
