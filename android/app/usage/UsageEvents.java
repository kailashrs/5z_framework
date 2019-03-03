package android.app.usage;

import android.annotation.SystemApi;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

public final class UsageEvents
  implements Parcelable
{
  public static final Parcelable.Creator<UsageEvents> CREATOR = new Parcelable.Creator()
  {
    public UsageEvents createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UsageEvents(paramAnonymousParcel);
    }
    
    public UsageEvents[] newArray(int paramAnonymousInt)
    {
      return new UsageEvents[paramAnonymousInt];
    }
  };
  public static final String INSTANT_APP_CLASS_NAME = "android.instant_class";
  public static final String INSTANT_APP_PACKAGE_NAME = "android.instant_app";
  private final int mEventCount;
  private List<Event> mEventsToWrite = null;
  private int mIndex = 0;
  private Parcel mParcel = null;
  private String[] mStringPool;
  
  UsageEvents()
  {
    mEventCount = 0;
  }
  
  public UsageEvents(Parcel paramParcel)
  {
    byte[] arrayOfByte = paramParcel.readBlob();
    paramParcel = Parcel.obtain();
    paramParcel.unmarshall(arrayOfByte, 0, arrayOfByte.length);
    paramParcel.setDataPosition(0);
    mEventCount = paramParcel.readInt();
    mIndex = paramParcel.readInt();
    if (mEventCount > 0)
    {
      mStringPool = paramParcel.createStringArray();
      int i = paramParcel.readInt();
      int j = paramParcel.readInt();
      mParcel = Parcel.obtain();
      mParcel.setDataPosition(0);
      mParcel.appendFrom(paramParcel, paramParcel.dataPosition(), i);
      mParcel.setDataSize(mParcel.dataPosition());
      mParcel.setDataPosition(j);
    }
  }
  
  public UsageEvents(List<Event> paramList, String[] paramArrayOfString)
  {
    mStringPool = paramArrayOfString;
    mEventCount = paramList.size();
    mEventsToWrite = paramList;
  }
  
  private int findStringIndex(String paramString)
  {
    int i = Arrays.binarySearch(mStringPool, paramString);
    if (i >= 0) {
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("String '");
    localStringBuilder.append(paramString);
    localStringBuilder.append("' is not in the string pool");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private void readEventFromParcel(Parcel paramParcel, Event paramEvent)
  {
    int i = paramParcel.readInt();
    if (i >= 0) {
      mPackage = mStringPool[i];
    } else {
      mPackage = null;
    }
    i = paramParcel.readInt();
    if (i >= 0) {
      mClass = mStringPool[i];
    } else {
      mClass = null;
    }
    mEventType = paramParcel.readInt();
    mTimeStamp = paramParcel.readLong();
    mConfiguration = null;
    mShortcutId = null;
    mAction = null;
    mContentType = null;
    mContentAnnotations = null;
    mNotificationChannelId = null;
    switch (mEventType)
    {
    case 6: 
    case 7: 
    case 10: 
    default: 
      break;
    case 12: 
      mNotificationChannelId = paramParcel.readString();
      break;
    case 11: 
      mBucketAndReason = paramParcel.readInt();
      break;
    case 9: 
      mAction = paramParcel.readString();
      mContentType = paramParcel.readString();
      mContentAnnotations = paramParcel.createStringArray();
      break;
    case 8: 
      mShortcutId = paramParcel.readString();
      break;
    case 5: 
      mConfiguration = ((Configuration)Configuration.CREATOR.createFromParcel(paramParcel));
    }
  }
  
  private void writeEventToParcel(Event paramEvent, Parcel paramParcel, int paramInt)
  {
    String str = mPackage;
    int i = -1;
    int j;
    if (str != null) {
      j = findStringIndex(mPackage);
    } else {
      j = -1;
    }
    if (mClass != null) {
      i = findStringIndex(mClass);
    }
    paramParcel.writeInt(j);
    paramParcel.writeInt(i);
    paramParcel.writeInt(mEventType);
    paramParcel.writeLong(mTimeStamp);
    switch (mEventType)
    {
    case 6: 
    case 7: 
    case 10: 
    default: 
      break;
    case 12: 
      paramParcel.writeString(mNotificationChannelId);
      break;
    case 11: 
      paramParcel.writeInt(mBucketAndReason);
      break;
    case 9: 
      paramParcel.writeString(mAction);
      paramParcel.writeString(mContentType);
      paramParcel.writeStringArray(mContentAnnotations);
      break;
    case 8: 
      paramParcel.writeString(mShortcutId);
      break;
    case 5: 
      mConfiguration.writeToParcel(paramParcel, paramInt);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean getNextEvent(Event paramEvent)
  {
    if (mIndex >= mEventCount) {
      return false;
    }
    readEventFromParcel(mParcel, paramEvent);
    mIndex += 1;
    if (mIndex >= mEventCount)
    {
      mParcel.recycle();
      mParcel = null;
    }
    return true;
  }
  
  public boolean hasNextEvent()
  {
    boolean bool;
    if (mIndex < mEventCount) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void resetToStart()
  {
    mIndex = 0;
    if (mParcel != null) {
      mParcel.setDataPosition(0);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Parcel localParcel1 = Parcel.obtain();
    localParcel1.writeInt(mEventCount);
    localParcel1.writeInt(mIndex);
    if (mEventCount > 0)
    {
      localParcel1.writeStringArray(mStringPool);
      if (mEventsToWrite != null)
      {
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel2.setDataPosition(0);
          for (int i = 0; i < mEventCount; i++) {
            writeEventToParcel((Event)mEventsToWrite.get(i), localParcel2, paramInt);
          }
          paramInt = localParcel2.dataPosition();
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(0);
          localParcel1.appendFrom(localParcel2, 0, paramInt);
        }
        finally
        {
          localParcel2.recycle();
        }
      }
      if (mParcel != null)
      {
        localParcel1.writeInt(mParcel.dataSize());
        localParcel1.writeInt(mParcel.dataPosition());
        localParcel1.appendFrom(mParcel, 0, mParcel.dataSize());
      }
      else
      {
        throw new IllegalStateException("Either mParcel or mEventsToWrite must not be null");
      }
    }
    paramParcel.writeBlob(localParcel1.marshall());
  }
  
  public static final class Event
  {
    public static final int ASUS_RECORD_FIRST_LAUNCH = 100;
    public static final int ASUS_RECORD_LAUNCH = 102;
    public static final int CHOOSER_ACTION = 9;
    public static final int CONFIGURATION_CHANGE = 5;
    public static final int CONTINUE_PREVIOUS_DAY = 4;
    public static final int END_OF_DAY = 3;
    public static final int FLAG_IS_PACKAGE_INSTANT_APP = 1;
    public static final int KEYGUARD_HIDDEN = 18;
    public static final int KEYGUARD_SHOWN = 17;
    public static final int MOVE_TO_BACKGROUND = 2;
    public static final int MOVE_TO_FOREGROUND = 1;
    public static final int NONE = 0;
    @SystemApi
    public static final int NOTIFICATION_INTERRUPTION = 12;
    @SystemApi
    public static final int NOTIFICATION_SEEN = 10;
    public static final int SCREEN_INTERACTIVE = 15;
    public static final int SCREEN_NON_INTERACTIVE = 16;
    public static final int SHORTCUT_INVOCATION = 8;
    @SystemApi
    public static final int SLICE_PINNED = 14;
    @SystemApi
    public static final int SLICE_PINNED_PRIV = 13;
    public static final int STANDBY_BUCKET_CHANGED = 11;
    @SystemApi
    public static final int SYSTEM_INTERACTION = 6;
    public static final int USER_INTERACTION = 7;
    public String mAction;
    public int mBucketAndReason;
    public String mClass;
    public Configuration mConfiguration;
    public String[] mContentAnnotations;
    public String mContentType;
    public int mEventType;
    public int mFlags;
    public String mNotificationChannelId;
    public String mPackage;
    public String mShortcutId;
    public long mTimeStamp;
    
    public Event() {}
    
    public Event(Event paramEvent)
    {
      mPackage = mPackage;
      mClass = mClass;
      mTimeStamp = mTimeStamp;
      mEventType = mEventType;
      mConfiguration = mConfiguration;
      mShortcutId = mShortcutId;
      mAction = mAction;
      mContentType = mContentType;
      mContentAnnotations = mContentAnnotations;
      mFlags = mFlags;
      mBucketAndReason = mBucketAndReason;
      mNotificationChannelId = mNotificationChannelId;
    }
    
    public int getAppStandbyBucket()
    {
      return (mBucketAndReason & 0xFFFF0000) >>> 16;
    }
    
    public String getClassName()
    {
      return mClass;
    }
    
    public Configuration getConfiguration()
    {
      return mConfiguration;
    }
    
    public int getEventType()
    {
      return mEventType;
    }
    
    @SystemApi
    public String getNotificationChannelId()
    {
      return mNotificationChannelId;
    }
    
    public Event getObfuscatedIfInstantApp()
    {
      if ((mFlags & 0x1) == 0) {
        return this;
      }
      Event localEvent = new Event(this);
      mPackage = "android.instant_app";
      mClass = "android.instant_class";
      return localEvent;
    }
    
    public String getPackageName()
    {
      return mPackage;
    }
    
    public String getShortcutId()
    {
      return mShortcutId;
    }
    
    public int getStandbyBucket()
    {
      return (mBucketAndReason & 0xFFFF0000) >>> 16;
    }
    
    public int getStandbyReason()
    {
      return mBucketAndReason & 0xFFFF;
    }
    
    public long getTimeStamp()
    {
      return mTimeStamp;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface EventFlags {}
  }
}
