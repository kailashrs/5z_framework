package android.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WallpaperUtilities
{
  protected static final String CUSTOMIZE_RES_PACKAGE_NAME = "com.asus.LauncherRes";
  private static final boolean DEBUG = false;
  private static final String DEFAULT_ID_CODE = "1a";
  private static final String DEFAULT_PROP_CID = "";
  private static final String DEFAULT_PROP_RUNTIME_DEVICE = "";
  public static final int INVALID_RESOURCE_ID = 0;
  private static final String PREFIX_DIR_CID = "ci";
  private static final String PROP_CID = "ro.config.CID";
  private static final String PROP_IDCODE = "ro.config.idcode";
  private static final String PROP_RUNTIME_DEVICE = "ro.config.asuswallpaper";
  private static final String RES_CUSTOMIZE_ETC_ROOT_PATH = "/system/etc/LauncherRes";
  private static final String RES_CUSTOMIZE_ROOT_PATH = "/system/vendor/etc";
  private static final String RES_VERSA_DEFAULT_FOLDER_NAME = "Generic";
  private static final String RES_XROM_ROOT_PATH = "/xrom/Wallpaper";
  private static final String TAG = "WallpaperUtilities";
  private static final String WALLPAPER_LIST_FILENAME = "wallpaper_list.xml";
  private static final String WALLPAPER_PATH = "/Launcher/wallpapers";
  
  public WallpaperUtilities() {}
  
  private static String getADFDefaultWallpaperFilePath(Context paramContext)
  {
    String[] arrayOfString = getWallpaperListInOrder(paramContext, "wallpaper_list.xml", getWallpaperFolderPath());
    Object localObject = null;
    paramContext = localObject;
    if (arrayOfString != null)
    {
      paramContext = localObject;
      if (arrayOfString.length > 0) {
        paramContext = getWallpaperFilePathFromADF(arrayOfString[0]);
      }
    }
    return paramContext;
  }
  
  protected static InputStream getAsusDefaultWallpaper(Context paramContext)
  {
    Object localObject1 = getLauncherResources(paramContext.getResources());
    int i = getDefaultWallpaperResId((Resources)localObject1, "com.asus.LauncherRes");
    if (i != 0) {
      return ((Resources)localObject1).openRawResource(i);
    }
    localObject1 = getEtcResDefaultWallpaperFilePath(paramContext);
    if (!TextUtils.isEmpty((CharSequence)localObject1)) {
      try
      {
        Object localObject2 = new java/io/FileInputStream;
        ((FileInputStream)localObject2).<init>((String)localObject1);
        localObject2 = new BufferedInputStream((InputStream)localObject2);
        return localObject2;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("File not found: ");
        localStringBuilder.append((String)localObject1);
        Log.v("WallpaperUtilities", localStringBuilder.toString());
      }
    }
    i = getDefaultWallpaperResId(paramContext.getResources(), "android");
    if (i != 0) {
      return paramContext.getResources().openRawResource(i);
    }
    return null;
  }
  
  private static String getCIDInLowerCase()
  {
    Object localObject1 = SystemProperties.get("ro.config.CID", "");
    Object localObject2 = localObject1;
    if (!TextUtils.isEmpty((CharSequence)localObject1)) {
      localObject2 = ((String)localObject1).toLowerCase();
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("getCID: ");
    ((StringBuilder)localObject1).append((String)localObject2);
    Log.d("WallpaperUtilities", ((StringBuilder)localObject1).toString());
    return localObject2;
  }
  
  private static String getColorIdCode()
  {
    Object localObject = getFixedColorIdCode();
    if (!TextUtils.isEmpty((CharSequence)localObject)) {
      return localObject;
    }
    String str = SystemProperties.get("ro.config.idcode", "").toLowerCase();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getColorIdCode: ");
    ((StringBuilder)localObject).append(str);
    Log.v("WallpaperUtilities", ((StringBuilder)localObject).toString());
    if (!TextUtils.isEmpty(str))
    {
      localObject = str;
      if (!str.equals("unknown")) {}
    }
    else
    {
      localObject = "1a";
    }
    return localObject;
  }
  
  private static int getDefaultWallpaperResId(Resources paramResources, String paramString)
  {
    String str1 = paramString;
    if (paramString == null) {
      str1 = "android";
    }
    Iterator localIterator = getMappingListForColorId().iterator();
    while (localIterator.hasNext())
    {
      String str2 = (String)localIterator.next();
      paramString = new StringBuilder();
      paramString.append("default_wallpaper_");
      paramString.append(str2);
      int i = paramResources.getIdentifier(paramString.toString(), "drawable", str1);
      if (i != 0) {
        return i;
      }
    }
    return 0;
  }
  
  private static String getEtcResDefaultWallpaperFilePath(Context paramContext)
  {
    Iterator localIterator = getMappingListForColorId().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (String)localIterator.next();
      StringBuilder localStringBuilder;
      if ((isSupportDds()) && (isDdsPad(paramContext)))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("pad_default_wallpaper_");
        localStringBuilder.append((String)localObject);
        localObject = localStringBuilder.toString();
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("default_wallpaper_");
        localStringBuilder.append((String)localObject);
        localObject = localStringBuilder.toString();
      }
      localObject = getWallpaperFileStartsWith((String)localObject);
      if ((localObject != null) && (localObject.length > 0))
      {
        if (localObject.length > 1) {
          Log.d("WallpaperUtilities", "EtcDefaultWallpaperFilePath may NOT UNIQUE in folder");
        }
        paramContext = new StringBuilder();
        paramContext.append("EtcDefaultWallpaperFilePath: ");
        paramContext.append(localObject[0].toString());
        Log.v("WallpaperUtilities", paramContext.toString());
        return localObject[0].toString();
      }
    }
    return null;
  }
  
  protected static String getEtcResDefaultWallpaperFilePathForLockscreen(Context paramContext)
  {
    if ((isSupportDds()) && (isDdsPad(paramContext))) {
      paramContext = "pad_default_wallpaper_1a";
    } else {
      paramContext = "default_wallpaper_1a";
    }
    paramContext = getFileStartsWith(getLauncherResFolderPath(), paramContext, true);
    if ((paramContext != null) && (paramContext.length > 0)) {
      return paramContext[0].toString();
    }
    return null;
  }
  
  private static File[] getFileStartsWith(final String paramString1, final String paramString2, boolean paramBoolean)
  {
    if (!isDirectory(paramString1)) {
      return null;
    }
    new File(paramString1).listFiles(new FilenameFilter()
    {
      public boolean accept(File paramAnonymousFile, String paramAnonymousString)
      {
        if (val$ignoreCase) {
          paramAnonymousFile = paramAnonymousString.toLowerCase();
        } else {
          paramAnonymousFile = paramAnonymousString;
        }
        if (val$ignoreCase) {
          paramAnonymousString = paramString2.toLowerCase();
        } else {
          paramAnonymousString = paramString2;
        }
        if (paramAnonymousFile.startsWith(paramAnonymousString))
        {
          paramAnonymousString = new StringBuilder();
          paramAnonymousString.append("find: ");
          paramAnonymousString.append(paramAnonymousFile);
          paramAnonymousString.append(", getFileStartsWith: ");
          paramAnonymousString.append(paramString2);
          paramAnonymousString.append(", ignoreCase= ");
          paramAnonymousString.append(val$ignoreCase);
          paramAnonymousString.append(", find in folder: ");
          paramAnonymousString.append(paramString1);
          Log.v("WallpaperUtilities", paramAnonymousString.toString());
          return true;
        }
        return false;
      }
    });
  }
  
  private static String getFixedColorIdCode()
  {
    Object localObject = getWallpaperFileStartsWith("color_id_mapping_rules.json");
    if ((localObject != null) && (localObject.length > 0))
    {
      localObject = getJSONObjectFromFile(localObject[0].toString());
      if (localObject != null) {
        try
        {
          String str = ((JSONObject)localObject).getString("fixed_color_id");
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Read fixedColorId from JSONFile: ");
          ((StringBuilder)localObject).append(str);
          Log.v("WallpaperUtilities", ((StringBuilder)localObject).toString());
          if (!TextUtils.isEmpty(str))
          {
            localObject = str.toLowerCase();
            return localObject;
          }
        }
        catch (JSONException localJSONException)
        {
          Log.v("WallpaperUtilities", "No defined fixedColorId from JSONFile.");
        }
      }
    }
    return null;
  }
  
  /* Error */
  private static JSONObject getJSONObjectFromFile(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aconst_null
    //   5: astore_3
    //   6: aconst_null
    //   7: astore 4
    //   9: aload 4
    //   11: astore 5
    //   13: aload_1
    //   14: astore 6
    //   16: aload_2
    //   17: astore 7
    //   19: aload_3
    //   20: astore 8
    //   22: new 261	java/io/BufferedReader
    //   25: astore 9
    //   27: aload 4
    //   29: astore 5
    //   31: aload_1
    //   32: astore 6
    //   34: aload_2
    //   35: astore 7
    //   37: aload_3
    //   38: astore 8
    //   40: new 263	java/io/FileReader
    //   43: astore 10
    //   45: aload 4
    //   47: astore 5
    //   49: aload_1
    //   50: astore 6
    //   52: aload_2
    //   53: astore 7
    //   55: aload_3
    //   56: astore 8
    //   58: aload 10
    //   60: aload_0
    //   61: invokespecial 264	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   64: aload 4
    //   66: astore 5
    //   68: aload_1
    //   69: astore 6
    //   71: aload_2
    //   72: astore 7
    //   74: aload_3
    //   75: astore 8
    //   77: aload 9
    //   79: aload 10
    //   81: invokespecial 267	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   84: aload 9
    //   86: astore_0
    //   87: aload_0
    //   88: astore 5
    //   90: aload_0
    //   91: astore 6
    //   93: aload_0
    //   94: astore 7
    //   96: aload_0
    //   97: astore 8
    //   99: new 119	java/lang/StringBuilder
    //   102: astore 4
    //   104: aload_0
    //   105: astore 5
    //   107: aload_0
    //   108: astore 6
    //   110: aload_0
    //   111: astore 7
    //   113: aload_0
    //   114: astore 8
    //   116: aload 4
    //   118: invokespecial 120	java/lang/StringBuilder:<init>	()V
    //   121: aload_0
    //   122: astore 5
    //   124: aload_0
    //   125: astore 6
    //   127: aload_0
    //   128: astore 7
    //   130: aload_0
    //   131: astore 8
    //   133: aload_0
    //   134: invokevirtual 270	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   137: astore 9
    //   139: aload 9
    //   141: ifnull +26 -> 167
    //   144: aload_0
    //   145: astore 5
    //   147: aload_0
    //   148: astore 6
    //   150: aload_0
    //   151: astore 7
    //   153: aload_0
    //   154: astore 8
    //   156: aload 4
    //   158: aload 9
    //   160: invokevirtual 126	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: goto -43 -> 121
    //   167: aload_0
    //   168: astore 5
    //   170: aload_0
    //   171: astore 6
    //   173: aload_0
    //   174: astore 7
    //   176: aload_0
    //   177: astore 8
    //   179: new 250	org/json/JSONObject
    //   182: dup
    //   183: aload 4
    //   185: invokevirtual 129	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   188: invokespecial 271	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   191: astore 9
    //   193: aload_0
    //   194: invokevirtual 274	java/io/BufferedReader:close	()V
    //   197: goto +14 -> 211
    //   200: astore_0
    //   201: ldc 49
    //   203: ldc_w 276
    //   206: aload_0
    //   207: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   210: pop
    //   211: aload 9
    //   213: areturn
    //   214: astore_0
    //   215: goto +85 -> 300
    //   218: astore_0
    //   219: aload 6
    //   221: astore 5
    //   223: aload_0
    //   224: invokevirtual 283	org/json/JSONException:printStackTrace	()V
    //   227: aload 6
    //   229: ifnull +69 -> 298
    //   232: aload 6
    //   234: invokevirtual 274	java/io/BufferedReader:close	()V
    //   237: goto +61 -> 298
    //   240: astore_0
    //   241: ldc 49
    //   243: ldc_w 276
    //   246: aload_0
    //   247: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   250: pop
    //   251: goto -14 -> 237
    //   254: astore_0
    //   255: aload 7
    //   257: astore 5
    //   259: aload_0
    //   260: invokevirtual 284	java/io/IOException:printStackTrace	()V
    //   263: aload 7
    //   265: ifnull +33 -> 298
    //   268: aload 7
    //   270: invokevirtual 274	java/io/BufferedReader:close	()V
    //   273: goto -36 -> 237
    //   276: astore_0
    //   277: aload 8
    //   279: astore 5
    //   281: aload_0
    //   282: invokevirtual 285	java/io/FileNotFoundException:printStackTrace	()V
    //   285: aload 8
    //   287: ifnull +11 -> 298
    //   290: aload 8
    //   292: invokevirtual 274	java/io/BufferedReader:close	()V
    //   295: goto -58 -> 237
    //   298: aconst_null
    //   299: areturn
    //   300: aload 5
    //   302: ifnull +24 -> 326
    //   305: aload 5
    //   307: invokevirtual 274	java/io/BufferedReader:close	()V
    //   310: goto +16 -> 326
    //   313: astore 5
    //   315: ldc 49
    //   317: ldc_w 276
    //   320: aload 5
    //   322: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   325: pop
    //   326: aload_0
    //   327: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	328	0	paramString	String
    //   1	68	1	localObject1	Object
    //   3	69	2	localObject2	Object
    //   5	70	3	localObject3	Object
    //   7	177	4	localStringBuilder	StringBuilder
    //   11	295	5	localObject4	Object
    //   313	8	5	localIOException	java.io.IOException
    //   14	219	6	localObject5	Object
    //   17	252	7	localObject6	Object
    //   20	271	8	localObject7	Object
    //   25	187	9	localObject8	Object
    //   43	37	10	localFileReader	java.io.FileReader
    // Exception table:
    //   from	to	target	type
    //   193	197	200	java/io/IOException
    //   22	27	214	finally
    //   40	45	214	finally
    //   58	64	214	finally
    //   77	84	214	finally
    //   99	104	214	finally
    //   116	121	214	finally
    //   133	139	214	finally
    //   156	164	214	finally
    //   179	193	214	finally
    //   223	227	214	finally
    //   259	263	214	finally
    //   281	285	214	finally
    //   22	27	218	org/json/JSONException
    //   40	45	218	org/json/JSONException
    //   58	64	218	org/json/JSONException
    //   77	84	218	org/json/JSONException
    //   99	104	218	org/json/JSONException
    //   116	121	218	org/json/JSONException
    //   133	139	218	org/json/JSONException
    //   156	164	218	org/json/JSONException
    //   179	193	218	org/json/JSONException
    //   232	237	240	java/io/IOException
    //   268	273	240	java/io/IOException
    //   290	295	240	java/io/IOException
    //   22	27	254	java/io/IOException
    //   40	45	254	java/io/IOException
    //   58	64	254	java/io/IOException
    //   77	84	254	java/io/IOException
    //   99	104	254	java/io/IOException
    //   116	121	254	java/io/IOException
    //   133	139	254	java/io/IOException
    //   156	164	254	java/io/IOException
    //   179	193	254	java/io/IOException
    //   22	27	276	java/io/FileNotFoundException
    //   40	45	276	java/io/FileNotFoundException
    //   58	64	276	java/io/FileNotFoundException
    //   77	84	276	java/io/FileNotFoundException
    //   99	104	276	java/io/FileNotFoundException
    //   116	121	276	java/io/FileNotFoundException
    //   133	139	276	java/io/FileNotFoundException
    //   156	164	276	java/io/FileNotFoundException
    //   179	193	276	java/io/FileNotFoundException
    //   305	310	313	java/io/IOException
  }
  
  private static String getLauncherResFolderCIDPath()
  {
    Object localObject1 = getLauncherResFolderRuntimeDevicePath();
    Object localObject2 = getCIDInLowerCase();
    if (TextUtils.isEmpty((CharSequence)localObject2)) {
      return localObject1;
    }
    localObject2 = getLauncherResFolderXRomCIDPath((String)localObject2);
    if (!TextUtils.isEmpty((CharSequence)localObject2)) {
      return localObject2;
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("/");
    ((StringBuilder)localObject2).append("ci");
    ((StringBuilder)localObject2).append(getCIDInLowerCase());
    localObject2 = ((StringBuilder)localObject2).toString();
    if (isDirectory((String)localObject2)) {
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  private static String getLauncherResFolderPath()
  {
    return "/system/etc/LauncherRes";
  }
  
  private static String getLauncherResFolderRuntimeDevicePath()
  {
    Object localObject1 = getLauncherResFolderPath();
    if (TextUtils.isEmpty(getRuntimeDeviceInLowerCase())) {
      return localObject1;
    }
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("/");
    ((StringBuilder)localObject2).append(getRuntimeDeviceInLowerCase());
    localObject2 = ((StringBuilder)localObject2).toString();
    if (isDirectory((String)localObject2)) {
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  private static String getLauncherResFolderXRomCIDPath(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getLauncherResFolderXRomPath());
    localStringBuilder.append("/");
    localStringBuilder.append("ci");
    localStringBuilder.append(paramString);
    paramString = localStringBuilder.toString();
    if (!isDirectory(paramString)) {
      paramString = "";
    }
    return paramString;
  }
  
  private static String getLauncherResFolderXRomPath()
  {
    return "/xrom/Wallpaper";
  }
  
  private static Resources getLauncherResources(Resources paramResources)
  {
    try
    {
      Object localObject = Class.forName("android.content.res.AssetManager").getDeclaredConstructor((Class[])null).newInstance((Object[])null);
      localObject.getClass().getDeclaredMethod("addAssetPath", new Class[] { String.class }).invoke(localObject, new Object[] { "/system/app/AsusLauncherRes.apk" });
      paramResources = (Resources)Resources.class.getDeclaredConstructor(new Class[] { localObject.getClass(), paramResources.getDisplayMetrics().getClass(), paramResources.getConfiguration().getClass() }).newInstance(new Object[] { localObject, paramResources.getDisplayMetrics(), paramResources.getConfiguration() });
      return paramResources;
    }
    catch (Exception paramResources) {}catch (Resources.NotFoundException paramResources) {}
    return null;
  }
  
  private static ArrayList<String> getMappingListForColorId()
  {
    ArrayList localArrayList = new ArrayList();
    String str1 = getColorIdCode();
    if ((!TextUtils.isEmpty(str1)) && (str1.length() > 2)) {
      localArrayList.add(str1);
    }
    String str2 = getRemappedColorIdCode(str1);
    if (!str1.equals(str2)) {
      localArrayList.add(str2);
    }
    if (!localArrayList.contains(str1)) {
      localArrayList.add(str1);
    }
    String str3 = getPartialMatchedColorIdCode(str1);
    if (!str2.equals(str3)) {
      localArrayList.add(str3);
    }
    if (str1.equals(str2))
    {
      str1 = mappingColorIdCodeForZf2(str3);
      if (!str3.equals(str1)) {
        localArrayList.add(str1);
      }
    }
    return localArrayList;
  }
  
  private static String getPartialMatchedColorIdCode(String paramString)
  {
    Object localObject = paramString;
    if (paramString.length() > 1)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(1);
      ((StringBuilder)localObject).append(paramString.substring(paramString.length() - 1));
      localObject = ((StringBuilder)localObject).toString();
      paramString = new StringBuilder();
      paramString.append("get partial matched idcode: ");
      paramString.append((String)localObject);
      Log.v("WallpaperUtilities", paramString.toString());
    }
    return localObject;
  }
  
  private static String getRemappedColorIdCode(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return paramString;
    }
    Object localObject1 = getFileStartsWith(getLauncherResFolderPath(), "color_id_mapping_rules.json", true);
    if ((localObject1 != null) && (localObject1.length > 0))
    {
      localObject1 = getJSONObjectFromFile(localObject1[0].toString());
      if (localObject1 != null) {
        try
        {
          boolean bool = ((JSONObject)localObject1).optBoolean("enable_multiple_rules", false);
          JSONArray localJSONArray = ((JSONObject)localObject1).getJSONArray("previous_sp_rules");
          Object localObject2 = paramString;
          int i = 0;
          for (;;)
          {
            localObject1 = localObject2;
            if (i >= localJSONArray.length()) {
              break;
            }
            localObject1 = localJSONArray.getJSONObject(i);
            int j = ((JSONObject)localObject1).optInt("type", -1);
            String str1 = ((JSONObject)localObject1).getString("find").toLowerCase();
            String str2 = ((JSONObject)localObject1).getString("replaced_by").toLowerCase();
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Read previous_sp_rules [");
            ((StringBuilder)localObject1).append(i);
            ((StringBuilder)localObject1).append("] type: ");
            ((StringBuilder)localObject1).append(j);
            ((StringBuilder)localObject1).append(", find: ");
            ((StringBuilder)localObject1).append(str1);
            ((StringBuilder)localObject1).append(", replacedBy: ");
            ((StringBuilder)localObject1).append(str2);
            Log.d("WallpaperUtilities", ((StringBuilder)localObject1).toString());
            switch (j)
            {
            default: 
              localObject1 = localObject2;
              break;
            case 2: 
              localObject1 = localObject2;
              if (((String)localObject2).length() > 1)
              {
                localObject1 = localObject2;
                if (((String)localObject2).substring(((String)localObject2).length() - 2, ((String)localObject2).length() - 1).equalsIgnoreCase(str1))
                {
                  localObject1 = new java/lang/StringBuilder;
                  ((StringBuilder)localObject1).<init>();
                  ((StringBuilder)localObject1).append(str2);
                  ((StringBuilder)localObject1).append(((String)localObject2).substring(((String)localObject2).length() - 1));
                  localObject1 = ((StringBuilder)localObject1).toString();
                }
              }
              break;
            case 1: 
              localObject1 = localObject2;
              if (((String)localObject2).substring(((String)localObject2).length() - 1).equalsIgnoreCase(str1))
              {
                localObject1 = new java/lang/StringBuilder;
                ((StringBuilder)localObject1).<init>();
                ((StringBuilder)localObject1).append(((String)localObject2).substring(0, ((String)localObject2).length() - 1));
                ((StringBuilder)localObject1).append(str2);
                localObject1 = ((StringBuilder)localObject1).toString();
              }
              break;
            case 0: 
              localObject1 = localObject2;
              if (((String)localObject2).length() > 1)
              {
                localObject1 = localObject2;
                if (((String)localObject2).substring(((String)localObject2).length() - 2).equalsIgnoreCase(str1)) {
                  localObject1 = str2;
                }
              }
              break;
            }
            if ((!bool) && (!paramString.equalsIgnoreCase((String)localObject1))) {
              break;
            }
            i++;
            localObject2 = localObject1;
          }
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Mapping ");
          ((StringBuilder)localObject2).append(paramString);
          ((StringBuilder)localObject2).append(" to ");
          ((StringBuilder)localObject2).append((String)localObject1);
          ((StringBuilder)localObject2).append(" according to previous_sp_rules, isEnableMultiRules: ");
          ((StringBuilder)localObject2).append(bool);
          Log.v("WallpaperUtilities", ((StringBuilder)localObject2).toString());
          return localObject1;
        }
        catch (JSONException localJSONException)
        {
          Log.d("WallpaperUtilities", "No Defined special mapping rules from JSONFile.");
        }
      }
    }
    return paramString;
  }
  
  private static String getRuntimeDeviceInLowerCase()
  {
    Object localObject1 = SystemProperties.get("ro.config.asuswallpaper", "");
    Object localObject2 = localObject1;
    if (!TextUtils.isEmpty((CharSequence)localObject1)) {
      localObject2 = ((String)localObject1).toLowerCase();
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("getRuntimeDevice: ");
    ((StringBuilder)localObject1).append((String)localObject2);
    Log.d("WallpaperUtilities", ((StringBuilder)localObject1).toString());
    return localObject2;
  }
  
  public static int getScreenSize(Context paramContext)
  {
    if (paramContext == null) {
      return -1;
    }
    return getResourcesgetConfigurationscreenLayout & 0xF;
  }
  
  private static String getVersatilityPath()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("/system/vendor/etc/");
    ((StringBuilder)localObject).append(SystemProperties.get("ro.config.versatility", "Generic").trim());
    ((StringBuilder)localObject).append("/");
    ((StringBuilder)localObject).append(SystemProperties.get("ro.config.CID", "").trim());
    localObject = ((StringBuilder)localObject).toString();
    if (isDirectory((String)localObject)) {
      return localObject;
    }
    if (isDirectory("/system/vendor/etc/Generic")) {
      return "/system/vendor/etc/Generic";
    }
    return null;
  }
  
  private static String getWallpaperFilePathFromADF(String paramString)
  {
    if (getVersatilityPath() == null) {
      return null;
    }
    paramString = getFileStartsWith(getWallpaperFolderPath(), paramString, true);
    if (paramString == null) {
      return null;
    }
    if (paramString.length > 0)
    {
      int i = paramString.length;
      return paramString[0].toString();
    }
    return null;
  }
  
  private static File[] getWallpaperFileStartsWith(String paramString)
  {
    String str = getLauncherResFolderPath();
    Object localObject = getLauncherResFolderCIDPath();
    if (str.equals(localObject)) {
      return searchEtcResWallpaperFile(str, paramString);
    }
    localObject = searchEtcResWallpaperFile((String)localObject, paramString);
    if (localObject != null) {
      return localObject;
    }
    localObject = getLauncherResFolderRuntimeDevicePath();
    if (str.equals(localObject)) {
      return searchEtcResWallpaperFile(str, paramString);
    }
    localObject = searchEtcResWallpaperFile((String)localObject, paramString);
    if (localObject != null) {
      return localObject;
    }
    return searchEtcResWallpaperFile(str, paramString);
  }
  
  private static String getWallpaperFolderPath()
  {
    String str = getVersatilityPath();
    if (TextUtils.isEmpty(str))
    {
      str = null;
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str);
      localStringBuilder.append("/Launcher/wallpapers");
      str = localStringBuilder.toString();
    }
    return str;
  }
  
  /* Error */
  private static String[] getWallpaperListInOrder(Context paramContext, String paramString1, String paramString2)
  {
    // Byte code:
    //   0: invokestatic 197	android/app/WallpaperUtilities:isSupportDds	()Z
    //   3: ifeq +17 -> 20
    //   6: aload_0
    //   7: invokestatic 201	android/app/WallpaperUtilities:isDdsPad	(Landroid/content/Context;)Z
    //   10: ifeq +10 -> 20
    //   13: ldc_w 472
    //   16: astore_3
    //   17: goto +7 -> 24
    //   20: ldc_w 474
    //   23: astore_3
    //   24: new 172	java/util/ArrayList
    //   27: dup
    //   28: invokespecial 355	java/util/ArrayList:<init>	()V
    //   31: astore 4
    //   33: aload_2
    //   34: aload_1
    //   35: iconst_1
    //   36: invokestatic 226	android/app/WallpaperUtilities:getFileStartsWith	(Ljava/lang/String;Ljava/lang/String;Z)[Ljava/io/File;
    //   39: astore 5
    //   41: aconst_null
    //   42: astore 6
    //   44: aload 5
    //   46: ifnonnull +5 -> 51
    //   49: aconst_null
    //   50: areturn
    //   51: aload 5
    //   53: arraylength
    //   54: ifle +511 -> 565
    //   57: aconst_null
    //   58: astore 7
    //   60: aconst_null
    //   61: astore 8
    //   63: aconst_null
    //   64: astore 9
    //   66: aconst_null
    //   67: astore 10
    //   69: aload 10
    //   71: astore_0
    //   72: aload 7
    //   74: astore_2
    //   75: aload 8
    //   77: astore 11
    //   79: aload 9
    //   81: astore 12
    //   83: new 114	java/io/BufferedInputStream
    //   86: astore_1
    //   87: aload 10
    //   89: astore_0
    //   90: aload 7
    //   92: astore_2
    //   93: aload 8
    //   95: astore 11
    //   97: aload 9
    //   99: astore 12
    //   101: new 109	java/io/FileInputStream
    //   104: astore 13
    //   106: aload 10
    //   108: astore_0
    //   109: aload 7
    //   111: astore_2
    //   112: aload 8
    //   114: astore 11
    //   116: aload 9
    //   118: astore 12
    //   120: aload 13
    //   122: aload 5
    //   124: iconst_0
    //   125: aaload
    //   126: invokespecial 477	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   129: aload 10
    //   131: astore_0
    //   132: aload 7
    //   134: astore_2
    //   135: aload 8
    //   137: astore 11
    //   139: aload 9
    //   141: astore 12
    //   143: aload_1
    //   144: aload 13
    //   146: invokespecial 117	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   149: aload_1
    //   150: astore_0
    //   151: aload_1
    //   152: astore_2
    //   153: aload_1
    //   154: astore 11
    //   156: aload_1
    //   157: astore 12
    //   159: invokestatic 483	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   162: astore 10
    //   164: aload_1
    //   165: astore_0
    //   166: aload_1
    //   167: astore_2
    //   168: aload_1
    //   169: astore 11
    //   171: aload_1
    //   172: astore 12
    //   174: aload 10
    //   176: ldc_w 485
    //   179: iconst_0
    //   180: invokeinterface 491 3 0
    //   185: aload_1
    //   186: astore_0
    //   187: aload_1
    //   188: astore_2
    //   189: aload_1
    //   190: astore 11
    //   192: aload_1
    //   193: astore 12
    //   195: aload 10
    //   197: aload_1
    //   198: aconst_null
    //   199: invokeinterface 495 3 0
    //   204: aload_1
    //   205: astore_0
    //   206: aload_1
    //   207: astore_2
    //   208: aload_1
    //   209: astore 11
    //   211: aload_1
    //   212: astore 12
    //   214: aload 10
    //   216: invokeinterface 498 1 0
    //   221: istore 14
    //   223: iload 14
    //   225: iconst_1
    //   226: if_icmpeq +230 -> 456
    //   229: aload_1
    //   230: astore_0
    //   231: aload_1
    //   232: astore_2
    //   233: aload_1
    //   234: astore 11
    //   236: aload_1
    //   237: astore 12
    //   239: ldc_w 500
    //   242: aload 10
    //   244: invokeinterface 503 1 0
    //   249: invokevirtual 166	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   252: ifeq +182 -> 434
    //   255: aload_1
    //   256: astore_0
    //   257: aload_1
    //   258: astore_2
    //   259: aload_1
    //   260: astore 11
    //   262: aload_1
    //   263: astore 12
    //   265: aload 10
    //   267: invokeinterface 498 1 0
    //   272: iconst_2
    //   273: if_icmpne +161 -> 434
    //   276: aload_1
    //   277: astore_0
    //   278: aload_1
    //   279: astore_2
    //   280: aload_1
    //   281: astore 11
    //   283: aload_1
    //   284: astore 12
    //   286: aload_3
    //   287: aload 10
    //   289: aconst_null
    //   290: ldc_w 505
    //   293: invokeinterface 508 3 0
    //   298: invokevirtual 166	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   301: ifeq +133 -> 434
    //   304: aload_1
    //   305: astore_0
    //   306: aload_1
    //   307: astore_2
    //   308: aload_1
    //   309: astore 11
    //   311: aload_1
    //   312: astore 12
    //   314: aload 10
    //   316: invokeinterface 511 1 0
    //   321: iconst_2
    //   322: if_icmpne +112 -> 434
    //   325: aload_1
    //   326: astore_0
    //   327: aload_1
    //   328: astore_2
    //   329: aload_1
    //   330: astore 11
    //   332: aload_1
    //   333: astore 12
    //   335: aload 10
    //   337: iconst_2
    //   338: aconst_null
    //   339: ldc_w 513
    //   342: invokeinterface 517 4 0
    //   347: aload_1
    //   348: astore_0
    //   349: aload_1
    //   350: astore_2
    //   351: aload_1
    //   352: astore 11
    //   354: aload_1
    //   355: astore 12
    //   357: aload 4
    //   359: aload 10
    //   361: invokeinterface 520 1 0
    //   366: invokevirtual 364	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   369: pop
    //   370: aload_1
    //   371: astore_0
    //   372: aload_1
    //   373: astore_2
    //   374: aload_1
    //   375: astore 11
    //   377: aload_1
    //   378: astore 12
    //   380: aload 10
    //   382: invokeinterface 498 1 0
    //   387: iconst_3
    //   388: if_icmpeq +21 -> 409
    //   391: aload_1
    //   392: astore_0
    //   393: aload_1
    //   394: astore_2
    //   395: aload_1
    //   396: astore 11
    //   398: aload_1
    //   399: astore 12
    //   401: aload 10
    //   403: invokeinterface 511 1 0
    //   408: pop
    //   409: aload_1
    //   410: astore_0
    //   411: aload_1
    //   412: astore_2
    //   413: aload_1
    //   414: astore 11
    //   416: aload_1
    //   417: astore 12
    //   419: aload 10
    //   421: iconst_3
    //   422: aconst_null
    //   423: ldc_w 513
    //   426: invokeinterface 517 4 0
    //   431: goto -127 -> 304
    //   434: aload_1
    //   435: astore_0
    //   436: aload_1
    //   437: astore_2
    //   438: aload_1
    //   439: astore 11
    //   441: aload_1
    //   442: astore 12
    //   444: aload 10
    //   446: invokeinterface 522 1 0
    //   451: istore 14
    //   453: goto -230 -> 223
    //   456: aload_1
    //   457: invokevirtual 525	java/io/InputStream:close	()V
    //   460: goto +64 -> 524
    //   463: astore_1
    //   464: goto +77 -> 541
    //   467: astore_1
    //   468: aload_2
    //   469: astore_0
    //   470: aload_1
    //   471: invokevirtual 526	org/xmlpull/v1/XmlPullParserException:printStackTrace	()V
    //   474: aload_2
    //   475: ifnull +90 -> 565
    //   478: aload_2
    //   479: invokevirtual 525	java/io/InputStream:close	()V
    //   482: goto +42 -> 524
    //   485: astore_1
    //   486: aload 11
    //   488: astore_0
    //   489: aload_1
    //   490: invokevirtual 284	java/io/IOException:printStackTrace	()V
    //   493: aload 11
    //   495: ifnull +70 -> 565
    //   498: aload 11
    //   500: invokevirtual 525	java/io/InputStream:close	()V
    //   503: goto +21 -> 524
    //   506: astore_1
    //   507: aload 12
    //   509: astore_0
    //   510: aload_1
    //   511: invokevirtual 285	java/io/FileNotFoundException:printStackTrace	()V
    //   514: aload 12
    //   516: ifnull +49 -> 565
    //   519: aload 12
    //   521: invokevirtual 525	java/io/InputStream:close	()V
    //   524: goto +41 -> 565
    //   527: astore_0
    //   528: ldc 49
    //   530: ldc_w 276
    //   533: aload_0
    //   534: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   537: pop
    //   538: goto -14 -> 524
    //   541: aload_0
    //   542: ifnull +21 -> 563
    //   545: aload_0
    //   546: invokevirtual 525	java/io/InputStream:close	()V
    //   549: goto +14 -> 563
    //   552: astore_0
    //   553: ldc 49
    //   555: ldc_w 276
    //   558: aload_0
    //   559: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   562: pop
    //   563: aload_1
    //   564: athrow
    //   565: aload 4
    //   567: invokevirtual 528	java/util/ArrayList:isEmpty	()Z
    //   570: ifeq +9 -> 579
    //   573: aload 6
    //   575: astore_0
    //   576: goto +20 -> 596
    //   579: aload 4
    //   581: aload 4
    //   583: invokevirtual 531	java/util/ArrayList:size	()I
    //   586: anewarray 146	java/lang/String
    //   589: invokevirtual 535	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   592: checkcast 537	[Ljava/lang/String;
    //   595: astore_0
    //   596: aload_0
    //   597: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	598	0	paramContext	Context
    //   0	598	1	paramString1	String
    //   0	598	2	paramString2	String
    //   16	271	3	str	String
    //   31	551	4	localArrayList	ArrayList
    //   39	84	5	arrayOfFile	File[]
    //   42	532	6	localObject1	Object
    //   58	75	7	localObject2	Object
    //   61	75	8	localObject3	Object
    //   64	76	9	localObject4	Object
    //   67	378	10	localXmlPullParser	org.xmlpull.v1.XmlPullParser
    //   77	422	11	localObject5	Object
    //   81	439	12	localObject6	Object
    //   104	41	13	localFileInputStream	FileInputStream
    //   221	231	14	i	int
    // Exception table:
    //   from	to	target	type
    //   83	87	463	finally
    //   101	106	463	finally
    //   120	129	463	finally
    //   143	149	463	finally
    //   159	164	463	finally
    //   174	185	463	finally
    //   195	204	463	finally
    //   214	223	463	finally
    //   239	255	463	finally
    //   265	276	463	finally
    //   286	304	463	finally
    //   314	325	463	finally
    //   335	347	463	finally
    //   357	370	463	finally
    //   380	391	463	finally
    //   401	409	463	finally
    //   419	431	463	finally
    //   444	453	463	finally
    //   470	474	463	finally
    //   489	493	463	finally
    //   510	514	463	finally
    //   83	87	467	org/xmlpull/v1/XmlPullParserException
    //   101	106	467	org/xmlpull/v1/XmlPullParserException
    //   120	129	467	org/xmlpull/v1/XmlPullParserException
    //   143	149	467	org/xmlpull/v1/XmlPullParserException
    //   159	164	467	org/xmlpull/v1/XmlPullParserException
    //   174	185	467	org/xmlpull/v1/XmlPullParserException
    //   195	204	467	org/xmlpull/v1/XmlPullParserException
    //   214	223	467	org/xmlpull/v1/XmlPullParserException
    //   239	255	467	org/xmlpull/v1/XmlPullParserException
    //   265	276	467	org/xmlpull/v1/XmlPullParserException
    //   286	304	467	org/xmlpull/v1/XmlPullParserException
    //   314	325	467	org/xmlpull/v1/XmlPullParserException
    //   335	347	467	org/xmlpull/v1/XmlPullParserException
    //   357	370	467	org/xmlpull/v1/XmlPullParserException
    //   380	391	467	org/xmlpull/v1/XmlPullParserException
    //   401	409	467	org/xmlpull/v1/XmlPullParserException
    //   419	431	467	org/xmlpull/v1/XmlPullParserException
    //   444	453	467	org/xmlpull/v1/XmlPullParserException
    //   83	87	485	java/io/IOException
    //   101	106	485	java/io/IOException
    //   120	129	485	java/io/IOException
    //   143	149	485	java/io/IOException
    //   159	164	485	java/io/IOException
    //   174	185	485	java/io/IOException
    //   195	204	485	java/io/IOException
    //   214	223	485	java/io/IOException
    //   239	255	485	java/io/IOException
    //   265	276	485	java/io/IOException
    //   286	304	485	java/io/IOException
    //   314	325	485	java/io/IOException
    //   335	347	485	java/io/IOException
    //   357	370	485	java/io/IOException
    //   380	391	485	java/io/IOException
    //   401	409	485	java/io/IOException
    //   419	431	485	java/io/IOException
    //   444	453	485	java/io/IOException
    //   83	87	506	java/io/FileNotFoundException
    //   101	106	506	java/io/FileNotFoundException
    //   120	129	506	java/io/FileNotFoundException
    //   143	149	506	java/io/FileNotFoundException
    //   159	164	506	java/io/FileNotFoundException
    //   174	185	506	java/io/FileNotFoundException
    //   195	204	506	java/io/FileNotFoundException
    //   214	223	506	java/io/FileNotFoundException
    //   239	255	506	java/io/FileNotFoundException
    //   265	276	506	java/io/FileNotFoundException
    //   286	304	506	java/io/FileNotFoundException
    //   314	325	506	java/io/FileNotFoundException
    //   335	347	506	java/io/FileNotFoundException
    //   357	370	506	java/io/FileNotFoundException
    //   380	391	506	java/io/FileNotFoundException
    //   401	409	506	java/io/FileNotFoundException
    //   419	431	506	java/io/FileNotFoundException
    //   444	453	506	java/io/FileNotFoundException
    //   456	460	527	java/io/IOException
    //   478	482	527	java/io/IOException
    //   498	503	527	java/io/IOException
    //   519	524	527	java/io/IOException
    //   545	549	552	java/io/IOException
  }
  
  public static boolean isDdsPad(Context paramContext)
  {
    boolean bool;
    if ((isSupportDds()) && (isScreenSizePad(getScreenSize(paramContext)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isDirectory(String paramString)
  {
    boolean bool;
    if (TextUtils.isEmpty(paramString)) {
      bool = false;
    } else {
      bool = new File(paramString).isDirectory();
    }
    return bool;
  }
  
  public static boolean isScreenSizePad(int paramInt)
  {
    boolean bool;
    if (paramInt > 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isSupportDds()
  {
    boolean bool = false;
    if (SystemProperties.getInt("persist.sys.padfone", 0) == 1) {
      bool = true;
    }
    return bool;
  }
  
  /* Error */
  public static boolean isZf2List()
  {
    // Byte code:
    //   0: invokestatic 222	android/app/WallpaperUtilities:getLauncherResFolderPath	()Ljava/lang/String;
    //   3: ldc_w 553
    //   6: iconst_1
    //   7: invokestatic 226	android/app/WallpaperUtilities:getFileStartsWith	(Ljava/lang/String;Ljava/lang/String;Z)[Ljava/io/File;
    //   10: astore_0
    //   11: aload_0
    //   12: ifnull +521 -> 533
    //   15: aload_0
    //   16: arraylength
    //   17: ifne +6 -> 23
    //   20: goto +513 -> 533
    //   23: aload_0
    //   24: arraylength
    //   25: ifle +506 -> 531
    //   28: aconst_null
    //   29: astore_1
    //   30: aconst_null
    //   31: astore_2
    //   32: aconst_null
    //   33: astore_3
    //   34: aconst_null
    //   35: astore 4
    //   37: aload 4
    //   39: astore 5
    //   41: aload_1
    //   42: astore 6
    //   44: aload_2
    //   45: astore 7
    //   47: aload_3
    //   48: astore 8
    //   50: new 114	java/io/BufferedInputStream
    //   53: astore 9
    //   55: aload 4
    //   57: astore 5
    //   59: aload_1
    //   60: astore 6
    //   62: aload_2
    //   63: astore 7
    //   65: aload_3
    //   66: astore 8
    //   68: new 109	java/io/FileInputStream
    //   71: astore 10
    //   73: aload 4
    //   75: astore 5
    //   77: aload_1
    //   78: astore 6
    //   80: aload_2
    //   81: astore 7
    //   83: aload_3
    //   84: astore 8
    //   86: aload 10
    //   88: aload_0
    //   89: iconst_0
    //   90: aaload
    //   91: invokespecial 477	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   94: aload 4
    //   96: astore 5
    //   98: aload_1
    //   99: astore 6
    //   101: aload_2
    //   102: astore 7
    //   104: aload_3
    //   105: astore 8
    //   107: aload 9
    //   109: aload 10
    //   111: invokespecial 117	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   114: aload 9
    //   116: astore 4
    //   118: aload 4
    //   120: astore 5
    //   122: aload 4
    //   124: astore 6
    //   126: aload 4
    //   128: astore 7
    //   130: aload 4
    //   132: astore 8
    //   134: invokestatic 483	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   137: astore 9
    //   139: aload 4
    //   141: astore 5
    //   143: aload 4
    //   145: astore 6
    //   147: aload 4
    //   149: astore 7
    //   151: aload 4
    //   153: astore 8
    //   155: aload 9
    //   157: ldc_w 485
    //   160: iconst_0
    //   161: invokeinterface 491 3 0
    //   166: aload 4
    //   168: astore 5
    //   170: aload 4
    //   172: astore 6
    //   174: aload 4
    //   176: astore 7
    //   178: aload 4
    //   180: astore 8
    //   182: aload 9
    //   184: aload 4
    //   186: aconst_null
    //   187: invokeinterface 495 3 0
    //   192: aload 4
    //   194: astore 5
    //   196: aload 4
    //   198: astore 6
    //   200: aload 4
    //   202: astore 7
    //   204: aload 4
    //   206: astore 8
    //   208: aload 9
    //   210: invokeinterface 498 1 0
    //   215: istore 11
    //   217: iload 11
    //   219: iconst_1
    //   220: if_icmpeq +181 -> 401
    //   223: aload 4
    //   225: astore 5
    //   227: aload 4
    //   229: astore 6
    //   231: aload 4
    //   233: astore 7
    //   235: aload 4
    //   237: astore 8
    //   239: ldc_w 555
    //   242: aload 9
    //   244: invokeinterface 503 1 0
    //   249: invokevirtual 166	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   252: ifeq +121 -> 373
    //   255: aload 4
    //   257: astore 5
    //   259: aload 4
    //   261: astore 6
    //   263: aload 4
    //   265: astore 7
    //   267: aload 4
    //   269: astore 8
    //   271: aload 9
    //   273: invokeinterface 498 1 0
    //   278: iconst_2
    //   279: if_icmpne +94 -> 373
    //   282: aload 4
    //   284: astore 5
    //   286: aload 4
    //   288: astore 6
    //   290: aload 4
    //   292: astore 7
    //   294: aload 4
    //   296: astore 8
    //   298: ldc_w 557
    //   301: aload 9
    //   303: aconst_null
    //   304: ldc_w 505
    //   307: invokeinterface 508 3 0
    //   312: invokevirtual 166	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   315: ifeq +58 -> 373
    //   318: aload 4
    //   320: astore 5
    //   322: aload 4
    //   324: astore 6
    //   326: aload 4
    //   328: astore 7
    //   330: aload 4
    //   332: astore 8
    //   334: ldc_w 559
    //   337: aload 9
    //   339: invokeinterface 520 1 0
    //   344: invokevirtual 166	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   347: istore 12
    //   349: aload 4
    //   351: invokevirtual 525	java/io/InputStream:close	()V
    //   354: goto +16 -> 370
    //   357: astore 5
    //   359: ldc 49
    //   361: ldc_w 276
    //   364: aload 5
    //   366: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   369: pop
    //   370: iload 12
    //   372: ireturn
    //   373: aload 4
    //   375: astore 5
    //   377: aload 4
    //   379: astore 6
    //   381: aload 4
    //   383: astore 7
    //   385: aload 4
    //   387: astore 8
    //   389: aload 9
    //   391: invokeinterface 522 1 0
    //   396: istore 11
    //   398: goto -181 -> 217
    //   401: aload 4
    //   403: invokevirtual 525	java/io/InputStream:close	()V
    //   406: goto +77 -> 483
    //   409: astore 4
    //   411: goto +91 -> 502
    //   414: astore 4
    //   416: aload 6
    //   418: astore 5
    //   420: aload 4
    //   422: invokevirtual 526	org/xmlpull/v1/XmlPullParserException:printStackTrace	()V
    //   425: aload 6
    //   427: ifnull +104 -> 531
    //   430: aload 6
    //   432: invokevirtual 525	java/io/InputStream:close	()V
    //   435: goto +48 -> 483
    //   438: astore 4
    //   440: aload 7
    //   442: astore 5
    //   444: aload 4
    //   446: invokevirtual 284	java/io/IOException:printStackTrace	()V
    //   449: aload 7
    //   451: ifnull +80 -> 531
    //   454: aload 7
    //   456: invokevirtual 525	java/io/InputStream:close	()V
    //   459: goto +24 -> 483
    //   462: astore 4
    //   464: aload 8
    //   466: astore 5
    //   468: aload 4
    //   470: invokevirtual 285	java/io/FileNotFoundException:printStackTrace	()V
    //   473: aload 8
    //   475: ifnull +56 -> 531
    //   478: aload 8
    //   480: invokevirtual 525	java/io/InputStream:close	()V
    //   483: goto +48 -> 531
    //   486: astore 5
    //   488: ldc 49
    //   490: ldc_w 276
    //   493: aload 5
    //   495: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   498: pop
    //   499: goto -16 -> 483
    //   502: aload 5
    //   504: ifnull +24 -> 528
    //   507: aload 5
    //   509: invokevirtual 525	java/io/InputStream:close	()V
    //   512: goto +16 -> 528
    //   515: astore 5
    //   517: ldc 49
    //   519: ldc_w 276
    //   522: aload 5
    //   524: invokestatic 280	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   527: pop
    //   528: aload 4
    //   530: athrow
    //   531: iconst_0
    //   532: ireturn
    //   533: iconst_0
    //   534: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   10	79	0	arrayOfFile	File[]
    //   29	70	1	localObject1	Object
    //   31	71	2	localObject2	Object
    //   33	72	3	localObject3	Object
    //   35	367	4	localObject4	Object
    //   409	1	4	localObject5	Object
    //   414	7	4	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    //   438	7	4	localIOException1	java.io.IOException
    //   462	67	4	localFileNotFoundException	FileNotFoundException
    //   39	282	5	localObject6	Object
    //   357	8	5	localIOException2	java.io.IOException
    //   375	92	5	localObject7	Object
    //   486	22	5	localIOException3	java.io.IOException
    //   515	8	5	localIOException4	java.io.IOException
    //   42	389	6	localObject8	Object
    //   45	410	7	localObject9	Object
    //   48	431	8	localObject10	Object
    //   53	337	9	localObject11	Object
    //   71	39	10	localFileInputStream	FileInputStream
    //   215	182	11	i	int
    //   347	24	12	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   349	354	357	java/io/IOException
    //   50	55	409	finally
    //   68	73	409	finally
    //   86	94	409	finally
    //   107	114	409	finally
    //   134	139	409	finally
    //   155	166	409	finally
    //   182	192	409	finally
    //   208	217	409	finally
    //   239	255	409	finally
    //   271	282	409	finally
    //   298	318	409	finally
    //   334	349	409	finally
    //   389	398	409	finally
    //   420	425	409	finally
    //   444	449	409	finally
    //   468	473	409	finally
    //   50	55	414	org/xmlpull/v1/XmlPullParserException
    //   68	73	414	org/xmlpull/v1/XmlPullParserException
    //   86	94	414	org/xmlpull/v1/XmlPullParserException
    //   107	114	414	org/xmlpull/v1/XmlPullParserException
    //   134	139	414	org/xmlpull/v1/XmlPullParserException
    //   155	166	414	org/xmlpull/v1/XmlPullParserException
    //   182	192	414	org/xmlpull/v1/XmlPullParserException
    //   208	217	414	org/xmlpull/v1/XmlPullParserException
    //   239	255	414	org/xmlpull/v1/XmlPullParserException
    //   271	282	414	org/xmlpull/v1/XmlPullParserException
    //   298	318	414	org/xmlpull/v1/XmlPullParserException
    //   334	349	414	org/xmlpull/v1/XmlPullParserException
    //   389	398	414	org/xmlpull/v1/XmlPullParserException
    //   50	55	438	java/io/IOException
    //   68	73	438	java/io/IOException
    //   86	94	438	java/io/IOException
    //   107	114	438	java/io/IOException
    //   134	139	438	java/io/IOException
    //   155	166	438	java/io/IOException
    //   182	192	438	java/io/IOException
    //   208	217	438	java/io/IOException
    //   239	255	438	java/io/IOException
    //   271	282	438	java/io/IOException
    //   298	318	438	java/io/IOException
    //   334	349	438	java/io/IOException
    //   389	398	438	java/io/IOException
    //   50	55	462	java/io/FileNotFoundException
    //   68	73	462	java/io/FileNotFoundException
    //   86	94	462	java/io/FileNotFoundException
    //   107	114	462	java/io/FileNotFoundException
    //   134	139	462	java/io/FileNotFoundException
    //   155	166	462	java/io/FileNotFoundException
    //   182	192	462	java/io/FileNotFoundException
    //   208	217	462	java/io/FileNotFoundException
    //   239	255	462	java/io/FileNotFoundException
    //   271	282	462	java/io/FileNotFoundException
    //   298	318	462	java/io/FileNotFoundException
    //   334	349	462	java/io/FileNotFoundException
    //   389	398	462	java/io/FileNotFoundException
    //   401	406	486	java/io/IOException
    //   430	435	486	java/io/IOException
    //   454	459	486	java/io/IOException
    //   478	483	486	java/io/IOException
    //   507	512	515	java/io/IOException
  }
  
  private static String mappingColorIdCodeForZf2(String paramString)
  {
    Object localObject1 = paramString;
    Object localObject2 = localObject1;
    if (isZf2List())
    {
      if (paramString.length() > 1) {
        localObject1 = paramString.substring(paramString.length() - 1);
      }
      localObject2 = localObject1;
      if ("b".equals(localObject1)) {
        localObject2 = "a";
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(1);
      ((StringBuilder)localObject1).append((String)localObject2);
      localObject2 = ((StringBuilder)localObject1).toString();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("mappingIdCode from ");
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append(" to ");
      ((StringBuilder)localObject1).append((String)localObject2);
      Log.v("WallpaperUtilities", ((StringBuilder)localObject1).toString());
    }
    return localObject2;
  }
  
  private static File[] searchEtcResWallpaperFile(String paramString1, String paramString2)
  {
    File[] arrayOfFile = getFileStartsWith(paramString1, paramString2, true);
    if ((arrayOfFile != null) && (arrayOfFile.length > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("searchEtcResWallpaperFile: ");
      localStringBuilder.append(paramString2);
      localStringBuilder.append(", return from ");
      localStringBuilder.append(paramString1);
      Log.d("WallpaperUtilities", localStringBuilder.toString());
      return arrayOfFile;
    }
    return null;
  }
}
