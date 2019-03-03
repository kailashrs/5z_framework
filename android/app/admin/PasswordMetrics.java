package android.app.admin;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PasswordMetrics
  implements Parcelable
{
  private static final int CHAR_DIGIT = 2;
  private static final int CHAR_LOWER_CASE = 0;
  private static final int CHAR_SYMBOL = 3;
  private static final int CHAR_UPPER_CASE = 1;
  public static final Parcelable.Creator<PasswordMetrics> CREATOR = new Parcelable.Creator()
  {
    public PasswordMetrics createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PasswordMetrics(paramAnonymousParcel, null);
    }
    
    public PasswordMetrics[] newArray(int paramAnonymousInt)
    {
      return new PasswordMetrics[paramAnonymousInt];
    }
  };
  public static final int MAX_ALLOWED_SEQUENCE = 3;
  public int length = 0;
  public int letters = 0;
  public int lowerCase = 0;
  public int nonLetter = 0;
  public int numeric = 0;
  public int quality = 0;
  public int symbols = 0;
  public int upperCase = 0;
  
  public PasswordMetrics() {}
  
  public PasswordMetrics(int paramInt1, int paramInt2)
  {
    quality = paramInt1;
    length = paramInt2;
  }
  
  public PasswordMetrics(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    this(paramInt1, paramInt2);
    letters = paramInt3;
    upperCase = paramInt4;
    lowerCase = paramInt5;
    numeric = paramInt6;
    symbols = paramInt7;
    nonLetter = paramInt8;
  }
  
  private PasswordMetrics(Parcel paramParcel)
  {
    quality = paramParcel.readInt();
    length = paramParcel.readInt();
    letters = paramParcel.readInt();
    upperCase = paramParcel.readInt();
    lowerCase = paramParcel.readInt();
    numeric = paramParcel.readInt();
    symbols = paramParcel.readInt();
    nonLetter = paramParcel.readInt();
  }
  
  private static int categoryChar(char paramChar)
  {
    if (('a' <= paramChar) && (paramChar <= 'z')) {
      return 0;
    }
    if (('A' <= paramChar) && (paramChar <= 'Z')) {
      return 1;
    }
    if (('0' <= paramChar) && (paramChar <= '9')) {
      return 2;
    }
    return 3;
  }
  
  public static PasswordMetrics computeForPassword(String paramString)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = paramString.length();
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5;
    while (i4 < m)
    {
      switch (categoryChar(paramString.charAt(i4)))
      {
      default: 
        i5 = i3;
        break;
      case 3: 
        i1++;
        k++;
        i5 = i3;
        break;
      case 2: 
        j++;
        k++;
        i5 = i3;
        break;
      case 1: 
        i5 = i3 + 1;
        i2++;
        break;
      case 0: 
        i5 = i3 + 1;
        i++;
      }
      i4++;
      i3 = i5;
    }
    i4 = 1;
    if (j > 0) {
      i5 = 1;
    } else {
      i5 = 0;
    }
    if (i3 + i1 <= 0) {
      i4 = 0;
    }
    if ((i4 != 0) && (i5 != 0)) {
      i5 = 327680;
    }
    for (;;)
    {
      break;
      if (i4 != 0) {
        i5 = 262144;
      } else if (i5 != 0)
      {
        if (maxLengthSequence(paramString) > 3) {
          i5 = 131072;
        } else {
          i5 = 196608;
        }
      }
      else {
        i5 = n;
      }
    }
    return new PasswordMetrics(i5, m, i3, i2, i, j, i1, k);
  }
  
  private static int maxDiffCategory(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0;
    case 2: 
      return 10;
    }
    return 1;
  }
  
  public static int maxLengthSequence(String paramString)
  {
    if (paramString.length() == 0) {
      return 0;
    }
    int i = paramString.charAt(0);
    int j = categoryChar(i);
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 1;
    char c = i;
    while (i2 < paramString.length())
    {
      i = paramString.charAt(i2);
      int i4 = categoryChar(i);
      int i5 = i - c;
      int i6;
      if ((i4 == j) && (Math.abs(i5) <= maxDiffCategory(j)))
      {
        c = n;
        i6 = i1;
        if (m != 0)
        {
          c = n;
          i6 = i1;
          if (i5 != k)
          {
            i3 = Math.max(n, i2 - i1);
            i6 = i2 - 1;
          }
        }
        k = i5;
        i1 = 1;
        n = i3;
      }
      else
      {
        n = Math.max(n, i2 - i1);
        i6 = i2;
        i1 = 0;
        j = i4;
      }
      int i3 = i;
      i2++;
      m = i1;
      i1 = i6;
    }
    return Math.max(n, paramString.length() - i1);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof PasswordMetrics;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (PasswordMetrics)paramObject;
    bool1 = bool2;
    if (quality == quality)
    {
      bool1 = bool2;
      if (length == length)
      {
        bool1 = bool2;
        if (letters == letters)
        {
          bool1 = bool2;
          if (upperCase == upperCase)
          {
            bool1 = bool2;
            if (lowerCase == lowerCase)
            {
              bool1 = bool2;
              if (numeric == numeric)
              {
                bool1 = bool2;
                if (symbols == symbols)
                {
                  bool1 = bool2;
                  if (nonLetter == nonLetter) {
                    bool1 = true;
                  }
                }
              }
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public boolean isDefault()
  {
    boolean bool;
    if ((quality == 0) && (length == 0) && (letters == 0) && (upperCase == 0) && (lowerCase == 0) && (numeric == 0) && (symbols == 0) && (nonLetter == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(quality);
    paramParcel.writeInt(length);
    paramParcel.writeInt(letters);
    paramParcel.writeInt(upperCase);
    paramParcel.writeInt(lowerCase);
    paramParcel.writeInt(numeric);
    paramParcel.writeInt(symbols);
    paramParcel.writeInt(nonLetter);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface CharacterCatagory {}
}
