package android.telecom.Logging;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telecom.Log;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;

public class Session
{
  public static final String CONTINUE_SUBSESSION = "CONTINUE_SUBSESSION";
  public static final String CREATE_SUBSESSION = "CREATE_SUBSESSION";
  public static final String END_SESSION = "END_SESSION";
  public static final String END_SUBSESSION = "END_SUBSESSION";
  public static final String EXTERNAL_INDICATOR = "E-";
  public static final String SESSION_SEPARATION_CHAR_CHILD = "_";
  public static final String START_EXTERNAL_SESSION = "START_EXTERNAL_SESSION";
  public static final String START_SESSION = "START_SESSION";
  public static final String SUBSESSION_SEPARATION_CHAR = "->";
  public static final String TRUNCATE_STRING = "...";
  public static final int UNDEFINED = -1;
  private int mChildCounter = 0;
  private ArrayList<Session> mChildSessions;
  private long mExecutionEndTimeMs = -1L;
  private long mExecutionStartTimeMs;
  private String mFullMethodPathCache;
  private boolean mIsCompleted = false;
  private boolean mIsExternal = false;
  private boolean mIsStartedFromActiveSession = false;
  private String mOwnerInfo;
  private Session mParentSession;
  private String mSessionId;
  private String mShortMethodName;
  
  public Session(String paramString1, String paramString2, long paramLong, boolean paramBoolean, String paramString3)
  {
    setSessionId(paramString1);
    setShortMethodName(paramString2);
    mExecutionStartTimeMs = paramLong;
    mParentSession = null;
    mChildSessions = new ArrayList(5);
    mIsStartedFromActiveSession = paramBoolean;
    mOwnerInfo = paramString3;
  }
  
  private void getFullMethodPath(StringBuilder paramStringBuilder, boolean paramBoolean)
  {
    try
    {
      if ((!TextUtils.isEmpty(mFullMethodPathCache)) && (!paramBoolean))
      {
        paramStringBuilder.append(mFullMethodPathCache);
        return;
      }
      Session localSession = getParentSession();
      boolean bool = false;
      if (localSession != null)
      {
        bool = mShortMethodName.equals(mShortMethodName) ^ true;
        localSession.getFullMethodPath(paramStringBuilder, paramBoolean);
        paramStringBuilder.append("->");
      }
      if (isExternal())
      {
        if (paramBoolean)
        {
          paramStringBuilder.append("...");
        }
        else
        {
          paramStringBuilder.append("(");
          paramStringBuilder.append(mShortMethodName);
          paramStringBuilder.append(")");
        }
      }
      else {
        paramStringBuilder.append(mShortMethodName);
      }
      if ((bool) && (!paramBoolean)) {
        mFullMethodPathCache = paramStringBuilder.toString();
      }
      return;
    }
    finally {}
  }
  
