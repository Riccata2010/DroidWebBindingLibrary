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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import app.common.util.droid.web.binding.library.annotations.BindHtmlReference;
import app.common.util.droid.web.binding.library.annotations.BindHtmlValue;
import app.common.util.droid.web.binding.library.annotations.BindInjectAppDialogUtils;
import app.common.util.droid.web.binding.library.annotations.BindInjectAppFinisher;

public class Bind implements BindReference {

    public static final String BIND = "BIND";
    public static final String VERISION = "beta-1.0";

    public static String BASE_ASSET_FILE_URL = "file:///android_asset/";

    private Activity activity = null;
    private WebView webview = null;
    private String page = null;
    private int webViewId = -1;
    private String htmlText = null;
    private Object annotatedClass = null;
    private Object javaScriptListener = null;
    private Map<String, Object> values = null;

    private Bind(final Activity activity) {
        this.activity = activity;
    }

    public static BindReference join(final Activity activity) {
        if (activity == null) {
            throw new NullPointerException("Parameter activity is null.");
        }
        return new Bind(activity);
    }

    @Override
    public BindReference withClass(Object annotatedClass) {
        this.annotatedClass = annotatedClass;
        return this;
    }

    @Override
    public BindReference withJavaScriptListener(Object javaScriptListener) {
        this.javaScriptListener = javaScriptListener;
        return this;
    }

