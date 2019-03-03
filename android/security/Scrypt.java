package android.security;

public class Scrypt
{
  public Scrypt() {}
  
  native byte[] nativeScrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public byte[] scrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return nativeScrypt(paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2, paramInt3, paramInt4);
  }
}
