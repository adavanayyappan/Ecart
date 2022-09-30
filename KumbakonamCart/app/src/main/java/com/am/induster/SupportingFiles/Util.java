package com.am.induster.SupportingFiles;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.am.induster.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class Util {

    public static List<UserList> getInboxData(Context ctx) {
        List<UserList> items = new ArrayList<>();
        String name_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        for (int i = 0; i < name_arr.length; i++) {
            UserList obj = new UserList();
            obj.from = name_arr[i];
            obj.email = "abc@gmail.com";
            obj.phone = "xxx";
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    public static int randInt(int max) {
        Random r = new Random();
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }

    /*
      this will toggle or action bar color
     */
    public static void toggleStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
    }

    public static boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static String random() {
        return UUID.randomUUID().toString();
    }
}
