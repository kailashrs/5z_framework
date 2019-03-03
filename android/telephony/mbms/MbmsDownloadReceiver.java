package android.telephony.mbms;

import android.annotation.SystemApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MbmsDownloadReceiver
  extends BroadcastReceiver
{
  public static final String DOWNLOAD_TOKEN_SUFFIX = ".download_token";
  private static final String EMBMS_INTENT_PERMISSION = "android.permission.SEND_EMBMS_INTENTS";
  private static final String LOG_TAG = "MbmsDownloadReceiver";
  private static final int MAX_TEMP_FILE_RETRIES = 5;
  public static final String MBMS_FILE_PROVIDER_META_DATA_KEY = "mbms-file-provider-authority";
  @SystemApi
  public static final int RESULT_APP_NOTIFICATION_ERROR = 6;
  @SystemApi
  public static final int RESULT_BAD_TEMP_FILE_ROOT = 3;
  @SystemApi
  public static final int RESULT_DOWNLOAD_FINALIZATION_ERROR = 4;
  @SystemApi
  public static final int RESULT_INVALID_ACTION = 1;
  @SystemApi
  public static final int RESULT_MALFORMED_INTENT = 2;
  @SystemApi
  public static final int RESULT_OK = 0;
  @SystemApi
  public static final int RESULT_TEMP_FILE_GENERATION_ERROR = 5;
  private static final String TEMP_FILE_STAGING_LOCATION = "staged_completed_files";
  private static final String TEMP_FILE_SUFFIX = ".embms.temp";
  private String mFileProviderAuthorityCache = null;
  private String mMiddlewarePackageNameCache = null;
  
  public MbmsDownloadReceiver() {}
  
  private void cleanupPostMove(Context paramContext, Intent paramIntent)
  {
    DownloadRequest localDownloadRequest = (DownloadRequest)paramIntent.getParcelableExtra("android.telephony.extra.MBMS_DOWNLOAD_REQUEST");
    if (localDownloadRequest == null)
    {
      Log.w("MbmsDownloadReceiver", "Intent does not include a DownloadRequest. Ignoring.");
      return;
    }
    paramIntent = paramIntent.getParcelableArrayListExtra("android.telephony.mbms.extra.TEMP_LIST");
    if (paramIntent == null) {
      return;
    }
    paramIntent = paramIntent.iterator();
    while (paramIntent.hasNext())
    {
      Object localObject = (Uri)paramIntent.next();
      if (verifyTempFilePath(paramContext, localDownloadRequest.getFileServiceId(), (Uri)localObject))
      {
        File localFile = new File(((Uri)localObject).getSchemeSpecificPart());
        if (!localFile.delete())
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Failed to delete temp file at ");
          ((StringBuilder)localObject).append(localFile.getPath());
          Log.w("MbmsDownloadReceiver", ((StringBuilder)localObject).toString());
        }
      }
    }
  }
  
  private void cleanupTempFiles(Context paramContext, Intent paramIntent)
  {
    paramContext = MbmsUtils.getEmbmsTempFileDirForService(paramContext, paramIntent.getStringExtra("android.telephony.mbms.extra.SERVICE_ID")).listFiles(new FileFilter()
    {
      public boolean accept(File paramAnonymousFile)
      {
        try
        {
          File localFile = paramAnonymousFile.getCanonicalFile();
          if (!localFile.getName().endsWith(".embms.temp")) {
            return false;
          }
          paramAnonymousFile = Uri.fromFile(localFile);
          return val$filesInUse.contains(paramAnonymousFile) ^ true;
        }
        catch (IOException localIOException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Got IOException canonicalizing ");
          localStringBuilder.append(paramAnonymousFile);
          localStringBuilder.append(", not deleting.");
          Log.w("MbmsDownloadReceiver", localStringBuilder.toString());
        }
        return false;
      }
    });
    int i = paramContext.length;
    for (int j = 0; j < i; j++) {
      paramContext[j].delete();
    }
  }
  
  private ArrayList<UriPathPair> generateFreshTempFiles(Context paramContext, String paramString, int paramInt)
  {
    File localFile = MbmsUtils.getEmbmsTempFileDirForService(paramContext, paramString);
    if (!localFile.exists()) {
      localFile.mkdirs();
    }
    paramString = new ArrayList(paramInt);
    for (int i = 0; i < paramInt; i++)
    {
      Object localObject = generateSingleTempFile(localFile);
      if (localObject == null)
      {
        setResultCode(5);
        Log.w("MbmsDownloadReceiver", "Failed to generate a temp file. Moving on.");
      }
      else
      {
        Uri localUri = Uri.fromFile((File)localObject);
        localObject = MbmsTempFileProvider.getUriForFile(paramContext, getFileProviderAuthorityCached(paramContext), (File)localObject);
        paramContext.grantUriPermission(getMiddlewarePackageCached(paramContext), (Uri)localObject, 3);
        paramString.add(new UriPathPair(localUri, (Uri)localObject));
      }
    }
    return paramString;
  }
  
  private static File generateSingleTempFile(File paramFile)
  {
    int i = 0;
    while (i < 5)
    {
      i++;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(UUID.randomUUID());
      ((StringBuilder)localObject).append(".embms.temp");
      localObject = new File(paramFile, ((StringBuilder)localObject).toString());
      try
      {
        if (((File)localObject).createNewFile())
        {
          localObject = ((File)localObject).getCanonicalFile();
          return localObject;
        }
      }
      catch (IOException localIOException) {}
    }
    return null;
  }
  
  private void generateTempFiles(Context paramContext, Intent paramIntent)
  {
    Object localObject = paramIntent.getStringExtra("android.telephony.mbms.extra.SERVICE_ID");
    if (localObject == null)
    {
      Log.w("MbmsDownloadReceiver", "Temp file request did not include the associated service id. Ignoring.");
      setResultCode(2);
      return;
    }
    int i = paramIntent.getIntExtra("android.telephony.mbms.extra.FD_COUNT", 0);
    ArrayList localArrayList = paramIntent.getParcelableArrayListExtra("android.telephony.mbms.extra.PAUSED_LIST");
    if ((i == 0) && ((localArrayList == null) || (localArrayList.size() == 0)))
    {
      Log.i("MbmsDownloadReceiver", "No temp files actually requested. Ending.");
      setResultCode(0);
      setResultExtras(Bundle.EMPTY);
      return;
    }
    paramIntent = generateFreshTempFiles(paramContext, (String)localObject, i);
    paramContext = generateUrisForPausedFiles(paramContext, (String)localObject, localArrayList);
    localObject = new Bundle();
    ((Bundle)localObject).putParcelableArrayList("android.telephony.mbms.extra.FREE_URI_LIST", paramIntent);
    ((Bundle)localObject).putParcelableArrayList("android.telephony.mbms.extra.PAUSED_URI_LIST", paramContext);
    setResultCode(0);
    setResultExtras((Bundle)localObject);
  }
  
  private ArrayList<UriPathPair> generateUrisForPausedFiles(Context paramContext, String paramString, List<Uri> paramList)
  {
    if (paramList == null) {
      return new ArrayList(0);
    }
    ArrayList localArrayList = new ArrayList(paramList.size());
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      Uri localUri = (Uri)paramList.next();
      Object localObject;
      if (!verifyTempFilePath(paramContext, paramString, localUri))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Supplied file ");
        ((StringBuilder)localObject).append(localUri);
        ((StringBuilder)localObject).append(" is not a valid temp file to resume");
        Log.w("MbmsDownloadReceiver", ((StringBuilder)localObject).toString());
        setResultCode(5);
      }
      else
      {
        localObject = new File(localUri.getSchemeSpecificPart());
        if (!((File)localObject).exists())
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Supplied file ");
          ((StringBuilder)localObject).append(localUri);
          ((StringBuilder)localObject).append(" does not exist.");
          Log.w("MbmsDownloadReceiver", ((StringBuilder)localObject).toString());
          setResultCode(5);
        }
        else
        {
          localObject = MbmsTempFileProvider.getUriForFile(paramContext, getFileProviderAuthorityCached(paramContext), (File)localObject);
          paramContext.grantUriPermission(getMiddlewarePackageCached(paramContext), (Uri)localObject, 3);
          localArrayList.add(new UriPathPair(localUri, (Uri)localObject));
        }
      }
    }
    return localArrayList;
  }
  
  private static String getFileProviderAuthority(Context paramContext)
  {
    try
    {
      ApplicationInfo localApplicationInfo = paramContext.getPackageManager().getApplicationInfo(paramContext.getPackageName(), 128);
      if (metaData != null)
      {
        paramContext = metaData.getString("mbms-file-provider-authority");
        if (paramContext != null) {
          return paramContext;
        }
        throw new RuntimeException("App must declare the file provider authority as metadata in the manifest.");
      }
      throw new RuntimeException("App must declare the file provider authority as metadata in the manifest.");
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Package manager couldn't find ");
      localStringBuilder.append(paramContext.getPackageName());
      throw new RuntimeException(localStringBuilder.toString());
    }
  }
  
  private String getFileProviderAuthorityCached(Context paramContext)
  {
    if (mFileProviderAuthorityCache != null) {
      return mFileProviderAuthorityCache;
    }
    mFileProviderAuthorityCache = getFileProviderAuthority(paramContext);
    return mFileProviderAuthorityCache;
  }
  
  @VisibleForTesting
  public static String getFileRelativePath(String paramString1, String paramString2)
  {
    String str = paramString1;
    if (paramString1.endsWith("*")) {
      str = paramString1.substring(0, paramString1.lastIndexOf('/'));
    }
    if (!paramString2.startsWith(str))
    {
      paramString1 = new StringBuilder();
      paramString1.append("File location specified in FileInfo does not match the source URI. source: ");
      paramString1.append(str);
      paramString1.append(" fileinfo path: ");
      paramString1.append(paramString2);
      Log.e("MbmsDownloadReceiver", paramString1.toString());
      return null;
    }
    if (paramString2.length() == str.length()) {
      return str.substring(str.lastIndexOf('/') + 1);
    }
    paramString2 = paramString2.substring(str.length());
    paramString1 = paramString2;
    if (paramString2.startsWith("/")) {
      paramString1 = paramString2.substring(1);
    }
    return paramString1;
  }
  
  private String getMiddlewarePackageCached(Context paramContext)
  {
    if (mMiddlewarePackageNameCache == null) {
      mMiddlewarePackageNameCache = getMiddlewareServiceInfo"android.telephony.action.EmbmsDownload"packageName;
    }
    return mMiddlewarePackageNameCache;
  }
  
  private void moveDownloadedFile(Context paramContext, Intent paramIntent)
  {
    DownloadRequest localDownloadRequest = (DownloadRequest)paramIntent.getParcelableExtra("android.telephony.extra.MBMS_DOWNLOAD_REQUEST");
    Intent localIntent = localDownloadRequest.getIntentForApp();
    if (localIntent == null)
    {
      Log.i("MbmsDownloadReceiver", "Malformed app notification intent");
      setResultCode(6);
      return;
    }
    int i = paramIntent.getIntExtra("android.telephony.extra.MBMS_DOWNLOAD_RESULT", 2);
    localIntent.putExtra("android.telephony.extra.MBMS_DOWNLOAD_RESULT", i);
    localIntent.putExtra("android.telephony.extra.MBMS_DOWNLOAD_REQUEST", localDownloadRequest);
    if (i != 1)
    {
      Log.i("MbmsDownloadReceiver", "Download request indicated a failed download. Aborting.");
      paramContext.sendBroadcast(localIntent);
      setResultCode(0);
      return;
    }
    Uri localUri = (Uri)paramIntent.getParcelableExtra("android.telephony.mbms.extra.FINAL_URI");
    if (!verifyTempFilePath(paramContext, localDownloadRequest.getFileServiceId(), localUri))
    {
      paramContext = new StringBuilder();
      paramContext.append("Download result specified an invalid temp file ");
      paramContext.append(localUri);
      Log.w("MbmsDownloadReceiver", paramContext.toString());
      setResultCode(4);
      return;
    }
    paramIntent = (FileInfo)paramIntent.getParcelableExtra("android.telephony.extra.MBMS_FILE_INFO");
    Path localPath = FileSystems.getDefault().getPath(localDownloadRequest.getDestinationUri().getPath(), new String[0]);
    try
    {
      localUri = moveToFinalLocation(localUri, localPath, getFileRelativePath(localDownloadRequest.getSourceUri().getPath(), paramIntent.getUri().getPath()));
      localIntent.putExtra("android.telephony.extra.MBMS_COMPLETED_FILE_URI", localUri);
      localIntent.putExtra("android.telephony.extra.MBMS_FILE_INFO", paramIntent);
      paramContext.sendBroadcast(localIntent);
      setResultCode(0);
      return;
    }
    catch (IOException paramContext)
    {
      Log.w("MbmsDownloadReceiver", "Failed to move temp file to final destination");
      setResultCode(4);
    }
  }
  
  private static Uri moveToFinalLocation(Uri paramUri, Path paramPath, String paramString)
    throws IOException
  {
    if (!"file".equals(paramUri.getScheme()))
    {
      paramPath = new StringBuilder();
      paramPath.append("Downloaded file location uri ");
      paramPath.append(paramUri);
      paramPath.append(" does not have a file scheme");
      Log.w("MbmsDownloadReceiver", paramPath.toString());
      return null;
    }
    paramUri = FileSystems.getDefault().getPath(paramUri.getPath(), new String[0]);
    paramPath = paramPath.resolve(paramString);
    if (!Files.isDirectory(paramPath.getParent(), new LinkOption[0])) {
      Files.createDirectories(paramPath.getParent(), new FileAttribute[0]);
    }
    return Uri.fromFile(Files.move(paramUri, paramPath, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE }).toFile());
  }
  
  private boolean verifyIntentContents(Context paramContext, Intent paramIntent)
  {
    if ("android.telephony.mbms.action.DOWNLOAD_RESULT_INTERNAL".equals(paramIntent.getAction()))
    {
      if (!paramIntent.hasExtra("android.telephony.extra.MBMS_DOWNLOAD_RESULT"))
      {
        Log.w("MbmsDownloadReceiver", "Download result did not include a result code. Ignoring.");
        return false;
      }
      if (!paramIntent.hasExtra("android.telephony.extra.MBMS_DOWNLOAD_REQUEST"))
      {
        Log.w("MbmsDownloadReceiver", "Download result did not include the associated request. Ignoring.");
        return false;
      }
      if (1 != paramIntent.getIntExtra("android.telephony.extra.MBMS_DOWNLOAD_RESULT", 2)) {
        return true;
      }
      if (!paramIntent.hasExtra("android.telephony.mbms.extra.TEMP_FILE_ROOT"))
      {
        Log.w("MbmsDownloadReceiver", "Download result did not include the temp file root. Ignoring.");
        return false;
      }
      if (!paramIntent.hasExtra("android.telephony.extra.MBMS_FILE_INFO"))
      {
        Log.w("MbmsDownloadReceiver", "Download result did not include the associated file info. Ignoring.");
        return false;
      }
      if (!paramIntent.hasExtra("android.telephony.mbms.extra.FINAL_URI"))
      {
        Log.w("MbmsDownloadReceiver", "Download result did not include the path to the final temp file. Ignoring.");
        return false;
      }
      paramIntent = (DownloadRequest)paramIntent.getParcelableExtra("android.telephony.extra.MBMS_DOWNLOAD_REQUEST");
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramIntent.getHash());
      ((StringBuilder)localObject).append(".download_token");
      localObject = ((StringBuilder)localObject).toString();
      paramIntent = new File(MbmsUtils.getEmbmsTempFileDirForService(paramContext, paramIntent.getFileServiceId()), (String)localObject);
      if (!paramIntent.exists())
      {
        paramContext = new StringBuilder();
        paramContext.append("Supplied download request does not match a token that we have. Expected ");
        paramContext.append(paramIntent);
        Log.w("MbmsDownloadReceiver", paramContext.toString());
        return false;
      }
    }
    else if ("android.telephony.mbms.action.FILE_DESCRIPTOR_REQUEST".equals(paramIntent.getAction()))
    {
      if (!paramIntent.hasExtra("android.telephony.mbms.extra.SERVICE_ID"))
      {
        Log.w("MbmsDownloadReceiver", "Temp file request did not include the associated service id. Ignoring.");
        return false;
      }
      if (!paramIntent.hasExtra("android.telephony.mbms.extra.TEMP_FILE_ROOT"))
      {
        Log.w("MbmsDownloadReceiver", "Download result did not include the temp file root. Ignoring.");
        return false;
      }
    }
    else if ("android.telephony.mbms.action.CLEANUP".equals(paramIntent.getAction()))
    {
      if (!paramIntent.hasExtra("android.telephony.mbms.extra.SERVICE_ID"))
      {
        Log.w("MbmsDownloadReceiver", "Cleanup request did not include the associated service id. Ignoring.");
        return false;
      }
      if (!paramIntent.hasExtra("android.telephony.mbms.extra.TEMP_FILE_ROOT"))
      {
        Log.w("MbmsDownloadReceiver", "Cleanup request did not include the temp file root. Ignoring.");
        return false;
      }
      if (!paramIntent.hasExtra("android.telephony.mbms.extra.TEMP_FILES_IN_USE"))
      {
        Log.w("MbmsDownloadReceiver", "Cleanup request did not include the list of temp files in use. Ignoring.");
        return false;
      }
    }
    return true;
  }
  
  private void verifyPermissionIntegrity(Context paramContext)
  {
    Object localObject = paramContext.getPackageManager().queryBroadcastReceivers(new Intent(paramContext, MbmsDownloadReceiver.class), 0);
    if (((List)localObject).size() == 1)
    {
      localObject = get0activityInfo;
      if (localObject != null)
      {
        if (MbmsUtils.getOverrideServiceName(paramContext, "android.telephony.action.EmbmsDownload") != null)
        {
          if (permission != null) {
            return;
          }
          throw new IllegalStateException("MbmsDownloadReceiver must require some permission");
        }
        if (Objects.equals("android.permission.SEND_EMBMS_INTENTS", permission)) {
          return;
        }
        throw new IllegalStateException("MbmsDownloadReceiver must require the SEND_EMBMS_INTENTS permission.");
      }
      throw new IllegalStateException("Queried ResolveInfo does not contain a receiver");
    }
    throw new IllegalStateException("Non-unique download receiver in your app");
  }
  
  private static boolean verifyTempFilePath(Context paramContext, String paramString, Uri paramUri)
  {
    if (!"file".equals(paramUri.getScheme()))
    {
      paramContext = new StringBuilder();
      paramContext.append("Uri ");
      paramContext.append(paramUri);
      paramContext.append(" does not have a file scheme");
      Log.w("MbmsDownloadReceiver", paramContext.toString());
      return false;
    }
    paramUri = paramUri.getSchemeSpecificPart();
    Object localObject = new File(paramUri);
    if (!((File)localObject).exists())
    {
      paramContext = new StringBuilder();
      paramContext.append("File at ");
      paramContext.append(paramUri);
      paramContext.append(" does not exist.");
      Log.w("MbmsDownloadReceiver", paramContext.toString());
      return false;
    }
    if (!MbmsUtils.isContainedIn(MbmsUtils.getEmbmsTempFileDirForService(paramContext, paramString), (File)localObject))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("File at ");
      ((StringBuilder)localObject).append(paramUri);
      ((StringBuilder)localObject).append(" is not contained in the temp file root, which is ");
      ((StringBuilder)localObject).append(MbmsUtils.getEmbmsTempFileDirForService(paramContext, paramString));
      Log.w("MbmsDownloadReceiver", ((StringBuilder)localObject).toString());
      return false;
    }
    return true;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    verifyPermissionIntegrity(paramContext);
    if (!verifyIntentContents(paramContext, paramIntent))
    {
      setResultCode(2);
      return;
    }
    if (!Objects.equals(paramIntent.getStringExtra("android.telephony.mbms.extra.TEMP_FILE_ROOT"), MbmsTempFileProvider.getEmbmsTempFileDir(paramContext).getPath()))
    {
      setResultCode(3);
      return;
    }
    if ("android.telephony.mbms.action.DOWNLOAD_RESULT_INTERNAL".equals(paramIntent.getAction()))
    {
      moveDownloadedFile(paramContext, paramIntent);
      cleanupPostMove(paramContext, paramIntent);
    }
    else if ("android.telephony.mbms.action.FILE_DESCRIPTOR_REQUEST".equals(paramIntent.getAction()))
    {
      generateTempFiles(paramContext, paramIntent);
    }
    else if ("android.telephony.mbms.action.CLEANUP".equals(paramIntent.getAction()))
    {
      cleanupTempFiles(paramContext, paramIntent);
    }
    else
    {
      setResultCode(1);
    }
  }
}
