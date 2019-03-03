package android.hardware.soundtrigger;

import android.util.ArraySet;
import java.util.Locale;

public class KeyphraseMetadata
{
  public final int id;
  public final String keyphrase;
  public final int recognitionModeFlags;
  public final ArraySet<Locale> supportedLocales;
  
  public KeyphraseMetadata(int paramInt1, String paramString, ArraySet<Locale> paramArraySet, int paramInt2)
  {
    id = paramInt1;
    keyphrase = paramString;
    supportedLocales = paramArraySet;
    recognitionModeFlags = paramInt2;
  }
  
  public boolean supportsLocale(Locale paramLocale)
  {
    boolean bool;
    if ((!supportedLocales.isEmpty()) && (!supportedLocales.contains(paramLocale))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean supportsPhrase(String paramString)
  {
    boolean bool;
    if ((!keyphrase.isEmpty()) && (!keyphrase.equalsIgnoreCase(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("id=");
    localStringBuilder.append(id);
    localStringBuilder.append(", keyphrase=");
    localStringBuilder.append(keyphrase);
    localStringBuilder.append(", supported-locales=");
    localStringBuilder.append(supportedLocales);
    localStringBuilder.append(", recognition-modes=");
    localStringBuilder.append(recognitionModeFlags);
    return localStringBuilder.toString();
  }
}
