package android.inputmethodservice;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.xmlpull.v1.XmlPullParserException;

public class Keyboard
{
  public static final int EDGE_BOTTOM = 8;
  public static final int EDGE_LEFT = 1;
  public static final int EDGE_RIGHT = 2;
  public static final int EDGE_TOP = 4;
  private static final int GRID_HEIGHT = 5;
  private static final int GRID_SIZE = 50;
  private static final int GRID_WIDTH = 10;
  public static final int KEYCODE_ALT = -6;
  public static final int KEYCODE_CANCEL = -3;
  public static final int KEYCODE_DELETE = -5;
  public static final int KEYCODE_DONE = -4;
  public static final int KEYCODE_MODE_CHANGE = -2;
  public static final int KEYCODE_SHIFT = -1;
  private static float SEARCH_DISTANCE = 1.8F;
  static final String TAG = "Keyboard";
  private static final String TAG_KEY = "Key";
  private static final String TAG_KEYBOARD = "Keyboard";
  private static final String TAG_ROW = "Row";
  private int mCellHeight;
  private int mCellWidth;
  private int mDefaultHeight;
  private int mDefaultHorizontalGap;
  private int mDefaultVerticalGap;
  private int mDefaultWidth;
  private int mDisplayHeight;
  private int mDisplayWidth;
  private int[][] mGridNeighbors;
  private int mKeyHeight;
  private int mKeyWidth;
  private int mKeyboardMode;
  private List<Key> mKeys;
  private CharSequence mLabel;
  private List<Key> mModifierKeys;
  private int mProximityThreshold;
  private int[] mShiftKeyIndices = { -1, -1 };
  private Key[] mShiftKeys = { null, null };
  private boolean mShifted;
  private int mTotalHeight;
  private int mTotalWidth;
  private ArrayList<Row> rows = new ArrayList();
  
  public Keyboard(Context paramContext, int paramInt)
  {
    this(paramContext, paramInt, 0);
  }
  
  public Keyboard(Context paramContext, int paramInt1, int paramInt2)
  {
    DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
    mDisplayWidth = widthPixels;
    mDisplayHeight = heightPixels;
    mDefaultHorizontalGap = 0;
    mDefaultWidth = (mDisplayWidth / 10);
    mDefaultVerticalGap = 0;
    mDefaultHeight = mDefaultWidth;
    mKeys = new ArrayList();
    mModifierKeys = new ArrayList();
    mKeyboardMode = paramInt2;
    loadKeyboard(paramContext, paramContext.getResources().getXml(paramInt1));
  }
  
  public Keyboard(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mDisplayWidth = paramInt3;
    mDisplayHeight = paramInt4;
    mDefaultHorizontalGap = 0;
    mDefaultWidth = (mDisplayWidth / 10);
    mDefaultVerticalGap = 0;
    mDefaultHeight = mDefaultWidth;
    mKeys = new ArrayList();
    mModifierKeys = new ArrayList();
    mKeyboardMode = paramInt2;
    loadKeyboard(paramContext, paramContext.getResources().getXml(paramInt1));
  }
  
  public Keyboard(Context paramContext, int paramInt1, CharSequence paramCharSequence, int paramInt2, int paramInt3)
  {
    this(paramContext, paramInt1);
    int i = 0;
    int j = 0;
    mTotalWidth = 0;
    paramContext = new Row(this);
    defaultHeight = mDefaultHeight;
    defaultWidth = mDefaultWidth;
    defaultHorizontalGap = mDefaultHorizontalGap;
    verticalGap = mDefaultVerticalGap;
    rowEdgeFlags = 12;
    int k;
    if (paramInt2 == -1) {
      k = Integer.MAX_VALUE;
    } else {
      k = paramInt2;
    }
    paramInt1 = 0;
    int m = 0;
    paramInt2 = j;
    j = m;
    while (j < paramCharSequence.length())
    {
      char c = paramCharSequence.charAt(j);
      int n;
      if (paramInt2 < k)
      {
        m = i;
        n = paramInt2;
        paramInt2 = paramInt1;
        if (mDefaultWidth + paramInt1 + paramInt3 <= mDisplayWidth) {}
      }
      else
      {
        paramInt2 = 0;
        m = i + (mDefaultVerticalGap + mDefaultHeight);
        n = 0;
      }
      Key localKey = new Key(paramContext);
      x = paramInt2;
      y = m;
      label = String.valueOf(c);
      codes = new int[] { c };
      n++;
      paramInt1 = paramInt2 + (width + gap);
      mKeys.add(localKey);
      mKeys.add(localKey);
      if (paramInt1 > mTotalWidth) {
        mTotalWidth = paramInt1;
      }
      j++;
      i = m;
      paramInt2 = n;
    }
    mTotalHeight = (mDefaultHeight + i);
    rows.add(paramContext);
  }
  
