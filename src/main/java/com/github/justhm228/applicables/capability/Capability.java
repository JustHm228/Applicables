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

package com.github.justhm228.applicables.capability;

import com.github.justhm228.applicables.Applicable;
import com.github.justhm228.applicables.context.ApplicationContext;
import com.github.justhm228.applicables.target.Targetable;
import java.util.function.Consumer;

public interface Capability<T extends Targetable<T>> extends Applicable<T> {

	static <T extends Targetable<T>> Capability<T> of(final Consumer<ApplicationContext<T>> applier, final Consumer<ApplicationContext<T>> finisher) {

		return new Capability<>() {

			@Override()
			public void onApply(final ApplicationContext<T> ctx) {

				applier.accept(ctx);
			}

			@Override()
			public void onFinish(final ApplicationContext<T> ctx) {

				finisher.accept(ctx);
			}
		};
	}

	static <T extends Targetable<T>> Capability.Instant<T> ofInstant(final Consumer<ApplicationContext<T>> applier, final Consumer<ApplicationContext<T>> finisher) {

		return new Capability.Instant<>() {

			@Override()
			public void onApply(final ApplicationContext<T> ctx) {

				applier.accept(ctx);
			}

			@Override()
			public void onFinish(final ApplicationContext<T> ctx) {

				finisher.accept(ctx);
			}
		};
	}

	void onApply(final ApplicationContext<T> ctx);

	void onFinish(final ApplicationContext<T> ctx);

	@Override()
	default void apply(final ApplicationContext<T> ctx) {

		try {

			onApply(ctx);

		} finally {

			ctx.addFinishHook(this::onFinish);
		}
	}

	@FunctionalInterface()
	interface Delegated<T extends Targetable<T>> extends Capability<T>, Applicable.Delegated<T> {

		Capability<T> getCapability();

		@Override()
		default Capability<T> getApplicable() {

			return getCapability();
		}

		@Override()
		default void onApply(final ApplicationContext<T> ctx) {

			getCapability().onApply(ctx);
		}

		@Override()
		default void onFinish(final ApplicationContext<T> ctx) {

			getCapability().onFinish(ctx);
		}

		@Override()
		default void apply(final ApplicationContext<T> ctx) {

			Capability.super.apply(ctx);
		}
	}

	interface Instant<T extends Targetable<T>> extends Capability<T> {

		@Override()
		default void apply(final ApplicationContext<T> ctx) {

			try (ctx) {

				Capability.super.apply(ctx);
			}
		}

		@FunctionalInterface()
		interface Delegated<T extends Targetable<T>> extends Capability.Delegated<T>, Capability.Instant<T> {

			@Override()
			default void apply(final ApplicationContext<T> ctx) {

				Capability.Instant.super.apply(ctx);
			}
		}
	}
}
