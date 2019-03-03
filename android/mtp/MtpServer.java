package android.mtp;

import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;

public class MtpServer
  implements Runnable
{
  private final MtpDatabase mDatabase;
  private long mNativeContext;
  private final Runnable mOnTerminate;
  
  static
  {
    System.loadLibrary("media_jni");
  }
  
  public MtpServer(MtpDatabase paramMtpDatabase, FileDescriptor paramFileDescriptor, boolean paramBoolean, Runnable paramRunnable, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    mDatabase = ((MtpDatabase)Preconditions.checkNotNull(paramMtpDatabase));
    mOnTerminate = ((Runnable)Preconditions.checkNotNull(paramRunnable));
    native_setup(paramMtpDatabase, paramFileDescriptor, paramBoolean, paramString1, paramString2, paramString3, paramString4);
    paramMtpDatabase.setServer(this);
  }
  
  public static void configure(boolean paramBoolean)
  {
    native_configure(paramBoolean);
  }
  
  private final native void native_add_storage(MtpStorage paramMtpStorage);
  
  private final native void native_cleanup();
  
  public static final native void native_configure(boolean paramBoolean);
  
  private final native void native_remove_storage(int paramInt);
  
  private final native void native_run();
  
  private final native void native_send_device_property_changed(int paramInt);
  
  private final native void native_send_object_added(int paramInt);
  
  private final native void native_send_object_info_changed(int paramInt);
  
  private final native void native_send_object_removed(int paramInt);
  
  private final native void native_setup(MtpDatabase paramMtpDatabase, FileDescriptor paramFileDescriptor, boolean paramBoolean, String paramString1, String paramString2, String paramString3, String paramString4);
  
  public void addStorage(MtpStorage paramMtpStorage)
  {
    native_add_storage(paramMtpStorage);
  }
  
  public void removeStorage(MtpStorage paramMtpStorage)
  {
    native_remove_storage(paramMtpStorage.getStorageId());
  }
  
  public void run()
  {
    native_run();
    native_cleanup();
    mDatabase.close();
    mOnTerminate.run();
  }
  
  public void sendDevicePropertyChanged(int paramInt)
  {
    native_send_device_property_changed(paramInt);
  }
  
  public void sendObjectAdded(int paramInt)
  {
    native_send_object_added(paramInt);
  }
  
  public void sendObjectInfoChanged(int paramInt)
  {
    native_send_object_info_changed(paramInt);
  }
  
  public void sendObjectRemoved(int paramInt)
  {
    native_send_object_removed(paramInt);
  }
  
  public void start()
  {
    new Thread(this, "MtpServer").start();
  }
}
