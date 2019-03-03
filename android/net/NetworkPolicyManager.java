package android.net;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.DebugUtils;
import android.util.Pair;
import android.util.Range;
import java.time.ZonedDateTime;
import java.util.Iterator;

public class NetworkPolicyManager
{
  private static final boolean ALLOW_PLATFORM_APP_POLICY = true;
  public static final String EXTRA_NETWORK_TEMPLATE = "android.net.NETWORK_TEMPLATE";
  public static final int FIREWALL_CHAIN_DOZABLE = 1;
  public static final String FIREWALL_CHAIN_NAME_DOZABLE = "dozable";
  public static final String FIREWALL_CHAIN_NAME_NONE = "none";
  public static final String FIREWALL_CHAIN_NAME_POWERSAVE = "powersave";
  public static final String FIREWALL_CHAIN_NAME_STANDBY = "standby";
  public static final int FIREWALL_CHAIN_NONE = 0;
  public static final int FIREWALL_CHAIN_POWERSAVE = 3;
  public static final int FIREWALL_CHAIN_STANDBY = 2;
  public static final int FIREWALL_RULE_ALLOW = 1;
  public static final int FIREWALL_RULE_DEFAULT = 0;
  public static final int FIREWALL_RULE_DENY = 2;
  public static final int FIREWALL_TYPE_BLACKLIST = 1;
  public static final int FIREWALL_TYPE_WHITELIST = 0;
  public static final int FOREGROUND_THRESHOLD_STATE = 4;
  public static final int MASK_ALL_NETWORKS = 240;
  public static final int MASK_METERED_NETWORKS = 15;
  public static final int OVERRIDE_CONGESTED = 2;
  public static final int OVERRIDE_UNMETERED = 1;
  public static final int POLICY_ALLOW_METERED_BACKGROUND = 4;
  public static final int POLICY_NONE = 0;
  public static final int POLICY_REJECT_METERED_BACKGROUND = 1;
  public static final int RULE_ALLOW_ALL = 32;
  public static final int RULE_ALLOW_METERED = 1;
  public static final int RULE_NONE = 0;
  public static final int RULE_REJECT_ALL = 64;
  public static final int RULE_REJECT_METERED = 4;
  public static final int RULE_TEMPORARY_ALLOW_METERED = 2;
  private final Context mContext;
  private INetworkPolicyManager mService;
  
  public NetworkPolicyManager(Context paramContext, INetworkPolicyManager paramINetworkPolicyManager)
  {
    if (paramINetworkPolicyManager != null)
    {
      mContext = paramContext;
      mService = paramINetworkPolicyManager;
      return;
    }
    throw new IllegalArgumentException("missing INetworkPolicyManager");
  }
  
  @Deprecated
  public static Iterator<Pair<ZonedDateTime, ZonedDateTime>> cycleIterator(NetworkPolicy paramNetworkPolicy)
  {
    new Iterator()
    {
      public boolean hasNext()
      {
        return NetworkPolicyManager.this.hasNext();
      }
      
      public Pair<ZonedDateTime, ZonedDateTime> next()
      {
        if (hasNext())
        {
          Range localRange = (Range)NetworkPolicyManager.this.next();
          return Pair.create((ZonedDateTime)localRange.getLower(), (ZonedDateTime)localRange.getUpper());
        }
        return Pair.create(null, null);
      }
    };
  }
  
  public static NetworkPolicyManager from(Context paramContext)
  {
    return (NetworkPolicyManager)paramContext.getSystemService("netpolicy");
  }
  
