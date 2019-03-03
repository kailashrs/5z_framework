package android.hardware.radio.V1_0;

import android.hidl.base.V1_0.DebugInfo;
import android.hidl.base.V1_0.IBase;
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

public abstract interface ISapCallback
  extends IBase
{
  public static final String kInterfaceName = "android.hardware.radio@1.0::ISapCallback";
  
  public static ISapCallback asInterface(IHwBinder paramIHwBinder)
  {
    if (paramIHwBinder == null) {
      return null;
    }
    Object localObject = paramIHwBinder.queryLocalInterface("android.hardware.radio@1.0::ISapCallback");
    if ((localObject != null) && ((localObject instanceof ISapCallback))) {
      return (ISapCallback)localObject;
    }
    paramIHwBinder = new Proxy(paramIHwBinder);
    try
    {
      localObject = paramIHwBinder.interfaceChain().iterator();
      while (((Iterator)localObject).hasNext())
      {
        boolean bool = ((String)((Iterator)localObject).next()).equals("android.hardware.radio@1.0::ISapCallback");
        if (bool) {
          return paramIHwBinder;
        }
      }
    }
    catch (RemoteException paramIHwBinder) {}
    return null;
  }
  
  public static ISapCallback castFrom(IHwInterface paramIHwInterface)
  {
    if (paramIHwInterface == null) {
      paramIHwInterface = null;
    } else {
      paramIHwInterface = asInterface(paramIHwInterface.asBinder());
    }
    return paramIHwInterface;
  }
  
  public static ISapCallback getService()
    throws RemoteException
  {
    return getService("default");
  }
  
  public static ISapCallback getService(String paramString)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.radio@1.0::ISapCallback", paramString));
  }
  
  public static ISapCallback getService(String paramString, boolean paramBoolean)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.radio@1.0::ISapCallback", paramString, paramBoolean));
  }
  
  public static ISapCallback getService(boolean paramBoolean)
    throws RemoteException
  {
    return getService("default", paramBoolean);
  }
  
  public abstract void apduResponse(int paramInt1, int paramInt2, ArrayList<Byte> paramArrayList)
    throws RemoteException;
  
  public abstract IHwBinder asBinder();
  
  public abstract void connectResponse(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void disconnectIndication(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void disconnectResponse(int paramInt)
    throws RemoteException;
  
  public abstract void errorResponse(int paramInt)
    throws RemoteException;
  
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
  
  public abstract void ping()
    throws RemoteException;
  
  public abstract void powerResponse(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void resetSimResponse(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setHALInstrumentation()
    throws RemoteException;
  
  public abstract void statusIndication(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void transferAtrResponse(int paramInt1, int paramInt2, ArrayList<Byte> paramArrayList)
    throws RemoteException;
  
  public abstract void transferCardReaderStatusResponse(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void transferProtocolResponse(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    throws RemoteException;
  
  public static final class Proxy
    implements ISapCallback
  {
    private IHwBinder mRemote;
    
    public Proxy(IHwBinder paramIHwBinder)
    {
      mRemote = ((IHwBinder)Objects.requireNonNull(paramIHwBinder));
    }
    
    public void apduResponse(int paramInt1, int paramInt2, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeInt32(paramInt2);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(4, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public IHwBinder asBinder()
    {
      return mRemote;
    }
    
    public void connectResponse(int paramInt1, int paramInt2, int paramInt3)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      localHwParcel1.writeInt32(paramInt3);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(1, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void disconnectIndication(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(3, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void disconnectResponse(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(2, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public final boolean equals(Object paramObject)
    {
      return HidlSupport.interfacesEqual(this, paramObject);
    }
    
    public void errorResponse(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(10, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
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
        Object localObject2 = mRemote;
        int i = 0;
        ((IHwBinder)localObject2).transact(256398152, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject2 = new java/util/ArrayList;
        ((ArrayList)localObject2).<init>();
        localObject1 = localHwParcel.readBuffer(16L);
        int j = ((HwBlob)localObject1).getInt32(8L);
        localObject1 = localHwParcel.readEmbeddedBuffer(j * 32, ((HwBlob)localObject1).handle(), 0L, true);
        ((ArrayList)localObject2).clear();
        while (i < j)
        {
          byte[] arrayOfByte = new byte[32];
          ((HwBlob)localObject1).copyToInt8Array(i * 32, arrayOfByte, 32);
          ((ArrayList)localObject2).add(arrayOfByte);
          i++;
        }
        return localObject2;
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
    
    public void powerResponse(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(6, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void resetSimResponse(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(7, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
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
    
    public void statusIndication(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(8, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
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
      return "[class or subclass of android.hardware.radio@1.0::ISapCallback]@Proxy";
    }
    
    public void transferAtrResponse(int paramInt1, int paramInt2, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeInt32(paramInt2);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(5, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void transferCardReaderStatusResponse(int paramInt1, int paramInt2, int paramInt3)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      localHwParcel1.writeInt32(paramInt3);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(9, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void transferProtocolResponse(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::ISapCallback");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(11, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
      throws RemoteException
    {
      return mRemote.unlinkToDeath(paramDeathRecipient);
    }
  }
  
  public static abstract class Stub
    extends HwBinder
    implements ISapCallback
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
      return new ArrayList(Arrays.asList(new byte[][] { { -47, -125, -28, 6, -17, 8, -105, -33, 33, 23, -87, -35, -29, -124, -24, -26, -22, 79, -90, -85, 28, 127, 111, 40, -26, 91, -121, 1, 18, 24, -55, -22 }, { -67, -38, -74, 24, 77, 122, 52, 109, -90, -96, 125, -64, -126, -116, -15, -102, 105, 111, 76, -86, 54, 17, -59, 31, 46, 20, 86, 90, 20, -76, 15, -39 } }));
    }
    
    public final ArrayList<String> interfaceChain()
    {
      return new ArrayList(Arrays.asList(new String[] { "android.hardware.radio@1.0::ISapCallback", "android.hidl.base@1.0::IBase" }));
    }
    
    public final String interfaceDescriptor()
    {
      return "android.hardware.radio@1.0::ISapCallback";
    }
    
    public final boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
    {
      return true;
    }
    
    public final void notifySyspropsChanged() {}
    
    public void onTransact(int paramInt1, HwParcel paramHwParcel1, HwParcel paramHwParcel2, int paramInt2)
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
      int i4 = 0;
      int i5 = 0;
      int i6 = 0;
      int i7 = 0;
      int i8 = 0;
      int i9 = 0;
      int i10 = 0;
      int i11 = 0;
      int i12 = 1;
      int i13 = 1;
      int i14 = 1;
      int i15 = 1;
      int i16 = 1;
      int i17 = 1;
      switch (paramInt1)
      {
      default: 
        switch (paramInt1)
        {
        default: 
          break;
        case 257250372: 
          paramInt1 = i11;
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
            paramInt1 = i17;
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
            ArrayList localArrayList = getHashChain();
            paramHwParcel2.writeStatus(0);
            HwBlob localHwBlob = new HwBlob(16);
            paramInt2 = localArrayList.size();
            localHwBlob.putInt32(8L, paramInt2);
            localHwBlob.putBool(12L, false);
            paramHwParcel1 = new HwBlob(paramInt2 * 32);
            for (paramInt1 = m; paramInt1 < paramInt2; paramInt1++) {
              paramHwParcel1.putInt8Array(paramInt1 * 32, (byte[])localArrayList.get(paramInt1));
            }
            localHwBlob.putBlob(0L, paramHwParcel1);
            paramHwParcel2.writeBuffer(localHwBlob);
            paramHwParcel2.send();
          }
          break;
        case 256136003: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i14;
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
            paramInt1 = i15;
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
            paramInt1 = i16;
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
      case 11: 
        paramInt1 = n;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          transferProtocolResponse(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 10: 
        paramInt1 = i1;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          errorResponse(paramHwParcel1.readInt32());
        }
        break;
      case 9: 
        paramInt1 = i2;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          transferCardReaderStatusResponse(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 8: 
        paramInt1 = i3;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          statusIndication(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 7: 
        paramInt1 = i4;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          resetSimResponse(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 6: 
        paramInt1 = i5;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          powerResponse(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 5: 
        paramInt1 = i6;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          transferAtrResponse(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
        }
        break;
      case 4: 
        paramInt1 = i7;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          apduResponse(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
        }
        break;
      case 3: 
        paramInt1 = i8;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          disconnectIndication(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 2: 
        paramInt1 = i9;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          disconnectResponse(paramHwParcel1.readInt32());
        }
        break;
      case 1: 
        paramInt1 = i10;
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::ISapCallback");
          connectResponse(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      }
    }
    
    public final void ping() {}
    
    public IHwInterface queryLocalInterface(String paramString)
    {
      if ("android.hardware.radio@1.0::ISapCallback".equals(paramString)) {
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
}
