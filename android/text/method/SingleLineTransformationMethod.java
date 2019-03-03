package android.text.method;

public class SingleLineTransformationMethod
  extends ReplacementTransformationMethod
{
  private static char[] ORIGINAL = { 10, 13 };
  private static char[] REPLACEMENT = { 32, -257 };
  private static SingleLineTransformationMethod sInstance;
  
  public SingleLineTransformationMethod() {}
  
  public static SingleLineTransformationMethod getInstance()
  {
    if (sInstance != null) {
      return sInstance;
    }
    sInstance = new SingleLineTransformationMethod();
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
