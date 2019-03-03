package android.hardware.display;

import android.content.pm.ParceledListSlice;
import android.graphics.Point;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjection.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.DisplayInfo;
import android.view.Surface;

public abstract interface IDisplayManager
  extends IInterface
{
  public abstract void connectWifiDisplay(String paramString)
    throws RemoteException;
  
  public abstract int createVirtualDisplay(IVirtualDisplayCallback paramIVirtualDisplayCallback, IMediaProjection paramIMediaProjection, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, Surface paramSurface, int paramInt4, String paramString3)
    throws RemoteException;
  
  public abstract void disconnectWifiDisplay()
    throws RemoteException;
  
  public abstract void forgetWifiDisplay(String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice getAmbientBrightnessStats()
    throws RemoteException;
  
  public abstract BrightnessConfiguration getBrightnessConfigurationForUser(int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getBrightnessEvents(String paramString)
    throws RemoteException;
  
  public abstract BrightnessConfiguration getDefaultBrightnessConfiguration()
    throws RemoteException;
  
  public abstract int[] getDisplayIds()
    throws RemoteException;
  
  public abstract DisplayInfo getDisplayInfo(int paramInt)
    throws RemoteException;
  
  public abstract Curve getMinimumBrightnessCurve()
    throws RemoteException;
  
  public abstract Point getStableDisplaySize()
    throws RemoteException;
  
  public abstract WifiDisplayStatus getWifiDisplayStatus()
    throws RemoteException;
  
  public abstract void pauseWifiDisplay()
    throws RemoteException;
  
  public abstract void registerCallback(IDisplayManagerCallback paramIDisplayManagerCallback)
    throws RemoteException;
  
  public abstract void releaseVirtualDisplay(IVirtualDisplayCallback paramIVirtualDisplayCallback)
    throws RemoteException;
  
  public abstract void renameWifiDisplay(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void requestColorMode(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void resizeVirtualDisplay(IVirtualDisplayCallback paramIVirtualDisplayCallback, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void resumeWifiDisplay()
    throws RemoteException;
  
  public abstract void setBrightnessConfigurationForUser(BrightnessConfiguration paramBrightnessConfiguration, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setSaturationLevel(float paramFloat)
    throws RemoteException;
  
  public abstract void setTemporaryAutoBrightnessAdjustment(float paramFloat)
    throws RemoteException;
  
  public abstract void setTemporaryBrightness(int paramInt)
    throws RemoteException;
  
  public abstract void setVirtualDisplaySurface(IVirtualDisplayCallback paramIVirtualDisplayCallback, Surface paramSurface)
    throws RemoteException;
  
  public abstract void startWifiDisplayScan()
    throws RemoteException;
  
  public abstract void stopWifiDisplayScan()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDisplayManager
  {
    private static final String DESCRIPTOR = "android.hardware.display.IDisplayManager";
    static final int TRANSACTION_connectWifiDisplay = 6;
    static final int TRANSACTION_createVirtualDisplay = 15;
    static final int TRANSACTION_disconnectWifiDisplay = 7;
    static final int TRANSACTION_forgetWifiDisplay = 9;
    static final int TRANSACTION_getAmbientBrightnessStats = 21;
    static final int TRANSACTION_getBrightnessConfigurationForUser = 23;
    static final int TRANSACTION_getBrightnessEvents = 20;
    static final int TRANSACTION_getDefaultBrightnessConfiguration = 24;
    static final int TRANSACTION_getDisplayIds = 2;
    static final int TRANSACTION_getDisplayInfo = 1;
    static final int TRANSACTION_getMinimumBrightnessCurve = 27;
    static final int TRANSACTION_getStableDisplaySize = 19;
    static final int TRANSACTION_getWifiDisplayStatus = 12;
    static final int TRANSACTION_pauseWifiDisplay = 10;
    static final int TRANSACTION_registerCallback = 3;
    static final int TRANSACTION_releaseVirtualDisplay = 18;
    static final int TRANSACTION_renameWifiDisplay = 8;
    static final int TRANSACTION_requestColorMode = 13;
    static final int TRANSACTION_resizeVirtualDisplay = 16;
    static final int TRANSACTION_resumeWifiDisplay = 11;
    static final int TRANSACTION_setBrightnessConfigurationForUser = 22;
    static final int TRANSACTION_setSaturationLevel = 14;
    static final int TRANSACTION_setTemporaryAutoBrightnessAdjustment = 26;
    static final int TRANSACTION_setTemporaryBrightness = 25;
    static final int TRANSACTION_setVirtualDisplaySurface = 17;
    static final int TRANSACTION_startWifiDisplayScan = 4;
    static final int TRANSACTION_stopWifiDisplayScan = 5;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.display.IDisplayManager");
    }
    
    public static IDisplayManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.display.IDisplayManager");
      if ((localIInterface != null) && ((localIInterface instanceof IDisplayManager))) {
        return (IDisplayManager)localIInterface;
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
        String str1 = null;
        Object localObject = null;
        IMediaProjection localIMediaProjection = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 27: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          paramParcel1 = getMinimumBrightnessCurve();
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
        case 26: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          setTemporaryAutoBrightnessAdjustment(paramParcel1.readFloat());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          setTemporaryBrightness(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          paramParcel1 = getDefaultBrightnessConfiguration();
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
        case 23: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          paramParcel1 = getBrightnessConfigurationForUser(paramParcel1.readInt());
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
        case 22: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          if (paramParcel1.readInt() != 0) {
            localObject = (BrightnessConfiguration)BrightnessConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = localIMediaProjection;
          }
          setBrightnessConfigurationForUser((BrightnessConfiguration)localObject, paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          paramParcel1 = getAmbientBrightnessStats();
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
        case 20: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          paramParcel1 = getBrightnessEvents(paramParcel1.readString());
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
        case 19: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          paramParcel1 = getStableDisplaySize();
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
        case 18: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          releaseVirtualDisplay(IVirtualDisplayCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          localObject = IVirtualDisplayCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          setVirtualDisplaySurface((IVirtualDisplayCallback)localObject, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          resizeVirtualDisplay(IVirtualDisplayCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          IVirtualDisplayCallback localIVirtualDisplayCallback = IVirtualDisplayCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localIMediaProjection = IMediaProjection.Stub.asInterface(paramParcel1.readStrongBinder());
          str1 = paramParcel1.readString();
          String str2 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          int i = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject = (Surface)Surface.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramInt1 = createVirtualDisplay(localIVirtualDisplayCallback, localIMediaProjection, str1, str2, paramInt2, i, paramInt1, (Surface)localObject, paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          setSaturationLevel(paramParcel1.readFloat());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          requestColorMode(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          paramParcel1 = getWifiDisplayStatus();
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
        case 11: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          resumeWifiDisplay();
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          pauseWifiDisplay();
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          forgetWifiDisplay(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          renameWifiDisplay(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          disconnectWifiDisplay();
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          connectWifiDisplay(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          stopWifiDisplayScan();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          startWifiDisplayScan();
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          registerCallback(IDisplayManagerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
          paramParcel1 = getDisplayIds();
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.display.IDisplayManager");
        paramParcel1 = getDisplayInfo(paramParcel1.readInt());
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
      }
      paramParcel2.writeString("android.hardware.display.IDisplayManager");
      return true;
    }
    
    private static class Proxy
      implements IDisplayManager
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
      
      public void connectWifiDisplay(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
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
      
      public int createVirtualDisplay(IVirtualDisplayCallback paramIVirtualDisplayCallback, IMediaProjection paramIMediaProjection, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, Surface paramSurface, int paramInt4, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          Object localObject = null;
          if (paramIVirtualDisplayCallback != null) {
            paramIVirtualDisplayCallback = paramIVirtualDisplayCallback.asBinder();
          } else {
            paramIVirtualDisplayCallback = null;
          }
          localParcel1.writeStrongBinder(paramIVirtualDisplayCallback);
          paramIVirtualDisplayCallback = localObject;
          if (paramIMediaProjection != null) {
            paramIVirtualDisplayCallback = paramIMediaProjection.asBinder();
          }
          localParcel1.writeStrongBinder(paramIVirtualDisplayCallback);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          if (paramSurface != null)
          {
            localParcel1.writeInt(1);
            paramSurface.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt4);
          localParcel1.writeString(paramString3);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disconnectWifiDisplay()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
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
      
      public void forgetWifiDisplay(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          localParcel1.writeString(paramString);
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
      
      public ParceledListSlice getAmbientBrightnessStats()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public BrightnessConfiguration getBrightnessConfigurationForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          BrightnessConfiguration localBrightnessConfiguration;
          if (localParcel2.readInt() != 0) {
            localBrightnessConfiguration = (BrightnessConfiguration)BrightnessConfiguration.CREATOR.createFromParcel(localParcel2);
          } else {
            localBrightnessConfiguration = null;
          }
          return localBrightnessConfiguration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getBrightnessEvents(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          localParcel1.writeString(paramString);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public BrightnessConfiguration getDefaultBrightnessConfiguration()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          BrightnessConfiguration localBrightnessConfiguration;
          if (localParcel2.readInt() != 0) {
            localBrightnessConfiguration = (BrightnessConfiguration)BrightnessConfiguration.CREATOR.createFromParcel(localParcel2);
          } else {
            localBrightnessConfiguration = null;
          }
          return localBrightnessConfiguration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getDisplayIds()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int[] arrayOfInt = localParcel2.createIntArray();
          return arrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public DisplayInfo getDisplayInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          DisplayInfo localDisplayInfo;
          if (localParcel2.readInt() != 0) {
            localDisplayInfo = (DisplayInfo)DisplayInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localDisplayInfo = null;
          }
          return localDisplayInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.display.IDisplayManager";
      }
      
      public Curve getMinimumBrightnessCurve()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Curve localCurve;
          if (localParcel2.readInt() != 0) {
            localCurve = (Curve)Curve.CREATOR.createFromParcel(localParcel2);
          } else {
            localCurve = null;
          }
          return localCurve;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Point getStableDisplaySize()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Point localPoint;
          if (localParcel2.readInt() != 0) {
            localPoint = (Point)Point.CREATOR.createFromParcel(localParcel2);
          } else {
            localPoint = null;
          }
          return localPoint;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WifiDisplayStatus getWifiDisplayStatus()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WifiDisplayStatus localWifiDisplayStatus;
          if (localParcel2.readInt() != 0) {
            localWifiDisplayStatus = (WifiDisplayStatus)WifiDisplayStatus.CREATOR.createFromParcel(localParcel2);
          } else {
            localWifiDisplayStatus = null;
          }
          return localWifiDisplayStatus;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void pauseWifiDisplay()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
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
      
      public void registerCallback(IDisplayManagerCallback paramIDisplayManagerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          if (paramIDisplayManagerCallback != null) {
            paramIDisplayManagerCallback = paramIDisplayManagerCallback.asBinder();
          } else {
            paramIDisplayManagerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIDisplayManagerCallback);
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
      
      public void releaseVirtualDisplay(IVirtualDisplayCallback paramIVirtualDisplayCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          if (paramIVirtualDisplayCallback != null) {
            paramIVirtualDisplayCallback = paramIVirtualDisplayCallback.asBinder();
          } else {
            paramIVirtualDisplayCallback = null;
          }
          localParcel1.writeStrongBinder(paramIVirtualDisplayCallback);
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
      
      public void renameWifiDisplay(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void requestColorMode(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resizeVirtualDisplay(IVirtualDisplayCallback paramIVirtualDisplayCallback, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          if (paramIVirtualDisplayCallback != null) {
            paramIVirtualDisplayCallback = paramIVirtualDisplayCallback.asBinder();
          } else {
            paramIVirtualDisplayCallback = null;
          }
          localParcel1.writeStrongBinder(paramIVirtualDisplayCallback);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void resumeWifiDisplay()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
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
      
      public void setBrightnessConfigurationForUser(BrightnessConfiguration paramBrightnessConfiguration, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          if (paramBrightnessConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramBrightnessConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void setSaturationLevel(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          localParcel1.writeFloat(paramFloat);
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
      
      public void setTemporaryAutoBrightnessAdjustment(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          localParcel1.writeFloat(paramFloat);
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
      
      public void setTemporaryBrightness(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
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
      
      public void setVirtualDisplaySurface(IVirtualDisplayCallback paramIVirtualDisplayCallback, Surface paramSurface)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
          if (paramIVirtualDisplayCallback != null) {
            paramIVirtualDisplayCallback = paramIVirtualDisplayCallback.asBinder();
          } else {
            paramIVirtualDisplayCallback = null;
          }
          localParcel1.writeStrongBinder(paramIVirtualDisplayCallback);
          if (paramSurface != null)
          {
            localParcel1.writeInt(1);
            paramSurface.writeToParcel(localParcel1, 0);
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
      
      public void startWifiDisplayScan()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
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
      
      public void stopWifiDisplayScan()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.display.IDisplayManager");
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
    }
  }
}
