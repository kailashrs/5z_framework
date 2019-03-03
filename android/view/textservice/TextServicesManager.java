package android.view.textservice;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import com.android.internal.textservice.ITextServicesManager;
import com.android.internal.textservice.ITextServicesManager.Stub;
import java.util.Locale;

public final class TextServicesManager
{
  private static final boolean DBG = false;
  public static final boolean DISABLE_PER_PROFILE_SPELL_CHECKER = true;
  private static final String TAG = TextServicesManager.class.getSimpleName();
  private static TextServicesManager sInstance;
  private final ITextServicesManager mService = ITextServicesManager.Stub.asInterface(ServiceManager.getServiceOrThrow("textservices"));
  
  private TextServicesManager()
    throws ServiceManager.ServiceNotFoundException
  {}
  
  public static TextServicesManager getInstance()
  {
    try
    {
      TextServicesManager localTextServicesManager1 = sInstance;
      if (localTextServicesManager1 == null) {
        try
        {
          localTextServicesManager1 = new android/view/textservice/TextServicesManager;
          localTextServicesManager1.<init>();
          sInstance = localTextServicesManager1;
        }
        catch (ServiceManager.ServiceNotFoundException localServiceNotFoundException)
        {
          IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
          localIllegalStateException.<init>(localServiceNotFoundException);
          throw localIllegalStateException;
        }
      }
      TextServicesManager localTextServicesManager2 = sInstance;
      return localTextServicesManager2;
    }
    finally {}
  }
  
  private static String parseLanguageFromLocaleString(String paramString)
  {
    int i = paramString.indexOf('_');
    if (i < 0) {
      return paramString;
    }
    return paramString.substring(0, i);
  }
  
  public SpellCheckerInfo getCurrentSpellChecker()
  {
    try
    {
      SpellCheckerInfo localSpellCheckerInfo = mService.getCurrentSpellChecker(null);
      return localSpellCheckerInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public SpellCheckerSubtype getCurrentSpellCheckerSubtype(boolean paramBoolean)
  {
    try
    {
      SpellCheckerSubtype localSpellCheckerSubtype = mService.getCurrentSpellCheckerSubtype(null, paramBoolean);
      return localSpellCheckerSubtype;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public SpellCheckerInfo[] getEnabledSpellCheckers()
  {
    try
    {
      SpellCheckerInfo[] arrayOfSpellCheckerInfo = mService.getEnabledSpellCheckers();
      return arrayOfSpellCheckerInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isSpellCheckerEnabled()
  {
    try
    {
      boolean bool = mService.isSpellCheckerEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public SpellCheckerSession newSpellCheckerSession(Bundle paramBundle, Locale paramLocale, SpellCheckerSession.SpellCheckerSessionListener paramSpellCheckerSessionListener, boolean paramBoolean)
  {
    if (paramSpellCheckerSessionListener != null)
    {
      if ((!paramBoolean) && (paramLocale == null)) {
        throw new IllegalArgumentException("Locale should not be null if you don't refer settings.");
      }
      if ((paramBoolean) && (!isSpellCheckerEnabled())) {
        return null;
      }
      try
      {
        SpellCheckerInfo localSpellCheckerInfo = mService.getCurrentSpellChecker(null);
        if (localSpellCheckerInfo == null) {
          return null;
        }
        Object localObject1 = null;
        Object localObject2;
        if (paramBoolean)
        {
          localObject1 = getCurrentSpellCheckerSubtype(true);
          if (localObject1 == null) {
            return null;
          }
          localObject2 = localObject1;
          if (paramLocale != null)
          {
            localObject2 = parseLanguageFromLocaleString(((SpellCheckerSubtype)localObject1).getLocale());
            if ((((String)localObject2).length() >= 2) && (paramLocale.getLanguage().equals(localObject2))) {
              localObject2 = localObject1;
            } else {
              return null;
            }
          }
        }
        else
        {
          String str1 = paramLocale.toString();
          int i = 0;
          for (;;)
          {
            localObject2 = localObject1;
            if (i >= localSpellCheckerInfo.getSubtypeCount()) {
              break;
            }
            localObject2 = localSpellCheckerInfo.getSubtypeAt(i);
            Object localObject3 = ((SpellCheckerSubtype)localObject2).getLocale();
            String str2 = parseLanguageFromLocaleString((String)localObject3);
            if (((String)localObject3).equals(str1)) {
              break;
            }
            localObject3 = localObject1;
            if (str2.length() >= 2)
            {
              localObject3 = localObject1;
              if (paramLocale.getLanguage().equals(str2)) {
                localObject3 = localObject2;
              }
            }
            i++;
            localObject1 = localObject3;
          }
        }
        if (localObject2 == null) {
          return null;
        }
        paramLocale = new SpellCheckerSession(localSpellCheckerInfo, mService, paramSpellCheckerSessionListener);
        try
        {
          mService.getSpellCheckerService(localSpellCheckerInfo.getId(), ((SpellCheckerSubtype)localObject2).getLocale(), paramLocale.getTextServicesSessionListener(), paramLocale.getSpellCheckerSessionListener(), paramBundle);
          return paramLocale;
        }
        catch (RemoteException paramBundle)
        {
          throw paramBundle.rethrowFromSystemServer();
        }
        throw new NullPointerException();
      }
      catch (RemoteException paramBundle)
      {
        return null;
      }
    }
  }
}
