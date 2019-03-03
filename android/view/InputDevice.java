package android.view;

import android.hardware.input.InputDeviceIdentifier;
import android.hardware.input.InputManager;
import android.os.NullVibrator;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Vibrator;
import java.util.ArrayList;
import java.util.List;

public final class InputDevice
  implements Parcelable
{
  public static final Parcelable.Creator<InputDevice> CREATOR = new Parcelable.Creator()
  {
    public InputDevice createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InputDevice(paramAnonymousParcel, null);
    }
    
    public InputDevice[] newArray(int paramAnonymousInt)
    {
      return new InputDevice[paramAnonymousInt];
    }
  };
  public static final int KEYBOARD_TYPE_ALPHABETIC = 2;
  public static final int KEYBOARD_TYPE_NONE = 0;
  public static final int KEYBOARD_TYPE_NON_ALPHABETIC = 1;
  private static final int MAX_RANGES = 1000;
  @Deprecated
  public static final int MOTION_RANGE_ORIENTATION = 8;
  @Deprecated
  public static final int MOTION_RANGE_PRESSURE = 2;
  @Deprecated
  public static final int MOTION_RANGE_SIZE = 3;
  @Deprecated
  public static final int MOTION_RANGE_TOOL_MAJOR = 6;
  @Deprecated
  public static final int MOTION_RANGE_TOOL_MINOR = 7;
  @Deprecated
  public static final int MOTION_RANGE_TOUCH_MAJOR = 4;
  @Deprecated
  public static final int MOTION_RANGE_TOUCH_MINOR = 5;
  @Deprecated
  public static final int MOTION_RANGE_X = 0;
  @Deprecated
  public static final int MOTION_RANGE_Y = 1;
  public static final int SOURCE_ANY = -256;
  public static final int SOURCE_BLUETOOTH_STYLUS = 49154;
  public static final int SOURCE_CLASS_BUTTON = 1;
  public static final int SOURCE_CLASS_JOYSTICK = 16;
  public static final int SOURCE_CLASS_MASK = 255;
  public static final int SOURCE_CLASS_NONE = 0;
  public static final int SOURCE_CLASS_POINTER = 2;
  public static final int SOURCE_CLASS_POSITION = 8;
  public static final int SOURCE_CLASS_TRACKBALL = 4;
  public static final int SOURCE_DPAD = 513;
  public static final int SOURCE_GAMEPAD = 1025;
  public static final int SOURCE_HDMI = 33554433;
  public static final int SOURCE_JOYSTICK = 16777232;
  public static final int SOURCE_KEYBOARD = 257;
  public static final int SOURCE_MOUSE = 8194;
  public static final int SOURCE_MOUSE_RELATIVE = 131076;
  public static final int SOURCE_ROTARY_ENCODER = 4194304;
  public static final int SOURCE_STYLUS = 16386;
  public static final int SOURCE_TOUCHPAD = 1048584;
  public static final int SOURCE_TOUCHSCREEN = 4098;
  public static final int SOURCE_TOUCH_NAVIGATION = 2097152;
  public static final int SOURCE_TRACKBALL = 65540;
  public static final int SOURCE_UNKNOWN = 0;
  private final int mControllerNumber;
  private final String mDescriptor;
  private final int mGeneration;
  private final boolean mHasButtonUnderPad;
  private final boolean mHasMicrophone;
  private final boolean mHasVibrator;
  private final int mId;
  private final InputDeviceIdentifier mIdentifier;
  private final boolean mIsExternal;
  private final KeyCharacterMap mKeyCharacterMap;
  private final int mKeyboardType;
  private final ArrayList<MotionRange> mMotionRanges = new ArrayList();
  private final String mName;
  private final int mProductId;
  private final int mSources;
  private final int mVendorId;
  private Vibrator mVibrator;
  
  private InputDevice(int paramInt1, int paramInt2, int paramInt3, String paramString1, int paramInt4, int paramInt5, String paramString2, boolean paramBoolean1, int paramInt6, int paramInt7, KeyCharacterMap paramKeyCharacterMap, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    mId = paramInt1;
    mGeneration = paramInt2;
    mControllerNumber = paramInt3;
    mName = paramString1;
    mVendorId = paramInt4;
    mProductId = paramInt5;
    mDescriptor = paramString2;
    mIsExternal = paramBoolean1;
    mSources = paramInt6;
    mKeyboardType = paramInt7;
    mKeyCharacterMap = paramKeyCharacterMap;
    mHasVibrator = paramBoolean2;
    mHasMicrophone = paramBoolean3;
    mHasButtonUnderPad = paramBoolean4;
    mIdentifier = new InputDeviceIdentifier(paramString2, paramInt4, paramInt5);
  }
  
  private InputDevice(Parcel paramParcel)
  {
    mId = paramParcel.readInt();
    mGeneration = paramParcel.readInt();
    mControllerNumber = paramParcel.readInt();
    mName = paramParcel.readString();
    mVendorId = paramParcel.readInt();
    mProductId = paramParcel.readInt();
    mDescriptor = paramParcel.readString();
    int i = paramParcel.readInt();
    int j = 0;
    boolean bool1 = true;
    boolean bool2;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsExternal = bool2;
    mSources = paramParcel.readInt();
    mKeyboardType = paramParcel.readInt();
    mKeyCharacterMap = ((KeyCharacterMap)KeyCharacterMap.CREATOR.createFromParcel(paramParcel));
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mHasVibrator = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mHasMicrophone = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    mHasButtonUnderPad = bool2;
    mIdentifier = new InputDeviceIdentifier(mDescriptor, mVendorId, mProductId);
    int k = paramParcel.readInt();
    i = k;
    if (k > 1000) {
      i = 1000;
    }
    while (j < i)
    {
      addMotionRange(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readFloat(), paramParcel.readFloat(), paramParcel.readFloat(), paramParcel.readFloat(), paramParcel.readFloat());
      j++;
    }
  }
  
  private void addMotionRange(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    mMotionRanges.add(new MotionRange(paramInt1, paramInt2, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, null));
  }
  
  private void appendSourceDescriptionIfApplicable(StringBuilder paramStringBuilder, int paramInt, String paramString)
  {
    if ((mSources & paramInt) == paramInt)
    {
      paramStringBuilder.append(" ");
      paramStringBuilder.append(paramString);
    }
  }
  
  public static InputDevice getDevice(int paramInt)
  {
    return InputManager.getInstance().getInputDevice(paramInt);
  }
  
  public static int[] getDeviceIds()
  {
    return InputManager.getInstance().getInputDeviceIds();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void disable()
  {
    InputManager.getInstance().disableInputDevice(mId);
  }
  
  public void enable()
  {
    InputManager.getInstance().enableInputDevice(mId);
  }
  
  public int getControllerNumber()
  {
    return mControllerNumber;
  }
  
  public String getDescriptor()
  {
    return mDescriptor;
  }
  
  public int getGeneration()
  {
    return mGeneration;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public InputDeviceIdentifier getIdentifier()
  {
    return mIdentifier;
  }
  
  public KeyCharacterMap getKeyCharacterMap()
  {
    return mKeyCharacterMap;
  }
  
  public int getKeyboardType()
  {
    return mKeyboardType;
  }
  
  public MotionRange getMotionRange(int paramInt)
  {
    int i = mMotionRanges.size();
    for (int j = 0; j < i; j++)
    {
      MotionRange localMotionRange = (MotionRange)mMotionRanges.get(j);
      if (mAxis == paramInt) {
        return localMotionRange;
      }
    }
    return null;
  }
  
  public MotionRange getMotionRange(int paramInt1, int paramInt2)
  {
    int i = mMotionRanges.size();
    for (int j = 0; j < i; j++)
    {
      MotionRange localMotionRange = (MotionRange)mMotionRanges.get(j);
      if ((mAxis == paramInt1) && (mSource == paramInt2)) {
        return localMotionRange;
      }
    }
    return null;
  }
  
  public List<MotionRange> getMotionRanges()
  {
    return mMotionRanges;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getProductId()
  {
    return mProductId;
  }
  
  public int getSources()
  {
    return mSources;
  }
  
  public int getVendorId()
  {
    return mVendorId;
  }
  
  public Vibrator getVibrator()
  {
    synchronized (mMotionRanges)
    {
      if (mVibrator == null) {
        if (mHasVibrator) {
          mVibrator = InputManager.getInstance().getInputDeviceVibrator(mId);
        } else {
          mVibrator = NullVibrator.getInstance();
        }
      }
      Vibrator localVibrator = mVibrator;
      return localVibrator;
    }
  }
  
  public boolean hasButtonUnderPad()
  {
    return mHasButtonUnderPad;
  }
  
  public boolean[] hasKeys(int... paramVarArgs)
  {
    return InputManager.getInstance().deviceHasKeys(mId, paramVarArgs);
  }
  
  public boolean hasMicrophone()
  {
    return mHasMicrophone;
  }
  
  public boolean isEnabled()
  {
    return InputManager.getInstance().isInputDeviceEnabled(mId);
  }
  
  public boolean isExternal()
  {
    return mIsExternal;
  }
  
  public boolean isFullKeyboard()
  {
    boolean bool;
    if (((mSources & 0x101) == 257) && (mKeyboardType == 2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVirtual()
  {
    boolean bool;
    if (mId < 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setCustomPointerIcon(PointerIcon paramPointerIcon)
  {
    InputManager.getInstance().setCustomPointerIcon(paramPointerIcon);
  }
  
  public void setPointerType(int paramInt)
  {
    InputManager.getInstance().setPointerIconType(paramInt);
  }
  
  public boolean supportsSource(int paramInt)
  {
    boolean bool;
    if ((mSources & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Input Device ");
    localStringBuilder.append(mId);
    localStringBuilder.append(": ");
    localStringBuilder.append(mName);
    localStringBuilder.append("\n");
    localStringBuilder.append("  Descriptor: ");
    localStringBuilder.append(mDescriptor);
    localStringBuilder.append("\n");
    localStringBuilder.append("  Generation: ");
    localStringBuilder.append(mGeneration);
    localStringBuilder.append("\n");
    localStringBuilder.append("  Location: ");
    Object localObject;
    if (mIsExternal) {
      localObject = "external";
    } else {
      localObject = "built-in";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append("\n");
    localStringBuilder.append("  Keyboard Type: ");
    switch (mKeyboardType)
    {
    default: 
      break;
    case 2: 
      localStringBuilder.append("alphabetic");
      break;
    case 1: 
      localStringBuilder.append("non-alphabetic");
      break;
    case 0: 
      localStringBuilder.append("none");
    }
    localStringBuilder.append("\n");
    localStringBuilder.append("  Has Vibrator: ");
    localStringBuilder.append(mHasVibrator);
    localStringBuilder.append("\n");
    localStringBuilder.append("  Has mic: ");
    localStringBuilder.append(mHasMicrophone);
    localStringBuilder.append("\n");
    localStringBuilder.append("  Sources: 0x");
    localStringBuilder.append(Integer.toHexString(mSources));
    localStringBuilder.append(" (");
    appendSourceDescriptionIfApplicable(localStringBuilder, 257, "keyboard");
    appendSourceDescriptionIfApplicable(localStringBuilder, 513, "dpad");
    appendSourceDescriptionIfApplicable(localStringBuilder, 4098, "touchscreen");
    appendSourceDescriptionIfApplicable(localStringBuilder, 8194, "mouse");
    appendSourceDescriptionIfApplicable(localStringBuilder, 16386, "stylus");
    appendSourceDescriptionIfApplicable(localStringBuilder, 65540, "trackball");
    appendSourceDescriptionIfApplicable(localStringBuilder, 131076, "mouse_relative");
    appendSourceDescriptionIfApplicable(localStringBuilder, 1048584, "touchpad");
    appendSourceDescriptionIfApplicable(localStringBuilder, 16777232, "joystick");
    appendSourceDescriptionIfApplicable(localStringBuilder, 1025, "gamepad");
    localStringBuilder.append(" )\n");
    int i = mMotionRanges.size();
    for (int j = 0; j < i; j++)
    {
      localObject = (MotionRange)mMotionRanges.get(j);
      localStringBuilder.append("    ");
      localStringBuilder.append(MotionEvent.axisToString(mAxis));
      localStringBuilder.append(": source=0x");
      localStringBuilder.append(Integer.toHexString(mSource));
      localStringBuilder.append(" min=");
      localStringBuilder.append(mMin);
      localStringBuilder.append(" max=");
      localStringBuilder.append(mMax);
      localStringBuilder.append(" flat=");
      localStringBuilder.append(mFlat);
      localStringBuilder.append(" fuzz=");
      localStringBuilder.append(mFuzz);
      localStringBuilder.append(" resolution=");
      localStringBuilder.append(mResolution);
      localStringBuilder.append("\n");
    }
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeInt(mGeneration);
    paramParcel.writeInt(mControllerNumber);
    paramParcel.writeString(mName);
    paramParcel.writeInt(mVendorId);
    paramParcel.writeInt(mProductId);
    paramParcel.writeString(mDescriptor);
    paramParcel.writeInt(mIsExternal);
    paramParcel.writeInt(mSources);
    paramParcel.writeInt(mKeyboardType);
    mKeyCharacterMap.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(mHasVibrator);
    paramParcel.writeInt(mHasMicrophone);
    paramParcel.writeInt(mHasButtonUnderPad);
    int i = mMotionRanges.size();
    paramParcel.writeInt(i);
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      MotionRange localMotionRange = (MotionRange)mMotionRanges.get(paramInt);
      paramParcel.writeInt(mAxis);
      paramParcel.writeInt(mSource);
      paramParcel.writeFloat(mMin);
      paramParcel.writeFloat(mMax);
      paramParcel.writeFloat(mFlat);
      paramParcel.writeFloat(mFuzz);
      paramParcel.writeFloat(mResolution);
    }
  }
  
  public static final class MotionRange
  {
    private int mAxis;
    private float mFlat;
    private float mFuzz;
    private float mMax;
    private float mMin;
    private float mResolution;
    private int mSource;
    
    private MotionRange(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
    {
      mAxis = paramInt1;
      mSource = paramInt2;
      mMin = paramFloat1;
      mMax = paramFloat2;
      mFlat = paramFloat3;
      mFuzz = paramFloat4;
      mResolution = paramFloat5;
    }
    
    public int getAxis()
    {
      return mAxis;
    }
    
    public float getFlat()
    {
      return mFlat;
    }
    
    public float getFuzz()
    {
      return mFuzz;
    }
    
    public float getMax()
    {
      return mMax;
    }
    
    public float getMin()
    {
      return mMin;
    }
    
    public float getRange()
    {
      return mMax - mMin;
    }
    
    public float getResolution()
    {
      return mResolution;
    }
    
    public int getSource()
    {
      return mSource;
    }
    
    public boolean isFromSource(int paramInt)
    {
      boolean bool;
      if ((getSource() & paramInt) == paramInt) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
}
