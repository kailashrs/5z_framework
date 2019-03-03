package android.media;

import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.accessibility.CaptioningManager.CaptionStyle;
import java.util.ArrayList;
import java.util.Arrays;

class Cea608CCParser
{
  private static final int AOF = 34;
  private static final int AON = 35;
  private static final int BS = 33;
  private static final int CR = 45;
  private static final boolean DEBUG = Log.isLoggable("Cea608CCParser", 3);
  private static final int DER = 36;
  private static final int EDM = 44;
  private static final int ENM = 46;
  private static final int EOC = 47;
  private static final int FON = 40;
  private static final int INVALID = -1;
  public static final int MAX_COLS = 32;
  public static final int MAX_ROWS = 15;
  private static final int MODE_PAINT_ON = 1;
  private static final int MODE_POP_ON = 3;
  private static final int MODE_ROLL_UP = 2;
  private static final int MODE_TEXT = 4;
  private static final int MODE_UNKNOWN = 0;
  private static final int RCL = 32;
  private static final int RDC = 41;
  private static final int RTD = 43;
  private static final int RU2 = 37;
  private static final int RU3 = 38;
  private static final int RU4 = 39;
  private static final String TAG = "Cea608CCParser";
  private static final int TR = 42;
  private static final char TS = ' ';
  private CCMemory mDisplay = new CCMemory();
  private final DisplayListener mListener;
  private int mMode = 1;
  private CCMemory mNonDisplay = new CCMemory();
  private int mPrevCtrlCode = -1;
  private int mRollUpSize = 4;
  private CCMemory mTextMem = new CCMemory();
  
  Cea608CCParser(DisplayListener paramDisplayListener)
  {
    mListener = paramDisplayListener;
  }
  
