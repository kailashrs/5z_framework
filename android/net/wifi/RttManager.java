package android.net.wifi;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.MacAddress;
import android.net.wifi.rtt.RangingRequest;
import android.net.wifi.rtt.RangingRequest.Builder;
import android.net.wifi.rtt.RangingResult;
import android.net.wifi.rtt.RangingResultCallback;
import android.net.wifi.rtt.ResponderConfig;
import android.net.wifi.rtt.WifiRttManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

@SystemApi
@Deprecated
public class RttManager
{
  public static final int BASE = 160256;
  public static final int CMD_OP_ABORTED = 160260;
  public static final int CMD_OP_DISABLE_RESPONDER = 160262;
  public static final int CMD_OP_ENABLE_RESPONDER = 160261;
  public static final int CMD_OP_ENALBE_RESPONDER_FAILED = 160264;
  public static final int CMD_OP_ENALBE_RESPONDER_SUCCEEDED = 160263;
  public static final int CMD_OP_FAILED = 160258;
  public static final int CMD_OP_REG_BINDER = 160265;
  public static final int CMD_OP_START_RANGING = 160256;
  public static final int CMD_OP_STOP_RANGING = 160257;
  public static final int CMD_OP_SUCCEEDED = 160259;
  private static final boolean DBG = false;
  public static final String DESCRIPTION_KEY = "android.net.wifi.RttManager.Description";
  public static final int PREAMBLE_HT = 2;
  public static final int PREAMBLE_LEGACY = 1;
  public static final int PREAMBLE_VHT = 4;
  public static final int REASON_INITIATOR_NOT_ALLOWED_WHEN_RESPONDER_ON = -6;
  public static final int REASON_INVALID_LISTENER = -3;
  public static final int REASON_INVALID_REQUEST = -4;
  public static final int REASON_NOT_AVAILABLE = -2;
  public static final int REASON_PERMISSION_DENIED = -5;
  public static final int REASON_UNSPECIFIED = -1;
  public static final int RTT_BW_10_SUPPORT = 2;
  public static final int RTT_BW_160_SUPPORT = 32;
  public static final int RTT_BW_20_SUPPORT = 4;
  public static final int RTT_BW_40_SUPPORT = 8;
  public static final int RTT_BW_5_SUPPORT = 1;
  public static final int RTT_BW_80_SUPPORT = 16;
  @Deprecated
  public static final int RTT_CHANNEL_WIDTH_10 = 6;
  @Deprecated
  public static final int RTT_CHANNEL_WIDTH_160 = 3;
  @Deprecated
  public static final int RTT_CHANNEL_WIDTH_20 = 0;
  @Deprecated
  public static final int RTT_CHANNEL_WIDTH_40 = 1;
  @Deprecated
  public static final int RTT_CHANNEL_WIDTH_5 = 5;
  @Deprecated
  public static final int RTT_CHANNEL_WIDTH_80 = 2;
  @Deprecated
  public static final int RTT_CHANNEL_WIDTH_80P80 = 4;
  @Deprecated
  public static final int RTT_CHANNEL_WIDTH_UNSPECIFIED = -1;
  public static final int RTT_PEER_NAN = 5;
  public static final int RTT_PEER_P2P_CLIENT = 4;
  public static final int RTT_PEER_P2P_GO = 3;
  public static final int RTT_PEER_TYPE_AP = 1;
  public static final int RTT_PEER_TYPE_STA = 2;
  @Deprecated
  public static final int RTT_PEER_TYPE_UNSPECIFIED = 0;
  public static final int RTT_STATUS_ABORTED = 8;
  public static final int RTT_STATUS_FAILURE = 1;
  public static final int RTT_STATUS_FAIL_AP_ON_DIFF_CHANNEL = 6;
  public static final int RTT_STATUS_FAIL_BUSY_TRY_LATER = 12;
  public static final int RTT_STATUS_FAIL_FTM_PARAM_OVERRIDE = 15;
  public static final int RTT_STATUS_FAIL_INVALID_TS = 9;
  public static final int RTT_STATUS_FAIL_NOT_SCHEDULED_YET = 4;
  public static final int RTT_STATUS_FAIL_NO_CAPABILITY = 7;
  public static final int RTT_STATUS_FAIL_NO_RSP = 2;
  public static final int RTT_STATUS_FAIL_PROTOCOL = 10;
  public static final int RTT_STATUS_FAIL_REJECTED = 3;
  public static final int RTT_STATUS_FAIL_SCHEDULE = 11;
  public static final int RTT_STATUS_FAIL_TM_TIMEOUT = 5;
  public static final int RTT_STATUS_INVALID_REQ = 13;
  public static final int RTT_STATUS_NO_WIFI = 14;
  public static final int RTT_STATUS_SUCCESS = 0;
  @Deprecated
  public static final int RTT_TYPE_11_MC = 4;
  @Deprecated
  public static final int RTT_TYPE_11_V = 2;
  public static final int RTT_TYPE_ONE_SIDED = 1;
  public static final int RTT_TYPE_TWO_SIDED = 2;
  @Deprecated
  public static final int RTT_TYPE_UNSPECIFIED = 0;
  private static final String TAG = "RttManager";
  private final Context mContext;
  private final WifiRttManager mNewService;
  private RttCapabilities mRttCapabilities;
  
