package android.app.admin;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public final class SystemUpdateInfo
  implements Parcelable
{
  private static final String ATTR_ORIGINAL_BUILD = "original-build";
  private static final String ATTR_RECEIVED_TIME = "received-time";
  private static final String ATTR_SECURITY_PATCH_STATE = "security-patch-state";
  public static final Parcelable.Creator<SystemUpdateInfo> CREATOR = new Parcelable.Creator()
  {
    public SystemUpdateInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SystemUpdateInfo(paramAnonymousParcel, null);
    }
    
    public SystemUpdateInfo[] newArray(int paramAnonymousInt)
    {
      return new SystemUpdateInfo[paramAnonymousInt];
    }
  };
  public static final int SECURITY_PATCH_STATE_FALSE = 1;
  public static final int SECURITY_PATCH_STATE_TRUE = 2;
  public static final int SECURITY_PATCH_STATE_UNKNOWN = 0;
  private final long mReceivedTime;
  private final int mSecurityPatchState;
  
  private SystemUpdateInfo(long paramLong, int paramInt)
  {
    mReceivedTime = paramLong;
    mSecurityPatchState = paramInt;
  }
  
  private SystemUpdateInfo(Parcel paramParcel)
  {
    mReceivedTime = paramParcel.readLong();
    mSecurityPatchState = paramParcel.readInt();
  }
  
  public static SystemUpdateInfo of(long paramLong)
  {
    SystemUpdateInfo localSystemUpdateInfo;
    if (paramLong == -1L) {
      localSystemUpdateInfo = null;
    } else {
      localSystemUpdateInfo = new SystemUpdateInfo(paramLong, 0);
    }
    return localSystemUpdateInfo;
  }
  
  public static SystemUpdateInfo of(long paramLong, boolean paramBoolean)
  {
    SystemUpdateInfo localSystemUpdateInfo;
    if (paramLong == -1L)
    {
      localSystemUpdateInfo = null;
    }
    else
    {
      int i;
      if (paramBoolean) {
        i = 2;
      } else {
        i = 1;
      }
      localSystemUpdateInfo = new SystemUpdateInfo(paramLong, i);
    }
    return localSystemUpdateInfo;
  }
  
  public static SystemUpdateInfo readFromXml(XmlPullParser paramXmlPullParser)
  {
    String str = paramXmlPullParser.getAttributeValue(null, "original-build");
    if (!Build.FINGERPRINT.equals(str)) {
      return null;
    }
    return new SystemUpdateInfo(Long.parseLong(paramXmlPullParser.getAttributeValue(null, "received-time")), Integer.parseInt(paramXmlPullParser.getAttributeValue(null, "security-patch-state")));
  }
  
  private static String securityPatchStateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unrecognized security patch state: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 2: 
      return "true";
    case 1: 
      return "false";
    }
    return "unknown";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (SystemUpdateInfo)paramObject;
      if ((mReceivedTime != mReceivedTime) || (mSecurityPatchState != mSecurityPatchState)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public long getReceivedTime()
  {
    return mReceivedTime;
  }
  
  public int getSecurityPatchState()
  {
    return mSecurityPatchState;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Long.valueOf(mReceivedTime), Integer.valueOf(mSecurityPatchState) });
  }
  
  public String toString()
  {
    return String.format("SystemUpdateInfo (receivedTime = %d, securityPatchState = %s)", new Object[] { Long.valueOf(mReceivedTime), securityPatchStateToString(mSecurityPatchState) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(getReceivedTime());
    paramParcel.writeInt(getSecurityPatchState());
  }
  
  public void writeToXml(XmlSerializer paramXmlSerializer, String paramString)
    throws IOException
  {
    paramXmlSerializer.startTag(null, paramString);
    paramXmlSerializer.attribute(null, "received-time", String.valueOf(mReceivedTime));
    paramXmlSerializer.attribute(null, "security-patch-state", String.valueOf(mSecurityPatchState));
    paramXmlSerializer.attribute(null, "original-build", Build.FINGERPRINT);
    paramXmlSerializer.endTag(null, paramString);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SecurityPatchState {}
}
