package android.hardware.cas.V1_0;

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

public abstract interface ICas
  extends IBase
{
  public static final String kInterfaceName = "android.hardware.cas@1.0::ICas";
  
  public static ICas asInterface(IHwBinder paramIHwBinder)
  {
    if (paramIHwBinder == null) {
      return null;
    }
    Object localObject = paramIHwBinder.queryLocalInterface("android.hardware.cas@1.0::ICas");
    if ((localObject != null) && ((localObject instanceof ICas))) {
      return (ICas)localObject;
    }
    paramIHwBinder = new Proxy(paramIHwBinder);
    try
    {
      localObject = paramIHwBinder.interfaceChain().iterator();
      while (((Iterator)localObject).hasNext())
      {
        boolean bool = ((String)((Iterator)localObject).next()).equals("android.hardware.cas@1.0::ICas");
        if (bool) {
          return paramIHwBinder;
        }
      }
    }
    catch (RemoteException paramIHwBinder) {}
    return null;
  }
  
  public static ICas castFrom(IHwInterface paramIHwInterface)
  {
    if (paramIHwInterface == null) {
      paramIHwInterface = null;
    } else {
      paramIHwInterface = asInterface(paramIHwInterface.asBinder());
    }
    return paramIHwInterface;
  }
  
  public static ICas getService()
    throws RemoteException
  {
    return getService("default");
  }
  
  public static ICas getService(String paramString)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.cas@1.0::ICas", paramString));
  }
  
  public static ICas getService(String paramString, boolean paramBoolean)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.cas@1.0::ICas", paramString, paramBoolean));
  }
  
  public static ICas getService(boolean paramBoolean)
    throws RemoteException
  {
    return getService("default", paramBoolean);
  }
  
  public abstract IHwBinder asBinder();
  
  public abstract int closeSession(ArrayList<Byte> paramArrayList)
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
  
  public abstract void openSession(openSessionCallback paramOpenSessionCallback)
    throws RemoteException;
  
  public abstract void ping()
    throws RemoteException;
  
  public abstract int processEcm(ArrayList<Byte> paramArrayList1, ArrayList<Byte> paramArrayList2)
    throws RemoteException;
  
  public abstract int processEmm(ArrayList<Byte> paramArrayList)
    throws RemoteException;
  
  public abstract int provision(String paramString)
    throws RemoteException;
  
  public abstract int refreshEntitlements(int paramInt, ArrayList<Byte> paramArrayList)
    throws RemoteException;
  
  public abstract int release()
    throws RemoteException;
  
  public abstract int sendEvent(int paramInt1, int paramInt2, ArrayList<Byte> paramArrayList)
    throws RemoteException;
  
  public abstract void setHALInstrumentation()
    throws RemoteException;
  
  public abstract int setPrivateData(ArrayList<Byte> paramArrayList)
    throws RemoteException;
  
  public abstract int setSessionPrivateData(ArrayList<Byte> paramArrayList1, ArrayList<Byte> paramArrayList2)
    throws RemoteException;
  
  public abstract boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    throws RemoteException;
  
  public static final class Proxy
    implements ICas
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
    
    public int closeSession(ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(3, localHwParcel, paramArrayList, 0);
        paramArrayList.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        int i = paramArrayList.readInt32();
        return i;
      }
      finally
      {
        paramArrayList.release();
      }
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
    
    public void openSession(ICas.openSessionCallback paramOpenSessionCallback)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(2, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        paramOpenSessionCallback.onValues(localHwParcel2.readInt32(), localHwParcel2.readInt8Vector());
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
    
    public int processEcm(ArrayList<Byte> paramArrayList1, ArrayList<Byte> paramArrayList2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      localHwParcel.writeInt8Vector(paramArrayList1);
      localHwParcel.writeInt8Vector(paramArrayList2);
      paramArrayList1 = new HwParcel();
      try
      {
        mRemote.transact(5, localHwParcel, paramArrayList1, 0);
        paramArrayList1.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        int i = paramArrayList1.readInt32();
        return i;
      }
      finally
      {
        paramArrayList1.release();
      }
    }
    
    public int processEmm(ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(6, localHwParcel, paramArrayList, 0);
        paramArrayList.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        int i = paramArrayList.readInt32();
        return i;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public int provision(String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(8, localHwParcel, paramString, 0);
        paramString.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        int i = paramString.readInt32();
        return i;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public int refreshEntitlements(int paramInt, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(9, localHwParcel, paramArrayList, 0);
        paramArrayList.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        paramInt = paramArrayList.readInt32();
        return paramInt;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public int release()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(10, localHwParcel1, localHwParcel2, 0);
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
    
    public int sendEvent(int paramInt1, int paramInt2, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeInt32(paramInt2);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(7, localHwParcel, paramArrayList, 0);
        paramArrayList.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        paramInt1 = paramArrayList.readInt32();
        return paramInt1;
      }
      finally
      {
        paramArrayList.release();
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
    
    public int setPrivateData(ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(1, localHwParcel, paramArrayList, 0);
        paramArrayList.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        int i = paramArrayList.readInt32();
        return i;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public int setSessionPrivateData(ArrayList<Byte> paramArrayList1, ArrayList<Byte> paramArrayList2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.cas@1.0::ICas");
      localHwParcel.writeInt8Vector(paramArrayList1);
      localHwParcel.writeInt8Vector(paramArrayList2);
      paramArrayList1 = new HwParcel();
      try
      {
        mRemote.transact(4, localHwParcel, paramArrayList1, 0);
        paramArrayList1.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        int i = paramArrayList1.readInt32();
        return i;
      }
      finally
      {
        paramArrayList1.release();
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
      return "[class or subclass of android.hardware.cas@1.0::ICas]@Proxy";
    }
    
    public boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
      throws RemoteException
    {
      return mRemote.unlinkToDeath(paramDeathRecipient);
    }
  }
  
  public static abstract class Stub
    extends HwBinder
    implements ICas
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
      return new ArrayList(Arrays.asList(new byte[][] { { 14, 101, 107, -95, -70, -63, 20, 97, -95, 112, -106, -17, 117, 43, 105, -46, 75, 0, 13, -126, 14, -11, 101, 47, 1, 80, -96, -7, 115, 29, 84, -62 }, { -67, -38, -74, 24, 77, 122, 52, 109, -90, -96, 125, -64, -126, -116, -15, -102, 105, 111, 76, -86, 54, 17, -59, 31, 46, 20, 86, 90, 20, -76, 15, -39 } }));
    }
    
    public final ArrayList<String> interfaceChain()
    {
      return new ArrayList(Arrays.asList(new String[] { "android.hardware.cas@1.0::ICas", "android.hidl.base@1.0::IBase" }));
    }
    
    public final String interfaceDescriptor()
    {
      return "android.hardware.cas@1.0::ICas";
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
      int i2 = 1;
      int i3 = 1;
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
      int i14 = 1;
      int i15 = 1;
      int i16 = 1;
      switch (paramInt1)
      {
      default: 
        switch (paramInt1)
        {
        default: 
          break;
        case 257250372: 
          paramInt1 = i1;
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
            paramHwParcel1 = getDebugInfo();
            paramHwParcel2.writeStatus(0);
            paramHwParcel1.writeToParcel(paramHwParcel2);
            paramHwParcel2.send();
          }
          break;
        case 256921159: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i2;
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
            paramInt1 = i3;
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
            paramHwParcel1 = new HwBlob(16);
            paramInt2 = localArrayList.size();
            paramHwParcel1.putInt32(8L, paramInt2);
            paramHwParcel1.putBool(12L, false);
            HwBlob localHwBlob = new HwBlob(paramInt2 * 32);
            for (paramInt1 = m; paramInt1 < paramInt2; paramInt1++) {
              localHwBlob.putInt8Array(paramInt1 * 32, (byte[])localArrayList.get(paramInt1));
            }
            paramHwParcel1.putBlob(0L, localHwBlob);
            paramHwParcel2.writeBuffer(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        case 256136003: 
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
            paramHwParcel1 = interfaceDescriptor();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.writeString(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        case 256131655: 
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
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.send();
          }
          break;
        case 256067662: 
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
            paramHwParcel1 = interfaceChain();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.writeStringVector(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        }
        break;
      case 10: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = release();
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 9: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = refreshEntitlements(paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 8: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = provision(paramHwParcel1.readString());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 7: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = sendEvent(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 6: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = processEmm(paramHwParcel1.readInt8Vector());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 5: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = processEcm(paramHwParcel1.readInt8Vector(), paramHwParcel1.readInt8Vector());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 4: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = setSessionPrivateData(paramHwParcel1.readInt8Vector(), paramHwParcel1.readInt8Vector());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 3: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = closeSession(paramHwParcel1.readInt8Vector());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 2: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          openSession(new ICas.openSessionCallback()
          {
            public void onValues(int paramAnonymousInt, ArrayList<Byte> paramAnonymousArrayList)
            {
              paramHwParcel2.writeStatus(0);
              paramHwParcel2.writeInt32(paramAnonymousInt);
              paramHwParcel2.writeInt8Vector(paramAnonymousArrayList);
              paramHwParcel2.send();
            }
          });
        }
        break;
      case 1: 
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
          paramHwParcel1.enforceInterface("android.hardware.cas@1.0::ICas");
          paramInt1 = setPrivateData(paramHwParcel1.readInt8Vector());
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
      if ("android.hardware.cas@1.0::ICas".equals(paramString)) {
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
  public static abstract interface openSessionCallback
  {
    public abstract void onValues(int paramInt, ArrayList<Byte> paramArrayList);
  }
}
