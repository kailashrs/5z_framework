package android.webkit;

import android.annotation.SystemApi;
import android.net.Uri;
import java.security.KeyPair;

@SystemApi
public abstract class TokenBindingService
{
  public static final String KEY_ALGORITHM_ECDSAP256 = "ECDSAP256";
  public static final String KEY_ALGORITHM_RSA2048_PKCS_1_5 = "RSA2048_PKCS_1.5";
  public static final String KEY_ALGORITHM_RSA2048_PSS = "RSA2048PSS";
  
  public TokenBindingService() {}
  
  public static TokenBindingService getInstance()
  {
    return WebViewFactory.getProvider().getTokenBindingService();
  }
  
  public abstract void deleteAllKeys(ValueCallback<Boolean> paramValueCallback);
  
  public abstract void deleteKey(Uri paramUri, ValueCallback<Boolean> paramValueCallback);
  
  public abstract void enableTokenBinding();
  
  public abstract void getKey(Uri paramUri, String[] paramArrayOfString, ValueCallback<TokenBindingKey> paramValueCallback);
  
  public static abstract class TokenBindingKey
  {
    public TokenBindingKey() {}
    
    public abstract String getAlgorithm();
    
    public abstract KeyPair getKeyPair();
  }
}
