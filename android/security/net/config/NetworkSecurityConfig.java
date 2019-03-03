package android.security.net.config;

import android.content.pm.ApplicationInfo;
import android.util.ArrayMap;
import android.util.ArraySet;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class NetworkSecurityConfig
{
  public static final boolean DEFAULT_CLEARTEXT_TRAFFIC_PERMITTED = true;
  public static final boolean DEFAULT_HSTS_ENFORCED = false;
  private Set<TrustAnchor> mAnchors;
  private final Object mAnchorsLock = new Object();
  private final List<CertificatesEntryRef> mCertificatesEntryRefs;
  private final boolean mCleartextTrafficPermitted;
  private final boolean mHstsEnforced;
  private final PinSet mPins;
  private NetworkSecurityTrustManager mTrustManager;
  private final Object mTrustManagerLock = new Object();
  
  private NetworkSecurityConfig(boolean paramBoolean1, boolean paramBoolean2, PinSet paramPinSet, List<CertificatesEntryRef> paramList)
  {
    mCleartextTrafficPermitted = paramBoolean1;
    mHstsEnforced = paramBoolean2;
    mPins = paramPinSet;
    mCertificatesEntryRefs = paramList;
    Collections.sort(mCertificatesEntryRefs, new Comparator()
    {
      public int compare(CertificatesEntryRef paramAnonymousCertificatesEntryRef1, CertificatesEntryRef paramAnonymousCertificatesEntryRef2)
      {
        if (paramAnonymousCertificatesEntryRef1.overridesPins())
        {
          int i;
          if (paramAnonymousCertificatesEntryRef2.overridesPins()) {
            i = 0;
          } else {
            i = -1;
          }
          return i;
        }
        return paramAnonymousCertificatesEntryRef2.overridesPins();
      }
    });
  }
  
  public static Builder getDefaultBuilder(ApplicationInfo paramApplicationInfo)
  {
    Builder localBuilder = new Builder().setHstsEnforced(false).addCertificatesEntryRef(new CertificatesEntryRef(SystemCertificateSource.getInstance(), false));
    boolean bool;
    if ((targetSdkVersion < 28) && (targetSandboxVersion < 2)) {
      bool = true;
    } else {
      bool = false;
    }
    localBuilder.setCleartextTrafficPermitted(bool);
    if ((targetSdkVersion <= 23) && (!paramApplicationInfo.isPrivilegedApp())) {
      localBuilder.addCertificatesEntryRef(new CertificatesEntryRef(UserCertificateSource.getInstance(), false));
    }
    return localBuilder;
  }
  
  public Set<X509Certificate> findAllCertificatesByIssuerAndSignature(X509Certificate paramX509Certificate)
  {
    ArraySet localArraySet = new ArraySet();
    Iterator localIterator = mCertificatesEntryRefs.iterator();
    while (localIterator.hasNext()) {
      localArraySet.addAll(((CertificatesEntryRef)localIterator.next()).findAllCertificatesByIssuerAndSignature(paramX509Certificate));
    }
    return localArraySet;
  }
  
  public TrustAnchor findTrustAnchorByIssuerAndSignature(X509Certificate paramX509Certificate)
  {
    Iterator localIterator = mCertificatesEntryRefs.iterator();
    while (localIterator.hasNext())
    {
      TrustAnchor localTrustAnchor = ((CertificatesEntryRef)localIterator.next()).findByIssuerAndSignature(paramX509Certificate);
      if (localTrustAnchor != null) {
        return localTrustAnchor;
      }
    }
    return null;
  }
  
  public TrustAnchor findTrustAnchorBySubjectAndPublicKey(X509Certificate paramX509Certificate)
  {
    Iterator localIterator = mCertificatesEntryRefs.iterator();
    while (localIterator.hasNext())
    {
      TrustAnchor localTrustAnchor = ((CertificatesEntryRef)localIterator.next()).findBySubjectAndPublicKey(paramX509Certificate);
      if (localTrustAnchor != null) {
        return localTrustAnchor;
      }
    }
    return null;
  }
  
  public PinSet getPins()
  {
    return mPins;
  }
  
  public Set<TrustAnchor> getTrustAnchors()
  {
    synchronized (mAnchorsLock)
    {
      if (mAnchors != null)
      {
        localObject2 = mAnchors;
        return localObject2;
      }
      Object localObject2 = new android/util/ArrayMap;
      ((ArrayMap)localObject2).<init>();
      Object localObject4 = mCertificatesEntryRefs.iterator();
      while (((Iterator)localObject4).hasNext())
      {
        Iterator localIterator = ((CertificatesEntryRef)((Iterator)localObject4).next()).getTrustAnchors().iterator();
        while (localIterator.hasNext())
        {
          TrustAnchor localTrustAnchor = (TrustAnchor)localIterator.next();
          X509Certificate localX509Certificate = certificate;
          if (!((Map)localObject2).containsKey(localX509Certificate)) {
            ((Map)localObject2).put(localX509Certificate, localTrustAnchor);
          }
        }
      }
      localObject4 = new android/util/ArraySet;
      ((ArraySet)localObject4).<init>(((Map)localObject2).size());
      ((ArraySet)localObject4).addAll(((Map)localObject2).values());
      mAnchors = ((Set)localObject4);
      localObject2 = mAnchors;
      return localObject2;
    }
  }
  
  public NetworkSecurityTrustManager getTrustManager()
  {
    synchronized (mTrustManagerLock)
    {
      if (mTrustManager == null)
      {
        localNetworkSecurityTrustManager = new android/security/net/config/NetworkSecurityTrustManager;
        localNetworkSecurityTrustManager.<init>(this);
        mTrustManager = localNetworkSecurityTrustManager;
      }
      NetworkSecurityTrustManager localNetworkSecurityTrustManager = mTrustManager;
      return localNetworkSecurityTrustManager;
    }
  }
  
  public void handleTrustStorageUpdate()
  {
    synchronized (mAnchorsLock)
    {
      mAnchors = null;
      Iterator localIterator = mCertificatesEntryRefs.iterator();
      while (localIterator.hasNext()) {
        ((CertificatesEntryRef)localIterator.next()).handleTrustStorageUpdate();
      }
      getTrustManager().handleTrustStorageUpdate();
      return;
    }
  }
  
  public boolean isCleartextTrafficPermitted()
  {
    return mCleartextTrafficPermitted;
  }
  
  public boolean isHstsEnforced()
  {
    return mHstsEnforced;
  }
  
  public static final class Builder
  {
    private List<CertificatesEntryRef> mCertificatesEntryRefs;
    private boolean mCleartextTrafficPermitted = true;
    private boolean mCleartextTrafficPermittedSet = false;
    private boolean mHstsEnforced = false;
    private boolean mHstsEnforcedSet = false;
    private Builder mParentBuilder;
    private PinSet mPinSet;
    
    public Builder() {}
    
    private List<CertificatesEntryRef> getEffectiveCertificatesEntryRefs()
    {
      if (mCertificatesEntryRefs != null) {
        return mCertificatesEntryRefs;
      }
      if (mParentBuilder != null) {
        return mParentBuilder.getEffectiveCertificatesEntryRefs();
      }
      return Collections.emptyList();
    }
    
    private boolean getEffectiveCleartextTrafficPermitted()
    {
      if (mCleartextTrafficPermittedSet) {
        return mCleartextTrafficPermitted;
      }
      if (mParentBuilder != null) {
        return mParentBuilder.getEffectiveCleartextTrafficPermitted();
      }
      return true;
    }
    
    private boolean getEffectiveHstsEnforced()
    {
      if (mHstsEnforcedSet) {
        return mHstsEnforced;
      }
      if (mParentBuilder != null) {
        return mParentBuilder.getEffectiveHstsEnforced();
      }
      return false;
    }
    
    private PinSet getEffectivePinSet()
    {
      if (mPinSet != null) {
        return mPinSet;
      }
      if (mParentBuilder != null) {
        return mParentBuilder.getEffectivePinSet();
      }
      return PinSet.EMPTY_PINSET;
    }
    
    public Builder addCertificatesEntryRef(CertificatesEntryRef paramCertificatesEntryRef)
    {
      if (mCertificatesEntryRefs == null) {
        mCertificatesEntryRefs = new ArrayList();
      }
      mCertificatesEntryRefs.add(paramCertificatesEntryRef);
      return this;
    }
    
    public Builder addCertificatesEntryRefs(Collection<? extends CertificatesEntryRef> paramCollection)
    {
      if (mCertificatesEntryRefs == null) {
        mCertificatesEntryRefs = new ArrayList();
      }
      mCertificatesEntryRefs.addAll(paramCollection);
      return this;
    }
    
    public NetworkSecurityConfig build()
    {
      return new NetworkSecurityConfig(getEffectiveCleartextTrafficPermitted(), getEffectiveHstsEnforced(), getEffectivePinSet(), getEffectiveCertificatesEntryRefs(), null);
    }
    
    List<CertificatesEntryRef> getCertificatesEntryRefs()
    {
      return mCertificatesEntryRefs;
    }
    
    public Builder getParent()
    {
      return mParentBuilder;
    }
    
    public boolean hasCertificatesEntryRefs()
    {
      boolean bool;
      if (mCertificatesEntryRefs != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public Builder setCleartextTrafficPermitted(boolean paramBoolean)
    {
      mCleartextTrafficPermitted = paramBoolean;
      mCleartextTrafficPermittedSet = true;
      return this;
    }
    
    public Builder setHstsEnforced(boolean paramBoolean)
    {
      mHstsEnforced = paramBoolean;
      mHstsEnforcedSet = true;
      return this;
    }
    
    public Builder setParent(Builder paramBuilder)
    {
      Builder localBuilder = paramBuilder;
      while (localBuilder != null) {
        if (localBuilder != this) {
          localBuilder = localBuilder.getParent();
        } else {
          throw new IllegalArgumentException("Loops are not allowed in Builder parents");
        }
      }
      mParentBuilder = paramBuilder;
      return this;
    }
    
    public Builder setPinSet(PinSet paramPinSet)
    {
      mPinSet = paramPinSet;
      return this;
    }
  }
}
