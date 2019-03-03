package android.net;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import java.util.Objects;

public class NetworkIdentity
  implements Comparable<NetworkIdentity>
{
  @Deprecated
  public static final boolean COMBINE_SUBTYPE_ENABLED = true;
  public static final int SUBTYPE_COMBINED = -1;
  private static final String TAG = "NetworkIdentity";
  final boolean mDefaultNetwork;
  final boolean mMetered;
  final String mNetworkId;
  final boolean mRoaming;
  final int mSubType;
  final String mSubscriberId;
  final int mType;
  
  public NetworkIdentity(int paramInt1, int paramInt2, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    mType = paramInt1;
    mSubType = -1;
    mSubscriberId = paramString1;
    mNetworkId = paramString2;
    mRoaming = paramBoolean1;
    mMetered = paramBoolean2;
    mDefaultNetwork = paramBoolean3;
  }
  
  public static NetworkIdentity buildNetworkIdentity(Context paramContext, NetworkState paramNetworkState, boolean paramBoolean)
  {
    int i = networkInfo.getType();
    int j = networkInfo.getSubtype();
    Object localObject1 = null;
    Object localObject2 = null;
    boolean bool1 = networkCapabilities.hasCapability(18);
    boolean bool2 = networkCapabilities.hasCapability(11);
    Object localObject3;
    if (ConnectivityManager.isNetworkTypeMobile(i))
    {
      if ((subscriberId == null) && (networkInfo.getState() != NetworkInfo.State.DISCONNECTED) && (networkInfo.getState() != NetworkInfo.State.UNKNOWN))
      {
        paramContext = new StringBuilder();
        paramContext.append("Active mobile network without subscriber! ni = ");
        paramContext.append(networkInfo);
        Slog.w("NetworkIdentity", paramContext.toString());
      }
      localObject3 = subscriberId;
    }
    else
    {
      localObject3 = localObject1;
      if (i == 1) {
        if (networkId != null)
        {
          localObject2 = networkId;
          localObject3 = localObject1;
        }
        else
        {
          paramContext = ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo();
          if (paramContext != null) {
            paramContext = paramContext.getSSID();
          } else {
            paramContext = null;
          }
          localObject2 = paramContext;
          localObject3 = localObject1;
        }
      }
    }
    return new NetworkIdentity(i, j, (String)localObject3, (String)localObject2, bool1 ^ true, bool2 ^ true, paramBoolean);
  }
  
  public static String scrubSubscriberId(String paramString)
  {
    if (Build.IS_ENG) {
      return paramString;
    }
    if (paramString != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString.substring(0, Math.min(6, paramString.length())));
      localStringBuilder.append("...");
      return localStringBuilder.toString();
    }
    return "null";
  }
  
  public static String[] scrubSubscriberId(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null) {
      return null;
    }
    String[] arrayOfString = new String[paramArrayOfString.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = scrubSubscriberId(paramArrayOfString[i]);
    }
    return arrayOfString;
  }
  
  public int compareTo(NetworkIdentity paramNetworkIdentity)
  {
    int i = Integer.compare(mType, mType);
    int j = i;
    if (i == 0) {
      j = Integer.compare(mSubType, mSubType);
    }
    i = j;
    if (j == 0)
    {
      i = j;
      if (mSubscriberId != null)
      {
        i = j;
        if (mSubscriberId != null) {
          i = mSubscriberId.compareTo(mSubscriberId);
        }
      }
    }
    int k = i;
    if (i == 0)
    {
      k = i;
      if (mNetworkId != null)
      {
        k = i;
        if (mNetworkId != null) {
          k = mNetworkId.compareTo(mNetworkId);
        }
      }
    }
    j = k;
    if (k == 0) {
      j = Boolean.compare(mRoaming, mRoaming);
    }
    i = j;
    if (j == 0) {
      i = Boolean.compare(mMetered, mMetered);
    }
    j = i;
    if (i == 0) {
      j = Boolean.compare(mDefaultNetwork, mDefaultNetwork);
    }
    return j;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof NetworkIdentity;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (NetworkIdentity)paramObject;
      bool1 = bool2;
      if (mType == mType)
      {
        bool1 = bool2;
        if (mSubType == mSubType)
        {
          bool1 = bool2;
          if (mRoaming == mRoaming)
          {
            bool1 = bool2;
            if (Objects.equals(mSubscriberId, mSubscriberId))
            {
              bool1 = bool2;
              if (Objects.equals(mNetworkId, mNetworkId))
              {
                bool1 = bool2;
                if (mMetered == mMetered)
                {
                  bool1 = bool2;
                  if (mDefaultNetwork == mDefaultNetwork) {
                    bool1 = true;
                  }
                }
              }
            }
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public boolean getDefaultNetwork()
  {
    return mDefaultNetwork;
  }
  
  public boolean getMetered()
  {
    return mMetered;
  }
  
  public String getNetworkId()
  {
    return mNetworkId;
  }
  
  public boolean getRoaming()
  {
    return mRoaming;
  }
  
  public int getSubType()
  {
    return mSubType;
  }
  
  public String getSubscriberId()
  {
    return mSubscriberId;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mType), Integer.valueOf(mSubType), mSubscriberId, mNetworkId, Boolean.valueOf(mRoaming), Boolean.valueOf(mMetered), Boolean.valueOf(mDefaultNetwork) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("{");
    localStringBuilder.append("type=");
    localStringBuilder.append(ConnectivityManager.getNetworkTypeName(mType));
    localStringBuilder.append(", subType=");
    localStringBuilder.append("COMBINED");
    if (mSubscriberId != null)
    {
      localStringBuilder.append(", subscriberId=");
      localStringBuilder.append(scrubSubscriberId(mSubscriberId));
    }
    if (mNetworkId != null)
    {
      localStringBuilder.append(", networkId=");
      localStringBuilder.append(mNetworkId);
    }
    if (mRoaming) {
      localStringBuilder.append(", ROAMING");
    }
    localStringBuilder.append(", metered=");
    localStringBuilder.append(mMetered);
    localStringBuilder.append(", defaultNetwork=");
    localStringBuilder.append(mDefaultNetwork);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1120986464257L, mType);
    if (mSubscriberId != null) {
      paramProtoOutputStream.write(1138166333442L, scrubSubscriberId(mSubscriberId));
    }
    paramProtoOutputStream.write(1138166333443L, mNetworkId);
    paramProtoOutputStream.write(1133871366148L, mRoaming);
    paramProtoOutputStream.write(1133871366149L, mMetered);
    paramProtoOutputStream.write(1133871366150L, mDefaultNetwork);
    paramProtoOutputStream.end(paramLong);
  }
}
