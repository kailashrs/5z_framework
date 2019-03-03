package android.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Log;

public class BlockedNumberContract
{
  public static final String AUTHORITY = "com.android.blockednumber";
  public static final Uri AUTHORITY_URI = Uri.parse("content://com.android.blockednumber");
  public static final String EXTRA_CALL_PRESENTATION = "extra_call_presentation";
  public static final String EXTRA_CONTACT_EXIST = "extra_contact_exist";
  public static final String EXTRA_ENHANCED_SETTING_KEY = "extra_enhanced_setting_key";
  public static final String EXTRA_ENHANCED_SETTING_VALUE = "extra_enhanced_setting_value";
  public static final String METHOD_CAN_CURRENT_USER_BLOCK_NUMBERS = "can_current_user_block_numbers";
  public static final String METHOD_IS_BLOCKED = "is_blocked";
  public static final String METHOD_UNBLOCK = "unblock";
  public static final String RES_CAN_BLOCK_NUMBERS = "can_block";
  public static final String RES_ENHANCED_SETTING_IS_ENABLED = "enhanced_setting_enabled";
  public static final String RES_NUMBER_IS_BLOCKED = "blocked";
  public static final String RES_NUM_ROWS_DELETED = "num_deleted";
  public static final String RES_SHOW_EMERGENCY_CALL_NOTIFICATION = "show_emergency_call_notification";
  
  private BlockedNumberContract() {}
  
  public static boolean canCurrentUserBlockNumbers(Context paramContext)
  {
    boolean bool1 = false;
    try
    {
      paramContext = paramContext.getContentResolver().call(AUTHORITY_URI, "can_current_user_block_numbers", null, null);
      boolean bool2 = bool1;
      if (paramContext != null)
      {
        boolean bool3 = paramContext.getBoolean("can_block", false);
        bool2 = bool1;
        if (bool3) {
          bool2 = true;
        }
      }
      return bool2;
    }
    catch (NullPointerException|IllegalArgumentException paramContext)
    {
      Log.w(null, "canCurrentUserBlockNumbers: provider not ready.", new Object[0]);
    }
    return false;
  }
  
  public static boolean isBlocked(Context paramContext, String paramString)
  {
    boolean bool1 = false;
    try
    {
      paramContext = paramContext.getContentResolver().call(AUTHORITY_URI, "is_blocked", paramString, null);
      boolean bool2 = bool1;
      if (paramContext != null)
      {
        boolean bool3 = paramContext.getBoolean("blocked", false);
        bool2 = bool1;
        if (bool3) {
          bool2 = true;
        }
      }
      return bool2;
    }
    catch (NullPointerException|IllegalArgumentException paramContext)
    {
      Log.w(null, "isBlocked: provider not ready.", new Object[0]);
    }
    return false;
  }
  
  public static int unblock(Context paramContext, String paramString)
  {
    return paramContext.getContentResolver().call(AUTHORITY_URI, "unblock", paramString, null).getInt("num_deleted", 0);
  }
  
  public static class BlockedNumbers
  {
    public static final String COLUMN_E164_NUMBER = "e164_number";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ORIGINAL_NUMBER = "original_number";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/blocked_number";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/blocked_number";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BlockedNumberContract.AUTHORITY_URI, "blocked");
    
