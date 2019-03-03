package com.android.internal.util;

public class CharSequences
{
  public CharSequences() {}
  
  public static int compareToIgnoreCase(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    int i = paramCharSequence1.length();
    int j = paramCharSequence2.length();
    int k = 0;
    int m = 0;
    int n;
    if (i < j) {
      n = i;
    } else {
      n = j;
    }
    while (k < n)
    {
      int i1 = Character.toLowerCase(paramCharSequence1.charAt(k)) - Character.toLowerCase(paramCharSequence2.charAt(m));
      if (i1 != 0) {
        return i1;
      }
      k++;
      m++;
    }
    return i - j;
  }
  
  public static boolean equals(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    if (paramCharSequence1.length() != paramCharSequence2.length()) {
      return false;
    }
    int i = paramCharSequence1.length();
    for (int j = 0; j < i; j++) {
      if (paramCharSequence1.charAt(j) != paramCharSequence2.charAt(j)) {
        return false;
      }
    }
    return true;
  }
  
  public static CharSequence forAsciiBytes(byte[] paramArrayOfByte)
  {
    new CharSequence()
    {
      public char charAt(int paramAnonymousInt)
      {
        return (char)CharSequences.this[paramAnonymousInt];
      }
      
      public int length()
      {
        return CharSequences.this.length;
      }
      
      public CharSequence subSequence(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        return CharSequences.forAsciiBytes(CharSequences.this, paramAnonymousInt1, paramAnonymousInt2);
      }
      
      public String toString()
      {
        return new String(CharSequences.this);
      }
    };
  }
  
  public static CharSequence forAsciiBytes(byte[] paramArrayOfByte, final int paramInt1, final int paramInt2)
  {
    validate(paramInt1, paramInt2, paramArrayOfByte.length);
    new CharSequence()
    {
      public char charAt(int paramAnonymousInt)
      {
        return (char)CharSequences.this[(paramInt1 + paramAnonymousInt)];
      }
      
      public int length()
      {
        return paramInt2 - paramInt1;
      }
      
      public CharSequence subSequence(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        paramAnonymousInt1 -= paramInt1;
        paramAnonymousInt2 -= paramInt1;
        CharSequences.validate(paramAnonymousInt1, paramAnonymousInt2, length());
        return CharSequences.forAsciiBytes(CharSequences.this, paramAnonymousInt1, paramAnonymousInt2);
      }
      
      public String toString()
      {
        return new String(CharSequences.this, paramInt1, length());
      }
    };
  }
  
  static void validate(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 >= 0)
    {
      if (paramInt2 >= 0)
      {
        if (paramInt2 <= paramInt3)
        {
          if (paramInt1 <= paramInt2) {
            return;
          }
          throw new IndexOutOfBoundsException();
        }
        throw new IndexOutOfBoundsException();
      }
      throw new IndexOutOfBoundsException();
    }
    throw new IndexOutOfBoundsException();
  }
}