  private void computeNearestNeighbors()
  {
    mCellWidth = ((getMinWidth() + 10 - 1) / 10);
    mCellHeight = ((getHeight() + 5 - 1) / 5);
    mGridNeighbors = new int[50][];
    int[] arrayOfInt = new int[mKeys.size()];
    int i = mCellWidth;
    int j = mCellHeight;
    int k = 0;
    while (k < i * 10)
    {
      int m = 0;
      while (m < 5 * j)
      {
        int n = 0;
        int i1 = 0;
        while (i1 < mKeys.size())
        {
          localObject = (Key)mKeys.get(i1);
          int i2;
          if ((((Key)localObject).squaredDistanceFrom(k, m) >= mProximityThreshold) && (((Key)localObject).squaredDistanceFrom(mCellWidth + k - 1, m) >= mProximityThreshold) && (((Key)localObject).squaredDistanceFrom(mCellWidth + k - 1, mCellHeight + m - 1) >= mProximityThreshold))
          {
            i2 = n;
            if (((Key)localObject).squaredDistanceFrom(k, mCellHeight + m - 1) >= mProximityThreshold) {}
          }
          else
          {
            arrayOfInt[n] = i1;
            i2 = n + 1;
          }
          i1++;
          n = i2;
        }
        Object localObject = new int[n];
        System.arraycopy(arrayOfInt, 0, localObject, 0, n);
        mGridNeighbors[(m / mCellHeight * 10 + k / mCellWidth)] = localObject;
        m += mCellHeight;
      }
      k += mCellWidth;
    }
  }
  
  static int getDimensionOrFraction(TypedArray paramTypedArray, int paramInt1, int paramInt2, int paramInt3)
  {
    TypedValue localTypedValue = paramTypedArray.peekValue(paramInt1);
    if (localTypedValue == null) {
      return paramInt3;
    }
    if (type == 5) {
      return paramTypedArray.getDimensionPixelOffset(paramInt1, paramInt3);
    }
    if (type == 6) {
      return Math.round(paramTypedArray.getFraction(paramInt1, paramInt2, paramInt2, paramInt3));
    }
    return paramInt3;
  }
  
