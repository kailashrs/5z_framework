package android.os;

import libcore.util.NativeAllocationRegistry;

public class HwRemoteBinder
  implements IHwBinder
{
  private static final String TAG = "HwRemoteBinder";
  private static final NativeAllocationRegistry sNativeRegistry;
  private long mNativeContext;
  
  static
  {
    long l = native_init();
    sNativeRegistry = new NativeAllocationRegistry(HwRemoteBinder.class.getClassLoader(), l, 128L);
  }
  
  public HwRemoteBinder()
  {
    native_setup_empty();
    sNativeRegistry.registerNativeAllocation(this, mNativeContext);
  }
  
  private static final native long native_init();
  
  private final native void native_setup_empty();
  
  private static final void sendDeathNotice(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
  {
    paramDeathRecipient.serviceDied(paramLong);
  }
  
  public final native boolean equals(Object paramObject);
  
  public final native int hashCode();
  
  public native boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong);
  
  public IHwInterface queryLocalInterface(String paramString)
  {
    return null;
  }
  
  public final native void transact(int paramInt1, HwParcel paramHwParcel1, HwParcel paramHwParcel2, int paramInt2)
    throws RemoteException;
  
  public native boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient);
}
