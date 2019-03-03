package android.app;

import android.content.pm.IPackageManager;
import android.os.FileUtils;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.BaseDexClassLoader.Reporter;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class DexLoadReporter
  implements BaseDexClassLoader.Reporter
{
  private static final boolean DEBUG = false;
  private static final DexLoadReporter INSTANCE = new DexLoadReporter();
  private static final String TAG = "DexLoadReporter";
  @GuardedBy("mDataDirs")
  private final Set<String> mDataDirs = new HashSet();
  
  private DexLoadReporter() {}
  
  static DexLoadReporter getInstance()
  {
    return INSTANCE;
  }
  
  private boolean isSecondaryDexFile(String paramString, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (FileUtils.contains(paramArrayOfString[j], paramString)) {
        return true;
      }
    }
    return false;
  }
  
  private void notifyPackageManager(List<BaseDexClassLoader> paramList, List<String> paramList1)
  {
    Object localObject = new ArrayList(paramList1.size());
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      ((List)localObject).add(((BaseDexClassLoader)paramList.next()).getClass().getName());
    }
    paramList = ActivityThread.currentPackageName();
    try
    {
      ActivityThread.getPackageManager().notifyDexLoad(paramList, (List)localObject, paramList1, VMRuntime.getRuntime().vmInstructionSet());
    }
    catch (RemoteException paramList1)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Failed to notify PM about dex load for package ");
      ((StringBuilder)localObject).append(paramList);
      Slog.e("DexLoadReporter", ((StringBuilder)localObject).toString(), paramList1);
    }
  }
  
  private void registerSecondaryDexForProfiling(String paramString, String[] paramArrayOfString)
  {
    if (!isSecondaryDexFile(paramString, paramArrayOfString)) {
      return;
    }
    File localFile = new File(paramString);
    paramArrayOfString = new File(localFile.getParent(), "oat");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(localFile.getName());
    ((StringBuilder)localObject).append(".cur.prof");
    localObject = new File(paramArrayOfString, ((StringBuilder)localObject).toString());
    if ((!paramArrayOfString.exists()) && (!paramArrayOfString.mkdir()))
    {
      paramString = new StringBuilder();
      paramString.append("Could not create the profile directory: ");
      paramString.append(localObject);
      Slog.e("DexLoadReporter", paramString.toString());
      return;
    }
    try
    {
      ((File)localObject).createNewFile();
      VMRuntime.registerAppInfo(((File)localObject).getPath(), new String[] { paramString });
      return;
    }
    catch (IOException localIOException)
    {
      paramArrayOfString = new StringBuilder();
      paramArrayOfString.append("Failed to create profile for secondary dex ");
      paramArrayOfString.append(paramString);
      paramArrayOfString.append(":");
      paramArrayOfString.append(localIOException.getMessage());
      Slog.e("DexLoadReporter", paramArrayOfString.toString());
    }
  }
  
  private void registerSecondaryDexForProfiling(String[] paramArrayOfString)
  {
    int i = 0;
    if (!SystemProperties.getBoolean("dalvik.vm.dexopt.secondary", false)) {
      return;
    }
    synchronized (mDataDirs)
    {
      String[] arrayOfString = (String[])mDataDirs.toArray(new String[0]);
      int j = paramArrayOfString.length;
      while (i < j)
      {
        registerSecondaryDexForProfiling(paramArrayOfString[i], arrayOfString);
        i++;
      }
      return;
    }
  }
  
  void registerAppDataDir(String arg1, String paramString2)
  {
    if (paramString2 != null) {
      synchronized (mDataDirs)
      {
        mDataDirs.add(paramString2);
      }
    }
  }
  
  public void report(List<BaseDexClassLoader> paramList, List<String> paramList1)
  {
    if (paramList.size() != paramList1.size())
    {
      Slog.wtf("DexLoadReporter", "Bad call to DexLoadReporter: argument size mismatch");
      return;
    }
    if (paramList1.isEmpty())
    {
      Slog.wtf("DexLoadReporter", "Bad call to DexLoadReporter: empty dex paths");
      return;
    }
    String[] arrayOfString = ((String)paramList1.get(0)).split(File.pathSeparator);
    if (arrayOfString.length == 0) {
      return;
    }
    notifyPackageManager(paramList, paramList1);
    registerSecondaryDexForProfiling(arrayOfString);
  }
}
