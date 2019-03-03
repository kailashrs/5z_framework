package android.util;

import java.io.PrintWriter;

public class PrintWriterPrinter
  implements Printer
{
  private final PrintWriter mPW;
  
  public PrintWriterPrinter(PrintWriter paramPrintWriter)
  {
    mPW = paramPrintWriter;
  }
  
  public void println(String paramString)
  {
    mPW.println(paramString);
  }
}
