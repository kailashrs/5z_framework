package android.os;

public abstract class OptiFlexInternal
{
  public static final int HOLD_ADJ_HIGH = 2;
  public static final int HOLD_ADJ_LOW = 1;
  public static final int HOLD_ADJ_NONE = 0;
  
  public OptiFlexInternal() {}
  
  public abstract int changeOomAdjLocked(int paramInt, String paramString);
}
