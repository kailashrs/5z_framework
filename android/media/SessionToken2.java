package android.media;

import android.content.Context;
import android.media.update.ApiLoader;
import android.media.update.SessionToken2Provider;
import android.media.update.StaticProvider;
import android.os.Bundle;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SessionToken2
{
  public static final int TYPE_LIBRARY_SERVICE = 2;
  public static final int TYPE_SESSION = 0;
  public static final int TYPE_SESSION_SERVICE = 1;
  private static final int UID_UNKNOWN = -1;
  private final SessionToken2Provider mProvider;
  
  public SessionToken2(Context paramContext, String paramString1, String paramString2)
  {
    this(paramContext, paramString1, paramString2, -1);
  }
  
  public SessionToken2(Context paramContext, String paramString1, String paramString2, int paramInt)
  {
    mProvider = ApiLoader.getProvider().createSessionToken2(paramContext, this, paramString1, paramString2, paramInt);
  }
  
  public SessionToken2(SessionToken2Provider paramSessionToken2Provider)
  {
    mProvider = paramSessionToken2Provider;
  }
  
  public static SessionToken2 fromBundle(Bundle paramBundle)
  {
    return ApiLoader.getProvider().fromBundle_SessionToken2(paramBundle);
  }
  
  public boolean equals(Object paramObject)
  {
    return mProvider.equals_impl(paramObject);
  }
  
  public String getId()
  {
    return mProvider.getId_imp();
  }
  
  public String getPackageName()
  {
    return mProvider.getPackageName_impl();
  }
  
  public SessionToken2Provider getProvider()
  {
    return mProvider;
  }
  
  public int getType()
  {
    return mProvider.getType_impl();
  }
  
  public int getUid()
  {
    return mProvider.getUid_impl();
  }
  
  public int hashCode()
  {
    return mProvider.hashCode_impl();
  }
  
  public Bundle toBundle()
  {
    return mProvider.toBundle_impl();
  }
  
  public String toString()
  {
    return mProvider.toString_impl();
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TokenType {}
}
