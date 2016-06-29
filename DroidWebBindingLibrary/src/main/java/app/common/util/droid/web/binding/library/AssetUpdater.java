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

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AssetUpdater {

    public static final Map<String, String> LOCAL_MIME_MAP = new HashMap<>();

    static {
        LOCAL_MIME_MAP.put(".apk", "application/vnd.android.package-archive");
        LOCAL_MIME_MAP.put(".txt", "text/plain");
        LOCAL_MIME_MAP.put(".csv", "text/csv");
        LOCAL_MIME_MAP.put(".xml", "text/xml");
        LOCAL_MIME_MAP.put(".htm", "text/html");
        LOCAL_MIME_MAP.put(".html", "text/html");
        LOCAL_MIME_MAP.put(".php", "text/php");
        LOCAL_MIME_MAP.put(".png", "image/png");
        LOCAL_MIME_MAP.put(".gif", "image/gif");
        LOCAL_MIME_MAP.put(".jpg", "image/jpg");
        LOCAL_MIME_MAP.put(".jpeg", "image/jpeg");
        LOCAL_MIME_MAP.put(".bmp", "image/bmp");
        LOCAL_MIME_MAP.put(".mp3", "audio/mp3");
        LOCAL_MIME_MAP.put(".zip", "application/zip");
        LOCAL_MIME_MAP.put(".3gp", "video/3gpp");
        LOCAL_MIME_MAP.put(".mpeg", "video/mpeg");
    }

    public static String getMime(File f) {
        if (f == null || f.isDirectory()) {
            return "file/*";
        }
        String n = f.getName();
        if (n.length() > 4) {
            int p = n.lastIndexOf(".");
            if (p > 0) {
                n = n.substring(p);
                return LOCAL_MIME_MAP.get(n);
            }
        }
        return "file/*";
    }

    public static String repalceAll(Context context, Map<String, ?> map_value, String file_name) throws Exception {
        try {
            if (context == null) {
                throw new Exception("Error Context is null.");
            }
            if (file_name == null) {
                throw new Exception("Error File Name is null.");
            }

            String txt = getTextFromAsset(context, file_name);

            if (map_value != null) {
                Iterator<String> keys = map_value.keySet().iterator();
                while (keys.hasNext()) {

                    String variable = keys.next().toString();
                    Object o = map_value.get(variable);

                    if (o == null) {
                        continue;
                    }

                    if (o instanceof char[]) {
                        String val = String.valueOf((char[]) o);
                        txt = replaceAll(variable, val, txt);
                    } else if (o instanceof String) {
                        String val = o.toString();
                        txt = replaceAll(variable, val, txt);
                    } else if (o instanceof Integer) {
                        txt = replaceAll(context, variable, ((Integer) o).intValue(), txt);
                    }
                }
            }
            return txt;
        } catch (Exception exc) {
            throw new Exception("Erron in repalceAll MAP EXC:" + exc);
        }
    }

    public static String replaceAll(Context context, String variable, String value, String file_name) throws Exception {
        try {
            String txt = getTextFromAsset(context, file_name);
            return replaceAll(variable, value, txt);
        } catch (Exception exc) {
            throw new Exception("Erron in replaceAll 1 EXC:" + exc);
        }
    }

    public static String replaceAll(String variable, String value, String txt) {
        if (variable == null || variable.length() == 0)
            return txt;
        if (value == null)
            return txt;
        variable = escapeVar(variable);
        return txt.replace(variable, value);
    }

    public static String replaceAll(Context c, String variable, int Rvalue, String txt) {
        if (variable == null || variable.length() == 0)
            return txt;
        if (Rvalue < 0)
            return txt;
        variable = escapeVar(variable);
        String m = c.getResources().getString(Rvalue);
        return txt.replace(variable, m);
    }

    public static String escapeVar(String variable) {
        if (!variable.startsWith("${") && !variable.endsWith("}")) {
            return "${" + variable + "}";
        }
        return variable;
    }

    public static String escapeVarRegEx(String variable) {
        if (variable.startsWith("${") && variable.endsWith("}")) {
            variable = variable.substring(variable.indexOf("{") + 1, variable.indexOf("}"));
        }
        return "\\$\\{" + variable + "\\}";
    }

    public static List<String> getRowsFromAsset(Context context, String asset_file) {

        List<String> al = new ArrayList<>();
        BufferedReader br = null;
        try {

            InputStream is_data = context.getAssets().open(asset_file);
            br = new BufferedReader(new InputStreamReader(is_data));

            String line;
            while ((line = br.readLine()) != null) {
                al.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
            }
        }
        return al;
    }

    public static String getTextFromAsset(Context context, String asset_file) {
        List<String> rows = getRowsFromAsset(context, asset_file);
        String ris = "";
        if (rows != null) {
            for (String r : rows) {
                ris += r + "\n";
            }
        }
        return ris;
    }
}