  public RttManager(Context paramContext, WifiRttManager paramWifiRttManager)
  {
    mNewService = paramWifiRttManager;
    mContext = paramContext;
    boolean bool = paramContext.getPackageManager().hasSystemFeature("android.hardware.wifi.rtt");
    mRttCapabilities = new RttCapabilities();
    mRttCapabilities.oneSidedRttSupported = bool;
    mRttCapabilities.twoSided11McRttSupported = bool;
    mRttCapabilities.lciSupported = false;
    mRttCapabilities.lcrSupported = false;
    mRttCapabilities.preambleSupported = 6;
    mRttCapabilities.bwSupported = 24;
    mRttCapabilities.responderSupported = false;
    mRttCapabilities.secureRttSupported = false;
  }
  
  public void disableResponder(ResponderCallback paramResponderCallback)
  {
    throw new UnsupportedOperationException("disableResponder is not supported in the adaptation layer");
  }
  
  public void enableResponder(ResponderCallback paramResponderCallback)
  {
    throw new UnsupportedOperationException("enableResponder is not supported in the adaptation layer");
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public Capabilities getCapabilities()
  {
    throw new UnsupportedOperationException("getCapabilities is not supported in the adaptation layer");
  }
  
  public RttCapabilities getRttCapabilities()
  {
    return mRttCapabilities;
  }
  
  public void startRanging(RttParams[] paramArrayOfRttParams, RttListener paramRttListener)
  {
    Log.i("RttManager", "Send RTT request to RTT Service");
    if (!mNewService.isAvailable())
    {
      paramRttListener.onFailure(-2, "");
      return;
    }
    Object localObject1 = new RangingRequest.Builder();
    int i = paramArrayOfRttParams.length;
    Object localObject2;
    Object localObject3;
    for (int j = 0; j < i; j++)
    {
      localObject2 = paramArrayOfRttParams[j];
      if (deviceType != 1)
      {
        paramRttListener.onFailure(-4, "Only AP peers are supported");
        return;
      }
      localObject3 = new ScanResult();
      BSSID = bssid;
      if (requestType == 2) {
        ((ScanResult)localObject3).setFlag(2L);
      }
      channelWidth = channelWidth;
      frequency = frequency;
      centerFreq0 = centerFreq0;
      centerFreq1 = centerFreq1;
      ((RangingRequest.Builder)localObject1).addResponder(ResponderConfig.fromScanResult((ScanResult)localObject3));
    }
    try
    {
      paramArrayOfRttParams = mNewService;
      localObject3 = ((RangingRequest.Builder)localObject1).build();
      localObject1 = mContext.getMainExecutor();
      localObject2 = new android/net/wifi/RttManager$1;
      ((1)localObject2).<init>(this, paramRttListener);
      paramArrayOfRttParams.startRanging((RangingRequest)localObject3, (Executor)localObject1, (RangingResultCallback)localObject2);
    }
    catch (SecurityException localSecurityException)
    {
      paramArrayOfRttParams = new StringBuilder();
      paramArrayOfRttParams.append("startRanging: security exception - ");
      paramArrayOfRttParams.append(localSecurityException);
      Log.e("RttManager", paramArrayOfRttParams.toString());
      paramRttListener.onFailure(-5, localSecurityException.getMessage());
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      paramArrayOfRttParams = new StringBuilder();
      paramArrayOfRttParams.append("startRanging: invalid arguments - ");
      paramArrayOfRttParams.append(localIllegalArgumentException);
      Log.e("RttManager", paramArrayOfRttParams.toString());
      paramRttListener.onFailure(-4, localIllegalArgumentException.getMessage());
    }
  }
  
  public void stopRanging(RttListener paramRttListener)
  {
    Log.e("RttManager", "stopRanging: unsupported operation - nop");
  }
  
  @Deprecated
  public class Capabilities
  {
    public int supportedPeerType;
    public int supportedType;
    
    public Capabilities() {}
  }
  
  @Deprecated
  public static class ParcelableRttParams
    implements Parcelable
  {
    public static final Parcelable.Creator<ParcelableRttParams> CREATOR = new Parcelable.Creator()
    {
      public RttManager.ParcelableRttParams createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = paramAnonymousParcel.readInt();
        RttManager.RttParams[] arrayOfRttParams = new RttManager.RttParams[i];
        for (int j = 0; j < i; j++)
        {
          arrayOfRttParams[j] = new RttManager.RttParams();
          deviceType = paramAnonymousParcel.readInt();
          requestType = paramAnonymousParcel.readInt();
          RttManager.RttParams localRttParams = arrayOfRttParams[j];
          int k = paramAnonymousParcel.readByte();
          boolean bool1 = true;
          boolean bool2;
          if (k != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          secure = bool2;
          bssid = paramAnonymousParcel.readString();
          channelWidth = paramAnonymousParcel.readInt();
          frequency = paramAnonymousParcel.readInt();
          centerFreq0 = paramAnonymousParcel.readInt();
          centerFreq1 = paramAnonymousParcel.readInt();
          numberBurst = paramAnonymousParcel.readInt();
          interval = paramAnonymousParcel.readInt();
          numSamplesPerBurst = paramAnonymousParcel.readInt();
          numRetriesPerMeasurementFrame = paramAnonymousParcel.readInt();
          numRetriesPerFTMR = paramAnonymousParcel.readInt();
          localRttParams = arrayOfRttParams[j];
          if (paramAnonymousParcel.readInt() == 1) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          LCIRequest = bool2;
          localRttParams = arrayOfRttParams[j];
          if (paramAnonymousParcel.readInt() == 1) {
            bool2 = bool1;
          } else {
            bool2 = false;
          }
          LCRRequest = bool2;
          burstTimeout = paramAnonymousParcel.readInt();
          preamble = paramAnonymousParcel.readInt();
          bandwidth = paramAnonymousParcel.readInt();
        }
        return new RttManager.ParcelableRttParams(arrayOfRttParams);
      }
      
      public RttManager.ParcelableRttParams[] newArray(int paramAnonymousInt)
      {
        return new RttManager.ParcelableRttParams[paramAnonymousInt];
      }
    };
    public RttManager.RttParams[] mParams;
    
    @VisibleForTesting
    public ParcelableRttParams(RttManager.RttParams[] paramArrayOfRttParams)
    {
      if (paramArrayOfRttParams == null) {
        paramArrayOfRttParams = new RttManager.RttParams[0];
      }
      mParams = paramArrayOfRttParams;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mParams.length);
      for (RttManager.RttParams localRttParams : mParams)
      {
        paramParcel.writeInt(deviceType);
        paramParcel.writeInt(requestType);
        paramParcel.writeByte(secure);
        paramParcel.writeString(bssid);
        paramParcel.writeInt(channelWidth);
        paramParcel.writeInt(frequency);
        paramParcel.writeInt(centerFreq0);
        paramParcel.writeInt(centerFreq1);
        paramParcel.writeInt(numberBurst);
        paramParcel.writeInt(interval);
        paramParcel.writeInt(numSamplesPerBurst);
        paramParcel.writeInt(numRetriesPerMeasurementFrame);
        paramParcel.writeInt(numRetriesPerFTMR);
        paramParcel.writeInt(LCIRequest);
        paramParcel.writeInt(LCRRequest);
        paramParcel.writeInt(burstTimeout);
        paramParcel.writeInt(preamble);
        paramParcel.writeInt(bandwidth);
      }
    }
  }
  
