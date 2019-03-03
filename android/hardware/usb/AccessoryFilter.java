package android.hardware.usb;

import com.android.internal.util.dump.DualDumpOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class AccessoryFilter
{
  public final String mManufacturer;
  public final String mModel;
  public final String mVersion;
  
  public AccessoryFilter(UsbAccessory paramUsbAccessory)
  {
    mManufacturer = paramUsbAccessory.getManufacturer();
    mModel = paramUsbAccessory.getModel();
    mVersion = paramUsbAccessory.getVersion();
  }
  
  public AccessoryFilter(String paramString1, String paramString2, String paramString3)
  {
    mManufacturer = paramString1;
    mModel = paramString2;
    mVersion = paramString3;
  }
  
  public static AccessoryFilter read(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    int i = paramXmlPullParser.getAttributeCount();
    int j = 0;
    while (j < i)
    {
      String str1 = paramXmlPullParser.getAttributeName(j);
      String str2 = paramXmlPullParser.getAttributeValue(j);
      Object localObject4;
      Object localObject5;
      if ("manufacturer".equals(str1))
      {
        localObject4 = str2;
        localObject5 = localObject2;
      }
      else if ("model".equals(str1))
      {
        localObject4 = localObject1;
        localObject5 = str2;
      }
      else
      {
        localObject4 = localObject1;
        localObject5 = localObject2;
        if ("version".equals(str1))
        {
          localObject3 = str2;
          localObject5 = localObject2;
          localObject4 = localObject1;
        }
      }
      j++;
      localObject1 = localObject4;
      localObject2 = localObject5;
    }
    return new AccessoryFilter(localObject1, localObject2, localObject3);
  }
  
  public boolean contains(AccessoryFilter paramAccessoryFilter)
  {
    String str = mManufacturer;
    boolean bool = false;
    if ((str != null) && (!Objects.equals(mManufacturer, mManufacturer))) {
      return false;
    }
    if ((mModel != null) && (!Objects.equals(mModel, mModel))) {
      return false;
    }
    if ((mVersion != null) && (!Objects.equals(mVersion, mVersion))) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public void dump(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("manufacturer", 1138166333441L, mManufacturer);
    paramDualDumpOutputStream.write("model", 1138166333442L, mModel);
    paramDualDumpOutputStream.write("version", 1138166333443L, mVersion);
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public boolean equals(Object paramObject)
  {
    String str = mManufacturer;
    boolean bool1 = false;
    boolean bool2 = false;
    if ((str != null) && (mModel != null) && (mVersion != null))
    {
      if ((paramObject instanceof AccessoryFilter))
      {
        paramObject = (AccessoryFilter)paramObject;
        if ((mManufacturer.equals(mManufacturer)) && (mModel.equals(mModel)) && (mVersion.equals(mVersion))) {
          bool2 = true;
        }
        return bool2;
      }
      if ((paramObject instanceof UsbAccessory))
      {
        paramObject = (UsbAccessory)paramObject;
        if ((mManufacturer.equals(paramObject.getManufacturer())) && (mModel.equals(paramObject.getModel())) && (mVersion.equals(paramObject.getVersion()))) {
          bool2 = true;
        } else {
          bool2 = bool1;
        }
        return bool2;
      }
      return false;
    }
    return false;
  }
  
  public int hashCode()
  {
    String str = mManufacturer;
    int i = 0;
    int j;
    if (str == null) {
      j = 0;
    } else {
      j = mManufacturer.hashCode();
    }
    int k;
    if (mModel == null) {
      k = 0;
    } else {
      k = mModel.hashCode();
    }
    if (mVersion != null) {
      i = mVersion.hashCode();
    }
    return j ^ k ^ i;
  }
  
  public boolean matches(UsbAccessory paramUsbAccessory)
  {
    String str = mManufacturer;
    boolean bool = false;
    if ((str != null) && (!paramUsbAccessory.getManufacturer().equals(mManufacturer))) {
      return false;
    }
    if ((mModel != null) && (!paramUsbAccessory.getModel().equals(mModel))) {
      return false;
    }
    if ((mVersion != null) && (!paramUsbAccessory.getVersion().equals(mVersion))) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AccessoryFilter[mManufacturer=\"");
    localStringBuilder.append(mManufacturer);
    localStringBuilder.append("\", mModel=\"");
    localStringBuilder.append(mModel);
    localStringBuilder.append("\", mVersion=\"");
    localStringBuilder.append(mVersion);
    localStringBuilder.append("\"]");
    return localStringBuilder.toString();
  }
  
  public void write(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.startTag(null, "usb-accessory");
    if (mManufacturer != null) {
      paramXmlSerializer.attribute(null, "manufacturer", mManufacturer);
    }
    if (mModel != null) {
      paramXmlSerializer.attribute(null, "model", mModel);
    }
    if (mVersion != null) {
      paramXmlSerializer.attribute(null, "version", mVersion);
    }
    paramXmlSerializer.endTag(null, "usb-accessory");
  }
}
