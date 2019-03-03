package android.telephony;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.android.internal.telephony.IPhoneStateListener;
import com.android.internal.telephony.IPhoneStateListener.Stub;
import java.lang.ref.WeakReference;
import java.util.List;

public class PhoneStateListener
{
  private static final boolean DBG = false;
  public static final int LISTEN_CALL_FORWARDING_INDICATOR = 8;
  public static final int LISTEN_CALL_STATE = 32;
  public static final int LISTEN_CARRIER_NETWORK_CHANGE = 65536;
  public static final int LISTEN_CELL_INFO = 1024;
  public static final int LISTEN_CELL_LOCATION = 16;
  public static final int LISTEN_DATA_ACTIVATION_STATE = 262144;
  public static final int LISTEN_DATA_ACTIVITY = 128;
  @Deprecated
  public static final int LISTEN_DATA_CONNECTION_REAL_TIME_INFO = 8192;
  public static final int LISTEN_DATA_CONNECTION_STATE = 64;
  public static final int LISTEN_MESSAGE_WAITING_INDICATOR = 4;
  public static final int LISTEN_NONE = 0;
  @Deprecated
  public static final int LISTEN_OEM_HOOK_RAW_EVENT = 32768;
  public static final int LISTEN_OTASP_CHANGED = 512;
  public static final int LISTEN_PHYSICAL_CHANNEL_CONFIGURATION = 1048576;
  public static final int LISTEN_PRECISE_CALL_STATE = 2048;
  public static final int LISTEN_PRECISE_DATA_CONNECTION_STATE = 4096;
  public static final int LISTEN_SERVICE_STATE = 1;
  @Deprecated
  public static final int LISTEN_SIGNAL_STRENGTH = 2;
  public static final int LISTEN_SIGNAL_STRENGTHS = 256;
  public static final int LISTEN_USER_MOBILE_DATA_STATE = 524288;
  public static final int LISTEN_VOICE_ACTIVATION_STATE = 131072;
  public static final int LISTEN_VOLTE_STATE = 16384;
  private static final String LOG_TAG = "PhoneStateListener";
  IPhoneStateListener callback = new IPhoneStateListenerStub(this);
  private final Handler mHandler;
  protected Integer mSubId;
  
  public PhoneStateListener()
  {
    this(null, Looper.myLooper());
  }
  
  public PhoneStateListener(Looper paramLooper)
  {
    this(null, paramLooper);
  }
  
  public PhoneStateListener(Integer paramInteger)
  {
    this(paramInteger, Looper.myLooper());
  }
  
