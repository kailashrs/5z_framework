package android.content;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class UriMatcher
{
  private static final int EXACT = 0;
  public static final int NO_MATCH = -1;
  private static final int NUMBER = 1;
  private static final int TEXT = 2;
  private ArrayList<UriMatcher> mChildren;
  private int mCode;
  private String mText;
  private int mWhich;
  
  private UriMatcher()
  {
    mCode = -1;
    mWhich = -1;
    mChildren = new ArrayList();
    mText = null;
  }
  
  public UriMatcher(int paramInt)
  {
    mCode = paramInt;
    mWhich = -1;
    mChildren = new ArrayList();
    mText = null;
  }
  
  public void addURI(String paramString1, String paramString2, int paramInt)
  {
    if (paramInt >= 0)
    {
      Object localObject1 = null;
      Object localObject2;
      if (paramString2 != null)
      {
        localObject1 = paramString2;
        localObject2 = localObject1;
        if (paramString2.length() > 1)
        {
          localObject2 = localObject1;
          if (paramString2.charAt(0) == '/') {
            localObject2 = paramString2.substring(1);
          }
        }
        localObject1 = ((String)localObject2).split("/");
      }
      int i;
      if (localObject1 != null) {
        i = localObject1.length;
      } else {
        i = 0;
      }
      paramString2 = this;
      for (int j = -1; j < i; j++)
      {
        String str;
        if (j < 0) {
          str = paramString1;
        } else {
          str = localObject1[j];
        }
        ArrayList localArrayList = mChildren;
        int k = localArrayList.size();
        for (int m = 0;; m++)
        {
          localObject2 = paramString2;
          if (m >= k) {
            break;
          }
          localObject2 = (UriMatcher)localArrayList.get(m);
          if (str.equals(mText)) {
            break;
          }
        }
        paramString2 = (String)localObject2;
        if (m == k)
        {
          paramString2 = new UriMatcher();
          if (str.equals("#")) {
            mWhich = 1;
          } else if (str.equals("*")) {
            mWhich = 2;
          } else {
            mWhich = 0;
          }
          mText = str;
          mChildren.add(paramString2);
        }
      }
      mCode = paramInt;
      return;
    }
    paramString1 = new StringBuilder();
    paramString1.append("code ");
    paramString1.append(paramInt);
    paramString1.append(" is invalid: it must be positive");
    throw new IllegalArgumentException(paramString1.toString());
  }
  
  public int match(Uri paramUri)
  {
    List localList = paramUri.getPathSegments();
    int i = localList.size();
    if ((i == 0) && (paramUri.getAuthority() == null)) {
      return mCode;
    }
    Object localObject1 = this;
    for (int j = -1; j < i; j++)
    {
      String str;
      if (j < 0) {
        str = paramUri.getAuthority();
      } else {
        str = (String)localList.get(j);
      }
      ArrayList localArrayList = mChildren;
      if (localArrayList == null) {
        break;
      }
      int k = localArrayList.size();
      Object localObject2 = null;
      int m = 0;
      for (;;)
      {
        localObject1 = localObject2;
        if (m >= k) {
          break;
        }
        UriMatcher localUriMatcher = (UriMatcher)localArrayList.get(m);
        switch (mWhich)
        {
        default: 
          localObject1 = localObject2;
          break;
        case 2: 
          localObject1 = localUriMatcher;
          break;
        case 1: 
          int n = str.length();
          for (int i1 = 0; i1 < n; i1++)
          {
            int i2 = str.charAt(i1);
            localObject1 = localObject2;
            if (i2 < 48) {
              break label247;
            }
            if (i2 > 57)
            {
              localObject1 = localObject2;
              break label247;
            }
          }
          localObject1 = localUriMatcher;
          break;
        case 0: 
          localObject1 = localObject2;
          if (mText.equals(str)) {
            localObject1 = localUriMatcher;
          }
          break;
        }
        label247:
        if (localObject1 != null) {
          break;
        }
        m++;
        localObject2 = localObject1;
      }
      if (localObject1 == null) {
        return -1;
      }
    }
    return mCode;
  }
}
