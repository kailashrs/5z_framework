package android.privacy.internal.rappor;

import android.privacy.DifferentialPrivacyEncoder;
import com.google.android.rappor.Encoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RapporEncoder
  implements DifferentialPrivacyEncoder
{
  private static final byte[] INSECURE_SECRET = { -41, 104, -103, -109, -108, 19, 83, 84, -2, -48, 126, 84, -2, -48, 126, 84, -41, 104, -103, -109, -108, 19, 83, 84, -2, -48, 126, 84, -2, -48, 126, 84, -41, 104, -103, -109, -108, 19, 83, 84, -2, -48, 126, 84, -2, -48, 126, 84 };
  private static final SecureRandom sSecureRandom = new SecureRandom();
  private final RapporConfig mConfig;
  private final Encoder mEncoder;
  private final boolean mIsSecure;
  
  private RapporEncoder(RapporConfig paramRapporConfig, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    mConfig = paramRapporConfig;
    mIsSecure = paramBoolean;
    Object localObject;
    if (paramBoolean)
    {
      localObject = sSecureRandom;
    }
    else
    {
      localObject = new Random(getInsecureSeed(mEncoderId));
      paramArrayOfByte = INSECURE_SECRET;
    }
    mEncoder = new Encoder((Random)localObject, null, null, paramArrayOfByte, mEncoderId, mNumBits, mProbabilityF, mProbabilityP, mProbabilityQ, mNumCohorts, mNumBloomHashes);
  }
  
  public static RapporEncoder createEncoder(RapporConfig paramRapporConfig, byte[] paramArrayOfByte)
  {
    return new RapporEncoder(paramRapporConfig, true, paramArrayOfByte);
  }
  
  public static RapporEncoder createInsecureEncoderForTest(RapporConfig paramRapporConfig)
  {
    return new RapporEncoder(paramRapporConfig, false, null);
  }
  
  private long getInsecureSeed(String paramString)
  {
    try
    {
      long l = ByteBuffer.wrap(MessageDigest.getInstance("SHA-256").digest(paramString.getBytes(StandardCharsets.UTF_8))).getLong();
      return l;
    }
    catch (NoSuchAlgorithmException paramString)
    {
      throw new AssertionError("Unable generate insecure seed");
    }
  }
  
  public byte[] encodeBits(byte[] paramArrayOfByte)
  {
    return mEncoder.encodeBits(paramArrayOfByte);
  }
  
  public byte[] encodeBoolean(boolean paramBoolean)
  {
    return mEncoder.encodeBoolean(paramBoolean);
  }
  
  public byte[] encodeString(String paramString)
  {
    return mEncoder.encodeString(paramString);
  }
  
  public RapporConfig getConfig()
  {
    return mConfig;
  }
  
  public boolean isInsecureEncoderForTest()
  {
    return mIsSecure ^ true;
  }
}
