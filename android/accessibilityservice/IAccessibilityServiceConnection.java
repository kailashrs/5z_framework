package android.accessibilityservice;

import android.content.pm.ParceledListSlice;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.accessibility.AccessibilityWindowInfo;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub;
import java.util.ArrayList;
import java.util.List;

public abstract interface IAccessibilityServiceConnection
  extends IInterface
{
  public abstract void disableSelf()
    throws RemoteException;
  
  public abstract String[] findAccessibilityNodeInfoByAccessibilityId(int paramInt1, long paramLong1, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, long paramLong2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract String[] findAccessibilityNodeInfosByText(int paramInt1, long paramLong1, String paramString, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
    throws RemoteException;
  
  public abstract String[] findAccessibilityNodeInfosByViewId(int paramInt1, long paramLong1, String paramString, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
    throws RemoteException;
  
  public abstract String[] findFocus(int paramInt1, long paramLong1, int paramInt2, int paramInt3, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
    throws RemoteException;
  
  public abstract String[] focusSearch(int paramInt1, long paramLong1, int paramInt2, int paramInt3, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
    throws RemoteException;
  
  public abstract float getMagnificationCenterX()
    throws RemoteException;
  
  public abstract float getMagnificationCenterY()
    throws RemoteException;
  
  public abstract Region getMagnificationRegion()
    throws RemoteException;
  
  public abstract float getMagnificationScale()
    throws RemoteException;
  
  public abstract AccessibilityServiceInfo getServiceInfo()
    throws RemoteException;
  
  public abstract AccessibilityWindowInfo getWindow(int paramInt)
    throws RemoteException;
  
  public abstract List<AccessibilityWindowInfo> getWindows()
    throws RemoteException;
  
  public abstract boolean isAccessibilityButtonAvailable()
    throws RemoteException;
  
  public abstract boolean isFingerprintGestureDetectionAvailable()
    throws RemoteException;
  
  public abstract boolean performAccessibilityAction(int paramInt1, long paramLong1, int paramInt2, Bundle paramBundle, int paramInt3, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
    throws RemoteException;
  
  public abstract boolean performGlobalAction(int paramInt)
    throws RemoteException;
  
  public abstract boolean resetMagnification(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void sendGesture(int paramInt, ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void setMagnificationCallbackEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setMagnificationScaleAndCenter(float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setOnKeyEventResult(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setServiceInfo(AccessibilityServiceInfo paramAccessibilityServiceInfo)
    throws RemoteException;
  
  public abstract void setSoftKeyboardCallbackEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setSoftKeyboardShowMode(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAccessibilityServiceConnection
  {
    private static final String DESCRIPTOR = "android.accessibilityservice.IAccessibilityServiceConnection";
    static final int TRANSACTION_disableSelf = 12;
    static final int TRANSACTION_findAccessibilityNodeInfoByAccessibilityId = 2;
    static final int TRANSACTION_findAccessibilityNodeInfosByText = 3;
    static final int TRANSACTION_findAccessibilityNodeInfosByViewId = 4;
    static final int TRANSACTION_findFocus = 5;
    static final int TRANSACTION_focusSearch = 6;
    static final int TRANSACTION_getMagnificationCenterX = 15;
    static final int TRANSACTION_getMagnificationCenterY = 16;
    static final int TRANSACTION_getMagnificationRegion = 17;
    static final int TRANSACTION_getMagnificationScale = 14;
    static final int TRANSACTION_getServiceInfo = 10;
    static final int TRANSACTION_getWindow = 8;
    static final int TRANSACTION_getWindows = 9;
    static final int TRANSACTION_isAccessibilityButtonAvailable = 23;
    static final int TRANSACTION_isFingerprintGestureDetectionAvailable = 25;
    static final int TRANSACTION_performAccessibilityAction = 7;
    static final int TRANSACTION_performGlobalAction = 11;
    static final int TRANSACTION_resetMagnification = 18;
    static final int TRANSACTION_sendGesture = 24;
    static final int TRANSACTION_setMagnificationCallbackEnabled = 20;
    static final int TRANSACTION_setMagnificationScaleAndCenter = 19;
    static final int TRANSACTION_setOnKeyEventResult = 13;
    static final int TRANSACTION_setServiceInfo = 1;
    static final int TRANSACTION_setSoftKeyboardCallbackEnabled = 22;
    static final int TRANSACTION_setSoftKeyboardShowMode = 21;
    
    public Stub()
    {
      attachInterface(this, "android.accessibilityservice.IAccessibilityServiceConnection");
    }
    
    public static IAccessibilityServiceConnection asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.accessibilityservice.IAccessibilityServiceConnection");
      if ((localIInterface != null) && ((localIInterface instanceof IAccessibilityServiceConnection))) {
        return (IAccessibilityServiceConnection)localIInterface;
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
        IAccessibilityInteractionConnectionCallback localIAccessibilityInteractionConnectionCallback = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        float f2;
        long l1;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 25: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramInt1 = isFingerprintGestureDetectionAvailable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIAccessibilityInteractionConnectionCallback;
          }
          sendGesture(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramInt1 = isAccessibilityButtonAvailable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          setSoftKeyboardCallbackEnabled(bool5);
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramInt1 = setSoftKeyboardShowMode(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          bool5 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          setMagnificationCallbackEnabled(bool5);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          float f1 = paramParcel1.readFloat();
          f2 = paramParcel1.readFloat();
          float f3 = paramParcel1.readFloat();
          bool5 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          paramInt1 = setMagnificationScaleAndCenter(f1, f2, f3, bool5);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          bool5 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          paramInt1 = resetMagnification(bool5);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramParcel1 = getMagnificationRegion();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          f2 = getMagnificationCenterY();
          paramParcel2.writeNoException();
          paramParcel2.writeFloat(f2);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          f2 = getMagnificationCenterX();
          paramParcel2.writeNoException();
          paramParcel2.writeFloat(f2);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          f2 = getMagnificationScale();
          paramParcel2.writeNoException();
          paramParcel2.writeFloat(f2);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          bool5 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          setOnKeyEventResult(bool5, paramParcel1.readInt());
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          disableSelf();
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramInt1 = performGlobalAction(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramParcel1 = getServiceInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramParcel1 = getWindows();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramParcel1 = getWindow(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramInt1 = paramParcel1.readInt();
          l1 = paramParcel1.readLong();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; localObject2 = localObject1) {
            break;
          }
          paramInt1 = performAccessibilityAction(paramInt1, l1, paramInt2, (Bundle)localObject2, paramParcel1.readInt(), IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramParcel1 = focusSearch(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt(), IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramParcel1 = findFocus(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readInt(), paramParcel1.readInt(), IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramParcel1 = findAccessibilityNodeInfosByViewId(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readString(), paramParcel1.readInt(), IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramParcel1 = findAccessibilityNodeInfosByText(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readString(), paramParcel1.readInt(), IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
          paramInt1 = paramParcel1.readInt();
          long l2 = paramParcel1.readLong();
          paramInt2 = paramParcel1.readInt();
          localIAccessibilityInteractionConnectionCallback = IAccessibilityInteractionConnectionCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          int i = paramParcel1.readInt();
          l1 = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject2) {
            break;
          }
          paramParcel1 = findAccessibilityNodeInfoByAccessibilityId(paramInt1, l2, paramInt2, localIAccessibilityInteractionConnectionCallback, i, l1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.accessibilityservice.IAccessibilityServiceConnection");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (AccessibilityServiceInfo)AccessibilityServiceInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject3;
        }
        setServiceInfo(paramParcel1);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.accessibilityservice.IAccessibilityServiceConnection");
      return true;
    }
    
    private static class Proxy
      implements IAccessibilityServiceConnection
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void disableSelf()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
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
      
      public String[] findAccessibilityNodeInfoByAccessibilityId(int paramInt1, long paramLong1, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, long paramLong2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeInt(paramInt2);
          if (paramIAccessibilityInteractionConnectionCallback != null) {
            paramIAccessibilityInteractionConnectionCallback = paramIAccessibilityInteractionConnectionCallback.asBinder();
          } else {
            paramIAccessibilityInteractionConnectionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIAccessibilityInteractionConnectionCallback);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeLong(paramLong2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIAccessibilityInteractionConnectionCallback = localParcel2.createStringArray();
          return paramIAccessibilityInteractionConnectionCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] findAccessibilityNodeInfosByText(int paramInt1, long paramLong1, String paramString, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          if (paramIAccessibilityInteractionConnectionCallback != null) {
            paramString = paramIAccessibilityInteractionConnectionCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] findAccessibilityNodeInfosByViewId(int paramInt1, long paramLong1, String paramString, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          if (paramIAccessibilityInteractionConnectionCallback != null) {
            paramString = paramIAccessibilityInteractionConnectionCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] findFocus(int paramInt1, long paramLong1, int paramInt2, int paramInt3, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          if (paramIAccessibilityInteractionConnectionCallback != null) {
            paramIAccessibilityInteractionConnectionCallback = paramIAccessibilityInteractionConnectionCallback.asBinder();
          } else {
            paramIAccessibilityInteractionConnectionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIAccessibilityInteractionConnectionCallback);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIAccessibilityInteractionConnectionCallback = localParcel2.createStringArray();
          return paramIAccessibilityInteractionConnectionCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] focusSearch(int paramInt1, long paramLong1, int paramInt2, int paramInt3, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          if (paramIAccessibilityInteractionConnectionCallback != null) {
            paramIAccessibilityInteractionConnectionCallback = paramIAccessibilityInteractionConnectionCallback.asBinder();
          } else {
            paramIAccessibilityInteractionConnectionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIAccessibilityInteractionConnectionCallback);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIAccessibilityInteractionConnectionCallback = localParcel2.createStringArray();
          return paramIAccessibilityInteractionConnectionCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.accessibilityservice.IAccessibilityServiceConnection";
      }
      
      public float getMagnificationCenterX()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          float f = localParcel2.readFloat();
          return f;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public float getMagnificationCenterY()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          float f = localParcel2.readFloat();
          return f;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Region getMagnificationRegion()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Region localRegion;
          if (localParcel2.readInt() != 0) {
            localRegion = (Region)Region.CREATOR.createFromParcel(localParcel2);
          } else {
            localRegion = null;
          }
          return localRegion;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public float getMagnificationScale()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          float f = localParcel2.readFloat();
          return f;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public AccessibilityServiceInfo getServiceInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          AccessibilityServiceInfo localAccessibilityServiceInfo;
          if (localParcel2.readInt() != 0) {
            localAccessibilityServiceInfo = (AccessibilityServiceInfo)AccessibilityServiceInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localAccessibilityServiceInfo = null;
          }
          return localAccessibilityServiceInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public AccessibilityWindowInfo getWindow(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          AccessibilityWindowInfo localAccessibilityWindowInfo;
          if (localParcel2.readInt() != 0) {
            localAccessibilityWindowInfo = (AccessibilityWindowInfo)AccessibilityWindowInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localAccessibilityWindowInfo = null;
          }
          return localAccessibilityWindowInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<AccessibilityWindowInfo> getWindows()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(AccessibilityWindowInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAccessibilityButtonAvailable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isFingerprintGestureDetectionAvailable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean performAccessibilityAction(int paramInt1, long paramLong1, int paramInt2, Bundle paramBundle, int paramInt3, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeInt(paramInt2);
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt3);
          if (paramIAccessibilityInteractionConnectionCallback != null) {
            paramBundle = paramIAccessibilityInteractionConnectionCallback.asBinder();
          } else {
            paramBundle = null;
          }
          localParcel1.writeStrongBinder(paramBundle);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean performGlobalAction(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean resetMagnification(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendGesture(int paramInt, ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt);
          if (paramParceledListSlice != null)
          {
            localParcel1.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setMagnificationCallbackEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramBoolean);
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
      
      public boolean setMagnificationScaleAndCenter(float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeFloat(paramFloat1);
          localParcel1.writeFloat(paramFloat2);
          localParcel1.writeFloat(paramFloat3);
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOnKeyEventResult(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel.writeInt(paramBoolean);
          localParcel.writeInt(paramInt);
          mRemote.transact(13, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setServiceInfo(AccessibilityServiceInfo paramAccessibilityServiceInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          if (paramAccessibilityServiceInfo != null)
          {
            localParcel1.writeInt(1);
            paramAccessibilityServiceInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setSoftKeyboardCallbackEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramBoolean);
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
      
      public boolean setSoftKeyboardShowMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.accessibilityservice.IAccessibilityServiceConnection");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
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
