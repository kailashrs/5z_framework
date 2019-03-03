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
import java.util.ArrayList;
import java.util.List;

public abstract interface IStatusBarService
  extends IInterface
{
  public abstract void addTile(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void clearNotificationEffects()
    throws RemoteException;
  
  public abstract void clickTile(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void collapsePanels()
    throws RemoteException;
  
  public abstract void disable(int paramInt, IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void disable2(int paramInt, IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void disable2ForUser(int paramInt1, IBinder paramIBinder, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void disableForUser(int paramInt1, IBinder paramIBinder, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void expandNotificationsPanel()
    throws RemoteException;
  
  public abstract void expandSettingsPanel(String paramString)
    throws RemoteException;
  
  public abstract void handleSystemKey(int paramInt)
    throws RemoteException;
  
  public abstract void hideFingerprintDialog()
    throws RemoteException;
  
  public abstract void onClearAllNotifications(int paramInt)
    throws RemoteException;
  
  public abstract void onFingerprintAuthenticated()
    throws RemoteException;
  
  public abstract void onFingerprintError(String paramString)
    throws RemoteException;
  
  public abstract void onFingerprintHelp(String paramString)
    throws RemoteException;
  
  public abstract void onGlobalActionsHidden()
    throws RemoteException;
  
  public abstract void onGlobalActionsShown()
    throws RemoteException;
  
  public abstract void onNotificationActionClick(String paramString, int paramInt, NotificationVisibility paramNotificationVisibility)
    throws RemoteException;
  
  public abstract void onNotificationClear(String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3, int paramInt3, NotificationVisibility paramNotificationVisibility)
    throws RemoteException;
  
  public abstract void onNotificationClick(String paramString, NotificationVisibility paramNotificationVisibility)
    throws RemoteException;
  
  public abstract void onNotificationDirectReplied(String paramString)
    throws RemoteException;
  
  public abstract void onNotificationError(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, int paramInt4)
    throws RemoteException;
  
  public abstract void onNotificationExpansionChanged(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void onNotificationSettingsViewed(String paramString)
    throws RemoteException;
  
  public abstract void onNotificationSmartRepliesAdded(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onNotificationSmartReplySent(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void onNotificationVisibilityChanged(NotificationVisibility[] paramArrayOfNotificationVisibility1, NotificationVisibility[] paramArrayOfNotificationVisibility2)
    throws RemoteException;
  
  public abstract void onPanelHidden()
    throws RemoteException;
  
  public abstract void onPanelRevealed(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void reboot(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void registerStatusBar(IStatusBar paramIStatusBar, List<String> paramList, List<StatusBarIcon> paramList1, int[] paramArrayOfInt, List<IBinder> paramList2, Rect paramRect1, Rect paramRect2)
    throws RemoteException;
  
  public abstract void remTile(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void removeIcon(String paramString)
    throws RemoteException;
  
  public abstract void setIcon(String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3)
    throws RemoteException;
  
  public abstract void setIconVisibility(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setImeWindowStatus(IBinder paramIBinder, int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSystemUiVisibility(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract void showFingerprintDialog(Bundle paramBundle, IBiometricPromptReceiver paramIBiometricPromptReceiver)
    throws RemoteException;
  
  public abstract void showPinningEnterExitToast(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showPinningEscapeToast()
    throws RemoteException;
  
  public abstract void shutdown()
    throws RemoteException;
  
  public abstract void togglePanel()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStatusBarService
  {
    private static final String DESCRIPTOR = "com.android.internal.statusbar.IStatusBarService";
    static final int TRANSACTION_addTile = 33;
    static final int TRANSACTION_clearNotificationEffects = 16;
    static final int TRANSACTION_clickTile = 35;
    static final int TRANSACTION_collapsePanels = 2;
    static final int TRANSACTION_disable = 4;
    static final int TRANSACTION_disable2 = 6;
    static final int TRANSACTION_disable2ForUser = 7;
    static final int TRANSACTION_disableForUser = 5;
    static final int TRANSACTION_expandNotificationsPanel = 1;
    static final int TRANSACTION_expandSettingsPanel = 12;
    static final int TRANSACTION_handleSystemKey = 36;
    static final int TRANSACTION_hideFingerprintDialog = 43;
    static final int TRANSACTION_onClearAllNotifications = 20;
    static final int TRANSACTION_onFingerprintAuthenticated = 40;
    static final int TRANSACTION_onFingerprintError = 42;
    static final int TRANSACTION_onFingerprintHelp = 41;
    static final int TRANSACTION_onGlobalActionsHidden = 30;
    static final int TRANSACTION_onGlobalActionsShown = 29;
    static final int TRANSACTION_onNotificationActionClick = 18;
    static final int TRANSACTION_onNotificationClear = 21;
    static final int TRANSACTION_onNotificationClick = 17;
    static final int TRANSACTION_onNotificationDirectReplied = 24;
    static final int TRANSACTION_onNotificationError = 19;
    static final int TRANSACTION_onNotificationExpansionChanged = 23;
    static final int TRANSACTION_onNotificationSettingsViewed = 27;
    static final int TRANSACTION_onNotificationSmartRepliesAdded = 25;
    static final int TRANSACTION_onNotificationSmartReplySent = 26;
    static final int TRANSACTION_onNotificationVisibilityChanged = 22;
    static final int TRANSACTION_onPanelHidden = 15;
    static final int TRANSACTION_onPanelRevealed = 14;
    static final int TRANSACTION_reboot = 32;
    static final int TRANSACTION_registerStatusBar = 13;
    static final int TRANSACTION_remTile = 34;
    static final int TRANSACTION_removeIcon = 10;
    static final int TRANSACTION_setIcon = 8;
    static final int TRANSACTION_setIconVisibility = 9;
    static final int TRANSACTION_setImeWindowStatus = 11;
    static final int TRANSACTION_setSystemUiVisibility = 28;
    static final int TRANSACTION_showFingerprintDialog = 39;
    static final int TRANSACTION_showPinningEnterExitToast = 37;
    static final int TRANSACTION_showPinningEscapeToast = 38;
    static final int TRANSACTION_shutdown = 31;
    static final int TRANSACTION_togglePanel = 3;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.statusbar.IStatusBarService");
    }
    
    public static IStatusBarService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.statusbar.IStatusBarService");
      if ((localIInterface != null) && ((localIInterface instanceof IStatusBarService))) {
        return (IStatusBarService)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        Rect localRect1 = null;
        ArrayList localArrayList = null;
        Rect localRect2 = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 43: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          hideFingerprintDialog();
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onFingerprintError(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 41: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onFingerprintHelp(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onFingerprintAuthenticated();
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject3;
          }
          showFingerprintDialog((Bundle)localObject1, IBiometricPromptReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          showPinningEscapeToast();
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          showPinningEnterExitToast(bool6);
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          handleSystemKey(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localRect1;
          }
          clickTile(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localArrayList;
          }
          remTile(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localRect2;
          }
          addTile(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          bool6 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          reboot(bool6);
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          shutdown();
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onGlobalActionsHidden();
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onGlobalActionsShown();
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          setSystemUiVisibility(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onNotificationSettingsViewed(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onNotificationSmartReplySent(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onNotificationSmartRepliesAdded(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onNotificationDirectReplied(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          } else {
            bool6 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onNotificationExpansionChanged((String)localObject1, bool6, bool2);
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onNotificationVisibilityChanged((NotificationVisibility[])paramParcel1.createTypedArray(NotificationVisibility.CREATOR), (NotificationVisibility[])paramParcel1.createTypedArray(NotificationVisibility.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          localObject2 = paramParcel1.readString();
          localObject3 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          int i = paramParcel1.readInt();
          localObject1 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationVisibility)NotificationVisibility.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          onNotificationClear((String)localObject2, (String)localObject3, paramInt1, i, (String)localObject1, paramInt2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onClearAllNotifications(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onNotificationError(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          localObject2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationVisibility)NotificationVisibility.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject1;
          }
          onNotificationActionClick((String)localObject2, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationVisibility)NotificationVisibility.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          onNotificationClick((String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          clearNotificationEffects();
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          onPanelHidden();
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          bool6 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          onPanelRevealed(bool6, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          localObject2 = IStatusBar.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject3 = new ArrayList();
          localObject1 = new ArrayList();
          paramInt1 = paramParcel1.readInt();
          if (paramInt1 < 0) {
            paramParcel1 = null;
          } else {
            paramParcel1 = new int[paramInt1];
          }
          localArrayList = new ArrayList();
          localRect1 = new Rect();
          localRect2 = new Rect();
          registerStatusBar((IStatusBar)localObject2, (List)localObject3, (List)localObject1, paramParcel1, localArrayList, localRect1, localRect2);
          paramParcel2.writeNoException();
          paramParcel2.writeStringList((List)localObject3);
          paramParcel2.writeTypedList((List)localObject1);
          paramParcel2.writeIntArray(paramParcel1);
          paramParcel2.writeBinderList(localArrayList);
          paramParcel2.writeInt(1);
          localRect1.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(1);
          localRect2.writeToParcel(paramParcel2, 1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          expandSettingsPanel(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          localObject1 = paramParcel1.readStrongBinder();
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          bool6 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          setImeWindowStatus((IBinder)localObject1, paramInt1, paramInt2, bool6);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          removeIcon(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          localObject1 = paramParcel1.readString();
          bool6 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool6 = true;
          }
          setIconVisibility((String)localObject1, bool6);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          setIcon(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          disable2ForUser(paramParcel1.readInt(), paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          disable2(paramParcel1.readInt(), paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          disableForUser(paramParcel1.readInt(), paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          disable(paramParcel1.readInt(), paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          togglePanel();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
          collapsePanels();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.statusbar.IStatusBarService");
        expandNotificationsPanel();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.statusbar.IStatusBarService");
      return true;
    }
    
    private static class Proxy
      implements IStatusBarService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addTile(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void clearNotificationEffects()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clickTile(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void collapsePanels()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disable(int paramInt, IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disable2(int paramInt, IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disable2ForUser(int paramInt1, IBinder paramIBinder, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableForUser(int paramInt1, IBinder paramIBinder, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void expandNotificationsPanel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void expandSettingsPanel(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.statusbar.IStatusBarService";
      }
      
      public void handleSystemKey(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void hideFingerprintDialog()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onClearAllNotifications(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onFingerprintAuthenticated()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onFingerprintError(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onFingerprintHelp(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onGlobalActionsHidden()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onGlobalActionsShown()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationActionClick(String paramString, int paramInt, NotificationVisibility paramNotificationVisibility)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramNotificationVisibility != null)
          {
            localParcel1.writeInt(1);
            paramNotificationVisibility.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationClear(String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3, int paramInt3, NotificationVisibility paramNotificationVisibility)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt3);
          if (paramNotificationVisibility != null)
          {
            localParcel1.writeInt(1);
            paramNotificationVisibility.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationClick(String paramString, NotificationVisibility paramNotificationVisibility)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          if (paramNotificationVisibility != null)
          {
            localParcel1.writeInt(1);
            paramNotificationVisibility.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationDirectReplied(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationError(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationExpansionChanged(String paramString, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationSettingsViewed(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationSmartRepliesAdded(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationSmartReplySent(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onNotificationVisibilityChanged(NotificationVisibility[] paramArrayOfNotificationVisibility1, NotificationVisibility[] paramArrayOfNotificationVisibility2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeTypedArray(paramArrayOfNotificationVisibility1, 0);
          localParcel1.writeTypedArray(paramArrayOfNotificationVisibility2, 0);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onPanelHidden()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onPanelRevealed(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reboot(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerStatusBar(IStatusBar paramIStatusBar, List<String> paramList, List<StatusBarIcon> paramList1, int[] paramArrayOfInt, List<IBinder> paramList2, Rect paramRect1, Rect paramRect2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          if (paramIStatusBar != null) {
            paramIStatusBar = paramIStatusBar.asBinder();
          } else {
            paramIStatusBar = null;
          }
          localParcel1.writeStrongBinder(paramIStatusBar);
          if (paramArrayOfInt == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfInt.length);
          }
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          localParcel2.readStringList(paramList);
          localParcel2.readTypedList(paramList1, StatusBarIcon.CREATOR);
          localParcel2.readIntArray(paramArrayOfInt);
          localParcel2.readBinderList(paramList2);
          if (localParcel2.readInt() != 0) {
            paramRect1.readFromParcel(localParcel2);
          }
          if (localParcel2.readInt() != 0) {
            paramRect2.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void remTile(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeIcon(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setIcon(String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString3);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setIconVisibility(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setImeWindowStatus(IBinder paramIBinder, int paramInt1, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSystemUiVisibility(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showFingerprintDialog(Bundle paramBundle, IBiometricPromptReceiver paramIBiometricPromptReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIBiometricPromptReceiver != null) {
            paramBundle = paramIBiometricPromptReceiver.asBinder();
          } else {
            paramBundle = null;
          }
          localParcel1.writeStrongBinder(paramBundle);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showPinningEnterExitToast(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showPinningEscapeToast()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void shutdown()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void togglePanel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.statusbar.IStatusBarService");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
