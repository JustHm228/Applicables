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

package com.github.justhm228.applicables.context;

import com.github.justhm228.applicables.Applicable;
import com.github.justhm228.applicables.target.TargetHolder;
import com.github.justhm228.applicables.target.Targetable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleApplicationContext<T extends Targetable<T>> implements ApplicationContext<T> {

	protected final Applicable<T> applied;

	protected final TargetHolder<T> targetHolder;

	protected final List<Consumer<ApplicationContext<T>>> finishHooks = new ArrayList<>(1);

	protected boolean finished = false;

	public SimpleApplicationContext(final Applicable<T> applied, final TargetHolder<T> targetHolder) {

		this(applied, targetHolder, false);
	}

	protected SimpleApplicationContext(final Applicable<T> applied, final TargetHolder<T> targetHolder, final boolean applies) {

		super();
		this.applied = applied;
		this.targetHolder = targetHolder;

		if (applies) {

			applied.apply(this);
		}
	}

	@Override()
	public State getState() {

		return finished ? State.FINISHED : State.RUNNING;
	}

	@Override()
	public Applicable<T> getApplied() {

		return applied;
	}

	@Override()
	public TargetHolder<T> getTargetHolder() {

		return targetHolder;
	}

	@Override()
	public T getTarget() {

		return targetHolder.getTarget();
	}

	@Override()
	public ApplicationContext<T> apply(final Applicable<T> applicable) {

		return targetHolder.apply(applicable);
	}

	@Override()
	public void addFinishHook(final Consumer<ApplicationContext<T>> finishHook) {

		finishHooks.add(finishHook);
	}

	@Override()
	public void finish() {

		try {

			for (final Consumer<ApplicationContext<T>> finishHook : finishHooks) {

				finishHook.accept(this);
			}

		} finally {

			finished = true;
		}
	}

	@Override()
	public void close() {

		finish();
	}
}
