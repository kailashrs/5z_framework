package android.media;

import android.graphics.Color;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class Cea708CCParser
{
  public static final int CAPTION_EMIT_TYPE_BUFFER = 1;
  public static final int CAPTION_EMIT_TYPE_COMMAND_CLW = 4;
  public static final int CAPTION_EMIT_TYPE_COMMAND_CWX = 3;
  public static final int CAPTION_EMIT_TYPE_COMMAND_DFX = 16;
  public static final int CAPTION_EMIT_TYPE_COMMAND_DLC = 10;
  public static final int CAPTION_EMIT_TYPE_COMMAND_DLW = 8;
  public static final int CAPTION_EMIT_TYPE_COMMAND_DLY = 9;
  public static final int CAPTION_EMIT_TYPE_COMMAND_DSW = 5;
  public static final int CAPTION_EMIT_TYPE_COMMAND_HDW = 6;
  public static final int CAPTION_EMIT_TYPE_COMMAND_RST = 11;
  public static final int CAPTION_EMIT_TYPE_COMMAND_SPA = 12;
  public static final int CAPTION_EMIT_TYPE_COMMAND_SPC = 13;
  public static final int CAPTION_EMIT_TYPE_COMMAND_SPL = 14;
  public static final int CAPTION_EMIT_TYPE_COMMAND_SWA = 15;
  public static final int CAPTION_EMIT_TYPE_COMMAND_TGW = 7;
  public static final int CAPTION_EMIT_TYPE_CONTROL = 2;
  private static final boolean DEBUG = false;
  private static final String MUSIC_NOTE_CHAR = new String("♫".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
  private static final String TAG = "Cea708CCParser";
  private final StringBuffer mBuffer = new StringBuffer();
  private int mCommand = 0;
  private DisplayListener mListener = new DisplayListener()
  {
    public void emitEvent(Cea708CCParser.CaptionEvent paramAnonymousCaptionEvent) {}
  };
  
  Cea708CCParser(DisplayListener paramDisplayListener)
  {
    if (paramDisplayListener != null) {
      mListener = paramDisplayListener;
    }
  }
  
  private void emitCaptionBuffer()
  {
    if (mBuffer.length() > 0)
    {
      mListener.emitEvent(new CaptionEvent(1, mBuffer.toString()));
      mBuffer.setLength(0);
    }
  }
  
  private void emitCaptionEvent(CaptionEvent paramCaptionEvent)
  {
    emitCaptionBuffer();
    mListener.emitEvent(paramCaptionEvent);
  }
  
  private int parseC0(byte[] paramArrayOfByte, int paramInt)
  {
    if ((mCommand >= 24) && (mCommand <= 31))
    {
      if (mCommand == 24)
      {
        if (paramArrayOfByte[paramInt] == 0) {}
        try
        {
          mBuffer.append((char)paramArrayOfByte[(paramInt + 1)]);
          break label77;
          String str = new java/lang/String;
          str.<init>(Arrays.copyOfRange(paramArrayOfByte, paramInt, paramInt + 2), "EUC-KR");
          mBuffer.append(str);
        }
        catch (UnsupportedEncodingException paramArrayOfByte)
        {
          label77:
          Log.e("Cea708CCParser", "P16 Code - Could not find supported encoding", paramArrayOfByte);
        }
      }
      paramInt += 2;
    }
    else if ((mCommand >= 16) && (mCommand <= 23))
    {
      paramInt++;
    }
    else
    {
      int i = mCommand;
      if (i != 0) {
        if (i != 3)
        {
          if (i != 8) {
            switch (i)
            {
            default: 
              break;
            case 14: 
              emitCaptionEvent(new CaptionEvent(2, Character.valueOf((char)mCommand)));
              break;
            case 13: 
              mBuffer.append('\n');
              break;
            case 12: 
              emitCaptionEvent(new CaptionEvent(2, Character.valueOf((char)mCommand)));
              break;
            }
          } else {
            emitCaptionEvent(new CaptionEvent(2, Character.valueOf((char)mCommand)));
          }
        }
        else {
          emitCaptionEvent(new CaptionEvent(2, Character.valueOf((char)mCommand)));
        }
      }
    }
    return paramInt;
  }
  
  private int parseC1(byte[] paramArrayOfByte, int paramInt)
  {
    int i = mCommand;
    int j;
    boolean bool1;
    boolean bool2;
    int m;
    int n;
    int i3;
    CaptionColor localCaptionColor1;
    CaptionColor localCaptionColor2;
    switch (i)
    {
    default: 
      int i2;
      switch (i)
      {
      default: 
        break;
      case 152: 
      case 153: 
      case 154: 
      case 155: 
      case 156: 
      case 157: 
      case 158: 
      case 159: 
        j = mCommand;
        if ((paramArrayOfByte[paramInt] & 0x20) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        if ((paramArrayOfByte[paramInt] & 0x10) != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        boolean bool3;
        if ((paramArrayOfByte[paramInt] & 0x8) != 0) {
          bool3 = true;
        } else {
          bool3 = false;
        }
        int k = paramArrayOfByte[paramInt];
        boolean bool4;
        if ((paramArrayOfByte[(paramInt + 1)] & 0x80) != 0) {
          bool4 = true;
        } else {
          bool4 = false;
        }
        m = paramArrayOfByte[(paramInt + 1)];
        n = paramArrayOfByte[(paramInt + 2)];
        int i1 = paramArrayOfByte[(paramInt + 3)];
        i2 = paramArrayOfByte[(paramInt + 3)];
        i3 = paramArrayOfByte[(paramInt + 4)];
        i = paramArrayOfByte[(paramInt + 5)];
        emitCaptionEvent(new CaptionEvent(16, new CaptionWindow(j - 152, bool1, bool2, bool3, k & 0x7, bool4, m & 0x7F, n & 0xFF, (i1 & 0xF0) >> 4, 0xF & i2, i3 & 0x3F, 0x7 & paramArrayOfByte[(paramInt + 5)], (i & 0x38) >> 3)));
        paramInt += 6;
        break;
      case 151: 
        localCaptionColor1 = new CaptionColor((paramArrayOfByte[paramInt] & 0xC0) >> 6, (paramArrayOfByte[paramInt] & 0x30) >> 4, (paramArrayOfByte[paramInt] & 0xC) >> 2, paramArrayOfByte[paramInt] & 0x3);
        i3 = paramArrayOfByte[(paramInt + 1)];
        n = paramArrayOfByte[(paramInt + 2)];
        localCaptionColor2 = new CaptionColor(0, (paramArrayOfByte[(paramInt + 1)] & 0x30) >> 4, (paramArrayOfByte[(paramInt + 1)] & 0xC) >> 2, paramArrayOfByte[(paramInt + 1)] & 0x3);
        if ((paramArrayOfByte[(paramInt + 2)] & 0x40) != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        j = paramArrayOfByte[(paramInt + 2)];
        i = paramArrayOfByte[(paramInt + 2)];
        m = paramArrayOfByte[(paramInt + 2)];
        i2 = paramArrayOfByte[(paramInt + 3)];
        emitCaptionEvent(new CaptionEvent(15, new CaptionWindowAttr(localCaptionColor1, localCaptionColor2, (n & 0x80) >> 5 | (i3 & 0xC0) >> 6, bool1, (j & 0x30) >> 4, (i & 0xC) >> 2, m & 0x3, (0xC & paramArrayOfByte[(paramInt + 3)]) >> 2, (i2 & 0xF0) >> 4, 0x3 & paramArrayOfByte[(paramInt + 3)])));
        paramInt += 4;
      }
      break;
    case 146: 
      i = paramArrayOfByte[paramInt];
      i3 = paramArrayOfByte[(paramInt + 1)];
      paramInt += 2;
      emitCaptionEvent(new CaptionEvent(14, new CaptionPenLocation(i & 0xF, i3 & 0x3F)));
      break;
    case 145: 
      localCaptionColor2 = new CaptionColor((paramArrayOfByte[paramInt] & 0xC0) >> 6, (paramArrayOfByte[paramInt] & 0x30) >> 4, (paramArrayOfByte[paramInt] & 0xC) >> 2, paramArrayOfByte[paramInt] & 0x3);
      paramInt++;
      localCaptionColor1 = new CaptionColor((paramArrayOfByte[paramInt] & 0xC0) >> 6, (paramArrayOfByte[paramInt] & 0x30) >> 4, (paramArrayOfByte[paramInt] & 0xC) >> 2, paramArrayOfByte[paramInt] & 0x3);
      paramInt++;
      paramArrayOfByte = new CaptionColor(0, (paramArrayOfByte[paramInt] & 0x30) >> 4, (0xC & paramArrayOfByte[paramInt]) >> 2, paramArrayOfByte[paramInt] & 0x3);
      paramInt++;
      emitCaptionEvent(new CaptionEvent(13, new CaptionPenColor(localCaptionColor2, localCaptionColor1, paramArrayOfByte)));
      break;
    case 144: 
      j = paramArrayOfByte[paramInt];
      i3 = paramArrayOfByte[paramInt];
      i = paramArrayOfByte[paramInt];
      if ((paramArrayOfByte[(paramInt + 1)] & 0x80) != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      if ((paramArrayOfByte[(paramInt + 1)] & 0x40) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      m = paramArrayOfByte[(paramInt + 1)];
      n = paramArrayOfByte[(paramInt + 1)];
      paramInt += 2;
      emitCaptionEvent(new CaptionEvent(12, new CaptionPenAttr(i3 & 0x3, (i & 0xC) >> 2, (j & 0xF0) >> 4, 0x7 & n, (m & 0x38) >> 3, bool2, bool1)));
      break;
    case 143: 
      emitCaptionEvent(new CaptionEvent(11, null));
      break;
    case 142: 
      emitCaptionEvent(new CaptionEvent(10, null));
      break;
    case 141: 
      i = paramArrayOfByte[paramInt];
      paramInt++;
      emitCaptionEvent(new CaptionEvent(9, Integer.valueOf(i & 0xFF)));
      break;
    case 140: 
      i = paramArrayOfByte[paramInt];
      paramInt++;
      emitCaptionEvent(new CaptionEvent(8, Integer.valueOf(i & 0xFF)));
      break;
    case 139: 
      i = paramArrayOfByte[paramInt];
      paramInt++;
      emitCaptionEvent(new CaptionEvent(7, Integer.valueOf(i & 0xFF)));
      break;
    case 138: 
      i = paramArrayOfByte[paramInt];
      paramInt++;
      emitCaptionEvent(new CaptionEvent(6, Integer.valueOf(i & 0xFF)));
      break;
    case 137: 
      i = paramArrayOfByte[paramInt];
      paramInt++;
      emitCaptionEvent(new CaptionEvent(5, Integer.valueOf(i & 0xFF)));
      break;
    case 136: 
      i = paramArrayOfByte[paramInt];
      paramInt++;
      emitCaptionEvent(new CaptionEvent(4, Integer.valueOf(i & 0xFF)));
      break;
    case 128: 
    case 129: 
    case 130: 
    case 131: 
    case 132: 
    case 133: 
    case 134: 
    case 135: 
      emitCaptionEvent(new CaptionEvent(3, Integer.valueOf(mCommand - 128)));
    }
    return paramInt;
  }
  
  private int parseC2(byte[] paramArrayOfByte, int paramInt)
  {
    int i;
    if ((mCommand >= 0) && (mCommand <= 7))
    {
      i = paramInt;
    }
    else if ((mCommand >= 8) && (mCommand <= 15))
    {
      i = paramInt + 1;
    }
    else if ((mCommand >= 16) && (mCommand <= 23))
    {
      i = paramInt + 2;
    }
    else
    {
      i = paramInt;
      if (mCommand >= 24)
      {
        i = paramInt;
        if (mCommand <= 31) {
          i = paramInt + 3;
        }
      }
    }
    return i;
  }
  
  private int parseC3(byte[] paramArrayOfByte, int paramInt)
  {
    int i;
    if ((mCommand >= 128) && (mCommand <= 135))
    {
      i = paramInt + 4;
    }
    else
    {
      i = paramInt;
      if (mCommand >= 136)
      {
        i = paramInt;
        if (mCommand <= 143) {
          i = paramInt + 5;
        }
      }
    }
    return i;
  }
  
  private int parseExt1(byte[] paramArrayOfByte, int paramInt)
  {
    mCommand = (paramArrayOfByte[paramInt] & 0xFF);
    int i = paramInt + 1;
    if ((mCommand >= 0) && (mCommand <= 31))
    {
      paramInt = parseC2(paramArrayOfByte, i);
    }
    else if ((mCommand >= 128) && (mCommand <= 159))
    {
      paramInt = parseC3(paramArrayOfByte, i);
    }
    else if ((mCommand >= 32) && (mCommand <= 127))
    {
      paramInt = parseG2(paramArrayOfByte, i);
    }
    else
    {
      paramInt = i;
      if (mCommand >= 160)
      {
        paramInt = i;
        if (mCommand <= 255) {
          paramInt = parseG3(paramArrayOfByte, i);
        }
      }
    }
    return paramInt;
  }
  
  private int parseG0(byte[] paramArrayOfByte, int paramInt)
  {
    if (mCommand == 127) {
      mBuffer.append(MUSIC_NOTE_CHAR);
    } else {
      mBuffer.append((char)mCommand);
    }
    return paramInt;
  }
  
  private int parseG1(byte[] paramArrayOfByte, int paramInt)
  {
    mBuffer.append((char)mCommand);
    return paramInt;
  }
  
  private int parseG2(byte[] paramArrayOfByte, int paramInt)
  {
    int i = mCommand;
    if (i != 48) {
      switch (i)
      {
      default: 
        break;
      case 33: 
        break;
      case 32: 
        break;
      }
    }
    return paramInt;
  }
  
  private int parseG3(byte[] paramArrayOfByte, int paramInt)
  {
    int i = mCommand;
    return paramInt;
  }
  
  private int parseServiceBlockData(byte[] paramArrayOfByte, int paramInt)
  {
    mCommand = (paramArrayOfByte[paramInt] & 0xFF);
    int i = paramInt + 1;
    if (mCommand == 16)
    {
      paramInt = parseExt1(paramArrayOfByte, i);
    }
    else if ((mCommand >= 0) && (mCommand <= 31))
    {
      paramInt = parseC0(paramArrayOfByte, i);
    }
    else if ((mCommand >= 128) && (mCommand <= 159))
    {
      paramInt = parseC1(paramArrayOfByte, i);
    }
    else if ((mCommand >= 32) && (mCommand <= 127))
    {
      paramInt = parseG0(paramArrayOfByte, i);
    }
    else
    {
      paramInt = i;
      if (mCommand >= 160)
      {
        paramInt = i;
        if (mCommand <= 255) {
          paramInt = parseG1(paramArrayOfByte, i);
        }
      }
    }
    return paramInt;
  }
  
  public void parse(byte[] paramArrayOfByte)
  {
    for (int i = 0; i < paramArrayOfByte.length; i = parseServiceBlockData(paramArrayOfByte, i)) {}
    emitCaptionBuffer();
  }
  
  public static class CaptionColor
  {
    private static final int[] COLOR_MAP = { 0, 15, 240, 255 };
    public static final int OPACITY_FLASH = 1;
    private static final int[] OPACITY_MAP = { 255, 254, 128, 0 };
    public static final int OPACITY_SOLID = 0;
    public static final int OPACITY_TRANSLUCENT = 2;
    public static final int OPACITY_TRANSPARENT = 3;
    public final int blue;
    public final int green;
    public final int opacity;
    public final int red;
    
    public CaptionColor(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      opacity = paramInt1;
      red = paramInt2;
      green = paramInt3;
      blue = paramInt4;
    }
    
    public int getArgbValue()
    {
      return Color.argb(OPACITY_MAP[opacity], COLOR_MAP[red], COLOR_MAP[green], COLOR_MAP[blue]);
    }
  }
  
  public static class CaptionEvent
  {
    public final Object obj;
    public final int type;
    
    public CaptionEvent(int paramInt, Object paramObject)
    {
      type = paramInt;
      obj = paramObject;
    }
  }
  
  public static class CaptionPenAttr
  {
    public static final int OFFSET_NORMAL = 1;
    public static final int OFFSET_SUBSCRIPT = 0;
    public static final int OFFSET_SUPERSCRIPT = 2;
    public static final int PEN_SIZE_LARGE = 2;
    public static final int PEN_SIZE_SMALL = 0;
    public static final int PEN_SIZE_STANDARD = 1;
    public final int edgeType;
    public final int fontTag;
    public final boolean italic;
    public final int penOffset;
    public final int penSize;
    public final int textTag;
    public final boolean underline;
    
    public CaptionPenAttr(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean1, boolean paramBoolean2)
    {
      penSize = paramInt1;
      penOffset = paramInt2;
      textTag = paramInt3;
      fontTag = paramInt4;
      edgeType = paramInt5;
      underline = paramBoolean1;
      italic = paramBoolean2;
    }
  }
  
  public static class CaptionPenColor
  {
    public final Cea708CCParser.CaptionColor backgroundColor;
    public final Cea708CCParser.CaptionColor edgeColor;
    public final Cea708CCParser.CaptionColor foregroundColor;
    
    public CaptionPenColor(Cea708CCParser.CaptionColor paramCaptionColor1, Cea708CCParser.CaptionColor paramCaptionColor2, Cea708CCParser.CaptionColor paramCaptionColor3)
    {
      foregroundColor = paramCaptionColor1;
      backgroundColor = paramCaptionColor2;
      edgeColor = paramCaptionColor3;
    }
  }
  
  public static class CaptionPenLocation
  {
    public final int column;
    public final int row;
    
    public CaptionPenLocation(int paramInt1, int paramInt2)
    {
      row = paramInt1;
      column = paramInt2;
    }
  }
  
  public static class CaptionWindow
  {
    public final int anchorHorizontal;
    public final int anchorId;
    public final int anchorVertical;
    public final int columnCount;
    public final boolean columnLock;
    public final int id;
    public final int penStyle;
    public final int priority;
    public final boolean relativePositioning;
    public final int rowCount;
    public final boolean rowLock;
    public final boolean visible;
    public final int windowStyle;
    
    public CaptionWindow(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt2, boolean paramBoolean4, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
    {
      id = paramInt1;
      visible = paramBoolean1;
      rowLock = paramBoolean2;
      columnLock = paramBoolean3;
      priority = paramInt2;
      relativePositioning = paramBoolean4;
      anchorVertical = paramInt3;
      anchorHorizontal = paramInt4;
      anchorId = paramInt5;
      rowCount = paramInt6;
      columnCount = paramInt7;
      penStyle = paramInt8;
      windowStyle = paramInt9;
    }
  }
  
  public static class CaptionWindowAttr
  {
    public final Cea708CCParser.CaptionColor borderColor;
    public final int borderType;
    public final int displayEffect;
    public final int effectDirection;
    public final int effectSpeed;
    public final Cea708CCParser.CaptionColor fillColor;
    public final int justify;
    public final int printDirection;
    public final int scrollDirection;
    public final boolean wordWrap;
    
    public CaptionWindowAttr(Cea708CCParser.CaptionColor paramCaptionColor1, Cea708CCParser.CaptionColor paramCaptionColor2, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
    {
      fillColor = paramCaptionColor1;
      borderColor = paramCaptionColor2;
      borderType = paramInt1;
      wordWrap = paramBoolean;
      printDirection = paramInt2;
      scrollDirection = paramInt3;
      justify = paramInt4;
      effectDirection = paramInt5;
      effectSpeed = paramInt6;
      displayEffect = paramInt7;
    }
  }
  
  private static class Const
  {
    public static final int CODE_C0_BS = 8;
    public static final int CODE_C0_CR = 13;
    public static final int CODE_C0_ETX = 3;
    public static final int CODE_C0_EXT1 = 16;
    public static final int CODE_C0_FF = 12;
    public static final int CODE_C0_HCR = 14;
    public static final int CODE_C0_NUL = 0;
    public static final int CODE_C0_P16 = 24;
    public static final int CODE_C0_RANGE_END = 31;
    public static final int CODE_C0_RANGE_START = 0;
    public static final int CODE_C0_SKIP1_RANGE_END = 23;
    public static final int CODE_C0_SKIP1_RANGE_START = 16;
    public static final int CODE_C0_SKIP2_RANGE_END = 31;
    public static final int CODE_C0_SKIP2_RANGE_START = 24;
    public static final int CODE_C1_CLW = 136;
    public static final int CODE_C1_CW0 = 128;
    public static final int CODE_C1_CW1 = 129;
    public static final int CODE_C1_CW2 = 130;
    public static final int CODE_C1_CW3 = 131;
    public static final int CODE_C1_CW4 = 132;
    public static final int CODE_C1_CW5 = 133;
    public static final int CODE_C1_CW6 = 134;
    public static final int CODE_C1_CW7 = 135;
    public static final int CODE_C1_DF0 = 152;
    public static final int CODE_C1_DF1 = 153;
    public static final int CODE_C1_DF2 = 154;
    public static final int CODE_C1_DF3 = 155;
    public static final int CODE_C1_DF4 = 156;
    public static final int CODE_C1_DF5 = 157;
    public static final int CODE_C1_DF6 = 158;
    public static final int CODE_C1_DF7 = 159;
    public static final int CODE_C1_DLC = 142;
    public static final int CODE_C1_DLW = 140;
    public static final int CODE_C1_DLY = 141;
    public static final int CODE_C1_DSW = 137;
    public static final int CODE_C1_HDW = 138;
    public static final int CODE_C1_RANGE_END = 159;
    public static final int CODE_C1_RANGE_START = 128;
    public static final int CODE_C1_RST = 143;
    public static final int CODE_C1_SPA = 144;
    public static final int CODE_C1_SPC = 145;
    public static final int CODE_C1_SPL = 146;
    public static final int CODE_C1_SWA = 151;
    public static final int CODE_C1_TGW = 139;
    public static final int CODE_C2_RANGE_END = 31;
    public static final int CODE_C2_RANGE_START = 0;
    public static final int CODE_C2_SKIP0_RANGE_END = 7;
    public static final int CODE_C2_SKIP0_RANGE_START = 0;
    public static final int CODE_C2_SKIP1_RANGE_END = 15;
    public static final int CODE_C2_SKIP1_RANGE_START = 8;
    public static final int CODE_C2_SKIP2_RANGE_END = 23;
    public static final int CODE_C2_SKIP2_RANGE_START = 16;
    public static final int CODE_C2_SKIP3_RANGE_END = 31;
    public static final int CODE_C2_SKIP3_RANGE_START = 24;
    public static final int CODE_C3_RANGE_END = 159;
    public static final int CODE_C3_RANGE_START = 128;
    public static final int CODE_C3_SKIP4_RANGE_END = 135;
    public static final int CODE_C3_SKIP4_RANGE_START = 128;
    public static final int CODE_C3_SKIP5_RANGE_END = 143;
    public static final int CODE_C3_SKIP5_RANGE_START = 136;
    public static final int CODE_G0_MUSICNOTE = 127;
    public static final int CODE_G0_RANGE_END = 127;
    public static final int CODE_G0_RANGE_START = 32;
    public static final int CODE_G1_RANGE_END = 255;
    public static final int CODE_G1_RANGE_START = 160;
    public static final int CODE_G2_BLK = 48;
    public static final int CODE_G2_NBTSP = 33;
    public static final int CODE_G2_RANGE_END = 127;
    public static final int CODE_G2_RANGE_START = 32;
    public static final int CODE_G2_TSP = 32;
    public static final int CODE_G3_CC = 160;
    public static final int CODE_G3_RANGE_END = 255;
    public static final int CODE_G3_RANGE_START = 160;
    
    private Const() {}
  }
  
  static abstract interface DisplayListener
  {
    public abstract void emitEvent(Cea708CCParser.CaptionEvent paramCaptionEvent);
  }
}
