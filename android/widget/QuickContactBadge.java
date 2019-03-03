package android.widget;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.QuickContact;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.android.internal.R.styleable;

public class QuickContactBadge
  extends ImageView
  implements View.OnClickListener
{
  static final int EMAIL_ID_COLUMN_INDEX = 0;
  static final String[] EMAIL_LOOKUP_PROJECTION = { "contact_id", "lookup" };
  static final int EMAIL_LOOKUP_STRING_COLUMN_INDEX = 1;
  private static final String EXTRA_URI_CONTENT = "uri_content";
  static final int PHONE_ID_COLUMN_INDEX = 0;
  static final String[] PHONE_LOOKUP_PROJECTION = { "_id", "lookup" };
  static final int PHONE_LOOKUP_STRING_COLUMN_INDEX = 1;
  private static final int TOKEN_EMAIL_LOOKUP = 0;
  private static final int TOKEN_EMAIL_LOOKUP_AND_TRIGGER = 2;
  private static final int TOKEN_PHONE_LOOKUP = 1;
  private static final int TOKEN_PHONE_LOOKUP_AND_TRIGGER = 3;
  private String mContactEmail;
  private String mContactPhone;
  private Uri mContactUri;
  private Drawable mDefaultAvatar;
  protected String[] mExcludeMimes = null;
  private Bundle mExtras = null;
  private Drawable mOverlay;
  private String mPrioritizedMimeType;
  private QueryHandler mQueryHandler;
  
  public QuickContactBadge(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public QuickContactBadge(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public QuickContactBadge(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public QuickContactBadge(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = mContext.obtainStyledAttributes(R.styleable.Theme);
    mOverlay = paramContext.getDrawable(325);
    paramContext.recycle();
    setOnClickListener(this);
  }
  
  private boolean isAssigned()
  {
    boolean bool;
    if ((mContactUri == null) && (mContactEmail == null) && (mContactPhone == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void onContactUriChanged()
  {
    setEnabled(isAssigned());
  }
  
  public void assignContactFromEmail(String paramString, boolean paramBoolean)
  {
    assignContactFromEmail(paramString, paramBoolean, null);
  }
  
  public void assignContactFromEmail(String paramString, boolean paramBoolean, Bundle paramBundle)
  {
    mContactEmail = paramString;
    mExtras = paramBundle;
    if ((!paramBoolean) && (mQueryHandler != null))
    {
      mQueryHandler.startQuery(0, null, Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI, Uri.encode(mContactEmail)), EMAIL_LOOKUP_PROJECTION, null, null, null);
    }
    else
    {
      mContactUri = null;
      onContactUriChanged();
    }
  }
  
  public void assignContactFromPhone(String paramString, boolean paramBoolean)
  {
    assignContactFromPhone(paramString, paramBoolean, new Bundle());
  }
  
  public void assignContactFromPhone(String paramString, boolean paramBoolean, Bundle paramBundle)
  {
    mContactPhone = paramString;
    mExtras = paramBundle;
    if ((!paramBoolean) && (mQueryHandler != null))
    {
      mQueryHandler.startQuery(1, null, Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, mContactPhone), PHONE_LOOKUP_PROJECTION, null, null, null);
    }
    else
    {
      mContactUri = null;
      onContactUriChanged();
    }
  }
  
  public void assignContactUri(Uri paramUri)
  {
    mContactUri = paramUri;
    mContactEmail = null;
    mContactPhone = null;
    onContactUriChanged();
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2)
  {
    super.drawableHotspotChanged(paramFloat1, paramFloat2);
    if (mOverlay != null) {
      mOverlay.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = mOverlay;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(getDrawableState()))) {
      invalidateDrawable(localDrawable);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return QuickContactBadge.class.getName();
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (!isInEditMode()) {
      mQueryHandler = new QueryHandler(mContext.getContentResolver());
    }
  }
  
  public void onClick(View paramView)
  {
    if (mExtras == null) {
      paramView = new Bundle();
    } else {
      paramView = mExtras;
    }
    if (mContactUri != null)
    {
      ContactsContract.QuickContact.showQuickContact(getContext(), this, mContactUri, mExcludeMimes, mPrioritizedMimeType);
    }
    else if ((mContactEmail != null) && (mQueryHandler != null))
    {
      paramView.putString("uri_content", mContactEmail);
      mQueryHandler.startQuery(2, paramView, Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI, Uri.encode(mContactEmail)), EMAIL_LOOKUP_PROJECTION, null, null, null);
    }
    else
    {
      if ((mContactPhone == null) || (mQueryHandler == null)) {
        return;
      }
      paramView.putString("uri_content", mContactPhone);
      mQueryHandler.startQuery(3, paramView, Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, mContactPhone), PHONE_LOOKUP_PROJECTION, null, null, null);
    }
    return;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (!isEnabled()) {
      return;
    }
    if ((mOverlay != null) && (mOverlay.getIntrinsicWidth() != 0) && (mOverlay.getIntrinsicHeight() != 0))
    {
      mOverlay.setBounds(0, 0, getWidth(), getHeight());
      if ((mPaddingTop == 0) && (mPaddingLeft == 0))
      {
        mOverlay.draw(paramCanvas);
      }
      else
      {
        int i = paramCanvas.getSaveCount();
        paramCanvas.save();
        paramCanvas.translate(mPaddingLeft, mPaddingTop);
        mOverlay.draw(paramCanvas);
        paramCanvas.restoreToCount(i);
      }
      return;
    }
  }
  
  public void setExcludeMimes(String[] paramArrayOfString)
  {
    mExcludeMimes = paramArrayOfString;
  }
  
  public void setImageToDefault()
  {
    if (mDefaultAvatar == null) {
      mDefaultAvatar = mContext.getDrawable(17302588);
    }
    setImageDrawable(mDefaultAvatar);
  }
  
  public void setMode(int paramInt) {}
  
  public void setOverlay(Drawable paramDrawable)
  {
    mOverlay = paramDrawable;
  }
  
  public void setPrioritizedMimeType(String paramString)
  {
    mPrioritizedMimeType = paramString;
  }
  
  private class QueryHandler
    extends AsyncQueryHandler
  {
    public QueryHandler(ContentResolver paramContentResolver)
    {
      super();
    }
    
    protected void onQueryComplete(int paramInt, Object paramObject, Cursor paramCursor)
    {
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3 = null;
      Uri localUri = null;
      int i = 0;
      int j = 0;
      int k = 0;
      Bundle localBundle;
      if (paramObject != null) {
        localBundle = (Bundle)paramObject;
      } else {
        localBundle = new Bundle();
      }
      paramObject = localObject2;
      switch (paramInt)
      {
      default: 
        localObject2 = localObject1;
        paramInt = j;
        break;
      case 3: 
        k = 1;
      case 2: 
        try
        {
          localUri = Uri.fromParts("tel", localBundle.getString("uri_content"), null);
        }
        finally
        {
          break label256;
        }
        i = 1;
        paramObject = Uri.fromParts("mailto", localBundle.getString("uri_content"), null);
        break;
      case 1: 
        localObject2 = localObject1;
        localObject3 = localUri;
        paramInt = k;
        if (paramCursor == null) {
          break label268;
        }
        localObject2 = localObject1;
        localObject3 = localUri;
        paramInt = k;
        if (!paramCursor.moveToFirst()) {
          break label268;
        }
        localObject2 = ContactsContract.Contacts.getLookupUri(paramCursor.getLong(0), paramCursor.getString(1));
        localObject3 = localUri;
        paramInt = k;
        break;
      }
      localObject2 = localObject1;
      localObject3 = paramObject;
      paramInt = i;
      if (paramCursor != null)
      {
        localObject2 = localObject1;
        localObject3 = paramObject;
        paramInt = i;
        if (paramCursor.moveToFirst())
        {
          localObject2 = ContactsContract.Contacts.getLookupUri(paramCursor.getLong(0), paramCursor.getString(1));
          localObject3 = paramObject;
          paramInt = i;
          break label268;
          label256:
          if (paramCursor != null) {
            paramCursor.close();
          }
          throw paramObject;
        }
      }
      label268:
      if (paramCursor != null) {
        paramCursor.close();
      }
      QuickContactBadge.access$002(QuickContactBadge.this, (Uri)localObject2);
      QuickContactBadge.this.onContactUriChanged();
      if ((paramInt != 0) && (mContactUri != null))
      {
        ContactsContract.QuickContact.showQuickContact(getContext(), QuickContactBadge.this, mContactUri, mExcludeMimes, mPrioritizedMimeType);
      }
      else if (localObject3 != null)
      {
        paramObject = new Intent("com.android.contacts.action.SHOW_OR_CREATE_CONTACT", localObject3);
        if (localBundle != null)
        {
          localBundle.remove("uri_content");
          paramObject.putExtras(localBundle);
        }
        getContext().startActivity(paramObject);
      }
    }
  }
}
