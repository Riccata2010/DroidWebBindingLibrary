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
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.view.Window;
import android.widget.Toast;

public class AppUtils {

    public static void showToast(Context c, String mes, int type) {
        Toast.makeText(c, mes, type).show();
    }

    public static void longToast(Context c, String mes) {
        showToast(c, mes, Toast.LENGTH_LONG);
    }

    public static void longToast(Context c, int mes) {
        String m = c.getResources().getString(mes);
        showToast(c, m, Toast.LENGTH_LONG);
    }

    public static void shortToast(Context c, String mes) {
        showToast(c, mes, Toast.LENGTH_SHORT);
    }

    public static void shortToast(Context c, int mes) {
        String m = c.getResources().getString(mes);
        showToast(c, m, Toast.LENGTH_SHORT);
    }

    public static void requestCustomTitle(Activity act) {
        act.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

    public static void setOrientationPortrait(Context c) {
        c.getResources().getConfiguration().orientation = Configuration.ORIENTATION_PORTRAIT;
    }

    public static void setOrientationLandscape(Context c) {
        c.getResources().getConfiguration().orientation = Configuration.ORIENTATION_LANDSCAPE;
    }

    public static void setOrientationSquare(Context c) {
        c.getResources().getConfiguration().orientation = Configuration.ORIENTATION_SQUARE;
    }

    public static void setOrientationUndefined(Context c) {
        c.getResources().getConfiguration().orientation = Configuration.ORIENTATION_UNDEFINED;
    }

    public static void setCustomTitle(Activity act, int Rvalue) {
        act.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, Rvalue);
    }

    public static class CommonDialogInterface implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            arg0.dismiss();
        }
    }
}
