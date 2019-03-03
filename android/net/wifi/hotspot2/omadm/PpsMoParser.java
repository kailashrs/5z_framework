package android.net.wifi.hotspot2.omadm;

import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.pps.Credential;
import android.net.wifi.hotspot2.pps.Credential.CertificateCredential;
import android.net.wifi.hotspot2.pps.Credential.SimCredential;
import android.net.wifi.hotspot2.pps.Credential.UserCredential;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.net.wifi.hotspot2.pps.Policy;
import android.net.wifi.hotspot2.pps.Policy.RoamingPartner;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xml.sax.SAXException;

public final class PpsMoParser
{
  private static final String NODE_AAA_SERVER_TRUST_ROOT = "AAAServerTrustRoot";
  private static final String NODE_ABLE_TO_SHARE = "AbleToShare";
  private static final String NODE_CERTIFICATE_TYPE = "CertificateType";
  private static final String NODE_CERT_SHA256_FINGERPRINT = "CertSHA256Fingerprint";
  private static final String NODE_CERT_URL = "CertURL";
  private static final String NODE_CHECK_AAA_SERVER_CERT_STATUS = "CheckAAAServerCertStatus";
  private static final String NODE_COUNTRY = "Country";
  private static final String NODE_CREATION_DATE = "CreationDate";
  private static final String NODE_CREDENTIAL = "Credential";
  private static final String NODE_CREDENTIAL_PRIORITY = "CredentialPriority";
  private static final String NODE_DATA_LIMIT = "DataLimit";
  private static final String NODE_DIGITAL_CERTIFICATE = "DigitalCertificate";
  private static final String NODE_DOWNLINK_BANDWIDTH = "DLBandwidth";
  private static final String NODE_EAP_METHOD = "EAPMethod";
  private static final String NODE_EAP_TYPE = "EAPType";
  private static final String NODE_EXPIRATION_DATE = "ExpirationDate";
  private static final String NODE_EXTENSION = "Extension";
  private static final String NODE_FQDN = "FQDN";
  private static final String NODE_FQDN_MATCH = "FQDN_Match";
  private static final String NODE_FRIENDLY_NAME = "FriendlyName";
  private static final String NODE_HESSID = "HESSID";
  private static final String NODE_HOMESP = "HomeSP";
  private static final String NODE_HOME_OI = "HomeOI";
  private static final String NODE_HOME_OI_LIST = "HomeOIList";
  private static final String NODE_HOME_OI_REQUIRED = "HomeOIRequired";
  private static final String NODE_ICON_URL = "IconURL";
  private static final String NODE_INNER_EAP_TYPE = "InnerEAPType";
  private static final String NODE_INNER_METHOD = "InnerMethod";
  private static final String NODE_INNER_VENDOR_ID = "InnerVendorID";
  private static final String NODE_INNER_VENDOR_TYPE = "InnerVendorType";
  private static final String NODE_IP_PROTOCOL = "IPProtocol";
  private static final String NODE_MACHINE_MANAGED = "MachineManaged";
  private static final String NODE_MAXIMUM_BSS_LOAD_VALUE = "MaximumBSSLoadValue";
  private static final String NODE_MIN_BACKHAUL_THRESHOLD = "MinBackhaulThreshold";
  private static final String NODE_NETWORK_ID = "NetworkID";
  private static final String NODE_NETWORK_TYPE = "NetworkType";
  private static final String NODE_OTHER = "Other";
  private static final String NODE_OTHER_HOME_PARTNERS = "OtherHomePartners";
  private static final String NODE_PASSWORD = "Password";
  private static final String NODE_PER_PROVIDER_SUBSCRIPTION = "PerProviderSubscription";
  private static final String NODE_POLICY = "Policy";
  private static final String NODE_POLICY_UPDATE = "PolicyUpdate";
  private static final String NODE_PORT_NUMBER = "PortNumber";
  private static final String NODE_PREFERRED_ROAMING_PARTNER_LIST = "PreferredRoamingPartnerList";
  private static final String NODE_PRIORITY = "Priority";
  private static final String NODE_REALM = "Realm";
  private static final String NODE_REQUIRED_PROTO_PORT_TUPLE = "RequiredProtoPortTuple";
  private static final String NODE_RESTRICTION = "Restriction";
  private static final String NODE_ROAMING_CONSORTIUM_OI = "RoamingConsortiumOI";
  private static final String NODE_SIM = "SIM";
  private static final String NODE_SIM_IMSI = "IMSI";
  private static final String NODE_SOFT_TOKEN_APP = "SoftTokenApp";
  private static final String NODE_SP_EXCLUSION_LIST = "SPExclusionList";
  private static final String NODE_SSID = "SSID";
  private static final String NODE_START_DATE = "StartDate";
  private static final String NODE_SUBSCRIPTION_PARAMETER = "SubscriptionParameter";
  private static final String NODE_SUBSCRIPTION_UPDATE = "SubscriptionUpdate";
  private static final String NODE_TIME_LIMIT = "TimeLimit";
  private static final String NODE_TRUST_ROOT = "TrustRoot";
  private static final String NODE_TYPE_OF_SUBSCRIPTION = "TypeOfSubscription";
  private static final String NODE_UPDATE_IDENTIFIER = "UpdateIdentifier";
  private static final String NODE_UPDATE_INTERVAL = "UpdateInterval";
  private static final String NODE_UPDATE_METHOD = "UpdateMethod";
  private static final String NODE_UPLINK_BANDWIDTH = "ULBandwidth";
  private static final String NODE_URI = "URI";
  private static final String NODE_USAGE_LIMITS = "UsageLimits";
  private static final String NODE_USAGE_TIME_PERIOD = "UsageTimePeriod";
  private static final String NODE_USERNAME = "Username";
  private static final String NODE_USERNAME_PASSWORD = "UsernamePassword";
  private static final String NODE_VENDOR_ID = "VendorId";
  private static final String NODE_VENDOR_TYPE = "VendorType";
  private static final String PPS_MO_URN = "urn:wfa:mo:hotspot2dot0-perprovidersubscription:1.0";
  private static final String TAG = "PpsMoParser";
  private static final String TAG_DDF_NAME = "DDFName";
  private static final String TAG_MANAGEMENT_TREE = "MgmtTree";
  private static final String TAG_NODE = "Node";
  private static final String TAG_NODE_NAME = "NodeName";
  private static final String TAG_RT_PROPERTIES = "RTProperties";
  private static final String TAG_TYPE = "Type";
  private static final String TAG_VALUE = "Value";
  private static final String TAG_VER_DTD = "VerDTD";
  
  public PpsMoParser() {}
  
