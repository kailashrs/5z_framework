package android.net.wifi;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.WorkSource;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SystemApi
public class WifiScanner
{
  private static final int BASE = 159744;
  public static final int CMD_DEREGISTER_SCAN_LISTENER = 159772;
  public static final int CMD_FULL_SCAN_RESULT = 159764;
  public static final int CMD_GET_SCAN_RESULTS = 159748;
  public static final int CMD_GET_SINGLE_SCAN_RESULTS = 159773;
  public static final int CMD_OP_FAILED = 159762;
  public static final int CMD_OP_SUCCEEDED = 159761;
  public static final int CMD_PNO_NETWORK_FOUND = 159770;
  public static final int CMD_REGISTER_SCAN_LISTENER = 159771;
  public static final int CMD_SCAN_RESULT = 159749;
  public static final int CMD_SINGLE_SCAN_COMPLETED = 159767;
  public static final int CMD_START_BACKGROUND_SCAN = 159746;
  public static final int CMD_START_PNO_SCAN = 159768;
  public static final int CMD_START_SINGLE_SCAN = 159765;
  public static final int CMD_STOP_BACKGROUND_SCAN = 159747;
  public static final int CMD_STOP_PNO_SCAN = 159769;
  public static final int CMD_STOP_SINGLE_SCAN = 159766;
  private static final boolean DBG = false;
  public static final String GET_AVAILABLE_CHANNELS_EXTRA = "Channels";
  private static final int INVALID_KEY = 0;
  public static final int MAX_SCAN_PERIOD_MS = 1024000;
  public static final int MIN_SCAN_PERIOD_MS = 1000;
  public static final String PNO_PARAMS_PNO_SETTINGS_KEY = "PnoSettings";
  public static final String PNO_PARAMS_SCAN_SETTINGS_KEY = "ScanSettings";
  public static final int REASON_DUPLICATE_REQEUST = -5;
  public static final int REASON_INVALID_LISTENER = -2;
  public static final int REASON_INVALID_REQUEST = -3;
  public static final int REASON_NOT_AUTHORIZED = -4;
  public static final int REASON_SUCCEEDED = 0;
  public static final int REASON_UNSPECIFIED = -1;
  @Deprecated
  public static final int REPORT_EVENT_AFTER_BUFFER_FULL = 0;
  public static final int REPORT_EVENT_AFTER_EACH_SCAN = 1;
  public static final int REPORT_EVENT_FULL_SCAN_RESULT = 2;
  public static final int REPORT_EVENT_NO_BATCH = 4;
  public static final String SCAN_PARAMS_SCAN_SETTINGS_KEY = "ScanSettings";
  public static final String SCAN_PARAMS_WORK_SOURCE_KEY = "WorkSource";
  private static final String TAG = "WifiScanner";
  public static final int TYPE_HIGH_ACCURACY = 2;
  public static final int TYPE_LOW_LATENCY = 0;
  public static final int TYPE_LOW_POWER = 1;
  public static final int WIFI_BAND_24_GHZ = 1;
  public static final int WIFI_BAND_5_GHZ = 2;
  public static final int WIFI_BAND_5_GHZ_DFS_ONLY = 4;
  public static final int WIFI_BAND_5_GHZ_WITH_DFS = 6;
  public static final int WIFI_BAND_BOTH = 3;
  public static final int WIFI_BAND_BOTH_WITH_DFS = 7;
  public static final int WIFI_BAND_UNSPECIFIED = 0;
  private AsyncChannel mAsyncChannel;
  private Context mContext;
  private final Handler mInternalHandler;
  private int mListenerKey = 1;
  private final SparseArray mListenerMap = new SparseArray();
  private final Object mListenerMapLock = new Object();
  private IWifiScanner mService;
  
