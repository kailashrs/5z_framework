package android.media;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.hardware.display.WifiDisplay;
import android.hardware.display.WifiDisplayStatus;
import android.media.session.MediaSession;
import android.os.Handler;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class MediaRouter
{
  public static final int AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE = 1;
  public static final int CALLBACK_FLAG_PASSIVE_DISCOVERY = 8;
  public static final int CALLBACK_FLAG_PERFORM_ACTIVE_SCAN = 1;
  public static final int CALLBACK_FLAG_REQUEST_DISCOVERY = 4;
  public static final int CALLBACK_FLAG_UNFILTERED_EVENTS = 2;
  private static final boolean DEBUG = Log.isLoggable("MediaRouter", 3);
  static final int ROUTE_TYPE_ANY = 8388615;
  public static final int ROUTE_TYPE_LIVE_AUDIO = 1;
  public static final int ROUTE_TYPE_LIVE_VIDEO = 2;
  public static final int ROUTE_TYPE_REMOTE_DISPLAY = 4;
  public static final int ROUTE_TYPE_USER = 8388608;
  private static final String TAG = "MediaRouter";
  static final HashMap<Context, MediaRouter> sRouters = new HashMap();
  static Static sStatic;
  
  public MediaRouter(Context paramContext)
  {
    try
    {
      if (sStatic == null)
      {
        paramContext = paramContext.getApplicationContext();
        Static localStatic = new android/media/MediaRouter$Static;
        localStatic.<init>(paramContext);
        sStatic = localStatic;
        sStatic.startMonitoringRoutes(paramContext);
      }
      return;
    }
    finally {}
  }
  
  static void addRouteStatic(RouteInfo paramRouteInfo)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Adding route: ");
    ((StringBuilder)localObject).append(paramRouteInfo);
    Log.v("MediaRouter", ((StringBuilder)localObject).toString());
    localObject = paramRouteInfo.getCategory();
    if (!sStaticmCategories.contains(localObject)) {
      sStaticmCategories.add(localObject);
    }
    if ((((RouteCategory)localObject).isGroupable()) && (!(paramRouteInfo instanceof RouteGroup)))
    {
      localObject = new RouteGroup(paramRouteInfo.getCategory());
      mSupportedTypes = mSupportedTypes;
      sStaticmRoutes.add(localObject);
      dispatchRouteAdded((RouteInfo)localObject);
      ((RouteGroup)localObject).addRoute(paramRouteInfo);
    }
    else
    {
      sStaticmRoutes.add(paramRouteInfo);
      dispatchRouteAdded(paramRouteInfo);
    }
  }
  
  static void dispatchRouteAdded(RouteInfo paramRouteInfo)
  {
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      CallbackInfo localCallbackInfo = (CallbackInfo)localIterator.next();
      if (localCallbackInfo.filterRouteEvent(paramRouteInfo)) {
        cb.onRouteAdded(router, paramRouteInfo);
      }
    }
  }
  
  static void dispatchRouteChanged(RouteInfo paramRouteInfo)
  {
    dispatchRouteChanged(paramRouteInfo, mSupportedTypes);
  }
  
  static void dispatchRouteChanged(RouteInfo paramRouteInfo, int paramInt)
  {
    Object localObject;
    if (DEBUG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Dispatching route change: ");
      ((StringBuilder)localObject).append(paramRouteInfo);
      Log.d("MediaRouter", ((StringBuilder)localObject).toString());
    }
    int i = mSupportedTypes;
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      localObject = (CallbackInfo)localIterator.next();
      boolean bool1 = ((CallbackInfo)localObject).filterRouteEvent(paramInt);
      boolean bool2 = ((CallbackInfo)localObject).filterRouteEvent(i);
      if ((!bool1) && (bool2))
      {
        cb.onRouteAdded(router, paramRouteInfo);
        if (paramRouteInfo.isSelected()) {
          cb.onRouteSelected(router, i, paramRouteInfo);
        }
      }
      if ((bool1) || (bool2)) {
        cb.onRouteChanged(router, paramRouteInfo);
      }
      if ((bool1) && (!bool2))
      {
        if (paramRouteInfo.isSelected()) {
          cb.onRouteUnselected(router, paramInt, paramRouteInfo);
        }
        cb.onRouteRemoved(router, paramRouteInfo);
      }
    }
  }
  
  static void dispatchRouteGrouped(RouteInfo paramRouteInfo, RouteGroup paramRouteGroup, int paramInt)
  {
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      CallbackInfo localCallbackInfo = (CallbackInfo)localIterator.next();
      if (localCallbackInfo.filterRouteEvent(paramRouteGroup)) {
        cb.onRouteGrouped(router, paramRouteInfo, paramRouteGroup, paramInt);
      }
    }
  }
  
  static void dispatchRoutePresentationDisplayChanged(RouteInfo paramRouteInfo)
  {
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      CallbackInfo localCallbackInfo = (CallbackInfo)localIterator.next();
      if (localCallbackInfo.filterRouteEvent(paramRouteInfo)) {
        cb.onRoutePresentationDisplayChanged(router, paramRouteInfo);
      }
    }
  }
  
  static void dispatchRouteRemoved(RouteInfo paramRouteInfo)
  {
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      CallbackInfo localCallbackInfo = (CallbackInfo)localIterator.next();
      if (localCallbackInfo.filterRouteEvent(paramRouteInfo)) {
        cb.onRouteRemoved(router, paramRouteInfo);
      }
    }
  }
  
  static void dispatchRouteSelected(int paramInt, RouteInfo paramRouteInfo)
  {
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      CallbackInfo localCallbackInfo = (CallbackInfo)localIterator.next();
      if (localCallbackInfo.filterRouteEvent(paramRouteInfo)) {
        cb.onRouteSelected(router, paramInt, paramRouteInfo);
      }
    }
  }
  
  static void dispatchRouteUngrouped(RouteInfo paramRouteInfo, RouteGroup paramRouteGroup)
  {
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      CallbackInfo localCallbackInfo = (CallbackInfo)localIterator.next();
      if (localCallbackInfo.filterRouteEvent(paramRouteGroup)) {
        cb.onRouteUngrouped(router, paramRouteInfo, paramRouteGroup);
      }
    }
  }
  
  static void dispatchRouteUnselected(int paramInt, RouteInfo paramRouteInfo)
  {
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      CallbackInfo localCallbackInfo = (CallbackInfo)localIterator.next();
      if (localCallbackInfo.filterRouteEvent(paramRouteInfo)) {
        cb.onRouteUnselected(router, paramInt, paramRouteInfo);
      }
    }
  }
  
  static void dispatchRouteVolumeChanged(RouteInfo paramRouteInfo)
  {
    Iterator localIterator = sStaticmCallbacks.iterator();
    while (localIterator.hasNext())
    {
      CallbackInfo localCallbackInfo = (CallbackInfo)localIterator.next();
      if (localCallbackInfo.filterRouteEvent(paramRouteInfo)) {
        cb.onRouteVolumeChanged(router, paramRouteInfo);
      }
    }
  }
  
  private int findCallbackInfo(Callback paramCallback)
  {
    int i = sStaticmCallbacks.size();
    for (int j = 0; j < i; j++) {
      if (sStaticmCallbacks.get(j)).cb == paramCallback) {
        return j;
      }
    }
    return -1;
  }
  
  private static WifiDisplay findWifiDisplay(WifiDisplay[] paramArrayOfWifiDisplay, String paramString)
  {
    for (int i = 0; i < paramArrayOfWifiDisplay.length; i++)
    {
      WifiDisplay localWifiDisplay = paramArrayOfWifiDisplay[i];
      if (localWifiDisplay.getDeviceAddress().equals(paramString)) {
        return localWifiDisplay;
      }
    }
    return null;
  }
  
  private static RouteInfo findWifiDisplayRoute(WifiDisplay paramWifiDisplay)
  {
    int i = sStaticmRoutes.size();
    for (int j = 0; j < i; j++)
    {
      RouteInfo localRouteInfo = (RouteInfo)sStaticmRoutes.get(j);
      if (paramWifiDisplay.getDeviceAddress().equals(mDeviceAddress)) {
        return localRouteInfo;
      }
    }
    return null;
  }
  
  static RouteInfo getRouteAtStatic(int paramInt)
  {
    return (RouteInfo)sStaticmRoutes.get(paramInt);
  }
  
  static int getRouteCountStatic()
  {
    return sStaticmRoutes.size();
  }
  
  static int getWifiDisplayStatusCode(WifiDisplay paramWifiDisplay, WifiDisplayStatus paramWifiDisplayStatus)
  {
    int i;
    if (paramWifiDisplayStatus.getScanState() == 1) {
      i = 1;
    }
    for (;;)
    {
      break;
      if (paramWifiDisplay.isAvailable())
      {
        if (paramWifiDisplay.canConnect()) {
          i = 3;
        } else {
          i = 5;
        }
      }
      else {
        i = 4;
      }
    }
    int j = i;
    if (paramWifiDisplay.equals(paramWifiDisplayStatus.getActiveDisplay())) {
      switch (paramWifiDisplayStatus.getActiveDisplayState())
      {
      default: 
        j = i;
        break;
      case 2: 
        j = 6;
        break;
      case 1: 
        j = 2;
        break;
      case 0: 
        Log.e("MediaRouter", "Active display is not connected!");
        j = i;
      }
    }
    return j;
  }
  
  static boolean isWifiDisplayEnabled(WifiDisplay paramWifiDisplay, WifiDisplayStatus paramWifiDisplayStatus)
  {
    boolean bool;
    if ((paramWifiDisplay.isAvailable()) && ((paramWifiDisplay.canConnect()) || (paramWifiDisplay.equals(paramWifiDisplayStatus.getActiveDisplay())))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  static RouteInfo makeWifiDisplayRoute(WifiDisplay paramWifiDisplay, WifiDisplayStatus paramWifiDisplayStatus)
  {
    RouteInfo localRouteInfo = new RouteInfo(sStaticmSystemCategory);
    mDeviceAddress = paramWifiDisplay.getDeviceAddress();
    mSupportedTypes = 7;
    mVolumeHandling = 0;
    mPlaybackType = 1;
    localRouteInfo.setRealStatusCode(getWifiDisplayStatusCode(paramWifiDisplay, paramWifiDisplayStatus));
    mEnabled = isWifiDisplayEnabled(paramWifiDisplay, paramWifiDisplayStatus);
    mName = paramWifiDisplay.getFriendlyDisplayName();
    mDescription = sStaticmResources.getText(17041301);
    localRouteInfo.updatePresentationDisplay();
    mDeviceType = 1;
    return localRouteInfo;
  }
  
  static boolean matchesDeviceAddress(WifiDisplay paramWifiDisplay, RouteInfo paramRouteInfo)
  {
    int i;
    if ((paramRouteInfo != null) && (mDeviceAddress != null)) {
      i = 1;
    } else {
      i = 0;
    }
    if ((paramWifiDisplay == null) && (i == 0)) {
      return true;
    }
    if ((paramWifiDisplay != null) && (i != 0)) {
      return paramWifiDisplay.getDeviceAddress().equals(mDeviceAddress);
    }
    return false;
  }
  
  static void removeRouteStatic(RouteInfo paramRouteInfo)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Removing route: ");
    ((StringBuilder)localObject).append(paramRouteInfo);
    Log.v("MediaRouter", ((StringBuilder)localObject).toString());
    if (sStaticmRoutes.remove(paramRouteInfo))
    {
      localObject = paramRouteInfo.getCategory();
      int i = sStaticmRoutes.size();
      int j = 0;
      int m;
      for (int k = 0;; k++)
      {
        m = j;
        if (k >= i) {
          break;
        }
        if (localObject == ((RouteInfo)sStaticmRoutes.get(k)).getCategory())
        {
          m = 1;
          break;
        }
      }
      if (paramRouteInfo.isSelected()) {
        selectDefaultRouteStatic();
      }
      if (m == 0) {
        sStaticmCategories.remove(localObject);
      }
      dispatchRouteRemoved(paramRouteInfo);
    }
  }
  
  static void selectDefaultRouteStatic()
  {
    if ((sStaticmSelectedRoute != sStaticmBluetoothA2dpRoute) && (sStatic.isBluetoothA2dpOn())) {
      selectRouteStatic(8388615, sStaticmBluetoothA2dpRoute, false);
    } else {
      selectRouteStatic(8388615, sStaticmDefaultAudioVideo, false);
    }
  }
  
  static void selectRouteStatic(int paramInt, RouteInfo paramRouteInfo, boolean paramBoolean)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Selecting route: ");
    ((StringBuilder)localObject1).append(paramRouteInfo);
    Log.v("MediaRouter", ((StringBuilder)localObject1).toString());
    RouteInfo localRouteInfo = sStaticmSelectedRoute;
    if (sStatic.isBluetoothA2dpOn()) {
      localObject1 = sStaticmBluetoothA2dpRoute;
    } else {
      localObject1 = sStaticmDefaultAudioVideo;
    }
    Object localObject2 = sStaticmDefaultAudioVideo;
    int i = 0;
    int j;
    if ((localRouteInfo != localObject2) && (localRouteInfo != sStaticmBluetoothA2dpRoute)) {
      j = 0;
    } else {
      j = 1;
    }
    if ((localRouteInfo == paramRouteInfo) && ((j == 0) || (paramRouteInfo == localObject1))) {
      return;
    }
    if (!paramRouteInfo.matchesTypes(paramInt))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("selectRoute ignored; cannot select route with supported types ");
      ((StringBuilder)localObject1).append(typesToString(paramRouteInfo.getSupportedTypes()));
      ((StringBuilder)localObject1).append(" into route types ");
      ((StringBuilder)localObject1).append(typesToString(paramInt));
      Log.w("MediaRouter", ((StringBuilder)localObject1).toString());
      return;
    }
    localObject2 = sStaticmBluetoothA2dpRoute;
    if ((sStatic.isPlaybackActive()) && (localObject2 != null) && ((paramInt & 0x1) != 0) && ((paramRouteInfo == localObject2) || (paramRouteInfo == sStaticmDefaultAudioVideo))) {
      try
      {
        localObject1 = sStaticmAudioService;
        boolean bool;
        if (paramRouteInfo == localObject2) {
          bool = true;
        } else {
          bool = false;
        }
        ((IAudioService)localObject1).setBluetoothA2dpOn(bool);
        if (paramRouteInfo != localObject2)
        {
          StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
          localObject1 = new java/lang/StringBuffer;
          ((StringBuffer)localObject1).<init>();
          for (j = 3; j < arrayOfStackTraceElement.length; j++)
          {
            localObject2 = arrayOfStackTraceElement[j];
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append(((StackTraceElement)localObject2).getClassName());
            localStringBuilder.append(".");
            localStringBuilder.append(((StackTraceElement)localObject2).getMethodName());
            localStringBuilder.append(":");
            localStringBuilder.append(((StackTraceElement)localObject2).getLineNumber());
            ((StringBuffer)localObject1).append(localStringBuilder.toString());
            ((StringBuffer)localObject1).append("  ");
          }
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Default route is selected while a BT route is available: pkgName=");
          ((StringBuilder)localObject2).append(sStaticmPackageName);
          ((StringBuilder)localObject2).append(", callers=");
          ((StringBuilder)localObject2).append(((StringBuffer)localObject1).toString());
          Log.w("MediaRouter", ((StringBuilder)localObject2).toString());
        }
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("MediaRouter", "Error changing Bluetooth A2DP state", localRemoteException);
      }
    }
    WifiDisplay localWifiDisplay = sStaticmDisplayService.getWifiDisplayStatus().getActiveDisplay();
    if ((localRouteInfo != null) && (mDeviceAddress != null)) {
      j = 1;
    } else {
      j = 0;
    }
    if (mDeviceAddress != null) {
      i = 1;
    }
    if ((localWifiDisplay != null) || (j != 0) || (i != 0)) {
      if ((i != 0) && (!matchesDeviceAddress(localWifiDisplay, paramRouteInfo)))
      {
        if (sStaticmCanConfigureWifiDisplays) {
          sStaticmDisplayService.connectWifiDisplay(mDeviceAddress);
        } else {
          Log.e("MediaRouter", "Cannot connect to wifi displays because this process is not allowed to do so.");
        }
      }
      else if ((localWifiDisplay != null) && (i == 0)) {
        sStaticmDisplayService.disconnectWifiDisplay();
      }
    }
    sStatic.setSelectedRoute(paramRouteInfo, paramBoolean);
    if (localRouteInfo != null)
    {
      dispatchRouteUnselected(localRouteInfo.getSupportedTypes() & paramInt, localRouteInfo);
      if (localRouteInfo.resolveStatusCode()) {
        dispatchRouteChanged(localRouteInfo);
      }
    }
    if (paramRouteInfo != null)
    {
      if (paramRouteInfo.resolveStatusCode()) {
        dispatchRouteChanged(paramRouteInfo);
      }
      dispatchRouteSelected(paramRouteInfo.getSupportedTypes() & paramInt, paramRouteInfo);
    }
    sStatic.updateDiscoveryRequest();
  }
  
  private static boolean shouldShowWifiDisplay(WifiDisplay paramWifiDisplay1, WifiDisplay paramWifiDisplay2)
  {
    boolean bool;
    if ((!paramWifiDisplay1.isRemembered()) && (!paramWifiDisplay1.equals(paramWifiDisplay2))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static void systemVolumeChanged(int paramInt)
  {
    RouteInfo localRouteInfo = sStaticmSelectedRoute;
    if (localRouteInfo == null) {
      return;
    }
    if ((localRouteInfo != sStaticmBluetoothA2dpRoute) && (localRouteInfo != sStaticmDefaultAudioVideo))
    {
      if (sStaticmBluetoothA2dpRoute != null) {
        try
        {
          if (sStaticmAudioService.isBluetoothA2dpOn()) {
            localRouteInfo = sStaticmBluetoothA2dpRoute;
          } else {
            localRouteInfo = sStaticmDefaultAudioVideo;
          }
          dispatchRouteVolumeChanged(localRouteInfo);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Error checking Bluetooth A2DP state to report volume change", localRemoteException);
        }
      } else {
        dispatchRouteVolumeChanged(sStaticmDefaultAudioVideo);
      }
    }
    else {
      dispatchRouteVolumeChanged(localRemoteException);
    }
  }
  
  static String typesToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramInt & 0x1) != 0) {
      localStringBuilder.append("ROUTE_TYPE_LIVE_AUDIO ");
    }
    if ((paramInt & 0x2) != 0) {
      localStringBuilder.append("ROUTE_TYPE_LIVE_VIDEO ");
    }
    if ((paramInt & 0x4) != 0) {
      localStringBuilder.append("ROUTE_TYPE_REMOTE_DISPLAY ");
    }
    if ((0x800000 & paramInt) != 0) {
      localStringBuilder.append("ROUTE_TYPE_USER ");
    }
    return localStringBuilder.toString();
  }
  
  static void updateRoute(RouteInfo paramRouteInfo)
  {
    dispatchRouteChanged(paramRouteInfo);
  }
  
  private static void updateWifiDisplayRoute(RouteInfo paramRouteInfo, WifiDisplay paramWifiDisplay, WifiDisplayStatus paramWifiDisplayStatus, boolean paramBoolean)
  {
    int i = 0;
    String str = paramWifiDisplay.getFriendlyDisplayName();
    if (!paramRouteInfo.getName().equals(str))
    {
      mName = str;
      i = 1;
    }
    boolean bool = isWifiDisplayEnabled(paramWifiDisplay, paramWifiDisplayStatus);
    int j;
    if (mEnabled != bool) {
      j = 1;
    } else {
      j = 0;
    }
    mEnabled = bool;
    if ((i | j | paramRouteInfo.setRealStatusCode(getWifiDisplayStatusCode(paramWifiDisplay, paramWifiDisplayStatus))) != 0) {
      dispatchRouteChanged(paramRouteInfo);
    }
    if (((!bool) || (paramBoolean)) && (paramRouteInfo.isSelected())) {
      selectDefaultRouteStatic();
    }
  }
  
  static void updateWifiDisplayStatus(WifiDisplayStatus paramWifiDisplayStatus)
  {
    int i = paramWifiDisplayStatus.getFeatureState();
    String str1 = null;
    WifiDisplay[] arrayOfWifiDisplay;
    Object localObject1;
    Object localObject2;
    if (i == 3)
    {
      arrayOfWifiDisplay = paramWifiDisplayStatus.getDisplays();
      localObject1 = paramWifiDisplayStatus.getActiveDisplay();
      localObject2 = localObject1;
      if (!sStaticmCanConfigureWifiDisplays) {
        if (localObject1 != null)
        {
          arrayOfWifiDisplay = new WifiDisplay[] { localObject1 };
          localObject2 = localObject1;
        }
        else
        {
          arrayOfWifiDisplay = WifiDisplay.EMPTY_ARRAY;
          localObject2 = localObject1;
        }
      }
    }
    else
    {
      arrayOfWifiDisplay = WifiDisplay.EMPTY_ARRAY;
      localObject2 = null;
    }
    if (localObject2 != null) {
      str1 = localObject2.getDeviceAddress();
    }
    for (i = 0; i < arrayOfWifiDisplay.length; i++)
    {
      WifiDisplay localWifiDisplay = arrayOfWifiDisplay[i];
      if (shouldShowWifiDisplay(localWifiDisplay, localObject2))
      {
        localObject1 = findWifiDisplayRoute(localWifiDisplay);
        if (localObject1 == null)
        {
          localObject1 = makeWifiDisplayRoute(localWifiDisplay, paramWifiDisplayStatus);
          addRouteStatic((RouteInfo)localObject1);
        }
        else
        {
          String str2 = localWifiDisplay.getDeviceAddress();
          boolean bool;
          if ((!str2.equals(str1)) && (str2.equals(sStaticmPreviousActiveWifiDisplayAddress))) {
            bool = true;
          } else {
            bool = false;
          }
          updateWifiDisplayRoute((RouteInfo)localObject1, localWifiDisplay, paramWifiDisplayStatus, bool);
        }
        if (localWifiDisplay.equals(localObject2)) {
          selectRouteStatic(((RouteInfo)localObject1).getSupportedTypes(), (RouteInfo)localObject1, false);
        }
      }
    }
    int j;
    for (i = sStaticmRoutes.size();; i = j)
    {
      j = i - 1;
      if (i <= 0) {
        break;
      }
      localObject1 = (RouteInfo)sStaticmRoutes.get(j);
      if (mDeviceAddress != null)
      {
        paramWifiDisplayStatus = findWifiDisplay(arrayOfWifiDisplay, mDeviceAddress);
        if ((paramWifiDisplayStatus == null) || (!shouldShowWifiDisplay(paramWifiDisplayStatus, localObject2))) {
          removeRouteStatic((RouteInfo)localObject1);
        }
      }
    }
    sStaticmPreviousActiveWifiDisplayAddress = str1;
  }
  
  public void addCallback(int paramInt, Callback paramCallback)
  {
    addCallback(paramInt, paramCallback, 0);
  }
  
  public void addCallback(int paramInt1, Callback paramCallback, int paramInt2)
  {
    int i = findCallbackInfo(paramCallback);
    if (i >= 0)
    {
      paramCallback = (CallbackInfo)sStaticmCallbacks.get(i);
      type |= paramInt1;
      flags |= paramInt2;
    }
    else
    {
      paramCallback = new CallbackInfo(paramCallback, paramInt1, paramInt2, this);
      sStaticmCallbacks.add(paramCallback);
    }
    sStatic.updateDiscoveryRequest();
  }
  
  public void addRouteInt(RouteInfo paramRouteInfo)
  {
    addRouteStatic(paramRouteInfo);
  }
  
  public void addUserRoute(UserRouteInfo paramUserRouteInfo)
  {
    addRouteStatic(paramUserRouteInfo);
  }
  
  public void clearUserRoutes()
  {
    int j;
    for (int i = 0; i < sStaticmRoutes.size(); i = j + 1)
    {
      RouteInfo localRouteInfo = (RouteInfo)sStaticmRoutes.get(i);
      if (!(localRouteInfo instanceof UserRouteInfo))
      {
        j = i;
        if (!(localRouteInfo instanceof RouteGroup)) {}
      }
      else
      {
        removeRouteStatic(localRouteInfo);
        j = i - 1;
      }
    }
  }
  
  public RouteCategory createRouteCategory(int paramInt, boolean paramBoolean)
  {
    return new RouteCategory(paramInt, 8388608, paramBoolean);
  }
  
  public RouteCategory createRouteCategory(CharSequence paramCharSequence, boolean paramBoolean)
  {
    return new RouteCategory(paramCharSequence, 8388608, paramBoolean);
  }
  
  public UserRouteInfo createUserRoute(RouteCategory paramRouteCategory)
  {
    return new UserRouteInfo(paramRouteCategory);
  }
  
  public RouteCategory getCategoryAt(int paramInt)
  {
    return (RouteCategory)sStaticmCategories.get(paramInt);
  }
  
  public int getCategoryCount()
  {
    return sStaticmCategories.size();
  }
  
  public RouteInfo getDefaultRoute()
  {
    return sStaticmDefaultAudioVideo;
  }
  
  public RouteInfo getFallbackRoute()
  {
    RouteInfo localRouteInfo;
    if (sStaticmBluetoothA2dpRoute != null) {
      localRouteInfo = sStaticmBluetoothA2dpRoute;
    } else {
      localRouteInfo = sStaticmDefaultAudioVideo;
    }
    return localRouteInfo;
  }
  
  public RouteInfo getRouteAt(int paramInt)
  {
    return (RouteInfo)sStaticmRoutes.get(paramInt);
  }
  
  public int getRouteCount()
  {
    return sStaticmRoutes.size();
  }
  
  public RouteInfo getSelectedRoute()
  {
    return getSelectedRoute(8388615);
  }
  
  public RouteInfo getSelectedRoute(int paramInt)
  {
    if ((sStaticmSelectedRoute != null) && ((sStaticmSelectedRoute.mSupportedTypes & paramInt) != 0)) {
      return sStaticmSelectedRoute;
    }
    if (paramInt == 8388608) {
      return null;
    }
    return sStaticmDefaultAudioVideo;
  }
  
  public RouteCategory getSystemCategory()
  {
    return sStaticmSystemCategory;
  }
  
  public boolean isRouteAvailable(int paramInt1, int paramInt2)
  {
    int i = sStaticmRoutes.size();
    for (int j = 0; j < i; j++)
    {
      RouteInfo localRouteInfo = (RouteInfo)sStaticmRoutes.get(j);
      if ((localRouteInfo.matchesTypes(paramInt1)) && (((paramInt2 & 0x1) == 0) || (localRouteInfo != sStaticmDefaultAudioVideo))) {
        return true;
      }
    }
    return false;
  }
  
  public void rebindAsUser(int paramInt)
  {
    sStatic.rebindAsUser(paramInt);
  }
  
  public void removeCallback(Callback paramCallback)
  {
    int i = findCallbackInfo(paramCallback);
    if (i >= 0)
    {
      sStaticmCallbacks.remove(i);
      sStatic.updateDiscoveryRequest();
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("removeCallback(");
      localStringBuilder.append(paramCallback);
      localStringBuilder.append("): callback not registered");
      Log.w("MediaRouter", localStringBuilder.toString());
    }
  }
  
  public void removeRouteInt(RouteInfo paramRouteInfo)
  {
    removeRouteStatic(paramRouteInfo);
  }
  
  public void removeUserRoute(UserRouteInfo paramUserRouteInfo)
  {
    removeRouteStatic(paramUserRouteInfo);
  }
  
  public void selectRoute(int paramInt, RouteInfo paramRouteInfo)
  {
    if (paramRouteInfo != null)
    {
      selectRouteStatic(paramInt, paramRouteInfo, true);
      return;
    }
    throw new IllegalArgumentException("Route cannot be null.");
  }
  
  public void selectRouteInt(int paramInt, RouteInfo paramRouteInfo, boolean paramBoolean)
  {
    selectRouteStatic(paramInt, paramRouteInfo, paramBoolean);
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo);
    
    public abstract void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo);
    
    public abstract void onRouteGrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup, int paramInt);
    
    public void onRoutePresentationDisplayChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo) {}
    
    public abstract void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo);
    
    public abstract void onRouteSelected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo);
    
    public abstract void onRouteUngrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup);
    
    public abstract void onRouteUnselected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo);
    
    public abstract void onRouteVolumeChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo);
  }
  
  static class CallbackInfo
  {
    public final MediaRouter.Callback cb;
    public int flags;
    public final MediaRouter router;
    public int type;
    
    public CallbackInfo(MediaRouter.Callback paramCallback, int paramInt1, int paramInt2, MediaRouter paramMediaRouter)
    {
      cb = paramCallback;
      type = paramInt1;
      flags = paramInt2;
      router = paramMediaRouter;
    }
    
    public boolean filterRouteEvent(int paramInt)
    {
      boolean bool;
      if (((flags & 0x2) == 0) && ((type & paramInt) == 0)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean filterRouteEvent(MediaRouter.RouteInfo paramRouteInfo)
    {
      return filterRouteEvent(mSupportedTypes);
    }
  }
  
  public static class RouteCategory
  {
    final boolean mGroupable;
    boolean mIsSystem;
    CharSequence mName;
    int mNameResId;
    int mTypes;
    
    RouteCategory(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      mNameResId = paramInt1;
      mTypes = paramInt2;
      mGroupable = paramBoolean;
    }
    
    RouteCategory(CharSequence paramCharSequence, int paramInt, boolean paramBoolean)
    {
      mName = paramCharSequence;
      mTypes = paramInt;
      mGroupable = paramBoolean;
    }
    
    public CharSequence getName()
    {
      return getName(sStaticmResources);
    }
    
    public CharSequence getName(Context paramContext)
    {
      return getName(paramContext.getResources());
    }
    
    CharSequence getName(Resources paramResources)
    {
      if (mNameResId != 0) {
        return paramResources.getText(mNameResId);
      }
      return mName;
    }
    
    public List<MediaRouter.RouteInfo> getRoutes(List<MediaRouter.RouteInfo> paramList)
    {
      if (paramList == null) {
        paramList = new ArrayList();
      } else {
        paramList.clear();
      }
      int i = MediaRouter.getRouteCountStatic();
      for (int j = 0; j < i; j++)
      {
        MediaRouter.RouteInfo localRouteInfo = MediaRouter.getRouteAtStatic(j);
        if (mCategory == this) {
          paramList.add(localRouteInfo);
        }
      }
      return paramList;
    }
    
    public int getSupportedTypes()
    {
      return mTypes;
    }
    
    public boolean isGroupable()
    {
      return mGroupable;
    }
    
    public boolean isSystem()
    {
      return mIsSystem;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RouteCategory{ name=");
      localStringBuilder.append(getName());
      localStringBuilder.append(" types=");
      localStringBuilder.append(MediaRouter.typesToString(mTypes));
      localStringBuilder.append(" groupable=");
      localStringBuilder.append(mGroupable);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
  }
  
  public static class RouteGroup
    extends MediaRouter.RouteInfo
  {
    final ArrayList<MediaRouter.RouteInfo> mRoutes = new ArrayList();
    private boolean mUpdateName;
    
    RouteGroup(MediaRouter.RouteCategory paramRouteCategory)
    {
      super();
      mGroup = this;
      mVolumeHandling = 0;
    }
    
    public void addRoute(MediaRouter.RouteInfo paramRouteInfo)
    {
      if (paramRouteInfo.getGroup() == null)
      {
        if (paramRouteInfo.getCategory() == mCategory)
        {
          int i = mRoutes.size();
          mRoutes.add(paramRouteInfo);
          mGroup = this;
          mUpdateName = true;
          updateVolume();
          routeUpdated();
          MediaRouter.dispatchRouteGrouped(paramRouteInfo, this, i);
          return;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Route cannot be added to a group with a different category. (Route category=");
        localStringBuilder.append(paramRouteInfo.getCategory());
        localStringBuilder.append(" group category=");
        localStringBuilder.append(mCategory);
        localStringBuilder.append(")");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Route ");
      localStringBuilder.append(paramRouteInfo);
      localStringBuilder.append(" is already part of a group.");
      throw new IllegalStateException(localStringBuilder.toString());
    }
    
    public void addRoute(MediaRouter.RouteInfo paramRouteInfo, int paramInt)
    {
      if (paramRouteInfo.getGroup() == null)
      {
        if (paramRouteInfo.getCategory() == mCategory)
        {
          mRoutes.add(paramInt, paramRouteInfo);
          mGroup = this;
          mUpdateName = true;
          updateVolume();
          routeUpdated();
          MediaRouter.dispatchRouteGrouped(paramRouteInfo, this, paramInt);
          return;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Route cannot be added to a group with a different category. (Route category=");
        localStringBuilder.append(paramRouteInfo.getCategory());
        localStringBuilder.append(" group category=");
        localStringBuilder.append(mCategory);
        localStringBuilder.append(")");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Route ");
      localStringBuilder.append(paramRouteInfo);
      localStringBuilder.append(" is already part of a group.");
      throw new IllegalStateException(localStringBuilder.toString());
    }
    
    CharSequence getName(Resources paramResources)
    {
      if (mUpdateName) {
        updateName();
      }
      return super.getName(paramResources);
    }
    
    public MediaRouter.RouteInfo getRouteAt(int paramInt)
    {
      return (MediaRouter.RouteInfo)mRoutes.get(paramInt);
    }
    
    public int getRouteCount()
    {
      return mRoutes.size();
    }
    
    void memberNameChanged(MediaRouter.RouteInfo paramRouteInfo, CharSequence paramCharSequence)
    {
      mUpdateName = true;
      routeUpdated();
    }
    
    void memberStatusChanged(MediaRouter.RouteInfo paramRouteInfo, CharSequence paramCharSequence)
    {
      setStatusInt(paramCharSequence);
    }
    
    void memberVolumeChanged(MediaRouter.RouteInfo paramRouteInfo)
    {
      updateVolume();
    }
    
    public void removeRoute(int paramInt)
    {
      MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)mRoutes.remove(paramInt);
      mGroup = null;
      mUpdateName = true;
      updateVolume();
      MediaRouter.dispatchRouteUngrouped(localRouteInfo, this);
      routeUpdated();
    }
    
    public void removeRoute(MediaRouter.RouteInfo paramRouteInfo)
    {
      if (paramRouteInfo.getGroup() == this)
      {
        mRoutes.remove(paramRouteInfo);
        mGroup = null;
        mUpdateName = true;
        updateVolume();
        MediaRouter.dispatchRouteUngrouped(paramRouteInfo, this);
        routeUpdated();
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Route ");
      localStringBuilder.append(paramRouteInfo);
      localStringBuilder.append(" is not a member of this group.");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void requestSetVolume(int paramInt)
    {
      int i = getVolumeMax();
      if (i == 0) {
        return;
      }
      float f = paramInt / i;
      int j = getRouteCount();
      for (i = 0; i < j; i++)
      {
        MediaRouter.RouteInfo localRouteInfo = getRouteAt(i);
        localRouteInfo.requestSetVolume((int)(localRouteInfo.getVolumeMax() * f));
      }
      if (paramInt != mVolume)
      {
        mVolume = paramInt;
        MediaRouter.dispatchRouteVolumeChanged(this);
      }
    }
    
    public void requestUpdateVolume(int paramInt)
    {
      if (getVolumeMax() == 0) {
        return;
      }
      int i = getRouteCount();
      int j = 0;
      int k = 0;
      while (k < i)
      {
        MediaRouter.RouteInfo localRouteInfo = getRouteAt(k);
        localRouteInfo.requestUpdateVolume(paramInt);
        int m = localRouteInfo.getVolume();
        int n = j;
        if (m > j) {
          n = m;
        }
        k++;
        j = n;
      }
      if (j != mVolume)
      {
        mVolume = j;
        MediaRouter.dispatchRouteVolumeChanged(this);
      }
    }
    
    void routeUpdated()
    {
      int i = mRoutes.size();
      if (i == 0)
      {
        MediaRouter.removeRouteStatic(this);
        return;
      }
      int j = 1;
      int k = 1;
      int m = 0;
      int n = 0;
      int i1 = 0;
      Object localObject;
      for (;;)
      {
        int i2 = 1;
        if (i1 >= i) {
          break;
        }
        localObject = (MediaRouter.RouteInfo)mRoutes.get(i1);
        n |= mSupportedTypes;
        int i3 = ((MediaRouter.RouteInfo)localObject).getVolumeMax();
        int i4 = m;
        if (i3 > m) {
          i4 = i3;
        }
        if (((MediaRouter.RouteInfo)localObject).getPlaybackType() == 0) {
          m = 1;
        } else {
          m = 0;
        }
        k &= m;
        if (((MediaRouter.RouteInfo)localObject).getVolumeHandling() == 0) {
          m = i2;
        } else {
          m = 0;
        }
        j &= m;
        i1++;
        m = i4;
      }
      if (k != 0) {
        k = 0;
      } else {
        k = 1;
      }
      mPlaybackType = k;
      if (j != 0) {
        j = 0;
      } else {
        j = 1;
      }
      mVolumeHandling = j;
      mSupportedTypes = n;
      mVolumeMax = m;
      if (i == 1) {
        localObject = ((MediaRouter.RouteInfo)mRoutes.get(0)).getIconDrawable();
      } else {
        localObject = null;
      }
      mIcon = ((Drawable)localObject);
      super.routeUpdated();
    }
    
    public void setIconDrawable(Drawable paramDrawable)
    {
      mIcon = paramDrawable;
    }
    
    public void setIconResource(int paramInt)
    {
      setIconDrawable(sStaticmResources.getDrawable(paramInt));
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(super.toString());
      localStringBuilder.append('[');
      int i = mRoutes.size();
      for (int j = 0; j < i; j++)
      {
        if (j > 0) {
          localStringBuilder.append(", ");
        }
        localStringBuilder.append(mRoutes.get(j));
      }
      localStringBuilder.append(']');
      return localStringBuilder.toString();
    }
    
    void updateName()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = mRoutes.size();
      for (int j = 0; j < i; j++)
      {
        MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)mRoutes.get(j);
        if (j > 0) {
          localStringBuilder.append(", ");
        }
        localStringBuilder.append(localRouteInfo.getName());
      }
      mName = localStringBuilder.toString();
      mUpdateName = false;
    }
    
    void updateVolume()
    {
      int i = getRouteCount();
      int j = 0;
      int k = 0;
      while (k < i)
      {
        int m = getRouteAt(k).getVolume();
        int n = j;
        if (m > j) {
          n = m;
        }
        k++;
        j = n;
      }
      if (j != mVolume)
      {
        mVolume = j;
        MediaRouter.dispatchRouteVolumeChanged(this);
      }
    }
  }
  
  public static class RouteInfo
  {
    public static final int DEVICE_TYPE_BLUETOOTH = 3;
    public static final int DEVICE_TYPE_SPEAKER = 2;
    public static final int DEVICE_TYPE_TV = 1;
    public static final int DEVICE_TYPE_UNKNOWN = 0;
    public static final int PLAYBACK_TYPE_LOCAL = 0;
    public static final int PLAYBACK_TYPE_REMOTE = 1;
    public static final int PLAYBACK_VOLUME_FIXED = 0;
    public static final int PLAYBACK_VOLUME_VARIABLE = 1;
    public static final int STATUS_AVAILABLE = 3;
    public static final int STATUS_CONNECTED = 6;
    public static final int STATUS_CONNECTING = 2;
    public static final int STATUS_IN_USE = 5;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_NOT_AVAILABLE = 4;
    public static final int STATUS_SCANNING = 1;
    final MediaRouter.RouteCategory mCategory;
    CharSequence mDescription;
    String mDeviceAddress;
    int mDeviceType;
    boolean mEnabled = true;
    String mGlobalRouteId;
    MediaRouter.RouteGroup mGroup;
    Drawable mIcon;
    CharSequence mName;
    int mNameResId;
    int mPlaybackStream = 3;
    int mPlaybackType = 0;
    Display mPresentationDisplay;
    int mPresentationDisplayId = -1;
    private int mRealStatusCode;
    final IRemoteVolumeObserver.Stub mRemoteVolObserver = new IRemoteVolumeObserver.Stub()
    {
      public void dispatchRemoteVolumeUpdate(final int paramAnonymousInt1, final int paramAnonymousInt2)
      {
        sStaticmHandler.post(new Runnable()
        {
          public void run()
          {
            if (mVcb != null) {
              if (paramAnonymousInt1 != 0) {
                mVcb.vcb.onVolumeUpdateRequest(mVcb.route, paramAnonymousInt1);
              } else {
                mVcb.vcb.onVolumeSetRequest(mVcb.route, paramAnonymousInt2);
              }
            }
          }
        });
      }
    };
    private int mResolvedStatusCode;
    private CharSequence mStatus;
    int mSupportedTypes;
    private Object mTag;
    MediaRouter.VolumeCallbackInfo mVcb;
    int mVolume = 15;
    int mVolumeHandling = 1;
    int mVolumeMax = 15;
    
    RouteInfo(MediaRouter.RouteCategory paramRouteCategory)
    {
      mCategory = paramRouteCategory;
      mDeviceType = 0;
    }
    
    private Display choosePresentationDisplay()
    {
      if ((mSupportedTypes & 0x2) != 0)
      {
        Display[] arrayOfDisplay = MediaRouter.sStatic.getAllPresentationDisplays();
        int i = mPresentationDisplayId;
        int j = 0;
        int k = 0;
        Display localDisplay;
        if (i >= 0)
        {
          j = arrayOfDisplay.length;
          while (k < j)
          {
            localDisplay = arrayOfDisplay[k];
            if (localDisplay.getDisplayId() == mPresentationDisplayId) {
              return localDisplay;
            }
            k++;
          }
          return null;
        }
        if (mDeviceAddress != null)
        {
          i = arrayOfDisplay.length;
          for (k = j; k < i; k++)
          {
            localDisplay = arrayOfDisplay[k];
            if ((localDisplay.getType() == 3) && (mDeviceAddress.equals(localDisplay.getAddress()))) {
              return localDisplay;
            }
          }
          return null;
        }
        if ((this == sStaticmDefaultAudioVideo) && (arrayOfDisplay.length > 0)) {
          return arrayOfDisplay[0];
        }
      }
      return null;
    }
    
    public MediaRouter.RouteCategory getCategory()
    {
      return mCategory;
    }
    
    public CharSequence getDescription()
    {
      return mDescription;
    }
    
    public String getDeviceAddress()
    {
      return mDeviceAddress;
    }
    
    public int getDeviceType()
    {
      return mDeviceType;
    }
    
    public MediaRouter.RouteGroup getGroup()
    {
      return mGroup;
    }
    
    public Drawable getIconDrawable()
    {
      return mIcon;
    }
    
    public CharSequence getName()
    {
      return getName(sStaticmResources);
    }
    
    public CharSequence getName(Context paramContext)
    {
      return getName(paramContext.getResources());
    }
    
    CharSequence getName(Resources paramResources)
    {
      if (mNameResId != 0) {
        return paramResources.getText(mNameResId);
      }
      return mName;
    }
    
    public int getPlaybackStream()
    {
      return mPlaybackStream;
    }
    
    public int getPlaybackType()
    {
      return mPlaybackType;
    }
    
    public Display getPresentationDisplay()
    {
      return mPresentationDisplay;
    }
    
    public CharSequence getStatus()
    {
      return mStatus;
    }
    
    public int getStatusCode()
    {
      return mResolvedStatusCode;
    }
    
    public int getSupportedTypes()
    {
      return mSupportedTypes;
    }
    
    public Object getTag()
    {
      return mTag;
    }
    
    public int getVolume()
    {
      if (mPlaybackType == 0)
      {
        int i = 0;
        try
        {
          int j = sStaticmAudioService.getStreamVolume(mPlaybackStream);
          i = j;
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Error getting local stream volume", localRemoteException);
        }
        return i;
      }
      return mVolume;
    }
    
    public int getVolumeHandling()
    {
      return mVolumeHandling;
    }
    
    public int getVolumeMax()
    {
      if (mPlaybackType == 0)
      {
        int i = 0;
        int j;
        try
        {
          j = sStaticmAudioService.getStreamMaxVolume(mPlaybackStream);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Error getting local stream volume", localRemoteException);
          j = i;
        }
        return j;
      }
      return mVolumeMax;
    }
    
    public boolean isBluetooth()
    {
      boolean bool;
      if (this == sStaticmBluetoothA2dpRoute) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isConnecting()
    {
      boolean bool;
      if (mResolvedStatusCode == 2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isDefault()
    {
      boolean bool;
      if (this == sStaticmDefaultAudioVideo) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isEnabled()
    {
      return mEnabled;
    }
    
    public boolean isSelected()
    {
      boolean bool;
      if (this == sStaticmSelectedRoute) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean matchesTypes(int paramInt)
    {
      boolean bool;
      if ((mSupportedTypes & paramInt) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void requestSetVolume(int paramInt)
    {
      if (mPlaybackType == 0) {
        try
        {
          sStaticmAudioService.setStreamVolume(mPlaybackStream, paramInt, 0, ActivityThread.currentPackageName());
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Error setting local stream volume", localRemoteException);
        }
      } else {
        MediaRouter.sStatic.requestSetVolume(this, paramInt);
      }
    }
    
    public void requestUpdateVolume(int paramInt)
    {
      if (mPlaybackType == 0) {
        try
        {
          paramInt = Math.max(0, Math.min(getVolume() + paramInt, getVolumeMax()));
          sStaticmAudioService.setStreamVolume(mPlaybackStream, paramInt, 0, ActivityThread.currentPackageName());
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Error setting local stream volume", localRemoteException);
        }
      } else {
        MediaRouter.sStatic.requestUpdateVolume(this, paramInt);
      }
    }
    
    boolean resolveStatusCode()
    {
      int i = mRealStatusCode;
      int j = i;
      if (isSelected()) {
        if ((i != 1) && (i != 3)) {
          j = i;
        } else {
          j = 2;
        }
      }
      int k = mResolvedStatusCode;
      i = 0;
      if (k == j) {
        return false;
      }
      mResolvedStatusCode = j;
      switch (j)
      {
      default: 
        j = i;
        break;
      case 5: 
        j = 17040338;
        break;
      case 4: 
        j = 17040339;
        break;
      case 3: 
        j = 17040336;
        break;
      case 2: 
        j = 17040337;
        break;
      case 1: 
        j = 17040340;
      }
      CharSequence localCharSequence;
      if (j != 0) {
        localCharSequence = sStaticmResources.getText(j);
      } else {
        localCharSequence = null;
      }
      mStatus = localCharSequence;
      return true;
    }
    
    void routeUpdated()
    {
      MediaRouter.updateRoute(this);
    }
    
    public void select()
    {
      MediaRouter.selectRouteStatic(mSupportedTypes, this, true);
    }
    
    boolean setRealStatusCode(int paramInt)
    {
      if (mRealStatusCode != paramInt)
      {
        mRealStatusCode = paramInt;
        return resolveStatusCode();
      }
      return false;
    }
    
    void setStatusInt(CharSequence paramCharSequence)
    {
      if (!paramCharSequence.equals(mStatus))
      {
        mStatus = paramCharSequence;
        if (mGroup != null) {
          mGroup.memberStatusChanged(this, paramCharSequence);
        }
        routeUpdated();
      }
    }
    
    public void setTag(Object paramObject)
    {
      mTag = paramObject;
      routeUpdated();
    }
    
    public String toString()
    {
      String str = MediaRouter.typesToString(getSupportedTypes());
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(getClass().getSimpleName());
      localStringBuilder.append("{ name=");
      localStringBuilder.append(getName());
      localStringBuilder.append(", description=");
      localStringBuilder.append(getDescription());
      localStringBuilder.append(", status=");
      localStringBuilder.append(getStatus());
      localStringBuilder.append(", category=");
      localStringBuilder.append(getCategory());
      localStringBuilder.append(", supportedTypes=");
      localStringBuilder.append(str);
      localStringBuilder.append(", presentationDisplay=");
      localStringBuilder.append(mPresentationDisplay);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
    
    boolean updatePresentationDisplay()
    {
      Display localDisplay = choosePresentationDisplay();
      if (mPresentationDisplay != localDisplay)
      {
        mPresentationDisplay = localDisplay;
        return true;
      }
      return false;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface DeviceType {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface PlaybackType {}
    
    @Retention(RetentionPolicy.SOURCE)
    private static @interface PlaybackVolume {}
  }
  
  public static class SimpleCallback
    extends MediaRouter.Callback
  {
    public SimpleCallback() {}
    
    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo) {}
    
    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo) {}
    
    public void onRouteGrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup, int paramInt) {}
    
    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo) {}
    
    public void onRouteSelected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo) {}
    
    public void onRouteUngrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup) {}
    
    public void onRouteUnselected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo) {}
    
    public void onRouteVolumeChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo) {}
  }
  
  static class Static
    implements DisplayManager.DisplayListener
  {
    boolean mActivelyScanningWifiDisplays;
    final IAudioRoutesObserver.Stub mAudioRoutesObserver = new IAudioRoutesObserver.Stub()
    {
      public void dispatchAudioRoutesChanged(final AudioRoutesInfo paramAnonymousAudioRoutesInfo)
      {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            updateAudioRoutes(paramAnonymousAudioRoutesInfo);
          }
        });
      }
    };
    final IAudioService mAudioService;
    MediaRouter.RouteInfo mBluetoothA2dpRoute;
    final CopyOnWriteArrayList<MediaRouter.CallbackInfo> mCallbacks = new CopyOnWriteArrayList();
    final boolean mCanConfigureWifiDisplays;
    final ArrayList<MediaRouter.RouteCategory> mCategories = new ArrayList();
    IMediaRouterClient mClient;
    MediaRouterClientState mClientState;
    final AudioRoutesInfo mCurAudioRoutesInfo = new AudioRoutesInfo();
    int mCurrentUserId = -1;
    MediaRouter.RouteInfo mDefaultAudioVideo;
    boolean mDiscoverRequestActiveScan;
    int mDiscoveryRequestRouteTypes;
    final DisplayManager mDisplayService;
    final Handler mHandler;
    final IMediaRouterService mMediaRouterService;
    final String mPackageName;
    String mPreviousActiveWifiDisplayAddress;
    final Resources mResources;
    final ArrayList<MediaRouter.RouteInfo> mRoutes = new ArrayList();
    MediaRouter.RouteInfo mSelectedRoute;
    final MediaRouter.RouteCategory mSystemCategory;
    
    Static(Context paramContext)
    {
      mPackageName = paramContext.getPackageName();
      mResources = paramContext.getResources();
      mHandler = new Handler(paramContext.getMainLooper());
      mAudioService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
      mDisplayService = ((DisplayManager)paramContext.getSystemService("display"));
      mMediaRouterService = IMediaRouterService.Stub.asInterface(ServiceManager.getService("media_router"));
      boolean bool = false;
      mSystemCategory = new MediaRouter.RouteCategory(17039866, 3, false);
      mSystemCategory.mIsSystem = true;
      if (paramContext.checkPermission("android.permission.CONFIGURE_WIFI_DISPLAY", Process.myPid(), Process.myUid()) == 0) {
        bool = true;
      }
      mCanConfigureWifiDisplays = bool;
    }
    
    private void updatePresentationDisplays(int paramInt)
    {
      int i = mRoutes.size();
      for (int j = 0; j < i; j++)
      {
        MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)mRoutes.get(j);
        if ((localRouteInfo.updatePresentationDisplay()) || ((mPresentationDisplay != null) && (mPresentationDisplay.getDisplayId() == paramInt))) {
          MediaRouter.dispatchRoutePresentationDisplayChanged(localRouteInfo);
        }
      }
    }
    
    MediaRouter.RouteInfo findGlobalRoute(String paramString)
    {
      int i = mRoutes.size();
      for (int j = 0; j < i; j++)
      {
        MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)mRoutes.get(j);
        if (paramString.equals(mGlobalRouteId)) {
          return localRouteInfo;
        }
      }
      return null;
    }
    
    public Display[] getAllPresentationDisplays()
    {
      return mDisplayService.getDisplays("android.hardware.display.category.PRESENTATION");
    }
    
    boolean isBluetoothA2dpOn()
    {
      boolean bool1 = false;
      boolean bool2 = bool1;
      try
      {
        if (mBluetoothA2dpRoute != null)
        {
          boolean bool3 = mAudioService.isBluetoothA2dpOn();
          bool2 = bool1;
          if (bool3) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("MediaRouter", "Error querying Bluetooth A2DP state", localRemoteException);
      }
      return false;
    }
    
    boolean isPlaybackActive()
    {
      if (mClient != null) {
        try
        {
          boolean bool = mMediaRouterService.isPlaybackActive(mClient);
          return bool;
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Unable to retrieve playback active state.", localRemoteException);
        }
      }
      return false;
    }
    
    MediaRouter.RouteInfo makeGlobalRoute(MediaRouterClientState.RouteInfo paramRouteInfo)
    {
      MediaRouter.RouteInfo localRouteInfo = new MediaRouter.RouteInfo(mSystemCategory);
      mGlobalRouteId = id;
      mName = name;
      mDescription = description;
      mSupportedTypes = supportedTypes;
      mDeviceType = deviceType;
      mEnabled = enabled;
      localRouteInfo.setRealStatusCode(statusCode);
      mPlaybackType = playbackType;
      mPlaybackStream = playbackStream;
      mVolume = volume;
      mVolumeMax = volumeMax;
      mVolumeHandling = volumeHandling;
      mPresentationDisplayId = presentationDisplayId;
      localRouteInfo.updatePresentationDisplay();
      return localRouteInfo;
    }
    
    public void onDisplayAdded(int paramInt)
    {
      updatePresentationDisplays(paramInt);
    }
    
    public void onDisplayChanged(int paramInt)
    {
      updatePresentationDisplays(paramInt);
    }
    
    public void onDisplayRemoved(int paramInt)
    {
      updatePresentationDisplays(paramInt);
    }
    
    void publishClientDiscoveryRequest()
    {
      if (mClient != null) {
        try
        {
          mMediaRouterService.setDiscoveryRequest(mClient, mDiscoveryRequestRouteTypes, mDiscoverRequestActiveScan);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Unable to publish media router client discovery request.", localRemoteException);
        }
      }
    }
    
    void publishClientSelectedRoute(boolean paramBoolean)
    {
      if (mClient != null) {
        try
        {
          IMediaRouterService localIMediaRouterService = mMediaRouterService;
          IMediaRouterClient localIMediaRouterClient = mClient;
          String str;
          if (mSelectedRoute != null) {
            str = mSelectedRoute.mGlobalRouteId;
          } else {
            str = null;
          }
          localIMediaRouterService.setSelectedRoute(localIMediaRouterClient, str, paramBoolean);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Unable to publish media router client selected route.", localRemoteException);
        }
      }
    }
    
    void rebindAsUser(int paramInt)
    {
      if ((mCurrentUserId != paramInt) || (paramInt < 0) || (mClient == null))
      {
        if (mClient != null)
        {
          try
          {
            mMediaRouterService.unregisterClient(mClient);
          }
          catch (RemoteException localRemoteException1)
          {
            Log.e("MediaRouter", "Unable to unregister media router client.", localRemoteException1);
          }
          mClient = null;
        }
        mCurrentUserId = paramInt;
        try
        {
          Client localClient = new android/media/MediaRouter$Static$Client;
          localClient.<init>(this);
          mMediaRouterService.registerClientAsUser(localClient, mPackageName, paramInt);
          mClient = localClient;
        }
        catch (RemoteException localRemoteException2)
        {
          Log.e("MediaRouter", "Unable to register media router client.", localRemoteException2);
        }
        publishClientDiscoveryRequest();
        publishClientSelectedRoute(false);
        updateClientState();
      }
    }
    
    void requestSetVolume(MediaRouter.RouteInfo paramRouteInfo, int paramInt)
    {
      if ((mGlobalRouteId != null) && (mClient != null)) {
        try
        {
          mMediaRouterService.requestSetVolume(mClient, mGlobalRouteId, paramInt);
        }
        catch (RemoteException paramRouteInfo)
        {
          Log.w("MediaRouter", "Unable to request volume change.", paramRouteInfo);
        }
      }
    }
    
    void requestUpdateVolume(MediaRouter.RouteInfo paramRouteInfo, int paramInt)
    {
      if ((mGlobalRouteId != null) && (mClient != null)) {
        try
        {
          mMediaRouterService.requestUpdateVolume(mClient, mGlobalRouteId, paramInt);
        }
        catch (RemoteException paramRouteInfo)
        {
          Log.w("MediaRouter", "Unable to request volume change.", paramRouteInfo);
        }
      }
    }
    
    void setSelectedRoute(MediaRouter.RouteInfo paramRouteInfo, boolean paramBoolean)
    {
      mSelectedRoute = paramRouteInfo;
      publishClientSelectedRoute(paramBoolean);
    }
    
    void startMonitoringRoutes(Context paramContext)
    {
      mDefaultAudioVideo = new MediaRouter.RouteInfo(mSystemCategory);
      mDefaultAudioVideo.mNameResId = 17039867;
      mDefaultAudioVideo.mSupportedTypes = 3;
      mDefaultAudioVideo.updatePresentationDisplay();
      if (((AudioManager)paramContext.getSystemService("audio")).isVolumeFixed()) {
        mDefaultAudioVideo.mVolumeHandling = 0;
      }
      MediaRouter.addRouteStatic(mDefaultAudioVideo);
      MediaRouter.updateWifiDisplayStatus(mDisplayService.getWifiDisplayStatus());
      paramContext.registerReceiver(new MediaRouter.WifiDisplayStatusChangedReceiver(), new IntentFilter("android.hardware.display.action.WIFI_DISPLAY_STATUS_CHANGED"));
      paramContext.registerReceiver(new MediaRouter.VolumeChangeReceiver(), new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
      mDisplayService.registerDisplayListener(this, mHandler);
      paramContext = null;
      try
      {
        AudioRoutesInfo localAudioRoutesInfo = mAudioService.startWatchingRoutes(mAudioRoutesObserver);
        paramContext = localAudioRoutesInfo;
      }
      catch (RemoteException localRemoteException) {}
      if (paramContext != null) {
        updateAudioRoutes(paramContext);
      }
      rebindAsUser(UserHandle.myUserId());
      if (mSelectedRoute == null) {
        MediaRouter.selectDefaultRouteStatic();
      }
    }
    
    void updateAudioRoutes(AudioRoutesInfo paramAudioRoutesInfo)
    {
      int i = 0;
      int j = 0;
      int k = 0;
      if (mainType != mCurAudioRoutesInfo.mainType)
      {
        mCurAudioRoutesInfo.mainType = mainType;
        if (((mainType & 0x2) == 0) && ((mainType & 0x1) == 0))
        {
          if ((mainType & 0x4) != 0) {
            j = 17039868;
          } else if ((mainType & 0x8) != 0) {
            j = 17039869;
          } else if ((mainType & 0x10) != 0) {
            j = 17039871;
          } else {
            j = 17039867;
          }
        }
        else {
          j = 17039870;
        }
        mDefaultAudioVideo.mNameResId = j;
        MediaRouter.dispatchRouteChanged(mDefaultAudioVideo);
        j = k;
        if ((mainType & 0x13) != 0) {
          j = 1;
        }
        i = 1;
      }
      Object localObject;
      if (!TextUtils.equals(bluetoothName, mCurAudioRoutesInfo.bluetoothName))
      {
        j = 0;
        mCurAudioRoutesInfo.bluetoothName = bluetoothName;
        if (mCurAudioRoutesInfo.bluetoothName != null)
        {
          if (mBluetoothA2dpRoute == null)
          {
            localObject = new MediaRouter.RouteInfo(mSystemCategory);
            mName = mCurAudioRoutesInfo.bluetoothName;
            mDescription = mResources.getText(17039614);
            mSupportedTypes = 1;
            mDeviceType = 3;
            mBluetoothA2dpRoute = ((MediaRouter.RouteInfo)localObject);
            MediaRouter.addRouteStatic(mBluetoothA2dpRoute);
          }
          else
          {
            mBluetoothA2dpRoute.mName = mCurAudioRoutesInfo.bluetoothName;
            MediaRouter.dispatchRouteChanged(mBluetoothA2dpRoute);
          }
        }
        else if (mBluetoothA2dpRoute != null)
        {
          MediaRouter.removeRouteStatic(mBluetoothA2dpRoute);
          mBluetoothA2dpRoute = null;
        }
        i = 1;
      }
      if (i != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Audio routes updated: ");
        ((StringBuilder)localObject).append(paramAudioRoutesInfo);
        ((StringBuilder)localObject).append(", a2dp=");
        ((StringBuilder)localObject).append(isBluetoothA2dpOn());
        Log.v("MediaRouter", ((StringBuilder)localObject).toString());
        if ((mSelectedRoute == null) || (mSelectedRoute == mDefaultAudioVideo) || (mSelectedRoute == mBluetoothA2dpRoute)) {
          if ((j == 0) && (mBluetoothA2dpRoute != null)) {
            MediaRouter.selectRouteStatic(1, mBluetoothA2dpRoute, false);
          } else {
            MediaRouter.selectRouteStatic(1, mDefaultAudioVideo, false);
          }
        }
      }
    }
    
    void updateClientState()
    {
      ArrayList localArrayList = null;
      mClientState = null;
      if (mClient != null) {
        try
        {
          mClientState = mMediaRouterService.getState(mClient);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("MediaRouter", "Unable to retrieve media router client state.", localRemoteException);
        }
      }
      if (mClientState != null) {
        localArrayList = mClientState.routes;
      }
      int i;
      if (localArrayList != null) {
        i = localArrayList.size();
      } else {
        i = 0;
      }
      Object localObject1;
      Object localObject2;
      for (int j = 0; j < i; j++)
      {
        localObject1 = (MediaRouterClientState.RouteInfo)localArrayList.get(j);
        localObject2 = findGlobalRoute(id);
        if (localObject2 == null) {
          MediaRouter.addRouteStatic(makeGlobalRoute((MediaRouterClientState.RouteInfo)localObject1));
        } else {
          updateGlobalRoute((MediaRouter.RouteInfo)localObject2, (MediaRouterClientState.RouteInfo)localObject1);
        }
      }
      int k;
      label218:
      for (j = mRoutes.size();; j = k)
      {
        k = j - 1;
        if (j <= 0) {
          break;
        }
        localObject1 = (MediaRouter.RouteInfo)mRoutes.get(k);
        localObject2 = mGlobalRouteId;
        if (localObject2 != null)
        {
          for (j = 0; j < i; j++) {
            if (((String)localObject2).equals(getid)) {
              break label218;
            }
          }
          MediaRouter.removeRouteStatic((MediaRouter.RouteInfo)localObject1);
        }
      }
    }
    
    void updateDiscoveryRequest()
    {
      boolean bool1 = false;
      int i = mCallbacks.size();
      int j = 0;
      int k = 0;
      int m = 0;
      int n = 0;
      while (n < i)
      {
        MediaRouter.CallbackInfo localCallbackInfo = (MediaRouter.CallbackInfo)mCallbacks.get(n);
        if ((flags & 0x5) != 0) {
          m |= type;
        } else if ((flags & 0x8) != 0) {
          k |= type;
        } else {
          m |= type;
        }
        int i1 = j;
        if ((0x1 & flags) != 0)
        {
          boolean bool2 = true;
          bool1 = bool2;
          i1 = j;
          if ((0x4 & type) != 0)
          {
            i1 = 1;
            bool1 = bool2;
          }
        }
        n++;
        j = i1;
      }
      if (m == 0)
      {
        n = m;
        if (!bool1) {}
      }
      else
      {
        n = m | k;
      }
      if (mCanConfigureWifiDisplays)
      {
        m = j;
        if (mSelectedRoute != null)
        {
          m = j;
          if (mSelectedRoute.matchesTypes(4)) {
            m = 0;
          }
        }
        if (m != 0)
        {
          if (!mActivelyScanningWifiDisplays)
          {
            mActivelyScanningWifiDisplays = true;
            mDisplayService.startWifiDisplayScan();
          }
        }
        else if (mActivelyScanningWifiDisplays)
        {
          mActivelyScanningWifiDisplays = false;
          mDisplayService.stopWifiDisplayScan();
        }
      }
      if ((n != mDiscoveryRequestRouteTypes) || (bool1 != mDiscoverRequestActiveScan))
      {
        mDiscoveryRequestRouteTypes = n;
        mDiscoverRequestActiveScan = bool1;
        publishClientDiscoveryRequest();
      }
    }
    
    void updateGlobalRoute(MediaRouter.RouteInfo paramRouteInfo, MediaRouterClientState.RouteInfo paramRouteInfo1)
    {
      int i = 0;
      int j = 0;
      int k = 0;
      if (!Objects.equals(mName, name))
      {
        mName = name;
        i = 1;
      }
      if (!Objects.equals(mDescription, description))
      {
        mDescription = description;
        i = 1;
      }
      int m = mSupportedTypes;
      if (m != supportedTypes)
      {
        mSupportedTypes = supportedTypes;
        i = 1;
      }
      if (mEnabled != enabled)
      {
        mEnabled = enabled;
        i = 1;
      }
      if (mRealStatusCode != statusCode)
      {
        paramRouteInfo.setRealStatusCode(statusCode);
        i = 1;
      }
      if (mPlaybackType != playbackType)
      {
        mPlaybackType = playbackType;
        i = 1;
      }
      if (mPlaybackStream != playbackStream)
      {
        mPlaybackStream = playbackStream;
        i = 1;
      }
      int n = i;
      i = j;
      if (mVolume != volume)
      {
        mVolume = volume;
        n = 1;
        i = 1;
      }
      if (mVolumeMax != volumeMax)
      {
        mVolumeMax = volumeMax;
        n = 1;
        i = 1;
      }
      if (mVolumeHandling != volumeHandling)
      {
        mVolumeHandling = volumeHandling;
        n = 1;
        i = 1;
      }
      if (mPresentationDisplayId != presentationDisplayId)
      {
        mPresentationDisplayId = presentationDisplayId;
        paramRouteInfo.updatePresentationDisplay();
        n = 1;
        k = 1;
      }
      if (n != 0) {
        MediaRouter.dispatchRouteChanged(paramRouteInfo, m);
      }
      if (i != 0) {
        MediaRouter.dispatchRouteVolumeChanged(paramRouteInfo);
      }
      if (k != 0) {
        MediaRouter.dispatchRoutePresentationDisplayChanged(paramRouteInfo);
      }
    }
    
    final class Client
      extends IMediaRouterClient.Stub
    {
      Client() {}
      
      public void onRestoreRoute()
      {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            if ((MediaRouter.Static.Client.this == mClient) && (mSelectedRoute != null) && ((mSelectedRoute == mDefaultAudioVideo) || (mSelectedRoute == mBluetoothA2dpRoute)))
            {
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("onRestoreRoute() : route=");
              localStringBuilder.append(mSelectedRoute);
              Log.v("MediaRouter", localStringBuilder.toString());
              mSelectedRoute.select();
              return;
            }
          }
        });
      }
      
      public void onStateChanged()
      {
        mHandler.post(new Runnable()
        {
          public void run()
          {
            if (MediaRouter.Static.Client.this == mClient) {
              updateClientState();
            }
          }
        });
      }
    }
  }
  
  public static class UserRouteInfo
    extends MediaRouter.RouteInfo
  {
    RemoteControlClient mRcc;
    SessionVolumeProvider mSvp;
    
    UserRouteInfo(MediaRouter.RouteCategory paramRouteCategory)
    {
      super();
      mSupportedTypes = 8388608;
      mPlaybackType = 1;
      mVolumeHandling = 0;
    }
    
    private void configureSessionVolume()
    {
      Object localObject;
      if (mRcc == null)
      {
        if (MediaRouter.DEBUG)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("No Rcc to configure volume for route ");
          ((StringBuilder)localObject).append(getName());
          Log.d("MediaRouter", ((StringBuilder)localObject).toString());
        }
        return;
      }
      MediaSession localMediaSession = mRcc.getMediaSession();
      if (localMediaSession == null)
      {
        if (MediaRouter.DEBUG) {
          Log.d("MediaRouter", "Rcc has no session to configure volume");
        }
        return;
      }
      if (mPlaybackType == 1)
      {
        int i = 0;
        if (mVolumeHandling == 1) {
          i = 2;
        }
        if ((mSvp == null) || (mSvp.getVolumeControl() != i) || (mSvp.getMaxVolume() != mVolumeMax))
        {
          mSvp = new SessionVolumeProvider(i, mVolumeMax, mVolume);
          localMediaSession.setPlaybackToRemote(mSvp);
        }
      }
      else
      {
        localObject = new AudioAttributes.Builder();
        ((AudioAttributes.Builder)localObject).setLegacyStreamType(mPlaybackStream);
        localMediaSession.setPlaybackToLocal(((AudioAttributes.Builder)localObject).build());
        mSvp = null;
      }
    }
    
    private void updatePlaybackInfoOnRcc()
    {
      configureSessionVolume();
    }
    
    public RemoteControlClient getRemoteControlClient()
    {
      return mRcc;
    }
    
    public void requestSetVolume(int paramInt)
    {
      if (mVolumeHandling == 1)
      {
        if (mVcb == null)
        {
          Log.e("MediaRouter", "Cannot requestSetVolume on user route - no volume callback set");
          return;
        }
        mVcb.vcb.onVolumeSetRequest(this, paramInt);
      }
    }
    
    public void requestUpdateVolume(int paramInt)
    {
      if (mVolumeHandling == 1)
      {
        if (mVcb == null)
        {
          Log.e("MediaRouter", "Cannot requestChangeVolume on user route - no volumec callback set");
          return;
        }
        mVcb.vcb.onVolumeUpdateRequest(this, paramInt);
      }
    }
    
    public void setDescription(CharSequence paramCharSequence)
    {
      mDescription = paramCharSequence;
      routeUpdated();
    }
    
    public void setIconDrawable(Drawable paramDrawable)
    {
      mIcon = paramDrawable;
    }
    
    public void setIconResource(int paramInt)
    {
      setIconDrawable(sStaticmResources.getDrawable(paramInt));
    }
    
    public void setName(int paramInt)
    {
      mNameResId = paramInt;
      mName = null;
      routeUpdated();
    }
    
    public void setName(CharSequence paramCharSequence)
    {
      mNameResId = 0;
      mName = paramCharSequence;
      routeUpdated();
    }
    
    public void setPlaybackStream(int paramInt)
    {
      if (mPlaybackStream != paramInt)
      {
        mPlaybackStream = paramInt;
        configureSessionVolume();
      }
    }
    
    public void setPlaybackType(int paramInt)
    {
      if (mPlaybackType != paramInt)
      {
        mPlaybackType = paramInt;
        configureSessionVolume();
      }
    }
    
    public void setRemoteControlClient(RemoteControlClient paramRemoteControlClient)
    {
      mRcc = paramRemoteControlClient;
      updatePlaybackInfoOnRcc();
    }
    
    public void setStatus(CharSequence paramCharSequence)
    {
      setStatusInt(paramCharSequence);
    }
    
    public void setVolume(int paramInt)
    {
      paramInt = Math.max(0, Math.min(paramInt, getVolumeMax()));
      if (mVolume != paramInt)
      {
        mVolume = paramInt;
        if (mSvp != null) {
          mSvp.setCurrentVolume(mVolume);
        }
        MediaRouter.dispatchRouteVolumeChanged(this);
        if (mGroup != null) {
          mGroup.memberVolumeChanged(this);
        }
      }
    }
    
    public void setVolumeCallback(MediaRouter.VolumeCallback paramVolumeCallback)
    {
      mVcb = new MediaRouter.VolumeCallbackInfo(paramVolumeCallback, this);
    }
    
    public void setVolumeHandling(int paramInt)
    {
      if (mVolumeHandling != paramInt)
      {
        mVolumeHandling = paramInt;
        configureSessionVolume();
      }
    }
    
    public void setVolumeMax(int paramInt)
    {
      if (mVolumeMax != paramInt)
      {
        mVolumeMax = paramInt;
        configureSessionVolume();
      }
    }
    
    class SessionVolumeProvider
      extends VolumeProvider
    {
      public SessionVolumeProvider(int paramInt1, int paramInt2, int paramInt3)
      {
        super(paramInt2, paramInt3);
      }
      
      public void onAdjustVolume(final int paramInt)
      {
        sStaticmHandler.post(new Runnable()
        {
          public void run()
          {
            if (mVcb != null) {
              mVcb.vcb.onVolumeUpdateRequest(mVcb.route, paramInt);
            }
          }
        });
      }
      
      public void onSetVolumeTo(final int paramInt)
      {
        sStaticmHandler.post(new Runnable()
        {
          public void run()
          {
            if (mVcb != null) {
              mVcb.vcb.onVolumeSetRequest(mVcb.route, paramInt);
            }
          }
        });
      }
    }
  }
  
  public static abstract class VolumeCallback
  {
    public VolumeCallback() {}
    
    public abstract void onVolumeSetRequest(MediaRouter.RouteInfo paramRouteInfo, int paramInt);
    
    public abstract void onVolumeUpdateRequest(MediaRouter.RouteInfo paramRouteInfo, int paramInt);
  }
  
  static class VolumeCallbackInfo
  {
    public final MediaRouter.RouteInfo route;
    public final MediaRouter.VolumeCallback vcb;
    
    public VolumeCallbackInfo(MediaRouter.VolumeCallback paramVolumeCallback, MediaRouter.RouteInfo paramRouteInfo)
    {
      vcb = paramVolumeCallback;
      route = paramRouteInfo;
    }
  }
  
  static class VolumeChangeReceiver
    extends BroadcastReceiver
  {
    VolumeChangeReceiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("android.media.VOLUME_CHANGED_ACTION"))
      {
        if (paramIntent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1) != 3) {
          return;
        }
        int i = paramIntent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
        if (i != paramIntent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0)) {
          MediaRouter.systemVolumeChanged(i);
        }
      }
    }
  }
  
  static class WifiDisplayStatusChangedReceiver
    extends BroadcastReceiver
  {
    WifiDisplayStatusChangedReceiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("android.hardware.display.action.WIFI_DISPLAY_STATUS_CHANGED")) {
        MediaRouter.updateWifiDisplayStatus((WifiDisplayStatus)paramIntent.getParcelableExtra("android.hardware.display.extra.WIFI_DISPLAY_STATUS"));
      }
    }
  }
}