    private BlockedNumbers() {}
  }
  
  public static class SystemContract
  {
    public static final String ACTION_BLOCK_SUPPRESSION_STATE_CHANGED = "android.provider.action.BLOCK_SUPPRESSION_STATE_CHANGED";
    public static final String ENHANCED_SETTING_KEY_BLOCK_PAYPHONE = "block_payphone_calls_setting";
    public static final String ENHANCED_SETTING_KEY_BLOCK_PRIVATE = "block_private_number_calls_setting";
    public static final String ENHANCED_SETTING_KEY_BLOCK_UNKNOWN = "block_unknown_calls_setting";
    public static final String ENHANCED_SETTING_KEY_BLOCK_UNREGISTERED = "block_numbers_not_in_contacts_setting";
    public static final String ENHANCED_SETTING_KEY_SHOW_EMERGENCY_CALL_NOTIFICATION = "show_emergency_call_notification";
    public static final String METHOD_END_BLOCK_SUPPRESSION = "end_block_suppression";
    public static final String METHOD_GET_BLOCK_SUPPRESSION_STATUS = "get_block_suppression_status";
    public static final String METHOD_GET_ENHANCED_BLOCK_SETTING = "get_enhanced_block_setting";
    public static final String METHOD_NOTIFY_EMERGENCY_CONTACT = "notify_emergency_contact";
    public static final String METHOD_SET_ENHANCED_BLOCK_SETTING = "set_enhanced_block_setting";
    public static final String METHOD_SHOULD_SHOW_EMERGENCY_CALL_NOTIFICATION = "should_show_emergency_call_notification";
    public static final String METHOD_SHOULD_SYSTEM_BLOCK_NUMBER = "should_system_block_number";
    public static final String RES_BLOCKING_SUPPRESSED_UNTIL_TIMESTAMP = "blocking_suppressed_until_timestamp";
    public static final String RES_IS_BLOCKING_SUPPRESSED = "blocking_suppressed";
    
    public SystemContract() {}
    
    public static void endBlockSuppression(Context paramContext)
    {
      paramContext.getContentResolver().call(BlockedNumberContract.AUTHORITY_URI, "end_block_suppression", null, null);
    }
    
    public static BlockSuppressionStatus getBlockSuppressionStatus(Context paramContext)
    {
      paramContext = paramContext.getContentResolver().call(BlockedNumberContract.AUTHORITY_URI, "get_block_suppression_status", null, null);
      return new BlockSuppressionStatus(paramContext.getBoolean("blocking_suppressed", false), paramContext.getLong("blocking_suppressed_until_timestamp", 0L));
    }
    
    public static boolean getEnhancedBlockSetting(Context paramContext, String paramString)
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("extra_enhanced_setting_key", paramString);
      boolean bool1 = false;
      try
      {
        paramContext = paramContext.getContentResolver().call(BlockedNumberContract.AUTHORITY_URI, "get_enhanced_block_setting", null, localBundle);
        boolean bool2 = bool1;
        if (paramContext != null)
        {
          boolean bool3 = paramContext.getBoolean("enhanced_setting_enabled", false);
          bool2 = bool1;
          if (bool3) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (NullPointerException|IllegalArgumentException paramContext)
      {
        Log.w(null, "getEnhancedBlockSetting: provider not ready.", new Object[0]);
      }
      return false;
    }
    
    public static void notifyEmergencyContact(Context paramContext)
    {
      try
      {
        paramContext.getContentResolver().call(BlockedNumberContract.AUTHORITY_URI, "notify_emergency_contact", null, null);
      }
      catch (NullPointerException|IllegalArgumentException paramContext)
      {
        Log.w(null, "notifyEmergencyContact: provider not ready.", new Object[0]);
      }
    }
    
    public static void setEnhancedBlockSetting(Context paramContext, String paramString, boolean paramBoolean)
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("extra_enhanced_setting_key", paramString);
      localBundle.putBoolean("extra_enhanced_setting_value", paramBoolean);
      paramContext.getContentResolver().call(BlockedNumberContract.AUTHORITY_URI, "set_enhanced_block_setting", null, localBundle);
    }
    
    public static boolean shouldShowEmergencyCallNotification(Context paramContext)
    {
      boolean bool1 = false;
      try
      {
        paramContext = paramContext.getContentResolver().call(BlockedNumberContract.AUTHORITY_URI, "should_show_emergency_call_notification", null, null);
        boolean bool2 = bool1;
        if (paramContext != null)
        {
          boolean bool3 = paramContext.getBoolean("show_emergency_call_notification", false);
          bool2 = bool1;
          if (bool3) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (NullPointerException|IllegalArgumentException paramContext)
      {
        Log.w(null, "shouldShowEmergencyCallNotification: provider not ready.", new Object[0]);
      }
      return false;
    }
    
    public static boolean shouldSystemBlockNumber(Context paramContext, String paramString, Bundle paramBundle)
    {
      boolean bool1 = false;
      try
      {
        paramContext = paramContext.getContentResolver().call(BlockedNumberContract.AUTHORITY_URI, "should_system_block_number", paramString, paramBundle);
        boolean bool2 = bool1;
        if (paramContext != null)
        {
          boolean bool3 = paramContext.getBoolean("blocked", false);
          bool2 = bool1;
          if (bool3) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (NullPointerException|IllegalArgumentException paramContext)
      {
        Log.w(null, "shouldSystemBlockNumber: provider not ready.", new Object[0]);
      }
      return false;
    }
    
    public static class BlockSuppressionStatus
    {
      public final boolean isSuppressed;
      public final long untilTimestampMillis;
      
      public BlockSuppressionStatus(boolean paramBoolean, long paramLong)
      {
        isSuppressed = paramBoolean;
        untilTimestampMillis = paramLong;
      }
    }
  }
}
