package android.service.autofill;

import android.app.Service;
import android.content.Intent;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.autofill.AutofillManager;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.function.Consumer;

public abstract class AutofillService
  extends Service
{
  public static final String SERVICE_INTERFACE = "android.service.autofill.AutofillService";
  public static final String SERVICE_META_DATA = "android.autofill";
  private static final String TAG = "AutofillService";
  private Handler mHandler;
  private final IAutoFillService mInterface = new IAutoFillService.Stub()
  {
    public void onConnectedStateChanged(boolean paramAnonymousBoolean)
    {
      Handler localHandler = mHandler;
      Object localObject;
      if (paramAnonymousBoolean) {
        localObject = _..Lambda.amIBeR2CTPTUHkT8htLcarZmUYc.INSTANCE;
      } else {
        localObject = _..Lambda.eWz26esczusoIA84WEwFlxQuDGQ.INSTANCE;
      }
      localHandler.sendMessage(PooledLambda.obtainMessage((Consumer)localObject, AutofillService.this));
    }
    
    public void onFillRequest(FillRequest paramAnonymousFillRequest, IFillCallback paramAnonymousIFillCallback)
    {
      ICancellationSignal localICancellationSignal = CancellationSignal.createTransport();
      try
      {
        paramAnonymousIFillCallback.onCancellable(localICancellationSignal);
      }
      catch (RemoteException localRemoteException)
      {
        localRemoteException.rethrowFromSystemServer();
      }
      mHandler.sendMessage(PooledLambda.obtainMessage(_..Lambda.I0gCKFrBTO70VZfSZTq2fj_wyG8.INSTANCE, AutofillService.this, paramAnonymousFillRequest, CancellationSignal.fromTransport(localICancellationSignal), new FillCallback(paramAnonymousIFillCallback, paramAnonymousFillRequest.getId())));
    }
    
    public void onSaveRequest(SaveRequest paramAnonymousSaveRequest, ISaveCallback paramAnonymousISaveCallback)
    {
      mHandler.sendMessage(PooledLambda.obtainMessage(_..Lambda.KrOZIsyY_3lh3prHWFldsWopHBw.INSTANCE, AutofillService.this, paramAnonymousSaveRequest, new SaveCallback(paramAnonymousISaveCallback)));
    }
  };
  
  public AutofillService() {}
  
  public final FillEventHistory getFillEventHistory()
  {
    AutofillManager localAutofillManager = (AutofillManager)getSystemService(AutofillManager.class);
    if (localAutofillManager == null) {
      return null;
    }
    return localAutofillManager.getFillEventHistory();
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    if ("android.service.autofill.AutofillService".equals(paramIntent.getAction())) {
      return mInterface.asBinder();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Tried to bind to wrong intent: ");
    localStringBuilder.append(paramIntent);
    Log.w("AutofillService", localStringBuilder.toString());
    return null;
  }
  
  public void onConnected() {}
  
  public void onCreate()
  {
    super.onCreate();
    mHandler = new Handler(Looper.getMainLooper(), null, true);
  }
  
  public void onDisconnected() {}
  
  public abstract void onFillRequest(FillRequest paramFillRequest, CancellationSignal paramCancellationSignal, FillCallback paramFillCallback);
  
  public abstract void onSaveRequest(SaveRequest paramSaveRequest, SaveCallback paramSaveCallback);
}
