package android.app.timezone;

import android.content.Context;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public final class RulesManager
{
  public static final String ACTION_RULES_UPDATE_OPERATION = "com.android.intent.action.timezone.RULES_UPDATE_OPERATION";
  private static final boolean DEBUG = false;
  public static final int ERROR_OPERATION_IN_PROGRESS = 1;
  public static final int ERROR_UNKNOWN_FAILURE = 2;
  public static final String EXTRA_OPERATION_STAGED = "staged";
  public static final int SUCCESS = 0;
  private static final String TAG = "timezone.RulesManager";
  private final Context mContext;
  private final IRulesManager mIRulesManager;
  
  public RulesManager(Context paramContext)
  {
    mContext = paramContext;
    mIRulesManager = IRulesManager.Stub.asInterface(ServiceManager.getService("timezone"));
  }
  
  static void logDebug(String paramString) {}
  
  public RulesState getRulesState()
  {
    try
    {
      logDebug("mIRulesManager.getRulesState()");
      RulesState localRulesState = mIRulesManager.getRulesState();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("mIRulesManager.getRulesState() returned ");
      localStringBuilder.append(localRulesState);
      logDebug(localStringBuilder.toString());
      return localRulesState;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int requestInstall(ParcelFileDescriptor paramParcelFileDescriptor, byte[] paramArrayOfByte, Callback paramCallback)
    throws IOException
  {
    paramCallback = new CallbackWrapper(mContext, paramCallback);
    try
    {
      logDebug("mIRulesManager.requestInstall()");
      int i = mIRulesManager.requestInstall(paramParcelFileDescriptor, paramArrayOfByte, paramCallback);
      return i;
    }
    catch (RemoteException paramParcelFileDescriptor)
    {
      throw paramParcelFileDescriptor.rethrowFromSystemServer();
    }
  }
  
  public void requestNothing(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("mIRulesManager.requestNothing() with token=");
      localStringBuilder.append(Arrays.toString(paramArrayOfByte));
      logDebug(localStringBuilder.toString());
      mIRulesManager.requestNothing(paramArrayOfByte, paramBoolean);
      return;
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  public int requestUninstall(byte[] paramArrayOfByte, Callback paramCallback)
  {
    paramCallback = new CallbackWrapper(mContext, paramCallback);
    try
    {
      logDebug("mIRulesManager.requestUninstall()");
      int i = mIRulesManager.requestUninstall(paramArrayOfByte, paramCallback);
      return i;
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  private class CallbackWrapper
    extends ICallback.Stub
  {
    final Callback mCallback;
    final Handler mHandler;
    
    CallbackWrapper(Context paramContext, Callback paramCallback)
    {
      mCallback = paramCallback;
      mHandler = new Handler(paramContext.getMainLooper());
    }
    
    public void onFinished(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("mCallback.onFinished(status), status=");
      localStringBuilder.append(paramInt);
      RulesManager.logDebug(localStringBuilder.toString());
      mHandler.post(new _..Lambda.RulesManager.CallbackWrapper.t7a48uTTxaRuSo3YBKxBIbPQznY(this, paramInt));
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ResultCode {}
}
