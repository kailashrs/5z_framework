package android.text.method;

public class HideReturnsTransformationMethod
  extends ReplacementTransformationMethod
{
  private static char[] ORIGINAL = { '\r' };
  private static char[] REPLACEMENT = { 65279 };
  private static HideReturnsTransformationMethod sInstance;
  
  public HideReturnsTransformationMethod() {}
  
  public static HideReturnsTransformationMethod getInstance()
  {
    if (sInstance != null) {
      return sInstance;
    }
    sInstance = new HideReturnsTransformationMethod();
    return sInstance;
  }
  
  protected char[] getOriginal()
  {
    return ORIGINAL;
  }
  
  protected char[] getReplacement()
  {
    return REPLACEMENT;
  }
}
