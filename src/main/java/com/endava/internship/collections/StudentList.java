package com.endava.internship.collections;

import java.util.*;

public class StudentList<E> implements List<E> {
    private final static int DEFAULT_CAPACITY = 10;
    private int size = 0;
    private int capacity = DEFAULT_CAPACITY;
    private Object[] objectsArray;

    public StudentList() {
        objectsArray = new Object[DEFAULT_CAPACITY];
    }

    public StudentList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        this.capacity = capacity;
        objectsArray = new Object[capacity];
    }

    public StudentList(Collection<E> objectsCollection) {
        objectsArray = new Object[objectsCollection.size() + 10];
        addAll(objectsCollection);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new ObjectIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(objectsArray, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] ts) {
        return (T[]) Arrays.copyOf(objectsArray, size, ts.getClass());
    }

    @Override
    public boolean add(Object object) {
        checkCapacity();
        objectsArray[size] = object;
        size++;
        return true;
    }

    private void checkCapacity() {
        checkCapacity(1);
    }

    private void checkCapacity(int min) {
        if (size + min >= capacity) {
            if ((capacity * 2) > (size + min)) {
                capacity *= 2;
            } else {
                capacity += min;
            }
            resizeArray();
        }
    }

    private void resizeArray() {
        objectsArray = Arrays.copyOf(objectsArray, capacity);
    }

    @Override
    public boolean remove(Object o) {
        int indx = indexOf(o);
        if (indx >= 0) {
            remove(indx);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            objectsArray[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int i) {
        indexInRange(i, size - 1);
        return (E) objectsArray[i];
    }

    private void indexInRange(int i, int upperLimit) {
        if (i < 0 || i > upperLimit) {
            throw new IndexOutOfBoundsException();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E set(int i, E object) {
        indexInRange(i, size - 1);
        Object replacedObject = objectsArray[i];
        objectsArray[i] = object;
        return (E) replacedObject;
    }

    @Override
    public void add(int i, Object object) {
        indexInRange(i, size);
        checkCapacity();

        for (int k = size; k > i; k--) {
            objectsArray[k] = objectsArray[k - 1];
        }
        objectsArray[i] = object;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int i) {
        indexInRange(i, size - 1);
        Object removedObject = objectsArray[i];

        for (int k = i; k < size - 1; k++) {
            objectsArray[k] = objectsArray[k + 1];
        }
        objectsArray[size - 1] = null;
        size--;
        return (E) removedObject;
    }

    @Override
    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOfRange(o, 0, size);
    }

    private int indexOfRange(Object o, int start, int end) {
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (objectsArray[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                if (objectsArray[i] != null && objectsArray[i].equals(o)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int lastIndexOfRange(Object o, int start, int end) {
        if (o == null) {
            for (int i = end - 1; i >= start; i--) {
                if (objectsArray[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = end - 1; i >= start; i--) {
                if (objectsArray[i] != null && objectsArray[i].equals(o)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public ListIterator<E> listIterator() {
        return new ListObjectIterator();
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        indexInRange(i, size - 1);
        return new ListObjectIterator(i);
    }

    @Override
    public List<E> subList(int i, int i1) {
        if (i < 0 || i1 > size) {
            throw new IndexOutOfBoundsException();
        } else if (i > i1) {
            throw new IllegalArgumentException();
        }
        return new SubList<>(this, i, i1);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Object[] c = collection.toArray(new Object[0]);
        int len = c.length;

        if (len == 0) {
            return false;
        }

        checkCapacity(len);

        for (int i = size; i < size + len; i++) {
            objectsArray[i] = c[i - size];
        }
        size += len;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object o : collection) {
            if (indexOf(o) < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> collection) {
        Object[] c = collection.toArray(new Object[0]);
        int len = c.length;
        if (len == 0) {
            return false;
        }

        indexInRange(i, size);
        checkCapacity(len);

        for (int times = 0; times < len; times++) {
            for (int k = size + len; k > i; k--) {
                objectsArray[k] = objectsArray[k - 1];
            }
        }
        for (int k = i; k < i + len; k++) {
            objectsArray[k] = c[k - i];
        }

        size += len;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean listChanged = false;
        for (Object o : collection) {
            if (indexOf(o) >= 0) {
                remove(o);
                listChanged = true;
            }
        }
        return listChanged;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean contained = false;
        StudentList<Object> toExclude = new StudentList<>();

        for (Object obj : objectsArray) {
            for (Object collObj : collection) {
                if (Objects.equals(obj, collObj)) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                toExclude.add(obj);
            } else contained = false;
        }

        return removeAll(toExclude);
    }

    @Override
    public String toString() {
        return "StudentList{" + "size=" + size + ", capacity=" + capacity + ", students=" + Arrays.toString(Arrays.copyOf(objectsArray, size)) + '}';
    }

    private class ObjectIterator implements Iterator<E> {
        int cursor;

        ObjectIterator() {
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (E) objectsArray[cursor++];
        }

        @Override
        public void remove() {
            if (cursor - 1 < 0) {
                throw new IllegalStateException();
            }

            StudentList.this.remove(cursor - 1);
            cursor--;
        }
    }

    private class ListObjectIterator extends ObjectIterator implements ListIterator<E> {
        public ListObjectIterator() {
        }

        public ListObjectIterator(int index) {
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E previous() {
            if (cursor - 1 < 0) {
                throw new NoSuchElementException();
            }
            return (E) objectsArray[cursor--];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void set(E element) {
            if (cursor - 1 < 0) {
                throw new IllegalStateException();
            }

            StudentList.this.set(cursor - 1, element);
        }

        @Override
        public void add(E element) {
            StudentList.this.add(cursor, element);
            cursor++;
        }
    }

    private class SubList<E> extends AbstractList<E> {
        private final StudentList<E> root;
        private final SubList<E> parent;
        private final int offset;
        private int size;

        public SubList(StudentList<E> root, int fromIndex, int toIndex) {
            this.root = root;
            this.parent = null;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
        }

        private SubList(SubList<E> parent, int fromIndex, int toIndex) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + fromIndex;
            this.size = toIndex - fromIndex;
        }

        @SuppressWarnings("unchecked")
        public E set(int index, E element) {
            indexInRange(index, size - 1);
            Object oldValue = root.objectsArray[offset + index];
            root.objectsArray[offset + index] = element;
            return (E) oldValue;
        }

        @SuppressWarnings("unchecked")
        public E get(int index) {
            indexInRange(index, size - 1);
            return (E) root.objectsArray[offset + index];
        }

        public int size() {
            return size;
        }

        public void add(int index, E element) {
            indexInRange(index, size);
            root.add(offset + index, element);
            updateSize(1);
        }

        public E remove(int index) {
            indexInRange(index, size - 1);
            E result = root.remove(offset + index);
            updateSize(-1);
            return result;
        }

        private void updateSize(int sizeChange) {
            SubList<E> slist = this;
            do {
                slist.size += sizeChange;
                slist = slist.parent;
            } while (slist != null);
        }

        public Object[] toArray() {
            return Arrays.copyOfRange(root.objectsArray, offset, offset + size);
        }

        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            return (T[]) Arrays.copyOfRange(root.objectsArray, offset, offset + size, a.getClass());
        }

        public int indexOf(Object o) {
            int index = root.indexOfRange(o, offset, offset + size);
            return index >= 0 ? index - offset : -1;
        }

        public int lastIndexOf(Object o) {
            int index = root.lastIndexOfRange(o, offset, offset + size);
            return index >= 0 ? index - offset : -1;
        }

        public boolean contains(Object o) {
            return indexOf(o) >= 0;
        }

        public List<E> subList(int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > size) {
                throw new IndexOutOfBoundsException();
            } else if (fromIndex > toIndex) {
                throw new IllegalArgumentException();
            }
            return new SubList<>(this, fromIndex, toIndex);
        }
    }

}