  private void loadKeyboard(Context paramContext, XmlResourceParser paramXmlResourceParser)
  {
    Resources localResources = paramContext.getResources();
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    Key localKey = null;
    paramContext = null;
    int n = 0;
    int i1 = 0;
    for (;;)
    {
      int i2 = m;
      try
      {
        int i3 = paramXmlResourceParser.next();
        int i4 = 1;
        if (i3 != 1)
        {
          if (i3 == 2)
          {
            i2 = m;
            String str = paramXmlResourceParser.getName();
            i2 = m;
            boolean bool = "Row".equals(str);
            if (bool)
            {
              k = 1;
              i2 = 0;
              try
              {
                paramContext = createRowFromXml(localResources, paramXmlResourceParser);
                rows.add(paramContext);
                if (mode != 0)
                {
                  i1 = mode;
                  i = mKeyboardMode;
                  if (i1 != i)
                  {
                    i1 = i4;
                    break label143;
                  }
                }
                i1 = 0;
                label143:
                if (i1 != 0) {
                  try
                  {
                    skipToEndOfRow(paramXmlResourceParser);
                    k = 0;
                  }
                  catch (Exception paramContext)
                  {
                    break label584;
                  }
                }
                i = k;
                k = i2;
              }
              catch (Exception paramContext)
              {
                break label584;
              }
            }
            i2 = m;
            bool = "Key".equals(str);
            if (bool) {
              try
              {
                localKey = createKeyFromXml(localResources, paramContext, k, m, paramXmlResourceParser);
                try
                {
                  mKeys.add(localKey);
                  if (codes[0] == -1)
                  {
                    for (n = 0; n < mShiftKeys.length; n++) {
                      if (mShiftKeys[n] == null)
                      {
                        mShiftKeys[n] = localKey;
                        mShiftKeyIndices[n] = (mKeys.size() - 1);
                        break;
                      }
                    }
                    mModifierKeys.add(localKey);
                  }
                  else if (codes[0] == -6)
                  {
                    mModifierKeys.add(localKey);
                  }
                  mKeys.add(localKey);
                  n = 1;
                }
                catch (Exception paramContext) {}
              }
              catch (Exception paramContext) {}
            }
            i2 = m;
            if ("Keyboard".equals(str))
            {
              i2 = m;
              parseKeyboardAttributes(localResources, paramXmlResourceParser);
            }
            continue;
          }
          int i5 = n;
          int i6 = m;
          int i7 = i;
          i4 = j;
          i2 = k;
          if (i3 == 3) {
            if (n != 0)
            {
              i5 = 0;
              i2 = m;
              i4 = gap;
              i2 = m;
              n = width;
              i2 = k + (i4 + n);
              try
              {
                if (i2 > mTotalWidth) {
                  mTotalWidth = i2;
                }
                i6 = m;
                i7 = i;
                i4 = j;
              }
              catch (Exception paramContext)
              {
                break label584;
              }
            }
            else
            {
              i5 = n;
              i6 = m;
              i7 = i;
              i4 = j;
              i2 = k;
              if (i != 0)
              {
                i7 = 0;
                i2 = m;
                m += verticalGap;
                i2 = m;
                i = defaultHeight;
                i6 = m + i;
                i4 = j + 1;
                i2 = k;
                i5 = n;
              }
            }
          }
          n = i5;
          m = i6;
          i = i7;
          j = i4;
          k = i2;
        }
      }
      catch (Exception paramContext)
      {
        m = i2;
        label584:
        paramXmlResourceParser = new StringBuilder();
        paramXmlResourceParser.append("Parse error:");
        paramXmlResourceParser.append(paramContext);
        Log.e("Keyboard", paramXmlResourceParser.toString());
        paramContext.printStackTrace();
      }
    }
    mTotalHeight = (m - mDefaultVerticalGap);
  }
  
  private void parseKeyboardAttributes(Resources paramResources, XmlResourceParser paramXmlResourceParser)
  {
    paramResources = paramResources.obtainAttributes(Xml.asAttributeSet(paramXmlResourceParser), R.styleable.Keyboard);
    mDefaultWidth = getDimensionOrFraction(paramResources, 0, mDisplayWidth, mDisplayWidth / 10);
    mDefaultHeight = getDimensionOrFraction(paramResources, 1, mDisplayHeight, 50);
    mDefaultHorizontalGap = getDimensionOrFraction(paramResources, 2, mDisplayWidth, 0);
    mDefaultVerticalGap = getDimensionOrFraction(paramResources, 3, mDisplayHeight, 0);
    mProximityThreshold = ((int)(mDefaultWidth * SEARCH_DISTANCE));
    mProximityThreshold *= mProximityThreshold;
    paramResources.recycle();
  }
  
  private void skipToEndOfRow(XmlResourceParser paramXmlResourceParser)
    throws XmlPullParserException, IOException
  {
    int i;
    do
    {
      i = paramXmlResourceParser.next();
    } while ((i != 1) && ((i != 3) || (!paramXmlResourceParser.getName().equals("Row"))));
  }
  
