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
package app.common.util.droid.web.binding.library.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import app.common.util.droid.web.binding.library.AbstractHandler;
import app.common.util.droid.web.binding.library.AnnotationResult;
import app.common.util.droid.web.binding.library.Bind;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface BindHtmlValue {

    String parameter();

    int R_val() default -1;

    class ReflectionHandler extends AbstractHandler {
        public static ReflectionHandler instance() {
            return new ReflectionHandler();
        }

        public AnnotationResult completeAnnotation(Bind instance, InvocationHandler handler, Map<String, Object> args) throws Throwable {

            String parameter = (String) handler.invoke(handler, find("parameter", BindHtmlValue.class), null);
            Field field = (Field) args.get("field");
            Object jsListener = args.get("jsListener");
            Object value = field.get(jsListener);

            if (value == null) {
                Method mR_value = find("R_val", BindHtmlValue.class);
                Integer rin = (Integer) handler.invoke(handler, mR_value, null);
                if (rin.intValue() > -1) {
                    field.set(jsListener, instance.getActivity().getResources().getString(rin.intValue()));
                    value = rin;
                }
            }

            AnnotationResult ar = AnnotationResult.Provider.create();

            if (parameter != null && value != null) {
                ar.putInMap(parameter, value);
            }

            return ar;
        }

        public boolean isBindAnnotation(InvocationHandler handler, Object args) throws Throwable {
            return BindHtmlValue.class.equals(getAnnotationType(handler));
        }
    }
}
