package com.gmail.at.ivanehreshi.utils;

import java.util.Iterator;

public class ConcatIter<T> implements Iterator<T> {
    Iterator<T> first;
    Iterator<T> second;

    public ConcatIter(Iterator<T> first, Iterator<T> second) {
        this.first = first;
        this.second = second;
    }


    @Override
    public boolean hasNext() {
        Iterator<T> it = currentIter();
        if(it == null)
            return false;

        if(!it.hasNext()) {
            if(it == first)
                first = null;
            else
                second = null;
            return hasNext();
        }

        return true;
    }

    @Override
    public T next() {
        Iterator<T> it = currentIter();
        T t = it.next();
        if(!it.hasNext()) {
            if(it == first)
                first = null;
            else
                second = null;
        }
        return t;
    }

    protected Iterator<T> currentIter() {
        if(first != null) {
            return first;
        }

        return second;
    }

    public Iterator<T> getFirst() {
        return first;
    }

    public Iterator<T> getSecond() {
        return second;
    }
}
