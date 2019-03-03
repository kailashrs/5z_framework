package android.media;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class AudioPresentation
{
  public static final int MASTERED_FOR_3D = 3;
  public static final int MASTERED_FOR_HEADPHONE = 4;
  public static final int MASTERED_FOR_STEREO = 1;
  public static final int MASTERED_FOR_SURROUND = 2;
  public static final int MASTERING_NOT_INDICATED = 0;
  private final boolean mAudioDescriptionAvailable;
  private final boolean mDialogueEnhancementAvailable;
  private final Map<String, String> mLabels;
  private final String mLanguage;
  private final int mMasteringIndication;
  private final int mPresentationId;
  private final int mProgramId;
  private final boolean mSpokenSubtitlesAvailable;
  
  public AudioPresentation(int paramInt1, int paramInt2, Map<String, String> paramMap, String paramString, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    mPresentationId = paramInt1;
    mProgramId = paramInt2;
    mLanguage = paramString;
    mMasteringIndication = paramInt3;
    mAudioDescriptionAvailable = paramBoolean1;
    mSpokenSubtitlesAvailable = paramBoolean2;
    mDialogueEnhancementAvailable = paramBoolean3;
    mLabels = new HashMap(paramMap);
  }
  
  public Map<Locale, String> getLabels()
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator = mLabels.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localHashMap.put(new Locale((String)localEntry.getKey()), (String)localEntry.getValue());
    }
    return localHashMap;
  }
  
  public Locale getLocale()
  {
    return new Locale(mLanguage);
  }
  
  public int getMasteringIndication()
  {
    return mMasteringIndication;
  }
  
  public int getPresentationId()
  {
    return mPresentationId;
  }
  
  public int getProgramId()
  {
    return mProgramId;
  }
  
  public boolean hasAudioDescription()
  {
    return mAudioDescriptionAvailable;
  }
  
  public boolean hasDialogueEnhancement()
  {
    return mDialogueEnhancementAvailable;
  }
  
  public boolean hasSpokenSubtitles()
  {
    return mSpokenSubtitlesAvailable;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MasteringIndicationType {}
}
