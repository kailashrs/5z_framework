package android.security;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AttestedKeyPair
{
  private final Certificate[] mAttestationRecord;
  private final KeyPair mKeyPair;
  
  public AttestedKeyPair(KeyPair paramKeyPair, Certificate[] paramArrayOfCertificate)
  {
    mKeyPair = paramKeyPair;
    mAttestationRecord = paramArrayOfCertificate;
  }
  
  public List<Certificate> getAttestationRecord()
  {
    if (mAttestationRecord == null) {
      return new ArrayList();
    }
    return Arrays.asList(mAttestationRecord);
  }
  
  public KeyPair getKeyPair()
  {
    return mKeyPair;
  }
}