  private CCMemory getMemory()
  {
    switch (mMode)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unrecoginized mode: ");
      localStringBuilder.append(mMode);
      Log.w("Cea608CCParser", localStringBuilder.toString());
      return mDisplay;
    case 4: 
      return mTextMem;
    case 3: 
      return mNonDisplay;
    }
    return mDisplay;
  }
  
  private boolean handleCtrlCode(CCData paramCCData)
  {
    int i = paramCCData.getCtrlCode();
    if ((mPrevCtrlCode != -1) && (mPrevCtrlCode == i))
    {
      mPrevCtrlCode = -1;
      return true;
    }
    switch (i)
    {
    case 34: 
    case 35: 
    default: 
      mPrevCtrlCode = -1;
      return false;
    case 47: 
      swapMemory();
      mMode = 3;
      updateDisplay();
      break;
    case 46: 
      mNonDisplay.erase();
      break;
    case 45: 
      if (mMode == 2) {
        getMemory().rollUp(mRollUpSize);
      } else {
        getMemory().cr();
      }
      if (mMode == 2) {
        updateDisplay();
      }
      break;
    case 44: 
      mDisplay.erase();
      updateDisplay();
      break;
    case 43: 
      mMode = 4;
      break;
    case 42: 
      mMode = 4;
      mTextMem.erase();
      break;
    case 41: 
      mMode = 1;
      break;
    case 40: 
      Log.i("Cea608CCParser", "Flash On");
      break;
    case 37: 
    case 38: 
    case 39: 
      mRollUpSize = (i - 35);
      if (mMode != 2)
      {
        mDisplay.erase();
        mNonDisplay.erase();
      }
      mMode = 2;
      break;
    case 36: 
      getMemory().der();
      break;
    case 33: 
      getMemory().bs();
      break;
    case 32: 
      mMode = 3;
    }
    mPrevCtrlCode = i;
    return true;
  }
  
  private boolean handleDisplayableChars(CCData paramCCData)
  {
    if (!paramCCData.isDisplayableChar()) {
      return false;
    }
    if (paramCCData.isExtendedChar()) {
      getMemory().bs();
    }
    getMemory().writeText(paramCCData.getDisplayText());
    if ((mMode == 1) || (mMode == 2)) {
      updateDisplay();
    }
    return true;
  }
  
  private boolean handleMidRowCode(CCData paramCCData)
  {
    paramCCData = paramCCData.getMidRow();
    if (paramCCData != null)
    {
      getMemory().writeMidRowCode(paramCCData);
      return true;
    }
    return false;
  }
  
  private boolean handlePACCode(CCData paramCCData)
  {
    paramCCData = paramCCData.getPAC();
    if (paramCCData != null)
    {
      if (mMode == 2) {
        getMemory().moveBaselineTo(paramCCData.getRow(), mRollUpSize);
      }
      getMemory().writePAC(paramCCData);
      return true;
    }
    return false;
  }
  
  private boolean handleTabOffsets(CCData paramCCData)
  {
    int i = paramCCData.getTabOffset();
    if (i > 0)
    {
      getMemory().tab(i);
      return true;
    }
    return false;
  }
  
  private void swapMemory()
  {
    CCMemory localCCMemory = mDisplay;
    mDisplay = mNonDisplay;
    mNonDisplay = localCCMemory;
  }
  
  private void updateDisplay()
  {
    if (mListener != null)
    {
      CaptioningManager.CaptionStyle localCaptionStyle = mListener.getCaptionStyle();
      mListener.onDisplayChanged(mDisplay.getStyledText(localCaptionStyle));
    }
  }
  
  public void parse(byte[] paramArrayOfByte)
  {
    paramArrayOfByte = CCData.fromByteArray(paramArrayOfByte);
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      if (DEBUG) {
        Log.d("Cea608CCParser", paramArrayOfByte[i].toString());
      }
      if ((!handleCtrlCode(paramArrayOfByte[i])) && (!handleTabOffsets(paramArrayOfByte[i])) && (!handlePACCode(paramArrayOfByte[i])) && (!handleMidRowCode(paramArrayOfByte[i]))) {
        handleDisplayableChars(paramArrayOfByte[i]);
      }
    }
  }
  
  private static class CCData
  {
    private static final String[] mCtrlCodeMap = { "RCL", "BS", "AOF", "AON", "DER", "RU2", "RU3", "RU4", "FON", "RDC", "TR", "RTD", "EDM", "CR", "ENM", "EOC" };
    private static final String[] mProtugueseCharMap = { "Ã", "ã", "Í", "Ì", "ì", "Ò", "ò", "Õ", "õ", "{", "}", "\\", "^", "_", "|", "~", "Ä", "ä", "Ö", "ö", "ß", "¥", "¤", "│", "Å", "å", "Ø", "ø", "┌", "┐", "└", "┘" };
    private static final String[] mSpanishCharMap;
    private static final String[] mSpecialCharMap = { "®", "°", "½", "¿", "™", "¢", "£", "♪", "à", " ", "è", "â", "ê", "î", "ô", "û" };
    private final byte mData1;
    private final byte mData2;
    private final byte mType;
    
    static
    {
      mSpanishCharMap = new String[] { "Á", "É", "Ó", "Ú", "Ü", "ü", "‘", "¡", "*", "'", "—", "©", "℠", "•", "“", "”", "À", "Â", "Ç", "È", "Ê", "Ë", "ë", "Î", "Ï", "ï", "Ô", "Ù", "ù", "Û", "«", "»" };
    }
    
    CCData(byte paramByte1, byte paramByte2, byte paramByte3)
    {
      mType = ((byte)paramByte1);
      mData1 = ((byte)paramByte2);
      mData2 = ((byte)paramByte3);
    }
    
    private String ctrlCodeToString(int paramInt)
    {
      return mCtrlCodeMap[(paramInt - 32)];
    }
    
    static CCData[] fromByteArray(byte[] paramArrayOfByte)
    {
      CCData[] arrayOfCCData = new CCData[paramArrayOfByte.length / 3];
      for (int i = 0; i < arrayOfCCData.length; i++) {
        arrayOfCCData[i] = new CCData(paramArrayOfByte[(i * 3)], paramArrayOfByte[(i * 3 + 1)], paramArrayOfByte[(i * 3 + 2)]);
      }
      return arrayOfCCData;
    }
    
    private char getBasicChar(byte paramByte)
    {
      int i;
      if (paramByte != 42)
      {
        if (paramByte != 92)
        {
          switch (paramByte)
          {
          default: 
            switch (paramByte)
            {
            default: 
              paramByte = (char)paramByte;
              i = paramByte;
              break;
            case 127: 
              paramByte = 9608;
              i = paramByte;
              break;
            case 126: 
              paramByte = 241;
              i = paramByte;
              break;
            case 125: 
              paramByte = 209;
              i = paramByte;
              break;
            case 124: 
              paramByte = 247;
              i = paramByte;
              break;
            case 123: 
              paramByte = 231;
              i = paramByte;
            }
            break;
          case 96: 
            paramByte = 250;
            i = paramByte;
            break;
          case 95: 
            paramByte = 243;
            i = paramByte;
            break;
          case 94: 
            paramByte = 237;
            i = paramByte;
            break;
          }
        }
        else
        {
          paramByte = 233;
          i = paramByte;
        }
      }
      else
      {
        paramByte = 225;
        i = paramByte;
      }
      return i;
    }
    
    private String getBasicChars()
    {
      if ((mData1 >= 32) && (mData1 <= Byte.MAX_VALUE))
      {
        StringBuilder localStringBuilder = new StringBuilder(2);
        localStringBuilder.append(getBasicChar(mData1));
        if ((mData2 >= 32) && (mData2 <= Byte.MAX_VALUE)) {
          localStringBuilder.append(getBasicChar(mData2));
        }
        return localStringBuilder.toString();
      }
      return null;
    }
    
    private String getExtendedChar()
    {
      if (((mData1 == 18) || (mData1 == 26)) && (mData2 >= 32) && (mData2 <= 63)) {
        return mSpanishCharMap[(mData2 - 32)];
      }
      if (((mData1 == 19) || (mData1 == 27)) && (mData2 >= 32) && (mData2 <= 63)) {
        return mProtugueseCharMap[(mData2 - 32)];
      }
      return null;
    }
    
    private String getSpecialChar()
    {
      if (((mData1 == 17) || (mData1 == 25)) && (mData2 >= 48) && (mData2 <= 63)) {
        return mSpecialCharMap[(mData2 - 48)];
      }
      return null;
    }
    
    private boolean isBasicChar()
    {
      boolean bool;
      if ((mData1 >= 32) && (mData1 <= Byte.MAX_VALUE)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isExtendedChar()
    {
      boolean bool;
      if (((mData1 == 18) || (mData1 == 26) || (mData1 == 19) || (mData1 == 27)) && (mData2 >= 32) && (mData2 <= 63)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isSpecialChar()
    {
      boolean bool;
      if (((mData1 == 17) || (mData1 == 25)) && (mData2 >= 48) && (mData2 <= 63)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    int getCtrlCode()
    {
      if (((mData1 == 20) || (mData1 == 28)) && (mData2 >= 32) && (mData2 <= 47)) {
        return mData2;
      }
      return -1;
    }
    
    String getDisplayText()
    {
      String str1 = getBasicChars();
      String str2 = str1;
      if (str1 == null)
      {
        str1 = getSpecialChar();
        str2 = str1;
        if (str1 == null) {
          str2 = getExtendedChar();
        }
      }
      return str2;
    }
    
    Cea608CCParser.StyleCode getMidRow()
    {
      if (((mData1 == 17) || (mData1 == 25)) && (mData2 >= 32) && (mData2 <= 47)) {
        return Cea608CCParser.StyleCode.fromByte(mData2);
      }
      return null;
    }
    
    Cea608CCParser.PAC getPAC()
    {
      if (((mData1 & 0x70) == 16) && ((mData2 & 0x40) == 64) && (((mData1 & 0x7) != 0) || ((mData2 & 0x20) == 0))) {
        return Cea608CCParser.PAC.fromBytes(mData1, mData2);
      }
      return null;
    }
    
    int getTabOffset()
    {
      if (((mData1 == 23) || (mData1 == 31)) && (mData2 >= 33) && (mData2 <= 35)) {
        return mData2 & 0x3;
      }
      return 0;
    }
    
    boolean isDisplayableChar()
    {
      boolean bool;
      if ((!isBasicChar()) && (!isSpecialChar()) && (!isExtendedChar())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public String toString()
    {
      if ((mData1 < 16) && (mData2 < 16)) {
        return String.format("[%d]Null: %02x %02x", new Object[] { Byte.valueOf(mType), Byte.valueOf(mData1), Byte.valueOf(mData2) });
      }
      int i = getCtrlCode();
      if (i != -1) {
        return String.format("[%d]%s", new Object[] { Byte.valueOf(mType), ctrlCodeToString(i) });
      }
      i = getTabOffset();
      if (i > 0) {
        return String.format("[%d]Tab%d", new Object[] { Byte.valueOf(mType), Integer.valueOf(i) });
      }
      Object localObject = getPAC();
      if (localObject != null) {
        return String.format("[%d]PAC: %s", new Object[] { Byte.valueOf(mType), ((Cea608CCParser.PAC)localObject).toString() });
      }
      localObject = getMidRow();
      if (localObject != null) {
        return String.format("[%d]Mid-row: %s", new Object[] { Byte.valueOf(mType), ((Cea608CCParser.StyleCode)localObject).toString() });
      }
      if (isDisplayableChar()) {
        return String.format("[%d]Displayable: %s (%02x %02x)", new Object[] { Byte.valueOf(mType), getDisplayText(), Byte.valueOf(mData1), Byte.valueOf(mData2) });
      }
      return String.format("[%d]Invalid: %02x %02x", new Object[] { Byte.valueOf(mType), Byte.valueOf(mData1), Byte.valueOf(mData2) });
    }
  }
  
  private static class CCLineBuilder
  {
    private final StringBuilder mDisplayChars;
    private final Cea608CCParser.StyleCode[] mMidRowStyles;
    private final Cea608CCParser.StyleCode[] mPACStyles;
    
    CCLineBuilder(String paramString)
    {
      mDisplayChars = new StringBuilder(paramString);
      mMidRowStyles = new Cea608CCParser.StyleCode[mDisplayChars.length()];
      mPACStyles = new Cea608CCParser.StyleCode[mDisplayChars.length()];
    }
    
    void applyStyleSpan(SpannableStringBuilder paramSpannableStringBuilder, Cea608CCParser.StyleCode paramStyleCode, int paramInt1, int paramInt2)
    {
      if (paramStyleCode.isItalics()) {
        paramSpannableStringBuilder.setSpan(new StyleSpan(2), paramInt1, paramInt2, 33);
      }
      if (paramStyleCode.isUnderline()) {
        paramSpannableStringBuilder.setSpan(new UnderlineSpan(), paramInt1, paramInt2, 33);
      }
    }
    
    char charAt(int paramInt)
    {
      return mDisplayChars.charAt(paramInt);
    }
    
    SpannableStringBuilder getStyledText(CaptioningManager.CaptionStyle paramCaptionStyle)
    {
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(mDisplayChars);
      int i = -1;
      int j = 0;
      int k = -1;
      Object localObject1 = null;
      while (j < mDisplayChars.length())
      {
        Object localObject2 = null;
        Object localObject3;
        if (mMidRowStyles[j] != null)
        {
          localObject3 = mMidRowStyles[j];
        }
        else
        {
          localObject3 = localObject2;
          if (mPACStyles[j] != null) {
            if (k >= 0)
            {
              localObject3 = localObject2;
              if (i >= 0) {}
            }
            else
            {
              localObject3 = mPACStyles[j];
            }
          }
        }
        int m = k;
        if (localObject3 != null)
        {
          localObject1 = localObject3;
          if ((k >= 0) && (i >= 0)) {
            applyStyleSpan(localSpannableStringBuilder, (Cea608CCParser.StyleCode)localObject3, k, j);
          }
          m = j;
        }
        if (mDisplayChars.charAt(j) != ' ')
        {
          k = i;
          if (i < 0) {
            k = j;
          }
        }
        else
        {
          k = i;
          if (i >= 0)
          {
            if (mDisplayChars.charAt(i) != ' ') {
              i--;
            }
            if (mDisplayChars.charAt(j - 1) == ' ') {
              k = j;
            } else {
              k = j + 1;
            }
            localSpannableStringBuilder.setSpan(new Cea608CCParser.MutableBackgroundColorSpan(backgroundColor), i, k, 33);
            if (m >= 0) {
              applyStyleSpan(localSpannableStringBuilder, localObject1, m, k);
            }
            k = -1;
          }
        }
        j++;
        i = k;
        k = m;
      }
      return localSpannableStringBuilder;
    }
    
    int length()
    {
      return mDisplayChars.length();
    }
    
    void setCharAt(int paramInt, char paramChar)
    {
      mDisplayChars.setCharAt(paramInt, paramChar);
      mMidRowStyles[paramInt] = null;
    }
    
    void setMidRowAt(int paramInt, Cea608CCParser.StyleCode paramStyleCode)
    {
      mDisplayChars.setCharAt(paramInt, ' ');
      mMidRowStyles[paramInt] = paramStyleCode;
    }
    
    void setPACAt(int paramInt, Cea608CCParser.PAC paramPAC)
    {
      mPACStyles[paramInt] = paramPAC;
    }
  }
  
  private static class CCMemory
  {
    private final String mBlankLine;
    private int mCol;
    private final Cea608CCParser.CCLineBuilder[] mLines = new Cea608CCParser.CCLineBuilder[17];
    private int mRow;
    
    CCMemory()
    {
      char[] arrayOfChar = new char[34];
      Arrays.fill(arrayOfChar, ' ');
      mBlankLine = new String(arrayOfChar);
    }
    
    private static int clamp(int paramInt1, int paramInt2, int paramInt3)
    {
      if (paramInt1 < paramInt2) {
        paramInt1 = paramInt2;
      } else if (paramInt1 > paramInt3) {
        paramInt1 = paramInt3;
      }
      return paramInt1;
    }
    
    private Cea608CCParser.CCLineBuilder getLineBuffer(int paramInt)
    {
      if (mLines[paramInt] == null) {
        mLines[paramInt] = new Cea608CCParser.CCLineBuilder(mBlankLine);
      }
      return mLines[paramInt];
    }
    
    private void moveBaselineTo(int paramInt1, int paramInt2)
    {
      if (mRow == paramInt1) {
        return;
      }
      int i = paramInt2;
      int j = i;
      if (paramInt1 < i) {
        j = paramInt1;
      }
      i = j;
      if (mRow < j) {
        i = mRow;
      }
      if (paramInt1 < mRow) {
        for (j = i - 1; j >= 0; j--) {
          mLines[(paramInt1 - j)] = mLines[(mRow - j)];
        }
      }
      for (j = 0; j < i; j++) {
        mLines[(paramInt1 - j)] = mLines[(mRow - j)];
      }
      for (j = 0; j <= paramInt1 - paramInt2; j++) {
        mLines[j] = null;
      }
      paramInt1++;
      while (paramInt1 < mLines.length)
      {
        mLines[paramInt1] = null;
        paramInt1++;
      }
    }
    
    private void moveCursorByCol(int paramInt)
    {
      mCol = clamp(mCol + paramInt, 1, 32);
    }
    
    private void moveCursorTo(int paramInt1, int paramInt2)
    {
      mRow = clamp(paramInt1, 1, 15);
      mCol = clamp(paramInt2, 1, 32);
    }
    
    private void moveCursorToRow(int paramInt)
    {
      mRow = clamp(paramInt, 1, 15);
    }
    
    void bs()
    {
      moveCursorByCol(-1);
      if (mLines[mRow] != null)
      {
        mLines[mRow].setCharAt(mCol, ' ');
        if (mCol == 31) {
          mLines[mRow].setCharAt(32, ' ');
        }
      }
    }
    
    void cr()
    {
      moveCursorTo(mRow + 1, 1);
    }
    
    void der()
    {
      if (mLines[mRow] != null)
      {
        for (int i = 0; i < mCol; i++) {
          if (mLines[mRow].charAt(i) != ' ')
          {
            for (i = mCol; i < mLines[mRow].length(); i++) {
              mLines[i].setCharAt(i, ' ');
            }
            return;
          }
        }
        mLines[mRow] = null;
      }
    }
    
    void erase()
    {
      for (int i = 0; i < mLines.length; i++) {
        mLines[i] = null;
      }
      mRow = 15;
      mCol = 1;
    }
    
    SpannableStringBuilder[] getStyledText(CaptioningManager.CaptionStyle paramCaptionStyle)
    {
      ArrayList localArrayList = new ArrayList(15);
      for (int i = 1; i <= 15; i++)
      {
        SpannableStringBuilder localSpannableStringBuilder;
        if (mLines[i] != null) {
          localSpannableStringBuilder = mLines[i].getStyledText(paramCaptionStyle);
        } else {
          localSpannableStringBuilder = null;
        }
        localArrayList.add(localSpannableStringBuilder);
      }
      return (SpannableStringBuilder[])localArrayList.toArray(new SpannableStringBuilder[15]);
    }
    
    void rollUp(int paramInt)
    {
      for (int i = 0; i <= mRow - paramInt; i++) {
        mLines[i] = null;
      }
      i = mRow - paramInt + 1;
      paramInt = i;
      if (i < 1) {}
      for (paramInt = 1; paramInt < mRow; paramInt++) {
        mLines[paramInt] = mLines[(paramInt + 1)];
      }
      for (paramInt = mRow; paramInt < mLines.length; paramInt++) {
        mLines[paramInt] = null;
      }
      mCol = 1;
    }
    
    void tab(int paramInt)
    {
      moveCursorByCol(paramInt);
    }
    
    void writeMidRowCode(Cea608CCParser.StyleCode paramStyleCode)
    {
      getLineBuffer(mRow).setMidRowAt(mCol, paramStyleCode);
      moveCursorByCol(1);
    }
    
    void writePAC(Cea608CCParser.PAC paramPAC)
    {
      if (paramPAC.isIndentPAC()) {
        moveCursorTo(paramPAC.getRow(), paramPAC.getCol());
      } else {
        moveCursorTo(paramPAC.getRow(), 1);
      }
      getLineBuffer(mRow).setPACAt(mCol, paramPAC);
    }
    
    void writeText(String paramString)
    {
      for (int i = 0; i < paramString.length(); i++)
      {
        getLineBuffer(mRow).setCharAt(mCol, paramString.charAt(i));
        moveCursorByCol(1);
      }
    }
  }
  
  static abstract interface DisplayListener
  {
    public abstract CaptioningManager.CaptionStyle getCaptionStyle();
    
    public abstract void onDisplayChanged(SpannableStringBuilder[] paramArrayOfSpannableStringBuilder);
  }
  
  public static class MutableBackgroundColorSpan
    extends CharacterStyle
    implements UpdateAppearance
  {
    private int mColor;
    
    public MutableBackgroundColorSpan(int paramInt)
    {
      mColor = paramInt;
    }
    
    public int getBackgroundColor()
    {
      return mColor;
    }
    
    public void setBackgroundColor(int paramInt)
    {
      mColor = paramInt;
    }
    
    public void updateDrawState(TextPaint paramTextPaint)
    {
      bgColor = mColor;
    }
  }
  
  private static class PAC
    extends Cea608CCParser.StyleCode
  {
    final int mCol;
    final int mRow;
    
    PAC(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramInt4);
      mRow = paramInt1;
      mCol = paramInt2;
    }
    
    static PAC fromBytes(byte paramByte1, byte paramByte2)
    {
      int i = new int[] { 11, 1, 3, 12, 14, 5, 7, 9 }[(paramByte1 & 0x7)] + ((paramByte2 & 0x20) >> 5);
      paramByte1 = 0;
      if ((paramByte2 & 0x1) != 0) {
        paramByte1 = 0x0 | 0x2;
      }
      if ((paramByte2 & 0x10) != 0) {
        return new PAC(i, (paramByte2 >> 1 & 0x7) * 4, paramByte1, 0);
      }
      byte b = paramByte2 >> 1 & 0x7;
      int j = paramByte1;
      paramByte2 = b;
      if (b == 7)
      {
        paramByte2 = 0;
        j = paramByte1 | 0x1;
      }
      return new PAC(i, -1, j, paramByte2);
    }
    
    int getCol()
    {
      return mCol;
    }
    
    int getRow()
    {
      return mRow;
    }
    
    boolean isIndentPAC()
    {
      boolean bool;
      if (mCol >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      return String.format("{%d, %d}, %s", new Object[] { Integer.valueOf(mRow), Integer.valueOf(mCol), super.toString() });
    }
  }
  
  private static class StyleCode
  {
    static final int COLOR_BLUE = 2;
    static final int COLOR_CYAN = 3;
    static final int COLOR_GREEN = 1;
    static final int COLOR_INVALID = 7;
    static final int COLOR_MAGENTA = 6;
    static final int COLOR_RED = 4;
    static final int COLOR_WHITE = 0;
    static final int COLOR_YELLOW = 5;
    static final int STYLE_ITALICS = 1;
    static final int STYLE_UNDERLINE = 2;
    static final String[] mColorMap = { "WHITE", "GREEN", "BLUE", "CYAN", "RED", "YELLOW", "MAGENTA", "INVALID" };
    final int mColor;
    final int mStyle;
    
    StyleCode(int paramInt1, int paramInt2)
    {
      mStyle = paramInt1;
      mColor = paramInt2;
    }
    
    static StyleCode fromByte(byte paramByte)
    {
      int i = 0;
      byte b = paramByte >> 1 & 0x7;
      if ((paramByte & 0x1) != 0) {
        i = 0x0 | 0x2;
      }
      int j = i;
      paramByte = b;
      if (b == 7)
      {
        paramByte = 0;
        j = i | 0x1;
      }
      return new StyleCode(j, paramByte);
    }
    
    int getColor()
    {
      return mColor;
    }
    
    boolean isItalics()
    {
      int i = mStyle;
      boolean bool = true;
      if ((i & 0x1) == 0) {
        bool = false;
      }
      return bool;
    }
    
    boolean isUnderline()
    {
      boolean bool;
      if ((mStyle & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{");
      localStringBuilder.append(mColorMap[mColor]);
      if ((mStyle & 0x1) != 0) {
        localStringBuilder.append(", ITALICS");
      }
      if ((mStyle & 0x2) != 0) {
        localStringBuilder.append(", UNDERLINE");
      }
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}
