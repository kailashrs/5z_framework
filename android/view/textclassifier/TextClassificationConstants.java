package android.view.textclassifier;

import android.util.KeyValueListParser;
import android.util.Slog;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public final class TextClassificationConstants
{
  private static final String CLASSIFY_TEXT_MAX_RANGE_LENGTH = "classify_text_max_range_length";
  private static final int CLASSIFY_TEXT_MAX_RANGE_LENGTH_DEFAULT = 10000;
  private static final String ENTITY_LIST_DEFAULT = "entity_list_default";
  private static final String ENTITY_LIST_DEFAULT_VALUE = new StringJoiner(":").add("address").add("email").add("phone").add("url").add("date").add("datetime").add("flight").toString();
  private static final String ENTITY_LIST_DELIMITER = ":";
  private static final String ENTITY_LIST_EDITABLE = "entity_list_editable";
  private static final String ENTITY_LIST_NOT_EDITABLE = "entity_list_not_editable";
  private static final String GENERATE_LINKS_LOG_SAMPLE_RATE = "generate_links_log_sample_rate";
  private static final int GENERATE_LINKS_LOG_SAMPLE_RATE_DEFAULT = 100;
  private static final String GENERATE_LINKS_MAX_TEXT_LENGTH = "generate_links_max_text_length";
  private static final int GENERATE_LINKS_MAX_TEXT_LENGTH_DEFAULT = 100000;
  private static final String LOCAL_TEXT_CLASSIFIER_ENABLED = "local_textclassifier_enabled";
  private static final boolean LOCAL_TEXT_CLASSIFIER_ENABLED_DEFAULT = true;
  private static final String LOG_TAG = "TextClassificationConstants";
  private static final String MODEL_DARK_LAUNCH_ENABLED = "model_dark_launch_enabled";
  private static final boolean MODEL_DARK_LAUNCH_ENABLED_DEFAULT = false;
  private static final String SMART_LINKIFY_ENABLED = "smart_linkify_enabled";
  private static final boolean SMART_LINKIFY_ENABLED_DEFAULT = true;
  private static final String SMART_SELECTION_ENABLED = "smart_selection_enabled";
  private static final boolean SMART_SELECTION_ENABLED_DEFAULT = true;
  private static final String SMART_SELECT_ANIMATION_ENABLED = "smart_select_animation_enabled";
  private static final boolean SMART_SELECT_ANIMATION_ENABLED_DEFAULT = true;
  private static final String SMART_TEXT_SHARE_ENABLED = "smart_text_share_enabled";
  private static final boolean SMART_TEXT_SHARE_ENABLED_DEFAULT = true;
  private static final String SUGGEST_SELECTION_MAX_RANGE_LENGTH = "suggest_selection_max_range_length";
  private static final int SUGGEST_SELECTION_MAX_RANGE_LENGTH_DEFAULT = 10000;
  private static final String SYSTEM_TEXT_CLASSIFIER_ENABLED = "system_textclassifier_enabled";
  private static final boolean SYSTEM_TEXT_CLASSIFIER_ENABLED_DEFAULT = true;
  private final int mClassifyTextMaxRangeLength;
  private final List<String> mEntityListDefault;
  private final List<String> mEntityListEditable;
  private final List<String> mEntityListNotEditable;
  private final int mGenerateLinksLogSampleRate;
  private final int mGenerateLinksMaxTextLength;
  private final boolean mLocalTextClassifierEnabled;
  private final boolean mModelDarkLaunchEnabled;
  private final boolean mSmartLinkifyEnabled;
  private final boolean mSmartSelectionAnimationEnabled;
  private final boolean mSmartSelectionEnabled;
  private final boolean mSmartTextShareEnabled;
  private final int mSuggestSelectionMaxRangeLength;
  private final boolean mSystemTextClassifierEnabled;
  
  private TextClassificationConstants(String paramString)
  {
    KeyValueListParser localKeyValueListParser = new KeyValueListParser(',');
    try
    {
      localKeyValueListParser.setString(paramString);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Bad TextClassifier settings: ");
      localStringBuilder.append(paramString);
      Slog.e("TextClassificationConstants", localStringBuilder.toString());
    }
    mSystemTextClassifierEnabled = localKeyValueListParser.getBoolean("system_textclassifier_enabled", true);
    mLocalTextClassifierEnabled = localKeyValueListParser.getBoolean("local_textclassifier_enabled", true);
    mModelDarkLaunchEnabled = localKeyValueListParser.getBoolean("model_dark_launch_enabled", false);
    mSmartSelectionEnabled = localKeyValueListParser.getBoolean("smart_selection_enabled", true);
    mSmartTextShareEnabled = localKeyValueListParser.getBoolean("smart_text_share_enabled", true);
    mSmartLinkifyEnabled = localKeyValueListParser.getBoolean("smart_linkify_enabled", true);
    mSmartSelectionAnimationEnabled = localKeyValueListParser.getBoolean("smart_select_animation_enabled", true);
    mSuggestSelectionMaxRangeLength = localKeyValueListParser.getInt("suggest_selection_max_range_length", 10000);
    mClassifyTextMaxRangeLength = localKeyValueListParser.getInt("classify_text_max_range_length", 10000);
    mGenerateLinksMaxTextLength = localKeyValueListParser.getInt("generate_links_max_text_length", 100000);
    mGenerateLinksLogSampleRate = localKeyValueListParser.getInt("generate_links_log_sample_rate", 100);
    mEntityListDefault = parseEntityList(localKeyValueListParser.getString("entity_list_default", ENTITY_LIST_DEFAULT_VALUE));
    mEntityListNotEditable = parseEntityList(localKeyValueListParser.getString("entity_list_not_editable", ENTITY_LIST_DEFAULT_VALUE));
    mEntityListEditable = parseEntityList(localKeyValueListParser.getString("entity_list_editable", ENTITY_LIST_DEFAULT_VALUE));
  }
  
  public static TextClassificationConstants loadFromString(String paramString)
  {
    return new TextClassificationConstants(paramString);
  }
  
  private static List<String> parseEntityList(String paramString)
  {
    return Collections.unmodifiableList(Arrays.asList(paramString.split(":")));
  }
  
  public int getClassifyTextMaxRangeLength()
  {
    return mClassifyTextMaxRangeLength;
  }
  
  public List<String> getEntityListDefault()
  {
    return mEntityListDefault;
  }
  
  public List<String> getEntityListEditable()
  {
    return mEntityListEditable;
  }
  
  public List<String> getEntityListNotEditable()
  {
    return mEntityListNotEditable;
  }
  
  public int getGenerateLinksLogSampleRate()
  {
    return mGenerateLinksLogSampleRate;
  }
  
  public int getGenerateLinksMaxTextLength()
  {
    return mGenerateLinksMaxTextLength;
  }
  
  public int getSuggestSelectionMaxRangeLength()
  {
    return mSuggestSelectionMaxRangeLength;
  }
  
  public boolean isLocalTextClassifierEnabled()
  {
    return mLocalTextClassifierEnabled;
  }
  
  public boolean isModelDarkLaunchEnabled()
  {
    return mModelDarkLaunchEnabled;
  }
  
  public boolean isSmartLinkifyEnabled()
  {
    return mSmartLinkifyEnabled;
  }
  
  public boolean isSmartSelectionAnimationEnabled()
  {
    return mSmartSelectionAnimationEnabled;
  }
  
  public boolean isSmartSelectionEnabled()
  {
    return mSmartSelectionEnabled;
  }
  
  public boolean isSmartTextShareEnabled()
  {
    return mSmartTextShareEnabled;
  }
  
  public boolean isSystemTextClassifierEnabled()
  {
    return mSystemTextClassifierEnabled;
  }
}
