package com.android.internal.statusbar;

import android.content.ComponentName;
import android.graphics.Rect;
import android.hardware.biometrics.IBiometricPromptReceiver;
import android.hardware.biometrics.IBiometricPromptReceiver.Stub;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IStatusBar
  extends IInterface
{
  public abstract void addQsTile(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void animateCollapsePanels()
    throws RemoteException;
  
  public abstract void animateExpandNotificationsPanel()
    throws RemoteException;
  
  public abstract void animateExpandSettingsPanel(String paramString)
    throws RemoteException;
  
  public abstract void appTransitionCancelled()
    throws RemoteException;
  
  public abstract void appTransitionFinished()
    throws RemoteException;
  
  public abstract void appTransitionPending()
    throws RemoteException;
  
  public abstract void appTransitionStarting(long paramLong1, long paramLong2)
    throws RemoteException;
  
  public abstract void cancelPreloadRecentApps()
    throws RemoteException;
  
  public abstract void clickQsTile(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void disable(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void dismissKeyboardShortcutsMenu()
    throws RemoteException;
  
  public abstract void handleSystemKey(int paramInt)
    throws RemoteException;
  
  public abstract void hideFingerprintDialog()
    throws RemoteException;
  
  public abstract void hideRecentApps(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void onCameraLaunchGestureDetected(int paramInt)
    throws RemoteException;
  
  public abstract void onFingerprintAuthenticated()
    throws RemoteException;
  
  public abstract void onFingerprintError(String paramString)
    throws RemoteException;
  
  public abstract void onFingerprintHelp(String paramString)
    throws RemoteException;
  
  public abstract void onProposedRotationChanged(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void preloadRecentApps()
    throws RemoteException;
  
  public abstract void remQsTile(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void removeIcon(String paramString)
    throws RemoteException;
  
  public abstract void setIcon(String paramString, StatusBarIcon paramStatusBarIcon)
    throws RemoteException;
  
  public abstract void setImeWindowStatus(IBinder paramIBinder, int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSystemUiVisibility(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rect paramRect1, Rect paramRect2)
    throws RemoteException;
  
  public abstract void setTopAppHidesStatusBar(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setWindowState(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void showAssistDisclosure()
    throws RemoteException;
  
  public abstract void showFingerprintDialog(Bundle paramBundle, IBiometricPromptReceiver paramIBiometricPromptReceiver)
    throws RemoteException;
  
  public abstract void showGlobalActionsMenu()
    throws RemoteException;
  
  public abstract void showPictureInPictureMenu()
    throws RemoteException;
  
  public abstract void showPinningEnterExitToast(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showPinningEscapeToast()
    throws RemoteException;
  
  public abstract void showRecentApps(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showScreenPinningRequest(int paramInt)
    throws RemoteException;
  
  public abstract void showShutdownUi(boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void showWirelessChargingAnimation(int paramInt)
    throws RemoteException;
  
  public abstract void startAssist(Bundle paramBundle)
    throws RemoteException;
  
  public abstract void toggleKeyboardShortcutsMenu(int paramInt)
    throws RemoteException;
  
  public abstract void togglePanel()
    throws RemoteException;
  
  public abstract void toggleRecentApps()
    throws RemoteException;
  
  public abstract void toggleSplitScreen()
    throws RemoteException;
  
  public abstract void topAppWindowChanged(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStatusBar
  {
    private static final String DESCRIPTOR = "com.android.internal.statusbar.IStatusBar";
    static final int TRANSACTION_addQsTile = 33;
    static final int TRANSACTION_animateCollapsePanels = 6;
    static final int TRANSACTION_animateExpandNotificationsPanel = 4;
    static final int TRANSACTION_animateExpandSettingsPanel = 5;
    static final int TRANSACTION_appTransitionCancelled = 23;
    static final int TRANSACTION_appTransitionFinished = 25;
    static final int TRANSACTION_appTransitionPending = 22;
    static final int TRANSACTION_appTransitionStarting = 24;
    static final int TRANSACTION_cancelPreloadRecentApps = 18;
    static final int TRANSACTION_clickQsTile = 35;
    static final int TRANSACTION_disable = 3;
    static final int TRANSACTION_dismissKeyboardShortcutsMenu = 20;
    static final int TRANSACTION_handleSystemKey = 36;
    static final int TRANSACTION_hideFingerprintDialog = 44;
    static final int TRANSACTION_hideRecentApps = 14;
    static final int TRANSACTION_onCameraLaunchGestureDetected = 28;
    static final int TRANSACTION_onFingerprintAuthenticated = 41;
    static final int TRANSACTION_onFingerprintError = 43;
    static final int TRANSACTION_onFingerprintHelp = 42;
    static final int TRANSACTION_onProposedRotationChanged = 31;
    static final int TRANSACTION_preloadRecentApps = 17;
    static final int TRANSACTION_remQsTile = 34;
    static final int TRANSACTION_removeIcon = 2;
    static final int TRANSACTION_setIcon = 1;
    static final int TRANSACTION_setImeWindowStatus = 11;
    static final int TRANSACTION_setSystemUiVisibility = 9;
    static final int TRANSACTION_setTopAppHidesStatusBar = 32;
    static final int TRANSACTION_setWindowState = 12;
    static final int TRANSACTION_showAssistDisclosure = 26;
    static final int TRANSACTION_showFingerprintDialog = 40;
    static final int TRANSACTION_showGlobalActionsMenu = 30;
    static final int TRANSACTION_showPictureInPictureMenu = 29;
    static final int TRANSACTION_showPinningEnterExitToast = 37;
    static final int TRANSACTION_showPinningEscapeToast = 38;
    static final int TRANSACTION_showRecentApps = 13;
    static final int TRANSACTION_showScreenPinningRequest = 19;
    static final int TRANSACTION_showShutdownUi = 39;
    static final int TRANSACTION_showWirelessChargingAnimation = 8;
    static final int TRANSACTION_startAssist = 27;
    static final int TRANSACTION_toggleKeyboardShortcutsMenu = 21;
    static final int TRANSACTION_togglePanel = 7;
    static final int TRANSACTION_toggleRecentApps = 15;
    static final int TRANSACTION_toggleSplitScreen = 16;
    static final int TRANSACTION_topAppWindowChanged = 10;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.statusbar.IStatusBar");
    }
    
    public static IStatusBar asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.statusbar.IStatusBar");
      if ((localIInterface != null) && ((localIInterface instanceof IStatusBar))) {
        return (IStatusBar)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 44: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          hideFingerprintDialog();
          return true;
        case 43: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          onFingerprintError(paramParcel1.readString());
          return true;
        case 42: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          onFingerprintHelp(paramParcel1.readString());
          return true;
        case 41: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          onFingerprintAuthenticated();
          return true;
        case 40: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject7;
          }
          showFingerprintDialog(paramParcel2, IBiometricPromptReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 39: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          }
          showShutdownUi(bool8, paramParcel1.readString());
          return true;
        case 38: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          showPinningEscapeToast();
          return true;
        case 37: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          bool8 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          }
          showPinningEnterExitToast(bool8);
          return true;
        case 36: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          handleSystemKey(paramParcel1.readInt());
          return true;
        case 35: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          clickQsTile(paramParcel1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          remQsTile(paramParcel1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          addQsTile(paramParcel1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          bool8 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          }
          setTopAppHidesStatusBar(bool8);
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          paramInt1 = paramParcel1.readInt();
          bool8 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          }
          onProposedRotationChanged(paramInt1, bool8);
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          showGlobalActionsMenu();
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          showPictureInPictureMenu();
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          onCameraLaunchGestureDetected(paramParcel1.readInt());
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          startAssist(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          showAssistDisclosure();
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          appTransitionFinished();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          appTransitionStarting(paramParcel1.readLong(), paramParcel1.readLong());
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          appTransitionCancelled();
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          appTransitionPending();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          toggleKeyboardShortcutsMenu(paramParcel1.readInt());
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          dismissKeyboardShortcutsMenu();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          showScreenPinningRequest(paramParcel1.readInt());
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          cancelPreloadRecentApps();
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          preloadRecentApps();
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          toggleSplitScreen();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          toggleRecentApps();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          } else {
            bool8 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          hideRecentApps(bool8, bool4);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          bool8 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          }
          showRecentApps(bool8);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          setWindowState(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          paramParcel2 = paramParcel1.readStrongBinder();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          bool8 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          }
          setImeWindowStatus(paramParcel2, paramInt1, paramInt2, bool8);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          bool8 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          }
          topAppWindowChanged(bool8);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          int i = paramParcel1.readInt();
          int j = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject5) {
            break;
          }
          setSystemUiVisibility(i, j, paramInt1, paramInt2, paramParcel2, paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          showWirelessChargingAnimation(paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          togglePanel();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          animateCollapsePanels();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          animateExpandSettingsPanel(paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          animateExpandNotificationsPanel();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          disable(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
          removeIcon(paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBar");
        paramParcel2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (StatusBarIcon)StatusBarIcon.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject6;
        }
        setIcon(paramParcel2, paramParcel1);
        return true;
      }
      paramParcel2.writeString("com.android.internal.statusbar.IStatusBar");
      return true;
    }
    
    private static class Proxy
      implements IStatusBar
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addQsTile(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(33, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void animateCollapsePanels()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void animateExpandNotificationsPanel()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void animateExpandSettingsPanel(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeString(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void appTransitionCancelled()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(23, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void appTransitionFinished()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(25, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void appTransitionPending()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(22, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void appTransitionStarting(long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeLong(paramLong1);
          localParcel.writeLong(paramLong2);
          mRemote.transact(24, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void cancelPreloadRecentApps()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(18, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void clickQsTile(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(35, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void disable(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void dismissKeyboardShortcutsMenu()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(20, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.statusbar.IStatusBar";
      }
      
      public void handleSystemKey(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt);
          mRemote.transact(36, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void hideFingerprintDialog()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(44, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void hideRecentApps(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramBoolean1);
          localParcel.writeInt(paramBoolean2);
          mRemote.transact(14, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onCameraLaunchGestureDetected(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt);
          mRemote.transact(28, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFingerprintAuthenticated()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(41, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFingerprintError(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeString(paramString);
          mRemote.transact(43, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onFingerprintHelp(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeString(paramString);
          mRemote.transact(42, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onProposedRotationChanged(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(31, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void preloadRecentApps()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(17, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void remQsTile(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          if (paramComponentName != null)
          {
            localParcel.writeInt(1);
            paramComponentName.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(34, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeIcon(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setIcon(String paramString, StatusBarIcon paramStatusBarIcon)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeString(paramString);
          if (paramStatusBarIcon != null)
          {
            localParcel.writeInt(1);
            paramStatusBarIcon.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setImeWindowStatus(IBinder paramIBinder, int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeStrongBinder(paramIBinder);
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setSystemUiVisibility(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rect paramRect1, Rect paramRect2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeInt(paramInt4);
          if (paramRect1 != null)
          {
            localParcel.writeInt(1);
            paramRect1.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramRect2 != null)
          {
            localParcel.writeInt(1);
            paramRect2.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setTopAppHidesStatusBar(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(32, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setWindowState(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(12, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showAssistDisclosure()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(26, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showFingerprintDialog(Bundle paramBundle, IBiometricPromptReceiver paramIBiometricPromptReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramIBiometricPromptReceiver != null) {
            paramBundle = paramIBiometricPromptReceiver.asBinder();
          } else {
            paramBundle = null;
          }
          localParcel.writeStrongBinder(paramBundle);
          mRemote.transact(40, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showGlobalActionsMenu()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(30, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showPictureInPictureMenu()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(29, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showPinningEnterExitToast(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(37, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showPinningEscapeToast()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(38, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showRecentApps(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showScreenPinningRequest(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt);
          mRemote.transact(19, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showShutdownUi(boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramBoolean);
          localParcel.writeString(paramString);
          mRemote.transact(39, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void showWirelessChargingAnimation(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void startAssist(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(27, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void toggleKeyboardShortcutsMenu(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramInt);
          mRemote.transact(21, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void togglePanel()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void toggleRecentApps()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(15, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void toggleSplitScreen()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          mRemote.transact(16, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void topAppWindowChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.statusbar.IStatusBar");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
