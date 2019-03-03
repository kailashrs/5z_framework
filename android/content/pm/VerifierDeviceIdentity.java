package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.annotations.VisibleForTesting;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;

public class VerifierDeviceIdentity
  implements Parcelable
{
  public static final Parcelable.Creator<VerifierDeviceIdentity> CREATOR = new Parcelable.Creator()
  {
    public VerifierDeviceIdentity createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VerifierDeviceIdentity(paramAnonymousParcel, null);
    }
    
    public VerifierDeviceIdentity[] newArray(int paramAnonymousInt)
    {
      return new VerifierDeviceIdentity[paramAnonymousInt];
    }
  };
  private static final char[] ENCODE = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55 };
  private static final int GROUP_SIZE = 4;
  private static final int LONG_SIZE = 13;
  private static final char SEPARATOR = '-';
  private final long mIdentity;
  private final String mIdentityString;
  
  public VerifierDeviceIdentity(long paramLong)
  {
    mIdentity = paramLong;
    mIdentityString = encodeBase32(paramLong);
  }
  
  private VerifierDeviceIdentity(Parcel paramParcel)
  {
    long l = paramParcel.readLong();
    mIdentity = l;
    mIdentityString = encodeBase32(l);
  }
  
  private static final long decodeBase32(byte[] paramArrayOfByte)
    throws IllegalArgumentException
  {
    long l = 0L;
    int i = 0;
    int j = paramArrayOfByte.length;
    int k = 0;
    while (k < j)
    {
      int m = paramArrayOfByte[k];
      if ((65 <= m) && (m <= 90)) {
        m -= 65;
      }
      for (;;)
      {
        break;
        if ((50 <= m) && (m <= 55))
        {
          m -= 24;
        }
        else
        {
          if (m == 45) {
            break label167;
          }
          if ((97 <= m) && (m <= 122))
          {
            m -= 97;
          }
          else if (m == 48)
          {
            m = 14;
          }
          else
          {
            if (m != 49) {
              break label183;
            }
            m = 8;
          }
        }
      }
      l = l << 5 | m;
      i++;
      if (i == 1)
      {
        if ((m & 0xF) != m) {
          throw new IllegalArgumentException("illegal start character; will overflow");
        }
      }
      else {
        if (i > 13) {
          break label173;
        }
      }
      label167:
      k++;
      continue;
      label173:
      throw new IllegalArgumentException("too long; should have 13 characters");
      label183:
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("base base-32 character: ");
      paramArrayOfByte.append(m);
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    if (i == 13) {
      return l;
    }
    throw new IllegalArgumentException("too short; should have 13 characters");
  }
  
  private static final String encodeBase32(long paramLong)
  {
    char[] arrayOfChar1 = ENCODE;
    char[] arrayOfChar2 = new char[16];
    int i = arrayOfChar2.length;
    for (int j = 0; j < 13; j++)
    {
      int k = i;
      if (j > 0)
      {
        k = i;
        if (j % 4 == 1)
        {
          k = i - 1;
          arrayOfChar2[k] = ((char)45);
        }
      }
      int m = (int)(0x1F & paramLong);
      paramLong >>>= 5;
      i = k - 1;
      arrayOfChar2[i] = ((char)arrayOfChar1[m]);
    }
    return String.valueOf(arrayOfChar2);
  }
  
  public static VerifierDeviceIdentity generate()
  {
    return generate(new SecureRandom());
  }
  
  @VisibleForTesting
  static VerifierDeviceIdentity generate(Random paramRandom)
  {
    return new VerifierDeviceIdentity(paramRandom.nextLong());
  }
  
  public static VerifierDeviceIdentity parse(String paramString)
    throws IllegalArgumentException
  {
    try
    {
      paramString = paramString.getBytes("US-ASCII");
      return new VerifierDeviceIdentity(decodeBase32(paramString));
    }
    catch (UnsupportedEncodingException paramString)
    {
      throw new IllegalArgumentException("bad base-32 characters in input");
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof VerifierDeviceIdentity;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (VerifierDeviceIdentity)paramObject;
    if (mIdentity == mIdentity) {
      bool2 = true;
    }
    return bool2;
  }
  
  public int hashCode()
  {
    return (int)mIdentity;
  }
  
  public String toString()
  {
    return mIdentityString;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mIdentity);
  }
}
