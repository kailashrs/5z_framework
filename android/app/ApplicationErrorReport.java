package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.util.Printer;
import com.android.internal.util.FastPrintWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ApplicationErrorReport
  implements Parcelable
{
  public static final Parcelable.Creator<ApplicationErrorReport> CREATOR = new Parcelable.Creator()
  {
    public ApplicationErrorReport createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ApplicationErrorReport(paramAnonymousParcel);
    }
    
    public ApplicationErrorReport[] newArray(int paramAnonymousInt)
    {
      return new ApplicationErrorReport[paramAnonymousInt];
    }
  };
  static final String DEFAULT_ERROR_RECEIVER_PROPERTY = "ro.error.receiver.default";
  static final String SYSTEM_APPS_ERROR_RECEIVER_PROPERTY = "ro.error.receiver.system.apps";
  public static final int TYPE_ANR = 2;
  public static final int TYPE_BATTERY = 3;
  public static final int TYPE_CRASH = 1;
  public static final int TYPE_NONE = 0;
  public static final int TYPE_RUNNING_SERVICE = 5;
  public AnrInfo anrInfo;
  public BatteryInfo batteryInfo;
  public CrashInfo crashInfo;
  public String installerPackageName;
  public String packageName;
  public String processName;
  public RunningServiceInfo runningServiceInfo;
  public boolean systemApp;
  public long time;
  public int type;
  
  public ApplicationErrorReport() {}
  
  ApplicationErrorReport(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public static ComponentName getErrorReportReceiver(Context paramContext, String paramString, int paramInt)
  {
    if (Settings.Global.getInt(paramContext.getContentResolver(), "send_action_app_error", 0) == 0) {
      return null;
    }
    PackageManager localPackageManager = paramContext.getPackageManager();
    paramContext = null;
    try
    {
      String str = localPackageManager.getInstallerPackageName(paramString);
      paramContext = str;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    if (paramContext != null)
    {
      paramContext = getErrorReportReceiver(localPackageManager, paramString, paramContext);
      if (paramContext != null) {
        return paramContext;
      }
    }
    if ((paramInt & 0x1) != 0)
    {
      paramContext = getErrorReportReceiver(localPackageManager, paramString, SystemProperties.get("ro.error.receiver.system.apps"));
      if (paramContext != null) {
        return paramContext;
      }
    }
    return getErrorReportReceiver(localPackageManager, paramString, SystemProperties.get("ro.error.receiver.default"));
  }
  
  static ComponentName getErrorReportReceiver(PackageManager paramPackageManager, String paramString1, String paramString2)
  {
    if ((paramString2 != null) && (paramString2.length() != 0))
    {
      if (paramString2.equals(paramString1)) {
        return null;
      }
      paramString1 = new Intent("android.intent.action.APP_ERROR");
      paramString1.setPackage(paramString2);
      paramPackageManager = paramPackageManager.resolveActivity(paramString1, 0);
      if ((paramPackageManager != null) && (activityInfo != null)) {
        return new ComponentName(paramString2, activityInfo.name);
      }
      return null;
    }
    return null;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("type: ");
    localStringBuilder.append(type);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("packageName: ");
    localStringBuilder.append(packageName);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("installerPackageName: ");
    localStringBuilder.append(installerPackageName);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("processName: ");
    localStringBuilder.append(processName);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("time: ");
    localStringBuilder.append(time);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("systemApp: ");
    localStringBuilder.append(systemApp);
    paramPrinter.println(localStringBuilder.toString());
    int i = type;
    if (i != 5) {
      switch (i)
      {
      default: 
        break;
      case 3: 
        batteryInfo.dump(paramPrinter, paramString);
        break;
      case 2: 
        anrInfo.dump(paramPrinter, paramString);
        break;
      case 1: 
        crashInfo.dump(paramPrinter, paramString);
        break;
      }
    } else {
      runningServiceInfo.dump(paramPrinter, paramString);
    }
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    type = paramParcel.readInt();
    packageName = paramParcel.readString();
    installerPackageName = paramParcel.readString();
    processName = paramParcel.readString();
    time = paramParcel.readLong();
    int i = paramParcel.readInt();
    int j = 0;
    boolean bool;
    if (i == 1) {
      bool = true;
    } else {
      bool = false;
    }
    systemApp = bool;
    if (paramParcel.readInt() == 1) {
      j = 1;
    }
    i = type;
    if (i != 5)
    {
      switch (i)
      {
      default: 
        break;
      case 3: 
        batteryInfo = new BatteryInfo(paramParcel);
        anrInfo = null;
        crashInfo = null;
        runningServiceInfo = null;
        break;
      case 2: 
        anrInfo = new AnrInfo(paramParcel);
        crashInfo = null;
        batteryInfo = null;
        runningServiceInfo = null;
        break;
      case 1: 
        if (j != 0) {
          paramParcel = new CrashInfo(paramParcel);
        } else {
          paramParcel = null;
        }
        crashInfo = paramParcel;
        anrInfo = null;
        batteryInfo = null;
        runningServiceInfo = null;
        break;
      }
    }
    else
    {
      batteryInfo = null;
      anrInfo = null;
      crashInfo = null;
      runningServiceInfo = new RunningServiceInfo(paramParcel);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(type);
    paramParcel.writeString(packageName);
    paramParcel.writeString(installerPackageName);
    paramParcel.writeString(processName);
    paramParcel.writeLong(time);
    paramParcel.writeInt(systemApp);
    if (crashInfo != null) {
      i = 1;
    } else {
      i = 0;
    }
    paramParcel.writeInt(i);
    int i = type;
    if (i != 5) {
      switch (i)
      {
      default: 
        break;
      case 3: 
        batteryInfo.writeToParcel(paramParcel, paramInt);
        break;
      case 2: 
        anrInfo.writeToParcel(paramParcel, paramInt);
        break;
      case 1: 
        if (crashInfo == null) {
          break;
        }
        crashInfo.writeToParcel(paramParcel, paramInt);
        break;
      }
    } else {
      runningServiceInfo.writeToParcel(paramParcel, paramInt);
    }
  }
  
  public static class AnrInfo
  {
    public String activity;
    public String cause;
    public String info;
    
    public AnrInfo() {}
    
    public AnrInfo(Parcel paramParcel)
    {
      activity = paramParcel.readString();
      cause = paramParcel.readString();
      info = paramParcel.readString();
    }
    
    public void dump(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("activity: ");
      localStringBuilder.append(activity);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("cause: ");
      localStringBuilder.append(cause);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("info: ");
      localStringBuilder.append(info);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(activity);
      paramParcel.writeString(cause);
      paramParcel.writeString(info);
    }
  }
  
  public static class BatteryInfo
  {
    public String checkinDetails;
    public long durationMicros;
    public String usageDetails;
    public int usagePercent;
    
    public BatteryInfo() {}
    
    public BatteryInfo(Parcel paramParcel)
    {
      usagePercent = paramParcel.readInt();
      durationMicros = paramParcel.readLong();
      usageDetails = paramParcel.readString();
      checkinDetails = paramParcel.readString();
    }
    
    public void dump(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("usagePercent: ");
      localStringBuilder.append(usagePercent);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("durationMicros: ");
      localStringBuilder.append(durationMicros);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("usageDetails: ");
      localStringBuilder.append(usageDetails);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("checkinDetails: ");
      localStringBuilder.append(checkinDetails);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(usagePercent);
      paramParcel.writeLong(durationMicros);
      paramParcel.writeString(usageDetails);
      paramParcel.writeString(checkinDetails);
    }
  }
  
  public static class CrashInfo
  {
    public String exceptionClassName;
    public String exceptionMessage;
    public String stackTrace;
    public String throwClassName;
    public String throwFileName;
    public int throwLineNumber;
    public String throwMethodName;
    
    public CrashInfo() {}
    
    public CrashInfo(Parcel paramParcel)
    {
      exceptionClassName = paramParcel.readString();
      exceptionMessage = paramParcel.readString();
      throwFileName = paramParcel.readString();
      throwClassName = paramParcel.readString();
      throwMethodName = paramParcel.readString();
      throwLineNumber = paramParcel.readInt();
      stackTrace = paramParcel.readString();
    }
    
    public CrashInfo(Throwable paramThrowable)
    {
      Object localObject1 = new StringWriter();
      Object localObject2 = new FastPrintWriter((Writer)localObject1, false, 256);
      paramThrowable.printStackTrace((PrintWriter)localObject2);
      ((PrintWriter)localObject2).flush();
      stackTrace = sanitizeString(((StringWriter)localObject1).toString());
      exceptionMessage = paramThrowable.getMessage();
      localObject1 = paramThrowable;
      localObject2 = paramThrowable;
      paramThrowable = (Throwable)localObject1;
      while (paramThrowable.getCause() != null)
      {
        paramThrowable = paramThrowable.getCause();
        localObject1 = localObject2;
        if (paramThrowable.getStackTrace() != null)
        {
          localObject1 = localObject2;
          if (paramThrowable.getStackTrace().length > 0) {
            localObject1 = paramThrowable;
          }
        }
        localObject2 = paramThrowable.getMessage();
        if ((localObject2 != null) && (((String)localObject2).length() > 0)) {
          exceptionMessage = ((String)localObject2);
        }
        localObject2 = localObject1;
      }
      exceptionClassName = localObject2.getClass().getName();
      if (((Throwable)localObject2).getStackTrace().length > 0)
      {
        paramThrowable = localObject2.getStackTrace()[0];
        throwFileName = paramThrowable.getFileName();
        throwClassName = paramThrowable.getClassName();
        throwMethodName = paramThrowable.getMethodName();
        throwLineNumber = paramThrowable.getLineNumber();
      }
      else
      {
        throwFileName = "unknown";
        throwClassName = "unknown";
        throwMethodName = "unknown";
        throwLineNumber = 0;
      }
      exceptionMessage = sanitizeString(exceptionMessage);
    }
    
    private String sanitizeString(String paramString)
    {
      int i = '⠀' + '⠀';
      if ((paramString != null) && (paramString.length() > i))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("\n[TRUNCATED ");
        localStringBuilder.append(paramString.length() - i);
        localStringBuilder.append(" CHARS]\n");
        String str = localStringBuilder.toString();
        localStringBuilder = new StringBuilder(str.length() + i);
        localStringBuilder.append(paramString.substring(0, 10240));
        localStringBuilder.append(str);
        localStringBuilder.append(paramString.substring(paramString.length() - 10240));
        return localStringBuilder.toString();
      }
      return paramString;
    }
    
    public void appendStackTrace(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(stackTrace);
      localStringBuilder.append(paramString);
      stackTrace = sanitizeString(localStringBuilder.toString());
    }
    
    public void dump(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("exceptionClassName: ");
      localStringBuilder.append(exceptionClassName);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("exceptionMessage: ");
      localStringBuilder.append(exceptionMessage);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("throwFileName: ");
      localStringBuilder.append(throwFileName);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("throwClassName: ");
      localStringBuilder.append(throwClassName);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("throwMethodName: ");
      localStringBuilder.append(throwMethodName);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("throwLineNumber: ");
      localStringBuilder.append(throwLineNumber);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("stackTrace: ");
      localStringBuilder.append(stackTrace);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.dataPosition();
      paramParcel.writeString(exceptionClassName);
      paramParcel.writeString(exceptionMessage);
      paramParcel.writeString(throwFileName);
      paramParcel.writeString(throwClassName);
      paramParcel.writeString(throwMethodName);
      paramParcel.writeInt(throwLineNumber);
      paramParcel.writeString(stackTrace);
      paramParcel.dataPosition();
    }
  }
  
  public static class ParcelableCrashInfo
    extends ApplicationErrorReport.CrashInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<ParcelableCrashInfo> CREATOR = new Parcelable.Creator()
    {
      public ApplicationErrorReport.ParcelableCrashInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ApplicationErrorReport.ParcelableCrashInfo(paramAnonymousParcel);
      }
      
      public ApplicationErrorReport.ParcelableCrashInfo[] newArray(int paramAnonymousInt)
      {
        return new ApplicationErrorReport.ParcelableCrashInfo[paramAnonymousInt];
      }
    };
    
    public ParcelableCrashInfo() {}
    
    public ParcelableCrashInfo(Parcel paramParcel)
    {
      super();
    }
    
    public ParcelableCrashInfo(Throwable paramThrowable)
    {
      super();
    }
    
    public int describeContents()
    {
      return 0;
    }
  }
  
  public static class RunningServiceInfo
  {
    public long durationMillis;
    public String serviceDetails;
    
    public RunningServiceInfo() {}
    
    public RunningServiceInfo(Parcel paramParcel)
    {
      durationMillis = paramParcel.readLong();
      serviceDetails = paramParcel.readString();
    }
    
    public void dump(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("durationMillis: ");
      localStringBuilder.append(durationMillis);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("serviceDetails: ");
      localStringBuilder.append(serviceDetails);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(durationMillis);
      paramParcel.writeString(serviceDetails);
    }
  }
}
