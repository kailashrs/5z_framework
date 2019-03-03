package android.media;

import android.graphics.Rect;
import android.os.Parcel;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public final class TimedText
{
  private static final int FIRST_PRIVATE_KEY = 101;
  private static final int FIRST_PUBLIC_KEY = 1;
  private static final int KEY_BACKGROUND_COLOR_RGBA = 3;
  private static final int KEY_DISPLAY_FLAGS = 1;
  private static final int KEY_END_CHAR = 104;
  private static final int KEY_FONT_ID = 105;
  private static final int KEY_FONT_SIZE = 106;
  private static final int KEY_GLOBAL_SETTING = 101;
  private static final int KEY_HIGHLIGHT_COLOR_RGBA = 4;
  private static final int KEY_LOCAL_SETTING = 102;
  private static final int KEY_SCROLL_DELAY = 5;
  private static final int KEY_START_CHAR = 103;
  private static final int KEY_START_TIME = 7;
  private static final int KEY_STRUCT_BLINKING_TEXT_LIST = 8;
  private static final int KEY_STRUCT_FONT_LIST = 9;
  private static final int KEY_STRUCT_HIGHLIGHT_LIST = 10;
  private static final int KEY_STRUCT_HYPER_TEXT_LIST = 11;
  private static final int KEY_STRUCT_JUSTIFICATION = 15;
  private static final int KEY_STRUCT_KARAOKE_LIST = 12;
  private static final int KEY_STRUCT_STYLE_LIST = 13;
  private static final int KEY_STRUCT_TEXT = 16;
  private static final int KEY_STRUCT_TEXT_POS = 14;
  private static final int KEY_STYLE_FLAGS = 2;
  private static final int KEY_TEXT_COLOR_RGBA = 107;
  private static final int KEY_WRAP_TEXT = 6;
  private static final int LAST_PRIVATE_KEY = 107;
  private static final int LAST_PUBLIC_KEY = 16;
  private static final String TAG = "TimedText";
  private int mBackgroundColorRGBA = -1;
  private List<CharPos> mBlinkingPosList = null;
  private int mDisplayFlags = -1;
  private List<Font> mFontList = null;
  private int mHighlightColorRGBA = -1;
  private List<CharPos> mHighlightPosList = null;
  private List<HyperText> mHyperTextList = null;
  private Justification mJustification;
  private List<Karaoke> mKaraokeList = null;
  private final HashMap<Integer, Object> mKeyObjectMap = new HashMap();
  private int mScrollDelay = -1;
  private List<Style> mStyleList = null;
  private Rect mTextBounds = null;
  private String mTextChars = null;
  private int mWrapText = -1;
  
  public TimedText(Parcel paramParcel)
  {
    if (parseParcel(paramParcel)) {
      return;
    }
    mKeyObjectMap.clear();
    throw new IllegalArgumentException("parseParcel() fails");
  }
  
  private boolean containsKey(int paramInt)
  {
    return (isValidKey(paramInt)) && (mKeyObjectMap.containsKey(Integer.valueOf(paramInt)));
  }
  
  private Object getObject(int paramInt)
  {
    if (containsKey(paramInt)) {
      return mKeyObjectMap.get(Integer.valueOf(paramInt));
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid key: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private boolean isValidKey(int paramInt)
  {
    return ((paramInt >= 1) && (paramInt <= 16)) || ((paramInt >= 101) && (paramInt <= 107));
  }
  
  private Set keySet()
  {
    return mKeyObjectMap.keySet();
  }
  
  private boolean parseParcel(Parcel paramParcel)
  {
    paramParcel.setDataPosition(0);
    if (paramParcel.dataAvail() == 0) {
      return false;
    }
    int i = paramParcel.readInt();
    int j;
    Object localObject;
    if (i == 102)
    {
      i = paramParcel.readInt();
      if (i != 7) {
        return false;
      }
      j = paramParcel.readInt();
      mKeyObjectMap.put(Integer.valueOf(i), Integer.valueOf(j));
      if (paramParcel.readInt() != 16) {
        return false;
      }
      paramParcel.readInt();
      localObject = paramParcel.createByteArray();
      if ((localObject != null) && (localObject.length != 0)) {
        mTextChars = new String((byte[])localObject);
      } else {
        mTextChars = null;
      }
    }
    else if (i != 101)
    {
      paramParcel = new StringBuilder();
      paramParcel.append("Invalid timed text key found: ");
      paramParcel.append(i);
      Log.w("TimedText", paramParcel.toString());
      return false;
    }
    while (paramParcel.dataAvail() > 0)
    {
      int k = paramParcel.readInt();
      if (!isValidKey(k))
      {
        paramParcel = new StringBuilder();
        paramParcel.append("Invalid timed text key found: ");
        paramParcel.append(k);
        Log.w("TimedText", paramParcel.toString());
        return false;
      }
      localObject = null;
      switch (k)
      {
      case 2: 
      case 7: 
      default: 
        break;
      case 15: 
        mJustification = new Justification(paramParcel.readInt(), paramParcel.readInt());
        localObject = mJustification;
        break;
      case 14: 
        i = paramParcel.readInt();
        int m = paramParcel.readInt();
        j = paramParcel.readInt();
        mTextBounds = new Rect(m, i, paramParcel.readInt(), j);
        break;
      case 13: 
        readStyle(paramParcel);
        localObject = mStyleList;
        break;
      case 12: 
        readKaraoke(paramParcel);
        localObject = mKaraokeList;
        break;
      case 11: 
        readHyperText(paramParcel);
        localObject = mHyperTextList;
        break;
      case 10: 
        readHighlight(paramParcel);
        localObject = mHighlightPosList;
        break;
      case 9: 
        readFont(paramParcel);
        localObject = mFontList;
        break;
      case 8: 
        readBlinkingText(paramParcel);
        localObject = mBlinkingPosList;
        break;
      case 6: 
        mWrapText = paramParcel.readInt();
        localObject = Integer.valueOf(mWrapText);
        break;
      case 5: 
        mScrollDelay = paramParcel.readInt();
        localObject = Integer.valueOf(mScrollDelay);
        break;
      case 4: 
        mHighlightColorRGBA = paramParcel.readInt();
        localObject = Integer.valueOf(mHighlightColorRGBA);
        break;
      case 3: 
        mBackgroundColorRGBA = paramParcel.readInt();
        localObject = Integer.valueOf(mBackgroundColorRGBA);
        break;
      case 1: 
        mDisplayFlags = paramParcel.readInt();
        localObject = Integer.valueOf(mDisplayFlags);
      }
      if (localObject != null)
      {
        if (mKeyObjectMap.containsKey(Integer.valueOf(k))) {
          mKeyObjectMap.remove(Integer.valueOf(k));
        }
        mKeyObjectMap.put(Integer.valueOf(k), localObject);
      }
    }
    return true;
  }
  
  private void readBlinkingText(Parcel paramParcel)
  {
    paramParcel = new CharPos(paramParcel.readInt(), paramParcel.readInt());
    if (mBlinkingPosList == null) {
      mBlinkingPosList = new ArrayList();
    }
    mBlinkingPosList.add(paramParcel);
  }
  
  private void readFont(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      int k = paramParcel.readInt();
      int m = paramParcel.readInt();
      Font localFont = new Font(k, new String(paramParcel.createByteArray(), 0, m));
      if (mFontList == null) {
        mFontList = new ArrayList();
      }
      mFontList.add(localFont);
    }
  }
  
  private void readHighlight(Parcel paramParcel)
  {
    paramParcel = new CharPos(paramParcel.readInt(), paramParcel.readInt());
    if (mHighlightPosList == null) {
      mHighlightPosList = new ArrayList();
    }
    mHighlightPosList.add(paramParcel);
  }
  
  private void readHyperText(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    int j = paramParcel.readInt();
    int k = paramParcel.readInt();
    String str = new String(paramParcel.createByteArray(), 0, k);
    k = paramParcel.readInt();
    paramParcel = new HyperText(i, j, str, new String(paramParcel.createByteArray(), 0, k));
    if (mHyperTextList == null) {
      mHyperTextList = new ArrayList();
    }
    mHyperTextList.add(paramParcel);
  }
  
  private void readKaraoke(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      Karaoke localKaraoke = new Karaoke(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
      if (mKaraokeList == null) {
        mKaraokeList = new ArrayList();
      }
      mKaraokeList.add(localKaraoke);
    }
  }
  
  private void readStyle(Parcel paramParcel)
  {
    int i = 0;
    int j = -1;
    int k = -1;
    int m = -1;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    int n = -1;
    int i1 = -1;
    while ((i == 0) && (paramParcel.dataAvail() > 0))
    {
      int i2 = paramParcel.readInt();
      boolean bool4;
      if (i2 != 2)
      {
        switch (i2)
        {
        default: 
          paramParcel.setDataPosition(paramParcel.dataPosition() - 4);
          i = 1;
          bool4 = bool2;
          break;
        case 107: 
          i1 = paramParcel.readInt();
          bool4 = bool2;
          break;
        case 106: 
          n = paramParcel.readInt();
          bool4 = bool2;
          break;
        case 105: 
          m = paramParcel.readInt();
          bool4 = bool2;
          break;
        case 104: 
          k = paramParcel.readInt();
          bool4 = bool2;
          break;
        case 103: 
          j = paramParcel.readInt();
          bool4 = bool2;
          break;
        }
      }
      else
      {
        i2 = paramParcel.readInt();
        bool1 = false;
        if (i2 % 2 == 1) {
          bool3 = true;
        } else {
          bool3 = false;
        }
        bool2 = bool3;
        if (i2 % 4 >= 2) {
          bool3 = true;
        } else {
          bool3 = false;
        }
        bool4 = bool3;
        bool3 = bool1;
        if (i2 / 4 == 1) {
          bool3 = true;
        }
        bool1 = bool2;
      }
      bool2 = bool4;
    }
    paramParcel = new Style(j, k, m, bool1, bool2, bool3, n, i1);
    if (mStyleList == null) {
      mStyleList = new ArrayList();
    }
    mStyleList.add(paramParcel);
  }
  
  public Rect getBounds()
  {
    return mTextBounds;
  }
  
  public String getText()
  {
    return mTextChars;
  }
  
  public static final class CharPos
  {
    public final int endChar;
    public final int startChar;
    
    public CharPos(int paramInt1, int paramInt2)
    {
      startChar = paramInt1;
      endChar = paramInt2;
    }
  }
  
  public static final class Font
  {
    public final int ID;
    public final String name;
    
    public Font(int paramInt, String paramString)
    {
      ID = paramInt;
      name = paramString;
    }
  }
  
  public static final class HyperText
  {
    public final String URL;
    public final String altString;
    public final int endChar;
    public final int startChar;
    
    public HyperText(int paramInt1, int paramInt2, String paramString1, String paramString2)
    {
      startChar = paramInt1;
      endChar = paramInt2;
      URL = paramString1;
      altString = paramString2;
    }
  }
  
  public static final class Justification
  {
    public final int horizontalJustification;
    public final int verticalJustification;
    
    public Justification(int paramInt1, int paramInt2)
    {
      horizontalJustification = paramInt1;
      verticalJustification = paramInt2;
    }
  }
  
  public static final class Karaoke
  {
    public final int endChar;
    public final int endTimeMs;
    public final int startChar;
    public final int startTimeMs;
    
    public Karaoke(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      startTimeMs = paramInt1;
      endTimeMs = paramInt2;
      startChar = paramInt3;
      endChar = paramInt4;
    }
  }
  
  public static final class Style
  {
    public final int colorRGBA;
    public final int endChar;
    public final int fontID;
    public final int fontSize;
    public final boolean isBold;
    public final boolean isItalic;
    public final boolean isUnderlined;
    public final int startChar;
    
    public Style(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt4, int paramInt5)
    {
      startChar = paramInt1;
      endChar = paramInt2;
      fontID = paramInt3;
      isBold = paramBoolean1;
      isItalic = paramBoolean2;
      isUnderlined = paramBoolean3;
      fontSize = paramInt4;
      colorRGBA = paramInt5;
    }
  }
}
