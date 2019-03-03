package android.widget;

import android.database.Cursor;

public abstract interface FilterQueryProvider
{
  public abstract Cursor runQuery(CharSequence paramCharSequence);
}
