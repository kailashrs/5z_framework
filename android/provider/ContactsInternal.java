package android.provider;

import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.UserHandle;
import android.text.TextUtils;
import android.widget.Toast;
import java.util.List;

public class ContactsInternal
{
  private static final int CONTACTS_URI_LOOKUP = 1001;
  private static final int CONTACTS_URI_LOOKUP_ID = 1000;
  private static final UriMatcher sContactsUriMatcher = new UriMatcher(-1);
  
  static
  {
    UriMatcher localUriMatcher = sContactsUriMatcher;
    localUriMatcher.addURI("com.android.contacts", "contacts/lookup/*", 1001);
    localUriMatcher.addURI("com.android.contacts", "contacts/lookup/*/#", 1000);
  }
  
  private ContactsInternal() {}
  
  private static boolean maybeStartManagedQuickContact(Context paramContext, Intent paramIntent)
  {
    Object localObject1 = paramIntent.getData();
    Object localObject2 = ((Uri)localObject1).getPathSegments();
    boolean bool;
    if (((List)localObject2).size() < 4) {
      bool = true;
    } else {
      bool = false;
    }
    long l1;
    if (bool) {
      l1 = ContactsContract.Contacts.ENTERPRISE_CONTACT_ID_BASE;
    } else {
      l1 = ContentUris.parseId((Uri)localObject1);
    }
    localObject2 = (String)((List)localObject2).get(2);
    localObject1 = ((Uri)localObject1).getQueryParameter("directory");
    long l2;
    if (localObject1 == null) {
      l2 = 1000000000L;
    } else {
      l2 = Long.parseLong((String)localObject1);
    }
    if ((!TextUtils.isEmpty((CharSequence)localObject2)) && (((String)localObject2).startsWith(ContactsContract.Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX)))
    {
      if (ContactsContract.Contacts.isEnterpriseContactId(l1))
      {
        if (ContactsContract.Directory.isEnterpriseDirectoryId(l2))
        {
          ((DevicePolicyManager)paramContext.getSystemService(DevicePolicyManager.class)).startManagedQuickContact(((String)localObject2).substring(ContactsContract.Contacts.ENTERPRISE_CONTACT_LOOKUP_PREFIX.length()), l1 - ContactsContract.Contacts.ENTERPRISE_CONTACT_ID_BASE, bool, l2 - 1000000000L, paramIntent);
          return true;
        }
        paramContext = new StringBuilder();
        paramContext.append("Invalid enterprise directory id: ");
        paramContext.append(l2);
        throw new IllegalArgumentException(paramContext.toString());
      }
      paramContext = new StringBuilder();
      paramContext.append("Invalid enterprise contact id: ");
      paramContext.append(l1);
      throw new IllegalArgumentException(paramContext.toString());
    }
    return false;
  }
  
  public static void startQuickContactWithErrorToast(Context paramContext, Intent paramIntent)
  {
    Uri localUri = paramIntent.getData();
    switch (sContactsUriMatcher.match(localUri))
    {
    default: 
      break;
    case 1000: 
    case 1001: 
      if (maybeStartManagedQuickContact(paramContext, paramIntent)) {
        return;
      }
      break;
    }
    startQuickContactWithErrorToastForUser(paramContext, paramIntent, paramContext.getUser());
  }
  
  public static void startQuickContactWithErrorToastForUser(Context paramContext, Intent paramIntent, UserHandle paramUserHandle)
  {
    try
    {
      paramContext.startActivityAsUser(paramIntent, paramUserHandle);
    }
    catch (ActivityNotFoundException paramIntent)
    {
      Toast.makeText(paramContext, 17040868, 0).show();
    }
  }
}
