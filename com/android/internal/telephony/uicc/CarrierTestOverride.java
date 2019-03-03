package com.android.internal.telephony.uicc;

import android.os.Environment;
import android.telephony.Rlog;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class CarrierTestOverride
{
  static final String CARRIER_TEST_XML_HEADER = "carrierTestOverrides";
  static final String CARRIER_TEST_XML_ITEM_KEY = "key";
  static final String CARRIER_TEST_XML_ITEM_KEY_STRING_GID1 = "gid1";
  static final String CARRIER_TEST_XML_ITEM_KEY_STRING_GID2 = "gid2";
  static final String CARRIER_TEST_XML_ITEM_KEY_STRING_ICCID = "iccid";
  static final String CARRIER_TEST_XML_ITEM_KEY_STRING_IMSI = "imsi";
  static final String CARRIER_TEST_XML_ITEM_KEY_STRING_ISINTESTMODE = "isInTestMode";
  static final String CARRIER_TEST_XML_ITEM_KEY_STRING_MCCMNC = "mccmnc";
  static final String CARRIER_TEST_XML_ITEM_KEY_STRING_PNN = "pnn";
  static final String CARRIER_TEST_XML_ITEM_KEY_STRING_SPN = "spn";
  static final String CARRIER_TEST_XML_ITEM_VALUE = "value";
  static final String CARRIER_TEST_XML_SUBHEADER = "carrierTestOverride";
  static final String DATA_CARRIER_TEST_OVERRIDE_PATH = "/user_de/0/com.android.phone/files/carrier_test_conf.xml";
  static final String LOG_TAG = "CarrierTestOverride";
  private HashMap<String, String> mCarrierTestParamMap = new HashMap();
  
  CarrierTestOverride()
  {
    loadCarrierTestOverrides();
  }
  
  private void loadCarrierTestOverrides()
  {
    Object localObject1 = new File(Environment.getDataDirectory(), "/user_de/0/com.android.phone/files/carrier_test_conf.xml");
    try
    {
      Object localObject2 = new java/io/FileReader;
      ((FileReader)localObject2).<init>((File)localObject1);
      Object localObject3 = new java/lang/StringBuilder;
      ((StringBuilder)localObject3).<init>();
      ((StringBuilder)localObject3).append("CarrierTestConfig file Modified Timestamp: ");
      ((StringBuilder)localObject3).append(((File)localObject1).lastModified());
      Rlog.d("CarrierTestOverride", ((StringBuilder)localObject3).toString());
      StringBuilder localStringBuilder1;
      try
      {
        localObject3 = Xml.newPullParser();
        ((XmlPullParser)localObject3).setInput((Reader)localObject2);
        XmlUtils.beginDocument((XmlPullParser)localObject3, "carrierTestOverrides");
        for (;;)
        {
          XmlUtils.nextElement((XmlPullParser)localObject3);
          if (!"carrierTestOverride".equals(((XmlPullParser)localObject3).getName()))
          {
            ((FileReader)localObject2).close();
            break;
          }
          String str1 = ((XmlPullParser)localObject3).getAttributeValue(null, "key");
          String str2 = ((XmlPullParser)localObject3).getAttributeValue(null, "value");
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("extracting key-values from CarrierTestConfig file: ");
          ((StringBuilder)localObject1).append(str1);
          ((StringBuilder)localObject1).append("|");
          ((StringBuilder)localObject1).append(str2);
          Rlog.d("CarrierTestOverride", ((StringBuilder)localObject1).toString());
          mCarrierTestParamMap.put(str1, str2);
        }
        return;
      }
      catch (IOException localIOException)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Exception in carrier_test_conf parser ");
        ((StringBuilder)localObject2).append(localIOException);
        Rlog.w("CarrierTestOverride", ((StringBuilder)localObject2).toString());
      }
      catch (XmlPullParserException localXmlPullParserException)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("Exception in carrier_test_conf parser ");
        localStringBuilder1.append(localXmlPullParserException);
        Rlog.w("CarrierTestOverride", localStringBuilder1.toString());
      }
      StringBuilder localStringBuilder2;
      return;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("Can not open ");
      localStringBuilder2.append(localStringBuilder1.getAbsolutePath());
      Rlog.w("CarrierTestOverride", localStringBuilder2.toString());
    }
  }
  
  String getFakeGid1()
  {
    try
    {
      String str = (String)mCarrierTestParamMap.get("gid1");
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("reading gid1 from CarrierTestConfig file: ");
      localStringBuilder.append(str);
      Rlog.d("CarrierTestOverride", localStringBuilder.toString());
      return str;
    }
    catch (NullPointerException localNullPointerException)
    {
      Rlog.w("CarrierTestOverride", "No gid1 in CarrierTestConfig file ");
    }
    return null;
  }
  
  String getFakeGid2()
  {
    try
    {
      String str = (String)mCarrierTestParamMap.get("gid2");
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("reading gid2 from CarrierTestConfig file: ");
      localStringBuilder.append(str);
      Rlog.d("CarrierTestOverride", localStringBuilder.toString());
      return str;
    }
    catch (NullPointerException localNullPointerException)
    {
      Rlog.w("CarrierTestOverride", "No gid2 in CarrierTestConfig file ");
    }
    return null;
  }
  
  String getFakeIMSI()
  {
    try
    {
      String str = (String)mCarrierTestParamMap.get("imsi");
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("reading imsi from CarrierTestConfig file: ");
      localStringBuilder.append(str);
      Rlog.d("CarrierTestOverride", localStringBuilder.toString());
      return str;
    }
    catch (NullPointerException localNullPointerException)
    {
      Rlog.w("CarrierTestOverride", "No imsi in CarrierTestConfig file ");
    }
    return null;
  }
  
  String getFakeIccid()
  {
    try
    {
      String str = (String)mCarrierTestParamMap.get("iccid");
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("reading iccid from CarrierTestConfig file: ");
      localStringBuilder.append(str);
      Rlog.d("CarrierTestOverride", localStringBuilder.toString());
      return str;
    }
    catch (NullPointerException localNullPointerException)
    {
      Rlog.w("CarrierTestOverride", "No iccid in CarrierTestConfig file ");
    }
    return null;
  }
  
  String getFakeMccMnc()
  {
    try
    {
      String str = (String)mCarrierTestParamMap.get("mccmnc");
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("reading mccmnc from CarrierTestConfig file: ");
      localStringBuilder.append(str);
      Rlog.d("CarrierTestOverride", localStringBuilder.toString());
      return str;
    }
    catch (NullPointerException localNullPointerException)
    {
      Rlog.w("CarrierTestOverride", "No mccmnc in CarrierTestConfig file ");
    }
    return null;
  }
  
  String getFakePnnHomeName()
  {
    try
    {
      String str = (String)mCarrierTestParamMap.get("pnn");
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("reading pnn from CarrierTestConfig file: ");
      localStringBuilder.append(str);
      Rlog.d("CarrierTestOverride", localStringBuilder.toString());
      return str;
    }
    catch (NullPointerException localNullPointerException)
    {
      Rlog.w("CarrierTestOverride", "No pnn in CarrierTestConfig file ");
    }
    return null;
  }
  
  String getFakeSpn()
  {
    try
    {
      String str = (String)mCarrierTestParamMap.get("spn");
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("reading spn from CarrierTestConfig file: ");
      localStringBuilder.append(str);
      Rlog.d("CarrierTestOverride", localStringBuilder.toString());
      return str;
    }
    catch (NullPointerException localNullPointerException)
    {
      Rlog.w("CarrierTestOverride", "No spn in CarrierTestConfig file ");
    }
    return null;
  }
  
  boolean isInTestMode()
  {
    boolean bool;
    if ((mCarrierTestParamMap.containsKey("isInTestMode")) && (((String)mCarrierTestParamMap.get("isInTestMode")).equals("true"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void override(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    mCarrierTestParamMap.put("isInTestMode", "true");
    mCarrierTestParamMap.put("mccmnc", paramString1);
    mCarrierTestParamMap.put("imsi", paramString2);
    mCarrierTestParamMap.put("iccid", paramString3);
    mCarrierTestParamMap.put("gid1", paramString4);
    mCarrierTestParamMap.put("gid2", paramString5);
    mCarrierTestParamMap.put("pnn", paramString6);
    mCarrierTestParamMap.put("spn", paramString7);
  }
}
