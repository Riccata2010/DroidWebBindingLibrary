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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AnnotationResult {

    String getStringValue();

    void setStringValue(String value);

    int getIntValue();

    void setIntValue(int value);

    Map<String, Object> getMap();

    void setMap(Map<String, Object> map);

    void putInMap(String k, Object v);

    Object getFromMap(String k);

    void addInList(Object o);

    Object getFromList(int i);

    List<Object> getList();

    int getListSize();

    class Provider {
        public static AnnotationResult create() {
            return new AnnotationResult() {
                String value = null;
                int ivalue = -1;
                Map<String, Object> map = null;
                List<Object> list = null;

                @Override
                public String getStringValue() {
                    return this.value;
                }

                @Override
                public void setStringValue(String value) {
                    this.value = value;
                }

                @Override
                public int getIntValue() {
                    return this.ivalue;
                }

                @Override
                public void setIntValue(int ivalue) {
                    this.ivalue = ivalue;
                }

                @Override
                public Map<String, Object> getMap() {
                    return this.map;
                }

                @Override
                public void setMap(Map<String, Object> map) {
                    this.map = map;
                }

                @Override
                public void putInMap(String k, Object v) {
                    if (this.map == null) this.map = new HashMap<String, Object>();
                    this.map.put(k, v);
                }

                @Override
                public Object getFromMap(String k) {
                    return this.map.get(k);
                }

                @Override
                public void addInList(Object o) {
                    if (list == null) list = new ArrayList<Object>();
                    list.add(o);
                }

                @Override
                public Object getFromList(int i) {
                    return list.get(i);
                }

                @Override
                public List<Object> getList() {
                    return list;
                }

                @Override
                public int getListSize() {
                    return (list == null) ? 0 : list.size();
                }
            };
        }
    }
}
