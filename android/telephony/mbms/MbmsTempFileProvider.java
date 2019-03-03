package android.telephony.mbms;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class MbmsTempFileProvider
  extends ContentProvider
{
  public static final String TEMP_FILE_ROOT_PREF_FILE_NAME = "MbmsTempFileRootPrefs";
  public static final String TEMP_FILE_ROOT_PREF_NAME = "mbms_temp_file_root";
  private String mAuthority;
  private Context mContext;
  
  public MbmsTempFileProvider() {}
  
  public static File getEmbmsTempFileDir(Context paramContext)
  {
    String str = paramContext.getSharedPreferences("MbmsTempFileRootPrefs", 0).getString("mbms_temp_file_root", null);
    File localFile;
    if (str != null)
    {
      try
      {
        paramContext = new java/io/File;
        paramContext.<init>(str);
        return paramContext.getCanonicalFile();
      }
      catch (IOException localIOException) {}
    }
    else
    {
      localFile = new java/io/File;
      localFile.<init>(paramContext.getFilesDir(), "androidMbmsTempFileRoot");
      paramContext = localFile.getCanonicalFile();
      return paramContext;
    }
    paramContext = new StringBuilder();
    paramContext.append("Unable to canonicalize temp file root path ");
    paramContext.append(localFile);
    throw new RuntimeException(paramContext.toString());
  }
  
  public static File getFileForUri(Context paramContext, String paramString, Uri paramUri)
    throws FileNotFoundException
  {
    if ("content".equals(paramUri.getScheme()))
    {
      if (Objects.equals(paramString, paramUri.getAuthority()))
      {
        paramString = Uri.decode(paramUri.getEncodedPath());
        try
        {
          paramContext = getEmbmsTempFileDir(paramContext).getCanonicalFile();
          paramUri = new java/io/File;
          paramUri.<init>(paramContext, paramString);
          paramString = paramUri.getCanonicalFile();
          if (paramString.getPath().startsWith(paramContext.getPath())) {
            return paramString;
          }
          throw new SecurityException("Resolved path jumped beyond configured root");
        }
        catch (IOException paramContext)
        {
          throw new FileNotFoundException("Could not resolve paths");
        }
      }
      paramContext = new StringBuilder();
      paramContext.append("Uri does not have a matching authority: ");
      paramContext.append(paramString);
      paramContext.append(", ");
      paramContext.append(paramUri.getAuthority());
      throw new IllegalArgumentException(paramContext.toString());
    }
    throw new IllegalArgumentException("Uri must have scheme content");
  }
  
  public static Uri getUriForFile(Context paramContext, String paramString, File paramFile)
  {
    try
    {
      String str = paramFile.getCanonicalPath();
      paramContext = getEmbmsTempFileDir(paramContext);
      if (MbmsUtils.isContainedIn(paramContext, paramFile)) {
        try
        {
          paramFile = paramContext.getCanonicalPath();
          if (paramFile.endsWith("/")) {
            paramContext = str.substring(paramFile.length());
          } else {
            paramContext = str.substring(paramFile.length() + 1);
          }
          paramContext = Uri.encode(paramContext);
          return new Uri.Builder().scheme("content").authority(paramString).encodedPath(paramContext).build();
        }
        catch (IOException paramString)
        {
          paramString = new StringBuilder();
          paramString.append("Could not get canonical path for temp file root dir ");
          paramString.append(paramContext);
          throw new RuntimeException(paramString.toString());
        }
      }
      paramString = new StringBuilder();
      paramString.append("File ");
      paramString.append(paramFile);
      paramString.append(" is not contained in the temp file directory, which is ");
      paramString.append(paramContext);
      throw new IllegalArgumentException(paramString.toString());
    }
    catch (IOException paramContext)
    {
      paramContext = new StringBuilder();
      paramContext.append("Could not get canonical path for file ");
      paramContext.append(paramFile);
      throw new IllegalArgumentException(paramContext.toString());
    }
  }
  
  public void attachInfo(Context paramContext, ProviderInfo paramProviderInfo)
  {
    super.attachInfo(paramContext, paramProviderInfo);
    if (!exported)
    {
      if (grantUriPermissions)
      {
        mAuthority = authority;
        mContext = paramContext;
        return;
      }
      throw new SecurityException("Provider must grant uri permissions");
    }
    throw new SecurityException("Provider must not be exported");
  }
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("No deleting supported");
  }
  
  public String getType(Uri paramUri)
  {
    return "application/octet-stream";
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    throw new UnsupportedOperationException("No inserting supported");
  }
  
  public boolean onCreate()
  {
    return true;
  }
  
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    return ParcelFileDescriptor.open(getFileForUri(mContext, mAuthority, paramUri), ParcelFileDescriptor.parseMode(paramString));
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    throw new UnsupportedOperationException("No querying supported");
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("No updating supported");
  }
}
