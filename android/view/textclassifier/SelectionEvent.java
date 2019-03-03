package android.view.textclassifier;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import java.util.Objects;

public final class SelectionEvent
  implements Parcelable
{
  public static final int ACTION_ABANDON = 107;
  public static final int ACTION_COPY = 101;
  public static final int ACTION_CUT = 103;
  public static final int ACTION_DRAG = 106;
  public static final int ACTION_OTHER = 108;
  public static final int ACTION_OVERTYPE = 100;
  public static final int ACTION_PASTE = 102;
  public static final int ACTION_RESET = 201;
  public static final int ACTION_SELECT_ALL = 200;
  public static final int ACTION_SHARE = 104;
  public static final int ACTION_SMART_SHARE = 105;
  public static final Parcelable.Creator<SelectionEvent> CREATOR = new Parcelable.Creator()
  {
    public SelectionEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SelectionEvent(paramAnonymousParcel, null);
    }
    
    public SelectionEvent[] newArray(int paramAnonymousInt)
    {
      return new SelectionEvent[paramAnonymousInt];
    }
  };
  public static final int EVENT_AUTO_SELECTION = 5;
  public static final int EVENT_SELECTION_MODIFIED = 2;
  public static final int EVENT_SELECTION_STARTED = 1;
  public static final int EVENT_SMART_SELECTION_MULTI = 4;
  public static final int EVENT_SMART_SELECTION_SINGLE = 3;
  public static final int INVOCATION_LINK = 2;
  public static final int INVOCATION_MANUAL = 1;
  public static final int INVOCATION_UNKNOWN = 0;
  private static final String NO_SIGNATURE = "";
  private final int mAbsoluteEnd;
  private final int mAbsoluteStart;
  private long mDurationSincePreviousEvent;
  private long mDurationSinceSessionStart;
  private int mEnd;
  private final String mEntityType;
  private int mEventIndex;
  private long mEventTime;
  private int mEventType;
  private int mInvocationMethod;
  private String mPackageName = "";
  private String mResultId;
  private TextClassificationSessionId mSessionId;
  private int mSmartEnd;
  private int mSmartStart;
  private int mStart;
  private String mWidgetType = "unknown";
  private String mWidgetVersion;
  
  SelectionEvent(int paramInt1, int paramInt2, int paramInt3, String paramString1, int paramInt4, String paramString2)
  {
    boolean bool;
    if (paramInt2 >= paramInt1) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "end cannot be less than start");
    mAbsoluteStart = paramInt1;
    mAbsoluteEnd = paramInt2;
    mEventType = paramInt3;
    mEntityType = ((String)Preconditions.checkNotNull(paramString1));
    mResultId = paramString2;
    mInvocationMethod = paramInt4;
  }
  
  private SelectionEvent(Parcel paramParcel)
  {
    mAbsoluteStart = paramParcel.readInt();
    mAbsoluteEnd = paramParcel.readInt();
    mEventType = paramParcel.readInt();
    mEntityType = paramParcel.readString();
    int i = paramParcel.readInt();
    Object localObject1 = null;
    if (i > 0) {
      localObject2 = paramParcel.readString();
    } else {
      localObject2 = null;
    }
    mWidgetVersion = ((String)localObject2);
    mPackageName = paramParcel.readString();
    mWidgetType = paramParcel.readString();
    mInvocationMethod = paramParcel.readInt();
    mResultId = paramParcel.readString();
    mEventTime = paramParcel.readLong();
    mDurationSinceSessionStart = paramParcel.readLong();
    mDurationSincePreviousEvent = paramParcel.readLong();
    mEventIndex = paramParcel.readInt();
    Object localObject2 = localObject1;
    if (paramParcel.readInt() > 0) {
      localObject2 = (TextClassificationSessionId)TextClassificationSessionId.CREATOR.createFromParcel(paramParcel);
    }
    mSessionId = ((TextClassificationSessionId)localObject2);
    mStart = paramParcel.readInt();
    mEnd = paramParcel.readInt();
    mSmartStart = paramParcel.readInt();
    mSmartEnd = paramParcel.readInt();
  }
  
  private static void checkActionType(int paramInt)
    throws IllegalArgumentException
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        throw new IllegalArgumentException(String.format(Locale.US, "%d is not an eventType", new Object[] { Integer.valueOf(paramInt) }));
      }
      break;
    }
  }
  
  public static SelectionEvent createSelectionActionEvent(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool;
    if (paramInt2 >= paramInt1) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "end cannot be less than start");
    checkActionType(paramInt3);
    return new SelectionEvent(paramInt1, paramInt2, paramInt3, "", 0, "");
  }
  
  public static SelectionEvent createSelectionActionEvent(int paramInt1, int paramInt2, int paramInt3, TextClassification paramTextClassification)
  {
    boolean bool;
    if (paramInt2 >= paramInt1) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "end cannot be less than start");
    Preconditions.checkNotNull(paramTextClassification);
    checkActionType(paramInt3);
    if (paramTextClassification.getEntityCount() > 0) {}
    for (String str = paramTextClassification.getEntity(0);; str = "") {
      break;
    }
    return new SelectionEvent(paramInt1, paramInt2, paramInt3, str, 0, paramTextClassification.getId());
  }
  
  public static SelectionEvent createSelectionModifiedEvent(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (paramInt2 >= paramInt1) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "end cannot be less than start");
    return new SelectionEvent(paramInt1, paramInt2, 2, "", 0, "");
  }
  
  public static SelectionEvent createSelectionModifiedEvent(int paramInt1, int paramInt2, TextClassification paramTextClassification)
  {
    boolean bool;
    if (paramInt2 >= paramInt1) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "end cannot be less than start");
    Preconditions.checkNotNull(paramTextClassification);
    if (paramTextClassification.getEntityCount() > 0) {}
    for (String str = paramTextClassification.getEntity(0);; str = "") {
      break;
    }
    return new SelectionEvent(paramInt1, paramInt2, 2, str, 0, paramTextClassification.getId());
  }
  
  public static SelectionEvent createSelectionModifiedEvent(int paramInt1, int paramInt2, TextSelection paramTextSelection)
  {
    boolean bool;
    if (paramInt2 >= paramInt1) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "end cannot be less than start");
    Preconditions.checkNotNull(paramTextSelection);
    if (paramTextSelection.getEntityCount() > 0) {}
    for (String str = paramTextSelection.getEntity(0);; str = "") {
      break;
    }
    return new SelectionEvent(paramInt1, paramInt2, 5, str, 0, paramTextSelection.getId());
  }
  
  public static SelectionEvent createSelectionStartedEvent(int paramInt1, int paramInt2)
  {
    return new SelectionEvent(paramInt2, paramInt2 + 1, 1, "", paramInt1, "");
  }
  
  public static boolean isTerminal(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    }
    return true;
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
    if (!(paramObject instanceof SelectionEvent)) {
      return false;
    }
    paramObject = (SelectionEvent)paramObject;
    if ((mAbsoluteStart != mAbsoluteStart) || (mAbsoluteEnd != mAbsoluteEnd) || (mEventType != mEventType) || (!Objects.equals(mEntityType, mEntityType)) || (!Objects.equals(mWidgetVersion, mWidgetVersion)) || (!Objects.equals(mPackageName, mPackageName)) || (!Objects.equals(mWidgetType, mWidgetType)) || (mInvocationMethod != mInvocationMethod) || (!Objects.equals(mResultId, mResultId)) || (mEventTime != mEventTime) || (mDurationSinceSessionStart != mDurationSinceSessionStart) || (mDurationSincePreviousEvent != mDurationSincePreviousEvent) || (mEventIndex != mEventIndex) || (!Objects.equals(mSessionId, mSessionId)) || (mStart != mStart) || (mEnd != mEnd) || (mSmartStart != mSmartStart) || (mSmartEnd != mSmartEnd)) {
      bool = false;
    }
    return bool;
  }
  
  int getAbsoluteEnd()
  {
    return mAbsoluteEnd;
  }
  
  int getAbsoluteStart()
  {
    return mAbsoluteStart;
  }
  
  public long getDurationSincePreviousEvent()
  {
    return mDurationSincePreviousEvent;
  }
  
  public long getDurationSinceSessionStart()
  {
    return mDurationSinceSessionStart;
  }
  
  public int getEnd()
  {
    return mEnd;
  }
  
  public String getEntityType()
  {
    return mEntityType;
  }
  
  public int getEventIndex()
  {
    return mEventIndex;
  }
  
  public long getEventTime()
  {
    return mEventTime;
  }
  
  public int getEventType()
  {
    return mEventType;
  }
  
  public int getInvocationMethod()
  {
    return mInvocationMethod;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public String getResultId()
  {
    return mResultId;
  }
  
  public TextClassificationSessionId getSessionId()
  {
    return mSessionId;
  }
  
  public int getSmartEnd()
  {
    return mSmartEnd;
  }
  
  public int getSmartStart()
  {
    return mSmartStart;
  }
  
  public int getStart()
  {
    return mStart;
  }
  
  public String getWidgetType()
  {
    return mWidgetType;
  }
  
  public String getWidgetVersion()
  {
    return mWidgetVersion;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mAbsoluteStart), Integer.valueOf(mAbsoluteEnd), Integer.valueOf(mEventType), mEntityType, mWidgetVersion, mPackageName, mWidgetType, Integer.valueOf(mInvocationMethod), mResultId, Long.valueOf(mEventTime), Long.valueOf(mDurationSinceSessionStart), Long.valueOf(mDurationSincePreviousEvent), Integer.valueOf(mEventIndex), mSessionId, Integer.valueOf(mStart), Integer.valueOf(mEnd), Integer.valueOf(mSmartStart), Integer.valueOf(mSmartEnd) });
  }
  
  boolean isTerminal()
  {
    return isTerminal(mEventType);
  }
  
  SelectionEvent setDurationSincePreviousEvent(long paramLong)
  {
    mDurationSincePreviousEvent = paramLong;
    return this;
  }
  
  SelectionEvent setDurationSinceSessionStart(long paramLong)
  {
    mDurationSinceSessionStart = paramLong;
    return this;
  }
  
  SelectionEvent setEnd(int paramInt)
  {
    mEnd = paramInt;
    return this;
  }
  
  SelectionEvent setEventIndex(int paramInt)
  {
    mEventIndex = paramInt;
    return this;
  }
  
  SelectionEvent setEventTime(long paramLong)
  {
    mEventTime = paramLong;
    return this;
  }
  
  void setEventType(int paramInt)
  {
    mEventType = paramInt;
  }
  
  void setInvocationMethod(int paramInt)
  {
    mInvocationMethod = paramInt;
  }
  
  SelectionEvent setResultId(String paramString)
  {
    mResultId = paramString;
    return this;
  }
  
  SelectionEvent setSessionId(TextClassificationSessionId paramTextClassificationSessionId)
  {
    mSessionId = paramTextClassificationSessionId;
    return this;
  }
  
  SelectionEvent setSmartEnd(int paramInt)
  {
    mSmartEnd = paramInt;
    return this;
  }
  
  SelectionEvent setSmartStart(int paramInt)
  {
    mSmartStart = paramInt;
    return this;
  }
  
  SelectionEvent setStart(int paramInt)
  {
    mStart = paramInt;
    return this;
  }
  
  void setTextClassificationSessionContext(TextClassificationContext paramTextClassificationContext)
  {
    mPackageName = paramTextClassificationContext.getPackageName();
    mWidgetType = paramTextClassificationContext.getWidgetType();
    mWidgetVersion = paramTextClassificationContext.getWidgetVersion();
  }
  
  public String toString()
  {
    return String.format(Locale.US, "SelectionEvent {absoluteStart=%d, absoluteEnd=%d, eventType=%d, entityType=%s, widgetVersion=%s, packageName=%s, widgetType=%s, invocationMethod=%s, resultId=%s, eventTime=%d, durationSinceSessionStart=%d, durationSincePreviousEvent=%d, eventIndex=%d,sessionId=%s, start=%d, end=%d, smartStart=%d, smartEnd=%d}", new Object[] { Integer.valueOf(mAbsoluteStart), Integer.valueOf(mAbsoluteEnd), Integer.valueOf(mEventType), mEntityType, mWidgetVersion, mPackageName, mWidgetType, Integer.valueOf(mInvocationMethod), mResultId, Long.valueOf(mEventTime), Long.valueOf(mDurationSinceSessionStart), Long.valueOf(mDurationSincePreviousEvent), Integer.valueOf(mEventIndex), mSessionId, Integer.valueOf(mStart), Integer.valueOf(mEnd), Integer.valueOf(mSmartStart), Integer.valueOf(mSmartEnd) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAbsoluteStart);
    paramParcel.writeInt(mAbsoluteEnd);
    paramParcel.writeInt(mEventType);
    paramParcel.writeString(mEntityType);
    String str = mWidgetVersion;
    int i = 0;
    if (str != null) {
      j = 1;
    } else {
      j = 0;
    }
    paramParcel.writeInt(j);
    if (mWidgetVersion != null) {
      paramParcel.writeString(mWidgetVersion);
    }
    paramParcel.writeString(mPackageName);
    paramParcel.writeString(mWidgetType);
    paramParcel.writeInt(mInvocationMethod);
    paramParcel.writeString(mResultId);
    paramParcel.writeLong(mEventTime);
    paramParcel.writeLong(mDurationSinceSessionStart);
    paramParcel.writeLong(mDurationSincePreviousEvent);
    paramParcel.writeInt(mEventIndex);
    int j = i;
    if (mSessionId != null) {
      j = 1;
    }
    paramParcel.writeInt(j);
    if (mSessionId != null) {
      mSessionId.writeToParcel(paramParcel, paramInt);
    }
    paramParcel.writeInt(mStart);
    paramParcel.writeInt(mEnd);
    paramParcel.writeInt(mSmartStart);
    paramParcel.writeInt(mSmartEnd);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ActionType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EventType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface InvocationMethod {}
}
