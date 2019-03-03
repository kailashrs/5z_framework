package android.net.nsd;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class NsdServiceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<NsdServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public NsdServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      NsdServiceInfo localNsdServiceInfo = new NsdServiceInfo();
      NsdServiceInfo.access$002(localNsdServiceInfo, paramAnonymousParcel.readString());
      NsdServiceInfo.access$102(localNsdServiceInfo, paramAnonymousParcel.readString());
      if (paramAnonymousParcel.readInt() == 1) {
        try
        {
          NsdServiceInfo.access$202(localNsdServiceInfo, InetAddress.getByAddress(paramAnonymousParcel.createByteArray()));
        }
        catch (UnknownHostException localUnknownHostException) {}
      }
      NsdServiceInfo.access$302(localNsdServiceInfo, paramAnonymousParcel.readInt());
      int i = paramAnonymousParcel.readInt();
      for (int j = 0; j < i; j++)
      {
        byte[] arrayOfByte = null;
        if (paramAnonymousParcel.readInt() == 1)
        {
          arrayOfByte = new byte[paramAnonymousParcel.readInt()];
          paramAnonymousParcel.readByteArray(arrayOfByte);
        }
        mTxtRecord.put(paramAnonymousParcel.readString(), arrayOfByte);
      }
      return localNsdServiceInfo;
    }
    
    public NsdServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new NsdServiceInfo[paramAnonymousInt];
    }
  };
  private static final String TAG = "NsdServiceInfo";
  private InetAddress mHost;
  private int mPort;
  private String mServiceName;
  private String mServiceType;
  private final ArrayMap<String, byte[]> mTxtRecord = new ArrayMap();
  
  public NsdServiceInfo() {}
  
  public NsdServiceInfo(String paramString1, String paramString2)
  {
    mServiceName = paramString1;
    mServiceType = paramString2;
  }
  
  private int getTxtRecordSize()
  {
    int i = 0;
    Iterator localIterator = mTxtRecord.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (Map.Entry)localIterator.next();
      int j = ((String)((Map.Entry)localObject).getKey()).length();
      localObject = (byte[])((Map.Entry)localObject).getValue();
      int k;
      if (localObject == null) {
        k = 0;
      } else {
        k = localObject.length;
      }
      i = i + 2 + j + k;
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Map<String, byte[]> getAttributes()
  {
    return Collections.unmodifiableMap(mTxtRecord);
  }
  
  public InetAddress getHost()
  {
    return mHost;
  }
  
  public int getPort()
  {
    return mPort;
  }
  
  public String getServiceName()
  {
    return mServiceName;
  }
  
  public String getServiceType()
  {
    return mServiceType;
  }
  
  public byte[] getTxtRecord()
  {
    int i = getTxtRecordSize();
    if (i == 0) {
      return new byte[0];
    }
    byte[] arrayOfByte = new byte[i];
    i = 0;
    Iterator localIterator = mTxtRecord.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (Map.Entry)localIterator.next();
      String str = (String)((Map.Entry)localObject).getKey();
      localObject = (byte[])((Map.Entry)localObject).getValue();
      int j = i + 1;
      int k = str.length();
      if (localObject == null) {
        m = 0;
      } else {
        m = localObject.length;
      }
      arrayOfByte[i] = ((byte)(byte)(k + m + 1));
      System.arraycopy(str.getBytes(StandardCharsets.US_ASCII), 0, arrayOfByte, j, str.length());
      i = j + str.length();
      int m = i + 1;
      arrayOfByte[i] = ((byte)61);
      i = m;
      if (localObject != null)
      {
        System.arraycopy((byte[])localObject, 0, arrayOfByte, m, localObject.length);
        i = m + localObject.length;
      }
    }
    return arrayOfByte;
  }
  
  public void removeAttribute(String paramString)
  {
    mTxtRecord.remove(paramString);
  }
  
  public void setAttribute(String paramString1, String paramString2)
  {
    if (paramString2 == null) {
      try
      {
        paramString2 = (byte[])null;
      }
      catch (UnsupportedEncodingException paramString1)
      {
        break label30;
      }
    } else {
      paramString2 = paramString2.getBytes("UTF-8");
    }
    setAttribute(paramString1, paramString2);
    return;
    label30:
    throw new IllegalArgumentException("Value must be UTF-8");
  }
  
  public void setAttribute(String paramString, byte[] paramArrayOfByte)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      int i = 0;
      int j = 0;
      while (j < paramString.length())
      {
        k = paramString.charAt(j);
        if ((k >= 32) && (k <= 126))
        {
          if (k != 61) {
            j++;
          } else {
            throw new IllegalArgumentException("Key strings must not include '='");
          }
        }
        else {
          throw new IllegalArgumentException("Key strings must be printable US-ASCII");
        }
      }
      int k = paramString.length();
      if (paramArrayOfByte == null) {
        j = 0;
      } else {
        j = paramArrayOfByte.length;
      }
      if (k + j < 255)
      {
        if (paramString.length() > 9)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Key lengths > 9 are discouraged: ");
          localStringBuilder.append(paramString);
          Log.w("NsdServiceInfo", localStringBuilder.toString());
        }
        int m = getTxtRecordSize();
        k = paramString.length();
        if (paramArrayOfByte == null) {
          j = i;
        } else {
          j = paramArrayOfByte.length;
        }
        j = k + m + j + 2;
        if (j <= 1300)
        {
          if (j > 400) {
            Log.w("NsdServiceInfo", "Total length of all attributes exceeds 400 bytes; truncation may occur");
          }
          mTxtRecord.put(paramString, paramArrayOfByte);
          return;
        }
        throw new IllegalArgumentException("Total length of attributes must be < 1300 bytes");
      }
      throw new IllegalArgumentException("Key length + value length must be < 255 bytes");
    }
    throw new IllegalArgumentException("Key cannot be empty");
  }
  
  public void setHost(InetAddress paramInetAddress)
  {
    mHost = paramInetAddress;
  }
  
  public void setPort(int paramInt)
  {
    mPort = paramInt;
  }
  
  public void setServiceName(String paramString)
  {
    mServiceName = paramString;
  }
  
  public void setServiceType(String paramString)
  {
    mServiceType = paramString;
  }
  
  public void setTxtRecords(String paramString)
  {
    int i = 0;
    byte[] arrayOfByte = Base64.decode(paramString, 0);
    int k;
    int m;
    label507:
    while (i < arrayOfByte.length)
    {
      int j = arrayOfByte[i] & 0xFF;
      k = i + 1;
      if (j != 0)
      {
        i = j;
        m = j;
      }
      try
      {
        if (k + j > arrayOfByte.length)
        {
          m = j;
          paramString = new java/lang/StringBuilder;
          m = j;
          paramString.<init>();
          m = j;
          paramString.append("Corrupt record length (pos = ");
          m = j;
          paramString.append(k);
          m = j;
          paramString.append("): ");
          m = j;
          paramString.append(j);
          m = j;
          Log.w("NsdServiceInfo", paramString.toString());
          m = j;
          i = arrayOfByte.length - k;
        }
        int n = 0;
        paramString = null;
        localObject1 = null;
        j = k;
        while (j < k + i)
        {
          if (localObject1 == null)
          {
            localObject2 = paramString;
            m = n;
            if (arrayOfByte[j] == 61)
            {
              m = i;
              localObject1 = new String(arrayOfByte, k, j - k, StandardCharsets.US_ASCII);
              localObject2 = paramString;
              m = n;
            }
          }
          else
          {
            localObject2 = paramString;
            if (paramString == null)
            {
              m = i;
              localObject2 = new byte[i - ((String)localObject1).length() - 1];
            }
            localObject2[n] = ((byte)arrayOfByte[j]);
            m = n + 1;
          }
          j++;
          paramString = (String)localObject2;
          n = m;
        }
        Object localObject2 = localObject1;
        if (localObject1 == null)
        {
          m = i;
          localObject2 = new java/lang/String;
          m = i;
          ((String)localObject2).<init>(arrayOfByte, k, i, StandardCharsets.US_ASCII);
        }
        m = i;
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          m = i;
          if (!getAttributes().containsKey(localObject2))
          {
            m = i;
            setAttribute((String)localObject2, paramString);
            break label507;
          }
          m = i;
          localObject1 = new java/lang/IllegalArgumentException;
          m = i;
          paramString = new java/lang/StringBuilder;
          m = i;
          paramString.<init>();
          m = i;
          paramString.append("Invalid txt record (duplicate key \"");
          m = i;
          paramString.append((String)localObject2);
          m = i;
          paramString.append("\")");
          m = i;
          ((IllegalArgumentException)localObject1).<init>(paramString.toString());
          m = i;
          throw ((Throwable)localObject1);
        }
        m = i;
        paramString = new java/lang/IllegalArgumentException;
        m = i;
        paramString.<init>("Invalid txt record (key is empty)");
        m = i;
        throw paramString;
      }
      catch (IllegalArgumentException paramString)
      {
        Object localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("While parsing txt records (pos = ");
        ((StringBuilder)localObject1).append(k);
        ((StringBuilder)localObject1).append("): ");
        ((StringBuilder)localObject1).append(paramString.getMessage());
        Log.e("NsdServiceInfo", ((StringBuilder)localObject1).toString());
        i = m;
        i = k + i;
      }
      m = j;
      paramString = new java/lang/IllegalArgumentException;
      m = j;
      paramString.<init>("Zero sized txt record");
      m = j;
      throw paramString;
    }
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("name: ");
    localStringBuffer.append(mServiceName);
    localStringBuffer.append(", type: ");
    localStringBuffer.append(mServiceType);
    localStringBuffer.append(", host: ");
    localStringBuffer.append(mHost);
    localStringBuffer.append(", port: ");
    localStringBuffer.append(mPort);
    byte[] arrayOfByte = getTxtRecord();
    if (arrayOfByte != null)
    {
      localStringBuffer.append(", txtRecord: ");
      localStringBuffer.append(new String(arrayOfByte, StandardCharsets.UTF_8));
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mServiceName);
    paramParcel.writeString(mServiceType);
    if (mHost != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeByteArray(mHost.getAddress());
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mPort);
    paramParcel.writeInt(mTxtRecord.size());
    Iterator localIterator = mTxtRecord.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      byte[] arrayOfByte = (byte[])mTxtRecord.get(str);
      if (arrayOfByte != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeInt(arrayOfByte.length);
        paramParcel.writeByteArray(arrayOfByte);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      paramParcel.writeString(str);
    }
  }
}
