package android.security;

import android.content.Context;
import android.security.keystore.KeyProperties.KeyAlgorithm;
import android.text.TextUtils;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;
import javax.security.auth.x500.X500Principal;

@Deprecated
public final class KeyPairGeneratorSpec
  implements AlgorithmParameterSpec
{
  private final Context mContext;
  private final Date mEndDate;
  private final int mFlags;
  private final int mKeySize;
  private final String mKeyType;
  private final String mKeystoreAlias;
  private final BigInteger mSerialNumber;
  private final AlgorithmParameterSpec mSpec;
  private final Date mStartDate;
  private final X500Principal mSubjectDN;
  
  public KeyPairGeneratorSpec(Context paramContext, String paramString1, String paramString2, int paramInt1, AlgorithmParameterSpec paramAlgorithmParameterSpec, X500Principal paramX500Principal, BigInteger paramBigInteger, Date paramDate1, Date paramDate2, int paramInt2)
  {
    if (paramContext != null)
    {
      if (!TextUtils.isEmpty(paramString1))
      {
        if (paramX500Principal != null)
        {
          if (paramBigInteger != null)
          {
            if (paramDate1 != null)
            {
              if (paramDate2 != null)
              {
                if (!paramDate2.before(paramDate1))
                {
                  if (!paramDate2.before(paramDate1))
                  {
                    mContext = paramContext;
                    mKeystoreAlias = paramString1;
                    mKeyType = paramString2;
                    mKeySize = paramInt1;
                    mSpec = paramAlgorithmParameterSpec;
                    mSubjectDN = paramX500Principal;
                    mSerialNumber = paramBigInteger;
                    mStartDate = paramDate1;
                    mEndDate = paramDate2;
                    mFlags = paramInt2;
                    return;
                  }
                  throw new IllegalArgumentException("endDate < startDate");
                }
                throw new IllegalArgumentException("endDate < startDate");
              }
              throw new IllegalArgumentException("endDate == null");
            }
            throw new IllegalArgumentException("startDate == null");
          }
          throw new IllegalArgumentException("serialNumber == null");
        }
        throw new IllegalArgumentException("subjectDN == null");
      }
      throw new IllegalArgumentException("keyStoreAlias must not be empty");
    }
    throw new IllegalArgumentException("context == null");
  }
  
  public AlgorithmParameterSpec getAlgorithmParameterSpec()
  {
    return mSpec;
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public Date getEndDate()
  {
    return mEndDate;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public int getKeySize()
  {
    return mKeySize;
  }
  
  public String getKeyType()
  {
    return mKeyType;
  }
  
  public String getKeystoreAlias()
  {
    return mKeystoreAlias;
  }
  
  public BigInteger getSerialNumber()
  {
    return mSerialNumber;
  }
  
  public Date getStartDate()
  {
    return mStartDate;
  }
  
  public X500Principal getSubjectDN()
  {
    return mSubjectDN;
  }
  
  public boolean isEncryptionRequired()
  {
    int i = mFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public static final class Builder
  {
    private final Context mContext;
    private Date mEndDate;
    private int mFlags;
    private int mKeySize = -1;
    private String mKeyType;
    private String mKeystoreAlias;
    private BigInteger mSerialNumber;
    private AlgorithmParameterSpec mSpec;
    private Date mStartDate;
    private X500Principal mSubjectDN;
    
    public Builder(Context paramContext)
    {
      if (paramContext != null)
      {
        mContext = paramContext;
        return;
      }
      throw new NullPointerException("context == null");
    }
    
    public KeyPairGeneratorSpec build()
    {
      return new KeyPairGeneratorSpec(mContext, mKeystoreAlias, mKeyType, mKeySize, mSpec, mSubjectDN, mSerialNumber, mStartDate, mEndDate, mFlags);
    }
    
    public Builder setAlgorithmParameterSpec(AlgorithmParameterSpec paramAlgorithmParameterSpec)
    {
      if (paramAlgorithmParameterSpec != null)
      {
        mSpec = paramAlgorithmParameterSpec;
        return this;
      }
      throw new NullPointerException("spec == null");
    }
    
    public Builder setAlias(String paramString)
    {
      if (paramString != null)
      {
        mKeystoreAlias = paramString;
        return this;
      }
      throw new NullPointerException("alias == null");
    }
    
    public Builder setEncryptionRequired()
    {
      mFlags |= 0x1;
      return this;
    }
    
    public Builder setEndDate(Date paramDate)
    {
      if (paramDate != null)
      {
        mEndDate = paramDate;
        return this;
      }
      throw new NullPointerException("endDate == null");
    }
    
    public Builder setKeySize(int paramInt)
    {
      if (paramInt >= 0)
      {
        mKeySize = paramInt;
        return this;
      }
      throw new IllegalArgumentException("keySize < 0");
    }
    
    public Builder setKeyType(String paramString)
      throws NoSuchAlgorithmException
    {
      if (paramString != null) {
        try
        {
          KeyProperties.KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(paramString);
          mKeyType = paramString;
          return this;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unsupported key type: ");
          localStringBuilder.append(paramString);
          throw new NoSuchAlgorithmException(localStringBuilder.toString());
        }
      }
      throw new NullPointerException("keyType == null");
    }
    
    public Builder setSerialNumber(BigInteger paramBigInteger)
    {
      if (paramBigInteger != null)
      {
        mSerialNumber = paramBigInteger;
        return this;
      }
      throw new NullPointerException("serialNumber == null");
    }
    
    public Builder setStartDate(Date paramDate)
    {
      if (paramDate != null)
      {
        mStartDate = paramDate;
        return this;
      }
      throw new NullPointerException("startDate == null");
    }
    
    public Builder setSubject(X500Principal paramX500Principal)
    {
      if (paramX500Principal != null)
      {
        mSubjectDN = paramX500Principal;
        return this;
      }
      throw new NullPointerException("subject == null");
    }
  }
}
