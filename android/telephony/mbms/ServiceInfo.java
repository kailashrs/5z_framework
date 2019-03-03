package android.telephony.mbms;

import android.os.Parcel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public class ServiceInfo
{
  static final int MAP_LIMIT = 1000;
  private final String className;
  private final List<Locale> locales;
  private final Map<Locale, String> names;
  private final String serviceId;
  private final Date sessionEndTime;
  private final Date sessionStartTime;
  
  protected ServiceInfo(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    if ((i <= 1000) && (i >= 0))
    {
      names = new HashMap(i);
      Locale localLocale;
      while (i > 0)
      {
        localLocale = (Locale)paramParcel.readSerializable();
        String str = paramParcel.readString();
        names.put(localLocale, str);
        i--;
      }
      className = paramParcel.readString();
      i = paramParcel.readInt();
      if ((i <= 1000) && (i >= 0))
      {
        locales = new ArrayList(i);
        while (i > 0)
        {
          localLocale = (Locale)paramParcel.readSerializable();
          locales.add(localLocale);
          i--;
        }
        serviceId = paramParcel.readString();
        sessionStartTime = ((Date)paramParcel.readSerializable());
        sessionEndTime = ((Date)paramParcel.readSerializable());
        return;
      }
      paramParcel = new StringBuilder();
      paramParcel.append("bad locale length ");
      paramParcel.append(i);
      throw new RuntimeException(paramParcel.toString());
    }
    paramParcel = new StringBuilder();
    paramParcel.append("bad map length");
    paramParcel.append(i);
    throw new RuntimeException(paramParcel.toString());
  }
  
  public ServiceInfo(Map<Locale, String> paramMap, String paramString1, List<Locale> paramList, String paramString2, Date paramDate1, Date paramDate2)
  {
    if ((paramMap != null) && (paramString1 != null) && (paramList != null) && (paramString2 != null) && (paramDate1 != null) && (paramDate2 != null))
    {
      if (paramMap.size() <= 1000)
      {
        if (paramList.size() <= 1000)
        {
          names = new HashMap(paramMap.size());
          names.putAll(paramMap);
          className = paramString1;
          locales = new ArrayList(paramList);
          serviceId = paramString2;
          sessionStartTime = ((Date)paramDate1.clone());
          sessionEndTime = ((Date)paramDate2.clone());
          return;
        }
        paramMap = new StringBuilder();
        paramMap.append("bad locales length ");
        paramMap.append(paramList.size());
        throw new RuntimeException(paramMap.toString());
      }
      paramString1 = new StringBuilder();
      paramString1.append("bad map length ");
      paramString1.append(paramMap.size());
      throw new RuntimeException(paramString1.toString());
    }
    throw new IllegalArgumentException("Bad ServiceInfo construction");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof ServiceInfo)) {
      return false;
    }
    paramObject = (ServiceInfo)paramObject;
    if ((!Objects.equals(names, names)) || (!Objects.equals(className, className)) || (!Objects.equals(locales, locales)) || (!Objects.equals(serviceId, serviceId)) || (!Objects.equals(sessionStartTime, sessionStartTime)) || (!Objects.equals(sessionEndTime, sessionEndTime))) {
      bool = false;
    }
    return bool;
  }
  
  public List<Locale> getLocales()
  {
    return locales;
  }
  
  public CharSequence getNameForLocale(Locale paramLocale)
  {
    if (names.containsKey(paramLocale)) {
      return (CharSequence)names.get(paramLocale);
    }
    throw new NoSuchElementException("Locale not supported");
  }
  
  public Set<Locale> getNamedContentLocales()
  {
    return Collections.unmodifiableSet(names.keySet());
  }
  
  public String getServiceClassName()
  {
    return className;
  }
  
  public String getServiceId()
  {
    return serviceId;
  }
  
  public Date getSessionEndTime()
  {
    return sessionEndTime;
  }
  
  public Date getSessionStartTime()
  {
    return sessionStartTime;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { names, className, locales, serviceId, sessionStartTime, sessionEndTime });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Object localObject = names.keySet();
    paramParcel.writeInt(((Set)localObject).size());
    Iterator localIterator = ((Set)localObject).iterator();
    while (localIterator.hasNext())
    {
      localObject = (Locale)localIterator.next();
      paramParcel.writeSerializable((Serializable)localObject);
      paramParcel.writeString((String)names.get(localObject));
    }
    paramParcel.writeString(className);
    paramParcel.writeInt(locales.size());
    localObject = locales.iterator();
    while (((Iterator)localObject).hasNext()) {
      paramParcel.writeSerializable((Locale)((Iterator)localObject).next());
    }
    paramParcel.writeString(serviceId);
    paramParcel.writeSerializable(sessionStartTime);
    paramParcel.writeSerializable(sessionEndTime);
  }
}
