package android.security;

import android.content.Context;
import java.security.KeyStore.ProtectionParameter;

@Deprecated
public final class KeyStoreParameter
  implements KeyStore.ProtectionParameter
{
  private final int mFlags;
  
  private KeyStoreParameter(int paramInt)
  {
    mFlags = paramInt;
  }
  
  public int getFlags()
  {
    return mFlags;
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
    private int mFlags;
    
    public Builder(Context paramContext)
    {
      if (paramContext != null) {
        return;
      }
      throw new NullPointerException("context == null");
    }
    
    public KeyStoreParameter build()
    {
      return new KeyStoreParameter(mFlags, null);
    }
    
    public Builder setEncryptionRequired(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x1;
      } else {
        mFlags &= 0xFFFFFFFE;
      }
      return this;
    }
  }
}
