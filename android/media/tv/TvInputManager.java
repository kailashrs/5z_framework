package android.media.tv;

import android.annotation.SystemApi;
import android.content.Intent;
import android.graphics.Rect;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventSender;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class TvInputManager
{
  public static final String ACTION_BLOCKED_RATINGS_CHANGED = "android.media.tv.action.BLOCKED_RATINGS_CHANGED";
  public static final String ACTION_PARENTAL_CONTROLS_ENABLED_CHANGED = "android.media.tv.action.PARENTAL_CONTROLS_ENABLED_CHANGED";
  public static final String ACTION_QUERY_CONTENT_RATING_SYSTEMS = "android.media.tv.action.QUERY_CONTENT_RATING_SYSTEMS";
  public static final String ACTION_SETUP_INPUTS = "android.media.tv.action.SETUP_INPUTS";
  public static final String ACTION_VIEW_RECORDING_SCHEDULES = "android.media.tv.action.VIEW_RECORDING_SCHEDULES";
  public static final int DVB_DEVICE_DEMUX = 0;
  public static final int DVB_DEVICE_DVR = 1;
  static final int DVB_DEVICE_END = 2;
  public static final int DVB_DEVICE_FRONTEND = 2;
  static final int DVB_DEVICE_START = 0;
  public static final int INPUT_STATE_CONNECTED = 0;
  public static final int INPUT_STATE_CONNECTED_STANDBY = 1;
  public static final int INPUT_STATE_DISCONNECTED = 2;
  public static final String META_DATA_CONTENT_RATING_SYSTEMS = "android.media.tv.metadata.CONTENT_RATING_SYSTEMS";
  static final int RECORDING_ERROR_END = 2;
  public static final int RECORDING_ERROR_INSUFFICIENT_SPACE = 1;
  public static final int RECORDING_ERROR_RESOURCE_BUSY = 2;
  static final int RECORDING_ERROR_START = 0;
  public static final int RECORDING_ERROR_UNKNOWN = 0;
  private static final String TAG = "TvInputManager";
  public static final long TIME_SHIFT_INVALID_TIME = Long.MIN_VALUE;
  public static final int TIME_SHIFT_STATUS_AVAILABLE = 3;
  public static final int TIME_SHIFT_STATUS_UNAVAILABLE = 2;
  public static final int TIME_SHIFT_STATUS_UNKNOWN = 0;
  public static final int TIME_SHIFT_STATUS_UNSUPPORTED = 1;
  public static final int VIDEO_UNAVAILABLE_REASON_AUDIO_ONLY = 4;
  public static final int VIDEO_UNAVAILABLE_REASON_BUFFERING = 3;
  static final int VIDEO_UNAVAILABLE_REASON_END = 4;
  static final int VIDEO_UNAVAILABLE_REASON_START = 0;
  public static final int VIDEO_UNAVAILABLE_REASON_TUNING = 1;
  public static final int VIDEO_UNAVAILABLE_REASON_UNKNOWN = 0;
  public static final int VIDEO_UNAVAILABLE_REASON_WEAK_SIGNAL = 2;
  private final List<TvInputCallbackRecord> mCallbackRecords = new LinkedList();
  private final ITvInputClient mClient;
  private final Object mLock = new Object();
  private int mNextSeq;
  private final ITvInputManager mService;
  private final SparseArray<SessionCallbackRecord> mSessionCallbackRecordMap = new SparseArray();
  private final Map<String, Integer> mStateMap = new ArrayMap();
  private final int mUserId;
  
  public TvInputManager(ITvInputManager arg1, int paramInt)
  {
    mService = ???;
    mUserId = paramInt;
    mClient = new ITvInputClient.Stub()
    {
      private void postVideoSizeChangedIfNeededLocked(TvInputManager.SessionCallbackRecord paramAnonymousSessionCallbackRecord)
      {
        TvTrackInfo localTvTrackInfo = TvInputManager.SessionCallbackRecord.access$100(paramAnonymousSessionCallbackRecord).getVideoTrackToNotify();
        if (localTvTrackInfo != null) {
          paramAnonymousSessionCallbackRecord.postVideoSizeChanged(localTvTrackInfo.getVideoWidth(), localTvTrackInfo.getVideoHeight());
        }
      }
      
      public void onChannelRetuned(Uri paramAnonymousUri, int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          TvInputManager.SessionCallbackRecord localSessionCallbackRecord = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localSessionCallbackRecord == null)
          {
            paramAnonymousUri = new java/lang/StringBuilder;
            paramAnonymousUri.<init>();
            paramAnonymousUri.append("Callback not found for seq ");
            paramAnonymousUri.append(paramAnonymousInt);
            Log.e("TvInputManager", paramAnonymousUri.toString());
            return;
          }
          localSessionCallbackRecord.postChannelRetuned(paramAnonymousUri);
          return;
        }
      }
      
      public void onContentAllowed(int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq ");
            ((StringBuilder)localObject1).append(paramAnonymousInt);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          ((TvInputManager.SessionCallbackRecord)localObject1).postContentAllowed();
          return;
        }
      }
      
      public void onContentBlocked(String paramAnonymousString, int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          TvInputManager.SessionCallbackRecord localSessionCallbackRecord = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localSessionCallbackRecord == null)
          {
            paramAnonymousString = new java/lang/StringBuilder;
            paramAnonymousString.<init>();
            paramAnonymousString.append("Callback not found for seq ");
            paramAnonymousString.append(paramAnonymousInt);
            Log.e("TvInputManager", paramAnonymousString.toString());
            return;
          }
          localSessionCallbackRecord.postContentBlocked(TvContentRating.unflattenFromString(paramAnonymousString));
          return;
        }
      }
      
      public void onError(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt2);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq ");
            ((StringBuilder)localObject1).append(paramAnonymousInt2);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          ((TvInputManager.SessionCallbackRecord)localObject1).postError(paramAnonymousInt1);
          return;
        }
      }
      
      public void onLayoutSurface(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt5);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq ");
            ((StringBuilder)localObject1).append(paramAnonymousInt5);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          ((TvInputManager.SessionCallbackRecord)localObject1).postLayoutSurface(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousInt4);
          return;
        }
      }
      
      public void onRecordingStopped(Uri paramAnonymousUri, int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          TvInputManager.SessionCallbackRecord localSessionCallbackRecord = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localSessionCallbackRecord == null)
          {
            paramAnonymousUri = new java/lang/StringBuilder;
            paramAnonymousUri.<init>();
            paramAnonymousUri.append("Callback not found for seq ");
            paramAnonymousUri.append(paramAnonymousInt);
            Log.e("TvInputManager", paramAnonymousUri.toString());
            return;
          }
          localSessionCallbackRecord.postRecordingStopped(paramAnonymousUri);
          return;
        }
      }
      
      public void onSessionCreated(String paramAnonymousString, IBinder paramAnonymousIBinder, InputChannel paramAnonymousInputChannel, int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          paramAnonymousString = mSessionCallbackRecordMap;
        }
        try
        {
          TvInputManager.SessionCallbackRecord localSessionCallbackRecord = (TvInputManager.SessionCallbackRecord)paramAnonymousString.get(paramAnonymousInt);
          if (localSessionCallbackRecord == null)
          {
            paramAnonymousString = new java/lang/StringBuilder;
            paramAnonymousString.<init>();
            paramAnonymousString.append("Callback not found for ");
            paramAnonymousString.append(paramAnonymousIBinder);
            Log.e("TvInputManager", paramAnonymousString.toString());
            return;
          }
          paramAnonymousString = null;
          if (paramAnonymousIBinder != null)
          {
            paramAnonymousString = new android/media/tv/TvInputManager$Session;
            paramAnonymousString.<init>(paramAnonymousIBinder, paramAnonymousInputChannel, mService, mUserId, paramAnonymousInt, mSessionCallbackRecordMap, null);
          }
          localSessionCallbackRecord.postSessionCreated(paramAnonymousString);
          return;
        }
        finally
        {
          for (;;) {}
        }
        paramAnonymousString = finally;
        throw paramAnonymousString;
      }
      
      public void onSessionEvent(String paramAnonymousString, Bundle paramAnonymousBundle, int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          TvInputManager.SessionCallbackRecord localSessionCallbackRecord = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localSessionCallbackRecord == null)
          {
            paramAnonymousString = new java/lang/StringBuilder;
            paramAnonymousString.<init>();
            paramAnonymousString.append("Callback not found for seq ");
            paramAnonymousString.append(paramAnonymousInt);
            Log.e("TvInputManager", paramAnonymousString.toString());
            return;
          }
          localSessionCallbackRecord.postSessionEvent(paramAnonymousString, paramAnonymousBundle);
          return;
        }
      }
      
      public void onSessionReleased(int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          mSessionCallbackRecordMap.delete(paramAnonymousInt);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq:");
            ((StringBuilder)localObject1).append(paramAnonymousInt);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          TvInputManager.Session.access$700(TvInputManager.SessionCallbackRecord.access$100((TvInputManager.SessionCallbackRecord)localObject1));
          ((TvInputManager.SessionCallbackRecord)localObject1).postSessionReleased();
          return;
        }
      }
      
      public void onTimeShiftCurrentPositionChanged(long paramAnonymousLong, int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq ");
            ((StringBuilder)localObject1).append(paramAnonymousInt);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          ((TvInputManager.SessionCallbackRecord)localObject1).postTimeShiftCurrentPositionChanged(paramAnonymousLong);
          return;
        }
      }
      
      public void onTimeShiftStartPositionChanged(long paramAnonymousLong, int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq ");
            ((StringBuilder)localObject1).append(paramAnonymousInt);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          ((TvInputManager.SessionCallbackRecord)localObject1).postTimeShiftStartPositionChanged(paramAnonymousLong);
          return;
        }
      }
      
      public void onTimeShiftStatusChanged(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt2);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq ");
            ((StringBuilder)localObject1).append(paramAnonymousInt2);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          ((TvInputManager.SessionCallbackRecord)localObject1).postTimeShiftStatusChanged(paramAnonymousInt1);
          return;
        }
      }
      
      public void onTrackSelected(int paramAnonymousInt1, String paramAnonymousString, int paramAnonymousInt2)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          TvInputManager.SessionCallbackRecord localSessionCallbackRecord = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt2);
          if (localSessionCallbackRecord == null)
          {
            paramAnonymousString = new java/lang/StringBuilder;
            paramAnonymousString.<init>();
            paramAnonymousString.append("Callback not found for seq ");
            paramAnonymousString.append(paramAnonymousInt2);
            Log.e("TvInputManager", paramAnonymousString.toString());
            return;
          }
          if (TvInputManager.SessionCallbackRecord.access$100(localSessionCallbackRecord).updateTrackSelection(paramAnonymousInt1, paramAnonymousString))
          {
            localSessionCallbackRecord.postTrackSelected(paramAnonymousInt1, paramAnonymousString);
            postVideoSizeChangedIfNeededLocked(localSessionCallbackRecord);
          }
          return;
        }
      }
      
      public void onTracksChanged(List<TvTrackInfo> paramAnonymousList, int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          TvInputManager.SessionCallbackRecord localSessionCallbackRecord = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localSessionCallbackRecord == null)
          {
            paramAnonymousList = new java/lang/StringBuilder;
            paramAnonymousList.<init>();
            paramAnonymousList.append("Callback not found for seq ");
            paramAnonymousList.append(paramAnonymousInt);
            Log.e("TvInputManager", paramAnonymousList.toString());
            return;
          }
          if (TvInputManager.SessionCallbackRecord.access$100(localSessionCallbackRecord).updateTracks(paramAnonymousList))
          {
            localSessionCallbackRecord.postTracksChanged(paramAnonymousList);
            postVideoSizeChangedIfNeededLocked(localSessionCallbackRecord);
          }
          return;
        }
      }
      
      public void onTuned(int paramAnonymousInt, Uri paramAnonymousUri)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          TvInputManager.SessionCallbackRecord localSessionCallbackRecord = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localSessionCallbackRecord == null)
          {
            paramAnonymousUri = new java/lang/StringBuilder;
            paramAnonymousUri.<init>();
            paramAnonymousUri.append("Callback not found for seq ");
            paramAnonymousUri.append(paramAnonymousInt);
            Log.e("TvInputManager", paramAnonymousUri.toString());
            return;
          }
          localSessionCallbackRecord.postTuned(paramAnonymousUri);
          return;
        }
      }
      
      public void onVideoAvailable(int paramAnonymousInt)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq ");
            ((StringBuilder)localObject1).append(paramAnonymousInt);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          ((TvInputManager.SessionCallbackRecord)localObject1).postVideoAvailable();
          return;
        }
      }
      
      public void onVideoUnavailable(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        synchronized (mSessionCallbackRecordMap)
        {
          Object localObject1 = (TvInputManager.SessionCallbackRecord)mSessionCallbackRecordMap.get(paramAnonymousInt2);
          if (localObject1 == null)
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("Callback not found for seq ");
            ((StringBuilder)localObject1).append(paramAnonymousInt2);
            Log.e("TvInputManager", ((StringBuilder)localObject1).toString());
            return;
          }
          ((TvInputManager.SessionCallbackRecord)localObject1).postVideoUnavailable(paramAnonymousInt1);
          return;
        }
      }
    };
    ??? = new ITvInputManagerCallback.Stub()
    {
      public void onInputAdded(String paramAnonymousString)
      {
        synchronized (mLock)
        {
          mStateMap.put(paramAnonymousString, Integer.valueOf(0));
          Iterator localIterator = mCallbackRecords.iterator();
          while (localIterator.hasNext()) {
            ((TvInputManager.TvInputCallbackRecord)localIterator.next()).postInputAdded(paramAnonymousString);
          }
          return;
        }
      }
      
      public void onInputRemoved(String paramAnonymousString)
      {
        synchronized (mLock)
        {
          mStateMap.remove(paramAnonymousString);
          Iterator localIterator = mCallbackRecords.iterator();
          while (localIterator.hasNext()) {
            ((TvInputManager.TvInputCallbackRecord)localIterator.next()).postInputRemoved(paramAnonymousString);
          }
          return;
        }
      }
      
      public void onInputStateChanged(String paramAnonymousString, int paramAnonymousInt)
      {
        synchronized (mLock)
        {
          mStateMap.put(paramAnonymousString, Integer.valueOf(paramAnonymousInt));
          Iterator localIterator = mCallbackRecords.iterator();
          while (localIterator.hasNext()) {
            ((TvInputManager.TvInputCallbackRecord)localIterator.next()).postInputStateChanged(paramAnonymousString, paramAnonymousInt);
          }
          return;
        }
      }
      
      public void onInputUpdated(String paramAnonymousString)
      {
        synchronized (mLock)
        {
          Iterator localIterator = mCallbackRecords.iterator();
          while (localIterator.hasNext()) {
            ((TvInputManager.TvInputCallbackRecord)localIterator.next()).postInputUpdated(paramAnonymousString);
          }
          return;
        }
      }
      
      public void onTvInputInfoUpdated(TvInputInfo paramAnonymousTvInputInfo)
      {
        synchronized (mLock)
        {
          Iterator localIterator = mCallbackRecords.iterator();
          while (localIterator.hasNext()) {
            ((TvInputManager.TvInputCallbackRecord)localIterator.next()).postTvInputInfoUpdated(paramAnonymousTvInputInfo);
          }
          return;
        }
      }
    };
    try
    {
      if (mService != null)
      {
        mService.registerCallback(???, mUserId);
        Object localObject1 = mService.getTvInputList(mUserId);
        synchronized (mLock)
        {
          Iterator localIterator = ((List)localObject1).iterator();
          while (localIterator.hasNext())
          {
            localObject1 = ((TvInputInfo)localIterator.next()).getId();
            mStateMap.put(localObject1, Integer.valueOf(mService.getTvInputState((String)localObject1, mUserId)));
          }
        }
      }
      return;
    }
    catch (RemoteException ???)
    {
      throw ???.rethrowFromSystemServer();
    }
  }
  
  private void createSessionInternal(String paramString, boolean paramBoolean, SessionCallback arg3, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(???);
    Preconditions.checkNotNull(paramHandler);
    paramHandler = new SessionCallbackRecord(???, paramHandler);
    synchronized (mSessionCallbackRecordMap)
    {
      int i = mNextSeq;
      mNextSeq = (i + 1);
      mSessionCallbackRecordMap.put(i, paramHandler);
      try
      {
        mService.createSession(mClient, paramString, paramBoolean, i, mUserId);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
  }
  
  @SystemApi
  public Hardware acquireTvInputHardware(int paramInt, TvInputInfo paramTvInputInfo, HardwareCallback paramHardwareCallback)
  {
    try
    {
      ITvInputManager localITvInputManager = mService;
      ITvInputHardwareCallback.Stub local3 = new android/media/tv/TvInputManager$3;
      local3.<init>(this, paramHardwareCallback);
      paramTvInputInfo = new Hardware(localITvInputManager.acquireTvInputHardware(paramInt, local3, paramTvInputInfo, mUserId), null);
      return paramTvInputInfo;
    }
    catch (RemoteException paramTvInputInfo)
    {
      throw paramTvInputInfo.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public Hardware acquireTvInputHardware(int paramInt, HardwareCallback paramHardwareCallback, TvInputInfo paramTvInputInfo)
  {
    return acquireTvInputHardware(paramInt, paramTvInputInfo, paramHardwareCallback);
  }
  
  @SystemApi
  public void addBlockedRating(TvContentRating paramTvContentRating)
  {
    Preconditions.checkNotNull(paramTvContentRating);
    try
    {
      mService.addBlockedRating(paramTvContentRating.flattenToString(), mUserId);
      return;
    }
    catch (RemoteException paramTvContentRating)
    {
      throw paramTvContentRating.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean captureFrame(String paramString, Surface paramSurface, TvStreamConfig paramTvStreamConfig)
  {
    try
    {
      boolean bool = mService.captureFrame(paramString, paramSurface, paramTvStreamConfig, mUserId);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void createRecordingSession(String paramString, SessionCallback paramSessionCallback, Handler paramHandler)
  {
    createSessionInternal(paramString, true, paramSessionCallback, paramHandler);
  }
  
  public void createSession(String paramString, SessionCallback paramSessionCallback, Handler paramHandler)
  {
    createSessionInternal(paramString, false, paramSessionCallback, paramHandler);
  }
  
  @SystemApi
  public List<TvStreamConfig> getAvailableTvStreamConfigList(String paramString)
  {
    try
    {
      paramString = mService.getAvailableTvStreamConfigList(paramString, mUserId);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public List<TvContentRating> getBlockedRatings()
  {
    try
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>();
      Iterator localIterator = mService.getBlockedRatings(mUserId).iterator();
      while (localIterator.hasNext()) {
        localArrayList.add(TvContentRating.unflattenFromString((String)localIterator.next()));
      }
      return localArrayList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<DvbDeviceInfo> getDvbDeviceList()
  {
    try
    {
      List localList = mService.getDvbDeviceList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public List<TvInputHardwareInfo> getHardwareList()
  {
    try
    {
      List localList = mService.getHardwareList();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getInputState(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    synchronized (mLock)
    {
      Object localObject2 = (Integer)mStateMap.get(paramString);
      if (localObject2 == null)
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Unrecognized input ID: ");
        ((StringBuilder)localObject2).append(paramString);
        Log.w("TvInputManager", ((StringBuilder)localObject2).toString());
        return 2;
      }
      int i = ((Integer)localObject2).intValue();
      return i;
    }
  }
  
  @SystemApi
  public List<TvContentRatingSystemInfo> getTvContentRatingSystemList()
  {
    try
    {
      List localList = mService.getTvContentRatingSystemList(mUserId);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public TvInputInfo getTvInputInfo(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    try
    {
      paramString = mService.getTvInputInfo(paramString, mUserId);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<TvInputInfo> getTvInputList()
  {
    try
    {
      List localList = mService.getTvInputList(mUserId);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isParentalControlsEnabled()
  {
    try
    {
      boolean bool = mService.isParentalControlsEnabled(mUserId);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isRatingBlocked(TvContentRating paramTvContentRating)
  {
    Preconditions.checkNotNull(paramTvContentRating);
    try
    {
      boolean bool = mService.isRatingBlocked(paramTvContentRating.flattenToString(), mUserId);
      return bool;
    }
    catch (RemoteException paramTvContentRating)
    {
      throw paramTvContentRating.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean isSingleSessionActive()
  {
    try
    {
      boolean bool = mService.isSingleSessionActive(mUserId);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void notifyPreviewProgramAddedToWatchNext(String paramString, long paramLong1, long paramLong2)
  {
    Intent localIntent = new Intent();
    localIntent.setAction("android.media.tv.action.PREVIEW_PROGRAM_ADDED_TO_WATCH_NEXT");
    localIntent.putExtra("android.media.tv.extra.PREVIEW_PROGRAM_ID", paramLong1);
    localIntent.putExtra("android.media.tv.extra.WATCH_NEXT_PROGRAM_ID", paramLong2);
    localIntent.setPackage(paramString);
    try
    {
      mService.sendTvInputNotifyIntent(localIntent, mUserId);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void notifyPreviewProgramBrowsableDisabled(String paramString, long paramLong)
  {
    Intent localIntent = new Intent();
    localIntent.setAction("android.media.tv.action.PREVIEW_PROGRAM_BROWSABLE_DISABLED");
    localIntent.putExtra("android.media.tv.extra.PREVIEW_PROGRAM_ID", paramLong);
    localIntent.setPackage(paramString);
    try
    {
      mService.sendTvInputNotifyIntent(localIntent, mUserId);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void notifyWatchNextProgramBrowsableDisabled(String paramString, long paramLong)
  {
    Intent localIntent = new Intent();
    localIntent.setAction("android.media.tv.action.WATCH_NEXT_PROGRAM_BROWSABLE_DISABLED");
    localIntent.putExtra("android.media.tv.extra.WATCH_NEXT_PROGRAM_ID", paramLong);
    localIntent.setPackage(paramString);
    try
    {
      mService.sendTvInputNotifyIntent(localIntent, mUserId);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public ParcelFileDescriptor openDvbDevice(DvbDeviceInfo paramDvbDeviceInfo, int paramInt)
  {
    if ((paramInt >= 0) && (2 >= paramInt)) {}
    try
    {
      return mService.openDvbDevice(paramDvbDeviceInfo, paramInt);
    }
    catch (RemoteException paramDvbDeviceInfo)
    {
      StringBuilder localStringBuilder;
      throw paramDvbDeviceInfo.rethrowFromSystemServer();
    }
    paramDvbDeviceInfo = new java/lang/IllegalArgumentException;
    localStringBuilder = new java/lang/StringBuilder;
    localStringBuilder.<init>();
    localStringBuilder.append("Invalid DVB device: ");
    localStringBuilder.append(paramInt);
    paramDvbDeviceInfo.<init>(localStringBuilder.toString());
    throw paramDvbDeviceInfo;
  }
  
  public void registerCallback(TvInputCallback paramTvInputCallback, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramTvInputCallback);
    Preconditions.checkNotNull(paramHandler);
    synchronized (mLock)
    {
      List localList = mCallbackRecords;
      TvInputCallbackRecord localTvInputCallbackRecord = new android/media/tv/TvInputManager$TvInputCallbackRecord;
      localTvInputCallbackRecord.<init>(paramTvInputCallback, paramHandler);
      localList.add(localTvInputCallbackRecord);
      return;
    }
  }
  
  @SystemApi
  public void releaseTvInputHardware(int paramInt, Hardware paramHardware)
  {
    try
    {
      mService.releaseTvInputHardware(paramInt, paramHardware.getInterface(), mUserId);
      return;
    }
    catch (RemoteException paramHardware)
    {
      throw paramHardware.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void removeBlockedRating(TvContentRating paramTvContentRating)
  {
    Preconditions.checkNotNull(paramTvContentRating);
    try
    {
      mService.removeBlockedRating(paramTvContentRating.flattenToString(), mUserId);
      return;
    }
    catch (RemoteException paramTvContentRating)
    {
      throw paramTvContentRating.rethrowFromSystemServer();
    }
  }
  
  public void requestChannelBrowsable(Uri paramUri)
  {
    try
    {
      mService.requestChannelBrowsable(paramUri, mUserId);
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setParentalControlsEnabled(boolean paramBoolean)
  {
    try
    {
      mService.setParentalControlsEnabled(paramBoolean, mUserId);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void unregisterCallback(TvInputCallback paramTvInputCallback)
  {
    Preconditions.checkNotNull(paramTvInputCallback);
    synchronized (mLock)
    {
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext()) {
        if (((TvInputCallbackRecord)localIterator.next()).getCallback() == paramTvInputCallback)
        {
          localIterator.remove();
          break;
        }
      }
      return;
    }
  }
  
  public void updateTvInputInfo(TvInputInfo paramTvInputInfo)
  {
    Preconditions.checkNotNull(paramTvInputInfo);
    try
    {
      mService.updateTvInputInfo(paramTvInputInfo, mUserId);
      return;
    }
    catch (RemoteException paramTvInputInfo)
    {
      throw paramTvInputInfo.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public static final class Hardware
  {
    private final ITvInputHardware mInterface;
    
    private Hardware(ITvInputHardware paramITvInputHardware)
    {
      mInterface = paramITvInputHardware;
    }
    
    private ITvInputHardware getInterface()
    {
      return mInterface;
    }
    
    @SystemApi
    public boolean dispatchKeyEventToHdmi(KeyEvent paramKeyEvent)
    {
      return false;
    }
    
    public void overrideAudioSink(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4)
    {
      try
      {
        mInterface.overrideAudioSink(paramInt1, paramString, paramInt2, paramInt3, paramInt4);
        return;
      }
      catch (RemoteException paramString)
      {
        throw new RuntimeException(paramString);
      }
    }
    
    public void setStreamVolume(float paramFloat)
    {
      try
      {
        mInterface.setStreamVolume(paramFloat);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw new RuntimeException(localRemoteException);
      }
    }
    
    public boolean setSurface(Surface paramSurface, TvStreamConfig paramTvStreamConfig)
    {
      try
      {
        boolean bool = mInterface.setSurface(paramSurface, paramTvStreamConfig);
        return bool;
      }
      catch (RemoteException paramSurface)
      {
        throw new RuntimeException(paramSurface);
      }
    }
  }
  
  @SystemApi
  public static abstract class HardwareCallback
  {
    public HardwareCallback() {}
    
    public abstract void onReleased();
    
    public abstract void onStreamConfigChanged(TvStreamConfig[] paramArrayOfTvStreamConfig);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface InputState {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RecordingError {}
  
  public static final class Session
  {
    static final int DISPATCH_HANDLED = 1;
    static final int DISPATCH_IN_PROGRESS = -1;
    static final int DISPATCH_NOT_HANDLED = 0;
    private static final long INPUT_SESSION_NOT_RESPONDING_TIMEOUT = 2500L;
    private final List<TvTrackInfo> mAudioTracks = new ArrayList();
    private InputChannel mChannel;
    private final InputEventHandler mHandler = new InputEventHandler(Looper.getMainLooper());
    private final Object mMetadataLock = new Object();
    private final Pools.Pool<PendingEvent> mPendingEventPool = new Pools.SimplePool(20);
    private final SparseArray<PendingEvent> mPendingEvents = new SparseArray(20);
    private String mSelectedAudioTrackId;
    private String mSelectedSubtitleTrackId;
    private String mSelectedVideoTrackId;
    private TvInputEventSender mSender;
    private final int mSeq;
    private final ITvInputManager mService;
    private final SparseArray<TvInputManager.SessionCallbackRecord> mSessionCallbackRecordMap;
    private final List<TvTrackInfo> mSubtitleTracks = new ArrayList();
    private IBinder mToken;
    private final int mUserId;
    private int mVideoHeight;
    private final List<TvTrackInfo> mVideoTracks = new ArrayList();
    private int mVideoWidth;
    
    private Session(IBinder paramIBinder, InputChannel paramInputChannel, ITvInputManager paramITvInputManager, int paramInt1, int paramInt2, SparseArray<TvInputManager.SessionCallbackRecord> paramSparseArray)
    {
      mToken = paramIBinder;
      mChannel = paramInputChannel;
      mService = paramITvInputManager;
      mUserId = paramInt1;
      mSeq = paramInt2;
      mSessionCallbackRecordMap = paramSparseArray;
    }
    
    private boolean containsTrack(List<TvTrackInfo> paramList, String paramString)
    {
      paramList = paramList.iterator();
      while (paramList.hasNext()) {
        if (((TvTrackInfo)paramList.next()).getId().equals(paramString)) {
          return true;
        }
      }
      return false;
    }
    
    private void flushPendingEventsLocked()
    {
      mHandler.removeMessages(3);
      int i = mPendingEvents.size();
      for (int j = 0; j < i; j++)
      {
        int k = mPendingEvents.keyAt(j);
        Message localMessage = mHandler.obtainMessage(3, k, 0);
        localMessage.setAsynchronous(true);
        localMessage.sendToTarget();
      }
    }
    
    private PendingEvent obtainPendingEventLocked(InputEvent paramInputEvent, Object paramObject, FinishedInputEventCallback paramFinishedInputEventCallback, Handler paramHandler)
    {
      PendingEvent localPendingEvent1 = (PendingEvent)mPendingEventPool.acquire();
      PendingEvent localPendingEvent2 = localPendingEvent1;
      if (localPendingEvent1 == null) {
        localPendingEvent2 = new PendingEvent(null);
      }
      mEvent = paramInputEvent;
      mEventToken = paramObject;
      mCallback = paramFinishedInputEventCallback;
      mEventHandler = paramHandler;
      return localPendingEvent2;
    }
    
    private void recyclePendingEventLocked(PendingEvent paramPendingEvent)
    {
      paramPendingEvent.recycle();
      mPendingEventPool.release(paramPendingEvent);
    }
    
    private void releaseInternal()
    {
      mToken = null;
      synchronized (mHandler)
      {
        if (mChannel != null)
        {
          if (mSender != null)
          {
            flushPendingEventsLocked();
            mSender.dispose();
            mSender = null;
          }
          mChannel.dispose();
          mChannel = null;
        }
        synchronized (mSessionCallbackRecordMap)
        {
          mSessionCallbackRecordMap.remove(mSeq);
          return;
        }
      }
    }
    
    private void sendInputEventAndReportResultOnMainLooper(PendingEvent paramPendingEvent)
    {
      synchronized (mHandler)
      {
        if (sendInputEventOnMainLooperLocked(paramPendingEvent) == -1) {
          return;
        }
        invokeFinishedInputEventCallback(paramPendingEvent, false);
        return;
      }
    }
    
    private int sendInputEventOnMainLooperLocked(PendingEvent paramPendingEvent)
    {
      if (mChannel != null)
      {
        if (mSender == null) {
          mSender = new TvInputEventSender(mChannel, mHandler.getLooper());
        }
        InputEvent localInputEvent = mEvent;
        int i = localInputEvent.getSequenceNumber();
        if (mSender.sendInputEvent(i, localInputEvent))
        {
          mPendingEvents.put(i, paramPendingEvent);
          paramPendingEvent = mHandler.obtainMessage(2, paramPendingEvent);
          paramPendingEvent.setAsynchronous(true);
          mHandler.sendMessageDelayed(paramPendingEvent, 2500L);
          return -1;
        }
        paramPendingEvent = new StringBuilder();
        paramPendingEvent.append("Unable to send input event to session: ");
        paramPendingEvent.append(mToken);
        paramPendingEvent.append(" dropping:");
        paramPendingEvent.append(localInputEvent);
        Log.w("TvInputManager", paramPendingEvent.toString());
      }
      return 0;
    }
    
    void createOverlayView(View paramView, Rect paramRect)
    {
      Preconditions.checkNotNull(paramView);
      Preconditions.checkNotNull(paramRect);
      if (paramView.getWindowToken() != null)
      {
        if (mToken == null)
        {
          Log.w("TvInputManager", "The session has been already released");
          return;
        }
        try
        {
          mService.createOverlayView(mToken, paramView.getWindowToken(), paramRect, mUserId);
          return;
        }
        catch (RemoteException paramView)
        {
          throw paramView.rethrowFromSystemServer();
        }
      }
      throw new IllegalStateException("view must be attached to a window");
    }
    
    public int dispatchInputEvent(InputEvent paramInputEvent, Object paramObject, FinishedInputEventCallback paramFinishedInputEventCallback, Handler paramHandler)
    {
      Preconditions.checkNotNull(paramInputEvent);
      Preconditions.checkNotNull(paramFinishedInputEventCallback);
      Preconditions.checkNotNull(paramHandler);
      synchronized (mHandler)
      {
        if (mChannel == null) {
          return 0;
        }
        paramInputEvent = obtainPendingEventLocked(paramInputEvent, paramObject, paramFinishedInputEventCallback, paramHandler);
        if (Looper.myLooper() == Looper.getMainLooper())
        {
          int i = sendInputEventOnMainLooperLocked(paramInputEvent);
          return i;
        }
        paramInputEvent = mHandler.obtainMessage(1, paramInputEvent);
        paramInputEvent.setAsynchronous(true);
        mHandler.sendMessage(paramInputEvent);
        return -1;
      }
    }
    
    public void dispatchSurfaceChanged(int paramInt1, int paramInt2, int paramInt3)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.dispatchSurfaceChanged(mToken, paramInt1, paramInt2, paramInt3, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    void finishedInputEvent(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    {
      synchronized (mHandler)
      {
        paramInt = mPendingEvents.indexOfKey(paramInt);
        if (paramInt < 0) {
          return;
        }
        PendingEvent localPendingEvent = (PendingEvent)mPendingEvents.valueAt(paramInt);
        mPendingEvents.removeAt(paramInt);
        if (paramBoolean2)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Timeout waiting for session to handle input event after 2500 ms: ");
          localStringBuilder.append(mToken);
          Log.w("TvInputManager", localStringBuilder.toString());
        }
        else
        {
          mHandler.removeMessages(2, localPendingEvent);
        }
        invokeFinishedInputEventCallback(localPendingEvent, paramBoolean1);
        return;
      }
    }
    
    public String getSelectedTrack(int paramInt)
    {
      Object localObject1 = mMetadataLock;
      if (paramInt == 0) {
        try
        {
          String str1 = mSelectedAudioTrackId;
          return str1;
        }
        finally
        {
          break label88;
        }
      }
      String str2;
      if (paramInt == 1)
      {
        str2 = mSelectedVideoTrackId;
        return str2;
      }
      if (paramInt == 2)
      {
        str2 = mSelectedSubtitleTrackId;
        return str2;
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("invalid type: ");
      ((StringBuilder)localObject1).append(paramInt);
      throw new IllegalArgumentException(((StringBuilder)localObject1).toString());
      label88:
      throw str2;
    }
    
    IBinder getToken()
    {
      return mToken;
    }
    
    public List<TvTrackInfo> getTracks(int paramInt)
    {
      Object localObject1 = mMetadataLock;
      if (paramInt == 0) {
        try
        {
          if (mAudioTracks == null) {
            return null;
          }
          ArrayList localArrayList1 = new java/util/ArrayList;
          localArrayList1.<init>(mAudioTracks);
          return localArrayList1;
        }
        finally
        {
          break label142;
        }
      }
      ArrayList localArrayList2;
      if (paramInt == 1)
      {
        if (mVideoTracks == null) {
          return null;
        }
        localArrayList2 = new java/util/ArrayList;
        localArrayList2.<init>(mVideoTracks);
        return localArrayList2;
      }
      if (paramInt == 2)
      {
        if (mSubtitleTracks == null) {
          return null;
        }
        localArrayList2 = new java/util/ArrayList;
        localArrayList2.<init>(mSubtitleTracks);
        return localArrayList2;
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("invalid type: ");
      ((StringBuilder)localObject1).append(paramInt);
      throw new IllegalArgumentException(((StringBuilder)localObject1).toString());
      label142:
      throw localArrayList2;
    }
    
    TvTrackInfo getVideoTrackToNotify()
    {
      synchronized (mMetadataLock)
      {
        if ((!mVideoTracks.isEmpty()) && (mSelectedVideoTrackId != null))
        {
          Iterator localIterator = mVideoTracks.iterator();
          while (localIterator.hasNext())
          {
            TvTrackInfo localTvTrackInfo = (TvTrackInfo)localIterator.next();
            if (localTvTrackInfo.getId().equals(mSelectedVideoTrackId))
            {
              int i = localTvTrackInfo.getVideoWidth();
              int j = localTvTrackInfo.getVideoHeight();
              if ((mVideoWidth != i) || (mVideoHeight != j))
              {
                mVideoWidth = i;
                mVideoHeight = j;
                return localTvTrackInfo;
              }
            }
          }
        }
        return null;
      }
    }
    
    void invokeFinishedInputEventCallback(PendingEvent paramPendingEvent, boolean paramBoolean)
    {
      mHandled = paramBoolean;
      if (mEventHandler.getLooper().isCurrentThread())
      {
        paramPendingEvent.run();
      }
      else
      {
        paramPendingEvent = Message.obtain(mEventHandler, paramPendingEvent);
        paramPendingEvent.setAsynchronous(true);
        paramPendingEvent.sendToTarget();
      }
    }
    
    void relayoutOverlayView(Rect paramRect)
    {
      Preconditions.checkNotNull(paramRect);
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.relayoutOverlayView(mToken, paramRect, mUserId);
        return;
      }
      catch (RemoteException paramRect)
      {
        throw paramRect.rethrowFromSystemServer();
      }
    }
    
    public void release()
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.releaseSession(mToken, mUserId);
        releaseInternal();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    void removeOverlayView()
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.removeOverlayView(mToken, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void selectTrack(int paramInt, String paramString)
    {
      Object localObject = mMetadataLock;
      if (paramInt == 0)
      {
        if (paramString != null) {
          try
          {
            if (containsTrack(mAudioTracks, paramString)) {
              break label196;
            }
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Invalid audio trackId: ");
            localStringBuilder.append(paramString);
            Log.w("TvInputManager", localStringBuilder.toString());
            return;
          }
          finally
          {
            break label283;
          }
        }
      }
      else if (paramInt == 1)
      {
        if ((paramString != null) && (!containsTrack(mVideoTracks, paramString)))
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Invalid video trackId: ");
          localStringBuilder.append(paramString);
          Log.w("TvInputManager", localStringBuilder.toString());
        }
      }
      else
      {
        if (paramInt != 2) {
          break label242;
        }
        if ((paramString != null) && (!containsTrack(mSubtitleTracks, paramString)))
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Invalid subtitle trackId: ");
          localStringBuilder.append(paramString);
          Log.w("TvInputManager", localStringBuilder.toString());
          return;
        }
      }
      label196:
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.selectTrack(mToken, paramInt, paramString, mUserId);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
      label242:
      paramString = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("invalid type: ");
      localStringBuilder.append(paramInt);
      paramString.<init>(localStringBuilder.toString());
      throw paramString;
      label283:
      throw paramString;
    }
    
    public void sendAppPrivateCommand(String paramString, Bundle paramBundle)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.sendAppPrivateCommand(mToken, paramString, paramBundle, mUserId);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    
    public void setCaptionEnabled(boolean paramBoolean)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.setCaptionEnabled(mToken, paramBoolean, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    void setMain()
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.setMainSession(mToken, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void setStreamVolume(float paramFloat)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F)) {}
      try
      {
        mService.setVolume(mToken, paramFloat, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        IllegalArgumentException localIllegalArgumentException;
        throw localRemoteException.rethrowFromSystemServer();
      }
      localIllegalArgumentException = new java/lang/IllegalArgumentException;
      localIllegalArgumentException.<init>("volume should be between 0.0f and 1.0f");
      throw localIllegalArgumentException;
    }
    
    public void setSurface(Surface paramSurface)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.setSurface(mToken, paramSurface, mUserId);
        return;
      }
      catch (RemoteException paramSurface)
      {
        throw paramSurface.rethrowFromSystemServer();
      }
    }
    
    void startRecording(Uri paramUri)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.startRecording(mToken, paramUri, mUserId);
        return;
      }
      catch (RemoteException paramUri)
      {
        throw paramUri.rethrowFromSystemServer();
      }
    }
    
    void stopRecording()
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.stopRecording(mToken, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    void timeShiftEnablePositionTracking(boolean paramBoolean)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.timeShiftEnablePositionTracking(mToken, paramBoolean, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    void timeShiftPause()
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.timeShiftPause(mToken, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    void timeShiftPlay(Uri paramUri)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.timeShiftPlay(mToken, paramUri, mUserId);
        return;
      }
      catch (RemoteException paramUri)
      {
        throw paramUri.rethrowFromSystemServer();
      }
    }
    
    void timeShiftResume()
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.timeShiftResume(mToken, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    void timeShiftSeekTo(long paramLong)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.timeShiftSeekTo(mToken, paramLong, mUserId);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    void timeShiftSetPlaybackParams(PlaybackParams paramPlaybackParams)
    {
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.timeShiftSetPlaybackParams(mToken, paramPlaybackParams, mUserId);
        return;
      }
      catch (RemoteException paramPlaybackParams)
      {
        throw paramPlaybackParams.rethrowFromSystemServer();
      }
    }
    
    public void tune(Uri paramUri)
    {
      tune(paramUri, null);
    }
    
    public void tune(Uri paramUri, Bundle paramBundle)
    {
      Preconditions.checkNotNull(paramUri);
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      synchronized (mMetadataLock)
      {
        mAudioTracks.clear();
        mVideoTracks.clear();
        mSubtitleTracks.clear();
        mSelectedAudioTrackId = null;
        mSelectedVideoTrackId = null;
        mSelectedSubtitleTrackId = null;
        mVideoWidth = 0;
        mVideoHeight = 0;
        try
        {
          mService.tune(mToken, paramUri, paramBundle, mUserId);
          return;
        }
        catch (RemoteException paramUri)
        {
          throw paramUri.rethrowFromSystemServer();
        }
      }
    }
    
    void unblockContent(TvContentRating paramTvContentRating)
    {
      Preconditions.checkNotNull(paramTvContentRating);
      if (mToken == null)
      {
        Log.w("TvInputManager", "The session has been already released");
        return;
      }
      try
      {
        mService.unblockContent(mToken, paramTvContentRating.flattenToString(), mUserId);
        return;
      }
      catch (RemoteException paramTvContentRating)
      {
        throw paramTvContentRating.rethrowFromSystemServer();
      }
    }
    
    boolean updateTrackSelection(int paramInt, String paramString)
    {
      Object localObject = mMetadataLock;
      if (paramInt == 0) {
        try
        {
          if (!TextUtils.equals(paramString, mSelectedAudioTrackId))
          {
            mSelectedAudioTrackId = paramString;
            return true;
          }
        }
        finally
        {
          break label89;
        }
      }
      if ((paramInt == 1) && (!TextUtils.equals(paramString, mSelectedVideoTrackId)))
      {
        mSelectedVideoTrackId = paramString;
        return true;
      }
      if ((paramInt == 2) && (!TextUtils.equals(paramString, mSelectedSubtitleTrackId)))
      {
        mSelectedSubtitleTrackId = paramString;
        return true;
      }
      return false;
      label89:
      throw paramString;
    }
    
    boolean updateTracks(List<TvTrackInfo> paramList)
    {
      synchronized (mMetadataLock)
      {
        mAudioTracks.clear();
        mVideoTracks.clear();
        mSubtitleTracks.clear();
        Iterator localIterator = paramList.iterator();
        boolean bool2;
        for (;;)
        {
          bool1 = localIterator.hasNext();
          bool2 = true;
          if (!bool1) {
            break;
          }
          paramList = (TvTrackInfo)localIterator.next();
          if (paramList.getType() == 0) {
            mAudioTracks.add(paramList);
          } else if (paramList.getType() == 1) {
            mVideoTracks.add(paramList);
          } else if (paramList.getType() == 2) {
            mSubtitleTracks.add(paramList);
          }
        }
        boolean bool1 = bool2;
        if (mAudioTracks.isEmpty())
        {
          bool1 = bool2;
          if (mVideoTracks.isEmpty()) {
            if (!mSubtitleTracks.isEmpty()) {
              bool1 = bool2;
            } else {
              bool1 = false;
            }
          }
        }
        return bool1;
      }
    }
    
    public static abstract interface FinishedInputEventCallback
    {
      public abstract void onFinishedInputEvent(Object paramObject, boolean paramBoolean);
    }
    
    private final class InputEventHandler
      extends Handler
    {
      public static final int MSG_FLUSH_INPUT_EVENT = 3;
      public static final int MSG_SEND_INPUT_EVENT = 1;
      public static final int MSG_TIMEOUT_INPUT_EVENT = 2;
      
      InputEventHandler(Looper paramLooper)
      {
        super(null, true);
      }
      
      public void handleMessage(Message paramMessage)
      {
        switch (what)
        {
        default: 
          return;
        case 3: 
          finishedInputEvent(arg1, false, false);
          return;
        case 2: 
          finishedInputEvent(arg1, false, true);
          return;
        }
        TvInputManager.Session.this.sendInputEventAndReportResultOnMainLooper((TvInputManager.Session.PendingEvent)obj);
      }
    }
    
    private final class PendingEvent
      implements Runnable
    {
      public TvInputManager.Session.FinishedInputEventCallback mCallback;
      public InputEvent mEvent;
      public Handler mEventHandler;
      public Object mEventToken;
      public boolean mHandled;
      
      private PendingEvent() {}
      
      public void recycle()
      {
        mEvent = null;
        mEventToken = null;
        mCallback = null;
        mEventHandler = null;
        mHandled = false;
      }
      
      public void run()
      {
        mCallback.onFinishedInputEvent(mEventToken, mHandled);
        synchronized (mEventHandler)
        {
          TvInputManager.Session.this.recyclePendingEventLocked(this);
          return;
        }
      }
    }
    
    private final class TvInputEventSender
      extends InputEventSender
    {
      public TvInputEventSender(InputChannel paramInputChannel, Looper paramLooper)
      {
        super(paramLooper);
      }
      
      public void onInputEventFinished(int paramInt, boolean paramBoolean)
      {
        finishedInputEvent(paramInt, paramBoolean, false);
      }
    }
  }
  
  public static abstract class SessionCallback
  {
    public SessionCallback() {}
    
    public void onChannelRetuned(TvInputManager.Session paramSession, Uri paramUri) {}
    
    public void onContentAllowed(TvInputManager.Session paramSession) {}
    
    public void onContentBlocked(TvInputManager.Session paramSession, TvContentRating paramTvContentRating) {}
    
    void onError(TvInputManager.Session paramSession, int paramInt) {}
    
    public void onLayoutSurface(TvInputManager.Session paramSession, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
    
    void onRecordingStopped(TvInputManager.Session paramSession, Uri paramUri) {}
    
    public void onSessionCreated(TvInputManager.Session paramSession) {}
    
    public void onSessionEvent(TvInputManager.Session paramSession, String paramString, Bundle paramBundle) {}
    
    public void onSessionReleased(TvInputManager.Session paramSession) {}
    
    public void onTimeShiftCurrentPositionChanged(TvInputManager.Session paramSession, long paramLong) {}
    
    public void onTimeShiftStartPositionChanged(TvInputManager.Session paramSession, long paramLong) {}
    
    public void onTimeShiftStatusChanged(TvInputManager.Session paramSession, int paramInt) {}
    
    public void onTrackSelected(TvInputManager.Session paramSession, int paramInt, String paramString) {}
    
    public void onTracksChanged(TvInputManager.Session paramSession, List<TvTrackInfo> paramList) {}
    
    void onTuned(TvInputManager.Session paramSession, Uri paramUri) {}
    
    public void onVideoAvailable(TvInputManager.Session paramSession) {}
    
    public void onVideoSizeChanged(TvInputManager.Session paramSession, int paramInt1, int paramInt2) {}
    
    public void onVideoUnavailable(TvInputManager.Session paramSession, int paramInt) {}
  }
  
  private static final class SessionCallbackRecord
  {
    private final Handler mHandler;
    private TvInputManager.Session mSession;
    private final TvInputManager.SessionCallback mSessionCallback;
    
    SessionCallbackRecord(TvInputManager.SessionCallback paramSessionCallback, Handler paramHandler)
    {
      mSessionCallback = paramSessionCallback;
      mHandler = paramHandler;
    }
    
    void postChannelRetuned(final Uri paramUri)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onChannelRetuned(mSession, paramUri);
        }
      });
    }
    
    void postContentAllowed()
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onContentAllowed(mSession);
        }
      });
    }
    
    void postContentBlocked(final TvContentRating paramTvContentRating)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onContentBlocked(mSession, paramTvContentRating);
        }
      });
    }
    
    void postError(final int paramInt)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onError(mSession, paramInt);
        }
      });
    }
    
    void postLayoutSurface(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onLayoutSurface(mSession, paramInt1, paramInt2, paramInt3, paramInt4);
        }
      });
    }
    
    void postRecordingStopped(final Uri paramUri)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onRecordingStopped(mSession, paramUri);
        }
      });
    }
    
    void postSessionCreated(final TvInputManager.Session paramSession)
    {
      mSession = paramSession;
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onSessionCreated(paramSession);
        }
      });
    }
    
    void postSessionEvent(final String paramString, final Bundle paramBundle)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onSessionEvent(mSession, paramString, paramBundle);
        }
      });
    }
    
    void postSessionReleased()
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onSessionReleased(mSession);
        }
      });
    }
    
    void postTimeShiftCurrentPositionChanged(final long paramLong)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onTimeShiftCurrentPositionChanged(mSession, paramLong);
        }
      });
    }
    
    void postTimeShiftStartPositionChanged(final long paramLong)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onTimeShiftStartPositionChanged(mSession, paramLong);
        }
      });
    }
    
    void postTimeShiftStatusChanged(final int paramInt)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onTimeShiftStatusChanged(mSession, paramInt);
        }
      });
    }
    
    void postTrackSelected(final int paramInt, final String paramString)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onTrackSelected(mSession, paramInt, paramString);
        }
      });
    }
    
    void postTracksChanged(final List<TvTrackInfo> paramList)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onTracksChanged(mSession, paramList);
        }
      });
    }
    
    void postTuned(final Uri paramUri)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onTuned(mSession, paramUri);
        }
      });
    }
    
    void postVideoAvailable()
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onVideoAvailable(mSession);
        }
      });
    }
    
    void postVideoSizeChanged(final int paramInt1, final int paramInt2)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onVideoSizeChanged(mSession, paramInt1, paramInt2);
        }
      });
    }
    
    void postVideoUnavailable(final int paramInt)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mSessionCallback.onVideoUnavailable(mSession, paramInt);
        }
      });
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TimeShiftStatus {}
  
  public static abstract class TvInputCallback
  {
    public TvInputCallback() {}
    
    public void onInputAdded(String paramString) {}
    
    public void onInputRemoved(String paramString) {}
    
    public void onInputStateChanged(String paramString, int paramInt) {}
    
    public void onInputUpdated(String paramString) {}
    
    public void onTvInputInfoUpdated(TvInputInfo paramTvInputInfo) {}
  }
  
  private static final class TvInputCallbackRecord
  {
    private final TvInputManager.TvInputCallback mCallback;
    private final Handler mHandler;
    
    public TvInputCallbackRecord(TvInputManager.TvInputCallback paramTvInputCallback, Handler paramHandler)
    {
      mCallback = paramTvInputCallback;
      mHandler = paramHandler;
    }
    
    public TvInputManager.TvInputCallback getCallback()
    {
      return mCallback;
    }
    
    public void postInputAdded(final String paramString)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onInputAdded(paramString);
        }
      });
    }
    
    public void postInputRemoved(final String paramString)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onInputRemoved(paramString);
        }
      });
    }
    
    public void postInputStateChanged(final String paramString, final int paramInt)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onInputStateChanged(paramString, paramInt);
        }
      });
    }
    
    public void postInputUpdated(final String paramString)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onInputUpdated(paramString);
        }
      });
    }
    
    public void postTvInputInfoUpdated(final TvInputInfo paramTvInputInfo)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          mCallback.onTvInputInfoUpdated(paramTvInputInfo);
        }
      });
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface VideoUnavailableReason {}
}
