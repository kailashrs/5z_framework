package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.Locale;

public class WifiSsid
  implements Parcelable
{
  public static final Parcelable.Creator<WifiSsid> CREATOR = new Parcelable.Creator()
  {
    public WifiSsid createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiSsid localWifiSsid = new WifiSsid(null);
      int i = paramAnonymousParcel.readInt();
      byte[] arrayOfByte = new byte[i];
      paramAnonymousParcel.readByteArray(arrayOfByte);
      octets.write(arrayOfByte, 0, i);
      return localWifiSsid;
    }
    
    public WifiSsid[] newArray(int paramAnonymousInt)
    {
      return new WifiSsid[paramAnonymousInt];
    }
  };
  private static final int HEX_RADIX = 16;
  public static final String NONE = "<unknown ssid>";
  private static final String TAG = "WifiSsid";
  public final ByteArrayOutputStream octets = new ByteArrayOutputStream(32);
  
  private WifiSsid() {}
  
  private void convertToBytes(String paramString)
  {
    int i = 0;
    while (i < paramString.length())
    {
      int j = paramString.charAt(i);
      if (j != 92)
      {
        octets.write(j);
        i++;
      }
      else
      {
        i++;
        j = paramString.charAt(i);
        if (j != 34)
        {
          if (j != 92)
          {
            if (j != 101)
            {
              if (j != 110)
              {
                if (j != 114)
                {
                  if (j != 116)
                  {
                    if (j != 120)
                    {
                      switch (j)
                      {
                      default: 
                        break;
                      case 48: 
                      case 49: 
                      case 50: 
                      case 51: 
                      case 52: 
                      case 53: 
                      case 54: 
                      case 55: 
                        int k = paramString.charAt(i) - '0';
                        int m = i + 1;
                        j = m;
                        i = k;
                        if (paramString.charAt(m) >= '0')
                        {
                          j = m;
                          i = k;
                          if (paramString.charAt(m) <= '7')
                          {
                            i = k * 8 + paramString.charAt(m) - 48;
                            j = m + 1;
                          }
                        }
                        k = j;
                        m = i;
                        if (paramString.charAt(j) >= '0')
                        {
                          k = j;
                          m = i;
                          if (paramString.charAt(j) <= '7')
                          {
                            m = i * 8 + paramString.charAt(j) - 48;
                            k = j + 1;
                          }
                        }
                        octets.write(m);
                        i = k;
                        break;
                      }
                    }
                    else
                    {
                      j = i + 1;
                      try
                      {
                        i = Integer.parseInt(paramString.substring(j, j + 2), 16);
                      }
                      catch (NumberFormatException localNumberFormatException)
                      {
                        i = -1;
                      }
                      if (i < 0)
                      {
                        i = Character.digit(paramString.charAt(j), 16);
                        if (i < 0)
                        {
                          i = j;
                        }
                        else
                        {
                          octets.write(i);
                          i = j + 1;
                        }
                      }
                      else
                      {
                        octets.write(i);
                        i = j + 2;
                      }
                    }
                  }
                  else
                  {
                    octets.write(9);
                    i++;
                  }
                }
                else
                {
                  octets.write(13);
                  i++;
                }
              }
              else
              {
                octets.write(10);
                i++;
              }
            }
            else
            {
              octets.write(27);
              i++;
            }
          }
          else
          {
            octets.write(92);
            i++;
          }
        }
        else
        {
          octets.write(34);
          i++;
        }
      }
    }
  }
  
  public static WifiSsid createFromAsciiEncoded(String paramString)
  {
    WifiSsid localWifiSsid = new WifiSsid();
    localWifiSsid.convertToBytes(paramString);
    return localWifiSsid;
  }
  
  public static WifiSsid createFromByteArray(byte[] paramArrayOfByte)
  {
    WifiSsid localWifiSsid = new WifiSsid();
    if (paramArrayOfByte != null) {
      octets.write(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    return localWifiSsid;
  }
  
  public static WifiSsid createFromHex(String paramString)
  {
    WifiSsid localWifiSsid = new WifiSsid();
    if (paramString == null) {
      return localWifiSsid;
    }
    String str;
    if (!paramString.startsWith("0x"))
    {
      str = paramString;
      if (!paramString.startsWith("0X")) {}
    }
    else
    {
      str = paramString.substring(2);
    }
    for (int i = 0; i < str.length() - 1; i += 2)
    {
      int j;
      try
      {
        j = Integer.parseInt(str.substring(i, i + 2), 16);
      }
      catch (NumberFormatException paramString)
      {
        j = 0;
      }
      octets.write(j);
    }
    return localWifiSsid;
  }
  
  private boolean isArrayAllZeroes(byte[] paramArrayOfByte)
  {
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      if (paramArrayOfByte[i] != 0) {
        return false;
      }
    }
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof WifiSsid)) {
      return false;
    }
    paramObject = (WifiSsid)paramObject;
    return Arrays.equals(octets.toByteArray(), octets.toByteArray());
  }
  
  public String getHexString()
  {
    byte[] arrayOfByte = getOctets();
    String str = "0x";
    for (int i = 0; i < octets.size(); i++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str);
      localStringBuilder.append(String.format(Locale.US, "%02x", new Object[] { Byte.valueOf(arrayOfByte[i]) }));
      str = localStringBuilder.toString();
    }
    if (octets.size() <= 0) {
      str = null;
    }
    return str;
  }
  
  public byte[] getOctets()
  {
    return octets.toByteArray();
  }
  
  public int hashCode()
  {
    return Arrays.hashCode(octets.toByteArray());
  }
  
  public boolean isHidden()
  {
    return isArrayAllZeroes(octets.toByteArray());
  }
  
  public String toString()
  {
    Object localObject = octets.toByteArray();
    if ((octets.size() > 0) && (!isArrayAllZeroes((byte[])localObject)))
    {
      CharsetDecoder localCharsetDecoder = Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
      CharBuffer localCharBuffer = CharBuffer.allocate(32);
      localObject = localCharsetDecoder.decode(ByteBuffer.wrap((byte[])localObject), localCharBuffer, true);
      localCharBuffer.flip();
      if (((CoderResult)localObject).isError()) {
        return "<unknown ssid>";
      }
      return localCharBuffer.toString();
    }
    return "";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(octets.size());
    paramParcel.writeByteArray(octets.toByteArray());
  }
}
