/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.fge.jackson.com.google.common.base;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Equivalence applied on functional result.
 *
 * @author Bob Lee
 * @author <a href="mailto:25544967+soberich@users.noreply.github.com">soberich</a> on 29-Nov-18
 * @since 1.11
 */
final class FunctionalEquivalence<F, T> extends Equivalence<F>
        implements Serializable {

    private static final long serialVersionUID = 0;

    private final Function<F, ? extends T> function;
    private final Equivalence<T> resultEquivalence;

    FunctionalEquivalence(
            Function<F, ? extends T> function, Equivalence<T> resultEquivalence) {
        if (function == null) {
            throw new NullPointerException();
        }
        this.function = function;
        if (resultEquivalence == null) {
            throw new NullPointerException();
        }
        this.resultEquivalence = resultEquivalence;
    }

    @Override protected boolean doEquivalent(F a, F b) {
        return resultEquivalence.equivalent(function.apply(a), function.apply(b));
    }

    @Override protected int doHash(F a) {
        return resultEquivalence.hash(function.apply(a));
    }

    @Override public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof FunctionalEquivalence) {
            FunctionalEquivalence<?, ?> that = (FunctionalEquivalence<?, ?>) obj;
            return function.equals(that.function)
                    && resultEquivalence.equals(that.resultEquivalence);
        }
        return false;
    }

    @Override public int hashCode() {
        return Arrays.hashCode(new Object[]{function, resultEquivalence});
    }

    @Override public String toString() {
        return resultEquivalence + ".onResultOf(" + function + ")";
    }
}
