package android.content.pm.dex;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Slog;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executor;

@SystemApi
public class ArtManager
{
  public static final int PROFILE_APPS = 0;
  public static final int PROFILE_BOOT_IMAGE = 1;
  public static final int SNAPSHOT_FAILED_CODE_PATH_NOT_FOUND = 1;
  public static final int SNAPSHOT_FAILED_INTERNAL_ERROR = 2;
  public static final int SNAPSHOT_FAILED_PACKAGE_NOT_FOUND = 0;
  private static final String TAG = "ArtManager";
  private final IArtManager mArtManager;
  private final Context mContext;
  
  public ArtManager(Context paramContext, IArtManager paramIArtManager)
  {
    mContext = paramContext;
    mArtManager = paramIArtManager;
  }
  
  public static String getCurrentProfilePath(String paramString1, int paramInt, String paramString2)
  {
    return new File(Environment.getDataProfilesDePackageDirectory(paramInt, paramString1), getProfileName(paramString2)).getAbsolutePath();
  }
  
  public static String getProfileName(String paramString)
  {
    if (paramString == null)
    {
      paramString = "primary.prof";
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(".split.prof");
      paramString = localStringBuilder.toString();
    }
    return paramString;
  }
  
  public static File getProfileSnapshotFileForName(String paramString1, String paramString2)
  {
    File localFile = Environment.getDataRefProfilesDePackageDirectory(paramString1);
    paramString1 = new StringBuilder();
    paramString1.append(paramString2);
    paramString1.append(".snapshot");
    return new File(localFile, paramString1.toString());
  }
  
  public boolean isRuntimeProfilingEnabled(int paramInt)
  {
    try
    {
      boolean bool = mArtManager.isRuntimeProfilingEnabled(paramInt, mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public void snapshotRuntimeProfile(int paramInt, String paramString1, String paramString2, Executor paramExecutor, SnapshotRuntimeProfileCallback paramSnapshotRuntimeProfileCallback)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Requesting profile snapshot for ");
    localStringBuilder.append(paramString1);
    localStringBuilder.append(":");
    localStringBuilder.append(paramString2);
    Slog.d("ArtManager", localStringBuilder.toString());
    paramExecutor = new SnapshotRuntimeProfileCallbackDelegate(paramSnapshotRuntimeProfileCallback, paramExecutor, null);
    try
    {
      mArtManager.snapshotRuntimeProfile(paramInt, paramString1, paramString2, paramExecutor, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowAsRuntimeException();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ProfileType {}
  
  public static abstract class SnapshotRuntimeProfileCallback
  {
    public SnapshotRuntimeProfileCallback() {}
    
    public abstract void onError(int paramInt);
    
    public abstract void onSuccess(ParcelFileDescriptor paramParcelFileDescriptor);
  }
  
  private static class SnapshotRuntimeProfileCallbackDelegate
    extends ISnapshotRuntimeProfileCallback.Stub
  {
    private final ArtManager.SnapshotRuntimeProfileCallback mCallback;
    private final Executor mExecutor;
    
    private SnapshotRuntimeProfileCallbackDelegate(ArtManager.SnapshotRuntimeProfileCallback paramSnapshotRuntimeProfileCallback, Executor paramExecutor)
    {
      mCallback = paramSnapshotRuntimeProfileCallback;
      mExecutor = paramExecutor;
    }
    
    public void onError(int paramInt)
    {
      mExecutor.execute(new _..Lambda.ArtManager.SnapshotRuntimeProfileCallbackDelegate.m2Wpsf6LxhWt_1tS6tQt3B8QcGo(this, paramInt));
    }
    
    public void onSuccess(ParcelFileDescriptor paramParcelFileDescriptor)
    {
      mExecutor.execute(new _..Lambda.ArtManager.SnapshotRuntimeProfileCallbackDelegate.OOdGv4iFxuVpH2kzFMr8KwX3X8s(this, paramParcelFileDescriptor));
    }
  }
}