  public static boolean isProcStateAllowedWhileIdleOrPowerSaveMode(int paramInt)
  {
    boolean bool;
    if (paramInt <= 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isProcStateAllowedWhileOnRestrictBackground(int paramInt)
  {
    boolean bool;
    if (paramInt <= 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public static boolean isUidValidForPolicy(Context paramContext, int paramInt)
  {
    return UserHandle.isApp(paramInt);
  }
  
  public static String resolveNetworkId(WifiConfiguration paramWifiConfiguration)
  {
    if (paramWifiConfiguration.isPasspoint()) {
      paramWifiConfiguration = providerFriendlyName;
    } else {
      paramWifiConfiguration = SSID;
    }
    return WifiInfo.removeDoubleQuotes(paramWifiConfiguration);
  }
  
  public static String resolveNetworkId(String paramString)
  {
    return WifiInfo.removeDoubleQuotes(paramString);
  }
  
  public static String uidPoliciesToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramInt);
    localStringBuilder = localStringBuilder.append(" (");
    if (paramInt == 0) {
      localStringBuilder.append("NONE");
    } else {
      localStringBuilder.append(DebugUtils.flagsToString(NetworkPolicyManager.class, "POLICY_", paramInt));
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public static String uidRulesToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramInt);
    localStringBuilder = localStringBuilder.append(" (");
    if (paramInt == 0) {
      localStringBuilder.append("NONE");
    } else {
      localStringBuilder.append(DebugUtils.flagsToString(NetworkPolicyManager.class, "RULE_", paramInt));
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void addUidPolicy(int paramInt1, int paramInt2)
  {
    try
    {
      mService.addUidPolicy(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void factoryReset(String paramString)
  {
    try
    {
      mService.factoryReset(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public NetworkPolicy[] getNetworkPolicies()
  {
    try
    {
      NetworkPolicy[] arrayOfNetworkPolicy = mService.getNetworkPolicies(mContext.getOpPackageName());
      return arrayOfNetworkPolicy;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean getRestrictBackground()
  {
    try
    {
      boolean bool = mService.getRestrictBackground();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getUidPolicy(int paramInt)
  {
    try
    {
      paramInt = mService.getUidPolicy(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int[] getUidsWithPolicy(int paramInt)
  {
    try
    {
      int[] arrayOfInt = mService.getUidsWithPolicy(paramInt);
      return arrayOfInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void registerListener(INetworkPolicyListener paramINetworkPolicyListener)
  {
    try
    {
      mService.registerListener(paramINetworkPolicyListener);
      return;
    }
    catch (RemoteException paramINetworkPolicyListener)
    {
      throw paramINetworkPolicyListener.rethrowFromSystemServer();
    }
  }
  
  public void removeUidPolicy(int paramInt1, int paramInt2)
  {
    try
    {
      mService.removeUidPolicy(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setNetworkPolicies(NetworkPolicy[] paramArrayOfNetworkPolicy)
  {
    try
    {
      mService.setNetworkPolicies(paramArrayOfNetworkPolicy);
      return;
    }
    catch (RemoteException paramArrayOfNetworkPolicy)
    {
      throw paramArrayOfNetworkPolicy.rethrowFromSystemServer();
    }
  }
  
  public void setRestrictBackground(boolean paramBoolean)
  {
    try
    {
      mService.setRestrictBackground(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setUidPolicy(int paramInt1, int paramInt2)
  {
    try
    {
      mService.setUidPolicy(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void unregisterListener(INetworkPolicyListener paramINetworkPolicyListener)
  {
    try
    {
      mService.unregisterListener(paramINetworkPolicyListener);
      return;
    }
    catch (RemoteException paramINetworkPolicyListener)
    {
      throw paramINetworkPolicyListener.rethrowFromSystemServer();
    }
  }
  
  public static class Listener
    extends INetworkPolicyListener.Stub
  {
    public Listener() {}
    
    public void onMeteredIfacesChanged(String[] paramArrayOfString) {}
    
    public void onRestrictBackgroundChanged(boolean paramBoolean) {}
    
    public void onSubscriptionOverride(int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onUidPoliciesChanged(int paramInt1, int paramInt2) {}
    
    public void onUidRulesChanged(int paramInt1, int paramInt2) {}
  }
}
