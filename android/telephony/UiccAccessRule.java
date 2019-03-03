package android.telephony;

import android.annotation.SystemApi;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

@SystemApi
public final class UiccAccessRule
  implements Parcelable
{
  public static final Parcelable.Creator<UiccAccessRule> CREATOR = new Parcelable.Creator()
  {
    public UiccAccessRule createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UiccAccessRule(paramAnonymousParcel);
    }
    
    public UiccAccessRule[] newArray(int paramAnonymousInt)
    {
      return new UiccAccessRule[paramAnonymousInt];
    }
  };
  private static final int ENCODING_VERSION = 1;
  private static final String TAG = "UiccAccessRule";
  private final long mAccessType;
  private final byte[] mCertificateHash;
  private final String mPackageName;
  
  UiccAccessRule(Parcel paramParcel)
  {
    mCertificateHash = paramParcel.createByteArray();
    mPackageName = paramParcel.readString();
    mAccessType = paramParcel.readLong();
  }
  
  public UiccAccessRule(byte[] paramArrayOfByte, String paramString, long paramLong)
  {
    mCertificateHash = paramArrayOfByte;
    mPackageName = paramString;
    mAccessType = paramLong;
  }
  
  /* Error */
  public static UiccAccessRule[] decodeRules(byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: ifnonnull +5 -> 8
    //   6: aconst_null
    //   7: areturn
    //   8: new 63	java/io/ByteArrayInputStream
    //   11: dup
    //   12: aload_0
    //   13: invokespecial 66	java/io/ByteArrayInputStream:<init>	([B)V
    //   16: astore_0
    //   17: new 68	java/io/DataInputStream
    //   20: astore_2
    //   21: aload_2
    //   22: aload_0
    //   23: invokespecial 71	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   26: aload_1
    //   27: astore_0
    //   28: aload_2
    //   29: invokevirtual 75	java/io/DataInputStream:readInt	()I
    //   32: pop
    //   33: aload_1
    //   34: astore_0
    //   35: aload_2
    //   36: invokevirtual 75	java/io/DataInputStream:readInt	()I
    //   39: istore_3
    //   40: aload_1
    //   41: astore_0
    //   42: iload_3
    //   43: anewarray 2	android/telephony/UiccAccessRule
    //   46: astore 4
    //   48: iconst_0
    //   49: istore 5
    //   51: iload 5
    //   53: iload_3
    //   54: if_icmpge +72 -> 126
    //   57: aload_1
    //   58: astore_0
    //   59: aload_2
    //   60: invokevirtual 75	java/io/DataInputStream:readInt	()I
    //   63: newarray byte
    //   65: astore 6
    //   67: aload_1
    //   68: astore_0
    //   69: aload_2
    //   70: aload 6
    //   72: invokevirtual 78	java/io/DataInputStream:readFully	([B)V
    //   75: aload_1
    //   76: astore_0
    //   77: aload_2
    //   78: invokevirtual 82	java/io/DataInputStream:readBoolean	()Z
    //   81: ifeq +14 -> 95
    //   84: aload_1
    //   85: astore_0
    //   86: aload_2
    //   87: invokevirtual 85	java/io/DataInputStream:readUTF	()Ljava/lang/String;
    //   90: astore 7
    //   92: goto +6 -> 98
    //   95: aconst_null
    //   96: astore 7
    //   98: aload_1
    //   99: astore_0
    //   100: aload 4
    //   102: iload 5
    //   104: new 2	android/telephony/UiccAccessRule
    //   107: dup
    //   108: aload 6
    //   110: aload 7
    //   112: aload_2
    //   113: invokevirtual 86	java/io/DataInputStream:readLong	()J
    //   116: invokespecial 88	android/telephony/UiccAccessRule:<init>	([BLjava/lang/String;J)V
    //   119: aastore
    //   120: iinc 5 1
    //   123: goto -72 -> 51
    //   126: aload_1
    //   127: astore_0
    //   128: aload_2
    //   129: invokevirtual 91	java/io/DataInputStream:close	()V
    //   132: aload_2
    //   133: invokevirtual 91	java/io/DataInputStream:close	()V
    //   136: aload 4
    //   138: areturn
    //   139: astore 7
    //   141: goto +11 -> 152
    //   144: astore 7
    //   146: aload 7
    //   148: astore_0
    //   149: aload 7
    //   151: athrow
    //   152: aload_0
    //   153: ifnull +19 -> 172
    //   156: aload_2
    //   157: invokevirtual 91	java/io/DataInputStream:close	()V
    //   160: goto +16 -> 176
    //   163: astore_1
    //   164: aload_0
    //   165: aload_1
    //   166: invokevirtual 95	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   169: goto +7 -> 176
    //   172: aload_2
    //   173: invokevirtual 91	java/io/DataInputStream:close	()V
    //   176: aload 7
    //   178: athrow
    //   179: astore_0
    //   180: new 97	java/lang/IllegalStateException
    //   183: dup
    //   184: ldc 99
    //   186: aload_0
    //   187: invokespecial 102	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   190: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	191	0	paramArrayOfByte	byte[]
    //   1	126	1	localObject1	Object
    //   163	3	1	localThrowable1	Throwable
    //   20	153	2	localDataInputStream	java.io.DataInputStream
    //   39	16	3	i	int
    //   46	91	4	arrayOfUiccAccessRule	UiccAccessRule[]
    //   49	72	5	j	int
    //   65	44	6	arrayOfByte	byte[]
    //   90	21	7	str	String
    //   139	1	7	localObject2	Object
    //   144	33	7	localThrowable2	Throwable
    // Exception table:
    //   from	to	target	type
    //   28	33	139	finally
    //   35	40	139	finally
    //   42	48	139	finally
    //   59	67	139	finally
    //   69	75	139	finally
    //   77	84	139	finally
    //   86	92	139	finally
    //   100	120	139	finally
    //   128	132	139	finally
    //   149	152	139	finally
    //   28	33	144	java/lang/Throwable
    //   35	40	144	java/lang/Throwable
    //   42	48	144	java/lang/Throwable
    //   59	67	144	java/lang/Throwable
    //   69	75	144	java/lang/Throwable
    //   77	84	144	java/lang/Throwable
    //   86	92	144	java/lang/Throwable
    //   100	120	144	java/lang/Throwable
    //   128	132	144	java/lang/Throwable
    //   156	160	163	java/lang/Throwable
    //   17	26	179	java/io/IOException
    //   132	136	179	java/io/IOException
    //   156	160	179	java/io/IOException
    //   164	169	179	java/io/IOException
    //   172	176	179	java/io/IOException
    //   176	179	179	java/io/IOException
  }
  
  public static byte[] encodeRules(UiccAccessRule[] paramArrayOfUiccAccessRule)
  {
    if (paramArrayOfUiccAccessRule == null) {
      return null;
    }
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new java/io/ByteArrayOutputStream;
      localByteArrayOutputStream.<init>();
      DataOutputStream localDataOutputStream = new java/io/DataOutputStream;
      localDataOutputStream.<init>(localByteArrayOutputStream);
      localDataOutputStream.writeInt(1);
      localDataOutputStream.writeInt(paramArrayOfUiccAccessRule.length);
      int i = paramArrayOfUiccAccessRule.length;
      for (int j = 0; j < i; j++)
      {
        UiccAccessRule localUiccAccessRule = paramArrayOfUiccAccessRule[j];
        localDataOutputStream.writeInt(mCertificateHash.length);
        localDataOutputStream.write(mCertificateHash);
        if (mPackageName != null)
        {
          localDataOutputStream.writeBoolean(true);
          localDataOutputStream.writeUTF(mPackageName);
        }
        else
        {
          localDataOutputStream.writeBoolean(false);
        }
        localDataOutputStream.writeLong(mAccessType);
      }
      localDataOutputStream.close();
      paramArrayOfUiccAccessRule = localByteArrayOutputStream.toByteArray();
      return paramArrayOfUiccAccessRule;
    }
    catch (IOException paramArrayOfUiccAccessRule)
    {
      throw new IllegalStateException("ByteArrayOutputStream should never lead to an IOException", paramArrayOfUiccAccessRule);
    }
  }
  
  private static byte[] getCertHash(Signature paramSignature, String paramString)
  {
    try
    {
      paramSignature = MessageDigest.getInstance(paramString).digest(paramSignature.toByteArray());
      return paramSignature;
    }
    catch (NoSuchAlgorithmException paramSignature)
    {
      paramString = new StringBuilder();
      paramString.append("NoSuchAlgorithmException: ");
      paramString.append(paramSignature);
      Rlog.e("UiccAccessRule", paramString.toString());
    }
    return null;
  }
  
  private boolean matches(byte[] paramArrayOfByte, String paramString)
  {
    boolean bool;
    if ((paramArrayOfByte != null) && (Arrays.equals(mCertificateHash, paramArrayOfByte)) && ((TextUtils.isEmpty(mPackageName)) || (mPackageName.equals(paramString)))) {
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
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (UiccAccessRule)paramObject;
      if ((!Arrays.equals(mCertificateHash, mCertificateHash)) || (!Objects.equals(mPackageName, mPackageName)) || (mAccessType != mAccessType)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getCarrierPrivilegeStatus(PackageInfo paramPackageInfo)
  {
    if ((signatures != null) && (signatures.length != 0))
    {
      Signature[] arrayOfSignature = signatures;
      int i = arrayOfSignature.length;
      for (int j = 0; j < i; j++)
      {
        int k = getCarrierPrivilegeStatus(arrayOfSignature[j], packageName);
        if (k != 0) {
          return k;
        }
      }
      return 0;
    }
    throw new IllegalArgumentException("Must use GET_SIGNATURES when looking up package info");
  }
  
  public int getCarrierPrivilegeStatus(Signature paramSignature, String paramString)
  {
    byte[] arrayOfByte = getCertHash(paramSignature, "SHA-1");
    paramSignature = getCertHash(paramSignature, "SHA-256");
    if ((!matches(arrayOfByte, paramString)) && (!matches(paramSignature, paramString))) {
      return 0;
    }
    return 1;
  }
  
  public String getCertificateHexString()
  {
    return IccUtils.bytesToHexString(mCertificateHash);
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * 1 + Arrays.hashCode(mCertificateHash)) + Objects.hashCode(mPackageName)) + Objects.hashCode(Long.valueOf(mAccessType));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("cert: ");
    localStringBuilder.append(IccUtils.bytesToHexString(mCertificateHash));
    localStringBuilder.append(" pkg: ");
    localStringBuilder.append(mPackageName);
    localStringBuilder.append(" access: ");
    localStringBuilder.append(mAccessType);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mCertificateHash);
    paramParcel.writeString(mPackageName);
    paramParcel.writeLong(mAccessType);
  }
}
