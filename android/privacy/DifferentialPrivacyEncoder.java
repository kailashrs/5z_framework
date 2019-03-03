package android.privacy;

public abstract interface DifferentialPrivacyEncoder
{
  public abstract byte[] encodeBits(byte[] paramArrayOfByte);
  
  public abstract byte[] encodeBoolean(boolean paramBoolean);
  
  public abstract byte[] encodeString(String paramString);
  
  public abstract DifferentialPrivacyConfig getConfig();
  
  public abstract boolean isInsecureEncoderForTest();
}
