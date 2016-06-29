/**
 * The MIT License (MIT)
 * <p/>
 * DroidWebBindingLibrary - Copyright (c) 2016 Riccata2010
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package app.common.util.droid.web.binding.library;

import android.app.Activity;
import android.content.Intent;

import java.util.concurrent.Executors;

public class AppFinisher implements SetupActivity {

    public static final int TIME_BACK_MS = 2000;
    public static final int INTERNAL_ALLOW_NUMBER_PUSHBACK_BUT = 2;
    private int current_back = INTERNAL_ALLOW_NUMBER_PUSHBACK_BUT;
    private Activity activity = null;
    private int toastMessage = -1;
    private boolean mainPage = false;

    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setToastMessage(int message) {
        this.toastMessage = message;
    }

    public void closeOnBackPressed(boolean mainPage, ActionFinisher finisher) {
        this.mainPage = mainPage;
        current_back--;
        if (current_back <= 0) {

            if (finisher != null) {
                finisher.onClosing();
            }

            closeApp();

        } else {
            AppUtils.longToast(activity, toastMessage);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                @SuppressWarnings("static-access")
                public void run() {
                    try {
                        Thread.currentThread().sleep(TIME_BACK_MS);
                        current_back = INTERNAL_ALLOW_NUMBER_PUSHBACK_BUT;
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public void closeOnBackPressed(boolean mainPage) {
        this.mainPage = mainPage;
        current_back--;
        if (current_back <= 0) {
            closeApp();
        } else {
            AppUtils.longToast(activity, toastMessage);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                @SuppressWarnings("static-access")
                public void run() {
                    try {
                        Thread.currentThread().sleep(2000);
                        current_back = INTERNAL_ALLOW_NUMBER_PUSHBACK_BUT;
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public void closeApp() {
        try {
            activity.finish();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        if (mainPage) {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public interface ActionFinisher {
        void onClosing();
    }
}
