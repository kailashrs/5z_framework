package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.FileObserver;
import android.os.FileUtils;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.provider.Downloads;
import android.text.TextUtils;
import android.util.AtomicFile;
import android.util.EventLog;
import android.util.Slog;
import android.util.StatsLog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FastXmlSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlSerializer;

public class BootReceiver
  extends BroadcastReceiver
{
  private static final String FSCK_FS_MODIFIED = "FILE SYSTEM WAS MODIFIED";
  private static final String FSCK_PASS_PATTERN = "Pass ([1-9]E?):";
  private static final String FSCK_TREE_OPTIMIZATION_PATTERN = "Inode [0-9]+ extent tree.*could be shorter";
  private static final int FS_STAT_FS_FIXED = 1024;
  private static final String FS_STAT_PATTERN = "fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)";
  private static final String LAST_HEADER_FILE = "last-header.txt";
  private static final String[] LAST_KMSG_FILES = { "/sys/fs/pstore/console-ramoops", "/proc/last_kmsg" };
  private static final String LAST_SHUTDOWN_TIME_PATTERN = "powerctl_shutdown_time_ms:([0-9]+):([0-9]+)";
  private static final String LOG_FILES_FILE = "log-files.xml";
  private static final int LOG_SIZE;
  private static final String METRIC_SHUTDOWN_TIME_START = "begin_shutdown";
  private static final String METRIC_SYSTEM_SERVER = "shutdown_system_server";
  private static final String[] MOUNT_DURATION_PROPS_POSTFIX;
  private static final String OLD_UPDATER_CLASS = "com.google.android.systemupdater.SystemUpdateReceiver";
  private static final String OLD_UPDATER_PACKAGE = "com.google.android.systemupdater";
  private static final String SHUTDOWN_METRICS_FILE = "/data/system/shutdown-metrics.txt";
  private static final String SHUTDOWN_TRON_METRICS_PREFIX = "shutdown_";
  private static final String TAG = "BootReceiver";
  private static final String TAG_TOMBSTONE = "SYSTEM_TOMBSTONE";
  private static final File TOMBSTONE_DIR;
  private static final int UMOUNT_STATUS_NOT_AVAILABLE = 4;
  private static final File lastHeaderFile;
  private static final AtomicFile sFile;
  private static FileObserver sTombstoneObserver;
  
  static
  {
    int i;
    if (SystemProperties.getInt("ro.debuggable", 0) == 1) {
      i = 98304;
    } else {
      i = 65536;
    }
    LOG_SIZE = i;
    TOMBSTONE_DIR = new File("/data/tombstones");
    sTombstoneObserver = null;
    sFile = new AtomicFile(new File(Environment.getDataSystemDirectory(), "log-files.xml"), "log-files");
    lastHeaderFile = new File(Environment.getDataSystemDirectory(), "last-header.txt");
    MOUNT_DURATION_PROPS_POSTFIX = new String[] { "early", "default", "late" };
  }
  
  public BootReceiver() {}
  
  private static void addAuditErrorsToDropBox(DropBoxManager paramDropBoxManager, HashMap<String, Long> paramHashMap, String paramString1, int paramInt, String paramString2)
    throws IOException
  {
    if ((paramDropBoxManager != null) && (paramDropBoxManager.isTagEnabled(paramString2)))
    {
      Slog.i("BootReceiver", "Copying audit failures to DropBox");
      Object localObject = new File("/proc/last_kmsg");
      long l1 = ((File)localObject).lastModified();
      long l2 = l1;
      if (l1 <= 0L)
      {
        localObject = new File("/sys/fs/pstore/console-ramoops");
        l1 = ((File)localObject).lastModified();
        l2 = l1;
        if (l1 <= 0L)
        {
          localObject = new File("/sys/fs/pstore/console-ramoops-0");
          l2 = ((File)localObject).lastModified();
        }
      }
      if (l2 <= 0L) {
        return;
      }
      if ((paramHashMap.containsKey(paramString2)) && (((Long)paramHashMap.get(paramString2)).longValue() == l2)) {
        return;
      }
      paramHashMap.put(paramString2, Long.valueOf(l2));
      localObject = FileUtils.readTextFile((File)localObject, paramInt, "[[TRUNCATED]]\n");
      paramHashMap = new StringBuilder();
      for (localObject : ((String)localObject).split("\n")) {
        if (((String)localObject).contains("audit"))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append((String)localObject);
          localStringBuilder.append("\n");
          paramHashMap.append(localStringBuilder.toString());
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Copied ");
      ((StringBuilder)localObject).append(paramHashMap.toString().length());
      ((StringBuilder)localObject).append(" worth of audits to DropBox");
      Slog.i("BootReceiver", ((StringBuilder)localObject).toString());
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString1);
      ((StringBuilder)localObject).append(paramHashMap.toString());
      paramDropBoxManager.addText(paramString2, ((StringBuilder)localObject).toString());
      return;
    }
  }
  
  private static void addFileToDropBox(DropBoxManager paramDropBoxManager, HashMap<String, Long> paramHashMap, String paramString1, String paramString2, int paramInt, String paramString3)
    throws IOException
  {
    addFileWithFootersToDropBox(paramDropBoxManager, paramHashMap, paramString1, "", paramString2, paramInt, paramString3);
  }
  
  private static void addFileWithFootersToDropBox(DropBoxManager paramDropBoxManager, HashMap<String, Long> paramHashMap, String paramString1, String paramString2, String paramString3, int paramInt, String paramString4)
    throws IOException
  {
    if ((paramDropBoxManager != null) && (paramDropBoxManager.isTagEnabled(paramString4)))
    {
      Object localObject = new File(paramString3);
      long l = ((File)localObject).lastModified();
      if (l <= 0L) {
        return;
      }
      if ((paramHashMap.containsKey(paramString3)) && (((Long)paramHashMap.get(paramString3)).longValue() == l)) {
        return;
      }
      paramHashMap.put(paramString3, Long.valueOf(l));
      paramHashMap = FileUtils.readTextFile((File)localObject, paramInt, "[[TRUNCATED]]\n");
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString1);
      ((StringBuilder)localObject).append(paramHashMap);
      ((StringBuilder)localObject).append(paramString2);
      paramString1 = ((StringBuilder)localObject).toString();
      if ((paramString4.equals("SYSTEM_TOMBSTONE")) && (paramHashMap.contains(">>> system_server <<<"))) {
        addTextToDropBox(paramDropBoxManager, "system_server_native_crash", paramString1, paramString3, paramInt);
      }
      addTextToDropBox(paramDropBoxManager, paramString4, paramString1, paramString3, paramInt);
      return;
    }
  }
  
  private static void addFsckErrorsToDropBoxAndLogFsStat(DropBoxManager paramDropBoxManager, HashMap<String, Long> paramHashMap, String paramString1, int paramInt, String paramString2)
    throws IOException
  {
    int i = 1;
    if ((paramDropBoxManager != null) && (paramDropBoxManager.isTagEnabled(paramString2))) {}
    for (;;)
    {
      break;
      i = 0;
    }
    Slog.i("BootReceiver", "Checking for fsck errors");
    File localFile = new File("/dev/fscklogs/log");
    if (localFile.lastModified() <= 0L) {
      return;
    }
    String str = FileUtils.readTextFile(localFile, paramInt, "[[TRUNCATED]]\n");
    Pattern localPattern = Pattern.compile("fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)");
    String[] arrayOfString = str.split("\n");
    int j = 0;
    int k = arrayOfString.length;
    int m = 0;
    int n = 0;
    int i1 = 0;
    while (m < k)
    {
      str = arrayOfString[m];
      if (str.contains("FILE SYSTEM WAS MODIFIED")) {
        n = 1;
      }
      for (;;)
      {
        break;
        if (str.contains("fs_stat"))
        {
          Object localObject = localPattern.matcher(str);
          if (((Matcher)localObject).find())
          {
            handleFsckFsStat((Matcher)localObject, arrayOfString, j, i1);
            j = i1;
          }
          else
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("cannot parse fs_stat:");
            ((StringBuilder)localObject).append(str);
            Slog.w("BootReceiver", ((StringBuilder)localObject).toString());
          }
        }
      }
      i1++;
      m++;
    }
    if ((i != 0) && (n != 0)) {
      addFileToDropBox(paramDropBoxManager, paramHashMap, paramString1, "/dev/fscklogs/log", paramInt, paramString2);
    }
    localFile.delete();
  }
  
  private static void addTextToDropBox(DropBoxManager paramDropBoxManager, String paramString1, String paramString2, String paramString3, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Copying ");
    localStringBuilder.append(paramString3);
    localStringBuilder.append(" to DropBox (");
    localStringBuilder.append(paramString1);
    localStringBuilder.append(")");
    Slog.i("BootReceiver", localStringBuilder.toString());
    paramDropBoxManager.addText(paramString1, paramString2);
    EventLog.writeEvent(81002, new Object[] { paramString3, Integer.valueOf(paramInt), paramString1 });
  }
  
  @VisibleForTesting
  public static int fixFsckFsStat(String paramString, int paramInt1, String[] paramArrayOfString, int paramInt2, int paramInt3)
  {
    int i = paramInt1;
    if ((i & 0x400) != 0)
    {
      Pattern localPattern1 = Pattern.compile("Pass ([1-9]E?):");
      Pattern localPattern2 = Pattern.compile("Inode [0-9]+ extent tree.*could be shorter");
      int j = 0;
      Object localObject1 = null;
      int k = 0;
      int m = 0;
      int n = 0;
      Object localObject2 = "";
      paramInt1 = paramInt2;
      paramInt2 = m;
      while (paramInt1 < paramInt3)
      {
        Object localObject3 = paramArrayOfString[paramInt1];
        if (((String)localObject3).contains("FILE SYSTEM WAS MODIFIED"))
        {
          paramInt1 = j;
          paramArrayOfString = localObject1;
          break label631;
        }
        Object localObject4;
        int i1;
        int i2;
        int i3;
        if (((String)localObject3).startsWith("Pass "))
        {
          localObject3 = localPattern1.matcher((CharSequence)localObject3);
          if (((Matcher)localObject3).find()) {
            localObject2 = ((Matcher)localObject3).group(1);
          }
          m = paramInt1;
          localObject4 = localObject2;
          i1 = n;
          i2 = paramInt2;
          i3 = k;
        }
        else if (((String)localObject3).startsWith("Inode "))
        {
          if ((localPattern2.matcher((CharSequence)localObject3).find()) && (((String)localObject2).equals("1")))
          {
            i1 = 1;
            localObject4 = new StringBuilder();
            ((StringBuilder)localObject4).append("fs_stat, partition:");
            ((StringBuilder)localObject4).append(paramString);
            ((StringBuilder)localObject4).append(" found tree optimization:");
            ((StringBuilder)localObject4).append((String)localObject3);
            Slog.i("BootReceiver", ((StringBuilder)localObject4).toString());
            m = paramInt1;
            localObject4 = localObject2;
            i2 = paramInt2;
            i3 = k;
          }
          else
          {
            paramInt1 = 1;
            paramArrayOfString = (String[])localObject3;
            break label631;
          }
        }
        else if ((((String)localObject3).startsWith("[QUOTA WARNING]")) && (((String)localObject2).equals("5")))
        {
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("fs_stat, partition:");
          ((StringBuilder)localObject4).append(paramString);
          ((StringBuilder)localObject4).append(" found quota warning:");
          ((StringBuilder)localObject4).append((String)localObject3);
          Slog.i("BootReceiver", ((StringBuilder)localObject4).toString());
          paramInt2 = 1;
          i2 = 1;
          m = paramInt1;
          localObject4 = localObject2;
          i1 = n;
          i3 = k;
          if (n == 0)
          {
            paramArrayOfString = (String[])localObject3;
            paramInt1 = j;
            break label631;
          }
        }
        else if ((((String)localObject3).startsWith("Update quota info")) && (((String)localObject2).equals("5")))
        {
          m = paramInt1;
          localObject4 = localObject2;
          i1 = n;
          i2 = paramInt2;
          i3 = k;
        }
        else if ((((String)localObject3).startsWith("Timestamp(s) on inode")) && (((String)localObject3).contains("beyond 2310-04-04 are likely pre-1970")) && (((String)localObject2).equals("1")))
        {
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("fs_stat, partition:");
          ((StringBuilder)localObject4).append(paramString);
          ((StringBuilder)localObject4).append(" found timestamp adjustment:");
          ((StringBuilder)localObject4).append((String)localObject3);
          Slog.i("BootReceiver", ((StringBuilder)localObject4).toString());
          k = paramInt1;
          if (paramArrayOfString[(paramInt1 + 1)].contains("Fix? yes")) {
            k = paramInt1 + 1;
          }
          i3 = 1;
          m = k;
          localObject4 = localObject2;
          i1 = n;
          i2 = paramInt2;
        }
        else
        {
          localObject3 = ((String)localObject3).trim();
          m = paramInt1;
          localObject4 = localObject2;
          i1 = n;
          i2 = paramInt2;
          i3 = k;
          if (!((String)localObject3).isEmpty())
          {
            m = paramInt1;
            localObject4 = localObject2;
            i1 = n;
            i2 = paramInt2;
            i3 = k;
            if (!((String)localObject2).isEmpty())
            {
              paramInt1 = 1;
              paramArrayOfString = (String[])localObject3;
              break label631;
            }
          }
        }
        paramInt1 = m + 1;
        localObject2 = localObject4;
        n = i1;
        paramInt2 = i2;
        k = i3;
      }
      paramArrayOfString = localObject1;
      paramInt1 = j;
      label631:
      if (paramInt1 != 0)
      {
        paramInt1 = i;
        if (paramArrayOfString != null)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("fs_stat, partition:");
          ((StringBuilder)localObject2).append(paramString);
          ((StringBuilder)localObject2).append(" fix:");
          ((StringBuilder)localObject2).append(paramArrayOfString);
          Slog.i("BootReceiver", ((StringBuilder)localObject2).toString());
          paramInt1 = i;
        }
      }
      else if ((paramInt2 != 0) && (n == 0))
      {
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("fs_stat, got quota fix without tree optimization, partition:");
        paramArrayOfString.append(paramString);
        Slog.i("BootReceiver", paramArrayOfString.toString());
        paramInt1 = i;
      }
      else if ((n == 0) || (paramInt2 == 0))
      {
        paramInt1 = i;
        if (k == 0) {}
      }
      else
      {
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("fs_stat, partition:");
        paramArrayOfString.append(paramString);
        paramArrayOfString.append(" fix ignored");
        Slog.i("BootReceiver", paramArrayOfString.toString());
        paramInt1 = i & 0xFBFF;
      }
    }
    else
    {
      paramInt1 = i;
    }
    return paramInt1;
  }
  
  private String getBootHeadersToLogAndUpdate()
    throws IOException
  {
    Object localObject1 = getPreviousBootHeaders();
    Object localObject2 = getCurrentBootHeaders();
    try
    {
      FileUtils.stringToFile(lastHeaderFile, (String)localObject2);
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error writing ");
      localStringBuilder.append(lastHeaderFile);
      Slog.e("BootReceiver", localStringBuilder.toString(), localIOException);
    }
    if (localObject1 == null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("isPrevious: false\n");
      ((StringBuilder)localObject1).append((String)localObject2);
      return ((StringBuilder)localObject1).toString();
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("isPrevious: true\n");
    ((StringBuilder)localObject2).append((String)localObject1);
    return ((StringBuilder)localObject2).toString();
  }
  
  private String getCurrentBootHeaders()
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder(512);
    localStringBuilder.append("Build: ");
    localStringBuilder.append(Build.FINGERPRINT);
    localStringBuilder.append("\n");
    localStringBuilder.append("Hardware: ");
    localStringBuilder.append(Build.BOARD);
    localStringBuilder.append("\n");
    localStringBuilder.append("Revision: ");
    localStringBuilder.append(SystemProperties.get("ro.revision", ""));
    localStringBuilder.append("\n");
    localStringBuilder.append("Bootloader: ");
    localStringBuilder.append(Build.BOOTLOADER);
    localStringBuilder.append("\n");
    localStringBuilder.append("Radio: ");
    localStringBuilder.append(Build.getRadioVersion());
    localStringBuilder.append("\n");
    localStringBuilder.append("Kernel: ");
    localStringBuilder.append(FileUtils.readTextFile(new File("/proc/version"), 1024, "...\n"));
    localStringBuilder.append("\n");
    return localStringBuilder.toString();
  }
  
  private String getPreviousBootHeaders()
  {
    try
    {
      String str = FileUtils.readTextFile(lastHeaderFile, 0, null);
      return str;
    }
    catch (IOException localIOException) {}
    return null;
  }
  
  private static void handleFsckFsStat(Matcher paramMatcher, String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    String str = paramMatcher.group(1);
    try
    {
      int i = Integer.decode(paramMatcher.group(2)).intValue();
      paramInt1 = fixFsckFsStat(str, i, paramArrayOfString, paramInt1, paramInt2);
      paramMatcher = new StringBuilder();
      paramMatcher.append("boot_fs_stat_");
      paramMatcher.append(str);
      MetricsLogger.histogram(null, paramMatcher.toString(), paramInt1);
      paramMatcher = new StringBuilder();
      paramMatcher.append("fs_stat, partition:");
      paramMatcher.append(str);
      paramMatcher.append(" stat:0x");
      paramMatcher.append(Integer.toHexString(paramInt1));
      Slog.i("BootReceiver", paramMatcher.toString());
      return;
    }
    catch (NumberFormatException paramArrayOfString)
    {
      paramArrayOfString = new StringBuilder();
      paramArrayOfString.append("cannot parse fs_stat: partition:");
      paramArrayOfString.append(str);
      paramArrayOfString.append(" stat:");
      paramArrayOfString.append(paramMatcher.group(2));
      Slog.w("BootReceiver", paramArrayOfString.toString());
    }
  }
  
  private void logBootEvents(Context paramContext)
    throws IOException
  {
    final DropBoxManager localDropBoxManager = (DropBoxManager)paramContext.getSystemService("dropbox");
    final String str1 = getBootHeadersToLogAndUpdate();
    Object localObject = SystemProperties.get("ro.boot.bootreason", null);
    String str2 = RecoverySystem.handleAftermath(paramContext);
    if ((str2 != null) && (localDropBoxManager != null))
    {
      paramContext = new StringBuilder();
      paramContext.append(str1);
      paramContext.append(str2);
      localDropBoxManager.addText("SYSTEM_RECOVERY_LOG", paramContext.toString());
    }
    paramContext = "";
    if (localObject != null)
    {
      paramContext = new StringBuilder(512);
      paramContext.append("\n");
      paramContext.append("Boot info:\n");
      paramContext.append("Last boot reason: ");
      paramContext.append((String)localObject);
      paramContext.append("\n");
      paramContext = paramContext.toString();
    }
    localObject = readTimestamps();
    if (SystemProperties.getLong("ro.runtime.firstboot", 0L) == 0L)
    {
      if (!StorageManager.inCryptKeeperBounce()) {
        SystemProperties.set("ro.runtime.firstboot", Long.toString(System.currentTimeMillis()));
      }
      if (localDropBoxManager != null) {
        localDropBoxManager.addText("SYSTEM_BOOT", str1);
      }
      addFileWithFootersToDropBox(localDropBoxManager, (HashMap)localObject, str1, paramContext, "/proc/last_kmsg", -LOG_SIZE, "SYSTEM_LAST_KMSG");
      addFileWithFootersToDropBox(localDropBoxManager, (HashMap)localObject, str1, paramContext, "/sys/fs/pstore/console-ramoops", -LOG_SIZE, "SYSTEM_LAST_KMSG");
      addFileWithFootersToDropBox(localDropBoxManager, (HashMap)localObject, str1, paramContext, "/sys/fs/pstore/console-ramoops-0", -LOG_SIZE, "SYSTEM_LAST_KMSG");
      addFileToDropBox(localDropBoxManager, (HashMap)localObject, str1, "/cache/recovery/log", -LOG_SIZE, "SYSTEM_RECOVERY_LOG");
      addFileToDropBox(localDropBoxManager, (HashMap)localObject, str1, "/cache/recovery/last_kmsg", -LOG_SIZE, "SYSTEM_RECOVERY_KMSG");
      addAuditErrorsToDropBox(localDropBoxManager, (HashMap)localObject, str1, -LOG_SIZE, "SYSTEM_AUDIT");
    }
    else if (localDropBoxManager != null)
    {
      localDropBoxManager.addText("SYSTEM_RESTART", str1);
    }
    logFsShutdownTime();
    logFsMountTime();
    addFsckErrorsToDropBoxAndLogFsStat(localDropBoxManager, (HashMap)localObject, str1, -LOG_SIZE, "SYSTEM_FSCK");
    logSystemServerShutdownTimeMetrics();
    paramContext = TOMBSTONE_DIR.listFiles();
    for (int i = 0; (paramContext != null) && (i < paramContext.length); i++) {
      if (paramContext[i].isFile()) {
        addFileToDropBox(localDropBoxManager, (HashMap)localObject, str1, paramContext[i].getPath(), LOG_SIZE, "SYSTEM_TOMBSTONE");
      }
    }
    writeTimestamps((HashMap)localObject);
    sTombstoneObserver = new FileObserver(TOMBSTONE_DIR.getPath(), 8)
    {
      public void onEvent(int paramAnonymousInt, String paramAnonymousString)
      {
        HashMap localHashMap = BootReceiver.access$200();
        try
        {
          File localFile = new java/io/File;
          localFile.<init>(BootReceiver.TOMBSTONE_DIR, paramAnonymousString);
          if (localFile.isFile()) {
            BootReceiver.addFileToDropBox(localDropBoxManager, localHashMap, str1, localFile.getPath(), BootReceiver.LOG_SIZE, "SYSTEM_TOMBSTONE");
          }
        }
        catch (IOException paramAnonymousString)
        {
          Slog.e("BootReceiver", "Can't log tombstone", paramAnonymousString);
        }
        BootReceiver.this.writeTimestamps(localHashMap);
      }
    };
    sTombstoneObserver.startWatching();
  }
  
  private static void logFsMountTime()
  {
    for (String str : MOUNT_DURATION_PROPS_POSTFIX)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ro.boottime.init.mount_all.");
      localStringBuilder.append(str);
      int k = SystemProperties.getInt(localStringBuilder.toString(), 0);
      if (k != 0)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("boot_mount_all_duration_");
        localStringBuilder.append(str);
        MetricsLogger.histogram(null, localStringBuilder.toString(), k);
      }
    }
  }
  
  private static void logFsShutdownTime()
  {
    Matcher localMatcher = null;
    String[] arrayOfString = LAST_KMSG_FILES;
    int i = arrayOfString.length;
    Object localObject;
    for (int j = 0;; j++)
    {
      localObject = localMatcher;
      if (j >= i) {
        break;
      }
      localObject = new File(arrayOfString[j]);
      if (((File)localObject).exists()) {
        break;
      }
    }
    if (localObject == null) {
      return;
    }
    try
    {
      localObject = FileUtils.readTextFile((File)localObject, 49152, null);
      localMatcher = Pattern.compile("powerctl_shutdown_time_ms:([0-9]+):([0-9]+)", 8).matcher((CharSequence)localObject);
      if (localMatcher.find())
      {
        MetricsLogger.histogram(null, "boot_fs_shutdown_duration", Integer.parseInt(localMatcher.group(1)));
        MetricsLogger.histogram(null, "boot_fs_shutdown_umount_stat", Integer.parseInt(localMatcher.group(2)));
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("boot_fs_shutdown,");
        ((StringBuilder)localObject).append(localMatcher.group(1));
        ((StringBuilder)localObject).append(",");
        ((StringBuilder)localObject).append(localMatcher.group(2));
        Slog.i("BootReceiver", ((StringBuilder)localObject).toString());
      }
      else
      {
        MetricsLogger.histogram(null, "boot_fs_shutdown_umount_stat", 4);
        Slog.w("BootReceiver", "boot_fs_shutdown, string not found");
      }
      return;
    }
    catch (IOException localIOException)
    {
      Slog.w("BootReceiver", "cannot read last msg", localIOException);
    }
  }
  
  private static void logStatsdShutdownAtom(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    boolean bool1 = false;
    String str = "<EMPTY>";
    long l1 = 0L;
    long l2 = 0L;
    boolean bool2;
    if (paramString1 != null)
    {
      if (paramString1.equals("y"))
      {
        bool2 = true;
      }
      else
      {
        bool2 = bool1;
        if (!paramString1.equals("n"))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unexpected value for reboot : ");
          localStringBuilder.append(paramString1);
          Slog.e("BootReceiver", localStringBuilder.toString());
          bool2 = bool1;
        }
      }
    }
    else
    {
      Slog.e("BootReceiver", "No value received for reboot");
      bool2 = bool1;
    }
    if (paramString2 != null)
    {
      paramString1 = paramString2;
    }
    else
    {
      Slog.e("BootReceiver", "No value received for shutdown reason");
      paramString1 = str;
    }
    if (paramString3 != null)
    {
      try
      {
        l3 = Long.parseLong(paramString3);
        l1 = l3;
      }
      catch (NumberFormatException paramString2)
      {
        for (;;)
        {
          paramString2 = new StringBuilder();
          paramString2.append("Cannot parse shutdown start time: ");
          paramString2.append(paramString3);
          Slog.e("BootReceiver", paramString2.toString());
        }
      }
      l3 = l1;
      break label195;
    }
    Slog.e("BootReceiver", "No value received for shutdown start time");
    long l3 = l1;
    label195:
    if (paramString4 != null)
    {
      try
      {
        l1 = Long.parseLong(paramString4);
      }
      catch (NumberFormatException paramString2)
      {
        for (;;)
        {
          paramString2 = new StringBuilder();
          paramString2.append("Cannot parse shutdown duration: ");
          paramString2.append(paramString3);
          Slog.e("BootReceiver", paramString2.toString());
          l1 = l2;
        }
      }
    }
    else
    {
      Slog.e("BootReceiver", "No value received for shutdown duration");
      l1 = l2;
    }
    StatsLog.write(56, bool2, paramString1, l3, l1);
  }
  
  private static void logSystemServerShutdownTimeMetrics()
  {
    File localFile = new File("/data/system/shutdown-metrics.txt");
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (localFile.exists()) {
      try
      {
        localObject2 = FileUtils.readTextFile(localFile, 0, null);
      }
      catch (IOException localIOException)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Problem reading ");
        ((StringBuilder)localObject2).append(localFile);
        Slog.e("BootReceiver", ((StringBuilder)localObject2).toString(), localIOException);
        localObject2 = localObject1;
      }
    }
    if (!TextUtils.isEmpty((CharSequence)localObject2))
    {
      String[] arrayOfString1 = ((String)localObject2).split(",");
      int i = arrayOfString1.length;
      Object localObject3 = null;
      String str = null;
      Object localObject4 = null;
      Object localObject5 = null;
      int j = 0;
      while (j < i)
      {
        String[] arrayOfString2 = arrayOfString1[j].split(":");
        Object localObject6;
        Object localObject7;
        if (arrayOfString2.length != 2)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Wrong format of shutdown metrics - ");
          ((StringBuilder)localObject1).append((String)localObject2);
          Slog.e("BootReceiver", ((StringBuilder)localObject1).toString());
          localObject6 = localObject5;
          localObject7 = localObject4;
        }
        else
        {
          localObject1 = localObject3;
          if (arrayOfString2[0].startsWith("shutdown_"))
          {
            logTronShutdownMetric(arrayOfString2[0], arrayOfString2[1]);
            localObject1 = localObject3;
            if (arrayOfString2[0].equals("shutdown_system_server")) {
              localObject1 = arrayOfString2[1];
            }
          }
          if (arrayOfString2[0].equals("reboot"))
          {
            localObject6 = arrayOfString2[1];
            localObject7 = localObject4;
            localObject3 = localObject1;
          }
          else if (arrayOfString2[0].equals("reason"))
          {
            localObject7 = arrayOfString2[1];
            localObject6 = localObject5;
            localObject3 = localObject1;
          }
          else
          {
            localObject6 = localObject5;
            localObject7 = localObject4;
            localObject3 = localObject1;
            if (arrayOfString2[0].equals("begin_shutdown"))
            {
              str = arrayOfString2[1];
              localObject3 = localObject1;
              localObject7 = localObject4;
              localObject6 = localObject5;
            }
          }
        }
        j++;
        localObject5 = localObject6;
        localObject4 = localObject7;
      }
      logStatsdShutdownAtom(localObject5, localObject4, str, localObject3);
    }
    localFile.delete();
  }
  
  private static void logTronShutdownMetric(String paramString1, String paramString2)
  {
    try
    {
      int i = Integer.parseInt(paramString2);
      if (i >= 0) {
        MetricsLogger.histogram(null, paramString1, i);
      }
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Cannot parse metric ");
      localStringBuilder.append(paramString1);
      localStringBuilder.append(" int value - ");
      localStringBuilder.append(paramString2);
      Slog.e("BootReceiver", localStringBuilder.toString());
    }
  }
  
  /* Error */
  private static HashMap<String, Long> readTimestamps()
  {
    // Byte code:
    //   0: getstatic 112	com/android/server/BootReceiver:sFile	Landroid/util/AtomicFile;
    //   3: astore_0
    //   4: aload_0
    //   5: monitorenter
    //   6: new 189	java/util/HashMap
    //   9: astore_1
    //   10: aload_1
    //   11: invokespecial 658	java/util/HashMap:<init>	()V
    //   14: iconst_0
    //   15: istore_2
    //   16: iconst_0
    //   17: istore_3
    //   18: iconst_0
    //   19: istore 4
    //   21: iconst_0
    //   22: istore 5
    //   24: iconst_0
    //   25: istore 6
    //   27: iconst_0
    //   28: istore 7
    //   30: iload 7
    //   32: istore 8
    //   34: iload_2
    //   35: istore 9
    //   37: iload_3
    //   38: istore 10
    //   40: iload 4
    //   42: istore 11
    //   44: iload 5
    //   46: istore 12
    //   48: iload 6
    //   50: istore 13
    //   52: getstatic 112	com/android/server/BootReceiver:sFile	Landroid/util/AtomicFile;
    //   55: invokevirtual 662	android/util/AtomicFile:openRead	()Ljava/io/FileInputStream;
    //   58: astore 14
    //   60: aconst_null
    //   61: astore 15
    //   63: aload 15
    //   65: astore 16
    //   67: invokestatic 668	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   70: astore 17
    //   72: aload 15
    //   74: astore 16
    //   76: aload 17
    //   78: aload 14
    //   80: getstatic 674	java/nio/charset/StandardCharsets:UTF_8	Ljava/nio/charset/Charset;
    //   83: invokevirtual 679	java/nio/charset/Charset:name	()Ljava/lang/String;
    //   86: invokeinterface 685 3 0
    //   91: aload 15
    //   93: astore 16
    //   95: aload 17
    //   97: invokeinterface 688 1 0
    //   102: istore 8
    //   104: iload 8
    //   106: iconst_2
    //   107: if_icmpeq +12 -> 119
    //   110: iload 8
    //   112: iconst_1
    //   113: if_icmpeq +6 -> 119
    //   116: goto -25 -> 91
    //   119: iload 8
    //   121: iconst_2
    //   122: if_icmpne +249 -> 371
    //   125: aload 15
    //   127: astore 16
    //   129: aload 17
    //   131: invokeinterface 691 1 0
    //   136: istore 8
    //   138: aload 15
    //   140: astore 16
    //   142: aload 17
    //   144: invokeinterface 688 1 0
    //   149: istore 9
    //   151: iload 9
    //   153: iconst_1
    //   154: if_icmpeq +178 -> 332
    //   157: iload 9
    //   159: iconst_3
    //   160: if_icmpne +19 -> 179
    //   163: aload 15
    //   165: astore 16
    //   167: aload 17
    //   169: invokeinterface 691 1 0
    //   174: iload 8
    //   176: if_icmple +156 -> 332
    //   179: iload 9
    //   181: iconst_3
    //   182: if_icmpeq -44 -> 138
    //   185: iload 9
    //   187: iconst_4
    //   188: if_icmpne +6 -> 194
    //   191: goto -53 -> 138
    //   194: aload 15
    //   196: astore 16
    //   198: aload 17
    //   200: invokeinterface 694 1 0
    //   205: ldc_w 696
    //   208: invokevirtual 267	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   211: ifeq +46 -> 257
    //   214: aload 15
    //   216: astore 16
    //   218: aload_1
    //   219: aload 17
    //   221: aconst_null
    //   222: ldc_w 698
    //   225: invokeinterface 701 3 0
    //   230: aload 17
    //   232: aconst_null
    //   233: ldc_w 703
    //   236: invokeinterface 701 3 0
    //   241: invokestatic 706	java/lang/Long:valueOf	(Ljava/lang/String;)Ljava/lang/Long;
    //   244: invokevirtual 202	java/lang/Long:longValue	()J
    //   247: invokestatic 206	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   250: invokevirtual 210	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   253: pop
    //   254: goto +75 -> 329
    //   257: aload 15
    //   259: astore 16
    //   261: new 220	java/lang/StringBuilder
    //   264: astore 18
    //   266: aload 15
    //   268: astore 16
    //   270: aload 18
    //   272: invokespecial 221	java/lang/StringBuilder:<init>	()V
    //   275: aload 15
    //   277: astore 16
    //   279: aload 18
    //   281: ldc_w 708
    //   284: invokevirtual 237	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   287: pop
    //   288: aload 15
    //   290: astore 16
    //   292: aload 18
    //   294: aload 17
    //   296: invokeinterface 694 1 0
    //   301: invokevirtual 237	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   304: pop
    //   305: aload 15
    //   307: astore 16
    //   309: ldc 58
    //   311: aload 18
    //   313: invokevirtual 241	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   316: invokestatic 308	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   319: pop
    //   320: aload 15
    //   322: astore 16
    //   324: aload 17
    //   326: invokestatic 714	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   329: goto -191 -> 138
    //   332: iconst_1
    //   333: istore 9
    //   335: iconst_1
    //   336: istore 10
    //   338: iconst_1
    //   339: istore 11
    //   341: iconst_1
    //   342: istore 12
    //   344: iconst_1
    //   345: istore 13
    //   347: iconst_1
    //   348: istore 8
    //   350: aload 14
    //   352: ifnull +8 -> 360
    //   355: aload 14
    //   357: invokevirtual 719	java/io/FileInputStream:close	()V
    //   360: iconst_1
    //   361: ifne +540 -> 901
    //   364: aload_1
    //   365: invokevirtual 722	java/util/HashMap:clear	()V
    //   368: goto +533 -> 901
    //   371: aload 15
    //   373: astore 16
    //   375: new 651	java/lang/IllegalStateException
    //   378: astore 18
    //   380: aload 15
    //   382: astore 16
    //   384: aload 18
    //   386: ldc_w 724
    //   389: invokespecial 725	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   392: aload 15
    //   394: astore 16
    //   396: aload 18
    //   398: athrow
    //   399: astore 15
    //   401: goto +12 -> 413
    //   404: astore 15
    //   406: aload 15
    //   408: astore 16
    //   410: aload 15
    //   412: athrow
    //   413: aload 14
    //   415: ifnull +99 -> 514
    //   418: aload 16
    //   420: ifnull +67 -> 487
    //   423: iload 7
    //   425: istore 8
    //   427: iload_2
    //   428: istore 9
    //   430: iload_3
    //   431: istore 10
    //   433: iload 4
    //   435: istore 11
    //   437: iload 5
    //   439: istore 12
    //   441: iload 6
    //   443: istore 13
    //   445: aload 14
    //   447: invokevirtual 719	java/io/FileInputStream:close	()V
    //   450: goto +64 -> 514
    //   453: astore 14
    //   455: iload 7
    //   457: istore 8
    //   459: iload_2
    //   460: istore 9
    //   462: iload_3
    //   463: istore 10
    //   465: iload 4
    //   467: istore 11
    //   469: iload 5
    //   471: istore 12
    //   473: iload 6
    //   475: istore 13
    //   477: aload 16
    //   479: aload 14
    //   481: invokevirtual 729	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   484: goto +30 -> 514
    //   487: iload 7
    //   489: istore 8
    //   491: iload_2
    //   492: istore 9
    //   494: iload_3
    //   495: istore 10
    //   497: iload 4
    //   499: istore 11
    //   501: iload 5
    //   503: istore 12
    //   505: iload 6
    //   507: istore 13
    //   509: aload 14
    //   511: invokevirtual 719	java/io/FileInputStream:close	()V
    //   514: iload 7
    //   516: istore 8
    //   518: iload_2
    //   519: istore 9
    //   521: iload_3
    //   522: istore 10
    //   524: iload 4
    //   526: istore 11
    //   528: iload 5
    //   530: istore 12
    //   532: iload 6
    //   534: istore 13
    //   536: aload 15
    //   538: athrow
    //   539: astore 16
    //   541: goto +364 -> 905
    //   544: astore 15
    //   546: iload 9
    //   548: istore 8
    //   550: new 220	java/lang/StringBuilder
    //   553: astore 16
    //   555: iload 9
    //   557: istore 8
    //   559: aload 16
    //   561: invokespecial 221	java/lang/StringBuilder:<init>	()V
    //   564: iload 9
    //   566: istore 8
    //   568: aload 16
    //   570: ldc_w 731
    //   573: invokevirtual 237	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   576: pop
    //   577: iload 9
    //   579: istore 8
    //   581: aload 16
    //   583: aload 15
    //   585: invokevirtual 395	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   588: pop
    //   589: iload 9
    //   591: istore 8
    //   593: ldc 58
    //   595: aload 16
    //   597: invokevirtual 241	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   600: invokestatic 308	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   603: pop
    //   604: iload 9
    //   606: ifne +295 -> 901
    //   609: goto -245 -> 364
    //   612: astore 16
    //   614: iload 10
    //   616: istore 8
    //   618: new 220	java/lang/StringBuilder
    //   621: astore 15
    //   623: iload 10
    //   625: istore 8
    //   627: aload 15
    //   629: invokespecial 221	java/lang/StringBuilder:<init>	()V
    //   632: iload 10
    //   634: istore 8
    //   636: aload 15
    //   638: ldc_w 731
    //   641: invokevirtual 237	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   644: pop
    //   645: iload 10
    //   647: istore 8
    //   649: aload 15
    //   651: aload 16
    //   653: invokevirtual 395	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   656: pop
    //   657: iload 10
    //   659: istore 8
    //   661: ldc 58
    //   663: aload 15
    //   665: invokevirtual 241	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   668: invokestatic 308	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   671: pop
    //   672: iload 10
    //   674: ifne +227 -> 901
    //   677: goto -313 -> 364
    //   680: astore 15
    //   682: iload 11
    //   684: istore 8
    //   686: new 220	java/lang/StringBuilder
    //   689: astore 16
    //   691: iload 11
    //   693: istore 8
    //   695: aload 16
    //   697: invokespecial 221	java/lang/StringBuilder:<init>	()V
    //   700: iload 11
    //   702: istore 8
    //   704: aload 16
    //   706: ldc_w 731
    //   709: invokevirtual 237	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   712: pop
    //   713: iload 11
    //   715: istore 8
    //   717: aload 16
    //   719: aload 15
    //   721: invokevirtual 395	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   724: pop
    //   725: iload 11
    //   727: istore 8
    //   729: ldc 58
    //   731: aload 16
    //   733: invokevirtual 241	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   736: invokestatic 308	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   739: pop
    //   740: iload 11
    //   742: ifne +159 -> 901
    //   745: goto -381 -> 364
    //   748: astore 15
    //   750: iload 12
    //   752: istore 8
    //   754: new 220	java/lang/StringBuilder
    //   757: astore 16
    //   759: iload 12
    //   761: istore 8
    //   763: aload 16
    //   765: invokespecial 221	java/lang/StringBuilder:<init>	()V
    //   768: iload 12
    //   770: istore 8
    //   772: aload 16
    //   774: ldc_w 731
    //   777: invokevirtual 237	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   780: pop
    //   781: iload 12
    //   783: istore 8
    //   785: aload 16
    //   787: aload 15
    //   789: invokevirtual 395	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   792: pop
    //   793: iload 12
    //   795: istore 8
    //   797: ldc 58
    //   799: aload 16
    //   801: invokevirtual 241	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   804: invokestatic 308	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   807: pop
    //   808: iload 12
    //   810: ifne +91 -> 901
    //   813: goto -449 -> 364
    //   816: astore 16
    //   818: iload 13
    //   820: istore 8
    //   822: new 220	java/lang/StringBuilder
    //   825: astore 16
    //   827: iload 13
    //   829: istore 8
    //   831: aload 16
    //   833: invokespecial 221	java/lang/StringBuilder:<init>	()V
    //   836: iload 13
    //   838: istore 8
    //   840: aload 16
    //   842: ldc_w 733
    //   845: invokevirtual 237	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   848: pop
    //   849: iload 13
    //   851: istore 8
    //   853: aload 16
    //   855: getstatic 112	com/android/server/BootReceiver:sFile	Landroid/util/AtomicFile;
    //   858: invokevirtual 736	android/util/AtomicFile:getBaseFile	()Ljava/io/File;
    //   861: invokevirtual 395	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   864: pop
    //   865: iload 13
    //   867: istore 8
    //   869: aload 16
    //   871: ldc_w 738
    //   874: invokevirtual 237	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   877: pop
    //   878: iload 13
    //   880: istore 8
    //   882: ldc 58
    //   884: aload 16
    //   886: invokevirtual 241	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   889: invokestatic 181	android/util/Slog:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   892: pop
    //   893: iload 13
    //   895: ifne +6 -> 901
    //   898: goto -534 -> 364
    //   901: aload_0
    //   902: monitorexit
    //   903: aload_1
    //   904: areturn
    //   905: iload 8
    //   907: ifne +7 -> 914
    //   910: aload_1
    //   911: invokevirtual 722	java/util/HashMap:clear	()V
    //   914: aload 16
    //   916: athrow
    //   917: astore 16
    //   919: aload_0
    //   920: monitorexit
    //   921: aload 16
    //   923: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   3	917	0	localAtomicFile	AtomicFile
    //   9	902	1	localHashMap	HashMap
    //   15	504	2	i	int
    //   17	505	3	j	int
    //   19	506	4	k	int
    //   22	507	5	m	int
    //   25	508	6	n	int
    //   28	487	7	i1	int
    //   32	874	8	i2	int
    //   35	570	9	i3	int
    //   38	635	10	i4	int
    //   42	699	11	i5	int
    //   46	763	12	i6	int
    //   50	844	13	i7	int
    //   58	388	14	localFileInputStream	java.io.FileInputStream
    //   453	57	14	localThrowable1	Throwable
    //   61	332	15	localObject1	Object
    //   399	1	15	localObject2	Object
    //   404	133	15	localThrowable2	Throwable
    //   544	40	15	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    //   621	43	15	localStringBuilder1	StringBuilder
    //   680	40	15	localIllegalStateException	IllegalStateException
    //   748	40	15	localIOException	IOException
    //   65	413	16	localObject3	Object
    //   539	1	16	localObject4	Object
    //   553	43	16	localStringBuilder2	StringBuilder
    //   612	40	16	localNullPointerException	NullPointerException
    //   689	111	16	localStringBuilder3	StringBuilder
    //   816	1	16	localFileNotFoundException	java.io.FileNotFoundException
    //   825	90	16	localStringBuilder4	StringBuilder
    //   917	5	16	localObject5	Object
    //   70	255	17	localXmlPullParser	org.xmlpull.v1.XmlPullParser
    //   264	133	18	localObject6	Object
    // Exception table:
    //   from	to	target	type
    //   67	72	399	finally
    //   76	91	399	finally
    //   95	104	399	finally
    //   129	138	399	finally
    //   142	151	399	finally
    //   167	179	399	finally
    //   198	214	399	finally
    //   218	254	399	finally
    //   261	266	399	finally
    //   270	275	399	finally
    //   279	288	399	finally
    //   292	305	399	finally
    //   309	320	399	finally
    //   324	329	399	finally
    //   375	380	399	finally
    //   384	392	399	finally
    //   396	399	399	finally
    //   410	413	399	finally
    //   67	72	404	java/lang/Throwable
    //   76	91	404	java/lang/Throwable
    //   95	104	404	java/lang/Throwable
    //   129	138	404	java/lang/Throwable
    //   142	151	404	java/lang/Throwable
    //   167	179	404	java/lang/Throwable
    //   198	214	404	java/lang/Throwable
    //   218	254	404	java/lang/Throwable
    //   261	266	404	java/lang/Throwable
    //   270	275	404	java/lang/Throwable
    //   279	288	404	java/lang/Throwable
    //   292	305	404	java/lang/Throwable
    //   309	320	404	java/lang/Throwable
    //   324	329	404	java/lang/Throwable
    //   375	380	404	java/lang/Throwable
    //   384	392	404	java/lang/Throwable
    //   396	399	404	java/lang/Throwable
    //   445	450	453	java/lang/Throwable
    //   52	60	539	finally
    //   355	360	539	finally
    //   445	450	539	finally
    //   477	484	539	finally
    //   509	514	539	finally
    //   536	539	539	finally
    //   550	555	539	finally
    //   559	564	539	finally
    //   568	577	539	finally
    //   581	589	539	finally
    //   593	604	539	finally
    //   618	623	539	finally
    //   627	632	539	finally
    //   636	645	539	finally
    //   649	657	539	finally
    //   661	672	539	finally
    //   686	691	539	finally
    //   695	700	539	finally
    //   704	713	539	finally
    //   717	725	539	finally
    //   729	740	539	finally
    //   754	759	539	finally
    //   763	768	539	finally
    //   772	781	539	finally
    //   785	793	539	finally
    //   797	808	539	finally
    //   822	827	539	finally
    //   831	836	539	finally
    //   840	849	539	finally
    //   853	865	539	finally
    //   869	878	539	finally
    //   882	893	539	finally
    //   52	60	544	org/xmlpull/v1/XmlPullParserException
    //   355	360	544	org/xmlpull/v1/XmlPullParserException
    //   445	450	544	org/xmlpull/v1/XmlPullParserException
    //   477	484	544	org/xmlpull/v1/XmlPullParserException
    //   509	514	544	org/xmlpull/v1/XmlPullParserException
    //   536	539	544	org/xmlpull/v1/XmlPullParserException
    //   52	60	612	java/lang/NullPointerException
    //   355	360	612	java/lang/NullPointerException
    //   445	450	612	java/lang/NullPointerException
    //   477	484	612	java/lang/NullPointerException
    //   509	514	612	java/lang/NullPointerException
    //   536	539	612	java/lang/NullPointerException
    //   52	60	680	java/lang/IllegalStateException
    //   355	360	680	java/lang/IllegalStateException
    //   445	450	680	java/lang/IllegalStateException
    //   477	484	680	java/lang/IllegalStateException
    //   509	514	680	java/lang/IllegalStateException
    //   536	539	680	java/lang/IllegalStateException
    //   52	60	748	java/io/IOException
    //   355	360	748	java/io/IOException
    //   445	450	748	java/io/IOException
    //   477	484	748	java/io/IOException
    //   509	514	748	java/io/IOException
    //   536	539	748	java/io/IOException
    //   52	60	816	java/io/FileNotFoundException
    //   355	360	816	java/io/FileNotFoundException
    //   445	450	816	java/io/FileNotFoundException
    //   477	484	816	java/io/FileNotFoundException
    //   509	514	816	java/io/FileNotFoundException
    //   536	539	816	java/io/FileNotFoundException
    //   6	14	917	finally
    //   364	368	917	finally
    //   901	903	917	finally
    //   910	914	917	finally
    //   914	917	917	finally
    //   919	921	917	finally
  }
  
  private void removeOldUpdatePackages(Context paramContext)
  {
    Downloads.removeAllDownloadsByPackage(paramContext, "com.google.android.systemupdater", "com.google.android.systemupdater.SystemUpdateReceiver");
  }
  
  private void writeTimestamps(HashMap<String, Long> paramHashMap)
  {
    try
    {
      synchronized (sFile)
      {
        FileOutputStream localFileOutputStream = sFile.startWrite();
        try
        {
          FastXmlSerializer localFastXmlSerializer = new com/android/internal/util/FastXmlSerializer;
          localFastXmlSerializer.<init>();
          localFastXmlSerializer.setOutput(localFileOutputStream, StandardCharsets.UTF_8.name());
          localFastXmlSerializer.startDocument(null, Boolean.valueOf(true));
          localFastXmlSerializer.startTag(null, "log-files");
          Iterator localIterator = paramHashMap.keySet().iterator();
          while (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            localFastXmlSerializer.startTag(null, "log");
            localFastXmlSerializer.attribute(null, "filename", str);
            localFastXmlSerializer.attribute(null, "timestamp", ((Long)paramHashMap.get(str)).toString());
            localFastXmlSerializer.endTag(null, "log");
          }
          localFastXmlSerializer.endTag(null, "log-files");
          localFastXmlSerializer.endDocument();
          sFile.finishWrite(localFileOutputStream);
        }
        catch (IOException localIOException2)
        {
          paramHashMap = new java/lang/StringBuilder;
          paramHashMap.<init>();
          paramHashMap.append("Failed to write timestamp file, using the backup: ");
          paramHashMap.append(localIOException2);
          Slog.w("BootReceiver", paramHashMap.toString());
          sFile.failWrite(localFileOutputStream);
        }
        return;
      }
    }
    catch (IOException localIOException1)
    {
      paramHashMap = new java/lang/StringBuilder;
      paramHashMap.<init>();
      paramHashMap.append("Failed to write timestamp file: ");
      paramHashMap.append(localIOException1);
      Slog.w("BootReceiver", paramHashMap.toString());
      return;
    }
  }
  
  public void onReceive(final Context paramContext, Intent paramIntent)
  {
    new Thread()
    {
      public void run()
      {
        try
        {
          BootReceiver.this.logBootEvents(paramContext);
        }
        catch (Exception localException1)
        {
          Slog.e("BootReceiver", "Can't log boot events", localException1);
        }
        int i = 0;
        try
        {
          boolean bool = IPackageManager.Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
          i = bool;
        }
        catch (Exception localException2)
        {
          break label68;
        }
        catch (RemoteException localRemoteException) {}
        if (i == 0)
        {
          BootReceiver.this.removeOldUpdatePackages(paramContext);
          return;
          label68:
          Slog.e("BootReceiver", "Can't remove old update packages", localRemoteException);
        }
      }
    }.start();
  }
}
