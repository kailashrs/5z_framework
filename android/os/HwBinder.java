package android.os;

import android.annotation.SystemApi;
import java.util.NoSuchElementException;
import libcore.util.NativeAllocationRegistry;

@SystemApi
public abstract class HwBinder
  implements IHwBinder
{
  private static final String TAG = "HwBinder";
  private static final NativeAllocationRegistry sNativeRegistry;
  private long mNativeContext;
  
  static
  {
    long l = native_init();
    sNativeRegistry = new NativeAllocationRegistry(HwBinder.class.getClassLoader(), l, 128L);
  }
  
  @SystemApi
  public HwBinder()
  {
    native_setup();
    sNativeRegistry.registerNativeAllocation(this, mNativeContext);
  }
  
  @SystemApi
  public static final native void configureRpcThreadpool(long paramLong, boolean paramBoolean);
  
  @SystemApi
  public static void enableInstrumentation() {}
  
  @SystemApi
  public static final IHwBinder getService(String paramString1, String paramString2)
    throws RemoteException, NoSuchElementException
  {
    return getService(paramString1, paramString2, false);
  }
  
  @SystemApi
  public static final native IHwBinder getService(String paramString1, String paramString2, boolean paramBoolean)
    throws RemoteException, NoSuchElementException;
  
  @SystemApi
  public static final native void joinRpcThreadpool();
  
  private static final native long native_init();
  
  private static native void native_report_sysprop_change();
  
  private final native void native_setup();
  
  public static void reportSyspropChanged() {}
  
  @SystemApi
  public abstract void onTransact(int paramInt1, HwParcel paramHwParcel1, HwParcel paramHwParcel2, int paramInt2)
    throws RemoteException;
  
  @SystemApi
  public final native void registerService(String paramString)
    throws RemoteException;
  
  public final native void transact(int paramInt1, HwParcel paramHwParcel1, HwParcel paramHwParcel2, int paramInt2)
    throws RemoteException;
}