  protected Key createKeyFromXml(Resources paramResources, Row paramRow, int paramInt1, int paramInt2, XmlResourceParser paramXmlResourceParser)
  {
    return new Key(paramResources, paramRow, paramInt1, paramInt2, paramXmlResourceParser);
  }
  
  protected Row createRowFromXml(Resources paramResources, XmlResourceParser paramXmlResourceParser)
  {
    return new Row(paramResources, this, paramXmlResourceParser);
  }
  
  public int getHeight()
  {
    return mTotalHeight;
  }
  
  protected int getHorizontalGap()
  {
    return mDefaultHorizontalGap;
  }
  
  protected int getKeyHeight()
  {
    return mDefaultHeight;
  }
  
  protected int getKeyWidth()
  {
    return mDefaultWidth;
  }
  
  public List<Key> getKeys()
  {
    return mKeys;
  }
  
  public int getMinWidth()
  {
    return mTotalWidth;
  }
  
  public List<Key> getModifierKeys()
  {
    return mModifierKeys;
  }
  
  public int[] getNearestKeys(int paramInt1, int paramInt2)
  {
    if (mGridNeighbors == null) {
      computeNearestNeighbors();
    }
    if ((paramInt1 >= 0) && (paramInt1 < getMinWidth()) && (paramInt2 >= 0) && (paramInt2 < getHeight()))
    {
      paramInt1 = paramInt2 / mCellHeight * 10 + paramInt1 / mCellWidth;
      if (paramInt1 < 50) {
        return mGridNeighbors[paramInt1];
      }
    }
    return new int[0];
  }
  
  public int getShiftKeyIndex()
  {
    return mShiftKeyIndices[0];
  }
  
  public int[] getShiftKeyIndices()
  {
    return mShiftKeyIndices;
  }
  
  protected int getVerticalGap()
  {
    return mDefaultVerticalGap;
  }
  
  public boolean isShifted()
  {
    return mShifted;
  }
  
  final void resize(int paramInt1, int paramInt2)
  {
    int i = rows.size();
    for (paramInt2 = 0; paramInt2 < i; paramInt2++)
    {
      Row localRow = (Row)rows.get(paramInt2);
      int j = mKeys.size();
      int k = 0;
      int m = 0;
      int n = 0;
      Key localKey;
      int i1;
      while (n < j)
      {
        localKey = (Key)mKeys.get(n);
        i1 = m;
        if (n > 0) {
          i1 = m + gap;
        }
        k += width;
        n++;
        m = i1;
      }
      if (m + k > paramInt1)
      {
        float f = (paramInt1 - m) / k;
        i1 = 0;
        for (k = 0; k < j; k++)
        {
          localKey = (Key)mKeys.get(k);
          width = ((int)(width * f));
          x = i1;
          i1 += width + gap;
        }
      }
    }
    mTotalWidth = paramInt1;
  }
  
  protected void setHorizontalGap(int paramInt)
  {
    mDefaultHorizontalGap = paramInt;
  }
  
  protected void setKeyHeight(int paramInt)
  {
    mDefaultHeight = paramInt;
  }
  
  protected void setKeyWidth(int paramInt)
  {
    mDefaultWidth = paramInt;
  }
  
  public boolean setShifted(boolean paramBoolean)
  {
    for (Key localKey : mShiftKeys) {
      if (localKey != null) {
        on = paramBoolean;
      }
    }
    if (mShifted != paramBoolean)
    {
      mShifted = paramBoolean;
      return true;
    }
    return false;
  }
  
  protected void setVerticalGap(int paramInt)
  {
    mDefaultVerticalGap = paramInt;
  }
  
