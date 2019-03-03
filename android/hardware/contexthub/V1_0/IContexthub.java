package android.hardware.contexthub.V1_0;

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

public abstract interface IContexthub
  extends IBase
{
  public static final String kInterfaceName = "android.hardware.contexthub@1.0::IContexthub";
  
  public static IContexthub asInterface(IHwBinder paramIHwBinder)
  {
    if (paramIHwBinder == null) {
      return null;
    }
    Object localObject = paramIHwBinder.queryLocalInterface("android.hardware.contexthub@1.0::IContexthub");
    if ((localObject != null) && ((localObject instanceof IContexthub))) {
      return (IContexthub)localObject;
    }
    localObject = new Proxy(paramIHwBinder);
    try
    {
      paramIHwBinder = ((IContexthub)localObject).interfaceChain().iterator();
      while (paramIHwBinder.hasNext())
      {
        boolean bool = ((String)paramIHwBinder.next()).equals("android.hardware.contexthub@1.0::IContexthub");
        if (bool) {
          return localObject;
        }
      }
    }
    catch (RemoteException paramIHwBinder) {}
    return null;
  }
  
  public static IContexthub castFrom(IHwInterface paramIHwInterface)
  {
    if (paramIHwInterface == null) {
      paramIHwInterface = null;
    } else {
      paramIHwInterface = asInterface(paramIHwInterface.asBinder());
    }
    return paramIHwInterface;
  }
  
  public static IContexthub getService()
    throws RemoteException
  {
    return getService("default");
  }
  
  public static IContexthub getService(String paramString)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.contexthub@1.0::IContexthub", paramString));
  }
  
  public static IContexthub getService(String paramString, boolean paramBoolean)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.contexthub@1.0::IContexthub", paramString, paramBoolean));
  }
  
  public static IContexthub getService(boolean paramBoolean)
    throws RemoteException
  {
    return getService("default", paramBoolean);
  }
  
  public abstract IHwBinder asBinder();
  
  public abstract int disableNanoApp(int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public abstract int enableNanoApp(int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public abstract DebugInfo getDebugInfo()
    throws RemoteException;
  
  public abstract ArrayList<byte[]> getHashChain()
    throws RemoteException;
  
  public abstract ArrayList<ContextHub> getHubs()
    throws RemoteException;
  
  public abstract ArrayList<String> interfaceChain()
    throws RemoteException;
  
  public abstract String interfaceDescriptor()
    throws RemoteException;
  
  public abstract boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
    throws RemoteException;
  
  public abstract int loadNanoApp(int paramInt1, NanoAppBinary paramNanoAppBinary, int paramInt2)
    throws RemoteException;
  
  public abstract void notifySyspropsChanged()
    throws RemoteException;
  
  public abstract void ping()
    throws RemoteException;
  
  public abstract int queryApps(int paramInt)
    throws RemoteException;
  
  public abstract int registerCallback(int paramInt, IContexthubCallback paramIContexthubCallback)
    throws RemoteException;
  
  public abstract int sendMessageToHub(int paramInt, ContextHubMsg paramContextHubMsg)
    throws RemoteException;
  
  public abstract void setHALInstrumentation()
    throws RemoteException;
  
  public abstract boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    throws RemoteException;
  
  public abstract int unloadNanoApp(int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public static final class Proxy
    implements IContexthub
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
    
    public int disableNanoApp(int paramInt1, long paramLong, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.contexthub@1.0::IContexthub");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt64(paramLong);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(7, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        paramInt1 = localHwParcel2.readInt32();
        return paramInt1;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public int enableNanoApp(int paramInt1, long paramLong, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.contexthub@1.0::IContexthub");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt64(paramLong);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(6, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        paramInt1 = localHwParcel2.readInt32();
        return paramInt1;
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
    
    public ArrayList<ContextHub> getHubs()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hardware.contexthub@1.0::IContexthub");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        mRemote.transact(1, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = ContextHub.readVectorFromParcel(localHwParcel);
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
    
    public int loadNanoApp(int paramInt1, NanoAppBinary paramNanoAppBinary, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.contexthub@1.0::IContexthub");
      localHwParcel.writeInt32(paramInt1);
      paramNanoAppBinary.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt2);
      paramNanoAppBinary = new HwParcel();
      try
      {
        mRemote.transact(4, localHwParcel, paramNanoAppBinary, 0);
        paramNanoAppBinary.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        paramInt1 = paramNanoAppBinary.readInt32();
        return paramInt1;
      }
      finally
      {
        paramNanoAppBinary.release();
      }
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
    
    public int queryApps(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.contexthub@1.0::IContexthub");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(8, localHwParcel1, localHwParcel2, 0);
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
    
    public int registerCallback(int paramInt, IContexthubCallback paramIContexthubCallback)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.contexthub@1.0::IContexthub");
      localHwParcel.writeInt32(paramInt);
      if (paramIContexthubCallback == null) {
        paramIContexthubCallback = null;
      } else {
        paramIContexthubCallback = paramIContexthubCallback.asBinder();
      }
      localHwParcel.writeStrongBinder(paramIContexthubCallback);
      paramIContexthubCallback = new HwParcel();
      try
      {
        mRemote.transact(2, localHwParcel, paramIContexthubCallback, 0);
        paramIContexthubCallback.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        paramInt = paramIContexthubCallback.readInt32();
        return paramInt;
      }
      finally
      {
        paramIContexthubCallback.release();
      }
    }
    
    public int sendMessageToHub(int paramInt, ContextHubMsg paramContextHubMsg)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.contexthub@1.0::IContexthub");
      localHwParcel.writeInt32(paramInt);
      paramContextHubMsg.writeToParcel(localHwParcel);
      paramContextHubMsg = new HwParcel();
      try
      {
        mRemote.transact(3, localHwParcel, paramContextHubMsg, 0);
        paramContextHubMsg.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        paramInt = paramContextHubMsg.readInt32();
        return paramInt;
      }
      finally
      {
        paramContextHubMsg.release();
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
      return "[class or subclass of android.hardware.contexthub@1.0::IContexthub]@Proxy";
    }
    
    public boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
      throws RemoteException
    {
      return mRemote.unlinkToDeath(paramDeathRecipient);
    }
    
    public int unloadNanoApp(int paramInt1, long paramLong, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.contexthub@1.0::IContexthub");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt64(paramLong);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(5, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        paramInt1 = localHwParcel2.readInt32();
        return paramInt1;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
  }
  
  public static abstract class Stub
    extends HwBinder
    implements IContexthub
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
      return new ArrayList(Arrays.asList(new byte[][] { { -91, -82, 15, -24, 102, 127, 11, 26, -16, -101, 19, -25, 45, 41, 96, 15, 78, -77, -123, 59, 37, 112, 121, -60, 90, -103, -74, -12, -93, 54, 6, 73 }, { -67, -38, -74, 24, 77, 122, 52, 109, -90, -96, 125, -64, -126, -116, -15, -102, 105, 111, 76, -86, 54, 17, -59, 31, 46, 20, 86, 90, 20, -76, 15, -39 } }));
    }
    
    public final ArrayList<String> interfaceChain()
    {
      return new ArrayList(Arrays.asList(new String[] { "android.hardware.contexthub@1.0::IContexthub", "android.hidl.base@1.0::IBase" }));
    }
    
    public final String interfaceDescriptor()
    {
      return "android.hardware.contexthub@1.0::IContexthub";
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
      int i1 = 1;
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
      Object localObject;
      switch (paramInt1)
      {
      default: 
        switch (paramInt1)
        {
        default: 
          break;
        case 257250372: 
          paramInt1 = n;
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
            paramHwParcel1 = getDebugInfo();
            paramHwParcel2.writeStatus(0);
            paramHwParcel1.writeToParcel(paramHwParcel2);
            paramHwParcel2.send();
          }
          break;
        case 256921159: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i1;
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
            paramHwParcel1 = getHashChain();
            paramHwParcel2.writeStatus(0);
            localObject = new HwBlob(16);
            paramInt2 = paramHwParcel1.size();
            ((HwBlob)localObject).putInt32(8L, paramInt2);
            ((HwBlob)localObject).putBool(12L, false);
            HwBlob localHwBlob = new HwBlob(paramInt2 * 32);
            for (paramInt1 = m; paramInt1 < paramInt2; paramInt1++) {
              localHwBlob.putInt8Array(paramInt1 * 32, (byte[])paramHwParcel1.get(paramInt1));
            }
            ((HwBlob)localObject).putBlob(0L, localHwBlob);
            paramHwParcel2.writeBuffer((HwBlob)localObject);
            paramHwParcel2.send();
          }
          break;
        case 256136003: 
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
            paramHwParcel1 = interfaceDescriptor();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.writeString(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        case 256131655: 
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
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.send();
          }
          break;
        case 256067662: 
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
            paramHwParcel1 = interfaceChain();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.writeStringVector(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        }
        break;
      case 8: 
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
          paramHwParcel1.enforceInterface("android.hardware.contexthub@1.0::IContexthub");
          paramInt1 = queryApps(paramHwParcel1.readInt32());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 7: 
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
          paramHwParcel1.enforceInterface("android.hardware.contexthub@1.0::IContexthub");
          paramInt1 = disableNanoApp(paramHwParcel1.readInt32(), paramHwParcel1.readInt64(), paramHwParcel1.readInt32());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 6: 
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
          paramHwParcel1.enforceInterface("android.hardware.contexthub@1.0::IContexthub");
          paramInt1 = enableNanoApp(paramHwParcel1.readInt32(), paramHwParcel1.readInt64(), paramHwParcel1.readInt32());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 5: 
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
          paramHwParcel1.enforceInterface("android.hardware.contexthub@1.0::IContexthub");
          paramInt1 = unloadNanoApp(paramHwParcel1.readInt32(), paramHwParcel1.readInt64(), paramHwParcel1.readInt32());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 4: 
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
          paramHwParcel1.enforceInterface("android.hardware.contexthub@1.0::IContexthub");
          paramInt1 = paramHwParcel1.readInt32();
          localObject = new NanoAppBinary();
          ((NanoAppBinary)localObject).readFromParcel(paramHwParcel1);
          paramInt1 = loadNanoApp(paramInt1, (NanoAppBinary)localObject, paramHwParcel1.readInt32());
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 3: 
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
          paramHwParcel1.enforceInterface("android.hardware.contexthub@1.0::IContexthub");
          paramInt1 = paramHwParcel1.readInt32();
          localObject = new ContextHubMsg();
          ((ContextHubMsg)localObject).readFromParcel(paramHwParcel1);
          paramInt1 = sendMessageToHub(paramInt1, (ContextHubMsg)localObject);
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 2: 
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
          paramHwParcel1.enforceInterface("android.hardware.contexthub@1.0::IContexthub");
          paramInt1 = registerCallback(paramHwParcel1.readInt32(), IContexthubCallback.asInterface(paramHwParcel1.readStrongBinder()));
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.writeInt32(paramInt1);
          paramHwParcel2.send();
        }
        break;
      case 1: 
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
          paramHwParcel1.enforceInterface("android.hardware.contexthub@1.0::IContexthub");
          paramHwParcel1 = getHubs();
          paramHwParcel2.writeStatus(0);
          ContextHub.writeVectorToParcel(paramHwParcel2, paramHwParcel1);
          paramHwParcel2.send();
        }
        break;
      }
    }
    
    public final void ping() {}
    
    public IHwInterface queryLocalInterface(String paramString)
    {
      if ("android.hardware.contexthub@1.0::IContexthub".equals(paramString)) {
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
