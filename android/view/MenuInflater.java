package android.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.MenuItemImpl;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MenuInflater
{
  private static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
  private static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = { Context.class };
  private static final String LOG_TAG = "MenuInflater";
  private static final int NO_ID = 0;
  private static final String XML_GROUP = "group";
  private static final String XML_ITEM = "item";
  private static final String XML_MENU = "menu";
  private final Object[] mActionProviderConstructorArguments;
  private final Object[] mActionViewConstructorArguments;
  private Context mContext;
  private Object mRealOwner;
  
  public MenuInflater(Context paramContext)
  {
    mContext = paramContext;
    mActionViewConstructorArguments = new Object[] { paramContext };
    mActionProviderConstructorArguments = mActionViewConstructorArguments;
  }
  
  public MenuInflater(Context paramContext, Object paramObject)
  {
    mContext = paramContext;
    mRealOwner = paramObject;
    mActionViewConstructorArguments = new Object[] { paramContext };
    mActionProviderConstructorArguments = mActionViewConstructorArguments;
  }
  
  private Object findRealOwner(Object paramObject)
  {
    if ((paramObject instanceof Activity)) {
      return paramObject;
    }
    if ((paramObject instanceof ContextWrapper)) {
      return findRealOwner(((ContextWrapper)paramObject).getBaseContext());
    }
    return paramObject;
  }
  
  private Object getRealOwner()
  {
    if (mRealOwner == null) {
      mRealOwner = findRealOwner(mContext);
    }
    return mRealOwner;
  }
  
  private void parseMenu(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Menu paramMenu)
    throws XmlPullParserException, IOException
  {
    MenuState localMenuState = new MenuState(paramMenu);
    int i = paramXmlPullParser.getEventType();
    int j = 0;
    Object localObject = null;
    int k;
    do
    {
      if (i == 2)
      {
        paramMenu = paramXmlPullParser.getName();
        if (paramMenu.equals("menu"))
        {
          k = paramXmlPullParser.next();
          break;
        }
        paramXmlPullParser = new StringBuilder();
        paramXmlPullParser.append("Expecting menu, got ");
        paramXmlPullParser.append(paramMenu);
        throw new RuntimeException(paramXmlPullParser.toString());
      }
      k = paramXmlPullParser.next();
      i = k;
    } while (k != 1);
    i = 0;
    int m = k;
    while (i == 0)
    {
      int n;
      switch (m)
      {
      default: 
        k = j;
        paramMenu = (Menu)localObject;
        n = i;
        break;
      case 3: 
        String str = paramXmlPullParser.getName();
        if ((j != 0) && (str.equals(localObject)))
        {
          k = 0;
          paramMenu = null;
          n = i;
        }
        else if (str.equals("group"))
        {
          localMenuState.resetGroup();
          k = j;
          paramMenu = (Menu)localObject;
          n = i;
        }
        else if (str.equals("item"))
        {
          k = j;
          paramMenu = (Menu)localObject;
          n = i;
          if (!localMenuState.hasAddedItem()) {
            if ((itemActionProvider != null) && (itemActionProvider.hasSubMenu()))
            {
              registerMenu(localMenuState.addSubMenuItem(), paramAttributeSet);
              k = j;
              paramMenu = (Menu)localObject;
              n = i;
            }
            else
            {
              registerMenu(localMenuState.addItem(), paramAttributeSet);
              k = j;
              paramMenu = (Menu)localObject;
              n = i;
            }
          }
        }
        else
        {
          k = j;
          paramMenu = (Menu)localObject;
          n = i;
          if (str.equals("menu"))
          {
            n = 1;
            k = j;
            paramMenu = (Menu)localObject;
          }
        }
        break;
      case 2: 
        if (j != 0)
        {
          k = j;
          paramMenu = (Menu)localObject;
          n = i;
        }
        else
        {
          paramMenu = paramXmlPullParser.getName();
          if (paramMenu.equals("group"))
          {
            localMenuState.readGroup(paramAttributeSet);
            k = j;
            paramMenu = (Menu)localObject;
            n = i;
          }
          else if (paramMenu.equals("item"))
          {
            localMenuState.readItem(paramAttributeSet);
            k = j;
            paramMenu = (Menu)localObject;
            n = i;
          }
          else if (paramMenu.equals("menu"))
          {
            paramMenu = localMenuState.addSubMenuItem();
            registerMenu(paramMenu, paramAttributeSet);
            parseMenu(paramXmlPullParser, paramAttributeSet, paramMenu);
            k = j;
            paramMenu = (Menu)localObject;
            n = i;
          }
          else
          {
            k = 1;
            n = i;
          }
        }
        break;
      case 1: 
        throw new RuntimeException("Unexpected end of document");
      }
      m = paramXmlPullParser.next();
      j = k;
      localObject = paramMenu;
      i = n;
    }
  }
  
  private void registerMenu(MenuItem paramMenuItem, AttributeSet paramAttributeSet) {}
  
  private void registerMenu(SubMenu paramSubMenu, AttributeSet paramAttributeSet) {}
  
  Context getContext()
  {
    return mContext;
  }
  
  /* Error */
  public void inflate(int paramInt, Menu paramMenu)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: aconst_null
    //   6: astore 5
    //   8: aload_0
    //   9: getfield 54	android/view/MenuInflater:mContext	Landroid/content/Context;
    //   12: invokevirtual 182	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   15: iload_1
    //   16: invokevirtual 188	android/content/res/Resources:getLayout	(I)Landroid/content/res/XmlResourceParser;
    //   19: astore 6
    //   21: aload 6
    //   23: astore 5
    //   25: aload 6
    //   27: astore_3
    //   28: aload 6
    //   30: astore 4
    //   32: aload_0
    //   33: aload 6
    //   35: aload 6
    //   37: invokestatic 194	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   40: aload_2
    //   41: invokespecial 172	android/view/MenuInflater:parseMenu	(Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;Landroid/view/Menu;)V
    //   44: aload 6
    //   46: ifnull +10 -> 56
    //   49: aload 6
    //   51: invokeinterface 199 1 0
    //   56: return
    //   57: astore_2
    //   58: goto +55 -> 113
    //   61: astore_2
    //   62: aload_3
    //   63: astore 5
    //   65: new 201	android/view/InflateException
    //   68: astore 4
    //   70: aload_3
    //   71: astore 5
    //   73: aload 4
    //   75: ldc -53
    //   77: aload_2
    //   78: invokespecial 206	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   81: aload_3
    //   82: astore 5
    //   84: aload 4
    //   86: athrow
    //   87: astore_3
    //   88: aload 4
    //   90: astore 5
    //   92: new 201	android/view/InflateException
    //   95: astore_2
    //   96: aload 4
    //   98: astore 5
    //   100: aload_2
    //   101: ldc -53
    //   103: aload_3
    //   104: invokespecial 206	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   107: aload 4
    //   109: astore 5
    //   111: aload_2
    //   112: athrow
    //   113: aload 5
    //   115: ifnull +10 -> 125
    //   118: aload 5
    //   120: invokeinterface 199 1 0
    //   125: aload_2
    //   126: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	127	0	this	MenuInflater
    //   0	127	1	paramInt	int
    //   0	127	2	paramMenu	Menu
    //   1	81	3	localObject1	Object
    //   87	17	3	localXmlPullParserException	XmlPullParserException
    //   3	105	4	localObject2	Object
    //   6	113	5	localObject3	Object
    //   19	31	6	localXmlResourceParser	android.content.res.XmlResourceParser
    // Exception table:
    //   from	to	target	type
    //   8	21	57	finally
    //   32	44	57	finally
    //   65	70	57	finally
    //   73	81	57	finally
    //   84	87	57	finally
    //   92	96	57	finally
    //   100	107	57	finally
    //   111	113	57	finally
    //   8	21	61	java/io/IOException
    //   32	44	61	java/io/IOException
    //   8	21	87	org/xmlpull/v1/XmlPullParserException
    //   32	44	87	org/xmlpull/v1/XmlPullParserException
  }
  
  private static class InflatedOnMenuItemClickListener
    implements MenuItem.OnMenuItemClickListener
  {
    private static final Class<?>[] PARAM_TYPES = { MenuItem.class };
    private Method mMethod;
    private Object mRealOwner;
    
    public InflatedOnMenuItemClickListener(Object paramObject, String paramString)
    {
      mRealOwner = paramObject;
      Class localClass = paramObject.getClass();
      try
      {
        mMethod = localClass.getMethod(paramString, PARAM_TYPES);
        return;
      }
      catch (Exception paramObject)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Couldn't resolve menu item onClick handler ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" in class ");
        localStringBuilder.append(localClass.getName());
        paramString = new InflateException(localStringBuilder.toString());
        paramString.initCause(paramObject);
        throw paramString;
      }
    }
    
    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      try
      {
        if (mMethod.getReturnType() == Boolean.TYPE) {
          return ((Boolean)mMethod.invoke(mRealOwner, new Object[] { paramMenuItem })).booleanValue();
        }
        mMethod.invoke(mRealOwner, new Object[] { paramMenuItem });
        return true;
      }
      catch (Exception paramMenuItem)
      {
        throw new RuntimeException(paramMenuItem);
      }
    }
  }
  
  private class MenuState
  {
    private static final int defaultGroupId = 0;
    private static final int defaultItemCategory = 0;
    private static final int defaultItemCheckable = 0;
    private static final boolean defaultItemChecked = false;
    private static final boolean defaultItemEnabled = true;
    private static final int defaultItemId = 0;
    private static final int defaultItemOrder = 0;
    private static final boolean defaultItemVisible = true;
    private int groupCategory;
    private int groupCheckable;
    private boolean groupEnabled;
    private int groupId;
    private int groupOrder;
    private boolean groupVisible;
    private ActionProvider itemActionProvider;
    private String itemActionProviderClassName;
    private String itemActionViewClassName;
    private int itemActionViewLayout;
    private boolean itemAdded;
    private int itemAlphabeticModifiers;
    private char itemAlphabeticShortcut;
    private int itemCategoryOrder;
    private int itemCheckable;
    private boolean itemChecked;
    private CharSequence itemContentDescription;
    private boolean itemEnabled;
    private int itemIconResId;
    private ColorStateList itemIconTintList = null;
    private PorterDuff.Mode itemIconTintMode = null;
    private int itemId;
    private String itemListenerMethodName;
    private int itemNumericModifiers;
    private char itemNumericShortcut;
    private int itemShowAsAction;
    private CharSequence itemTitle;
    private CharSequence itemTitleCondensed;
    private CharSequence itemTooltipText;
    private boolean itemVisible;
    private Menu menu;
    
    public MenuState(Menu paramMenu)
    {
      menu = paramMenu;
      resetGroup();
    }
    
    private char getShortcut(String paramString)
    {
      if (paramString == null) {
        return '\000';
      }
      return paramString.charAt(0);
    }
    
    private <T> T newInstance(String paramString, Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject)
    {
      try
      {
        paramArrayOfClass = mContext.getClassLoader().loadClass(paramString).getConstructor(paramArrayOfClass);
        paramArrayOfClass.setAccessible(true);
        paramArrayOfClass = paramArrayOfClass.newInstance(paramArrayOfObject);
        return paramArrayOfClass;
      }
      catch (Exception paramArrayOfClass)
      {
        paramArrayOfObject = new StringBuilder();
        paramArrayOfObject.append("Cannot instantiate class: ");
        paramArrayOfObject.append(paramString);
        Log.w("MenuInflater", paramArrayOfObject.toString(), paramArrayOfClass);
      }
      return null;
    }
    
    private void setItem(MenuItem paramMenuItem)
    {
      Object localObject = paramMenuItem.setChecked(itemChecked).setVisible(itemVisible).setEnabled(itemEnabled);
      boolean bool;
      if (itemCheckable >= 1) {
        bool = true;
      } else {
        bool = false;
      }
      ((MenuItem)localObject).setCheckable(bool).setTitleCondensed(itemTitleCondensed).setIcon(itemIconResId).setAlphabeticShortcut(itemAlphabeticShortcut, itemAlphabeticModifiers).setNumericShortcut(itemNumericShortcut, itemNumericModifiers);
      if (itemShowAsAction >= 0) {
        paramMenuItem.setShowAsAction(itemShowAsAction);
      }
      if (itemIconTintMode != null) {
        paramMenuItem.setIconTintMode(itemIconTintMode);
      }
      if (itemIconTintList != null) {
        paramMenuItem.setIconTintList(itemIconTintList);
      }
      if (itemListenerMethodName != null) {
        if (!mContext.isRestricted()) {
          paramMenuItem.setOnMenuItemClickListener(new MenuInflater.InflatedOnMenuItemClickListener(MenuInflater.this.getRealOwner(), itemListenerMethodName));
        } else {
          throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
        }
      }
      if ((paramMenuItem instanceof MenuItemImpl))
      {
        localObject = (MenuItemImpl)paramMenuItem;
        if (itemCheckable >= 2) {
          ((MenuItemImpl)localObject).setExclusiveCheckable(true);
        }
      }
      int i = 0;
      if (itemActionViewClassName != null)
      {
        paramMenuItem.setActionView((View)newInstance(itemActionViewClassName, MenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, mActionViewConstructorArguments));
        i = 1;
      }
      if (itemActionViewLayout > 0) {
        if (i == 0) {
          paramMenuItem.setActionView(itemActionViewLayout);
        } else {
          Log.w("MenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
        }
      }
      if (itemActionProvider != null) {
        paramMenuItem.setActionProvider(itemActionProvider);
      }
      paramMenuItem.setContentDescription(itemContentDescription);
      paramMenuItem.setTooltipText(itemTooltipText);
    }
    
    public MenuItem addItem()
    {
      itemAdded = true;
      MenuItem localMenuItem = menu.add(groupId, itemId, itemCategoryOrder, itemTitle);
      setItem(localMenuItem);
      return localMenuItem;
    }
    
    public SubMenu addSubMenuItem()
    {
      itemAdded = true;
      SubMenu localSubMenu = menu.addSubMenu(groupId, itemId, itemCategoryOrder, itemTitle);
      setItem(localSubMenu.getItem());
      return localSubMenu;
    }
    
    public boolean hasAddedItem()
    {
      return itemAdded;
    }
    
    public void readGroup(AttributeSet paramAttributeSet)
    {
      paramAttributeSet = mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MenuGroup);
      groupId = paramAttributeSet.getResourceId(1, 0);
      groupCategory = paramAttributeSet.getInt(3, 0);
      groupOrder = paramAttributeSet.getInt(4, 0);
      groupCheckable = paramAttributeSet.getInt(5, 0);
      groupVisible = paramAttributeSet.getBoolean(2, true);
      groupEnabled = paramAttributeSet.getBoolean(0, true);
      paramAttributeSet.recycle();
    }
    
    public void readItem(AttributeSet paramAttributeSet)
    {
      paramAttributeSet = mContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MenuItem);
      itemId = paramAttributeSet.getResourceId(2, 0);
      itemCategoryOrder = (0xFFFF0000 & paramAttributeSet.getInt(5, groupCategory) | 0xFFFF & paramAttributeSet.getInt(6, groupOrder));
      itemTitle = paramAttributeSet.getText(7);
      itemTitleCondensed = paramAttributeSet.getText(8);
      itemIconResId = paramAttributeSet.getResourceId(0, 0);
      if (paramAttributeSet.hasValue(22)) {
        itemIconTintMode = Drawable.parseTintMode(paramAttributeSet.getInt(22, -1), itemIconTintMode);
      } else {
        itemIconTintMode = null;
      }
      if (paramAttributeSet.hasValue(21)) {
        itemIconTintList = paramAttributeSet.getColorStateList(21);
      } else {
        itemIconTintList = null;
      }
      itemAlphabeticShortcut = getShortcut(paramAttributeSet.getString(9));
      itemAlphabeticModifiers = paramAttributeSet.getInt(19, 4096);
      itemNumericShortcut = getShortcut(paramAttributeSet.getString(10));
      itemNumericModifiers = paramAttributeSet.getInt(20, 4096);
      if (paramAttributeSet.hasValue(11)) {
        itemCheckable = paramAttributeSet.getBoolean(11, false);
      } else {
        itemCheckable = groupCheckable;
      }
      itemChecked = paramAttributeSet.getBoolean(3, false);
      itemVisible = paramAttributeSet.getBoolean(4, groupVisible);
      boolean bool = groupEnabled;
      int i = 1;
      itemEnabled = paramAttributeSet.getBoolean(1, bool);
      itemShowAsAction = paramAttributeSet.getInt(14, -1);
      itemListenerMethodName = paramAttributeSet.getString(12);
      itemActionViewLayout = paramAttributeSet.getResourceId(15, 0);
      itemActionViewClassName = paramAttributeSet.getString(16);
      itemActionProviderClassName = paramAttributeSet.getString(17);
      if (itemActionProviderClassName == null) {
        i = 0;
      }
      if ((i != 0) && (itemActionViewLayout == 0) && (itemActionViewClassName == null))
      {
        itemActionProvider = ((ActionProvider)newInstance(itemActionProviderClassName, MenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, mActionProviderConstructorArguments));
      }
      else
      {
        if (i != 0) {
          Log.w("MenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified.");
        }
        itemActionProvider = null;
      }
      itemContentDescription = paramAttributeSet.getText(13);
      itemTooltipText = paramAttributeSet.getText(18);
      paramAttributeSet.recycle();
      itemAdded = false;
    }
    
    public void resetGroup()
    {
      groupId = 0;
      groupCategory = 0;
      groupOrder = 0;
      groupCheckable = 0;
      groupVisible = true;
      groupEnabled = true;
    }
  }
}
