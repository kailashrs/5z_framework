package com.android.internal.policy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class PhoneLayoutInflater
  extends LayoutInflater
{
  private static final String[] sClassPrefixList = { "android.widget.", "android.webkit.", "android.app." };
  
  public PhoneLayoutInflater(Context paramContext)
  {
    super(paramContext);
  }
  
  protected PhoneLayoutInflater(LayoutInflater paramLayoutInflater, Context paramContext)
  {
    super(paramLayoutInflater, paramContext);
  }
  
  public LayoutInflater cloneInContext(Context paramContext)
  {
    return new PhoneLayoutInflater(this, paramContext);
  }
  
  protected View onCreateView(String paramString, AttributeSet paramAttributeSet)
    throws ClassNotFoundException
  {
    for (Object localObject : sClassPrefixList) {
      try
      {
        localObject = createView(paramString, (String)localObject, paramAttributeSet);
        if (localObject != null) {
          return localObject;
        }
      }
      catch (ClassNotFoundException localClassNotFoundException) {}
    }
    return super.onCreateView(paramString, paramAttributeSet);
  }
}