  public PhoneStateListener(Integer paramInteger, Looper paramLooper)
  {
    mSubId = paramInteger;
    mHandler = new Handler(paramLooper)
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        int i = what;
        boolean bool1 = false;
        boolean bool2 = false;
        PhoneStateListener localPhoneStateListener;
        switch (i)
        {
        default: 
          break;
        case 1048576: 
          onPhysicalChannelConfigurationChanged((List)obj);
          break;
        case 524288: 
          onUserMobileDataStateChanged(((Boolean)obj).booleanValue());
          break;
        case 262144: 
          onDataActivationStateChanged(((Integer)obj).intValue());
          break;
        case 131072: 
          onVoiceActivationStateChanged(((Integer)obj).intValue());
          break;
        case 65536: 
          onCarrierNetworkChange(((Boolean)obj).booleanValue());
          break;
        case 32768: 
          onOemHookRawEvent((byte[])obj);
          break;
        case 16384: 
          onVoLteServiceStateChanged((VoLteServiceState)obj);
          break;
        case 8192: 
          onDataConnectionRealTimeInfoChanged((DataConnectionRealTimeInfo)obj);
          break;
        case 4096: 
          onPreciseDataConnectionStateChanged((PreciseDataConnectionState)obj);
          break;
        case 2048: 
          onPreciseCallStateChanged((PreciseCallState)obj);
          break;
        case 1024: 
          onCellInfoChanged((List)obj);
          break;
        case 512: 
          onOtaspChanged(arg1);
          break;
        case 256: 
          onSignalStrengthsChanged((SignalStrength)obj);
          break;
        case 128: 
          onDataActivity(arg1);
          break;
        case 64: 
          onDataConnectionStateChanged(arg1, arg2);
          onDataConnectionStateChanged(arg1);
          break;
        case 32: 
          onCallStateChanged(arg1, (String)obj);
          break;
        case 16: 
          onCellLocationChanged((CellLocation)obj);
          break;
        case 8: 
          localPhoneStateListener = PhoneStateListener.this;
          if (arg1 != 0) {
            bool2 = true;
          }
          localPhoneStateListener.onCallForwardingIndicatorChanged(bool2);
          break;
        case 4: 
          localPhoneStateListener = PhoneStateListener.this;
          bool2 = bool1;
          if (arg1 != 0) {
            bool2 = true;
          }
          localPhoneStateListener.onMessageWaitingIndicatorChanged(bool2);
          break;
        case 2: 
          onSignalStrengthChanged(arg1);
          break;
        case 1: 
          onServiceStateChanged((ServiceState)obj);
        }
      }
    };
  }
  
  private void log(String paramString)
  {
    Rlog.d("PhoneStateListener", paramString);
  }
  
  public void onCallForwardingIndicatorChanged(boolean paramBoolean) {}
  
  public void onCallStateChanged(int paramInt, String paramString) {}
  
  public void onCarrierNetworkChange(boolean paramBoolean) {}
  
  public void onCellInfoChanged(List<CellInfo> paramList) {}
  
  public void onCellLocationChanged(CellLocation paramCellLocation) {}
  
  public void onDataActivationStateChanged(int paramInt) {}
  
  public void onDataActivity(int paramInt) {}
  
  public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo paramDataConnectionRealTimeInfo) {}
  
  public void onDataConnectionStateChanged(int paramInt) {}
  
  public void onDataConnectionStateChanged(int paramInt1, int paramInt2) {}
  
  public void onMessageWaitingIndicatorChanged(boolean paramBoolean) {}
  
  public void onOemHookRawEvent(byte[] paramArrayOfByte) {}
  
  public void onOtaspChanged(int paramInt) {}
  
  public void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> paramList) {}
  
  public void onPreciseCallStateChanged(PreciseCallState paramPreciseCallState) {}
  
  public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState paramPreciseDataConnectionState) {}
  
  public void onServiceStateChanged(ServiceState paramServiceState) {}
  
  @Deprecated
  public void onSignalStrengthChanged(int paramInt) {}
  
  public void onSignalStrengthsChanged(SignalStrength paramSignalStrength) {}
  
  public void onUserMobileDataStateChanged(boolean paramBoolean) {}
  
  public void onVoLteServiceStateChanged(VoLteServiceState paramVoLteServiceState) {}
  
  public void onVoiceActivationStateChanged(int paramInt) {}
  
  private static class IPhoneStateListenerStub
    extends IPhoneStateListener.Stub
  {
    private WeakReference<PhoneStateListener> mPhoneStateListenerWeakRef;
    
    public IPhoneStateListenerStub(PhoneStateListener paramPhoneStateListener)
    {
      mPhoneStateListenerWeakRef = new WeakReference(paramPhoneStateListener);
    }
    
    private void send(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
    {
      PhoneStateListener localPhoneStateListener = (PhoneStateListener)mPhoneStateListenerWeakRef.get();
      if (localPhoneStateListener != null) {
        Message.obtain(mHandler, paramInt1, paramInt2, paramInt3, paramObject).sendToTarget();
      }
    }
    
    public void onCallForwardingIndicatorChanged(boolean paramBoolean)
    {
      send(8, paramBoolean, 0, null);
    }
    
    public void onCallStateChanged(int paramInt, String paramString)
    {
      send(32, paramInt, 0, paramString);
    }
    
    public void onCarrierNetworkChange(boolean paramBoolean)
    {
      send(65536, 0, 0, Boolean.valueOf(paramBoolean));
    }
    
    public void onCellInfoChanged(List<CellInfo> paramList)
    {
      send(1024, 0, 0, paramList);
    }
    
    public void onCellLocationChanged(Bundle paramBundle)
    {
      send(16, 0, 0, CellLocation.newFromBundle(paramBundle));
    }
    
    public void onDataActivationStateChanged(int paramInt)
    {
      send(262144, 0, 0, Integer.valueOf(paramInt));
    }
    
    public void onDataActivity(int paramInt)
    {
      send(128, paramInt, 0, null);
    }
    
    public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo paramDataConnectionRealTimeInfo)
    {
      send(8192, 0, 0, paramDataConnectionRealTimeInfo);
    }
    
    public void onDataConnectionStateChanged(int paramInt1, int paramInt2)
    {
      send(64, paramInt1, paramInt2, null);
    }
    
    public void onMessageWaitingIndicatorChanged(boolean paramBoolean)
    {
      send(4, paramBoolean, 0, null);
    }
    
    public void onOemHookRawEvent(byte[] paramArrayOfByte)
    {
      send(32768, 0, 0, paramArrayOfByte);
    }
    
    public void onOtaspChanged(int paramInt)
    {
      send(512, paramInt, 0, null);
    }
    
    public void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> paramList)
    {
      send(1048576, 0, 0, paramList);
    }
    
    public void onPreciseCallStateChanged(PreciseCallState paramPreciseCallState)
    {
      send(2048, 0, 0, paramPreciseCallState);
    }
    
    public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState paramPreciseDataConnectionState)
    {
      send(4096, 0, 0, paramPreciseDataConnectionState);
    }
    
    public void onServiceStateChanged(ServiceState paramServiceState)
    {
      send(1, 0, 0, paramServiceState);
    }
    
    public void onSignalStrengthChanged(int paramInt)
    {
      send(2, paramInt, 0, null);
    }
    
    public void onSignalStrengthsChanged(SignalStrength paramSignalStrength)
    {
      send(256, 0, 0, paramSignalStrength);
    }
    
    public void onUserMobileDataStateChanged(boolean paramBoolean)
    {
      send(524288, 0, 0, Boolean.valueOf(paramBoolean));
    }
    
    public void onVoLteServiceStateChanged(VoLteServiceState paramVoLteServiceState)
    {
      send(16384, 0, 0, paramVoLteServiceState);
    }
    
    public void onVoiceActivationStateChanged(int paramInt)
    {
      send(131072, 0, 0, Integer.valueOf(paramInt));
    }
  }
}
