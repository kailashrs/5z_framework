package com.android.internal.os;

import android.os.ShellCommand;
import java.io.PrintStream;

public abstract class BaseCommand
{
  public static final String FATAL_ERROR_CODE = "Error type 1";
  public static final String NO_CLASS_ERROR_CODE = "Error type 3";
  public static final String NO_SYSTEM_ERROR_CODE = "Error type 2";
  protected final ShellCommand mArgs = new ShellCommand()
  {
    public int onCommand(String paramAnonymousString)
    {
      return 0;
    }
    
    public void onHelp() {}
  };
  private String[] mRawArgs;
  
  public BaseCommand() {}
  
  public String[] getRawArgs()
  {
    return mRawArgs;
  }
  
  public String nextArg()
  {
    return mArgs.getNextArg();
  }
  
  public String nextArgRequired()
  {
    return mArgs.getNextArgRequired();
  }
  
  public String nextOption()
  {
    return mArgs.getNextOption();
  }
  
  public abstract void onRun()
    throws Exception;
  
  public abstract void onShowUsage(PrintStream paramPrintStream);
  
  public String peekNextArg()
  {
    return mArgs.peekNextArg();
  }
  
  public void run(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 1)
    {
      onShowUsage(System.out);
      return;
    }
    mRawArgs = paramArrayOfString;
    mArgs.init(null, null, null, null, paramArrayOfString, null, 0);
    try
    {
      onRun();
    }
    catch (Exception paramArrayOfString)
    {
      paramArrayOfString.printStackTrace(System.err);
      System.exit(1);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        onShowUsage(System.err);
        System.err.println();
        PrintStream localPrintStream = System.err;
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("Error: ");
        paramArrayOfString.append(localIllegalArgumentException.getMessage());
        localPrintStream.println(paramArrayOfString.toString());
      }
    }
  }
  
  public void showError(String paramString)
  {
    onShowUsage(System.err);
    System.err.println();
    System.err.println(paramString);
  }
  
  public void showUsage()
  {
    onShowUsage(System.err);
  }
}
