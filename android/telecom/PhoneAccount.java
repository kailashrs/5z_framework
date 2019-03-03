package android.telecom;

import android.annotation.SystemApi;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class PhoneAccount
  implements Parcelable
{
  public static final int CAPABILITY_CALL_PROVIDER = 2;
  public static final int CAPABILITY_CALL_SUBJECT = 64;
  public static final int CAPABILITY_CONNECTION_MANAGER = 1;
  public static final int CAPABILITY_EMERGENCY_CALLS_ONLY = 128;
  public static final int CAPABILITY_EMERGENCY_VIDEO_CALLING = 512;
  @SystemApi
  public static final int CAPABILITY_MULTI_USER = 32;
  public static final int CAPABILITY_PLACE_EMERGENCY_CALLS = 16;
  public static final int CAPABILITY_RTT = 4096;
  public static final int CAPABILITY_SELF_MANAGED = 2048;
  public static final int CAPABILITY_SIM_SUBSCRIPTION = 4;
  public static final int CAPABILITY_SUPPORTS_VIDEO_CALLING = 1024;
  public static final int CAPABILITY_VIDEO_CALLING = 8;
  public static final int CAPABILITY_VIDEO_CALLING_RELIES_ON_PRESENCE = 256;
  public static final Parcelable.Creator<PhoneAccount> CREATOR = new Parcelable.Creator()
  {
    public PhoneAccount createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PhoneAccount(paramAnonymousParcel, null);
    }
    
    public PhoneAccount[] newArray(int paramAnonymousInt)
    {
      return new PhoneAccount[paramAnonymousInt];
    }
  };
  public static final String EXTRA_ALWAYS_USE_VOIP_AUDIO_MODE = "android.telecom.extra.ALWAYS_USE_VOIP_AUDIO_MODE";
  public static final String EXTRA_CALL_SUBJECT_CHARACTER_ENCODING = "android.telecom.extra.CALL_SUBJECT_CHARACTER_ENCODING";
  public static final String EXTRA_CALL_SUBJECT_MAX_LENGTH = "android.telecom.extra.CALL_SUBJECT_MAX_LENGTH";
  public static final String EXTRA_LOG_SELF_MANAGED_CALLS = "android.telecom.extra.LOG_SELF_MANAGED_CALLS";
  public static final String EXTRA_PLAY_CALL_RECORDING_TONE = "android.telecom.extra.PLAY_CALL_RECORDING_TONE";
  public static final String EXTRA_SORT_ORDER = "android.telecom.extra.SORT_ORDER";
  public static final String EXTRA_SUPPORTS_HANDOVER_FROM = "android.telecom.extra.SUPPORTS_HANDOVER_FROM";
  public static final String EXTRA_SUPPORTS_HANDOVER_TO = "android.telecom.extra.SUPPORTS_HANDOVER_TO";
  public static final String EXTRA_SUPPORTS_VIDEO_CALLING_FALLBACK = "android.telecom.extra.SUPPORTS_VIDEO_CALLING_FALLBACK";
  public static final int NO_HIGHLIGHT_COLOR = 0;
  public static final int NO_ICON_TINT = 0;
  public static final int NO_RESOURCE_ID = -1;
  public static final String SCHEME_SIP = "sip";
  public static final String SCHEME_TEL = "tel";
  public static final String SCHEME_VOICEMAIL = "voicemail";
  private final PhoneAccountHandle mAccountHandle;
  private final Uri mAddress;
  private final int mCapabilities;
  private final Bundle mExtras;
  private String mGroupId;
  private final int mHighlightColor;
  private final Icon mIcon;
  private boolean mIsEnabled;
  private final CharSequence mLabel;
  private final CharSequence mShortDescription;
  private final Uri mSubscriptionAddress;
  private final int mSupportedAudioRoutes;
  private final List<String> mSupportedUriSchemes;
  
  private PhoneAccount(Parcel paramParcel)
  {
    if (paramParcel.readInt() > 0) {
      mAccountHandle = ((PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel));
    } else {
      mAccountHandle = null;
    }
    if (paramParcel.readInt() > 0) {
      mAddress = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    } else {
      mAddress = null;
    }
    if (paramParcel.readInt() > 0) {
      mSubscriptionAddress = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    } else {
      mSubscriptionAddress = null;
    }
    mCapabilities = paramParcel.readInt();
    mHighlightColor = paramParcel.readInt();
    mLabel = paramParcel.readCharSequence();
    mShortDescription = paramParcel.readCharSequence();
    mSupportedUriSchemes = Collections.unmodifiableList(paramParcel.createStringArrayList());
    if (paramParcel.readInt() > 0) {
      mIcon = ((Icon)Icon.CREATOR.createFromParcel(paramParcel));
    } else {
      mIcon = null;
    }
    int i = paramParcel.readByte();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mIsEnabled = bool;
    mExtras = paramParcel.readBundle();
    mGroupId = paramParcel.readString();
    mSupportedAudioRoutes = paramParcel.readInt();
  }
  
  private PhoneAccount(PhoneAccountHandle paramPhoneAccountHandle, Uri paramUri1, Uri paramUri2, int paramInt1, Icon paramIcon, int paramInt2, CharSequence paramCharSequence1, CharSequence paramCharSequence2, List<String> paramList, Bundle paramBundle, int paramInt3, boolean paramBoolean, String paramString)
  {
    mAccountHandle = paramPhoneAccountHandle;
    mAddress = paramUri1;
    mSubscriptionAddress = paramUri2;
    mCapabilities = paramInt1;
    mIcon = paramIcon;
    mHighlightColor = paramInt2;
    mLabel = paramCharSequence1;
    mShortDescription = paramCharSequence2;
    mSupportedUriSchemes = Collections.unmodifiableList(paramList);
    mExtras = paramBundle;
    mSupportedAudioRoutes = paramInt3;
    mIsEnabled = paramBoolean;
    mGroupId = paramString;
  }
  
  private String audioRoutesToString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (hasAudioRoutes(2)) {
      localStringBuilder.append("B");
    }
    if (hasAudioRoutes(1)) {
      localStringBuilder.append("E");
    }
    if (hasAudioRoutes(8)) {
      localStringBuilder.append("S");
    }
    if (hasAudioRoutes(4)) {
      localStringBuilder.append("W");
    }
    return localStringBuilder.toString();
  }
  
  public static Builder builder(PhoneAccountHandle paramPhoneAccountHandle, CharSequence paramCharSequence)
  {
    return new Builder(paramPhoneAccountHandle, paramCharSequence);
  }
  
  private String capabilitiesToString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (hasCapabilities(2048)) {
      localStringBuilder.append("SelfManaged ");
    }
    if (hasCapabilities(1024)) {
      localStringBuilder.append("SuppVideo ");
    }
    if (hasCapabilities(8)) {
      localStringBuilder.append("Video ");
    }
    if (hasCapabilities(256)) {
      localStringBuilder.append("Presence ");
    }
    if (hasCapabilities(2)) {
      localStringBuilder.append("CallProvider ");
    }
    if (hasCapabilities(64)) {
      localStringBuilder.append("CallSubject ");
    }
    if (hasCapabilities(1)) {
      localStringBuilder.append("ConnectionMgr ");
    }
    if (hasCapabilities(128)) {
      localStringBuilder.append("EmergOnly ");
    }
    if (hasCapabilities(32)) {
      localStringBuilder.append("MultiUser ");
    }
    if (hasCapabilities(16)) {
      localStringBuilder.append("PlaceEmerg ");
    }
    if (hasCapabilities(512)) {
      localStringBuilder.append("EmergVideo ");
    }
    if (hasCapabilities(4)) {
      localStringBuilder.append("SimSub ");
    }
    if (hasCapabilities(4096)) {
      localStringBuilder.append("Rtt");
    }
    return localStringBuilder.toString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PhoneAccountHandle getAccountHandle()
  {
    return mAccountHandle;
  }
  
  public Uri getAddress()
  {
    return mAddress;
  }
  
  public int getCapabilities()
  {
    return mCapabilities;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public String getGroupId()
  {
    return mGroupId;
  }
  
  public int getHighlightColor()
  {
    return mHighlightColor;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  public CharSequence getShortDescription()
  {
    return mShortDescription;
  }
  
  public Uri getSubscriptionAddress()
  {
    return mSubscriptionAddress;
  }
  
  public int getSupportedAudioRoutes()
  {
    return mSupportedAudioRoutes;
  }
  
  public List<String> getSupportedUriSchemes()
  {
    return mSupportedUriSchemes;
  }
  
  public boolean hasAudioRoutes(int paramInt)
  {
    boolean bool;
    if ((mSupportedAudioRoutes & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasCapabilities(int paramInt)
  {
    boolean bool;
    if ((mCapabilities & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEnabled()
  {
    return mIsEnabled;
  }
  
  public boolean isSelfManaged()
  {
    boolean bool;
    if ((mCapabilities & 0x800) == 2048) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setIsEnabled(boolean paramBoolean)
  {
    mIsEnabled = paramBoolean;
  }
  
  public boolean supportsUriScheme(String paramString)
  {
    if ((mSupportedUriSchemes != null) && (paramString != null))
    {
      Iterator localIterator = mSupportedUriSchemes.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ((str != null) && (str.equals(paramString))) {
          return true;
        }
      }
      return false;
    }
    return false;
  }
  
  public Builder toBuilder()
  {
    return new Builder(this);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[[");
    char c1;
    char c2;
    if (mIsEnabled)
    {
      c1 = 'X';
      c2 = c1;
    }
    else
    {
      c1 = ' ';
      c2 = c1;
    }
    localStringBuilder.append(c2);
    localStringBuilder.append("] PhoneAccount: ");
    localStringBuilder.append(mAccountHandle);
    localStringBuilder.append(" Capabilities: ");
    localStringBuilder.append(capabilitiesToString());
    localStringBuilder.append(" Audio Routes: ");
    localStringBuilder.append(audioRoutesToString());
    localStringBuilder = localStringBuilder.append(" Schemes: ");
    Iterator localIterator = mSupportedUriSchemes.iterator();
    while (localIterator.hasNext())
    {
      localStringBuilder.append((String)localIterator.next());
      localStringBuilder.append(" ");
    }
    localStringBuilder.append(" Extras: ");
    localStringBuilder.append(mExtras);
    localStringBuilder.append(" GroupId: ");
    localStringBuilder.append(Log.pii(mGroupId));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mAccountHandle == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mAccountHandle.writeToParcel(paramParcel, paramInt);
    }
    if (mAddress == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mAddress.writeToParcel(paramParcel, paramInt);
    }
    if (mSubscriptionAddress == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mSubscriptionAddress.writeToParcel(paramParcel, paramInt);
    }
    paramParcel.writeInt(mCapabilities);
    paramParcel.writeInt(mHighlightColor);
    paramParcel.writeCharSequence(mLabel);
    paramParcel.writeCharSequence(mShortDescription);
    paramParcel.writeStringList(mSupportedUriSchemes);
    if (mIcon == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mIcon.writeToParcel(paramParcel, paramInt);
    }
    paramParcel.writeByte((byte)mIsEnabled);
    paramParcel.writeBundle(mExtras);
    paramParcel.writeString(mGroupId);
    paramParcel.writeInt(mSupportedAudioRoutes);
  }
  
  public static class Builder
  {
    private PhoneAccountHandle mAccountHandle;
    private Uri mAddress;
    private int mCapabilities;
    private Bundle mExtras;
    private String mGroupId = "";
    private int mHighlightColor = 0;
    private Icon mIcon;
    private boolean mIsEnabled = false;
    private CharSequence mLabel;
    private CharSequence mShortDescription;
    private Uri mSubscriptionAddress;
    private int mSupportedAudioRoutes = 15;
    private List<String> mSupportedUriSchemes = new ArrayList();
    
    public Builder(PhoneAccount paramPhoneAccount)
    {
      mAccountHandle = paramPhoneAccount.getAccountHandle();
      mAddress = paramPhoneAccount.getAddress();
      mSubscriptionAddress = paramPhoneAccount.getSubscriptionAddress();
      mCapabilities = paramPhoneAccount.getCapabilities();
      mHighlightColor = paramPhoneAccount.getHighlightColor();
      mLabel = paramPhoneAccount.getLabel();
      mShortDescription = paramPhoneAccount.getShortDescription();
      mSupportedUriSchemes.addAll(paramPhoneAccount.getSupportedUriSchemes());
      mIcon = paramPhoneAccount.getIcon();
      mIsEnabled = paramPhoneAccount.isEnabled();
      mExtras = paramPhoneAccount.getExtras();
      mGroupId = paramPhoneAccount.getGroupId();
      mSupportedAudioRoutes = paramPhoneAccount.getSupportedAudioRoutes();
    }
    
    public Builder(PhoneAccountHandle paramPhoneAccountHandle, CharSequence paramCharSequence)
    {
      mAccountHandle = paramPhoneAccountHandle;
      mLabel = paramCharSequence;
    }
    
    public Builder addSupportedUriScheme(String paramString)
    {
      if ((!TextUtils.isEmpty(paramString)) && (!mSupportedUriSchemes.contains(paramString))) {
        mSupportedUriSchemes.add(paramString);
      }
      return this;
    }
    
    public PhoneAccount build()
    {
      if (mSupportedUriSchemes.isEmpty()) {
        addSupportedUriScheme("tel");
      }
      return new PhoneAccount(mAccountHandle, mAddress, mSubscriptionAddress, mCapabilities, mIcon, mHighlightColor, mLabel, mShortDescription, mSupportedUriSchemes, mExtras, mSupportedAudioRoutes, mIsEnabled, mGroupId, null);
    }
    
    public Builder setAddress(Uri paramUri)
    {
      mAddress = paramUri;
      return this;
    }
    
    public Builder setCapabilities(int paramInt)
    {
      mCapabilities = paramInt;
      return this;
    }
    
    public Builder setExtras(Bundle paramBundle)
    {
      mExtras = paramBundle;
      return this;
    }
    
    public Builder setGroupId(String paramString)
    {
      if (paramString != null) {
        mGroupId = paramString;
      } else {
        mGroupId = "";
      }
      return this;
    }
    
    public Builder setHighlightColor(int paramInt)
    {
      mHighlightColor = paramInt;
      return this;
    }
    
    public Builder setIcon(Icon paramIcon)
    {
      mIcon = paramIcon;
      return this;
    }
    
    public Builder setIsEnabled(boolean paramBoolean)
    {
      mIsEnabled = paramBoolean;
      return this;
    }
    
    public Builder setLabel(CharSequence paramCharSequence)
    {
      mLabel = paramCharSequence;
      return this;
    }
    
    public Builder setShortDescription(CharSequence paramCharSequence)
    {
      mShortDescription = paramCharSequence;
      return this;
    }
    
    public Builder setSubscriptionAddress(Uri paramUri)
    {
      mSubscriptionAddress = paramUri;
      return this;
    }
    
    public Builder setSupportedAudioRoutes(int paramInt)
    {
      mSupportedAudioRoutes = paramInt;
      return this;
    }
    
    public Builder setSupportedUriSchemes(List<String> paramList)
    {
      mSupportedUriSchemes.clear();
      if ((paramList != null) && (!paramList.isEmpty()))
      {
        paramList = paramList.iterator();
        while (paramList.hasNext()) {
          addSupportedUriScheme((String)paramList.next());
        }
      }
      return this;
    }
  }
}
