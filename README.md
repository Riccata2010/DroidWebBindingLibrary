# DroidWebBindingLibrary

DroidWebBindingLibrary is a small library for Android that allows you to quickly connect html page from an Activity. Uses Java annotations to inject the required dependencies at runtime.

  The main annotations are:
  - @BindHtmlReference (to load your html page and the webview instance)
  - @BindAttribute (to translate variables with the R.string value)
  - @BindHtmlValue (set local field and translate)
  - @BindInjectAppDialogUtils (inject AppDialogUtils utility instance)
  - @BindInjectAppFinisher (inject AppFinisher to manage the closing of app)

Example:
``` java
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
        [..]
        Bind.join(this).withClass(this).withJavaScriptListener(this).build();
        [..]
    }
```
### Version
Beta 1.0
