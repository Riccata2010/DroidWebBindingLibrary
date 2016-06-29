package app.common.util.droid.web.binding;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import app.common.util.droid.web.binding.library.AppDialogUtils;
import app.common.util.droid.web.binding.library.AppFinisher;
import app.common.util.droid.web.binding.library.Bind;
import app.common.util.droid.web.binding.library.annotations.BindAttribute;
import app.common.util.droid.web.binding.library.annotations.BindHtmlReference;
import app.common.util.droid.web.binding.library.annotations.BindHtmlValue;
import app.common.util.droid.web.binding.library.annotations.BindInjectAppDialogUtils;
import app.common.util.droid.web.binding.library.annotations.BindInjectAppFinisher;

import static app.common.util.droid.web.binding.Constants.VERSION;

/**
 * This class is an example to test the main utility of Droid Web Binding Library.
 */
@BindHtmlReference(page = "login.html", R_webview = R.id.mainWebView, attributes = {
        @BindAttribute(var = "lbl.user", R_val = R.string.user),
        @BindAttribute(var = "lbl.password", R_val = R.string.lbl_password),
        @BindAttribute(var = "btn.login", R_val = R.string.btn_login),
        @BindAttribute(var = "app.version", val = VERSION)})
public class MainActivity extends Activity {

    @BindHtmlValue(parameter = "welcome.message", R_val = R.string.welcome_message)
    private String welcomeMessage;

    @BindInjectAppDialogUtils
    private AppDialogUtils dialogs;

    @BindInjectAppFinisher(toastMessage = R.string.exit_message)
    private AppFinisher finisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Bind.join(this).withClass(this).withJavaScriptListener(this).build();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finisher.closeOnBackPressed(false, new AppFinisher.ActionFinisher() {

            @Override
            public void onClosing() {
                /* Called when exit the app. */
            }
        });
    }

    @JavascriptInterface
    public void callMyLogin(String user, String pwd) {
        dialogs.showAlertDialog(getString(R.string.login_message) + " " + user, "Ok", true);
    }
}
