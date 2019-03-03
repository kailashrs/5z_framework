package android.preference;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class PreferenceInflater
  extends GenericInflater<Preference, PreferenceGroup>
{
  private static final String EXTRA_TAG_NAME = "extra";
  private static final String INTENT_TAG_NAME = "intent";
  private static final String TAG = "PreferenceInflater";
  private PreferenceManager mPreferenceManager;
  
  public PreferenceInflater(Context paramContext, PreferenceManager paramPreferenceManager)
  {
    super(paramContext);
    init(paramPreferenceManager);
  }
  
  PreferenceInflater(GenericInflater<Preference, PreferenceGroup> paramGenericInflater, PreferenceManager paramPreferenceManager, Context paramContext)
  {
    super(paramGenericInflater, paramContext);
    init(paramPreferenceManager);
  }
  
  private void init(PreferenceManager paramPreferenceManager)
  {
    mPreferenceManager = paramPreferenceManager;
    setDefaultPackage("android.preference.");
  }
  
  public GenericInflater<Preference, PreferenceGroup> cloneInContext(Context paramContext)
  {
    return new PreferenceInflater(this, mPreferenceManager, paramContext);
  }
  
  protected boolean onCreateCustomFromTag(XmlPullParser paramXmlPullParser, Preference paramPreference, AttributeSet paramAttributeSet)
    throws XmlPullParserException
  {
    String str = paramXmlPullParser.getName();
    if (str.equals("intent")) {
      try
      {
        paramXmlPullParser = Intent.parseIntent(getContext().getResources(), paramXmlPullParser, paramAttributeSet);
        if (paramXmlPullParser != null) {
          paramPreference.setIntent(paramXmlPullParser);
        }
        return true;
      }
      catch (IOException paramPreference)
      {
        paramXmlPullParser = new XmlPullParserException("Error parsing preference");
        paramXmlPullParser.initCause(paramPreference);
        throw paramXmlPullParser;
      }
    }
    if (str.equals("extra"))
    {
      getContext().getResources().parseBundleExtra("extra", paramAttributeSet, paramPreference.getExtras());
      try
      {
        XmlUtils.skipCurrentTag(paramXmlPullParser);
        return true;
      }
      catch (IOException paramPreference)
      {
        paramXmlPullParser = new XmlPullParserException("Error parsing preference");
        paramXmlPullParser.initCause(paramPreference);
        throw paramXmlPullParser;
      }
    }
    return false;
  }
  
  protected PreferenceGroup onMergeRoots(PreferenceGroup paramPreferenceGroup1, boolean paramBoolean, PreferenceGroup paramPreferenceGroup2)
  {
    if (paramPreferenceGroup1 == null)
    {
      paramPreferenceGroup2.onAttachedToHierarchy(mPreferenceManager);
      return paramPreferenceGroup2;
    }
    return paramPreferenceGroup1;
  }
}
