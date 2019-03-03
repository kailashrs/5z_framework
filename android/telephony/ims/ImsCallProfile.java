package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.VideoProfile;
import android.util.Log;

@SystemApi
public final class ImsCallProfile
  implements Parcelable
{
  public static final int CALL_RESTRICT_CAUSE_DISABLED = 2;
  public static final int CALL_RESTRICT_CAUSE_HD = 3;
  public static final int CALL_RESTRICT_CAUSE_NONE = 0;
  public static final int CALL_RESTRICT_CAUSE_RAT = 1;
  public static final int CALL_TYPE_UNKNOWN = -1;
  public static final int CALL_TYPE_VIDEO_N_VOICE = 3;
  public static final int CALL_TYPE_VOICE = 2;
  public static final int CALL_TYPE_VOICE_N_VIDEO = 1;
  public static final int CALL_TYPE_VS = 8;
  public static final int CALL_TYPE_VS_RX = 10;
  public static final int CALL_TYPE_VS_TX = 9;
  public static final int CALL_TYPE_VT = 4;
  public static final int CALL_TYPE_VT_NODIR = 7;
  public static final int CALL_TYPE_VT_RX = 6;
  public static final int CALL_TYPE_VT_TX = 5;
  public static final Parcelable.Creator<ImsCallProfile> CREATOR = new Parcelable.Creator()
  {
    public ImsCallProfile createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsCallProfile(paramAnonymousParcel);
    }
    
    public ImsCallProfile[] newArray(int paramAnonymousInt)
    {
      return new ImsCallProfile[paramAnonymousInt];
    }
  };
  public static final int DIALSTRING_NORMAL = 0;
  public static final int DIALSTRING_SS_CONF = 1;
  public static final int DIALSTRING_USSD = 2;
  public static final String EXTRA_ADDITIONAL_CALL_INFO = "AdditionalCallInfo";
  public static final String EXTRA_CALL_MODE_CHANGEABLE = "call_mode_changeable";
  public static final String EXTRA_CALL_RAT_TYPE = "CallRadioTech";
  public static final String EXTRA_CALL_RAT_TYPE_ALT = "callRadioTech";
  public static final String EXTRA_CHILD_NUMBER = "ChildNum";
  public static final String EXTRA_CNA = "cna";
  public static final String EXTRA_CNAP = "cnap";
  public static final String EXTRA_CODEC = "Codec";
  public static final String EXTRA_CONFERENCE = "conference";
  public static final String EXTRA_CONFERENCE_AVAIL = "conference_avail";
  public static final String EXTRA_DIALSTRING = "dialstring";
  public static final String EXTRA_DISPLAY_TEXT = "DisplayText";
  public static final String EXTRA_E_CALL = "e_call";
  public static final String EXTRA_IS_CALL_PULL = "CallPull";
  public static final String EXTRA_OEM_EXTRAS = "OemCallExtras";
  public static final String EXTRA_OI = "oi";
  public static final String EXTRA_OIR = "oir";
  public static final String EXTRA_REMOTE_URI = "remote_uri";
  public static final String EXTRA_USSD = "ussd";
  public static final String EXTRA_VMS = "vms";
  public static final int OIR_DEFAULT = 0;
  public static final int OIR_PRESENTATION_NOT_RESTRICTED = 2;
  public static final int OIR_PRESENTATION_PAYPHONE = 4;
  public static final int OIR_PRESENTATION_RESTRICTED = 1;
  public static final int OIR_PRESENTATION_UNKNOWN = 3;
  public static final int SERVICE_TYPE_EMERGENCY = 2;
  public static final int SERVICE_TYPE_NONE = 0;
  public static final int SERVICE_TYPE_NORMAL = 1;
  private static final String TAG = "ImsCallProfile";
  public Bundle mCallExtras;
  public int mCallType;
  public ImsStreamMediaProfile mMediaProfile;
  public int mRestrictCause = 0;
  public int mServiceType;
  
  public ImsCallProfile()
  {
    mServiceType = 1;
    mCallType = 1;
    mCallExtras = new Bundle();
    mMediaProfile = new ImsStreamMediaProfile();
  }
  
  public ImsCallProfile(int paramInt1, int paramInt2)
  {
    mServiceType = paramInt1;
    mCallType = paramInt2;
    mCallExtras = new Bundle();
    mMediaProfile = new ImsStreamMediaProfile();
  }
  
  public ImsCallProfile(int paramInt1, int paramInt2, Bundle paramBundle, ImsStreamMediaProfile paramImsStreamMediaProfile)
  {
    mServiceType = paramInt1;
    mCallType = paramInt2;
    mCallExtras = paramBundle;
    mMediaProfile = paramImsStreamMediaProfile;
  }
  
  public ImsCallProfile(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static int OIRToPresentation(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 3;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 1;
    }
    return 2;
  }
  
  public static int getCallTypeFromVideoState(int paramInt)
  {
    boolean bool1 = isVideoStateSet(paramInt, 1);
    boolean bool2 = isVideoStateSet(paramInt, 2);
    if (isVideoStateSet(paramInt, 4)) {
      return 7;
    }
    if ((bool1) && (!bool2)) {
      return 5;
    }
    if ((!bool1) && (bool2)) {
      return 6;
    }
    if ((bool1) && (bool2)) {
      return 4;
    }
    return 2;
  }
  
  public static int getVideoStateFromCallType(int paramInt)
  {
    if (paramInt != 2) {
      switch (paramInt)
      {
      default: 
        paramInt = 0;
        break;
      case 6: 
        paramInt = 2;
        break;
      case 5: 
        paramInt = 1;
        break;
      case 4: 
        paramInt = 3;
        break;
      }
    } else {
      paramInt = 0;
    }
    return paramInt;
  }
  
  public static int getVideoStateFromImsCallProfile(ImsCallProfile paramImsCallProfile)
  {
    int i = getVideoStateFromCallType(mCallType);
    if ((paramImsCallProfile.isVideoPaused()) && (!VideoProfile.isAudioOnly(i))) {
      i |= 0x4;
    } else {
      i &= 0xFFFFFFFB;
    }
    return i;
  }
  
  private static boolean isVideoStateSet(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 & paramInt2) == paramInt2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private Bundle maybeCleanseExtras(Bundle paramBundle)
  {
    if (paramBundle == null) {
      return null;
    }
    int i = paramBundle.size();
    paramBundle = paramBundle.filterValues();
    int j = paramBundle.size();
    if (i != j)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("maybeCleanseExtras: ");
      localStringBuilder.append(i - j);
      localStringBuilder.append(" extra values were removed - only primitive types and system parcelables are permitted.");
      Log.i("ImsCallProfile", localStringBuilder.toString());
    }
    return paramBundle;
  }
  
  public static int presentationToOIR(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 1;
    }
    return 2;
  }
  
  public static int presentationToOir(int paramInt)
  {
    return presentationToOIR(paramInt);
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    mServiceType = paramParcel.readInt();
    mCallType = paramParcel.readInt();
    mCallExtras = paramParcel.readBundle();
    mMediaProfile = ((ImsStreamMediaProfile)paramParcel.readParcelable(ImsStreamMediaProfile.class.getClassLoader()));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getCallExtra(String paramString)
  {
    return getCallExtra(paramString, "");
  }
  
  public String getCallExtra(String paramString1, String paramString2)
  {
    if (mCallExtras == null) {
      return paramString2;
    }
    return mCallExtras.getString(paramString1, paramString2);
  }
  
  public boolean getCallExtraBoolean(String paramString)
  {
    return getCallExtraBoolean(paramString, false);
  }
  
  public boolean getCallExtraBoolean(String paramString, boolean paramBoolean)
  {
    if (mCallExtras == null) {
      return paramBoolean;
    }
    return mCallExtras.getBoolean(paramString, paramBoolean);
  }
  
  public int getCallExtraInt(String paramString)
  {
    return getCallExtraInt(paramString, -1);
  }
  
  public int getCallExtraInt(String paramString, int paramInt)
  {
    if (mCallExtras == null) {
      return paramInt;
    }
    return mCallExtras.getInt(paramString, paramInt);
  }
  
  public Bundle getCallExtras()
  {
    return mCallExtras;
  }
  
  public int getCallType()
  {
    return mCallType;
  }
  
  public ImsStreamMediaProfile getMediaProfile()
  {
    return mMediaProfile;
  }
  
  public int getRestrictCause()
  {
    return mRestrictCause;
  }
  
  public int getServiceType()
  {
    return mServiceType;
  }
  
  public boolean isVideoCall()
  {
    return VideoProfile.isVideo(getVideoStateFromCallType(mCallType));
  }
  
  public boolean isVideoPaused()
  {
    boolean bool;
    if (mMediaProfile.mVideoDirection == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setCallExtra(String paramString1, String paramString2)
  {
    if (mCallExtras != null) {
      mCallExtras.putString(paramString1, paramString2);
    }
  }
  
  public void setCallExtraBoolean(String paramString, boolean paramBoolean)
  {
    if (mCallExtras != null) {
      mCallExtras.putBoolean(paramString, paramBoolean);
    }
  }
  
  public void setCallExtraInt(String paramString, int paramInt)
  {
    if (mCallExtras != null) {
      mCallExtras.putInt(paramString, paramInt);
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ serviceType=");
    localStringBuilder.append(mServiceType);
    localStringBuilder.append(", callType=");
    localStringBuilder.append(mCallType);
    localStringBuilder.append(", restrictCause=");
    localStringBuilder.append(mRestrictCause);
    localStringBuilder.append(", mediaProfile=");
    localStringBuilder.append(mMediaProfile.toString());
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void updateCallExtras(ImsCallProfile paramImsCallProfile)
  {
    mCallExtras.clear();
    mCallExtras = ((Bundle)mCallExtras.clone());
  }
  
  public void updateCallType(ImsCallProfile paramImsCallProfile)
  {
    mCallType = mCallType;
  }
  
  public void updateMediaProfile(ImsCallProfile paramImsCallProfile)
  {
    mMediaProfile = mMediaProfile;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Bundle localBundle = maybeCleanseExtras(mCallExtras);
    paramParcel.writeInt(mServiceType);
    paramParcel.writeInt(mCallType);
    paramParcel.writeBundle(localBundle);
    paramParcel.writeParcelable(mMediaProfile, 0);
  }
}
