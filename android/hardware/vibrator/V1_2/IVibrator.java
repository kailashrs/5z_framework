package android.hardware.vibrator.V1_2;

import android.hardware.vibrator.V1_0.IVibrator.performCallback;
import android.hardware.vibrator.V1_1.IVibrator.perform_1_1Callback;
import android.hidl.base.V1_0.DebugInfo;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwBinder.DeathRecipient;
import android.os.IHwInterface;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public abstract interface IVibrator
  extends android.hardware.vibrator.V1_1.IVibrator
{
  public static final String kInterfaceName = "android.hardware.vibrator@1.2::IVibrator";
  
  public static IVibrator asInterface(IHwBinder paramIHwBinder)
  {
    if (paramIHwBinder == null) {
      return null;
    }
    Object localObject = paramIHwBinder.queryLocalInterface("android.hardware.vibrator@1.2::IVibrator");
    if ((localObject != null) && ((localObject instanceof IVibrator))) {
      return (IVibrator)localObject;
    }
    localObject = new Proxy(paramIHwBinder);
    try
    {
      paramIHwBinder = ((IVibrator)localObject).interfaceChain().iterator();
      while (paramIHwBinder.hasNext())
      {
        boolean bool = ((String)paramIHwBinder.next()).equals("android.hardware.vibrator@1.2::IVibrator");
        if (bool) {
          return localObject;
        }
      }
    }
    catch (RemoteException paramIHwBinder) {}
    return null;
  }
  
  public static IVibrator castFrom(IHwInterface paramIHwInterface)
  {
    if (paramIHwInterface == null) {
      paramIHwInterface = null;
    } else {
      paramIHwInterface = asInterface(paramIHwInterface.asBinder());
    }
    return paramIHwInterface;
  }
  
  public static IVibrator getService()
    throws RemoteException
  {
    return getService("default");
  }
  
  public static IVibrator getService(String paramString)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.vibrator@1.2::IVibrator", paramString));
  }
  
  public static IVibrator getService(String paramString, boolean paramBoolean)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.vibrator@1.2::IVibrator", paramString, paramBoolean));
  }
  
  public static IVibrator getService(boolean paramBoolean)
    throws RemoteException
  {
    return getService("default", paramBoolean);
  }
  
  public abstract IHwBinder asBinder();
  
  public abstract DebugInfo getDebugInfo()
    throws RemoteException;
  
  public abstract ArrayList<byte[]> getHashChain()
    throws RemoteException;
  
  public abstract ArrayList<String> interfaceChain()
    throws RemoteException;
  
  public abstract String interfaceDescriptor()
    throws RemoteException;
  
  public abstract boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
    throws RemoteException;
  
  public abstract void notifySyspropsChanged()
    throws RemoteException;
  
  public abstract void perform_1_2(int paramInt, byte paramByte, perform_1_2Callback paramPerform_1_2Callback)
    throws RemoteException;
  
  public abstract void ping()
    throws RemoteException;
  
  public abstract void setHALInstrumentation()
    throws RemoteException;
  
  public abstract boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    throws RemoteException;
  
  public static final class Proxy
    implements IVibrator
  {
    private IHwBinder mRemote;
    
    public Proxy(IHwBinder paramIHwBinder)
    {
      mRemote = ((IHwBinder)Objects.requireNonNull(paramIHwBinder));
    }
    
    public IHwBinder asBinder()
    {
      return mRemote;
    }
    
    public final boolean equals(Object paramObject)
    {
      return HidlSupport.interfacesEqual(this, paramObject);
    }
    
    public DebugInfo getDebugInfo()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        mRemote.transact(257049926, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = new android/hidl/base/V1_0/DebugInfo;
        ((DebugInfo)localObject1).<init>();
        ((DebugInfo)localObject1).readFromParcel(localHwParcel);
        return localObject1;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public ArrayList<byte[]> getHashChain()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        Object localObject3 = mRemote;
        int i = 0;
        ((IHwBinder)localObject3).transact(256398152, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = new java/util/ArrayList;
        ((ArrayList)localObject1).<init>();
        localObject3 = localHwParcel.readBuffer(16L);
        int j = ((HwBlob)localObject3).getInt32(8L);
        localObject3 = localHwParcel.readEmbeddedBuffer(j * 32, ((HwBlob)localObject3).handle(), 0L, true);
        ((ArrayList)localObject1).clear();
        while (i < j)
        {
          byte[] arrayOfByte = new byte[32];
          ((HwBlob)localObject3).copyToInt8Array(i * 32, arrayOfByte, 32);
          ((ArrayList)localObject1).add(arrayOfByte);
          i++;
        }
        return localObject1;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public final int hashCode()
    {
      return asBinder().hashCode();
    }
    
    public ArrayList<String> interfaceChain()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        mRemote.transact(256067662, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = localHwParcel.readStringVector();
        return localObject1;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public String interfaceDescriptor()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        mRemote.transact(256136003, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = localHwParcel.readString();
        return localObject1;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
      throws RemoteException
    {
      return mRemote.linkToDeath(paramDeathRecipient, paramLong);
    }
    
    public void notifySyspropsChanged()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(257120595, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public int off()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.vibrator@1.0::IVibrator");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(2, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        int i = localHwParcel2.readInt32();
        return i;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public int on(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.vibrator@1.0::IVibrator");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(1, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        paramInt = localHwParcel2.readInt32();
        return paramInt;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void perform(int paramInt, byte paramByte, IVibrator.performCallback paramPerformCallback)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.vibrator@1.0::IVibrator");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeInt8(paramByte);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(5, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        paramPerformCallback.onValues(localHwParcel2.readInt32(), localHwParcel2.readInt32());
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void perform_1_1(int paramInt, byte paramByte, IVibrator.perform_1_1Callback paramPerform_1_1Callback)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.vibrator@1.1::IVibrator");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeInt8(paramByte);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(6, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        paramPerform_1_1Callback.onValues(localHwParcel2.readInt32(), localHwParcel2.readInt32());
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void perform_1_2(int paramInt, byte paramByte, IVibrator.perform_1_2Callback paramPerform_1_2Callback)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.vibrator@1.2::IVibrator");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeInt8(paramByte);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(7, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        paramPerform_1_2Callback.onValues(localHwParcel2.readInt32(), localHwParcel2.readInt32());
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void ping()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(256921159, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public int setAmplitude(byte paramByte)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.vibrator@1.0::IVibrator");
      localHwParcel1.writeInt8(paramByte);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(4, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        int i = localHwParcel2.readInt32();
        return i;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setHALInstrumentation()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(256462420, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public boolean supportsAmplitudeControl()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.vibrator@1.0::IVibrator");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(3, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        boolean bool = localHwParcel2.readBool();
        return bool;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public String toString()
    {
      try
      {
        Object localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append(interfaceDescriptor());
        ((StringBuilder)localObject).append("@Proxy");
        localObject = ((StringBuilder)localObject).toString();
        return localObject;
      }
      catch (RemoteException localRemoteException) {}
      return "[class or subclass of android.hardware.vibrator@1.2::IVibrator]@Proxy";
    }
    
    public boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
      throws RemoteException
    {
      return mRemote.unlinkToDeath(paramDeathRecipient);
    }
  }
  
  public static abstract class Stub
    extends HwBinder
    implements IVibrator
  {
    public Stub() {}
    
    public IHwBinder asBinder()
    {
      return this;
    }
    
    public final DebugInfo getDebugInfo()
    {
      DebugInfo localDebugInfo = new DebugInfo();
      pid = HidlSupport.getPidIfSharable();
      ptr = 0L;
      arch = 0;
      return localDebugInfo;
    }
    
    public final ArrayList<byte[]> getHashChain()
    {
      byte[] arrayOfByte1 = { 27, -4, -97, -39, 83, 110, -48, -97, 4, -68, -81, 34, 42, 51, 43, -55, 25, -15, 86, 93, 77, 8, -67, -36, -51, -21, -31, -65, -54, -113, 1, -75 };
      byte[] arrayOfByte2 = { 6, -22, 100, -52, 53, 101, 119, 127, 59, 37, -98, 64, 15, -6, 113, 0, -48, 127, 56, 39, -83, -109, 87, -80, -59, -45, -58, 81, 56, 78, 85, 83 };
      return new ArrayList(Arrays.asList(new byte[][] { arrayOfByte1, { -7, 90, 30, -123, 97, 47, 45, 13, 97, 110, -84, -46, -21, 99, -59, 45, 16, -33, -88, -119, -15, 101, -33, 87, 105, 124, 48, -31, -12, 123, 71, -123 }, arrayOfByte2, { -67, -38, -74, 24, 77, 122, 52, 109, -90, -96, 125, -64, -126, -116, -15, -102, 105, 111, 76, -86, 54, 17, -59, 31, 46, 20, 86, 90, 20, -76, 15, -39 } }));
    }
    
    public final ArrayList<String> interfaceChain()
    {
      return new ArrayList(Arrays.asList(new String[] { "android.hardware.vibrator@1.2::IVibrator", "android.hardware.vibrator@1.1::IVibrator", "android.hardware.vibrator@1.0::IVibrator", "android.hidl.base@1.0::IBase" }));
    }
    
    public final String interfaceDescriptor()
    {
      return "android.hardware.vibrator@1.2::IVibrator";
    }
    
    public final boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
    {
      return true;
    }
    
    public final void notifySyspropsChanged() {}
    
    public void onTransact(int paramInt1, HwParcel paramHwParcel1, final HwParcel paramHwParcel2, int paramInt2)
      throws RemoteException
    {
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      int n = 0;
      int i1 = 0;
      int i2 = 0;
      int i3 = 0;
      int i4 = 1;
      int i5 = 1;
      int i6 = 1;
      int i7 = 1;
      int i8 = 1;
      int i9 = 1;
      int i10 = 1;
      int i11 = 1;
      int i12 = 1;
      int i13 = 1;
      switch (paramInt1)
      {
      default: 
        switch (paramInt1)
        {
        default: 
          break;
        case 257250372: 
          paramInt1 = i3;
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = 1;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          break;
        case 257120595: 
          paramInt1 = i;
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = 1;
          }
          if (paramInt1 != 1)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            notifySyspropsChanged();
          }
          break;
        case 257049926: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i13;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel1 = getDebugInfo();
            paramHwParcel2.writeStatus(0);
            paramHwParcel1.writeToParcel(paramHwParcel2);
            paramHwParcel2.send();
          }
          break;
        case 256921159: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i4;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            ping();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.send();
          }
          break;
        case 256660548: 
          paramInt1 = j;
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = 1;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          break;
        case 256462420: 
          paramInt1 = k;
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = 1;
          }
          if (paramInt1 != 1)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            setHALInstrumentation();
          }
          break;
        case 256398152: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i5;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel1 = getHashChain();
            paramHwParcel2.writeStatus(0);
            HwBlob localHwBlob1 = new HwBlob(16);
            paramInt2 = paramHwParcel1.size();
            localHwBlob1.putInt32(8L, paramInt2);
            localHwBlob1.putBool(12L, false);
            HwBlob localHwBlob2 = new HwBlob(paramInt2 * 32);
            for (paramInt1 = m; paramInt1 < paramInt2; paramInt1++) {
              localHwBlob2.putInt8Array(paramInt1 * 32, (byte[])paramHwParcel1.get(paramInt1));
            }
            localHwBlob1.putBlob(0L, localHwBlob2);
            paramHwParcel2.writeBuffer(localHwBlob1);
            paramHwParcel2.send();
          }
          break;
        case 256136003: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i6;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel1 = interfaceDescriptor();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.writeString(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        case 256131655: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i7;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.send();
          }
          break;
        case 256067662: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i8;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel1 = interfaceChain();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.writeStringVector(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        }
        break;
      case 7: 
        paramInt1 = n;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 0)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.vibrator@1.2::IVibrator");
          perform_1_2(paramHwParcel1.readInt32(), paramHwParcel1.readInt8(), new IVibrator.perform_1_2Callback()
          {
            public void onValues(int paramAnonymousInt1, int paramAnonymousInt2)
            {
              paramHwParcel2.writeStatus(0);
              paramHwParcel2.writeInt32(paramAnonymousInt1);
              paramHwParcel2.writeInt32(paramAnonymousInt2);
              paramHwParcel2.send();
            }
          });
        }
        break;
      case 6: 
        paramInt1 = i1;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 0)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.vibrator@1.1::IVibrator");
          perform_1_1(paramHwParcel1.readInt32(), paramHwParcel1.readInt8(), new IVibrator.perform_1_1Callback()
          {
            public void onValues(int paramAnonymousInt1, int paramAnonymousInt2)
            {
              paramHwParcel2.writeStatus(0);
              paramHwParcel2.writeInt32(paramAnonymousInt1);
              paramHwParcel2.writeInt32(paramAnonymousInt2);
              paramHwParcel2.send();
            }
          });
        }
        break;
      case 5: 
        paramInt1 = i2;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 0)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.vibrator@1.0::IVibrator");
          perform(paramHwParcel1.readInt32(), paramHwParcel1.readInt8(), new IVibrator.performCallback()
          {
            public void onValues(int paramAnonymousInt1, int paramAnonymousInt2)
            {
              paramHwParcel2.writeStatus(0);
              paramHwParcel2.writeInt32(paramAnonymousInt1);
              paramHwParcel2.writeInt32(paramAnonymousInt2);
              paramHwParcel2.send();
            }
          });
        }
        break;
      case 4: 
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = i9;
        } else {
          paramInt1 = 0;
        }
        if (paramInt1 != 0)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.vibrator@1.0::IVibrator");
          paramInt1 = setAmplitude(paramHwParcel1.readInt8());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 3: 
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = i10;
        } else {
          paramInt1 = 0;
        }
        if (paramInt1 != 0)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.vibrator@1.0::IVibrator");
          boolean bool = supportsAmplitudeControl();
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeBool(bool);
          paramHwParcel2.send();
        }
        break;
      case 2: 
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = i11;
        } else {
          paramInt1 = 0;
        }
        if (paramInt1 != 0)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.vibrator@1.0::IVibrator");
          paramInt1 = off();
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 1: 
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = i12;
        } else {
          paramInt1 = 0;
        }
        if (paramInt1 != 0)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.vibrator@1.0::IVibrator");
          paramInt1 = on(paramHwParcel1.readInt32());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      }
    }
    
    public final void ping() {}
    
    public IHwInterface queryLocalInterface(String paramString)
    {
      if ("android.hardware.vibrator@1.2::IVibrator".equals(paramString)) {
        return this;
      }
      return null;
    }
    
    public void registerAsService(String paramString)
      throws RemoteException
    {
      registerService(paramString);
    }
    
    public final void setHALInstrumentation() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(interfaceDescriptor());
      localStringBuilder.append("@Stub");
      return localStringBuilder.toString();
    }
    
    public final boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    {
      return true;
    }
  }
  
  @FunctionalInterface
  public static abstract interface perform_1_2Callback
  {
    public abstract void onValues(int paramInt1, int paramInt2);
  }
}
