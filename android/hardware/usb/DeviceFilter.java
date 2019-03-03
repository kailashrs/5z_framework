package android.hardware.usb;

import android.util.Slog;
import com.android.internal.util.dump.DualDumpOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class DeviceFilter
{
  private static final String TAG = DeviceFilter.class.getSimpleName();
  public final int mClass;
  public final String mManufacturerName;
  public final int mProductId;
  public final String mProductName;
  public final int mProtocol;
  public final String mSerialNumber;
  public final int mSubclass;
  public final int mVendorId;
  
  public DeviceFilter(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString1, String paramString2, String paramString3)
  {
    mVendorId = paramInt1;
    mProductId = paramInt2;
    mClass = paramInt3;
    mSubclass = paramInt4;
    mProtocol = paramInt5;
    mManufacturerName = paramString1;
    mProductName = paramString2;
    mSerialNumber = paramString3;
  }
  
  public DeviceFilter(UsbDevice paramUsbDevice)
  {
    mVendorId = paramUsbDevice.getVendorId();
    mProductId = paramUsbDevice.getProductId();
    mClass = paramUsbDevice.getDeviceClass();
    mSubclass = paramUsbDevice.getDeviceSubclass();
    mProtocol = paramUsbDevice.getDeviceProtocol();
    mManufacturerName = paramUsbDevice.getManufacturerName();
    mProductName = paramUsbDevice.getProductName();
    mSerialNumber = paramUsbDevice.getSerialNumber();
  }
  
  private boolean matches(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool;
    if (((mClass != -1) && (paramInt1 != mClass)) || ((mSubclass != -1) && (paramInt2 != mSubclass)) || ((mProtocol != -1) && (paramInt3 != mProtocol))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static DeviceFilter read(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getAttributeCount();
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    int j = -1;
    int k = -1;
    int m = -1;
    int n = -1;
    int i1 = -1;
    int i2 = 0;
    for (;;)
    {
      Object localObject4 = paramXmlPullParser;
      if (i2 >= i) {
        break;
      }
      String str = ((XmlPullParser)localObject4).getAttributeName(i2);
      localObject4 = ((XmlPullParser)localObject4).getAttributeValue(i2);
      int i3;
      int i4;
      int i5;
      int i6;
      Object localObject6;
      Object localObject7;
      Object localObject5;
      if ("manufacturer-name".equals(str))
      {
        i3 = i1;
        i4 = n;
        i5 = m;
        i6 = k;
        localObject6 = localObject2;
        localObject7 = localObject1;
      }
      else if ("product-name".equals(str))
      {
        localObject6 = localObject4;
        i3 = i1;
        i4 = n;
        i5 = m;
        i6 = k;
        localObject4 = localObject3;
        localObject7 = localObject1;
      }
      else if ("serial-number".equals(str))
      {
        localObject7 = localObject4;
        i3 = i1;
        i4 = n;
        i5 = m;
        i6 = k;
        localObject4 = localObject3;
        localObject6 = localObject2;
      }
      else
      {
        i3 = 10;
        localObject6 = localObject4;
        int i7 = i3;
        if (localObject4 != null)
        {
          localObject6 = localObject4;
          i7 = i3;
          if (((String)localObject4).length() > 2)
          {
            localObject6 = localObject4;
            i7 = i3;
            if (((String)localObject4).charAt(0) == '0') {
              if (((String)localObject4).charAt(1) != 'x')
              {
                localObject6 = localObject4;
                i7 = i3;
                if (((String)localObject4).charAt(1) != 'X') {}
              }
              else
              {
                i7 = 16;
                localObject6 = ((String)localObject4).substring(2);
              }
            }
          }
        }
        try
        {
          i7 = Integer.parseInt((String)localObject6, i7);
          if ("vendor-id".equals(str))
          {
            i3 = i7;
            i4 = n;
            i5 = m;
            i6 = k;
            localObject4 = localObject3;
            localObject6 = localObject2;
            localObject7 = localObject1;
          }
          else if ("product-id".equals(str))
          {
            i3 = i1;
            i4 = i7;
            i5 = m;
            i6 = k;
            localObject4 = localObject3;
            localObject6 = localObject2;
            localObject7 = localObject1;
          }
          else if ("class".equals(str))
          {
            i3 = i1;
            i4 = n;
            i5 = i7;
            i6 = k;
            localObject4 = localObject3;
            localObject6 = localObject2;
            localObject7 = localObject1;
          }
          else if ("subclass".equals(str))
          {
            i3 = i1;
            i4 = n;
            i5 = m;
            i6 = i7;
            localObject4 = localObject3;
            localObject6 = localObject2;
            localObject7 = localObject1;
          }
          else
          {
            i3 = i1;
            i4 = n;
            i5 = m;
            i6 = k;
            localObject4 = localObject3;
            localObject6 = localObject2;
            localObject7 = localObject1;
            if ("protocol".equals(str))
            {
              i3 = i1;
              i4 = n;
              i5 = m;
              i6 = k;
              j = i7;
              localObject4 = localObject3;
              localObject6 = localObject2;
              localObject7 = localObject1;
            }
          }
        }
        catch (NumberFormatException localNumberFormatException)
        {
          localObject6 = TAG;
          localObject7 = new StringBuilder();
          ((StringBuilder)localObject7).append("invalid number for field ");
          ((StringBuilder)localObject7).append(str);
          Slog.e((String)localObject6, ((StringBuilder)localObject7).toString(), localNumberFormatException);
          localObject7 = localObject1;
          localObject6 = localObject2;
          localObject5 = localObject3;
          i6 = k;
          i5 = m;
          i4 = n;
          i3 = i1;
        }
      }
      i2++;
      i1 = i3;
      n = i4;
      m = i5;
      k = i6;
      localObject3 = localObject5;
      localObject2 = localObject6;
      localObject1 = localObject7;
    }
    return new DeviceFilter(i1, n, m, k, j, localObject3, localObject2, localObject1);
  }
  
  public boolean contains(DeviceFilter paramDeviceFilter)
  {
    if ((mVendorId != -1) && (mVendorId != mVendorId)) {
      return false;
    }
    if ((mProductId != -1) && (mProductId != mProductId)) {
      return false;
    }
    if ((mManufacturerName != null) && (!Objects.equals(mManufacturerName, mManufacturerName))) {
      return false;
    }
    if ((mProductName != null) && (!Objects.equals(mProductName, mProductName))) {
      return false;
    }
    if ((mSerialNumber != null) && (!Objects.equals(mSerialNumber, mSerialNumber))) {
      return false;
    }
    return matches(mClass, mSubclass, mProtocol);
  }
  
  public void dump(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("vendor_id", 1120986464257L, mVendorId);
    paramDualDumpOutputStream.write("product_id", 1120986464258L, mProductId);
    paramDualDumpOutputStream.write("class", 1120986464259L, mClass);
    paramDualDumpOutputStream.write("subclass", 1120986464260L, mSubclass);
    paramDualDumpOutputStream.write("protocol", 1120986464261L, mProtocol);
    paramDualDumpOutputStream.write("manufacturer_name", 1138166333446L, mManufacturerName);
    paramDualDumpOutputStream.write("product_name", 1138166333447L, mProductName);
    paramDualDumpOutputStream.write("serial_number", 1138166333448L, mSerialNumber);
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((mVendorId != -1) && (mProductId != -1) && (mClass != -1) && (mSubclass != -1) && (mProtocol != -1))
    {
      if ((paramObject instanceof DeviceFilter))
      {
        paramObject = (DeviceFilter)paramObject;
        if ((mVendorId == mVendorId) && (mProductId == mProductId) && (mClass == mClass) && (mSubclass == mSubclass) && (mProtocol == mProtocol))
        {
          if (((mManufacturerName != null) && (mManufacturerName == null)) || ((mManufacturerName == null) && (mManufacturerName != null)) || ((mProductName != null) && (mProductName == null)) || ((mProductName == null) && (mProductName != null)) || ((mSerialNumber != null) && (mSerialNumber == null)) || ((mSerialNumber == null) && (mSerialNumber != null))) {
            return false;
          }
          return ((mManufacturerName == null) || (mManufacturerName == null) || (mManufacturerName.equals(mManufacturerName))) && ((mProductName == null) || (mProductName == null) || (mProductName.equals(mProductName))) && ((mSerialNumber == null) || (mSerialNumber == null) || (mSerialNumber.equals(mSerialNumber)));
        }
        return false;
      }
      if ((paramObject instanceof UsbDevice))
      {
        paramObject = (UsbDevice)paramObject;
        if ((paramObject.getVendorId() == mVendorId) && (paramObject.getProductId() == mProductId) && (paramObject.getDeviceClass() == mClass) && (paramObject.getDeviceSubclass() == mSubclass) && (paramObject.getDeviceProtocol() == mProtocol))
        {
          if (((mManufacturerName != null) && (paramObject.getManufacturerName() == null)) || ((mManufacturerName == null) && (paramObject.getManufacturerName() != null)) || ((mProductName != null) && (paramObject.getProductName() == null)) || ((mProductName == null) && (paramObject.getProductName() != null)) || ((mSerialNumber != null) && (paramObject.getSerialNumber() == null)) || ((mSerialNumber == null) && (paramObject.getSerialNumber() != null))) {
            return false;
          }
          return ((paramObject.getManufacturerName() == null) || (mManufacturerName.equals(paramObject.getManufacturerName()))) && ((paramObject.getProductName() == null) || (mProductName.equals(paramObject.getProductName()))) && ((paramObject.getSerialNumber() == null) || (mSerialNumber.equals(paramObject.getSerialNumber())));
        }
        return false;
      }
      return false;
    }
    return false;
  }
  
  public int hashCode()
  {
    return (mVendorId << 16 | mProductId) ^ (mClass << 16 | mSubclass << 8 | mProtocol);
  }
  
  public boolean matches(UsbDevice paramUsbDevice)
  {
    if ((mVendorId != -1) && (paramUsbDevice.getVendorId() != mVendorId)) {
      return false;
    }
    if ((mProductId != -1) && (paramUsbDevice.getProductId() != mProductId)) {
      return false;
    }
    if ((mManufacturerName != null) && (paramUsbDevice.getManufacturerName() == null)) {
      return false;
    }
    if ((mProductName != null) && (paramUsbDevice.getProductName() == null)) {
      return false;
    }
    if ((mSerialNumber != null) && (paramUsbDevice.getSerialNumber() == null)) {
      return false;
    }
    if ((mManufacturerName != null) && (paramUsbDevice.getManufacturerName() != null) && (!mManufacturerName.equals(paramUsbDevice.getManufacturerName()))) {
      return false;
    }
    if ((mProductName != null) && (paramUsbDevice.getProductName() != null) && (!mProductName.equals(paramUsbDevice.getProductName()))) {
      return false;
    }
    if ((mSerialNumber != null) && (paramUsbDevice.getSerialNumber() != null) && (!mSerialNumber.equals(paramUsbDevice.getSerialNumber()))) {
      return false;
    }
    if (matches(paramUsbDevice.getDeviceClass(), paramUsbDevice.getDeviceSubclass(), paramUsbDevice.getDeviceProtocol())) {
      return true;
    }
    int i = paramUsbDevice.getInterfaceCount();
    for (int j = 0; j < i; j++)
    {
      UsbInterface localUsbInterface = paramUsbDevice.getInterface(j);
      if (matches(localUsbInterface.getInterfaceClass(), localUsbInterface.getInterfaceSubclass(), localUsbInterface.getInterfaceProtocol())) {
        return true;
      }
    }
    return false;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DeviceFilter[mVendorId=");
    localStringBuilder.append(mVendorId);
    localStringBuilder.append(",mProductId=");
    localStringBuilder.append(mProductId);
    localStringBuilder.append(",mClass=");
    localStringBuilder.append(mClass);
    localStringBuilder.append(",mSubclass=");
    localStringBuilder.append(mSubclass);
    localStringBuilder.append(",mProtocol=");
    localStringBuilder.append(mProtocol);
    localStringBuilder.append(",mManufacturerName=");
    localStringBuilder.append(mManufacturerName);
    localStringBuilder.append(",mProductName=");
    localStringBuilder.append(mProductName);
    localStringBuilder.append(",mSerialNumber=");
    localStringBuilder.append(mSerialNumber);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void write(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    paramXmlSerializer.startTag(null, "usb-device");
    if (mVendorId != -1) {
      paramXmlSerializer.attribute(null, "vendor-id", Integer.toString(mVendorId));
    }
    if (mProductId != -1) {
      paramXmlSerializer.attribute(null, "product-id", Integer.toString(mProductId));
    }
    if (mClass != -1) {
      paramXmlSerializer.attribute(null, "class", Integer.toString(mClass));
    }
    if (mSubclass != -1) {
      paramXmlSerializer.attribute(null, "subclass", Integer.toString(mSubclass));
    }
    if (mProtocol != -1) {
      paramXmlSerializer.attribute(null, "protocol", Integer.toString(mProtocol));
    }
    if (mManufacturerName != null) {
      paramXmlSerializer.attribute(null, "manufacturer-name", mManufacturerName);
    }
    if (mProductName != null) {
      paramXmlSerializer.attribute(null, "product-name", mProductName);
    }
    if (mSerialNumber != null) {
      paramXmlSerializer.attribute(null, "serial-number", mSerialNumber);
    }
    paramXmlSerializer.endTag(null, "usb-device");
  }
}
