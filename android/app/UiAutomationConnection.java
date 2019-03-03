package android.app;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.InputEvent;
import android.view.SurfaceControl;
import android.view.WindowAnimationFrameStats;
import android.view.WindowContentFrameStats;
import android.view.accessibility.IAccessibilityManager;
import android.view.accessibility.IAccessibilityManager.Stub;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import libcore.io.IoUtils;

public final class UiAutomationConnection
  extends IUiAutomationConnection.Stub
{
  private static final int INITIAL_FROZEN_ROTATION_UNSPECIFIED = -1;
  private static final String TAG = "UiAutomationConnection";
  private final IAccessibilityManager mAccessibilityManager = IAccessibilityManager.Stub.asInterface(ServiceManager.getService("accessibility"));
  private IAccessibilityServiceClient mClient;
  private int mInitialFrozenRotation = -1;
  private boolean mIsShutdown;
  private final Object mLock = new Object();
  private int mOwningUid;
  private final IPackageManager mPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
  private final Binder mToken = new Binder();
  private final IWindowManager mWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
  
  public UiAutomationConnection() {}
  
  private boolean isConnectedLocked()
  {
    boolean bool;
    if (mClient != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void registerUiTestAutomationServiceLocked(IAccessibilityServiceClient paramIAccessibilityServiceClient, int paramInt)
  {
    IAccessibilityManager localIAccessibilityManager = IAccessibilityManager.Stub.asInterface(ServiceManager.getService("accessibility"));
    AccessibilityServiceInfo localAccessibilityServiceInfo = new AccessibilityServiceInfo();
    eventTypes = -1;
    feedbackType = 16;
    flags |= 0x10012;
    localAccessibilityServiceInfo.setCapabilities(15);
    try
    {
      localIAccessibilityManager.registerUiTestAutomationService(mToken, paramIAccessibilityServiceClient, localAccessibilityServiceInfo, paramInt);
      mClient = paramIAccessibilityServiceClient;
      return;
    }
    catch (RemoteException paramIAccessibilityServiceClient)
    {
      throw new IllegalStateException("Error while registering UiTestAutomationService.", paramIAccessibilityServiceClient);
    }
  }
  
  private void restoreRotationStateLocked()
  {
    try
    {
      if (mInitialFrozenRotation != -1) {
        mWindowManager.freezeRotation(mInitialFrozenRotation);
      } else {
        mWindowManager.thawRotation();
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  private void storeRotationStateLocked()
  {
    try
    {
      if (mWindowManager.isRotationFrozen()) {
        mInitialFrozenRotation = mWindowManager.getDefaultDisplayRotation();
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  private void throwIfCalledByNotTrustedUidLocked()
  {
    int i = Binder.getCallingUid();
    if ((i != mOwningUid) && (mOwningUid != 1000) && (i != 0)) {
      throw new SecurityException("Calling from not trusted UID!");
    }
  }
  
  private void throwIfNotConnectedLocked()
  {
    if (isConnectedLocked()) {
      return;
    }
    throw new IllegalStateException("Not connected!");
  }
  
  private void throwIfShutdownLocked()
  {
    if (!mIsShutdown) {
      return;
    }
    throw new IllegalStateException("Connection shutdown!");
  }
  
  private void unregisterUiTestAutomationServiceLocked()
  {
    IAccessibilityManager localIAccessibilityManager = IAccessibilityManager.Stub.asInterface(ServiceManager.getService("accessibility"));
    try
    {
      localIAccessibilityManager.unregisterUiTestAutomationService(mClient);
      mClient = null;
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new IllegalStateException("Error while unregistering UiTestAutomationService", localRemoteException);
    }
  }
  
  public void clearWindowAnimationFrameStats()
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      long l = Binder.clearCallingIdentity();
      try
      {
        SurfaceControl.clearAnimationFrameStats();
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  public boolean clearWindowContentFrameStats(int paramInt)
    throws RemoteException
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      int i = UserHandle.getCallingUserId();
      long l = Binder.clearCallingIdentity();
      try
      {
        ??? = mAccessibilityManager.getWindowToken(paramInt, i);
        if (??? == null) {
          return false;
        }
        boolean bool = mWindowManager.clearWindowContentFrameStats((IBinder)???);
        return bool;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  public void connect(IAccessibilityServiceClient paramIAccessibilityServiceClient, int paramInt)
  {
    if (paramIAccessibilityServiceClient != null) {
      synchronized (mLock)
      {
        throwIfShutdownLocked();
        if (!isConnectedLocked())
        {
          mOwningUid = Binder.getCallingUid();
          registerUiTestAutomationServiceLocked(paramIAccessibilityServiceClient, paramInt);
          storeRotationStateLocked();
          return;
        }
        paramIAccessibilityServiceClient = new java/lang/IllegalStateException;
        paramIAccessibilityServiceClient.<init>("Already connected.");
        throw paramIAccessibilityServiceClient;
      }
    }
    throw new IllegalArgumentException("Client cannot be null!");
  }
  
  public void disconnect()
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      if (isConnectedLocked())
      {
        mOwningUid = -1;
        unregisterUiTestAutomationServiceLocked();
        restoreRotationStateLocked();
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("Already disconnected.");
      throw localIllegalStateException;
    }
  }
  
  public void executeShellCommand(final String paramString, final ParcelFileDescriptor paramParcelFileDescriptor1, final ParcelFileDescriptor paramParcelFileDescriptor2)
    throws RemoteException
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      try
      {
        final Process localProcess = Runtime.getRuntime().exec(paramString);
        if (paramParcelFileDescriptor1 != null)
        {
          paramString = new Thread(new Repeater(localProcess.getInputStream(), new FileOutputStream(paramParcelFileDescriptor1.getFileDescriptor())));
          paramString.start();
        }
        else
        {
          paramString = null;
        }
        if (paramParcelFileDescriptor2 != null)
        {
          ??? = localProcess.getOutputStream();
          ??? = new Thread(new Repeater(new FileInputStream(paramParcelFileDescriptor2.getFileDescriptor()), (OutputStream)???));
          ((Thread)???).start();
        }
        else
        {
          ??? = null;
        }
        new Thread(new Runnable()
        {
          public void run()
          {
            try
            {
              if (val$writeToProcess != null) {
                val$writeToProcess.join();
              }
              if (paramString != null) {
                paramString.join();
              }
            }
            catch (InterruptedException localInterruptedException)
            {
              Log.e("UiAutomationConnection", "At least one of the threads was interrupted");
            }
            IoUtils.closeQuietly(paramParcelFileDescriptor1);
            IoUtils.closeQuietly(paramParcelFileDescriptor2);
            localProcess.destroy();
          }
        }).start();
        return;
      }
      catch (IOException paramParcelFileDescriptor2)
      {
        paramParcelFileDescriptor1 = new StringBuilder();
        paramParcelFileDescriptor1.append("Error running shell command '");
        paramParcelFileDescriptor1.append(paramString);
        paramParcelFileDescriptor1.append("'");
        throw new RuntimeException(paramParcelFileDescriptor1.toString(), paramParcelFileDescriptor2);
      }
    }
  }
  
  public WindowAnimationFrameStats getWindowAnimationFrameStats()
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      long l = Binder.clearCallingIdentity();
      try
      {
        ??? = new android/view/WindowAnimationFrameStats;
        ((WindowAnimationFrameStats)???).<init>();
        SurfaceControl.getAnimationFrameStats((WindowAnimationFrameStats)???);
        return ???;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  public WindowContentFrameStats getWindowContentFrameStats(int paramInt)
    throws RemoteException
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      int i = UserHandle.getCallingUserId();
      long l = Binder.clearCallingIdentity();
      try
      {
        Object localObject2 = mAccessibilityManager.getWindowToken(paramInt, i);
        if (localObject2 == null) {
          return null;
        }
        localObject2 = mWindowManager.getWindowContentFrameStats((IBinder)localObject2);
        return localObject2;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  public void grantRuntimePermission(String paramString1, String paramString2, int paramInt)
    throws RemoteException
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      long l = Binder.clearCallingIdentity();
      try
      {
        mPackageManager.grantRuntimePermission(paramString1, paramString2, paramInt);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  public boolean injectInputEvent(InputEvent paramInputEvent, boolean paramBoolean)
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      int i;
      if (paramBoolean) {
        i = 2;
      } else {
        i = 0;
      }
      long l = Binder.clearCallingIdentity();
      try
      {
        paramBoolean = InputManager.getInstance().injectInputEvent(paramInputEvent, i);
        return paramBoolean;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  public void revokeRuntimePermission(String paramString1, String paramString2, int paramInt)
    throws RemoteException
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      long l = Binder.clearCallingIdentity();
      try
      {
        mPackageManager.revokeRuntimePermission(paramString1, paramString2, paramInt);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  /* Error */
  public boolean setRotation(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 75	android/app/UiAutomationConnection:mLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: invokespecial 172	android/app/UiAutomationConnection:throwIfCalledByNotTrustedUidLocked	()V
    //   11: aload_0
    //   12: invokespecial 174	android/app/UiAutomationConnection:throwIfShutdownLocked	()V
    //   15: aload_0
    //   16: invokespecial 176	android/app/UiAutomationConnection:throwIfNotConnectedLocked	()V
    //   19: aload_2
    //   20: monitorexit
    //   21: invokestatic 180	android/os/Binder:clearCallingIdentity	()J
    //   24: lstore_3
    //   25: iload_1
    //   26: bipush -2
    //   28: if_icmpne +23 -> 51
    //   31: aload_0
    //   32: getfield 52	android/app/UiAutomationConnection:mWindowManager	Landroid/view/IWindowManager;
    //   35: invokeinterface 130 1 0
    //   40: goto +21 -> 61
    //   43: astore_2
    //   44: goto +23 -> 67
    //   47: astore_2
    //   48: goto +25 -> 73
    //   51: aload_0
    //   52: getfield 52	android/app/UiAutomationConnection:mWindowManager	Landroid/view/IWindowManager;
    //   55: iload_1
    //   56: invokeinterface 127 2 0
    //   61: lload_3
    //   62: invokestatic 189	android/os/Binder:restoreCallingIdentity	(J)V
    //   65: iconst_1
    //   66: ireturn
    //   67: lload_3
    //   68: invokestatic 189	android/os/Binder:restoreCallingIdentity	(J)V
    //   71: aload_2
    //   72: athrow
    //   73: lload_3
    //   74: invokestatic 189	android/os/Binder:restoreCallingIdentity	(J)V
    //   77: iconst_0
    //   78: ireturn
    //   79: astore 5
    //   81: aload_2
    //   82: monitorexit
    //   83: aload 5
    //   85: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	86	0	this	UiAutomationConnection
    //   0	86	1	paramInt	int
    //   4	16	2	localObject1	Object
    //   43	1	2	localObject2	Object
    //   47	35	2	localRemoteException	RemoteException
    //   24	50	3	l	long
    //   79	5	5	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   31	40	43	finally
    //   51	61	43	finally
    //   31	40	47	android/os/RemoteException
    //   51	61	47	android/os/RemoteException
    //   7	21	79	finally
    //   81	83	79	finally
  }
  
  public void shutdown()
  {
    synchronized (mLock)
    {
      if (isConnectedLocked()) {
        throwIfCalledByNotTrustedUidLocked();
      }
      throwIfShutdownLocked();
      mIsShutdown = true;
      if (isConnectedLocked()) {
        disconnect();
      }
      return;
    }
  }
  
  public Bitmap takeScreenshot(Rect paramRect, int paramInt)
  {
    synchronized (mLock)
    {
      throwIfCalledByNotTrustedUidLocked();
      throwIfShutdownLocked();
      throwIfNotConnectedLocked();
      long l = Binder.clearCallingIdentity();
      try
      {
        paramRect = SurfaceControl.screenshot(paramRect, paramRect.width(), paramRect.height(), paramInt);
        return paramRect;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  public class Repeater
    implements Runnable
  {
    private final InputStream readFrom;
    private final OutputStream writeTo;
    
    public Repeater(InputStream paramInputStream, OutputStream paramOutputStream)
    {
      readFrom = paramInputStream;
      writeTo = paramOutputStream;
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: sipush 8192
      //   3: newarray byte
      //   5: astore_1
      //   6: aload_0
      //   7: getfield 24	android/app/UiAutomationConnection$Repeater:readFrom	Ljava/io/InputStream;
      //   10: aload_1
      //   11: invokevirtual 36	java/io/InputStream:read	([B)I
      //   14: istore_2
      //   15: iload_2
      //   16: ifge +18 -> 34
      //   19: aload_0
      //   20: getfield 24	android/app/UiAutomationConnection$Repeater:readFrom	Ljava/io/InputStream;
      //   23: invokestatic 42	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   26: aload_0
      //   27: getfield 26	android/app/UiAutomationConnection$Repeater:writeTo	Ljava/io/OutputStream;
      //   30: invokestatic 42	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   33: return
      //   34: aload_0
      //   35: getfield 26	android/app/UiAutomationConnection$Repeater:writeTo	Ljava/io/OutputStream;
      //   38: aload_1
      //   39: iconst_0
      //   40: iload_2
      //   41: invokevirtual 48	java/io/OutputStream:write	([BII)V
      //   44: aload_0
      //   45: getfield 26	android/app/UiAutomationConnection$Repeater:writeTo	Ljava/io/OutputStream;
      //   48: invokevirtual 51	java/io/OutputStream:flush	()V
      //   51: goto -45 -> 6
      //   54: astore_1
      //   55: goto +17 -> 72
      //   58: astore_1
      //   59: new 53	java/lang/RuntimeException
      //   62: astore_3
      //   63: aload_3
      //   64: ldc 55
      //   66: aload_1
      //   67: invokespecial 58	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   70: aload_3
      //   71: athrow
      //   72: aload_0
      //   73: getfield 24	android/app/UiAutomationConnection$Repeater:readFrom	Ljava/io/InputStream;
      //   76: invokestatic 42	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   79: aload_0
      //   80: getfield 26	android/app/UiAutomationConnection$Repeater:writeTo	Ljava/io/OutputStream;
      //   83: invokestatic 42	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   86: aload_1
      //   87: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	88	0	this	Repeater
      //   5	34	1	arrayOfByte	byte[]
      //   54	1	1	localObject	Object
      //   58	29	1	localIOException	IOException
      //   14	27	2	i	int
      //   62	9	3	localRuntimeException	RuntimeException
      // Exception table:
      //   from	to	target	type
      //   0	6	54	finally
      //   6	15	54	finally
      //   34	51	54	finally
      //   59	72	54	finally
      //   0	6	58	java/io/IOException
      //   6	15	58	java/io/IOException
      //   34	51	58	java/io/IOException
    }
  }
}
