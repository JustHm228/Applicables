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

public sealed interface TargetHolder<T extends Targetable<T>> extends Targetable.Delegated<T>
		permits TargetHolder.Delegated, TargetHolder.Immutable, TargetHolder.Mutable {

	static <T extends Targetable<T>> TargetHolder.Immutable<T> ofImmutable(final T target) {

		return new StaticTargetHolder<>(target);
	}

	static <T extends Targetable<T>> TargetHolder.Mutable<T> ofMutable(final T target) {

		return new DynamicTargetHolder<>(target);
	}

	@FunctionalInterface()
	non-sealed interface Delegated<T extends Targetable<T>> extends TargetHolder<T> {

		TargetHolder<T> getTargetHolder();

		@Override()
		default T getTarget() {

			return getTargetHolder().getTarget();
		}
	}

	@FunctionalInterface()
	non-sealed interface Immutable<T extends Targetable<T>> extends TargetHolder<T> {

		@FunctionalInterface()
		interface Delegated<T extends Targetable<T>> extends Immutable<T>, TargetHolder.Delegated<T> {

		}
	}

	non-sealed interface Mutable<T extends Targetable<T>> extends TargetHolder<T> {

		void setTarget(final T target);

		@FunctionalInterface()
		interface Delegated<T extends Targetable<T>> extends Mutable<T>, TargetHolder.Delegated<T> {

			@Override()
			TargetHolder.Mutable<T> getTargetHolder();

			@Override()
			default void setTarget(final T target) {

				getTargetHolder().setTarget(target);
			}
		}
	}
}
