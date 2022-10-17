package com.txscore.utils.m;

/**
 * @Copyright © 2019 sanbo Inc. All rights reserved.
 * @Description: TODO
 * @Version: 1.0
 * @Create: 2019/1/24 22:52
 * @Author: sanbo
 */
public class VolatileBox<T> {

    private volatile T mValue;

    public VolatileBox() {
    }

    public VolatileBox(T value) {
        set(value);
    }

    public T get() {
        return mValue;
    }

    public void set(T value) {
        mValue = value;
    }

    public boolean isNull() {
        return mValue == null;
    }

    public boolean notNull() {
        return mValue != null;
    }

    public void setAndNotify(T value) {
        mValue = value;
        synchronized (this) {
            notify();
        }
    }

    public T blockedGet() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return mValue;
    }

    public T blockedGetOrThrow(Class<? extends RuntimeException> exception) {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                try {
                    throw exception.newInstance();
                } catch (InstantiationException e1) {
                    throw new RuntimeException(e1);
                } catch (IllegalAccessException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
        return mValue;
    }
}
