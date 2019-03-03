package android.nfc;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class NdefRecord
  implements Parcelable
{
  public static final Parcelable.Creator<NdefRecord> CREATOR = new Parcelable.Creator()
  {
    public NdefRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      short s = (short)paramAnonymousParcel.readInt();
      byte[] arrayOfByte1 = new byte[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readByteArray(arrayOfByte1);
      byte[] arrayOfByte2 = new byte[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readByteArray(arrayOfByte2);
      byte[] arrayOfByte3 = new byte[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readByteArray(arrayOfByte3);
      return new NdefRecord(s, arrayOfByte1, arrayOfByte2, arrayOfByte3);
    }
    
    public NdefRecord[] newArray(int paramAnonymousInt)
    {
      return new NdefRecord[paramAnonymousInt];
    }
  };
  private static final byte[] EMPTY_BYTE_ARRAY;
  private static final byte FLAG_CF = 32;
  private static final byte FLAG_IL = 8;
  private static final byte FLAG_MB = -128;
  private static final byte FLAG_ME = 64;
  private static final byte FLAG_SR = 16;
  private static final int MAX_PAYLOAD_SIZE = 10485760;
  public static final byte[] RTD_ALTERNATIVE_CARRIER;
  public static final byte[] RTD_ANDROID_APP;
  public static final byte[] RTD_HANDOVER_CARRIER;
  public static final byte[] RTD_HANDOVER_REQUEST;
  public static final byte[] RTD_HANDOVER_SELECT;
  public static final byte[] RTD_SMART_POSTER;
  public static final byte[] RTD_TEXT = { 84 };
  public static final byte[] RTD_URI = { 85 };
  public static final short TNF_ABSOLUTE_URI = 3;
  public static final short TNF_EMPTY = 0;
  public static final short TNF_EXTERNAL_TYPE = 4;
  public static final short TNF_MIME_MEDIA = 2;
  public static final short TNF_RESERVED = 7;
  public static final short TNF_UNCHANGED = 6;
  public static final short TNF_UNKNOWN = 5;
  public static final short TNF_WELL_KNOWN = 1;
  private static final String[] URI_PREFIX_MAP;
  private final byte[] mId;
  private final byte[] mPayload;
  private final short mTnf;
  private final byte[] mType;
  
  static
  {
    RTD_SMART_POSTER = new byte[] { 83, 112 };
    RTD_ALTERNATIVE_CARRIER = new byte[] { 97, 99 };
    RTD_HANDOVER_CARRIER = new byte[] { 72, 99 };
    RTD_HANDOVER_REQUEST = new byte[] { 72, 114 };
    RTD_HANDOVER_SELECT = new byte[] { 72, 115 };
    RTD_ANDROID_APP = "android.com:pkg".getBytes();
    URI_PREFIX_MAP = new String[] { "", "http://www.", "https://www.", "http://", "https://", "tel:", "mailto:", "ftp://anonymous:anonymous@", "ftp://ftp.", "ftps://", "sftp://", "smb://", "nfs://", "ftp://", "dav://", "news:", "telnet://", "imap:", "rtsp://", "urn:", "pop:", "sip:", "sips:", "tftp:", "btspp://", "btl2cap://", "btgoep://", "tcpobex://", "irdaobex://", "file://", "urn:epc:id:", "urn:epc:tag:", "urn:epc:pat:", "urn:epc:raw:", "urn:epc:", "urn:nfc:" };
    EMPTY_BYTE_ARRAY = new byte[0];
  }
  
  public NdefRecord(short paramShort, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    byte[] arrayOfByte = paramArrayOfByte1;
    if (paramArrayOfByte1 == null) {
      arrayOfByte = EMPTY_BYTE_ARRAY;
    }
    paramArrayOfByte1 = paramArrayOfByte2;
    if (paramArrayOfByte2 == null) {
      paramArrayOfByte1 = EMPTY_BYTE_ARRAY;
    }
    paramArrayOfByte2 = paramArrayOfByte3;
    if (paramArrayOfByte3 == null) {
      paramArrayOfByte2 = EMPTY_BYTE_ARRAY;
    }
    paramArrayOfByte3 = validateTnf(paramShort, arrayOfByte, paramArrayOfByte1, paramArrayOfByte2);
    if (paramArrayOfByte3 == null)
    {
      mTnf = paramShort;
      mType = arrayOfByte;
      mId = paramArrayOfByte1;
      mPayload = paramArrayOfByte2;
      return;
    }
    throw new IllegalArgumentException(paramArrayOfByte3);
  }
  
  @Deprecated
  public NdefRecord(byte[] paramArrayOfByte)
    throws FormatException
  {
    paramArrayOfByte = ByteBuffer.wrap(paramArrayOfByte);
    NdefRecord[] arrayOfNdefRecord = parse(paramArrayOfByte, true);
    if (paramArrayOfByte.remaining() <= 0)
    {
      mTnf = ((short)0mTnf);
      mType = 0mType;
      mId = 0mId;
      mPayload = 0mPayload;
      return;
    }
    throw new FormatException("data too long");
  }
  
  private static StringBuilder bytesToString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++) {
      localStringBuilder.append(String.format("%02X", new Object[] { Byte.valueOf(paramArrayOfByte[j]) }));
    }
    return localStringBuilder;
  }
  
  public static NdefRecord createApplicationRecord(String paramString)
  {
    if (paramString != null)
    {
      if (paramString.length() != 0) {
        return new NdefRecord((short)4, RTD_ANDROID_APP, null, paramString.getBytes(StandardCharsets.UTF_8));
      }
      throw new IllegalArgumentException("packageName is empty");
    }
    throw new NullPointerException("packageName is null");
  }
  
  public static NdefRecord createExternal(String paramString1, String paramString2, byte[] paramArrayOfByte)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null)
      {
        paramString1 = paramString1.trim().toLowerCase(Locale.ROOT);
        paramString2 = paramString2.trim().toLowerCase(Locale.ROOT);
        if (paramString1.length() != 0)
        {
          if (paramString2.length() != 0)
          {
            paramString1 = paramString1.getBytes(StandardCharsets.UTF_8);
            byte[] arrayOfByte = paramString2.getBytes(StandardCharsets.UTF_8);
            paramString2 = new byte[paramString1.length + 1 + arrayOfByte.length];
            System.arraycopy(paramString1, 0, paramString2, 0, paramString1.length);
            paramString2[paramString1.length] = ((byte)58);
            System.arraycopy(arrayOfByte, 0, paramString2, paramString1.length + 1, arrayOfByte.length);
            return new NdefRecord((short)4, paramString2, null, paramArrayOfByte);
          }
          throw new IllegalArgumentException("type is empty");
        }
        throw new IllegalArgumentException("domain is empty");
      }
      throw new NullPointerException("type is null");
    }
    throw new NullPointerException("domain is null");
  }
  
  public static NdefRecord createMime(String paramString, byte[] paramArrayOfByte)
  {
    if (paramString != null)
    {
      paramString = Intent.normalizeMimeType(paramString);
      if (paramString.length() != 0)
      {
        int i = paramString.indexOf('/');
        if (i != 0)
        {
          if (i != paramString.length() - 1) {
            return new NdefRecord((short)2, paramString.getBytes(StandardCharsets.US_ASCII), null, paramArrayOfByte);
          }
          throw new IllegalArgumentException("mimeType must have minor type");
        }
        throw new IllegalArgumentException("mimeType must have major type");
      }
      throw new IllegalArgumentException("mimeType is empty");
    }
    throw new NullPointerException("mimeType is null");
  }
  
  public static NdefRecord createTextRecord(String paramString1, String paramString2)
  {
    if (paramString2 != null)
    {
      paramString2 = paramString2.getBytes(StandardCharsets.UTF_8);
      if ((paramString1 != null) && (!paramString1.isEmpty())) {
        paramString1 = paramString1.getBytes(StandardCharsets.US_ASCII);
      } else {
        paramString1 = Locale.getDefault().getLanguage().getBytes(StandardCharsets.US_ASCII);
      }
      if (paramString1.length < 64)
      {
        ByteBuffer localByteBuffer = ByteBuffer.allocate(paramString1.length + 1 + paramString2.length);
        localByteBuffer.put((byte)(paramString1.length & 0xFF));
        localByteBuffer.put(paramString1);
        localByteBuffer.put(paramString2);
        return new NdefRecord((short)1, RTD_TEXT, null, localByteBuffer.array());
      }
      throw new IllegalArgumentException("language code is too long, must be <64 bytes.");
    }
    throw new NullPointerException("text is null");
  }
  
  public static NdefRecord createUri(Uri paramUri)
  {
    if (paramUri != null)
    {
      Object localObject = paramUri.normalizeScheme().toString();
      if (((String)localObject).length() != 0)
      {
        int i = 0;
        int k;
        for (int j = 1;; j++)
        {
          paramUri = (Uri)localObject;
          k = i;
          if (j >= URI_PREFIX_MAP.length) {
            break;
          }
          if (((String)localObject).startsWith(URI_PREFIX_MAP[j]))
          {
            k = (byte)j;
            paramUri = ((String)localObject).substring(URI_PREFIX_MAP[j].length());
            break;
          }
        }
        paramUri = paramUri.getBytes(StandardCharsets.UTF_8);
        localObject = new byte[paramUri.length + 1];
        localObject[0] = ((byte)k);
        System.arraycopy(paramUri, 0, (byte[])localObject, 1, paramUri.length);
        return new NdefRecord((short)1, RTD_URI, null, (byte[])localObject);
      }
      throw new IllegalArgumentException("uri is empty");
    }
    throw new NullPointerException("uri is null");
  }
  
  public static NdefRecord createUri(String paramString)
  {
    return createUri(Uri.parse(paramString));
  }
  
  private static void ensureSanePayloadSize(long paramLong)
    throws FormatException
  {
    if (paramLong <= 10485760L) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("payload above max limit: ");
    localStringBuilder.append(paramLong);
    localStringBuilder.append(" > ");
    localStringBuilder.append(10485760);
    throw new FormatException(localStringBuilder.toString());
  }
  
  static NdefRecord[] parse(ByteBuffer paramByteBuffer, boolean paramBoolean)
    throws FormatException
  {
    ArrayList localArrayList1 = new ArrayList();
    try
    {
      ArrayList localArrayList2 = new java/util/ArrayList;
      localArrayList2.<init>();
      short s1 = -1;
      short s2 = 0;
      Object localObject1 = null;
      Object localObject2 = null;
      byte[] arrayOfByte1 = null;
      int i = 0;
      Object localObject3 = paramByteBuffer;
      if (i == 0)
      {
        short s3 = paramByteBuffer.get();
        int j = 1;
        int k;
        if ((s3 & 0xFFFFFF80) != 0) {
          k = 1;
        } else {
          k = 0;
        }
        if ((s3 & 0x40) != 0) {
          i = 1;
        } else {
          i = 0;
        }
        int m;
        if ((s3 & 0x20) != 0) {
          m = 1;
        } else {
          m = 0;
        }
        int n;
        if ((s3 & 0x10) != 0) {
          n = 1;
        } else {
          n = 0;
        }
        if ((s3 & 0x8) == 0) {
          j = 0;
        }
        s3 = (short)(s3 & 0x7);
        if ((k == 0) && (localArrayList1.size() == 0) && (s2 == 0) && (!paramBoolean))
        {
          paramByteBuffer = new android/nfc/FormatException;
          paramByteBuffer.<init>("expected MB flag");
          throw paramByteBuffer;
        }
        if ((k != 0) && ((localArrayList1.size() != 0) || (s2 != 0)) && (!paramBoolean))
        {
          paramByteBuffer = new android/nfc/FormatException;
          paramByteBuffer.<init>("unexpected MB flag");
          throw paramByteBuffer;
        }
        if ((s2 != 0) && (j != 0))
        {
          paramByteBuffer = new android/nfc/FormatException;
          paramByteBuffer.<init>("unexpected IL flag in non-leading chunk");
          throw paramByteBuffer;
        }
        if ((m != 0) && (i != 0))
        {
          paramByteBuffer = new android/nfc/FormatException;
          paramByteBuffer.<init>("unexpected ME flag in non-trailing chunk");
          throw paramByteBuffer;
        }
        if ((s2 != 0) && (s3 != 6))
        {
          paramByteBuffer = new android/nfc/FormatException;
          paramByteBuffer.<init>("expected TNF_UNCHANGED in non-leading chunk");
          throw paramByteBuffer;
        }
        if ((s2 == 0) && (s3 == 6))
        {
          paramByteBuffer = new android/nfc/FormatException;
          paramByteBuffer.<init>("unexpected TNF_UNCHANGED in first chunk or unchunked record");
          throw paramByteBuffer;
        }
        int i1 = paramByteBuffer.get() & 0xFF;
        long l;
        if (n != 0) {
          l = paramByteBuffer.get() & 0xFF;
        } else {
          l = paramByteBuffer.getInt() & 0xFFFFFFFF;
        }
        if (j != 0) {
          k = paramByteBuffer.get() & 0xFF;
        } else {
          k = 0;
        }
        if ((s2 != 0) && (i1 != 0))
        {
          paramByteBuffer = new android/nfc/FormatException;
          paramByteBuffer.<init>("expected zero-length type in non-leading chunk");
          throw paramByteBuffer;
        }
        if (s2 == 0)
        {
          if (i1 > 0) {
            arrayOfByte1 = new byte[i1];
          } else {
            arrayOfByte1 = EMPTY_BYTE_ARRAY;
          }
          if (k > 0) {
            localObject1 = new byte[k];
          } else {
            localObject1 = EMPTY_BYTE_ARRAY;
          }
          ((ByteBuffer)localObject3).get(arrayOfByte1);
          ((ByteBuffer)localObject3).get((byte[])localObject1);
        }
        else
        {
          localObject1 = localObject2;
        }
        ensureSanePayloadSize(l);
        if (l > 0L) {
          localObject2 = new byte[(int)l];
        } else {
          localObject2 = EMPTY_BYTE_ARRAY;
        }
        ((ByteBuffer)localObject3).get((byte[])localObject2);
        if ((m != 0) && (s2 == 0))
        {
          if ((i1 == 0) && (s3 != 5))
          {
            paramByteBuffer = new android/nfc/FormatException;
            paramByteBuffer.<init>("expected non-zero type length in first chunk");
            throw paramByteBuffer;
          }
          localArrayList2.clear();
          s1 = s3;
        }
        if ((m != 0) || (s2 != 0)) {
          localArrayList2.add(localObject2);
        }
        short s4;
        if ((m == 0) && (s2 != 0))
        {
          l = 0L;
          localObject3 = localArrayList2.iterator();
          while (((Iterator)localObject3).hasNext()) {
            l += ((byte[])((Iterator)localObject3).next()).length;
          }
          ensureSanePayloadSize(l);
          localObject3 = new byte[(int)l];
          localObject2 = localArrayList2.iterator();
          k = 0;
          while (((Iterator)localObject2).hasNext())
          {
            byte[] arrayOfByte2 = (byte[])((Iterator)localObject2).next();
            System.arraycopy(arrayOfByte2, 0, (byte[])localObject3, k, arrayOfByte2.length);
            k += arrayOfByte2.length;
          }
          s2 = s1;
          localObject2 = localObject3;
          s4 = s2;
        }
        else
        {
          s4 = s3;
        }
        if (m != 0) {
          s2 = 1;
        }
        for (;;)
        {
          localObject3 = localObject1;
          localObject1 = localObject2;
          localObject2 = localObject3;
          break;
          s2 = 0;
          localObject3 = validateTnf(s4, arrayOfByte1, (byte[])localObject1, (byte[])localObject2);
          if (localObject3 != null) {
            break label796;
          }
          localObject3 = new android/nfc/NdefRecord;
          ((NdefRecord)localObject3).<init>(s4, arrayOfByte1, (byte[])localObject1, (byte[])localObject2);
          localArrayList1.add(localObject3);
          if (paramBoolean) {
            break label808;
          }
        }
        label796:
        paramByteBuffer = new android/nfc/FormatException;
        paramByteBuffer.<init>((String)localObject3);
        throw paramByteBuffer;
      }
      label808:
      return (NdefRecord[])localArrayList1.toArray(new NdefRecord[localArrayList1.size()]);
    }
    catch (BufferUnderflowException paramByteBuffer)
    {
      throw new FormatException("expected more data", paramByteBuffer);
    }
  }
  
  private Uri parseWktUri()
  {
    if (mPayload.length < 2) {
      return null;
    }
    int i = mPayload[0] & 0xFFFFFFFF;
    if ((i >= 0) && (i < URI_PREFIX_MAP.length))
    {
      String str1 = URI_PREFIX_MAP[i];
      String str2 = new String(Arrays.copyOfRange(mPayload, 1, mPayload.length), StandardCharsets.UTF_8);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str1);
      localStringBuilder.append(str2);
      return Uri.parse(localStringBuilder.toString());
    }
    return null;
  }
  
  private Uri toUri(boolean paramBoolean)
  {
    int i = mTnf;
    Object localObject1 = null;
    if (i != 1)
    {
      switch (i)
      {
      default: 
        break;
      case 4: 
        if (paramBoolean) {
          break;
        }
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("vnd.android.nfc://ext/");
        ((StringBuilder)localObject1).append(new String(mType, StandardCharsets.US_ASCII));
        return Uri.parse(((StringBuilder)localObject1).toString());
      case 3: 
        return Uri.parse(new String(mType, StandardCharsets.UTF_8)).normalizeScheme();
      }
    }
    else
    {
      Object localObject2;
      if ((Arrays.equals(mType, RTD_SMART_POSTER)) && (!paramBoolean)) {
        try
        {
          localObject1 = new android/nfc/NdefMessage;
          ((NdefMessage)localObject1).<init>(mPayload);
          localObject2 = ((NdefMessage)localObject1).getRecords();
          int j = localObject2.length;
          for (i = 0; i < j; i++)
          {
            localObject1 = localObject2[i].toUri(true);
            if (localObject1 != null) {
              return localObject1;
            }
          }
        }
        catch (FormatException localFormatException) {}
      }
      if (Arrays.equals(mType, RTD_URI))
      {
        localObject2 = parseWktUri();
        Uri localUri;
        if (localObject2 != null) {
          localUri = ((Uri)localObject2).normalizeScheme();
        }
        return localUri;
      }
    }
    return null;
  }
  
  static String validateTnf(short paramShort, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    switch (paramShort)
    {
    default: 
      return String.format("unexpected tnf value: 0x%02x", new Object[] { Short.valueOf(paramShort) });
    case 6: 
      return "unexpected TNF_UNCHANGED in first chunk or logical record";
    case 5: 
    case 7: 
      if (paramArrayOfByte1.length != 0) {
        return "unexpected type field in TNF_UNKNOWN or TNF_RESERVEd record";
      }
      return null;
    case 1: 
    case 2: 
    case 3: 
    case 4: 
      return null;
    }
    if ((paramArrayOfByte1.length == 0) && (paramArrayOfByte2.length == 0) && (paramArrayOfByte3.length == 0)) {
      return null;
    }
    return "unexpected data in TNF_EMPTY record";
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
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (NdefRecord)paramObject;
    if (!Arrays.equals(mId, mId)) {
      return false;
    }
    if (!Arrays.equals(mPayload, mPayload)) {
      return false;
    }
    if (mTnf != mTnf) {
      return false;
    }
    return Arrays.equals(mType, mType);
  }
  
  int getByteLength()
  {
    int i = 3 + mType.length + mId.length + mPayload.length;
    int j = mPayload.length;
    int k = 1;
    if (j < 256) {
      m = 1;
    } else {
      m = 0;
    }
    while (mId.length > 0) {}
    k = 0;
    j = i;
    if (m == 0) {
      j = i + 3;
    }
    int m = j;
    if (k != 0) {
      m = j + 1;
    }
    return m;
  }
  
  public byte[] getId()
  {
    return (byte[])mId.clone();
  }
  
  public byte[] getPayload()
  {
    return (byte[])mPayload.clone();
  }
  
  public short getTnf()
  {
    return mTnf;
  }
  
  public byte[] getType()
  {
    return (byte[])mType.clone();
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * 1 + Arrays.hashCode(mId)) + Arrays.hashCode(mPayload)) + mTnf) + Arrays.hashCode(mType);
  }
  
  @Deprecated
  public byte[] toByteArray()
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(getByteLength());
    writeToByteBuffer(localByteBuffer, true, true);
    return localByteBuffer.array();
  }
  
  public String toMimeType()
  {
    switch (mTnf)
    {
    default: 
      break;
    case 2: 
      return Intent.normalizeMimeType(new String(mType, StandardCharsets.US_ASCII));
    case 1: 
      if (Arrays.equals(mType, RTD_TEXT)) {
        return "text/plain";
      }
      break;
    }
    return null;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(String.format("NdefRecord tnf=%X", new Object[] { Short.valueOf(mTnf) }));
    if (mType.length > 0)
    {
      localStringBuilder.append(" type=");
      localStringBuilder.append(bytesToString(mType));
    }
    if (mId.length > 0)
    {
      localStringBuilder.append(" id=");
      localStringBuilder.append(bytesToString(mId));
    }
    if (mPayload.length > 0)
    {
      localStringBuilder.append(" payload=");
      localStringBuilder.append(bytesToString(mPayload));
    }
    return localStringBuilder.toString();
  }
  
  public Uri toUri()
  {
    return toUri(false);
  }
  
  void writeToByteBuffer(ByteBuffer paramByteBuffer, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = mPayload.length;
    int j = 1;
    int k = 0;
    if (i < 256) {
      i = 1;
    } else {
      i = 0;
    }
    while (mId.length > 0) {}
    j = 0;
    int m;
    if (paramBoolean1) {
      m = -128;
    } else {
      m = 0;
    }
    int n;
    if (paramBoolean2) {
      n = 64;
    } else {
      n = 0;
    }
    int i1;
    if (i != 0) {
      i1 = 16;
    } else {
      i1 = 0;
    }
    if (j != 0) {
      k = 8;
    }
    paramByteBuffer.put((byte)(k | m | n | i1 | mTnf));
    paramByteBuffer.put((byte)mType.length);
    if (i != 0) {
      paramByteBuffer.put((byte)mPayload.length);
    } else {
      paramByteBuffer.putInt(mPayload.length);
    }
    if (j != 0) {
      paramByteBuffer.put((byte)mId.length);
    }
    paramByteBuffer.put(mType);
    paramByteBuffer.put(mId);
    paramByteBuffer.put(mPayload);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mTnf);
    paramParcel.writeInt(mType.length);
    paramParcel.writeByteArray(mType);
    paramParcel.writeInt(mId.length);
    paramParcel.writeByteArray(mId);
    paramParcel.writeInt(mPayload.length);
    paramParcel.writeByteArray(mPayload);
  }
}
