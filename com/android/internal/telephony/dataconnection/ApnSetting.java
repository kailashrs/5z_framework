package com.android.internal.telephony.dataconnection;

import android.content.Context;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccRecords;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ApnSetting
{
  private static final boolean DBG = false;
  static final String LOG_TAG = "ApnSetting";
  static final String TAG = "ApnSetting";
  static final String V2_FORMAT_REGEX = "^\\[ApnSettingV2\\]\\s*";
  static final String V3_FORMAT_REGEX = "^\\[ApnSettingV3\\]\\s*";
  static final String V4_FORMAT_REGEX = "^\\[ApnSettingV4\\]\\s*";
  static final String V5_FORMAT_REGEX = "^\\[ApnSettingV5\\]\\s*";
  private static final boolean VDBG = false;
  public final String apn;
  public final int apnSetId;
  public final int authType;
  @Deprecated
  private final int bearer;
  @Deprecated
  public final int bearerBitmask;
  public final String carrier;
  public final boolean carrierEnabled;
  public final int id;
  public final int maxConns;
  public final int maxConnsTime;
  public final String mmsPort;
  public final String mmsProxy;
  public final String mmsc;
  public final boolean modemCognitive;
  public final int mtu;
  public final String mvnoMatchData;
  public final String mvnoType;
  public final int networkTypeBitmask;
  public final String numeric;
  public final String password;
  public boolean permanentFailed = false;
  public final String port;
  public final int profileId;
  public final String protocol;
  public final String proxy;
  public final String roamingProtocol;
  public final String[] types;
  public final int typesBitmap;
  public final String user;
  public final int waitTime;
  
  @Deprecated
  public ApnSetting(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, int paramInt2, String[] paramArrayOfString, String paramString11, String paramString12, boolean paramBoolean1, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean2, int paramInt6, int paramInt7, int paramInt8, int paramInt9, String paramString13, String paramString14)
  {
    id = paramInt1;
    numeric = paramString1;
    carrier = paramString2;
    apn = paramString3;
    proxy = paramString4;
    port = paramString5;
    mmsc = paramString6;
    mmsProxy = paramString7;
    mmsPort = paramString8;
    user = paramString9;
    password = paramString10;
    authType = paramInt2;
    types = new String[paramArrayOfString.length];
    paramInt1 = 0;
    paramInt2 = 0;
    while (paramInt1 < paramArrayOfString.length)
    {
      types[paramInt1] = paramArrayOfString[paramInt1].toLowerCase();
      paramInt2 |= getApnBitmask(types[paramInt1]);
      paramInt1++;
    }
    typesBitmap = paramInt2;
    protocol = paramString11;
    roamingProtocol = paramString12;
    carrierEnabled = paramBoolean1;
    bearer = paramInt3;
    bearerBitmask = (paramInt4 | ServiceState.getBitmaskForTech(paramInt3));
    profileId = paramInt5;
    modemCognitive = paramBoolean2;
    maxConns = paramInt6;
    waitTime = paramInt7;
    maxConnsTime = paramInt8;
    mtu = paramInt9;
    mvnoType = paramString13;
    mvnoMatchData = paramString14;
    apnSetId = 0;
    networkTypeBitmask = ServiceState.convertBearerBitmaskToNetworkTypeBitmask(bearerBitmask);
  }
  
  public ApnSetting(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, int paramInt2, String[] paramArrayOfString, String paramString11, String paramString12, boolean paramBoolean1, int paramInt3, int paramInt4, boolean paramBoolean2, int paramInt5, int paramInt6, int paramInt7, int paramInt8, String paramString13, String paramString14)
  {
    this(paramInt1, paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramInt2, paramArrayOfString, paramString11, paramString12, paramBoolean1, paramInt3, paramInt4, paramBoolean2, paramInt5, paramInt6, paramInt7, paramInt8, paramString13, paramString14, 0);
  }
  
  public ApnSetting(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, int paramInt2, String[] paramArrayOfString, String paramString11, String paramString12, boolean paramBoolean1, int paramInt3, int paramInt4, boolean paramBoolean2, int paramInt5, int paramInt6, int paramInt7, int paramInt8, String paramString13, String paramString14, int paramInt9)
  {
    id = paramInt1;
    numeric = paramString1;
    carrier = paramString2;
    apn = paramString3;
    proxy = paramString4;
    port = paramString5;
    mmsc = paramString6;
    mmsProxy = paramString7;
    mmsPort = paramString8;
    user = paramString9;
    password = paramString10;
    authType = paramInt2;
    types = new String[paramArrayOfString.length];
    paramInt1 = 0;
    paramInt2 = 0;
    while (paramInt1 < paramArrayOfString.length)
    {
      types[paramInt1] = paramArrayOfString[paramInt1].toLowerCase();
      paramInt2 |= getApnBitmask(types[paramInt1]);
      paramInt1++;
    }
    typesBitmap = paramInt2;
    protocol = paramString11;
    roamingProtocol = paramString12;
    carrierEnabled = paramBoolean1;
    bearer = 0;
    bearerBitmask = ServiceState.convertNetworkTypeBitmaskToBearerBitmask(paramInt3);
    networkTypeBitmask = paramInt3;
    profileId = paramInt4;
    modemCognitive = paramBoolean2;
    maxConns = paramInt5;
    waitTime = paramInt6;
    maxConnsTime = paramInt7;
    mtu = paramInt8;
    mvnoType = paramString13;
    mvnoMatchData = paramString14;
    apnSetId = paramInt9;
  }
  
  public ApnSetting(ApnSetting paramApnSetting)
  {
    this(id, numeric, carrier, apn, proxy, port, mmsc, mmsProxy, mmsPort, user, password, authType, types, protocol, roamingProtocol, carrierEnabled, networkTypeBitmask, profileId, modemCognitive, maxConns, waitTime, maxConnsTime, mtu, mvnoType, mvnoMatchData, apnSetId);
  }
  
  public static List<ApnSetting> arrayFromString(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    if (TextUtils.isEmpty(paramString)) {
      return localArrayList;
    }
    paramString = paramString.split("\\s*;\\s*");
    int i = paramString.length;
    for (int j = 0; j < i; j++)
    {
      ApnSetting localApnSetting = fromString(paramString[j]);
      if (localApnSetting != null) {
        localArrayList.add(localApnSetting);
      }
    }
    return localArrayList;
  }
  
  public static ApnSetting fromString(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i;
    if (paramString.matches("^\\[ApnSettingV5\\]\\s*.*"))
    {
      i = 5;
      paramString = paramString.replaceFirst("^\\[ApnSettingV5\\]\\s*", "");
    }
    for (;;)
    {
      break;
      if (paramString.matches("^\\[ApnSettingV4\\]\\s*.*"))
      {
        i = 4;
        paramString = paramString.replaceFirst("^\\[ApnSettingV4\\]\\s*", "");
      }
      else if (paramString.matches("^\\[ApnSettingV3\\]\\s*.*"))
      {
        i = 3;
        paramString = paramString.replaceFirst("^\\[ApnSettingV3\\]\\s*", "");
      }
      else if (paramString.matches("^\\[ApnSettingV2\\]\\s*.*"))
      {
        i = 2;
        paramString = paramString.replaceFirst("^\\[ApnSettingV2\\]\\s*", "");
      }
      else
      {
        i = 1;
      }
    }
    String[] arrayOfString1 = paramString.split("\\s*,\\s*");
    if (arrayOfString1.length < 14) {
      return null;
    }
    int j;
    try
    {
      j = Integer.parseInt(arrayOfString1[12]);
    }
    catch (NumberFormatException paramString)
    {
      j = 0;
    }
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    boolean bool1 = false;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    paramString = "";
    String str1 = "";
    String[] arrayOfString2;
    String str2;
    boolean bool2;
    String str4;
    if (i == 1)
    {
      arrayOfString2 = new String[arrayOfString1.length - 13];
      System.arraycopy(arrayOfString1, 13, arrayOfString2, 0, arrayOfString1.length - 13);
      str2 = "IP";
      paramString = "IP";
      i3 = 0;
      bool1 = false;
      n = 0;
      i = 0;
      i7 = 0;
      i5 = 0;
      String str3 = "";
      str1 = "";
      i2 = 0;
      bool2 = true;
      i4 = m;
      m = n;
    }
    else
    {
      if (arrayOfString1.length < 18) {
        return null;
      }
      arrayOfString2 = arrayOfString1[13].split("\\s*\\|\\s*");
      str2 = arrayOfString1[14];
      localObject = arrayOfString1[15];
      bool2 = Boolean.parseBoolean(arrayOfString1[16]);
      k = ServiceState.getBitmaskFromString(arrayOfString1[17]);
      int i9 = arrayOfString1.length;
      i = i1;
      m = i5;
      i5 = i7;
      if (i9 > 22)
      {
        bool1 = Boolean.parseBoolean(arrayOfString1[19]);
        i = i2;
        i3 = i4;
        m = i6;
        try
        {
          i2 = Integer.parseInt(arrayOfString1[18]);
          i = i2;
          i3 = i4;
          m = i6;
          i4 = Integer.parseInt(arrayOfString1[20]);
          i = i2;
          i3 = i4;
          m = i6;
          i5 = Integer.parseInt(arrayOfString1[21]);
          i = i2;
          i3 = i4;
          m = i5;
          i6 = Integer.parseInt(arrayOfString1[22]);
          i7 = i6;
          i = i2;
          i3 = i4;
          m = i5;
          i5 = i7;
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          i5 = i7;
        }
      }
      i7 = i8;
      if (arrayOfString1.length > 23) {
        try
        {
          i7 = Integer.parseInt(arrayOfString1[23]);
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          i7 = i8;
        }
      }
      if (arrayOfString1.length > 25)
      {
        paramString = arrayOfString1[24];
        str1 = arrayOfString1[25];
      }
      i4 = n;
      if (arrayOfString1.length > 26) {
        i4 = ServiceState.getBitmaskFromString(arrayOfString1[26]);
      }
      if (arrayOfString1.length > 27) {
        i2 = Integer.parseInt(arrayOfString1[27]);
      } else {
        i2 = 0;
      }
      i8 = i7;
      i7 = i5;
      n = m;
      m = i3;
      i3 = i;
      str4 = paramString;
      i5 = i8;
      i = n;
      paramString = (String)localObject;
    }
    n = i4;
    if (i4 == 0) {
      n = ServiceState.convertBearerBitmaskToNetworkTypeBitmask(k);
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(arrayOfString1[10]);
    ((StringBuilder)localObject).append(arrayOfString1[11]);
    return new ApnSetting(-1, ((StringBuilder)localObject).toString(), arrayOfString1[0], arrayOfString1[1], arrayOfString1[2], arrayOfString1[3], arrayOfString1[7], arrayOfString1[8], arrayOfString1[9], arrayOfString1[4], arrayOfString1[5], j, arrayOfString2, str2, paramString, bool2, n, i3, bool1, m, i, i7, i5, str4, str1, i2);
  }
  
  private static int getApnBitmask(String paramString)
  {
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 1629013393: 
      if (paramString.equals("emergency")) {
        i = 9;
      }
      break;
    case 1544803905: 
      if (paramString.equals("default")) {
        i = 0;
      }
      break;
    case 99285510: 
      if (paramString.equals("hipri")) {
        i = 4;
      }
      break;
    case 3541982: 
      if (paramString.equals("supl")) {
        i = 2;
      }
      break;
    case 3149046: 
      if (paramString.equals("fota")) {
        i = 5;
      }
      break;
    case 108243: 
      if (paramString.equals("mms")) {
        i = 1;
      }
      break;
    case 104399: 
      if (paramString.equals("ims")) {
        i = 6;
      }
      break;
    case 99837: 
      if (paramString.equals("dun")) {
        i = 3;
      }
      break;
    case 98292: 
      if (paramString.equals("cbs")) {
        i = 7;
      }
      break;
    case 3352: 
      if (paramString.equals("ia")) {
        i = 8;
      }
      break;
    case 42: 
      if (paramString.equals("*")) {
        i = 10;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      return 0;
    case 10: 
      return 1023;
    case 9: 
      return 512;
    case 8: 
      return 256;
    case 7: 
      return 128;
    case 6: 
      return 64;
    case 5: 
      return 32;
    case 4: 
      return 16;
    case 3: 
      return 8;
    case 2: 
      return 4;
    case 1: 
      return 2;
    }
    return 1;
  }
  
  private static boolean iccidMatches(String paramString1, String paramString2)
  {
    paramString1 = paramString1.split(",");
    int i = paramString1.length;
    for (int j = 0; j < i; j++) {
      if (paramString2.startsWith(paramString1[j]))
      {
        Log.d("ApnSetting", "mvno icc id match found");
        return true;
      }
    }
    return false;
  }
  
  private static boolean imsiMatches(String paramString1, String paramString2)
  {
    int i = paramString1.length();
    if (i <= 0) {
      return false;
    }
    if (i > paramString2.length()) {
      return false;
    }
    for (int j = 0; j < i; j++)
    {
      int k = paramString1.charAt(j);
      if ((k != 120) && (k != 88) && (k != paramString2.charAt(j))) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean isMeteredApnType(String paramString, Phone paramPhone)
  {
    if (paramPhone == null) {
      return true;
    }
    boolean bool = paramPhone.getServiceState().getDataRoaming();
    int i;
    if (paramPhone.getServiceState().getRilDataRadioTechnology() == 18) {
      i = 1;
    } else {
      i = 0;
    }
    int j = paramPhone.getSubId();
    String str;
    if (i != 0) {
      str = "carrier_metered_iwlan_apn_types_strings";
    }
    for (;;)
    {
      break;
      if (bool) {
        str = "carrier_metered_roaming_apn_types_strings";
      } else {
        str = "carrier_metered_apn_types_strings";
      }
    }
    paramPhone = (CarrierConfigManager)paramPhone.getContext().getSystemService("carrier_config");
    if (paramPhone == null)
    {
      Rlog.e("ApnSetting", "Carrier config service is not available");
      return true;
    }
    paramPhone = paramPhone.getConfigForSubId(j);
    if (paramPhone == null)
    {
      paramString = new StringBuilder();
      paramString.append("Can't get the config. subId = ");
      paramString.append(j);
      Rlog.e("ApnSetting", paramString.toString());
      return true;
    }
    paramPhone = paramPhone.getStringArray(str);
    if (paramPhone == null)
    {
      paramString = new StringBuilder();
      paramString.append(str);
      paramString.append(" is not available. subId = ");
      paramString.append(j);
      Rlog.e("ApnSetting", paramString.toString());
      return true;
    }
    paramPhone = new HashSet(Arrays.asList(paramPhone));
    if (paramPhone.contains("*")) {
      return true;
    }
    if (paramPhone.contains(paramString)) {
      return true;
    }
    return (paramString.equals("*")) && (paramPhone.size() > 0);
  }
  
  public static boolean mvnoMatches(IccRecords paramIccRecords, String paramString1, String paramString2)
  {
    if (paramString1.equalsIgnoreCase("spn"))
    {
      if ((paramIccRecords.getServiceProviderName() != null) && (paramIccRecords.getServiceProviderName().equalsIgnoreCase(paramString2))) {
        return true;
      }
    }
    else if (paramString1.equalsIgnoreCase("imsi"))
    {
      paramIccRecords = paramIccRecords.getIMSI();
      if ((paramIccRecords != null) && (imsiMatches(paramString2, paramIccRecords))) {
        return true;
      }
    }
    else if (paramString1.equalsIgnoreCase("gid"))
    {
      paramIccRecords = paramIccRecords.getGid1();
      int i = paramString2.length();
      if ((paramIccRecords != null) && (paramIccRecords.length() >= i) && (paramIccRecords.substring(0, i).equalsIgnoreCase(paramString2))) {
        return true;
      }
    }
    else if (paramString1.equalsIgnoreCase("iccid"))
    {
      paramIccRecords = paramIccRecords.getIccId();
      if ((paramIccRecords != null) && (iccidMatches(paramString2, paramIccRecords))) {
        return true;
      }
    }
    return false;
  }
  
  private boolean typeSameAny(ApnSetting paramApnSetting1, ApnSetting paramApnSetting2)
  {
    for (int i = 0; i < types.length; i++)
    {
      int j = 0;
      while (j < types.length) {
        if ((!types[i].equals("*")) && (!types[j].equals("*")) && (!types[i].equals(types[j]))) {
          j++;
        } else {
          return true;
        }
      }
    }
    return false;
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
  
  public boolean canHandleType(String paramString)
  {
    if (!carrierEnabled) {
      return false;
    }
    int i = 1;
    if ("ia".equalsIgnoreCase(paramString)) {
      i = 0;
    }
    String[] arrayOfString = types;
    int j = arrayOfString.length;
    int k = 0;
    while (k < j)
    {
      String str = arrayOfString[k];
      if (paramString.equalsIgnoreCase("dun"))
      {
        if (str.equalsIgnoreCase("dun")) {
          return true;
        }
      }
      else if (paramString.equalsIgnoreCase("ims"))
      {
        if (str.equalsIgnoreCase("ims")) {
          return true;
        }
      }
      else {
        if ((str.equalsIgnoreCase(paramString)) || ((i != 0) && (str.equalsIgnoreCase("*"))) || ((str.equalsIgnoreCase("default")) && (paramString.equalsIgnoreCase("hipri")))) {
          break label146;
        }
      }
      k++;
      continue;
      label146:
      return true;
    }
    return false;
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
    if (carrier.equals(carrier))
    {
      bool1 = bool2;
      if (id == id)
      {
        bool1 = bool2;
        if (numeric.equals(numeric))
        {
          bool1 = bool2;
          if (apn.equals(apn))
          {
            bool1 = bool2;
            if (proxy.equals(proxy))
            {
              bool1 = bool2;
              if (mmsc.equals(mmsc))
              {
                bool1 = bool2;
                if (mmsProxy.equals(mmsProxy))
                {
                  bool1 = bool2;
                  if (TextUtils.equals(mmsPort, mmsPort))
                  {
                    bool1 = bool2;
                    if (port.equals(port))
                    {
                      bool1 = bool2;
                      if (TextUtils.equals(user, user))
                      {
                        bool1 = bool2;
                        if (TextUtils.equals(password, password))
                        {
                          bool1 = bool2;
                          if (authType == authType)
                          {
                            bool1 = bool2;
                            if (Arrays.deepEquals(types, types))
                            {
                              bool1 = bool2;
                              if (typesBitmap == typesBitmap)
                              {
                                bool1 = bool2;
                                if (protocol.equals(protocol))
                                {
                                  bool1 = bool2;
                                  if (roamingProtocol.equals(roamingProtocol))
                                  {
                                    bool1 = bool2;
                                    if (carrierEnabled == carrierEnabled)
                                    {
                                      bool1 = bool2;
                                      if (bearer == bearer)
                                      {
                                        bool1 = bool2;
                                        if (bearerBitmask == bearerBitmask)
                                        {
                                          bool1 = bool2;
                                          if (profileId == profileId)
                                          {
                                            bool1 = bool2;
                                            if (modemCognitive == modemCognitive)
                                            {
                                              bool1 = bool2;
                                              if (maxConns == maxConns)
                                              {
                                                bool1 = bool2;
                                                if (waitTime == waitTime)
                                                {
                                                  bool1 = bool2;
                                                  if (maxConnsTime == maxConnsTime)
                                                  {
                                                    bool1 = bool2;
                                                    if (mtu == mtu)
                                                    {
                                                      bool1 = bool2;
                                                      if (mvnoType.equals(mvnoType))
                                                      {
                                                        bool1 = bool2;
                                                        if (mvnoMatchData.equals(mvnoMatchData))
                                                        {
                                                          bool1 = bool2;
                                                          if (networkTypeBitmask == networkTypeBitmask)
                                                          {
                                                            bool1 = bool2;
                                                            if (apnSetId == apnSetId) {
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
          }
        }
      }
    }
    return bool1;
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public boolean equals(Object paramObject, boolean paramBoolean)
  {
    boolean bool1 = paramObject instanceof ApnSetting;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (ApnSetting)paramObject;
    bool1 = bool2;
    if (carrier.equals(carrier))
    {
      bool1 = bool2;
      if (numeric.equals(numeric))
      {
        bool1 = bool2;
        if (apn.equals(apn))
        {
          bool1 = bool2;
          if (proxy.equals(proxy))
          {
            bool1 = bool2;
            if (mmsc.equals(mmsc))
            {
              bool1 = bool2;
              if (mmsProxy.equals(mmsProxy))
              {
                bool1 = bool2;
                if (TextUtils.equals(mmsPort, mmsPort))
                {
                  bool1 = bool2;
                  if (port.equals(port))
                  {
                    bool1 = bool2;
                    if (TextUtils.equals(user, user))
                    {
                      bool1 = bool2;
                      if (TextUtils.equals(password, password))
                      {
                        bool1 = bool2;
                        if (authType == authType)
                        {
                          bool1 = bool2;
                          if (Arrays.deepEquals(types, types))
                          {
                            bool1 = bool2;
                            if (typesBitmap == typesBitmap) {
                              if (!paramBoolean)
                              {
                                bool1 = bool2;
                                if (!protocol.equals(protocol)) {}
                              }
                              else if (paramBoolean)
                              {
                                bool1 = bool2;
                                if (!roamingProtocol.equals(roamingProtocol)) {}
                              }
                              else
                              {
                                bool1 = bool2;
                                if (carrierEnabled == carrierEnabled)
                                {
                                  bool1 = bool2;
                                  if (profileId == profileId)
                                  {
                                    bool1 = bool2;
                                    if (modemCognitive == modemCognitive)
                                    {
                                      bool1 = bool2;
                                      if (maxConns == maxConns)
                                      {
                                        bool1 = bool2;
                                        if (waitTime == waitTime)
                                        {
                                          bool1 = bool2;
                                          if (maxConnsTime == maxConnsTime)
                                          {
                                            bool1 = bool2;
                                            if (mtu == mtu)
                                            {
                                              bool1 = bool2;
                                              if (mvnoType.equals(mvnoType))
                                              {
                                                bool1 = bool2;
                                                if (mvnoMatchData.equals(mvnoMatchData))
                                                {
                                                  bool1 = bool2;
                                                  if (apnSetId == apnSetId) {
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
    return bool1;
  }
  
  public boolean hasMvnoParams()
  {
    boolean bool;
    if ((!TextUtils.isEmpty(mvnoType)) && (!TextUtils.isEmpty(mvnoMatchData))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isMetered(Phone paramPhone)
  {
    if (paramPhone == null) {
      return true;
    }
    String[] arrayOfString = types;
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (isMeteredApnType(arrayOfString[j], paramPhone)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean similar(ApnSetting paramApnSetting)
  {
    boolean bool;
    if ((!canHandleType("dun")) && (!paramApnSetting.canHandleType("dun")) && (Objects.equals(apn, apn)) && (!typeSameAny(this, paramApnSetting)) && (xorEquals(proxy, proxy)) && (xorEquals(port, port)) && (xorEquals(protocol, protocol)) && (xorEquals(roamingProtocol, roamingProtocol)) && (carrierEnabled == carrierEnabled) && (bearerBitmask == bearerBitmask) && (profileId == profileId) && (Objects.equals(mvnoType, mvnoType)) && (Objects.equals(mvnoMatchData, mvnoMatchData)) && (xorEquals(mmsc, mmsc)) && (xorEquals(mmsProxy, mmsProxy)) && (xorEquals(mmsPort, mmsPort)) && (networkTypeBitmask == networkTypeBitmask) && (apnSetId == apnSetId)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ApnSettingV5] ");
    localStringBuilder.append(carrier);
    localStringBuilder.append(", ");
    localStringBuilder.append(id);
    localStringBuilder.append(", ");
    localStringBuilder.append(numeric);
    localStringBuilder.append(", ");
    localStringBuilder.append(apn);
    localStringBuilder.append(", ");
    localStringBuilder.append(proxy);
    localStringBuilder.append(", ");
    localStringBuilder.append(mmsc);
    localStringBuilder.append(", ");
    localStringBuilder.append(mmsProxy);
    localStringBuilder.append(", ");
    localStringBuilder.append(mmsPort);
    localStringBuilder.append(", ");
    localStringBuilder.append(port);
    localStringBuilder.append(", ");
    localStringBuilder.append(authType);
    localStringBuilder.append(", ");
    for (int i = 0; i < types.length; i++)
    {
      localStringBuilder.append(types[i]);
      if (i < types.length - 1) {
        localStringBuilder.append(" | ");
      }
    }
    localStringBuilder.append(", ");
    localStringBuilder.append(protocol);
    localStringBuilder.append(", ");
    localStringBuilder.append(roamingProtocol);
    localStringBuilder.append(", ");
    localStringBuilder.append(carrierEnabled);
    localStringBuilder.append(", ");
    localStringBuilder.append(bearer);
    localStringBuilder.append(", ");
    localStringBuilder.append(bearerBitmask);
    localStringBuilder.append(", ");
    localStringBuilder.append(profileId);
    localStringBuilder.append(", ");
    localStringBuilder.append(modemCognitive);
    localStringBuilder.append(", ");
    localStringBuilder.append(maxConns);
    localStringBuilder.append(", ");
    localStringBuilder.append(waitTime);
    localStringBuilder.append(", ");
    localStringBuilder.append(maxConnsTime);
    localStringBuilder.append(", ");
    localStringBuilder.append(mtu);
    localStringBuilder.append(", ");
    localStringBuilder.append(mvnoType);
    localStringBuilder.append(", ");
    localStringBuilder.append(mvnoMatchData);
    localStringBuilder.append(", ");
    localStringBuilder.append(permanentFailed);
    localStringBuilder.append(", ");
    localStringBuilder.append(networkTypeBitmask);
    localStringBuilder.append(", ");
    localStringBuilder.append(apnSetId);
    return localStringBuilder.toString();
  }
}
