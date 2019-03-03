package android.util;

import java.io.UnsupportedEncodingException;

public class Base64
{
  public static final int CRLF = 4;
  public static final int DEFAULT = 0;
  public static final int NO_CLOSE = 16;
  public static final int NO_PADDING = 1;
  public static final int NO_WRAP = 2;
  public static final int URL_SAFE = 8;
  
  private Base64() {}
  
  public static byte[] decode(String paramString, int paramInt)
  {
    return decode(paramString.getBytes(), paramInt);
  }
  
  public static byte[] decode(byte[] paramArrayOfByte, int paramInt)
  {
    return decode(paramArrayOfByte, 0, paramArrayOfByte.length, paramInt);
  }
  
  public static byte[] decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    Decoder localDecoder = new Decoder(paramInt3, new byte[paramInt2 * 3 / 4]);
    if (localDecoder.process(paramArrayOfByte, paramInt1, paramInt2, true))
    {
      if (op == output.length) {
        return output;
      }
      paramArrayOfByte = new byte[op];
      System.arraycopy(output, 0, paramArrayOfByte, 0, op);
      return paramArrayOfByte;
    }
    throw new IllegalArgumentException("bad base-64");
  }
  
  public static byte[] encode(byte[] paramArrayOfByte, int paramInt)
  {
    return encode(paramArrayOfByte, 0, paramArrayOfByte.length, paramInt);
  }
  
  public static byte[] encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    Encoder localEncoder = new Encoder(paramInt3, null);
    int i = paramInt2 / 3 * 4;
    if (do_padding)
    {
      paramInt3 = i;
      if (paramInt2 % 3 > 0) {
        paramInt3 = i + 4;
      }
    }
    else
    {
      switch (paramInt2 % 3)
      {
      default: 
        paramInt3 = i;
        break;
      case 2: 
        paramInt3 = i + 3;
        break;
      case 1: 
        paramInt3 = i + 2;
        break;
      case 0: 
        paramInt3 = i;
      }
    }
    i = paramInt3;
    if (do_newline)
    {
      i = paramInt3;
      if (paramInt2 > 0)
      {
        int j = (paramInt2 - 1) / 57;
        if (do_cr) {
          i = 2;
        } else {
          i = 1;
        }
        i = paramInt3 + (j + 1) * i;
      }
    }
    output = new byte[i];
    localEncoder.process(paramArrayOfByte, paramInt1, paramInt2, true);
    return output;
  }
  
  public static String encodeToString(byte[] paramArrayOfByte, int paramInt)
  {
    try
    {
      paramArrayOfByte = new String(encode(paramArrayOfByte, paramInt), "US-ASCII");
      return paramArrayOfByte;
    }
    catch (UnsupportedEncodingException paramArrayOfByte)
    {
      throw new AssertionError(paramArrayOfByte);
    }
  }
  
  public static String encodeToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      paramArrayOfByte = new String(encode(paramArrayOfByte, paramInt1, paramInt2, paramInt3), "US-ASCII");
      return paramArrayOfByte;
    }
    catch (UnsupportedEncodingException paramArrayOfByte)
    {
      throw new AssertionError(paramArrayOfByte);
    }
  }
  
  static abstract class Coder
  {
    public int op;
    public byte[] output;
    
    Coder() {}
    
    public abstract int maxOutputSize(int paramInt);
    
    public abstract boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean);
  }
  
  static class Decoder
    extends Base64.Coder
  {
    private static final int[] DECODE = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private static final int[] DECODE_WEBSAFE = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private static final int EQUALS = -2;
    private static final int SKIP = -1;
    private final int[] alphabet;
    private int state;
    private int value;
    
    public Decoder(int paramInt, byte[] paramArrayOfByte)
    {
      output = paramArrayOfByte;
      if ((paramInt & 0x8) == 0) {
        paramArrayOfByte = DECODE;
      } else {
        paramArrayOfByte = DECODE_WEBSAFE;
      }
      alphabet = paramArrayOfByte;
      state = 0;
      value = 0;
    }
    
    public int maxOutputSize(int paramInt)
    {
      return paramInt * 3 / 4 + 10;
    }
    
    public boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      if (state == 6) {
        return false;
      }
      int i = paramInt1;
      int j = paramInt2 + paramInt1;
      int k = state;
      paramInt2 = value;
      paramInt1 = 0;
      byte[] arrayOfByte = output;
      int[] arrayOfInt = alphabet;
      int m;
      int n;
      for (;;)
      {
        m = paramInt2;
        n = paramInt1;
        if (i >= j) {
          break;
        }
        int i1 = i;
        m = paramInt2;
        n = paramInt1;
        if (k == 0)
        {
          while (i + 4 <= j)
          {
            m = arrayOfInt[(paramArrayOfByte[i] & 0xFF)] << 18 | arrayOfInt[(paramArrayOfByte[(i + 1)] & 0xFF)] << 12 | arrayOfInt[(paramArrayOfByte[(i + 2)] & 0xFF)] << 6 | arrayOfInt[(paramArrayOfByte[(i + 3)] & 0xFF)];
            n = m;
            paramInt2 = n;
            if (m < 0) {
              break;
            }
            arrayOfByte[(paramInt1 + 2)] = ((byte)(byte)n);
            arrayOfByte[(paramInt1 + 1)] = ((byte)(byte)(n >> 8));
            arrayOfByte[paramInt1] = ((byte)(byte)(n >> 16));
            paramInt1 += 3;
            i += 4;
            paramInt2 = n;
          }
          i1 = i;
          m = paramInt2;
          n = paramInt1;
          if (i >= j)
          {
            m = paramInt2;
            n = paramInt1;
            break;
          }
        }
        i = arrayOfInt[(paramArrayOfByte[i1] & 0xFF)];
        switch (k)
        {
        default: 
          paramInt1 = k;
          paramInt2 = m;
          break;
        case 5: 
          paramInt1 = k;
          paramInt2 = m;
          if (i != -1)
          {
            state = 6;
            return false;
          }
          break;
        case 4: 
          if (i == -2)
          {
            paramInt1 = k + 1;
            paramInt2 = m;
          }
          else
          {
            paramInt1 = k;
            paramInt2 = m;
            if (i != -1)
            {
              state = 6;
              return false;
            }
          }
          break;
        case 3: 
          if (i >= 0)
          {
            paramInt2 = m << 6 | i;
            arrayOfByte[(n + 2)] = ((byte)(byte)paramInt2);
            arrayOfByte[(n + 1)] = ((byte)(byte)(paramInt2 >> 8));
            arrayOfByte[n] = ((byte)(byte)(paramInt2 >> 16));
            n += 3;
            paramInt1 = 0;
          }
          else if (i == -2)
          {
            arrayOfByte[(n + 1)] = ((byte)(byte)(m >> 2));
            arrayOfByte[n] = ((byte)(byte)(m >> 10));
            n += 2;
            paramInt1 = 5;
            paramInt2 = m;
          }
          else
          {
            paramInt1 = k;
            paramInt2 = m;
            if (i != -1)
            {
              state = 6;
              return false;
            }
          }
          break;
        case 2: 
          if (i >= 0)
          {
            paramInt2 = m << 6 | i;
            paramInt1 = k + 1;
          }
          else if (i == -2)
          {
            arrayOfByte[n] = ((byte)(byte)(m >> 4));
            paramInt1 = 4;
            n++;
            paramInt2 = m;
          }
          else
          {
            paramInt1 = k;
            paramInt2 = m;
            if (i != -1)
            {
              state = 6;
              return false;
            }
          }
          break;
        case 1: 
          if (i >= 0)
          {
            paramInt2 = m << 6 | i;
            paramInt1 = k + 1;
          }
          else
          {
            paramInt1 = k;
            paramInt2 = m;
            if (i != -1)
            {
              state = 6;
              return false;
            }
          }
          break;
        case 0: 
          if (i >= 0)
          {
            paramInt2 = i;
            paramInt1 = k + 1;
          }
          else
          {
            paramInt1 = k;
            paramInt2 = m;
            if (i != -1)
            {
              state = 6;
              return false;
            }
          }
          break;
        }
        i = i1 + 1;
        k = paramInt1;
        paramInt1 = n;
      }
      if (!paramBoolean)
      {
        state = k;
        value = m;
        op = n;
        return true;
      }
      switch (k)
      {
      default: 
        break;
      case 4: 
        state = 6;
        return false;
      case 3: 
        paramInt1 = n + 1;
        arrayOfByte[n] = ((byte)(byte)(m >> 10));
        n = paramInt1 + 1;
        arrayOfByte[paramInt1] = ((byte)(byte)(m >> 2));
        break;
      case 2: 
        arrayOfByte[n] = ((byte)(byte)(m >> 4));
        n++;
        break;
      case 1: 
        state = 6;
        return false;
      }
      state = k;
      op = n;
      return true;
    }
  }
  
  static class Encoder
    extends Base64.Coder
  {
    private static final byte[] ENCODE = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
    private static final byte[] ENCODE_WEBSAFE = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
    public static final int LINE_GROUPS = 19;
    private final byte[] alphabet;
    private int count;
    public final boolean do_cr;
    public final boolean do_newline;
    public final boolean do_padding;
    private final byte[] tail;
    int tailLen;
    
    public Encoder(int paramInt, byte[] paramArrayOfByte)
    {
      output = paramArrayOfByte;
      boolean bool1 = true;
      boolean bool2;
      if ((paramInt & 0x1) == 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      do_padding = bool2;
      if ((paramInt & 0x2) == 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      do_newline = bool2;
      if ((paramInt & 0x4) != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      do_cr = bool2;
      if ((paramInt & 0x8) == 0) {
        paramArrayOfByte = ENCODE;
      } else {
        paramArrayOfByte = ENCODE_WEBSAFE;
      }
      alphabet = paramArrayOfByte;
      tail = new byte[2];
      tailLen = 0;
      if (do_newline) {
        paramInt = 19;
      } else {
        paramInt = -1;
      }
      count = paramInt;
    }
    
    public int maxOutputSize(int paramInt)
    {
      return paramInt * 8 / 5 + 10;
    }
    
    public boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      byte[] arrayOfByte1 = alphabet;
      byte[] arrayOfByte2 = output;
      int i = 0;
      int j = count;
      int k = paramInt2 + paramInt1;
      int m = -1;
      switch (tailLen)
      {
      default: 
        break;
      case 2: 
        if (paramInt1 + 1 <= k)
        {
          m = (tail[0] & 0xFF) << 16 | (tail[1] & 0xFF) << 8 | paramArrayOfByte[paramInt1] & 0xFF;
          tailLen = 0;
          n = paramInt1 + 1;
        }
        break;
      case 1: 
        if (paramInt1 + 2 <= k)
        {
          paramInt2 = tail[0];
          m = paramInt1 + 1;
          paramInt1 = paramArrayOfByte[paramInt1];
          n = m + 1;
          m = (paramInt1 & 0xFF) << 8 | (paramInt2 & 0xFF) << 16 | paramArrayOfByte[m] & 0xFF;
          tailLen = 0;
        }
        break;
      }
      int n = paramInt1;
      paramInt1 = i;
      i = j;
      paramInt2 = n;
      int i1;
      if (m != -1)
      {
        paramInt2 = 0 + 1;
        arrayOfByte2[0] = ((byte)arrayOfByte1[(m >> 18 & 0x3F)]);
        paramInt1 = paramInt2 + 1;
        arrayOfByte2[paramInt2] = ((byte)arrayOfByte1[(m >> 12 & 0x3F)]);
        paramInt2 = paramInt1 + 1;
        arrayOfByte2[paramInt1] = ((byte)arrayOfByte1[(m >> 6 & 0x3F)]);
        i1 = paramInt2 + 1;
        arrayOfByte2[paramInt2] = ((byte)arrayOfByte1[(m & 0x3F)]);
        m = j - 1;
        paramInt1 = i1;
        i = m;
        paramInt2 = n;
        if (m == 0)
        {
          paramInt1 = i1;
          if (do_cr)
          {
            arrayOfByte2[i1] = ((byte)13);
            paramInt1 = i1 + 1;
          }
          i = paramInt1 + 1;
          arrayOfByte2[paramInt1] = ((byte)10);
          paramInt2 = n;
          paramInt1 = i;
        }
      }
      for (;;)
      {
        i = 19;
        do
        {
          if (paramInt2 + 3 > k) {
            break;
          }
          n = (paramArrayOfByte[paramInt2] & 0xFF) << 16 | (paramArrayOfByte[(paramInt2 + 1)] & 0xFF) << 8 | paramArrayOfByte[(paramInt2 + 2)] & 0xFF;
          arrayOfByte2[paramInt1] = ((byte)arrayOfByte1[(n >> 18 & 0x3F)]);
          arrayOfByte2[(paramInt1 + 1)] = ((byte)arrayOfByte1[(n >> 12 & 0x3F)]);
          arrayOfByte2[(paramInt1 + 2)] = ((byte)arrayOfByte1[(n >> 6 & 0x3F)]);
          arrayOfByte2[(paramInt1 + 3)] = ((byte)arrayOfByte1[(n & 0x3F)]);
          n = paramInt2 + 3;
          m = paramInt1 + 4;
          i1 = i - 1;
          paramInt1 = m;
          i = i1;
          paramInt2 = n;
        } while (i1 != 0);
        paramInt1 = m;
        if (do_cr)
        {
          arrayOfByte2[m] = ((byte)13);
          paramInt1 = m + 1;
        }
        paramInt2 = paramInt1 + 1;
        arrayOfByte2[paramInt1] = ((byte)10);
        paramInt1 = paramInt2;
        paramInt2 = n;
      }
      if (paramBoolean)
      {
        if (paramInt2 - tailLen == k - 1)
        {
          n = 0;
          if (tailLen > 0)
          {
            m = tail[0];
            n = 0 + 1;
          }
          else
          {
            i1 = paramInt2 + 1;
            m = paramArrayOfByte[paramInt2];
            paramInt2 = i1;
          }
          m = (m & 0xFF) << 4;
          tailLen -= n;
          i1 = paramInt1 + 1;
          arrayOfByte2[paramInt1] = ((byte)arrayOfByte1[(m >> 6 & 0x3F)]);
          n = i1 + 1;
          arrayOfByte2[i1] = ((byte)arrayOfByte1[(m & 0x3F)]);
          paramInt1 = n;
          if (do_padding)
          {
            m = n + 1;
            arrayOfByte2[n] = ((byte)61);
            paramInt1 = m + 1;
            arrayOfByte2[m] = ((byte)61);
          }
          n = paramInt1;
          if (do_newline)
          {
            if (do_cr)
            {
              n = paramInt1 + 1;
              arrayOfByte2[paramInt1] = ((byte)13);
              paramInt1 = n;
            }
            n = paramInt1 + 1;
            arrayOfByte2[paramInt1] = ((byte)10);
          }
        }
        for (;;)
        {
          paramInt2 = n;
          break label1044;
          if (paramInt2 - tailLen != k - 2) {
            break;
          }
          n = 0;
          if (tailLen > 1)
          {
            m = tail[0];
            n = 0 + 1;
          }
          else
          {
            i1 = paramInt2 + 1;
            m = paramArrayOfByte[paramInt2];
            paramInt2 = i1;
          }
          if (tailLen > 0)
          {
            i1 = tail[n];
            n++;
          }
          else
          {
            i1 = paramArrayOfByte[paramInt2];
            paramInt2++;
          }
          m = (m & 0xFF) << 10 | (i1 & 0xFF) << 2;
          tailLen -= n;
          n = paramInt1 + 1;
          arrayOfByte2[paramInt1] = ((byte)arrayOfByte1[(m >> 12 & 0x3F)]);
          paramInt1 = n + 1;
          arrayOfByte2[n] = ((byte)arrayOfByte1[(m >> 6 & 0x3F)]);
          n = paramInt1 + 1;
          arrayOfByte2[paramInt1] = ((byte)arrayOfByte1[(m & 0x3F)]);
          paramInt1 = n;
          if (do_padding)
          {
            arrayOfByte2[n] = ((byte)61);
            paramInt1 = n + 1;
          }
          n = paramInt1;
          if (do_newline)
          {
            n = paramInt1;
            if (do_cr)
            {
              arrayOfByte2[paramInt1] = ((byte)13);
              n = paramInt1 + 1;
            }
            arrayOfByte2[n] = ((byte)10);
            n++;
          }
        }
        paramInt2 = paramInt1;
        if (do_newline)
        {
          paramInt2 = paramInt1;
          if (paramInt1 > 0)
          {
            paramInt2 = paramInt1;
            if (i != 19)
            {
              if (do_cr)
              {
                paramInt2 = paramInt1 + 1;
                arrayOfByte2[paramInt1] = ((byte)13);
                paramInt1 = paramInt2;
              }
              paramInt2 = paramInt1 + 1;
              arrayOfByte2[paramInt1] = ((byte)10);
            }
          }
        }
        label1044:
        n = paramInt2;
      }
      else if (paramInt2 == k - 1)
      {
        arrayOfByte2 = tail;
        n = tailLen;
        tailLen = (n + 1);
        arrayOfByte2[n] = ((byte)paramArrayOfByte[paramInt2]);
        n = paramInt1;
      }
      else
      {
        n = paramInt1;
        if (paramInt2 == k - 2)
        {
          arrayOfByte2 = tail;
          n = tailLen;
          tailLen = (n + 1);
          arrayOfByte2[n] = ((byte)paramArrayOfByte[paramInt2]);
          arrayOfByte2 = tail;
          n = tailLen;
          tailLen = (n + 1);
          arrayOfByte2[n] = ((byte)paramArrayOfByte[(paramInt2 + 1)]);
          n = paramInt1;
        }
      }
      op = n;
      count = i;
      return true;
    }
  }
}