  private String getFullSessionId()
  {
    Session localSession = mParentSession;
    if (localSession == null) {
      return mSessionId;
    }
    if (Log.VERBOSE)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(localSession.getFullSessionId());
      localStringBuilder.append("_");
      localStringBuilder.append(mSessionId);
      return localStringBuilder.toString();
    }
    return localSession.getFullSessionId();
  }
  
  private boolean isSessionExternal()
  {
    if (getParentSession() == null) {
      return isExternal();
    }
    return getParentSession().isSessionExternal();
  }
  
  private void printSessionTree(int paramInt, StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append(toString());
    Iterator localIterator = mChildSessions.iterator();
    while (localIterator.hasNext())
    {
      Session localSession = (Session)localIterator.next();
      paramStringBuilder.append("\n");
      for (int i = 0; i <= paramInt; i++) {
        paramStringBuilder.append("\t");
      }
      localSession.printSessionTree(paramInt + 1, paramStringBuilder);
    }
  }
  
  public void addChild(Session paramSession)
  {
    if (paramSession != null) {
      mChildSessions.add(paramSession);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (Session)paramObject;
      if (mExecutionStartTimeMs != mExecutionStartTimeMs) {
        return false;
      }
      if (mExecutionEndTimeMs != mExecutionEndTimeMs) {
        return false;
      }
      if (mIsCompleted != mIsCompleted) {
        return false;
      }
      if (mChildCounter != mChildCounter) {
        return false;
      }
      if (mIsStartedFromActiveSession != mIsStartedFromActiveSession) {
        return false;
      }
      if (mSessionId != null ? !mSessionId.equals(mSessionId) : mSessionId != null) {
        return false;
      }
      if (mShortMethodName != null ? !mShortMethodName.equals(mShortMethodName) : mShortMethodName != null) {
        return false;
      }
      if (mParentSession != null ? !mParentSession.equals(mParentSession) : mParentSession != null) {
        return false;
      }
      if (mChildSessions != null ? !mChildSessions.equals(mChildSessions) : mChildSessions != null) {
        return false;
      }
      if (mOwnerInfo != null) {
        bool = mOwnerInfo.equals(mOwnerInfo);
      } else if (mOwnerInfo != null) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public ArrayList<Session> getChildSessions()
  {
    return mChildSessions;
  }
  
  public long getExecutionStartTimeMilliseconds()
  {
    return mExecutionStartTimeMs;
  }
  
  public String getFullMethodPath(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    getFullMethodPath(localStringBuilder, paramBoolean);
    return localStringBuilder.toString();
  }
  
  public Info getInfo()
  {
    return Info.getInfo(this);
  }
  
  public long getLocalExecutionTime()
  {
    if (mExecutionEndTimeMs == -1L) {
      return -1L;
    }
    return mExecutionEndTimeMs - mExecutionStartTimeMs;
  }
  
  public String getNextChildId()
  {
    try
    {
      int i = mChildCounter;
      mChildCounter = (i + 1);
      return String.valueOf(i);
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Session getParentSession()
  {
    return mParentSession;
  }
  
  @VisibleForTesting
  public String getSessionId()
  {
    return mSessionId;
  }
  
  public String getShortMethodName()
  {
    return mShortMethodName;
  }
  
  public int hashCode()
  {
    String str = mSessionId;
    int i = 0;
    int j;
    if (str != null) {
      j = mSessionId.hashCode();
    } else {
      j = 0;
    }
    int k;
    if (mShortMethodName != null) {
      k = mShortMethodName.hashCode();
    } else {
      k = 0;
    }
    int m = (int)(mExecutionStartTimeMs ^ mExecutionStartTimeMs >>> 32);
    int n = (int)(mExecutionEndTimeMs ^ mExecutionEndTimeMs >>> 32);
    int i1;
    if (mParentSession != null) {
      i1 = mParentSession.hashCode();
    } else {
      i1 = 0;
    }
    int i2;
    if (mChildSessions != null) {
      i2 = mChildSessions.hashCode();
    } else {
      i2 = 0;
    }
    int i3 = mIsCompleted;
    int i4 = mChildCounter;
    int i5 = mIsStartedFromActiveSession;
    if (mOwnerInfo != null) {
      i = mOwnerInfo.hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * j + k) + m) + n) + i1) + i2) + i3) + i4) + i5) + i;
  }
  
  public boolean isExternal()
  {
    return mIsExternal;
  }
  
  public boolean isSessionCompleted()
  {
    return mIsCompleted;
  }
  
  public boolean isStartedFromActiveSession()
  {
    return mIsStartedFromActiveSession;
  }
  
  public void markSessionCompleted(long paramLong)
  {
    mExecutionEndTimeMs = paramLong;
    mIsCompleted = true;
  }
  
  public String printFullSessionTree()
  {
    for (Session localSession = this; localSession.getParentSession() != null; localSession = localSession.getParentSession()) {}
    return localSession.printSessionTree();
  }
  
  public String printSessionTree()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    printSessionTree(0, localStringBuilder);
    return localStringBuilder.toString();
  }
  
  public void removeChild(Session paramSession)
  {
    if (paramSession != null) {
      mChildSessions.remove(paramSession);
    }
  }
  
  public void setExecutionStartTimeMs(long paramLong)
  {
    mExecutionStartTimeMs = paramLong;
  }
  
  public void setIsExternal(boolean paramBoolean)
  {
    mIsExternal = paramBoolean;
  }
  
  public void setParentSession(Session paramSession)
  {
    mParentSession = paramSession;
  }
  
  public void setSessionId(String paramString)
  {
    if (paramString == null) {
      mSessionId = "?";
    }
    mSessionId = paramString;
  }
  
  public void setShortMethodName(String paramString)
  {
    String str = paramString;
    if (paramString == null) {
      str = "";
    }
    mShortMethodName = str;
  }
  
  public String toString()
  {
    if ((mParentSession != null) && (mIsStartedFromActiveSession)) {
      return mParentSession.toString();
    }
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append(getFullMethodPath(false));
    if ((mOwnerInfo != null) && (!mOwnerInfo.isEmpty()))
    {
      localStringBuilder1.append("(InCall package: ");
      localStringBuilder1.append(mOwnerInfo);
      localStringBuilder1.append(")");
    }
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(localStringBuilder1.toString());
    localStringBuilder2.append("@");
    localStringBuilder2.append(getFullSessionId());
    return localStringBuilder2.toString();
  }
  
  public static class Info
    implements Parcelable
  {
    public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator()
    {
      public Session.Info createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Session.Info(paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), null);
      }
      
      public Session.Info[] newArray(int paramAnonymousInt)
      {
        return new Session.Info[paramAnonymousInt];
      }
    };
    public final String methodPath;
    public final String sessionId;
    
    private Info(String paramString1, String paramString2)
    {
      sessionId = paramString1;
      methodPath = paramString2;
    }
    
    public static Info getInfo(Session paramSession)
    {
      String str = paramSession.getFullSessionId();
      boolean bool;
      if ((!Log.DEBUG) && (paramSession.isSessionExternal())) {
        bool = true;
      } else {
        bool = false;
      }
      return new Info(str, paramSession.getFullMethodPath(bool));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(sessionId);
      paramParcel.writeString(methodPath);
    }
  }
}