  private static PPSNode buildPpsNode(XMLNode paramXMLNode)
    throws PpsMoParser.ParsingException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    ArrayList localArrayList = new ArrayList();
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramXMLNode.getChildren().iterator();
    paramXMLNode = (XMLNode)localObject1;
    while (localIterator.hasNext())
    {
      XMLNode localXMLNode = (XMLNode)localIterator.next();
      localObject1 = localXMLNode.getTag();
      if (TextUtils.equals((CharSequence)localObject1, "NodeName"))
      {
        if (paramXMLNode == null) {
          paramXMLNode = localXMLNode.getText();
        } else {
          throw new ParsingException("Duplicate NodeName node");
        }
      }
      else if (TextUtils.equals((CharSequence)localObject1, "Node"))
      {
        localObject1 = buildPpsNode(localXMLNode);
        if (!localHashSet.contains(((PPSNode)localObject1).getName()))
        {
          localHashSet.add(((PPSNode)localObject1).getName());
          localArrayList.add(localObject1);
        }
        else
        {
          paramXMLNode = new StringBuilder();
          paramXMLNode.append("Duplicate node: ");
          paramXMLNode.append(((PPSNode)localObject1).getName());
          throw new ParsingException(paramXMLNode.toString());
        }
      }
      else
      {
        if (!TextUtils.equals((CharSequence)localObject1, "Value")) {
          break label218;
        }
        if (localObject2 != null) {
          break label207;
        }
        localObject2 = localXMLNode.getText();
      }
      continue;
      label207:
      throw new ParsingException("Duplicate Value node");
      label218:
      paramXMLNode = new StringBuilder();
      paramXMLNode.append("Unknown tag: ");
      paramXMLNode.append((String)localObject1);
      throw new ParsingException(paramXMLNode.toString());
    }
    if (paramXMLNode != null)
    {
      if ((localObject2 == null) && (localArrayList.size() == 0))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Invalid node: ");
        ((StringBuilder)localObject2).append(paramXMLNode);
        ((StringBuilder)localObject2).append(" missing both value and children");
        throw new ParsingException(((StringBuilder)localObject2).toString());
      }
      if ((localObject2 != null) && (localArrayList.size() > 0))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Invalid node: ");
        ((StringBuilder)localObject2).append(paramXMLNode);
        ((StringBuilder)localObject2).append(" contained both value and children");
        throw new ParsingException(((StringBuilder)localObject2).toString());
      }
      if (localObject2 != null) {
        return new LeafNode(paramXMLNode, (String)localObject2);
      }
      return new InternalNode(paramXMLNode, localArrayList);
    }
    throw new ParsingException("Invalid node: missing NodeName");
  }
  
  private static long[] convertFromLongList(List<Long> paramList)
  {
    Long[] arrayOfLong = (Long[])paramList.toArray(new Long[paramList.size()]);
    paramList = new long[arrayOfLong.length];
    for (int i = 0; i < arrayOfLong.length; i++) {
      paramList[i] = arrayOfLong[i].longValue();
    }
    return paramList;
  }
  
  private static String getPpsNodeValue(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (paramPPSNode.isLeaf()) {
      return paramPPSNode.getValue();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Cannot get value from a non-leaf node: ");
    localStringBuilder.append(paramPPSNode.getName());
    throw new ParsingException(localStringBuilder.toString());
  }
  
  private static Map<String, byte[]> parseAAAServerTrustRootList(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      HashMap localHashMap = new HashMap();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext())
      {
        Pair localPair = parseTrustRoot((PPSNode)paramPPSNode.next());
        localHashMap.put((String)first, (byte[])second);
      }
      return localHashMap;
    }
    throw new ParsingException("Leaf node not expected for AAAServerTrustRoot");
  }
  
  private static Credential.CertificateCredential parseCertificateCredential(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Object localObject = new Credential.CertificateCredential();
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        String str = paramPPSNode.getName();
        int i = -1;
        int j = str.hashCode();
        if (j != -1914611375)
        {
          if ((j == -285451687) && (str.equals("CertSHA256Fingerprint"))) {
            i = 1;
          }
        }
        else if (str.equals("CertificateType")) {
          i = 0;
        }
        switch (i)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown node under DigitalCertificate: ");
          ((StringBuilder)localObject).append(paramPPSNode.getName());
          throw new ParsingException(((StringBuilder)localObject).toString());
        case 1: 
          ((Credential.CertificateCredential)localObject).setCertSha256Fingerprint(parseHexString(getPpsNodeValue(paramPPSNode)));
          break;
        case 0: 
          ((Credential.CertificateCredential)localObject).setCertType(getPpsNodeValue(paramPPSNode));
        }
      }
      return localObject;
    }
    throw new ParsingException("Leaf node not expected for DigitalCertificate");
  }
  
  private static Credential parseCredential(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Object localObject = new Credential();
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        String str = paramPPSNode.getName();
        int i = -1;
        switch (str.hashCode())
        {
        default: 
          break;
        case 1749851981: 
          if (str.equals("CreationDate")) {
            i = 0;
          }
          break;
        case 646045490: 
          if (str.equals("CheckAAAServerCertStatus")) {
            i = 5;
          }
          break;
        case 494843313: 
          if (str.equals("UsernamePassword")) {
            i = 2;
          }
          break;
        case 78834287: 
          if (str.equals("Realm")) {
            i = 4;
          }
          break;
        case 82103: 
          if (str.equals("SIM")) {
            i = 6;
          }
          break;
        case -1208321921: 
          if (str.equals("DigitalCertificate")) {
            i = 3;
          }
          break;
        case -1670804707: 
          if (str.equals("ExpirationDate")) {
            i = 1;
          }
          break;
        }
        switch (i)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown node under Credential: ");
          ((StringBuilder)localObject).append(paramPPSNode.getName());
          throw new ParsingException(((StringBuilder)localObject).toString());
        case 6: 
          ((Credential)localObject).setSimCredential(parseSimCredential(paramPPSNode));
          break;
        case 5: 
          ((Credential)localObject).setCheckAaaServerCertStatus(Boolean.parseBoolean(getPpsNodeValue(paramPPSNode)));
          break;
        case 4: 
          ((Credential)localObject).setRealm(getPpsNodeValue(paramPPSNode));
          break;
        case 3: 
          ((Credential)localObject).setCertCredential(parseCertificateCredential(paramPPSNode));
          break;
        case 2: 
          ((Credential)localObject).setUserCredential(parseUserCredential(paramPPSNode));
          break;
        case 1: 
          ((Credential)localObject).setExpirationTimeInMillis(parseDate(getPpsNodeValue(paramPPSNode)));
          break;
        case 0: 
          ((Credential)localObject).setCreationTimeInMillis(parseDate(getPpsNodeValue(paramPPSNode)));
        }
      }
      return localObject;
    }
    throw new ParsingException("Leaf node not expected for HomeSP");
  }
  
  private static long parseDate(String paramString)
    throws PpsMoParser.ParsingException
  {
    try
    {
      SimpleDateFormat localSimpleDateFormat = new java/text/SimpleDateFormat;
      localSimpleDateFormat.<init>("yyyy-MM-dd'T'HH:mm:ss'Z'");
      long l = localSimpleDateFormat.parse(paramString).getTime();
      return l;
    }
    catch (ParseException localParseException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Badly formatted time: ");
      localStringBuilder.append(paramString);
      throw new ParsingException(localStringBuilder.toString());
    }
  }
  
  private static void parseEAPMethod(PPSNode paramPPSNode, Credential.UserCredential paramUserCredential)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        Object localObject = paramPPSNode.getName();
        int i = -1;
        switch (((String)localObject).hashCode())
        {
        default: 
          break;
        case 961456313: 
          if (((String)localObject).equals("InnerVendorID")) {
            i = 5;
          }
          break;
        case 901061303: 
          if (((String)localObject).equals("InnerMethod")) {
            i = 1;
          }
          break;
        case 541930360: 
          if (((String)localObject).equals("InnerVendorType")) {
            i = 6;
          }
          break;
        case -1249356658: 
          if (((String)localObject).equals("EAPType")) {
            i = 0;
          }
          break;
        case -1607163710: 
          if (((String)localObject).equals("VendorType")) {
            i = 3;
          }
          break;
        case -1706447464: 
          if (((String)localObject).equals("InnerEAPType")) {
            i = 4;
          }
          break;
        case -2048597853: 
          if (((String)localObject).equals("VendorId")) {
            i = 2;
          }
          break;
        }
        switch (i)
        {
        default: 
          paramUserCredential = new StringBuilder();
          paramUserCredential.append("Unknown node under EAPMethod: ");
          paramUserCredential.append(paramPPSNode.getName());
          throw new ParsingException(paramUserCredential.toString());
        case 2: 
        case 3: 
        case 4: 
        case 5: 
        case 6: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Ignore unsupported EAP method parameter: ");
          ((StringBuilder)localObject).append(paramPPSNode.getName());
          Log.d("PpsMoParser", ((StringBuilder)localObject).toString());
          break;
        case 1: 
          paramUserCredential.setNonEapInnerMethod(getPpsNodeValue(paramPPSNode));
          break;
        case 0: 
          paramUserCredential.setEapType(parseInteger(getPpsNodeValue(paramPPSNode)));
        }
      }
      return;
    }
    throw new ParsingException("Leaf node not expected for EAPMethod");
  }
  
  private static byte[] parseHexString(String paramString)
    throws PpsMoParser.ParsingException
  {
    if ((paramString.length() & 0x1) != 1)
    {
      byte[] arrayOfByte = new byte[paramString.length() / 2];
      int i = 0;
      while (i < arrayOfByte.length)
      {
        int j = i * 2;
        try
        {
          arrayOfByte[i] = ((byte)(byte)Integer.parseInt(paramString.substring(j, j + 2), 16));
          i++;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid hex string: ");
          localStringBuilder.append(paramString);
          throw new ParsingException(localStringBuilder.toString());
        }
      }
      return localStringBuilder;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Odd length hex string: ");
    localStringBuilder.append(paramString.length());
    throw new ParsingException(localStringBuilder.toString());
  }
  
  private static Pair<Long, Boolean> parseHomeOIInstance(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Long localLong = null;
      PPSNode localPPSNode = null;
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      paramPPSNode = localPPSNode;
      while (localIterator.hasNext())
      {
        localPPSNode = (PPSNode)localIterator.next();
        String str = localPPSNode.getName();
        int i = -1;
        int j = str.hashCode();
        if (j != -2127810791)
        {
          if ((j == -1935174184) && (str.equals("HomeOIRequired"))) {
            i = 1;
          }
        }
        else if (str.equals("HomeOI")) {
          i = 0;
        }
        switch (i)
        {
        default: 
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Unknown node under NetworkID instance: ");
          paramPPSNode.append(localPPSNode.getName());
          throw new ParsingException(paramPPSNode.toString());
        case 1: 
          paramPPSNode = Boolean.valueOf(getPpsNodeValue(localPPSNode));
          break;
        }
        try
        {
          localLong = Long.valueOf(getPpsNodeValue(localPPSNode), 16);
        }
        catch (NumberFormatException paramPPSNode)
        {
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Invalid HomeOI: ");
          paramPPSNode.append(getPpsNodeValue(localPPSNode));
          throw new ParsingException(paramPPSNode.toString());
        }
      }
      if (localLong != null)
      {
        if (paramPPSNode != null) {
          return new Pair(localLong, paramPPSNode);
        }
        throw new ParsingException("HomeOI instance missing required field");
      }
      throw new ParsingException("HomeOI instance missing OI field");
    }
    throw new ParsingException("Leaf node not expected for HomeOI instance");
  }
  
  private static Pair<List<Long>, List<Long>> parseHomeOIList(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      ArrayList localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext())
      {
        Pair localPair = parseHomeOIInstance((PPSNode)paramPPSNode.next());
        if (((Boolean)second).booleanValue()) {
          localArrayList1.add((Long)first);
        } else {
          localArrayList2.add((Long)first);
        }
      }
      return new Pair(localArrayList1, localArrayList2);
    }
    throw new ParsingException("Leaf node not expected for HomeOIList");
  }
  
  private static HomeSp parseHomeSP(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      HomeSp localHomeSp = new HomeSp();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext())
      {
        Object localObject = (PPSNode)paramPPSNode.next();
        String str = ((PPSNode)localObject).getName();
        int i = -1;
        switch (str.hashCode())
        {
        default: 
          break;
        case 1956561338: 
          if (str.equals("OtherHomePartners")) {
            i = 6;
          }
          break;
        case 626253302: 
          if (str.equals("FriendlyName")) {
            i = 1;
          }
          break;
        case 542998228: 
          if (str.equals("RoamingConsortiumOI")) {
            i = 2;
          }
          break;
        case 2165397: 
          if (str.equals("FQDN")) {
            i = 0;
          }
          break;
        case -228216919: 
          if (str.equals("NetworkID")) {
            i = 4;
          }
          break;
        case -991549930: 
          if (str.equals("IconURL")) {
            i = 3;
          }
          break;
        case -1560207529: 
          if (str.equals("HomeOIList")) {
            i = 5;
          }
          break;
        }
        switch (i)
        {
        default: 
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Unknown node under HomeSP: ");
          paramPPSNode.append(((PPSNode)localObject).getName());
          throw new ParsingException(paramPPSNode.toString());
        case 6: 
          localHomeSp.setOtherHomePartners(parseOtherHomePartners((PPSNode)localObject));
          break;
        case 5: 
          localObject = parseHomeOIList((PPSNode)localObject);
          localHomeSp.setMatchAllOis(convertFromLongList((List)first));
          localHomeSp.setMatchAnyOis(convertFromLongList((List)second));
          break;
        case 4: 
          localHomeSp.setHomeNetworkIds(parseNetworkIds((PPSNode)localObject));
          break;
        case 3: 
          localHomeSp.setIconUrl(getPpsNodeValue((PPSNode)localObject));
          break;
        case 2: 
          localHomeSp.setRoamingConsortiumOis(parseRoamingConsortiumOI(getPpsNodeValue((PPSNode)localObject)));
          break;
        case 1: 
          localHomeSp.setFriendlyName(getPpsNodeValue((PPSNode)localObject));
          break;
        case 0: 
          localHomeSp.setFqdn(getPpsNodeValue((PPSNode)localObject));
        }
      }
      return localHomeSp;
    }
    throw new ParsingException("Leaf node not expected for HomeSP");
  }
  
  private static int parseInteger(String paramString)
    throws PpsMoParser.ParsingException
  {
    try
    {
      int i = Integer.parseInt(paramString);
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid integer value: ");
      localStringBuilder.append(paramString);
      throw new ParsingException(localStringBuilder.toString());
    }
  }
  
  private static long parseLong(String paramString, int paramInt)
    throws PpsMoParser.ParsingException
  {
    try
    {
      long l = Long.parseLong(paramString, paramInt);
      return l;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid long integer value: ");
      localStringBuilder.append(paramString);
      throw new ParsingException(localStringBuilder.toString());
    }
  }
  
  private static void parseMinBackhaulThreshold(PPSNode paramPPSNode, Policy paramPolicy)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext()) {
        parseMinBackhaulThresholdInstance((PPSNode)paramPPSNode.next(), paramPolicy);
      }
      return;
    }
    throw new ParsingException("Leaf node not expected for MinBackhaulThreshold");
  }
  
  private static void parseMinBackhaulThresholdInstance(PPSNode paramPPSNode, Policy paramPolicy)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      PPSNode localPPSNode = null;
      long l1 = Long.MIN_VALUE;
      long l2 = Long.MIN_VALUE;
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      paramPPSNode = localPPSNode;
      while (localIterator.hasNext())
      {
        localPPSNode = (PPSNode)localIterator.next();
        String str = localPPSNode.getName();
        int i = -1;
        int j = str.hashCode();
        if (j != -272744856)
        {
          if (j != -133967910)
          {
            if ((j == 349434121) && (str.equals("DLBandwidth"))) {
              i = 1;
            }
          }
          else if (str.equals("ULBandwidth")) {
            i = 2;
          }
        }
        else if (str.equals("NetworkType")) {
          i = 0;
        }
        switch (i)
        {
        default: 
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Unknown node under MinBackhaulThreshold instance ");
          paramPPSNode.append(localPPSNode.getName());
          throw new ParsingException(paramPPSNode.toString());
        case 2: 
          l2 = parseLong(getPpsNodeValue(localPPSNode), 10);
          break;
        case 1: 
          l1 = parseLong(getPpsNodeValue(localPPSNode), 10);
          break;
        case 0: 
          paramPPSNode = getPpsNodeValue(localPPSNode);
        }
      }
      if (paramPPSNode != null)
      {
        if (TextUtils.equals(paramPPSNode, "home"))
        {
          paramPolicy.setMinHomeDownlinkBandwidth(l1);
          paramPolicy.setMinHomeUplinkBandwidth(l2);
        }
        else
        {
          if (!TextUtils.equals(paramPPSNode, "roaming")) {
            break label290;
          }
          paramPolicy.setMinRoamingDownlinkBandwidth(l1);
          paramPolicy.setMinRoamingUplinkBandwidth(l2);
        }
        return;
        label290:
        paramPolicy = new StringBuilder();
        paramPolicy.append("Invalid network type: ");
        paramPolicy.append(paramPPSNode);
        throw new ParsingException(paramPolicy.toString());
      }
      throw new ParsingException("Missing NetworkType field");
    }
    throw new ParsingException("Leaf node not expected for MinBackhaulThreshold instance");
  }
  
  public static PasspointConfiguration parseMoText(String paramString)
  {
    Object localObject1 = new XMLParser();
    try
    {
      Object localObject2 = ((XMLParser)localObject1).parse(paramString);
      if (localObject2 == null) {
        return null;
      }
      if (((XMLNode)localObject2).getTag() != "MgmtTree")
      {
        Log.e("PpsMoParser", "Root is not a MgmtTree");
        return null;
      }
      paramString = null;
      localObject1 = null;
      localObject2 = ((XMLNode)localObject2).getChildren().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        XMLNode localXMLNode = (XMLNode)((Iterator)localObject2).next();
        String str = localXMLNode.getTag();
        int i = -1;
        int j = str.hashCode();
        if (j != -1736120495)
        {
          if ((j == 2433570) && (str.equals("Node"))) {
            i = 1;
          }
        }
        else if (str.equals("VerDTD")) {
          i = 0;
        }
        switch (i)
        {
        default: 
          paramString = new StringBuilder();
          paramString.append("Unknown node: ");
          paramString.append(localXMLNode.getTag());
          Log.e("PpsMoParser", paramString.toString());
          return null;
        case 1: 
          if (localObject1 != null)
          {
            Log.e("PpsMoParser", "Unexpected multiple Node element under MgmtTree");
            return null;
          }
          try
          {
            localObject1 = parsePpsNode(localXMLNode);
          }
          catch (ParsingException paramString)
          {
            Log.e("PpsMoParser", paramString.getMessage());
            return null;
          }
        case 0: 
          if (paramString != null)
          {
            Log.e("PpsMoParser", "Duplicate VerDTD element");
            return null;
          }
          paramString = localXMLNode.getText();
        }
      }
      return localObject1;
    }
    catch (IOException|SAXException paramString) {}
    return null;
  }
  
  private static Pair<String, Long> parseNetworkIdInstance(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      PPSNode localPPSNode = null;
      Long localLong = null;
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      paramPPSNode = localPPSNode;
      while (localIterator.hasNext())
      {
        localPPSNode = (PPSNode)localIterator.next();
        String str = localPPSNode.getName();
        int i = -1;
        int j = str.hashCode();
        if (j != 2554747)
        {
          if ((j == 2127576568) && (str.equals("HESSID"))) {
            i = 1;
          }
        }
        else if (str.equals("SSID")) {
          i = 0;
        }
        switch (i)
        {
        default: 
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Unknown node under NetworkID instance: ");
          paramPPSNode.append(localPPSNode.getName());
          throw new ParsingException(paramPPSNode.toString());
        case 1: 
          localLong = Long.valueOf(parseLong(getPpsNodeValue(localPPSNode), 16));
          break;
        case 0: 
          paramPPSNode = getPpsNodeValue(localPPSNode);
        }
      }
      if (paramPPSNode != null) {
        return new Pair(paramPPSNode, localLong);
      }
      throw new ParsingException("NetworkID instance missing SSID");
    }
    throw new ParsingException("Leaf node not expected for NetworkID instance");
  }
  
  private static Map<String, Long> parseNetworkIds(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      HashMap localHashMap = new HashMap();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext())
      {
        Pair localPair = parseNetworkIdInstance((PPSNode)paramPPSNode.next());
        localHashMap.put((String)first, (Long)second);
      }
      return localHashMap;
    }
    throw new ParsingException("Leaf node not expected for NetworkID");
  }
  
  private static String parseOtherHomePartnerInstance(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Object localObject = null;
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      paramPPSNode = (PPSNode)localObject;
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        localObject = paramPPSNode.getName();
        int i = -1;
        if ((((String)localObject).hashCode() == 2165397) && (((String)localObject).equals("FQDN"))) {
          i = 0;
        }
        if (i == 0)
        {
          paramPPSNode = getPpsNodeValue(paramPPSNode);
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown node under OtherHomePartner instance: ");
          ((StringBuilder)localObject).append(paramPPSNode.getName());
          throw new ParsingException(((StringBuilder)localObject).toString());
        }
      }
      if (paramPPSNode != null) {
        return paramPPSNode;
      }
      throw new ParsingException("OtherHomePartner instance missing FQDN field");
    }
    throw new ParsingException("Leaf node not expected for OtherHomePartner instance");
  }
  
  private static String[] parseOtherHomePartners(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      ArrayList localArrayList = new ArrayList();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext()) {
        localArrayList.add(parseOtherHomePartnerInstance((PPSNode)paramPPSNode.next()));
      }
      return (String[])localArrayList.toArray(new String[localArrayList.size()]);
    }
    throw new ParsingException("Leaf node not expected for OtherHomePartners");
  }
  
  private static Policy parsePolicy(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Object localObject = new Policy();
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        String str = paramPPSNode.getName();
        int i = -1;
        switch (str.hashCode())
        {
        default: 
          break;
        case 1337803246: 
          if (str.equals("PreferredRoamingPartnerList")) {
            i = 0;
          }
          break;
        case 783647838: 
          if (str.equals("RequiredProtoPortTuple")) {
            i = 4;
          }
          break;
        case 586018863: 
          if (str.equals("SPExclusionList")) {
            i = 3;
          }
          break;
        case -166875607: 
          if (str.equals("MaximumBSSLoadValue")) {
            i = 5;
          }
          break;
        case -281271454: 
          if (str.equals("MinBackhaulThreshold")) {
            i = 1;
          }
          break;
        case -1710886725: 
          if (str.equals("PolicyUpdate")) {
            i = 2;
          }
          break;
        }
        switch (i)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown node under Policy: ");
          ((StringBuilder)localObject).append(paramPPSNode.getName());
          throw new ParsingException(((StringBuilder)localObject).toString());
        case 5: 
          ((Policy)localObject).setMaximumBssLoadValue(parseInteger(getPpsNodeValue(paramPPSNode)));
          break;
        case 4: 
          ((Policy)localObject).setRequiredProtoPortMap(parseRequiredProtoPortTuple(paramPPSNode));
          break;
        case 3: 
          ((Policy)localObject).setExcludedSsidList(parseSpExclusionList(paramPPSNode));
          break;
        case 2: 
          ((Policy)localObject).setPolicyUpdate(parseUpdateParameter(paramPPSNode));
          break;
        case 1: 
          parseMinBackhaulThreshold(paramPPSNode, (Policy)localObject);
          break;
        case 0: 
          ((Policy)localObject).setPreferredRoamingPartnerList(parsePreferredRoamingPartnerList(paramPPSNode));
        }
      }
      return localObject;
    }
    throw new ParsingException("Leaf node not expected for Policy");
  }
  
  private static PasspointConfiguration parsePpsInstance(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Object localObject = new PasspointConfiguration();
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        String str = paramPPSNode.getName();
        int i = -1;
        switch (str.hashCode())
        {
        default: 
          break;
        case 2017737531: 
          if (str.equals("CredentialPriority")) {
            i = 6;
          }
          break;
        case 1391410207: 
          if (str.equals("Extension")) {
            i = 7;
          }
          break;
        case 1310049399: 
          if (str.equals("Credential")) {
            i = 1;
          }
          break;
        case 314411254: 
          if (str.equals("AAAServerTrustRoot")) {
            i = 3;
          }
          break;
        case 162345062: 
          if (str.equals("SubscriptionUpdate")) {
            i = 4;
          }
          break;
        case -102647060: 
          if (str.equals("SubscriptionParameter")) {
            i = 5;
          }
          break;
        case -1898802862: 
          if (str.equals("Policy")) {
            i = 2;
          }
          break;
        case -2127810660: 
          if (str.equals("HomeSP")) {
            i = 0;
          }
          break;
        }
        switch (i)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown node: ");
          ((StringBuilder)localObject).append(paramPPSNode.getName());
          throw new ParsingException(((StringBuilder)localObject).toString());
        case 7: 
          Log.d("PpsMoParser", "Ignore Extension node for vendor specific information");
          break;
        case 6: 
          ((PasspointConfiguration)localObject).setCredentialPriority(parseInteger(getPpsNodeValue(paramPPSNode)));
          break;
        case 5: 
          parseSubscriptionParameter(paramPPSNode, (PasspointConfiguration)localObject);
          break;
        case 4: 
          ((PasspointConfiguration)localObject).setSubscriptionUpdate(parseUpdateParameter(paramPPSNode));
          break;
        case 3: 
          ((PasspointConfiguration)localObject).setTrustRootCertList(parseAAAServerTrustRootList(paramPPSNode));
          break;
        case 2: 
          ((PasspointConfiguration)localObject).setPolicy(parsePolicy(paramPPSNode));
          break;
        case 1: 
          ((PasspointConfiguration)localObject).setCredential(parseCredential(paramPPSNode));
          break;
        case 0: 
          ((PasspointConfiguration)localObject).setHomeSp(parseHomeSP(paramPPSNode));
        }
      }
      return localObject;
    }
    throw new ParsingException("Leaf node not expected for PPS instance");
  }
  
  private static PasspointConfiguration parsePpsNode(XMLNode paramXMLNode)
    throws PpsMoParser.ParsingException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    int i = Integer.MIN_VALUE;
    Iterator localIterator = paramXMLNode.getChildren().iterator();
    paramXMLNode = (XMLNode)localObject2;
    while (localIterator.hasNext())
    {
      localObject2 = (XMLNode)localIterator.next();
      String str = ((XMLNode)localObject2).getTag();
      int j = -1;
      int k = str.hashCode();
      if (k != -1852765931)
      {
        if (k != 2433570)
        {
          if ((k == 1187524557) && (str.equals("NodeName"))) {
            j = 0;
          }
        }
        else if (str.equals("Node")) {
          j = 1;
        }
      }
      else if (str.equals("RTProperties")) {
        j = 2;
      }
      switch (j)
      {
      default: 
        paramXMLNode = new StringBuilder();
        paramXMLNode.append("Unknown tag under PPS node: ");
        paramXMLNode.append(((XMLNode)localObject2).getTag());
        throw new ParsingException(paramXMLNode.toString());
      case 2: 
        localObject2 = parseUrn((XMLNode)localObject2);
        if (!TextUtils.equals((CharSequence)localObject2, "urn:wfa:mo:hotspot2dot0-perprovidersubscription:1.0"))
        {
          paramXMLNode = new StringBuilder();
          paramXMLNode.append("Unknown URN: ");
          paramXMLNode.append((String)localObject2);
          throw new ParsingException(paramXMLNode.toString());
        }
        break;
      case 1: 
        localObject2 = buildPpsNode((XMLNode)localObject2);
        if (TextUtils.equals(((PPSNode)localObject2).getName(), "UpdateIdentifier"))
        {
          if (i == Integer.MIN_VALUE) {
            i = parseInteger(getPpsNodeValue((PPSNode)localObject2));
          } else {
            throw new ParsingException("Multiple node for UpdateIdentifier");
          }
        }
        else if (localObject1 == null) {
          localObject1 = parsePpsInstance((PPSNode)localObject2);
        } else {
          throw new ParsingException("Multiple PPS instance");
        }
        break;
      case 0: 
        if (paramXMLNode != null) {
          break label372;
        }
        paramXMLNode = ((XMLNode)localObject2).getText();
        if (TextUtils.equals(paramXMLNode, "PerProviderSubscription")) {
          continue;
        }
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Unexpected NodeName: ");
      ((StringBuilder)localObject1).append(paramXMLNode);
      throw new ParsingException(((StringBuilder)localObject1).toString());
      label372:
      paramXMLNode = new StringBuilder();
      paramXMLNode.append("Duplicate NodeName: ");
      paramXMLNode.append(((XMLNode)localObject2).getText());
      throw new ParsingException(paramXMLNode.toString());
    }
    if ((localObject1 != null) && (i != Integer.MIN_VALUE)) {
      ((PasspointConfiguration)localObject1).setUpdateIdentifier(i);
    }
    return localObject1;
  }
  
  private static Policy.RoamingPartner parsePreferredRoamingPartner(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Object localObject1 = new Policy.RoamingPartner();
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        Object localObject2 = paramPPSNode.getName();
        int i = -1;
        int j = ((String)localObject2).hashCode();
        if (j != -1672482954)
        {
          if (j != -1100816956)
          {
            if ((j == 305746811) && (((String)localObject2).equals("FQDN_Match"))) {
              i = 0;
            }
          }
          else if (((String)localObject2).equals("Priority")) {
            i = 1;
          }
        }
        else if (((String)localObject2).equals("Country")) {
          i = 2;
        }
        switch (i)
        {
        default: 
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unknown node under PreferredRoamingPartnerList instance ");
          ((StringBuilder)localObject1).append(paramPPSNode.getName());
          throw new ParsingException(((StringBuilder)localObject1).toString());
        case 2: 
          ((Policy.RoamingPartner)localObject1).setCountries(getPpsNodeValue(paramPPSNode));
          break;
        case 1: 
          ((Policy.RoamingPartner)localObject1).setPriority(parseInteger(getPpsNodeValue(paramPPSNode)));
          break;
        case 0: 
          paramPPSNode = getPpsNodeValue(paramPPSNode);
          localObject2 = paramPPSNode.split(",");
          if (localObject2.length != 2) {
            break label318;
          }
          ((Policy.RoamingPartner)localObject1).setFqdn(localObject2[0]);
          if (TextUtils.equals(localObject2[1], "exactMatch"))
          {
            ((Policy.RoamingPartner)localObject1).setFqdnExactMatch(true);
          }
          else
          {
            if (!TextUtils.equals(localObject2[1], "includeSubdomains")) {
              break label284;
            }
            ((Policy.RoamingPartner)localObject1).setFqdnExactMatch(false);
          }
          break;
        }
        continue;
        label284:
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Invalid FQDN_Match: ");
        ((StringBuilder)localObject1).append(paramPPSNode);
        throw new ParsingException(((StringBuilder)localObject1).toString());
        label318:
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Invalid FQDN_Match: ");
        ((StringBuilder)localObject1).append(paramPPSNode);
        throw new ParsingException(((StringBuilder)localObject1).toString());
      }
      return localObject1;
    }
    throw new ParsingException("Leaf node not expected for PreferredRoamingPartner instance");
  }
  
  private static List<Policy.RoamingPartner> parsePreferredRoamingPartnerList(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      ArrayList localArrayList = new ArrayList();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext()) {
        localArrayList.add(parsePreferredRoamingPartner((PPSNode)paramPPSNode.next()));
      }
      return localArrayList;
    }
    throw new ParsingException("Leaf node not expected for PreferredRoamingPartnerList");
  }
  
  private static Pair<Integer, String> parseProtoPortTuple(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      int i = Integer.MIN_VALUE;
      PPSNode localPPSNode = null;
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      paramPPSNode = localPPSNode;
      while (localIterator.hasNext())
      {
        localPPSNode = (PPSNode)localIterator.next();
        String str = localPPSNode.getName();
        int j = -1;
        int k = str.hashCode();
        if (k != -952572705)
        {
          if ((k == 1727403850) && (str.equals("PortNumber"))) {
            j = 1;
          }
        }
        else if (str.equals("IPProtocol")) {
          j = 0;
        }
        switch (j)
        {
        default: 
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Unknown node under RequiredProtoPortTuple instance");
          paramPPSNode.append(localPPSNode.getName());
          throw new ParsingException(paramPPSNode.toString());
        case 1: 
          paramPPSNode = getPpsNodeValue(localPPSNode);
          break;
        case 0: 
          i = parseInteger(getPpsNodeValue(localPPSNode));
        }
      }
      if (i != Integer.MIN_VALUE)
      {
        if (paramPPSNode != null) {
          return Pair.create(Integer.valueOf(i), paramPPSNode);
        }
        throw new ParsingException("Missing PortNumber field");
      }
      throw new ParsingException("Missing IPProtocol field");
    }
    throw new ParsingException("Leaf node not expected for RequiredProtoPortTuple instance");
  }
  
  private static Map<Integer, String> parseRequiredProtoPortTuple(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      HashMap localHashMap = new HashMap();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext())
      {
        Pair localPair = parseProtoPortTuple((PPSNode)paramPPSNode.next());
        localHashMap.put((Integer)first, (String)second);
      }
      return localHashMap;
    }
    throw new ParsingException("Leaf node not expected for RequiredProtoPortTuple");
  }
  
  private static long[] parseRoamingConsortiumOI(String paramString)
    throws PpsMoParser.ParsingException
  {
    String[] arrayOfString = paramString.split(",");
    paramString = new long[arrayOfString.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      paramString[i] = parseLong(arrayOfString[i], 16);
    }
    return paramString;
  }
  
  private static Credential.SimCredential parseSimCredential(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Object localObject = new Credential.SimCredential();
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        String str = paramPPSNode.getName();
        int i = -1;
        int j = str.hashCode();
        if (j != -1249356658)
        {
          if ((j == 2251386) && (str.equals("IMSI"))) {
            i = 0;
          }
        }
        else if (str.equals("EAPType")) {
          i = 1;
        }
        switch (i)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown node under SIM: ");
          ((StringBuilder)localObject).append(paramPPSNode.getName());
          throw new ParsingException(((StringBuilder)localObject).toString());
        case 1: 
          ((Credential.SimCredential)localObject).setEapType(parseInteger(getPpsNodeValue(paramPPSNode)));
          break;
        case 0: 
          ((Credential.SimCredential)localObject).setImsi(getPpsNodeValue(paramPPSNode));
        }
      }
      return localObject;
    }
    throw new ParsingException("Leaf node not expected for SIM");
  }
  
  private static String parseSpExclusionInstance(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      PPSNode localPPSNode = null;
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      paramPPSNode = localPPSNode;
      while (localIterator.hasNext())
      {
        localPPSNode = (PPSNode)localIterator.next();
        paramPPSNode = localPPSNode.getName();
        int i = -1;
        if ((paramPPSNode.hashCode() == 2554747) && (paramPPSNode.equals("SSID"))) {
          i = 0;
        }
        if (i == 0) {
          paramPPSNode = getPpsNodeValue(localPPSNode);
        } else {
          throw new ParsingException("Unknown node under SPExclusion instance");
        }
      }
      return paramPPSNode;
    }
    throw new ParsingException("Leaf node not expected for SPExclusion instance");
  }
  
  private static String[] parseSpExclusionList(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      ArrayList localArrayList = new ArrayList();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext()) {
        localArrayList.add(parseSpExclusionInstance((PPSNode)paramPPSNode.next()));
      }
      return (String[])localArrayList.toArray(new String[localArrayList.size()]);
    }
    throw new ParsingException("Leaf node not expected for SPExclusionList");
  }
  
  private static void parseSubscriptionParameter(PPSNode paramPPSNode, PasspointConfiguration paramPasspointConfiguration)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        String str = paramPPSNode.getName();
        int i = -1;
        int j = str.hashCode();
        if (j != -1930116871)
        {
          if (j != -1670804707)
          {
            if (j != -1655596402)
            {
              if ((j == 1749851981) && (str.equals("CreationDate"))) {
                i = 0;
              }
            }
            else if (str.equals("TypeOfSubscription")) {
              i = 2;
            }
          }
          else if (str.equals("ExpirationDate")) {
            i = 1;
          }
        }
        else if (str.equals("UsageLimits")) {
          i = 3;
        }
        switch (i)
        {
        default: 
          paramPasspointConfiguration = new StringBuilder();
          paramPasspointConfiguration.append("Unknown node under SubscriptionParameter");
          paramPasspointConfiguration.append(paramPPSNode.getName());
          throw new ParsingException(paramPasspointConfiguration.toString());
        case 3: 
          parseUsageLimits(paramPPSNode, paramPasspointConfiguration);
          break;
        case 2: 
          paramPasspointConfiguration.setSubscriptionType(getPpsNodeValue(paramPPSNode));
          break;
        case 1: 
          paramPasspointConfiguration.setSubscriptionExpirationTimeInMillis(parseDate(getPpsNodeValue(paramPPSNode)));
          break;
        case 0: 
          paramPasspointConfiguration.setSubscriptionCreationTimeInMillis(parseDate(getPpsNodeValue(paramPPSNode)));
        }
      }
      return;
    }
    throw new ParsingException("Leaf node not expected for SubscriptionParameter");
  }
  
  private static Pair<String, byte[]> parseTrustRoot(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      PPSNode localPPSNode = null;
      byte[] arrayOfByte = null;
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      paramPPSNode = localPPSNode;
      while (localIterator.hasNext())
      {
        localPPSNode = (PPSNode)localIterator.next();
        String str = localPPSNode.getName();
        int i = -1;
        int j = str.hashCode();
        if (j != -1961397109)
        {
          if ((j == -285451687) && (str.equals("CertSHA256Fingerprint"))) {
            i = 1;
          }
        }
        else if (str.equals("CertURL")) {
          i = 0;
        }
        switch (i)
        {
        default: 
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Unknown node under TrustRoot: ");
          paramPPSNode.append(localPPSNode.getName());
          throw new ParsingException(paramPPSNode.toString());
        case 1: 
          arrayOfByte = parseHexString(getPpsNodeValue(localPPSNode));
          break;
        case 0: 
          paramPPSNode = getPpsNodeValue(localPPSNode);
        }
      }
      return Pair.create(paramPPSNode, arrayOfByte);
    }
    throw new ParsingException("Leaf node not expected for TrustRoot");
  }
  
  private static UpdateParameter parseUpdateParameter(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      UpdateParameter localUpdateParameter = new UpdateParameter();
      paramPPSNode = paramPPSNode.getChildren().iterator();
      while (paramPPSNode.hasNext())
      {
        Object localObject1 = (PPSNode)paramPPSNode.next();
        Object localObject2 = ((PPSNode)localObject1).getName();
        int i = -1;
        switch (((String)localObject2).hashCode())
        {
        default: 
          break;
        case 494843313: 
          if (((String)localObject2).equals("UsernamePassword")) {
            i = 4;
          }
          break;
        case 438596814: 
          if (((String)localObject2).equals("UpdateInterval")) {
            i = 0;
          }
          break;
        case 106806188: 
          if (((String)localObject2).equals("Restriction")) {
            i = 2;
          }
          break;
        case 76517104: 
          if (((String)localObject2).equals("Other")) {
            i = 6;
          }
          break;
        case 84300: 
          if (((String)localObject2).equals("URI")) {
            i = 3;
          }
          break;
        case -524654790: 
          if (((String)localObject2).equals("TrustRoot")) {
            i = 5;
          }
          break;
        case -961491158: 
          if (((String)localObject2).equals("UpdateMethod")) {
            i = 1;
          }
          break;
        }
        switch (i)
        {
        default: 
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Unknown node under Update Parameters: ");
          paramPPSNode.append(((PPSNode)localObject1).getName());
          throw new ParsingException(paramPPSNode.toString());
        case 6: 
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Ignore unsupported paramter: ");
          ((StringBuilder)localObject2).append(((PPSNode)localObject1).getName());
          Log.d("PpsMoParser", ((StringBuilder)localObject2).toString());
          break;
        case 5: 
          localObject1 = parseTrustRoot((PPSNode)localObject1);
          localUpdateParameter.setTrustRootCertUrl((String)first);
          localUpdateParameter.setTrustRootCertSha256Fingerprint((byte[])second);
          break;
        case 4: 
          localObject1 = parseUpdateUserCredential((PPSNode)localObject1);
          localUpdateParameter.setUsername((String)first);
          localUpdateParameter.setBase64EncodedPassword((String)second);
          break;
        case 3: 
          localUpdateParameter.setServerUri(getPpsNodeValue((PPSNode)localObject1));
          break;
        case 2: 
          localUpdateParameter.setRestriction(getPpsNodeValue((PPSNode)localObject1));
          break;
        case 1: 
          localUpdateParameter.setUpdateMethod(getPpsNodeValue((PPSNode)localObject1));
          break;
        case 0: 
          localUpdateParameter.setUpdateIntervalInMinutes(parseLong(getPpsNodeValue((PPSNode)localObject1), 10));
        }
      }
      return localUpdateParameter;
    }
    throw new ParsingException("Leaf node not expected for Update Parameters");
  }
  
  private static Pair<String, String> parseUpdateUserCredential(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      String str1 = null;
      PPSNode localPPSNode = null;
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      paramPPSNode = localPPSNode;
      while (localIterator.hasNext())
      {
        localPPSNode = (PPSNode)localIterator.next();
        String str2 = localPPSNode.getName();
        int i = -1;
        int j = str2.hashCode();
        if (j != -201069322)
        {
          if ((j == 1281629883) && (str2.equals("Password"))) {
            i = 1;
          }
        }
        else if (str2.equals("Username")) {
          i = 0;
        }
        switch (i)
        {
        default: 
          paramPPSNode = new StringBuilder();
          paramPPSNode.append("Unknown node under UsernamePassword: ");
          paramPPSNode.append(localPPSNode.getName());
          throw new ParsingException(paramPPSNode.toString());
        case 1: 
          paramPPSNode = getPpsNodeValue(localPPSNode);
          break;
        case 0: 
          str1 = getPpsNodeValue(localPPSNode);
        }
      }
      return Pair.create(str1, paramPPSNode);
    }
    throw new ParsingException("Leaf node not expected for UsernamePassword");
  }
  
  private static String parseUrn(XMLNode paramXMLNode)
    throws PpsMoParser.ParsingException
  {
    if (paramXMLNode.getChildren().size() == 1)
    {
      paramXMLNode = (XMLNode)paramXMLNode.getChildren().get(0);
      if (paramXMLNode.getChildren().size() == 1)
      {
        if (TextUtils.equals(paramXMLNode.getTag(), "Type"))
        {
          localObject = (XMLNode)paramXMLNode.getChildren().get(0);
          if (((XMLNode)localObject).getChildren().isEmpty())
          {
            if (TextUtils.equals(((XMLNode)localObject).getTag(), "DDFName")) {
              return ((XMLNode)localObject).getText();
            }
            paramXMLNode = new StringBuilder();
            paramXMLNode.append("Unexpected tag for DDFName: ");
            paramXMLNode.append(((XMLNode)localObject).getTag());
            throw new ParsingException(paramXMLNode.toString());
          }
          throw new ParsingException("Expect DDFName node to have no child");
        }
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unexpected tag for Type: ");
        ((StringBuilder)localObject).append(paramXMLNode.getTag());
        throw new ParsingException(((StringBuilder)localObject).toString());
      }
      throw new ParsingException("Expect Type node to only have one child");
    }
    throw new ParsingException("Expect RTPProperties node to only have one child");
  }
  
  private static void parseUsageLimits(PPSNode paramPPSNode, PasspointConfiguration paramPasspointConfiguration)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        String str = paramPPSNode.getName();
        int i = -1;
        int j = str.hashCode();
        if (j != -125810928)
        {
          if (j != 587064143)
          {
            if (j != 1622722065)
            {
              if ((j == 2022760654) && (str.equals("TimeLimit"))) {
                i = 2;
              }
            }
            else if (str.equals("DataLimit")) {
              i = 0;
            }
          }
          else if (str.equals("UsageTimePeriod")) {
            i = 3;
          }
        }
        else if (str.equals("StartDate")) {
          i = 1;
        }
        switch (i)
        {
        default: 
          paramPasspointConfiguration = new StringBuilder();
          paramPasspointConfiguration.append("Unknown node under UsageLimits");
          paramPasspointConfiguration.append(paramPPSNode.getName());
          throw new ParsingException(paramPasspointConfiguration.toString());
        case 3: 
          paramPasspointConfiguration.setUsageLimitUsageTimePeriodInMinutes(parseLong(getPpsNodeValue(paramPPSNode), 10));
          break;
        case 2: 
          paramPasspointConfiguration.setUsageLimitTimeLimitInMinutes(parseLong(getPpsNodeValue(paramPPSNode), 10));
          break;
        case 1: 
          paramPasspointConfiguration.setUsageLimitStartTimeInMillis(parseDate(getPpsNodeValue(paramPPSNode)));
          break;
        case 0: 
          paramPasspointConfiguration.setUsageLimitDataLimit(parseLong(getPpsNodeValue(paramPPSNode), 10));
        }
      }
      return;
    }
    throw new ParsingException("Leaf node not expected for UsageLimits");
  }
  
  private static Credential.UserCredential parseUserCredential(PPSNode paramPPSNode)
    throws PpsMoParser.ParsingException
  {
    if (!paramPPSNode.isLeaf())
    {
      Object localObject = new Credential.UserCredential();
      Iterator localIterator = paramPPSNode.getChildren().iterator();
      while (localIterator.hasNext())
      {
        paramPPSNode = (PPSNode)localIterator.next();
        String str = paramPPSNode.getName();
        int i = -1;
        switch (str.hashCode())
        {
        default: 
          break;
        case 1740345653: 
          if (str.equals("EAPMethod")) {
            i = 5;
          }
          break;
        case 1410776018: 
          if (str.equals("SoftTokenApp")) {
            i = 3;
          }
          break;
        case 1281629883: 
          if (str.equals("Password")) {
            i = 1;
          }
          break;
        case 1045832056: 
          if (str.equals("MachineManaged")) {
            i = 2;
          }
          break;
        case -123996342: 
          if (str.equals("AbleToShare")) {
            i = 4;
          }
          break;
        case -201069322: 
          if (str.equals("Username")) {
            i = 0;
          }
          break;
        }
        switch (i)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown node under UsernamPassword: ");
          ((StringBuilder)localObject).append(paramPPSNode.getName());
          throw new ParsingException(((StringBuilder)localObject).toString());
        case 5: 
          parseEAPMethod(paramPPSNode, (Credential.UserCredential)localObject);
          break;
        case 4: 
          ((Credential.UserCredential)localObject).setAbleToShare(Boolean.parseBoolean(getPpsNodeValue(paramPPSNode)));
          break;
        case 3: 
          ((Credential.UserCredential)localObject).setSoftTokenApp(getPpsNodeValue(paramPPSNode));
          break;
        case 2: 
          ((Credential.UserCredential)localObject).setMachineManaged(Boolean.parseBoolean(getPpsNodeValue(paramPPSNode)));
          break;
        case 1: 
          ((Credential.UserCredential)localObject).setPassword(getPpsNodeValue(paramPPSNode));
          break;
        case 0: 
          ((Credential.UserCredential)localObject).setUsername(getPpsNodeValue(paramPPSNode));
        }
      }
      return localObject;
    }
    throw new ParsingException("Leaf node not expected for UsernamePassword");
  }
  
  private static class InternalNode
    extends PpsMoParser.PPSNode
  {
    private final List<PpsMoParser.PPSNode> mChildren;
    
    public InternalNode(String paramString, List<PpsMoParser.PPSNode> paramList)
    {
      super();
      mChildren = paramList;
    }
    
    public List<PpsMoParser.PPSNode> getChildren()
    {
      return mChildren;
    }
    
    public String getValue()
    {
      return null;
    }
    
    public boolean isLeaf()
    {
      return false;
    }
  }
  
  private static class LeafNode
    extends PpsMoParser.PPSNode
  {
    private final String mValue;
    
    public LeafNode(String paramString1, String paramString2)
    {
      super();
      mValue = paramString2;
    }
    
    public List<PpsMoParser.PPSNode> getChildren()
    {
      return null;
    }
    
    public String getValue()
    {
      return mValue;
    }
    
    public boolean isLeaf()
    {
      return true;
    }
  }
  
  private static abstract class PPSNode
  {
    private final String mName;
    
    public PPSNode(String paramString)
    {
      mName = paramString;
    }
    
    public abstract List<PPSNode> getChildren();
    
    public String getName()
    {
      return mName;
    }
    
    public abstract String getValue();
    
    public abstract boolean isLeaf();
  }
  
  private static class ParsingException
    extends Exception
  {
    public ParsingException(String paramString)
    {
      super();
    }
  }
}
