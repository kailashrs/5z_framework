package android.hardware.camera2.utils;

public final class HashCodeHelpers
{
  public HashCodeHelpers() {}
  
  public static int hashCode(float... paramVarArgs)
  {
    int i = 0;
    if (paramVarArgs == null) {
      return 0;
    }
    int j = 1;
    int k = paramVarArgs.length;
    while (i < k)
    {
      j = (j << 5) - j ^ Float.floatToIntBits(paramVarArgs[i]);
      i++;
    }
    return j;
  }
  
  public static int hashCode(int... paramVarArgs)
  {
    int i = 0;
    if (paramVarArgs == null) {
      return 0;
    }
    int j = 1;
    int k = paramVarArgs.length;
    while (i < k)
    {
      j = (j << 5) - j ^ paramVarArgs[i];
      i++;
    }
    return j;
  }
  
  public static <T> int hashCodeGeneric(T... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return 0;
    }
    int i = paramVarArgs.length;
    int j = 1;
    for (int k = 0; k < i; k++)
    {
      T ? = paramVarArgs[k];
      int m;
      if (? == null) {
        m = 0;
      } else {
        m = ?.hashCode();
      }
      j = (j << 5) - j ^ m;
    }
    return j;
  }
}
