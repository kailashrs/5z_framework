package android.os;

import android.annotation.SystemApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import libcore.io.Streams;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;

public class RecoverySystem
{
  private static final String ACTION_EUICC_FACTORY_RESET = "com.android.internal.action.EUICC_FACTORY_RESET";
  public static final File BLOCK_MAP_FILE = new File(RECOVERY_DIR, "block.map");
  private static final long DEFAULT_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 30000L;
  private static final File DEFAULT_KEYSTORE = new File("/system/etc/security/otacerts.zip");
  private static final File LAST_INSTALL_FILE;
  private static final String LAST_PREFIX = "last_";
  private static final File LOG_FILE;
  private static final int LOG_FILE_MAX_LENGTH = 65536;
  private static final long MAX_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 60000L;
  private static final long MIN_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 5000L;
  private static final String PACKAGE_NAME_WIPING_EUICC_DATA_CALLBACK = "android";
  private static final long PUBLISH_PROGRESS_INTERVAL_MS = 500L;
  private static final File RECOVERY_DIR = new File("/cache/recovery");
  private static final String TAG = "RecoverySystem";
  public static final File UNCRYPT_PACKAGE_FILE = new File(RECOVERY_DIR, "uncrypt_file");
  public static final File UNCRYPT_STATUS_FILE = new File(RECOVERY_DIR, "uncrypt_status");
  private static final Object sRequestLock = new Object();
  private final IRecoverySystem mService;
  
  static
  {
    LOG_FILE = new File(RECOVERY_DIR, "log");
    LAST_INSTALL_FILE = new File(RECOVERY_DIR, "last_install");
  }
  
  public RecoverySystem()
  {
    mService = null;
  }
  
  public RecoverySystem(IRecoverySystem paramIRecoverySystem)
  {
    mService = paramIRecoverySystem;
  }
  
