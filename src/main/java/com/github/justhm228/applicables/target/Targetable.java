/*
 * The MIT License
 *
 * Copyright (c) 2025 JustHuman228
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.justhm228.applicables.target;

import com.github.justhm228.applicables.Applicable;
import com.github.justhm228.applicables.context.ApplicationContext;

@FunctionalInterface()
public interface Targetable<T extends Targetable<T>> {

	static <T extends Targetable.Adapted<T, O>, O> Targetable.Adapted<T, O> adapt(final O o, final TargetAdapter<T, O> adapter) {

		return adapter.adapt(o);
	}

	ApplicationContext<T> apply(final Applicable<T> applicable);

	interface Adapted<T extends Adapted<T, O>, O> extends Targetable<T> {

		O getTarget();
	}

	@FunctionalInterface()
	interface Delegated<T extends Targetable<T>> extends Targetable<T> {

		T getTarget();

		@Override()
		default ApplicationContext<T> apply(final Applicable<T> applicable) {

			return getTarget().apply(applicable);
		}
	}
}
