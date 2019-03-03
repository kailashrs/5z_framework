package android.security.keystore;

import java.util.Date;

abstract class Utils
{
  private Utils() {}
  
  static Date cloneIfNotNull(Date paramDate)
  {
    if (paramDate != null) {
      paramDate = (Date)paramDate.clone();
    } else {
      paramDate = null;
    }
    return paramDate;
  }
  
  static byte[] cloneIfNotNull(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null) {
      paramArrayOfByte = (byte[])paramArrayOfByte.clone();
    } else {
      paramArrayOfByte = null;
    }
    return paramArrayOfByte;
  }
}
