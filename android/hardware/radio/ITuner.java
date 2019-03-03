package android.hardware.radio;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

public abstract interface ITuner
  extends IInterface
{
  public abstract void cancel()
    throws RemoteException;
  
  public abstract void cancelAnnouncement()
    throws RemoteException;
  
  public abstract void close()
    throws RemoteException;
  
  public abstract RadioManager.BandConfig getConfiguration()
    throws RemoteException;
  
  public abstract Bitmap getImage(int paramInt)
    throws RemoteException;
  
  public abstract Map getParameters(List<String> paramList)
    throws RemoteException;
  
  public abstract boolean isClosed()
    throws RemoteException;
  
  public abstract boolean isConfigFlagSet(int paramInt)
    throws RemoteException;
  
  public abstract boolean isConfigFlagSupported(int paramInt)
    throws RemoteException;
  
  public abstract boolean isMuted()
    throws RemoteException;
  
  public abstract void scan(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void setConfigFlag(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setConfiguration(RadioManager.BandConfig paramBandConfig)
    throws RemoteException;
  
  public abstract void setMuted(boolean paramBoolean)
    throws RemoteException;
  
  public abstract Map setParameters(Map paramMap)
    throws RemoteException;
  
  public abstract boolean startBackgroundScan()
    throws RemoteException;
  
  public abstract void startProgramListUpdates(ProgramList.Filter paramFilter)
    throws RemoteException;
  
  public abstract void step(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void stopProgramListUpdates()
    throws RemoteException;
  
  public abstract void tune(ProgramSelector paramProgramSelector)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITuner
  {
    private static final String DESCRIPTOR = "android.hardware.radio.ITuner";
    static final int TRANSACTION_cancel = 10;
    static final int TRANSACTION_cancelAnnouncement = 11;
    static final int TRANSACTION_close = 1;
    static final int TRANSACTION_getConfiguration = 4;
    static final int TRANSACTION_getImage = 12;
    static final int TRANSACTION_getParameters = 20;
    static final int TRANSACTION_isClosed = 2;
    static final int TRANSACTION_isConfigFlagSet = 17;
    static final int TRANSACTION_isConfigFlagSupported = 16;
    static final int TRANSACTION_isMuted = 6;
    static final int TRANSACTION_scan = 8;
    static final int TRANSACTION_setConfigFlag = 18;
    static final int TRANSACTION_setConfiguration = 3;
    static final int TRANSACTION_setMuted = 5;
    static final int TRANSACTION_setParameters = 19;
    static final int TRANSACTION_startBackgroundScan = 13;
    static final int TRANSACTION_startProgramListUpdates = 14;
    static final int TRANSACTION_step = 7;
    static final int TRANSACTION_stopProgramListUpdates = 15;
    static final int TRANSACTION_tune = 9;
    
    public Stub()
    {
      attachInterface(this, "android.hardware.radio.ITuner");
    }
    
    public static ITuner asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.hardware.radio.ITuner");
      if ((localIInterface != null) && ((localIInterface instanceof ITuner))) {
        return (ITuner)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 20: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramParcel1 = getParameters(paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          paramParcel2.writeMap(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramParcel1 = setParameters(paramParcel1.readHashMap(getClass().getClassLoader()));
          paramParcel2.writeNoException();
          paramParcel2.writeMap(paramParcel1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setConfigFlag(paramInt1, bool4);
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramInt1 = isConfigFlagSet(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramInt1 = isConfigFlagSupported(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          stopProgramListUpdates();
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ProgramList.Filter)ProgramList.Filter.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          startProgramListUpdates(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramInt1 = startBackgroundScan();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramParcel1 = getImage(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          cancelAnnouncement();
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          cancel();
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ProgramSelector)ProgramSelector.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          tune(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          scan(bool4, bool1);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          bool1 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          step(bool4, bool1);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramInt1 = isMuted();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          setMuted(bool4);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramParcel1 = getConfiguration();
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
        case 3: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RadioManager.BandConfig)RadioManager.BandConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          setConfiguration(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.hardware.radio.ITuner");
          paramInt1 = isClosed();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.hardware.radio.ITuner");
        close();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.hardware.radio.ITuner");
      return true;
    }
    
    private static class Proxy
      implements ITuner
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
      
      public void cancel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
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
      
      public void cancelAnnouncement()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
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
      
      public void close()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
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
      
      public RadioManager.BandConfig getConfiguration()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          RadioManager.BandConfig localBandConfig;
          if (localParcel2.readInt() != 0) {
            localBandConfig = (RadioManager.BandConfig)RadioManager.BandConfig.CREATOR.createFromParcel(localParcel2);
          } else {
            localBandConfig = null;
          }
          return localBandConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bitmap getImage(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bitmap localBitmap;
          if (localParcel2.readInt() != 0) {
            localBitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(localParcel2);
          } else {
            localBitmap = null;
          }
          return localBitmap;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.hardware.radio.ITuner";
      }
      
      public Map getParameters(List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeStringList(paramList);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramList = localParcel2.readHashMap(getClass().getClassLoader());
          return paramList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isClosed()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean isConfigFlagSet(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(17, localParcel1, localParcel2, 0);
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
      
      public boolean isConfigFlagSupported(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(16, localParcel1, localParcel2, 0);
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
      
      public boolean isMuted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(6, localParcel1, localParcel2, 0);
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
      
      public void scan(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void setConfigFlag(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void setConfiguration(RadioManager.BandConfig paramBandConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          if (paramBandConfig != null)
          {
            localParcel1.writeInt(1);
            paramBandConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void setMuted(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeInt(paramBoolean);
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
      
      public Map setParameters(Map paramMap)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeMap(paramMap);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramMap = localParcel2.readHashMap(getClass().getClassLoader());
          return paramMap;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startBackgroundScan()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(13, localParcel1, localParcel2, 0);
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
      
      public void startProgramListUpdates(ProgramList.Filter paramFilter)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          if (paramFilter != null)
          {
            localParcel1.writeInt(1);
            paramFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void step(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void stopProgramListUpdates()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
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
      
      public void tune(ProgramSelector paramProgramSelector)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.hardware.radio.ITuner");
          if (paramProgramSelector != null)
          {
            localParcel1.writeInt(1);
            paramProgramSelector.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