  private static void bootCommand(Context paramContext, String... paramVarArgs)
    throws IOException
  {
    LOG_FILE.delete();
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramVarArgs[j];
      if (!TextUtils.isEmpty(str))
      {
        localStringBuilder.append(str);
        localStringBuilder.append("\n");
      }
    }
    ((RecoverySystem)paramContext.getSystemService("recovery")).rebootRecoveryWithCommand(localStringBuilder.toString());
    throw new IOException("Reboot failed (no permissions?)");
  }
  
  @SystemApi
  public static void cancelScheduledUpdate(Context paramContext)
    throws IOException
  {
    if (((RecoverySystem)paramContext.getSystemService("recovery")).clearBcb()) {
      return;
    }
    throw new IOException("cancel scheduled update failed");
  }
  
  private boolean clearBcb()
  {
    try
    {
      boolean bool = mService.clearBcb();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  private static HashSet<X509Certificate> getTrustedCerts(File paramFile)
    throws IOException, GeneralSecurityException
  {
    HashSet localHashSet = new HashSet();
    Object localObject1 = paramFile;
    if (paramFile == null) {
      localObject1 = DEFAULT_KEYSTORE;
    }
    paramFile = new ZipFile((File)localObject1);
    try
    {
      CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
      Enumeration localEnumeration = paramFile.entries();
      for (;;)
      {
        if (localEnumeration.hasMoreElements()) {
          localObject1 = paramFile.getInputStream((ZipEntry)localEnumeration.nextElement());
        }
        try
        {
          localHashSet.add((X509Certificate)localCertificateFactory.generateCertificate((InputStream)localObject1));
          ((InputStream)localObject1).close();
        }
        finally
        {
          ((InputStream)localObject1).close();
        }
      }
      return localHashSet1;
    }
    finally
    {
      paramFile.close();
    }
  }
  
  public static String handleAftermath(Context paramContext)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3;
    try
    {
      String str2 = FileUtils.readTextFile(LOG_FILE, -65536, "...\n");
      localObject2 = str2;
    }
    catch (IOException localIOException2)
    {
      Log.e("RecoverySystem", "Error reading recovery log", localIOException2);
      localObject3 = localObject1;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      for (;;)
      {
        Log.i("RecoverySystem", "No recovery log file");
      }
    }
    if (localObject3 != null) {
      parseLastInstallLog(paramContext);
    }
    boolean bool = BLOCK_MAP_FILE.exists();
    int i = 0;
    if ((!bool) && (UNCRYPT_PACKAGE_FILE.exists()))
    {
      paramContext = null;
      try
      {
        String str1 = FileUtils.readTextFile(UNCRYPT_PACKAGE_FILE, 0, null);
        paramContext = str1;
      }
      catch (IOException localIOException1)
      {
        Log.e("RecoverySystem", "Error reading uncrypt file", localIOException1);
      }
      if ((paramContext != null) && (paramContext.startsWith("/data")))
      {
        StringBuilder localStringBuilder;
        if (UNCRYPT_PACKAGE_FILE.delete())
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Deleted: ");
          localStringBuilder.append(paramContext);
          Log.i("RecoverySystem", localStringBuilder.toString());
        }
        else
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Can't delete: ");
          localStringBuilder.append(paramContext);
          Log.e("RecoverySystem", localStringBuilder.toString());
        }
      }
    }
    paramContext = RECOVERY_DIR.list();
    while ((paramContext != null) && (i < paramContext.length))
    {
      if ((!paramContext[i].startsWith("last_")) && ((!bool) || (!paramContext[i].equals(BLOCK_MAP_FILE.getName()))) && ((!bool) || (!paramContext[i].equals(UNCRYPT_PACKAGE_FILE.getName())))) {
        recursiveDelete(new File(RECOVERY_DIR, paramContext[i]));
      }
      i++;
    }
    return localObject3;
  }
  
  public static void installPackage(Context paramContext, File paramFile)
    throws IOException
  {
    installPackage(paramContext, paramFile, false);
  }
  
  @SystemApi
  public static void installPackage(Context paramContext, File paramFile, boolean paramBoolean)
    throws IOException
  {
    synchronized (sRequestLock)
    {
      LOG_FILE.delete();
      UNCRYPT_PACKAGE_FILE.delete();
      Object localObject2 = paramFile.getCanonicalPath();
      paramFile = new java/lang/StringBuilder;
      paramFile.<init>();
      paramFile.append("!!! REBOOTING TO INSTALL ");
      paramFile.append((String)localObject2);
      paramFile.append(" !!!");
      Log.w("RecoverySystem", paramFile.toString());
      boolean bool = ((String)localObject2).endsWith("_s.zip");
      paramFile = (File)localObject2;
      if (((String)localObject2).startsWith("/data/")) {
        if (paramBoolean)
        {
          if (!BLOCK_MAP_FILE.exists())
          {
            Log.e("RecoverySystem", "Package claimed to have been processed but failed to find the block map file.");
            paramContext = new java/io/IOException;
            paramContext.<init>("Failed to find block map file");
            throw paramContext;
          }
        }
        else
        {
          paramFile = new java/io/FileWriter;
          paramFile.<init>(UNCRYPT_PACKAGE_FILE);
        }
      }
      try
      {
        localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append((String)localObject2);
        ((StringBuilder)localObject3).append("\n");
        paramFile.write(((StringBuilder)localObject3).toString());
        paramFile.close();
        if ((!UNCRYPT_PACKAGE_FILE.setReadable(true, false)) || (!UNCRYPT_PACKAGE_FILE.setWritable(true, false)))
        {
          paramFile = new java/lang/StringBuilder;
          paramFile.<init>();
          paramFile.append("Error setting permission for ");
          paramFile.append(UNCRYPT_PACKAGE_FILE);
          Log.e("RecoverySystem", paramFile.toString());
        }
        BLOCK_MAP_FILE.delete();
        paramFile = "@/cache/recovery/block.map";
      }
      finally
      {
        paramFile.close();
      }
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("--update_package=");
      ((StringBuilder)localObject2).append(paramFile);
      ((StringBuilder)localObject2).append("\n");
      paramFile = ((StringBuilder)localObject2).toString();
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("--locale=");
      ((StringBuilder)localObject2).append(Locale.getDefault().toLanguageTag());
      ((StringBuilder)localObject2).append("\n");
      Object localObject3 = ((StringBuilder)localObject2).toString();
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append(paramFile);
      ((StringBuilder)localObject2).append((String)localObject3);
      localObject2 = ((StringBuilder)localObject2).toString();
      paramFile = (File)localObject2;
      if (bool)
      {
        paramFile = new java/lang/StringBuilder;
        paramFile.<init>();
        paramFile.append((String)localObject2);
        paramFile.append("--security\n");
        paramFile = paramFile.toString();
      }
      if (((RecoverySystem)paramContext.getSystemService("recovery")).setupBcb(paramFile))
      {
        localObject3 = (PowerManager)paramContext.getSystemService("power");
        localObject2 = "recovery-update";
        paramFile = (File)localObject2;
        if (paramContext.getPackageManager().hasSystemFeature("android.software.leanback"))
        {
          paramFile = (File)localObject2;
          if (((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getState() != 2)
          {
            paramContext = new java/lang/StringBuilder;
            paramContext.<init>();
            paramContext.append("recovery-update");
            paramContext.append(",quiescent");
            paramFile = paramContext.toString();
          }
        }
        ((PowerManager)localObject3).reboot(paramFile);
        paramContext = new java/io/IOException;
        paramContext.<init>("Reboot failed (no permissions?)");
        throw paramContext;
      }
      paramContext = new java/io/IOException;
      paramContext.<init>("Setup BCB failed");
      throw paramContext;
    }
  }
  
  /* Error */
  private static void parseLastInstallLog(Context paramContext)
  {
    // Byte code:
    //   0: new 400	java/io/BufferedReader
    //   3: astore_1
    //   4: new 402	java/io/FileReader
    //   7: astore_2
    //   8: aload_2
    //   9: getstatic 99	android/os/RecoverySystem:LAST_INSTALL_FILE	Ljava/io/File;
    //   12: invokespecial 403	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   15: aload_1
    //   16: aload_2
    //   17: invokespecial 406	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   20: iconst_m1
    //   21: istore_3
    //   22: iconst_m1
    //   23: istore 4
    //   25: iconst_m1
    //   26: istore 5
    //   28: iconst_m1
    //   29: istore 6
    //   31: iconst_m1
    //   32: istore 7
    //   34: iconst_m1
    //   35: istore 8
    //   37: iconst_m1
    //   38: istore 9
    //   40: iconst_m1
    //   41: istore 10
    //   43: iconst_m1
    //   44: istore 11
    //   46: iconst_m1
    //   47: istore 12
    //   49: aload_1
    //   50: invokevirtual 409	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   53: astore 13
    //   55: aload 13
    //   57: ifnull +685 -> 742
    //   60: aload 13
    //   62: bipush 58
    //   64: invokevirtual 413	java/lang/String:indexOf	(I)I
    //   67: istore 14
    //   69: iload 14
    //   71: iconst_m1
    //   72: if_icmpeq +649 -> 721
    //   75: iload 14
    //   77: iconst_1
    //   78: iadd
    //   79: aload 13
    //   81: invokevirtual 416	java/lang/String:length	()I
    //   84: if_icmplt +6 -> 90
    //   87: goto +634 -> 721
    //   90: aload 13
    //   92: iload 14
    //   94: iconst_1
    //   95: iadd
    //   96: invokevirtual 420	java/lang/String:substring	(I)Ljava/lang/String;
    //   99: invokevirtual 423	java/lang/String:trim	()Ljava/lang/String;
    //   102: astore_2
    //   103: aload_2
    //   104: invokestatic 429	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   107: lstore 15
    //   109: aload 13
    //   111: ldc_w 431
    //   114: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   117: istore 17
    //   119: iload 17
    //   121: ifeq +21 -> 142
    //   124: lload 15
    //   126: ldc2_w 432
    //   129: ldiv
    //   130: invokestatic 439	java/lang/Math:toIntExact	(J)I
    //   133: istore 14
    //   135: goto +14 -> 149
    //   138: astore_2
    //   139: goto +509 -> 648
    //   142: lload 15
    //   144: invokestatic 439	java/lang/Math:toIntExact	(J)I
    //   147: istore 14
    //   149: aload 13
    //   151: ldc_w 441
    //   154: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   157: ifeq +73 -> 230
    //   160: iload 6
    //   162: istore 18
    //   164: iload 7
    //   166: istore 19
    //   168: iload 8
    //   170: istore 20
    //   172: iload 9
    //   174: istore 21
    //   176: iload 10
    //   178: istore 22
    //   180: iload 4
    //   182: istore 23
    //   184: iload 14
    //   186: istore 24
    //   188: iload 11
    //   190: istore 25
    //   192: iload 12
    //   194: istore 14
    //   196: iload 18
    //   198: istore 6
    //   200: iload 19
    //   202: istore 7
    //   204: iload 20
    //   206: istore 8
    //   208: iload 21
    //   210: istore 9
    //   212: iload 22
    //   214: istore 10
    //   216: iload 23
    //   218: istore 4
    //   220: iload 24
    //   222: istore_3
    //   223: iload 25
    //   225: istore 11
    //   227: goto +417 -> 644
    //   230: aload 13
    //   232: ldc_w 443
    //   235: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   238: ifeq +37 -> 275
    //   241: iload 11
    //   243: istore 25
    //   245: iload_3
    //   246: istore 24
    //   248: iload 14
    //   250: istore 23
    //   252: iload 10
    //   254: istore 22
    //   256: iload 9
    //   258: istore 21
    //   260: iload 8
    //   262: istore 20
    //   264: iload 7
    //   266: istore 19
    //   268: iload 6
    //   270: istore 18
    //   272: goto -80 -> 192
    //   275: aload 13
    //   277: ldc_w 445
    //   280: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   283: ifeq +37 -> 320
    //   286: iload 11
    //   288: istore 25
    //   290: iload_3
    //   291: istore 24
    //   293: iload 4
    //   295: istore 23
    //   297: iload 10
    //   299: istore 22
    //   301: iload 14
    //   303: istore 21
    //   305: iload 8
    //   307: istore 20
    //   309: iload 7
    //   311: istore 19
    //   313: iload 6
    //   315: istore 18
    //   317: goto -125 -> 192
    //   320: aload 13
    //   322: ldc_w 447
    //   325: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   328: ifeq +53 -> 381
    //   331: iload 11
    //   333: iconst_m1
    //   334: if_icmpne +6 -> 340
    //   337: goto +10 -> 347
    //   340: iload 11
    //   342: iload 14
    //   344: iadd
    //   345: istore 14
    //   347: iload 14
    //   349: istore 25
    //   351: iload_3
    //   352: istore 24
    //   354: iload 4
    //   356: istore 23
    //   358: iload 10
    //   360: istore 22
    //   362: iload 9
    //   364: istore 21
    //   366: iload 8
    //   368: istore 20
    //   370: iload 7
    //   372: istore 19
    //   374: iload 6
    //   376: istore 18
    //   378: goto -186 -> 192
    //   381: aload 13
    //   383: ldc_w 449
    //   386: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   389: ifeq +53 -> 442
    //   392: iload 10
    //   394: iconst_m1
    //   395: if_icmpne +6 -> 401
    //   398: goto +10 -> 408
    //   401: iload 10
    //   403: iload 14
    //   405: iadd
    //   406: istore 14
    //   408: iload 11
    //   410: istore 25
    //   412: iload_3
    //   413: istore 24
    //   415: iload 4
    //   417: istore 23
    //   419: iload 14
    //   421: istore 22
    //   423: iload 9
    //   425: istore 21
    //   427: iload 8
    //   429: istore 20
    //   431: iload 7
    //   433: istore 19
    //   435: iload 6
    //   437: istore 18
    //   439: goto -247 -> 192
    //   442: aload 13
    //   444: ldc_w 451
    //   447: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   450: ifeq +37 -> 487
    //   453: iload 11
    //   455: istore 25
    //   457: iload_3
    //   458: istore 24
    //   460: iload 4
    //   462: istore 23
    //   464: iload 10
    //   466: istore 22
    //   468: iload 9
    //   470: istore 21
    //   472: iload 14
    //   474: istore 20
    //   476: iload 7
    //   478: istore 19
    //   480: iload 6
    //   482: istore 18
    //   484: goto -292 -> 192
    //   487: aload 13
    //   489: ldc_w 453
    //   492: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   495: ifeq +37 -> 532
    //   498: iload 11
    //   500: istore 25
    //   502: iload_3
    //   503: istore 24
    //   505: iload 4
    //   507: istore 23
    //   509: iload 10
    //   511: istore 22
    //   513: iload 9
    //   515: istore 21
    //   517: iload 8
    //   519: istore 20
    //   521: iload 14
    //   523: istore 19
    //   525: iload 6
    //   527: istore 18
    //   529: goto -337 -> 192
    //   532: aload 13
    //   534: ldc_w 455
    //   537: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   540: ifeq +37 -> 577
    //   543: iload 11
    //   545: istore 25
    //   547: iload_3
    //   548: istore 24
    //   550: iload 4
    //   552: istore 23
    //   554: iload 10
    //   556: istore 22
    //   558: iload 9
    //   560: istore 21
    //   562: iload 8
    //   564: istore 20
    //   566: iload 7
    //   568: istore 19
    //   570: iload 14
    //   572: istore 18
    //   574: goto -382 -> 192
    //   577: aload 13
    //   579: ldc_w 457
    //   582: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   585: ifeq +14 -> 599
    //   588: iload 14
    //   590: istore 5
    //   592: iload 12
    //   594: istore 14
    //   596: goto +48 -> 644
    //   599: iload 11
    //   601: istore 25
    //   603: iload_3
    //   604: istore 24
    //   606: iload 4
    //   608: istore 23
    //   610: iload 10
    //   612: istore 22
    //   614: iload 9
    //   616: istore 21
    //   618: iload 8
    //   620: istore 20
    //   622: iload 7
    //   624: istore 19
    //   626: iload 6
    //   628: istore 18
    //   630: aload 13
    //   632: ldc_w 459
    //   635: invokevirtual 272	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   638: ifeq -446 -> 192
    //   641: goto -414 -> 227
    //   644: goto +81 -> 725
    //   647: astore_2
    //   648: new 128	java/lang/StringBuilder
    //   651: astore_2
    //   652: aload_2
    //   653: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   656: aload_2
    //   657: ldc_w 461
    //   660: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   663: pop
    //   664: aload_2
    //   665: aload 13
    //   667: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   670: pop
    //   671: ldc 52
    //   673: aload_2
    //   674: invokevirtual 153	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   677: invokestatic 278	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   680: pop
    //   681: goto +40 -> 721
    //   684: astore_2
    //   685: new 128	java/lang/StringBuilder
    //   688: astore_2
    //   689: aload_2
    //   690: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   693: aload_2
    //   694: ldc_w 463
    //   697: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   700: pop
    //   701: aload_2
    //   702: aload 13
    //   704: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   707: pop
    //   708: ldc 52
    //   710: aload_2
    //   711: invokevirtual 153	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   714: invokestatic 278	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   717: pop
    //   718: goto +3 -> 721
    //   721: iload 12
    //   723: istore 14
    //   725: iload 14
    //   727: istore 12
    //   729: goto -680 -> 49
    //   732: astore_0
    //   733: aconst_null
    //   734: astore_2
    //   735: goto +190 -> 925
    //   738: astore_0
    //   739: goto +177 -> 916
    //   742: iload_3
    //   743: iconst_m1
    //   744: if_icmpeq +22 -> 766
    //   747: aload_0
    //   748: ldc_w 465
    //   751: iload_3
    //   752: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   755: goto +11 -> 766
    //   758: astore_0
    //   759: goto +151 -> 910
    //   762: astore_0
    //   763: goto +153 -> 916
    //   766: iload 4
    //   768: iconst_m1
    //   769: if_icmpeq +12 -> 781
    //   772: aload_0
    //   773: ldc_w 473
    //   776: iload 4
    //   778: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   781: iload 9
    //   783: iconst_m1
    //   784: if_icmpeq +12 -> 796
    //   787: aload_0
    //   788: ldc_w 475
    //   791: iload 9
    //   793: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   796: iload 11
    //   798: iconst_m1
    //   799: if_icmpeq +12 -> 811
    //   802: aload_0
    //   803: ldc_w 477
    //   806: iload 11
    //   808: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   811: iload 10
    //   813: iconst_m1
    //   814: if_icmpeq +12 -> 826
    //   817: aload_0
    //   818: ldc_w 479
    //   821: iload 10
    //   823: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   826: iload 8
    //   828: iconst_m1
    //   829: if_icmpeq +12 -> 841
    //   832: aload_0
    //   833: ldc_w 481
    //   836: iload 8
    //   838: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   841: iload 7
    //   843: iconst_m1
    //   844: if_icmpeq +12 -> 856
    //   847: aload_0
    //   848: ldc_w 483
    //   851: iload 7
    //   853: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   856: iload 6
    //   858: iconst_m1
    //   859: if_icmpeq +12 -> 871
    //   862: aload_0
    //   863: ldc_w 485
    //   866: iload 6
    //   868: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   871: iload 5
    //   873: iconst_m1
    //   874: if_icmpeq +12 -> 886
    //   877: aload_0
    //   878: ldc_w 487
    //   881: iload 5
    //   883: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   886: iload 12
    //   888: iconst_m1
    //   889: if_icmpeq +12 -> 901
    //   892: aload_0
    //   893: ldc_w 489
    //   896: iload 12
    //   898: invokestatic 471	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   901: aconst_null
    //   902: aload_1
    //   903: invokestatic 491	android/os/RecoverySystem:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   906: goto +37 -> 943
    //   909: astore_0
    //   910: aconst_null
    //   911: astore_2
    //   912: goto +13 -> 925
    //   915: astore_0
    //   916: aload_0
    //   917: athrow
    //   918: astore 13
    //   920: aload_0
    //   921: astore_2
    //   922: aload 13
    //   924: astore_0
    //   925: aload_2
    //   926: aload_1
    //   927: invokestatic 491	android/os/RecoverySystem:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   930: aload_0
    //   931: athrow
    //   932: astore_0
    //   933: ldc 52
    //   935: ldc_w 493
    //   938: aload_0
    //   939: invokestatic 250	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   942: pop
    //   943: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	944	0	paramContext	Context
    //   3	924	1	localBufferedReader	java.io.BufferedReader
    //   7	97	2	localObject1	Object
    //   138	1	2	localArithmeticException1	ArithmeticException
    //   647	1	2	localArithmeticException2	ArithmeticException
    //   651	23	2	localStringBuilder	StringBuilder
    //   684	1	2	localNumberFormatException	NumberFormatException
    //   688	238	2	localObject2	Object
    //   21	731	3	i	int
    //   23	754	4	j	int
    //   26	856	5	k	int
    //   29	838	6	m	int
    //   32	820	7	n	int
    //   35	802	8	i1	int
    //   38	754	9	i2	int
    //   41	781	10	i3	int
    //   44	763	11	i4	int
    //   47	850	12	i5	int
    //   53	650	13	str	String
    //   918	5	13	localObject3	Object
    //   67	659	14	i6	int
    //   107	36	15	l	long
    //   117	3	17	bool	boolean
    //   162	467	18	i7	int
    //   166	459	19	i8	int
    //   170	451	20	i9	int
    //   174	443	21	i10	int
    //   178	435	22	i11	int
    //   182	427	23	i12	int
    //   186	419	24	i13	int
    //   190	412	25	i14	int
    // Exception table:
    //   from	to	target	type
    //   124	135	138	java/lang/ArithmeticException
    //   142	149	138	java/lang/ArithmeticException
    //   109	119	647	java/lang/ArithmeticException
    //   103	109	684	java/lang/NumberFormatException
    //   60	69	732	finally
    //   75	87	732	finally
    //   90	103	732	finally
    //   103	109	732	finally
    //   109	119	732	finally
    //   60	69	738	java/lang/Throwable
    //   75	87	738	java/lang/Throwable
    //   90	103	738	java/lang/Throwable
    //   103	109	738	java/lang/Throwable
    //   109	119	738	java/lang/Throwable
    //   124	135	758	finally
    //   142	149	758	finally
    //   149	160	758	finally
    //   230	241	758	finally
    //   275	286	758	finally
    //   320	331	758	finally
    //   381	392	758	finally
    //   442	453	758	finally
    //   487	498	758	finally
    //   532	543	758	finally
    //   577	588	758	finally
    //   630	641	758	finally
    //   648	681	758	finally
    //   685	718	758	finally
    //   747	755	758	finally
    //   772	781	758	finally
    //   787	796	758	finally
    //   802	811	758	finally
    //   817	826	758	finally
    //   832	841	758	finally
    //   847	856	758	finally
    //   862	871	758	finally
    //   877	886	758	finally
    //   892	901	758	finally
    //   124	135	762	java/lang/Throwable
    //   142	149	762	java/lang/Throwable
    //   149	160	762	java/lang/Throwable
    //   230	241	762	java/lang/Throwable
    //   275	286	762	java/lang/Throwable
    //   320	331	762	java/lang/Throwable
    //   381	392	762	java/lang/Throwable
    //   442	453	762	java/lang/Throwable
    //   487	498	762	java/lang/Throwable
    //   532	543	762	java/lang/Throwable
    //   577	588	762	java/lang/Throwable
    //   630	641	762	java/lang/Throwable
    //   648	681	762	java/lang/Throwable
    //   685	718	762	java/lang/Throwable
    //   747	755	762	java/lang/Throwable
    //   772	781	762	java/lang/Throwable
    //   787	796	762	java/lang/Throwable
    //   802	811	762	java/lang/Throwable
    //   817	826	762	java/lang/Throwable
    //   832	841	762	java/lang/Throwable
    //   847	856	762	java/lang/Throwable
    //   862	871	762	java/lang/Throwable
    //   877	886	762	java/lang/Throwable
    //   892	901	762	java/lang/Throwable
    //   49	55	909	finally
    //   49	55	915	java/lang/Throwable
    //   916	918	918	finally
    //   0	20	932	java/io/IOException
    //   901	906	932	java/io/IOException
    //   925	932	932	java/io/IOException
  }
  
  @SystemApi
  public static void processPackage(Context paramContext, File paramFile, ProgressListener paramProgressListener)
    throws IOException
  {
    processPackage(paramContext, paramFile, paramProgressListener, null);
  }
  
  @SystemApi
  public static void processPackage(Context paramContext, File paramFile, final ProgressListener paramProgressListener, Handler paramHandler)
    throws IOException
  {
    String str = paramFile.getCanonicalPath();
    if (!str.startsWith("/data/")) {
      return;
    }
    RecoverySystem localRecoverySystem = (RecoverySystem)paramContext.getSystemService("recovery");
    paramFile = null;
    if (paramProgressListener != null)
    {
      if (paramHandler == null) {
        paramHandler = new Handler(paramContext.getMainLooper());
      }
      paramFile = new IRecoverySystemProgressListener.Stub()
      {
        int lastProgress = 0;
        long lastPublishTime = System.currentTimeMillis();
        
        public void onProgress(final int paramAnonymousInt)
        {
          final long l = System.currentTimeMillis();
          post(new Runnable()
          {
            public void run()
            {
              if ((paramAnonymousInt > lastProgress) && (l - lastPublishTime > 500L))
              {
                lastProgress = paramAnonymousInt;
                lastPublishTime = l;
                val$listener.onProgress(paramAnonymousInt);
              }
            }
          });
        }
      };
    }
    if (localRecoverySystem.uncrypt(str, paramFile)) {
      return;
    }
    throw new IOException("process package failed");
  }
  
  /* Error */
  private static boolean readAndVerifyPackageCompatibilityEntry(File paramFile)
    throws IOException
  {
    // Byte code:
    //   0: new 183	java/util/zip/ZipFile
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 186	java/util/zip/ZipFile:<init>	(Ljava/io/File;)V
    //   8: astore_1
    //   9: aconst_null
    //   10: astore_2
    //   11: aload_2
    //   12: astore_0
    //   13: aload_1
    //   14: ldc_w 520
    //   17: invokevirtual 524	java/util/zip/ZipFile:getEntry	(Ljava/lang/String;)Ljava/util/zip/ZipEntry;
    //   20: astore_3
    //   21: aload_3
    //   22: ifnonnull +10 -> 32
    //   25: aconst_null
    //   26: aload_1
    //   27: invokestatic 491	android/os/RecoverySystem:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   30: iconst_1
    //   31: ireturn
    //   32: aload_2
    //   33: astore_0
    //   34: aload_1
    //   35: aload_3
    //   36: invokevirtual 213	java/util/zip/ZipFile:getInputStream	(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
    //   39: invokestatic 528	android/os/RecoverySystem:verifyPackageCompatibility	(Ljava/io/InputStream;)Z
    //   42: istore 4
    //   44: aconst_null
    //   45: aload_1
    //   46: invokestatic 491	android/os/RecoverySystem:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   49: iload 4
    //   51: ireturn
    //   52: astore_2
    //   53: goto +8 -> 61
    //   56: astore_2
    //   57: aload_2
    //   58: astore_0
    //   59: aload_2
    //   60: athrow
    //   61: aload_0
    //   62: aload_1
    //   63: invokestatic 491	android/os/RecoverySystem:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   66: aload_2
    //   67: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	68	0	paramFile	File
    //   8	55	1	localZipFile	ZipFile
    //   10	23	2	localObject1	Object
    //   52	1	2	localObject2	Object
    //   56	11	2	localThrowable	Throwable
    //   20	16	3	localZipEntry	ZipEntry
    //   42	8	4	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   13	21	52	finally
    //   34	44	52	finally
    //   59	61	52	finally
    //   13	21	56	java/lang/Throwable
    //   34	44	56	java/lang/Throwable
  }
  
  public static void rebootPromptAndWipeUserData(Context paramContext, String paramString)
    throws IOException
  {
    Object localObject = null;
    if (!TextUtils.isEmpty(paramString))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("--reason=");
      ((StringBuilder)localObject).append(sanitizeArg(paramString));
      localObject = ((StringBuilder)localObject).toString();
    }
    paramString = new StringBuilder();
    paramString.append("--locale=");
    paramString.append(Locale.getDefault().toString());
    bootCommand(paramContext, new String[] { null, "--prompt_and_wipe_data", localObject, paramString.toString() });
  }
  
  private void rebootRecoveryWithCommand(String paramString)
  {
    try
    {
      mService.rebootRecoveryWithCommand(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  @SystemApi
  public static void rebootWipeAb(Context paramContext, File paramFile, String paramString)
    throws IOException
  {
    Object localObject = null;
    if (!TextUtils.isEmpty(paramString))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("--reason=");
      ((StringBuilder)localObject).append(sanitizeArg(paramString));
      localObject = ((StringBuilder)localObject).toString();
    }
    paramString = paramFile.getCanonicalPath();
    paramFile = new StringBuilder();
    paramFile.append("--wipe_package=");
    paramFile.append(paramString);
    paramString = paramFile.toString();
    paramFile = new StringBuilder();
    paramFile.append("--locale=");
    paramFile.append(Locale.getDefault().toLanguageTag());
    bootCommand(paramContext, new String[] { "--wipe_ab", paramString, localObject, paramFile.toString() });
  }
  
  public static void rebootWipeCache(Context paramContext)
    throws IOException
  {
    rebootWipeCache(paramContext, paramContext.getPackageName());
  }
  
  public static void rebootWipeCache(Context paramContext, String paramString)
    throws IOException
  {
    Object localObject = null;
    if (!TextUtils.isEmpty(paramString))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("--reason=");
      ((StringBuilder)localObject).append(sanitizeArg(paramString));
      localObject = ((StringBuilder)localObject).toString();
    }
    paramString = new StringBuilder();
    paramString.append("--locale=");
    paramString.append(Locale.getDefault().toLanguageTag());
    bootCommand(paramContext, new String[] { "--wipe_cache", localObject, paramString.toString() });
  }
  
  public static void rebootWipeUserData(Context paramContext)
    throws IOException
  {
    rebootWipeUserData(paramContext, false, paramContext.getPackageName(), false, false);
  }
  
  public static void rebootWipeUserData(Context paramContext, String paramString)
    throws IOException
  {
    rebootWipeUserData(paramContext, false, paramString, false, false);
  }
  
  public static void rebootWipeUserData(Context paramContext, boolean paramBoolean)
    throws IOException
  {
    rebootWipeUserData(paramContext, paramBoolean, paramContext.getPackageName(), false, false);
  }
  
  public static void rebootWipeUserData(Context paramContext, boolean paramBoolean1, String paramString, boolean paramBoolean2)
    throws IOException
  {
    rebootWipeUserData(paramContext, paramBoolean1, paramString, paramBoolean2, false);
  }
  
  public static void rebootWipeUserData(Context paramContext, boolean paramBoolean1, String paramString, boolean paramBoolean2, boolean paramBoolean3)
    throws IOException
  {
    Object localObject1 = (UserManager)paramContext.getSystemService("user");
    if ((!paramBoolean2) && (((UserManager)localObject1).hasUserRestriction("no_factory_reset"))) {
      throw new SecurityException("Wiping data is not allowed for this user.");
    }
    Object localObject2 = new ConditionVariable();
    localObject1 = new Intent("android.intent.action.MASTER_CLEAR_NOTIFICATION");
    ((Intent)localObject1).addFlags(285212672);
    paramContext.sendOrderedBroadcastAsUser((Intent)localObject1, UserHandle.SYSTEM, "android.permission.MASTER_CLEAR", new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        open();
      }
    }, null, 0, null, null);
    ((ConditionVariable)localObject2).block();
    if (paramBoolean3) {
      wipeEuiccData(paramContext, "android");
    }
    localObject1 = null;
    if (paramBoolean1) {
      localObject1 = "--shutdown_after";
    }
    localObject2 = null;
    if (!TextUtils.isEmpty(paramString))
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("--reason=");
      ((StringBuilder)localObject2).append(sanitizeArg(paramString));
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    paramString = new StringBuilder();
    paramString.append("--locale=");
    paramString.append(Locale.getDefault().toLanguageTag());
    bootCommand(paramContext, new String[] { localObject1, "--wipe_data", localObject2, paramString.toString() });
  }
  
  private static void recursiveDelete(File paramFile)
  {
    Object localObject;
    if (paramFile.isDirectory())
    {
      localObject = paramFile.list();
      for (int i = 0; (localObject != null) && (i < localObject.length); i++) {
        recursiveDelete(new File(paramFile, localObject[i]));
      }
    }
    if (!paramFile.delete())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't delete: ");
      ((StringBuilder)localObject).append(paramFile);
      Log.e("RecoverySystem", ((StringBuilder)localObject).toString());
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Deleted: ");
      ((StringBuilder)localObject).append(paramFile);
      Log.i("RecoverySystem", ((StringBuilder)localObject).toString());
    }
  }
  
  private static String sanitizeArg(String paramString)
  {
    return paramString.replace('\000', '?').replace('\n', '?');
  }
  
  @SystemApi
  public static void scheduleUpdateOnBoot(Context paramContext, File paramFile)
    throws IOException
  {
    Object localObject = paramFile.getCanonicalPath();
    boolean bool = ((String)localObject).endsWith("_s.zip");
    paramFile = (File)localObject;
    if (((String)localObject).startsWith("/data/")) {
      paramFile = "@/cache/recovery/block.map";
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("--update_package=");
    ((StringBuilder)localObject).append(paramFile);
    ((StringBuilder)localObject).append("\n");
    paramFile = ((StringBuilder)localObject).toString();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("--locale=");
    ((StringBuilder)localObject).append(Locale.getDefault().toLanguageTag());
    ((StringBuilder)localObject).append("\n");
    String str = ((StringBuilder)localObject).toString();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramFile);
    ((StringBuilder)localObject).append(str);
    localObject = ((StringBuilder)localObject).toString();
    paramFile = (File)localObject;
    if (bool)
    {
      paramFile = new StringBuilder();
      paramFile.append((String)localObject);
      paramFile.append("--security\n");
      paramFile = paramFile.toString();
    }
    if (((RecoverySystem)paramContext.getSystemService("recovery")).setupBcb(paramFile)) {
      return;
    }
    throw new IOException("schedule update on boot failed");
  }
  
  private boolean setupBcb(String paramString)
  {
    try
    {
      boolean bool = mService.setupBcb(paramString);
      return bool;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  private boolean uncrypt(String paramString, IRecoverySystemProgressListener paramIRecoverySystemProgressListener)
  {
    try
    {
      boolean bool = mService.uncrypt(paramString, paramIRecoverySystemProgressListener);
      return bool;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  public static void verifyPackage(File paramFile1, ProgressListener paramProgressListener, File paramFile2)
    throws IOException, GeneralSecurityException
  {
    long l1 = paramFile1.length();
    RandomAccessFile localRandomAccessFile = new RandomAccessFile(paramFile1, "r");
    try
    {
      long l2 = System.currentTimeMillis();
      if (paramProgressListener != null) {
        paramProgressListener.onProgress(0);
      }
      localRandomAccessFile.seek(l1 - 6L);
      Object localObject1 = new byte[6];
      localRandomAccessFile.readFully((byte[])localObject1);
      if ((localObject1[2] == -1) && (localObject1[3] == -1))
      {
        int i = localObject1[4];
        int j = (localObject1[5] & 0xFF) << 8 | i & 0xFF;
        int k = localObject1[0] & 0xFF | (localObject1[1] & 0xFF) << 8;
        localObject1 = new byte[j + 22];
        long l3 = j + 22;
        try
        {
          localRandomAccessFile.seek(l1 - l3);
          localRandomAccessFile.readFully((byte[])localObject1);
          if ((localObject1[0] == 80) && (localObject1[1] == 75) && (localObject1[2] == 5) && (localObject1[3] == 6))
          {
            for (i = 4; i < localObject1.length - 3; i++) {
              if ((localObject1[i] == 80) && (localObject1[(i + 1)] == 75) && (localObject1[(i + 2)] == 5) && (localObject1[(i + 3)] == 6))
              {
                paramFile1 = new java/security/SignatureException;
                paramFile1.<init>("EOCD marker found after start of EOCD");
                throw paramFile1;
              }
            }
            PKCS7 localPKCS7 = new sun/security/pkcs/PKCS7;
            Object localObject2 = new java/io/ByteArrayInputStream;
            ((ByteArrayInputStream)localObject2).<init>((byte[])localObject1, j + 22 - k, k);
            localPKCS7.<init>((InputStream)localObject2);
            localObject1 = localPKCS7.getCertificates();
            if ((localObject1 != null) && (localObject1.length != 0))
            {
              localObject2 = localObject1[0].getPublicKey();
              localObject1 = localPKCS7.getSignerInfos();
              if ((localObject1 != null) && (localObject1.length != 0))
              {
                SignerInfo localSignerInfo = localObject1[0];
                i = 0;
                if (paramFile2 == null) {
                  paramFile2 = DEFAULT_KEYSTORE;
                }
                localObject1 = getTrustedCerts(paramFile2);
                paramFile2 = ((HashSet)localObject1).iterator();
                while (paramFile2.hasNext()) {
                  if (((X509Certificate)paramFile2.next()).getPublicKey().equals(localObject2))
                  {
                    i = 1;
                    break;
                  }
                }
                if (i != 0)
                {
                  localRandomAccessFile.seek(0L);
                  try
                  {
                    paramFile2 = new android/os/RecoverySystem$1;
                    boolean bool;
                    paramFile1 = new java/security/SignatureException;
                  }
                  finally
                  {
                    paramFile1 = finally;
                  }
                }
                paramFile1.<init>("signature doesn't match any trusted key");
                throw paramFile1;
              }
              paramFile1 = new java/security/SignatureException;
              paramFile1.<init>("signature contains no signedData");
              throw paramFile1;
            }
            paramFile1 = new java/security/SignatureException;
            paramFile1.<init>("signature contains no certificates");
            throw paramFile1;
          }
          paramFile1 = new java/security/SignatureException;
          paramFile1.<init>("no signature in file (bad footer)");
          throw paramFile1;
        }
        finally
        {
          break label608;
        }
      }
      paramFile1 = new java/security/SignatureException;
      paramFile1.<init>("no signature in file (no footer)");
      throw paramFile1;
    }
    finally {}
    label608:
    localRandomAccessFile.close();
    throw paramFile1;
  }
  
  /* Error */
  @SystemApi
  @android.annotation.SuppressLint({"Doclava125"})
  public static boolean verifyPackageCompatibility(File paramFile)
    throws IOException
  {
    // Byte code:
    //   0: new 734	java/io/FileInputStream
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 735	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   8: astore_1
    //   9: aconst_null
    //   10: astore_0
    //   11: aload_1
    //   12: invokestatic 528	android/os/RecoverySystem:verifyPackageCompatibility	(Ljava/io/InputStream;)Z
    //   15: istore_2
    //   16: aconst_null
    //   17: aload_1
    //   18: invokestatic 491	android/os/RecoverySystem:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   21: iload_2
    //   22: ireturn
    //   23: astore_3
    //   24: goto +8 -> 32
    //   27: astore_3
    //   28: aload_3
    //   29: astore_0
    //   30: aload_3
    //   31: athrow
    //   32: aload_0
    //   33: aload_1
    //   34: invokestatic 491	android/os/RecoverySystem:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   37: aload_3
    //   38: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	39	0	paramFile	File
    //   8	26	1	localFileInputStream	java.io.FileInputStream
    //   15	7	2	bool	boolean
    //   23	1	3	localObject	Object
    //   27	11	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   11	16	23	finally
    //   30	32	23	finally
    //   11	16	27	java/lang/Throwable
  }
  
  private static boolean verifyPackageCompatibility(InputStream paramInputStream)
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    paramInputStream = new ZipInputStream(paramInputStream);
    long l;
    for (;;)
    {
      Object localObject = paramInputStream.getNextEntry();
      if (localObject == null) {
        break label116;
      }
      l = ((ZipEntry)localObject).getSize();
      if ((l > 2147483647L) || (l < 0L)) {
        break;
      }
      localObject = new byte[(int)l];
      Streams.readFully(paramInputStream, (byte[])localObject);
      localArrayList.add(new String((byte[])localObject, StandardCharsets.UTF_8));
    }
    paramInputStream = new StringBuilder();
    paramInputStream.append("invalid entry size (");
    paramInputStream.append(l);
    paramInputStream.append(") in the compatibility file");
    throw new IOException(paramInputStream.toString());
    label116:
    if (!localArrayList.isEmpty())
    {
      boolean bool;
      if (VintfObject.verify((String[])localArrayList.toArray(new String[localArrayList.size()])) == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    throw new IOException("no entries found in the compatibility file");
  }
  
  /* Error */
  public static boolean wipeEuiccData(Context paramContext, String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 797	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   4: ldc_w 799
    //   7: iconst_0
    //   8: invokestatic 805	android/provider/Settings$Global:getInt	(Landroid/content/ContentResolver;Ljava/lang/String;I)I
    //   11: ifne +14 -> 25
    //   14: ldc 52
    //   16: ldc_w 807
    //   19: invokestatic 810	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   22: pop
    //   23: iconst_1
    //   24: ireturn
    //   25: aload_0
    //   26: ldc_w 812
    //   29: invokevirtual 149	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   32: checkcast 814	android/telephony/euicc/EuiccManager
    //   35: astore_2
    //   36: aload_2
    //   37: ifnull +297 -> 334
    //   40: aload_2
    //   41: invokevirtual 817	android/telephony/euicc/EuiccManager:isEnabled	()Z
    //   44: ifeq +290 -> 334
    //   47: new 819	java/util/concurrent/CountDownLatch
    //   50: dup
    //   51: iconst_1
    //   52: invokespecial 821	java/util/concurrent/CountDownLatch:<init>	(I)V
    //   55: astore_3
    //   56: new 823	java/util/concurrent/atomic/AtomicBoolean
    //   59: dup
    //   60: iconst_0
    //   61: invokespecial 826	java/util/concurrent/atomic/AtomicBoolean:<init>	(Z)V
    //   64: astore 4
    //   66: new 14	android/os/RecoverySystem$4
    //   69: dup
    //   70: aload 4
    //   72: aload_3
    //   73: invokespecial 829	android/os/RecoverySystem$4:<init>	(Ljava/util/concurrent/atomic/AtomicBoolean;Ljava/util/concurrent/CountDownLatch;)V
    //   76: astore 5
    //   78: new 581	android/content/Intent
    //   81: dup
    //   82: ldc 21
    //   84: invokespecial 584	android/content/Intent:<init>	(Ljava/lang/String;)V
    //   87: astore 6
    //   89: aload 6
    //   91: aload_1
    //   92: invokevirtual 833	android/content/Intent:setPackage	(Ljava/lang/String;)Landroid/content/Intent;
    //   95: pop
    //   96: aload_0
    //   97: iconst_0
    //   98: aload 6
    //   100: ldc_w 834
    //   103: getstatic 595	android/os/UserHandle:SYSTEM	Landroid/os/UserHandle;
    //   106: invokestatic 840	android/app/PendingIntent:getBroadcastAsUser	(Landroid/content/Context;ILandroid/content/Intent;ILandroid/os/UserHandle;)Landroid/app/PendingIntent;
    //   109: astore_1
    //   110: new 842	android/content/IntentFilter
    //   113: dup
    //   114: invokespecial 843	android/content/IntentFilter:<init>	()V
    //   117: astore 6
    //   119: aload 6
    //   121: ldc 21
    //   123: invokevirtual 846	android/content/IntentFilter:addAction	(Ljava/lang/String;)V
    //   126: new 848	android/os/HandlerThread
    //   129: dup
    //   130: ldc_w 850
    //   133: invokespecial 851	android/os/HandlerThread:<init>	(Ljava/lang/String;)V
    //   136: astore 7
    //   138: aload 7
    //   140: invokevirtual 854	android/os/HandlerThread:start	()V
    //   143: new 500	android/os/Handler
    //   146: dup
    //   147: aload 7
    //   149: invokevirtual 857	android/os/HandlerThread:getLooper	()Landroid/os/Looper;
    //   152: invokespecial 507	android/os/Handler:<init>	(Landroid/os/Looper;)V
    //   155: astore 7
    //   157: aload_0
    //   158: invokevirtual 861	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   161: aload 5
    //   163: aload 6
    //   165: aconst_null
    //   166: aload 7
    //   168: invokevirtual 865	android/content/Context:registerReceiver	(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;)Landroid/content/Intent;
    //   171: pop
    //   172: aload_2
    //   173: aload_1
    //   174: invokevirtual 869	android/telephony/euicc/EuiccManager:eraseSubscriptions	(Landroid/app/PendingIntent;)V
    //   177: aload_0
    //   178: invokevirtual 797	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   181: astore_1
    //   182: aload_1
    //   183: ldc_w 871
    //   186: ldc2_w 26
    //   189: invokestatic 875	android/provider/Settings$Global:getLong	(Landroid/content/ContentResolver;Ljava/lang/String;J)J
    //   192: lstore 8
    //   194: lload 8
    //   196: ldc2_w 41
    //   199: lcmp
    //   200: ifge +11 -> 211
    //   203: ldc2_w 41
    //   206: lstore 10
    //   208: goto +21 -> 229
    //   211: lload 8
    //   213: lstore 10
    //   215: lload 8
    //   217: ldc2_w 38
    //   220: lcmp
    //   221: ifle +8 -> 229
    //   224: ldc2_w 38
    //   227: lstore 10
    //   229: getstatic 881	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   232: astore_1
    //   233: aload_3
    //   234: lload 10
    //   236: aload_1
    //   237: invokevirtual 885	java/util/concurrent/CountDownLatch:await	(JLjava/util/concurrent/TimeUnit;)Z
    //   240: ifne +23 -> 263
    //   243: ldc 52
    //   245: ldc_w 887
    //   248: invokestatic 278	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   251: pop
    //   252: aload_0
    //   253: invokevirtual 861	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   256: aload 5
    //   258: invokevirtual 891	android/content/Context:unregisterReceiver	(Landroid/content/BroadcastReceiver;)V
    //   261: iconst_0
    //   262: ireturn
    //   263: aload_0
    //   264: invokevirtual 861	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   267: aload 5
    //   269: invokevirtual 891	android/content/Context:unregisterReceiver	(Landroid/content/BroadcastReceiver;)V
    //   272: aload 4
    //   274: invokevirtual 894	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   277: ireturn
    //   278: astore_1
    //   279: goto +16 -> 295
    //   282: astore_1
    //   283: goto +40 -> 323
    //   286: astore_1
    //   287: goto +8 -> 295
    //   290: astore_1
    //   291: goto +32 -> 323
    //   294: astore_1
    //   295: invokestatic 898	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   298: invokevirtual 901	java/lang/Thread:interrupt	()V
    //   301: ldc 52
    //   303: ldc_w 903
    //   306: aload_1
    //   307: invokestatic 250	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   310: pop
    //   311: aload_0
    //   312: invokevirtual 861	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   315: aload 5
    //   317: invokevirtual 891	android/content/Context:unregisterReceiver	(Landroid/content/BroadcastReceiver;)V
    //   320: iconst_0
    //   321: ireturn
    //   322: astore_1
    //   323: aload_0
    //   324: invokevirtual 861	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   327: aload 5
    //   329: invokevirtual 891	android/content/Context:unregisterReceiver	(Landroid/content/BroadcastReceiver;)V
    //   332: aload_1
    //   333: athrow
    //   334: iconst_0
    //   335: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	336	0	paramContext	Context
    //   0	336	1	paramString	String
    //   35	138	2	localEuiccManager	android.telephony.euicc.EuiccManager
    //   55	179	3	localCountDownLatch	CountDownLatch
    //   64	209	4	localAtomicBoolean	AtomicBoolean
    //   76	252	5	local4	4
    //   87	77	6	localObject1	Object
    //   136	31	7	localObject2	Object
    //   192	24	8	l1	long
    //   206	29	10	l2	long
    // Exception table:
    //   from	to	target	type
    //   233	252	278	java/lang/InterruptedException
    //   182	194	282	finally
    //   229	233	282	finally
    //   182	194	286	java/lang/InterruptedException
    //   229	233	286	java/lang/InterruptedException
    //   177	182	290	finally
    //   177	182	294	java/lang/InterruptedException
    //   233	252	322	finally
    //   295	311	322	finally
  }
  
  public static abstract interface ProgressListener
  {
    public abstract void onProgress(int paramInt);
  }
}
