package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.BitUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public final class NetworkCapabilities
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkCapabilities> CREATOR = new Parcelable.Creator()
  {
    public NetworkCapabilities createFromParcel(Parcel paramAnonymousParcel)
    {
      NetworkCapabilities localNetworkCapabilities = new NetworkCapabilities();
      NetworkCapabilities.access$002(localNetworkCapabilities, paramAnonymousParcel.readLong());
      NetworkCapabilities.access$102(localNetworkCapabilities, paramAnonymousParcel.readLong());
      NetworkCapabilities.access$202(localNetworkCapabilities, paramAnonymousParcel.readLong());
      NetworkCapabilities.access$302(localNetworkCapabilities, paramAnonymousParcel.readInt());
      NetworkCapabilities.access$402(localNetworkCapabilities, paramAnonymousParcel.readInt());
      NetworkCapabilities.access$502(localNetworkCapabilities, (NetworkSpecifier)paramAnonymousParcel.readParcelable(null));
      NetworkCapabilities.access$602(localNetworkCapabilities, paramAnonymousParcel.readInt());
      NetworkCapabilities.access$702(localNetworkCapabilities, paramAnonymousParcel.readArraySet(null));
      NetworkCapabilities.access$802(localNetworkCapabilities, paramAnonymousParcel.readString());
      return localNetworkCapabilities;
    }
    
    public NetworkCapabilities[] newArray(int paramAnonymousInt)
    {
      return new NetworkCapabilities[paramAnonymousInt];
    }
  };
  private static final long DEFAULT_CAPABILITIES = 57344L;
  private static final long FORCE_RESTRICTED_CAPABILITIES = 4194304L;
  private static final int INVALID_UID = -1;
  public static final int LINK_BANDWIDTH_UNSPECIFIED = 0;
  private static final int MAX_NET_CAPABILITY = 22;
  public static final int MAX_TRANSPORT = 6;
  private static final int MIN_NET_CAPABILITY = 0;
  public static final int MIN_TRANSPORT = 0;
  private static final long MUTABLE_CAPABILITIES = 4145152L;
  public static final int NET_CAPABILITY_CAPTIVE_PORTAL = 17;
  public static final int NET_CAPABILITY_CBS = 5;
  public static final int NET_CAPABILITY_DUN = 2;
  public static final int NET_CAPABILITY_EIMS = 10;
  public static final int NET_CAPABILITY_FOREGROUND = 19;
  public static final int NET_CAPABILITY_FOTA = 3;
  public static final int NET_CAPABILITY_IA = 7;
  public static final int NET_CAPABILITY_IMS = 4;
  public static final int NET_CAPABILITY_INTERNET = 12;
  public static final int NET_CAPABILITY_MMS = 0;
  public static final int NET_CAPABILITY_NOT_CONGESTED = 20;
  public static final int NET_CAPABILITY_NOT_METERED = 11;
  public static final int NET_CAPABILITY_NOT_RESTRICTED = 13;
  public static final int NET_CAPABILITY_NOT_ROAMING = 18;
  public static final int NET_CAPABILITY_NOT_SUSPENDED = 21;
  public static final int NET_CAPABILITY_NOT_VPN = 15;
  @SystemApi
  public static final int NET_CAPABILITY_OEM_PAID = 22;
  public static final int NET_CAPABILITY_RCS = 8;
  public static final int NET_CAPABILITY_SUPL = 1;
  public static final int NET_CAPABILITY_TRUSTED = 14;
  public static final int NET_CAPABILITY_VALIDATED = 16;
  public static final int NET_CAPABILITY_WIFI_P2P = 6;
  public static final int NET_CAPABILITY_XCAP = 9;
  private static final long NON_REQUESTABLE_CAPABILITIES = 4128768L;
  @VisibleForTesting
  static final long RESTRICTED_CAPABILITIES = 1980L;
  public static final int SIGNAL_STRENGTH_UNSPECIFIED = Integer.MIN_VALUE;
  private static final String TAG = "NetworkCapabilities";
  public static final int TRANSPORT_BLUETOOTH = 2;
  public static final int TRANSPORT_CELLULAR = 0;
  public static final int TRANSPORT_ETHERNET = 3;
  public static final int TRANSPORT_LOWPAN = 6;
  private static final String[] TRANSPORT_NAMES = { "CELLULAR", "WIFI", "BLUETOOTH", "ETHERNET", "VPN", "WIFI_AWARE", "LOWPAN" };
  public static final int TRANSPORT_VPN = 4;
  public static final int TRANSPORT_WIFI = 1;
  public static final int TRANSPORT_WIFI_AWARE = 5;
  @VisibleForTesting
  static final long UNRESTRICTED_CAPABILITIES = 4163L;
  private int mEstablishingVpnAppUid = -1;
  private int mLinkDownBandwidthKbps = 0;
  private int mLinkUpBandwidthKbps = 0;
  private long mNetworkCapabilities;
  private NetworkSpecifier mNetworkSpecifier = null;
  private String mSSID;
  private int mSignalStrength = Integer.MIN_VALUE;
  private long mTransportTypes;
  private ArraySet<UidRange> mUids = null;
  private long mUnwantedNetworkCapabilities;
  
  public NetworkCapabilities()
  {
    clearAll();
    mNetworkCapabilities = 57344L;
  }
  
  public NetworkCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    if (paramNetworkCapabilities != null) {
      set(paramNetworkCapabilities);
    }
  }
  
  public static void appendStringRepresentationOfBitMaskToStringBuilder(StringBuilder paramStringBuilder, long paramLong, NameOf paramNameOf, String paramString)
  {
    int i = 0;
    int k;
    for (int j = 0; paramLong != 0L; j = k)
    {
      k = j;
      if ((1L & paramLong) != 0L)
      {
        if (j != 0) {
          paramStringBuilder.append(paramString);
        } else {
          j = 1;
        }
        paramStringBuilder.append(paramNameOf.nameOf(i));
        k = j;
      }
      paramLong >>= 1;
      i++;
    }
  }
  
  public static String capabilityNameOf(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 22: 
      return "OEM_PAID";
    case 21: 
      return "NOT_SUSPENDED";
    case 20: 
      return "NOT_CONGESTED";
    case 19: 
      return "FOREGROUND";
    case 18: 
      return "NOT_ROAMING";
    case 17: 
      return "CAPTIVE_PORTAL";
    case 16: 
      return "VALIDATED";
    case 15: 
      return "NOT_VPN";
    case 14: 
      return "TRUSTED";
    case 13: 
      return "NOT_RESTRICTED";
    case 12: 
      return "INTERNET";
    case 11: 
      return "NOT_METERED";
    case 10: 
      return "EIMS";
    case 9: 
      return "XCAP";
    case 8: 
      return "RCS";
    case 7: 
      return "IA";
    case 6: 
      return "WIFI_P2P";
    case 5: 
      return "CBS";
    case 4: 
      return "IMS";
    case 3: 
      return "FOTA";
    case 2: 
      return "DUN";
    case 1: 
      return "SUPL";
    }
    return "MMS";
  }
  
  public static String capabilityNamesOf(int[] paramArrayOfInt)
  {
    StringJoiner localStringJoiner = new StringJoiner("|");
    if (paramArrayOfInt != null)
    {
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++) {
        localStringJoiner.add(capabilityNameOf(paramArrayOfInt[j]));
      }
    }
    return localStringJoiner.toString();
  }
  
  private static void checkValidCapability(int paramInt)
  {
    boolean bool = isValidCapability(paramInt);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NetworkCapability ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("out of range");
    Preconditions.checkArgument(bool, localStringBuilder.toString());
  }
  
  private static void checkValidTransportType(int paramInt)
  {
    boolean bool = isValidTransport(paramInt);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid TransportType ");
    localStringBuilder.append(paramInt);
    Preconditions.checkArgument(bool, localStringBuilder.toString());
  }
  
  private void combineLinkBandwidths(NetworkCapabilities paramNetworkCapabilities)
  {
    mLinkUpBandwidthKbps = Math.max(mLinkUpBandwidthKbps, mLinkUpBandwidthKbps);
    mLinkDownBandwidthKbps = Math.max(mLinkDownBandwidthKbps, mLinkDownBandwidthKbps);
  }
  
  private void combineNetCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    mNetworkCapabilities |= mNetworkCapabilities;
    mUnwantedNetworkCapabilities |= mUnwantedNetworkCapabilities;
  }
  
  private void combineSSIDs(NetworkCapabilities paramNetworkCapabilities)
  {
    if ((mSSID != null) && (!mSSID.equals(mSSID))) {
      throw new IllegalStateException("Can't combine two SSIDs");
    }
    setSSID(mSSID);
  }
  
  private void combineSignalStrength(NetworkCapabilities paramNetworkCapabilities)
  {
    mSignalStrength = Math.max(mSignalStrength, mSignalStrength);
  }
  
  private void combineSpecifiers(NetworkCapabilities paramNetworkCapabilities)
  {
    if ((mNetworkSpecifier != null) && (!mNetworkSpecifier.equals(mNetworkSpecifier))) {
      throw new IllegalStateException("Can't combine two networkSpecifiers");
    }
    setNetworkSpecifier(mNetworkSpecifier);
  }
  
  private void combineTransportTypes(NetworkCapabilities paramNetworkCapabilities)
  {
    mTransportTypes |= mTransportTypes;
  }
  
  private void combineUids(NetworkCapabilities paramNetworkCapabilities)
  {
    if ((mUids != null) && (mUids != null))
    {
      mUids.addAll(mUids);
      return;
    }
    mUids = null;
  }
  
  private boolean equalsLinkBandwidths(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if ((mLinkUpBandwidthKbps == mLinkUpBandwidthKbps) && (mLinkDownBandwidthKbps == mLinkDownBandwidthKbps)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean equalsNetCapabilitiesRequestable(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if (((mNetworkCapabilities & 0xFFFFFFFFFFC0FFFF) == (mNetworkCapabilities & 0xFFFFFFFFFFC0FFFF)) && ((mUnwantedNetworkCapabilities & 0xFFFFFFFFFFC0FFFF) == (0xFFFFFFFFFFC0FFFF & mUnwantedNetworkCapabilities))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean equalsSignalStrength(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if (mSignalStrength == mSignalStrength) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean equalsSpecifier(NetworkCapabilities paramNetworkCapabilities)
  {
    return Objects.equals(mNetworkSpecifier, mNetworkSpecifier);
  }
  
  private static boolean isValidCapability(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt <= 22)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isValidTransport(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt <= 6)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static int maxBandwidth(int paramInt1, int paramInt2)
  {
    return Math.max(paramInt1, paramInt2);
  }
  
  public static int minBandwidth(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 0) {
      return paramInt2;
    }
    if (paramInt2 == 0) {
      return paramInt1;
    }
    return Math.min(paramInt1, paramInt2);
  }
  
  private boolean satisfiedByLinkBandwidths(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if ((mLinkUpBandwidthKbps <= mLinkUpBandwidthKbps) && (mLinkDownBandwidthKbps <= mLinkDownBandwidthKbps)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean satisfiedByNetCapabilities(NetworkCapabilities paramNetworkCapabilities, boolean paramBoolean)
  {
    long l1 = mNetworkCapabilities;
    long l2 = mUnwantedNetworkCapabilities;
    long l3 = mNetworkCapabilities;
    long l4 = l1;
    long l5 = l2;
    if (paramBoolean)
    {
      l4 = l1 & 0xFFFFFFFFFFC0BFFF;
      l5 = l2 & 0xFFFFFFFFFFC0BFFF;
    }
    if (((l3 & l4) == l4) && ((l5 & l3) == 0L)) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  private boolean satisfiedByNetworkCapabilities(NetworkCapabilities paramNetworkCapabilities, boolean paramBoolean)
  {
    if ((paramNetworkCapabilities != null) && (satisfiedByNetCapabilities(paramNetworkCapabilities, paramBoolean)) && (satisfiedByTransportTypes(paramNetworkCapabilities)) && ((paramBoolean) || (satisfiedByLinkBandwidths(paramNetworkCapabilities))) && (satisfiedBySpecifier(paramNetworkCapabilities)) && ((paramBoolean) || (satisfiedBySignalStrength(paramNetworkCapabilities))) && ((paramBoolean) || (satisfiedByUids(paramNetworkCapabilities))) && ((paramBoolean) || (satisfiedBySSID(paramNetworkCapabilities)))) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  private boolean satisfiedBySignalStrength(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if (mSignalStrength <= mSignalStrength) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean satisfiedBySpecifier(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if ((mNetworkSpecifier != null) && (!mNetworkSpecifier.satisfiedBy(mNetworkSpecifier)) && (!(mNetworkSpecifier instanceof MatchAllNetworkSpecifier))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean satisfiedByTransportTypes(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if ((mTransportTypes != 0L) && ((mTransportTypes & mTransportTypes) == 0L)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static String transportNameOf(int paramInt)
  {
    if (!isValidTransport(paramInt)) {
      return "UNKNOWN";
    }
    return TRANSPORT_NAMES[paramInt];
  }
  
  public static String transportNamesOf(int[] paramArrayOfInt)
  {
    StringJoiner localStringJoiner = new StringJoiner("|");
    if (paramArrayOfInt != null)
    {
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++) {
        localStringJoiner.add(transportNameOf(paramArrayOfInt[j]));
      }
    }
    return localStringJoiner.toString();
  }
  
  public NetworkCapabilities addCapability(int paramInt)
  {
    checkValidCapability(paramInt);
    mNetworkCapabilities |= 1 << paramInt;
    mUnwantedNetworkCapabilities &= 1 << paramInt;
    return this;
  }
  
  public NetworkCapabilities addTransportType(int paramInt)
  {
    checkValidTransportType(paramInt);
    mTransportTypes |= 1 << paramInt;
    setNetworkSpecifier(mNetworkSpecifier);
    return this;
  }
  
  public void addUnwantedCapability(int paramInt)
  {
    checkValidCapability(paramInt);
    mUnwantedNetworkCapabilities |= 1 << paramInt;
    mNetworkCapabilities &= 1 << paramInt;
  }
  
  public boolean appliesToUid(int paramInt)
  {
    if (mUids == null) {
      return true;
    }
    Iterator localIterator = mUids.iterator();
    while (localIterator.hasNext()) {
      if (((UidRange)localIterator.next()).contains(paramInt)) {
        return true;
      }
    }
    return false;
  }
  
  @VisibleForTesting
  public boolean appliesToUidRange(UidRange paramUidRange)
  {
    if (mUids == null) {
      return true;
    }
    Iterator localIterator = mUids.iterator();
    while (localIterator.hasNext()) {
      if (((UidRange)localIterator.next()).containsRange(paramUidRange)) {
        return true;
      }
    }
    return false;
  }
  
  public void clearAll()
  {
    mUnwantedNetworkCapabilities = 0L;
    mTransportTypes = 0L;
    mNetworkCapabilities = 0L;
    mLinkDownBandwidthKbps = 0;
    mLinkUpBandwidthKbps = 0;
    mNetworkSpecifier = null;
    mSignalStrength = Integer.MIN_VALUE;
    mUids = null;
    mEstablishingVpnAppUid = -1;
    mSSID = null;
  }
  
  public void combineCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    combineNetCapabilities(paramNetworkCapabilities);
    combineTransportTypes(paramNetworkCapabilities);
    combineLinkBandwidths(paramNetworkCapabilities);
    combineSpecifiers(paramNetworkCapabilities);
    combineSignalStrength(paramNetworkCapabilities);
    combineUids(paramNetworkCapabilities);
    combineSSIDs(paramNetworkCapabilities);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String describeFirstNonRequestableCapability()
  {
    long l = (mNetworkCapabilities | mUnwantedNetworkCapabilities) & 0x3F0000;
    if (l != 0L) {
      return capabilityNameOf(BitUtils.unpackBits(l)[0]);
    }
    if ((mLinkUpBandwidthKbps == 0) && (mLinkDownBandwidthKbps == 0))
    {
      if (hasSignalStrength()) {
        return "signalStrength";
      }
      return null;
    }
    return "link bandwidth";
  }
  
  public String describeImmutableDifferences(NetworkCapabilities paramNetworkCapabilities)
  {
    if (paramNetworkCapabilities == null) {
      return "other NetworkCapabilities was null";
    }
    StringJoiner localStringJoiner = new StringJoiner(", ");
    long l1 = mNetworkCapabilities & 0xFFFFFFFFFFC0B7FF;
    long l2 = 0xFFFFFFFFFFC0B7FF & mNetworkCapabilities;
    if (l1 != l2) {
      localStringJoiner.add(String.format("immutable capabilities changed: %s -> %s", new Object[] { capabilityNamesOf(BitUtils.unpackBits(l1)), capabilityNamesOf(BitUtils.unpackBits(l2)) }));
    }
    if (!equalsSpecifier(paramNetworkCapabilities)) {
      localStringJoiner.add(String.format("specifier changed: %s -> %s", new Object[] { getNetworkSpecifier(), paramNetworkCapabilities.getNetworkSpecifier() }));
    }
    if (!equalsTransportTypes(paramNetworkCapabilities)) {
      localStringJoiner.add(String.format("transports changed: %s -> %s", new Object[] { transportNamesOf(getTransportTypes()), transportNamesOf(paramNetworkCapabilities.getTransportTypes()) }));
    }
    return localStringJoiner.toString();
  }
  
  public boolean equalRequestableCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool1 = false;
    if (paramNetworkCapabilities == null) {
      return false;
    }
    boolean bool2 = bool1;
    if (equalsNetCapabilitiesRequestable(paramNetworkCapabilities))
    {
      bool2 = bool1;
      if (equalsTransportTypes(paramNetworkCapabilities))
      {
        bool2 = bool1;
        if (equalsSpecifier(paramNetworkCapabilities)) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if ((paramObject != null) && ((paramObject instanceof NetworkCapabilities)))
    {
      paramObject = (NetworkCapabilities)paramObject;
      boolean bool2 = bool1;
      if (equalsNetCapabilities(paramObject))
      {
        bool2 = bool1;
        if (equalsTransportTypes(paramObject))
        {
          bool2 = bool1;
          if (equalsLinkBandwidths(paramObject))
          {
            bool2 = bool1;
            if (equalsSignalStrength(paramObject))
            {
              bool2 = bool1;
              if (equalsSpecifier(paramObject))
              {
                bool2 = bool1;
                if (equalsUids(paramObject))
                {
                  bool2 = bool1;
                  if (equalsSSID(paramObject)) {
                    bool2 = true;
                  }
                }
              }
            }
          }
        }
      }
      return bool2;
    }
    return false;
  }
  
  public boolean equalsNetCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if ((mNetworkCapabilities == mNetworkCapabilities) && (mUnwantedNetworkCapabilities == mUnwantedNetworkCapabilities)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean equalsSSID(NetworkCapabilities paramNetworkCapabilities)
  {
    return Objects.equals(mSSID, mSSID);
  }
  
  public boolean equalsTransportTypes(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if (mTransportTypes == mTransportTypes) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @VisibleForTesting
  public boolean equalsUids(NetworkCapabilities paramNetworkCapabilities)
  {
    Object localObject = mUids;
    boolean bool = false;
    if (localObject == null)
    {
      if (mUids == null) {
        bool = true;
      }
      return bool;
    }
    if (mUids == null) {
      return false;
    }
    paramNetworkCapabilities = new ArraySet(mUids);
    localObject = ((Set)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      UidRange localUidRange = (UidRange)((Iterator)localObject).next();
      if (!paramNetworkCapabilities.contains(localUidRange)) {
        return false;
      }
      paramNetworkCapabilities.remove(localUidRange);
    }
    return paramNetworkCapabilities.isEmpty();
  }
  
  public int[] getCapabilities()
  {
    return BitUtils.unpackBits(mNetworkCapabilities);
  }
  
  public int getLinkDownstreamBandwidthKbps()
  {
    return mLinkDownBandwidthKbps;
  }
  
  public int getLinkUpstreamBandwidthKbps()
  {
    return mLinkUpBandwidthKbps;
  }
  
  public NetworkSpecifier getNetworkSpecifier()
  {
    return mNetworkSpecifier;
  }
  
  public String getSSID()
  {
    return mSSID;
  }
  
  public int getSignalStrength()
  {
    return mSignalStrength;
  }
  
  public int[] getTransportTypes()
  {
    return BitUtils.unpackBits(mTransportTypes);
  }
  
  public Set<UidRange> getUids()
  {
    ArraySet localArraySet;
    if (mUids == null) {
      localArraySet = null;
    } else {
      localArraySet = new ArraySet(mUids);
    }
    return localArraySet;
  }
  
  public int[] getUnwantedCapabilities()
  {
    return BitUtils.unpackBits(mUnwantedNetworkCapabilities);
  }
  
  public boolean hasCapability(int paramInt)
  {
    boolean bool1 = isValidCapability(paramInt);
    boolean bool2 = true;
    if ((!bool1) || ((mNetworkCapabilities & 1 << paramInt) == 0L)) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean hasSignalStrength()
  {
    boolean bool;
    if (mSignalStrength > Integer.MIN_VALUE) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasTransport(int paramInt)
  {
    boolean bool1 = isValidTransport(paramInt);
    boolean bool2 = true;
    if ((!bool1) || ((mTransportTypes & 1 << paramInt) == 0L)) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean hasUnwantedCapability(int paramInt)
  {
    boolean bool1 = isValidCapability(paramInt);
    boolean bool2 = true;
    if ((!bool1) || ((mUnwantedNetworkCapabilities & 1 << paramInt) == 0L)) {
      bool2 = false;
    }
    return bool2;
  }
  
  public int hashCode()
  {
    return (int)(mNetworkCapabilities & 0xFFFFFFFFFFFFFFFF) + (int)(mNetworkCapabilities >> 32) * 3 + (int)(mUnwantedNetworkCapabilities & 0xFFFFFFFFFFFFFFFF) * 5 + (int)(mUnwantedNetworkCapabilities >> 32) * 7 + (int)(0xFFFFFFFFFFFFFFFF & mTransportTypes) * 11 + (int)(mTransportTypes >> 32) * 13 + mLinkUpBandwidthKbps * 17 + mLinkDownBandwidthKbps * 19 + Objects.hashCode(mNetworkSpecifier) * 23 + mSignalStrength * 29 + Objects.hashCode(mUids) * 31 + Objects.hashCode(mSSID) * 37;
  }
  
  public void maybeMarkCapabilitiesRestricted()
  {
    long l = mNetworkCapabilities;
    int i = 0;
    int j;
    if ((l & 0x400000) != 0L) {
      j = 1;
    } else {
      j = 0;
    }
    int k;
    if ((mNetworkCapabilities & 0x1043) != 0L) {
      k = 1;
    } else {
      k = 0;
    }
    if ((mNetworkCapabilities & 0x7BC) != 0L) {
      i = 1;
    }
    if ((j != 0) || ((i != 0) && (k == 0))) {
      removeCapability(13);
    }
  }
  
  public NetworkCapabilities removeCapability(int paramInt)
  {
    checkValidCapability(paramInt);
    long l = 1 << paramInt;
    mNetworkCapabilities &= l;
    mUnwantedNetworkCapabilities &= l;
    return this;
  }
  
  public NetworkCapabilities removeTransportType(int paramInt)
  {
    checkValidTransportType(paramInt);
    mTransportTypes &= 1 << paramInt;
    setNetworkSpecifier(mNetworkSpecifier);
    return this;
  }
  
  public boolean satisfiedByImmutableNetworkCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    return satisfiedByNetworkCapabilities(paramNetworkCapabilities, true);
  }
  
  public boolean satisfiedByNetworkCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    return satisfiedByNetworkCapabilities(paramNetworkCapabilities, false);
  }
  
  public boolean satisfiedBySSID(NetworkCapabilities paramNetworkCapabilities)
  {
    boolean bool;
    if ((mSSID != null) && (!mSSID.equals(mSSID))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean satisfiedByUids(NetworkCapabilities paramNetworkCapabilities)
  {
    if ((mUids != null) && (mUids != null))
    {
      Iterator localIterator = mUids.iterator();
      while (localIterator.hasNext())
      {
        UidRange localUidRange = (UidRange)localIterator.next();
        if (localUidRange.contains(mEstablishingVpnAppUid)) {
          return true;
        }
        if (!paramNetworkCapabilities.appliesToUidRange(localUidRange)) {
          return false;
        }
      }
      return true;
    }
    return true;
  }
  
  public void set(NetworkCapabilities paramNetworkCapabilities)
  {
    mNetworkCapabilities = mNetworkCapabilities;
    mTransportTypes = mTransportTypes;
    mLinkUpBandwidthKbps = mLinkUpBandwidthKbps;
    mLinkDownBandwidthKbps = mLinkDownBandwidthKbps;
    mNetworkSpecifier = mNetworkSpecifier;
    mSignalStrength = mSignalStrength;
    setUids(mUids);
    mEstablishingVpnAppUid = mEstablishingVpnAppUid;
    mUnwantedNetworkCapabilities = mUnwantedNetworkCapabilities;
    mSSID = mSSID;
  }
  
  @Deprecated
  public void setCapabilities(int[] paramArrayOfInt)
  {
    setCapabilities(paramArrayOfInt, new int[0]);
  }
  
  public void setCapabilities(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    mNetworkCapabilities = BitUtils.packBits(paramArrayOfInt1);
    mUnwantedNetworkCapabilities = BitUtils.packBits(paramArrayOfInt2);
  }
  
  public NetworkCapabilities setCapability(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean) {
      addCapability(paramInt);
    } else {
      removeCapability(paramInt);
    }
    return this;
  }
  
  public void setEstablishingVpnAppUid(int paramInt)
  {
    mEstablishingVpnAppUid = paramInt;
  }
  
  public NetworkCapabilities setLinkDownstreamBandwidthKbps(int paramInt)
  {
    mLinkDownBandwidthKbps = paramInt;
    return this;
  }
  
  public NetworkCapabilities setLinkUpstreamBandwidthKbps(int paramInt)
  {
    mLinkUpBandwidthKbps = paramInt;
    return this;
  }
  
  public NetworkCapabilities setNetworkSpecifier(NetworkSpecifier paramNetworkSpecifier)
  {
    if ((paramNetworkSpecifier != null) && (Long.bitCount(mTransportTypes) != 1)) {
      throw new IllegalStateException("Must have a single transport specified to use setNetworkSpecifier");
    }
    mNetworkSpecifier = paramNetworkSpecifier;
    return this;
  }
  
  public NetworkCapabilities setSSID(String paramString)
  {
    mSSID = paramString;
    return this;
  }
  
  public NetworkCapabilities setSignalStrength(int paramInt)
  {
    mSignalStrength = paramInt;
    return this;
  }
  
  public NetworkCapabilities setSingleUid(int paramInt)
  {
    ArraySet localArraySet = new ArraySet(1);
    localArraySet.add(new UidRange(paramInt, paramInt));
    setUids(localArraySet);
    return this;
  }
  
  public NetworkCapabilities setTransportType(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean) {
      addTransportType(paramInt);
    } else {
      removeTransportType(paramInt);
    }
    return this;
  }
  
  public void setTransportTypes(int[] paramArrayOfInt)
  {
    mTransportTypes = BitUtils.packBits(paramArrayOfInt);
  }
  
  public NetworkCapabilities setUids(Set<UidRange> paramSet)
  {
    if (paramSet == null) {
      mUids = null;
    } else {
      mUids = new ArraySet(paramSet);
    }
    return this;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("[");
    if (0L != mTransportTypes)
    {
      localStringBuilder.append(" Transports: ");
      appendStringRepresentationOfBitMaskToStringBuilder(localStringBuilder, mTransportTypes, _..Lambda.FpGXkd3pLxeXY58eJ_84mi1PLWQ.INSTANCE, "|");
    }
    if (0L != mNetworkCapabilities)
    {
      localStringBuilder.append(" Capabilities: ");
      appendStringRepresentationOfBitMaskToStringBuilder(localStringBuilder, mNetworkCapabilities, _..Lambda.p1_56lwnt1xBuY1muPblbN1Dtkw.INSTANCE, "&");
    }
    if (0L != mNetworkCapabilities)
    {
      localStringBuilder.append(" Unwanted: ");
      appendStringRepresentationOfBitMaskToStringBuilder(localStringBuilder, mUnwantedNetworkCapabilities, _..Lambda.p1_56lwnt1xBuY1muPblbN1Dtkw.INSTANCE, "&");
    }
    if (mLinkUpBandwidthKbps > 0)
    {
      localStringBuilder.append(" LinkUpBandwidth>=");
      localStringBuilder.append(mLinkUpBandwidthKbps);
      localStringBuilder.append("Kbps");
    }
    if (mLinkDownBandwidthKbps > 0)
    {
      localStringBuilder.append(" LinkDnBandwidth>=");
      localStringBuilder.append(mLinkDownBandwidthKbps);
      localStringBuilder.append("Kbps");
    }
    if (mNetworkSpecifier != null)
    {
      localStringBuilder.append(" Specifier: <");
      localStringBuilder.append(mNetworkSpecifier);
      localStringBuilder.append(">");
    }
    if (hasSignalStrength())
    {
      localStringBuilder.append(" SignalStrength: ");
      localStringBuilder.append(mSignalStrength);
    }
    if (mUids != null) {
      if ((1 == mUids.size()) && (((UidRange)mUids.valueAt(0)).count() == 1))
      {
        localStringBuilder.append(" Uid: ");
        localStringBuilder.append(mUids.valueAt(0)).start);
      }
      else
      {
        localStringBuilder.append(" Uids: <");
        localStringBuilder.append(mUids);
        localStringBuilder.append(">");
      }
    }
    if (mEstablishingVpnAppUid != -1)
    {
      localStringBuilder.append(" EstablishingAppUid: ");
      localStringBuilder.append(mEstablishingVpnAppUid);
    }
    if (mSSID != null)
    {
      localStringBuilder.append(" SSID: ");
      localStringBuilder.append(mSSID);
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mNetworkCapabilities);
    paramParcel.writeLong(mUnwantedNetworkCapabilities);
    paramParcel.writeLong(mTransportTypes);
    paramParcel.writeInt(mLinkUpBandwidthKbps);
    paramParcel.writeInt(mLinkDownBandwidthKbps);
    paramParcel.writeParcelable((Parcelable)mNetworkSpecifier, paramInt);
    paramParcel.writeInt(mSignalStrength);
    paramParcel.writeArraySet(mUids);
    paramParcel.writeString(mSSID);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    int[] arrayOfInt = getTransportTypes();
    int i = arrayOfInt.length;
    int j = 0;
    for (int k = 0; k < i; k++) {
      paramProtoOutputStream.write(2259152797697L, arrayOfInt[k]);
    }
    arrayOfInt = getCapabilities();
    i = arrayOfInt.length;
    for (k = j; k < i; k++) {
      paramProtoOutputStream.write(2259152797698L, arrayOfInt[k]);
    }
    paramProtoOutputStream.write(1120986464259L, mLinkUpBandwidthKbps);
    paramProtoOutputStream.write(1120986464260L, mLinkDownBandwidthKbps);
    if (mNetworkSpecifier != null) {
      paramProtoOutputStream.write(1138166333445L, mNetworkSpecifier.toString());
    }
    paramProtoOutputStream.write(1133871366150L, hasSignalStrength());
    paramProtoOutputStream.write(1172526071815L, mSignalStrength);
    paramProtoOutputStream.end(paramLong);
  }
  
  private static abstract interface NameOf
  {
    public abstract String nameOf(int paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface NetCapability {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Transport {}
}
