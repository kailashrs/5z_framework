package android.text.util;

import android.widget.MultiAutoCompleteTextView.Tokenizer;
import java.util.ArrayList;
import java.util.Collection;

public class Rfc822Tokenizer
  implements MultiAutoCompleteTextView.Tokenizer
{
  public Rfc822Tokenizer() {}
  
  private static void crunch(StringBuilder paramStringBuilder)
  {
    int i = 0;
    int j = paramStringBuilder.length();
    while (i < j) {
      if (paramStringBuilder.charAt(i) == 0)
      {
        if ((i != 0) && (i != j - 1) && (paramStringBuilder.charAt(i - 1) != ' ') && (paramStringBuilder.charAt(i - 1) != 0) && (paramStringBuilder.charAt(i + 1) != ' ') && (paramStringBuilder.charAt(i + 1) != 0))
        {
          i++;
        }
        else
        {
          paramStringBuilder.deleteCharAt(i);
          j--;
        }
      }
      else {
        i++;
      }
    }
    for (i = 0; i < j; i++) {
      if (paramStringBuilder.charAt(i) == 0) {
        paramStringBuilder.setCharAt(i, ' ');
      }
    }
  }
  
  public static void tokenize(CharSequence paramCharSequence, Collection<Rfc822Token> paramCollection)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = new StringBuilder();
    StringBuilder localStringBuilder3 = new StringBuilder();
    int i = 0;
    int j = paramCharSequence.length();
    while (i < j)
    {
      char c = paramCharSequence.charAt(i);
      if ((c != ',') && (c != ';'))
      {
        int k;
        if (c == '"')
        {
          k = i + 1;
          for (;;)
          {
            i = k;
            if (k >= j) {
              break;
            }
            c = paramCharSequence.charAt(k);
            if (c == '"')
            {
              i = k + 1;
              break;
            }
            if (c == '\\')
            {
              if (k + 1 < j) {
                localStringBuilder1.append(paramCharSequence.charAt(k + 1));
              }
              k += 2;
            }
            else
            {
              localStringBuilder1.append(c);
              k++;
            }
          }
        }
        if (c == '(')
        {
          k = 1;
          i++;
          while ((i < j) && (k > 0))
          {
            c = paramCharSequence.charAt(i);
            if (c == ')')
            {
              if (k > 1) {
                localStringBuilder3.append(c);
              }
              k--;
              i++;
            }
            else if (c == '(')
            {
              localStringBuilder3.append(c);
              k++;
              i++;
            }
            else if (c == '\\')
            {
              if (i + 1 < j) {
                localStringBuilder3.append(paramCharSequence.charAt(i + 1));
              }
              i += 2;
            }
            else
            {
              localStringBuilder3.append(c);
              i++;
            }
          }
        }
        else
        {
          if (c == '<') {
            for (k = i + 1;; k++)
            {
              i = k;
              if (k >= j) {
                break;
              }
              c = paramCharSequence.charAt(k);
              if (c == '>')
              {
                i = k + 1;
                break;
              }
              localStringBuilder2.append(c);
            }
          }
          if (c == ' ')
          {
            localStringBuilder1.append('\000');
            i++;
          }
          else
          {
            localStringBuilder1.append(c);
            i++;
          }
        }
      }
      else
      {
        i++;
        while ((i < j) && (paramCharSequence.charAt(i) == ' ')) {
          i++;
        }
        crunch(localStringBuilder1);
        if (localStringBuilder2.length() > 0) {
          paramCollection.add(new Rfc822Token(localStringBuilder1.toString(), localStringBuilder2.toString(), localStringBuilder3.toString()));
        } else if (localStringBuilder1.length() > 0) {
          paramCollection.add(new Rfc822Token(null, localStringBuilder1.toString(), localStringBuilder3.toString()));
        }
        localStringBuilder1.setLength(0);
        localStringBuilder2.setLength(0);
        localStringBuilder3.setLength(0);
      }
    }
    crunch(localStringBuilder1);
    if (localStringBuilder2.length() > 0) {
      paramCollection.add(new Rfc822Token(localStringBuilder1.toString(), localStringBuilder2.toString(), localStringBuilder3.toString()));
    } else if (localStringBuilder1.length() > 0) {
      paramCollection.add(new Rfc822Token(null, localStringBuilder1.toString(), localStringBuilder3.toString()));
    }
  }
  
  public static Rfc822Token[] tokenize(CharSequence paramCharSequence)
  {
    ArrayList localArrayList = new ArrayList();
    tokenize(paramCharSequence, localArrayList);
    return (Rfc822Token[])localArrayList.toArray(new Rfc822Token[localArrayList.size()]);
  }
  
  public int findTokenEnd(CharSequence paramCharSequence, int paramInt)
  {
    int i = paramCharSequence.length();
    while (paramInt < i)
    {
      int j = paramCharSequence.charAt(paramInt);
      if ((j != 44) && (j != 59))
      {
        if (j == 34)
        {
          j = paramInt + 1;
          for (;;)
          {
            paramInt = j;
            if (j >= i) {
              break;
            }
            paramInt = paramCharSequence.charAt(j);
            if (paramInt == 34)
            {
              paramInt = j + 1;
              break;
            }
            if ((paramInt == 92) && (j + 1 < i)) {
              j += 2;
            } else {
              j++;
            }
          }
        }
        if (j == 40)
        {
          j = 1;
          paramInt++;
          while ((paramInt < i) && (j > 0))
          {
            int k = paramCharSequence.charAt(paramInt);
            if (k == 41)
            {
              j--;
              paramInt++;
            }
            else if (k == 40)
            {
              j++;
              paramInt++;
            }
            else if ((k == 92) && (paramInt + 1 < i))
            {
              paramInt += 2;
            }
            else
            {
              paramInt++;
            }
          }
        }
        else
        {
          if (j == 60) {
            for (j = paramInt + 1;; j++)
            {
              paramInt = j;
              if (j >= i) {
                break;
              }
              if (paramCharSequence.charAt(j) == '>')
              {
                paramInt = j + 1;
                break;
              }
            }
          }
          paramInt++;
        }
      }
      else
      {
        return paramInt;
      }
    }
    return paramInt;
  }
  
  public int findTokenStart(CharSequence paramCharSequence, int paramInt)
  {
    int i = 0;
    int j = 0;
    while (j < paramInt)
    {
      int k = findTokenEnd(paramCharSequence, j);
      j = k;
      if (k < paramInt)
      {
        k++;
        while ((k < paramInt) && (paramCharSequence.charAt(k) == ' ')) {
          k++;
        }
        j = k;
        if (k < paramInt)
        {
          i = k;
          j = k;
        }
      }
    }
    return i;
  }
  
  public CharSequence terminateToken(CharSequence paramCharSequence)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramCharSequence);
    localStringBuilder.append(", ");
    return localStringBuilder.toString();
  }
}
