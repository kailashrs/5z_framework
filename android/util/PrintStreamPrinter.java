package android.util;

import java.io.PrintStream;

public class PrintStreamPrinter
  implements Printer
{
  private final PrintStream mPS;
  
  public PrintStreamPrinter(PrintStream paramPrintStream)
  {
    mPS = paramPrintStream;
  }
  
  public void println(String paramString)
  {
    mPS.println(paramString);
  }
}
