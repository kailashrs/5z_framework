package android.content.pm;

import android.annotation.SystemApi;
import android.app.AppGlobals;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.FileBridge.FileBridgeOutputStream;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.ParcelableException;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.util.ExceptionUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PackageInstaller
{
  public static final String ACTION_CONFIRM_PERMISSIONS = "android.content.pm.action.CONFIRM_PERMISSIONS";
  public static final String ACTION_SESSION_COMMITTED = "android.content.pm.action.SESSION_COMMITTED";
  public static final String ACTION_SESSION_DETAILS = "android.content.pm.action.SESSION_DETAILS";
  public static final boolean ENABLE_REVOCABLE_FD = SystemProperties.getBoolean("fw.revocable_fd", false);
  public static final String EXTRA_CALLBACK = "android.content.pm.extra.CALLBACK";
  public static final String EXTRA_LEGACY_BUNDLE = "android.content.pm.extra.LEGACY_BUNDLE";
  public static final String EXTRA_LEGACY_STATUS = "android.content.pm.extra.LEGACY_STATUS";
  public static final String EXTRA_OTHER_PACKAGE_NAME = "android.content.pm.extra.OTHER_PACKAGE_NAME";
  public static final String EXTRA_PACKAGE_NAME = "android.content.pm.extra.PACKAGE_NAME";
  @Deprecated
  public static final String EXTRA_PACKAGE_NAMES = "android.content.pm.extra.PACKAGE_NAMES";
  public static final String EXTRA_SESSION = "android.content.pm.extra.SESSION";
  public static final String EXTRA_SESSION_ID = "android.content.pm.extra.SESSION_ID";
  public static final String EXTRA_STATUS = "android.content.pm.extra.STATUS";
  public static final String EXTRA_STATUS_MESSAGE = "android.content.pm.extra.STATUS_MESSAGE";
  public static final String EXTRA_STORAGE_PATH = "android.content.pm.extra.STORAGE_PATH";
  public static final int STATUS_FAILURE = 1;
  public static final int STATUS_FAILURE_ABORTED = 3;
  public static final int STATUS_FAILURE_BLOCKED = 2;
  public static final int STATUS_FAILURE_CONFLICT = 5;
  public static final int STATUS_FAILURE_INCOMPATIBLE = 7;
  public static final int STATUS_FAILURE_INVALID = 4;
  public static final int STATUS_FAILURE_STORAGE = 6;
  public static final int STATUS_PENDING_USER_ACTION = -1;
  public static final int STATUS_SUCCESS = 0;
  private static final String TAG = "PackageInstaller";
  private final ArrayList<SessionCallbackDelegate> mDelegates = new ArrayList();
  private final IPackageInstaller mInstaller;
  private final String mInstallerPackageName;
  private final int mUserId;
  
  public PackageInstaller(IPackageInstaller paramIPackageInstaller, String paramString, int paramInt)
  {
    mInstaller = paramIPackageInstaller;
    mInstallerPackageName = paramString;
    mUserId = paramInt;
  }
  
  public void abandonSession(int paramInt)
  {
    try
    {
      mInstaller.abandonSession(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void addSessionCallback(SessionCallback paramSessionCallback)
  {
    registerSessionCallback(paramSessionCallback);
  }
  
  @Deprecated
  public void addSessionCallback(SessionCallback paramSessionCallback, Handler paramHandler)
  {
    registerSessionCallback(paramSessionCallback, paramHandler);
  }
  
  public int createSession(SessionParams paramSessionParams)
    throws IOException
  {
    try
    {
      String str;
      if (installerPackageName == null) {
        str = mInstallerPackageName;
      } else {
        str = installerPackageName;
      }
      int i = mInstaller.createSession(paramSessionParams, str, mUserId);
      return i;
    }
    catch (RemoteException paramSessionParams)
    {
      throw paramSessionParams.rethrowFromSystemServer();
    }
    catch (RuntimeException paramSessionParams)
    {
      ExceptionUtils.maybeUnwrapIOException(paramSessionParams);
      throw paramSessionParams;
    }
  }
  
  public List<SessionInfo> getAllSessions()
  {
    try
    {
      List localList = mInstaller.getAllSessions(mUserId).getList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<SessionInfo> getMySessions()
  {
    try
    {
      List localList = mInstaller.getMySessions(mInstallerPackageName, mUserId).getList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public SessionInfo getSessionInfo(int paramInt)
  {
    try
    {
      SessionInfo localSessionInfo = mInstaller.getSessionInfo(paramInt);
      return localSessionInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Session openSession(int paramInt)
    throws IOException
  {
    try
    {
      Session localSession = new Session(mInstaller.openSession(paramInt));
      return localSession;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
    catch (RuntimeException localRuntimeException)
    {
      ExceptionUtils.maybeUnwrapIOException(localRuntimeException);
      throw localRuntimeException;
    }
  }
  
  public void registerSessionCallback(SessionCallback paramSessionCallback)
  {
    registerSessionCallback(paramSessionCallback, new Handler());
  }
  
  public void registerSessionCallback(SessionCallback paramSessionCallback, Handler paramHandler)
  {
    synchronized (mDelegates)
    {
      SessionCallbackDelegate localSessionCallbackDelegate = new android/content/pm/PackageInstaller$SessionCallbackDelegate;
      localSessionCallbackDelegate.<init>(paramSessionCallback, paramHandler.getLooper());
      try
      {
        mInstaller.registerCallback(localSessionCallbackDelegate, mUserId);
        mDelegates.add(localSessionCallbackDelegate);
        return;
      }
      catch (RemoteException paramSessionCallback)
      {
        throw paramSessionCallback.rethrowFromSystemServer();
      }
    }
  }
  
  @Deprecated
  public void removeSessionCallback(SessionCallback paramSessionCallback)
  {
    unregisterSessionCallback(paramSessionCallback);
  }
  
  @SystemApi
  public void setPermissionsResult(int paramInt, boolean paramBoolean)
  {
    try
    {
      mInstaller.setPermissionsResult(paramInt, paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void uninstall(VersionedPackage paramVersionedPackage, int paramInt, IntentSender paramIntentSender)
  {
    Preconditions.checkNotNull(paramVersionedPackage, "versionedPackage cannot be null");
    try
    {
      mInstaller.uninstall(paramVersionedPackage, mInstallerPackageName, paramInt, paramIntentSender, mUserId);
      return;
    }
    catch (RemoteException paramVersionedPackage)
    {
      throw paramVersionedPackage.rethrowFromSystemServer();
    }
  }
  
  public void uninstall(VersionedPackage paramVersionedPackage, IntentSender paramIntentSender)
  {
    uninstall(paramVersionedPackage, 0, paramIntentSender);
  }
  
  public void uninstall(String paramString, int paramInt, IntentSender paramIntentSender)
  {
    uninstall(new VersionedPackage(paramString, -1), paramInt, paramIntentSender);
  }
  
  public void uninstall(String paramString, IntentSender paramIntentSender)
  {
    uninstall(paramString, 0, paramIntentSender);
  }
  
  public void unregisterSessionCallback(SessionCallback paramSessionCallback)
  {
    synchronized (mDelegates)
    {
      Iterator localIterator = mDelegates.iterator();
      while (localIterator.hasNext())
      {
        SessionCallbackDelegate localSessionCallbackDelegate = (SessionCallbackDelegate)localIterator.next();
        SessionCallback localSessionCallback = mCallback;
        if (localSessionCallback == paramSessionCallback) {
          try
          {
            mInstaller.unregisterCallback(localSessionCallbackDelegate);
            localIterator.remove();
          }
          catch (RemoteException paramSessionCallback)
          {
            throw paramSessionCallback.rethrowFromSystemServer();
          }
        }
      }
      return;
    }
  }
  
  public void updateSessionAppIcon(int paramInt, Bitmap paramBitmap)
  {
    try
    {
      mInstaller.updateSessionAppIcon(paramInt, paramBitmap);
      return;
    }
    catch (RemoteException paramBitmap)
    {
      throw paramBitmap.rethrowFromSystemServer();
    }
  }
  
  public void updateSessionAppLabel(int paramInt, CharSequence paramCharSequence)
  {
    if (paramCharSequence != null) {
      try
      {
        paramCharSequence = paramCharSequence.toString();
      }
      catch (RemoteException paramCharSequence)
      {
        break label32;
      }
    } else {
      paramCharSequence = null;
    }
    mInstaller.updateSessionAppLabel(paramInt, paramCharSequence);
    return;
    label32:
    throw paramCharSequence.rethrowFromSystemServer();
  }
  
  public static class Session
    implements Closeable
  {
    private IPackageInstallerSession mSession;
    
    public Session(IPackageInstallerSession paramIPackageInstallerSession)
    {
      mSession = paramIPackageInstallerSession;
    }
    
    public void abandon()
    {
      try
      {
        mSession.abandon();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void addProgress(float paramFloat)
    {
      try
      {
        mSession.addClientProgress(paramFloat);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void close()
    {
      try
      {
        mSession.close();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void commit(IntentSender paramIntentSender)
    {
      try
      {
        mSession.commit(paramIntentSender, false);
        return;
      }
      catch (RemoteException paramIntentSender)
      {
        throw paramIntentSender.rethrowFromSystemServer();
      }
    }
    
    @SystemApi
    public void commitTransferred(IntentSender paramIntentSender)
    {
      try
      {
        mSession.commit(paramIntentSender, true);
        return;
      }
      catch (RemoteException paramIntentSender)
      {
        throw paramIntentSender.rethrowFromSystemServer();
      }
    }
    
    public void fsync(OutputStream paramOutputStream)
      throws IOException
    {
      if (PackageInstaller.ENABLE_REVOCABLE_FD)
      {
        if ((paramOutputStream instanceof ParcelFileDescriptor.AutoCloseOutputStream)) {
          try
          {
            Os.fsync(((ParcelFileDescriptor.AutoCloseOutputStream)paramOutputStream).getFD());
          }
          catch (ErrnoException paramOutputStream)
          {
            throw paramOutputStream.rethrowAsIOException();
          }
        } else {
          throw new IllegalArgumentException("Unrecognized stream");
        }
      }
      else
      {
        if (!(paramOutputStream instanceof FileBridge.FileBridgeOutputStream)) {
          break label57;
        }
        ((FileBridge.FileBridgeOutputStream)paramOutputStream).fsync();
      }
      return;
      label57:
      throw new IllegalArgumentException("Unrecognized stream");
    }
    
    public String[] getNames()
      throws IOException
    {
      try
      {
        String[] arrayOfString = mSession.getNames();
        return arrayOfString;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
      catch (RuntimeException localRuntimeException)
      {
        ExceptionUtils.maybeUnwrapIOException(localRuntimeException);
        throw localRuntimeException;
      }
    }
    
    public InputStream openRead(String paramString)
      throws IOException
    {
      try
      {
        paramString = new ParcelFileDescriptor.AutoCloseInputStream(mSession.openRead(paramString));
        return paramString;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
      catch (RuntimeException paramString)
      {
        ExceptionUtils.maybeUnwrapIOException(paramString);
        throw paramString;
      }
    }
    
    public OutputStream openWrite(String paramString, long paramLong1, long paramLong2)
      throws IOException
    {
      try
      {
        if (PackageInstaller.ENABLE_REVOCABLE_FD) {
          return new ParcelFileDescriptor.AutoCloseOutputStream(mSession.openWrite(paramString, paramLong1, paramLong2));
        }
        paramString = new FileBridge.FileBridgeOutputStream(mSession.openWrite(paramString, paramLong1, paramLong2));
        return paramString;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
      catch (RuntimeException paramString)
      {
        ExceptionUtils.maybeUnwrapIOException(paramString);
        throw paramString;
      }
    }
    
    public void removeSplit(String paramString)
      throws IOException
    {
      try
      {
        mSession.removeSplit(paramString);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
      catch (RuntimeException paramString)
      {
        ExceptionUtils.maybeUnwrapIOException(paramString);
        throw paramString;
      }
    }
    
    @Deprecated
    public void setProgress(float paramFloat)
    {
      setStagingProgress(paramFloat);
    }
    
    public void setStagingProgress(float paramFloat)
    {
      try
      {
        mSession.setClientProgress(paramFloat);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void transfer(String paramString)
      throws PackageManager.NameNotFoundException
    {
      Preconditions.checkNotNull(paramString);
      try
      {
        mSession.transfer(paramString);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
      catch (ParcelableException paramString)
      {
        paramString.maybeRethrow(PackageManager.NameNotFoundException.class);
        throw new RuntimeException(paramString);
      }
    }
    
    public void write(String paramString, long paramLong1, long paramLong2, ParcelFileDescriptor paramParcelFileDescriptor)
      throws IOException
    {
      try
      {
        mSession.write(paramString, paramLong1, paramLong2, paramParcelFileDescriptor);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
      catch (RuntimeException paramString)
      {
        ExceptionUtils.maybeUnwrapIOException(paramString);
        throw paramString;
      }
    }
  }
  
  public static abstract class SessionCallback
  {
    public SessionCallback() {}
    
    public abstract void onActiveChanged(int paramInt, boolean paramBoolean);
    
    public abstract void onBadgingChanged(int paramInt);
    
    public abstract void onCreated(int paramInt);
    
    public abstract void onFinished(int paramInt, boolean paramBoolean);
    
    public abstract void onProgressChanged(int paramInt, float paramFloat);
  }
  
  private static class SessionCallbackDelegate
    extends IPackageInstallerCallback.Stub
    implements Handler.Callback
  {
    private static final int MSG_SESSION_ACTIVE_CHANGED = 3;
    private static final int MSG_SESSION_BADGING_CHANGED = 2;
    private static final int MSG_SESSION_CREATED = 1;
    private static final int MSG_SESSION_FINISHED = 5;
    private static final int MSG_SESSION_PROGRESS_CHANGED = 4;
    final PackageInstaller.SessionCallback mCallback;
    final Handler mHandler;
    
    public SessionCallbackDelegate(PackageInstaller.SessionCallback paramSessionCallback, Looper paramLooper)
    {
      mCallback = paramSessionCallback;
      mHandler = new Handler(paramLooper, this);
    }
    
    public boolean handleMessage(Message paramMessage)
    {
      int i = arg1;
      int j = what;
      boolean bool1 = false;
      boolean bool2 = false;
      switch (j)
      {
      default: 
        return false;
      case 5: 
        PackageInstaller.SessionCallback localSessionCallback = mCallback;
        if (arg2 != 0) {
          bool2 = true;
        }
        localSessionCallback.onFinished(i, bool2);
        return true;
      case 4: 
        mCallback.onProgressChanged(i, ((Float)obj).floatValue());
        return true;
      case 3: 
        bool2 = bool1;
        if (arg2 != 0) {
          bool2 = true;
        }
        mCallback.onActiveChanged(i, bool2);
        return true;
      case 2: 
        mCallback.onBadgingChanged(i);
        return true;
      }
      mCallback.onCreated(i);
      return true;
    }
    
    public void onSessionActiveChanged(int paramInt, boolean paramBoolean)
    {
      mHandler.obtainMessage(3, paramInt, paramBoolean).sendToTarget();
    }
    
    public void onSessionBadgingChanged(int paramInt)
    {
      mHandler.obtainMessage(2, paramInt, 0).sendToTarget();
    }
    
    public void onSessionCreated(int paramInt)
    {
      mHandler.obtainMessage(1, paramInt, 0).sendToTarget();
    }
    
    public void onSessionFinished(int paramInt, boolean paramBoolean)
    {
      mHandler.obtainMessage(5, paramInt, paramBoolean).sendToTarget();
    }
    
    public void onSessionProgressChanged(int paramInt, float paramFloat)
    {
      mHandler.obtainMessage(4, paramInt, 0, Float.valueOf(paramFloat)).sendToTarget();
    }
  }
  
  public static class SessionInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<SessionInfo> CREATOR = new Parcelable.Creator()
    {
      public PackageInstaller.SessionInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageInstaller.SessionInfo(paramAnonymousParcel);
      }
      
      public PackageInstaller.SessionInfo[] newArray(int paramAnonymousInt)
      {
        return new PackageInstaller.SessionInfo[paramAnonymousInt];
      }
    };
    public boolean active;
    public Bitmap appIcon;
    public CharSequence appLabel;
    public String appPackageName;
    public String[] grantedRuntimePermissions;
    public int installFlags;
    public int installLocation;
    public int installReason;
    public String installerPackageName;
    public int mode;
    public int originatingUid;
    public Uri originatingUri;
    public float progress;
    public Uri referrerUri;
    public String resolvedBaseCodePath;
    public boolean sealed;
    public int sessionId;
    public long sizeBytes;
    
    public SessionInfo() {}
    
    public SessionInfo(Parcel paramParcel)
    {
      sessionId = paramParcel.readInt();
      installerPackageName = paramParcel.readString();
      resolvedBaseCodePath = paramParcel.readString();
      progress = paramParcel.readFloat();
      int i = paramParcel.readInt();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sealed = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      }
      active = bool2;
      mode = paramParcel.readInt();
      installReason = paramParcel.readInt();
      sizeBytes = paramParcel.readLong();
      appPackageName = paramParcel.readString();
      appIcon = ((Bitmap)paramParcel.readParcelable(null));
      appLabel = paramParcel.readString();
      installLocation = paramParcel.readInt();
      originatingUri = ((Uri)paramParcel.readParcelable(null));
      originatingUid = paramParcel.readInt();
      referrerUri = ((Uri)paramParcel.readParcelable(null));
      grantedRuntimePermissions = paramParcel.readStringArray();
      installFlags = paramParcel.readInt();
    }
    
    public Intent createDetailsIntent()
    {
      Intent localIntent = new Intent("android.content.pm.action.SESSION_DETAILS");
      localIntent.putExtra("android.content.pm.extra.SESSION_ID", sessionId);
      localIntent.setPackage(installerPackageName);
      localIntent.setFlags(268435456);
      return localIntent;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    @SystemApi
    public boolean getAllocateAggressive()
    {
      boolean bool;
      if ((installFlags & 0x8000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    @SystemApi
    public boolean getAllowDowngrade()
    {
      boolean bool;
      if ((installFlags & 0x80) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public Bitmap getAppIcon()
    {
      if (appIcon == null) {
        try
        {
          Object localObject = AppGlobals.getPackageManager().getPackageInstaller().getSessionInfo(sessionId);
          if (localObject != null) {
            localObject = appIcon;
          } else {
            localObject = null;
          }
          appIcon = ((Bitmap)localObject);
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      return appIcon;
    }
    
    public CharSequence getAppLabel()
    {
      return appLabel;
    }
    
    public String getAppPackageName()
    {
      return appPackageName;
    }
    
    @Deprecated
    public Intent getDetailsIntent()
    {
      return createDetailsIntent();
    }
    
    @SystemApi
    public boolean getDontKillApp()
    {
      boolean bool;
      if ((installFlags & 0x1000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    @SystemApi
    public String[] getGrantedRuntimePermissions()
    {
      return grantedRuntimePermissions;
    }
    
    @SystemApi
    public boolean getInstallAsFullApp(boolean paramBoolean)
    {
      if ((installFlags & 0x4000) != 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      return paramBoolean;
    }
    
    @SystemApi
    public boolean getInstallAsInstantApp(boolean paramBoolean)
    {
      if ((installFlags & 0x800) != 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      return paramBoolean;
    }
    
    @SystemApi
    public boolean getInstallAsVirtualPreload()
    {
      boolean bool;
      if ((installFlags & 0x10000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int getInstallLocation()
    {
      return installLocation;
    }
    
    public int getInstallReason()
    {
      return installReason;
    }
    
    public String getInstallerPackageName()
    {
      return installerPackageName;
    }
    
    public int getMode()
    {
      return mode;
    }
    
    public int getOriginatingUid()
    {
      return originatingUid;
    }
    
    public Uri getOriginatingUri()
    {
      return originatingUri;
    }
    
    public float getProgress()
    {
      return progress;
    }
    
    public Uri getReferrerUri()
    {
      return referrerUri;
    }
    
    public int getSessionId()
    {
      return sessionId;
    }
    
    public long getSize()
    {
      return sizeBytes;
    }
    
    public boolean isActive()
    {
      return active;
    }
    
    @Deprecated
    public boolean isOpen()
    {
      return isActive();
    }
    
    public boolean isSealed()
    {
      return sealed;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(sessionId);
      paramParcel.writeString(installerPackageName);
      paramParcel.writeString(resolvedBaseCodePath);
      paramParcel.writeFloat(progress);
      paramParcel.writeInt(sealed);
      paramParcel.writeInt(active);
      paramParcel.writeInt(mode);
      paramParcel.writeInt(installReason);
      paramParcel.writeLong(sizeBytes);
      paramParcel.writeString(appPackageName);
      paramParcel.writeParcelable(appIcon, paramInt);
      String str;
      if (appLabel != null) {
        str = appLabel.toString();
      } else {
        str = null;
      }
      paramParcel.writeString(str);
      paramParcel.writeInt(installLocation);
      paramParcel.writeParcelable(originatingUri, paramInt);
      paramParcel.writeInt(originatingUid);
      paramParcel.writeParcelable(referrerUri, paramInt);
      paramParcel.writeStringArray(grantedRuntimePermissions);
      paramParcel.writeInt(installFlags);
    }
  }
  
  public static class SessionParams
    implements Parcelable
  {
    public static final Parcelable.Creator<SessionParams> CREATOR = new Parcelable.Creator()
    {
      public PackageInstaller.SessionParams createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PackageInstaller.SessionParams(paramAnonymousParcel);
      }
      
      public PackageInstaller.SessionParams[] newArray(int paramAnonymousInt)
      {
        return new PackageInstaller.SessionParams[paramAnonymousInt];
      }
    };
    public static final int MODE_FULL_INSTALL = 1;
    public static final int MODE_INHERIT_EXISTING = 2;
    public static final int MODE_INVALID = -1;
    public static final int UID_UNKNOWN = -1;
    public String abiOverride;
    public Bitmap appIcon;
    public long appIconLastModified = -1L;
    public String appLabel;
    public String appPackageName;
    public String[] grantedRuntimePermissions;
    public int installFlags;
    public int installLocation = 1;
    public int installReason = 0;
    public String installerPackageName;
    public int mode = -1;
    public int originatingUid = -1;
    public Uri originatingUri;
    public Uri referrerUri;
    public long sizeBytes = -1L;
    public String volumeUuid;
    
    public SessionParams(int paramInt)
    {
      mode = paramInt;
    }
    
    public SessionParams(Parcel paramParcel)
    {
      mode = paramParcel.readInt();
      installFlags = paramParcel.readInt();
      installLocation = paramParcel.readInt();
      installReason = paramParcel.readInt();
      sizeBytes = paramParcel.readLong();
      appPackageName = paramParcel.readString();
      appIcon = ((Bitmap)paramParcel.readParcelable(null));
      appLabel = paramParcel.readString();
      originatingUri = ((Uri)paramParcel.readParcelable(null));
      originatingUid = paramParcel.readInt();
      referrerUri = ((Uri)paramParcel.readParcelable(null));
      abiOverride = paramParcel.readString();
      volumeUuid = paramParcel.readString();
      grantedRuntimePermissions = paramParcel.readStringArray();
      installerPackageName = paramParcel.readString();
    }
    
    public boolean areHiddenOptionsSet()
    {
      boolean bool;
      if (((installFlags & 0x1D880) == installFlags) && (abiOverride == null) && (volumeUuid == null)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void dump(IndentingPrintWriter paramIndentingPrintWriter)
    {
      paramIndentingPrintWriter.printPair("mode", Integer.valueOf(mode));
      paramIndentingPrintWriter.printHexPair("installFlags", installFlags);
      paramIndentingPrintWriter.printPair("installLocation", Integer.valueOf(installLocation));
      paramIndentingPrintWriter.printPair("sizeBytes", Long.valueOf(sizeBytes));
      paramIndentingPrintWriter.printPair("appPackageName", appPackageName);
      boolean bool;
      if (appIcon != null) {
        bool = true;
      } else {
        bool = false;
      }
      paramIndentingPrintWriter.printPair("appIcon", Boolean.valueOf(bool));
      paramIndentingPrintWriter.printPair("appLabel", appLabel);
      paramIndentingPrintWriter.printPair("originatingUri", originatingUri);
      paramIndentingPrintWriter.printPair("originatingUid", Integer.valueOf(originatingUid));
      paramIndentingPrintWriter.printPair("referrerUri", referrerUri);
      paramIndentingPrintWriter.printPair("abiOverride", abiOverride);
      paramIndentingPrintWriter.printPair("volumeUuid", volumeUuid);
      paramIndentingPrintWriter.printPair("grantedRuntimePermissions", grantedRuntimePermissions);
      paramIndentingPrintWriter.printPair("installerPackageName", installerPackageName);
      paramIndentingPrintWriter.println();
    }
    
    @SystemApi
    public void setAllocateAggressive(boolean paramBoolean)
    {
      if (paramBoolean) {
        installFlags |= 0x8000;
      } else {
        installFlags &= 0xFFFF7FFF;
      }
    }
    
    @SystemApi
    public void setAllowDowngrade(boolean paramBoolean)
    {
      if (paramBoolean) {
        installFlags |= 0x80;
      } else {
        installFlags &= 0xFF7F;
      }
    }
    
    public void setAppIcon(Bitmap paramBitmap)
    {
      appIcon = paramBitmap;
    }
    
    public void setAppLabel(CharSequence paramCharSequence)
    {
      if (paramCharSequence != null) {
        paramCharSequence = paramCharSequence.toString();
      } else {
        paramCharSequence = null;
      }
      appLabel = paramCharSequence;
    }
    
    public void setAppPackageName(String paramString)
    {
      appPackageName = paramString;
    }
    
    @SystemApi
    public void setDontKillApp(boolean paramBoolean)
    {
      if (paramBoolean) {
        installFlags |= 0x1000;
      } else {
        installFlags &= 0xEFFF;
      }
    }
    
    @SystemApi
    public void setGrantedRuntimePermissions(String[] paramArrayOfString)
    {
      installFlags |= 0x100;
      grantedRuntimePermissions = paramArrayOfString;
    }
    
    @SystemApi
    public void setInstallAsInstantApp(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        installFlags |= 0x800;
        installFlags &= 0xBFFF;
      }
      else
      {
        installFlags &= 0xF7FF;
        installFlags |= 0x4000;
      }
    }
    
    @SystemApi
    public void setInstallAsVirtualPreload()
    {
      installFlags |= 0x10000;
    }
    
    public void setInstallFlagsExternal()
    {
      installFlags |= 0x8;
      installFlags &= 0xFFFFFFEF;
    }
    
    public void setInstallFlagsForcePermissionPrompt()
    {
      installFlags |= 0x400;
    }
    
    public void setInstallFlagsInternal()
    {
      installFlags |= 0x10;
      installFlags &= 0xFFFFFFF7;
    }
    
    public void setInstallLocation(int paramInt)
    {
      installLocation = paramInt;
    }
    
    public void setInstallReason(int paramInt)
    {
      installReason = paramInt;
    }
    
    public void setInstallerPackageName(String paramString)
    {
      installerPackageName = paramString;
    }
    
    public void setOriginatingUid(int paramInt)
    {
      originatingUid = paramInt;
    }
    
    public void setOriginatingUri(Uri paramUri)
    {
      originatingUri = paramUri;
    }
    
    public void setReferrerUri(Uri paramUri)
    {
      referrerUri = paramUri;
    }
    
    public void setSize(long paramLong)
    {
      sizeBytes = paramLong;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mode);
      paramParcel.writeInt(installFlags);
      paramParcel.writeInt(installLocation);
      paramParcel.writeInt(installReason);
      paramParcel.writeLong(sizeBytes);
      paramParcel.writeString(appPackageName);
      paramParcel.writeParcelable(appIcon, paramInt);
      paramParcel.writeString(appLabel);
      paramParcel.writeParcelable(originatingUri, paramInt);
      paramParcel.writeInt(originatingUid);
      paramParcel.writeParcelable(referrerUri, paramInt);
      paramParcel.writeString(abiOverride);
      paramParcel.writeString(volumeUuid);
      paramParcel.writeStringArray(grantedRuntimePermissions);
      paramParcel.writeString(installerPackageName);
    }
  }
}
