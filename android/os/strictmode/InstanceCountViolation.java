package android.os.strictmode;

import android.os.StrictMode.VmPolicy;

public class InstanceCountViolation
  extends Violation
{
  private static final StackTraceElement[] FAKE_STACK = { new StackTraceElement("android.os.StrictMode", "setClassInstanceLimit", "StrictMode.java", 1) };
  private Class mClass;
  private final long mInstances;
  private final int mLimit;
  
  public InstanceCountViolation(Class paramClass, long paramLong, int paramInt)
  {
    super(localStringBuilder.toString());
    setStackTrace(FAKE_STACK);
    mClass = paramClass;
    mInstances = paramLong;
    mLimit = paramInt;
  }
  
  public void dumpHeap(StrictMode.VmPolicy paramVmPolicy)
  {
    if (paramVmPolicy == null) {
      return;
    }
    if ((mInstances >= 3L) && (Math.abs(mInstances - mLimit) >= 2L)) {
      paramVmPolicy.dumpHeapForInstanceCount(mClass, mInstances, mLimit);
    }
  }
  
  public long getNumberOfInstances()
  {
    return mInstances;
  }
}
