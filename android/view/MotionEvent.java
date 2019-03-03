package android.view;

import android.graphics.Matrix;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;

public final class MotionEvent
  extends InputEvent
  implements Parcelable
{
  public static final int ACTION_BUTTON_PRESS = 11;
  public static final int ACTION_BUTTON_RELEASE = 12;
  public static final int ACTION_CANCEL = 3;
  public static final int ACTION_DOWN = 0;
  public static final int ACTION_HOVER_ENTER = 9;
  public static final int ACTION_HOVER_EXIT = 10;
  public static final int ACTION_HOVER_MOVE = 7;
  public static final int ACTION_MASK = 255;
  public static final int ACTION_MOVE = 2;
  public static final int ACTION_OUTSIDE = 4;
  @Deprecated
  public static final int ACTION_POINTER_1_DOWN = 5;
  @Deprecated
  public static final int ACTION_POINTER_1_UP = 6;
  @Deprecated
  public static final int ACTION_POINTER_2_DOWN = 261;
  @Deprecated
  public static final int ACTION_POINTER_2_UP = 262;
  @Deprecated
  public static final int ACTION_POINTER_3_DOWN = 517;
  @Deprecated
  public static final int ACTION_POINTER_3_UP = 518;
  public static final int ACTION_POINTER_DOWN = 5;
  @Deprecated
  public static final int ACTION_POINTER_ID_MASK = 65280;
  @Deprecated
  public static final int ACTION_POINTER_ID_SHIFT = 8;
  public static final int ACTION_POINTER_INDEX_MASK = 65280;
  public static final int ACTION_POINTER_INDEX_SHIFT = 8;
  public static final int ACTION_POINTER_UP = 6;
  public static final int ACTION_SCROLL = 8;
  public static final int ACTION_UP = 1;
  public static final int AXIS_BRAKE = 23;
  public static final int AXIS_DISTANCE = 24;
  public static final int AXIS_GAS = 22;
  public static final int AXIS_GENERIC_1 = 32;
  public static final int AXIS_GENERIC_10 = 41;
  public static final int AXIS_GENERIC_11 = 42;
  public static final int AXIS_GENERIC_12 = 43;
  public static final int AXIS_GENERIC_13 = 44;
  public static final int AXIS_GENERIC_14 = 45;
  public static final int AXIS_GENERIC_15 = 46;
  public static final int AXIS_GENERIC_16 = 47;
  public static final int AXIS_GENERIC_2 = 33;
  public static final int AXIS_GENERIC_3 = 34;
  public static final int AXIS_GENERIC_4 = 35;
  public static final int AXIS_GENERIC_5 = 36;
  public static final int AXIS_GENERIC_6 = 37;
  public static final int AXIS_GENERIC_7 = 38;
  public static final int AXIS_GENERIC_8 = 39;
  public static final int AXIS_GENERIC_9 = 40;
  public static final int AXIS_HAT_X = 15;
  public static final int AXIS_HAT_Y = 16;
  public static final int AXIS_HSCROLL = 10;
  public static final int AXIS_LTRIGGER = 17;
  public static final int AXIS_ORIENTATION = 8;
  public static final int AXIS_PRESSURE = 2;
  public static final int AXIS_RELATIVE_X = 27;
  public static final int AXIS_RELATIVE_Y = 28;
  public static final int AXIS_RTRIGGER = 18;
  public static final int AXIS_RUDDER = 20;
  public static final int AXIS_RX = 12;
  public static final int AXIS_RY = 13;
  public static final int AXIS_RZ = 14;
  public static final int AXIS_SCROLL = 26;
  public static final int AXIS_SIZE = 3;
  private static final SparseArray<String> AXIS_SYMBOLIC_NAMES = new SparseArray();
  public static final int AXIS_THROTTLE = 19;
  public static final int AXIS_TILT = 25;
  public static final int AXIS_TOOL_MAJOR = 6;
  public static final int AXIS_TOOL_MINOR = 7;
  public static final int AXIS_TOUCH_MAJOR = 4;
  public static final int AXIS_TOUCH_MINOR = 5;
  public static final int AXIS_VSCROLL = 9;
  public static final int AXIS_WHEEL = 21;
  public static final int AXIS_X = 0;
  public static final int AXIS_Y = 1;
  public static final int AXIS_Z = 11;
  public static final int BUTTON_BACK = 8;
  public static final int BUTTON_FORWARD = 16;
  public static final int BUTTON_MENU = 1073741824;
  public static final int BUTTON_PRIMARY = 1;
  public static final int BUTTON_SECONDARY = 2;
  public static final int BUTTON_STYLUS_PRIMARY = 32;
  public static final int BUTTON_STYLUS_SECONDARY = 64;
  private static final String[] BUTTON_SYMBOLIC_NAMES;
  public static final int BUTTON_TERTIARY = 4;
  public static final Parcelable.Creator<MotionEvent> CREATOR = new Parcelable.Creator()
  {
    public MotionEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel.readInt();
      return MotionEvent.createFromParcelBody(paramAnonymousParcel);
    }
    
    public MotionEvent[] newArray(int paramAnonymousInt)
    {
      return new MotionEvent[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG_CONCISE_TOSTRING = false;
  public static final int EDGE_BOTTOM = 2;
  public static final int EDGE_LEFT = 4;
  public static final int EDGE_RIGHT = 8;
  public static final int EDGE_TOP = 1;
  public static final int FLAG_HOVER_EXIT_PENDING = 4;
  public static final int FLAG_IS_GENERATED_GESTURE = 8;
  public static final int FLAG_TAINTED = Integer.MIN_VALUE;
  public static final int FLAG_TARGET_ACCESSIBILITY_FOCUS = 1073741824;
  public static final int FLAG_WINDOW_IS_OBSCURED = 1;
  public static final int FLAG_WINDOW_IS_PARTIALLY_OBSCURED = 2;
  private static final int HISTORY_CURRENT = Integer.MIN_VALUE;
  public static final int INVALID_POINTER_ID = -1;
  private static final String LABEL_PREFIX = "AXIS_";
  private static final int MAX_RECYCLED = 10;
  private static final long NS_PER_MS = 1000000L;
  public static final int TOOL_TYPE_ERASER = 4;
  public static final int TOOL_TYPE_FINGER = 1;
  public static final int TOOL_TYPE_MOUSE = 3;
  public static final int TOOL_TYPE_STYLUS = 2;
  private static final SparseArray<String> TOOL_TYPE_SYMBOLIC_NAMES;
  public static final int TOOL_TYPE_UNKNOWN = 0;
  private static final Object gRecyclerLock;
  private static MotionEvent gRecyclerTop;
  private static int gRecyclerUsed;
  private static final Object gSharedTempLock;
  private static PointerCoords[] gSharedTempPointerCoords;
  private static int[] gSharedTempPointerIndexMap;
  private static PointerProperties[] gSharedTempPointerProperties;
  private long mNativePtr;
  private MotionEvent mNext;
  
  static
  {
    SparseArray localSparseArray = AXIS_SYMBOLIC_NAMES;
    localSparseArray.append(0, "AXIS_X");
    localSparseArray.append(1, "AXIS_Y");
    localSparseArray.append(2, "AXIS_PRESSURE");
    localSparseArray.append(3, "AXIS_SIZE");
    localSparseArray.append(4, "AXIS_TOUCH_MAJOR");
    localSparseArray.append(5, "AXIS_TOUCH_MINOR");
    localSparseArray.append(6, "AXIS_TOOL_MAJOR");
    localSparseArray.append(7, "AXIS_TOOL_MINOR");
    localSparseArray.append(8, "AXIS_ORIENTATION");
    localSparseArray.append(9, "AXIS_VSCROLL");
    localSparseArray.append(10, "AXIS_HSCROLL");
    localSparseArray.append(11, "AXIS_Z");
    localSparseArray.append(12, "AXIS_RX");
    localSparseArray.append(13, "AXIS_RY");
    localSparseArray.append(14, "AXIS_RZ");
    localSparseArray.append(15, "AXIS_HAT_X");
    localSparseArray.append(16, "AXIS_HAT_Y");
    localSparseArray.append(17, "AXIS_LTRIGGER");
    localSparseArray.append(18, "AXIS_RTRIGGER");
    localSparseArray.append(19, "AXIS_THROTTLE");
    localSparseArray.append(20, "AXIS_RUDDER");
    localSparseArray.append(21, "AXIS_WHEEL");
    localSparseArray.append(22, "AXIS_GAS");
    localSparseArray.append(23, "AXIS_BRAKE");
    localSparseArray.append(24, "AXIS_DISTANCE");
    localSparseArray.append(25, "AXIS_TILT");
    localSparseArray.append(26, "AXIS_SCROLL");
    localSparseArray.append(27, "AXIS_REALTIVE_X");
    localSparseArray.append(28, "AXIS_REALTIVE_Y");
    localSparseArray.append(32, "AXIS_GENERIC_1");
    localSparseArray.append(33, "AXIS_GENERIC_2");
    localSparseArray.append(34, "AXIS_GENERIC_3");
    localSparseArray.append(35, "AXIS_GENERIC_4");
    localSparseArray.append(36, "AXIS_GENERIC_5");
    localSparseArray.append(37, "AXIS_GENERIC_6");
    localSparseArray.append(38, "AXIS_GENERIC_7");
    localSparseArray.append(39, "AXIS_GENERIC_8");
    localSparseArray.append(40, "AXIS_GENERIC_9");
    localSparseArray.append(41, "AXIS_GENERIC_10");
    localSparseArray.append(42, "AXIS_GENERIC_11");
    localSparseArray.append(43, "AXIS_GENERIC_12");
    localSparseArray.append(44, "AXIS_GENERIC_13");
    localSparseArray.append(45, "AXIS_GENERIC_14");
    localSparseArray.append(46, "AXIS_GENERIC_15");
    localSparseArray.append(47, "AXIS_GENERIC_16");
    BUTTON_SYMBOLIC_NAMES = new String[] { "BUTTON_PRIMARY", "BUTTON_SECONDARY", "BUTTON_TERTIARY", "BUTTON_BACK", "BUTTON_FORWARD", "BUTTON_STYLUS_PRIMARY", "BUTTON_STYLUS_SECONDARY", "0x00000080", "0x00000100", "0x00000200", "0x00000400", "0x00000800", "0x00001000", "0x00002000", "0x00004000", "0x00008000", "0x00010000", "0x00020000", "0x00040000", "0x00080000", "0x00100000", "0x00200000", "0x00400000", "0x00800000", "0x01000000", "0x02000000", "0x04000000", "0x08000000", "0x10000000", "0x20000000", "0x40000000", "0x80000000" };
    TOOL_TYPE_SYMBOLIC_NAMES = new SparseArray();
    localSparseArray = TOOL_TYPE_SYMBOLIC_NAMES;
    localSparseArray.append(0, "TOOL_TYPE_UNKNOWN");
    localSparseArray.append(1, "TOOL_TYPE_FINGER");
    localSparseArray.append(2, "TOOL_TYPE_STYLUS");
    localSparseArray.append(3, "TOOL_TYPE_MOUSE");
    localSparseArray.append(4, "TOOL_TYPE_ERASER");
    gRecyclerLock = new Object();
    gSharedTempLock = new Object();
  }
  
  private MotionEvent() {}
  
  public static String actionToString(int paramInt)
  {
    int i;
    switch (paramInt)
    {
    case 5: 
    case 6: 
    default: 
      i = (0xFF00 & paramInt) >> 8;
      switch (paramInt & 0xFF)
      {
      default: 
        return Integer.toString(paramInt);
      }
    case 12: 
      return "ACTION_BUTTON_RELEASE";
    case 11: 
      return "ACTION_BUTTON_PRESS";
    case 10: 
      return "ACTION_HOVER_EXIT";
    case 9: 
      return "ACTION_HOVER_ENTER";
    case 8: 
      return "ACTION_SCROLL";
    case 7: 
      return "ACTION_HOVER_MOVE";
    case 4: 
      return "ACTION_OUTSIDE";
    case 3: 
      return "ACTION_CANCEL";
    case 2: 
      return "ACTION_MOVE";
    case 1: 
      return "ACTION_UP";
    }
    return "ACTION_DOWN";
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ACTION_POINTER_UP(");
    localStringBuilder.append(i);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("ACTION_POINTER_DOWN(");
    localStringBuilder.append(i);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  private static <T> void appendUnless(T paramT1, StringBuilder paramStringBuilder, String paramString, T paramT2)
  {
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(paramT2);
  }
  
  public static int axisFromString(String paramString)
  {
    String str = paramString;
    int i;
    if (paramString.startsWith("AXIS_"))
    {
      str = paramString.substring("AXIS_".length());
      i = nativeAxisFromString(str);
      if (i >= 0) {
        return i;
      }
    }
    try
    {
      i = Integer.parseInt(str, 10);
      return i;
    }
    catch (NumberFormatException paramString) {}
    return -1;
  }
  
  public static String axisToString(int paramInt)
  {
    String str = nativeAxisToString(paramInt);
    if (str != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AXIS_");
      localStringBuilder.append(str);
      str = localStringBuilder.toString();
    }
    else
    {
      str = Integer.toString(paramInt);
    }
    return str;
  }
  
  public static String buttonStateToString(int paramInt)
  {
    if (paramInt == 0) {
      return "0";
    }
    Object localObject1 = null;
    int i = paramInt;
    paramInt = 0;
    while (i != 0)
    {
      int j;
      if ((i & 0x1) != 0) {
        j = 1;
      } else {
        j = 0;
      }
      i >>>= 1;
      Object localObject2 = localObject1;
      if (j != 0)
      {
        localObject2 = BUTTON_SYMBOLIC_NAMES[paramInt];
        if (localObject1 == null)
        {
          if (i == 0) {
            return localObject2;
          }
          localObject2 = new StringBuilder((String)localObject2);
        }
        else
        {
          localObject1.append('|');
          localObject1.append((String)localObject2);
          localObject2 = localObject1;
        }
      }
      paramInt++;
      localObject1 = localObject2;
    }
    return localObject1.toString();
  }
  
  private static final float clamp(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 < paramFloat2) {
      return paramFloat2;
    }
    if (paramFloat1 > paramFloat3) {
      return paramFloat3;
    }
    return paramFloat1;
  }
  
  public static MotionEvent createFromParcelBody(Parcel paramParcel)
  {
    MotionEvent localMotionEvent = obtain();
    mNativePtr = nativeReadFromParcel(mNativePtr, paramParcel);
    return localMotionEvent;
  }
  
  private static final void ensureSharedTempPointerCapacity(int paramInt)
  {
    if ((gSharedTempPointerCoords == null) || (gSharedTempPointerCoords.length < paramInt))
    {
      int i;
      if (gSharedTempPointerCoords != null) {
        i = gSharedTempPointerCoords.length;
      } else {
        i = 8;
      }
      while (i < paramInt) {
        i *= 2;
      }
      gSharedTempPointerCoords = PointerCoords.createArray(i);
      gSharedTempPointerProperties = PointerProperties.createArray(i);
      gSharedTempPointerIndexMap = new int[i];
    }
  }
  
  private static native void nativeAddBatch(long paramLong1, long paramLong2, PointerCoords[] paramArrayOfPointerCoords, int paramInt);
  
  private static native int nativeAxisFromString(String paramString);
  
  private static native String nativeAxisToString(int paramInt);
  
  @CriticalNative
  private static native long nativeCopy(long paramLong1, long paramLong2, boolean paramBoolean);
  
  private static native void nativeDispose(long paramLong);
  
  @CriticalNative
  private static native int nativeFindPointerIndex(long paramLong, int paramInt);
  
  @CriticalNative
  private static native int nativeGetAction(long paramLong);
  
  @CriticalNative
  private static native int nativeGetActionButton(long paramLong);
  
  @FastNative
  private static native float nativeGetAxisValue(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  @CriticalNative
  private static native int nativeGetButtonState(long paramLong);
  
  @CriticalNative
  private static native int nativeGetDeviceId(long paramLong);
  
  @CriticalNative
  private static native long nativeGetDownTimeNanos(long paramLong);
  
  @CriticalNative
  private static native int nativeGetEdgeFlags(long paramLong);
  
  @FastNative
  private static native long nativeGetEventTimeNanos(long paramLong, int paramInt);
  
  @CriticalNative
  private static native int nativeGetFlags(long paramLong);
  
  @CriticalNative
  private static native int nativeGetHistorySize(long paramLong);
  
  @CriticalNative
  private static native int nativeGetMetaState(long paramLong);
  
  private static native void nativeGetPointerCoords(long paramLong, int paramInt1, int paramInt2, PointerCoords paramPointerCoords);
  
  @CriticalNative
  private static native int nativeGetPointerCount(long paramLong);
  
  @FastNative
  private static native int nativeGetPointerId(long paramLong, int paramInt);
  
  private static native void nativeGetPointerProperties(long paramLong, int paramInt, PointerProperties paramPointerProperties);
  
  @FastNative
  private static native float nativeGetRawAxisValue(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  @CriticalNative
  private static native int nativeGetSource(long paramLong);
  
  @FastNative
  private static native int nativeGetToolType(long paramLong, int paramInt);
  
  @CriticalNative
  private static native float nativeGetXOffset(long paramLong);
  
  @CriticalNative
  private static native float nativeGetXPrecision(long paramLong);
  
  @CriticalNative
  private static native float nativeGetYOffset(long paramLong);
  
  @CriticalNative
  private static native float nativeGetYPrecision(long paramLong);
  
  private static native long nativeInitialize(long paramLong1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong2, long paramLong3, int paramInt8, PointerProperties[] paramArrayOfPointerProperties, PointerCoords[] paramArrayOfPointerCoords);
  
  @CriticalNative
  private static native boolean nativeIsTouchEvent(long paramLong);
  
  @CriticalNative
  private static native void nativeOffsetLocation(long paramLong, float paramFloat1, float paramFloat2);
  
  private static native long nativeReadFromParcel(long paramLong, Parcel paramParcel);
  
  @CriticalNative
  private static native void nativeScale(long paramLong, float paramFloat);
  
  @CriticalNative
  private static native void nativeSetAction(long paramLong, int paramInt);
  
  @CriticalNative
  private static native void nativeSetActionButton(long paramLong, int paramInt);
  
  @CriticalNative
  private static native void nativeSetButtonState(long paramLong, int paramInt);
  
  @CriticalNative
  private static native void nativeSetDownTimeNanos(long paramLong1, long paramLong2);
  
  @CriticalNative
  private static native void nativeSetEdgeFlags(long paramLong, int paramInt);
  
  @CriticalNative
  private static native void nativeSetFlags(long paramLong, int paramInt);
  
  @CriticalNative
  private static native int nativeSetSource(long paramLong, int paramInt);
  
  @CriticalNative
  private static native void nativeTransform(long paramLong1, long paramLong2);
  
  private static native void nativeWriteToParcel(long paramLong, Parcel paramParcel);
  
  private static MotionEvent obtain()
  {
    synchronized (gRecyclerLock)
    {
      MotionEvent localMotionEvent = gRecyclerTop;
      if (localMotionEvent == null)
      {
        localMotionEvent = new android/view/MotionEvent;
        localMotionEvent.<init>();
        return localMotionEvent;
      }
      gRecyclerTop = mNext;
      gRecyclerUsed -= 1;
      mNext = null;
      localMotionEvent.prepareForReuse();
      return localMotionEvent;
    }
  }
  
  public static MotionEvent obtain(long paramLong1, long paramLong2, int paramInt1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt2, float paramFloat5, float paramFloat6, int paramInt3, int paramInt4)
  {
    MotionEvent localMotionEvent = obtain();
    synchronized (gSharedTempLock)
    {
      ensureSharedTempPointerCapacity(1);
      PointerProperties[] arrayOfPointerProperties = gSharedTempPointerProperties;
      arrayOfPointerProperties[0].clear();
      0id = 0;
      PointerCoords[] arrayOfPointerCoords = gSharedTempPointerCoords;
      arrayOfPointerCoords[0].clear();
      0x = paramFloat1;
      0y = paramFloat2;
      0pressure = paramFloat3;
      0size = paramFloat4;
      mNativePtr = nativeInitialize(mNativePtr, paramInt3, 0, paramInt1, 0, paramInt4, paramInt2, 0, 0.0F, 0.0F, paramFloat5, paramFloat6, paramLong1 * 1000000L, paramLong2 * 1000000L, 1, arrayOfPointerProperties, arrayOfPointerCoords);
      return localMotionEvent;
    }
  }
  
  public static MotionEvent obtain(long paramLong1, long paramLong2, int paramInt1, float paramFloat1, float paramFloat2, int paramInt2)
  {
    return obtain(paramLong1, paramLong2, paramInt1, paramFloat1, paramFloat2, 1.0F, 1.0F, paramInt2, 1.0F, 1.0F, 0, 0);
  }
  
  @Deprecated
  public static MotionEvent obtain(long paramLong1, long paramLong2, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt3, float paramFloat5, float paramFloat6, int paramInt4, int paramInt5)
  {
    return obtain(paramLong1, paramLong2, paramInt1, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramInt3, paramFloat5, paramFloat6, paramInt4, paramInt5);
  }
  
  @Deprecated
  public static MotionEvent obtain(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int[] paramArrayOfInt, PointerCoords[] paramArrayOfPointerCoords, int paramInt3, float paramFloat1, float paramFloat2, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    synchronized (gSharedTempLock)
    {
      ensureSharedTempPointerCapacity(paramInt2);
      PointerProperties[] arrayOfPointerProperties = gSharedTempPointerProperties;
      for (int i = 0; i < paramInt2; i++)
      {
        arrayOfPointerProperties[i].clear();
        id = paramArrayOfInt[i];
      }
      paramArrayOfInt = obtain(paramLong1, paramLong2, paramInt1, paramInt2, arrayOfPointerProperties, paramArrayOfPointerCoords, paramInt3, 0, paramFloat1, paramFloat2, paramInt4, paramInt5, paramInt6, paramInt7);
      return paramArrayOfInt;
    }
  }
  
  public static MotionEvent obtain(long paramLong1, long paramLong2, int paramInt1, int paramInt2, PointerProperties[] paramArrayOfPointerProperties, PointerCoords[] paramArrayOfPointerCoords, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    MotionEvent localMotionEvent = obtain();
    mNativePtr = nativeInitialize(mNativePtr, paramInt5, paramInt7, paramInt1, paramInt8, paramInt6, paramInt3, paramInt4, 0.0F, 0.0F, paramFloat1, paramFloat2, paramLong1 * 1000000L, paramLong2 * 1000000L, paramInt2, paramArrayOfPointerProperties, paramArrayOfPointerCoords);
    return localMotionEvent;
  }
  
  public static MotionEvent obtain(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent != null)
    {
      MotionEvent localMotionEvent = obtain();
      mNativePtr = nativeCopy(mNativePtr, mNativePtr, true);
      return localMotionEvent;
    }
    throw new IllegalArgumentException("other motion event must not be null");
  }
  
  public static MotionEvent obtainNoHistory(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent != null)
    {
      MotionEvent localMotionEvent = obtain();
      mNativePtr = nativeCopy(mNativePtr, mNativePtr, false);
      return localMotionEvent;
    }
    throw new IllegalArgumentException("other motion event must not be null");
  }
  
  public static String toolTypeToString(int paramInt)
  {
    String str = (String)TOOL_TYPE_SYMBOLIC_NAMES.get(paramInt);
    if (str == null) {
      str = Integer.toString(paramInt);
    }
    return str;
  }
  
  public final void addBatch(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt)
  {
    synchronized (gSharedTempLock)
    {
      ensureSharedTempPointerCapacity(1);
      PointerCoords[] arrayOfPointerCoords = gSharedTempPointerCoords;
      arrayOfPointerCoords[0].clear();
      0x = paramFloat1;
      0y = paramFloat2;
      0pressure = paramFloat3;
      0size = paramFloat4;
      nativeAddBatch(mNativePtr, 1000000L * paramLong, arrayOfPointerCoords, paramInt);
      return;
    }
  }
  
  public final void addBatch(long paramLong, PointerCoords[] paramArrayOfPointerCoords, int paramInt)
  {
    nativeAddBatch(mNativePtr, 1000000L * paramLong, paramArrayOfPointerCoords, paramInt);
  }
  
  public final boolean addBatch(MotionEvent paramMotionEvent)
  {
    int i = nativeGetAction(mNativePtr);
    if ((i != 2) && (i != 7)) {
      return false;
    }
    if (i != nativeGetAction(mNativePtr)) {
      return false;
    }
    if ((nativeGetDeviceId(mNativePtr) == nativeGetDeviceId(mNativePtr)) && (nativeGetSource(mNativePtr) == nativeGetSource(mNativePtr)) && (nativeGetFlags(mNativePtr) == nativeGetFlags(mNativePtr)))
    {
      int j = nativeGetPointerCount(mNativePtr);
      if (j != nativeGetPointerCount(mNativePtr)) {
        return false;
      }
      synchronized (gSharedTempLock)
      {
        ensureSharedTempPointerCapacity(Math.max(j, 2));
        PointerProperties[] arrayOfPointerProperties = gSharedTempPointerProperties;
        PointerCoords[] arrayOfPointerCoords = gSharedTempPointerCoords;
        for (i = 0; i < j; i++)
        {
          nativeGetPointerProperties(mNativePtr, i, arrayOfPointerProperties[0]);
          nativeGetPointerProperties(mNativePtr, i, arrayOfPointerProperties[1]);
          if (!arrayOfPointerProperties[0].equals(arrayOfPointerProperties[1])) {
            return false;
          }
        }
        int k = nativeGetMetaState(mNativePtr);
        int m = nativeGetHistorySize(mNativePtr);
        for (i = 0; i <= m; i++)
        {
          int n;
          if (i == m) {
            n = Integer.MIN_VALUE;
          } else {
            n = i;
          }
          for (int i1 = 0; i1 < j; i1++) {
            nativeGetPointerCoords(mNativePtr, i1, n, arrayOfPointerCoords[i1]);
          }
          long l = nativeGetEventTimeNanos(mNativePtr, n);
          nativeAddBatch(mNativePtr, l, arrayOfPointerCoords, k);
        }
        return true;
      }
    }
    return false;
  }
  
  public final void cancel()
  {
    setAction(3);
  }
  
  public final MotionEvent clampNoHistory(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    MotionEvent localMotionEvent = obtain();
    label272:
    synchronized (gSharedTempLock)
    {
      int i = nativeGetPointerCount(mNativePtr);
      ensureSharedTempPointerCapacity(i);
      PointerProperties[] arrayOfPointerProperties = gSharedTempPointerProperties;
      PointerCoords[] arrayOfPointerCoords1 = gSharedTempPointerCoords;
      int j = 0;
      for (;;)
      {
        if (j < i) {
          try
          {
            nativeGetPointerProperties(mNativePtr, j, arrayOfPointerProperties[j]);
            nativeGetPointerCoords(mNativePtr, j, Integer.MIN_VALUE, arrayOfPointerCoords1[j]);
            PointerCoords localPointerCoords = arrayOfPointerCoords1[j];
            float f = x;
            try
            {
              x = clamp(f, paramFloat1, paramFloat3);
              localPointerCoords = arrayOfPointerCoords1[j];
              f = y;
              y = clamp(f, paramFloat2, paramFloat4);
              j++;
            }
            finally {}
            break label272;
          }
          finally {}
        }
      }
      mNativePtr = nativeInitialize(mNativePtr, nativeGetDeviceId(mNativePtr), nativeGetSource(mNativePtr), nativeGetAction(mNativePtr), nativeGetFlags(mNativePtr), nativeGetEdgeFlags(mNativePtr), nativeGetMetaState(mNativePtr), nativeGetButtonState(mNativePtr), nativeGetXOffset(mNativePtr), nativeGetYOffset(mNativePtr), nativeGetXPrecision(mNativePtr), nativeGetYPrecision(mNativePtr), nativeGetDownTimeNanos(mNativePtr), nativeGetEventTimeNanos(mNativePtr, Integer.MIN_VALUE), i, arrayOfPointerProperties, arrayOfPointerCoords2);
      return localMotionEvent;
    }
  }
  
  public MotionEvent copy()
  {
    return obtain(this);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mNativePtr != 0L)
      {
        nativeDispose(mNativePtr);
        mNativePtr = 0L;
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public final int findPointerIndex(int paramInt)
  {
    return nativeFindPointerIndex(mNativePtr, paramInt);
  }
  
  public final int getAction()
  {
    return nativeGetAction(mNativePtr);
  }
  
  public final int getActionButton()
  {
    return nativeGetActionButton(mNativePtr);
  }
  
  public final int getActionIndex()
  {
    return (nativeGetAction(mNativePtr) & 0xFF00) >> 8;
  }
  
  public final int getActionMasked()
  {
    return nativeGetAction(mNativePtr) & 0xFF;
  }
  
  public final float getAxisValue(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, paramInt, 0, Integer.MIN_VALUE);
  }
  
  public final float getAxisValue(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, paramInt1, paramInt2, Integer.MIN_VALUE);
  }
  
  public final int getButtonState()
  {
    return nativeGetButtonState(mNativePtr);
  }
  
  public final int getDeviceId()
  {
    return nativeGetDeviceId(mNativePtr);
  }
  
  public final long getDownTime()
  {
    return nativeGetDownTimeNanos(mNativePtr) / 1000000L;
  }
  
  public final int getEdgeFlags()
  {
    return nativeGetEdgeFlags(mNativePtr);
  }
  
  public final long getEventTime()
  {
    return nativeGetEventTimeNanos(mNativePtr, Integer.MIN_VALUE) / 1000000L;
  }
  
  public final long getEventTimeNano()
  {
    return nativeGetEventTimeNanos(mNativePtr, Integer.MIN_VALUE);
  }
  
  public final int getFlags()
  {
    return nativeGetFlags(mNativePtr);
  }
  
  public final float getHistoricalAxisValue(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, paramInt1, 0, paramInt2);
  }
  
  public final float getHistoricalAxisValue(int paramInt1, int paramInt2, int paramInt3)
  {
    return nativeGetAxisValue(mNativePtr, paramInt1, paramInt2, paramInt3);
  }
  
  public final long getHistoricalEventTime(int paramInt)
  {
    return nativeGetEventTimeNanos(mNativePtr, paramInt) / 1000000L;
  }
  
  public final long getHistoricalEventTimeNano(int paramInt)
  {
    return nativeGetEventTimeNanos(mNativePtr, paramInt);
  }
  
  public final float getHistoricalOrientation(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 8, 0, paramInt);
  }
  
  public final float getHistoricalOrientation(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 8, paramInt1, paramInt2);
  }
  
  public final void getHistoricalPointerCoords(int paramInt1, int paramInt2, PointerCoords paramPointerCoords)
  {
    nativeGetPointerCoords(mNativePtr, paramInt1, paramInt2, paramPointerCoords);
  }
  
  public final float getHistoricalPressure(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 2, 0, paramInt);
  }
  
  public final float getHistoricalPressure(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 2, paramInt1, paramInt2);
  }
  
  public final float getHistoricalSize(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 3, 0, paramInt);
  }
  
  public final float getHistoricalSize(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 3, paramInt1, paramInt2);
  }
  
  public final float getHistoricalToolMajor(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 6, 0, paramInt);
  }
  
  public final float getHistoricalToolMajor(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 6, paramInt1, paramInt2);
  }
  
  public final float getHistoricalToolMinor(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 7, 0, paramInt);
  }
  
  public final float getHistoricalToolMinor(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 7, paramInt1, paramInt2);
  }
  
  public final float getHistoricalTouchMajor(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 4, 0, paramInt);
  }
  
  public final float getHistoricalTouchMajor(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 4, paramInt1, paramInt2);
  }
  
  public final float getHistoricalTouchMinor(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 5, 0, paramInt);
  }
  
  public final float getHistoricalTouchMinor(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 5, paramInt1, paramInt2);
  }
  
  public final float getHistoricalX(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 0, 0, paramInt);
  }
  
  public final float getHistoricalX(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 0, paramInt1, paramInt2);
  }
  
  public final float getHistoricalY(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 1, 0, paramInt);
  }
  
  public final float getHistoricalY(int paramInt1, int paramInt2)
  {
    return nativeGetAxisValue(mNativePtr, 1, paramInt1, paramInt2);
  }
  
  public final int getHistorySize()
  {
    return nativeGetHistorySize(mNativePtr);
  }
  
  public final int getMetaState()
  {
    return nativeGetMetaState(mNativePtr);
  }
  
  public final float getOrientation()
  {
    return nativeGetAxisValue(mNativePtr, 8, 0, Integer.MIN_VALUE);
  }
  
  public final float getOrientation(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 8, paramInt, Integer.MIN_VALUE);
  }
  
  public final void getPointerCoords(int paramInt, PointerCoords paramPointerCoords)
  {
    nativeGetPointerCoords(mNativePtr, paramInt, Integer.MIN_VALUE, paramPointerCoords);
  }
  
  public final int getPointerCount()
  {
    return nativeGetPointerCount(mNativePtr);
  }
  
  public final int getPointerId(int paramInt)
  {
    return nativeGetPointerId(mNativePtr, paramInt);
  }
  
  public final int getPointerIdBits()
  {
    int i = 0;
    int j = nativeGetPointerCount(mNativePtr);
    for (int k = 0; k < j; k++) {
      i |= 1 << nativeGetPointerId(mNativePtr, k);
    }
    return i;
  }
  
  public final void getPointerProperties(int paramInt, PointerProperties paramPointerProperties)
  {
    nativeGetPointerProperties(mNativePtr, paramInt, paramPointerProperties);
  }
  
  public final float getPressure()
  {
    return nativeGetAxisValue(mNativePtr, 2, 0, Integer.MIN_VALUE);
  }
  
  public final float getPressure(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 2, paramInt, Integer.MIN_VALUE);
  }
  
  public final float getRawX()
  {
    return nativeGetRawAxisValue(mNativePtr, 0, 0, Integer.MIN_VALUE);
  }
  
  public final float getRawY()
  {
    return nativeGetRawAxisValue(mNativePtr, 1, 0, Integer.MIN_VALUE);
  }
  
  public final float getSize()
  {
    return nativeGetAxisValue(mNativePtr, 3, 0, Integer.MIN_VALUE);
  }
  
  public final float getSize(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 3, paramInt, Integer.MIN_VALUE);
  }
  
  public final int getSource()
  {
    return nativeGetSource(mNativePtr);
  }
  
  public final float getToolMajor()
  {
    return nativeGetAxisValue(mNativePtr, 6, 0, Integer.MIN_VALUE);
  }
  
  public final float getToolMajor(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 6, paramInt, Integer.MIN_VALUE);
  }
  
  public final float getToolMinor()
  {
    return nativeGetAxisValue(mNativePtr, 7, 0, Integer.MIN_VALUE);
  }
  
  public final float getToolMinor(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 7, paramInt, Integer.MIN_VALUE);
  }
  
  public final int getToolType(int paramInt)
  {
    return nativeGetToolType(mNativePtr, paramInt);
  }
  
  public final float getTouchMajor()
  {
    return nativeGetAxisValue(mNativePtr, 4, 0, Integer.MIN_VALUE);
  }
  
  public final float getTouchMajor(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 4, paramInt, Integer.MIN_VALUE);
  }
  
  public final float getTouchMinor()
  {
    return nativeGetAxisValue(mNativePtr, 5, 0, Integer.MIN_VALUE);
  }
  
  public final float getTouchMinor(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 5, paramInt, Integer.MIN_VALUE);
  }
  
  public final float getX()
  {
    return nativeGetAxisValue(mNativePtr, 0, 0, Integer.MIN_VALUE);
  }
  
  public final float getX(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 0, paramInt, Integer.MIN_VALUE);
  }
  
  public final float getXPrecision()
  {
    return nativeGetXPrecision(mNativePtr);
  }
  
  public final float getY()
  {
    return nativeGetAxisValue(mNativePtr, 1, 0, Integer.MIN_VALUE);
  }
  
  public final float getY(int paramInt)
  {
    return nativeGetAxisValue(mNativePtr, 1, paramInt, Integer.MIN_VALUE);
  }
  
  public final float getYPrecision()
  {
    return nativeGetYPrecision(mNativePtr);
  }
  
  public final boolean isButtonPressed(int paramInt)
  {
    boolean bool = false;
    if (paramInt == 0) {
      return false;
    }
    if ((getButtonState() & paramInt) == paramInt) {
      bool = true;
    }
    return bool;
  }
  
  public final boolean isHoverExitPending()
  {
    boolean bool;
    if ((getFlags() & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isTainted()
  {
    boolean bool;
    if ((0x80000000 & getFlags()) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isTargetAccessibilityFocus()
  {
    boolean bool;
    if ((0x40000000 & getFlags()) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isTouchEvent()
  {
    return nativeIsTouchEvent(mNativePtr);
  }
  
  public final boolean isWithinBoundsNoHistory(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    int i = nativeGetPointerCount(mNativePtr);
    int j = 0;
    while (j < i)
    {
      float f1 = nativeGetAxisValue(mNativePtr, 0, j, Integer.MIN_VALUE);
      float f2 = nativeGetAxisValue(mNativePtr, 1, j, Integer.MIN_VALUE);
      if ((f1 >= paramFloat1) && (f1 <= paramFloat3) && (f2 >= paramFloat2) && (f2 <= paramFloat4)) {
        j++;
      } else {
        return false;
      }
    }
    return true;
  }
  
  public final void offsetLocation(float paramFloat1, float paramFloat2)
  {
    if ((paramFloat1 != 0.0F) || (paramFloat2 != 0.0F)) {
      nativeOffsetLocation(mNativePtr, paramFloat1, paramFloat2);
    }
  }
  
  public final void recycle()
  {
    super.recycle();
    synchronized (gRecyclerLock)
    {
      if (gRecyclerUsed < 10)
      {
        gRecyclerUsed += 1;
        mNext = gRecyclerTop;
        gRecyclerTop = this;
      }
      return;
    }
  }
  
  public final void scale(float paramFloat)
  {
    if (paramFloat != 1.0F) {
      nativeScale(mNativePtr, paramFloat);
    }
  }
  
  public final void setAction(int paramInt)
  {
    nativeSetAction(mNativePtr, paramInt);
  }
  
  public final void setActionButton(int paramInt)
  {
    nativeSetActionButton(mNativePtr, paramInt);
  }
  
  public final void setButtonState(int paramInt)
  {
    nativeSetButtonState(mNativePtr, paramInt);
  }
  
  public final void setDownTime(long paramLong)
  {
    nativeSetDownTimeNanos(mNativePtr, 1000000L * paramLong);
  }
  
  public final void setEdgeFlags(int paramInt)
  {
    nativeSetEdgeFlags(mNativePtr, paramInt);
  }
  
  public void setHoverExitPending(boolean paramBoolean)
  {
    int i = getFlags();
    long l = mNativePtr;
    if (paramBoolean) {
      i |= 0x4;
    } else {
      i &= 0xFFFFFFFB;
    }
    nativeSetFlags(l, i);
  }
  
  public final void setLocation(float paramFloat1, float paramFloat2)
  {
    offsetLocation(paramFloat1 - getX(), paramFloat2 - getY());
  }
  
  public final void setSource(int paramInt)
  {
    nativeSetSource(mNativePtr, paramInt);
  }
  
  public final void setTainted(boolean paramBoolean)
  {
    int i = getFlags();
    long l = mNativePtr;
    if (paramBoolean) {
      i = 0x80000000 | i;
    } else {
      i = 0x7FFFFFFF & i;
    }
    nativeSetFlags(l, i);
  }
  
  public final void setTargetAccessibilityFocus(boolean paramBoolean)
  {
    int i = getFlags();
    long l = mNativePtr;
    if (paramBoolean) {
      i = 0x40000000 | i;
    } else {
      i = 0xBFFFFFFF & i;
    }
    nativeSetFlags(l, i);
  }
  
  /* Error */
  public final MotionEvent split(int paramInt)
  {
    // Byte code:
    //   0: invokestatic 425	android/view/MotionEvent:obtain	()Landroid/view/MotionEvent;
    //   3: astore_2
    //   4: getstatic 334	android/view/MotionEvent:gSharedTempLock	Ljava/lang/Object;
    //   7: astore_3
    //   8: aload_3
    //   9: monitorenter
    //   10: aload_2
    //   11: astore 4
    //   13: aload_3
    //   14: astore 4
    //   16: aload_0
    //   17: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   20: invokestatic 579	android/view/MotionEvent:nativeGetPointerCount	(J)I
    //   23: istore 5
    //   25: aload_2
    //   26: astore 4
    //   28: aload_3
    //   29: astore 4
    //   31: iload 5
    //   33: invokestatic 518	android/view/MotionEvent:ensureSharedTempPointerCapacity	(I)V
    //   36: aload_2
    //   37: astore 4
    //   39: aload_3
    //   40: astore 4
    //   42: getstatic 444	android/view/MotionEvent:gSharedTempPointerProperties	[Landroid/view/MotionEvent$PointerProperties;
    //   45: astore 6
    //   47: aload_2
    //   48: astore 4
    //   50: aload_3
    //   51: astore 4
    //   53: getstatic 435	android/view/MotionEvent:gSharedTempPointerCoords	[Landroid/view/MotionEvent$PointerCoords;
    //   56: astore 7
    //   58: aload_2
    //   59: astore 4
    //   61: aload_3
    //   62: astore 4
    //   64: getstatic 446	android/view/MotionEvent:gSharedTempPointerIndexMap	[I
    //   67: astore 8
    //   69: aload_2
    //   70: astore 4
    //   72: aload_3
    //   73: astore 4
    //   75: aload_0
    //   76: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   79: invokestatic 571	android/view/MotionEvent:nativeGetAction	(J)I
    //   82: istore 9
    //   84: iload 9
    //   86: sipush 255
    //   89: iand
    //   90: istore 10
    //   92: ldc 51
    //   94: iload 9
    //   96: iand
    //   97: bipush 8
    //   99: ishr
    //   100: istore 11
    //   102: iconst_m1
    //   103: istore 12
    //   105: iconst_0
    //   106: istore 13
    //   108: iconst_0
    //   109: istore 14
    //   111: iconst_0
    //   112: istore 15
    //   114: iconst_1
    //   115: istore 16
    //   117: iload 15
    //   119: iload 5
    //   121: if_icmpge +120 -> 241
    //   124: aload_2
    //   125: astore 4
    //   127: aload_3
    //   128: astore 4
    //   130: aload_0
    //   131: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   134: iload 15
    //   136: aload 6
    //   138: iload 13
    //   140: aaload
    //   141: invokestatic 587	android/view/MotionEvent:nativeGetPointerProperties	(JILandroid/view/MotionEvent$PointerProperties;)V
    //   144: aload_2
    //   145: astore 4
    //   147: aload_3
    //   148: astore 4
    //   150: iconst_1
    //   151: aload 6
    //   153: iload 13
    //   155: aaload
    //   156: getfield 524	android/view/MotionEvent$PointerProperties:id	I
    //   159: ishl
    //   160: istore 17
    //   162: iload 13
    //   164: istore 18
    //   166: iload 12
    //   168: istore 19
    //   170: iload 14
    //   172: istore 16
    //   174: iload 17
    //   176: iload_1
    //   177: iand
    //   178: ifeq +38 -> 216
    //   181: iload 15
    //   183: iload 11
    //   185: if_icmpne +7 -> 192
    //   188: iload 13
    //   190: istore 12
    //   192: aload 8
    //   194: iload 13
    //   196: iload 15
    //   198: iastore
    //   199: iload 13
    //   201: iconst_1
    //   202: iadd
    //   203: istore 18
    //   205: iload 14
    //   207: iload 17
    //   209: ior
    //   210: istore 16
    //   212: iload 12
    //   214: istore 19
    //   216: iinc 15 1
    //   219: iload 18
    //   221: istore 13
    //   223: iload 19
    //   225: istore 12
    //   227: iload 16
    //   229: istore 14
    //   231: goto -117 -> 114
    //   234: astore_3
    //   235: aload 4
    //   237: astore_2
    //   238: goto +452 -> 690
    //   241: iload 13
    //   243: ifeq +421 -> 664
    //   246: iload 10
    //   248: iconst_5
    //   249: if_icmpeq +20 -> 269
    //   252: iload 10
    //   254: bipush 6
    //   256: if_icmpne +6 -> 262
    //   259: goto +10 -> 269
    //   262: iload 9
    //   264: istore 12
    //   266: goto +48 -> 314
    //   269: iload 12
    //   271: ifge +11 -> 282
    //   274: iconst_2
    //   275: istore_1
    //   276: iload_1
    //   277: istore 12
    //   279: goto +35 -> 314
    //   282: iload 13
    //   284: iconst_1
    //   285: if_icmpne +17 -> 302
    //   288: iload 16
    //   290: istore_1
    //   291: iload 10
    //   293: iconst_5
    //   294: if_icmpne +5 -> 299
    //   297: iconst_0
    //   298: istore_1
    //   299: goto -23 -> 276
    //   302: iload 12
    //   304: bipush 8
    //   306: ishl
    //   307: iload 10
    //   309: ior
    //   310: istore_1
    //   311: goto -35 -> 276
    //   314: aload_2
    //   315: astore 4
    //   317: aload_3
    //   318: astore 4
    //   320: aload_0
    //   321: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   324: invokestatic 595	android/view/MotionEvent:nativeGetHistorySize	(J)I
    //   327: istore_1
    //   328: iconst_0
    //   329: istore 18
    //   331: iload 9
    //   333: istore 14
    //   335: iload 10
    //   337: istore 15
    //   339: iload 11
    //   341: istore 16
    //   343: iload 13
    //   345: istore 19
    //   347: iload_1
    //   348: istore 13
    //   350: iload 18
    //   352: istore_1
    //   353: iload 5
    //   355: istore 9
    //   357: iload_1
    //   358: iload 13
    //   360: if_icmpgt +297 -> 657
    //   363: iload_1
    //   364: iload 13
    //   366: if_icmpne +10 -> 376
    //   369: ldc -92
    //   371: istore 18
    //   373: goto +6 -> 379
    //   376: iload_1
    //   377: istore 18
    //   379: iconst_0
    //   380: istore 5
    //   382: iload 5
    //   384: iload 19
    //   386: if_icmpge +34 -> 420
    //   389: aload_2
    //   390: astore 4
    //   392: aload_3
    //   393: astore 4
    //   395: aload_0
    //   396: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   399: aload 8
    //   401: iload 5
    //   403: iaload
    //   404: iload 18
    //   406: aload 7
    //   408: iload 5
    //   410: aaload
    //   411: invokestatic 597	android/view/MotionEvent:nativeGetPointerCoords	(JIILandroid/view/MotionEvent$PointerCoords;)V
    //   414: iinc 5 1
    //   417: goto -35 -> 382
    //   420: aload_2
    //   421: astore 4
    //   423: aload_3
    //   424: astore 4
    //   426: aload_0
    //   427: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   430: iload 18
    //   432: invokestatic 599	android/view/MotionEvent:nativeGetEventTimeNanos	(JI)J
    //   435: lstore 20
    //   437: iload_1
    //   438: ifne +198 -> 636
    //   441: aload_2
    //   442: astore 4
    //   444: aload_3
    //   445: astore 4
    //   447: aload_2
    //   448: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   451: lstore 22
    //   453: aload_2
    //   454: astore 4
    //   456: aload_3
    //   457: astore 4
    //   459: aload_0
    //   460: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   463: invokestatic 573	android/view/MotionEvent:nativeGetDeviceId	(J)I
    //   466: istore 11
    //   468: aload_2
    //   469: astore 4
    //   471: aload_3
    //   472: astore 4
    //   474: aload_0
    //   475: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   478: invokestatic 575	android/view/MotionEvent:nativeGetSource	(J)I
    //   481: istore 5
    //   483: aload_2
    //   484: astore 4
    //   486: aload_3
    //   487: astore 4
    //   489: aload_0
    //   490: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   493: invokestatic 577	android/view/MotionEvent:nativeGetFlags	(J)I
    //   496: istore 10
    //   498: aload_2
    //   499: astore 4
    //   501: aload_3
    //   502: astore 4
    //   504: aload_0
    //   505: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   508: invokestatic 609	android/view/MotionEvent:nativeGetEdgeFlags	(J)I
    //   511: istore 17
    //   513: aload_2
    //   514: astore 4
    //   516: aload_3
    //   517: astore 4
    //   519: aload_0
    //   520: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   523: invokestatic 593	android/view/MotionEvent:nativeGetMetaState	(J)I
    //   526: istore 24
    //   528: aload_2
    //   529: astore 4
    //   531: aload_3
    //   532: astore 4
    //   534: aload_0
    //   535: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   538: invokestatic 611	android/view/MotionEvent:nativeGetButtonState	(J)I
    //   541: istore 18
    //   543: aload_2
    //   544: astore 4
    //   546: aload_3
    //   547: astore 4
    //   549: aload_0
    //   550: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   553: invokestatic 613	android/view/MotionEvent:nativeGetXOffset	(J)F
    //   556: fstore 25
    //   558: aload_3
    //   559: astore 4
    //   561: lload 22
    //   563: iload 11
    //   565: iload 5
    //   567: iload 12
    //   569: iload 10
    //   571: iload 17
    //   573: iload 24
    //   575: iload 18
    //   577: fload 25
    //   579: aload_0
    //   580: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   583: invokestatic 615	android/view/MotionEvent:nativeGetYOffset	(J)F
    //   586: aload_0
    //   587: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   590: invokestatic 617	android/view/MotionEvent:nativeGetXPrecision	(J)F
    //   593: aload_0
    //   594: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   597: invokestatic 619	android/view/MotionEvent:nativeGetYPrecision	(J)F
    //   600: aload_0
    //   601: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   604: invokestatic 621	android/view/MotionEvent:nativeGetDownTimeNanos	(J)J
    //   607: lload 20
    //   609: iload 19
    //   611: aload 6
    //   613: aload 7
    //   615: invokestatic 540	android/view/MotionEvent:nativeInitialize	(JIIIIIIIFFFFJJI[Landroid/view/MotionEvent$PointerProperties;[Landroid/view/MotionEvent$PointerCoords;)J
    //   618: lstore 20
    //   620: aload_2
    //   621: lload 20
    //   623: putfield 427	android/view/MotionEvent:mNativePtr	J
    //   626: goto +25 -> 651
    //   629: astore_3
    //   630: aload 4
    //   632: astore_2
    //   633: goto +57 -> 690
    //   636: aload_3
    //   637: astore 4
    //   639: aload_2
    //   640: getfield 427	android/view/MotionEvent:mNativePtr	J
    //   643: lload 20
    //   645: aload 7
    //   647: iconst_0
    //   648: invokestatic 567	android/view/MotionEvent:nativeAddBatch	(JJ[Landroid/view/MotionEvent$PointerCoords;I)V
    //   651: iinc 1 1
    //   654: goto -297 -> 357
    //   657: aload_3
    //   658: astore 4
    //   660: aload_3
    //   661: monitorexit
    //   662: aload_2
    //   663: areturn
    //   664: aload_3
    //   665: astore 4
    //   667: new 554	java/lang/IllegalArgumentException
    //   670: astore_2
    //   671: aload_3
    //   672: astore 4
    //   674: aload_2
    //   675: ldc_w 767
    //   678: invokespecial 557	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   681: aload_3
    //   682: astore 4
    //   684: aload_2
    //   685: athrow
    //   686: astore_3
    //   687: aload 4
    //   689: astore_2
    //   690: aload_2
    //   691: astore 4
    //   693: aload_2
    //   694: monitorexit
    //   695: aload_3
    //   696: athrow
    //   697: astore_3
    //   698: aload 4
    //   700: astore_2
    //   701: goto -11 -> 690
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	704	0	this	MotionEvent
    //   0	704	1	paramInt	int
    //   3	698	2	localObject1	Object
    //   7	141	3	localObject2	Object
    //   234	325	3	localObject3	Object
    //   629	53	3	localObject4	Object
    //   686	10	3	localObject5	Object
    //   697	1	3	localObject6	Object
    //   11	688	4	localObject7	Object
    //   23	543	5	i	int
    //   45	567	6	arrayOfPointerProperties	PointerProperties[]
    //   56	590	7	arrayOfPointerCoords	PointerCoords[]
    //   67	333	8	arrayOfInt	int[]
    //   82	274	9	j	int
    //   90	480	10	k	int
    //   100	464	11	m	int
    //   103	465	12	n	int
    //   106	261	13	i1	int
    //   109	225	14	i2	int
    //   112	226	15	i3	int
    //   115	227	16	i4	int
    //   160	412	17	i5	int
    //   164	412	18	i6	int
    //   168	442	19	i7	int
    //   435	209	20	l1	long
    //   451	111	22	l2	long
    //   526	48	24	i8	int
    //   556	22	25	f	float
    // Exception table:
    //   from	to	target	type
    //   130	144	234	finally
    //   150	162	234	finally
    //   395	414	234	finally
    //   561	620	629	finally
    //   16	25	686	finally
    //   31	36	686	finally
    //   42	47	686	finally
    //   53	58	686	finally
    //   64	69	686	finally
    //   75	84	686	finally
    //   320	328	686	finally
    //   426	437	686	finally
    //   447	453	686	finally
    //   459	468	686	finally
    //   474	483	686	finally
    //   489	498	686	finally
    //   504	513	686	finally
    //   519	528	686	finally
    //   534	543	686	finally
    //   549	558	686	finally
    //   620	626	697	finally
    //   639	651	697	finally
    //   660	662	697	finally
    //   667	671	697	finally
    //   674	681	697	finally
    //   684	686	697	finally
    //   693	695	697	finally
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("MotionEvent { action=");
    localStringBuilder1.append(actionToString(getAction()));
    appendUnless("0", localStringBuilder1, ", actionButton=", buttonStateToString(getActionButton()));
    int i = getPointerCount();
    for (int j = 0; j < i; j++)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(", id[");
      ((StringBuilder)localObject).append(j);
      ((StringBuilder)localObject).append("]=");
      appendUnless(Integer.valueOf(j), localStringBuilder1, ((StringBuilder)localObject).toString(), Integer.valueOf(getPointerId(j)));
      float f1 = getX(j);
      float f2 = getY(j);
      localStringBuilder1.append(", x[");
      localStringBuilder1.append(j);
      localStringBuilder1.append("]=");
      localStringBuilder1.append(f1);
      localStringBuilder1.append(", y[");
      localStringBuilder1.append(j);
      localStringBuilder1.append("]=");
      localStringBuilder1.append(f2);
      localObject = (String)TOOL_TYPE_SYMBOLIC_NAMES.get(1);
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", toolType[");
      localStringBuilder2.append(j);
      localStringBuilder2.append("]=");
      appendUnless(localObject, localStringBuilder1, localStringBuilder2.toString(), toolTypeToString(getToolType(j)));
    }
    appendUnless("0", localStringBuilder1, ", buttonState=", buttonStateToString(getButtonState()));
    appendUnless("0", localStringBuilder1, ", metaState=", KeyEvent.metaStateToString(getMetaState()));
    appendUnless("0", localStringBuilder1, ", flags=0x", Integer.toHexString(getFlags()));
    appendUnless("0", localStringBuilder1, ", edgeFlags=0x", Integer.toHexString(getEdgeFlags()));
    appendUnless(Integer.valueOf(1), localStringBuilder1, ", pointerCount=", Integer.valueOf(i));
    appendUnless(Integer.valueOf(0), localStringBuilder1, ", historySize=", Integer.valueOf(getHistorySize()));
    localStringBuilder1.append(", eventTime=");
    localStringBuilder1.append(getEventTime());
    localStringBuilder1.append(", downTime=");
    localStringBuilder1.append(getDownTime());
    localStringBuilder1.append(", deviceId=");
    localStringBuilder1.append(getDeviceId());
    localStringBuilder1.append(", source=0x");
    localStringBuilder1.append(Integer.toHexString(getSource()));
    localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }
  
  public final void transform(Matrix paramMatrix)
  {
    if (paramMatrix != null)
    {
      nativeTransform(mNativePtr, native_instance);
      return;
    }
    throw new IllegalArgumentException("matrix must not be null");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(1);
    nativeWriteToParcel(mNativePtr, paramParcel);
  }
  
  public static final class PointerCoords
  {
    private static final int INITIAL_PACKED_AXIS_VALUES = 8;
    private long mPackedAxisBits;
    private float[] mPackedAxisValues;
    public float orientation;
    public float pressure;
    public float size;
    public float toolMajor;
    public float toolMinor;
    public float touchMajor;
    public float touchMinor;
    public float x;
    public float y;
    
    public PointerCoords() {}
    
    public PointerCoords(PointerCoords paramPointerCoords)
    {
      copyFrom(paramPointerCoords);
    }
    
    public static PointerCoords[] createArray(int paramInt)
    {
      PointerCoords[] arrayOfPointerCoords = new PointerCoords[paramInt];
      for (int i = 0; i < paramInt; i++) {
        arrayOfPointerCoords[i] = new PointerCoords();
      }
      return arrayOfPointerCoords;
    }
    
    public void clear()
    {
      mPackedAxisBits = 0L;
      x = 0.0F;
      y = 0.0F;
      pressure = 0.0F;
      size = 0.0F;
      touchMajor = 0.0F;
      touchMinor = 0.0F;
      toolMajor = 0.0F;
      toolMinor = 0.0F;
      orientation = 0.0F;
    }
    
    public void copyFrom(PointerCoords paramPointerCoords)
    {
      long l = mPackedAxisBits;
      mPackedAxisBits = l;
      if (l != 0L)
      {
        float[] arrayOfFloat1 = mPackedAxisValues;
        int i = Long.bitCount(l);
        float[] arrayOfFloat2 = mPackedAxisValues;
        float[] arrayOfFloat3;
        if (arrayOfFloat2 != null)
        {
          arrayOfFloat3 = arrayOfFloat2;
          if (i <= arrayOfFloat2.length) {}
        }
        else
        {
          arrayOfFloat3 = new float[arrayOfFloat1.length];
          mPackedAxisValues = arrayOfFloat3;
        }
        System.arraycopy(arrayOfFloat1, 0, arrayOfFloat3, 0, i);
      }
      x = x;
      y = y;
      pressure = pressure;
      size = size;
      touchMajor = touchMajor;
      touchMinor = touchMinor;
      toolMajor = toolMajor;
      toolMinor = toolMinor;
      orientation = orientation;
    }
    
    public float getAxisValue(int paramInt)
    {
      long l;
      switch (paramInt)
      {
      default: 
        if ((paramInt < 0) || (paramInt > 63)) {
          break label144;
        }
        l = mPackedAxisBits;
        if ((l & Long.MIN_VALUE >>> paramInt) == 0L) {
          return 0.0F;
        }
        break;
      case 8: 
        return orientation;
      case 7: 
        return toolMinor;
      case 6: 
        return toolMajor;
      case 5: 
        return touchMinor;
      case 4: 
        return touchMajor;
      case 3: 
        return size;
      case 2: 
        return pressure;
      case 1: 
        return y;
      case 0: 
        return x;
      }
      paramInt = Long.bitCount(-1L >>> paramInt & l);
      return mPackedAxisValues[paramInt];
      label144:
      throw new IllegalArgumentException("Axis out of range.");
    }
    
    public void setAxisValue(int paramInt, float paramFloat)
    {
      long l1;
      long l2;
      float[] arrayOfFloat1;
      float[] arrayOfFloat2;
      switch (paramInt)
      {
      default: 
        if ((paramInt < 0) || (paramInt > 63)) {
          break label295;
        }
        l1 = mPackedAxisBits;
        l2 = Long.MIN_VALUE >>> paramInt;
        paramInt = Long.bitCount(-1L >>> paramInt & l1);
        arrayOfFloat1 = mPackedAxisValues;
        arrayOfFloat2 = arrayOfFloat1;
        if ((l1 & l2) != 0L) {
          break label289;
        }
        if (arrayOfFloat1 == null)
        {
          arrayOfFloat2 = new float[8];
          mPackedAxisValues = arrayOfFloat2;
        }
        break;
      case 8: 
        orientation = paramFloat;
        break;
      case 7: 
        toolMinor = paramFloat;
        break;
      case 6: 
        toolMajor = paramFloat;
        break;
      case 5: 
        touchMinor = paramFloat;
        break;
      case 4: 
        touchMajor = paramFloat;
        break;
      case 3: 
        size = paramFloat;
        break;
      case 2: 
        pressure = paramFloat;
        break;
      case 1: 
        y = paramFloat;
        break;
      case 0: 
        x = paramFloat;
        break;
      }
      int i = Long.bitCount(l1);
      if (i < arrayOfFloat1.length)
      {
        arrayOfFloat2 = arrayOfFloat1;
        if (paramInt != i)
        {
          System.arraycopy(arrayOfFloat1, paramInt, arrayOfFloat1, paramInt + 1, i - paramInt);
          arrayOfFloat2 = arrayOfFloat1;
        }
      }
      else
      {
        arrayOfFloat2 = new float[i * 2];
        System.arraycopy(arrayOfFloat1, 0, arrayOfFloat2, 0, paramInt);
        System.arraycopy(arrayOfFloat1, paramInt, arrayOfFloat2, paramInt + 1, i - paramInt);
        mPackedAxisValues = arrayOfFloat2;
      }
      mPackedAxisBits = (l1 | l2);
      label289:
      arrayOfFloat2[paramInt] = paramFloat;
      return;
      label295:
      throw new IllegalArgumentException("Axis out of range.");
    }
  }
  
  public static final class PointerProperties
  {
    public int id;
    public int toolType;
    
    public PointerProperties()
    {
      clear();
    }
    
    public PointerProperties(PointerProperties paramPointerProperties)
    {
      copyFrom(paramPointerProperties);
    }
    
    public static PointerProperties[] createArray(int paramInt)
    {
      PointerProperties[] arrayOfPointerProperties = new PointerProperties[paramInt];
      for (int i = 0; i < paramInt; i++) {
        arrayOfPointerProperties[i] = new PointerProperties();
      }
      return arrayOfPointerProperties;
    }
    
    private boolean equals(PointerProperties paramPointerProperties)
    {
      boolean bool;
      if ((paramPointerProperties != null) && (id == id) && (toolType == toolType)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void clear()
    {
      id = -1;
      toolType = 0;
    }
    
    public void copyFrom(PointerProperties paramPointerProperties)
    {
      id = id;
      toolType = toolType;
    }
    
    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof PointerProperties)) {
        return equals((PointerProperties)paramObject);
      }
      return false;
    }
    
    public int hashCode()
    {
      return id | toolType << 8;
    }
  }
}
