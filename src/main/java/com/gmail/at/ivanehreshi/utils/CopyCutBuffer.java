package com.gmail.at.ivanehreshi.utils;

public class CopyCutBuffer<T> {
    private T element;
    private State state;

    public CopyCutBuffer() {
        element = null;
        state = State.EMPTY;
    }

    public void copy(T t) {
        element = t;
        state = State.COPY;
    }

    public void cut(T t) {
        element = t;
        state = State.CUT;
    }

    public T pop() {
        switch (state) {
            case COPY:
                return element;
            case CUT:
                T temp = element;
                state = State.EMPTY;
                element = null;
                return temp;
        }
        throw new IndexOutOfBoundsException("No element to pop");
    }

    public boolean hasElement() {
        return state != State.EMPTY && element != null;
    }

    public enum State {
        COPY,
        CUT,
        EMPTY
    }
}
