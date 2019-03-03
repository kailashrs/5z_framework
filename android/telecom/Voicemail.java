package android.telecom;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Voicemail
  implements Parcelable
{
  public static final Parcelable.Creator<Voicemail> CREATOR = new Parcelable.Creator()
  {
    public Voicemail createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Voicemail(paramAnonymousParcel, null);
    }
    
    public Voicemail[] newArray(int paramAnonymousInt)
    {
      return new Voicemail[paramAnonymousInt];
    }
  };
  private final Long mDuration;
  private final Boolean mHasContent;
  private final Long mId;
  private final Boolean mIsRead;
  private final String mNumber;
  private final PhoneAccountHandle mPhoneAccount;
  private final String mProviderData;
  private final String mSource;
  private final Long mTimestamp;
  private final String mTranscription;
  private final Uri mUri;
  
  private Voicemail(Parcel paramParcel)
  {
    mTimestamp = Long.valueOf(paramParcel.readLong());
    mNumber = ((String)paramParcel.readCharSequence());
    if (paramParcel.readInt() > 0) {
      mPhoneAccount = ((PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel));
    } else {
      mPhoneAccount = null;
    }
    mId = Long.valueOf(paramParcel.readLong());
    mDuration = Long.valueOf(paramParcel.readLong());
    mSource = ((String)paramParcel.readCharSequence());
    mProviderData = ((String)paramParcel.readCharSequence());
    if (paramParcel.readInt() > 0) {
      mUri = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    } else {
      mUri = null;
    }
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsRead = Boolean.valueOf(bool2);
    boolean bool2 = bool1;
    if (paramParcel.readInt() > 0) {
      bool2 = true;
    }
    mHasContent = Boolean.valueOf(bool2);
    mTranscription = ((String)paramParcel.readCharSequence());
  }
  
  private Voicemail(Long paramLong1, String paramString1, PhoneAccountHandle paramPhoneAccountHandle, Long paramLong2, Long paramLong3, String paramString2, String paramString3, Uri paramUri, Boolean paramBoolean1, Boolean paramBoolean2, String paramString4)
  {
    mTimestamp = paramLong1;
    mNumber = paramString1;
    mPhoneAccount = paramPhoneAccountHandle;
    mId = paramLong2;
    mDuration = paramLong3;
    mSource = paramString2;
    mProviderData = paramString3;
    mUri = paramUri;
    mIsRead = paramBoolean1;
    mHasContent = paramBoolean2;
    mTranscription = paramString4;
  }
  
  public static Builder createForInsertion(long paramLong, String paramString)
  {
    return new Builder(null).setNumber(paramString).setTimestamp(paramLong);
  }
  
  public static Builder createForUpdate(long paramLong, String paramString)
  {
    return new Builder(null).setId(paramLong).setSourceData(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getDuration()
  {
    return mDuration.longValue();
  }
  
  public long getId()
  {
    return mId.longValue();
  }
  
  public String getNumber()
  {
    return mNumber;
  }
  
  public PhoneAccountHandle getPhoneAccount()
  {
    return mPhoneAccount;
  }
  
  public String getSourceData()
  {
    return mProviderData;
  }
  
  public String getSourcePackage()
  {
    return mSource;
  }
  
  public long getTimestampMillis()
  {
    return mTimestamp.longValue();
  }
  
  public String getTranscription()
  {
    return mTranscription;
  }
  
  public Uri getUri()
  {
    return mUri;
  }
  
  public boolean hasContent()
  {
    return mHasContent.booleanValue();
  }
  
  public boolean isRead()
  {
    return mIsRead.booleanValue();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mTimestamp.longValue());
    paramParcel.writeCharSequence(mNumber);
    if (mPhoneAccount == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mPhoneAccount.writeToParcel(paramParcel, paramInt);
    }
    paramParcel.writeLong(mId.longValue());
    paramParcel.writeLong(mDuration.longValue());
    paramParcel.writeCharSequence(mSource);
    paramParcel.writeCharSequence(mProviderData);
    if (mUri == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mUri.writeToParcel(paramParcel, paramInt);
    }
    if (mIsRead.booleanValue()) {
      paramParcel.writeInt(1);
    } else {
      paramParcel.writeInt(0);
    }
    if (mHasContent.booleanValue()) {
      paramParcel.writeInt(1);
    } else {
      paramParcel.writeInt(0);
    }
    paramParcel.writeCharSequence(mTranscription);
  }
  
  public static class Builder
  {
    private Long mBuilderDuration;
    private boolean mBuilderHasContent;
    private Long mBuilderId;
    private Boolean mBuilderIsRead;
    private String mBuilderNumber;
    private PhoneAccountHandle mBuilderPhoneAccount;
    private String mBuilderSourceData;
    private String mBuilderSourcePackage;
    private Long mBuilderTimestamp;
    private String mBuilderTranscription;
    private Uri mBuilderUri;
    
    private Builder() {}
    
    public Voicemail build()
    {
      long l1;
      if (mBuilderId == null) {
        l1 = -1L;
      } else {
        l1 = mBuilderId.longValue();
      }
      mBuilderId = Long.valueOf(l1);
      Long localLong = mBuilderTimestamp;
      long l2 = 0L;
      if (localLong == null) {
        l1 = 0L;
      } else {
        l1 = mBuilderTimestamp.longValue();
      }
      mBuilderTimestamp = Long.valueOf(l1);
      if (mBuilderDuration == null) {
        l1 = l2;
      } else {
        l1 = mBuilderDuration.longValue();
      }
      mBuilderDuration = Long.valueOf(l1);
      boolean bool;
      if (mBuilderIsRead == null) {
        bool = false;
      } else {
        bool = mBuilderIsRead.booleanValue();
      }
      mBuilderIsRead = Boolean.valueOf(bool);
      return new Voicemail(mBuilderTimestamp, mBuilderNumber, mBuilderPhoneAccount, mBuilderId, mBuilderDuration, mBuilderSourcePackage, mBuilderSourceData, mBuilderUri, mBuilderIsRead, Boolean.valueOf(mBuilderHasContent), mBuilderTranscription, null);
    }
    
    public Builder setDuration(long paramLong)
    {
      mBuilderDuration = Long.valueOf(paramLong);
      return this;
    }
    
    public Builder setHasContent(boolean paramBoolean)
    {
      mBuilderHasContent = paramBoolean;
      return this;
    }
    
    public Builder setId(long paramLong)
    {
      mBuilderId = Long.valueOf(paramLong);
      return this;
    }
    
    public Builder setIsRead(boolean paramBoolean)
    {
      mBuilderIsRead = Boolean.valueOf(paramBoolean);
      return this;
    }
    
    public Builder setNumber(String paramString)
    {
      mBuilderNumber = paramString;
      return this;
    }
    
    public Builder setPhoneAccount(PhoneAccountHandle paramPhoneAccountHandle)
    {
      mBuilderPhoneAccount = paramPhoneAccountHandle;
      return this;
    }
    
    public Builder setSourceData(String paramString)
    {
      mBuilderSourceData = paramString;
      return this;
    }
    
    public Builder setSourcePackage(String paramString)
    {
      mBuilderSourcePackage = paramString;
      return this;
    }
    
    public Builder setTimestamp(long paramLong)
    {
      mBuilderTimestamp = Long.valueOf(paramLong);
      return this;
    }
    
    public Builder setTranscription(String paramString)
    {
      mBuilderTranscription = paramString;
      return this;
    }
    
    public Builder setUri(Uri paramUri)
    {
      mBuilderUri = paramUri;
      return this;
    }
  }
}
