package android.preference;

import android.animation.LayoutTransition;
import android.app.Fragment;
import android.app.FragmentBreadCrumbs;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class PreferenceActivity
  extends ListActivity
  implements PreferenceManager.OnPreferenceTreeClickListener, PreferenceFragment.OnPreferenceStartFragmentCallback
{
  private static final String BACK_STACK_PREFS = ":android:prefs";
  private static final String CUR_HEADER_TAG = ":android:cur_header";
  public static final String EXTRA_NO_HEADERS = ":android:no_headers";
  private static final String EXTRA_PREFS_SET_BACK_TEXT = "extra_prefs_set_back_text";
  private static final String EXTRA_PREFS_SET_NEXT_TEXT = "extra_prefs_set_next_text";
  private static final String EXTRA_PREFS_SHOW_BUTTON_BAR = "extra_prefs_show_button_bar";
  private static final String EXTRA_PREFS_SHOW_SKIP = "extra_prefs_show_skip";
  public static final String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";
  public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":android:show_fragment_args";
  public static final String EXTRA_SHOW_FRAGMENT_SHORT_TITLE = ":android:show_fragment_short_title";
  public static final String EXTRA_SHOW_FRAGMENT_TITLE = ":android:show_fragment_title";
  private static final int FIRST_REQUEST_CODE = 100;
  private static final String HEADERS_TAG = ":android:headers";
  public static final long HEADER_ID_UNDEFINED = -1L;
  private static final int MSG_BIND_PREFERENCES = 1;
  private static final int MSG_BUILD_HEADERS = 2;
  private static final String PREFERENCES_TAG = ":android:preferences";
  private static final String TAG = "PreferenceActivity";
  private CharSequence mActivityTitle;
  private Header mCurHeader;
  private FragmentBreadCrumbs mFragmentBreadCrumbs;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 2: 
        Object localObject = new ArrayList(mHeaders);
        mHeaders.clear();
        onBuildHeaders(mHeaders);
        if ((mAdapter instanceof BaseAdapter)) {
          ((BaseAdapter)mAdapter).notifyDataSetChanged();
        }
        paramAnonymousMessage = onGetNewHeader();
        if ((paramAnonymousMessage != null) && (fragment != null))
        {
          localObject = findBestMatchingHeader(paramAnonymousMessage, (ArrayList)localObject);
          if ((localObject == null) || (mCurHeader != localObject)) {
            switchToHeader(paramAnonymousMessage);
          }
        }
        else if (mCurHeader != null)
        {
          paramAnonymousMessage = findBestMatchingHeader(mCurHeader, mHeaders);
          if (paramAnonymousMessage != null) {
            setSelectedHeader(paramAnonymousMessage);
          }
        }
        break;
      case 1: 
        PreferenceActivity.this.bindPreferences();
      }
    }
  };
  private final ArrayList<Header> mHeaders = new ArrayList();
  private ViewGroup mHeadersContainer;
  private FrameLayout mListFooter;
  private Button mNextButton;
  private int mPreferenceHeaderItemResId = 0;
  private boolean mPreferenceHeaderRemoveEmptyIcon = false;
  private PreferenceManager mPreferenceManager;
  private ViewGroup mPrefsContainer;
  private Bundle mSavedInstanceState;
  private boolean mSinglePane;
  
  public PreferenceActivity() {}
  
  private void bindPreferences()
  {
    PreferenceScreen localPreferenceScreen = getPreferenceScreen();
    if (localPreferenceScreen != null)
    {
      localPreferenceScreen.bind(getListView());
      if (mSavedInstanceState != null)
      {
        super.onRestoreInstanceState(mSavedInstanceState);
        mSavedInstanceState = null;
      }
    }
  }
  
  private void postBindPreferences()
  {
    if (mHandler.hasMessages(1)) {
      return;
    }
    mHandler.obtainMessage(1).sendToTarget();
  }
  
  private void requirePreferenceManager()
  {
    if (mPreferenceManager == null)
    {
      if (mAdapter == null) {
        throw new RuntimeException("This should be called after super.onCreate.");
      }
      throw new RuntimeException("Modern two-pane PreferenceActivity requires use of a PreferenceFragment");
    }
  }
  
  private void switchToHeaderInner(String paramString, Bundle paramBundle)
  {
    getFragmentManager().popBackStack(":android:prefs", 1);
    if (isValidFragment(paramString))
    {
      paramString = Fragment.instantiate(this, paramString, paramBundle);
      paramBundle = getFragmentManager().beginTransaction();
      int i;
      if (mSinglePane) {
        i = 0;
      } else {
        i = 4099;
      }
      paramBundle.setTransition(i);
      paramBundle.replace(16909254, paramString);
      paramBundle.commitAllowingStateLoss();
      if ((mSinglePane) && (mPrefsContainer.getVisibility() == 8))
      {
        mPrefsContainer.setVisibility(0);
        mHeadersContainer.setVisibility(8);
      }
      return;
    }
    paramBundle = new StringBuilder();
    paramBundle.append("Invalid fragment for this activity: ");
    paramBundle.append(paramString);
    throw new IllegalArgumentException(paramBundle.toString());
  }
  
  @Deprecated
  public void addPreferencesFromIntent(Intent paramIntent)
  {
    requirePreferenceManager();
    setPreferenceScreen(mPreferenceManager.inflateFromIntent(paramIntent, getPreferenceScreen()));
  }
  
  @Deprecated
  public void addPreferencesFromResource(int paramInt)
  {
    requirePreferenceManager();
    setPreferenceScreen(mPreferenceManager.inflateFromResource(this, paramInt, getPreferenceScreen()));
  }
  
  Header findBestMatchingHeader(Header paramHeader, ArrayList<Header> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    int j = 0;
    while (j < paramArrayList.size())
    {
      Header localHeader = (Header)paramArrayList.get(j);
      if ((paramHeader != localHeader) && ((id == -1L) || (id != id)))
      {
        if (fragment != null)
        {
          if (fragment.equals(fragment)) {
            localArrayList.add(localHeader);
          }
        }
        else if (intent != null)
        {
          if (intent.equals(intent)) {
            localArrayList.add(localHeader);
          }
        }
        else if ((title != null) && (title.equals(title))) {
          localArrayList.add(localHeader);
        }
        j++;
      }
      else
      {
        localArrayList.clear();
        localArrayList.add(localHeader);
      }
    }
    int k = localArrayList.size();
    if (k == 1) {
      return (Header)localArrayList.get(0);
    }
    if (k > 1) {
      for (j = i; j < k; j++)
      {
        paramArrayList = (Header)localArrayList.get(j);
        if ((fragmentArguments != null) && (fragmentArguments.equals(fragmentArguments))) {
          return paramArrayList;
        }
        if ((extras != null) && (extras.equals(extras))) {
          return paramArrayList;
        }
        if ((title != null) && (title.equals(title))) {
          return paramArrayList;
        }
      }
    }
    return null;
  }
  
  @Deprecated
  public Preference findPreference(CharSequence paramCharSequence)
  {
    if (mPreferenceManager == null) {
      return null;
    }
    return mPreferenceManager.findPreference(paramCharSequence);
  }
  
  public void finishPreferencePanel(Fragment paramFragment, int paramInt, Intent paramIntent)
  {
    onBackPressed();
    if ((paramFragment != null) && (paramFragment.getTargetFragment() != null)) {
      paramFragment.getTargetFragment().onActivityResult(paramFragment.getTargetRequestCode(), paramInt, paramIntent);
    }
  }
  
  public List<Header> getHeaders()
  {
    return mHeaders;
  }
  
  protected Button getNextButton()
  {
    return mNextButton;
  }
  
  @Deprecated
  public PreferenceManager getPreferenceManager()
  {
    return mPreferenceManager;
  }
  
  @Deprecated
  public PreferenceScreen getPreferenceScreen()
  {
    if (mPreferenceManager != null) {
      return mPreferenceManager.getPreferenceScreen();
    }
    return null;
  }
  
  public boolean hasHeaders()
  {
    boolean bool;
    if ((mHeadersContainer != null) && (mHeadersContainer.getVisibility() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean hasNextButton()
  {
    boolean bool;
    if (mNextButton != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void invalidateHeaders()
  {
    if (!mHandler.hasMessages(2)) {
      mHandler.sendEmptyMessage(2);
    }
  }
  
  public boolean isMultiPane()
  {
    return mSinglePane ^ true;
  }
  
  protected boolean isValidFragment(String paramString)
  {
    if (getApplicationInfotargetSdkVersion < 19) {
      return true;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Subclasses of PreferenceActivity must override isValidFragment(String) to verify that the Fragment class is valid! ");
    localStringBuilder.append(getClass().getName());
    localStringBuilder.append(" has not checked if fragment ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(" is valid.");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  /* Error */
  public void loadHeadersFromResource(int paramInt, List<Header> paramList)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: aconst_null
    //   6: astore 5
    //   8: aconst_null
    //   9: astore 6
    //   11: aconst_null
    //   12: astore 7
    //   14: aconst_null
    //   15: astore 8
    //   17: aload_0
    //   18: invokevirtual 401	android/preference/PreferenceActivity:getResources	()Landroid/content/res/Resources;
    //   21: astore 9
    //   23: aload 8
    //   25: astore 6
    //   27: aload 9
    //   29: iload_1
    //   30: invokevirtual 407	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   33: astore 5
    //   35: aload 5
    //   37: astore 6
    //   39: aload 5
    //   41: astore_3
    //   42: aload 5
    //   44: astore 4
    //   46: aload 5
    //   48: invokestatic 413	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   51: astore 8
    //   53: aload 5
    //   55: astore 6
    //   57: aload 5
    //   59: astore_3
    //   60: aload 5
    //   62: astore 4
    //   64: aload 5
    //   66: invokeinterface 418 1 0
    //   71: istore_1
    //   72: iload_1
    //   73: iconst_1
    //   74: if_icmpeq +11 -> 85
    //   77: iload_1
    //   78: iconst_2
    //   79: if_icmpeq +6 -> 85
    //   82: goto -29 -> 53
    //   85: aload 5
    //   87: astore 6
    //   89: aload 5
    //   91: astore_3
    //   92: aload 5
    //   94: astore 4
    //   96: aload 5
    //   98: invokeinterface 419 1 0
    //   103: astore 7
    //   105: aload 5
    //   107: astore 6
    //   109: aload 5
    //   111: astore_3
    //   112: aload 5
    //   114: astore 4
    //   116: ldc_w 421
    //   119: aload 7
    //   121: invokevirtual 305	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   124: ifeq +643 -> 767
    //   127: aconst_null
    //   128: astore 7
    //   130: aload 5
    //   132: astore 6
    //   134: aload 5
    //   136: astore_3
    //   137: aload 5
    //   139: astore 4
    //   141: aload 5
    //   143: invokeinterface 424 1 0
    //   148: istore_1
    //   149: aload 5
    //   151: astore 6
    //   153: aload 5
    //   155: astore_3
    //   156: aload 5
    //   158: astore 4
    //   160: aload 5
    //   162: invokeinterface 418 1 0
    //   167: istore 10
    //   169: iload 10
    //   171: iconst_1
    //   172: if_icmpeq +582 -> 754
    //   175: iload 10
    //   177: iconst_3
    //   178: if_icmpne +25 -> 203
    //   181: aload 5
    //   183: astore 6
    //   185: aload 5
    //   187: astore_3
    //   188: aload 5
    //   190: astore 4
    //   192: aload 5
    //   194: invokeinterface 424 1 0
    //   199: iload_1
    //   200: if_icmple +554 -> 754
    //   203: iload 10
    //   205: iconst_3
    //   206: if_icmpeq +545 -> 751
    //   209: iload 10
    //   211: iconst_4
    //   212: if_icmpne +6 -> 218
    //   215: goto +536 -> 751
    //   218: aload 5
    //   220: astore 6
    //   222: aload 5
    //   224: astore_3
    //   225: aload 5
    //   227: astore 4
    //   229: ldc_w 426
    //   232: aload 5
    //   234: invokeinterface 419 1 0
    //   239: invokevirtual 305	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   242: ifeq +497 -> 739
    //   245: aload 5
    //   247: astore 6
    //   249: aload 5
    //   251: astore_3
    //   252: aload 5
    //   254: astore 4
    //   256: new 18	android/preference/PreferenceActivity$Header
    //   259: astore 9
    //   261: aload 5
    //   263: astore 6
    //   265: aload 5
    //   267: astore_3
    //   268: aload 5
    //   270: astore 4
    //   272: aload 9
    //   274: invokespecial 427	android/preference/PreferenceActivity$Header:<init>	()V
    //   277: aload 5
    //   279: astore 6
    //   281: aload 5
    //   283: astore_3
    //   284: aload 5
    //   286: astore 4
    //   288: getstatic 433	com/android/internal/R$styleable:PreferenceHeader	[I
    //   291: astore 11
    //   293: aload_0
    //   294: aload 8
    //   296: aload 11
    //   298: invokevirtual 437	android/preference/PreferenceActivity:obtainStyledAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   301: astore 6
    //   303: aload 9
    //   305: aload 6
    //   307: iconst_1
    //   308: iconst_m1
    //   309: invokevirtual 443	android/content/res/TypedArray:getResourceId	(II)I
    //   312: i2l
    //   313: putfield 296	android/preference/PreferenceActivity$Header:id	J
    //   316: aload 6
    //   318: iconst_2
    //   319: invokevirtual 447	android/content/res/TypedArray:peekValue	(I)Landroid/util/TypedValue;
    //   322: astore_3
    //   323: aload_3
    //   324: ifnull +39 -> 363
    //   327: aload_3
    //   328: getfield 452	android/util/TypedValue:type	I
    //   331: iconst_3
    //   332: if_icmpne +31 -> 363
    //   335: aload_3
    //   336: getfield 455	android/util/TypedValue:resourceId	I
    //   339: ifeq +15 -> 354
    //   342: aload 9
    //   344: aload_3
    //   345: getfield 455	android/util/TypedValue:resourceId	I
    //   348: putfield 458	android/preference/PreferenceActivity$Header:titleRes	I
    //   351: goto +12 -> 363
    //   354: aload 9
    //   356: aload_3
    //   357: getfield 461	android/util/TypedValue:string	Ljava/lang/CharSequence;
    //   360: putfield 318	android/preference/PreferenceActivity$Header:title	Ljava/lang/CharSequence;
    //   363: aload 6
    //   365: iconst_3
    //   366: invokevirtual 447	android/content/res/TypedArray:peekValue	(I)Landroid/util/TypedValue;
    //   369: astore_3
    //   370: aload_3
    //   371: ifnull +39 -> 410
    //   374: aload_3
    //   375: getfield 452	android/util/TypedValue:type	I
    //   378: iconst_3
    //   379: if_icmpne +31 -> 410
    //   382: aload_3
    //   383: getfield 455	android/util/TypedValue:resourceId	I
    //   386: ifeq +15 -> 401
    //   389: aload 9
    //   391: aload_3
    //   392: getfield 455	android/util/TypedValue:resourceId	I
    //   395: putfield 464	android/preference/PreferenceActivity$Header:summaryRes	I
    //   398: goto +12 -> 410
    //   401: aload 9
    //   403: aload_3
    //   404: getfield 461	android/util/TypedValue:string	Ljava/lang/CharSequence;
    //   407: putfield 467	android/preference/PreferenceActivity$Header:summary	Ljava/lang/CharSequence;
    //   410: aload 6
    //   412: iconst_5
    //   413: invokevirtual 447	android/content/res/TypedArray:peekValue	(I)Landroid/util/TypedValue;
    //   416: astore_3
    //   417: aload_3
    //   418: ifnull +39 -> 457
    //   421: aload_3
    //   422: getfield 452	android/util/TypedValue:type	I
    //   425: iconst_3
    //   426: if_icmpne +31 -> 457
    //   429: aload_3
    //   430: getfield 455	android/util/TypedValue:resourceId	I
    //   433: ifeq +15 -> 448
    //   436: aload 9
    //   438: aload_3
    //   439: getfield 455	android/util/TypedValue:resourceId	I
    //   442: putfield 470	android/preference/PreferenceActivity$Header:breadCrumbTitleRes	I
    //   445: goto +12 -> 457
    //   448: aload 9
    //   450: aload_3
    //   451: getfield 461	android/util/TypedValue:string	Ljava/lang/CharSequence;
    //   454: putfield 473	android/preference/PreferenceActivity$Header:breadCrumbTitle	Ljava/lang/CharSequence;
    //   457: aload 6
    //   459: bipush 6
    //   461: invokevirtual 447	android/content/res/TypedArray:peekValue	(I)Landroid/util/TypedValue;
    //   464: astore_3
    //   465: aload_3
    //   466: ifnull +39 -> 505
    //   469: aload_3
    //   470: getfield 452	android/util/TypedValue:type	I
    //   473: iconst_3
    //   474: if_icmpne +31 -> 505
    //   477: aload_3
    //   478: getfield 455	android/util/TypedValue:resourceId	I
    //   481: ifeq +15 -> 496
    //   484: aload 9
    //   486: aload_3
    //   487: getfield 455	android/util/TypedValue:resourceId	I
    //   490: putfield 476	android/preference/PreferenceActivity$Header:breadCrumbShortTitleRes	I
    //   493: goto +12 -> 505
    //   496: aload 9
    //   498: aload_3
    //   499: getfield 461	android/util/TypedValue:string	Ljava/lang/CharSequence;
    //   502: putfield 479	android/preference/PreferenceActivity$Header:breadCrumbShortTitle	Ljava/lang/CharSequence;
    //   505: aload 9
    //   507: aload 6
    //   509: iconst_0
    //   510: iconst_0
    //   511: invokevirtual 443	android/content/res/TypedArray:getResourceId	(II)I
    //   514: putfield 482	android/preference/PreferenceActivity$Header:iconRes	I
    //   517: aload 9
    //   519: aload 6
    //   521: iconst_4
    //   522: invokevirtual 486	android/content/res/TypedArray:getString	(I)Ljava/lang/String;
    //   525: putfield 299	android/preference/PreferenceActivity$Header:fragment	Ljava/lang/String;
    //   528: aload 6
    //   530: invokevirtual 489	android/content/res/TypedArray:recycle	()V
    //   533: aload 7
    //   535: astore 6
    //   537: aload 7
    //   539: ifnonnull +13 -> 552
    //   542: new 491	android/os/Bundle
    //   545: astore 6
    //   547: aload 6
    //   549: invokespecial 492	android/os/Bundle:<init>	()V
    //   552: aload 5
    //   554: invokeinterface 424 1 0
    //   559: istore 10
    //   561: aload 5
    //   563: invokeinterface 418 1 0
    //   568: istore 12
    //   570: iload 12
    //   572: iconst_1
    //   573: if_icmpeq +116 -> 689
    //   576: iload 12
    //   578: iconst_3
    //   579: if_icmpne +15 -> 594
    //   582: aload 5
    //   584: invokeinterface 424 1 0
    //   589: iload 10
    //   591: if_icmple +98 -> 689
    //   594: iload 12
    //   596: iconst_3
    //   597: if_icmpeq +89 -> 686
    //   600: iload 12
    //   602: iconst_4
    //   603: if_icmpne +6 -> 609
    //   606: goto +80 -> 686
    //   609: aload 5
    //   611: invokeinterface 419 1 0
    //   616: astore 7
    //   618: aload 7
    //   620: ldc_w 494
    //   623: invokevirtual 305	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   626: ifeq +25 -> 651
    //   629: aload_0
    //   630: invokevirtual 401	android/preference/PreferenceActivity:getResources	()Landroid/content/res/Resources;
    //   633: ldc_w 494
    //   636: aload 8
    //   638: aload 6
    //   640: invokevirtual 498	android/content/res/Resources:parseBundleExtra	(Ljava/lang/String;Landroid/util/AttributeSet;Landroid/os/Bundle;)V
    //   643: aload 5
    //   645: invokestatic 504	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   648: goto +38 -> 686
    //   651: aload 7
    //   653: ldc_w 505
    //   656: invokevirtual 305	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   659: ifeq +22 -> 681
    //   662: aload 9
    //   664: aload_0
    //   665: invokevirtual 401	android/preference/PreferenceActivity:getResources	()Landroid/content/res/Resources;
    //   668: aload 5
    //   670: aload 8
    //   672: invokestatic 511	android/content/Intent:parseIntent	(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;)Landroid/content/Intent;
    //   675: putfield 312	android/preference/PreferenceActivity$Header:intent	Landroid/content/Intent;
    //   678: goto +8 -> 686
    //   681: aload 5
    //   683: invokestatic 504	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   686: goto -125 -> 561
    //   689: aload 6
    //   691: astore 7
    //   693: aload 6
    //   695: invokevirtual 512	android/os/Bundle:size	()I
    //   698: ifle +13 -> 711
    //   701: aload 9
    //   703: aload 6
    //   705: putfield 324	android/preference/PreferenceActivity$Header:fragmentArguments	Landroid/os/Bundle;
    //   708: aconst_null
    //   709: astore 7
    //   711: aload 5
    //   713: astore 6
    //   715: aload_2
    //   716: aload 9
    //   718: invokeinterface 515 2 0
    //   723: pop
    //   724: goto +27 -> 751
    //   727: astore_2
    //   728: goto +164 -> 892
    //   731: astore_2
    //   732: goto +168 -> 900
    //   735: astore_2
    //   736: goto +198 -> 934
    //   739: aload 5
    //   741: astore 6
    //   743: aload 5
    //   745: invokestatic 504	com/android/internal/util/XmlUtils:skipCurrentTag	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   748: goto +3 -> 751
    //   751: goto -602 -> 149
    //   754: aload 5
    //   756: ifnull +10 -> 766
    //   759: aload 5
    //   761: invokeinterface 518 1 0
    //   766: return
    //   767: aload 5
    //   769: astore 6
    //   771: new 184	java/lang/RuntimeException
    //   774: astore_3
    //   775: aload 5
    //   777: astore 6
    //   779: new 249	java/lang/StringBuilder
    //   782: astore_2
    //   783: aload 5
    //   785: astore 6
    //   787: aload_2
    //   788: invokespecial 250	java/lang/StringBuilder:<init>	()V
    //   791: aload 5
    //   793: astore 6
    //   795: aload_2
    //   796: ldc_w 520
    //   799: invokevirtual 256	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   802: pop
    //   803: aload 5
    //   805: astore 6
    //   807: aload_2
    //   808: aload 7
    //   810: invokevirtual 256	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   813: pop
    //   814: aload 5
    //   816: astore 6
    //   818: aload_2
    //   819: ldc_w 522
    //   822: invokevirtual 256	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   825: pop
    //   826: aload 5
    //   828: astore 6
    //   830: aload_2
    //   831: aload 5
    //   833: invokeinterface 525 1 0
    //   838: invokevirtual 256	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   841: pop
    //   842: aload 5
    //   844: astore 6
    //   846: aload_3
    //   847: aload_2
    //   848: invokevirtual 262	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   851: invokespecial 189	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   854: aload 5
    //   856: astore 6
    //   858: aload_3
    //   859: athrow
    //   860: astore_2
    //   861: goto +39 -> 900
    //   864: astore_2
    //   865: goto +69 -> 934
    //   868: astore_2
    //   869: aload 6
    //   871: astore 5
    //   873: goto +19 -> 892
    //   876: astore_2
    //   877: aload_3
    //   878: astore 5
    //   880: goto +20 -> 900
    //   883: astore_2
    //   884: aload 4
    //   886: astore 5
    //   888: goto +46 -> 934
    //   891: astore_2
    //   892: goto +76 -> 968
    //   895: astore_2
    //   896: aload 6
    //   898: astore 5
    //   900: aload 5
    //   902: astore 6
    //   904: new 184	java/lang/RuntimeException
    //   907: astore 7
    //   909: aload 5
    //   911: astore 6
    //   913: aload 7
    //   915: ldc_w 527
    //   918: aload_2
    //   919: invokespecial 530	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   922: aload 5
    //   924: astore 6
    //   926: aload 7
    //   928: athrow
    //   929: astore_2
    //   930: aload 7
    //   932: astore 5
    //   934: aload 5
    //   936: astore 6
    //   938: new 184	java/lang/RuntimeException
    //   941: astore 7
    //   943: aload 5
    //   945: astore 6
    //   947: aload 7
    //   949: ldc_w 527
    //   952: aload_2
    //   953: invokespecial 530	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   956: aload 5
    //   958: astore 6
    //   960: aload 7
    //   962: athrow
    //   963: astore_2
    //   964: aload 6
    //   966: astore 5
    //   968: aload 5
    //   970: ifnull +10 -> 980
    //   973: aload 5
    //   975: invokeinterface 518 1 0
    //   980: aload_2
    //   981: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	982	0	this	PreferenceActivity
    //   0	982	1	paramInt	int
    //   0	982	2	paramList	List<Header>
    //   1	877	3	localObject1	Object
    //   3	882	4	localObject2	Object
    //   6	968	5	localObject3	Object
    //   9	956	6	localObject4	Object
    //   12	949	7	localObject5	Object
    //   15	656	8	localAttributeSet	android.util.AttributeSet
    //   21	696	9	localObject6	Object
    //   167	425	10	i	int
    //   291	6	11	arrayOfInt	int[]
    //   568	36	12	j	int
    // Exception table:
    //   from	to	target	type
    //   293	323	727	finally
    //   327	351	727	finally
    //   354	363	727	finally
    //   363	370	727	finally
    //   374	398	727	finally
    //   401	410	727	finally
    //   410	417	727	finally
    //   421	445	727	finally
    //   448	457	727	finally
    //   457	465	727	finally
    //   469	493	727	finally
    //   496	505	727	finally
    //   505	533	727	finally
    //   542	552	727	finally
    //   552	561	727	finally
    //   561	570	727	finally
    //   582	594	727	finally
    //   609	648	727	finally
    //   651	678	727	finally
    //   681	686	727	finally
    //   693	708	727	finally
    //   293	323	731	java/io/IOException
    //   327	351	731	java/io/IOException
    //   354	363	731	java/io/IOException
    //   363	370	731	java/io/IOException
    //   374	398	731	java/io/IOException
    //   401	410	731	java/io/IOException
    //   410	417	731	java/io/IOException
    //   421	445	731	java/io/IOException
    //   448	457	731	java/io/IOException
    //   457	465	731	java/io/IOException
    //   469	493	731	java/io/IOException
    //   496	505	731	java/io/IOException
    //   505	533	731	java/io/IOException
    //   542	552	731	java/io/IOException
    //   552	561	731	java/io/IOException
    //   561	570	731	java/io/IOException
    //   582	594	731	java/io/IOException
    //   609	648	731	java/io/IOException
    //   651	678	731	java/io/IOException
    //   681	686	731	java/io/IOException
    //   693	708	731	java/io/IOException
    //   293	323	735	org/xmlpull/v1/XmlPullParserException
    //   327	351	735	org/xmlpull/v1/XmlPullParserException
    //   354	363	735	org/xmlpull/v1/XmlPullParserException
    //   363	370	735	org/xmlpull/v1/XmlPullParserException
    //   374	398	735	org/xmlpull/v1/XmlPullParserException
    //   401	410	735	org/xmlpull/v1/XmlPullParserException
    //   410	417	735	org/xmlpull/v1/XmlPullParserException
    //   421	445	735	org/xmlpull/v1/XmlPullParserException
    //   448	457	735	org/xmlpull/v1/XmlPullParserException
    //   457	465	735	org/xmlpull/v1/XmlPullParserException
    //   469	493	735	org/xmlpull/v1/XmlPullParserException
    //   496	505	735	org/xmlpull/v1/XmlPullParserException
    //   505	533	735	org/xmlpull/v1/XmlPullParserException
    //   542	552	735	org/xmlpull/v1/XmlPullParserException
    //   552	561	735	org/xmlpull/v1/XmlPullParserException
    //   561	570	735	org/xmlpull/v1/XmlPullParserException
    //   582	594	735	org/xmlpull/v1/XmlPullParserException
    //   609	648	735	org/xmlpull/v1/XmlPullParserException
    //   651	678	735	org/xmlpull/v1/XmlPullParserException
    //   681	686	735	org/xmlpull/v1/XmlPullParserException
    //   693	708	735	org/xmlpull/v1/XmlPullParserException
    //   715	724	860	java/io/IOException
    //   743	748	860	java/io/IOException
    //   771	775	860	java/io/IOException
    //   779	783	860	java/io/IOException
    //   787	791	860	java/io/IOException
    //   795	803	860	java/io/IOException
    //   807	814	860	java/io/IOException
    //   818	826	860	java/io/IOException
    //   830	842	860	java/io/IOException
    //   846	854	860	java/io/IOException
    //   858	860	860	java/io/IOException
    //   715	724	864	org/xmlpull/v1/XmlPullParserException
    //   743	748	864	org/xmlpull/v1/XmlPullParserException
    //   771	775	864	org/xmlpull/v1/XmlPullParserException
    //   779	783	864	org/xmlpull/v1/XmlPullParserException
    //   787	791	864	org/xmlpull/v1/XmlPullParserException
    //   795	803	864	org/xmlpull/v1/XmlPullParserException
    //   807	814	864	org/xmlpull/v1/XmlPullParserException
    //   818	826	864	org/xmlpull/v1/XmlPullParserException
    //   830	842	864	org/xmlpull/v1/XmlPullParserException
    //   846	854	864	org/xmlpull/v1/XmlPullParserException
    //   858	860	864	org/xmlpull/v1/XmlPullParserException
    //   27	35	868	finally
    //   46	53	868	finally
    //   64	72	868	finally
    //   96	105	868	finally
    //   116	127	868	finally
    //   141	149	868	finally
    //   160	169	868	finally
    //   192	203	868	finally
    //   229	245	868	finally
    //   256	261	868	finally
    //   272	277	868	finally
    //   288	293	868	finally
    //   27	35	876	java/io/IOException
    //   46	53	876	java/io/IOException
    //   64	72	876	java/io/IOException
    //   96	105	876	java/io/IOException
    //   116	127	876	java/io/IOException
    //   141	149	876	java/io/IOException
    //   160	169	876	java/io/IOException
    //   192	203	876	java/io/IOException
    //   229	245	876	java/io/IOException
    //   256	261	876	java/io/IOException
    //   272	277	876	java/io/IOException
    //   288	293	876	java/io/IOException
    //   27	35	883	org/xmlpull/v1/XmlPullParserException
    //   46	53	883	org/xmlpull/v1/XmlPullParserException
    //   64	72	883	org/xmlpull/v1/XmlPullParserException
    //   96	105	883	org/xmlpull/v1/XmlPullParserException
    //   116	127	883	org/xmlpull/v1/XmlPullParserException
    //   141	149	883	org/xmlpull/v1/XmlPullParserException
    //   160	169	883	org/xmlpull/v1/XmlPullParserException
    //   192	203	883	org/xmlpull/v1/XmlPullParserException
    //   229	245	883	org/xmlpull/v1/XmlPullParserException
    //   256	261	883	org/xmlpull/v1/XmlPullParserException
    //   272	277	883	org/xmlpull/v1/XmlPullParserException
    //   288	293	883	org/xmlpull/v1/XmlPullParserException
    //   17	23	891	finally
    //   17	23	895	java/io/IOException
    //   17	23	929	org/xmlpull/v1/XmlPullParserException
    //   715	724	963	finally
    //   743	748	963	finally
    //   771	775	963	finally
    //   779	783	963	finally
    //   787	791	963	finally
    //   795	803	963	finally
    //   807	814	963	finally
    //   818	826	963	finally
    //   830	842	963	finally
    //   846	854	963	finally
    //   858	860	963	finally
    //   904	909	963	finally
    //   913	922	963	finally
    //   926	929	963	finally
    //   938	943	963	finally
    //   947	956	963	finally
    //   960	963	963	finally
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (mPreferenceManager != null) {
      mPreferenceManager.dispatchActivityResult(paramInt1, paramInt2, paramIntent);
    }
  }
  
  public void onBackPressed()
  {
    if ((mCurHeader != null) && (mSinglePane) && (getFragmentManager().getBackStackEntryCount() == 0) && (getIntent().getStringExtra(":android:show_fragment") == null))
    {
      mCurHeader = null;
      mPrefsContainer.setVisibility(8);
      mHeadersContainer.setVisibility(0);
      if (mActivityTitle != null) {
        showBreadCrumbs(mActivityTitle, null);
      }
      getListView().clearChoices();
    }
    else
    {
      super.onBackPressed();
    }
  }
  
  public void onBuildHeaders(List<Header> paramList) {}
  
  public Intent onBuildStartFragmentIntent(String paramString, Bundle paramBundle, int paramInt1, int paramInt2)
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.setClass(this, getClass());
    localIntent.putExtra(":android:show_fragment", paramString);
    localIntent.putExtra(":android:show_fragment_args", paramBundle);
    localIntent.putExtra(":android:show_fragment_title", paramInt1);
    localIntent.putExtra(":android:show_fragment_short_title", paramInt2);
    localIntent.putExtra(":android:no_headers", true);
    return localIntent;
  }
  
  public void onContentChanged()
  {
    super.onContentChanged();
    if (mPreferenceManager != null) {
      postBindPreferences();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject1 = obtainStyledAttributes(null, R.styleable.PreferenceActivity, 17891496, 0);
    int i = ((TypedArray)localObject1).getResourceId(0, 17367262);
    mPreferenceHeaderItemResId = ((TypedArray)localObject1).getResourceId(1, 17367256);
    mPreferenceHeaderRemoveEmptyIcon = ((TypedArray)localObject1).getBoolean(2, false);
    ((TypedArray)localObject1).recycle();
    setContentView(i);
    mListFooter = ((FrameLayout)findViewById(16909091));
    mPrefsContainer = ((ViewGroup)findViewById(16909256));
    mHeadersContainer = ((ViewGroup)findViewById(16909002));
    boolean bool;
    if ((!onIsHidingHeaders()) && (onIsMultiPane())) {
      bool = false;
    } else {
      bool = true;
    }
    mSinglePane = bool;
    localObject1 = getIntent().getStringExtra(":android:show_fragment");
    Object localObject2 = getIntent().getBundleExtra(":android:show_fragment_args");
    i = getIntent().getIntExtra(":android:show_fragment_title", 0);
    int j = getIntent().getIntExtra(":android:show_fragment_short_title", 0);
    mActivityTitle = getTitle();
    if (paramBundle != null)
    {
      localObject2 = paramBundle.getParcelableArrayList(":android:headers");
      if (localObject2 != null)
      {
        mHeaders.addAll((Collection)localObject2);
        int k = paramBundle.getInt(":android:cur_header", -1);
        if ((k >= 0) && (k < mHeaders.size())) {
          setSelectedHeader((Header)mHeaders.get(k));
        } else if ((!mSinglePane) && (localObject1 == null)) {
          switchToHeader(onGetInitialHeader());
        }
      }
      else
      {
        showBreadCrumbs(getTitle(), null);
      }
    }
    else
    {
      if (!onIsHidingHeaders()) {
        onBuildHeaders(mHeaders);
      }
      if (localObject1 != null) {
        switchToHeader((String)localObject1, (Bundle)localObject2);
      } else if ((!mSinglePane) && (mHeaders.size() > 0)) {
        switchToHeader(onGetInitialHeader());
      }
    }
    if (mHeaders.size() > 0)
    {
      setListAdapter(new HeaderAdapter(this, mHeaders, mPreferenceHeaderItemResId, mPreferenceHeaderRemoveEmptyIcon));
      if (!mSinglePane) {
        getListView().setChoiceMode(1);
      }
    }
    if ((mSinglePane) && (localObject1 != null) && (i != 0))
    {
      localObject2 = getText(i);
      if (j != 0) {
        paramBundle = getText(j);
      } else {
        paramBundle = null;
      }
      showBreadCrumbs((CharSequence)localObject2, paramBundle);
    }
    if ((mHeaders.size() == 0) && (localObject1 == null))
    {
      setContentView(17367264);
      mListFooter = ((FrameLayout)findViewById(16909091));
      mPrefsContainer = ((ViewGroup)findViewById(16909254));
      mPreferenceManager = new PreferenceManager(this, 100);
      mPreferenceManager.setOnPreferenceTreeClickListener(this);
      mHeadersContainer = null;
    }
    else if (mSinglePane)
    {
      if ((localObject1 == null) && (mCurHeader == null)) {
        mPrefsContainer.setVisibility(8);
      } else {
        mHeadersContainer.setVisibility(8);
      }
      ((ViewGroup)findViewById(16909255)).setLayoutTransition(new LayoutTransition());
    }
    else if ((mHeaders.size() > 0) && (mCurHeader != null))
    {
      setSelectedHeader(mCurHeader);
    }
    paramBundle = getIntent();
    if (paramBundle.getBooleanExtra("extra_prefs_show_button_bar", false))
    {
      findViewById(16908828).setVisibility(0);
      localObject1 = (Button)findViewById(16908802);
      ((Button)localObject1).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          setResult(0);
          finish();
        }
      });
      localObject2 = (Button)findViewById(16909375);
      ((Button)localObject2).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          setResult(-1);
          finish();
        }
      });
      mNextButton = ((Button)findViewById(16909160));
      mNextButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          setResult(-1);
          finish();
        }
      });
      String str;
      if (paramBundle.hasExtra("extra_prefs_set_next_text"))
      {
        str = paramBundle.getStringExtra("extra_prefs_set_next_text");
        if (TextUtils.isEmpty(str)) {
          mNextButton.setVisibility(8);
        } else {
          mNextButton.setText(str);
        }
      }
      if (paramBundle.hasExtra("extra_prefs_set_back_text"))
      {
        str = paramBundle.getStringExtra("extra_prefs_set_back_text");
        if (TextUtils.isEmpty(str)) {
          ((Button)localObject1).setVisibility(8);
        } else {
          ((Button)localObject1).setText(str);
        }
      }
      if (paramBundle.getBooleanExtra("extra_prefs_show_skip", false)) {
        ((Button)localObject2).setVisibility(0);
      }
    }
  }
  
  protected void onDestroy()
  {
    mHandler.removeMessages(1);
    mHandler.removeMessages(2);
    super.onDestroy();
    if (mPreferenceManager != null) {
      mPreferenceManager.dispatchActivityDestroy();
    }
  }
  
  public Header onGetInitialHeader()
  {
    for (int i = 0; i < mHeaders.size(); i++)
    {
      Header localHeader = (Header)mHeaders.get(i);
      if (fragment != null) {
        return localHeader;
      }
    }
    throw new IllegalStateException("Must have at least one header with a fragment");
  }
  
  public Header onGetNewHeader()
  {
    return null;
  }
  
  public void onHeaderClick(Header paramHeader, int paramInt)
  {
    if (fragment != null) {
      switchToHeader(paramHeader);
    } else if (intent != null) {
      startActivity(intent);
    }
  }
  
  public boolean onIsHidingHeaders()
  {
    return getIntent().getBooleanExtra(":android:no_headers", false);
  }
  
  public boolean onIsMultiPane()
  {
    return getResources().getBoolean(17957114);
  }
  
  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    if (!isResumed()) {
      return;
    }
    super.onListItemClick(paramListView, paramView, paramInt, paramLong);
    if (mAdapter != null)
    {
      paramListView = mAdapter.getItem(paramInt);
      if ((paramListView instanceof Header)) {
        onHeaderClick((Header)paramListView, paramInt);
      }
    }
  }
  
  protected void onNewIntent(Intent paramIntent)
  {
    if (mPreferenceManager != null) {
      mPreferenceManager.dispatchNewIntent(paramIntent);
    }
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == 16908332)
    {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  public boolean onPreferenceStartFragment(PreferenceFragment paramPreferenceFragment, Preference paramPreference)
  {
    startPreferencePanel(paramPreference.getFragment(), paramPreference.getExtras(), paramPreference.getTitleRes(), paramPreference.getTitle(), null, 0);
    return true;
  }
  
  @Deprecated
  public boolean onPreferenceTreeClick(PreferenceScreen paramPreferenceScreen, Preference paramPreference)
  {
    return false;
  }
  
  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    if (mPreferenceManager != null)
    {
      Bundle localBundle = paramBundle.getBundle(":android:preferences");
      if (localBundle != null)
      {
        PreferenceScreen localPreferenceScreen = getPreferenceScreen();
        if (localPreferenceScreen != null)
        {
          localPreferenceScreen.restoreHierarchyState(localBundle);
          mSavedInstanceState = paramBundle;
          return;
        }
      }
    }
    super.onRestoreInstanceState(paramBundle);
    if ((!mSinglePane) && (mCurHeader != null)) {
      setSelectedHeader(mCurHeader);
    }
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (mHeaders.size() > 0)
    {
      paramBundle.putParcelableArrayList(":android:headers", mHeaders);
      if (mCurHeader != null)
      {
        int i = mHeaders.indexOf(mCurHeader);
        if (i >= 0) {
          paramBundle.putInt(":android:cur_header", i);
        }
      }
    }
    if (mPreferenceManager != null)
    {
      PreferenceScreen localPreferenceScreen = getPreferenceScreen();
      if (localPreferenceScreen != null)
      {
        Bundle localBundle = new Bundle();
        localPreferenceScreen.saveHierarchyState(localBundle);
        paramBundle.putBundle(":android:preferences", localBundle);
      }
    }
  }
  
  protected void onStop()
  {
    super.onStop();
    if (mPreferenceManager != null) {
      mPreferenceManager.dispatchActivityStop();
    }
  }
  
  public void setListFooter(View paramView)
  {
    mListFooter.removeAllViews();
    mListFooter.addView(paramView, new FrameLayout.LayoutParams(-1, -2));
  }
  
  public void setParentTitle(CharSequence paramCharSequence1, CharSequence paramCharSequence2, View.OnClickListener paramOnClickListener)
  {
    if (mFragmentBreadCrumbs != null) {
      mFragmentBreadCrumbs.setParentTitle(paramCharSequence1, paramCharSequence2, paramOnClickListener);
    }
  }
  
  @Deprecated
  public void setPreferenceScreen(PreferenceScreen paramPreferenceScreen)
  {
    requirePreferenceManager();
    if ((mPreferenceManager.setPreferences(paramPreferenceScreen)) && (paramPreferenceScreen != null))
    {
      postBindPreferences();
      paramPreferenceScreen = getPreferenceScreen().getTitle();
      if (paramPreferenceScreen != null) {
        setTitle(paramPreferenceScreen);
      }
    }
  }
  
  void setSelectedHeader(Header paramHeader)
  {
    mCurHeader = paramHeader;
    int i = mHeaders.indexOf(paramHeader);
    if (i >= 0) {
      getListView().setItemChecked(i, true);
    } else {
      getListView().clearChoices();
    }
    showBreadCrumbs(paramHeader);
  }
  
  void showBreadCrumbs(Header paramHeader)
  {
    if (paramHeader != null)
    {
      Object localObject1 = paramHeader.getBreadCrumbTitle(getResources());
      Object localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = paramHeader.getTitle(getResources());
      }
      localObject1 = localObject2;
      if (localObject2 == null) {
        localObject1 = getTitle();
      }
      showBreadCrumbs((CharSequence)localObject1, paramHeader.getBreadCrumbShortTitle(getResources()));
    }
    else
    {
      showBreadCrumbs(getTitle(), null);
    }
  }
  
  public void showBreadCrumbs(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    if (mFragmentBreadCrumbs == null)
    {
      View localView = findViewById(16908310);
      try
      {
        mFragmentBreadCrumbs = ((FragmentBreadCrumbs)localView);
        if (mFragmentBreadCrumbs == null)
        {
          if (paramCharSequence1 != null) {
            setTitle(paramCharSequence1);
          }
          return;
        }
        if (mSinglePane)
        {
          mFragmentBreadCrumbs.setVisibility(8);
          localView = findViewById(16908817);
          if (localView != null) {
            localView.setVisibility(8);
          }
          setTitle(paramCharSequence1);
        }
        mFragmentBreadCrumbs.setMaxVisible(2);
        mFragmentBreadCrumbs.setActivity(this);
      }
      catch (ClassCastException paramCharSequence2)
      {
        setTitle(paramCharSequence1);
        return;
      }
    }
    if (mFragmentBreadCrumbs.getVisibility() != 0)
    {
      setTitle(paramCharSequence1);
    }
    else
    {
      mFragmentBreadCrumbs.setTitle(paramCharSequence1, paramCharSequence2);
      mFragmentBreadCrumbs.setParentTitle(null, null, null);
    }
  }
  
  public void startPreferenceFragment(Fragment paramFragment, boolean paramBoolean)
  {
    FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
    localFragmentTransaction.replace(16909254, paramFragment);
    if (paramBoolean)
    {
      localFragmentTransaction.setTransition(4097);
      localFragmentTransaction.addToBackStack(":android:prefs");
    }
    else
    {
      localFragmentTransaction.setTransition(4099);
    }
    localFragmentTransaction.commitAllowingStateLoss();
  }
  
  public void startPreferencePanel(String paramString, Bundle paramBundle, int paramInt1, CharSequence paramCharSequence, Fragment paramFragment, int paramInt2)
  {
    paramString = Fragment.instantiate(this, paramString, paramBundle);
    if (paramFragment != null) {
      paramString.setTargetFragment(paramFragment, paramInt2);
    }
    paramBundle = getFragmentManager().beginTransaction();
    paramBundle.replace(16909254, paramString);
    if (paramInt1 != 0) {
      paramBundle.setBreadCrumbTitle(paramInt1);
    } else if (paramCharSequence != null) {
      paramBundle.setBreadCrumbTitle(paramCharSequence);
    }
    paramBundle.setTransition(4097);
    paramBundle.addToBackStack(":android:prefs");
    paramBundle.commitAllowingStateLoss();
  }
  
  public void startWithFragment(String paramString, Bundle paramBundle, Fragment paramFragment, int paramInt)
  {
    startWithFragment(paramString, paramBundle, paramFragment, paramInt, 0, 0);
  }
  
  public void startWithFragment(String paramString, Bundle paramBundle, Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3)
  {
    paramString = onBuildStartFragmentIntent(paramString, paramBundle, paramInt2, paramInt3);
    if (paramFragment == null) {
      startActivity(paramString);
    } else {
      paramFragment.startActivityForResult(paramString, paramInt1);
    }
  }
  
  public void switchToHeader(Header paramHeader)
  {
    if (mCurHeader == paramHeader)
    {
      getFragmentManager().popBackStack(":android:prefs", 1);
    }
    else
    {
      if (fragment == null) {
        break label46;
      }
      switchToHeaderInner(fragment, fragmentArguments);
      setSelectedHeader(paramHeader);
    }
    return;
    label46:
    throw new IllegalStateException("can't switch to header that has no fragment");
  }
  
  public void switchToHeader(String paramString, Bundle paramBundle)
  {
    Object localObject1 = null;
    Object localObject2;
    for (int i = 0;; i++)
    {
      localObject2 = localObject1;
      if (i >= mHeaders.size()) {
        break;
      }
      if (paramString.equals(mHeaders.get(i)).fragment))
      {
        localObject2 = (Header)mHeaders.get(i);
        break;
      }
    }
    setSelectedHeader((Header)localObject2);
    switchToHeaderInner(paramString, paramBundle);
  }
  
  public static final class Header
    implements Parcelable
  {
    public static final Parcelable.Creator<Header> CREATOR = new Parcelable.Creator()
    {
      public PreferenceActivity.Header createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PreferenceActivity.Header(paramAnonymousParcel);
      }
      
      public PreferenceActivity.Header[] newArray(int paramAnonymousInt)
      {
        return new PreferenceActivity.Header[paramAnonymousInt];
      }
    };
    public CharSequence breadCrumbShortTitle;
    public int breadCrumbShortTitleRes;
    public CharSequence breadCrumbTitle;
    public int breadCrumbTitleRes;
    public Bundle extras;
    public String fragment;
    public Bundle fragmentArguments;
    public int iconRes;
    public long id = -1L;
    public Intent intent;
    public CharSequence summary;
    public int summaryRes;
    public CharSequence title;
    public int titleRes;
    
    public Header() {}
    
    Header(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public CharSequence getBreadCrumbShortTitle(Resources paramResources)
    {
      if (breadCrumbShortTitleRes != 0) {
        return paramResources.getText(breadCrumbShortTitleRes);
      }
      return breadCrumbShortTitle;
    }
    
    public CharSequence getBreadCrumbTitle(Resources paramResources)
    {
      if (breadCrumbTitleRes != 0) {
        return paramResources.getText(breadCrumbTitleRes);
      }
      return breadCrumbTitle;
    }
    
    public CharSequence getSummary(Resources paramResources)
    {
      if (summaryRes != 0) {
        return paramResources.getText(summaryRes);
      }
      return summary;
    }
    
    public CharSequence getTitle(Resources paramResources)
    {
      if (titleRes != 0) {
        return paramResources.getText(titleRes);
      }
      return title;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      id = paramParcel.readLong();
      titleRes = paramParcel.readInt();
      title = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      summaryRes = paramParcel.readInt();
      summary = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      breadCrumbTitleRes = paramParcel.readInt();
      breadCrumbTitle = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      breadCrumbShortTitleRes = paramParcel.readInt();
      breadCrumbShortTitle = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      iconRes = paramParcel.readInt();
      fragment = paramParcel.readString();
      fragmentArguments = paramParcel.readBundle();
      if (paramParcel.readInt() != 0) {
        intent = ((Intent)Intent.CREATOR.createFromParcel(paramParcel));
      }
      extras = paramParcel.readBundle();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(id);
      paramParcel.writeInt(titleRes);
      TextUtils.writeToParcel(title, paramParcel, paramInt);
      paramParcel.writeInt(summaryRes);
      TextUtils.writeToParcel(summary, paramParcel, paramInt);
      paramParcel.writeInt(breadCrumbTitleRes);
      TextUtils.writeToParcel(breadCrumbTitle, paramParcel, paramInt);
      paramParcel.writeInt(breadCrumbShortTitleRes);
      TextUtils.writeToParcel(breadCrumbShortTitle, paramParcel, paramInt);
      paramParcel.writeInt(iconRes);
      paramParcel.writeString(fragment);
      paramParcel.writeBundle(fragmentArguments);
      if (intent != null)
      {
        paramParcel.writeInt(1);
        intent.writeToParcel(paramParcel, paramInt);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      paramParcel.writeBundle(extras);
    }
  }
  
  private static class HeaderAdapter
    extends ArrayAdapter<PreferenceActivity.Header>
  {
    private LayoutInflater mInflater;
    private int mLayoutResId;
    private boolean mRemoveIconIfEmpty;
    
    public HeaderAdapter(Context paramContext, List<PreferenceActivity.Header> paramList, int paramInt, boolean paramBoolean)
    {
      super(0, paramList);
      mInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
      mLayoutResId = paramInt;
      mRemoveIconIfEmpty = paramBoolean;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = mInflater.inflate(mLayoutResId, paramViewGroup, false);
        paramViewGroup = new HeaderViewHolder(null);
        icon = ((ImageView)paramView.findViewById(16908294));
        title = ((TextView)paramView.findViewById(16908310));
        summary = ((TextView)paramView.findViewById(16908304));
        paramView.setTag(paramViewGroup);
      }
      else
      {
        paramViewGroup = (HeaderViewHolder)paramView.getTag();
      }
      Object localObject = (PreferenceActivity.Header)getItem(paramInt);
      if (mRemoveIconIfEmpty)
      {
        if (iconRes == 0)
        {
          icon.setVisibility(8);
        }
        else
        {
          icon.setVisibility(0);
          icon.setImageResource(iconRes);
        }
      }
      else {
        icon.setImageResource(iconRes);
      }
      title.setText(((PreferenceActivity.Header)localObject).getTitle(getContext().getResources()));
      localObject = ((PreferenceActivity.Header)localObject).getSummary(getContext().getResources());
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        summary.setVisibility(0);
        summary.setText((CharSequence)localObject);
      }
      else
      {
        summary.setVisibility(8);
      }
      return paramView;
    }
    
    private static class HeaderViewHolder
    {
      ImageView icon;
      TextView summary;
      TextView title;
      
      private HeaderViewHolder() {}
    }
  }
}
