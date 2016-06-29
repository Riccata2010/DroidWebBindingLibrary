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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import app.common.util.droid.web.binding.library.AppUtils.CommonDialogInterface;

public class AppDialogUtils implements SetupActivity {

    private Activity activity = null;

    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void showAlertDialog(int mes, int but) {
        String m = activity.getResources().getString(mes);
        String b = activity.getResources().getString(but);
        showAlertDialog(m, b, false);
    }

    public void showAlertDialog(int mes, int but, boolean cancelable) {
        String m = activity.getResources().getString(mes);
        String b = activity.getResources().getString(but);
        showAlertDialog(m, b, cancelable);
    }

    public void showAlertDialog(String mes, String but, boolean cancelable) {
        Builder bu = new AlertDialog.Builder(activity);
        bu.setMessage(mes);
        bu.setCancelable(cancelable);
        bu.setNeutralButton(but, new CommonDialogInterface());
        bu.create();
        bu.show();
    }

    public void showAlertDialog(Context context, String mes, String but, boolean cancelable) {
        Builder bu = new AlertDialog.Builder(context);
        bu.setMessage(mes);
        bu.setCancelable(cancelable);
        bu.setNeutralButton(but, new CommonDialogInterface());
        bu.create();
        bu.show();
    }

    public ProgressDialog showProgressDialog(int title, int message, boolean inde, boolean cancelable) {
        String t = activity.getResources().getString(title);
        String m = activity.getResources().getString(message);
        return ProgressDialog.show(activity, t, m, inde, cancelable);
    }

    public ProgressDialog showProgressDialog(Context context, int title, int message, boolean inde, boolean cancelable) {
        String t = context.getResources().getString(title);
        String m = context.getResources().getString(message);
        return ProgressDialog.show(context, t, m, inde, cancelable);
    }

    public <I> AlertDialog showAlertMessage(Context context, int title, int message, final AlertListener response) {
        return new AlertDialog.Builder(context).setTitle(context.getResources().getString(title)).setCancelable(false)
                .setMessage(context.getResources().getString(message)).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        response.onClickOk();
                    }
                }).show();
    }

    public <I> AlertDialog showAlertMessage(Context context, String title, String message, final AlertListener response) {
        return new AlertDialog.Builder(context).setTitle(title).setCancelable(false).setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        response.onClickOk();
                    }
                }).show();
    }

    public <I> AlertDialog showAlertOkCancelDialog(Context context, int title, int message, final DialogListener response) {
        return showAlertOkCancelDialog(context, title, message, "Ok", "Cancel", response);
    }

    public <I> AlertDialog showAlertOkCancelDialog(Context context, int title, int message, int buttonOk, int buttonCancel,
                                                   final DialogListener response) {
        return showAlertOkCancelDialog(context, title, message, context.getString(buttonOk), context.getString(buttonCancel), response);
    }

    public <I> AlertDialog showAlertOkCancelDialog(Context context, int title, int message, final String buttonOk,
                                                   final String buttonCancel, final DialogListener response) {
        return new AlertDialog.Builder(context).setTitle(context.getResources().getString(title)).setCancelable(false)
                .setMessage(context.getResources().getString(message)).setPositiveButton(buttonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        response.onClickOk();
                    }
                }).setNegativeButton(buttonCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        response.onClickCancel();
                    }
                }).show();
    }

    public <I> AlertDialog showInputDialog(Context context, int title, int message, final InputDialogListener<I> response,
                                           final boolean cancelable) {
        final EditText text = new EditText(context);
        return new AlertDialog.Builder(context).setTitle(context.getResources().getString(title))
                .setMessage(context.getResources().getString(message)).setView(text)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        response.onClickOk((I) text.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        response.onClickCancel();
                    }
                }).setCancelable(cancelable).show();
    }

    public <I, K> void showMultipleChoice(Context context, int title, final List<I> choice, final InputDialogListener<List<K>> response) {

        final List<K> selected = new ArrayList<K>();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(title));
        builder.setMultiChoiceItems(choice.toArray(new CharSequence[choice.size()]), new boolean[choice.size()],
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        @SuppressWarnings("all")
                        K k = (K) new Integer(which);

                        if (isChecked) {
                            selected.add(k);
                        } else if (selected.contains(k))
                            selected.remove(k);
                    }
                });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (response == null)
                    return;
                response.onClickOk(selected);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (response == null)
                    return;
                response.onClickCancel();
            }
        });
        AlertDialog alert = builder.setCancelable(false).create();
        alert.show();
    }

    public <I> void showSingleChoice(Context context, int title, final List<I> choice, final InputDialogListener<Integer> response) {

        final List<Integer> selected = new ArrayList<Integer>();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(title));
        builder.setSingleChoiceItems(choice.toArray(new CharSequence[choice.size()]), -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected.clear();
                selected.add(which);
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (response == null)
                    return;
                if (selected == null || selected.isEmpty())
                    return;
                response.onClickOk(selected.get(0));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (response == null)
                    return;
                response.onClickCancel();
            }
        });
        AlertDialog alert = builder.setCancelable(false).create();
        alert.show();
    }
}
