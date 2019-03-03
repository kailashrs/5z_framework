package android.filterfw.core;

public class NativeProgram
  extends Program
{
  private boolean mHasGetValueFunction = false;
  private boolean mHasInitFunction = false;
  private boolean mHasResetFunction = false;
  private boolean mHasSetValueFunction = false;
  private boolean mHasTeardownFunction = false;
  private boolean mTornDown = false;
  private int nativeProgramId;
  
  static
  {
    System.loadLibrary("filterfw");
  }
  
  public NativeProgram(String paramString1, String paramString2)
  {
    allocate();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("lib");
    ((StringBuilder)localObject).append(paramString1);
    ((StringBuilder)localObject).append(".so");
    paramString1 = ((StringBuilder)localObject).toString();
    if (openNativeLibrary(paramString1))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString2);
      ((StringBuilder)localObject).append("_process");
      localObject = ((StringBuilder)localObject).toString();
      if (bindProcessFunction((String)localObject))
      {
        paramString1 = new StringBuilder();
        paramString1.append(paramString2);
        paramString1.append("_init");
        mHasInitFunction = bindInitFunction(paramString1.toString());
        paramString1 = new StringBuilder();
        paramString1.append(paramString2);
        paramString1.append("_teardown");
        mHasTeardownFunction = bindTeardownFunction(paramString1.toString());
        paramString1 = new StringBuilder();
        paramString1.append(paramString2);
        paramString1.append("_setvalue");
        mHasSetValueFunction = bindSetValueFunction(paramString1.toString());
        paramString1 = new StringBuilder();
        paramString1.append(paramString2);
        paramString1.append("_getvalue");
        mHasGetValueFunction = bindGetValueFunction(paramString1.toString());
        paramString1 = new StringBuilder();
        paramString1.append(paramString2);
        paramString1.append("_reset");
        mHasResetFunction = bindResetFunction(paramString1.toString());
        if ((mHasInitFunction) && (!callNativeInit())) {
          throw new RuntimeException("Could not initialize NativeProgram!");
        }
        return;
      }
      paramString2 = new StringBuilder();
      paramString2.append("Could not find native program function name ");
      paramString2.append((String)localObject);
      paramString2.append(" in library ");
      paramString2.append(paramString1);
      paramString2.append("! This function is required!");
      throw new RuntimeException(paramString2.toString());
    }
    paramString2 = new StringBuilder();
    paramString2.append("Could not find native library named '");
    paramString2.append(paramString1);
    paramString2.append("' required for native program!");
    throw new RuntimeException(paramString2.toString());
  }
  
  private native boolean allocate();
  
  private native boolean bindGetValueFunction(String paramString);
  
  private native boolean bindInitFunction(String paramString);
  
  private native boolean bindProcessFunction(String paramString);
  
  private native boolean bindResetFunction(String paramString);
  
  private native boolean bindSetValueFunction(String paramString);
  
  private native boolean bindTeardownFunction(String paramString);
  
  private native String callNativeGetValue(String paramString);
  
  private native boolean callNativeInit();
  
  private native boolean callNativeProcess(NativeFrame[] paramArrayOfNativeFrame, NativeFrame paramNativeFrame);
  
  private native boolean callNativeReset();
  
  private native boolean callNativeSetValue(String paramString1, String paramString2);
  
  private native boolean callNativeTeardown();
  
  private native boolean deallocate();
  
  private native boolean nativeInit();
  
  private native boolean openNativeLibrary(String paramString);
  
  protected void finalize()
    throws Throwable
  {
    tearDown();
  }
  
  public Object getHostValue(String paramString)
  {
    if (!mTornDown)
    {
      if (mHasGetValueFunction) {
        return callNativeGetValue(paramString);
      }
      throw new RuntimeException("Attempting to get native variable, but native code does not define native getvalue function!");
    }
    throw new RuntimeException("NativeProgram already torn down!");
  }
  
  public void process(Frame[] paramArrayOfFrame, Frame paramFrame)
  {
    if (!mTornDown)
    {
      NativeFrame[] arrayOfNativeFrame = new NativeFrame[paramArrayOfFrame.length];
      for (int i = 0; i < paramArrayOfFrame.length; i++)
      {
        if ((paramArrayOfFrame[i] != null) && (!(paramArrayOfFrame[i] instanceof NativeFrame)))
        {
          paramArrayOfFrame = new StringBuilder();
          paramArrayOfFrame.append("NativeProgram got non-native frame as input ");
          paramArrayOfFrame.append(i);
          paramArrayOfFrame.append("!");
          throw new RuntimeException(paramArrayOfFrame.toString());
        }
        arrayOfNativeFrame[i] = ((NativeFrame)paramArrayOfFrame[i]);
      }
      if ((paramFrame != null) && (!(paramFrame instanceof NativeFrame))) {
        throw new RuntimeException("NativeProgram got non-native output frame!");
      }
      if (callNativeProcess(arrayOfNativeFrame, (NativeFrame)paramFrame)) {
        return;
      }
      throw new RuntimeException("Calling native process() caused error!");
    }
    throw new RuntimeException("NativeProgram already torn down!");
  }
  
  public void reset()
  {
    if ((mHasResetFunction) && (!callNativeReset())) {
      throw new RuntimeException("Could not reset NativeProgram!");
    }
  }
  
  public void setHostValue(String paramString, Object paramObject)
  {
    if (!mTornDown)
    {
      if (mHasSetValueFunction)
      {
        if (callNativeSetValue(paramString, paramObject.toString())) {
          return;
        }
        paramObject = new StringBuilder();
        paramObject.append("Error setting native value for variable '");
        paramObject.append(paramString);
        paramObject.append("'!");
        throw new RuntimeException(paramObject.toString());
      }
      throw new RuntimeException("Attempting to set native variable, but native code does not define native setvalue function!");
    }
    throw new RuntimeException("NativeProgram already torn down!");
  }
  
  public void tearDown()
  {
    if (mTornDown) {
      return;
    }
    if ((mHasTeardownFunction) && (!callNativeTeardown())) {
      throw new RuntimeException("Could not tear down NativeProgram!");
    }
    deallocate();
    mTornDown = true;
  }
}