    @Override
    public void addPageEvent(final PageEvent pageEvent) {

        if (webview == null)
            throw new NullPointerException("Not found a valid WebView object, please call build first.");

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                return pageEvent.shouldOverrideUrlLoading(view, urlNewString);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                pageEvent.onPageStarted(view, url, facIcon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pageEvent.onPageFinished(view, url);
            }
        });
    }

    @Override
    public BindReference build() throws Throwable {

        if (annotatedClass == null) {
            throw new NullPointerException("Can't find a valid html class reference. Please use annotation @BindHtmlReference.");
        }

        Annotation[] anno = annotatedClass.getClass().getDeclaredAnnotations();

        for (Annotation annotation : anno) {

            InvocationHandler handler = AbstractHandler.getInvocationHandler(annotation);

            if (handler != null) {

                BindHtmlReference.ReflectionHandler ref = BindHtmlReference.ReflectionHandler.instance();

                if (ref.isBindAnnotation(handler, null)) {

                    AnnotationResult ar = ref.completeAnnotation(this, handler, null);
                    page = ar.getStringValue();
                    webViewId = ar.getIntValue();

                    if (javaScriptListener != null)
                        __setup_WebView(javaScriptListener);

                    for (int i = 0; ar.getList() != null && i < ar.getListSize(); i++) {
                        AnnotationResult attr = (AnnotationResult) ar.getFromList(i);
                        updateInternalMap(attr);
                    }

                    break;
                }
            }
        }

        Field[] fields = annotatedClass.getClass().getDeclaredFields();

        for (int i = 0; fields != null && i < fields.length; i++) {
            fields[i].setAccessible(true);
            anno = fields[i].getDeclaredAnnotations();
            for (Annotation annotation : anno) {
                if (Proxy.isProxyClass(annotation.getClass())) {

                    InvocationHandler handler = Proxy.getInvocationHandler(annotation);

                    __setup_BindHtmlValue(handler, fields[i], annotatedClass);
                    __setup_BindInjectAppFinisher(handler, fields[i], annotatedClass);
                    __setup_BindInjectAppDialogUtils(handler, fields[i], annotatedClass);
                }
            }
        }

        if (page != null) {
            htmlText = AssetUpdater.repalceAll(getActivity(), values, page);
            reloadPage(htmlText);
        }

        return this;
    }

    @Override
    public String getBindVersion() {
        return this.VERISION;
    }

    private void put(String var, Object value) {
        if (values == null) {
            values = new WeakHashMap<>();
        }
        values.put(var, value);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void __setup_WebView(Object javaScriptListener) throws Exception {
        try {
            webview = (WebView) activity.findViewById(getWebView());
        } catch (Exception exc) {
            Exception error = new Exception("Error setup WebView by id.", exc);
            throw error;
        }

        if (webview == null) {
            throw new NullPointerException("Can't find a WebView.");
        }

        webview.getSettings().setJavaScriptEnabled(true);

        if (javaScriptListener != null)
            webview.addJavascriptInterface(javaScriptListener, BIND);
    }

    private void __setup_BindHtmlValue(InvocationHandler handler, Field field, Object jsListener) throws IllegalArgumentException {

        try {

            BindHtmlValue.ReflectionHandler ref = BindHtmlValue.ReflectionHandler.instance();

            if (ref.isBindAnnotation(handler, null)) {

                Map<String, Object> map = new HashMap<>();
                map.put("field", field);
                map.put("jsListener", jsListener);

                AnnotationResult ar = ref.completeAnnotation(this, handler, map);
                if (ar != null && ar.getMap() != null && !ar.getMap().isEmpty()) {
                    updateInternalMap(ar);
                }
            }
        } catch (Throwable exc) {
            IllegalArgumentException iae = new IllegalArgumentException("Error processing annotation BindHtmlValue.");
            iae.setStackTrace(exc.getStackTrace());
            throw iae;
        }
    }

    private void __setup_BindInjectAppDialogUtils(InvocationHandler handler, Field field, Object jsListener)
            throws IllegalArgumentException {

        try {

            BindInjectAppDialogUtils.ReflectionHandler ref = BindInjectAppDialogUtils.ReflectionHandler.instance();

            if (ref.isBindAnnotation(handler, null)) {

                Map<String, Object> map = new HashMap<>();
                map.put("field", field);
                map.put("jsListener", jsListener);

                ref.completeAnnotation(this, handler, map);
            }
        } catch (Throwable exc) {
            IllegalArgumentException iae = new IllegalArgumentException("Error processing annotation BindInjectAppDialogUtils.");
            iae.setStackTrace(exc.getStackTrace());
            throw iae;
        }
    }

    private void __setup_BindInjectAppFinisher(InvocationHandler handler, Field field, Object jsListener) throws IllegalArgumentException {

        try {

            BindInjectAppFinisher.ReflectionHandler ref = BindInjectAppFinisher.ReflectionHandler.instance();

            if (ref.isBindAnnotation(handler, null)) {

                Map<String, Object> map = new HashMap<>();
                map.put("field", field);
                map.put("jsListener", jsListener);

                ref.completeAnnotation(this, handler, map);
            }
        } catch (Throwable exc) {
            IllegalArgumentException iae = new IllegalArgumentException("Error processing annotation BindInjectAppFinisher.");
            iae.setStackTrace(exc.getStackTrace());
            throw iae;
        }
    }

    private void updateInternalMap(AnnotationResult attr) {

        Map<String, Object> map = attr.getMap();

        if (map == null)
            return;

        for (String k : map.keySet()) {
            put(k, map.get(k));
        }
    }

    // *******************************************************************************************//
    // *******************************************************************************************//
    // *******************************************************************************************//

    @Override
    public int getWebView() {
        return webViewId;
    }

    @Override
    public void setWebView(int webView, Object javaScriptListener) throws IllegalArgumentException {
        this.webViewId = webView;
        try {
            __setup_WebView(javaScriptListener);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Activity getActivity() {
        return this.activity;
    }

    @Override
    public void invokeJavascriptFunction(String function, Object... params) {
        if (webview != null && function != null && function.length() > 0) {

            final StringBuilder sb = new StringBuilder();
            sb.append(function).append("(");

            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof String)
                        sb.append("\'").append(params[i]).append("\'");
                    else
                        sb.append(params[i]);
                    if (i + 1 < params.length) {
                        sb.append(", ");
                    }
                }
            }

            sb.append(");");
            javascript(sb.toString());
        }
    }

    @Override
    public <V> void eval(JSElement<V>... elements) {
        if (webview != null && elements != null && elements.length > 0) {

            final StringBuilder sb = new StringBuilder();
            for (JSElement<V> e : elements) {
                sb.append("if(document.getElementById('").append(e.getId()).append("') != null ){");
                sb.append("document.getElementById('").append(e.getId()).append("')");
                for (int i = 0; i < e.getProperty().length; i++) {
                    sb.append("['").append(e.getProperty()[i]).append("']");
                }
                if (e.getValue() instanceof String)
                    sb.append("=").append("'").append(e.getValue()).append("'").append("; ");
                else
                    sb.append("=").append(e.getValue()).append("; ");
                sb.append("}");
            }
            eval(sb.toString());
        }
    }

    @Override
    public void eval(String rawJsCode) {
        javascript("eval(\"" + rawJsCode + "\");");
    }

    @Override
    public void javascript(final String text) {
        if (webview != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Log.i(BIND, text);
                    webview.loadUrl("javascript:" + text);
                }
            });
        } else {
            Log.e(BIND, "Error WebView is null.");
        }
    }

    @Override
    public void reloadJoinedPage() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (webview != null) {
                    try {
                        webview.loadUrl("about:blank");
                        webview.clearCache(true);
                        build();
                        webview.loadUrl("javascript:window.location.reload(true)");
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(BIND, "Error WebView is null.");
                }
            }
        });
    }

    @Override
    public void reloadPage(String source) {
        if (webview != null) {
            webview.loadDataWithBaseURL(BASE_ASSET_FILE_URL, source, "text/html", "UTF-8", null);
        } else {
            Log.e(BIND, "Error WebView is null.");
        }
    }

    @Override
    public String getPageSource() {
        return this.htmlText;
    }
}