package android.location;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.util.SeempLog;
import com.android.internal.location.ProviderProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationManager
{
  public static final String EXTRA_GPS_ENABLED = "enabled";
  public static final String FUSED_PROVIDER = "fused";
  public static final String GPS_ENABLED_CHANGE_ACTION = "android.location.GPS_ENABLED_CHANGE";
  public static final String GPS_FIX_CHANGE_ACTION = "android.location.GPS_FIX_CHANGE";
  public static final String GPS_PROVIDER = "gps";
  public static final String HIGH_POWER_REQUEST_CHANGE_ACTION = "android.location.HIGH_POWER_REQUEST_CHANGE";
  public static final String KEY_LOCATION_CHANGED = "location";
  public static final String KEY_PROVIDER_ENABLED = "providerEnabled";
  public static final String KEY_PROXIMITY_ENTERING = "entering";
  public static final String KEY_STATUS_CHANGED = "status";
  public static final String METADATA_SETTINGS_FOOTER_STRING = "com.android.settings.location.FOOTER_STRING";
  public static final String MODE_CHANGED_ACTION = "android.location.MODE_CHANGED";
  public static final String MODE_CHANGING_ACTION = "com.android.settings.location.MODE_CHANGING";
  public static final String NETWORK_PROVIDER = "network";
  public static final String PASSIVE_PROVIDER = "passive";
  public static final String PROVIDERS_CHANGED_ACTION = "android.location.PROVIDERS_CHANGED";
  public static final String SETTINGS_FOOTER_DISPLAYED_ACTION = "com.android.settings.location.DISPLAYED_FOOTER";
  public static final String SETTINGS_FOOTER_REMOVED_ACTION = "com.android.settings.location.REMOVED_FOOTER";
  private static final String TAG = "LocationManager";
  private final BatchedLocationCallbackTransport mBatchedLocationCallbackTransport;
  private final Context mContext;
  private final GnssMeasurementCallbackTransport mGnssMeasurementCallbackTransport;
  private final GnssNavigationMessageCallbackTransport mGnssNavigationMessageCallbackTransport;
  private final HashMap<OnNmeaMessageListener, GnssStatusListenerTransport> mGnssNmeaListeners = new HashMap();
  private volatile GnssStatus mGnssStatus;
  private final HashMap<GnssStatus.Callback, GnssStatusListenerTransport> mGnssStatusListeners = new HashMap();
  private final HashMap<GpsStatus.NmeaListener, GnssStatusListenerTransport> mGpsNmeaListeners = new HashMap();
  private final HashMap<GpsStatus.Listener, GnssStatusListenerTransport> mGpsStatusListeners = new HashMap();
  private HashMap<LocationListener, ListenerTransport> mListeners = new HashMap();
  private final ILocationManager mService;
  private int mTimeToFirstFix;
  
  public LocationManager(Context paramContext, ILocationManager paramILocationManager)
  {
    mService = paramILocationManager;
    mContext = paramContext;
    mGnssMeasurementCallbackTransport = new GnssMeasurementCallbackTransport(mContext, mService);
    mGnssNavigationMessageCallbackTransport = new GnssNavigationMessageCallbackTransport(mContext, mService);
    mBatchedLocationCallbackTransport = new BatchedLocationCallbackTransport(mContext, mService);
  }
  
  private static void checkCriteria(Criteria paramCriteria)
  {
    if (paramCriteria != null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid criteria: ");
    localStringBuilder.append(paramCriteria);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkGeofence(Geofence paramGeofence)
  {
    if (paramGeofence != null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid geofence: ");
    localStringBuilder.append(paramGeofence);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkListener(LocationListener paramLocationListener)
  {
    if (paramLocationListener != null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid listener: ");
    localStringBuilder.append(paramLocationListener);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private void checkPendingIntent(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent != null)
    {
      if (!paramPendingIntent.isTargetedToPackage())
      {
        paramPendingIntent = new IllegalArgumentException("pending intent must be targeted to package");
        if (mContext.getApplicationInfo().targetSdkVersion <= 16) {
          Log.w("LocationManager", paramPendingIntent);
        } else {
          throw paramPendingIntent;
        }
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid pending intent: ");
    localStringBuilder.append(paramPendingIntent);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void checkProvider(String paramString)
  {
    if (paramString != null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid provider: ");
    localStringBuilder.append(paramString);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private LocationProvider createProvider(String paramString, ProviderProperties paramProviderProperties)
  {
    return new LocationProvider(paramString, paramProviderProperties);
  }
  
  private void requestLocationUpdates(LocationRequest paramLocationRequest, LocationListener paramLocationListener, Looper paramLooper, PendingIntent paramPendingIntent)
  {
    SeempLog.record(47);
    String str = mContext.getPackageName();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("requestLocationUpdates, ");
    localStringBuilder.append(paramLocationRequest);
    localStringBuilder.append(" packageName= ");
    localStringBuilder.append(str);
    localStringBuilder.append(", listener= ");
    localStringBuilder.append(paramLocationListener);
    Log.d("LocationManager", localStringBuilder.toString());
    paramLocationListener = wrapListener(paramLocationListener, paramLooper);
    try
    {
      mService.requestLocationUpdates(paramLocationRequest, paramLocationListener, paramPendingIntent, str);
      return;
    }
    catch (RemoteException paramLocationRequest)
    {
      throw paramLocationRequest.rethrowFromSystemServer();
    }
  }
  
  private ListenerTransport wrapListener(LocationListener paramLocationListener, Looper paramLooper)
  {
    if (paramLocationListener == null) {
      return null;
    }
    synchronized (mListeners)
    {
      ListenerTransport localListenerTransport1 = (ListenerTransport)mListeners.get(paramLocationListener);
      ListenerTransport localListenerTransport2 = localListenerTransport1;
      if (localListenerTransport1 == null)
      {
        localListenerTransport2 = new android/location/LocationManager$ListenerTransport;
        localListenerTransport2.<init>(this, paramLocationListener, paramLooper);
      }
      mListeners.put(paramLocationListener, localListenerTransport2);
      return localListenerTransport2;
    }
  }
  
  public void addGeofence(LocationRequest paramLocationRequest, Geofence paramGeofence, PendingIntent paramPendingIntent)
  {
    checkPendingIntent(paramPendingIntent);
    checkGeofence(paramGeofence);
    try
    {
      mService.requestGeofence(paramLocationRequest, paramGeofence, paramPendingIntent, mContext.getPackageName());
      return;
    }
    catch (RemoteException paramLocationRequest)
    {
      throw paramLocationRequest.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  @Deprecated
  @SuppressLint({"Doclava125"})
  public boolean addGpsMeasurementListener(GpsMeasurementsEvent.Listener paramListener)
  {
    return false;
  }
  
  @SystemApi
  @Deprecated
  @SuppressLint({"Doclava125"})
  public boolean addGpsNavigationMessageListener(GpsNavigationMessageEvent.Listener paramListener)
  {
    return false;
  }
  
  @Deprecated
  public boolean addGpsStatusListener(GpsStatus.Listener paramListener)
  {
    SeempLog.record(43);
    if (mGpsStatusListeners.get(paramListener) != null) {
      return true;
    }
    try
    {
      GnssStatusListenerTransport localGnssStatusListenerTransport = new android/location/LocationManager$GnssStatusListenerTransport;
      localGnssStatusListenerTransport.<init>(this, paramListener);
      boolean bool = mService.registerGnssStatusCallback(localGnssStatusListenerTransport, mContext.getPackageName());
      if (bool) {
        mGpsStatusListeners.put(paramListener, localGnssStatusListenerTransport);
      }
      return bool;
    }
    catch (RemoteException paramListener)
    {
      throw paramListener.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean addNmeaListener(GpsStatus.NmeaListener paramNmeaListener)
  {
    SeempLog.record(44);
    if (mGpsNmeaListeners.get(paramNmeaListener) != null) {
      return true;
    }
    try
    {
      GnssStatusListenerTransport localGnssStatusListenerTransport = new android/location/LocationManager$GnssStatusListenerTransport;
      localGnssStatusListenerTransport.<init>(this, paramNmeaListener);
      boolean bool = mService.registerGnssStatusCallback(localGnssStatusListenerTransport, mContext.getPackageName());
      if (bool) {
        mGpsNmeaListeners.put(paramNmeaListener, localGnssStatusListenerTransport);
      }
      return bool;
    }
    catch (RemoteException paramNmeaListener)
    {
      throw paramNmeaListener.rethrowFromSystemServer();
    }
  }
  
  public boolean addNmeaListener(OnNmeaMessageListener paramOnNmeaMessageListener)
  {
    return addNmeaListener(paramOnNmeaMessageListener, null);
  }
  
  public boolean addNmeaListener(OnNmeaMessageListener paramOnNmeaMessageListener, Handler paramHandler)
  {
    if (mGpsNmeaListeners.get(paramOnNmeaMessageListener) != null) {
      return true;
    }
    try
    {
      GnssStatusListenerTransport localGnssStatusListenerTransport = new android/location/LocationManager$GnssStatusListenerTransport;
      localGnssStatusListenerTransport.<init>(this, paramOnNmeaMessageListener, paramHandler);
      boolean bool = mService.registerGnssStatusCallback(localGnssStatusListenerTransport, mContext.getPackageName());
      if (bool) {
        mGnssNmeaListeners.put(paramOnNmeaMessageListener, localGnssStatusListenerTransport);
      }
      return bool;
    }
    catch (RemoteException paramOnNmeaMessageListener)
    {
      throw paramOnNmeaMessageListener.rethrowFromSystemServer();
    }
  }
  
  public void addProximityAlert(double paramDouble1, double paramDouble2, float paramFloat, long paramLong, PendingIntent paramPendingIntent)
  {
    SeempLog.record(45);
    checkPendingIntent(paramPendingIntent);
    long l = paramLong;
    if (paramLong < 0L) {
      l = Long.MAX_VALUE;
    }
    Geofence localGeofence = Geofence.createCircle(paramDouble1, paramDouble2, paramFloat);
    LocationRequest localLocationRequest = new LocationRequest().setExpireIn(l);
    try
    {
      mService.requestGeofence(localLocationRequest, localGeofence, paramPendingIntent, mContext.getPackageName());
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void addTestProvider(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7, int paramInt1, int paramInt2)
  {
    Object localObject = new ProviderProperties(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramBoolean5, paramBoolean6, paramBoolean7, paramInt1, paramInt2);
    if (!paramString.matches("[^a-zA-Z0-9]")) {
      try
      {
        mService.addTestProvider(paramString, (ProviderProperties)localObject, mContext.getOpPackageName());
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("provider name contains illegal character: ");
    ((StringBuilder)localObject).append(paramString);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public void clearTestProviderEnabled(String paramString)
  {
    try
    {
      mService.clearTestProviderEnabled(paramString, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void clearTestProviderLocation(String paramString)
  {
    try
    {
      mService.clearTestProviderLocation(paramString, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void clearTestProviderStatus(String paramString)
  {
    try
    {
      mService.clearTestProviderStatus(paramString, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void flushGnssBatch()
  {
    try
    {
      mService.flushGnssBatch(mContext.getPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<String> getAllProviders()
  {
    try
    {
      List localList = mService.getAllProviders();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getBackgroundThrottlingWhitelist()
  {
    try
    {
      String[] arrayOfString = mService.getBackgroundThrottlingWhitelist();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getBestProvider(Criteria paramCriteria, boolean paramBoolean)
  {
    checkCriteria(paramCriteria);
    try
    {
      paramCriteria = mService.getBestProvider(paramCriteria, paramBoolean);
      return paramCriteria;
    }
    catch (RemoteException paramCriteria)
    {
      throw paramCriteria.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public int getGnssBatchSize()
  {
    try
    {
      int i = mService.getGnssBatchSize(mContext.getPackageName());
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getGnssHardwareModelName()
  {
    try
    {
      String str = mService.getGnssHardwareModelName();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getGnssYearOfHardware()
  {
    try
    {
      int i = mService.getGnssYearOfHardware();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public GpsStatus getGpsStatus(GpsStatus paramGpsStatus)
  {
    GpsStatus localGpsStatus = paramGpsStatus;
    if (paramGpsStatus == null) {
      localGpsStatus = new GpsStatus();
    }
    if (mGnssStatus != null) {
      localGpsStatus.setStatus(mGnssStatus, mTimeToFirstFix);
    }
    return localGpsStatus;
  }
  
  public Location getLastKnownLocation(String paramString)
  {
    SeempLog.record(46);
    checkProvider(paramString);
    String str = mContext.getPackageName();
    paramString = LocationRequest.createFromDeprecatedProvider(paramString, 0L, 0.0F, true);
    try
    {
      paramString = mService.getLastLocation(paramString, str);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Location getLastLocation()
  {
    Object localObject = mContext.getPackageName();
    try
    {
      localObject = mService.getLastLocation(null, (String)localObject);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public LocationProvider getProvider(String paramString)
  {
    checkProvider(paramString);
    try
    {
      ProviderProperties localProviderProperties = mService.getProviderProperties(paramString);
      if (localProviderProperties == null) {
        return null;
      }
      paramString = createProvider(paramString, localProviderProperties);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<String> getProviders(Criteria paramCriteria, boolean paramBoolean)
  {
    checkCriteria(paramCriteria);
    try
    {
      paramCriteria = mService.getProviders(paramCriteria, paramBoolean);
      return paramCriteria;
    }
    catch (RemoteException paramCriteria)
    {
      throw paramCriteria.rethrowFromSystemServer();
    }
  }
  
  public List<String> getProviders(boolean paramBoolean)
  {
    try
    {
      List localList = mService.getProviders(null, paramBoolean);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean injectLocation(Location paramLocation)
  {
    try
    {
      boolean bool = mService.injectLocation(paramLocation);
      return bool;
    }
    catch (RemoteException paramLocation)
    {
      throw paramLocation.rethrowFromSystemServer();
    }
  }
  
  public boolean isLocationEnabled()
  {
    return isLocationEnabledForUser(Process.myUserHandle());
  }
  
  @SystemApi
  public boolean isLocationEnabledForUser(UserHandle paramUserHandle)
  {
    try
    {
      boolean bool = mService.isLocationEnabledForUser(paramUserHandle.getIdentifier());
      return bool;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  public boolean isProviderEnabled(String paramString)
  {
    return isProviderEnabledForUser(paramString, Process.myUserHandle());
  }
  
  @SystemApi
  public boolean isProviderEnabledForUser(String paramString, UserHandle paramUserHandle)
  {
    checkProvider(paramString);
    try
    {
      boolean bool = mService.isProviderEnabledForUser(paramString, paramUserHandle.getIdentifier());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean registerGnssBatchedLocationCallback(long paramLong, boolean paramBoolean, BatchedLocationCallback paramBatchedLocationCallback, Handler paramHandler)
  {
    mBatchedLocationCallbackTransport.add(paramBatchedLocationCallback, paramHandler);
    try
    {
      paramBoolean = mService.startGnssBatch(paramLong, paramBoolean, mContext.getPackageName());
      return paramBoolean;
    }
    catch (RemoteException paramBatchedLocationCallback)
    {
      throw paramBatchedLocationCallback.rethrowFromSystemServer();
    }
  }
  
  public boolean registerGnssMeasurementsCallback(GnssMeasurementsEvent.Callback paramCallback)
  {
    return registerGnssMeasurementsCallback(paramCallback, null);
  }
  
  public boolean registerGnssMeasurementsCallback(GnssMeasurementsEvent.Callback paramCallback, Handler paramHandler)
  {
    return mGnssMeasurementCallbackTransport.add(paramCallback, paramHandler);
  }
  
  public boolean registerGnssNavigationMessageCallback(GnssNavigationMessage.Callback paramCallback)
  {
    return registerGnssNavigationMessageCallback(paramCallback, null);
  }
  
  public boolean registerGnssNavigationMessageCallback(GnssNavigationMessage.Callback paramCallback, Handler paramHandler)
  {
    return mGnssNavigationMessageCallbackTransport.add(paramCallback, paramHandler);
  }
  
  public boolean registerGnssStatusCallback(GnssStatus.Callback paramCallback)
  {
    return registerGnssStatusCallback(paramCallback, null);
  }
  
  public boolean registerGnssStatusCallback(GnssStatus.Callback paramCallback, Handler paramHandler)
  {
    if (mGnssStatusListeners.get(paramCallback) != null) {
      return true;
    }
    try
    {
      GnssStatusListenerTransport localGnssStatusListenerTransport = new android/location/LocationManager$GnssStatusListenerTransport;
      localGnssStatusListenerTransport.<init>(this, paramCallback, paramHandler);
      boolean bool = mService.registerGnssStatusCallback(localGnssStatusListenerTransport, mContext.getPackageName());
      if (bool) {
        mGnssStatusListeners.put(paramCallback, localGnssStatusListenerTransport);
      }
      return bool;
    }
    catch (RemoteException paramCallback)
    {
      throw paramCallback.rethrowFromSystemServer();
    }
  }
  
  public void removeAllGeofences(PendingIntent paramPendingIntent)
  {
    checkPendingIntent(paramPendingIntent);
    String str = mContext.getPackageName();
    try
    {
      mService.removeGeofence(null, paramPendingIntent, str);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void removeGeofence(Geofence paramGeofence, PendingIntent paramPendingIntent)
  {
    checkPendingIntent(paramPendingIntent);
    checkGeofence(paramGeofence);
    String str = mContext.getPackageName();
    try
    {
      mService.removeGeofence(paramGeofence, paramPendingIntent, str);
      return;
    }
    catch (RemoteException paramGeofence)
    {
      throw paramGeofence.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  @Deprecated
  @SuppressLint({"Doclava125"})
  public void removeGpsMeasurementListener(GpsMeasurementsEvent.Listener paramListener) {}
  
  @SystemApi
  @Deprecated
  @SuppressLint({"Doclava125"})
  public void removeGpsNavigationMessageListener(GpsNavigationMessageEvent.Listener paramListener) {}
  
  @Deprecated
  public void removeGpsStatusListener(GpsStatus.Listener paramListener)
  {
    try
    {
      paramListener = (GnssStatusListenerTransport)mGpsStatusListeners.remove(paramListener);
      if (paramListener != null) {
        mService.unregisterGnssStatusCallback(paramListener);
      }
      return;
    }
    catch (RemoteException paramListener)
    {
      throw paramListener.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void removeNmeaListener(GpsStatus.NmeaListener paramNmeaListener)
  {
    try
    {
      paramNmeaListener = (GnssStatusListenerTransport)mGpsNmeaListeners.remove(paramNmeaListener);
      if (paramNmeaListener != null) {
        mService.unregisterGnssStatusCallback(paramNmeaListener);
      }
      return;
    }
    catch (RemoteException paramNmeaListener)
    {
      throw paramNmeaListener.rethrowFromSystemServer();
    }
  }
  
  public void removeNmeaListener(OnNmeaMessageListener paramOnNmeaMessageListener)
  {
    try
    {
      paramOnNmeaMessageListener = (GnssStatusListenerTransport)mGnssNmeaListeners.remove(paramOnNmeaMessageListener);
      if (paramOnNmeaMessageListener != null) {
        mService.unregisterGnssStatusCallback(paramOnNmeaMessageListener);
      }
      return;
    }
    catch (RemoteException paramOnNmeaMessageListener)
    {
      throw paramOnNmeaMessageListener.rethrowFromSystemServer();
    }
  }
  
  public void removeProximityAlert(PendingIntent paramPendingIntent)
  {
    checkPendingIntent(paramPendingIntent);
    String str = mContext.getPackageName();
    try
    {
      mService.removeGeofence(null, paramPendingIntent, str);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void removeTestProvider(String paramString)
  {
    try
    {
      mService.removeTestProvider(paramString, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void removeUpdates(PendingIntent paramPendingIntent)
  {
    checkPendingIntent(paramPendingIntent);
    String str = mContext.getPackageName();
    try
    {
      mService.removeUpdates(null, paramPendingIntent, str);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void removeUpdates(LocationListener paramLocationListener)
  {
    checkListener(paramLocationListener);
    String str = mContext.getPackageName();
    ??? = new StringBuilder();
    ((StringBuilder)???).append("removeUpdates, packageName= ");
    ((StringBuilder)???).append(str);
    ((StringBuilder)???).append(",listener= ");
    ((StringBuilder)???).append(paramLocationListener);
    Log.d("LocationManager", ((StringBuilder)???).toString());
    synchronized (mListeners)
    {
      paramLocationListener = (ListenerTransport)mListeners.remove(paramLocationListener);
      if (paramLocationListener == null) {
        return;
      }
      try
      {
        mService.removeUpdates(paramLocationListener, null, str);
        return;
      }
      catch (RemoteException paramLocationListener)
      {
        throw paramLocationListener.rethrowFromSystemServer();
      }
    }
  }
  
  public void requestLocationUpdates(long paramLong, float paramFloat, Criteria paramCriteria, PendingIntent paramPendingIntent)
  {
    SeempLog.record(47);
    checkCriteria(paramCriteria);
    checkPendingIntent(paramPendingIntent);
    requestLocationUpdates(LocationRequest.createFromDeprecatedCriteria(paramCriteria, paramLong, paramFloat, false), null, null, paramPendingIntent);
  }
  
  public void requestLocationUpdates(long paramLong, float paramFloat, Criteria paramCriteria, LocationListener paramLocationListener, Looper paramLooper)
  {
    SeempLog.record(47);
    checkCriteria(paramCriteria);
    checkListener(paramLocationListener);
    requestLocationUpdates(LocationRequest.createFromDeprecatedCriteria(paramCriteria, paramLong, paramFloat, false), paramLocationListener, paramLooper, null);
  }
  
  @SystemApi
  public void requestLocationUpdates(LocationRequest paramLocationRequest, PendingIntent paramPendingIntent)
  {
    SeempLog.record(47);
    checkPendingIntent(paramPendingIntent);
    requestLocationUpdates(paramLocationRequest, null, null, paramPendingIntent);
  }
  
  @SystemApi
  public void requestLocationUpdates(LocationRequest paramLocationRequest, LocationListener paramLocationListener, Looper paramLooper)
  {
    SeempLog.record(47);
    checkListener(paramLocationListener);
    requestLocationUpdates(paramLocationRequest, paramLocationListener, paramLooper, null);
  }
  
  public void requestLocationUpdates(String paramString, long paramLong, float paramFloat, PendingIntent paramPendingIntent)
  {
    SeempLog.record(47);
    checkProvider(paramString);
    checkPendingIntent(paramPendingIntent);
    requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(paramString, paramLong, paramFloat, false), null, null, paramPendingIntent);
  }
  
  public void requestLocationUpdates(String paramString, long paramLong, float paramFloat, LocationListener paramLocationListener)
  {
    SeempLog.record(47);
    checkProvider(paramString);
    checkListener(paramLocationListener);
    requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(paramString, paramLong, paramFloat, false), paramLocationListener, null, null);
  }
  
  public void requestLocationUpdates(String paramString, long paramLong, float paramFloat, LocationListener paramLocationListener, Looper paramLooper)
  {
    SeempLog.record(47);
    checkProvider(paramString);
    checkListener(paramLocationListener);
    requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(paramString, paramLong, paramFloat, false), paramLocationListener, paramLooper, null);
  }
  
  public void requestSingleUpdate(Criteria paramCriteria, PendingIntent paramPendingIntent)
  {
    SeempLog.record(64);
    checkCriteria(paramCriteria);
    checkPendingIntent(paramPendingIntent);
    requestLocationUpdates(LocationRequest.createFromDeprecatedCriteria(paramCriteria, 0L, 0.0F, true), null, null, paramPendingIntent);
  }
  
  public void requestSingleUpdate(Criteria paramCriteria, LocationListener paramLocationListener, Looper paramLooper)
  {
    SeempLog.record(64);
    checkCriteria(paramCriteria);
    checkListener(paramLocationListener);
    requestLocationUpdates(LocationRequest.createFromDeprecatedCriteria(paramCriteria, 0L, 0.0F, true), paramLocationListener, paramLooper, null);
  }
  
  public void requestSingleUpdate(String paramString, PendingIntent paramPendingIntent)
  {
    SeempLog.record(64);
    checkProvider(paramString);
    checkPendingIntent(paramPendingIntent);
    requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(paramString, 0L, 0.0F, true), null, null, paramPendingIntent);
  }
  
  public void requestSingleUpdate(String paramString, LocationListener paramLocationListener, Looper paramLooper)
  {
    SeempLog.record(64);
    checkProvider(paramString);
    checkListener(paramLocationListener);
    requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(paramString, 0L, 0.0F, true), paramLocationListener, paramLooper, null);
  }
  
  public boolean sendExtraCommand(String paramString1, String paramString2, Bundle paramBundle)
  {
    SeempLog.record(48);
    try
    {
      boolean bool = mService.sendExtraCommand(paramString1, paramString2, paramBundle);
      return bool;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public boolean sendNiResponse(int paramInt1, int paramInt2)
  {
    try
    {
      boolean bool = mService.sendNiResponse(paramInt1, paramInt2);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setLocationEnabledForUser(boolean paramBoolean, UserHandle paramUserHandle)
  {
    try
    {
      mService.setLocationEnabledForUser(paramBoolean, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean setProviderEnabledForUser(String paramString, boolean paramBoolean, UserHandle paramUserHandle)
  {
    checkProvider(paramString);
    try
    {
      paramBoolean = mService.setProviderEnabledForUser(paramString, paramBoolean, paramUserHandle.getIdentifier());
      return paramBoolean;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setTestProviderEnabled(String paramString, boolean paramBoolean)
  {
    try
    {
      mService.setTestProviderEnabled(paramString, paramBoolean, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setTestProviderLocation(String paramString, Location paramLocation)
  {
    if (!paramLocation.isComplete())
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Incomplete location object, missing timestamp or accuracy? ");
      ((StringBuilder)localObject).append(paramLocation);
      localObject = new IllegalArgumentException(((StringBuilder)localObject).toString());
      if (mContext.getApplicationInfo().targetSdkVersion <= 16)
      {
        Log.w("LocationManager", (Throwable)localObject);
        paramLocation.makeComplete();
      }
      else
      {
        throw ((Throwable)localObject);
      }
    }
    try
    {
      mService.setTestProviderLocation(paramString, paramLocation, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setTestProviderStatus(String paramString, int paramInt, Bundle paramBundle, long paramLong)
  {
    try
    {
      mService.setTestProviderStatus(paramString, paramInt, paramBundle, paramLong, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean unregisterGnssBatchedLocationCallback(BatchedLocationCallback paramBatchedLocationCallback)
  {
    mBatchedLocationCallbackTransport.remove(paramBatchedLocationCallback);
    try
    {
      boolean bool = mService.stopGnssBatch();
      return bool;
    }
    catch (RemoteException paramBatchedLocationCallback)
    {
      throw paramBatchedLocationCallback.rethrowFromSystemServer();
    }
  }
  
  public void unregisterGnssMeasurementsCallback(GnssMeasurementsEvent.Callback paramCallback)
  {
    mGnssMeasurementCallbackTransport.remove(paramCallback);
  }
  
  public void unregisterGnssNavigationMessageCallback(GnssNavigationMessage.Callback paramCallback)
  {
    mGnssNavigationMessageCallbackTransport.remove(paramCallback);
  }
  
  public void unregisterGnssStatusCallback(GnssStatus.Callback paramCallback)
  {
    try
    {
      paramCallback = (GnssStatusListenerTransport)mGnssStatusListeners.remove(paramCallback);
      if (paramCallback != null) {
        mService.unregisterGnssStatusCallback(paramCallback);
      }
      return;
    }
    catch (RemoteException paramCallback)
    {
      throw paramCallback.rethrowFromSystemServer();
    }
  }
  
  private class GnssStatusListenerTransport
    extends IGnssStatusListener.Stub
  {
    private static final int NMEA_RECEIVED = 1000;
    private final GnssStatus.Callback mGnssCallback;
    private final Handler mGnssHandler;
    private final OnNmeaMessageListener mGnssNmeaListener;
    private final GpsStatus.Listener mGpsListener;
    private final GpsStatus.NmeaListener mGpsNmeaListener;
    private final ArrayList<Nmea> mNmeaBuffer;
    
    GnssStatusListenerTransport(GnssStatus.Callback paramCallback)
    {
      this(paramCallback, null);
    }
    
    GnssStatusListenerTransport(GnssStatus.Callback paramCallback, Handler paramHandler)
    {
      mGnssCallback = paramCallback;
      mGnssHandler = new GnssHandler(paramHandler);
      mGnssNmeaListener = null;
      mNmeaBuffer = null;
      mGpsListener = null;
      mGpsNmeaListener = null;
    }
    
    GnssStatusListenerTransport(GpsStatus.Listener paramListener)
    {
      this(paramListener, null);
    }
    
    GnssStatusListenerTransport(GpsStatus.Listener paramListener, Handler paramHandler)
    {
      mGpsListener = paramListener;
      mGnssHandler = new GnssHandler(paramHandler);
      mGpsNmeaListener = null;
      mNmeaBuffer = null;
      if (mGpsListener != null) {
        this$1 = new GnssStatus.Callback()
        {
          public void onFirstFix(int paramAnonymousInt)
          {
            mGpsListener.onGpsStatusChanged(3);
          }
          
          public void onSatelliteStatusChanged(GnssStatus paramAnonymousGnssStatus)
          {
            mGpsListener.onGpsStatusChanged(4);
          }
          
          public void onStarted()
          {
            mGpsListener.onGpsStatusChanged(1);
          }
          
          public void onStopped()
          {
            mGpsListener.onGpsStatusChanged(2);
          }
        };
      } else {
        this$1 = null;
      }
      mGnssCallback = LocationManager.this;
      mGnssNmeaListener = null;
    }
    
    GnssStatusListenerTransport(GpsStatus.NmeaListener paramNmeaListener)
    {
      this(paramNmeaListener, null);
    }
    
    GnssStatusListenerTransport(GpsStatus.NmeaListener paramNmeaListener, Handler paramHandler)
    {
      Object localObject = null;
      mGpsListener = null;
      mGnssHandler = new GnssHandler(paramHandler);
      mGpsNmeaListener = paramNmeaListener;
      mNmeaBuffer = new ArrayList();
      mGnssCallback = null;
      paramNmeaListener = localObject;
      if (mGpsNmeaListener != null) {
        paramNmeaListener = new OnNmeaMessageListener()
        {
          public void onNmeaMessage(String paramAnonymousString, long paramAnonymousLong)
          {
            mGpsNmeaListener.onNmeaReceived(paramAnonymousLong, paramAnonymousString);
          }
        };
      }
      mGnssNmeaListener = paramNmeaListener;
    }
    
    GnssStatusListenerTransport(OnNmeaMessageListener paramOnNmeaMessageListener)
    {
      this(paramOnNmeaMessageListener, null);
    }
    
    GnssStatusListenerTransport(OnNmeaMessageListener paramOnNmeaMessageListener, Handler paramHandler)
    {
      mGnssCallback = null;
      mGnssHandler = new GnssHandler(paramHandler);
      mGnssNmeaListener = paramOnNmeaMessageListener;
      mGpsListener = null;
      mGpsNmeaListener = null;
      mNmeaBuffer = new ArrayList();
    }
    
    public void onFirstFix(int paramInt)
    {
      if (mGnssCallback != null)
      {
        LocationManager.access$502(LocationManager.this, paramInt);
        Message localMessage = Message.obtain();
        what = 3;
        mGnssHandler.sendMessage(localMessage);
      }
    }
    
    public void onGnssStarted()
    {
      if (mGnssCallback != null)
      {
        Message localMessage = Message.obtain();
        what = 1;
        mGnssHandler.sendMessage(localMessage);
      }
    }
    
    public void onGnssStopped()
    {
      if (mGnssCallback != null)
      {
        Message localMessage = Message.obtain();
        what = 2;
        mGnssHandler.sendMessage(localMessage);
      }
    }
    
    public void onNmeaReceived(long paramLong, String paramString)
    {
      if (mGnssNmeaListener != null) {
        synchronized (mNmeaBuffer)
        {
          ArrayList localArrayList2 = mNmeaBuffer;
          Nmea localNmea = new android/location/LocationManager$GnssStatusListenerTransport$Nmea;
          localNmea.<init>(this, paramLong, paramString);
          localArrayList2.add(localNmea);
          paramString = Message.obtain();
          what = 1000;
          mGnssHandler.removeMessages(1000);
          mGnssHandler.sendMessage(paramString);
        }
      }
    }
    
    public void onSvStatusChanged(int paramInt, int[] paramArrayOfInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4)
    {
      if (mGnssCallback != null)
      {
        LocationManager.access$602(LocationManager.this, new GnssStatus(paramInt, paramArrayOfInt, paramArrayOfFloat1, paramArrayOfFloat2, paramArrayOfFloat3, paramArrayOfFloat4));
        paramArrayOfInt = Message.obtain();
        what = 4;
        mGnssHandler.removeMessages(4);
        mGnssHandler.sendMessage(paramArrayOfInt);
      }
    }
    
    private class GnssHandler
      extends Handler
    {
      public GnssHandler(Handler paramHandler)
      {
        super();
      }
      
      public void handleMessage(Message arg1)
      {
        int i = what;
        if (i != 1000) {
          switch (i)
          {
          default: 
            break;
          case 4: 
            mGnssCallback.onSatelliteStatusChanged(mGnssStatus);
            break;
          case 3: 
            mGnssCallback.onFirstFix(mTimeToFirstFix);
            break;
          case 2: 
            mGnssCallback.onStopped();
            break;
          case 1: 
            mGnssCallback.onStarted();
            break;
          }
        }
        synchronized (mNmeaBuffer)
        {
          int j = mNmeaBuffer.size();
          for (i = 0; i < j; i++)
          {
            LocationManager.GnssStatusListenerTransport.Nmea localNmea = (LocationManager.GnssStatusListenerTransport.Nmea)mNmeaBuffer.get(i);
            mGnssNmeaListener.onNmeaMessage(mNmea, mTimestamp);
          }
          mNmeaBuffer.clear();
          return;
        }
      }
    }
    
    private class Nmea
    {
      String mNmea;
      long mTimestamp;
      
      Nmea(long paramLong, String paramString)
      {
        mTimestamp = paramLong;
        mNmea = paramString;
      }
    }
  }
  
  private class ListenerTransport
    extends ILocationListener.Stub
  {
    private static final int TYPE_LOCATION_CHANGED = 1;
    private static final int TYPE_PROVIDER_DISABLED = 4;
    private static final int TYPE_PROVIDER_ENABLED = 3;
    private static final int TYPE_STATUS_CHANGED = 2;
    private LocationListener mListener;
    private final Handler mListenerHandler;
    
    ListenerTransport(LocationListener paramLocationListener, Looper paramLooper)
    {
      mListener = paramLocationListener;
      if (paramLooper == null) {
        mListenerHandler = new Handler()
        {
          public void handleMessage(Message paramAnonymousMessage)
          {
            LocationManager.ListenerTransport.this._handleMessage(paramAnonymousMessage);
          }
        };
      } else {
        mListenerHandler = new Handler(paramLooper)
        {
          public void handleMessage(Message paramAnonymousMessage)
          {
            LocationManager.ListenerTransport.this._handleMessage(paramAnonymousMessage);
          }
        };
      }
    }
    
    private void _handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 4: 
        mListener.onProviderDisabled((String)obj);
        break;
      case 3: 
        mListener.onProviderEnabled((String)obj);
        break;
      case 2: 
        Bundle localBundle = (Bundle)obj;
        paramMessage = localBundle.getString("provider");
        int i = localBundle.getInt("status");
        localBundle = localBundle.getBundle("extras");
        mListener.onStatusChanged(paramMessage, i, localBundle);
        break;
      case 1: 
        paramMessage = new Location((Location)obj);
        mListener.onLocationChanged(paramMessage);
      }
      try
      {
        mService.locationCallbackFinished(this);
        return;
      }
      catch (RemoteException paramMessage)
      {
        throw paramMessage.rethrowFromSystemServer();
      }
    }
    
    public void onLocationChanged(Location paramLocation)
    {
      Message localMessage = Message.obtain();
      what = 1;
      obj = paramLocation;
      mListenerHandler.sendMessage(localMessage);
    }
    
    public void onProviderDisabled(String paramString)
    {
      Message localMessage = Message.obtain();
      what = 4;
      obj = paramString;
      mListenerHandler.sendMessage(localMessage);
    }
    
    public void onProviderEnabled(String paramString)
    {
      Message localMessage = Message.obtain();
      what = 3;
      obj = paramString;
      mListenerHandler.sendMessage(localMessage);
    }
    
    public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle)
    {
      Message localMessage = Message.obtain();
      what = 2;
      Bundle localBundle = new Bundle();
      localBundle.putString("provider", paramString);
      localBundle.putInt("status", paramInt);
      if (paramBundle != null) {
        localBundle.putBundle("extras", paramBundle);
      }
      obj = localBundle;
      mListenerHandler.sendMessage(localMessage);
    }
  }
}
