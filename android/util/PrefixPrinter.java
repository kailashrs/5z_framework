package android.util;

public class PrefixPrinter
  implements Printer
{
  private final String mPrefix;
  private final Printer mPrinter;
  
  private PrefixPrinter(Printer paramPrinter, String paramString)
  {
    mPrinter = paramPrinter;
    mPrefix = paramString;
  }
  
  public static Printer create(Printer paramPrinter, String paramString)
  {
    if ((paramString != null) && (!paramString.equals(""))) {
      return new PrefixPrinter(paramPrinter, paramString);
    }
    return paramPrinter;
  }
  
  public void println(String paramString)
  {
    Printer localPrinter = mPrinter;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mPrefix);
    localStringBuilder.append(paramString);
    localPrinter.println(localStringBuilder.toString());
  }
}