  public static class Key
  {
    private static final int[] KEY_STATE_NORMAL = new int[0];
    private static final int[] KEY_STATE_NORMAL_OFF;
    private static final int[] KEY_STATE_NORMAL_ON = { 16842911, 16842912 };
    private static final int[] KEY_STATE_PRESSED = { 16842919 };
    private static final int[] KEY_STATE_PRESSED_OFF;
    private static final int[] KEY_STATE_PRESSED_ON = { 16842919, 16842911, 16842912 };
    public int[] codes;
    public int edgeFlags;
    public int gap;
    public int height;
    public Drawable icon;
    public Drawable iconPreview;
    private Keyboard keyboard;
    public CharSequence label;
    public boolean modifier;
    public boolean on;
    public CharSequence popupCharacters;
    public int popupResId;
    public boolean pressed;
    public boolean repeatable;
    public boolean sticky;
    public CharSequence text;
    public int width;
    public int x;
    public int y;
    
    static
    {
      KEY_STATE_NORMAL_OFF = new int[] { 16842911 };
      KEY_STATE_PRESSED_OFF = new int[] { 16842919, 16842911 };
    }
    
    public Key(Resources paramResources, Keyboard.Row paramRow, int paramInt1, int paramInt2, XmlResourceParser paramXmlResourceParser)
    {
      this(paramRow);
      x = paramInt1;
      y = paramInt2;
      TypedArray localTypedArray = paramResources.obtainAttributes(Xml.asAttributeSet(paramXmlResourceParser), R.styleable.Keyboard);
      width = Keyboard.getDimensionOrFraction(localTypedArray, 0, keyboard.mDisplayWidth, defaultWidth);
      height = Keyboard.getDimensionOrFraction(localTypedArray, 1, keyboard.mDisplayHeight, defaultHeight);
      gap = Keyboard.getDimensionOrFraction(localTypedArray, 2, keyboard.mDisplayWidth, defaultHorizontalGap);
      localTypedArray.recycle();
      paramResources = paramResources.obtainAttributes(Xml.asAttributeSet(paramXmlResourceParser), R.styleable.Keyboard_Key);
      x += gap;
      paramXmlResourceParser = new TypedValue();
      paramResources.getValue(0, paramXmlResourceParser);
      if ((type != 16) && (type != 17))
      {
        if (type == 3) {
          codes = parseCSV(string.toString());
        }
      }
      else {
        codes = new int[] { data };
      }
      iconPreview = paramResources.getDrawable(7);
      if (iconPreview != null) {
        iconPreview.setBounds(0, 0, iconPreview.getIntrinsicWidth(), iconPreview.getIntrinsicHeight());
      }
      popupCharacters = paramResources.getText(2);
      popupResId = paramResources.getResourceId(1, 0);
      repeatable = paramResources.getBoolean(6, false);
      modifier = paramResources.getBoolean(4, false);
      sticky = paramResources.getBoolean(5, false);
      edgeFlags = paramResources.getInt(3, 0);
      edgeFlags |= rowEdgeFlags;
      icon = paramResources.getDrawable(10);
      if (icon != null) {
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
      }
      label = paramResources.getText(9);
      text = paramResources.getText(8);
      if ((codes == null) && (!TextUtils.isEmpty(label))) {
        codes = new int[] { label.charAt(0) };
      }
      paramResources.recycle();
    }
    
    public Key(Keyboard.Row paramRow)
    {
      keyboard = Keyboard.Row.access$600(paramRow);
      height = defaultHeight;
      width = defaultWidth;
      gap = defaultHorizontalGap;
      edgeFlags = rowEdgeFlags;
    }
    
    public int[] getCurrentDrawableState()
    {
      int[] arrayOfInt = KEY_STATE_NORMAL;
      if (on)
      {
        if (pressed) {
          arrayOfInt = KEY_STATE_PRESSED_ON;
        } else {
          arrayOfInt = KEY_STATE_NORMAL_ON;
        }
      }
      else if (sticky)
      {
        if (pressed) {
          arrayOfInt = KEY_STATE_PRESSED_OFF;
        } else {
          arrayOfInt = KEY_STATE_NORMAL_OFF;
        }
      }
      else if (pressed) {
        arrayOfInt = KEY_STATE_PRESSED;
      }
      return arrayOfInt;
    }
    
