package android.app.admin;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.util.EventLog.Event;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Objects;

public class SecurityLog
{
  public static final int LEVEL_ERROR = 3;
  public static final int LEVEL_INFO = 1;
  public static final int LEVEL_WARNING = 2;
  private static final String PROPERTY_LOGGING_ENABLED = "persist.logd.security";
  public static final int TAG_ADB_SHELL_CMD = 210002;
  public static final int TAG_ADB_SHELL_INTERACTIVE = 210001;
  public static final int TAG_APP_PROCESS_START = 210005;
  public static final int TAG_CERT_AUTHORITY_INSTALLED = 210029;
  public static final int TAG_CERT_AUTHORITY_REMOVED = 210030;
  public static final int TAG_CERT_VALIDATION_FAILURE = 210033;
  public static final int TAG_CRYPTO_SELF_TEST_COMPLETED = 210031;
  public static final int TAG_KEYGUARD_DISABLED_FEATURES_SET = 210021;
  public static final int TAG_KEYGUARD_DISMISSED = 210006;
  public static final int TAG_KEYGUARD_DISMISS_AUTH_ATTEMPT = 210007;
  public static final int TAG_KEYGUARD_SECURED = 210008;
  public static final int TAG_KEY_DESTRUCTION = 210026;
  public static final int TAG_KEY_GENERATED = 210024;
  public static final int TAG_KEY_IMPORT = 210025;
  public static final int TAG_KEY_INTEGRITY_VIOLATION = 210032;
  public static final int TAG_LOGGING_STARTED = 210011;
  public static final int TAG_LOGGING_STOPPED = 210012;
  public static final int TAG_LOG_BUFFER_SIZE_CRITICAL = 210015;
  public static final int TAG_MAX_PASSWORD_ATTEMPTS_SET = 210020;
  public static final int TAG_MAX_SCREEN_LOCK_TIMEOUT_SET = 210019;
  public static final int TAG_MEDIA_MOUNT = 210013;
  public static final int TAG_MEDIA_UNMOUNT = 210014;
  public static final int TAG_OS_SHUTDOWN = 210010;
  public static final int TAG_OS_STARTUP = 210009;
  public static final int TAG_PASSWORD_COMPLEXITY_SET = 210017;
  public static final int TAG_PASSWORD_EXPIRATION_SET = 210016;
  public static final int TAG_PASSWORD_HISTORY_LENGTH_SET = 210018;
  public static final int TAG_REMOTE_LOCK = 210022;
  public static final int TAG_SYNC_RECV_FILE = 210003;
  public static final int TAG_SYNC_SEND_FILE = 210004;
  public static final int TAG_USER_RESTRICTION_ADDED = 210027;
  public static final int TAG_USER_RESTRICTION_REMOVED = 210028;
  public static final int TAG_WIPE_FAILURE = 210023;
  
  public SecurityLog() {}
  
  public static boolean getLoggingEnabledProperty()
  {
    return SystemProperties.getBoolean("persist.logd.security", false);
  }
  
  public static native boolean isLoggingEnabled();
  
  public static native void readEvents(Collection<SecurityEvent> paramCollection)
    throws IOException;
  
  public static native void readEventsOnWrapping(long paramLong, Collection<SecurityEvent> paramCollection)
    throws IOException;
  
  public static native void readEventsSince(long paramLong, Collection<SecurityEvent> paramCollection)
    throws IOException;
  
  public static native void readPreviousEvents(Collection<SecurityEvent> paramCollection)
    throws IOException;
  
  public static void setLoggingEnabledProperty(boolean paramBoolean)
  {
    String str;
    if (paramBoolean) {
      str = "true";
    } else {
      str = "false";
    }
    SystemProperties.set("persist.logd.security", str);
  }
  
  public static native int writeEvent(int paramInt, String paramString);
  
  public static native int writeEvent(int paramInt, Object... paramVarArgs);
  
  public static final class SecurityEvent
    implements Parcelable
  {
    public static final Parcelable.Creator<SecurityEvent> CREATOR = new Parcelable.Creator()
    {
      public SecurityLog.SecurityEvent createFromParcel(Parcel paramAnonymousParcel)
      {
        return new SecurityLog.SecurityEvent(paramAnonymousParcel);
      }
      
      public SecurityLog.SecurityEvent[] newArray(int paramAnonymousInt)
      {
        return new SecurityLog.SecurityEvent[paramAnonymousInt];
      }
    };
    private EventLog.Event mEvent;
    private long mId;
    
    public SecurityEvent(long paramLong, byte[] paramArrayOfByte)
    {
      mId = paramLong;
      mEvent = EventLog.Event.fromBytes(paramArrayOfByte);
    }
    
    SecurityEvent(Parcel paramParcel)
    {
      this(paramParcel.readLong(), paramParcel.createByteArray());
    }
    
    SecurityEvent(byte[] paramArrayOfByte)
    {
      this(0L, paramArrayOfByte);
    }
    
    private boolean getSuccess()
    {
      Object localObject = getData();
      boolean bool1 = false;
      if ((localObject != null) && ((localObject instanceof Object[])))
      {
        localObject = (Object[])localObject;
        boolean bool2 = bool1;
        if (localObject.length >= 1)
        {
          bool2 = bool1;
          if ((localObject[0] instanceof Integer))
          {
            bool2 = bool1;
            if (((Integer)localObject[0]).intValue() != 0) {
              bool2 = true;
            }
          }
        }
        return bool2;
      }
      return false;
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
        paramObject = (SecurityEvent)paramObject;
        if ((!mEvent.equals(mEvent)) || (mId != mId)) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public Object getData()
    {
      return mEvent.getData();
    }
    
    public long getId()
    {
      return mId;
    }
    
    public int getLogLevel()
    {
      int i = mEvent.getTag();
      int j = 3;
      int k = 2;
      switch (i)
      {
      case 210021: 
      case 210022: 
      default: 
        return 1;
      case 210033: 
        return 2;
      case 210030: 
      case 210031: 
        k = j;
        if (getSuccess()) {
          k = 1;
        }
        return k;
      case 210015: 
      case 210023: 
      case 210032: 
        return 3;
      case 210007: 
      case 210024: 
      case 210025: 
      case 210026: 
      case 210029: 
        if (getSuccess()) {
          k = 1;
        }
        return k;
      }
      return 1;
    }
    
    public int getTag()
    {
      return mEvent.getTag();
    }
    
    public long getTimeNanos()
    {
      return mEvent.getTimeNanos();
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mEvent, Long.valueOf(mId) });
    }
    
    public void setId(long paramLong)
    {
      mId = paramLong;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(mId);
      paramParcel.writeByteArray(mEvent.getBytes());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SecurityLogLevel {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SecurityLogTag {}
}
