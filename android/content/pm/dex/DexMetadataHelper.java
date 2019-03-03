package android.content.pm.dex;

import android.content.pm.PackageParser;
import android.content.pm.PackageParser.Package;
import android.content.pm.PackageParser.PackageLite;
import android.content.pm.PackageParser.PackageParserException;
import android.util.ArrayMap;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DexMetadataHelper
{
  private static final String DEX_METADATA_FILE_EXTENSION = ".dm";
  
  private DexMetadataHelper() {}
  
  public static String buildDexMetadataPathForApk(String paramString)
  {
    if (PackageParser.isApkPath(paramString))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString.substring(0, paramString.length() - ".apk".length()));
      localStringBuilder.append(".dm");
      return localStringBuilder.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Corrupted package. Code path is not an apk ");
    localStringBuilder.append(paramString);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private static String buildDexMetadataPathForFile(File paramFile)
  {
    if (PackageParser.isApkFile(paramFile))
    {
      paramFile = buildDexMetadataPathForApk(paramFile.getPath());
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramFile.getPath());
      localStringBuilder.append(".dm");
      paramFile = localStringBuilder.toString();
    }
    return paramFile;
  }
  
  private static Map<String, String> buildPackageApkToDexMetadataMap(List<String> paramList)
  {
    ArrayMap localArrayMap = new ArrayMap();
    for (int i = paramList.size() - 1; i >= 0; i--)
    {
      String str1 = (String)paramList.get(i);
      String str2 = buildDexMetadataPathForFile(new File(str1));
      if (Files.exists(Paths.get(str2, new String[0]), new LinkOption[0])) {
        localArrayMap.put(str1, str2);
      }
    }
    return localArrayMap;
  }
  
  public static File findDexMetadataForFile(File paramFile)
  {
    paramFile = new File(buildDexMetadataPathForFile(paramFile));
    if (!paramFile.exists()) {
      paramFile = null;
    }
    return paramFile;
  }
  
  public static Map<String, String> getPackageDexMetadata(PackageParser.Package paramPackage)
  {
    return buildPackageApkToDexMetadataMap(paramPackage.getAllCodePaths());
  }
  
  private static Map<String, String> getPackageDexMetadata(PackageParser.PackageLite paramPackageLite)
  {
    return buildPackageApkToDexMetadataMap(paramPackageLite.getAllCodePaths());
  }
  
  public static long getPackageDexMetadataSize(PackageParser.PackageLite paramPackageLite)
  {
    long l = 0L;
    paramPackageLite = getPackageDexMetadata(paramPackageLite).values().iterator();
    while (paramPackageLite.hasNext()) {
      l += new File((String)paramPackageLite.next()).length();
    }
    return l;
  }
  
  public static boolean isDexMetadataFile(File paramFile)
  {
    return isDexMetadataPath(paramFile.getName());
  }
  
  private static boolean isDexMetadataPath(String paramString)
  {
    return paramString.endsWith(".dm");
  }
  
  /* Error */
  private static void validateDexMetadataFile(String paramString)
    throws PackageParser.PackageParserException
  {
    // Byte code:
    //   0: new 166	android/util/jar/StrictJarFile
    //   3: dup
    //   4: aload_0
    //   5: iconst_0
    //   6: iconst_0
    //   7: invokespecial 169	android/util/jar/StrictJarFile:<init>	(Ljava/lang/String;ZZ)V
    //   10: astore_1
    //   11: aload_1
    //   12: invokevirtual 172	android/util/jar/StrictJarFile:close	()V
    //   15: goto +7 -> 22
    //   18: astore_0
    //   19: goto -4 -> 15
    //   22: return
    //   23: astore_0
    //   24: goto +42 -> 66
    //   27: astore_1
    //   28: new 162	android/content/pm/PackageParser$PackageParserException
    //   31: astore_2
    //   32: new 23	java/lang/StringBuilder
    //   35: astore_3
    //   36: aload_3
    //   37: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   40: aload_3
    //   41: ldc -82
    //   43: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: pop
    //   47: aload_3
    //   48: aload_0
    //   49: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: pop
    //   53: aload_2
    //   54: bipush -117
    //   56: aload_3
    //   57: invokevirtual 44	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   60: aload_1
    //   61: invokespecial 177	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   64: aload_2
    //   65: athrow
    //   66: iconst_0
    //   67: ifeq +12 -> 79
    //   70: new 179	java/lang/NullPointerException
    //   73: dup
    //   74: invokespecial 180	java/lang/NullPointerException:<init>	()V
    //   77: athrow
    //   78: astore_1
    //   79: aload_0
    //   80: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	81	0	paramString	String
    //   10	2	1	localStrictJarFile	android.util.jar.StrictJarFile
    //   27	34	1	localIOException1	java.io.IOException
    //   78	1	1	localIOException2	java.io.IOException
    //   31	34	2	localPackageParserException	PackageParser.PackageParserException
    //   35	22	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   11	15	18	java/io/IOException
    //   0	11	23	finally
    //   28	66	23	finally
    //   0	11	27	java/io/IOException
    //   70	78	78	java/io/IOException
  }
  
  public static void validateDexPaths(String[] paramArrayOfString)
  {
    ArrayList localArrayList1 = new ArrayList();
    int i = 0;
    for (int j = 0; j < paramArrayOfString.length; j++) {
      if (PackageParser.isApkPath(paramArrayOfString[j])) {
        localArrayList1.add(paramArrayOfString[j]);
      }
    }
    ArrayList localArrayList2 = new ArrayList();
    for (j = i; j < paramArrayOfString.length; j++)
    {
      String str = paramArrayOfString[j];
      if (isDexMetadataPath(str))
      {
        int k = 0;
        for (int m = localArrayList1.size() - 1;; m--)
        {
          i = k;
          if (m < 0) {
            break;
          }
          if (str.equals(buildDexMetadataPathForFile(new File((String)localArrayList1.get(m)))))
          {
            i = 1;
            break;
          }
        }
        if (i == 0) {
          localArrayList2.add(str);
        }
      }
    }
    if (localArrayList2.isEmpty()) {
      return;
    }
    paramArrayOfString = new StringBuilder();
    paramArrayOfString.append("Unmatched .dm files: ");
    paramArrayOfString.append(localArrayList2);
    throw new IllegalStateException(paramArrayOfString.toString());
  }
  
  public static void validatePackageDexMetadata(PackageParser.Package paramPackage)
    throws PackageParser.PackageParserException
  {
    paramPackage = getPackageDexMetadata(paramPackage).values().iterator();
    while (paramPackage.hasNext()) {
      validateDexMetadataFile((String)paramPackage.next());
    }
  }
}