    public boolean isInside(int paramInt1, int paramInt2)
    {
      int i;
      if ((edgeFlags & 0x1) > 0) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if ((edgeFlags & 0x2) > 0) {
        j = 1;
      } else {
        j = 0;
      }
      int k;
      if ((edgeFlags & 0x4) > 0) {
        k = 1;
      } else {
        k = 0;
      }
      int m;
      if ((edgeFlags & 0x8) > 0) {
        m = 1;
      } else {
        m = 0;
      }
      return ((paramInt1 >= x) || ((i != 0) && (paramInt1 <= x + width))) && ((paramInt1 < x + width) || ((j != 0) && (paramInt1 >= x))) && ((paramInt2 >= y) || ((k != 0) && (paramInt2 <= y + height))) && ((paramInt2 < y + height) || ((m != 0) && (paramInt2 >= y)));
    }
    
    public void onPressed()
    {
      pressed ^= true;
    }
    
    public void onReleased(boolean paramBoolean)
    {
      pressed ^= true;
      if ((sticky) && (paramBoolean)) {
        on ^= true;
      }
    }
    
    int[] parseCSV(String paramString)
    {
      int i = 0;
      int j = 0;
      if (paramString.length() > 0) {
        for (k = 0 + 1;; k++)
        {
          int m = paramString.indexOf(",", j + 1);
          j = m;
          i = k;
          if (m <= 0) {
            break;
          }
        }
      }
      int[] arrayOfInt = new int[i];
      int k = 0;
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
      while (localStringTokenizer.hasMoreTokens())
      {
        try
        {
          arrayOfInt[k] = Integer.parseInt(localStringTokenizer.nextToken());
        }
        catch (NumberFormatException localNumberFormatException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Error parsing keycodes ");
          localStringBuilder.append(paramString);
          Log.e("Keyboard", localStringBuilder.toString());
        }
        k++;
      }
      return arrayOfInt;
    }
    
    public int squaredDistanceFrom(int paramInt1, int paramInt2)
    {
      paramInt1 = x + width / 2 - paramInt1;
      paramInt2 = y + height / 2 - paramInt2;
      return paramInt1 * paramInt1 + paramInt2 * paramInt2;
    }
  }
  
  public static class Row
  {
    public int defaultHeight;
    public int defaultHorizontalGap;
    public int defaultWidth;
    ArrayList<Keyboard.Key> mKeys = new ArrayList();
    public int mode;
    private Keyboard parent;
    public int rowEdgeFlags;
    public int verticalGap;
    
    public Row(Resources paramResources, Keyboard paramKeyboard, XmlResourceParser paramXmlResourceParser)
    {
      parent = paramKeyboard;
      TypedArray localTypedArray = paramResources.obtainAttributes(Xml.asAttributeSet(paramXmlResourceParser), R.styleable.Keyboard);
      defaultWidth = Keyboard.getDimensionOrFraction(localTypedArray, 0, mDisplayWidth, mDefaultWidth);
      defaultHeight = Keyboard.getDimensionOrFraction(localTypedArray, 1, mDisplayHeight, mDefaultHeight);
      defaultHorizontalGap = Keyboard.getDimensionOrFraction(localTypedArray, 2, mDisplayWidth, mDefaultHorizontalGap);
      verticalGap = Keyboard.getDimensionOrFraction(localTypedArray, 3, mDisplayHeight, mDefaultVerticalGap);
      localTypedArray.recycle();
      paramResources = paramResources.obtainAttributes(Xml.asAttributeSet(paramXmlResourceParser), R.styleable.Keyboard_Row);
      rowEdgeFlags = paramResources.getInt(0, 0);
      mode = paramResources.getResourceId(1, 0);
    }
    
    public Row(Keyboard paramKeyboard)
    {
      parent = paramKeyboard;
    }
  }
}
