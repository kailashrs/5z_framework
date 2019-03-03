package com.android.internal.telephony.cat;

import android.graphics.Bitmap;
import java.util.Iterator;
import java.util.List;

class SelectItemParams
  extends CommandParams
{
  boolean mLoadTitleIcon = false;
  Menu mMenu = null;
  
  SelectItemParams(CommandDetails paramCommandDetails, Menu paramMenu, boolean paramBoolean)
  {
    super(paramCommandDetails);
    mMenu = paramMenu;
    mLoadTitleIcon = paramBoolean;
  }
  
  boolean setIcon(Bitmap paramBitmap)
  {
    if ((paramBitmap != null) && (mMenu != null))
    {
      if ((mLoadTitleIcon) && (mMenu.titleIcon == null))
      {
        mMenu.titleIcon = paramBitmap;
      }
      else
      {
        Iterator localIterator = mMenu.items.iterator();
        while (localIterator.hasNext())
        {
          Item localItem = (Item)localIterator.next();
          if (icon == null) {
            icon = paramBitmap;
          }
        }
      }
      return true;
    }
    return false;
  }
}
