package android.util;

public class StringBuilderPrinter
  implements Printer
{
  private final StringBuilder mBuilder;
  
  public StringBuilderPrinter(StringBuilder paramStringBuilder)
  {
    mBuilder = paramStringBuilder;
  }
  
  public void println(String paramString)
  {
    mBuilder.append(paramString);
    int i = paramString.length();
    if ((i <= 0) || (paramString.charAt(i - 1) != '\n')) {
      mBuilder.append('\n');
    }
  }
}