  @Deprecated
  public static class ParcelableRttResults
    implements Parcelable
  {
    public static final Parcelable.Creator<ParcelableRttResults> CREATOR = new Parcelable.Creator()
    {
      public RttManager.ParcelableRttResults createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = paramAnonymousParcel.readInt();
        if (i == 0) {
          return new RttManager.ParcelableRttResults(null);
        }
        RttManager.RttResult[] arrayOfRttResult = new RttManager.RttResult[i];
        for (int j = 0; j < i; j++)
        {
          arrayOfRttResult[j] = new RttManager.RttResult();
          bssid = paramAnonymousParcel.readString();
          burstNumber = paramAnonymousParcel.readInt();
          measurementFrameNumber = paramAnonymousParcel.readInt();
          successMeasurementFrameNumber = paramAnonymousParcel.readInt();
          frameNumberPerBurstPeer = paramAnonymousParcel.readInt();
          status = paramAnonymousParcel.readInt();
          measurementType = paramAnonymousParcel.readInt();
          retryAfterDuration = paramAnonymousParcel.readInt();
          ts = paramAnonymousParcel.readLong();
          rssi = paramAnonymousParcel.readInt();
          rssiSpread = paramAnonymousParcel.readInt();
          txRate = paramAnonymousParcel.readInt();
          rtt = paramAnonymousParcel.readLong();
          rttStandardDeviation = paramAnonymousParcel.readLong();
          rttSpread = paramAnonymousParcel.readLong();
          distance = paramAnonymousParcel.readInt();
          distanceStandardDeviation = paramAnonymousParcel.readInt();
          distanceSpread = paramAnonymousParcel.readInt();
          burstDuration = paramAnonymousParcel.readInt();
          negotiatedBurstNum = paramAnonymousParcel.readInt();
          LCI = new RttManager.WifiInformationElement();
          LCI.id = paramAnonymousParcel.readByte();
          int k;
          if (LCI.id != -1)
          {
            k = paramAnonymousParcel.readByte();
            LCI.data = new byte[k];
            paramAnonymousParcel.readByteArray(LCI.data);
          }
          LCR = new RttManager.WifiInformationElement();
          LCR.id = paramAnonymousParcel.readByte();
          if (LCR.id != -1)
          {
            k = paramAnonymousParcel.readByte();
            LCR.data = new byte[k];
            paramAnonymousParcel.readByteArray(LCR.data);
          }
          RttManager.RttResult localRttResult = arrayOfRttResult[j];
          boolean bool;
          if (paramAnonymousParcel.readByte() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          secure = bool;
        }
        return new RttManager.ParcelableRttResults(arrayOfRttResult);
      }
      
      public RttManager.ParcelableRttResults[] newArray(int paramAnonymousInt)
      {
        return new RttManager.ParcelableRttResults[paramAnonymousInt];
      }
    };
    public RttManager.RttResult[] mResults;
    
    public ParcelableRttResults(RttManager.RttResult[] paramArrayOfRttResult)
    {
      mResults = paramArrayOfRttResult;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      for (int i = 0; i < mResults.length; i++)
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("[");
        localStringBuilder2.append(i);
        localStringBuilder2.append("]: ");
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("bssid=");
        localStringBuilder2.append(mResults[i].bssid);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", burstNumber=");
        localStringBuilder2.append(mResults[i].burstNumber);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", measurementFrameNumber=");
        localStringBuilder2.append(mResults[i].measurementFrameNumber);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", successMeasurementFrameNumber=");
        localStringBuilder2.append(mResults[i].successMeasurementFrameNumber);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", frameNumberPerBurstPeer=");
        localStringBuilder2.append(mResults[i].frameNumberPerBurstPeer);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", status=");
        localStringBuilder2.append(mResults[i].status);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", requestType=");
        localStringBuilder2.append(mResults[i].requestType);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", measurementType=");
        localStringBuilder2.append(mResults[i].measurementType);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", retryAfterDuration=");
        localStringBuilder2.append(mResults[i].retryAfterDuration);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", ts=");
        localStringBuilder2.append(mResults[i].ts);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rssi=");
        localStringBuilder2.append(mResults[i].rssi);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rssi_spread=");
        localStringBuilder2.append(mResults[i].rssi_spread);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rssiSpread=");
        localStringBuilder2.append(mResults[i].rssiSpread);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", tx_rate=");
        localStringBuilder2.append(mResults[i].tx_rate);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", txRate=");
        localStringBuilder2.append(mResults[i].txRate);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rxRate=");
        localStringBuilder2.append(mResults[i].rxRate);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rtt_ns=");
        localStringBuilder2.append(mResults[i].rtt_ns);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rtt=");
        localStringBuilder2.append(mResults[i].rtt);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rtt_sd_ns=");
        localStringBuilder2.append(mResults[i].rtt_sd_ns);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rttStandardDeviation=");
        localStringBuilder2.append(mResults[i].rttStandardDeviation);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rtt_spread_ns=");
        localStringBuilder2.append(mResults[i].rtt_spread_ns);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", rttSpread=");
        localStringBuilder2.append(mResults[i].rttSpread);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", distance_cm=");
        localStringBuilder2.append(mResults[i].distance_cm);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", distance=");
        localStringBuilder2.append(mResults[i].distance);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", distance_sd_cm=");
        localStringBuilder2.append(mResults[i].distance_sd_cm);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", distanceStandardDeviation=");
        localStringBuilder2.append(mResults[i].distanceStandardDeviation);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", distance_spread_cm=");
        localStringBuilder2.append(mResults[i].distance_spread_cm);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", distanceSpread=");
        localStringBuilder2.append(mResults[i].distanceSpread);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", burstDuration=");
        localStringBuilder2.append(mResults[i].burstDuration);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", negotiatedBurstNum=");
        localStringBuilder2.append(mResults[i].negotiatedBurstNum);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", LCI=");
        localStringBuilder2.append(mResults[i].LCI);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", LCR=");
        localStringBuilder2.append(mResults[i].LCR);
        localStringBuilder1.append(localStringBuilder2.toString());
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(", secure=");
        localStringBuilder2.append(mResults[i].secure);
        localStringBuilder1.append(localStringBuilder2.toString());
      }
      return localStringBuilder1.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      RttManager.RttResult[] arrayOfRttResult = mResults;
      paramInt = 0;
      if (arrayOfRttResult != null)
      {
        paramParcel.writeInt(mResults.length);
        arrayOfRttResult = mResults;
        int i = arrayOfRttResult.length;
        while (paramInt < i)
        {
          RttManager.RttResult localRttResult = arrayOfRttResult[paramInt];
          paramParcel.writeString(bssid);
          paramParcel.writeInt(burstNumber);
          paramParcel.writeInt(measurementFrameNumber);
          paramParcel.writeInt(successMeasurementFrameNumber);
          paramParcel.writeInt(frameNumberPerBurstPeer);
          paramParcel.writeInt(status);
          paramParcel.writeInt(measurementType);
          paramParcel.writeInt(retryAfterDuration);
          paramParcel.writeLong(ts);
          paramParcel.writeInt(rssi);
          paramParcel.writeInt(rssiSpread);
          paramParcel.writeInt(txRate);
          paramParcel.writeLong(rtt);
          paramParcel.writeLong(rttStandardDeviation);
          paramParcel.writeLong(rttSpread);
          paramParcel.writeInt(distance);
          paramParcel.writeInt(distanceStandardDeviation);
          paramParcel.writeInt(distanceSpread);
          paramParcel.writeInt(burstDuration);
          paramParcel.writeInt(negotiatedBurstNum);
          paramParcel.writeByte(LCI.id);
          if (LCI.id != -1)
          {
            paramParcel.writeByte((byte)LCI.data.length);
            paramParcel.writeByteArray(LCI.data);
          }
          paramParcel.writeByte(LCR.id);
          if (LCR.id != -1)
          {
            paramParcel.writeByte((byte)LCR.data.length);
            paramParcel.writeByteArray(LCR.data);
          }
          paramParcel.writeByte(secure);
          paramInt++;
        }
      }
      paramParcel.writeInt(0);
    }
  }
  
  @Deprecated
  public static abstract class ResponderCallback
  {
    public ResponderCallback() {}
    
    public abstract void onResponderEnableFailure(int paramInt);
    
    public abstract void onResponderEnabled(RttManager.ResponderConfig paramResponderConfig);
  }
  
  @Deprecated
  public static class ResponderConfig
    implements Parcelable
  {
    public static final Parcelable.Creator<ResponderConfig> CREATOR = new Parcelable.Creator()
    {
      public RttManager.ResponderConfig createFromParcel(Parcel paramAnonymousParcel)
      {
        RttManager.ResponderConfig localResponderConfig = new RttManager.ResponderConfig();
        macAddress = paramAnonymousParcel.readString();
        frequency = paramAnonymousParcel.readInt();
        centerFreq0 = paramAnonymousParcel.readInt();
        centerFreq1 = paramAnonymousParcel.readInt();
        channelWidth = paramAnonymousParcel.readInt();
        preamble = paramAnonymousParcel.readInt();
        return localResponderConfig;
      }
      
      public RttManager.ResponderConfig[] newArray(int paramAnonymousInt)
      {
        return new RttManager.ResponderConfig[paramAnonymousInt];
      }
    };
    public int centerFreq0;
    public int centerFreq1;
    public int channelWidth;
    public int frequency;
    public String macAddress = "";
    public int preamble;
    
    public ResponderConfig() {}
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("macAddress = ");
      localStringBuilder.append(macAddress);
      localStringBuilder.append(" frequency = ");
      localStringBuilder.append(frequency);
      localStringBuilder.append(" centerFreq0 = ");
      localStringBuilder.append(centerFreq0);
      localStringBuilder.append(" centerFreq1 = ");
      localStringBuilder.append(centerFreq1);
      localStringBuilder.append(" channelWidth = ");
      localStringBuilder.append(channelWidth);
      localStringBuilder.append(" preamble = ");
      localStringBuilder.append(preamble);
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(macAddress);
      paramParcel.writeInt(frequency);
      paramParcel.writeInt(centerFreq0);
      paramParcel.writeInt(centerFreq1);
      paramParcel.writeInt(channelWidth);
      paramParcel.writeInt(preamble);
    }
  }
  
  @Deprecated
  public static class RttCapabilities
    implements Parcelable
  {
    public static final Parcelable.Creator<RttCapabilities> CREATOR = new Parcelable.Creator()
    {
      public RttManager.RttCapabilities createFromParcel(Parcel paramAnonymousParcel)
      {
        RttManager.RttCapabilities localRttCapabilities = new RttManager.RttCapabilities();
        int i = paramAnonymousParcel.readInt();
        boolean bool1 = false;
        if (i == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        oneSidedRttSupported = bool2;
        if (paramAnonymousParcel.readInt() == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        twoSided11McRttSupported = bool2;
        if (paramAnonymousParcel.readInt() == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        lciSupported = bool2;
        if (paramAnonymousParcel.readInt() == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        lcrSupported = bool2;
        preambleSupported = paramAnonymousParcel.readInt();
        bwSupported = paramAnonymousParcel.readInt();
        if (paramAnonymousParcel.readInt() == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        responderSupported = bool2;
        boolean bool2 = bool1;
        if (paramAnonymousParcel.readInt() == 1) {
          bool2 = true;
        }
        secureRttSupported = bool2;
        mcVersion = paramAnonymousParcel.readInt();
        return localRttCapabilities;
      }
      
      public RttManager.RttCapabilities[] newArray(int paramAnonymousInt)
      {
        return new RttManager.RttCapabilities[paramAnonymousInt];
      }
    };
    public int bwSupported;
    public boolean lciSupported;
    public boolean lcrSupported;
    public int mcVersion;
    public boolean oneSidedRttSupported;
    public int preambleSupported;
    public boolean responderSupported;
    public boolean secureRttSupported;
    @Deprecated
    public boolean supportedPeerType;
    @Deprecated
    public boolean supportedType;
    public boolean twoSided11McRttSupported;
    
    public RttCapabilities() {}
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("oneSidedRtt ");
      if (oneSidedRttSupported) {
        localObject = "is Supported. ";
      } else {
        localObject = "is not supported. ";
      }
      localStringBuffer.append((String)localObject);
      localStringBuffer.append("twoSided11McRtt ");
      if (twoSided11McRttSupported) {
        localObject = "is Supported. ";
      } else {
        localObject = "is not supported. ";
      }
      localStringBuffer.append((String)localObject);
      localStringBuffer.append("lci ");
      if (lciSupported) {
        localObject = "is Supported. ";
      } else {
        localObject = "is not supported. ";
      }
      localStringBuffer.append((String)localObject);
      localStringBuffer.append("lcr ");
      if (lcrSupported) {
        localObject = "is Supported. ";
      } else {
        localObject = "is not supported. ";
      }
      localStringBuffer.append((String)localObject);
      if ((preambleSupported & 0x1) != 0) {
        localStringBuffer.append("Legacy ");
      }
      if ((preambleSupported & 0x2) != 0) {
        localStringBuffer.append("HT ");
      }
      if ((preambleSupported & 0x4) != 0) {
        localStringBuffer.append("VHT ");
      }
      localStringBuffer.append("is supported. ");
      if ((bwSupported & 0x1) != 0) {
        localStringBuffer.append("5 MHz ");
      }
      if ((bwSupported & 0x2) != 0) {
        localStringBuffer.append("10 MHz ");
      }
      if ((bwSupported & 0x4) != 0) {
        localStringBuffer.append("20 MHz ");
      }
      if ((bwSupported & 0x8) != 0) {
        localStringBuffer.append("40 MHz ");
      }
      if ((bwSupported & 0x10) != 0) {
        localStringBuffer.append("80 MHz ");
      }
      if ((bwSupported & 0x20) != 0) {
        localStringBuffer.append("160 MHz ");
      }
      localStringBuffer.append("is supported.");
      localStringBuffer.append(" STA responder role is ");
      if (responderSupported) {
        localObject = "supported";
      } else {
        localObject = "not supported";
      }
      localStringBuffer.append((String)localObject);
      localStringBuffer.append(" Secure RTT protocol is ");
      if (secureRttSupported) {
        localObject = "supported";
      } else {
        localObject = "not supported";
      }
      localStringBuffer.append((String)localObject);
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" 11mc version is ");
      ((StringBuilder)localObject).append(mcVersion);
      localStringBuffer.append(((StringBuilder)localObject).toString());
      return localStringBuffer.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(oneSidedRttSupported);
      paramParcel.writeInt(twoSided11McRttSupported);
      paramParcel.writeInt(lciSupported);
      paramParcel.writeInt(lcrSupported);
      paramParcel.writeInt(preambleSupported);
      paramParcel.writeInt(bwSupported);
      paramParcel.writeInt(responderSupported);
      paramParcel.writeInt(secureRttSupported);
      paramParcel.writeInt(mcVersion);
    }
  }
  
  @Deprecated
  public static abstract interface RttListener
  {
    public abstract void onAborted();
    
    public abstract void onFailure(int paramInt, String paramString);
    
    public abstract void onSuccess(RttManager.RttResult[] paramArrayOfRttResult);
  }
  
  @Deprecated
  public static class RttParams
  {
    public boolean LCIRequest;
    public boolean LCRRequest;
    public int bandwidth = 4;
    public String bssid;
    public int burstTimeout = 15;
    public int centerFreq0;
    public int centerFreq1;
    public int channelWidth;
    public int deviceType = 1;
    public int frequency;
    public int interval;
    public int numRetriesPerFTMR = 0;
    public int numRetriesPerMeasurementFrame = 0;
    public int numSamplesPerBurst = 8;
    @Deprecated
    public int num_retries;
    @Deprecated
    public int num_samples;
    public int numberBurst = 0;
    public int preamble = 2;
    public int requestType = 1;
    public boolean secure;
    
    public RttParams() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("deviceType=");
      localStringBuilder2.append(deviceType);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", requestType=");
      localStringBuilder2.append(requestType);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", secure=");
      localStringBuilder2.append(secure);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", bssid=");
      localStringBuilder2.append(bssid);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", frequency=");
      localStringBuilder2.append(frequency);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", channelWidth=");
      localStringBuilder2.append(channelWidth);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", centerFreq0=");
      localStringBuilder2.append(centerFreq0);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", centerFreq1=");
      localStringBuilder2.append(centerFreq1);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", num_samples=");
      localStringBuilder2.append(num_samples);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", num_retries=");
      localStringBuilder2.append(num_retries);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", numberBurst=");
      localStringBuilder2.append(numberBurst);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", interval=");
      localStringBuilder2.append(interval);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", numSamplesPerBurst=");
      localStringBuilder2.append(numSamplesPerBurst);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", numRetriesPerMeasurementFrame=");
      localStringBuilder2.append(numRetriesPerMeasurementFrame);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", numRetriesPerFTMR=");
      localStringBuilder2.append(numRetriesPerFTMR);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", LCIRequest=");
      localStringBuilder2.append(LCIRequest);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", LCRRequest=");
      localStringBuilder2.append(LCRRequest);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", burstTimeout=");
      localStringBuilder2.append(burstTimeout);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", preamble=");
      localStringBuilder2.append(preamble);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", bandwidth=");
      localStringBuilder2.append(bandwidth);
      localStringBuilder1.append(localStringBuilder2.toString());
      return localStringBuilder1.toString();
    }
  }
  
  @Deprecated
  public static class RttResult
  {
    public RttManager.WifiInformationElement LCI;
    public RttManager.WifiInformationElement LCR;
    public String bssid;
    public int burstDuration;
    public int burstNumber;
    public int distance;
    public int distanceSpread;
    public int distanceStandardDeviation;
    @Deprecated
    public int distance_cm;
    @Deprecated
    public int distance_sd_cm;
    @Deprecated
    public int distance_spread_cm;
    public int frameNumberPerBurstPeer;
    public int measurementFrameNumber;
    public int measurementType;
    public int negotiatedBurstNum;
    @Deprecated
    public int requestType;
    public int retryAfterDuration;
    public int rssi;
    public int rssiSpread;
    @Deprecated
    public int rssi_spread;
    public long rtt;
    public long rttSpread;
    public long rttStandardDeviation;
    @Deprecated
    public long rtt_ns;
    @Deprecated
    public long rtt_sd_ns;
    @Deprecated
    public long rtt_spread_ns;
    public int rxRate;
    public boolean secure;
    public int status;
    public int successMeasurementFrameNumber;
    public long ts;
    public int txRate;
    @Deprecated
    public int tx_rate;
    
    public RttResult() {}
  }
  
  @Deprecated
  public static class WifiInformationElement
  {
    public byte[] data;
    public byte id;
    
    public WifiInformationElement() {}
  }
}
