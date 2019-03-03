package android.content.pm;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Build.FEATURES;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PatternMatcher;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.ByteStringUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.PackageUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.TypedValue;
import android.util.apk.ApkSignatureVerifier;
import com.android.internal.R.styleable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.ClassLoaderFactory;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class PackageParser
{
  public static final String ANDROID_MANIFEST_FILENAME = "AndroidManifest.xml";
  private static final String ANDROID_RESOURCES = "http://schemas.android.com/apk/res/android";
  public static final String APK_FILE_EXTENSION = ".apk";
  private static final Set<String> CHILD_PACKAGE_TAGS;
  private static final boolean DEBUG_BACKUP = false;
  private static final boolean DEBUG_JAR = false;
  private static final boolean DEBUG_PARSER = false;
  private static final float DEFAULT_PRE_O_MAX_ASPECT_RATIO = 1.86F;
  private static final boolean LOG_PARSE_TIMINGS = Build.IS_DEBUGGABLE;
  private static final int LOG_PARSE_TIMINGS_THRESHOLD_MS = 100;
  private static final boolean LOG_UNSAFE_BROADCASTS = false;
  private static final String METADATA_MAX_ASPECT_RATIO = "android.max_aspect";
  private static final String MNT_EXPAND = "/mnt/expand/";
  private static final boolean MULTI_PACKAGE_APK_ENABLED;
  public static final NewPermissionInfo[] NEW_PERMISSIONS;
  public static final int PARSE_CHATTY = Integer.MIN_VALUE;
  public static final int PARSE_COLLECT_CERTIFICATES = 32;
  private static final int PARSE_DEFAULT_INSTALL_LOCATION = -1;
  private static final int PARSE_DEFAULT_TARGET_SANDBOX = 1;
  public static final int PARSE_ENFORCE_CODE = 64;
  public static final int PARSE_EXTERNAL_STORAGE = 8;
  public static final int PARSE_FORCE_SDK = 128;
  @Deprecated
  public static final int PARSE_FORWARD_LOCK = 4;
  public static final int PARSE_IGNORE_PROCESSES = 2;
  public static final int PARSE_IS_SYSTEM_DIR = 16;
  public static final int PARSE_MUST_BE_APK = 1;
  private static final String PROPERTY_CHILD_PACKAGES_ENABLED = "persist.sys.child_packages_enabled";
  private static final int RECREATE_ON_CONFIG_CHANGES_MASK = 3;
  private static final boolean RIGID_PARSER = false;
  private static final Set<String> SAFE_BROADCASTS;
  private static final String[] SDK_CODENAMES = Build.VERSION.ACTIVE_CODENAMES;
  private static final int SDK_VERSION;
  public static final SplitPermissionInfo[] SPLIT_PERMISSIONS;
  private static final String TAG = "PackageParser";
  private static final String TAG_ADOPT_PERMISSIONS = "adopt-permissions";
  private static final String TAG_APPLICATION = "application";
  private static final String TAG_COMPATIBLE_SCREENS = "compatible-screens";
  private static final String TAG_EAT_COMMENT = "eat-comment";
  private static final String TAG_FEATURE_GROUP = "feature-group";
  private static final String TAG_INSTRUMENTATION = "instrumentation";
  private static final String TAG_KEY_SETS = "key-sets";
  private static final String TAG_MANIFEST = "manifest";
  private static final String TAG_ORIGINAL_PACKAGE = "original-package";
  private static final String TAG_OVERLAY = "overlay";
  private static final String TAG_PACKAGE = "package";
  private static final String TAG_PACKAGE_VERIFIER = "package-verifier";
  private static final String TAG_PERMISSION = "permission";
  private static final String TAG_PERMISSION_GROUP = "permission-group";
  private static final String TAG_PERMISSION_TREE = "permission-tree";
  private static final String TAG_PROTECTED_BROADCAST = "protected-broadcast";
  private static final String TAG_RESTRICT_UPDATE = "restrict-update";
  private static final String TAG_SUPPORTS_INPUT = "supports-input";
  private static final String TAG_SUPPORT_SCREENS = "supports-screens";
  private static final String TAG_USES_CONFIGURATION = "uses-configuration";
  private static final String TAG_USES_FEATURE = "uses-feature";
  private static final String TAG_USES_GL_TEXTURE = "uses-gl-texture";
  private static final String TAG_USES_PERMISSION = "uses-permission";
  private static final String TAG_USES_PERMISSION_SDK_23 = "uses-permission-sdk-23";
  private static final String TAG_USES_PERMISSION_SDK_M = "uses-permission-sdk-m";
  private static final String TAG_USES_SDK = "uses-sdk";
  private static final String TAG_USES_SPLIT = "uses-split";
  public static final AtomicInteger sCachedPackageReadCount;
  private static boolean sCompatibilityModeEnabled = true;
  private static final Comparator<String> sSplitNameComparator = new SplitNameComparator(null);
  @Deprecated
  private String mArchiveSourcePath;
  private File mCacheDir;
  private Callback mCallback;
  private DisplayMetrics mMetrics = new DisplayMetrics();
  private boolean mOnlyCoreApps;
  private int mParseError = 1;
  private ParsePackageItemArgs mParseInstrumentationArgs;
  private String[] mSeparateProcesses;
  
  static
  {
    boolean bool;
    if ((Build.IS_DEBUGGABLE) && (SystemProperties.getBoolean("persist.sys.child_packages_enabled", false))) {
      bool = true;
    } else {
      bool = false;
    }
    MULTI_PACKAGE_APK_ENABLED = bool;
    CHILD_PACKAGE_TAGS = new ArraySet();
    CHILD_PACKAGE_TAGS.add("application");
    CHILD_PACKAGE_TAGS.add("uses-permission");
    CHILD_PACKAGE_TAGS.add("uses-permission-sdk-m");
    CHILD_PACKAGE_TAGS.add("uses-permission-sdk-23");
    CHILD_PACKAGE_TAGS.add("uses-configuration");
    CHILD_PACKAGE_TAGS.add("uses-feature");
    CHILD_PACKAGE_TAGS.add("feature-group");
    CHILD_PACKAGE_TAGS.add("uses-sdk");
    CHILD_PACKAGE_TAGS.add("supports-screens");
    CHILD_PACKAGE_TAGS.add("instrumentation");
    CHILD_PACKAGE_TAGS.add("uses-gl-texture");
    CHILD_PACKAGE_TAGS.add("compatible-screens");
    CHILD_PACKAGE_TAGS.add("supports-input");
    CHILD_PACKAGE_TAGS.add("eat-comment");
    sCachedPackageReadCount = new AtomicInteger();
    SAFE_BROADCASTS = new ArraySet();
    SAFE_BROADCASTS.add("android.intent.action.BOOT_COMPLETED");
    NEW_PERMISSIONS = new NewPermissionInfo[] { new NewPermissionInfo("android.permission.WRITE_EXTERNAL_STORAGE", 4, 0), new NewPermissionInfo("android.permission.READ_PHONE_STATE", 4, 0) };
    SPLIT_PERMISSIONS = new SplitPermissionInfo[] { new SplitPermissionInfo("android.permission.WRITE_EXTERNAL_STORAGE", new String[] { "android.permission.READ_EXTERNAL_STORAGE" }, 10001), new SplitPermissionInfo("android.permission.READ_CONTACTS", new String[] { "android.permission.READ_CALL_LOG" }, 16), new SplitPermissionInfo("android.permission.WRITE_CONTACTS", new String[] { "android.permission.WRITE_CALL_LOG" }, 16) };
    SDK_VERSION = Build.VERSION.SDK_INT;
  }
  
  public PackageParser()
  {
    mMetrics.setToDefaults();
  }
  
  private void adjustPackageToBeUnresizeableAndUnpipable(Package paramPackage)
  {
    paramPackage = activities.iterator();
    while (paramPackage.hasNext())
    {
      Object localObject = (Activity)paramPackage.next();
      info.resizeMode = 0;
      localObject = info;
      flags &= 0xFFBFFFFF;
    }
  }
  
  private static String buildClassName(String paramString, CharSequence paramCharSequence, String[] paramArrayOfString)
  {
    if ((paramCharSequence != null) && (paramCharSequence.length() > 0))
    {
      paramCharSequence = paramCharSequence.toString();
      if (paramCharSequence.charAt(0) == '.')
      {
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append(paramString);
        paramArrayOfString.append(paramCharSequence);
        return paramArrayOfString.toString();
      }
      if (paramCharSequence.indexOf('.') < 0)
      {
        paramString = new StringBuilder(paramString);
        paramString.append('.');
        paramString.append(paramCharSequence);
        return paramString.toString();
      }
      return paramCharSequence;
    }
    paramCharSequence = new StringBuilder();
    paramCharSequence.append("Empty class name in package ");
    paramCharSequence.append(paramString);
    paramArrayOfString[0] = paramCharSequence.toString();
    return null;
  }
  
  private static String buildCompoundName(String paramString1, CharSequence paramCharSequence, String paramString2, String[] paramArrayOfString)
  {
    paramCharSequence = paramCharSequence.toString();
    int i = paramCharSequence.charAt(0);
    StringBuilder localStringBuilder;
    if ((paramString1 != null) && (i == 58))
    {
      if (paramCharSequence.length() < 2)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Bad ");
        localStringBuilder.append(paramString2);
        localStringBuilder.append(" name ");
        localStringBuilder.append(paramCharSequence);
        localStringBuilder.append(" in package ");
        localStringBuilder.append(paramString1);
        localStringBuilder.append(": must be at least two characters");
        paramArrayOfString[0] = localStringBuilder.toString();
        return null;
      }
      str = validateName(paramCharSequence.substring(1), false, false);
      if (str != null)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid ");
        localStringBuilder.append(paramString2);
        localStringBuilder.append(" name ");
        localStringBuilder.append(paramCharSequence);
        localStringBuilder.append(" in package ");
        localStringBuilder.append(paramString1);
        localStringBuilder.append(": ");
        localStringBuilder.append(str);
        paramArrayOfString[0] = localStringBuilder.toString();
        return null;
      }
      paramString2 = new StringBuilder();
      paramString2.append(paramString1);
      paramString2.append(paramCharSequence);
      return paramString2.toString();
    }
    String str = validateName(paramCharSequence, true, false);
    if ((str != null) && (!"system".equals(paramCharSequence)))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid ");
      localStringBuilder.append(paramString2);
      localStringBuilder.append(" name ");
      localStringBuilder.append(paramCharSequence);
      localStringBuilder.append(" in package ");
      localStringBuilder.append(paramString1);
      localStringBuilder.append(": ");
      localStringBuilder.append(str);
      paramArrayOfString[0] = localStringBuilder.toString();
      return null;
    }
    return paramCharSequence;
  }
  
  private static String buildProcessName(String paramString1, String paramString2, CharSequence paramCharSequence, int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if (((paramInt & 0x2) != 0) && (!"system".equals(paramCharSequence)))
    {
      if (paramString2 != null) {
        paramString1 = paramString2;
      }
      return paramString1;
    }
    if (paramArrayOfString1 != null)
    {
      paramInt = paramArrayOfString1.length - 1;
      while (paramInt >= 0)
      {
        String str = paramArrayOfString1[paramInt];
        if ((!str.equals(paramString1)) && (!str.equals(paramString2)) && (!str.equals(paramCharSequence))) {
          paramInt--;
        } else {
          return paramString1;
        }
      }
    }
    if ((paramCharSequence != null) && (paramCharSequence.length() > 0)) {
      return TextUtils.safeIntern(buildCompoundName(paramString1, paramCharSequence, "process", paramArrayOfString2));
    }
    return paramString2;
  }
  
  private static String buildTaskAffinityName(String paramString1, String paramString2, CharSequence paramCharSequence, String[] paramArrayOfString)
  {
    if (paramCharSequence == null) {
      return paramString2;
    }
    if (paramCharSequence.length() <= 0) {
      return null;
    }
    return buildCompoundName(paramString1, paramCharSequence, "taskAffinity", paramArrayOfString);
  }
  
  /* Error */
  private void cacheResult(File paramFile, int paramInt, Package paramPackage)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 504	android/content/pm/PackageParser:mCacheDir	Ljava/io/File;
    //   4: ifnonnull +4 -> 8
    //   7: return
    //   8: aload_0
    //   9: aload_1
    //   10: iload_2
    //   11: invokespecial 508	android/content/pm/PackageParser:getCacheKey	(Ljava/io/File;I)Ljava/lang/String;
    //   14: astore_1
    //   15: new 510	java/io/File
    //   18: astore 4
    //   20: aload 4
    //   22: aload_0
    //   23: getfield 504	android/content/pm/PackageParser:mCacheDir	Ljava/io/File;
    //   26: aload_1
    //   27: invokespecial 513	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   30: aload 4
    //   32: invokevirtual 516	java/io/File:exists	()Z
    //   35: ifeq +44 -> 79
    //   38: aload 4
    //   40: invokevirtual 519	java/io/File:delete	()Z
    //   43: ifne +36 -> 79
    //   46: new 436	java/lang/StringBuilder
    //   49: astore_1
    //   50: aload_1
    //   51: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   54: aload_1
    //   55: ldc_w 521
    //   58: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: aload_1
    //   63: aload 4
    //   65: invokevirtual 524	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: ldc -78
    //   71: aload_1
    //   72: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   75: invokestatic 530	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   78: pop
    //   79: aload_0
    //   80: aload_3
    //   81: invokevirtual 534	android/content/pm/PackageParser:toCacheEntry	(Landroid/content/pm/PackageParser$Package;)[B
    //   84: astore_3
    //   85: aload_3
    //   86: ifnonnull +4 -> 90
    //   89: return
    //   90: new 536	java/io/FileOutputStream
    //   93: astore 5
    //   95: aload 5
    //   97: aload 4
    //   99: invokespecial 539	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   102: aconst_null
    //   103: astore_1
    //   104: aload 5
    //   106: aload_3
    //   107: invokevirtual 543	java/io/FileOutputStream:write	([B)V
    //   110: aload 5
    //   112: invokevirtual 546	java/io/FileOutputStream:close	()V
    //   115: goto +59 -> 174
    //   118: astore_3
    //   119: goto +8 -> 127
    //   122: astore_3
    //   123: aload_3
    //   124: astore_1
    //   125: aload_3
    //   126: athrow
    //   127: aload_1
    //   128: ifnull +22 -> 150
    //   131: aload 5
    //   133: invokevirtual 546	java/io/FileOutputStream:close	()V
    //   136: goto +19 -> 155
    //   139: astore 5
    //   141: aload_1
    //   142: aload 5
    //   144: invokevirtual 550	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   147: goto +8 -> 155
    //   150: aload 5
    //   152: invokevirtual 546	java/io/FileOutputStream:close	()V
    //   155: aload_3
    //   156: athrow
    //   157: astore_1
    //   158: ldc -78
    //   160: ldc_w 552
    //   163: aload_1
    //   164: invokestatic 556	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   167: pop
    //   168: aload 4
    //   170: invokevirtual 519	java/io/File:delete	()Z
    //   173: pop
    //   174: goto +14 -> 188
    //   177: astore_1
    //   178: ldc -78
    //   180: ldc_w 558
    //   183: aload_1
    //   184: invokestatic 556	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   187: pop
    //   188: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	189	0	this	PackageParser
    //   0	189	1	paramFile	File
    //   0	189	2	paramInt	int
    //   0	189	3	paramPackage	Package
    //   18	151	4	localFile	File
    //   93	39	5	localFileOutputStream	java.io.FileOutputStream
    //   139	12	5	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   104	110	118	finally
    //   125	127	118	finally
    //   104	110	122	java/lang/Throwable
    //   131	136	139	java/lang/Throwable
    //   90	102	157	java/io/IOException
    //   110	115	157	java/io/IOException
    //   131	136	157	java/io/IOException
    //   141	147	157	java/io/IOException
    //   150	155	157	java/io/IOException
    //   155	157	157	java/io/IOException
    //   8	79	177	java/lang/Throwable
    //   79	85	177	java/lang/Throwable
    //   90	102	177	java/lang/Throwable
    //   110	115	177	java/lang/Throwable
    //   141	147	177	java/lang/Throwable
    //   150	155	177	java/lang/Throwable
    //   155	157	177	java/lang/Throwable
    //   158	174	177	java/lang/Throwable
  }
  
  private boolean checkOverlayRequiredSystemProperty(String paramString1, String paramString2)
  {
    boolean bool1 = TextUtils.isEmpty(paramString1);
    boolean bool2 = false;
    if ((!bool1) && (!TextUtils.isEmpty(paramString2)))
    {
      paramString1 = SystemProperties.get(paramString1);
      bool1 = bool2;
      if (paramString1 != null)
      {
        bool1 = bool2;
        if (paramString1.equals(paramString2)) {
          bool1 = true;
        }
      }
      return bool1;
    }
    if ((TextUtils.isEmpty(paramString1)) && (TextUtils.isEmpty(paramString2))) {
      return true;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Disabling overlay - incomplete property :'");
    localStringBuilder.append(paramString1);
    localStringBuilder.append("=");
    localStringBuilder.append(paramString2);
    localStringBuilder.append("' - require both requiredSystemPropertyName AND requiredSystemPropertyValue to be specified.");
    Slog.w("PackageParser", localStringBuilder.toString());
    return false;
  }
  
  private static boolean checkUseInstalledOrHidden(int paramInt, PackageUserState paramPackageUserState, ApplicationInfo paramApplicationInfo)
  {
    boolean bool;
    if ((!paramPackageUserState.isAvailable(paramInt)) && ((paramApplicationInfo == null) || (!paramApplicationInfo.isSystemApp()) || ((0x402000 & paramInt) == 0))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static void collectCertificates(Package paramPackage, File paramFile, boolean paramBoolean)
    throws PackageParser.PackageParserException
  {
    String str = paramFile.getAbsolutePath();
    int i = 1;
    if (applicationInfo.isStaticSharedLibrary()) {
      i = 2;
    }
    if (paramBoolean) {
      paramFile = ApkSignatureVerifier.plsCertsNoVerifyOnlyCerts(str, i);
    } else {
      paramFile = ApkSignatureVerifier.verify(str, i);
    }
    if (mSigningDetails == SigningDetails.UNKNOWN) {
      mSigningDetails = paramFile;
    } else {
      if (!Signature.areExactMatch(mSigningDetails.signatures, signatures)) {
        break label78;
      }
    }
    return;
    label78:
    paramPackage = new StringBuilder();
    paramPackage.append(str);
    paramPackage.append(" has mismatched certificates");
    throw new PackageParserException(-104, paramPackage.toString());
  }
  
  public static void collectCertificates(Package paramPackage, boolean paramBoolean)
    throws PackageParser.PackageParserException
  {
    collectCertificatesInternal(paramPackage, paramBoolean);
    ArrayList localArrayList = childPackages;
    int i = 0;
    int j;
    if (localArrayList != null) {
      j = childPackages.size();
    } else {
      j = 0;
    }
    while (i < j)
    {
      childPackages.get(i)).mSigningDetails = mSigningDetails;
      i++;
    }
  }
  
  private static void collectCertificatesInternal(Package paramPackage, boolean paramBoolean)
    throws PackageParser.PackageParserException
  {
    mSigningDetails = SigningDetails.UNKNOWN;
    Trace.traceBegin(262144L, "collectCertificates");
    try
    {
      File localFile = new java/io/File;
      localFile.<init>(baseCodePath);
      collectCertificates(paramPackage, localFile, paramBoolean);
      if (!ArrayUtils.isEmpty(splitCodePaths)) {
        for (int i = 0; i < splitCodePaths.length; i++)
        {
          localFile = new java/io/File;
          localFile.<init>(splitCodePaths[i]);
          collectCertificates(paramPackage, localFile, paramBoolean);
        }
      }
      return;
    }
    finally
    {
      Trace.traceEnd(262144L);
    }
  }
  
  public static int computeMinSdkVersion(int paramInt1, String paramString, int paramInt2, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if (paramString == null)
    {
      if (paramInt1 <= paramInt2) {
        return paramInt1;
      }
      paramString = new StringBuilder();
      paramString.append("Requires newer sdk version #");
      paramString.append(paramInt1);
      paramString.append(" (current version is #");
      paramString.append(paramInt2);
      paramString.append(")");
      paramArrayOfString2[0] = paramString.toString();
      return -1;
    }
    if (ArrayUtils.contains(paramArrayOfString1, paramString)) {
      return 10000;
    }
    if (paramArrayOfString1.length > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Requires development platform ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" (current platform is any of ");
      localStringBuilder.append(Arrays.toString(paramArrayOfString1));
      localStringBuilder.append(")");
      paramArrayOfString2[0] = localStringBuilder.toString();
    }
    else
    {
      paramArrayOfString1 = new StringBuilder();
      paramArrayOfString1.append("Requires development platform ");
      paramArrayOfString1.append(paramString);
      paramArrayOfString1.append(" but this is a release platform.");
      paramArrayOfString2[0] = paramArrayOfString1.toString();
    }
    return -1;
  }
  
  public static int computeTargetSdkVersion(int paramInt, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean)
  {
    if (paramString == null) {
      return paramInt;
    }
    if ((!ArrayUtils.contains(paramArrayOfString1, paramString)) && (!paramBoolean))
    {
      if (paramArrayOfString1.length > 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Requires development platform ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" (current platform is any of ");
        localStringBuilder.append(Arrays.toString(paramArrayOfString1));
        localStringBuilder.append(")");
        paramArrayOfString2[0] = localStringBuilder.toString();
      }
      else
      {
        paramArrayOfString1 = new StringBuilder();
        paramArrayOfString1.append("Requires development platform ");
        paramArrayOfString1.append(paramString);
        paramArrayOfString1.append(" but this is a release platform.");
        paramArrayOfString2[0] = paramArrayOfString1.toString();
      }
      return -1;
    }
    return 10000;
  }
  
  private static boolean copyNeeded(int paramInt1, Package paramPackage, PackageUserState paramPackageUserState, Bundle paramBundle, int paramInt2)
  {
    if (paramInt2 != 0) {
      return true;
    }
    int i;
    if (enabled != 0)
    {
      if (enabled == 1) {
        i = 1;
      } else {
        i = 0;
      }
      if (applicationInfo.enabled != i) {
        return true;
      }
    }
    if ((applicationInfo.flags & 0x40000000) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if (suspended != i) {
      return true;
    }
    if ((installed) && (!hidden))
    {
      if (stopped) {
        return true;
      }
      if (instantApp != applicationInfo.isInstantApp()) {
        return true;
      }
      if (((paramInt1 & 0x80) != 0) && ((paramBundle != null) || (mAppMetaData != null))) {
        return true;
      }
      if (((paramInt1 & 0x400) != 0) && (usesLibraryFiles != null)) {
        return true;
      }
      return staticSharedLibName != null;
    }
    return true;
  }
  
  @VisibleForTesting
  public static Package fromCacheEntryStatic(byte[] paramArrayOfByte)
  {
    Parcel localParcel = Parcel.obtain();
    localParcel.unmarshall(paramArrayOfByte, 0, paramArrayOfByte.length);
    localParcel.setDataPosition(0);
    new PackageParserCacheHelper.ReadHelper(localParcel).startAndInstall();
    paramArrayOfByte = new Package(localParcel);
    localParcel.recycle();
    sCachedPackageReadCount.incrementAndGet();
    return paramArrayOfByte;
  }
  
  public static final ActivityInfo generateActivityInfo(ActivityInfo paramActivityInfo, int paramInt1, PackageUserState paramPackageUserState, int paramInt2)
  {
    if (paramActivityInfo == null) {
      return null;
    }
    if (!checkUseInstalledOrHidden(paramInt1, paramPackageUserState, applicationInfo)) {
      return null;
    }
    paramActivityInfo = new ActivityInfo(paramActivityInfo);
    applicationInfo = generateApplicationInfo(applicationInfo, paramInt1, paramPackageUserState, paramInt2);
    return paramActivityInfo;
  }
  
  public static final ActivityInfo generateActivityInfo(Activity paramActivity, int paramInt1, PackageUserState paramPackageUserState, int paramInt2)
  {
    if (paramActivity == null) {
      return null;
    }
    if (!checkUseInstalledOrHidden(paramInt1, paramPackageUserState, owner.applicationInfo)) {
      return null;
    }
    if (!copyNeeded(paramInt1, owner, paramPackageUserState, metaData, paramInt2))
    {
      updateApplicationInfo(info.applicationInfo, paramInt1, paramPackageUserState);
      return info;
    }
    ActivityInfo localActivityInfo = new ActivityInfo(info);
    metaData = metaData;
    applicationInfo = generateApplicationInfo(owner, paramInt1, paramPackageUserState, paramInt2);
    return localActivityInfo;
  }
  
  public static ApplicationInfo generateApplicationInfo(ApplicationInfo paramApplicationInfo, int paramInt1, PackageUserState paramPackageUserState, int paramInt2)
  {
    if (paramApplicationInfo == null) {
      return null;
    }
    if (!checkUseInstalledOrHidden(paramInt1, paramPackageUserState, paramApplicationInfo)) {
      return null;
    }
    paramApplicationInfo = new ApplicationInfo(paramApplicationInfo);
    paramApplicationInfo.initForUser(paramInt2);
    if (stopped) {
      flags |= 0x200000;
    } else {
      flags &= 0xFFDFFFFF;
    }
    updateApplicationInfo(paramApplicationInfo, paramInt1, paramPackageUserState);
    return paramApplicationInfo;
  }
  
  public static ApplicationInfo generateApplicationInfo(Package paramPackage, int paramInt, PackageUserState paramPackageUserState)
  {
    return generateApplicationInfo(paramPackage, paramInt, paramPackageUserState, UserHandle.getCallingUserId());
  }
  
  public static ApplicationInfo generateApplicationInfo(Package paramPackage, int paramInt1, PackageUserState paramPackageUserState, int paramInt2)
  {
    if (paramPackage == null) {
      return null;
    }
    if ((checkUseInstalledOrHidden(paramInt1, paramPackageUserState, applicationInfo)) && (paramPackage.isMatch(paramInt1)))
    {
      if ((!copyNeeded(paramInt1, paramPackage, paramPackageUserState, null, paramInt2)) && (((0x8000 & paramInt1) == 0) || (enabled != 4)))
      {
        updateApplicationInfo(applicationInfo, paramInt1, paramPackageUserState);
        return applicationInfo;
      }
      ApplicationInfo localApplicationInfo = new ApplicationInfo(applicationInfo);
      localApplicationInfo.initForUser(paramInt2);
      if ((paramInt1 & 0x80) != 0) {
        metaData = mAppMetaData;
      }
      if ((paramInt1 & 0x400) != 0) {
        sharedLibraryFiles = usesLibraryFiles;
      }
      if (stopped) {
        flags |= 0x200000;
      } else {
        flags &= 0xFFDFFFFF;
      }
      updateApplicationInfo(localApplicationInfo, paramInt1, paramPackageUserState);
      return localApplicationInfo;
    }
    return null;
  }
  
  public static final InstrumentationInfo generateInstrumentationInfo(Instrumentation paramInstrumentation, int paramInt)
  {
    if (paramInstrumentation == null) {
      return null;
    }
    if ((paramInt & 0x80) == 0) {
      return info;
    }
    InstrumentationInfo localInstrumentationInfo = new InstrumentationInfo(info);
    metaData = metaData;
    return localInstrumentationInfo;
  }
  
  public static PackageInfo generatePackageInfo(Package paramPackage, int[] paramArrayOfInt, int paramInt, long paramLong1, long paramLong2, Set<String> paramSet, PackageUserState paramPackageUserState)
  {
    return generatePackageInfo(paramPackage, paramArrayOfInt, paramInt, paramLong1, paramLong2, paramSet, paramPackageUserState, UserHandle.getCallingUserId());
  }
  
  public static PackageInfo generatePackageInfo(Package paramPackage, int[] paramArrayOfInt, int paramInt1, long paramLong1, long paramLong2, Set<String> paramSet, PackageUserState paramPackageUserState, int paramInt2)
  {
    if ((checkUseInstalledOrHidden(paramInt1, paramPackageUserState, applicationInfo)) && (paramPackage.isMatch(paramInt1)))
    {
      PackageInfo localPackageInfo = new PackageInfo();
      packageName = packageName;
      splitNames = splitNames;
      versionCodeMajor = mVersionCodeMajor;
      baseRevisionCode = baseRevisionCode;
      splitRevisionCodes = splitRevisionCodes;
      versionName = mVersionName;
      sharedUserId = mSharedUserId;
      sharedUserLabel = mSharedUserLabel;
      applicationInfo = generateApplicationInfo(paramPackage, paramInt1, paramPackageUserState, paramInt2);
      installLocation = installLocation;
      isStub = isStub;
      coreApp = coreApp;
      if (((Build.ISASUSVERMAX) && ((applicationInfo.flagsAsus & 0x8) > 0)) || ((applicationInfo.flagsAsus & 0x20) > 0)) {
        versionCode = Integer.MAX_VALUE;
      } else {
        versionCode = mVersionCode;
      }
      if (((applicationInfo.flags & 0x1) != 0) || ((applicationInfo.flags & 0x80) != 0)) {
        requiredForAllUsers = mRequiredForAllUsers;
      }
      restrictedAccountType = mRestrictedAccountType;
      requiredAccountType = mRequiredAccountType;
      overlayTarget = mOverlayTarget;
      overlayCategory = mOverlayCategory;
      overlayPriority = mOverlayPriority;
      mOverlayIsStatic = mOverlayIsStatic;
      compileSdkVersion = mCompileSdkVersion;
      compileSdkVersionCodename = mCompileSdkVersionCodename;
      firstInstallTime = paramLong1;
      lastUpdateTime = paramLong2;
      if ((paramInt1 & 0x100) != 0) {
        gids = paramArrayOfInt;
      }
      int i;
      if ((paramInt1 & 0x4000) != 0)
      {
        if (configPreferences != null) {
          i = configPreferences.size();
        } else {
          i = 0;
        }
        if (i > 0)
        {
          configPreferences = new ConfigurationInfo[i];
          configPreferences.toArray(configPreferences);
        }
        if (reqFeatures != null) {
          i = reqFeatures.size();
        } else {
          i = 0;
        }
        if (i > 0)
        {
          reqFeatures = new FeatureInfo[i];
          reqFeatures.toArray(reqFeatures);
        }
        if (featureGroups != null) {
          i = featureGroups.size();
        } else {
          i = 0;
        }
        if (i > 0)
        {
          featureGroups = new FeatureGroupInfo[i];
          featureGroups.toArray(featureGroups);
        }
      }
      int j;
      int k;
      int m;
      Object localObject;
      if ((paramInt1 & 0x1) != 0)
      {
        j = activities.size();
        if (j > 0)
        {
          paramArrayOfInt = new ActivityInfo[j];
          k = 0;
          m = 0;
          while (m < j)
          {
            localObject = (Activity)activities.get(m);
            i = k;
            if (paramPackageUserState.isMatch(info, paramInt1))
            {
              paramArrayOfInt[k] = generateActivityInfo((Activity)localObject, paramInt1, paramPackageUserState, paramInt2);
              i = k + 1;
            }
            m++;
            k = i;
          }
          activities = ((ActivityInfo[])ArrayUtils.trimToSize(paramArrayOfInt, k));
        }
      }
      if ((paramInt1 & 0x2) != 0)
      {
        m = receivers.size();
        if (m > 0)
        {
          paramArrayOfInt = new ActivityInfo[m];
          j = 0;
          k = 0;
          while (k < m)
          {
            localObject = (Activity)receivers.get(k);
            i = j;
            if (paramPackageUserState.isMatch(info, paramInt1))
            {
              paramArrayOfInt[j] = generateActivityInfo((Activity)localObject, paramInt1, paramPackageUserState, paramInt2);
              i = j + 1;
            }
            k++;
            j = i;
          }
          receivers = ((ActivityInfo[])ArrayUtils.trimToSize(paramArrayOfInt, j));
        }
      }
      if ((paramInt1 & 0x4) != 0)
      {
        i = services.size();
        if (i > 0)
        {
          localObject = new ServiceInfo[i];
          k = 0;
          m = 0;
          while (m < i)
          {
            paramArrayOfInt = (Service)services.get(m);
            j = k;
            if (paramPackageUserState.isMatch(info, paramInt1))
            {
              localObject[k] = generateServiceInfo(paramArrayOfInt, paramInt1, paramPackageUserState, paramInt2);
              j = k + 1;
            }
            m++;
            k = j;
          }
          services = ((ServiceInfo[])ArrayUtils.trimToSize((Object[])localObject, k));
        }
      }
      if ((paramInt1 & 0x8) != 0)
      {
        k = providers.size();
        if (k > 0)
        {
          paramArrayOfInt = new ProviderInfo[k];
          i = 0;
          m = 0;
          while (m < k)
          {
            localObject = (Provider)providers.get(m);
            j = i;
            if (paramPackageUserState.isMatch(info, paramInt1))
            {
              paramArrayOfInt[i] = generateProviderInfo((Provider)localObject, paramInt1, paramPackageUserState, paramInt2);
              j = i + 1;
            }
            m++;
            i = j;
          }
          providers = ((ProviderInfo[])ArrayUtils.trimToSize(paramArrayOfInt, i));
        }
      }
      if ((paramInt1 & 0x10) != 0)
      {
        i = instrumentation.size();
        if (i > 0)
        {
          instrumentation = new InstrumentationInfo[i];
          for (paramInt2 = 0; paramInt2 < i; paramInt2++) {
            instrumentation[paramInt2] = generateInstrumentationInfo((Instrumentation)instrumentation.get(paramInt2), paramInt1);
          }
        }
      }
      if ((paramInt1 & 0x1000) != 0)
      {
        i = permissions.size();
        if (i > 0)
        {
          permissions = new PermissionInfo[i];
          for (paramInt2 = 0; paramInt2 < i; paramInt2++) {
            permissions[paramInt2] = generatePermissionInfo((Permission)permissions.get(paramInt2), paramInt1);
          }
        }
        i = requestedPermissions.size();
        if (i > 0)
        {
          requestedPermissions = new String[i];
          requestedPermissionsFlags = new int[i];
          for (paramInt2 = 0; paramInt2 < i; paramInt2++)
          {
            paramPackageUserState = (String)requestedPermissions.get(paramInt2);
            requestedPermissions[paramInt2] = paramPackageUserState;
            paramArrayOfInt = requestedPermissionsFlags;
            paramArrayOfInt[paramInt2] |= 0x1;
            if ((paramSet != null) && (paramSet.contains(paramPackageUserState)))
            {
              paramArrayOfInt = requestedPermissionsFlags;
              paramArrayOfInt[paramInt2] |= 0x2;
            }
          }
        }
      }
      if ((paramInt1 & 0x40) != 0) {
        if (mSigningDetails.hasPastSigningCertificates())
        {
          signatures = new Signature[1];
          signatures[0] = mSigningDetails.pastSigningCertificates[0];
        }
        else if (mSigningDetails.hasSignatures())
        {
          paramInt2 = mSigningDetails.signatures.length;
          signatures = new Signature[paramInt2];
          System.arraycopy(mSigningDetails.signatures, 0, signatures, 0, paramInt2);
        }
      }
      if ((0x8000000 & paramInt1) != 0) {
        if (mSigningDetails != SigningDetails.UNKNOWN) {
          signingInfo = new SigningInfo(mSigningDetails);
        } else {
          signingInfo = null;
        }
      }
      return localPackageInfo;
    }
    return null;
  }
  
  public static final PermissionGroupInfo generatePermissionGroupInfo(PermissionGroup paramPermissionGroup, int paramInt)
  {
    if (paramPermissionGroup == null) {
      return null;
    }
    if ((paramInt & 0x80) == 0) {
      return info;
    }
    PermissionGroupInfo localPermissionGroupInfo = new PermissionGroupInfo(info);
    metaData = metaData;
    return localPermissionGroupInfo;
  }
  
  public static final PermissionInfo generatePermissionInfo(Permission paramPermission, int paramInt)
  {
    if (paramPermission == null) {
      return null;
    }
    if ((paramInt & 0x80) == 0) {
      return info;
    }
    PermissionInfo localPermissionInfo = new PermissionInfo(info);
    metaData = metaData;
    return localPermissionInfo;
  }
  
  public static final ProviderInfo generateProviderInfo(Provider paramProvider, int paramInt1, PackageUserState paramPackageUserState, int paramInt2)
  {
    if (paramProvider == null) {
      return null;
    }
    if (!checkUseInstalledOrHidden(paramInt1, paramPackageUserState, owner.applicationInfo)) {
      return null;
    }
    if ((!copyNeeded(paramInt1, owner, paramPackageUserState, metaData, paramInt2)) && (((paramInt1 & 0x800) != 0) || (info.uriPermissionPatterns == null)))
    {
      updateApplicationInfo(info.applicationInfo, paramInt1, paramPackageUserState);
      return info;
    }
    ProviderInfo localProviderInfo = new ProviderInfo(info);
    metaData = metaData;
    if ((paramInt1 & 0x800) == 0) {
      uriPermissionPatterns = null;
    }
    applicationInfo = generateApplicationInfo(owner, paramInt1, paramPackageUserState, paramInt2);
    return localProviderInfo;
  }
  
  public static final ServiceInfo generateServiceInfo(Service paramService, int paramInt1, PackageUserState paramPackageUserState, int paramInt2)
  {
    if (paramService == null) {
      return null;
    }
    if (!checkUseInstalledOrHidden(paramInt1, paramPackageUserState, owner.applicationInfo)) {
      return null;
    }
    if (!copyNeeded(paramInt1, owner, paramPackageUserState, metaData, paramInt2))
    {
      updateApplicationInfo(info.applicationInfo, paramInt1, paramPackageUserState);
      return info;
    }
    ServiceInfo localServiceInfo = new ServiceInfo(info);
    metaData = metaData;
    applicationInfo = generateApplicationInfo(owner, paramInt1, paramPackageUserState, paramInt2);
    return localServiceInfo;
  }
  
  public static int getActivityConfigChanges(int paramInt1, int paramInt2)
  {
    return paramInt2 & 0x3 | paramInt1;
  }
  
  private String getCacheKey(File paramFile, int paramInt)
  {
    paramFile = new StringBuilder(paramFile.getName());
    paramFile.append('-');
    paramFile.append(paramInt);
    return paramFile.toString();
  }
  
  private Package getCachedResult(File paramFile, int paramInt)
  {
    if (mCacheDir == null) {
      return null;
    }
    Object localObject = getCacheKey(paramFile, paramInt);
    localObject = new File(mCacheDir, (String)localObject);
    try
    {
      if (!isCacheUpToDate(paramFile, (File)localObject)) {
        return null;
      }
      paramFile = fromCacheEntry(IoUtils.readFileAsByteArray(((File)localObject).getAbsolutePath()));
      if (mCallback != null)
      {
        String[] arrayOfString = mCallback.getOverlayApks(packageName);
        if ((arrayOfString != null) && (arrayOfString.length > 0))
        {
          int i = arrayOfString.length;
          for (paramInt = 0; paramInt < i; paramInt++)
          {
            String str = arrayOfString[paramInt];
            File localFile = new java/io/File;
            localFile.<init>(str);
            boolean bool = isCacheUpToDate(localFile, (File)localObject);
            if (!bool) {
              return null;
            }
          }
        }
      }
      return paramFile;
    }
    catch (Throwable paramFile)
    {
      Slog.w("PackageParser", "Error reading package cache: ", paramFile);
      ((File)localObject).delete();
    }
    return null;
  }
  
  private static boolean hasDomainURLs(Package paramPackage)
  {
    if ((paramPackage != null) && (activities != null))
    {
      paramPackage = activities;
      int i = paramPackage.size();
      for (int j = 0; j < i; j++)
      {
        ArrayList localArrayList = getintents;
        if (localArrayList != null)
        {
          int k = localArrayList.size();
          int m = 0;
          while (m < k)
          {
            ActivityIntentInfo localActivityIntentInfo = (ActivityIntentInfo)localArrayList.get(m);
            if ((!localActivityIntentInfo.hasAction("android.intent.action.VIEW")) || (!localActivityIntentInfo.hasAction("android.intent.action.VIEW")) || ((!localActivityIntentInfo.hasDataScheme("http")) && (!localActivityIntentInfo.hasDataScheme("https")))) {
              m++;
            } else {
              return true;
            }
          }
        }
      }
      return false;
    }
    return false;
  }
  
  public static final boolean isApkFile(File paramFile)
  {
    return isApkPath(paramFile.getName());
  }
  
  public static boolean isApkPath(String paramString)
  {
    return paramString.endsWith(".apk");
  }
  
  public static boolean isAvailable(PackageUserState paramPackageUserState)
  {
    return checkUseInstalledOrHidden(0, paramPackageUserState, null);
  }
  
  private static boolean isCacheUpToDate(File paramFile1, File paramFile2)
  {
    boolean bool = false;
    try
    {
      paramFile1 = Os.stat(paramFile1.getAbsolutePath());
      paramFile2 = Os.stat(paramFile2.getAbsolutePath());
      long l1 = st_mtime;
      long l2 = st_mtime;
      if (l1 < l2) {
        bool = true;
      }
      return bool;
    }
    catch (ErrnoException paramFile1)
    {
      if (errno != OsConstants.ENOENT) {
        Slog.w("Error while stating package cache : ", paramFile1);
      }
    }
    return false;
  }
  
  private boolean isImplicitlyExposedIntent(IntentInfo paramIntentInfo)
  {
    boolean bool;
    if ((!paramIntentInfo.hasCategory("android.intent.category.BROWSABLE")) && (!paramIntentInfo.hasAction("android.intent.action.SEND")) && (!paramIntentInfo.hasAction("android.intent.action.SENDTO")) && (!paramIntentInfo.hasAction("android.intent.action.SEND_MULTIPLE"))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static AssetManager newConfiguredAssetManager()
  {
    AssetManager localAssetManager = new AssetManager();
    localAssetManager.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
    return localAssetManager;
  }
  
  private Activity parseActivity(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt, String[] paramArrayOfString, CachedComponentArgs paramCachedComponentArgs, boolean paramBoolean1, boolean paramBoolean2)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = paramPackage;
    Object localObject2 = paramResources;
    Object localObject3 = paramXmlResourceParser;
    paramPackage = paramArrayOfString;
    Object localObject4 = ((Resources)localObject2).obtainAttributes((AttributeSet)localObject3, R.styleable.AndroidManifestActivity);
    if (mActivityArgs == null) {
      mActivityArgs = new ParseComponentArgs((Package)localObject1, paramPackage, 3, 1, 2, 44, 23, 30, mSeparateProcesses, 7, 17, 5);
    }
    Object localObject5 = mActivityArgs;
    if (paramBoolean1) {
      localObject6 = "<receiver>";
    } else {
      localObject6 = "<activity>";
    }
    tag = ((String)localObject6);
    mActivityArgs.sa = ((TypedArray)localObject4);
    mActivityArgs.flags = paramInt;
    localObject5 = new Activity(mActivityArgs, new ActivityInfo());
    if (paramPackage[0] != null)
    {
      ((TypedArray)localObject4).recycle();
      return null;
    }
    boolean bool1 = ((TypedArray)localObject4).hasValue(6);
    if (bool1) {
      info.exported = ((TypedArray)localObject4).getBoolean(6, false);
    }
    info.theme = ((TypedArray)localObject4).getResourceId(0, 0);
    info.uiOptions = ((TypedArray)localObject4).getInt(26, info.applicationInfo.uiOptions);
    paramCachedComponentArgs = ((TypedArray)localObject4).getNonConfigurationString(27, 1024);
    if (paramCachedComponentArgs != null)
    {
      localObject6 = buildClassName(info.packageName, paramCachedComponentArgs, paramPackage);
      if (paramPackage[0] == null)
      {
        info.parentActivityName = ((String)localObject6);
      }
      else
      {
        localObject6 = new StringBuilder();
        ((StringBuilder)localObject6).append("Activity ");
        ((StringBuilder)localObject6).append(info.name);
        ((StringBuilder)localObject6).append(" specified invalid parentActivityName ");
        ((StringBuilder)localObject6).append(paramCachedComponentArgs);
        Log.e("PackageParser", ((StringBuilder)localObject6).toString());
        paramPackage[0] = null;
      }
    }
    paramCachedComponentArgs = ((TypedArray)localObject4).getNonConfigurationString(4, 0);
    if (paramCachedComponentArgs == null)
    {
      info.permission = applicationInfo.permission;
    }
    else
    {
      localObject6 = info;
      if (paramCachedComponentArgs.length() > 0) {
        paramCachedComponentArgs = paramCachedComponentArgs.toString().intern();
      } else {
        paramCachedComponentArgs = null;
      }
      permission = paramCachedComponentArgs;
    }
    Object localObject6 = ((TypedArray)localObject4).getNonConfigurationString(8, 1024);
    info.taskAffinity = buildTaskAffinityName(applicationInfo.packageName, applicationInfo.taskAffinity, (CharSequence)localObject6, paramPackage);
    info.splitName = ((TypedArray)localObject4).getNonConfigurationString(48, 0);
    info.flags = 0;
    if (((TypedArray)localObject4).getBoolean(9, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x1;
    }
    if (((TypedArray)localObject4).getBoolean(10, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x2;
    }
    if (((TypedArray)localObject4).getBoolean(11, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x4;
    }
    if (((TypedArray)localObject4).getBoolean(21, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x80;
    }
    if (((TypedArray)localObject4).getBoolean(18, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x8;
    }
    if (((TypedArray)localObject4).getBoolean(12, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x10;
    }
    if (((TypedArray)localObject4).getBoolean(13, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x20;
    }
    boolean bool2;
    if ((applicationInfo.flags & 0x20) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    if (((TypedArray)localObject4).getBoolean(19, bool2))
    {
      paramCachedComponentArgs = info;
      flags |= 0x40;
    }
    if (((TypedArray)localObject4).getBoolean(22, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x100;
    }
    if ((((TypedArray)localObject4).getBoolean(29, false)) || (((TypedArray)localObject4).getBoolean(39, false)))
    {
      paramCachedComponentArgs = info;
      flags |= 0x400;
    }
    if (((TypedArray)localObject4).getBoolean(24, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x800;
    }
    if (((TypedArray)localObject4).getBoolean(54, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x20000000;
    }
    ActivityInfo localActivityInfo;
    if (!paramBoolean1)
    {
      if (((TypedArray)localObject4).getBoolean(25, paramBoolean2))
      {
        paramCachedComponentArgs = info;
        flags |= 0x200;
      }
      info.launchMode = ((TypedArray)localObject4).getInt(14, 0);
      info.documentLaunchMode = ((TypedArray)localObject4).getInt(33, 0);
      info.maxRecents = ((TypedArray)localObject4).getInt(34, ActivityManager.getDefaultAppRecentsLimitStatic());
      info.configChanges = getActivityConfigChanges(((TypedArray)localObject4).getInt(16, 0), ((TypedArray)localObject4).getInt(47, 0));
      info.softInputMode = ((TypedArray)localObject4).getInt(20, 0);
      info.persistableMode = ((TypedArray)localObject4).getInteger(32, 0);
      if (((TypedArray)localObject4).getBoolean(31, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x80000000;
      }
      if (((TypedArray)localObject4).getBoolean(35, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x2000;
      }
      if (((TypedArray)localObject4).getBoolean(36, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x1000;
      }
      if (((TypedArray)localObject4).getBoolean(37, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x4000;
      }
      info.screenOrientation = ((TypedArray)localObject4).getInt(15, -1);
      setActivityResizeMode(info, (TypedArray)localObject4, (Package)localObject1);
      if (((TypedArray)localObject4).getBoolean(41, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x400000;
      }
      if (((TypedArray)localObject4).getBoolean(53, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x40000;
      }
      if ((((TypedArray)localObject4).hasValue(50)) && (((TypedArray)localObject4).getType(50) == 4)) {
        ((Activity)localObject5).setMaxAspectRatio(((TypedArray)localObject4).getFloat(50, 0.0F));
      }
      info.lockTaskLaunchMode = ((TypedArray)localObject4).getInt(38, 0);
      paramCachedComponentArgs = info;
      localActivityInfo = info;
      paramBoolean2 = ((TypedArray)localObject4).getBoolean(42, false);
      directBootAware = paramBoolean2;
      encryptionAware = paramBoolean2;
      info.requestedVrComponent = ((TypedArray)localObject4).getString(43);
      info.rotationAnimation = ((TypedArray)localObject4).getInt(46, -1);
      info.colorMode = ((TypedArray)localObject4).getInt(49, 0);
      if (((TypedArray)localObject4).getBoolean(51, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x800000;
      }
      if (((TypedArray)localObject4).getBoolean(52, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x1000000;
      }
    }
    else
    {
      info.launchMode = 0;
      info.configChanges = 0;
      if (((TypedArray)localObject4).getBoolean(28, false))
      {
        paramCachedComponentArgs = info;
        flags |= 0x40000000;
      }
      paramCachedComponentArgs = info;
      localActivityInfo = info;
      paramBoolean2 = ((TypedArray)localObject4).getBoolean(42, false);
      directBootAware = paramBoolean2;
      encryptionAware = paramBoolean2;
    }
    if (info.directBootAware)
    {
      paramCachedComponentArgs = applicationInfo;
      privateFlags |= 0x100;
    }
    paramBoolean2 = ((TypedArray)localObject4).getBoolean(45, false);
    if (paramBoolean2)
    {
      paramCachedComponentArgs = info;
      flags |= 0x100000;
      visibleToInstantApps = true;
    }
    ((TypedArray)localObject4).recycle();
    if ((paramBoolean1) && ((applicationInfo.privateFlags & 0x2) != 0) && (info.processName == packageName)) {
      paramPackage[0] = "Heavy-weight applications can not have receivers in main process";
    }
    if (paramPackage[0] != null) {
      return null;
    }
    int i = paramXmlResourceParser.getDepth();
    paramCachedComponentArgs = (CachedComponentArgs)localObject2;
    localObject2 = localObject4;
    for (;;)
    {
      paramInt = paramXmlResourceParser.next();
      if (paramInt == 1) {
        break;
      }
      if ((paramInt == 3) && (paramXmlResourceParser.getDepth() <= i)) {
        break;
      }
      if (paramInt != 3)
      {
        if (paramInt == 4)
        {
          localObject4 = paramPackage;
          paramPackage = (Package)localObject3;
          localObject3 = localObject4;
        }
        else
        {
          if (paramXmlResourceParser.getName().equals("intent-filter"))
          {
            paramPackage = new ActivityIntentInfo((Activity)localObject5);
            if (!parseIntent(paramCachedComponentArgs, (XmlResourceParser)localObject3, true, true, paramPackage, paramArrayOfString)) {
              return null;
            }
            if (paramPackage.countActions() == 0)
            {
              paramCachedComponentArgs = new StringBuilder();
              paramCachedComponentArgs.append("No actions in intent filter at ");
              paramCachedComponentArgs.append(mArchiveSourcePath);
              paramCachedComponentArgs.append(" ");
              paramCachedComponentArgs.append(paramXmlResourceParser.getPositionDescription());
              Slog.w("PackageParser", paramCachedComponentArgs.toString());
            }
            else
            {
              order = Math.max(paramPackage.getOrder(), order);
              intents.add(paramPackage);
            }
            if (paramBoolean2) {
              paramInt = 1;
            } else if ((!paramBoolean1) && (isImplicitlyExposedIntent(paramPackage))) {
              paramInt = 2;
            } else {
              paramInt = 0;
            }
            paramPackage.setVisibilityToInstantApp(paramInt);
            if (paramPackage.isVisibleToInstantApp())
            {
              paramCachedComponentArgs = info;
              flags |= 0x100000;
            }
            if (paramPackage.isImplicitlyVisibleToInstantApp())
            {
              paramPackage = info;
              flags |= 0x200000;
            }
          }
          else
          {
            paramPackage = (Package)localObject1;
            if ((!paramBoolean1) && (paramXmlResourceParser.getName().equals("preferred")))
            {
              paramCachedComponentArgs = new ActivityIntentInfo((Activity)localObject5);
              if (!parseIntent(paramResources, paramXmlResourceParser, false, false, paramCachedComponentArgs, paramArrayOfString)) {
                return null;
              }
              if (paramCachedComponentArgs.countActions() == 0)
              {
                paramPackage = new StringBuilder();
                paramPackage.append("No actions in preferred at ");
                paramPackage.append(mArchiveSourcePath);
                paramPackage.append(" ");
                paramPackage.append(paramXmlResourceParser.getPositionDescription());
                Slog.w("PackageParser", paramPackage.toString());
              }
              else
              {
                if (preferredActivityFilters == null) {
                  preferredActivityFilters = new ArrayList();
                }
                preferredActivityFilters.add(paramCachedComponentArgs);
              }
              if (paramBoolean2) {
                paramInt = 1;
              } else if ((!paramBoolean1) && (isImplicitlyExposedIntent(paramCachedComponentArgs))) {
                paramInt = 2;
              } else {
                paramInt = 0;
              }
              paramCachedComponentArgs.setVisibilityToInstantApp(paramInt);
              if (paramCachedComponentArgs.isVisibleToInstantApp())
              {
                paramPackage = info;
                flags |= 0x100000;
              }
              if (paramCachedComponentArgs.isImplicitlyVisibleToInstantApp())
              {
                paramPackage = info;
                flags |= 0x200000;
              }
            }
            else
            {
              if (!paramXmlResourceParser.getName().equals("meta-data")) {
                break label2376;
              }
              localObject4 = metaData;
              paramCachedComponentArgs = paramResources;
              paramPackage = paramXmlResourceParser;
              localObject3 = paramArrayOfString;
              localObject4 = parseMetaData(paramCachedComponentArgs, paramPackage, (Bundle)localObject4, (String[])localObject3);
              metaData = ((Bundle)localObject4);
              if (localObject4 == null) {
                return null;
              }
              if (("com.android.systemui".equals(info.packageName)) && (metaData != null) && (metaData.containsKey("alwaysFocusable")) && (metaData.getBoolean("alwaysFocusable", false)))
              {
                localObject4 = info;
                flags |= 0x40000;
                break label2652;
              }
            }
          }
          localObject3 = paramArrayOfString;
          paramPackage = paramXmlResourceParser;
          paramCachedComponentArgs = paramResources;
          break label2652;
          label2376:
          paramCachedComponentArgs = paramResources;
          paramPackage = paramXmlResourceParser;
          localObject3 = paramArrayOfString;
          if ((!paramBoolean1) && (paramXmlResourceParser.getName().equals("layout")))
          {
            parseLayout(paramCachedComponentArgs, paramPackage, (Activity)localObject5);
          }
          else
          {
            localObject4 = new StringBuilder();
            ((StringBuilder)localObject4).append("Problem in package ");
            ((StringBuilder)localObject4).append(mArchiveSourcePath);
            ((StringBuilder)localObject4).append(":");
            Slog.w("PackageParser", ((StringBuilder)localObject4).toString());
            if (paramBoolean1)
            {
              localObject4 = new StringBuilder();
              ((StringBuilder)localObject4).append("Unknown element under <receiver>: ");
              ((StringBuilder)localObject4).append(paramXmlResourceParser.getName());
              ((StringBuilder)localObject4).append(" at ");
              ((StringBuilder)localObject4).append(mArchiveSourcePath);
              ((StringBuilder)localObject4).append(" ");
              ((StringBuilder)localObject4).append(paramXmlResourceParser.getPositionDescription());
              Slog.w("PackageParser", ((StringBuilder)localObject4).toString());
            }
            else
            {
              localObject4 = new StringBuilder();
              ((StringBuilder)localObject4).append("Unknown element under <activity>: ");
              ((StringBuilder)localObject4).append(paramXmlResourceParser.getName());
              ((StringBuilder)localObject4).append(" at ");
              ((StringBuilder)localObject4).append(mArchiveSourcePath);
              ((StringBuilder)localObject4).append(" ");
              ((StringBuilder)localObject4).append(paramXmlResourceParser.getPositionDescription());
              Slog.w("PackageParser", ((StringBuilder)localObject4).toString());
            }
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
        }
      }
      else
      {
        localObject4 = localObject3;
        localObject3 = paramPackage;
        paramPackage = (Package)localObject4;
      }
      label2652:
      localObject4 = localObject1;
      localObject1 = paramPackage;
      paramPackage = (Package)localObject3;
      localObject3 = localObject1;
      localObject1 = localObject4;
    }
    paramBoolean1 = false;
    if (!bool1)
    {
      paramPackage = info;
      if (intents.size() > 0) {
        paramBoolean1 = true;
      }
      exported = paramBoolean1;
    }
    return localObject5;
  }
  
  private Activity parseActivityAlias(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt, String[] paramArrayOfString, CachedComponentArgs paramCachedComponentArgs)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = paramResources;
    Object localObject2 = paramXmlResourceParser;
    TypedArray localTypedArray = ((Resources)localObject1).obtainAttributes((AttributeSet)localObject2, R.styleable.AndroidManifestActivityAlias);
    Object localObject3 = localTypedArray.getNonConfigurationString(7, 1024);
    if (localObject3 == null)
    {
      paramArrayOfString[0] = "<activity-alias> does not specify android:targetActivity";
      localTypedArray.recycle();
      return null;
    }
    localObject3 = buildClassName(applicationInfo.packageName, (CharSequence)localObject3, paramArrayOfString);
    if (localObject3 == null)
    {
      localTypedArray.recycle();
      return null;
    }
    if (mActivityAliasArgs == null)
    {
      mActivityAliasArgs = new ParseComponentArgs(paramPackage, paramArrayOfString, 2, 0, 1, 11, 8, 10, mSeparateProcesses, 0, 6, 4);
      mActivityAliasArgs.tag = "<activity-alias>";
    }
    mActivityAliasArgs.sa = localTypedArray;
    mActivityAliasArgs.flags = paramInt;
    int i = activities.size();
    paramInt = 0;
    while (paramInt < i)
    {
      Activity localActivity = (Activity)activities.get(paramInt);
      String str = info.name;
      localObject4 = localObject3;
      if (((String)localObject4).equals(str))
      {
        paramPackage = localActivity;
        break label226;
      }
      paramInt++;
      localObject3 = localObject4;
    }
    paramPackage = null;
    label226:
    if (paramPackage == null)
    {
      paramPackage = new StringBuilder();
      paramPackage.append("<activity-alias> target activity ");
      paramPackage.append((String)localObject3);
      paramPackage.append(" not found in manifest");
      paramArrayOfString[0] = paramPackage.toString();
      localTypedArray.recycle();
      return null;
    }
    Object localObject4 = new ActivityInfo();
    targetActivity = ((String)localObject3);
    configChanges = info.configChanges;
    flags = info.flags;
    icon = info.icon;
    logo = info.logo;
    banner = info.banner;
    labelRes = info.labelRes;
    nonLocalizedLabel = info.nonLocalizedLabel;
    launchMode = info.launchMode;
    lockTaskLaunchMode = info.lockTaskLaunchMode;
    processName = info.processName;
    if (descriptionRes == 0) {
      descriptionRes = info.descriptionRes;
    }
    screenOrientation = info.screenOrientation;
    taskAffinity = info.taskAffinity;
    theme = info.theme;
    softInputMode = info.softInputMode;
    uiOptions = info.uiOptions;
    parentActivityName = info.parentActivityName;
    maxRecents = info.maxRecents;
    windowLayout = info.windowLayout;
    resizeMode = info.resizeMode;
    maxAspectRatio = info.maxAspectRatio;
    requestedVrComponent = info.requestedVrComponent;
    boolean bool = info.directBootAware;
    directBootAware = bool;
    encryptionAware = bool;
    localObject4 = new Activity(mActivityAliasArgs, (ActivityInfo)localObject4);
    if (paramArrayOfString[0] != null)
    {
      localTypedArray.recycle();
      return null;
    }
    bool = localTypedArray.hasValue(5);
    if (bool) {
      info.exported = localTypedArray.getBoolean(5, false);
    }
    localObject3 = localTypedArray.getNonConfigurationString(3, 0);
    if (localObject3 != null)
    {
      paramCachedComponentArgs = info;
      if (((String)localObject3).length() > 0) {
        paramPackage = ((String)localObject3).toString().intern();
      } else {
        paramPackage = null;
      }
      permission = paramPackage;
    }
    paramPackage = localTypedArray.getNonConfigurationString(9, 1024);
    if (paramPackage != null)
    {
      paramCachedComponentArgs = buildClassName(info.packageName, paramPackage, paramArrayOfString);
      if (paramArrayOfString[0] == null)
      {
        info.parentActivityName = paramCachedComponentArgs;
      }
      else
      {
        paramCachedComponentArgs = new StringBuilder();
        paramCachedComponentArgs.append("Activity alias ");
        paramCachedComponentArgs.append(info.name);
        paramCachedComponentArgs.append(" specified invalid parentActivityName ");
        paramCachedComponentArgs.append(paramPackage);
        Log.e("PackageParser", paramCachedComponentArgs.toString());
        paramArrayOfString[0] = null;
      }
    }
    i = info.flags;
    paramInt = 1;
    if ((i & 0x100000) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    localTypedArray.recycle();
    if (paramArrayOfString[0] != null) {
      return null;
    }
    int j = paramXmlResourceParser.getDepth();
    paramPackage = (Package)localObject1;
    paramCachedComponentArgs = (CachedComponentArgs)localObject2;
    localObject2 = localTypedArray;
    for (;;)
    {
      int k = paramXmlResourceParser.next();
      if (k == paramInt) {
        break;
      }
      if ((k == 3) && (paramXmlResourceParser.getDepth() <= j))
      {
        paramInt = 1;
        break;
      }
      if ((k != 3) && (k != 4)) {
        if (paramXmlResourceParser.getName().equals("intent-filter"))
        {
          localObject1 = new ActivityIntentInfo((Activity)localObject4);
          if (!parseIntent(paramPackage, paramCachedComponentArgs, true, true, (IntentInfo)localObject1, paramArrayOfString)) {
            return null;
          }
          if (((ActivityIntentInfo)localObject1).countActions() == 0)
          {
            paramPackage = new StringBuilder();
            paramPackage.append("No actions in intent filter at ");
            paramPackage.append(mArchiveSourcePath);
            paramPackage.append(" ");
            paramPackage.append(paramXmlResourceParser.getPositionDescription());
            Slog.w("PackageParser", paramPackage.toString());
          }
          else
          {
            order = Math.max(((ActivityIntentInfo)localObject1).getOrder(), order);
            intents.add(localObject1);
          }
          if (i != 0) {
            paramInt = 1;
          } else if (isImplicitlyExposedIntent((IntentInfo)localObject1)) {
            paramInt = 2;
          } else {
            paramInt = 0;
          }
          ((ActivityIntentInfo)localObject1).setVisibilityToInstantApp(paramInt);
          if (((ActivityIntentInfo)localObject1).isVisibleToInstantApp())
          {
            paramPackage = info;
            flags |= 0x100000;
          }
          if (((ActivityIntentInfo)localObject1).isImplicitlyVisibleToInstantApp())
          {
            paramPackage = info;
            flags |= 0x200000;
          }
          paramCachedComponentArgs = paramXmlResourceParser;
          paramPackage = paramResources;
        }
        else if (paramXmlResourceParser.getName().equals("meta-data"))
        {
          localObject1 = metaData;
          paramCachedComponentArgs = paramXmlResourceParser;
          paramPackage = paramResources;
          localObject1 = parseMetaData(paramPackage, paramCachedComponentArgs, (Bundle)localObject1, paramArrayOfString);
          metaData = ((Bundle)localObject1);
          if (localObject1 == null) {
            return null;
          }
        }
        else
        {
          paramCachedComponentArgs = paramXmlResourceParser;
          paramPackage = paramResources;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unknown element under <activity-alias>: ");
          ((StringBuilder)localObject1).append(paramXmlResourceParser.getName());
          ((StringBuilder)localObject1).append(" at ");
          ((StringBuilder)localObject1).append(mArchiveSourcePath);
          ((StringBuilder)localObject1).append(" ");
          ((StringBuilder)localObject1).append(paramXmlResourceParser.getPositionDescription());
          Slog.w("PackageParser", ((StringBuilder)localObject1).toString());
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
      }
      paramInt = 1;
    }
    if (!bool)
    {
      paramPackage = info;
      if (intents.size() <= 0) {
        paramInt = 0;
      }
      exported = paramInt;
    }
    return localObject4;
  }
  
  private String[] parseAdditionalCertificates(Resources paramResources, XmlResourceParser paramXmlResourceParser, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    String[] arrayOfString = EmptyArray.STRING;
    int i = paramXmlResourceParser.getDepth();
    for (;;)
    {
      int j = paramXmlResourceParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlResourceParser.getDepth() <= i))) {
        break;
      }
      if ((j != 3) && (j != 4)) {
        if (paramXmlResourceParser.getName().equals("additional-certificate"))
        {
          TypedArray localTypedArray = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestAdditionalCertificate);
          String str = localTypedArray.getNonResourceString(0);
          localTypedArray.recycle();
          if (TextUtils.isEmpty(str))
          {
            paramResources = new StringBuilder();
            paramResources.append("Bad additional-certificate declaration with empty certDigest:");
            paramResources.append(str);
            paramArrayOfString[0] = paramResources.toString();
            mParseError = -108;
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
            localTypedArray.recycle();
            return null;
          }
          arrayOfString = (String[])ArrayUtils.appendElement(String.class, arrayOfString, str.replace(":", "").toLowerCase());
        }
        else
        {
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
      }
    }
    return arrayOfString;
  }
  
  private boolean parseAllMetaData(Resources paramResources, XmlResourceParser paramXmlResourceParser, String paramString, Component<?> paramComponent, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlResourceParser.getDepth();
    for (;;)
    {
      int j = paramXmlResourceParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlResourceParser.getDepth() <= i))) {
        break;
      }
      if ((j != 3) && (j != 4))
      {
        Object localObject;
        if (paramXmlResourceParser.getName().equals("meta-data"))
        {
          localObject = parseMetaData(paramResources, paramXmlResourceParser, metaData, paramArrayOfString);
          metaData = ((Bundle)localObject);
          if (localObject == null) {
            return false;
          }
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown element under ");
          ((StringBuilder)localObject).append(paramString);
          ((StringBuilder)localObject).append(": ");
          ((StringBuilder)localObject).append(paramXmlResourceParser.getName());
          ((StringBuilder)localObject).append(" at ");
          ((StringBuilder)localObject).append(mArchiveSourcePath);
          ((StringBuilder)localObject).append(" ");
          ((StringBuilder)localObject).append(paramXmlResourceParser.getPositionDescription());
          Slog.w("PackageParser", ((StringBuilder)localObject).toString());
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
      }
    }
    return true;
  }
  
  public static ApkLite parseApkLite(File paramFile, int paramInt)
    throws PackageParser.PackageParserException
  {
    return parseApkLiteInner(paramFile, null, null, paramInt);
  }
  
  public static ApkLite parseApkLite(FileDescriptor paramFileDescriptor, String paramString, int paramInt)
    throws PackageParser.PackageParserException
  {
    return parseApkLiteInner(null, paramFileDescriptor, paramString, paramInt);
  }
  
  private static ApkLite parseApkLite(String paramString, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, SigningDetails paramSigningDetails)
    throws IOException, XmlPullParserException, PackageParser.PackageParserException
  {
    Pair localPair = parsePackageSplitNames(paramXmlPullParser, paramAttributeSet);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = true;
    Object localObject1 = null;
    Object localObject2 = null;
    boolean bool5 = false;
    boolean bool6 = false;
    boolean bool7 = false;
    int i = 0;
    int j = 0;
    int k = 0;
    int m = -1;
    int n = 0;
    int i1;
    int i2;
    while (n < paramAttributeSet.getAttributeCount())
    {
      localObject3 = paramAttributeSet.getAttributeName(n);
      int i3;
      int i4;
      if (((String)localObject3).equals("installLocation"))
      {
        i1 = paramAttributeSet.getAttributeIntValue(n, -1);
        i2 = k;
        i3 = j;
        i4 = i;
        bool8 = bool7;
        bool9 = bool6;
        localObject4 = localObject1;
      }
      else if (((String)localObject3).equals("versionCode"))
      {
        i2 = paramAttributeSet.getAttributeIntValue(n, 0);
        i1 = m;
        i3 = j;
        i4 = i;
        bool8 = bool7;
        bool9 = bool6;
        localObject4 = localObject1;
      }
      else if (((String)localObject3).equals("versionCodeMajor"))
      {
        i3 = paramAttributeSet.getAttributeIntValue(n, 0);
        i1 = m;
        i2 = k;
        i4 = i;
        bool8 = bool7;
        bool9 = bool6;
        localObject4 = localObject1;
      }
      else if (((String)localObject3).equals("revisionCode"))
      {
        i4 = paramAttributeSet.getAttributeIntValue(n, 0);
        i1 = m;
        i2 = k;
        i3 = j;
        bool8 = bool7;
        bool9 = bool6;
        localObject4 = localObject1;
      }
      else if (((String)localObject3).equals("coreApp"))
      {
        bool8 = paramAttributeSet.getAttributeBooleanValue(n, false);
        i1 = m;
        i2 = k;
        i3 = j;
        i4 = i;
        bool9 = bool6;
        localObject4 = localObject1;
      }
      else if (((String)localObject3).equals("isolatedSplits"))
      {
        bool9 = paramAttributeSet.getAttributeBooleanValue(n, false);
        i1 = m;
        i2 = k;
        i3 = j;
        i4 = i;
        bool8 = bool7;
        localObject4 = localObject1;
      }
      else if (((String)localObject3).equals("configForSplit"))
      {
        localObject4 = paramAttributeSet.getAttributeValue(n);
        i1 = m;
        i2 = k;
        i3 = j;
        i4 = i;
        bool8 = bool7;
        bool9 = bool6;
      }
      else
      {
        i1 = m;
        i2 = k;
        i3 = j;
        i4 = i;
        bool8 = bool7;
        bool9 = bool6;
        localObject4 = localObject1;
        if (((String)localObject3).equals("isFeatureSplit"))
        {
          bool5 = paramAttributeSet.getAttributeBooleanValue(n, false);
          localObject4 = localObject1;
          bool9 = bool6;
          bool8 = bool7;
          i4 = i;
          i3 = j;
          i2 = k;
          i1 = m;
        }
      }
      n++;
      m = i1;
      k = i2;
      j = i3;
      i = i4;
      bool7 = bool8;
      bool6 = bool9;
      localObject1 = localObject4;
    }
    n = paramXmlPullParser.getDepth() + 1;
    Object localObject3 = new ArrayList();
    boolean bool9 = bool1;
    Object localObject4 = localObject2;
    bool1 = bool4;
    boolean bool8 = bool3;
    for (;;)
    {
      i1 = paramXmlPullParser.next();
      if (i1 == 1) {
        break;
      }
      if ((i1 == 3) && (paramXmlPullParser.getDepth() < n)) {
        break;
      }
      if (i1 != 3)
      {
        if (i1 == 4) {}
        for (;;)
        {
          localObject2 = localObject4;
          break label892;
          if (paramXmlPullParser.getDepth() == n)
          {
            if (!"package-verifier".equals(paramXmlPullParser.getName())) {
              break;
            }
            localObject2 = parseVerifier(paramAttributeSet);
            if (localObject2 != null) {
              ((List)localObject3).add(localObject2);
            }
          }
        }
        if ("application".equals(paramXmlPullParser.getName()))
        {
          for (i2 = 0; i2 < paramAttributeSet.getAttributeCount(); i2++)
          {
            localObject2 = paramAttributeSet.getAttributeName(i2);
            if ("debuggable".equals(localObject2)) {
              bool9 = paramAttributeSet.getAttributeBooleanValue(i2, false);
            }
            if ("multiArch".equals(localObject2)) {
              bool2 = paramAttributeSet.getAttributeBooleanValue(i2, false);
            }
            if ("use32bitAbi".equals(localObject2)) {
              bool8 = paramAttributeSet.getAttributeBooleanValue(i2, false);
            }
            if ("extractNativeLibs".equals(localObject2)) {
              bool1 = paramAttributeSet.getAttributeBooleanValue(i2, true);
            }
          }
          continue;
        }
        localObject2 = localObject4;
        if ("uses-split".equals(paramXmlPullParser.getName())) {
          if (localObject4 != null)
          {
            Slog.w("PackageParser", "Only one <uses-split> permitted. Ignoring others.");
            localObject2 = localObject4;
          }
          else
          {
            localObject2 = paramAttributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
            if (localObject2 == null) {
              throw new PackageParserException(-108, "<uses-split> tag requires 'android:name' attribute");
            }
          }
        }
      }
      else
      {
        localObject2 = localObject4;
      }
      label892:
      localObject4 = localObject2;
    }
    return new ApkLite(paramString, (String)first, (String)second, bool5, localObject1, (String)localObject4, k, j, i, m, (List)localObject3, paramSigningDetails, bool7, bool9, bool2, bool8, bool1, bool6);
  }
  
  /* Error */
  private static ApkLite parseApkLiteInner(File paramFile, FileDescriptor paramFileDescriptor, String paramString, int paramInt)
    throws PackageParser.PackageParserException
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +9 -> 10
    //   4: aload_2
    //   5: astore 4
    //   7: goto +9 -> 16
    //   10: aload_0
    //   11: invokevirtual 594	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   14: astore 4
    //   16: aconst_null
    //   17: astore 5
    //   19: aconst_null
    //   20: astore 6
    //   22: iconst_0
    //   23: istore 7
    //   25: aload_1
    //   26: ifnull +34 -> 60
    //   29: aload 6
    //   31: astore 8
    //   33: aload 5
    //   35: astore 9
    //   37: aload_1
    //   38: aload_2
    //   39: iconst_0
    //   40: iconst_0
    //   41: invokestatic 1722	android/content/res/ApkAssets:loadFromFd	(Ljava/io/FileDescriptor;Ljava/lang/String;ZZ)Landroid/content/res/ApkAssets;
    //   44: astore_1
    //   45: goto +29 -> 74
    //   48: astore_0
    //   49: goto +398 -> 447
    //   52: astore_0
    //   53: goto +272 -> 325
    //   56: astore_0
    //   57: goto +173 -> 230
    //   60: aload 6
    //   62: astore 8
    //   64: aload 5
    //   66: astore 9
    //   68: aload 4
    //   70: invokestatic 1726	android/content/res/ApkAssets:loadFromPath	(Ljava/lang/String;)Landroid/content/res/ApkAssets;
    //   73: astore_1
    //   74: aload 6
    //   76: astore 8
    //   78: aload 5
    //   80: astore 9
    //   82: aload_1
    //   83: ldc 110
    //   85: invokevirtual 1730	android/content/res/ApkAssets:openXml	(Ljava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   88: astore_1
    //   89: iload_3
    //   90: bipush 32
    //   92: iand
    //   93: ifeq +106 -> 199
    //   96: aload_1
    //   97: astore 8
    //   99: aload_1
    //   100: astore 9
    //   102: new 42	android/content/pm/PackageParser$Package
    //   105: astore_2
    //   106: aload_1
    //   107: astore 8
    //   109: aload_1
    //   110: astore 9
    //   112: aload_2
    //   113: aconst_null
    //   114: checkcast 327	java/lang/String
    //   117: invokespecial 1731	android/content/pm/PackageParser$Package:<init>	(Ljava/lang/String;)V
    //   120: iload_3
    //   121: bipush 16
    //   123: iand
    //   124: ifeq +6 -> 130
    //   127: iconst_1
    //   128: istore 7
    //   130: aload_1
    //   131: astore 8
    //   133: aload_1
    //   134: astore 9
    //   136: ldc2_w 647
    //   139: ldc_w 649
    //   142: invokestatic 655	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   145: aload_2
    //   146: aload_0
    //   147: iload 7
    //   149: invokestatic 661	android/content/pm/PackageParser:collectCertificates	(Landroid/content/pm/PackageParser$Package;Ljava/io/File;Z)V
    //   152: aload_1
    //   153: astore 8
    //   155: aload_1
    //   156: astore 9
    //   158: ldc2_w 647
    //   161: invokestatic 673	android/os/Trace:traceEnd	(J)V
    //   164: aload_1
    //   165: astore 8
    //   167: aload_1
    //   168: astore 9
    //   170: aload_2
    //   171: getfield 614	android/content/pm/PackageParser$Package:mSigningDetails	Landroid/content/pm/PackageParser$SigningDetails;
    //   174: astore_0
    //   175: goto +34 -> 209
    //   178: astore_0
    //   179: aload_1
    //   180: astore 8
    //   182: aload_1
    //   183: astore 9
    //   185: ldc2_w 647
    //   188: invokestatic 673	android/os/Trace:traceEnd	(J)V
    //   191: aload_1
    //   192: astore 8
    //   194: aload_1
    //   195: astore 9
    //   197: aload_0
    //   198: athrow
    //   199: aload_1
    //   200: astore 8
    //   202: aload_1
    //   203: astore 9
    //   205: getstatic 617	android/content/pm/PackageParser$SigningDetails:UNKNOWN	Landroid/content/pm/PackageParser$SigningDetails;
    //   208: astore_0
    //   209: aload_1
    //   210: astore 8
    //   212: aload_1
    //   213: astore 9
    //   215: aload 4
    //   217: aload_1
    //   218: aload_1
    //   219: aload_0
    //   220: invokestatic 1733	android/content/pm/PackageParser:parseApkLite	(Ljava/lang/String;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/content/pm/PackageParser$SigningDetails;)Landroid/content/pm/PackageParser$ApkLite;
    //   223: astore_0
    //   224: aload_1
    //   225: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   228: aload_0
    //   229: areturn
    //   230: aload 6
    //   232: astore 8
    //   234: aload 5
    //   236: astore 9
    //   238: new 50	android/content/pm/PackageParser$PackageParserException
    //   241: astore_0
    //   242: aload 6
    //   244: astore 8
    //   246: aload 5
    //   248: astore 9
    //   250: new 436	java/lang/StringBuilder
    //   253: astore_1
    //   254: aload 6
    //   256: astore 8
    //   258: aload 5
    //   260: astore 9
    //   262: aload_1
    //   263: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   266: aload 6
    //   268: astore 8
    //   270: aload 5
    //   272: astore 9
    //   274: aload_1
    //   275: ldc_w 1739
    //   278: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: aload 6
    //   284: astore 8
    //   286: aload 5
    //   288: astore 9
    //   290: aload_1
    //   291: aload 4
    //   293: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   296: pop
    //   297: aload 6
    //   299: astore 8
    //   301: aload 5
    //   303: astore 9
    //   305: aload_0
    //   306: bipush -100
    //   308: aload_1
    //   309: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   312: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   315: aload 6
    //   317: astore 8
    //   319: aload 5
    //   321: astore 9
    //   323: aload_0
    //   324: athrow
    //   325: aload 9
    //   327: astore 8
    //   329: new 436	java/lang/StringBuilder
    //   332: astore_1
    //   333: aload 9
    //   335: astore 8
    //   337: aload_1
    //   338: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   341: aload 9
    //   343: astore 8
    //   345: aload_1
    //   346: ldc_w 1739
    //   349: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   352: pop
    //   353: aload 9
    //   355: astore 8
    //   357: aload_1
    //   358: aload 4
    //   360: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   363: pop
    //   364: aload 9
    //   366: astore 8
    //   368: ldc -78
    //   370: aload_1
    //   371: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   374: aload_0
    //   375: invokestatic 556	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   378: pop
    //   379: aload 9
    //   381: astore 8
    //   383: new 50	android/content/pm/PackageParser$PackageParserException
    //   386: astore_2
    //   387: aload 9
    //   389: astore 8
    //   391: new 436	java/lang/StringBuilder
    //   394: astore_1
    //   395: aload 9
    //   397: astore 8
    //   399: aload_1
    //   400: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   403: aload 9
    //   405: astore 8
    //   407: aload_1
    //   408: ldc_w 1739
    //   411: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   414: pop
    //   415: aload 9
    //   417: astore 8
    //   419: aload_1
    //   420: aload 4
    //   422: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   425: pop
    //   426: aload 9
    //   428: astore 8
    //   430: aload_2
    //   431: bipush -102
    //   433: aload_1
    //   434: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   437: aload_0
    //   438: invokespecial 1742	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   441: aload 9
    //   443: astore 8
    //   445: aload_2
    //   446: athrow
    //   447: aload 8
    //   449: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   452: aload_0
    //   453: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	454	0	paramFile	File
    //   0	454	1	paramFileDescriptor	FileDescriptor
    //   0	454	2	paramString	String
    //   0	454	3	paramInt	int
    //   5	416	4	str	String
    //   17	303	5	localObject1	Object
    //   20	296	6	localObject2	Object
    //   23	125	7	bool	boolean
    //   31	417	8	localObject3	Object
    //   35	407	9	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   37	45	48	finally
    //   68	74	48	finally
    //   82	89	48	finally
    //   102	106	48	finally
    //   112	120	48	finally
    //   136	145	48	finally
    //   158	164	48	finally
    //   170	175	48	finally
    //   185	191	48	finally
    //   197	199	48	finally
    //   205	209	48	finally
    //   215	224	48	finally
    //   238	242	48	finally
    //   250	254	48	finally
    //   262	266	48	finally
    //   274	282	48	finally
    //   290	297	48	finally
    //   305	315	48	finally
    //   323	325	48	finally
    //   329	333	48	finally
    //   337	341	48	finally
    //   345	353	48	finally
    //   357	364	48	finally
    //   368	379	48	finally
    //   383	387	48	finally
    //   391	395	48	finally
    //   399	403	48	finally
    //   407	415	48	finally
    //   419	426	48	finally
    //   430	441	48	finally
    //   445	447	48	finally
    //   37	45	52	org/xmlpull/v1/XmlPullParserException
    //   37	45	52	java/lang/RuntimeException
    //   68	74	52	org/xmlpull/v1/XmlPullParserException
    //   68	74	52	java/lang/RuntimeException
    //   82	89	52	org/xmlpull/v1/XmlPullParserException
    //   82	89	52	java/io/IOException
    //   82	89	52	java/lang/RuntimeException
    //   102	106	52	org/xmlpull/v1/XmlPullParserException
    //   102	106	52	java/io/IOException
    //   102	106	52	java/lang/RuntimeException
    //   112	120	52	org/xmlpull/v1/XmlPullParserException
    //   112	120	52	java/io/IOException
    //   112	120	52	java/lang/RuntimeException
    //   136	145	52	org/xmlpull/v1/XmlPullParserException
    //   136	145	52	java/io/IOException
    //   136	145	52	java/lang/RuntimeException
    //   158	164	52	org/xmlpull/v1/XmlPullParserException
    //   158	164	52	java/io/IOException
    //   158	164	52	java/lang/RuntimeException
    //   170	175	52	org/xmlpull/v1/XmlPullParserException
    //   170	175	52	java/io/IOException
    //   170	175	52	java/lang/RuntimeException
    //   185	191	52	org/xmlpull/v1/XmlPullParserException
    //   185	191	52	java/io/IOException
    //   185	191	52	java/lang/RuntimeException
    //   197	199	52	org/xmlpull/v1/XmlPullParserException
    //   197	199	52	java/io/IOException
    //   197	199	52	java/lang/RuntimeException
    //   205	209	52	org/xmlpull/v1/XmlPullParserException
    //   205	209	52	java/io/IOException
    //   205	209	52	java/lang/RuntimeException
    //   215	224	52	org/xmlpull/v1/XmlPullParserException
    //   215	224	52	java/io/IOException
    //   215	224	52	java/lang/RuntimeException
    //   238	242	52	org/xmlpull/v1/XmlPullParserException
    //   238	242	52	java/io/IOException
    //   238	242	52	java/lang/RuntimeException
    //   250	254	52	org/xmlpull/v1/XmlPullParserException
    //   250	254	52	java/io/IOException
    //   250	254	52	java/lang/RuntimeException
    //   262	266	52	org/xmlpull/v1/XmlPullParserException
    //   262	266	52	java/io/IOException
    //   262	266	52	java/lang/RuntimeException
    //   274	282	52	org/xmlpull/v1/XmlPullParserException
    //   274	282	52	java/io/IOException
    //   274	282	52	java/lang/RuntimeException
    //   290	297	52	org/xmlpull/v1/XmlPullParserException
    //   290	297	52	java/io/IOException
    //   290	297	52	java/lang/RuntimeException
    //   305	315	52	org/xmlpull/v1/XmlPullParserException
    //   305	315	52	java/io/IOException
    //   305	315	52	java/lang/RuntimeException
    //   323	325	52	org/xmlpull/v1/XmlPullParserException
    //   323	325	52	java/io/IOException
    //   323	325	52	java/lang/RuntimeException
    //   37	45	56	java/io/IOException
    //   68	74	56	java/io/IOException
    //   145	152	178	finally
  }
  
  /* Error */
  private Package parseBaseApk(File paramFile, AssetManager paramAssetManager, int paramInt)
    throws PackageParser.PackageParserException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 594	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   4: astore 4
    //   6: aconst_null
    //   7: astore 5
    //   9: aload 4
    //   11: ldc -118
    //   13: invokevirtual 1749	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   16: ifeq +31 -> 47
    //   19: aload 4
    //   21: bipush 47
    //   23: ldc -118
    //   25: invokevirtual 457	java/lang/String:length	()I
    //   28: invokevirtual 1751	java/lang/String:indexOf	(II)I
    //   31: istore 6
    //   33: aload 4
    //   35: ldc -118
    //   37: invokevirtual 457	java/lang/String:length	()I
    //   40: iload 6
    //   42: invokevirtual 1753	java/lang/String:substring	(II)Ljava/lang/String;
    //   45: astore 5
    //   47: aload_0
    //   48: iconst_1
    //   49: putfield 365	android/content/pm/PackageParser:mParseError	I
    //   52: aload_0
    //   53: aload_1
    //   54: invokevirtual 594	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   57: putfield 1476	android/content/pm/PackageParser:mArchiveSourcePath	Ljava/lang/String;
    //   60: aconst_null
    //   61: astore 7
    //   63: aconst_null
    //   64: astore 8
    //   66: aconst_null
    //   67: astore 9
    //   69: aload 9
    //   71: astore_1
    //   72: aload_2
    //   73: aload 4
    //   75: invokevirtual 1757	android/content/res/AssetManager:findCookieForPath	(Ljava/lang/String;)I
    //   78: istore 6
    //   80: iload 6
    //   82: ifeq +190 -> 272
    //   85: aload 9
    //   87: astore_1
    //   88: aload_2
    //   89: iload 6
    //   91: ldc 110
    //   93: invokevirtual 1761	android/content/res/AssetManager:openXmlResourceParser	(ILjava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   96: astore 9
    //   98: aload 9
    //   100: astore_1
    //   101: new 1278	android/content/res/Resources
    //   104: astore 7
    //   106: aload 7
    //   108: aload_2
    //   109: aload_0
    //   110: getfield 370	android/content/pm/PackageParser:mMetrics	Landroid/util/DisplayMetrics;
    //   113: aconst_null
    //   114: invokespecial 1764	android/content/res/Resources:<init>	(Landroid/content/res/AssetManager;Landroid/util/DisplayMetrics;Landroid/content/res/Configuration;)V
    //   117: iconst_1
    //   118: anewarray 327	java/lang/String
    //   121: astore_2
    //   122: aload_0
    //   123: aload 4
    //   125: aload 7
    //   127: aload_1
    //   128: iload_3
    //   129: aload_2
    //   130: invokespecial 1767	android/content/pm/PackageParser:parseBaseApk	(Ljava/lang/String;Landroid/content/res/Resources;Landroid/content/res/XmlResourceParser;I[Ljava/lang/String;)Landroid/content/pm/PackageParser$Package;
    //   133: astore 7
    //   135: aload 7
    //   137: ifnull +39 -> 176
    //   140: aload 7
    //   142: aload 5
    //   144: invokevirtual 1770	android/content/pm/PackageParser$Package:setVolumeUuid	(Ljava/lang/String;)V
    //   147: aload 7
    //   149: aload 5
    //   151: invokevirtual 1773	android/content/pm/PackageParser$Package:setApplicationVolumeUuid	(Ljava/lang/String;)V
    //   154: aload 7
    //   156: aload 4
    //   158: invokevirtual 1776	android/content/pm/PackageParser$Package:setBaseCodePath	(Ljava/lang/String;)V
    //   161: aload 7
    //   163: getstatic 617	android/content/pm/PackageParser$SigningDetails:UNKNOWN	Landroid/content/pm/PackageParser$SigningDetails;
    //   166: invokevirtual 1779	android/content/pm/PackageParser$Package:setSigningDetails	(Landroid/content/pm/PackageParser$SigningDetails;)V
    //   169: aload_1
    //   170: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   173: aload 7
    //   175: areturn
    //   176: new 50	android/content/pm/PackageParser$PackageParserException
    //   179: astore 7
    //   181: aload_0
    //   182: getfield 365	android/content/pm/PackageParser:mParseError	I
    //   185: istore_3
    //   186: new 436	java/lang/StringBuilder
    //   189: astore 5
    //   191: aload 5
    //   193: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   196: aload 5
    //   198: aload 4
    //   200: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload 5
    //   206: ldc_w 1781
    //   209: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: pop
    //   213: aload 5
    //   215: aload_1
    //   216: invokeinterface 1481 1 0
    //   221: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: pop
    //   225: aload 5
    //   227: ldc_w 1783
    //   230: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: pop
    //   234: aload 5
    //   236: aload_2
    //   237: iconst_0
    //   238: aaload
    //   239: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: pop
    //   243: aload 7
    //   245: iload_3
    //   246: aload 5
    //   248: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   251: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   254: aload 7
    //   256: athrow
    //   257: astore_2
    //   258: goto +155 -> 413
    //   261: astore 5
    //   263: aload_1
    //   264: astore_2
    //   265: goto +79 -> 344
    //   268: astore_2
    //   269: goto +142 -> 411
    //   272: aload 9
    //   274: astore_1
    //   275: new 50	android/content/pm/PackageParser$PackageParserException
    //   278: astore 5
    //   280: aload 9
    //   282: astore_1
    //   283: new 436	java/lang/StringBuilder
    //   286: astore_2
    //   287: aload 9
    //   289: astore_1
    //   290: aload_2
    //   291: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   294: aload 9
    //   296: astore_1
    //   297: aload_2
    //   298: ldc_w 1785
    //   301: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   304: pop
    //   305: aload 9
    //   307: astore_1
    //   308: aload_2
    //   309: aload 4
    //   311: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   314: pop
    //   315: aload 9
    //   317: astore_1
    //   318: aload 5
    //   320: bipush -101
    //   322: aload_2
    //   323: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   326: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   329: aload 9
    //   331: astore_1
    //   332: aload 5
    //   334: athrow
    //   335: astore_2
    //   336: goto +77 -> 413
    //   339: astore 5
    //   341: aload 7
    //   343: astore_2
    //   344: aload_2
    //   345: astore_1
    //   346: new 50	android/content/pm/PackageParser$PackageParserException
    //   349: astore 8
    //   351: aload_2
    //   352: astore_1
    //   353: new 436	java/lang/StringBuilder
    //   356: astore 7
    //   358: aload_2
    //   359: astore_1
    //   360: aload 7
    //   362: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   365: aload_2
    //   366: astore_1
    //   367: aload 7
    //   369: ldc_w 1787
    //   372: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   375: pop
    //   376: aload_2
    //   377: astore_1
    //   378: aload 7
    //   380: aload 4
    //   382: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   385: pop
    //   386: aload_2
    //   387: astore_1
    //   388: aload 8
    //   390: bipush -102
    //   392: aload 7
    //   394: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   397: aload 5
    //   399: invokespecial 1742	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   402: aload_2
    //   403: astore_1
    //   404: aload 8
    //   406: athrow
    //   407: astore_2
    //   408: aload 8
    //   410: astore_1
    //   411: aload_2
    //   412: athrow
    //   413: aload_1
    //   414: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   417: aload_2
    //   418: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	419	0	this	PackageParser
    //   0	419	1	paramFile	File
    //   0	419	2	paramAssetManager	AssetManager
    //   0	419	3	paramInt	int
    //   4	377	4	str	String
    //   7	240	5	localObject1	Object
    //   261	1	5	localException1	Exception
    //   278	55	5	localPackageParserException1	PackageParserException
    //   339	59	5	localException2	Exception
    //   31	59	6	i	int
    //   61	332	7	localObject2	Object
    //   64	345	8	localPackageParserException2	PackageParserException
    //   67	263	9	localXmlResourceParser	XmlResourceParser
    // Exception table:
    //   from	to	target	type
    //   101	135	257	finally
    //   140	169	257	finally
    //   176	257	257	finally
    //   101	135	261	java/lang/Exception
    //   140	169	261	java/lang/Exception
    //   176	257	261	java/lang/Exception
    //   101	135	268	android/content/pm/PackageParser$PackageParserException
    //   140	169	268	android/content/pm/PackageParser$PackageParserException
    //   176	257	268	android/content/pm/PackageParser$PackageParserException
    //   72	80	335	finally
    //   88	98	335	finally
    //   275	280	335	finally
    //   283	287	335	finally
    //   290	294	335	finally
    //   297	305	335	finally
    //   308	315	335	finally
    //   318	329	335	finally
    //   332	335	335	finally
    //   346	351	335	finally
    //   353	358	335	finally
    //   360	365	335	finally
    //   367	376	335	finally
    //   378	386	335	finally
    //   388	402	335	finally
    //   404	407	335	finally
    //   411	413	335	finally
    //   72	80	339	java/lang/Exception
    //   88	98	339	java/lang/Exception
    //   275	280	339	java/lang/Exception
    //   283	287	339	java/lang/Exception
    //   290	294	339	java/lang/Exception
    //   297	305	339	java/lang/Exception
    //   308	315	339	java/lang/Exception
    //   318	329	339	java/lang/Exception
    //   332	335	339	java/lang/Exception
    //   72	80	407	android/content/pm/PackageParser$PackageParserException
    //   88	98	407	android/content/pm/PackageParser$PackageParserException
    //   275	280	407	android/content/pm/PackageParser$PackageParserException
    //   283	287	407	android/content/pm/PackageParser$PackageParserException
    //   290	294	407	android/content/pm/PackageParser$PackageParserException
    //   297	305	407	android/content/pm/PackageParser$PackageParserException
    //   308	315	407	android/content/pm/PackageParser$PackageParserException
    //   318	329	407	android/content/pm/PackageParser$PackageParserException
    //   332	335	407	android/content/pm/PackageParser$PackageParserException
  }
  
  private Package parseBaseApk(String paramString, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    try
    {
      Object localObject1 = parsePackageSplitNames(paramXmlResourceParser, paramXmlResourceParser);
      Object localObject2 = (String)first;
      localObject1 = (String)second;
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("Expected base APK, but found split ");
        paramString.append((String)localObject1);
        paramArrayOfString[0] = paramString.toString();
        mParseError = -106;
        return null;
      }
      if (mCallback != null)
      {
        paramString = mCallback.getOverlayPaths((String)localObject2, paramString);
        if ((paramString != null) && (paramString.length > 0))
        {
          int i = paramString.length;
          for (int j = 0; j < i; j++)
          {
            localObject1 = paramString[j];
            paramResources.getAssets().addOverlayPath((String)localObject1);
          }
        }
      }
      localObject2 = new Package((String)localObject2);
      paramString = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifest);
      mVersionCode = paramString.getInteger(1, 0);
      mVersionCodeMajor = paramString.getInteger(11, 0);
      applicationInfo.setVersionCode(((Package)localObject2).getLongVersionCode());
      baseRevisionCode = paramString.getInteger(5, 0);
      mVersionName = paramString.getNonConfigurationString(2, 0);
      if (mVersionName != null) {
        mVersionName = mVersionName.intern();
      }
      coreApp = paramXmlResourceParser.getAttributeBooleanValue(null, "coreApp", false);
      mCompileSdkVersion = paramString.getInteger(9, 0);
      applicationInfo.compileSdkVersion = mCompileSdkVersion;
      mCompileSdkVersionCodename = paramString.getNonConfigurationString(10, 0);
      if (mCompileSdkVersionCodename != null) {
        mCompileSdkVersionCodename = mCompileSdkVersionCodename.intern();
      }
      applicationInfo.compileSdkVersionCodename = mCompileSdkVersionCodename;
      paramString.recycle();
      return parseBaseApkCommon((Package)localObject2, null, paramResources, paramXmlResourceParser, paramInt, paramArrayOfString);
    }
    catch (PackageParserException paramString)
    {
      mParseError = -106;
    }
    return null;
  }
  
  private boolean parseBaseApkChild(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    Object localObject = paramXmlResourceParser.getAttributeValue(null, "package");
    if (validateName((String)localObject, true, false) != null)
    {
      mParseError = -106;
      return false;
    }
    if (((String)localObject).equals(packageName))
    {
      paramResources = new StringBuilder();
      paramResources.append("Child package name cannot be equal to parent package name: ");
      paramResources.append(packageName);
      paramPackage = paramResources.toString();
      Slog.w("PackageParser", paramPackage);
      paramArrayOfString[0] = paramPackage;
      mParseError = -108;
      return false;
    }
    if (paramPackage.hasChildPackage((String)localObject))
    {
      paramPackage = new StringBuilder();
      paramPackage.append("Duplicate child package:");
      paramPackage.append((String)localObject);
      paramPackage = paramPackage.toString();
      Slog.w("PackageParser", paramPackage);
      paramArrayOfString[0] = paramPackage;
      mParseError = -108;
      return false;
    }
    localObject = new Package((String)localObject);
    mVersionCode = mVersionCode;
    baseRevisionCode = baseRevisionCode;
    mVersionName = mVersionName;
    applicationInfo.targetSdkVersion = applicationInfo.targetSdkVersion;
    applicationInfo.minSdkVersion = applicationInfo.minSdkVersion;
    paramResources = parseBaseApkCommon((Package)localObject, CHILD_PACKAGE_TAGS, paramResources, paramXmlResourceParser, paramInt, paramArrayOfString);
    if (paramResources == null) {
      return false;
    }
    if (childPackages == null) {
      childPackages = new ArrayList();
    }
    childPackages.add(paramResources);
    parentPackage = paramPackage;
    return true;
  }
  
  private Package parseBaseApkCommon(Package paramPackage, Set<String> paramSet, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    mParseInstrumentationArgs = null;
    Object localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifest);
    String str1 = ((TypedArray)localObject1).getNonConfigurationString(0, 0);
    int i = 3;
    Object localObject2;
    if ((str1 != null) && (str1.length() > 0))
    {
      localObject2 = validateName(str1, true, false);
      if ((localObject2 != null) && (!"android".equals(packageName)))
      {
        paramPackage = new StringBuilder();
        paramPackage.append("<manifest> specifies bad sharedUserId name \"");
        paramPackage.append(str1);
        paramPackage.append("\": ");
        paramPackage.append((String)localObject2);
        paramArrayOfString[0] = paramPackage.toString();
        mParseError = -107;
        return null;
      }
      mSharedUserId = str1.intern();
      mSharedUserLabel = ((TypedArray)localObject1).getResourceId(3, 0);
    }
    installLocation = ((TypedArray)localObject1).getInteger(4, -1);
    applicationInfo.installLocation = installLocation;
    int j = ((TypedArray)localObject1).getInteger(7, 1);
    applicationInfo.targetSandboxVersion = j;
    if ((paramInt & 0x4) != 0)
    {
      localObject2 = applicationInfo;
      privateFlags |= 0x4;
    }
    if ((paramInt & 0x8) != 0)
    {
      localObject2 = applicationInfo;
      flags |= 0x40000;
    }
    if (((TypedArray)localObject1).getBoolean(6, false))
    {
      localObject2 = applicationInfo;
      privateFlags |= 0x8000;
    }
    int k = paramXmlResourceParser.getDepth();
    int m = 1;
    int n = 1;
    int i1 = 0;
    int i2 = 1;
    int i3 = 1;
    int i4 = 1;
    int i5 = 1;
    for (;;)
    {
      localObject2 = paramSet;
      int i6 = paramXmlResourceParser.next();
      if (i6 == 1) {
        break;
      }
      if ((i6 == i) && (paramXmlResourceParser.getDepth() <= k)) {
        break;
      }
      int i7 = k;
      int i8;
      if (i6 != 3)
      {
        if (i6 == 4)
        {
          i8 = i2;
          i2 = i3;
        }
        else
        {
          Object localObject3 = paramXmlResourceParser.getName();
          if ((localObject2 != null) && (!((Set)localObject2).contains(localObject3)))
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Skipping unsupported element under <manifest>: ");
            ((StringBuilder)localObject2).append((String)localObject3);
            ((StringBuilder)localObject2).append(" at ");
            ((StringBuilder)localObject2).append(mArchiveSourcePath);
            ((StringBuilder)localObject2).append(" ");
            ((StringBuilder)localObject2).append(paramXmlResourceParser.getPositionDescription());
            Slog.w("PackageParser", ((StringBuilder)localObject2).toString());
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
          for (;;)
          {
            i8 = i2;
            i2 = i3;
            break;
            i8 = i2;
            k = i3;
            if (!((String)localObject3).equals("application")) {
              break label586;
            }
            if (i1 == 0) {
              break label527;
            }
            Slog.w("PackageParser", "<manifest> has more than one <application>");
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
          label527:
          if (!parseBaseApplication(paramPackage, paramResources, paramXmlResourceParser, paramInt, paramArrayOfString)) {
            return null;
          }
          i1 = 1;
          label567:
          label586:
          label820:
          for (;;)
          {
            i6 = i5;
            k = i2;
            i5 = n;
            i = m;
            i8 = i3;
            m = i8;
            i3 = i6;
            n = i;
            i2 = i5;
            break label2734;
            i6 = i5;
            i = i4;
            if (!((String)localObject3).equals("overlay")) {
              break label838;
            }
            localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestResourceOverlay);
            mOverlayTarget = ((TypedArray)localObject1).getString(1);
            mOverlayCategory = ((TypedArray)localObject1).getString(2);
            mOverlayPriority = ((TypedArray)localObject1).getInt(0, 0);
            mOverlayIsStatic = ((TypedArray)localObject1).getBoolean(3, false);
            localObject3 = ((TypedArray)localObject1).getString(4);
            localObject2 = ((TypedArray)localObject1).getString(5);
            ((TypedArray)localObject1).recycle();
            if (mOverlayTarget == null)
            {
              paramArrayOfString[0] = "<overlay> does not specify a target package";
              mParseError = -108;
              return null;
            }
            if ((mOverlayPriority < 0) || (mOverlayPriority > 9999)) {
              break;
            }
            if (!checkOverlayRequiredSystemProperty((String)localObject3, (String)localObject2))
            {
              paramSet = new StringBuilder();
              paramSet.append("Skipping target and overlay pair ");
              paramSet.append(mOverlayTarget);
              paramSet.append(" and ");
              paramSet.append(baseCodePath);
              paramSet.append(": overlay ignored due to required system property: ");
              paramSet.append((String)localObject3);
              paramSet.append(" with value: ");
              paramSet.append((String)localObject2);
              Slog.i("PackageParser", paramSet.toString());
              return null;
            }
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
          paramArrayOfString[0] = "<overlay> priority must be between 0 and 9999";
          mParseError = -108;
          return null;
          label838:
          if (((String)localObject3).equals("key-sets")) {
            if (!parseKeySets(paramPackage, paramResources, paramXmlResourceParser, paramArrayOfString)) {
              return null;
            }
          }
          for (;;)
          {
            label863:
            break;
            if (((String)localObject3).equals("permission-group"))
            {
              if (!parsePermissionGroup(paramPackage, paramInt, paramResources, paramXmlResourceParser, paramArrayOfString)) {
                return null;
              }
            }
            else if (((String)localObject3).equals("permission"))
            {
              if (!parsePermission(paramPackage, paramResources, paramXmlResourceParser, paramArrayOfString)) {
                return null;
              }
            }
            else if (((String)localObject3).equals("permission-tree"))
            {
              if (!parsePermissionTree(paramPackage, paramResources, paramXmlResourceParser, paramArrayOfString)) {
                return null;
              }
            }
            else if (((String)localObject3).equals("uses-permission"))
            {
              if (!parseUsesPermission(paramPackage, paramResources, paramXmlResourceParser)) {
                return null;
              }
            }
            else if ((!((String)localObject3).equals("uses-permission-sdk-m")) && (!((String)localObject3).equals("uses-permission-sdk-23")))
            {
              if (((String)localObject3).equals("uses-configuration"))
              {
                localObject2 = new ConfigurationInfo();
                localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestUsesConfiguration);
                reqTouchScreen = ((TypedArray)localObject1).getInt(0, 0);
                reqKeyboardType = ((TypedArray)localObject1).getInt(1, 0);
                if (((TypedArray)localObject1).getBoolean(2, false)) {
                  reqInputFeatures |= 0x1;
                }
                reqNavigation = ((TypedArray)localObject1).getInt(3, 0);
                if (((TypedArray)localObject1).getBoolean(4, false)) {
                  reqInputFeatures = (0x2 | reqInputFeatures);
                }
                ((TypedArray)localObject1).recycle();
                configPreferences = ArrayUtils.add(configPreferences, localObject2);
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
                i3 = i;
                i4 = k;
                i2 = i8;
              }
              for (k = i6;; k = i5)
              {
                i8 = i4;
                i6 = k;
                i4 = i3;
                i = m;
                i5 = n;
                k = i2;
                break label567;
                if (((String)localObject3).equals("uses-feature"))
                {
                  localObject2 = parseUsesFeature(paramResources, paramXmlResourceParser);
                  reqFeatures = ArrayUtils.add(reqFeatures, localObject2);
                  if (name == null)
                  {
                    localObject3 = new ConfigurationInfo();
                    reqGlEsVersion = reqGlEsVersion;
                    configPreferences = ArrayUtils.add(configPreferences, localObject3);
                  }
                  if ((Build.FEATURES.ENABLE_APP_SCALING) && ("android.software.vr.mode".equals(name))) {
                    applicationInfo.actualMaxAspect = 0.0F;
                  }
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                  label1273:
                  break label863;
                }
                Object localObject4;
                if (((String)localObject3).equals("feature-group"))
                {
                  localObject3 = new FeatureGroupInfo();
                  localObject2 = null;
                  k = paramXmlResourceParser.getDepth();
                  for (;;)
                  {
                    i = paramXmlResourceParser.next();
                    if (i == 1) {
                      break;
                    }
                    if ((i == 3) && (paramXmlResourceParser.getDepth() <= k)) {
                      break;
                    }
                    if ((i != 3) && (i != 4))
                    {
                      String str2 = paramXmlResourceParser.getName();
                      if (str2.equals("uses-feature"))
                      {
                        localObject4 = parseUsesFeature(paramResources, paramXmlResourceParser);
                        flags |= 0x1;
                        localObject2 = ArrayUtils.add((ArrayList)localObject2, localObject4);
                      }
                      else
                      {
                        localObject4 = new StringBuilder();
                        ((StringBuilder)localObject4).append("Unknown element under <feature-group>: ");
                        ((StringBuilder)localObject4).append(str2);
                        ((StringBuilder)localObject4).append(" at ");
                        ((StringBuilder)localObject4).append(mArchiveSourcePath);
                        ((StringBuilder)localObject4).append(" ");
                        ((StringBuilder)localObject4).append(paramXmlResourceParser.getPositionDescription());
                        Slog.w("PackageParser", ((StringBuilder)localObject4).toString());
                      }
                      XmlUtils.skipCurrentTag(paramXmlResourceParser);
                    }
                  }
                  if (localObject2 != null)
                  {
                    features = new FeatureInfo[((ArrayList)localObject2).size()];
                    features = ((FeatureInfo[])((ArrayList)localObject2).toArray(features));
                  }
                  featureGroups = ArrayUtils.add(featureGroups, localObject3);
                  break label820;
                }
                if (((String)localObject3).equals("uses-sdk"))
                {
                  if (SDK_VERSION > 0)
                  {
                    localObject4 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestUsesSdk);
                    localObject1 = null;
                    i3 = 0;
                    localObject2 = null;
                    localObject3 = ((TypedArray)localObject4).peekValue(0);
                    if (localObject3 != null)
                    {
                      if ((type == 3) && (string != null))
                      {
                        localObject2 = string.toString();
                        localObject1 = localObject2;
                        i = 1;
                      }
                      else
                      {
                        i3 = data;
                        i = i3;
                      }
                    }
                    else {
                      i = 1;
                    }
                    localObject3 = ((TypedArray)localObject4).peekValue(1);
                    if (localObject3 != null) {
                      if ((type == 3) && (string != null))
                      {
                        localObject3 = string.toString();
                        localObject2 = localObject1;
                        if (localObject1 == null) {
                          localObject2 = localObject3;
                        }
                        localObject1 = localObject2;
                        localObject2 = localObject3;
                      }
                      else
                      {
                        i3 = data;
                      }
                    }
                    ((TypedArray)localObject4).recycle();
                    i6 = SDK_VERSION;
                    localObject3 = localObject4;
                    i = computeMinSdkVersion(i, (String)localObject1, i6, SDK_CODENAMES, paramArrayOfString);
                    if (i < 0)
                    {
                      mParseError = -12;
                      return null;
                    }
                    boolean bool;
                    if ((paramInt & 0x80) != 0) {
                      bool = true;
                    } else {
                      bool = false;
                    }
                    i3 = computeTargetSdkVersion(i3, (String)localObject2, SDK_CODENAMES, paramArrayOfString, bool);
                    if (i3 < 0)
                    {
                      mParseError = -12;
                      return null;
                    }
                    applicationInfo.minSdkVersion = i;
                    applicationInfo.targetSdkVersion = i3;
                    localObject1 = localObject3;
                  }
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                  i3 = k;
                  break;
                }
                if (!((String)localObject3).equals("supports-screens")) {
                  break label2006;
                }
                localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestSupportsScreens);
                applicationInfo.requiresSmallestWidthDp = ((TypedArray)localObject1).getInteger(6, 0);
                applicationInfo.compatibleWidthLimitDp = ((TypedArray)localObject1).getInteger(7, 0);
                applicationInfo.largestWidthLimitDp = ((TypedArray)localObject1).getInteger(8, 0);
                i3 = ((TypedArray)localObject1).getInteger(1, i);
                i5 = ((TypedArray)localObject1).getInteger(2, i6);
                i4 = ((TypedArray)localObject1).getInteger(3, k);
                i2 = ((TypedArray)localObject1).getInteger(5, i8);
                m = ((TypedArray)localObject1).getInteger(4, m);
                n = ((TypedArray)localObject1).getInteger(0, n);
                ((TypedArray)localObject1).recycle();
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
              }
              label2006:
              int i9 = m;
              if (((String)localObject3).equals("protected-broadcast"))
              {
                localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestProtectedBroadcast);
                localObject2 = ((TypedArray)localObject1).getNonResourceString(0);
                ((TypedArray)localObject1).recycle();
                if (localObject2 != null)
                {
                  if (protectedBroadcasts == null) {
                    protectedBroadcasts = new ArrayList();
                  }
                  if (!protectedBroadcasts.contains(localObject2)) {
                    protectedBroadcasts.add(((String)localObject2).intern());
                  }
                }
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
              }
              for (;;)
              {
                i4 = k;
                k = i6;
                i3 = i;
                i2 = i8;
                m = i9;
                break;
                if (((String)localObject3).equals("instrumentation"))
                {
                  if (parseInstrumentation(paramPackage, paramResources, paramXmlResourceParser, paramArrayOfString) == null) {
                    return null;
                  }
                  break label1273;
                }
                if (((String)localObject3).equals("original-package"))
                {
                  localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestOriginalPackage);
                  localObject2 = ((TypedArray)localObject1).getNonConfigurationString(0, 0);
                  if (!packageName.equals(localObject2))
                  {
                    if (mOriginalPackages == null)
                    {
                      mOriginalPackages = new ArrayList();
                      mRealPackage = packageName;
                    }
                    mOriginalPackages.add(localObject2);
                  }
                  ((TypedArray)localObject1).recycle();
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                }
                else
                {
                  if (!((String)localObject3).equals("adopt-permissions")) {
                    break label2315;
                  }
                  localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestOriginalPackage);
                  localObject2 = ((TypedArray)localObject1).getNonConfigurationString(0, 0);
                  ((TypedArray)localObject1).recycle();
                  if (localObject2 != null)
                  {
                    if (mAdoptPermissions == null) {
                      mAdoptPermissions = new ArrayList();
                    }
                    mAdoptPermissions.add(localObject2);
                  }
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                }
              }
              label2315:
              if (((String)localObject3).equals("uses-gl-texture")) {
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
              }
              for (;;)
              {
                i2 = k;
                break label2772;
                if (((String)localObject3).equals("compatible-screens"))
                {
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                }
                else if (((String)localObject3).equals("supports-input"))
                {
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                }
                else if (((String)localObject3).equals("eat-comment"))
                {
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                }
                else
                {
                  if (!((String)localObject3).equals("package")) {
                    break label2432;
                  }
                  if (MULTI_PACKAGE_APK_ENABLED) {
                    break;
                  }
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                }
              }
              if (!parseBaseApkChild(paramPackage, paramResources, paramXmlResourceParser, paramInt, paramArrayOfString))
              {
                return null;
                label2432:
                i2 = k;
                if (((String)localObject3).equals("restrict-update"))
                {
                  if ((paramInt & 0x10) != 0)
                  {
                    localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestRestrictUpdate);
                    localObject3 = ((TypedArray)localObject1).getNonConfigurationString(0, 0);
                    ((TypedArray)localObject1).recycle();
                    restrictUpdateHash = null;
                    if (localObject3 != null)
                    {
                      i3 = ((String)localObject3).length();
                      localObject2 = new byte[i3 / 2];
                      for (m = 0; m < i3; m += 2) {
                        localObject2[(m / 2)] = ((byte)(byte)((Character.digit(((String)localObject3).charAt(m), 16) << 4) + Character.digit(((String)localObject3).charAt(m + 1), 16)));
                      }
                      restrictUpdateHash = ((byte[])localObject2);
                    }
                    else {}
                  }
                  XmlUtils.skipCurrentTag(paramXmlResourceParser);
                  i3 = i6;
                  i4 = i;
                  k = i8;
                  m = i2;
                  i2 = n;
                  n = i9;
                  break label2734;
                }
                localObject2 = new StringBuilder();
                ((StringBuilder)localObject2).append("Unknown element under <manifest>: ");
                ((StringBuilder)localObject2).append(paramXmlResourceParser.getName());
                ((StringBuilder)localObject2).append(" at ");
                ((StringBuilder)localObject2).append(mArchiveSourcePath);
                ((StringBuilder)localObject2).append(" ");
                ((StringBuilder)localObject2).append(paramXmlResourceParser.getPositionDescription());
                Slog.w("PackageParser", ((StringBuilder)localObject2).toString());
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
                break label2772;
              }
            }
            else if (!parseUsesPermission(paramPackage, paramResources, paramXmlResourceParser))
            {
              return null;
            }
          }
          i4 = i;
          i5 = i3;
          k = i2;
          i2 = n;
          n = m;
          i3 = i6;
          m = i5;
          label2734:
          i = 3;
          i8 = k;
          i6 = m;
          i5 = i3;
          k = i7;
          m = n;
          n = i2;
          break label2783;
        }
      }
      else
      {
        i8 = i2;
        i2 = i3;
      }
      label2772:
      k = i7;
      i = 3;
      i6 = i2;
      label2783:
      i2 = i8;
      i3 = i6;
    }
    k = n;
    if ((i1 == 0) && (instrumentation.size() == 0))
    {
      paramArrayOfString[0] = "<manifest> does not contain an <application> or <instrumentation>";
      mParseError = -109;
    }
    n = NEW_PERMISSIONS.length;
    paramSet = null;
    paramInt = 0;
    while (paramInt < n)
    {
      paramXmlResourceParser = NEW_PERMISSIONS[paramInt];
      if (applicationInfo.targetSdkVersion >= sdkVersion) {
        break;
      }
      paramResources = paramSet;
      if (!requestedPermissions.contains(name))
      {
        if (paramSet == null)
        {
          paramSet = new StringBuilder(128);
          paramSet.append(packageName);
          paramSet.append(": compat added ");
        }
        else
        {
          paramSet.append(' ');
        }
        paramSet.append(name);
        requestedPermissions.add(name);
        paramResources = paramSet;
      }
      paramInt++;
      paramSet = paramResources;
    }
    if (paramSet != null) {
      Slog.i("PackageParser", paramSet.toString());
    }
    i1 = SPLIT_PERMISSIONS.length;
    for (paramInt = 0; paramInt < i1; paramInt++)
    {
      paramSet = SPLIT_PERMISSIONS[paramInt];
      if ((applicationInfo.targetSdkVersion < targetSdk) && (requestedPermissions.contains(rootPerm))) {
        for (n = 0; n < newPerms.length; n++)
        {
          paramResources = newPerms[n];
          if (!requestedPermissions.contains(paramResources)) {
            requestedPermissions.add(paramResources);
          }
        }
      }
    }
    if ((i4 < 0) || ((i4 > 0) && (applicationInfo.targetSdkVersion >= 4)))
    {
      paramSet = applicationInfo;
      flags |= 0x200;
    }
    if (i5 != 0)
    {
      paramSet = applicationInfo;
      flags |= 0x400;
    }
    if ((i3 < 0) || ((i3 > 0) && (applicationInfo.targetSdkVersion >= 4)))
    {
      paramSet = applicationInfo;
      flags |= 0x800;
    }
    if ((i2 < 0) || ((i2 > 0) && (applicationInfo.targetSdkVersion >= 9)))
    {
      paramSet = applicationInfo;
      flags |= 0x80000;
    }
    if ((m < 0) || ((m > 0) && (applicationInfo.targetSdkVersion >= 4)))
    {
      paramSet = applicationInfo;
      flags |= 0x1000;
    }
    if ((k < 0) || ((k > 0) && (applicationInfo.targetSdkVersion >= 4)))
    {
      paramSet = applicationInfo;
      flags |= 0x2000;
    }
    if (applicationInfo.usesCompatibilityMode()) {
      adjustPackageToBeUnresizeableAndUnpipable(paramPackage);
    }
    return paramPackage;
  }
  
  private boolean parseBaseApplication(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = this;
    Package localPackage = paramPackage;
    Object localObject2 = paramXmlResourceParser;
    ApplicationInfo localApplicationInfo = applicationInfo;
    Object localObject3 = applicationInfo.packageName;
    Object localObject4 = paramResources.obtainAttributes((AttributeSet)localObject2, R.styleable.AndroidManifestApplication);
    Object localObject5 = paramArrayOfString;
    if (!parsePackageItemInfo(localPackage, localApplicationInfo, paramArrayOfString, "<application>", (TypedArray)localObject4, false, 3, 1, 2, 42, 22, 30))
    {
      ((TypedArray)localObject4).recycle();
      mParseError = -108;
      return false;
    }
    if (name != null) {
      className = name;
    }
    Object localObject6 = ((TypedArray)localObject4).getNonConfigurationString(4, 1024);
    if (localObject6 != null) {
      manageSpaceActivityName = buildClassName((String)localObject3, (CharSequence)localObject6, (String[])localObject5);
    }
    if (((TypedArray)localObject4).getBoolean(17, true))
    {
      flags |= 0x8000;
      localObject6 = ((TypedArray)localObject4).getNonConfigurationString(16, 1024);
      if (localObject6 != null)
      {
        backupAgentName = buildClassName((String)localObject3, (CharSequence)localObject6, (String[])localObject5);
        if (((TypedArray)localObject4).getBoolean(18, true)) {
          flags |= 0x10000;
        }
        if (((TypedArray)localObject4).getBoolean(21, false)) {
          flags |= 0x20000;
        }
        if (((TypedArray)localObject4).getBoolean(32, false)) {
          flags |= 0x4000000;
        }
        if (((TypedArray)localObject4).getBoolean(40, false)) {
          privateFlags |= 0x2000;
        }
      }
      localObject6 = ((TypedArray)localObject4).peekValue(35);
      if (localObject6 != null)
      {
        i = resourceId;
        fullBackupContent = i;
        if (i == 0)
        {
          if (data == 0) {
            i = -1;
          } else {
            i = 0;
          }
          fullBackupContent = i;
        }
      }
    }
    theme = ((TypedArray)localObject4).getResourceId(0, 0);
    descriptionRes = ((TypedArray)localObject4).getResourceId(13, 0);
    if (((TypedArray)localObject4).getBoolean(8, false))
    {
      localObject6 = ((TypedArray)localObject4).getNonResourceString(45);
      if ((localObject6 == null) || (mCallback.hasFeature((String)localObject6))) {
        flags |= 0x8;
      }
    }
    if (((TypedArray)localObject4).getBoolean(27, false)) {
      mRequiredForAllUsers = true;
    }
    String str1 = ((TypedArray)localObject4).getString(28);
    if ((str1 != null) && (str1.length() > 0)) {
      mRestrictedAccountType = str1;
    }
    localObject6 = ((TypedArray)localObject4).getString(29);
    if ((localObject6 != null) && (((String)localObject6).length() > 0)) {
      mRequiredAccountType = ((String)localObject6);
    }
    if (((TypedArray)localObject4).getBoolean(10, false)) {
      flags |= 0x2;
    }
    if (((TypedArray)localObject4).getBoolean(20, false)) {
      flags |= 0x4000;
    }
    if (applicationInfo.targetSdkVersion >= 14) {
      bool = true;
    } else {
      bool = false;
    }
    baseHardwareAccelerated = ((TypedArray)localObject4).getBoolean(23, bool);
    if (baseHardwareAccelerated) {
      flags |= 0x20000000;
    }
    if (((TypedArray)localObject4).getBoolean(7, true)) {
      flags |= 0x4;
    }
    if (((TypedArray)localObject4).getBoolean(14, false)) {
      flags |= 0x20;
    }
    if (((TypedArray)localObject4).getBoolean(5, true)) {
      flags |= 0x40;
    }
    if ((parentPackage == null) && (((TypedArray)localObject4).getBoolean(15, false))) {
      flags |= 0x100;
    }
    if (((TypedArray)localObject4).getBoolean(24, false)) {
      flags |= 0x100000;
    }
    if (applicationInfo.targetSdkVersion < 28) {
      bool = true;
    } else {
      bool = false;
    }
    if (((TypedArray)localObject4).getBoolean(36, bool)) {
      flags |= 0x8000000;
    }
    if (((TypedArray)localObject4).getBoolean(26, false)) {
      flags |= 0x400000;
    }
    if (((TypedArray)localObject4).getBoolean(33, false)) {
      flags |= 0x80000000;
    }
    if (((TypedArray)localObject4).getBoolean(34, true)) {
      flags |= 0x10000000;
    }
    if (((TypedArray)localObject4).getBoolean(38, false)) {
      privateFlags |= 0x20;
    }
    if (((TypedArray)localObject4).getBoolean(39, false)) {
      privateFlags |= 0x40;
    }
    if (((TypedArray)localObject4).hasValueOrEmpty(37))
    {
      if (((TypedArray)localObject4).getBoolean(37, true)) {
        privateFlags |= 0x400;
      } else {
        privateFlags |= 0x800;
      }
    }
    else if (applicationInfo.targetSdkVersion >= 24) {
      privateFlags |= 0x1000;
    }
    maxAspectRatio = ((TypedArray)localObject4).getFloat(44, 0.0F);
    networkSecurityConfigRes = ((TypedArray)localObject4).getResourceId(41, 0);
    category = ((TypedArray)localObject4).getInt(43, -1);
    localObject6 = ((TypedArray)localObject4).getNonConfigurationString(6, 0);
    if ((localObject6 != null) && (((String)localObject6).length() > 0)) {
      localObject6 = ((String)localObject6).intern();
    } else {
      localObject6 = null;
    }
    permission = ((String)localObject6);
    if (applicationInfo.targetSdkVersion >= 8) {}
    for (localObject6 = ((TypedArray)localObject4).getNonConfigurationString(12, 1024);; localObject6 = ((TypedArray)localObject4).getNonResourceString(12)) {
      break;
    }
    taskAffinity = buildTaskAffinityName(packageName, packageName, (CharSequence)localObject6, (String[])localObject5);
    localObject6 = ((TypedArray)localObject4).getNonResourceString(48);
    if (localObject6 != null) {
      appComponentFactory = buildClassName(packageName, (CharSequence)localObject6, (String[])localObject5);
    }
    if (localObject5[0] == null)
    {
      if (applicationInfo.targetSdkVersion >= 8) {
        localObject6 = ((TypedArray)localObject4).getNonConfigurationString(11, 1024);
      } else {
        localObject6 = ((TypedArray)localObject4).getNonResourceString(11);
      }
      processName = buildProcessName(packageName, null, (CharSequence)localObject6, paramInt, mSeparateProcesses, (String[])localObject5);
      enabled = ((TypedArray)localObject4).getBoolean(9, true);
      if (((TypedArray)localObject4).getBoolean(31, false)) {
        flags |= 0x2000000;
      }
      if (((TypedArray)localObject4).getBoolean(47, false))
      {
        privateFlags |= 0x2;
        if ((processName != null) && (!processName.equals(packageName))) {
          localObject5[0] = "cantSaveState applications can not use custom processes";
        }
      }
      else {}
    }
    uiOptions = ((TypedArray)localObject4).getInt(25, 0);
    classLoaderName = ((TypedArray)localObject4).getString(46);
    if ((classLoaderName != null) && (!ClassLoaderFactory.isValidClassLoaderName(classLoaderName)))
    {
      localObject6 = new StringBuilder();
      ((StringBuilder)localObject6).append("Invalid class loader name: ");
      ((StringBuilder)localObject6).append(classLoaderName);
      localObject5[0] = ((StringBuilder)localObject6).toString();
    }
    ((TypedArray)localObject4).recycle();
    if (localObject5[0] != null)
    {
      mParseError = -108;
      return false;
    }
    int j = paramXmlResourceParser.getDepth();
    CachedComponentArgs localCachedComponentArgs = new CachedComponentArgs(null);
    int i = 0;
    int k = 0;
    int m = 0;
    for (;;)
    {
      int n = paramXmlResourceParser.next();
      if (n == 1) {
        break;
      }
      if ((n == 3) && (paramXmlResourceParser.getDepth() <= j)) {
        break;
      }
      if (n != 3)
      {
        if (n == 4)
        {
          localObject6 = localObject3;
          localObject3 = localObject2;
          localObject2 = localObject5;
          localObject5 = localObject6;
        }
        else
        {
          String str2 = paramXmlResourceParser.getName();
          if (str2.equals("activity"))
          {
            localObject6 = ((PackageParser)localObject1).parseActivity(localPackage, paramResources, (XmlResourceParser)localObject2, paramInt, (String[])localObject5, localCachedComponentArgs, false, baseHardwareAccelerated);
            if (localObject6 == null)
            {
              mParseError = -108;
              return false;
            }
            if (order != 0) {
              n = 1;
            } else {
              n = 0;
            }
            activities.add(localObject6);
            i |= n;
          }
          Object localObject7;
          label1974:
          do
          {
            localObject6 = localObject3;
            localObject3 = localObject2;
            localObject2 = localObject5;
            localObject5 = localObject6;
            break label2555;
            localObject4 = localObject3;
            if (str2.equals("receiver"))
            {
              localObject3 = ((PackageParser)localObject1).parseActivity(localPackage, paramResources, (XmlResourceParser)localObject2, paramInt, (String[])localObject5, localCachedComponentArgs, true, false);
              if (localObject3 == null)
              {
                mParseError = -108;
                return false;
              }
              if (order != 0) {
                n = 1;
              } else {
                n = 0;
              }
              localPackage = paramPackage;
              receivers.add(localObject3);
              k |= n;
            }
            for (;;)
            {
              localObject2 = paramArrayOfString;
              localObject3 = paramXmlResourceParser;
              localObject5 = localObject4;
              break label2555;
              localObject5 = localApplicationInfo;
              localObject7 = localObject1;
              if (str2.equals("service"))
              {
                localObject3 = localObject7.parseService(localPackage, paramResources, paramXmlResourceParser, paramInt, paramArrayOfString, localCachedComponentArgs);
                if (localObject3 == null)
                {
                  mParseError = -108;
                  return false;
                }
                if (order != 0) {
                  n = 1;
                } else {
                  n = 0;
                }
                services.add(localObject3);
                m |= n;
              }
              else
              {
                if (str2.equals("provider"))
                {
                  localObject2 = localObject7.parseProvider(localPackage, paramResources, paramXmlResourceParser, paramInt, paramArrayOfString, localCachedComponentArgs);
                  if (localObject2 == null)
                  {
                    mParseError = -108;
                    return false;
                  }
                  providers.add(localObject2);
                  localObject2 = paramXmlResourceParser;
                  localObject5 = paramArrayOfString;
                  break;
                }
                if (!str2.equals("activity-alias")) {
                  break label1974;
                }
                localObject3 = localObject7.parseActivityAlias(localPackage, paramResources, paramXmlResourceParser, paramInt, paramArrayOfString, localCachedComponentArgs);
                if (localObject3 == null)
                {
                  mParseError = -108;
                  return false;
                }
                if (order != 0) {
                  n = 1;
                } else {
                  n = 0;
                }
                activities.add(localObject3);
                i |= n;
              }
            }
            if (!paramXmlResourceParser.getName().equals("meta-data")) {
              break;
            }
            localObject6 = mAppMetaData;
            localObject2 = paramXmlResourceParser;
            localObject5 = paramArrayOfString;
            localObject6 = localObject7.parseMetaData(paramResources, (XmlResourceParser)localObject2, (Bundle)localObject6, (String[])localObject5);
            mAppMetaData = ((Bundle)localObject6);
          } while (localObject6 != null);
          mParseError = -108;
          return false;
          localObject6 = paramXmlResourceParser;
          localObject2 = paramArrayOfString;
          if (str2.equals("static-library"))
          {
            TypedArray localTypedArray = paramResources.obtainAttributes((AttributeSet)localObject6, R.styleable.AndroidManifestStaticLibrary);
            str2 = localTypedArray.getNonResourceString(0);
            n = localTypedArray.getInt(1, -1);
            int i1 = localTypedArray.getInt(2, 0);
            localTypedArray.recycle();
            if ((str2 != null) && (n >= 0))
            {
              if (mSharedUserId != null)
              {
                localObject2[0] = "sharedUserId not allowed in static shared library";
                mParseError = -107;
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
                return false;
              }
              if (staticSharedLibName != null)
              {
                paramPackage = new StringBuilder();
                paramPackage.append("Multiple static-shared libs for package ");
                paramPackage.append((String)localObject4);
                localObject2[0] = paramPackage.toString();
                mParseError = -108;
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
                return false;
              }
              staticSharedLibName = str2.intern();
              if (n >= 0) {
                staticSharedLibVersion = PackageInfo.composeLongVersionCode(i1, n);
              } else {
                staticSharedLibVersion = n;
              }
              privateFlags |= 0x4000;
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
            }
            else
            {
              paramPackage = new StringBuilder();
              paramPackage.append("Bad static-library declaration name: ");
              paramPackage.append(str2);
              paramPackage.append(" version: ");
              paramPackage.append(n);
              localObject2[0] = paramPackage.toString();
              mParseError = -108;
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
              return false;
            }
          }
          else
          {
            localObject5 = localObject4;
            if (!str2.equals("library")) {
              break label2403;
            }
            localObject4 = paramResources.obtainAttributes((AttributeSet)localObject6, R.styleable.AndroidManifestLibrary);
            localObject5 = ((TypedArray)localObject4).getNonResourceString(0);
            ((TypedArray)localObject4).recycle();
            if (localObject5 != null)
            {
              localObject5 = ((String)localObject5).intern();
              if (!ArrayUtils.contains(libraryNames, localObject5)) {
                libraryNames = ArrayUtils.add(libraryNames, localObject5);
              }
            }
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
          label2403:
          do
          {
            localObject5 = localObject3;
            localObject3 = localObject6;
            break label2555;
            if (!str2.equals("uses-static-library")) {
              break;
            }
          } while (localObject7.parseUsesStaticLibrary(localPackage, paramResources, (XmlResourceParser)localObject6, (String[])localObject2));
          return false;
          if (str2.equals("uses-library"))
          {
            localObject3 = paramResources.obtainAttributes((AttributeSet)localObject6, R.styleable.AndroidManifestUsesLibrary);
            localObject4 = ((TypedArray)localObject3).getNonResourceString(0);
            bool = ((TypedArray)localObject3).getBoolean(1, true);
            ((TypedArray)localObject3).recycle();
            if (localObject4 != null)
            {
              localObject3 = ((String)localObject4).intern();
              if (bool) {
                usesLibraries = ArrayUtils.add(usesLibraries, localObject3);
              } else {
                usesOptionalLibraries = ArrayUtils.add(usesOptionalLibraries, localObject3);
              }
            }
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
            localObject3 = localObject6;
          }
          else
          {
            if (!str2.equals("uses-package")) {
              break label2558;
            }
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
            localObject3 = localObject6;
          }
          label2555:
          break label2663;
          label2558:
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("Unknown element under <application>: ");
          ((StringBuilder)localObject3).append(str2);
          ((StringBuilder)localObject3).append(" at ");
          ((StringBuilder)localObject3).append(mArchiveSourcePath);
          ((StringBuilder)localObject3).append(" ");
          ((StringBuilder)localObject3).append(paramXmlResourceParser.getPositionDescription());
          Slog.w("PackageParser", ((StringBuilder)localObject3).toString());
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
          localObject3 = localObject6;
        }
      }
      else
      {
        localObject6 = localObject5;
        localObject5 = localObject3;
        localObject3 = localObject2;
        localObject2 = localObject6;
      }
      label2663:
      localObject4 = localObject1;
      localObject6 = localObject3;
      localObject1 = localObject2;
      localObject3 = localObject5;
      localObject2 = localObject6;
      localObject5 = localObject1;
      localObject1 = localObject4;
    }
    boolean bool = false;
    if (i != 0) {
      Collections.sort(activities, _..Lambda.PackageParser.0aobsT7Zf7WVZCqMZ5z2clAuQf4.INSTANCE);
    }
    if (k != 0) {
      Collections.sort(receivers, _..Lambda.PackageParser.0DZRgzfgaIMpCOhJqjw6PUiU5vw.INSTANCE);
    }
    if (m != 0) {
      Collections.sort(services, _..Lambda.PackageParser.M_9fHqS_eEp1oYkuKJhRHOGUxf8.INSTANCE);
    }
    setMaxAspectRatio(paramPackage);
    PackageBackwardCompatibility.modifySharedLibraries(paramPackage);
    if (hasDomainURLs(paramPackage))
    {
      paramPackage = applicationInfo;
      privateFlags |= 0x10;
    }
    else
    {
      paramPackage = applicationInfo;
      privateFlags &= 0xFFFFFFEF;
    }
    if (Build.FEATURES.ENABLE_APP_SCALING)
    {
      if ((paramInt & 0x10) != 0) {
        bool = true;
      }
      if (actualMaxAspect == -1.0F) {
        actualMaxAspect = AspectRatioChecker.getInstance().getActualAspectRatio(packageName, bool);
      }
      if ((ApplicationInfo.OVERRIDE_MAX_ASPECT_DEFAULT != -1.0F) && (actualMaxAspect != -1.0F)) {
        overrideMaxAspect = -1.0F;
      }
    }
    return true;
  }
  
  /* Error */
  private Package parseClusterPackage(File paramFile, int paramInt)
    throws PackageParser.PackageParserException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_1
    //   3: iconst_0
    //   4: invokestatic 2230	android/content/pm/PackageParser:parseClusterPackageLite	(Ljava/io/File;I)Landroid/content/pm/PackageParser$PackageLite;
    //   7: astore 4
    //   9: aload_0
    //   10: getfield 2232	android/content/pm/PackageParser:mOnlyCoreApps	Z
    //   13: ifeq +54 -> 67
    //   16: aload 4
    //   18: getfield 2233	android/content/pm/PackageParser$PackageLite:coreApp	Z
    //   21: ifeq +6 -> 27
    //   24: goto +43 -> 67
    //   27: new 436	java/lang/StringBuilder
    //   30: dup
    //   31: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   34: astore 5
    //   36: aload 5
    //   38: ldc_w 2235
    //   41: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   44: pop
    //   45: aload 5
    //   47: aload_1
    //   48: invokevirtual 524	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   51: pop
    //   52: new 50	android/content/pm/PackageParser$PackageParserException
    //   55: dup
    //   56: bipush -108
    //   58: aload 5
    //   60: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   63: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   66: athrow
    //   67: aconst_null
    //   68: astore 6
    //   70: aload 4
    //   72: getfield 2237	android/content/pm/PackageParser$PackageLite:isolatedSplits	Z
    //   75: ifeq +53 -> 128
    //   78: aload 4
    //   80: getfield 2238	android/content/pm/PackageParser$PackageLite:splitNames	[Ljava/lang/String;
    //   83: invokestatic 669	com/android/internal/util/ArrayUtils:isEmpty	([Ljava/lang/Object;)Z
    //   86: ifne +42 -> 128
    //   89: aload 4
    //   91: invokestatic 2244	android/content/pm/split/SplitAssetDependencyLoader:createDependenciesFromPackage	(Landroid/content/pm/PackageParser$PackageLite;)Landroid/util/SparseArray;
    //   94: astore 6
    //   96: new 2240	android/content/pm/split/SplitAssetDependencyLoader
    //   99: dup
    //   100: aload 4
    //   102: aload 6
    //   104: iload_2
    //   105: invokespecial 2247	android/content/pm/split/SplitAssetDependencyLoader:<init>	(Landroid/content/pm/PackageParser$PackageLite;Landroid/util/SparseArray;I)V
    //   108: astore 5
    //   110: goto +30 -> 140
    //   113: astore_1
    //   114: new 50	android/content/pm/PackageParser$PackageParserException
    //   117: dup
    //   118: bipush -101
    //   120: aload_1
    //   121: invokevirtual 2250	android/content/pm/split/SplitDependencyLoader$IllegalDependencyException:getMessage	()Ljava/lang/String;
    //   124: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   127: athrow
    //   128: new 2252	android/content/pm/split/DefaultSplitAssetLoader
    //   131: dup
    //   132: aload 4
    //   134: iload_2
    //   135: invokespecial 2255	android/content/pm/split/DefaultSplitAssetLoader:<init>	(Landroid/content/pm/PackageParser$PackageLite;I)V
    //   138: astore 5
    //   140: aload 5
    //   142: invokeinterface 2260 1 0
    //   147: astore 7
    //   149: new 510	java/io/File
    //   152: astore 8
    //   154: aload 8
    //   156: aload 4
    //   158: getfield 2261	android/content/pm/PackageParser$PackageLite:baseCodePath	Ljava/lang/String;
    //   161: invokespecial 659	java/io/File:<init>	(Ljava/lang/String;)V
    //   164: aload_0
    //   165: aload 8
    //   167: aload 7
    //   169: iload_2
    //   170: invokespecial 2263	android/content/pm/PackageParser:parseBaseApk	(Ljava/io/File;Landroid/content/res/AssetManager;I)Landroid/content/pm/PackageParser$Package;
    //   173: astore 7
    //   175: aload 7
    //   177: ifnull +161 -> 338
    //   180: aload 4
    //   182: getfield 2238	android/content/pm/PackageParser$PackageLite:splitNames	[Ljava/lang/String;
    //   185: invokestatic 669	com/android/internal/util/ArrayUtils:isEmpty	([Ljava/lang/Object;)Z
    //   188: ifne +123 -> 311
    //   191: aload 4
    //   193: getfield 2238	android/content/pm/PackageParser$PackageLite:splitNames	[Ljava/lang/String;
    //   196: arraylength
    //   197: istore 9
    //   199: aload 7
    //   201: aload 4
    //   203: getfield 2238	android/content/pm/PackageParser$PackageLite:splitNames	[Ljava/lang/String;
    //   206: putfield 852	android/content/pm/PackageParser$Package:splitNames	[Ljava/lang/String;
    //   209: aload 7
    //   211: aload 4
    //   213: getfield 2264	android/content/pm/PackageParser$PackageLite:splitCodePaths	[Ljava/lang/String;
    //   216: putfield 664	android/content/pm/PackageParser$Package:splitCodePaths	[Ljava/lang/String;
    //   219: aload 7
    //   221: aload 4
    //   223: getfield 2265	android/content/pm/PackageParser$PackageLite:splitRevisionCodes	[I
    //   226: putfield 867	android/content/pm/PackageParser$Package:splitRevisionCodes	[I
    //   229: aload 7
    //   231: iload 9
    //   233: newarray int
    //   235: putfield 2268	android/content/pm/PackageParser$Package:splitFlags	[I
    //   238: aload 7
    //   240: iload 9
    //   242: newarray int
    //   244: putfield 2271	android/content/pm/PackageParser$Package:splitPrivateFlags	[I
    //   247: aload 7
    //   249: getfield 598	android/content/pm/PackageParser$Package:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   252: aload 7
    //   254: getfield 852	android/content/pm/PackageParser$Package:splitNames	[Ljava/lang/String;
    //   257: putfield 2272	android/content/pm/ApplicationInfo:splitNames	[Ljava/lang/String;
    //   260: aload 7
    //   262: getfield 598	android/content/pm/PackageParser$Package:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   265: aload 6
    //   267: putfield 2276	android/content/pm/ApplicationInfo:splitDependencies	Landroid/util/SparseArray;
    //   270: aload 7
    //   272: getfield 598	android/content/pm/PackageParser$Package:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   275: iload 9
    //   277: anewarray 327	java/lang/String
    //   280: putfield 2279	android/content/pm/ApplicationInfo:splitClassLoaderNames	[Ljava/lang/String;
    //   283: iload_3
    //   284: iload 9
    //   286: if_icmpge +25 -> 311
    //   289: aload_0
    //   290: aload 7
    //   292: iload_3
    //   293: aload 5
    //   295: iload_3
    //   296: invokeinterface 2283 2 0
    //   301: iload_2
    //   302: invokespecial 2287	android/content/pm/PackageParser:parseSplitApk	(Landroid/content/pm/PackageParser$Package;ILandroid/content/res/AssetManager;I)V
    //   305: iinc 3 1
    //   308: goto -25 -> 283
    //   311: aload 7
    //   313: aload_1
    //   314: invokevirtual 2290	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   317: invokevirtual 2293	android/content/pm/PackageParser$Package:setCodePath	(Ljava/lang/String;)V
    //   320: aload 7
    //   322: aload 4
    //   324: getfield 2295	android/content/pm/PackageParser$PackageLite:use32bitAbi	Z
    //   327: invokevirtual 2299	android/content/pm/PackageParser$Package:setUse32bitAbi	(Z)V
    //   330: aload 5
    //   332: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   335: aload 7
    //   337: areturn
    //   338: new 50	android/content/pm/PackageParser$PackageParserException
    //   341: astore 6
    //   343: new 436	java/lang/StringBuilder
    //   346: astore_1
    //   347: aload_1
    //   348: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   351: aload_1
    //   352: ldc_w 2301
    //   355: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   358: pop
    //   359: aload_1
    //   360: aload 8
    //   362: invokevirtual 524	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   365: pop
    //   366: aload 6
    //   368: bipush -100
    //   370: aload_1
    //   371: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   374: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   377: aload 6
    //   379: athrow
    //   380: astore_1
    //   381: goto +54 -> 435
    //   384: astore 8
    //   386: new 50	android/content/pm/PackageParser$PackageParserException
    //   389: astore_1
    //   390: new 436	java/lang/StringBuilder
    //   393: astore 6
    //   395: aload 6
    //   397: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   400: aload 6
    //   402: ldc_w 2303
    //   405: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   408: pop
    //   409: aload 6
    //   411: aload 4
    //   413: getfield 2261	android/content/pm/PackageParser$PackageLite:baseCodePath	Ljava/lang/String;
    //   416: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   419: pop
    //   420: aload_1
    //   421: bipush -102
    //   423: aload 6
    //   425: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   428: aload 8
    //   430: invokespecial 1742	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   433: aload_1
    //   434: athrow
    //   435: aload 5
    //   437: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   440: aload_1
    //   441: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	442	0	this	PackageParser
    //   0	442	1	paramFile	File
    //   0	442	2	paramInt	int
    //   1	305	3	i	int
    //   7	405	4	localPackageLite	PackageLite
    //   34	402	5	localObject1	Object
    //   68	356	6	localObject2	Object
    //   147	189	7	localObject3	Object
    //   152	209	8	localFile	File
    //   384	45	8	localIOException	IOException
    //   197	90	9	j	int
    // Exception table:
    //   from	to	target	type
    //   89	110	113	android/content/pm/split/SplitDependencyLoader$IllegalDependencyException
    //   140	175	380	finally
    //   180	283	380	finally
    //   289	305	380	finally
    //   311	330	380	finally
    //   338	380	380	finally
    //   386	435	380	finally
    //   140	175	384	java/io/IOException
    //   180	283	384	java/io/IOException
    //   289	305	384	java/io/IOException
    //   311	330	384	java/io/IOException
    //   338	380	384	java/io/IOException
  }
  
  static PackageLite parseClusterPackageLite(File paramFile, int paramInt)
    throws PackageParser.PackageParserException
  {
    Object localObject1 = paramFile.listFiles();
    if (!ArrayUtils.isEmpty((Object[])localObject1))
    {
      Trace.traceBegin(262144L, "parseApkLite");
      ArrayMap localArrayMap = new ArrayMap();
      int i = localObject1.length;
      int j = 0;
      int k = 0;
      Object localObject2 = null;
      Object localObject3;
      Object localObject4;
      for (int m = 0; m < i; m++)
      {
        localObject3 = localObject1[m];
        if (isApkFile((File)localObject3))
        {
          localObject4 = parseApkLite((File)localObject3, paramInt);
          if (localObject2 == null)
          {
            localObject2 = packageName;
            k = versionCode;
          }
          else
          {
            if (!((String)localObject2).equals(packageName)) {
              break label261;
            }
            if (k != versionCode) {
              break label191;
            }
          }
          if (localArrayMap.put(splitName, localObject4) != null)
          {
            paramFile = new StringBuilder();
            paramFile.append("Split name ");
            paramFile.append(splitName);
            paramFile.append(" defined more than once; most recent was ");
            paramFile.append(localObject3);
            throw new PackageParserException(-101, paramFile.toString());
            label191:
            paramFile = new StringBuilder();
            paramFile.append("Inconsistent version ");
            paramFile.append(versionCode);
            paramFile.append(" in ");
            paramFile.append(localObject3);
            paramFile.append("; expected ");
            paramFile.append(k);
            throw new PackageParserException(-101, paramFile.toString());
            label261:
            paramFile = new StringBuilder();
            paramFile.append("Inconsistent package ");
            paramFile.append(packageName);
            paramFile.append(" in ");
            paramFile.append(localObject3);
            paramFile.append("; expected ");
            paramFile.append((String)localObject2);
            throw new PackageParserException(-101, paramFile.toString());
          }
        }
      }
      Trace.traceEnd(262144L);
      ApkLite localApkLite = (ApkLite)localArrayMap.remove(null);
      if (localApkLite != null)
      {
        k = localArrayMap.size();
        localObject2 = null;
        localObject4 = null;
        localObject3 = null;
        localObject1 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        if (k > 0)
        {
          localObject2 = new String[k];
          boolean[] arrayOfBoolean = new boolean[k];
          String[] arrayOfString1 = new String[k];
          String[] arrayOfString2 = new String[k];
          String[] arrayOfString3 = new String[k];
          int[] arrayOfInt = new int[k];
          String[] arrayOfString4 = (String[])localArrayMap.keySet().toArray((Object[])localObject2);
          Arrays.sort(arrayOfString4, sSplitNameComparator);
          for (paramInt = j;; paramInt++)
          {
            localObject2 = arrayOfString4;
            localObject4 = arrayOfBoolean;
            localObject3 = arrayOfString1;
            localObject1 = arrayOfString2;
            localObject5 = arrayOfString3;
            localObject6 = arrayOfInt;
            if (paramInt >= k) {
              break;
            }
            localObject2 = (ApkLite)localArrayMap.get(arrayOfString4[paramInt]);
            arrayOfString1[paramInt] = usesSplitName;
            arrayOfBoolean[paramInt] = isFeatureSplit;
            arrayOfString2[paramInt] = configForSplit;
            arrayOfString3[paramInt] = codePath;
            arrayOfInt[paramInt] = revisionCode;
          }
        }
        return new PackageLite(paramFile.getAbsolutePath(), localApkLite, (String[])localObject2, (boolean[])localObject4, (String[])localObject3, (String[])localObject1, localObject5, localObject6);
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Missing base APK in ");
      ((StringBuilder)localObject2).append(paramFile);
      throw new PackageParserException(-101, ((StringBuilder)localObject2).toString());
    }
    throw new PackageParserException(-100, "No packages found in split");
  }
  
  private Instrumentation parseInstrumentation(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestInstrumentation);
    if (mParseInstrumentationArgs == null)
    {
      mParseInstrumentationArgs = new ParsePackageItemArgs(paramPackage, paramArrayOfString, 2, 0, 1, 8, 6, 7);
      mParseInstrumentationArgs.tag = "<instrumentation>";
    }
    mParseInstrumentationArgs.sa = localTypedArray;
    Instrumentation localInstrumentation = new Instrumentation(mParseInstrumentationArgs, new InstrumentationInfo());
    if (paramArrayOfString[0] != null)
    {
      localTypedArray.recycle();
      mParseError = -108;
      return null;
    }
    String str = localTypedArray.getNonResourceString(3);
    InstrumentationInfo localInstrumentationInfo = info;
    if (str != null) {
      str = str.intern();
    } else {
      str = null;
    }
    targetPackage = str;
    str = localTypedArray.getNonResourceString(9);
    localInstrumentationInfo = info;
    if (str != null) {
      str = str.intern();
    } else {
      str = null;
    }
    targetProcesses = str;
    info.handleProfiling = localTypedArray.getBoolean(4, false);
    info.functionalTest = localTypedArray.getBoolean(5, false);
    localTypedArray.recycle();
    if (info.targetPackage == null)
    {
      paramArrayOfString[0] = "<instrumentation> does not specify targetPackage";
      mParseError = -108;
      return null;
    }
    if (!parseAllMetaData(paramResources, paramXmlResourceParser, "<instrumentation>", localInstrumentation, paramArrayOfString))
    {
      mParseError = -108;
      return null;
    }
    instrumentation.add(localInstrumentation);
    return localInstrumentation;
  }
  
  private boolean parseIntent(Resources paramResources, XmlResourceParser paramXmlResourceParser, boolean paramBoolean1, boolean paramBoolean2, IntentInfo paramIntentInfo, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestIntentFilter);
    paramIntentInfo.setPriority(((TypedArray)localObject1).getInt(2, 0));
    paramIntentInfo.setOrder(((TypedArray)localObject1).getInt(3, 0));
    Object localObject2 = ((TypedArray)localObject1).peekValue(0);
    int i;
    if (localObject2 != null)
    {
      i = resourceId;
      labelRes = i;
      if (i == 0) {
        nonLocalizedLabel = ((TypedValue)localObject2).coerceToString();
      }
    }
    if (Resources.getSystem().getBoolean(17957069)) {
      i = ((TypedArray)localObject1).getResourceId(7, 0);
    } else {
      i = 0;
    }
    if (i != 0) {
      icon = i;
    } else {
      icon = ((TypedArray)localObject1).getResourceId(1, 0);
    }
    logo = ((TypedArray)localObject1).getResourceId(4, 0);
    banner = ((TypedArray)localObject1).getResourceId(5, 0);
    if (paramBoolean2) {
      paramIntentInfo.setAutoVerify(((TypedArray)localObject1).getBoolean(6, false));
    }
    ((TypedArray)localObject1).recycle();
    int j = paramXmlResourceParser.getDepth();
    for (;;)
    {
      i = paramXmlResourceParser.next();
      if (i == 1) {
        break;
      }
      if ((i == 3) && (paramXmlResourceParser.getDepth() <= j)) {
        break;
      }
      if ((i != 3) && (i != 4))
      {
        localObject1 = paramXmlResourceParser.getName();
        if (((String)localObject1).equals("action"))
        {
          localObject1 = paramXmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
          if ((localObject1 != null) && (localObject1 != ""))
          {
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
            paramIntentInfo.addAction((String)localObject1);
          }
        }
        for (;;)
        {
          break;
          paramArrayOfString[0] = "No value supplied for <android:name>";
          return false;
          if (((String)localObject1).equals("category"))
          {
            localObject1 = paramXmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
            if ((localObject1 != null) && (localObject1 != ""))
            {
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
              paramIntentInfo.addCategory((String)localObject1);
            }
            else
            {
              paramArrayOfString[0] = "No value supplied for <android:name>";
              return false;
            }
          }
          else if (((String)localObject1).equals("data"))
          {
            localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestData);
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(0, 0);
            if (localObject2 != null) {
              try
              {
                paramIntentInfo.addDataType((String)localObject2);
              }
              catch (IntentFilter.MalformedMimeTypeException paramResources)
              {
                paramArrayOfString[0] = paramResources.toString();
                ((TypedArray)localObject1).recycle();
                return false;
              }
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(1, 0);
            if (localObject2 != null) {
              paramIntentInfo.addDataScheme((String)localObject2);
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(7, 0);
            if (localObject2 != null) {
              paramIntentInfo.addDataSchemeSpecificPart((String)localObject2, 0);
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(8, 0);
            if (localObject2 != null) {
              paramIntentInfo.addDataSchemeSpecificPart((String)localObject2, 1);
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(9, 0);
            if (localObject2 != null)
            {
              if (!paramBoolean1)
              {
                paramArrayOfString[0] = "sspPattern not allowed here; ssp must be literal";
                return false;
              }
              paramIntentInfo.addDataSchemeSpecificPart((String)localObject2, 2);
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(2, 0);
            String str = ((TypedArray)localObject1).getNonConfigurationString(3, 0);
            if (localObject2 != null) {
              paramIntentInfo.addDataAuthority((String)localObject2, str);
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(4, 0);
            if (localObject2 != null) {
              paramIntentInfo.addDataPath((String)localObject2, 0);
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(5, 0);
            if (localObject2 != null) {
              paramIntentInfo.addDataPath((String)localObject2, 1);
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(6, 0);
            if (localObject2 != null)
            {
              if (!paramBoolean1)
              {
                paramArrayOfString[0] = "pathPattern not allowed here; path must be literal";
                return false;
              }
              paramIntentInfo.addDataPath((String)localObject2, 2);
            }
            localObject2 = ((TypedArray)localObject1).getNonConfigurationString(10, 0);
            if (localObject2 != null)
            {
              if (!paramBoolean1)
              {
                paramArrayOfString[0] = "pathAdvancedPattern not allowed here; path must be literal";
                return false;
              }
              paramIntentInfo.addDataPath((String)localObject2, 3);
            }
            ((TypedArray)localObject1).recycle();
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
          else
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Unknown element under <intent-filter>: ");
            ((StringBuilder)localObject1).append(paramXmlResourceParser.getName());
            ((StringBuilder)localObject1).append(" at ");
            ((StringBuilder)localObject1).append(mArchiveSourcePath);
            ((StringBuilder)localObject1).append(" ");
            ((StringBuilder)localObject1).append(paramXmlResourceParser.getPositionDescription());
            Slog.w("PackageParser", ((StringBuilder)localObject1).toString());
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
        }
      }
    }
    hasDefault = paramIntentInfo.hasCategory("android.intent.category.DEFAULT");
    return true;
  }
  
  private boolean parseKeySets(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlResourceParser.getDepth();
    int j = -1;
    Object localObject1 = null;
    ArrayMap localArrayMap = new ArrayMap();
    ArraySet localArraySet1 = new ArraySet();
    Object localObject2 = new ArrayMap();
    ArraySet localArraySet2 = new ArraySet();
    Object localObject3;
    label679:
    label764:
    for (;;)
    {
      int k = paramXmlResourceParser.next();
      if (k == 1) {
        break;
      }
      if ((k == 3) && (paramXmlResourceParser.getDepth() <= i)) {
        break;
      }
      if (k == 3)
      {
        if (paramXmlResourceParser.getDepth() == j)
        {
          localObject1 = null;
          j = -1;
        }
        else {}
      }
      else
      {
        localObject3 = paramXmlResourceParser.getName();
        if (((String)localObject3).equals("key-set"))
        {
          if (localObject1 != null)
          {
            paramPackage = new StringBuilder();
            paramPackage.append("Improperly nested 'key-set' tag at ");
            paramPackage.append(paramXmlResourceParser.getPositionDescription());
            paramArrayOfString[0] = paramPackage.toString();
            mParseError = -108;
            return false;
          }
          localObject3 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestKeySet);
          localObject1 = ((TypedArray)localObject3).getNonResourceString(0);
          ((ArrayMap)localObject2).put(localObject1, new ArraySet());
          j = paramXmlResourceParser.getDepth();
          ((TypedArray)localObject3).recycle();
        }
        else if (((String)localObject3).equals("public-key"))
        {
          if (localObject1 == null)
          {
            paramPackage = new StringBuilder();
            paramPackage.append("Improperly nested 'key-set' tag at ");
            paramPackage.append(paramXmlResourceParser.getPositionDescription());
            paramArrayOfString[0] = paramPackage.toString();
            mParseError = -108;
            return false;
          }
          localObject3 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestPublicKey);
          Object localObject4 = ((TypedArray)localObject3).getNonResourceString(0);
          Object localObject5 = ((TypedArray)localObject3).getNonResourceString(1);
          if ((localObject5 == null) && (localArrayMap.get(localObject4) == null))
          {
            paramPackage = new StringBuilder();
            paramPackage.append("'public-key' ");
            paramPackage.append((String)localObject4);
            paramPackage.append(" must define a public-key value on first use at ");
            paramPackage.append(paramXmlResourceParser.getPositionDescription());
            paramArrayOfString[0] = paramPackage.toString();
            mParseError = -108;
            ((TypedArray)localObject3).recycle();
            return false;
          }
          if (localObject5 != null)
          {
            localObject5 = parsePublicKey((String)localObject5);
            if (localObject5 == null)
            {
              localObject4 = new StringBuilder();
              ((StringBuilder)localObject4).append("No recognized valid key in 'public-key' tag at ");
              ((StringBuilder)localObject4).append(paramXmlResourceParser.getPositionDescription());
              ((StringBuilder)localObject4).append(" key-set ");
              ((StringBuilder)localObject4).append((String)localObject1);
              ((StringBuilder)localObject4).append(" will not be added to the package's defined key-sets.");
              Slog.w("PackageParser", ((StringBuilder)localObject4).toString());
              ((TypedArray)localObject3).recycle();
              localArraySet2.add(localObject1);
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
              break label764;
            }
            if ((localArrayMap.get(localObject4) != null) && (!((PublicKey)localArrayMap.get(localObject4)).equals(localObject5)))
            {
              paramPackage = new StringBuilder();
              paramPackage.append("Value of 'public-key' ");
              paramPackage.append((String)localObject4);
              paramPackage.append(" conflicts with previously defined value at ");
              paramPackage.append(paramXmlResourceParser.getPositionDescription());
              paramArrayOfString[0] = paramPackage.toString();
              mParseError = -108;
              ((TypedArray)localObject3).recycle();
              return false;
            }
            localArrayMap.put(localObject4, localObject5);
          }
          ((ArraySet)((ArrayMap)localObject2).get(localObject1)).add(localObject4);
          ((TypedArray)localObject3).recycle();
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
        else
        {
          if (!((String)localObject3).equals("upgrade-key-set")) {
            break label679;
          }
          localObject3 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestUpgradeKeySet);
          localArraySet1.add(((TypedArray)localObject3).getNonResourceString(0));
          ((TypedArray)localObject3).recycle();
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
        continue;
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Unknown element under <key-sets>: ");
        ((StringBuilder)localObject3).append(paramXmlResourceParser.getName());
        ((StringBuilder)localObject3).append(" at ");
        ((StringBuilder)localObject3).append(mArchiveSourcePath);
        ((StringBuilder)localObject3).append(" ");
        ((StringBuilder)localObject3).append(paramXmlResourceParser.getPositionDescription());
        Slog.w("PackageParser", ((StringBuilder)localObject3).toString());
        XmlUtils.skipCurrentTag(paramXmlResourceParser);
      }
    }
    paramResources = localArrayMap.keySet();
    if (paramResources.removeAll(((ArrayMap)localObject2).keySet()))
    {
      paramResources = new StringBuilder();
      paramResources.append("Package");
      paramResources.append(packageName);
      paramResources.append(" AndroidManifext.xml 'key-set' and 'public-key' names must be distinct.");
      paramArrayOfString[0] = paramResources.toString();
      mParseError = -108;
      return false;
    }
    mKeySetMapping = new ArrayMap();
    localObject1 = ((ArrayMap)localObject2).entrySet().iterator();
    if (((Iterator)localObject1).hasNext())
    {
      paramXmlResourceParser = (Map.Entry)((Iterator)localObject1).next();
      localObject2 = (String)paramXmlResourceParser.getKey();
      if (((ArraySet)paramXmlResourceParser.getValue()).size() == 0)
      {
        paramXmlResourceParser = new StringBuilder();
        paramXmlResourceParser.append("Package");
        paramXmlResourceParser.append(packageName);
        paramXmlResourceParser.append(" AndroidManifext.xml 'key-set' ");
        paramXmlResourceParser.append((String)localObject2);
        paramXmlResourceParser.append(" has no valid associated 'public-key'. Not including in package's defined key-sets.");
        Slog.w("PackageParser", paramXmlResourceParser.toString());
      }
      for (;;)
      {
        break;
        if (localArraySet2.contains(localObject2))
        {
          paramXmlResourceParser = new StringBuilder();
          paramXmlResourceParser.append("Package");
          paramXmlResourceParser.append(packageName);
          paramXmlResourceParser.append(" AndroidManifext.xml 'key-set' ");
          paramXmlResourceParser.append((String)localObject2);
          paramXmlResourceParser.append(" contained improper 'public-key' tags. Not including in package's defined key-sets.");
          Slog.w("PackageParser", paramXmlResourceParser.toString());
        }
        else
        {
          mKeySetMapping.put(localObject2, new ArraySet());
          paramXmlResourceParser = ((ArraySet)paramXmlResourceParser.getValue()).iterator();
          while (paramXmlResourceParser.hasNext())
          {
            localObject3 = (String)paramXmlResourceParser.next();
            ((ArraySet)mKeySetMapping.get(localObject2)).add((PublicKey)localArrayMap.get(localObject3));
          }
        }
      }
    }
    if (mKeySetMapping.keySet().containsAll(localArraySet1))
    {
      mUpgradeKeySets = localArraySet1;
      return true;
    }
    paramResources = new StringBuilder();
    paramResources.append("Package");
    paramResources.append(packageName);
    paramResources.append(" AndroidManifext.xml does not define all 'upgrade-key-set's .");
    paramArrayOfString[0] = paramResources.toString();
    mParseError = -108;
    return false;
  }
  
  private void parseLayout(Resources paramResources, AttributeSet paramAttributeSet, Activity paramActivity)
  {
    paramResources = paramResources.obtainAttributes(paramAttributeSet, R.styleable.AndroidManifestLayout);
    int i = -1;
    float f1 = -1.0F;
    int j = -1;
    float f2 = -1.0F;
    int k = paramResources.getType(3);
    float f3;
    if (k == 6)
    {
      f3 = paramResources.getFraction(3, 1, 1, -1.0F);
    }
    else
    {
      f3 = f1;
      if (k == 5)
      {
        i = paramResources.getDimensionPixelSize(3, -1);
        f3 = f1;
      }
    }
    k = paramResources.getType(4);
    if (k == 6)
    {
      f1 = paramResources.getFraction(4, 1, 1, -1.0F);
    }
    else
    {
      f1 = f2;
      if (k == 5)
      {
        j = paramResources.getDimensionPixelSize(4, -1);
        f1 = f2;
      }
    }
    int m = paramResources.getInt(0, 17);
    k = paramResources.getDimensionPixelSize(1, -1);
    int n = paramResources.getDimensionPixelSize(2, -1);
    paramResources.recycle();
    info.windowLayout = new ActivityInfo.WindowLayout(i, f3, j, f1, m, k, n);
  }
  
  private Bundle parseMetaData(Resources paramResources, XmlResourceParser paramXmlResourceParser, Bundle paramBundle, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestMetaData);
    paramResources = paramBundle;
    if (paramBundle == null) {
      paramResources = new Bundle();
    }
    boolean bool = false;
    String str = localTypedArray.getNonConfigurationString(0, 0);
    paramBundle = null;
    if (str == null)
    {
      paramArrayOfString[0] = "<meta-data> requires an android:name attribute";
      localTypedArray.recycle();
      return null;
    }
    str = str.intern();
    TypedValue localTypedValue = localTypedArray.peekValue(2);
    if ((localTypedValue != null) && (resourceId != 0))
    {
      paramResources.putInt(str, resourceId);
    }
    else
    {
      localTypedValue = localTypedArray.peekValue(1);
      if (localTypedValue != null)
      {
        if (type == 3)
        {
          paramArrayOfString = localTypedValue.coerceToString();
          if (paramArrayOfString != null) {
            paramBundle = paramArrayOfString.toString();
          }
          paramResources.putString(str, paramBundle);
        }
        else if (type == 18)
        {
          if (data != 0) {
            bool = true;
          }
          paramResources.putBoolean(str, bool);
        }
        else if ((type >= 16) && (type <= 31))
        {
          paramResources.putInt(str, data);
        }
        else if (type == 4)
        {
          paramResources.putFloat(str, localTypedValue.getFloat());
        }
        else
        {
          paramBundle = new StringBuilder();
          paramBundle.append("<meta-data> only supports string, integer, float, color, boolean, and resource reference types: ");
          paramBundle.append(paramXmlResourceParser.getName());
          paramBundle.append(" at ");
          paramBundle.append(mArchiveSourcePath);
          paramBundle.append(" ");
          paramBundle.append(paramXmlResourceParser.getPositionDescription());
          Slog.w("PackageParser", paramBundle.toString());
        }
      }
      else
      {
        paramArrayOfString[0] = "<meta-data> requires an android:value or android:resource attribute";
        paramResources = null;
      }
    }
    localTypedArray.recycle();
    XmlUtils.skipCurrentTag(paramXmlResourceParser);
    return paramResources;
  }
  
  private static PackageLite parseMonolithicPackageLite(File paramFile, int paramInt)
    throws PackageParser.PackageParserException
  {
    Trace.traceBegin(262144L, "parseApkLite");
    ApkLite localApkLite = parseApkLite(paramFile, paramInt);
    paramFile = paramFile.getAbsolutePath();
    Trace.traceEnd(262144L);
    return new PackageLite(paramFile, localApkLite, null, null, null, null, null, null);
  }
  
  private static boolean parsePackageItemInfo(Package paramPackage, PackageItemInfo paramPackageItemInfo, String[] paramArrayOfString, String paramString, TypedArray paramTypedArray, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    if (paramTypedArray == null)
    {
      paramPackage = new StringBuilder();
      paramPackage.append(paramString);
      paramPackage.append(" does not contain any attributes");
      paramArrayOfString[0] = paramPackage.toString();
      return false;
    }
    String str = paramTypedArray.getNonConfigurationString(paramInt1, 0);
    if (str == null)
    {
      if (paramBoolean)
      {
        paramPackage = new StringBuilder();
        paramPackage.append(paramString);
        paramPackage.append(" does not specify android:name");
        paramArrayOfString[0] = paramPackage.toString();
        return false;
      }
    }
    else
    {
      name = buildClassName(applicationInfo.packageName, str, paramArrayOfString);
      if (name == null) {
        return false;
      }
    }
    if (Resources.getSystem().getBoolean(17957069)) {
      paramInt1 = paramTypedArray.getResourceId(paramInt4, 0);
    } else {
      paramInt1 = 0;
    }
    if (paramInt1 != 0)
    {
      icon = paramInt1;
      nonLocalizedLabel = null;
    }
    else
    {
      paramInt1 = paramTypedArray.getResourceId(paramInt3, 0);
      if (paramInt1 != 0)
      {
        icon = paramInt1;
        nonLocalizedLabel = null;
      }
    }
    paramInt1 = paramTypedArray.getResourceId(paramInt5, 0);
    if (paramInt1 != 0) {
      logo = paramInt1;
    }
    paramInt1 = paramTypedArray.getResourceId(paramInt6, 0);
    if (paramInt1 != 0) {
      banner = paramInt1;
    }
    paramArrayOfString = paramTypedArray.peekValue(paramInt2);
    if (paramArrayOfString != null)
    {
      paramInt1 = resourceId;
      labelRes = paramInt1;
      if (paramInt1 == 0) {
        nonLocalizedLabel = paramArrayOfString.coerceToString();
      }
    }
    packageName = packageName;
    return true;
  }
  
  public static PackageLite parsePackageLite(File paramFile, int paramInt)
    throws PackageParser.PackageParserException
  {
    if (paramFile.isDirectory()) {
      return parseClusterPackageLite(paramFile, paramInt);
    }
    return parseMonolithicPackageLite(paramFile, paramInt);
  }
  
  private static Pair<String, String> parsePackageSplitNames(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
    throws IOException, XmlPullParserException, PackageParser.PackageParserException
  {
    int i;
    do
    {
      i = paramXmlPullParser.next();
    } while ((i != 2) && (i != 1));
    if (i == 2)
    {
      if (paramXmlPullParser.getName().equals("manifest"))
      {
        String str = paramAttributeSet.getAttributeValue(null, "package");
        if (!"android".equals(str))
        {
          paramXmlPullParser = validateName(str, true, true);
          if (paramXmlPullParser != null)
          {
            paramAttributeSet = new StringBuilder();
            paramAttributeSet.append("Invalid manifest package: ");
            paramAttributeSet.append(paramXmlPullParser);
            throw new PackageParserException(-106, paramAttributeSet.toString());
          }
        }
        paramAttributeSet = paramAttributeSet.getAttributeValue(null, "split");
        paramXmlPullParser = paramAttributeSet;
        if (paramAttributeSet != null) {
          if (paramAttributeSet.length() == 0)
          {
            paramXmlPullParser = null;
          }
          else
          {
            paramXmlPullParser = validateName(paramAttributeSet, false, false);
            if (paramXmlPullParser == null)
            {
              paramXmlPullParser = paramAttributeSet;
            }
            else
            {
              paramAttributeSet = new StringBuilder();
              paramAttributeSet.append("Invalid manifest split: ");
              paramAttributeSet.append(paramXmlPullParser);
              throw new PackageParserException(-106, paramAttributeSet.toString());
            }
          }
        }
        paramAttributeSet = str.intern();
        if (paramXmlPullParser != null) {
          paramXmlPullParser = paramXmlPullParser.intern();
        }
        return Pair.create(paramAttributeSet, paramXmlPullParser);
      }
      throw new PackageParserException(-108, "No <manifest> tag");
    }
    throw new PackageParserException(-108, "No start tag found");
  }
  
  private boolean parsePermission(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestPermission);
    Permission localPermission = new Permission(paramPackage);
    if (!parsePackageItemInfo(paramPackage, info, paramArrayOfString, "<permission>", localTypedArray, true, 2, 0, 1, 9, 6, 8))
    {
      localTypedArray.recycle();
      mParseError = -108;
      return false;
    }
    info.group = localTypedArray.getNonResourceString(4);
    if (info.group != null) {
      info.group = info.group.intern();
    }
    info.descriptionRes = localTypedArray.getResourceId(5, 0);
    info.requestRes = localTypedArray.getResourceId(10, 0);
    info.protectionLevel = localTypedArray.getInt(3, 0);
    info.flags = localTypedArray.getInt(7, 0);
    localTypedArray.recycle();
    if (info.protectionLevel == -1)
    {
      paramArrayOfString[0] = "<permission> does not specify protectionLevel";
      mParseError = -108;
      return false;
    }
    info.protectionLevel = PermissionInfo.fixProtectionLevel(info.protectionLevel);
    if ((info.getProtectionFlags() != 0) && ((info.protectionLevel & 0x1000) == 0) && ((info.protectionLevel & 0x2000) == 0) && ((info.protectionLevel & 0xF) != 2))
    {
      paramArrayOfString[0] = "<permission>  protectionLevel specifies a non-instant flag but is not based on signature type";
      mParseError = -108;
      return false;
    }
    if (!parseAllMetaData(paramResources, paramXmlResourceParser, "<permission>", localPermission, paramArrayOfString))
    {
      mParseError = -108;
      return false;
    }
    permissions.add(localPermission);
    return true;
  }
  
  private boolean parsePermissionGroup(Package paramPackage, int paramInt, Resources paramResources, XmlResourceParser paramXmlResourceParser, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    PermissionGroup localPermissionGroup = new PermissionGroup(paramPackage);
    TypedArray localTypedArray = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestPermissionGroup);
    if (!parsePackageItemInfo(paramPackage, info, paramArrayOfString, "<permission-group>", localTypedArray, true, 2, 0, 1, 8, 5, 7))
    {
      localTypedArray.recycle();
      mParseError = -108;
      return false;
    }
    info.descriptionRes = localTypedArray.getResourceId(4, 0);
    info.requestRes = localTypedArray.getResourceId(9, 0);
    info.flags = localTypedArray.getInt(6, 0);
    info.priority = localTypedArray.getInt(3, 0);
    localTypedArray.recycle();
    if (!parseAllMetaData(paramResources, paramXmlResourceParser, "<permission-group>", localPermissionGroup, paramArrayOfString))
    {
      mParseError = -108;
      return false;
    }
    permissionGroups.add(localPermissionGroup);
    return true;
  }
  
  private boolean parsePermissionTree(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    Permission localPermission = new Permission(paramPackage);
    TypedArray localTypedArray = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestPermissionTree);
    if (!parsePackageItemInfo(paramPackage, info, paramArrayOfString, "<permission-tree>", localTypedArray, true, 2, 0, 1, 5, 3, 4))
    {
      localTypedArray.recycle();
      mParseError = -108;
      return false;
    }
    localTypedArray.recycle();
    int i = info.name.indexOf('.');
    int j = i;
    if (i > 0) {
      j = info.name.indexOf('.', i + 1);
    }
    if (j < 0)
    {
      paramPackage = new StringBuilder();
      paramPackage.append("<permission-tree> name has less than three segments: ");
      paramPackage.append(info.name);
      paramArrayOfString[0] = paramPackage.toString();
      mParseError = -108;
      return false;
    }
    info.descriptionRes = 0;
    info.requestRes = 0;
    info.protectionLevel = 0;
    tree = true;
    if (!parseAllMetaData(paramResources, paramXmlResourceParser, "<permission-tree>", localPermission, paramArrayOfString))
    {
      mParseError = -108;
      return false;
    }
    permissions.add(localPermission);
    return true;
  }
  
  private Provider parseProvider(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt, String[] paramArrayOfString, CachedComponentArgs paramCachedComponentArgs)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestProvider);
    if (mProviderArgs == null)
    {
      mProviderArgs = new ParseComponentArgs(paramPackage, paramArrayOfString, 2, 0, 1, 19, 15, 17, mSeparateProcesses, 8, 14, 6);
      mProviderArgs.tag = "<provider>";
    }
    mProviderArgs.sa = localTypedArray;
    mProviderArgs.flags = paramInt;
    Provider localProvider = new Provider(mProviderArgs, new ProviderInfo());
    if (paramArrayOfString[0] != null)
    {
      localTypedArray.recycle();
      return null;
    }
    boolean bool = false;
    if (applicationInfo.targetSdkVersion < 17) {
      bool = true;
    }
    info.exported = localTypedArray.getBoolean(7, bool);
    String str = localTypedArray.getNonConfigurationString(10, 0);
    info.isSyncable = localTypedArray.getBoolean(11, false);
    paramCachedComponentArgs = localTypedArray.getNonConfigurationString(3, 0);
    Object localObject1 = localTypedArray.getNonConfigurationString(4, 0);
    Object localObject2 = localObject1;
    if (localObject1 == null) {
      localObject2 = paramCachedComponentArgs;
    }
    if (localObject2 == null)
    {
      info.readPermission = applicationInfo.permission;
    }
    else
    {
      localObject1 = info;
      if (((String)localObject2).length() > 0) {
        localObject2 = ((String)localObject2).toString().intern();
      } else {
        localObject2 = null;
      }
      readPermission = ((String)localObject2);
    }
    localObject1 = localTypedArray.getNonConfigurationString(5, 0);
    localObject2 = localObject1;
    if (localObject1 == null) {
      localObject2 = paramCachedComponentArgs;
    }
    if (localObject2 == null)
    {
      info.writePermission = applicationInfo.permission;
    }
    else
    {
      localObject1 = info;
      if (((String)localObject2).length() > 0) {
        paramCachedComponentArgs = ((String)localObject2).toString().intern();
      } else {
        paramCachedComponentArgs = null;
      }
      writePermission = paramCachedComponentArgs;
    }
    info.grantUriPermissions = localTypedArray.getBoolean(13, false);
    info.multiprocess = localTypedArray.getBoolean(9, false);
    info.initOrder = localTypedArray.getInt(12, 0);
    info.splitName = localTypedArray.getNonConfigurationString(21, 0);
    info.flags = 0;
    if (localTypedArray.getBoolean(16, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x40000000;
    }
    paramCachedComponentArgs = info;
    localObject2 = info;
    bool = localTypedArray.getBoolean(18, false);
    directBootAware = bool;
    encryptionAware = bool;
    if (info.directBootAware)
    {
      paramCachedComponentArgs = applicationInfo;
      privateFlags |= 0x100;
    }
    bool = localTypedArray.getBoolean(20, false);
    if (bool)
    {
      paramCachedComponentArgs = info;
      flags |= 0x100000;
      visibleToInstantApps = true;
    }
    localTypedArray.recycle();
    if (((applicationInfo.privateFlags & 0x2) != 0) && (info.processName == packageName))
    {
      paramArrayOfString[0] = "Heavy-weight applications can not have providers in main process";
      return null;
    }
    if (str == null)
    {
      paramArrayOfString[0] = "<provider> does not include authorities attribute";
      return null;
    }
    if (str.length() <= 0)
    {
      paramArrayOfString[0] = "<provider> has empty authorities attribute";
      return null;
    }
    info.authority = str.intern();
    if (!parseProviderTags(paramResources, paramXmlResourceParser, bool, localProvider, paramArrayOfString)) {
      return null;
    }
    return localProvider;
  }
  
  private boolean parseProviderTags(Resources paramResources, XmlResourceParser paramXmlResourceParser, boolean paramBoolean, Provider paramProvider, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlResourceParser.getDepth();
    for (;;)
    {
      int j = paramXmlResourceParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlResourceParser.getDepth() <= i))) {
        break;
      }
      if ((j != 3) && (j != 4))
      {
        Object localObject1;
        Object localObject2;
        if (paramXmlResourceParser.getName().equals("intent-filter"))
        {
          localObject1 = new ProviderIntentInfo(paramProvider);
          if (!parseIntent(paramResources, paramXmlResourceParser, true, false, (IntentInfo)localObject1, paramArrayOfString)) {
            return false;
          }
          if (paramBoolean)
          {
            ((ProviderIntentInfo)localObject1).setVisibilityToInstantApp(1);
            localObject2 = info;
            flags |= 0x100000;
          }
          order = Math.max(((ProviderIntentInfo)localObject1).getOrder(), order);
          intents.add(localObject1);
        }
        else if (paramXmlResourceParser.getName().equals("meta-data"))
        {
          localObject2 = parseMetaData(paramResources, paramXmlResourceParser, metaData, paramArrayOfString);
          metaData = ((Bundle)localObject2);
          if (localObject2 == null) {
            return false;
          }
        }
        else
        {
          Object localObject3;
          if (paramXmlResourceParser.getName().equals("grant-uri-permission"))
          {
            localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestGrantUriPermission);
            localObject2 = null;
            localObject3 = ((TypedArray)localObject1).getNonConfigurationString(0, 0);
            if (localObject3 != null) {
              localObject2 = new PatternMatcher((String)localObject3, 0);
            }
            localObject3 = ((TypedArray)localObject1).getNonConfigurationString(1, 0);
            if (localObject3 != null) {
              localObject2 = new PatternMatcher((String)localObject3, 1);
            }
            localObject3 = ((TypedArray)localObject1).getNonConfigurationString(2, 0);
            if (localObject3 != null) {
              localObject2 = new PatternMatcher((String)localObject3, 2);
            }
            ((TypedArray)localObject1).recycle();
            if (localObject2 != null)
            {
              if (info.uriPermissionPatterns == null)
              {
                info.uriPermissionPatterns = new PatternMatcher[1];
                info.uriPermissionPatterns[0] = localObject2;
              }
              else
              {
                j = info.uriPermissionPatterns.length;
                localObject1 = new PatternMatcher[j + 1];
                System.arraycopy(info.uriPermissionPatterns, 0, localObject1, 0, j);
                localObject1[j] = localObject2;
                info.uriPermissionPatterns = ((PatternMatcher[])localObject1);
              }
              info.grantUriPermissions = true;
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
            }
            else
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("Unknown element under <path-permission>: ");
              ((StringBuilder)localObject2).append(paramXmlResourceParser.getName());
              ((StringBuilder)localObject2).append(" at ");
              ((StringBuilder)localObject2).append(mArchiveSourcePath);
              ((StringBuilder)localObject2).append(" ");
              ((StringBuilder)localObject2).append(paramXmlResourceParser.getPositionDescription());
              Slog.w("PackageParser", ((StringBuilder)localObject2).toString());
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
            }
          }
          else if (paramXmlResourceParser.getName().equals("path-permission"))
          {
            TypedArray localTypedArray = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestPathPermission);
            String str1 = null;
            localObject1 = localTypedArray.getNonConfigurationString(0, 0);
            localObject3 = localTypedArray.getNonConfigurationString(1, 0);
            localObject2 = localObject3;
            if (localObject3 == null) {
              localObject2 = localObject1;
            }
            localObject3 = localObject2;
            String str2 = localTypedArray.getNonConfigurationString(2, 0);
            localObject2 = str2;
            if (str2 == null) {
              localObject2 = localObject1;
            }
            j = 0;
            localObject1 = localObject3;
            if (localObject3 != null)
            {
              localObject1 = ((String)localObject3).intern();
              j = 1;
            }
            localObject3 = localObject2;
            if (localObject2 != null)
            {
              localObject3 = ((String)localObject2).intern();
              j = 1;
            }
            if (j == 0)
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("No readPermission or writePermssion for <path-permission>: ");
              ((StringBuilder)localObject2).append(paramXmlResourceParser.getName());
              ((StringBuilder)localObject2).append(" at ");
              ((StringBuilder)localObject2).append(mArchiveSourcePath);
              ((StringBuilder)localObject2).append(" ");
              ((StringBuilder)localObject2).append(paramXmlResourceParser.getPositionDescription());
              Slog.w("PackageParser", ((StringBuilder)localObject2).toString());
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
            }
            else
            {
              str2 = localTypedArray.getNonConfigurationString(3, 0);
              localObject2 = str1;
              if (str2 != null) {
                localObject2 = new PathPermission(str2, 0, (String)localObject1, (String)localObject3);
              }
              str1 = localTypedArray.getNonConfigurationString(4, 0);
              if (str1 != null) {
                localObject2 = new PathPermission(str1, 1, (String)localObject1, (String)localObject3);
              }
              str1 = localTypedArray.getNonConfigurationString(5, 0);
              if (str1 != null) {
                localObject2 = new PathPermission(str1, 2, (String)localObject1, (String)localObject3);
              }
              str1 = localTypedArray.getNonConfigurationString(6, 0);
              if (str1 != null) {
                localObject2 = new PathPermission(str1, 3, (String)localObject1, (String)localObject3);
              }
              localTypedArray.recycle();
              if (localObject2 != null)
              {
                if (info.pathPermissions == null)
                {
                  info.pathPermissions = new PathPermission[1];
                  info.pathPermissions[0] = localObject2;
                }
                else
                {
                  j = info.pathPermissions.length;
                  localObject1 = new PathPermission[j + 1];
                  System.arraycopy(info.pathPermissions, 0, localObject1, 0, j);
                  localObject1[j] = localObject2;
                  info.pathPermissions = ((PathPermission[])localObject1);
                }
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
              }
              else
              {
                localObject2 = new StringBuilder();
                ((StringBuilder)localObject2).append("No path, pathPrefix, or pathPattern for <path-permission>: ");
                ((StringBuilder)localObject2).append(paramXmlResourceParser.getName());
                ((StringBuilder)localObject2).append(" at ");
                ((StringBuilder)localObject2).append(mArchiveSourcePath);
                ((StringBuilder)localObject2).append(" ");
                ((StringBuilder)localObject2).append(paramXmlResourceParser.getPositionDescription());
                Slog.w("PackageParser", ((StringBuilder)localObject2).toString());
                XmlUtils.skipCurrentTag(paramXmlResourceParser);
              }
            }
          }
          else
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Unknown element under <provider>: ");
            ((StringBuilder)localObject2).append(paramXmlResourceParser.getName());
            ((StringBuilder)localObject2).append(" at ");
            ((StringBuilder)localObject2).append(mArchiveSourcePath);
            ((StringBuilder)localObject2).append(" ");
            ((StringBuilder)localObject2).append(paramXmlResourceParser.getPositionDescription());
            Slog.w("PackageParser", ((StringBuilder)localObject2).toString());
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
        }
      }
    }
    return true;
  }
  
  public static final PublicKey parsePublicKey(String paramString)
  {
    if (paramString == null)
    {
      Slog.w("PackageParser", "Could not parse null public key");
      return null;
    }
    try
    {
      paramString = new X509EncodedKeySpec(Base64.decode(paramString, 0));
      try
      {
        PublicKey localPublicKey1 = KeyFactory.getInstance("RSA").generatePublic(paramString);
        return localPublicKey1;
      }
      catch (InvalidKeySpecException localInvalidKeySpecException1) {}catch (NoSuchAlgorithmException localNoSuchAlgorithmException1)
      {
        Slog.wtf("PackageParser", "Could not parse public key: RSA KeyFactory not included in build");
      }
      try
      {
        PublicKey localPublicKey2 = KeyFactory.getInstance("EC").generatePublic(paramString);
        return localPublicKey2;
      }
      catch (InvalidKeySpecException localInvalidKeySpecException2) {}catch (NoSuchAlgorithmException localNoSuchAlgorithmException2)
      {
        Slog.wtf("PackageParser", "Could not parse public key: EC KeyFactory not included in build");
      }
      try
      {
        paramString = KeyFactory.getInstance("DSA").generatePublic(paramString);
        return paramString;
      }
      catch (InvalidKeySpecException paramString) {}catch (NoSuchAlgorithmException paramString)
      {
        Slog.wtf("PackageParser", "Could not parse public key: DSA KeyFactory not included in build");
      }
      return null;
    }
    catch (IllegalArgumentException paramString)
    {
      Slog.w("PackageParser", "Could not parse verifier public key; invalid Base64");
    }
    return null;
  }
  
  private Service parseService(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt, String[] paramArrayOfString, CachedComponentArgs paramCachedComponentArgs)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = paramResources;
    XmlResourceParser localXmlResourceParser = paramXmlResourceParser;
    Object localObject2 = paramArrayOfString;
    TypedArray localTypedArray = ((Resources)localObject1).obtainAttributes(localXmlResourceParser, R.styleable.AndroidManifestService);
    if (mServiceArgs == null)
    {
      mServiceArgs = new ParseComponentArgs(paramPackage, (String[])localObject2, 2, 0, 1, 15, 8, 12, mSeparateProcesses, 6, 7, 4);
      mServiceArgs.tag = "<service>";
    }
    mServiceArgs.sa = localTypedArray;
    mServiceArgs.flags = paramInt;
    Service localService = new Service(mServiceArgs, new ServiceInfo());
    if (localObject2[0] != null)
    {
      localTypedArray.recycle();
      return null;
    }
    boolean bool1 = localTypedArray.hasValue(5);
    if (bool1) {
      info.exported = localTypedArray.getBoolean(5, false);
    }
    paramCachedComponentArgs = localTypedArray.getNonConfigurationString(3, 0);
    if (paramCachedComponentArgs == null)
    {
      info.permission = applicationInfo.permission;
    }
    else
    {
      localServiceInfo = info;
      if (paramCachedComponentArgs.length() > 0) {
        paramCachedComponentArgs = paramCachedComponentArgs.toString().intern();
      } else {
        paramCachedComponentArgs = null;
      }
      permission = paramCachedComponentArgs;
    }
    info.splitName = localTypedArray.getNonConfigurationString(17, 0);
    info.flags = 0;
    boolean bool2 = localTypedArray.getBoolean(9, false);
    paramInt = 1;
    if (bool2)
    {
      paramCachedComponentArgs = info;
      flags |= 0x1;
    }
    if (localTypedArray.getBoolean(10, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x2;
    }
    if (localTypedArray.getBoolean(14, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x4;
    }
    if (localTypedArray.getBoolean(11, false))
    {
      paramCachedComponentArgs = info;
      flags |= 0x40000000;
    }
    paramCachedComponentArgs = info;
    ServiceInfo localServiceInfo = info;
    bool2 = localTypedArray.getBoolean(13, false);
    directBootAware = bool2;
    encryptionAware = bool2;
    if (info.directBootAware)
    {
      paramCachedComponentArgs = applicationInfo;
      privateFlags |= 0x100;
    }
    bool2 = localTypedArray.getBoolean(16, false);
    if (bool2)
    {
      paramCachedComponentArgs = info;
      flags |= 0x100000;
      visibleToInstantApps = true;
    }
    localTypedArray.recycle();
    if (((applicationInfo.privateFlags & 0x2) != 0) && (info.processName == packageName))
    {
      localObject2[0] = "Heavy-weight applications can not have services in main process";
      return null;
    }
    int i = paramXmlResourceParser.getDepth();
    paramCachedComponentArgs = (CachedComponentArgs)localObject1;
    paramPackage = (Package)localObject2;
    localObject2 = localTypedArray;
    for (;;)
    {
      int j = paramXmlResourceParser.next();
      if (j == paramInt) {
        break;
      }
      if ((j == 3) && (paramXmlResourceParser.getDepth() <= i))
      {
        paramInt = 1;
        break;
      }
      if (j != 3)
      {
        if (j == 4)
        {
          localObject1 = paramPackage;
          paramPackage = paramCachedComponentArgs;
          paramCachedComponentArgs = (CachedComponentArgs)localObject1;
        }
        else if (paramXmlResourceParser.getName().equals("intent-filter"))
        {
          paramPackage = new ServiceIntentInfo(localService);
          if (!parseIntent(paramCachedComponentArgs, localXmlResourceParser, true, false, paramPackage, paramArrayOfString)) {
            return null;
          }
          if (bool2)
          {
            paramPackage.setVisibilityToInstantApp(1);
            paramCachedComponentArgs = info;
            flags |= 0x100000;
          }
          order = Math.max(paramPackage.getOrder(), order);
          intents.add(paramPackage);
          paramCachedComponentArgs = paramArrayOfString;
          paramPackage = paramResources;
        }
        else if (paramXmlResourceParser.getName().equals("meta-data"))
        {
          localObject1 = metaData;
          paramCachedComponentArgs = paramArrayOfString;
          paramPackage = paramResources;
          localObject1 = parseMetaData(paramPackage, localXmlResourceParser, (Bundle)localObject1, paramCachedComponentArgs);
          metaData = ((Bundle)localObject1);
          if (localObject1 == null) {
            return null;
          }
        }
        else
        {
          paramCachedComponentArgs = paramArrayOfString;
          paramPackage = paramResources;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unknown element under <service>: ");
          ((StringBuilder)localObject1).append(paramXmlResourceParser.getName());
          ((StringBuilder)localObject1).append(" at ");
          ((StringBuilder)localObject1).append(mArchiveSourcePath);
          ((StringBuilder)localObject1).append(" ");
          ((StringBuilder)localObject1).append(paramXmlResourceParser.getPositionDescription());
          Slog.w("PackageParser", ((StringBuilder)localObject1).toString());
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
      }
      else
      {
        localObject1 = paramCachedComponentArgs;
        paramCachedComponentArgs = paramPackage;
        paramPackage = (Package)localObject1;
      }
      localObject1 = paramPackage;
      paramPackage = paramCachedComponentArgs;
      paramInt = 1;
      paramCachedComponentArgs = (CachedComponentArgs)localObject1;
    }
    if (!bool1)
    {
      paramPackage = info;
      if (intents.size() <= 0) {
        paramInt = 0;
      }
      exported = paramInt;
    }
    return localService;
  }
  
  private Package parseSplitApk(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt1, int paramInt2, String[] paramArrayOfString)
    throws XmlPullParserException, IOException, PackageParser.PackageParserException
  {
    parsePackageSplitNames(paramXmlResourceParser, paramXmlResourceParser);
    mParseInstrumentationArgs = null;
    int i = 0;
    int j = paramXmlResourceParser.getDepth();
    for (;;)
    {
      int k = paramXmlResourceParser.next();
      if ((k == 1) || ((k == 3) && (paramXmlResourceParser.getDepth() <= j))) {
        break;
      }
      if ((k != 3) && (k != 4)) {
        if (paramXmlResourceParser.getName().equals("application"))
        {
          if (i != 0)
          {
            Slog.w("PackageParser", "<manifest> has more than one <application>");
            XmlUtils.skipCurrentTag(paramXmlResourceParser);
          }
          else
          {
            i = 1;
            if (!parseSplitApplication(paramPackage, paramResources, paramXmlResourceParser, paramInt1, paramInt2, paramArrayOfString)) {
              return null;
            }
          }
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown element under <manifest>: ");
          localStringBuilder.append(paramXmlResourceParser.getName());
          localStringBuilder.append(" at ");
          localStringBuilder.append(mArchiveSourcePath);
          localStringBuilder.append(" ");
          localStringBuilder.append(paramXmlResourceParser.getPositionDescription());
          Slog.w("PackageParser", localStringBuilder.toString());
          XmlUtils.skipCurrentTag(paramXmlResourceParser);
        }
      }
    }
    if (i == 0)
    {
      paramArrayOfString[0] = "<manifest> does not contain an <application>";
      mParseError = -109;
    }
    return paramPackage;
  }
  
  /* Error */
  private void parseSplitApk(Package paramPackage, int paramInt1, AssetManager paramAssetManager, int paramInt2)
    throws PackageParser.PackageParserException
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 664	android/content/pm/PackageParser$Package:splitCodePaths	[Ljava/lang/String;
    //   4: iload_2
    //   5: aaload
    //   6: astore 5
    //   8: aload_0
    //   9: iconst_1
    //   10: putfield 365	android/content/pm/PackageParser:mParseError	I
    //   13: aload_0
    //   14: aload 5
    //   16: putfield 1476	android/content/pm/PackageParser:mArchiveSourcePath	Ljava/lang/String;
    //   19: aconst_null
    //   20: astore 6
    //   22: aconst_null
    //   23: astore 7
    //   25: aconst_null
    //   26: astore 8
    //   28: aload 8
    //   30: astore 9
    //   32: aload_3
    //   33: aload 5
    //   35: invokevirtual 1757	android/content/res/AssetManager:findCookieForPath	(Ljava/lang/String;)I
    //   38: istore 10
    //   40: iload 10
    //   42: ifeq +178 -> 220
    //   45: aload 8
    //   47: astore 9
    //   49: aload_3
    //   50: iload 10
    //   52: ldc 110
    //   54: invokevirtual 1761	android/content/res/AssetManager:openXmlResourceParser	(ILjava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   57: astore 8
    //   59: new 1278	android/content/res/Resources
    //   62: astore 9
    //   64: aload 9
    //   66: aload_3
    //   67: aload_0
    //   68: getfield 370	android/content/pm/PackageParser:mMetrics	Landroid/util/DisplayMetrics;
    //   71: aconst_null
    //   72: invokespecial 1764	android/content/res/Resources:<init>	(Landroid/content/res/AssetManager;Landroid/util/DisplayMetrics;Landroid/content/res/Configuration;)V
    //   75: iconst_1
    //   76: anewarray 327	java/lang/String
    //   79: astore_3
    //   80: aload_0
    //   81: aload_1
    //   82: aload 9
    //   84: aload 8
    //   86: iload 4
    //   88: iload_2
    //   89: aload_3
    //   90: invokespecial 2856	android/content/pm/PackageParser:parseSplitApk	(Landroid/content/pm/PackageParser$Package;Landroid/content/res/Resources;Landroid/content/res/XmlResourceParser;II[Ljava/lang/String;)Landroid/content/pm/PackageParser$Package;
    //   93: astore_1
    //   94: aload_1
    //   95: ifnull +9 -> 104
    //   98: aload 8
    //   100: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   103: return
    //   104: new 50	android/content/pm/PackageParser$PackageParserException
    //   107: astore_1
    //   108: aload_0
    //   109: getfield 365	android/content/pm/PackageParser:mParseError	I
    //   112: istore_2
    //   113: new 436	java/lang/StringBuilder
    //   116: astore 9
    //   118: aload 9
    //   120: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   123: aload 9
    //   125: aload 5
    //   127: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   130: pop
    //   131: aload 9
    //   133: ldc_w 1781
    //   136: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: pop
    //   140: aload 9
    //   142: aload 8
    //   144: invokeinterface 1481 1 0
    //   149: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: pop
    //   153: aload 9
    //   155: ldc_w 1783
    //   158: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: pop
    //   162: aload 9
    //   164: aload_3
    //   165: iconst_0
    //   166: aaload
    //   167: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: pop
    //   171: aload_1
    //   172: iload_2
    //   173: aload 9
    //   175: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   178: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   181: aload_1
    //   182: athrow
    //   183: astore_1
    //   184: goto +12 -> 196
    //   187: astore_1
    //   188: goto +16 -> 204
    //   191: astore_1
    //   192: goto +21 -> 213
    //   195: astore_1
    //   196: aload 8
    //   198: astore 9
    //   200: goto +171 -> 371
    //   203: astore_1
    //   204: aload_1
    //   205: astore_3
    //   206: aload 8
    //   208: astore_1
    //   209: goto +86 -> 295
    //   212: astore_1
    //   213: aload 8
    //   215: astore 9
    //   217: goto +152 -> 369
    //   220: aload 8
    //   222: astore 9
    //   224: new 50	android/content/pm/PackageParser$PackageParserException
    //   227: astore_1
    //   228: aload 8
    //   230: astore 9
    //   232: new 436	java/lang/StringBuilder
    //   235: astore_3
    //   236: aload 8
    //   238: astore 9
    //   240: aload_3
    //   241: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   244: aload 8
    //   246: astore 9
    //   248: aload_3
    //   249: ldc_w 1785
    //   252: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   255: pop
    //   256: aload 8
    //   258: astore 9
    //   260: aload_3
    //   261: aload 5
    //   263: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   266: pop
    //   267: aload 8
    //   269: astore 9
    //   271: aload_1
    //   272: bipush -101
    //   274: aload_3
    //   275: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   278: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   281: aload 8
    //   283: astore 9
    //   285: aload_1
    //   286: athrow
    //   287: astore_1
    //   288: goto +83 -> 371
    //   291: astore_3
    //   292: aload 6
    //   294: astore_1
    //   295: aload_1
    //   296: astore 9
    //   298: new 50	android/content/pm/PackageParser$PackageParserException
    //   301: astore 8
    //   303: aload_1
    //   304: astore 9
    //   306: new 436	java/lang/StringBuilder
    //   309: astore 6
    //   311: aload_1
    //   312: astore 9
    //   314: aload 6
    //   316: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   319: aload_1
    //   320: astore 9
    //   322: aload 6
    //   324: ldc_w 1787
    //   327: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   330: pop
    //   331: aload_1
    //   332: astore 9
    //   334: aload 6
    //   336: aload 5
    //   338: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   341: pop
    //   342: aload_1
    //   343: astore 9
    //   345: aload 8
    //   347: bipush -102
    //   349: aload 6
    //   351: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   354: aload_3
    //   355: invokespecial 1742	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   358: aload_1
    //   359: astore 9
    //   361: aload 8
    //   363: athrow
    //   364: astore_1
    //   365: aload 7
    //   367: astore 9
    //   369: aload_1
    //   370: athrow
    //   371: aload 9
    //   373: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   376: aload_1
    //   377: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	378	0	this	PackageParser
    //   0	378	1	paramPackage	Package
    //   0	378	2	paramInt1	int
    //   0	378	3	paramAssetManager	AssetManager
    //   0	378	4	paramInt2	int
    //   6	331	5	str	String
    //   20	330	6	localStringBuilder	StringBuilder
    //   23	343	7	localObject1	Object
    //   26	336	8	localObject2	Object
    //   30	342	9	localObject3	Object
    //   38	13	10	i	int
    // Exception table:
    //   from	to	target	type
    //   104	183	183	finally
    //   104	183	187	java/lang/Exception
    //   104	183	191	android/content/pm/PackageParser$PackageParserException
    //   59	94	195	finally
    //   59	94	203	java/lang/Exception
    //   59	94	212	android/content/pm/PackageParser$PackageParserException
    //   32	40	287	finally
    //   49	59	287	finally
    //   224	228	287	finally
    //   232	236	287	finally
    //   240	244	287	finally
    //   248	256	287	finally
    //   260	267	287	finally
    //   271	281	287	finally
    //   285	287	287	finally
    //   298	303	287	finally
    //   306	311	287	finally
    //   314	319	287	finally
    //   322	331	287	finally
    //   334	342	287	finally
    //   345	358	287	finally
    //   361	364	287	finally
    //   369	371	287	finally
    //   32	40	291	java/lang/Exception
    //   49	59	291	java/lang/Exception
    //   224	228	291	java/lang/Exception
    //   232	236	291	java/lang/Exception
    //   240	244	291	java/lang/Exception
    //   248	256	291	java/lang/Exception
    //   260	267	291	java/lang/Exception
    //   271	281	291	java/lang/Exception
    //   285	287	291	java/lang/Exception
    //   32	40	364	android/content/pm/PackageParser$PackageParserException
    //   49	59	364	android/content/pm/PackageParser$PackageParserException
    //   224	228	364	android/content/pm/PackageParser$PackageParserException
    //   232	236	364	android/content/pm/PackageParser$PackageParserException
    //   240	244	364	android/content/pm/PackageParser$PackageParserException
    //   248	256	364	android/content/pm/PackageParser$PackageParserException
    //   260	267	364	android/content/pm/PackageParser$PackageParserException
    //   271	281	364	android/content/pm/PackageParser$PackageParserException
    //   285	287	364	android/content/pm/PackageParser$PackageParserException
  }
  
  private boolean parseSplitApplication(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, int paramInt1, int paramInt2, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    PackageParser localPackageParser = this;
    Package localPackage = paramPackage;
    paramPackage = paramResources;
    Object localObject1 = paramXmlResourceParser;
    String[] arrayOfString = paramArrayOfString;
    Object localObject2 = paramPackage.obtainAttributes((AttributeSet)localObject1, R.styleable.AndroidManifestApplication);
    if (((TypedArray)localObject2).getBoolean(7, true))
    {
      localObject3 = splitFlags;
      localObject3[paramInt2] |= 0x4;
    }
    Object localObject3 = ((TypedArray)localObject2).getString(46);
    int i = -108;
    int j = 0;
    if ((localObject3 != null) && (!ClassLoaderFactory.isValidClassLoaderName((String)localObject3)))
    {
      paramPackage = new StringBuilder();
      paramPackage.append("Invalid class loader name: ");
      paramPackage.append((String)localObject3);
      arrayOfString[0] = paramPackage.toString();
      mParseError = -108;
      return false;
    }
    applicationInfo.splitClassLoaderNames[paramInt2] = localObject3;
    int k = paramXmlResourceParser.getDepth();
    label796:
    label825:
    label910:
    for (;;)
    {
      int m = paramXmlResourceParser.next();
      if (m == 1) {
        break;
      }
      if ((m == 3) && (paramXmlResourceParser.getDepth() <= k)) {
        break;
      }
      if ((m != 3) && (m != 4))
      {
        CachedComponentArgs localCachedComponentArgs = new CachedComponentArgs(null);
        String str = paramXmlResourceParser.getName();
        Object localObject4;
        if (str.equals("activity"))
        {
          localObject2 = localPackageParser.parseActivity(localPackage, paramPackage, (XmlResourceParser)localObject1, paramInt1, arrayOfString, localCachedComponentArgs, false, baseHardwareAccelerated);
          if (localObject2 == null)
          {
            mParseError = i;
            return false;
          }
          j = 0;
          activities.add(localObject2);
          localObject2 = info;
        }
        else
        {
          if (str.equals("receiver"))
          {
            paramPackage = localPackageParser.parseActivity(localPackage, paramPackage, (XmlResourceParser)localObject1, paramInt1, paramArrayOfString, localCachedComponentArgs, true, false);
            if (paramPackage == null)
            {
              mParseError = i;
              return j;
            }
            receivers.add(paramPackage);
            paramPackage = info;
          }
          for (;;)
          {
            localObject4 = paramResources;
            localObject1 = paramXmlResourceParser;
            arrayOfString = paramArrayOfString;
            localObject2 = paramPackage;
            paramPackage = (Package)localObject4;
            break label796;
            m = i;
            localObject2 = localPackage;
            localObject4 = localPackageParser;
            if (str.equals("service"))
            {
              paramPackage = ((PackageParser)localObject4).parseService((Package)localObject2, paramResources, paramXmlResourceParser, paramInt1, paramArrayOfString, localCachedComponentArgs);
              if (paramPackage == null)
              {
                mParseError = m;
                return j;
              }
              services.add(paramPackage);
              paramPackage = info;
            }
            else if (str.equals("provider"))
            {
              paramPackage = ((PackageParser)localObject4).parseProvider((Package)localObject2, paramResources, paramXmlResourceParser, paramInt1, paramArrayOfString, localCachedComponentArgs);
              if (paramPackage == null)
              {
                mParseError = m;
                return j;
              }
              providers.add(paramPackage);
              paramPackage = info;
            }
            else
            {
              if (!str.equals("activity-alias")) {
                break;
              }
              paramPackage = ((PackageParser)localObject4).parseActivityAlias((Package)localObject2, paramResources, paramXmlResourceParser, paramInt1, paramArrayOfString, localCachedComponentArgs);
              if (paramPackage == null)
              {
                mParseError = m;
                return j;
              }
              activities.add(paramPackage);
              paramPackage = info;
            }
          }
          if (paramXmlResourceParser.getName().equals("meta-data"))
          {
            paramPackage = ((PackageParser)localObject4).parseMetaData(paramResources, paramXmlResourceParser, mAppMetaData, paramArrayOfString);
            mAppMetaData = paramPackage;
            if (paramPackage == null)
            {
              mParseError = m;
              return j;
            }
          }
          else
          {
            paramPackage = paramResources;
            localObject1 = paramXmlResourceParser;
            arrayOfString = paramArrayOfString;
            if (str.equals("uses-static-library"))
            {
              if (!((PackageParser)localObject4).parseUsesStaticLibrary((Package)localObject2, paramPackage, (XmlResourceParser)localObject1, arrayOfString)) {
                return j;
              }
            }
            else if (str.equals("uses-library"))
            {
              localObject1 = paramPackage.obtainAttributes((AttributeSet)localObject1, R.styleable.AndroidManifestUsesLibrary);
              paramPackage = ((TypedArray)localObject1).getNonResourceString(j);
              boolean bool = ((TypedArray)localObject1).getBoolean(1, true);
              ((TypedArray)localObject1).recycle();
              if (paramPackage != null)
              {
                paramPackage = paramPackage.intern();
                if (bool)
                {
                  usesLibraries = ArrayUtils.add(usesLibraries, paramPackage);
                  usesOptionalLibraries = ArrayUtils.remove(usesOptionalLibraries, paramPackage);
                }
                else if (!ArrayUtils.contains(usesLibraries, paramPackage))
                {
                  usesOptionalLibraries = ArrayUtils.add(usesOptionalLibraries, paramPackage);
                }
              }
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
            }
            else
            {
              if (!str.equals("uses-package")) {
                break label825;
              }
              XmlUtils.skipCurrentTag(paramXmlResourceParser);
            }
          }
          arrayOfString = paramArrayOfString;
          localObject1 = paramXmlResourceParser;
          paramPackage = paramResources;
          localObject2 = null;
        }
        if ((localObject2 != null) && (splitName == null)) {
          splitName = splitNames[paramInt2];
        }
        break label910;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Unknown element under <application>: ");
        ((StringBuilder)localObject2).append(str);
        ((StringBuilder)localObject2).append(" at ");
        ((StringBuilder)localObject2).append(mArchiveSourcePath);
        ((StringBuilder)localObject2).append(" ");
        ((StringBuilder)localObject2).append(paramXmlResourceParser.getPositionDescription());
        Slog.w("PackageParser", ((StringBuilder)localObject2).toString());
        XmlUtils.skipCurrentTag(paramXmlResourceParser);
      }
    }
    return true;
  }
  
  private FeatureInfo parseUsesFeature(Resources paramResources, AttributeSet paramAttributeSet)
  {
    FeatureInfo localFeatureInfo = new FeatureInfo();
    paramResources = paramResources.obtainAttributes(paramAttributeSet, R.styleable.AndroidManifestUsesFeature);
    name = paramResources.getNonResourceString(0);
    version = paramResources.getInt(3, 0);
    if (name == null) {
      reqGlEsVersion = paramResources.getInt(1, 0);
    }
    if (paramResources.getBoolean(2, true)) {
      flags |= 0x1;
    }
    paramResources.recycle();
    return localFeatureInfo;
  }
  
  private boolean parseUsesPermission(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestUsesPermission);
    paramResources = ((TypedArray)localObject1).getNonResourceString(0);
    int i = 0;
    Object localObject2 = ((TypedArray)localObject1).peekValue(1);
    int j = i;
    if (localObject2 != null)
    {
      j = i;
      if (type >= 16)
      {
        j = i;
        if (type <= 31) {
          j = data;
        }
      }
    }
    localObject2 = ((TypedArray)localObject1).getNonConfigurationString(2, 0);
    String str = ((TypedArray)localObject1).getNonConfigurationString(3, 0);
    ((TypedArray)localObject1).recycle();
    XmlUtils.skipCurrentTag(paramXmlResourceParser);
    if (paramResources == null) {
      return true;
    }
    if ((j != 0) && (j < Build.VERSION.RESOURCES_SDK_INT)) {
      return true;
    }
    if ((localObject2 != null) && (mCallback != null) && (!mCallback.hasFeature((String)localObject2))) {
      return true;
    }
    if ((str != null) && (mCallback != null) && (mCallback.hasFeature(str))) {
      return true;
    }
    if (requestedPermissions.indexOf(paramResources) == -1)
    {
      requestedPermissions.add(paramResources.intern());
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Ignoring duplicate uses-permissions/uses-permissions-sdk-m: ");
      ((StringBuilder)localObject1).append(paramResources);
      ((StringBuilder)localObject1).append(" in package: ");
      ((StringBuilder)localObject1).append(packageName);
      ((StringBuilder)localObject1).append(" at: ");
      ((StringBuilder)localObject1).append(paramXmlResourceParser.getPositionDescription());
      Slog.w("PackageParser", ((StringBuilder)localObject1).toString());
    }
    return true;
  }
  
  private boolean parseUsesStaticLibrary(Package paramPackage, Resources paramResources, XmlResourceParser paramXmlResourceParser, String[] paramArrayOfString)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = paramResources.obtainAttributes(paramXmlResourceParser, R.styleable.AndroidManifestUsesStaticLibrary);
    Object localObject2 = ((TypedArray)localObject1).getNonResourceString(0);
    int i = ((TypedArray)localObject1).getInt(1, -1);
    String str = ((TypedArray)localObject1).getNonResourceString(2);
    ((TypedArray)localObject1).recycle();
    if ((localObject2 != null) && (i >= 0) && (str != null))
    {
      if ((usesStaticLibraries != null) && (usesStaticLibraries.contains(localObject2)))
      {
        paramPackage = new StringBuilder();
        paramPackage.append("Depending on multiple versions of static library ");
        paramPackage.append((String)localObject2);
        paramArrayOfString[0] = paramPackage.toString();
        mParseError = -108;
        XmlUtils.skipCurrentTag(paramXmlResourceParser);
        return false;
      }
      localObject1 = ((String)localObject2).intern();
      str = str.replace(":", "").toLowerCase();
      localObject2 = EmptyArray.STRING;
      if (applicationInfo.targetSdkVersion >= 27)
      {
        paramXmlResourceParser = parseAdditionalCertificates(paramResources, paramXmlResourceParser, paramArrayOfString);
        paramResources = paramXmlResourceParser;
        if (paramXmlResourceParser == null) {
          return false;
        }
      }
      else
      {
        XmlUtils.skipCurrentTag(paramXmlResourceParser);
        paramResources = (Resources)localObject2;
      }
      paramXmlResourceParser = new String[paramResources.length + 1];
      paramXmlResourceParser[0] = str;
      System.arraycopy(paramResources, 0, paramXmlResourceParser, 1, paramResources.length);
      usesStaticLibraries = ArrayUtils.add(usesStaticLibraries, localObject1);
      usesStaticLibrariesVersions = ArrayUtils.appendLong(usesStaticLibrariesVersions, i, true);
      usesStaticLibrariesCertDigests = ((String[][])ArrayUtils.appendElement([Ljava.lang.String.class, usesStaticLibrariesCertDigests, paramXmlResourceParser, true));
      return true;
    }
    paramPackage = new StringBuilder();
    paramPackage.append("Bad uses-static-library declaration name: ");
    paramPackage.append((String)localObject2);
    paramPackage.append(" version: ");
    paramPackage.append(i);
    paramPackage.append(" certDigest");
    paramPackage.append(str);
    paramArrayOfString[0] = paramPackage.toString();
    mParseError = -108;
    XmlUtils.skipCurrentTag(paramXmlResourceParser);
    return false;
  }
  
  private static VerifierInfo parseVerifier(AttributeSet paramAttributeSet)
  {
    String str1 = null;
    String str2 = null;
    int i = paramAttributeSet.getAttributeCount();
    for (int j = 0; j < i; j++)
    {
      int k = paramAttributeSet.getAttributeNameResource(j);
      if (k != 16842755)
      {
        if (k == 16843686) {
          str2 = paramAttributeSet.getAttributeValue(j);
        }
      }
      else {
        str1 = paramAttributeSet.getAttributeValue(j);
      }
    }
    if ((str1 != null) && (str1.length() != 0))
    {
      paramAttributeSet = parsePublicKey(str2);
      if (paramAttributeSet == null)
      {
        paramAttributeSet = new StringBuilder();
        paramAttributeSet.append("Unable to parse verifier public key for ");
        paramAttributeSet.append(str1);
        Slog.i("PackageParser", paramAttributeSet.toString());
        return null;
      }
      return new VerifierInfo(str1, paramAttributeSet);
    }
    Slog.i("PackageParser", "verifier package name was null; skipping");
    return null;
  }
  
  private void setActivityResizeMode(ActivityInfo paramActivityInfo, TypedArray paramTypedArray, Package paramPackage)
  {
    int i = applicationInfo.privateFlags;
    boolean bool = true;
    if ((i & 0xC00) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if ((!paramTypedArray.hasValue(40)) && (i == 0))
    {
      if ((applicationInfo.privateFlags & 0x1000) != 0)
      {
        resizeMode = 1;
        return;
      }
      if (paramActivityInfo.isFixedOrientationPortrait()) {
        resizeMode = 6;
      } else if (paramActivityInfo.isFixedOrientationLandscape()) {
        resizeMode = 5;
      } else if (paramActivityInfo.isFixedOrientation()) {
        resizeMode = 7;
      } else {
        resizeMode = 4;
      }
      return;
    }
    if ((applicationInfo.privateFlags & 0x400) == 0) {
      bool = false;
    }
    if (paramTypedArray.getBoolean(40, bool)) {
      resizeMode = 2;
    } else {
      resizeMode = 0;
    }
  }
  
  public static void setCompatibilityModeEnabled(boolean paramBoolean)
  {
    sCompatibilityModeEnabled = paramBoolean;
  }
  
  private void setMaxAspectRatio(Package paramPackage)
  {
    float f1;
    if (applicationInfo.targetSdkVersion < 26) {
      f1 = 1.86F;
    } else {
      f1 = 0.0F;
    }
    float f2;
    if (applicationInfo.maxAspectRatio != 0.0F)
    {
      f2 = applicationInfo.maxAspectRatio;
    }
    else
    {
      f2 = f1;
      if (mAppMetaData != null)
      {
        f2 = f1;
        if (mAppMetaData.containsKey("android.max_aspect")) {
          f2 = mAppMetaData.getFloat("android.max_aspect", f1);
        }
      }
    }
    paramPackage = activities.iterator();
    while (paramPackage.hasNext())
    {
      Activity localActivity = (Activity)paramPackage.next();
      if (!localActivity.hasMaxAspectRatio())
      {
        if (metaData != null) {
          f1 = metaData.getFloat("android.max_aspect", f2);
        } else {
          f1 = f2;
        }
        localActivity.setMaxAspectRatio(f1);
      }
    }
  }
  
  @VisibleForTesting
  public static byte[] toCacheEntryStatic(Package paramPackage)
  {
    Parcel localParcel = Parcel.obtain();
    PackageParserCacheHelper.WriteHelper localWriteHelper = new PackageParserCacheHelper.WriteHelper(localParcel);
    paramPackage.writeToParcel(localParcel, 0);
    localWriteHelper.finishAndUninstall();
    paramPackage = localParcel.marshall();
    localParcel.recycle();
    return paramPackage;
  }
  
  public static ArraySet<PublicKey> toSigningKeys(Signature[] paramArrayOfSignature)
    throws CertificateException
  {
    ArraySet localArraySet = new ArraySet(paramArrayOfSignature.length);
    for (int i = 0; i < paramArrayOfSignature.length; i++) {
      localArraySet.add(paramArrayOfSignature[i].getPublicKey());
    }
    return localArraySet;
  }
  
  private static void updateApplicationInfo(ApplicationInfo paramApplicationInfo, int paramInt, PackageUserState paramPackageUserState)
  {
    if (!sCompatibilityModeEnabled) {
      paramApplicationInfo.disableCompatibilityMode();
    }
    if (installed) {
      flags |= 0x800000;
    } else {
      flags &= 0xFF7FFFFF;
    }
    if (suspended) {
      flags |= 0x40000000;
    } else {
      flags &= 0xBFFFFFFF;
    }
    if (instantApp) {
      privateFlags |= 0x80;
    } else {
      privateFlags &= 0xFF7F;
    }
    if (virtualPreload) {
      privateFlags |= 0x10000;
    } else {
      privateFlags &= 0xFFFEFFFF;
    }
    boolean bool1 = hidden;
    boolean bool2 = true;
    if (bool1) {
      privateFlags |= 0x1;
    } else {
      privateFlags &= 0xFFFFFFFE;
    }
    if (enabled == 1)
    {
      enabled = true;
    }
    else if (enabled == 4)
    {
      if ((0x8000 & paramInt) == 0) {
        bool2 = false;
      }
      enabled = bool2;
    }
    else if ((enabled == 2) || (enabled == 3))
    {
      enabled = false;
    }
    enabledSetting = enabled;
    if (category == -1) {
      category = categoryHint;
    }
    if (category == -1) {
      category = FallbackCategoryProvider.getFallbackCategory(packageName);
    }
    seInfoUser = SELinuxUtil.assignSeinfoUser(paramPackageUserState);
    resourceDirs = overlayPaths;
    if (Build.FEATURES.ENABLE_APP_SCALING) {
      overrideMaxAspect = overrideMaxAspect;
    }
    if (Build.FEATURES.ENABLE_NOTCH_UI) {
      fillNotchRegion = AspectRatioChecker.getInstance().isFillNotchRegion(packageName, paramPackageUserState);
    }
  }
  
  private static String validateName(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = paramString.length();
    int j = 0;
    int k = 1;
    int m = 0;
    while (m < i)
    {
      char c = paramString.charAt(m);
      int n;
      int i1;
      if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')))
      {
        n = 0;
        i1 = j;
      }
      else
      {
        if (k == 0)
        {
          if (c >= '0')
          {
            i1 = j;
            n = k;
            if (c <= '9') {
              break label124;
            }
          }
          if (c == '_')
          {
            i1 = j;
            n = k;
            break label124;
          }
        }
        if (c != '.') {
          break label138;
        }
        i1 = 1;
        n = 1;
      }
      label124:
      m++;
      j = i1;
      k = n;
      continue;
      label138:
      paramString = new StringBuilder();
      paramString.append("bad character '");
      paramString.append(c);
      paramString.append("'");
      return paramString.toString();
    }
    if ((paramBoolean2) && (!FileUtils.isValidExtFilename(paramString))) {
      return "Invalid filename";
    }
    if ((j == 0) && (paramBoolean1)) {
      paramString = "must have at least one '.' separator";
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  @VisibleForTesting
  protected Package fromCacheEntry(byte[] paramArrayOfByte)
  {
    return fromCacheEntryStatic(paramArrayOfByte);
  }
  
  /* Error */
  @Deprecated
  public Package parseMonolithicPackage(File paramFile, int paramInt)
    throws PackageParser.PackageParserException
  {
    // Byte code:
    //   0: aload_1
    //   1: iload_2
    //   2: invokestatic 2618	android/content/pm/PackageParser:parseMonolithicPackageLite	(Ljava/io/File;I)Landroid/content/pm/PackageParser$PackageLite;
    //   5: astore_3
    //   6: aload_0
    //   7: getfield 2232	android/content/pm/PackageParser:mOnlyCoreApps	Z
    //   10: ifeq +53 -> 63
    //   13: aload_3
    //   14: getfield 2233	android/content/pm/PackageParser$PackageLite:coreApp	Z
    //   17: ifeq +6 -> 23
    //   20: goto +43 -> 63
    //   23: new 436	java/lang/StringBuilder
    //   26: dup
    //   27: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   30: astore 4
    //   32: aload 4
    //   34: ldc_w 2235
    //   37: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: pop
    //   41: aload 4
    //   43: aload_1
    //   44: invokevirtual 524	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   47: pop
    //   48: new 50	android/content/pm/PackageParser$PackageParserException
    //   51: dup
    //   52: bipush -108
    //   54: aload 4
    //   56: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   59: invokespecial 632	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   62: athrow
    //   63: new 2252	android/content/pm/split/DefaultSplitAssetLoader
    //   66: dup
    //   67: aload_3
    //   68: iload_2
    //   69: invokespecial 2255	android/content/pm/split/DefaultSplitAssetLoader:<init>	(Landroid/content/pm/PackageParser$PackageLite;I)V
    //   72: astore 4
    //   74: aload_0
    //   75: aload_1
    //   76: aload 4
    //   78: invokeinterface 2260 1 0
    //   83: iload_2
    //   84: invokespecial 2263	android/content/pm/PackageParser:parseBaseApk	(Ljava/io/File;Landroid/content/res/AssetManager;I)Landroid/content/pm/PackageParser$Package;
    //   87: astore 5
    //   89: aload 5
    //   91: aload_1
    //   92: invokevirtual 2290	java/io/File:getCanonicalPath	()Ljava/lang/String;
    //   95: invokevirtual 2293	android/content/pm/PackageParser$Package:setCodePath	(Ljava/lang/String;)V
    //   98: aload 5
    //   100: aload_3
    //   101: getfield 2295	android/content/pm/PackageParser$PackageLite:use32bitAbi	Z
    //   104: invokevirtual 2299	android/content/pm/PackageParser$Package:setUse32bitAbi	(Z)V
    //   107: aload 4
    //   109: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   112: aload 5
    //   114: areturn
    //   115: astore_1
    //   116: goto +48 -> 164
    //   119: astore 6
    //   121: new 50	android/content/pm/PackageParser$PackageParserException
    //   124: astore 5
    //   126: new 436	java/lang/StringBuilder
    //   129: astore_3
    //   130: aload_3
    //   131: invokespecial 437	java/lang/StringBuilder:<init>	()V
    //   134: aload_3
    //   135: ldc_w 2303
    //   138: invokevirtual 441	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: aload_3
    //   143: aload_1
    //   144: invokevirtual 524	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: aload 5
    //   150: bipush -102
    //   152: aload_3
    //   153: invokevirtual 442	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   156: aload 6
    //   158: invokespecial 1742	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   161: aload 5
    //   163: athrow
    //   164: aload 4
    //   166: invokestatic 1737	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   169: aload_1
    //   170: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	171	0	this	PackageParser
    //   0	171	1	paramFile	File
    //   0	171	2	paramInt	int
    //   5	148	3	localObject1	Object
    //   30	135	4	localObject2	Object
    //   87	75	5	localObject3	Object
    //   119	38	6	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   74	107	115	finally
    //   121	164	115	finally
    //   74	107	119	java/io/IOException
  }
  
  public Package parsePackage(File paramFile, int paramInt)
    throws PackageParser.PackageParserException
  {
    return parsePackage(paramFile, paramInt, false);
  }
  
  public Package parsePackage(File paramFile, int paramInt, boolean paramBoolean)
    throws PackageParser.PackageParserException
  {
    Package localPackage;
    if (paramBoolean) {
      localPackage = getCachedResult(paramFile, paramInt);
    } else {
      localPackage = null;
    }
    if (localPackage != null) {
      return localPackage;
    }
    paramBoolean = LOG_PARSE_TIMINGS;
    long l1 = 0L;
    long l2;
    if (paramBoolean) {
      l2 = SystemClock.uptimeMillis();
    } else {
      l2 = 0L;
    }
    if (paramFile.isDirectory()) {
      localPackage = parseClusterPackage(paramFile, paramInt);
    } else {
      localPackage = parseMonolithicPackage(paramFile, paramInt);
    }
    if (LOG_PARSE_TIMINGS) {
      l1 = SystemClock.uptimeMillis();
    }
    cacheResult(paramFile, paramInt, localPackage);
    if (LOG_PARSE_TIMINGS)
    {
      l2 = l1 - l2;
      l1 = SystemClock.uptimeMillis() - l1;
      if (l2 + l1 > 100L)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Parse times for '");
        localStringBuilder.append(paramFile);
        localStringBuilder.append("': parse=");
        localStringBuilder.append(l2);
        localStringBuilder.append("ms, update_cache=");
        localStringBuilder.append(l1);
        localStringBuilder.append(" ms");
        Slog.i("PackageParser", localStringBuilder.toString());
      }
    }
    return localPackage;
  }
  
  public void setCacheDir(File paramFile)
  {
    mCacheDir = paramFile;
  }
  
  public void setCallback(Callback paramCallback)
  {
    mCallback = paramCallback;
  }
  
  public void setDisplayMetrics(DisplayMetrics paramDisplayMetrics)
  {
    mMetrics = paramDisplayMetrics;
  }
  
  public void setOnlyCoreApps(boolean paramBoolean)
  {
    mOnlyCoreApps = paramBoolean;
  }
  
  public void setSeparateProcesses(String[] paramArrayOfString)
  {
    mSeparateProcesses = paramArrayOfString;
  }
  
  @VisibleForTesting
  protected byte[] toCacheEntry(Package paramPackage)
  {
    return toCacheEntryStatic(paramPackage);
  }
  
  public static final class Activity
    extends PackageParser.Component<PackageParser.ActivityIntentInfo>
    implements Parcelable
  {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
      public PackageParser.Activity createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageParser.Activity(paramAnonymousParcel, null);
      }
      
      public PackageParser.Activity[] newArray(int paramAnonymousInt)
      {
        return new PackageParser.Activity[paramAnonymousInt];
      }
    };
    public final ActivityInfo info;
    private boolean mHasMaxAspectRatio;
    
    public Activity(PackageParser.ParseComponentArgs paramParseComponentArgs, ActivityInfo paramActivityInfo)
    {
      super(paramActivityInfo);
      info = paramActivityInfo;
      info.applicationInfo = owner.applicationInfo;
    }
    
    private Activity(Parcel paramParcel)
    {
      super();
      info = ((ActivityInfo)paramParcel.readParcelable(Object.class.getClassLoader()));
      mHasMaxAspectRatio = paramParcel.readBoolean();
      Iterator localIterator = intents.iterator();
      while (localIterator.hasNext())
      {
        paramParcel = (PackageParser.ActivityIntentInfo)localIterator.next();
        activity = this;
        order = Math.max(paramParcel.getOrder(), order);
      }
      if (info.permission != null) {
        info.permission = info.permission.intern();
      }
    }
    
    private boolean hasMaxAspectRatio()
    {
      return mHasMaxAspectRatio;
    }
    
    private void setMaxAspectRatio(float paramFloat)
    {
      if ((info.resizeMode != 2) && (info.resizeMode != 1))
      {
        if ((paramFloat < 1.0F) && (paramFloat != 0.0F)) {
          return;
        }
        info.maxAspectRatio = paramFloat;
        mHasMaxAspectRatio = true;
        return;
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void setPackageName(String paramString)
    {
      super.setPackageName(paramString);
      info.packageName = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("Activity{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(' ');
      appendComponentShortName(localStringBuilder);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeParcelable(info, paramInt | 0x2);
      paramParcel.writeBoolean(mHasMaxAspectRatio);
    }
  }
  
  public static final class ActivityIntentInfo
    extends PackageParser.IntentInfo
  {
    public PackageParser.Activity activity;
    
    public ActivityIntentInfo(PackageParser.Activity paramActivity)
    {
      activity = paramActivity;
    }
    
    public ActivityIntentInfo(Parcel paramParcel)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("ActivityIntentInfo{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(' ');
      activity.appendComponentShortName(localStringBuilder);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
  
  public static class ApkLite
  {
    public final String codePath;
    public final String configForSplit;
    public final boolean coreApp;
    public final boolean debuggable;
    public final boolean extractNativeLibs;
    public final int installLocation;
    public boolean isFeatureSplit;
    public final boolean isolatedSplits;
    public final boolean multiArch;
    public final String packageName;
    public final int revisionCode;
    public final PackageParser.SigningDetails signingDetails;
    public final String splitName;
    public final boolean use32bitAbi;
    public final String usesSplitName;
    public final VerifierInfo[] verifiers;
    public final int versionCode;
    public final int versionCodeMajor;
    
    public ApkLite(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, String paramString4, String paramString5, int paramInt1, int paramInt2, int paramInt3, int paramInt4, List<VerifierInfo> paramList, PackageParser.SigningDetails paramSigningDetails, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7)
    {
      codePath = paramString1;
      packageName = paramString2;
      splitName = paramString3;
      isFeatureSplit = paramBoolean1;
      configForSplit = paramString4;
      usesSplitName = paramString5;
      versionCode = paramInt1;
      versionCodeMajor = paramInt2;
      revisionCode = paramInt3;
      installLocation = paramInt4;
      signingDetails = paramSigningDetails;
      verifiers = ((VerifierInfo[])paramList.toArray(new VerifierInfo[paramList.size()]));
      coreApp = paramBoolean2;
      debuggable = paramBoolean3;
      multiArch = paramBoolean4;
      use32bitAbi = paramBoolean5;
      extractNativeLibs = paramBoolean6;
      isolatedSplits = paramBoolean7;
    }
    
    public long getLongVersionCode()
    {
      return PackageInfo.composeLongVersionCode(versionCodeMajor, versionCode);
    }
  }
  
  private static class CachedComponentArgs
  {
    PackageParser.ParseComponentArgs mActivityAliasArgs;
    PackageParser.ParseComponentArgs mActivityArgs;
    PackageParser.ParseComponentArgs mProviderArgs;
    PackageParser.ParseComponentArgs mServiceArgs;
    
    private CachedComponentArgs() {}
  }
  
  public static abstract interface Callback
  {
    public abstract String[] getOverlayApks(String paramString);
    
    public abstract String[] getOverlayPaths(String paramString1, String paramString2);
    
    public abstract boolean hasFeature(String paramString);
  }
  
  public static final class CallbackImpl
    implements PackageParser.Callback
  {
    private final PackageManager mPm;
    
    public CallbackImpl(PackageManager paramPackageManager)
    {
      mPm = paramPackageManager;
    }
    
    public String[] getOverlayApks(String paramString)
    {
      return null;
    }
    
    public String[] getOverlayPaths(String paramString1, String paramString2)
    {
      return null;
    }
    
    public boolean hasFeature(String paramString)
    {
      return mPm.hasSystemFeature(paramString);
    }
  }
  
  public static abstract class Component<II extends PackageParser.IntentInfo>
  {
    public final String className;
    ComponentName componentName;
    String componentShortName;
    public final ArrayList<II> intents;
    public Bundle metaData;
    public int order;
    public PackageParser.Package owner;
    
    public Component(Component<II> paramComponent)
    {
      owner = owner;
      intents = intents;
      className = className;
      componentName = componentName;
      componentShortName = componentShortName;
    }
    
    public Component(PackageParser.Package paramPackage)
    {
      owner = paramPackage;
      intents = null;
      className = null;
    }
    
    public Component(PackageParser.ParseComponentArgs paramParseComponentArgs, ComponentInfo paramComponentInfo)
    {
      this(paramParseComponentArgs, paramComponentInfo);
      if (outError[0] != null) {
        return;
      }
      if (processRes != 0)
      {
        if (owner.applicationInfo.targetSdkVersion >= 8) {}
        for (String str = sa.getNonConfigurationString(processRes, 1024);; str = sa.getNonResourceString(processRes)) {
          break;
        }
        processName = PackageParser.buildProcessName(owner.applicationInfo.packageName, owner.applicationInfo.processName, str, flags, sepProcesses, outError);
      }
      if (descriptionRes != 0) {
        descriptionRes = sa.getResourceId(descriptionRes, 0);
      }
      enabled = sa.getBoolean(enabledRes, true);
    }
    
    public Component(PackageParser.ParsePackageItemArgs paramParsePackageItemArgs, PackageItemInfo paramPackageItemInfo)
    {
      owner = owner;
      intents = new ArrayList(0);
      if (PackageParser.parsePackageItemInfo(owner, paramPackageItemInfo, outError, tag, sa, true, nameRes, labelRes, iconRes, roundIconRes, logoRes, bannerRes)) {
        className = name;
      } else {
        className = null;
      }
    }
    
    protected Component(Parcel paramParcel)
    {
      className = paramParcel.readString();
      metaData = paramParcel.readBundle();
      intents = createIntentsList(paramParcel);
      owner = null;
    }
    
    private static <T extends PackageParser.IntentInfo> ArrayList<T> createIntentsList(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      if (i == -1) {
        return null;
      }
      if (i == 0) {
        return new ArrayList(0);
      }
      String str = paramParcel.readString();
      try
      {
        Constructor localConstructor = Class.forName(str).getConstructor(new Class[] { Parcel.class });
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>(i);
        for (int j = 0; j < i; j++) {
          localArrayList.add((PackageParser.IntentInfo)localConstructor.newInstance(new Object[] { paramParcel }));
        }
        return localArrayList;
      }
      catch (ReflectiveOperationException paramParcel)
      {
        paramParcel = new StringBuilder();
        paramParcel.append("Unable to construct intent list for: ");
        paramParcel.append(str);
        throw new AssertionError(paramParcel.toString());
      }
    }
    
    private static void writeIntentsList(ArrayList<? extends PackageParser.IntentInfo> paramArrayList, Parcel paramParcel, int paramInt)
    {
      if (paramArrayList == null)
      {
        paramParcel.writeInt(-1);
        return;
      }
      int i = paramArrayList.size();
      paramParcel.writeInt(i);
      if (i > 0)
      {
        int j = 0;
        paramParcel.writeString(((PackageParser.IntentInfo)paramArrayList.get(0)).getClass().getName());
        while (j < i)
        {
          ((PackageParser.IntentInfo)paramArrayList.get(j)).writeIntentInfoToParcel(paramParcel, paramInt);
          j++;
        }
      }
    }
    
    public void appendComponentShortName(StringBuilder paramStringBuilder)
    {
      ComponentName.appendShortString(paramStringBuilder, owner.applicationInfo.packageName, className);
    }
    
    public ComponentName getComponentName()
    {
      if (componentName != null) {
        return componentName;
      }
      if (className != null) {
        componentName = new ComponentName(owner.applicationInfo.packageName, className);
      }
      return componentName;
    }
    
    public void printComponentShortName(PrintWriter paramPrintWriter)
    {
      ComponentName.printShortString(paramPrintWriter, owner.applicationInfo.packageName, className);
    }
    
    public void setPackageName(String paramString)
    {
      componentName = null;
      componentShortName = null;
    }
    
    protected void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(className);
      paramParcel.writeBundle(metaData);
      writeIntentsList(intents, paramParcel, paramInt);
    }
  }
  
  public static final class Instrumentation
    extends PackageParser.Component<PackageParser.IntentInfo>
    implements Parcelable
  {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
      public PackageParser.Instrumentation createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageParser.Instrumentation(paramAnonymousParcel, null);
      }
      
      public PackageParser.Instrumentation[] newArray(int paramAnonymousInt)
      {
        return new PackageParser.Instrumentation[paramAnonymousInt];
      }
    };
    public final InstrumentationInfo info;
    
    public Instrumentation(PackageParser.ParsePackageItemArgs paramParsePackageItemArgs, InstrumentationInfo paramInstrumentationInfo)
    {
      super(paramInstrumentationInfo);
      info = paramInstrumentationInfo;
    }
    
    private Instrumentation(Parcel paramParcel)
    {
      super();
      info = ((InstrumentationInfo)paramParcel.readParcelable(Object.class.getClassLoader()));
      if (info.targetPackage != null) {
        info.targetPackage = info.targetPackage.intern();
      }
      if (info.targetProcesses != null) {
        info.targetProcesses = info.targetProcesses.intern();
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void setPackageName(String paramString)
    {
      super.setPackageName(paramString);
      info.packageName = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("Instrumentation{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(' ');
      appendComponentShortName(localStringBuilder);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeParcelable(info, paramInt);
    }
  }
  
  public static abstract class IntentInfo
    extends IntentFilter
  {
    public int banner;
    public boolean hasDefault;
    public int icon;
    public int labelRes;
    public int logo;
    public CharSequence nonLocalizedLabel;
    public int preferred;
    
    protected IntentInfo() {}
    
    protected IntentInfo(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      hasDefault = bool;
      labelRes = paramParcel.readInt();
      nonLocalizedLabel = paramParcel.readCharSequence();
      icon = paramParcel.readInt();
      logo = paramParcel.readInt();
      banner = paramParcel.readInt();
      preferred = paramParcel.readInt();
    }
    
    public void writeIntentInfoToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(hasDefault);
      paramParcel.writeInt(labelRes);
      paramParcel.writeCharSequence(nonLocalizedLabel);
      paramParcel.writeInt(icon);
      paramParcel.writeInt(logo);
      paramParcel.writeInt(banner);
      paramParcel.writeInt(preferred);
    }
  }
  
  public static class NewPermissionInfo
  {
    public final int fileVersion;
    public final String name;
    public final int sdkVersion;
    
    public NewPermissionInfo(String paramString, int paramInt1, int paramInt2)
    {
      name = paramString;
      sdkVersion = paramInt1;
      fileVersion = paramInt2;
    }
  }
  
  public static final class Package
    implements Parcelable
  {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
      public PackageParser.Package createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageParser.Package(paramAnonymousParcel);
      }
      
      public PackageParser.Package[] newArray(int paramAnonymousInt)
      {
        return new PackageParser.Package[paramAnonymousInt];
      }
    };
    public final ArrayList<PackageParser.Activity> activities;
    public ApplicationInfo applicationInfo = new ApplicationInfo();
    public String baseCodePath;
    public boolean baseHardwareAccelerated;
    public int baseRevisionCode;
    public ArrayList<Package> childPackages;
    public String codePath;
    public ArrayList<ConfigurationInfo> configPreferences;
    public boolean coreApp;
    public String cpuAbiOverride;
    public ArrayList<FeatureGroupInfo> featureGroups;
    public int installLocation;
    public final ArrayList<PackageParser.Instrumentation> instrumentation;
    public boolean isStub;
    public ArrayList<String> libraryNames;
    public ArrayList<String> mAdoptPermissions;
    public Bundle mAppMetaData;
    public int mCompileSdkVersion;
    public String mCompileSdkVersionCodename;
    public Object mExtras;
    public ArrayMap<String, ArraySet<PublicKey>> mKeySetMapping;
    public long[] mLastPackageUsageTimeInMills;
    public ArrayList<String> mOriginalPackages;
    public String mOverlayCategory;
    public boolean mOverlayIsStatic;
    public int mOverlayPriority;
    public String mOverlayTarget;
    public int mPreferredOrder;
    public String mRealPackage;
    public String mRequiredAccountType;
    public boolean mRequiredForAllUsers;
    public String mRestrictedAccountType;
    public String mSharedUserId;
    public int mSharedUserLabel;
    public PackageParser.SigningDetails mSigningDetails;
    public ArraySet<String> mUpgradeKeySets;
    public int mVersionCode;
    public int mVersionCodeMajor;
    public String mVersionName;
    public String manifestPackageName;
    public String packageName;
    public Package parentPackage;
    public final ArrayList<PackageParser.PermissionGroup> permissionGroups;
    public final ArrayList<PackageParser.Permission> permissions;
    public ArrayList<PackageParser.ActivityIntentInfo> preferredActivityFilters;
    public ArrayList<String> protectedBroadcasts;
    public final ArrayList<PackageParser.Provider> providers;
    public final ArrayList<PackageParser.Activity> receivers;
    public ArrayList<FeatureInfo> reqFeatures;
    public final ArrayList<String> requestedPermissions;
    public byte[] restrictUpdateHash;
    public final ArrayList<PackageParser.Service> services;
    public String[] splitCodePaths;
    public int[] splitFlags;
    public String[] splitNames;
    public int[] splitPrivateFlags;
    public int[] splitRevisionCodes;
    public String staticSharedLibName;
    public long staticSharedLibVersion;
    public boolean use32bitAbi;
    public ArrayList<String> usesLibraries;
    public String[] usesLibraryFiles;
    public ArrayList<String> usesOptionalLibraries;
    public ArrayList<String> usesStaticLibraries;
    public String[][] usesStaticLibrariesCertDigests;
    public long[] usesStaticLibrariesVersions;
    public boolean visibleToInstantApps;
    public String volumeUuid;
    
    public Package(Parcel paramParcel)
    {
      boolean bool1 = false;
      permissions = new ArrayList(0);
      permissionGroups = new ArrayList(0);
      activities = new ArrayList(0);
      receivers = new ArrayList(0);
      providers = new ArrayList(0);
      services = new ArrayList(0);
      instrumentation = new ArrayList(0);
      requestedPermissions = new ArrayList();
      staticSharedLibName = null;
      staticSharedLibVersion = 0L;
      libraryNames = null;
      usesLibraries = null;
      usesStaticLibraries = null;
      usesStaticLibrariesVersions = null;
      usesStaticLibrariesCertDigests = null;
      usesOptionalLibraries = null;
      usesLibraryFiles = null;
      preferredActivityFilters = null;
      mOriginalPackages = null;
      mRealPackage = null;
      mAdoptPermissions = null;
      mAppMetaData = null;
      mSigningDetails = PackageParser.SigningDetails.UNKNOWN;
      mPreferredOrder = 0;
      mLastPackageUsageTimeInMills = new long[8];
      configPreferences = null;
      reqFeatures = null;
      featureGroups = null;
      ClassLoader localClassLoader = Object.class.getClassLoader();
      packageName = paramParcel.readString().intern();
      manifestPackageName = paramParcel.readString();
      splitNames = paramParcel.readStringArray();
      volumeUuid = paramParcel.readString();
      codePath = paramParcel.readString();
      baseCodePath = paramParcel.readString();
      splitCodePaths = paramParcel.readStringArray();
      baseRevisionCode = paramParcel.readInt();
      splitRevisionCodes = paramParcel.createIntArray();
      splitFlags = paramParcel.createIntArray();
      splitPrivateFlags = paramParcel.createIntArray();
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      baseHardwareAccelerated = bool2;
      applicationInfo = ((ApplicationInfo)paramParcel.readParcelable(localClassLoader));
      if (applicationInfo.permission != null) {
        applicationInfo.permission = applicationInfo.permission.intern();
      }
      paramParcel.readParcelableList(permissions, localClassLoader);
      fixupOwner(permissions);
      paramParcel.readParcelableList(permissionGroups, localClassLoader);
      fixupOwner(permissionGroups);
      paramParcel.readParcelableList(activities, localClassLoader);
      fixupOwner(activities);
      paramParcel.readParcelableList(receivers, localClassLoader);
      fixupOwner(receivers);
      paramParcel.readParcelableList(providers, localClassLoader);
      fixupOwner(providers);
      paramParcel.readParcelableList(services, localClassLoader);
      fixupOwner(services);
      paramParcel.readParcelableList(instrumentation, localClassLoader);
      fixupOwner(instrumentation);
      paramParcel.readStringList(requestedPermissions);
      internStringArrayList(requestedPermissions);
      protectedBroadcasts = paramParcel.createStringArrayList();
      internStringArrayList(protectedBroadcasts);
      parentPackage = ((Package)paramParcel.readParcelable(localClassLoader));
      childPackages = new ArrayList();
      paramParcel.readParcelableList(childPackages, localClassLoader);
      if (childPackages.size() == 0) {
        childPackages = null;
      }
      staticSharedLibName = paramParcel.readString();
      if (staticSharedLibName != null) {
        staticSharedLibName = staticSharedLibName.intern();
      }
      staticSharedLibVersion = paramParcel.readLong();
      libraryNames = paramParcel.createStringArrayList();
      internStringArrayList(libraryNames);
      usesLibraries = paramParcel.createStringArrayList();
      internStringArrayList(usesLibraries);
      usesOptionalLibraries = paramParcel.createStringArrayList();
      internStringArrayList(usesOptionalLibraries);
      usesLibraryFiles = paramParcel.readStringArray();
      int i = paramParcel.readInt();
      if (i > 0)
      {
        usesStaticLibraries = new ArrayList(i);
        paramParcel.readStringList(usesStaticLibraries);
        internStringArrayList(usesStaticLibraries);
        usesStaticLibrariesVersions = new long[i];
        paramParcel.readLongArray(usesStaticLibrariesVersions);
        usesStaticLibrariesCertDigests = new String[i][];
        for (int j = 0; j < i; j++) {
          usesStaticLibrariesCertDigests[j] = paramParcel.createStringArray();
        }
      }
      preferredActivityFilters = new ArrayList();
      paramParcel.readParcelableList(preferredActivityFilters, localClassLoader);
      if (preferredActivityFilters.size() == 0) {
        preferredActivityFilters = null;
      }
      mOriginalPackages = paramParcel.createStringArrayList();
      mRealPackage = paramParcel.readString();
      mAdoptPermissions = paramParcel.createStringArrayList();
      mAppMetaData = paramParcel.readBundle();
      mVersionCode = paramParcel.readInt();
      mVersionCodeMajor = paramParcel.readInt();
      mVersionName = paramParcel.readString();
      if (mVersionName != null) {
        mVersionName = mVersionName.intern();
      }
      mSharedUserId = paramParcel.readString();
      if (mSharedUserId != null) {
        mSharedUserId = mSharedUserId.intern();
      }
      mSharedUserLabel = paramParcel.readInt();
      mSigningDetails = ((PackageParser.SigningDetails)paramParcel.readParcelable(localClassLoader));
      mPreferredOrder = paramParcel.readInt();
      configPreferences = new ArrayList();
      paramParcel.readParcelableList(configPreferences, localClassLoader);
      if (configPreferences.size() == 0) {
        configPreferences = null;
      }
      reqFeatures = new ArrayList();
      paramParcel.readParcelableList(reqFeatures, localClassLoader);
      if (reqFeatures.size() == 0) {
        reqFeatures = null;
      }
      featureGroups = new ArrayList();
      paramParcel.readParcelableList(featureGroups, localClassLoader);
      if (featureGroups.size() == 0) {
        featureGroups = null;
      }
      installLocation = paramParcel.readInt();
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      coreApp = bool2;
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mRequiredForAllUsers = bool2;
      mRestrictedAccountType = paramParcel.readString();
      mRequiredAccountType = paramParcel.readString();
      mOverlayTarget = paramParcel.readString();
      mOverlayCategory = paramParcel.readString();
      mOverlayPriority = paramParcel.readInt();
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mOverlayIsStatic = bool2;
      mCompileSdkVersion = paramParcel.readInt();
      mCompileSdkVersionCodename = paramParcel.readString();
      mUpgradeKeySets = paramParcel.readArraySet(localClassLoader);
      mKeySetMapping = readKeySetMapping(paramParcel);
      cpuAbiOverride = paramParcel.readString();
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      use32bitAbi = bool2;
      restrictUpdateHash = paramParcel.createByteArray();
      boolean bool2 = bool1;
      if (paramParcel.readInt() == 1) {
        bool2 = true;
      }
      visibleToInstantApps = bool2;
    }
    
    public Package(String paramString)
    {
      permissions = new ArrayList(0);
      permissionGroups = new ArrayList(0);
      activities = new ArrayList(0);
      receivers = new ArrayList(0);
      providers = new ArrayList(0);
      services = new ArrayList(0);
      instrumentation = new ArrayList(0);
      requestedPermissions = new ArrayList();
      staticSharedLibName = null;
      staticSharedLibVersion = 0L;
      libraryNames = null;
      usesLibraries = null;
      usesStaticLibraries = null;
      usesStaticLibrariesVersions = null;
      usesStaticLibrariesCertDigests = null;
      usesOptionalLibraries = null;
      usesLibraryFiles = null;
      preferredActivityFilters = null;
      mOriginalPackages = null;
      mRealPackage = null;
      mAdoptPermissions = null;
      mAppMetaData = null;
      mSigningDetails = PackageParser.SigningDetails.UNKNOWN;
      mPreferredOrder = 0;
      mLastPackageUsageTimeInMills = new long[8];
      configPreferences = null;
      reqFeatures = null;
      featureGroups = null;
      packageName = paramString;
      manifestPackageName = paramString;
      applicationInfo.packageName = paramString;
      applicationInfo.uid = -1;
    }
    
    private void fixupOwner(List<? extends PackageParser.Component<?>> paramList)
    {
      if (paramList != null)
      {
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          paramList = (PackageParser.Component)localIterator.next();
          owner = this;
          if ((paramList instanceof PackageParser.Activity)) {
            info.applicationInfo = applicationInfo;
          } else if ((paramList instanceof PackageParser.Service)) {
            info.applicationInfo = applicationInfo;
          } else if ((paramList instanceof PackageParser.Provider)) {
            info.applicationInfo = applicationInfo;
          }
        }
      }
    }
    
    private static void internStringArrayList(List<String> paramList)
    {
      if (paramList != null)
      {
        int i = paramList.size();
        for (int j = 0; j < i; j++) {
          paramList.set(j, ((String)paramList.get(j)).intern());
        }
      }
    }
    
    private static ArrayMap<String, ArraySet<PublicKey>> readKeySetMapping(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      if (i == -1) {
        return null;
      }
      ArrayMap localArrayMap = new ArrayMap();
      for (int j = 0; j < i; j++)
      {
        String str = paramParcel.readString();
        int k = paramParcel.readInt();
        if (k == -1)
        {
          localArrayMap.put(str, null);
        }
        else
        {
          ArraySet localArraySet = new ArraySet(k);
          for (int m = 0; m < k; m++) {
            localArraySet.add((PublicKey)paramParcel.readSerializable());
          }
          localArrayMap.put(str, localArraySet);
        }
      }
      return localArrayMap;
    }
    
    private static void writeKeySetMapping(Parcel paramParcel, ArrayMap<String, ArraySet<PublicKey>> paramArrayMap)
    {
      if (paramArrayMap == null)
      {
        paramParcel.writeInt(-1);
        return;
      }
      int i = paramArrayMap.size();
      paramParcel.writeInt(i);
      for (int j = 0; j < i; j++)
      {
        paramParcel.writeString((String)paramArrayMap.keyAt(j));
        ArraySet localArraySet = (ArraySet)paramArrayMap.valueAt(j);
        if (localArraySet == null)
        {
          paramParcel.writeInt(-1);
        }
        else
        {
          int k = localArraySet.size();
          paramParcel.writeInt(k);
          for (int m = 0; m < k; m++) {
            paramParcel.writeSerializable((Serializable)localArraySet.valueAt(m));
          }
        }
      }
    }
    
    public boolean canHaveOatDir()
    {
      boolean bool;
      if (((!isSystem()) || (isUpdatedSystemApp())) && (!isForwardLocked()) && (!applicationInfo.isExternalAsec())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public List<String> getAllCodePaths()
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(baseCodePath);
      if (!ArrayUtils.isEmpty(splitCodePaths)) {
        Collections.addAll(localArrayList, splitCodePaths);
      }
      return localArrayList;
    }
    
    public List<String> getAllCodePathsExcludingResourceOnly()
    {
      ArrayList localArrayList = new ArrayList();
      if ((applicationInfo.flags & 0x4) != 0) {
        localArrayList.add(baseCodePath);
      }
      if (!ArrayUtils.isEmpty(splitCodePaths)) {
        for (int i = 0; i < splitCodePaths.length; i++) {
          if ((splitFlags[i] & 0x4) != 0) {
            localArrayList.add(splitCodePaths[i]);
          }
        }
      }
      return localArrayList;
    }
    
    public List<String> getChildPackageNames()
    {
      if (childPackages == null) {
        return null;
      }
      int i = childPackages.size();
      ArrayList localArrayList = new ArrayList(i);
      for (int j = 0; j < i; j++) {
        localArrayList.add(childPackages.get(j)).packageName);
      }
      return localArrayList;
    }
    
    public long getLatestForegroundPackageUseTimeInMills()
    {
      int[] arrayOfInt = new int[2];
      int[] tmp5_4 = arrayOfInt;
      tmp5_4[0] = 0;
      int[] tmp9_5 = tmp5_4;
      tmp9_5[1] = 2;
      tmp9_5;
      long l = 0L;
      int i = arrayOfInt.length;
      for (int j = 0; j < i; j++)
      {
        int k = arrayOfInt[j];
        l = Math.max(l, mLastPackageUsageTimeInMills[k]);
      }
      return l;
    }
    
    public long getLatestPackageUseTimeInMills()
    {
      long l = 0L;
      long[] arrayOfLong = mLastPackageUsageTimeInMills;
      int i = arrayOfLong.length;
      for (int j = 0; j < i; j++) {
        l = Math.max(l, arrayOfLong[j]);
      }
      return l;
    }
    
    public long getLongVersionCode()
    {
      return PackageInfo.composeLongVersionCode(mVersionCodeMajor, mVersionCode);
    }
    
    public boolean hasChildPackage(String paramString)
    {
      int i;
      if (childPackages != null) {
        i = childPackages.size();
      } else {
        i = 0;
      }
      for (int j = 0; j < i; j++) {
        if (childPackages.get(j)).packageName.equals(paramString)) {
          return true;
        }
      }
      return false;
    }
    
    public boolean hasComponentClassName(String paramString)
    {
      for (int i = activities.size() - 1; i >= 0; i--) {
        if (paramString.equals(activities.get(i)).className)) {
          return true;
        }
      }
      for (i = receivers.size() - 1; i >= 0; i--) {
        if (paramString.equals(receivers.get(i)).className)) {
          return true;
        }
      }
      for (i = providers.size() - 1; i >= 0; i--) {
        if (paramString.equals(providers.get(i)).className)) {
          return true;
        }
      }
      for (i = services.size() - 1; i >= 0; i--) {
        if (paramString.equals(services.get(i)).className)) {
          return true;
        }
      }
      for (i = instrumentation.size() - 1; i >= 0; i--) {
        if (paramString.equals(instrumentation.get(i)).className)) {
          return true;
        }
      }
      return false;
    }
    
    public boolean isExternal()
    {
      return applicationInfo.isExternal();
    }
    
    public boolean isForwardLocked()
    {
      return applicationInfo.isForwardLocked();
    }
    
    public boolean isLibrary()
    {
      boolean bool;
      if ((staticSharedLibName == null) && (ArrayUtils.isEmpty(libraryNames))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean isMatch(int paramInt)
    {
      if ((0x100000 & paramInt) != 0) {
        return isSystem();
      }
      return true;
    }
    
    public boolean isOem()
    {
      return applicationInfo.isOem();
    }
    
    public boolean isPrivileged()
    {
      return applicationInfo.isPrivilegedApp();
    }
    
    public boolean isProduct()
    {
      return applicationInfo.isProduct();
    }
    
    public boolean isSystem()
    {
      return applicationInfo.isSystemApp();
    }
    
    public boolean isUpdatedSystemApp()
    {
      return applicationInfo.isUpdatedSystemApp();
    }
    
    public boolean isVendor()
    {
      return applicationInfo.isVendor();
    }
    
    public void setApplicationInfoBaseCodePath(String paramString)
    {
      applicationInfo.setBaseCodePath(paramString);
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).applicationInfo.setBaseCodePath(paramString);
        }
      }
    }
    
    @Deprecated
    public void setApplicationInfoBaseResourcePath(String paramString)
    {
      applicationInfo.setBaseResourcePath(paramString);
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).applicationInfo.setBaseResourcePath(paramString);
        }
      }
    }
    
    public void setApplicationInfoCodePath(String paramString)
    {
      applicationInfo.setCodePath(paramString);
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).applicationInfo.setCodePath(paramString);
        }
      }
    }
    
    public void setApplicationInfoFlags(int paramInt1, int paramInt2)
    {
      applicationInfo.flags = (applicationInfo.flags & paramInt1 | paramInt1 & paramInt2);
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).applicationInfo.flags = (applicationInfo.flags & paramInt1 | paramInt1 & paramInt2);
        }
      }
    }
    
    @Deprecated
    public void setApplicationInfoResourcePath(String paramString)
    {
      applicationInfo.setResourcePath(paramString);
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).applicationInfo.setResourcePath(paramString);
        }
      }
    }
    
    public void setApplicationInfoSplitCodePaths(String[] paramArrayOfString)
    {
      applicationInfo.setSplitCodePaths(paramArrayOfString);
    }
    
    @Deprecated
    public void setApplicationInfoSplitResourcePaths(String[] paramArrayOfString)
    {
      applicationInfo.setSplitResourcePaths(paramArrayOfString);
    }
    
    public void setApplicationVolumeUuid(String paramString)
    {
      UUID localUUID = StorageManager.convert(paramString);
      applicationInfo.volumeUuid = paramString;
      applicationInfo.storageUuid = localUUID;
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++)
        {
          childPackages.get(j)).applicationInfo.volumeUuid = paramString;
          childPackages.get(j)).applicationInfo.storageUuid = localUUID;
        }
      }
    }
    
    public void setBaseCodePath(String paramString)
    {
      baseCodePath = paramString;
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).baseCodePath = paramString;
        }
      }
    }
    
    public void setCodePath(String paramString)
    {
      codePath = paramString;
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).codePath = paramString;
        }
      }
    }
    
    public void setPackageName(String paramString)
    {
      packageName = paramString;
      applicationInfo.packageName = paramString;
      for (int i = permissions.size() - 1; i >= 0; i--) {
        ((PackageParser.Permission)permissions.get(i)).setPackageName(paramString);
      }
      for (i = permissionGroups.size() - 1; i >= 0; i--) {
        ((PackageParser.PermissionGroup)permissionGroups.get(i)).setPackageName(paramString);
      }
      for (i = activities.size() - 1; i >= 0; i--) {
        ((PackageParser.Activity)activities.get(i)).setPackageName(paramString);
      }
      for (i = receivers.size() - 1; i >= 0; i--) {
        ((PackageParser.Activity)receivers.get(i)).setPackageName(paramString);
      }
      for (i = providers.size() - 1; i >= 0; i--) {
        ((PackageParser.Provider)providers.get(i)).setPackageName(paramString);
      }
      for (i = services.size() - 1; i >= 0; i--) {
        ((PackageParser.Service)services.get(i)).setPackageName(paramString);
      }
      for (i = instrumentation.size() - 1; i >= 0; i--) {
        ((PackageParser.Instrumentation)instrumentation.get(i)).setPackageName(paramString);
      }
    }
    
    public void setSigningDetails(PackageParser.SigningDetails paramSigningDetails)
    {
      mSigningDetails = paramSigningDetails;
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).mSigningDetails = paramSigningDetails;
        }
      }
    }
    
    public void setSplitCodePaths(String[] paramArrayOfString)
    {
      splitCodePaths = paramArrayOfString;
    }
    
    public void setUse32bitAbi(boolean paramBoolean)
    {
      use32bitAbi = paramBoolean;
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).use32bitAbi = paramBoolean;
        }
      }
    }
    
    public void setVolumeUuid(String paramString)
    {
      volumeUuid = paramString;
      if (childPackages != null)
      {
        int i = childPackages.size();
        for (int j = 0; j < i; j++) {
          childPackages.get(j)).volumeUuid = paramString;
        }
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Package{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" ");
      localStringBuilder.append(packageName);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(packageName);
      paramParcel.writeString(manifestPackageName);
      paramParcel.writeStringArray(splitNames);
      paramParcel.writeString(volumeUuid);
      paramParcel.writeString(codePath);
      paramParcel.writeString(baseCodePath);
      paramParcel.writeStringArray(splitCodePaths);
      paramParcel.writeInt(baseRevisionCode);
      paramParcel.writeIntArray(splitRevisionCodes);
      paramParcel.writeIntArray(splitFlags);
      paramParcel.writeIntArray(splitPrivateFlags);
      paramParcel.writeInt(baseHardwareAccelerated);
      paramParcel.writeParcelable(applicationInfo, paramInt);
      paramParcel.writeParcelableList(permissions, paramInt);
      paramParcel.writeParcelableList(permissionGroups, paramInt);
      paramParcel.writeParcelableList(activities, paramInt);
      paramParcel.writeParcelableList(receivers, paramInt);
      paramParcel.writeParcelableList(providers, paramInt);
      paramParcel.writeParcelableList(services, paramInt);
      paramParcel.writeParcelableList(instrumentation, paramInt);
      paramParcel.writeStringList(requestedPermissions);
      paramParcel.writeStringList(protectedBroadcasts);
      paramParcel.writeParcelable(parentPackage, paramInt);
      paramParcel.writeParcelableList(childPackages, paramInt);
      paramParcel.writeString(staticSharedLibName);
      paramParcel.writeLong(staticSharedLibVersion);
      paramParcel.writeStringList(libraryNames);
      paramParcel.writeStringList(usesLibraries);
      paramParcel.writeStringList(usesOptionalLibraries);
      paramParcel.writeStringArray(usesLibraryFiles);
      if (ArrayUtils.isEmpty(usesStaticLibraries))
      {
        paramParcel.writeInt(-1);
      }
      else
      {
        paramParcel.writeInt(usesStaticLibraries.size());
        paramParcel.writeStringList(usesStaticLibraries);
        paramParcel.writeLongArray(usesStaticLibrariesVersions);
        String[][] arrayOfString = usesStaticLibrariesCertDigests;
        int i = arrayOfString.length;
        for (int j = 0; j < i; j++) {
          paramParcel.writeStringArray(arrayOfString[j]);
        }
      }
      paramParcel.writeParcelableList(preferredActivityFilters, paramInt);
      paramParcel.writeStringList(mOriginalPackages);
      paramParcel.writeString(mRealPackage);
      paramParcel.writeStringList(mAdoptPermissions);
      paramParcel.writeBundle(mAppMetaData);
      paramParcel.writeInt(mVersionCode);
      paramParcel.writeInt(mVersionCodeMajor);
      paramParcel.writeString(mVersionName);
      paramParcel.writeString(mSharedUserId);
      paramParcel.writeInt(mSharedUserLabel);
      paramParcel.writeParcelable(mSigningDetails, paramInt);
      paramParcel.writeInt(mPreferredOrder);
      paramParcel.writeParcelableList(configPreferences, paramInt);
      paramParcel.writeParcelableList(reqFeatures, paramInt);
      paramParcel.writeParcelableList(featureGroups, paramInt);
      paramParcel.writeInt(installLocation);
      paramParcel.writeInt(coreApp);
      paramParcel.writeInt(mRequiredForAllUsers);
      paramParcel.writeString(mRestrictedAccountType);
      paramParcel.writeString(mRequiredAccountType);
      paramParcel.writeString(mOverlayTarget);
      paramParcel.writeString(mOverlayCategory);
      paramParcel.writeInt(mOverlayPriority);
      paramParcel.writeInt(mOverlayIsStatic);
      paramParcel.writeInt(mCompileSdkVersion);
      paramParcel.writeString(mCompileSdkVersionCodename);
      paramParcel.writeArraySet(mUpgradeKeySets);
      writeKeySetMapping(paramParcel, mKeySetMapping);
      paramParcel.writeString(cpuAbiOverride);
      paramParcel.writeInt(use32bitAbi);
      paramParcel.writeByteArray(restrictUpdateHash);
      paramParcel.writeInt(visibleToInstantApps);
    }
  }
  
  public static class PackageLite
  {
    public final String baseCodePath;
    public final int baseRevisionCode;
    public final String codePath;
    public final String[] configForSplit;
    public final boolean coreApp;
    public final boolean debuggable;
    public final boolean extractNativeLibs;
    public final int installLocation;
    public final boolean[] isFeatureSplits;
    public final boolean isolatedSplits;
    public final boolean multiArch;
    public final String packageName;
    public final String[] splitCodePaths;
    public final String[] splitNames;
    public final int[] splitRevisionCodes;
    public final boolean use32bitAbi;
    public final String[] usesSplitNames;
    public final VerifierInfo[] verifiers;
    public final int versionCode;
    public final int versionCodeMajor;
    
    public PackageLite(String paramString, PackageParser.ApkLite paramApkLite, String[] paramArrayOfString1, boolean[] paramArrayOfBoolean, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4, int[] paramArrayOfInt)
    {
      packageName = packageName;
      versionCode = versionCode;
      versionCodeMajor = versionCodeMajor;
      installLocation = installLocation;
      verifiers = verifiers;
      splitNames = paramArrayOfString1;
      isFeatureSplits = paramArrayOfBoolean;
      usesSplitNames = paramArrayOfString2;
      configForSplit = paramArrayOfString3;
      codePath = paramString;
      baseCodePath = codePath;
      splitCodePaths = paramArrayOfString4;
      baseRevisionCode = revisionCode;
      splitRevisionCodes = paramArrayOfInt;
      coreApp = coreApp;
      debuggable = debuggable;
      multiArch = multiArch;
      use32bitAbi = use32bitAbi;
      extractNativeLibs = extractNativeLibs;
      isolatedSplits = isolatedSplits;
    }
    
    public List<String> getAllCodePaths()
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(baseCodePath);
      if (!ArrayUtils.isEmpty(splitCodePaths)) {
        Collections.addAll(localArrayList, splitCodePaths);
      }
      return localArrayList;
    }
  }
  
  public static class PackageParserException
    extends Exception
  {
    public final int error;
    
    public PackageParserException(int paramInt, String paramString)
    {
      super();
      error = paramInt;
    }
    
    public PackageParserException(int paramInt, String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
      error = paramInt;
    }
  }
  
  @VisibleForTesting
  public static class ParseComponentArgs
    extends PackageParser.ParsePackageItemArgs
  {
    final int descriptionRes;
    final int enabledRes;
    int flags;
    final int processRes;
    final String[] sepProcesses;
    
    public ParseComponentArgs(PackageParser.Package paramPackage, String[] paramArrayOfString1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String[] paramArrayOfString2, int paramInt7, int paramInt8, int paramInt9)
    {
      super(paramArrayOfString1, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
      sepProcesses = paramArrayOfString2;
      processRes = paramInt7;
      descriptionRes = paramInt8;
      enabledRes = paramInt9;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ParseFlags {}
  
  static class ParsePackageItemArgs
  {
    final int bannerRes;
    final int iconRes;
    final int labelRes;
    final int logoRes;
    final int nameRes;
    final String[] outError;
    final PackageParser.Package owner;
    final int roundIconRes;
    TypedArray sa;
    String tag;
    
    ParsePackageItemArgs(PackageParser.Package paramPackage, String[] paramArrayOfString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
    {
      owner = paramPackage;
      outError = paramArrayOfString;
      nameRes = paramInt1;
      labelRes = paramInt2;
      iconRes = paramInt3;
      logoRes = paramInt5;
      bannerRes = paramInt6;
      roundIconRes = paramInt4;
    }
  }
  
  public static final class Permission
    extends PackageParser.Component<PackageParser.IntentInfo>
    implements Parcelable
  {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
      public PackageParser.Permission createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageParser.Permission(paramAnonymousParcel, null);
      }
      
      public PackageParser.Permission[] newArray(int paramAnonymousInt)
      {
        return new PackageParser.Permission[paramAnonymousInt];
      }
    };
    public PackageParser.PermissionGroup group;
    public final PermissionInfo info;
    public boolean tree;
    
    public Permission(PackageParser.Package paramPackage)
    {
      super();
      info = new PermissionInfo();
    }
    
    public Permission(PackageParser.Package paramPackage, PermissionInfo paramPermissionInfo)
    {
      super();
      info = paramPermissionInfo;
    }
    
    private Permission(Parcel paramParcel)
    {
      super();
      ClassLoader localClassLoader = Object.class.getClassLoader();
      info = ((PermissionInfo)paramParcel.readParcelable(localClassLoader));
      if (info.group != null) {
        info.group = info.group.intern();
      }
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      tree = bool;
      group = ((PackageParser.PermissionGroup)paramParcel.readParcelable(localClassLoader));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean isAppOp()
    {
      return info.isAppOp();
    }
    
    public void setPackageName(String paramString)
    {
      super.setPackageName(paramString);
      info.packageName = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Permission{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" ");
      localStringBuilder.append(info.name);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeParcelable(info, paramInt);
      paramParcel.writeInt(tree);
      paramParcel.writeParcelable(group, paramInt);
    }
  }
  
  public static final class PermissionGroup
    extends PackageParser.Component<PackageParser.IntentInfo>
    implements Parcelable
  {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
      public PackageParser.PermissionGroup createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageParser.PermissionGroup(paramAnonymousParcel, null);
      }
      
      public PackageParser.PermissionGroup[] newArray(int paramAnonymousInt)
      {
        return new PackageParser.PermissionGroup[paramAnonymousInt];
      }
    };
    public final PermissionGroupInfo info;
    
    public PermissionGroup(PackageParser.Package paramPackage)
    {
      super();
      info = new PermissionGroupInfo();
    }
    
    public PermissionGroup(PackageParser.Package paramPackage, PermissionGroupInfo paramPermissionGroupInfo)
    {
      super();
      info = paramPermissionGroupInfo;
    }
    
    private PermissionGroup(Parcel paramParcel)
    {
      super();
      info = ((PermissionGroupInfo)paramParcel.readParcelable(Object.class.getClassLoader()));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void setPackageName(String paramString)
    {
      super.setPackageName(paramString);
      info.packageName = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PermissionGroup{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" ");
      localStringBuilder.append(info.name);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeParcelable(info, paramInt);
    }
  }
  
  public static final class Provider
    extends PackageParser.Component<PackageParser.ProviderIntentInfo>
    implements Parcelable
  {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
      public PackageParser.Provider createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageParser.Provider(paramAnonymousParcel, null);
      }
      
      public PackageParser.Provider[] newArray(int paramAnonymousInt)
      {
        return new PackageParser.Provider[paramAnonymousInt];
      }
    };
    public final ProviderInfo info;
    public boolean syncable;
    
    public Provider(PackageParser.ParseComponentArgs paramParseComponentArgs, ProviderInfo paramProviderInfo)
    {
      super(paramProviderInfo);
      info = paramProviderInfo;
      info.applicationInfo = owner.applicationInfo;
      syncable = false;
    }
    
    public Provider(Provider paramProvider)
    {
      super();
      info = info;
      syncable = syncable;
    }
    
    private Provider(Parcel paramParcel)
    {
      super();
      info = ((ProviderInfo)paramParcel.readParcelable(Object.class.getClassLoader()));
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      syncable = bool;
      paramParcel = intents.iterator();
      while (paramParcel.hasNext()) {
        nextprovider = this;
      }
      if (info.readPermission != null) {
        info.readPermission = info.readPermission.intern();
      }
      if (info.writePermission != null) {
        info.writePermission = info.writePermission.intern();
      }
      if (info.authority != null) {
        info.authority = info.authority.intern();
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void setPackageName(String paramString)
    {
      super.setPackageName(paramString);
      info.packageName = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("Provider{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(' ');
      appendComponentShortName(localStringBuilder);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeParcelable(info, paramInt | 0x2);
      paramParcel.writeInt(syncable);
    }
  }
  
  public static final class ProviderIntentInfo
    extends PackageParser.IntentInfo
  {
    public PackageParser.Provider provider;
    
    public ProviderIntentInfo(PackageParser.Provider paramProvider)
    {
      provider = paramProvider;
    }
    
    public ProviderIntentInfo(Parcel paramParcel)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("ProviderIntentInfo{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(' ');
      provider.appendComponentShortName(localStringBuilder);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
  
  public static final class Service
    extends PackageParser.Component<PackageParser.ServiceIntentInfo>
    implements Parcelable
  {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
      public PackageParser.Service createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageParser.Service(paramAnonymousParcel, null);
      }
      
      public PackageParser.Service[] newArray(int paramAnonymousInt)
      {
        return new PackageParser.Service[paramAnonymousInt];
      }
    };
    public final ServiceInfo info;
    
    public Service(PackageParser.ParseComponentArgs paramParseComponentArgs, ServiceInfo paramServiceInfo)
    {
      super(paramServiceInfo);
      info = paramServiceInfo;
      info.applicationInfo = owner.applicationInfo;
    }
    
    private Service(Parcel paramParcel)
    {
      super();
      info = ((ServiceInfo)paramParcel.readParcelable(Object.class.getClassLoader()));
      paramParcel = intents.iterator();
      while (paramParcel.hasNext())
      {
        PackageParser.ServiceIntentInfo localServiceIntentInfo = (PackageParser.ServiceIntentInfo)paramParcel.next();
        service = this;
        order = Math.max(localServiceIntentInfo.getOrder(), order);
      }
      if (info.permission != null) {
        info.permission = info.permission.intern();
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void setPackageName(String paramString)
    {
      super.setPackageName(paramString);
      info.packageName = paramString;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("Service{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(' ');
      appendComponentShortName(localStringBuilder);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeParcelable(info, paramInt | 0x2);
    }
  }
  
  public static final class ServiceIntentInfo
    extends PackageParser.IntentInfo
  {
    public PackageParser.Service service;
    
    public ServiceIntentInfo(PackageParser.Service paramService)
    {
      service = paramService;
    }
    
    public ServiceIntentInfo(Parcel paramParcel)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("ServiceIntentInfo{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(' ');
      service.appendComponentShortName(localStringBuilder);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
  
  public static final class SigningDetails
    implements Parcelable
  {
    public static final Parcelable.Creator<SigningDetails> CREATOR = new Parcelable.Creator()
    {
      public PackageParser.SigningDetails createFromParcel(Parcel paramAnonymousParcel)
      {
        if (paramAnonymousParcel.readBoolean()) {
          return PackageParser.SigningDetails.UNKNOWN;
        }
        return new PackageParser.SigningDetails(paramAnonymousParcel);
      }
      
      public PackageParser.SigningDetails[] newArray(int paramAnonymousInt)
      {
        return new PackageParser.SigningDetails[paramAnonymousInt];
      }
    };
    private static final int PAST_CERT_EXISTS = 0;
    public static final SigningDetails UNKNOWN = new SigningDetails(null, 0, null, null, null);
    public final Signature[] pastSigningCertificates;
    public final int[] pastSigningCertificatesFlags;
    public final ArraySet<PublicKey> publicKeys;
    @SignatureSchemeVersion
    public final int signatureSchemeVersion;
    public final Signature[] signatures;
    
    public SigningDetails(SigningDetails paramSigningDetails)
    {
      if (paramSigningDetails != null)
      {
        if (signatures != null) {
          signatures = ((Signature[])signatures.clone());
        } else {
          signatures = null;
        }
        signatureSchemeVersion = signatureSchemeVersion;
        publicKeys = new ArraySet(publicKeys);
        if (pastSigningCertificates != null)
        {
          pastSigningCertificates = ((Signature[])pastSigningCertificates.clone());
          pastSigningCertificatesFlags = ((int[])pastSigningCertificatesFlags.clone());
        }
        else
        {
          pastSigningCertificates = null;
          pastSigningCertificatesFlags = null;
        }
      }
      else
      {
        signatures = null;
        signatureSchemeVersion = 0;
        publicKeys = null;
        pastSigningCertificates = null;
        pastSigningCertificatesFlags = null;
      }
    }
    
    protected SigningDetails(Parcel paramParcel)
    {
      ClassLoader localClassLoader = Object.class.getClassLoader();
      signatures = ((Signature[])paramParcel.createTypedArray(Signature.CREATOR));
      signatureSchemeVersion = paramParcel.readInt();
      publicKeys = paramParcel.readArraySet(localClassLoader);
      pastSigningCertificates = ((Signature[])paramParcel.createTypedArray(Signature.CREATOR));
      pastSigningCertificatesFlags = paramParcel.createIntArray();
    }
    
    public SigningDetails(Signature[] paramArrayOfSignature, @SignatureSchemeVersion int paramInt)
      throws CertificateException
    {
      this(paramArrayOfSignature, paramInt, null, null);
    }
    
    @VisibleForTesting
    public SigningDetails(Signature[] paramArrayOfSignature1, @SignatureSchemeVersion int paramInt, ArraySet<PublicKey> paramArraySet, Signature[] paramArrayOfSignature2, int[] paramArrayOfInt)
    {
      signatures = paramArrayOfSignature1;
      signatureSchemeVersion = paramInt;
      publicKeys = paramArraySet;
      pastSigningCertificates = paramArrayOfSignature2;
      pastSigningCertificatesFlags = paramArrayOfInt;
    }
    
    public SigningDetails(Signature[] paramArrayOfSignature1, @SignatureSchemeVersion int paramInt, Signature[] paramArrayOfSignature2, int[] paramArrayOfInt)
      throws CertificateException
    {
      this(paramArrayOfSignature1, paramInt, PackageParser.toSigningKeys(paramArrayOfSignature1), paramArrayOfSignature2, paramArrayOfInt);
    }
    
    private boolean hasCertificateInternal(Signature paramSignature, int paramInt)
    {
      SigningDetails localSigningDetails = UNKNOWN;
      boolean bool1 = false;
      if (this == localSigningDetails) {
        return false;
      }
      if (hasPastSigningCertificates()) {
        for (int i = 0; i < pastSigningCertificates.length - 1; i++) {
          if ((pastSigningCertificates[i].equals(paramSignature)) && ((paramInt == 0) || ((pastSigningCertificatesFlags[i] & paramInt) == paramInt))) {
            return true;
          }
        }
      }
      boolean bool2 = bool1;
      if (signatures.length == 1)
      {
        bool2 = bool1;
        if (signatures[0].equals(paramSignature)) {
          bool2 = true;
        }
      }
      return bool2;
    }
    
    private boolean hasSha256CertificateInternal(byte[] paramArrayOfByte, int paramInt)
    {
      if (this == UNKNOWN) {
        return false;
      }
      if (hasPastSigningCertificates()) {
        for (int i = 0; i < pastSigningCertificates.length - 1; i++) {
          if ((Arrays.equals(paramArrayOfByte, PackageUtils.computeSha256DigestBytes(pastSigningCertificates[i].toByteArray()))) && ((paramInt == 0) || ((pastSigningCertificatesFlags[i] & paramInt) == paramInt))) {
            return true;
          }
        }
      }
      if (signatures.length == 1) {
        return Arrays.equals(paramArrayOfByte, PackageUtils.computeSha256DigestBytes(signatures[0].toByteArray()));
      }
      return false;
    }
    
    public boolean checkCapability(SigningDetails paramSigningDetails, @CertCapabilities int paramInt)
    {
      if ((this != UNKNOWN) && (paramSigningDetails != UNKNOWN))
      {
        if (signatures.length > 1) {
          return signaturesMatchExactly(paramSigningDetails);
        }
        return hasCertificate(signatures[0], paramInt);
      }
      return false;
    }
    
    public boolean checkCapability(String paramString, @CertCapabilities int paramInt)
    {
      if (this == UNKNOWN) {
        return false;
      }
      if (hasSha256Certificate(ByteStringUtils.fromHexToByteArray(paramString), paramInt)) {
        return true;
      }
      String[] arrayOfString = PackageUtils.computeSignaturesSha256Digests(signatures);
      return PackageUtils.computeSignaturesSha256Digest(arrayOfString).equals(paramString);
    }
    
    public boolean checkCapabilityRecover(SigningDetails paramSigningDetails, @CertCapabilities int paramInt)
      throws CertificateException
    {
      if ((paramSigningDetails != UNKNOWN) && (this != UNKNOWN))
      {
        if ((hasPastSigningCertificates()) && (signatures.length == 1))
        {
          for (int i = 0; i < pastSigningCertificates.length; i++) {
            if ((Signature.areEffectiveMatch(signatures[0], pastSigningCertificates[i])) && (pastSigningCertificatesFlags[i] == paramInt)) {
              return true;
            }
          }
          return false;
        }
        return Signature.areEffectiveMatch(signatures, signatures);
      }
      return false;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof SigningDetails)) {
        return false;
      }
      paramObject = (SigningDetails)paramObject;
      if (signatureSchemeVersion != signatureSchemeVersion) {
        return false;
      }
      if (!Signature.areExactMatch(signatures, signatures)) {
        return false;
      }
      if (publicKeys != null)
      {
        if (!publicKeys.equals(publicKeys)) {
          return false;
        }
      }
      else if (publicKeys != null) {
        return false;
      }
      if (!Arrays.equals(pastSigningCertificates, pastSigningCertificates)) {
        return false;
      }
      return Arrays.equals(pastSigningCertificatesFlags, pastSigningCertificatesFlags);
    }
    
    public boolean hasAncestor(SigningDetails paramSigningDetails)
    {
      if ((this != UNKNOWN) && (paramSigningDetails != UNKNOWN))
      {
        if ((hasPastSigningCertificates()) && (signatures.length == 1)) {
          for (int i = 0; i < pastSigningCertificates.length - 1; i++) {
            if (pastSigningCertificates[i].equals(signatures[i])) {
              return true;
            }
          }
        }
        return false;
      }
      return false;
    }
    
    public boolean hasAncestorOrSelf(SigningDetails paramSigningDetails)
    {
      if ((this != UNKNOWN) && (paramSigningDetails != UNKNOWN))
      {
        if (signatures.length > 1) {
          return signaturesMatchExactly(paramSigningDetails);
        }
        return hasCertificate(signatures[0]);
      }
      return false;
    }
    
    public boolean hasCertificate(Signature paramSignature)
    {
      return hasCertificateInternal(paramSignature, 0);
    }
    
    public boolean hasCertificate(Signature paramSignature, @CertCapabilities int paramInt)
    {
      return hasCertificateInternal(paramSignature, paramInt);
    }
    
    public boolean hasCertificate(byte[] paramArrayOfByte)
    {
      return hasCertificate(new Signature(paramArrayOfByte));
    }
    
    public boolean hasPastSigningCertificates()
    {
      boolean bool;
      if ((pastSigningCertificates != null) && (pastSigningCertificates.length > 0)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasSha256Certificate(byte[] paramArrayOfByte)
    {
      return hasSha256CertificateInternal(paramArrayOfByte, 0);
    }
    
    public boolean hasSha256Certificate(byte[] paramArrayOfByte, @CertCapabilities int paramInt)
    {
      return hasSha256CertificateInternal(paramArrayOfByte, paramInt);
    }
    
    public boolean hasSignatures()
    {
      boolean bool;
      if ((signatures != null) && (signatures.length > 0)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      int i = Arrays.hashCode(signatures);
      int j = signatureSchemeVersion;
      int k;
      if (publicKeys != null) {
        k = publicKeys.hashCode();
      } else {
        k = 0;
      }
      return 31 * (31 * (31 * (31 * i + j) + k) + Arrays.hashCode(pastSigningCertificates)) + Arrays.hashCode(pastSigningCertificatesFlags);
    }
    
    public boolean signaturesMatchExactly(SigningDetails paramSigningDetails)
    {
      return Signature.areExactMatch(signatures, signatures);
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      boolean bool;
      if (UNKNOWN == this) {
        bool = true;
      } else {
        bool = false;
      }
      paramParcel.writeBoolean(bool);
      if (bool) {
        return;
      }
      paramParcel.writeTypedArray(signatures, paramInt);
      paramParcel.writeInt(signatureSchemeVersion);
      paramParcel.writeArraySet(publicKeys);
      paramParcel.writeTypedArray(pastSigningCertificates, paramInt);
      paramParcel.writeIntArray(pastSigningCertificatesFlags);
    }
    
    public static class Builder
    {
      private Signature[] mPastSigningCertificates;
      private int[] mPastSigningCertificatesFlags;
      private int mSignatureSchemeVersion = 0;
      private Signature[] mSignatures;
      
      public Builder() {}
      
      private void checkInvariants()
      {
        if (mSignatures != null)
        {
          int i = 0;
          if ((mPastSigningCertificates != null) && (mPastSigningCertificatesFlags != null))
          {
            if (mPastSigningCertificates.length != mPastSigningCertificatesFlags.length) {
              i = 1;
            }
          }
          else if ((mPastSigningCertificates != null) || (mPastSigningCertificatesFlags != null)) {
            i = 1;
          }
          if (i == 0) {
            return;
          }
          throw new IllegalStateException("SigningDetails must have a one to one mapping between pastSigningCertificates and pastSigningCertificatesFlags");
        }
        throw new IllegalStateException("SigningDetails requires the current signing certificates.");
      }
      
      public PackageParser.SigningDetails build()
        throws CertificateException
      {
        checkInvariants();
        return new PackageParser.SigningDetails(mSignatures, mSignatureSchemeVersion, mPastSigningCertificates, mPastSigningCertificatesFlags);
      }
      
      public Builder setPastSigningCertificates(Signature[] paramArrayOfSignature)
      {
        mPastSigningCertificates = paramArrayOfSignature;
        return this;
      }
      
      public Builder setPastSigningCertificatesFlags(int[] paramArrayOfInt)
      {
        mPastSigningCertificatesFlags = paramArrayOfInt;
        return this;
      }
      
      public Builder setSignatureSchemeVersion(int paramInt)
      {
        mSignatureSchemeVersion = paramInt;
        return this;
      }
      
      public Builder setSignatures(Signature[] paramArrayOfSignature)
      {
        mSignatures = paramArrayOfSignature;
        return this;
      }
    }
    
    public static @interface CertCapabilities
    {
      public static final int AUTH = 16;
      public static final int INSTALLED_DATA = 1;
      public static final int PERMISSION = 4;
      public static final int ROLLBACK = 8;
      public static final int SHARED_USER_ID = 2;
    }
    
    public static @interface SignatureSchemeVersion
    {
      public static final int JAR = 1;
      public static final int SIGNING_BLOCK_V2 = 2;
      public static final int SIGNING_BLOCK_V3 = 3;
      public static final int UNKNOWN = 0;
    }
  }
  
  private static class SplitNameComparator
    implements Comparator<String>
  {
    private SplitNameComparator() {}
    
    public int compare(String paramString1, String paramString2)
    {
      if (paramString1 == null) {
        return -1;
      }
      if (paramString2 == null) {
        return 1;
      }
      return paramString1.compareTo(paramString2);
    }
  }
  
  public static class SplitPermissionInfo
  {
    public final String[] newPerms;
    public final String rootPerm;
    public final int targetSdk;
    
    public SplitPermissionInfo(String paramString, String[] paramArrayOfString, int paramInt)
    {
      rootPerm = paramString;
      newPerms = paramArrayOfString;
      targetSdk = paramInt;
    }
  }
}
