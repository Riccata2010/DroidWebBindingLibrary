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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"all"})
public abstract class AbstractHandler {

    public final static Logger LOGGER = Logger.getLogger(AbstractHandler.class.getName());

    public static InvocationHandler getInvocationHandler(final Annotation annotation) {
        InvocationHandler handler = null;
        if (Proxy.isProxyClass(annotation.getClass())) {
            handler = Proxy.getInvocationHandler(annotation);
        }
        return handler;
    }

    public abstract AnnotationResult completeAnnotation(Bind instance, InvocationHandler handler, Map<String, Object> args) throws Throwable;

    public abstract boolean isBindAnnotation(InvocationHandler handler, Object args) throws Throwable;

    protected Class<? extends Annotation> getAnnotationType(InvocationHandler handler) throws Throwable {
        Class $class = (Class) handler.invoke(handler, annotationType(), null);
        return $class;
    }

    protected Method annotationType() throws NoSuchMethodException {
        return Annotation.class.getMethod("annotationType", null);
    }

    protected Method find(final String name, final Class<? extends Annotation> $class) {
        try {
            return locate(name, $class);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, exc.toString(), exc);
            return null;
        }
    }

    protected Method find(final String name, final Class<? extends Annotation> $class, Class... params) {
        try {
            return locate(name, $class, params);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, exc.toString(), exc);
            return null;
        }
    }

    protected Method locate(final String name, final Class<? extends Annotation> $class) throws NoSuchMethodException {
        return $class.getMethod(name, null);
    }

    protected Method locate(final String name, final Class<? extends Annotation> $class, Class... params) throws NoSuchMethodException {
        return $class.getMethod(name, params);
    }
}