  public WifiScanner(Context paramContext, IWifiScanner paramIWifiScanner, Looper paramLooper)
  {
    mContext = paramContext;
    mService = paramIWifiScanner;
    try
    {
      paramContext = mService.getMessenger();
      if (paramContext != null)
      {
        mAsyncChannel = new AsyncChannel();
        mInternalHandler = new ServiceHandler(paramLooper);
        mAsyncChannel.connectSync(mContext, mInternalHandler, paramContext);
        mAsyncChannel.sendMessage(69633);
        return;
      }
      throw new IllegalStateException("getMessenger() returned null!  This is invalid.");
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  private int addListener(ActionListener paramActionListener)
  {
    synchronized (mListenerMapLock)
    {
      int i;
      if (getListenerKey(paramActionListener) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      int j = putListener(paramActionListener);
      if (i != 0)
      {
        paramActionListener = new android/net/wifi/WifiScanner$OperationResult;
        paramActionListener.<init>(-5, "Outstanding request with same key not stopped yet");
        Message.obtain(mInternalHandler, 159762, 0, j, paramActionListener).sendToTarget();
        return 0;
      }
      return j;
    }
  }
  
  private Object getListener(int paramInt)
  {
    if (paramInt == 0) {
      return null;
    }
    synchronized (mListenerMapLock)
    {
      Object localObject2 = mListenerMap.get(paramInt);
      return localObject2;
    }
  }
  
  private int getListenerKey(Object paramObject)
  {
    if (paramObject == null) {
      return 0;
    }
    synchronized (mListenerMapLock)
    {
      int i = mListenerMap.indexOfValue(paramObject);
      if (i == -1) {
        return 0;
      }
      i = mListenerMap.keyAt(i);
      return i;
    }
  }
  
  private int putListener(Object paramObject)
  {
    if (paramObject == null) {
      return 0;
    }
    synchronized (mListenerMapLock)
    {
      int i;
      do
      {
        i = mListenerKey;
        mListenerKey = (i + 1);
      } while (i == 0);
      mListenerMap.put(i, paramObject);
      return i;
    }
  }
  
  private int removeListener(Object arg1)
  {
    int i = getListenerKey(???);
    if (i == 0)
    {
      Log.e("WifiScanner", "listener cannot be found");
      return i;
    }
    synchronized (mListenerMapLock)
    {
      mListenerMap.remove(i);
      return i;
    }
  }
  
  private Object removeListener(int paramInt)
  {
    if (paramInt == 0) {
      return null;
    }
    synchronized (mListenerMapLock)
    {
      Object localObject2 = mListenerMap.get(paramInt);
      mListenerMap.remove(paramInt);
      return localObject2;
    }
  }
  
  private void startPnoScan(ScanSettings paramScanSettings, PnoSettings paramPnoSettings, int paramInt)
  {
    Bundle localBundle = new Bundle();
    isPnoScan = true;
    localBundle.putParcelable("ScanSettings", paramScanSettings);
    localBundle.putParcelable("PnoSettings", paramPnoSettings);
    mAsyncChannel.sendMessage(159768, 0, paramInt, localBundle);
  }
  
  private void validateChannel()
  {
    if (mAsyncChannel != null) {
      return;
    }
    throw new IllegalStateException("No permission to access and change wifi or a bad initialization");
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public void configureWifiChange(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, BssidInfo[] paramArrayOfBssidInfo)
  {
    throw new UnsupportedOperationException();
  }
  
  @SystemApi
  @Deprecated
  @SuppressLint({"Doclava125"})
  public void configureWifiChange(WifiChangeSettings paramWifiChangeSettings)
  {
    throw new UnsupportedOperationException();
  }
  
  public void deregisterScanListener(ScanListener paramScanListener)
  {
    Preconditions.checkNotNull(paramScanListener, "listener cannot be null");
    int i = removeListener(paramScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    mAsyncChannel.sendMessage(159772, 0, i);
  }
  
  public List<Integer> getAvailableChannels(int paramInt)
  {
    try
    {
      ArrayList localArrayList = mService.getAvailableChannels(paramInt).getIntegerArrayList("Channels");
      return localArrayList;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public boolean getScanResults()
  {
    validateChannel();
    AsyncChannel localAsyncChannel = mAsyncChannel;
    boolean bool = false;
    if (sendMessageSynchronously1597480what == 159761) {
      bool = true;
    }
    return bool;
  }
  
  public List<ScanResult> getSingleScanResults()
  {
    validateChannel();
    Object localObject = mAsyncChannel.sendMessageSynchronously(159773, 0);
    if (what == 159761) {
      return Arrays.asList(((ParcelableScanResults)obj).getResults());
    }
    localObject = (OperationResult)obj;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Error retrieving SingleScan results reason: ");
    localStringBuilder.append(reason);
    localStringBuilder.append(" description: ");
    localStringBuilder.append(description);
    Log.e("WifiScanner", localStringBuilder.toString());
    return new ArrayList();
  }
  
  public void registerScanListener(ScanListener paramScanListener)
  {
    Preconditions.checkNotNull(paramScanListener, "listener cannot be null");
    int i = addListener(paramScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    mAsyncChannel.sendMessage(159771, 0, i);
  }
  
  public void startBackgroundScan(ScanSettings paramScanSettings, ScanListener paramScanListener)
  {
    startBackgroundScan(paramScanSettings, paramScanListener, null);
  }
  
  public void startBackgroundScan(ScanSettings paramScanSettings, ScanListener paramScanListener, WorkSource paramWorkSource)
  {
    Preconditions.checkNotNull(paramScanListener, "listener cannot be null");
    int i = addListener(paramScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    paramScanListener = new Bundle();
    paramScanListener.putParcelable("ScanSettings", paramScanSettings);
    paramScanListener.putParcelable("WorkSource", paramWorkSource);
    mAsyncChannel.sendMessage(159746, 0, i, paramScanListener);
  }
  
  public void startConnectedPnoScan(ScanSettings paramScanSettings, PnoSettings paramPnoSettings, PnoScanListener paramPnoScanListener)
  {
    Preconditions.checkNotNull(paramPnoScanListener, "listener cannot be null");
    Preconditions.checkNotNull(paramPnoSettings, "pnoSettings cannot be null");
    int i = addListener(paramPnoScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    isConnected = true;
    startPnoScan(paramScanSettings, paramPnoSettings, i);
  }
  
  public void startDisconnectedPnoScan(ScanSettings paramScanSettings, PnoSettings paramPnoSettings, PnoScanListener paramPnoScanListener)
  {
    Preconditions.checkNotNull(paramPnoScanListener, "listener cannot be null");
    Preconditions.checkNotNull(paramPnoSettings, "pnoSettings cannot be null");
    int i = addListener(paramPnoScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    isConnected = false;
    startPnoScan(paramScanSettings, paramPnoSettings, i);
  }
  
  public void startScan(ScanSettings paramScanSettings, ScanListener paramScanListener)
  {
    startScan(paramScanSettings, paramScanListener, null);
  }
  
  public void startScan(ScanSettings paramScanSettings, ScanListener paramScanListener, WorkSource paramWorkSource)
  {
    Preconditions.checkNotNull(paramScanListener, "listener cannot be null");
    int i = addListener(paramScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    paramScanListener = new Bundle();
    paramScanListener.putParcelable("ScanSettings", paramScanSettings);
    paramScanListener.putParcelable("WorkSource", paramWorkSource);
    mAsyncChannel.sendMessage(159765, 0, i, paramScanListener);
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public void startTrackingBssids(BssidInfo[] paramArrayOfBssidInfo, int paramInt, BssidListener paramBssidListener)
  {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public void startTrackingWifiChange(WifiChangeListener paramWifiChangeListener)
  {
    throw new UnsupportedOperationException();
  }
  
  public void stopBackgroundScan(ScanListener paramScanListener)
  {
    Preconditions.checkNotNull(paramScanListener, "listener cannot be null");
    int i = removeListener(paramScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    mAsyncChannel.sendMessage(159747, 0, i);
  }
  
  public void stopPnoScan(ScanListener paramScanListener)
  {
    Preconditions.checkNotNull(paramScanListener, "listener cannot be null");
    int i = removeListener(paramScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    mAsyncChannel.sendMessage(159769, 0, i);
  }
  
  public void stopScan(ScanListener paramScanListener)
  {
    Preconditions.checkNotNull(paramScanListener, "listener cannot be null");
    int i = removeListener(paramScanListener);
    if (i == 0) {
      return;
    }
    validateChannel();
    mAsyncChannel.sendMessage(159766, 0, i);
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public void stopTrackingBssids(BssidListener paramBssidListener)
  {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public void stopTrackingWifiChange(WifiChangeListener paramWifiChangeListener)
  {
    throw new UnsupportedOperationException();
  }
  
  @SystemApi
  public static abstract interface ActionListener
  {
    public abstract void onFailure(int paramInt, String paramString);
    
    public abstract void onSuccess();
  }
  
  @Deprecated
  public static class BssidInfo
  {
    public String bssid;
    public int frequencyHint;
    public int high;
    public int low;
    
    public BssidInfo() {}
  }
  
  @Deprecated
  public static abstract interface BssidListener
    extends WifiScanner.ActionListener
  {
    public abstract void onFound(ScanResult[] paramArrayOfScanResult);
    
    public abstract void onLost(ScanResult[] paramArrayOfScanResult);
  }
  
  public static class ChannelSpec
  {
    public int dwellTimeMS;
    public int frequency;
    public boolean passive;
    
    public ChannelSpec(int paramInt)
    {
      frequency = paramInt;
      passive = false;
      dwellTimeMS = 0;
    }
  }
  
  @SystemApi
  @Deprecated
  public static class HotlistSettings
    implements Parcelable
  {
    public static final Parcelable.Creator<HotlistSettings> CREATOR = new Parcelable.Creator()
    {
      public WifiScanner.HotlistSettings createFromParcel(Parcel paramAnonymousParcel)
      {
        return new WifiScanner.HotlistSettings();
      }
      
      public WifiScanner.HotlistSettings[] newArray(int paramAnonymousInt)
      {
        return new WifiScanner.HotlistSettings[paramAnonymousInt];
      }
    };
    public int apLostThreshold;
    public WifiScanner.BssidInfo[] bssidInfos;
    
    public HotlistSettings() {}
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt) {}
  }
  
  public static class OperationResult
    implements Parcelable
  {
    public static final Parcelable.Creator<OperationResult> CREATOR = new Parcelable.Creator()
    {
      public WifiScanner.OperationResult createFromParcel(Parcel paramAnonymousParcel)
      {
        return new WifiScanner.OperationResult(paramAnonymousParcel.readInt(), paramAnonymousParcel.readString());
      }
      
      public WifiScanner.OperationResult[] newArray(int paramAnonymousInt)
      {
        return new WifiScanner.OperationResult[paramAnonymousInt];
      }
    };
    public String description;
    public int reason;
    
    public OperationResult(int paramInt, String paramString)
    {
      reason = paramInt;
      description = paramString;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(reason);
      paramParcel.writeString(description);
    }
  }
  
  public static class ParcelableScanData
    implements Parcelable
  {
    public static final Parcelable.Creator<ParcelableScanData> CREATOR = new Parcelable.Creator()
    {
      public WifiScanner.ParcelableScanData createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = paramAnonymousParcel.readInt();
        WifiScanner.ScanData[] arrayOfScanData = new WifiScanner.ScanData[i];
        for (int j = 0; j < i; j++) {
          arrayOfScanData[j] = ((WifiScanner.ScanData)WifiScanner.ScanData.CREATOR.createFromParcel(paramAnonymousParcel));
        }
        return new WifiScanner.ParcelableScanData(arrayOfScanData);
      }
      
      public WifiScanner.ParcelableScanData[] newArray(int paramAnonymousInt)
      {
        return new WifiScanner.ParcelableScanData[paramAnonymousInt];
      }
    };
    public WifiScanner.ScanData[] mResults;
    
    public ParcelableScanData(WifiScanner.ScanData[] paramArrayOfScanData)
    {
      mResults = paramArrayOfScanData;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public WifiScanner.ScanData[] getResults()
    {
      return mResults;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      WifiScanner.ScanData[] arrayOfScanData = mResults;
      int i = 0;
      if (arrayOfScanData != null)
      {
        paramParcel.writeInt(mResults.length);
        while (i < mResults.length)
        {
          mResults[i].writeToParcel(paramParcel, paramInt);
          i++;
        }
      }
      paramParcel.writeInt(0);
    }
  }
  
  public static class ParcelableScanResults
    implements Parcelable
  {
    public static final Parcelable.Creator<ParcelableScanResults> CREATOR = new Parcelable.Creator()
    {
      public WifiScanner.ParcelableScanResults createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = paramAnonymousParcel.readInt();
        ScanResult[] arrayOfScanResult = new ScanResult[i];
        for (int j = 0; j < i; j++) {
          arrayOfScanResult[j] = ((ScanResult)ScanResult.CREATOR.createFromParcel(paramAnonymousParcel));
        }
        return new WifiScanner.ParcelableScanResults(arrayOfScanResult);
      }
      
      public WifiScanner.ParcelableScanResults[] newArray(int paramAnonymousInt)
      {
        return new WifiScanner.ParcelableScanResults[paramAnonymousInt];
      }
    };
    public ScanResult[] mResults;
    
    public ParcelableScanResults(ScanResult[] paramArrayOfScanResult)
    {
      mResults = paramArrayOfScanResult;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public ScanResult[] getResults()
    {
      return mResults;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      ScanResult[] arrayOfScanResult = mResults;
      int i = 0;
      if (arrayOfScanResult != null)
      {
        paramParcel.writeInt(mResults.length);
        while (i < mResults.length)
        {
          mResults[i].writeToParcel(paramParcel, paramInt);
          i++;
        }
      }
      paramParcel.writeInt(0);
    }
  }
  
  public static abstract interface PnoScanListener
    extends WifiScanner.ScanListener
  {
    public abstract void onPnoNetworkFound(ScanResult[] paramArrayOfScanResult);
  }
  
  public static class PnoSettings
    implements Parcelable
  {
    public static final Parcelable.Creator<PnoSettings> CREATOR = new Parcelable.Creator()
    {
      public WifiScanner.PnoSettings createFromParcel(Parcel paramAnonymousParcel)
      {
        WifiScanner.PnoSettings localPnoSettings = new WifiScanner.PnoSettings();
        int i = paramAnonymousParcel.readInt();
        int j = 0;
        boolean bool = true;
        if (i != 1) {
          bool = false;
        }
        isConnected = bool;
        min5GHzRssi = paramAnonymousParcel.readInt();
        min24GHzRssi = paramAnonymousParcel.readInt();
        initialScoreMax = paramAnonymousParcel.readInt();
        currentConnectionBonus = paramAnonymousParcel.readInt();
        sameNetworkBonus = paramAnonymousParcel.readInt();
        secureBonus = paramAnonymousParcel.readInt();
        band5GHzBonus = paramAnonymousParcel.readInt();
        i = paramAnonymousParcel.readInt();
        networkList = new WifiScanner.PnoSettings.PnoNetwork[i];
        while (j < i)
        {
          WifiScanner.PnoSettings.PnoNetwork localPnoNetwork = new WifiScanner.PnoSettings.PnoNetwork(paramAnonymousParcel.readString());
          flags = paramAnonymousParcel.readByte();
          authBitField = paramAnonymousParcel.readByte();
          networkList[j] = localPnoNetwork;
          j++;
        }
        return localPnoSettings;
      }
      
      public WifiScanner.PnoSettings[] newArray(int paramAnonymousInt)
      {
        return new WifiScanner.PnoSettings[paramAnonymousInt];
      }
    };
    public int band5GHzBonus;
    public int currentConnectionBonus;
    public int initialScoreMax;
    public boolean isConnected;
    public int min24GHzRssi;
    public int min5GHzRssi;
    public PnoNetwork[] networkList;
    public int sameNetworkBonus;
    public int secureBonus;
    
    public PnoSettings() {}
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(isConnected);
      paramParcel.writeInt(min5GHzRssi);
      paramParcel.writeInt(min24GHzRssi);
      paramParcel.writeInt(initialScoreMax);
      paramParcel.writeInt(currentConnectionBonus);
      paramParcel.writeInt(sameNetworkBonus);
      paramParcel.writeInt(secureBonus);
      paramParcel.writeInt(band5GHzBonus);
      PnoNetwork[] arrayOfPnoNetwork = networkList;
      paramInt = 0;
      if (arrayOfPnoNetwork != null)
      {
        paramParcel.writeInt(networkList.length);
        while (paramInt < networkList.length)
        {
          paramParcel.writeString(networkList[paramInt].ssid);
          paramParcel.writeByte(networkList[paramInt].flags);
          paramParcel.writeByte(networkList[paramInt].authBitField);
          paramInt++;
        }
      }
      paramParcel.writeInt(0);
    }
    
    public static class PnoNetwork
    {
      public static final byte AUTH_CODE_EAPOL = 4;
      public static final byte AUTH_CODE_OPEN = 1;
      public static final byte AUTH_CODE_PSK = 2;
      public static final byte FLAG_A_BAND = 2;
      public static final byte FLAG_DIRECTED_SCAN = 1;
      public static final byte FLAG_G_BAND = 4;
      public static final byte FLAG_SAME_NETWORK = 16;
      public static final byte FLAG_STRICT_MATCH = 8;
      public byte authBitField;
      public byte flags;
      public String ssid;
      
      public PnoNetwork(String paramString)
      {
        ssid = paramString;
        flags = ((byte)0);
        authBitField = ((byte)0);
      }
    }
  }
  
  public static class ScanData
    implements Parcelable
  {
    public static final Parcelable.Creator<ScanData> CREATOR = new Parcelable.Creator()
    {
      public WifiScanner.ScanData createFromParcel(Parcel paramAnonymousParcel)
      {
        int i = paramAnonymousParcel.readInt();
        int j = paramAnonymousParcel.readInt();
        int k = paramAnonymousParcel.readInt();
        int m = paramAnonymousParcel.readInt();
        int n = 0;
        boolean bool;
        if (m != 0) {
          bool = true;
        } else {
          bool = false;
        }
        m = paramAnonymousParcel.readInt();
        ScanResult[] arrayOfScanResult = new ScanResult[m];
        while (n < m)
        {
          arrayOfScanResult[n] = ((ScanResult)ScanResult.CREATOR.createFromParcel(paramAnonymousParcel));
          n++;
        }
        return new WifiScanner.ScanData(i, j, k, bool, arrayOfScanResult);
      }
      
      public WifiScanner.ScanData[] newArray(int paramAnonymousInt)
      {
        return new WifiScanner.ScanData[paramAnonymousInt];
      }
    };
    private boolean mAllChannelsScanned;
    private int mBucketsScanned;
    private int mFlags;
    private int mId;
    private ScanResult[] mResults;
    
    ScanData() {}
    
    public ScanData(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, ScanResult[] paramArrayOfScanResult)
    {
      mId = paramInt1;
      mFlags = paramInt2;
      mBucketsScanned = paramInt3;
      mAllChannelsScanned = paramBoolean;
      mResults = paramArrayOfScanResult;
    }
    
    public ScanData(int paramInt1, int paramInt2, ScanResult[] paramArrayOfScanResult)
    {
      mId = paramInt1;
      mFlags = paramInt2;
      mResults = paramArrayOfScanResult;
    }
    
    public ScanData(ScanData paramScanData)
    {
      mId = mId;
      mFlags = mFlags;
      mBucketsScanned = mBucketsScanned;
      mAllChannelsScanned = mAllChannelsScanned;
      mResults = new ScanResult[mResults.length];
      for (int i = 0; i < mResults.length; i++)
      {
        ScanResult localScanResult = new ScanResult(mResults[i]);
        mResults[i] = localScanResult;
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getBucketsScanned()
    {
      return mBucketsScanned;
    }
    
    public int getFlags()
    {
      return mFlags;
    }
    
    public int getId()
    {
      return mId;
    }
    
    public ScanResult[] getResults()
    {
      return mResults;
    }
    
    public boolean isAllChannelsScanned()
    {
      return mAllChannelsScanned;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      ScanResult[] arrayOfScanResult = mResults;
      int i = 0;
      if (arrayOfScanResult != null)
      {
        paramParcel.writeInt(mId);
        paramParcel.writeInt(mFlags);
        paramParcel.writeInt(mBucketsScanned);
        paramParcel.writeInt(mAllChannelsScanned);
        paramParcel.writeInt(mResults.length);
        while (i < mResults.length)
        {
          mResults[i].writeToParcel(paramParcel, paramInt);
          i++;
        }
      }
      paramParcel.writeInt(0);
    }
  }
  
  public static abstract interface ScanListener
    extends WifiScanner.ActionListener
  {
    public abstract void onFullResult(ScanResult paramScanResult);
    
    public abstract void onPeriodChanged(int paramInt);
    
    public abstract void onResults(WifiScanner.ScanData[] paramArrayOfScanData);
  }
  
  public static class ScanSettings
    implements Parcelable
  {
    public static final Parcelable.Creator<ScanSettings> CREATOR = new Parcelable.Creator()
    {
      public WifiScanner.ScanSettings createFromParcel(Parcel paramAnonymousParcel)
      {
        WifiScanner.ScanSettings localScanSettings = new WifiScanner.ScanSettings();
        band = paramAnonymousParcel.readInt();
        periodInMs = paramAnonymousParcel.readInt();
        reportEvents = paramAnonymousParcel.readInt();
        numBssidsPerScan = paramAnonymousParcel.readInt();
        maxScansToCache = paramAnonymousParcel.readInt();
        maxPeriodInMs = paramAnonymousParcel.readInt();
        stepCount = paramAnonymousParcel.readInt();
        int i = paramAnonymousParcel.readInt();
        int j = 0;
        boolean bool;
        if (i == 1) {
          bool = true;
        } else {
          bool = false;
        }
        isPnoScan = bool;
        type = paramAnonymousParcel.readInt();
        int k = paramAnonymousParcel.readInt();
        channels = new WifiScanner.ChannelSpec[k];
        Object localObject;
        for (i = 0; i < k; i++)
        {
          localObject = new WifiScanner.ChannelSpec(paramAnonymousParcel.readInt());
          dwellTimeMS = paramAnonymousParcel.readInt();
          if (paramAnonymousParcel.readInt() == 1) {
            bool = true;
          } else {
            bool = false;
          }
          passive = bool;
          channels[i] = localObject;
        }
        k = paramAnonymousParcel.readInt();
        hiddenNetworks = new WifiScanner.ScanSettings.HiddenNetwork[k];
        for (i = j; i < k; i++)
        {
          localObject = paramAnonymousParcel.readString();
          hiddenNetworks[i] = new WifiScanner.ScanSettings.HiddenNetwork((String)localObject);
        }
        return localScanSettings;
      }
      
      public WifiScanner.ScanSettings[] newArray(int paramAnonymousInt)
      {
        return new WifiScanner.ScanSettings[paramAnonymousInt];
      }
    };
    public int band;
    public WifiScanner.ChannelSpec[] channels;
    public HiddenNetwork[] hiddenNetworks;
    public boolean isPnoScan;
    public int maxPeriodInMs;
    public int maxScansToCache;
    public int numBssidsPerScan;
    public int periodInMs;
    public int reportEvents;
    public int stepCount;
    public int type = 0;
    
    public ScanSettings() {}
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(band);
      paramParcel.writeInt(periodInMs);
      paramParcel.writeInt(reportEvents);
      paramParcel.writeInt(numBssidsPerScan);
      paramParcel.writeInt(maxScansToCache);
      paramParcel.writeInt(maxPeriodInMs);
      paramParcel.writeInt(stepCount);
      paramParcel.writeInt(isPnoScan);
      paramParcel.writeInt(type);
      WifiScanner.ChannelSpec[] arrayOfChannelSpec = channels;
      int i = 0;
      if (arrayOfChannelSpec != null)
      {
        paramParcel.writeInt(channels.length);
        for (paramInt = 0; paramInt < channels.length; paramInt++)
        {
          paramParcel.writeInt(channels[paramInt].frequency);
          paramParcel.writeInt(channels[paramInt].dwellTimeMS);
          paramParcel.writeInt(channels[paramInt].passive);
        }
      }
      paramParcel.writeInt(0);
      if (hiddenNetworks != null)
      {
        paramParcel.writeInt(hiddenNetworks.length);
        for (paramInt = i; paramInt < hiddenNetworks.length; paramInt++) {
          paramParcel.writeString(hiddenNetworks[paramInt].ssid);
        }
      }
      paramParcel.writeInt(0);
    }
    
    public static class HiddenNetwork
    {
      public String ssid;
      
      public HiddenNetwork(String paramString)
      {
        ssid = paramString;
      }
    }
  }
  
  private class ServiceHandler
    extends Handler
  {
    ServiceHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      if (i != 69634)
      {
        if (i != 69636)
        {
          Object localObject = WifiScanner.this.getListener(arg2);
          if (localObject == null) {
            return;
          }
          switch (what)
          {
          default: 
            return;
          case 159770: 
            ((WifiScanner.PnoScanListener)localObject).onPnoNetworkFound(((WifiScanner.ParcelableScanResults)obj).getResults());
            return;
          case 159767: 
            WifiScanner.this.removeListener(arg2);
            break;
          case 159764: 
            paramMessage = (ScanResult)obj;
            ((WifiScanner.ScanListener)localObject).onFullResult(paramMessage);
            return;
          case 159762: 
            WifiScanner.OperationResult localOperationResult = (WifiScanner.OperationResult)obj;
            ((WifiScanner.ActionListener)localObject).onFailure(reason, description);
            WifiScanner.this.removeListener(arg2);
            break;
          case 159761: 
            ((WifiScanner.ActionListener)localObject).onSuccess();
            return;
          }
          ((WifiScanner.ScanListener)localObject).onResults(((WifiScanner.ParcelableScanData)obj).getResults());
          return;
        }
        Log.e("WifiScanner", "Channel connection lost");
        WifiScanner.access$002(WifiScanner.this, null);
        getLooper().quit();
        return;
      }
    }
  }
  
  @Deprecated
  public static abstract interface WifiChangeListener
    extends WifiScanner.ActionListener
  {
    public abstract void onChanging(ScanResult[] paramArrayOfScanResult);
    
    public abstract void onQuiescence(ScanResult[] paramArrayOfScanResult);
  }
  
  @SystemApi
  @Deprecated
  public static class WifiChangeSettings
    implements Parcelable
  {
    public static final Parcelable.Creator<WifiChangeSettings> CREATOR = new Parcelable.Creator()
    {
      public WifiScanner.WifiChangeSettings createFromParcel(Parcel paramAnonymousParcel)
      {
        return new WifiScanner.WifiChangeSettings();
      }
      
      public WifiScanner.WifiChangeSettings[] newArray(int paramAnonymousInt)
      {
        return new WifiScanner.WifiChangeSettings[paramAnonymousInt];
      }
    };
    public WifiScanner.BssidInfo[] bssidInfos;
    public int lostApSampleSize;
    public int minApsBreachingThreshold;
    public int periodInMs;
    public int rssiSampleSize;
    public int unchangedSampleSize;
    
    public WifiChangeSettings() {}
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt) {}
  }
}
