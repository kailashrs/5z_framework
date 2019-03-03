package android.telephony.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ApnSetting
  implements Parcelable
{
  private static final Map<Integer, String> APN_TYPE_INT_MAP;
  private static final Map<String, Integer> APN_TYPE_STRING_MAP = new ArrayMap();
  public static final int AUTH_TYPE_CHAP = 2;
  public static final int AUTH_TYPE_NONE = 0;
  public static final int AUTH_TYPE_PAP = 1;
  public static final int AUTH_TYPE_PAP_OR_CHAP = 3;
  public static final Parcelable.Creator<ApnSetting> CREATOR = new Parcelable.Creator()
  {
    public ApnSetting createFromParcel(Parcel paramAnonymousParcel)
    {
      return ApnSetting.readFromParcel(paramAnonymousParcel);
    }
    
    public ApnSetting[] newArray(int paramAnonymousInt)
    {
      return new ApnSetting[paramAnonymousInt];
    }
  };
  private static final String LOG_TAG = "ApnSetting";
  public static final int MVNO_TYPE_GID = 2;
  public static final int MVNO_TYPE_ICCID = 3;
  public static final int MVNO_TYPE_IMSI = 1;
  private static final Map<Integer, String> MVNO_TYPE_INT_MAP;
  public static final int MVNO_TYPE_SPN = 0;
  private static final Map<String, Integer> MVNO_TYPE_STRING_MAP;
  private static final int NOT_IN_MAP_INT = -1;
  private static final int NO_PORT_SPECIFIED = -1;
  private static final Map<Integer, String> PROTOCOL_INT_MAP;
  public static final int PROTOCOL_IP = 0;
  public static final int PROTOCOL_IPV4V6 = 2;
  public static final int PROTOCOL_IPV6 = 1;
  public static final int PROTOCOL_PPP = 3;
  private static final Map<String, Integer> PROTOCOL_STRING_MAP;
  private static final int TYPE_ALL_BUT_IA = 767;
  public static final int TYPE_CBS = 128;
  public static final int TYPE_DEFAULT = 17;
  public static final int TYPE_DUN = 8;
  public static final int TYPE_EMERGENCY = 512;
  public static final int TYPE_FOTA = 32;
  public static final int TYPE_HIPRI = 16;
  public static final int TYPE_IA = 256;
  public static final int TYPE_IMS = 64;
  public static final int TYPE_MMS = 2;
  public static final int TYPE_SUPL = 4;
  private static final boolean VDBG = false;
  private final String mApnName;
  private final int mApnTypeBitmask;
  private final int mAuthType;
  private final boolean mCarrierEnabled;
  private final String mEntryName;
  private final int mId;
  private final int mMaxConns;
  private final int mMaxConnsTime;
  private final InetAddress mMmsProxyAddress;
  private final int mMmsProxyPort;
  private final Uri mMmsc;
  private final boolean mModemCognitive;
  private final int mMtu;
  private final String mMvnoMatchData;
  private final int mMvnoType;
  private final int mNetworkTypeBitmask;
  private final String mOperatorNumeric;
  private final String mPassword;
  private boolean mPermanentFailed = false;
  private final int mProfileId;
  private final int mProtocol;
  private final InetAddress mProxyAddress;
  private final int mProxyPort;
  private final int mRoamingProtocol;
  private final String mUser;
  private final int mWaitTime;
  
  static
  {
    APN_TYPE_STRING_MAP.put("*", Integer.valueOf(767));
    APN_TYPE_STRING_MAP.put("default", Integer.valueOf(17));
    APN_TYPE_STRING_MAP.put("mms", Integer.valueOf(2));
    APN_TYPE_STRING_MAP.put("supl", Integer.valueOf(4));
    APN_TYPE_STRING_MAP.put("dun", Integer.valueOf(8));
    APN_TYPE_STRING_MAP.put("hipri", Integer.valueOf(16));
    APN_TYPE_STRING_MAP.put("fota", Integer.valueOf(32));
    APN_TYPE_STRING_MAP.put("ims", Integer.valueOf(64));
    APN_TYPE_STRING_MAP.put("cbs", Integer.valueOf(128));
    APN_TYPE_STRING_MAP.put("ia", Integer.valueOf(256));
    APN_TYPE_STRING_MAP.put("emergency", Integer.valueOf(512));
    APN_TYPE_INT_MAP = new ArrayMap();
    APN_TYPE_INT_MAP.put(Integer.valueOf(17), "default");
    APN_TYPE_INT_MAP.put(Integer.valueOf(2), "mms");
    APN_TYPE_INT_MAP.put(Integer.valueOf(4), "supl");
    APN_TYPE_INT_MAP.put(Integer.valueOf(8), "dun");
    APN_TYPE_INT_MAP.put(Integer.valueOf(16), "hipri");
    APN_TYPE_INT_MAP.put(Integer.valueOf(32), "fota");
    APN_TYPE_INT_MAP.put(Integer.valueOf(64), "ims");
    APN_TYPE_INT_MAP.put(Integer.valueOf(128), "cbs");
    APN_TYPE_INT_MAP.put(Integer.valueOf(256), "ia");
    APN_TYPE_INT_MAP.put(Integer.valueOf(512), "emergency");
    PROTOCOL_STRING_MAP = new ArrayMap();
    PROTOCOL_STRING_MAP.put("IP", Integer.valueOf(0));
    PROTOCOL_STRING_MAP.put("IPV6", Integer.valueOf(1));
    PROTOCOL_STRING_MAP.put("IPV4V6", Integer.valueOf(2));
    PROTOCOL_STRING_MAP.put("PPP", Integer.valueOf(3));
    PROTOCOL_INT_MAP = new ArrayMap();
    PROTOCOL_INT_MAP.put(Integer.valueOf(0), "IP");
    PROTOCOL_INT_MAP.put(Integer.valueOf(1), "IPV6");
    PROTOCOL_INT_MAP.put(Integer.valueOf(2), "IPV4V6");
    PROTOCOL_INT_MAP.put(Integer.valueOf(3), "PPP");
    MVNO_TYPE_STRING_MAP = new ArrayMap();
    MVNO_TYPE_STRING_MAP.put("spn", Integer.valueOf(0));
    MVNO_TYPE_STRING_MAP.put("imsi", Integer.valueOf(1));
    MVNO_TYPE_STRING_MAP.put("gid", Integer.valueOf(2));
    MVNO_TYPE_STRING_MAP.put("iccid", Integer.valueOf(3));
    MVNO_TYPE_INT_MAP = new ArrayMap();
    MVNO_TYPE_INT_MAP.put(Integer.valueOf(0), "spn");
    MVNO_TYPE_INT_MAP.put(Integer.valueOf(1), "imsi");
    MVNO_TYPE_INT_MAP.put(Integer.valueOf(2), "gid");
    MVNO_TYPE_INT_MAP.put(Integer.valueOf(3), "iccid");
  }
  
  private ApnSetting(Builder paramBuilder)
  {
    mEntryName = mEntryName;
    mApnName = mApnName;
    mProxyAddress = mProxyAddress;
    mProxyPort = mProxyPort;
    mMmsc = mMmsc;
    mMmsProxyAddress = mMmsProxyAddress;
    mMmsProxyPort = mMmsProxyPort;
    mUser = mUser;
    mPassword = mPassword;
    mAuthType = mAuthType;
    mApnTypeBitmask = mApnTypeBitmask;
    mId = mId;
    mOperatorNumeric = mOperatorNumeric;
    mProtocol = mProtocol;
    mRoamingProtocol = mRoamingProtocol;
    mMtu = mMtu;
    mCarrierEnabled = mCarrierEnabled;
    mNetworkTypeBitmask = mNetworkTypeBitmask;
    mProfileId = mProfileId;
    mModemCognitive = mModemCognitive;
    mMaxConns = mMaxConns;
    mWaitTime = mWaitTime;
    mMaxConnsTime = mMaxConnsTime;
    mMvnoType = mMvnoType;
    mMvnoMatchData = mMvnoMatchData;
  }
  
  private static Uri UriFromString(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      paramString = null;
    } else {
      paramString = Uri.parse(paramString);
    }
    return paramString;
  }
  
  private static String UriToString(Uri paramUri)
  {
    if (paramUri == null) {
      paramUri = "";
    } else {
      paramUri = paramUri.toString();
    }
    return paramUri;
  }
  
  private String deParseTypes(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = APN_TYPE_INT_MAP.keySet().iterator();
    while (localIterator.hasNext())
    {
      Integer localInteger = (Integer)localIterator.next();
      if ((localInteger.intValue() & paramInt) == localInteger.intValue()) {
        localArrayList.add((String)APN_TYPE_INT_MAP.get(localInteger));
      }
    }
    return TextUtils.join(",", localArrayList);
  }
  
  private static InetAddress inetAddressFromString(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return null;
    }
    try
    {
      paramString = InetAddress.getByName(paramString);
      return paramString;
    }
    catch (UnknownHostException paramString)
    {
      Log.e("ApnSetting", "Can't parse InetAddress from string: unknown host.");
    }
    return null;
  }
  
  private static String inetAddressToString(InetAddress paramInetAddress)
  {
    if (paramInetAddress == null) {
      return null;
    }
    String str = paramInetAddress.toString();
    if (TextUtils.isEmpty(str)) {
      return null;
    }
    paramInetAddress = str.substring(0, str.indexOf("/"));
    str = str.substring(str.indexOf("/") + 1);
    if ((TextUtils.isEmpty(paramInetAddress)) && (TextUtils.isEmpty(str))) {
      return null;
    }
    if (TextUtils.isEmpty(paramInetAddress)) {
      paramInetAddress = str;
    }
    return paramInetAddress;
  }
  
  public static ApnSetting makeApnSetting(int paramInt1, String paramString1, String paramString2, String paramString3, InetAddress paramInetAddress1, int paramInt2, Uri paramUri, InetAddress paramInetAddress2, int paramInt3, String paramString4, String paramString5, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean1, int paramInt8, int paramInt9, boolean paramBoolean2, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, String paramString6)
  {
    return new Builder().setId(paramInt1).setOperatorNumeric(paramString1).setEntryName(paramString2).setApnName(paramString3).setProxyAddress(paramInetAddress1).setProxyPort(paramInt2).setMmsc(paramUri).setMmsProxyAddress(paramInetAddress2).setMmsProxyPort(paramInt3).setUser(paramString4).setPassword(paramString5).setAuthType(paramInt4).setApnTypeBitmask(paramInt5).setProtocol(paramInt6).setRoamingProtocol(paramInt7).setCarrierEnabled(paramBoolean1).setNetworkTypeBitmask(paramInt8).setProfileId(paramInt9).setModemCognitive(paramBoolean2).setMaxConns(paramInt10).setWaitTime(paramInt11).setMaxConnsTime(paramInt12).setMtu(paramInt13).setMvnoType(paramInt14).setMvnoMatchData(paramString6).build();
  }
  
  public static ApnSetting makeApnSetting(Cursor paramCursor)
  {
    int i = parseTypes(paramCursor.getString(paramCursor.getColumnIndexOrThrow("type")));
    int j = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("network_type_bitmask"));
    int k = j;
    if (j == 0)
    {
      k = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("bearer_bitmask"));
      k = ServiceState.convertBearerBitmaskToNetworkTypeBitmask(k);
    }
    int m = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("_id"));
    String str1 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("numeric"));
    String str2 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("name"));
    String str3 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("apn"));
    InetAddress localInetAddress1 = inetAddressFromString(paramCursor.getString(paramCursor.getColumnIndexOrThrow("proxy")));
    int n = portFromString(paramCursor.getString(paramCursor.getColumnIndexOrThrow("port")));
    Uri localUri = UriFromString(paramCursor.getString(paramCursor.getColumnIndexOrThrow("mmsc")));
    InetAddress localInetAddress2 = inetAddressFromString(paramCursor.getString(paramCursor.getColumnIndexOrThrow("mmsproxy")));
    j = portFromString(paramCursor.getString(paramCursor.getColumnIndexOrThrow("mmsport")));
    String str4 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("user"));
    String str5 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("password"));
    int i1 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("authtype"));
    int i2 = nullToNotInMapInt((Integer)PROTOCOL_STRING_MAP.get(paramCursor.getString(paramCursor.getColumnIndexOrThrow("protocol"))));
    int i3 = nullToNotInMapInt((Integer)PROTOCOL_STRING_MAP.get(paramCursor.getString(paramCursor.getColumnIndexOrThrow("roaming_protocol"))));
    boolean bool1;
    if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("carrier_enabled")) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    int i4 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("profile_id"));
    boolean bool2;
    if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("modem_cognitive")) == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    return makeApnSetting(m, str1, str2, str3, localInetAddress1, n, localUri, localInetAddress2, j, str4, str5, i1, i, i2, i3, bool1, k, i4, bool2, paramCursor.getInt(paramCursor.getColumnIndexOrThrow("max_conns")), paramCursor.getInt(paramCursor.getColumnIndexOrThrow("wait_time")), paramCursor.getInt(paramCursor.getColumnIndexOrThrow("max_conns_time")), paramCursor.getInt(paramCursor.getColumnIndexOrThrow("mtu")), nullToNotInMapInt((Integer)MVNO_TYPE_STRING_MAP.get(paramCursor.getString(paramCursor.getColumnIndexOrThrow("mvno_type")))), paramCursor.getString(paramCursor.getColumnIndexOrThrow("mvno_match_data")));
  }
  
  public static ApnSetting makeApnSetting(ApnSetting paramApnSetting)
  {
    return makeApnSetting(mId, mOperatorNumeric, mEntryName, mApnName, mProxyAddress, mProxyPort, mMmsc, mMmsProxyAddress, mMmsProxyPort, mUser, mPassword, mAuthType, mApnTypeBitmask, mProtocol, mRoamingProtocol, mCarrierEnabled, mNetworkTypeBitmask, mProfileId, mModemCognitive, mMaxConns, mWaitTime, mMaxConnsTime, mMtu, mMvnoType, mMvnoMatchData);
  }
  
  private String nullToEmpty(String paramString)
  {
    if (paramString == null) {
      paramString = "";
    }
    return paramString;
  }
  
  private static int nullToNotInMapInt(Integer paramInteger)
  {
    int i;
    if (paramInteger == null) {
      i = -1;
    } else {
      i = paramInteger.intValue();
    }
    return i;
  }
  
  public static int parseTypes(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return 767;
    }
    int i = 0;
    paramString = paramString.split(",");
    int j = paramString.length;
    int k = 0;
    while (k < j)
    {
      Object localObject = paramString[k];
      localObject = (Integer)APN_TYPE_STRING_MAP.get(localObject);
      int m = i;
      if (localObject != null) {
        m = i | ((Integer)localObject).intValue();
      }
      k++;
      i = m;
    }
    return i;
  }
  
  private static int portFromString(String paramString)
  {
    int i = -1;
    int j = i;
    if (!TextUtils.isEmpty(paramString)) {
      try
      {
        j = Integer.parseInt(paramString);
      }
      catch (NumberFormatException paramString)
      {
        Log.e("ApnSetting", "Can't parse port from String");
        j = i;
      }
    }
    return j;
  }
  
  private static String portToString(int paramInt)
  {
    String str;
    if (paramInt == -1) {
      str = "";
    } else {
      str = Integer.toString(paramInt);
    }
    return str;
  }
  
  private static ApnSetting readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    String str1 = paramParcel.readString();
    String str2 = paramParcel.readString();
    String str3 = paramParcel.readString();
    InetAddress localInetAddress1 = (InetAddress)paramParcel.readValue(InetAddress.class.getClassLoader());
    int j = paramParcel.readInt();
    Uri localUri = (Uri)paramParcel.readValue(Uri.class.getClassLoader());
    InetAddress localInetAddress2 = (InetAddress)paramParcel.readValue(InetAddress.class.getClassLoader());
    int k = paramParcel.readInt();
    String str4 = paramParcel.readString();
    String str5 = paramParcel.readString();
    int m = paramParcel.readInt();
    int n = paramParcel.readInt();
    int i1 = paramParcel.readInt();
    int i2 = paramParcel.readInt();
    if (paramParcel.readInt() > 0) {}
    for (boolean bool = true;; bool = false) {
      break;
    }
    int i3 = paramParcel.readInt();
    return makeApnSetting(i, str1, str2, str3, localInetAddress1, j, localUri, localInetAddress2, k, str4, str5, m, n, i1, i2, bool, paramParcel.readInt(), 0, false, 0, 0, 0, 0, i3, null);
  }
  
  private boolean typeSameAny(ApnSetting paramApnSetting1, ApnSetting paramApnSetting2)
  {
    return (mApnTypeBitmask & mApnTypeBitmask) != 0;
  }
  
  private boolean xorEquals(Object paramObject1, Object paramObject2)
  {
    boolean bool;
    if ((paramObject1 != null) && (paramObject2 != null) && (!paramObject1.equals(paramObject2))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean xorEquals(String paramString1, String paramString2)
  {
    boolean bool;
    if ((!Objects.equals(paramString1, paramString2)) && (!TextUtils.isEmpty(paramString1)) && (!TextUtils.isEmpty(paramString2))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean xorEqualsPort(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 != -1) && (paramInt2 != -1) && (!Objects.equals(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2)))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean canHandleType(int paramInt)
  {
    boolean bool;
    if ((mCarrierEnabled) && ((mApnTypeBitmask & paramInt) == paramInt)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof ApnSetting;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (ApnSetting)paramObject;
    bool1 = bool2;
    if (mEntryName.equals(mEntryName))
    {
      bool1 = bool2;
      if (Objects.equals(Integer.valueOf(mId), Integer.valueOf(mId)))
      {
        bool1 = bool2;
        if (Objects.equals(mOperatorNumeric, mOperatorNumeric))
        {
          bool1 = bool2;
          if (Objects.equals(mApnName, mApnName))
          {
            bool1 = bool2;
            if (Objects.equals(mProxyAddress, mProxyAddress))
            {
              bool1 = bool2;
              if (Objects.equals(mMmsc, mMmsc))
              {
                bool1 = bool2;
                if (Objects.equals(mMmsProxyAddress, mMmsProxyAddress))
                {
                  bool1 = bool2;
                  if (Objects.equals(Integer.valueOf(mMmsProxyPort), Integer.valueOf(mMmsProxyPort)))
                  {
                    bool1 = bool2;
                    if (Objects.equals(Integer.valueOf(mProxyPort), Integer.valueOf(mProxyPort)))
                    {
                      bool1 = bool2;
                      if (Objects.equals(mUser, mUser))
                      {
                        bool1 = bool2;
                        if (Objects.equals(mPassword, mPassword))
                        {
                          bool1 = bool2;
                          if (Objects.equals(Integer.valueOf(mAuthType), Integer.valueOf(mAuthType)))
                          {
                            bool1 = bool2;
                            if (Objects.equals(Integer.valueOf(mApnTypeBitmask), Integer.valueOf(mApnTypeBitmask)))
                            {
                              bool1 = bool2;
                              if (Objects.equals(Integer.valueOf(mProtocol), Integer.valueOf(mProtocol)))
                              {
                                bool1 = bool2;
                                if (Objects.equals(Integer.valueOf(mRoamingProtocol), Integer.valueOf(mRoamingProtocol)))
                                {
                                  bool1 = bool2;
                                  if (Objects.equals(Boolean.valueOf(mCarrierEnabled), Boolean.valueOf(mCarrierEnabled)))
                                  {
                                    bool1 = bool2;
                                    if (Objects.equals(Integer.valueOf(mProfileId), Integer.valueOf(mProfileId)))
                                    {
                                      bool1 = bool2;
                                      if (Objects.equals(Boolean.valueOf(mModemCognitive), Boolean.valueOf(mModemCognitive)))
                                      {
                                        bool1 = bool2;
                                        if (Objects.equals(Integer.valueOf(mMaxConns), Integer.valueOf(mMaxConns)))
                                        {
                                          bool1 = bool2;
                                          if (Objects.equals(Integer.valueOf(mWaitTime), Integer.valueOf(mWaitTime)))
                                          {
                                            bool1 = bool2;
                                            if (Objects.equals(Integer.valueOf(mMaxConnsTime), Integer.valueOf(mMaxConnsTime)))
                                            {
                                              bool1 = bool2;
                                              if (Objects.equals(Integer.valueOf(mMtu), Integer.valueOf(mMtu)))
                                              {
                                                bool1 = bool2;
                                                if (Objects.equals(Integer.valueOf(mMvnoType), Integer.valueOf(mMvnoType)))
                                                {
                                                  bool1 = bool2;
                                                  if (Objects.equals(mMvnoMatchData, mMvnoMatchData))
                                                  {
                                                    bool1 = bool2;
                                                    if (Objects.equals(Integer.valueOf(mNetworkTypeBitmask), Integer.valueOf(mNetworkTypeBitmask))) {
                                                      bool1 = true;
                                                    }
                                                  }
                                                }
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public boolean equals(Object paramObject, boolean paramBoolean)
  {
    boolean bool1 = paramObject instanceof ApnSetting;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (ApnSetting)paramObject;
    bool1 = bool2;
    if (mEntryName.equals(mEntryName))
    {
      bool1 = bool2;
      if (Objects.equals(mOperatorNumeric, mOperatorNumeric))
      {
        bool1 = bool2;
        if (Objects.equals(mApnName, mApnName))
        {
          bool1 = bool2;
          if (Objects.equals(mProxyAddress, mProxyAddress))
          {
            bool1 = bool2;
            if (Objects.equals(mMmsc, mMmsc))
            {
              bool1 = bool2;
              if (Objects.equals(mMmsProxyAddress, mMmsProxyAddress))
              {
                bool1 = bool2;
                if (Objects.equals(Integer.valueOf(mMmsProxyPort), Integer.valueOf(mMmsProxyPort)))
                {
                  bool1 = bool2;
                  if (Objects.equals(Integer.valueOf(mProxyPort), Integer.valueOf(mProxyPort)))
                  {
                    bool1 = bool2;
                    if (Objects.equals(mUser, mUser))
                    {
                      bool1 = bool2;
                      if (Objects.equals(mPassword, mPassword))
                      {
                        bool1 = bool2;
                        if (Objects.equals(Integer.valueOf(mAuthType), Integer.valueOf(mAuthType)))
                        {
                          bool1 = bool2;
                          if (Objects.equals(Integer.valueOf(mApnTypeBitmask), Integer.valueOf(mApnTypeBitmask))) {
                            if (!paramBoolean)
                            {
                              bool1 = bool2;
                              if (!Objects.equals(Integer.valueOf(mProtocol), Integer.valueOf(mProtocol))) {}
                            }
                            else if (paramBoolean)
                            {
                              bool1 = bool2;
                              if (!Objects.equals(Integer.valueOf(mRoamingProtocol), Integer.valueOf(mRoamingProtocol))) {}
                            }
                            else
                            {
                              bool1 = bool2;
                              if (Objects.equals(Boolean.valueOf(mCarrierEnabled), Boolean.valueOf(mCarrierEnabled)))
                              {
                                bool1 = bool2;
                                if (Objects.equals(Integer.valueOf(mProfileId), Integer.valueOf(mProfileId)))
                                {
                                  bool1 = bool2;
                                  if (Objects.equals(Boolean.valueOf(mModemCognitive), Boolean.valueOf(mModemCognitive)))
                                  {
                                    bool1 = bool2;
                                    if (Objects.equals(Integer.valueOf(mMaxConns), Integer.valueOf(mMaxConns)))
                                    {
                                      bool1 = bool2;
                                      if (Objects.equals(Integer.valueOf(mWaitTime), Integer.valueOf(mWaitTime)))
                                      {
                                        bool1 = bool2;
                                        if (Objects.equals(Integer.valueOf(mMaxConnsTime), Integer.valueOf(mMaxConnsTime)))
                                        {
                                          bool1 = bool2;
                                          if (Objects.equals(Integer.valueOf(mMtu), Integer.valueOf(mMtu)))
                                          {
                                            bool1 = bool2;
                                            if (Objects.equals(Integer.valueOf(mMvnoType), Integer.valueOf(mMvnoType)))
                                            {
                                              bool1 = bool2;
                                              if (Objects.equals(mMvnoMatchData, mMvnoMatchData)) {
                                                bool1 = true;
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public String getApnName()
  {
    return mApnName;
  }
  
  public int getApnTypeBitmask()
  {
    return mApnTypeBitmask;
  }
  
  public int getAuthType()
  {
    return mAuthType;
  }
  
  public String getEntryName()
  {
    return mEntryName;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getMaxConns()
  {
    return mMaxConns;
  }
  
  public int getMaxConnsTime()
  {
    return mMaxConnsTime;
  }
  
  public InetAddress getMmsProxyAddress()
  {
    return mMmsProxyAddress;
  }
  
  public int getMmsProxyPort()
  {
    return mMmsProxyPort;
  }
  
  public Uri getMmsc()
  {
    return mMmsc;
  }
  
  public boolean getModemCognitive()
  {
    return mModemCognitive;
  }
  
  public int getMtu()
  {
    return mMtu;
  }
  
  public String getMvnoMatchData()
  {
    return mMvnoMatchData;
  }
  
  public int getMvnoType()
  {
    return mMvnoType;
  }
  
  public int getNetworkTypeBitmask()
  {
    return mNetworkTypeBitmask;
  }
  
  public String getOperatorNumeric()
  {
    return mOperatorNumeric;
  }
  
  public String getPassword()
  {
    return mPassword;
  }
  
  public boolean getPermanentFailed()
  {
    return mPermanentFailed;
  }
  
  public int getProfileId()
  {
    return mProfileId;
  }
  
  public int getProtocol()
  {
    return mProtocol;
  }
  
  public InetAddress getProxyAddress()
  {
    return mProxyAddress;
  }
  
  public int getProxyPort()
  {
    return mProxyPort;
  }
  
  public int getRoamingProtocol()
  {
    return mRoamingProtocol;
  }
  
  public String getUser()
  {
    return mUser;
  }
  
  public int getWaitTime()
  {
    return mWaitTime;
  }
  
  public boolean hasMvnoParams()
  {
    boolean bool;
    if ((mMvnoType != -1) && (!TextUtils.isEmpty(mMvnoMatchData))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEnabled()
  {
    return mCarrierEnabled;
  }
  
  public void setPermanentFailed(boolean paramBoolean)
  {
    mPermanentFailed = paramBoolean;
  }
  
  public boolean similar(ApnSetting paramApnSetting)
  {
    boolean bool;
    if ((!canHandleType(8)) && (!paramApnSetting.canHandleType(8)) && (Objects.equals(mApnName, mApnName)) && (!typeSameAny(this, paramApnSetting)) && (xorEquals(mProxyAddress, mProxyAddress)) && (xorEqualsPort(mProxyPort, mProxyPort)) && (xorEquals(Integer.valueOf(mProtocol), Integer.valueOf(mProtocol))) && (xorEquals(Integer.valueOf(mRoamingProtocol), Integer.valueOf(mRoamingProtocol))) && (Objects.equals(Boolean.valueOf(mCarrierEnabled), Boolean.valueOf(mCarrierEnabled))) && (Objects.equals(Integer.valueOf(mProfileId), Integer.valueOf(mProfileId))) && (Objects.equals(Integer.valueOf(mMvnoType), Integer.valueOf(mMvnoType))) && (Objects.equals(mMvnoMatchData, mMvnoMatchData)) && (xorEquals(mMmsc, mMmsc)) && (xorEquals(mMmsProxyAddress, mMmsProxyAddress)) && (xorEqualsPort(mMmsProxyPort, mMmsProxyPort)) && (Objects.equals(Integer.valueOf(mNetworkTypeBitmask), Integer.valueOf(mNetworkTypeBitmask)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public ContentValues toContentValues()
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("numeric", nullToEmpty(mOperatorNumeric));
    localContentValues.put("name", nullToEmpty(mEntryName));
    localContentValues.put("apn", nullToEmpty(mApnName));
    String str;
    if (mProxyAddress == null) {
      str = "";
    } else {
      str = inetAddressToString(mProxyAddress);
    }
    localContentValues.put("proxy", str);
    localContentValues.put("port", portToString(mProxyPort));
    if (mMmsc == null) {
      str = "";
    } else {
      str = UriToString(mMmsc);
    }
    localContentValues.put("mmsc", str);
    localContentValues.put("mmsport", portToString(mMmsProxyPort));
    if (mMmsProxyAddress == null) {
      str = "";
    } else {
      str = inetAddressToString(mMmsProxyAddress);
    }
    localContentValues.put("mmsproxy", str);
    localContentValues.put("user", nullToEmpty(mUser));
    localContentValues.put("password", nullToEmpty(mPassword));
    localContentValues.put("authtype", Integer.valueOf(mAuthType));
    localContentValues.put("type", nullToEmpty(deParseTypes(mApnTypeBitmask)));
    localContentValues.put("protocol", nullToEmpty((String)PROTOCOL_INT_MAP.get(Integer.valueOf(mProtocol))));
    localContentValues.put("roaming_protocol", nullToEmpty((String)PROTOCOL_INT_MAP.get(Integer.valueOf(mRoamingProtocol))));
    localContentValues.put("carrier_enabled", Boolean.valueOf(mCarrierEnabled));
    localContentValues.put("mvno_type", nullToEmpty((String)MVNO_TYPE_INT_MAP.get(Integer.valueOf(mMvnoType))));
    localContentValues.put("network_type_bitmask", Integer.valueOf(mNetworkTypeBitmask));
    return localContentValues;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ApnSettingV4] ");
    localStringBuilder.append(mEntryName);
    localStringBuilder.append(", ");
    localStringBuilder.append(mId);
    localStringBuilder.append(", ");
    localStringBuilder.append(mOperatorNumeric);
    localStringBuilder.append(", ");
    localStringBuilder.append(mApnName);
    localStringBuilder.append(", ");
    localStringBuilder.append(inetAddressToString(mProxyAddress));
    localStringBuilder.append(", ");
    localStringBuilder.append(UriToString(mMmsc));
    localStringBuilder.append(", ");
    localStringBuilder.append(inetAddressToString(mMmsProxyAddress));
    localStringBuilder.append(", ");
    localStringBuilder.append(portToString(mMmsProxyPort));
    localStringBuilder.append(", ");
    localStringBuilder.append(portToString(mProxyPort));
    localStringBuilder.append(", ");
    localStringBuilder.append(mAuthType);
    localStringBuilder.append(", ");
    localStringBuilder.append(TextUtils.join(" | ", deParseTypes(mApnTypeBitmask).split(",")));
    localStringBuilder.append(", ");
    localStringBuilder.append(", ");
    localStringBuilder.append(mProtocol);
    localStringBuilder.append(", ");
    localStringBuilder.append(mRoamingProtocol);
    localStringBuilder.append(", ");
    localStringBuilder.append(mCarrierEnabled);
    localStringBuilder.append(", ");
    localStringBuilder.append(mProfileId);
    localStringBuilder.append(", ");
    localStringBuilder.append(mModemCognitive);
    localStringBuilder.append(", ");
    localStringBuilder.append(mMaxConns);
    localStringBuilder.append(", ");
    localStringBuilder.append(mWaitTime);
    localStringBuilder.append(", ");
    localStringBuilder.append(mMaxConnsTime);
    localStringBuilder.append(", ");
    localStringBuilder.append(mMtu);
    localStringBuilder.append(", ");
    localStringBuilder.append(mMvnoType);
    localStringBuilder.append(", ");
    localStringBuilder.append(mMvnoMatchData);
    localStringBuilder.append(", ");
    localStringBuilder.append(mPermanentFailed);
    localStringBuilder.append(", ");
    localStringBuilder.append(mNetworkTypeBitmask);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeString(mOperatorNumeric);
    paramParcel.writeString(mEntryName);
    paramParcel.writeString(mApnName);
    paramParcel.writeValue(mProxyAddress);
    paramParcel.writeInt(mProxyPort);
    paramParcel.writeValue(mMmsc);
    paramParcel.writeValue(mMmsProxyAddress);
    paramParcel.writeInt(mMmsProxyPort);
    paramParcel.writeString(mUser);
    paramParcel.writeString(mPassword);
    paramParcel.writeInt(mAuthType);
    paramParcel.writeInt(mApnTypeBitmask);
    paramParcel.writeInt(mProtocol);
    paramParcel.writeInt(mRoamingProtocol);
    paramParcel.writeInt(mCarrierEnabled);
    paramParcel.writeInt(mMvnoType);
    paramParcel.writeInt(mNetworkTypeBitmask);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ApnType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AuthType {}
  
  public static class Builder
  {
    private String mApnName;
    private int mApnTypeBitmask;
    private int mAuthType;
    private boolean mCarrierEnabled;
    private String mEntryName;
    private int mId;
    private int mMaxConns;
    private int mMaxConnsTime;
    private InetAddress mMmsProxyAddress;
    private int mMmsProxyPort = -1;
    private Uri mMmsc;
    private boolean mModemCognitive;
    private int mMtu;
    private String mMvnoMatchData;
    private int mMvnoType = -1;
    private int mNetworkTypeBitmask;
    private String mOperatorNumeric;
    private String mPassword;
    private int mProfileId;
    private int mProtocol = -1;
    private InetAddress mProxyAddress;
    private int mProxyPort = -1;
    private int mRoamingProtocol = -1;
    private String mUser;
    private int mWaitTime;
    
    public Builder() {}
    
    private Builder setId(int paramInt)
    {
      mId = paramInt;
      return this;
    }
    
    public ApnSetting build()
    {
      if (((mApnTypeBitmask & 0x3FF) != 0) && (!TextUtils.isEmpty(mApnName)) && (!TextUtils.isEmpty(mEntryName))) {
        return new ApnSetting(this, null);
      }
      return null;
    }
    
    public Builder setApnName(String paramString)
    {
      mApnName = paramString;
      return this;
    }
    
    public Builder setApnTypeBitmask(int paramInt)
    {
      mApnTypeBitmask = paramInt;
      return this;
    }
    
    public Builder setAuthType(int paramInt)
    {
      mAuthType = paramInt;
      return this;
    }
    
    public Builder setCarrierEnabled(boolean paramBoolean)
    {
      mCarrierEnabled = paramBoolean;
      return this;
    }
    
    public Builder setEntryName(String paramString)
    {
      mEntryName = paramString;
      return this;
    }
    
    public Builder setMaxConns(int paramInt)
    {
      mMaxConns = paramInt;
      return this;
    }
    
    public Builder setMaxConnsTime(int paramInt)
    {
      mMaxConnsTime = paramInt;
      return this;
    }
    
    public Builder setMmsProxyAddress(InetAddress paramInetAddress)
    {
      mMmsProxyAddress = paramInetAddress;
      return this;
    }
    
    public Builder setMmsProxyPort(int paramInt)
    {
      mMmsProxyPort = paramInt;
      return this;
    }
    
    public Builder setMmsc(Uri paramUri)
    {
      mMmsc = paramUri;
      return this;
    }
    
    public Builder setModemCognitive(boolean paramBoolean)
    {
      mModemCognitive = paramBoolean;
      return this;
    }
    
    public Builder setMtu(int paramInt)
    {
      mMtu = paramInt;
      return this;
    }
    
    public Builder setMvnoMatchData(String paramString)
    {
      mMvnoMatchData = paramString;
      return this;
    }
    
    public Builder setMvnoType(int paramInt)
    {
      mMvnoType = paramInt;
      return this;
    }
    
    public Builder setNetworkTypeBitmask(int paramInt)
    {
      mNetworkTypeBitmask = paramInt;
      return this;
    }
    
    public Builder setOperatorNumeric(String paramString)
    {
      mOperatorNumeric = paramString;
      return this;
    }
    
    public Builder setPassword(String paramString)
    {
      mPassword = paramString;
      return this;
    }
    
    public Builder setProfileId(int paramInt)
    {
      mProfileId = paramInt;
      return this;
    }
    
    public Builder setProtocol(int paramInt)
    {
      mProtocol = paramInt;
      return this;
    }
    
    public Builder setProxyAddress(InetAddress paramInetAddress)
    {
      mProxyAddress = paramInetAddress;
      return this;
    }
    
    public Builder setProxyPort(int paramInt)
    {
      mProxyPort = paramInt;
      return this;
    }
    
    public Builder setRoamingProtocol(int paramInt)
    {
      mRoamingProtocol = paramInt;
      return this;
    }
    
    public Builder setUser(String paramString)
    {
      mUser = paramString;
      return this;
    }
    
    public Builder setWaitTime(int paramInt)
    {
      mWaitTime = paramInt;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MvnoType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ProtocolType {}
}
