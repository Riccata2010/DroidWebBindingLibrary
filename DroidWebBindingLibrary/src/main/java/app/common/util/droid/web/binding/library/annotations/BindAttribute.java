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
import java.lang.reflect.InvocationHandler;
import java.util.Map;

import app.common.util.droid.web.binding.library.AbstractHandler;
import app.common.util.droid.web.binding.library.AnnotationResult;
import app.common.util.droid.web.binding.library.Bind;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface BindAttribute {

    String var();

    String val() default "";

    int R_val() default -1;

    class ReflectionHandler extends AbstractHandler {
        public static ReflectionHandler instance() {
            return new ReflectionHandler();
        }

        public AnnotationResult completeAnnotation(Bind instance, InvocationHandler handler, Map<String, Object> args) throws Throwable {

            String var = (String) handler.invoke(handler, find("var", BindAttribute.class), null);
            String val = (String) handler.invoke(handler, find("val", BindAttribute.class), null);
            Integer R_val = (Integer) handler.invoke(handler, find("R_val", BindAttribute.class), null);

            AnnotationResult result = AnnotationResult.Provider.create();

            if (R_val.intValue() > -1)
                result.putInMap(var, R_val);
            else result.putInMap(var, val);

            return result;
        }

        public boolean isBindAnnotation(InvocationHandler handler, Object args) throws Throwable {
            return BindHtmlReference.class.equals(getAnnotationType(handler));
        }
    }
}
