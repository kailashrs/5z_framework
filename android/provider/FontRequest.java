package android.provider;

import android.util.Base64;
import com.android.internal.util.Preconditions;
import java.util.Collections;
import java.util.List;

public final class FontRequest
{
  private final List<List<byte[]>> mCertificates;
  private final String mIdentifier;
  private final String mProviderAuthority;
  private final String mProviderPackage;
  private final String mQuery;
  
  public FontRequest(String paramString1, String paramString2, String paramString3)
  {
    mProviderAuthority = ((String)Preconditions.checkNotNull(paramString1));
    mQuery = ((String)Preconditions.checkNotNull(paramString3));
    mProviderPackage = ((String)Preconditions.checkNotNull(paramString2));
    mCertificates = Collections.emptyList();
    paramString1 = new StringBuilder(mProviderAuthority);
    paramString1.append("-");
    paramString1.append(mProviderPackage);
    paramString1.append("-");
    paramString1.append(mQuery);
    mIdentifier = paramString1.toString();
  }
  
  public FontRequest(String paramString1, String paramString2, String paramString3, List<List<byte[]>> paramList)
  {
    mProviderAuthority = ((String)Preconditions.checkNotNull(paramString1));
    mProviderPackage = ((String)Preconditions.checkNotNull(paramString2));
    mQuery = ((String)Preconditions.checkNotNull(paramString3));
    mCertificates = ((List)Preconditions.checkNotNull(paramList));
    paramString1 = new StringBuilder(mProviderAuthority);
    paramString1.append("-");
    paramString1.append(mProviderPackage);
    paramString1.append("-");
    paramString1.append(mQuery);
    mIdentifier = paramString1.toString();
  }
  
  public List<List<byte[]>> getCertificates()
  {
    return mCertificates;
  }
  
  public String getIdentifier()
  {
    return mIdentifier;
  }
  
  public String getProviderAuthority()
  {
    return mProviderAuthority;
  }
  
  public String getProviderPackage()
  {
    return mProviderPackage;
  }
  
  public String getQuery()
  {
    return mQuery;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("FontRequest {mProviderAuthority: ");
    ((StringBuilder)localObject).append(mProviderAuthority);
    ((StringBuilder)localObject).append(", mProviderPackage: ");
    ((StringBuilder)localObject).append(mProviderPackage);
    ((StringBuilder)localObject).append(", mQuery: ");
    ((StringBuilder)localObject).append(mQuery);
    ((StringBuilder)localObject).append(", mCertificates:");
    localStringBuilder.append(((StringBuilder)localObject).toString());
    for (int i = 0; i < mCertificates.size(); i++)
    {
      localStringBuilder.append(" [");
      localObject = (List)mCertificates.get(i);
      for (int j = 0; j < ((List)localObject).size(); j++)
      {
        localStringBuilder.append(" \"");
        localStringBuilder.append(Base64.encodeToString((byte[])((List)localObject).get(j), 0));
        localStringBuilder.append("\"");
      }
      localStringBuilder.append(" ]");
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
