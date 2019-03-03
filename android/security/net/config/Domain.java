package android.security.net.config;

import java.util.Locale;

public final class Domain
{
  public final String hostname;
  public final boolean subdomainsIncluded;
  
  public Domain(String paramString, boolean paramBoolean)
  {
    if (paramString != null)
    {
      hostname = paramString.toLowerCase(Locale.US);
      subdomainsIncluded = paramBoolean;
      return;
    }
    throw new NullPointerException("Hostname must not be null");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof Domain)) {
      return false;
    }
    paramObject = (Domain)paramObject;
    if ((subdomainsIncluded != subdomainsIncluded) || (!hostname.equals(hostname))) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    int i = hostname.hashCode();
    int j;
    if (subdomainsIncluded) {
      j = 1231;
    } else {
      j = 1237;
    }
    return i ^ j;
  }
}
