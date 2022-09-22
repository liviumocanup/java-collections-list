package com.endava.internship.collections;

import java.util.*;

public class StudentList implements List<Student> {
    private final static int DEFAULT_CAPACITY = 10;
    private int size = 0;
    private int capacity = DEFAULT_CAPACITY;
    private Student[] students;

    public StudentList() {
        students = new Student[DEFAULT_CAPACITY];
    }

    public StudentList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        this.capacity = capacity;
        students = new Student[capacity];
    }

    public StudentList(Collection<Student> studentsCollection) {
        addAll(studentsCollection);
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
    public Iterator<Student> iterator() {
        return new StudentIterator();
    }

    @Override
    public Student[] toArray() {
        return Arrays.copyOf(students, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] ts) {
        return (T[]) Arrays.copyOf(students, size, ts.getClass());
    }

    @Override
    public boolean add(Student student) {
        checkCapacity();
        students[size] = student;
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
        students = Arrays.copyOf(students, capacity);
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
            students[i] = null;
        }
        size = 0;
    }

    @Override
    public Student get(int i) {
        indexInRange(i, size - 1);
        return students[i];
    }

    private void indexInRange(int i, int upperLimit) {
        if (i < 0 || i > upperLimit) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Student set(int i, Student student) {
        indexInRange(i, size - 1);
        Student replacedStudent = students[i];
        students[i] = student;
        return replacedStudent;
    }

    @Override
    public void add(int i, Student student) {
        indexInRange(i, size);
        checkCapacity();

        for (int k = size; k > i; k--) {
            students[k] = students[k - 1];
        }
        students[i] = student;
        size++;
    }

    @Override
    public Student remove(int i) {
        indexInRange(i, size - 1);
        Student removedStudent = students[i];

        for (int k = i; k < size - 1; k++) {
            students[k] = students[k + 1];
        }
        students[size - 1] = null;
        size--;
        return removedStudent;
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
        if (o instanceof Student) {
            for (int i = start; i < end; i++) {
                if (students[i] != null && students[i].equals(o)) {
                    return i;
                }
            }
        } else if (o == null) {
            for (int i = start; i < end; i++) {
                if (students[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int lastIndexOfRange(Object o, int start, int end) {
        if (o instanceof Student) {
            for (int i = end - 1; i >= start; i--) {
                if (students[i] != null && students[i].equals(o)) {
                    return i;
                }
            }
        } else if (o == null) {
            for (int i = end - 1; i >= start; i--) {
                if (students[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    public ListIterator<Student> listIterator() {
        return new ListStudentIterator();
    }

    @Override
    public ListIterator<Student> listIterator(int i) {
        indexInRange(i, size - 1);
        return new ListStudentIterator(i);
    }

    @Override
    public List<Student> subList(int i, int i1) {
        if (i < 0 || i1 > size) {
            throw new IndexOutOfBoundsException();
        } else if (i > i1) {
            throw new IllegalArgumentException();
        }
        return new SubList<>(this, i, i1);
    }

    @Override
    public boolean addAll(Collection<? extends Student> collection) {
        Student[] c = collection.toArray(new Student[0]);
        int len = c.length;
        students = new Student[len];

        if (len == 0) {
            return false;
        }

        checkCapacity(len);

        for (int i = size; i < size + len; i++) {
            students[i] = c[i - size];
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
    public boolean addAll(int i, Collection<? extends Student> collection) {
        Student[] c = collection.toArray(new Student[0]);
        int len = c.length;
        if (len == 0) {
            return false;
        }

        indexInRange(i, size);
        checkCapacity(len);

        for (int times = 0; times < len; times++) {
            for (int k = size + len; k > i; k--) {
                students[k] = students[k - 1];
            }
        }
        for (int k = i; k < i + len; k++) {
            students[k] = c[k - i];
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
        StudentList toExclude = new StudentList();

        for (Student s : students) {
            for (Object o : collection) {
                if (s.equals(o)) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                toExclude.add(s);
            } else contained = false;
        }

        return removeAll(toExclude);
    }

    @Override
    public String toString() {
        return "StudentList{" + "size=" + size + ", capacity=" + capacity + ", students=" + Arrays.toString(Arrays.copyOf(students, size)) + '}';
    }

    private class StudentIterator implements Iterator<Student> {
        int cursor;

        StudentIterator() {
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public Student next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return students[cursor++];
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

    private class ListStudentIterator extends StudentIterator implements ListIterator<Student> {
        public ListStudentIterator() {
        }

        public ListStudentIterator(int index) {
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public Student previous() {
            if (cursor - 1 < 0) {
                throw new NoSuchElementException();
            }
            return students[cursor--];
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
        public void set(Student student) {
            if (cursor - 1 < 0) {
                throw new IllegalStateException();
            }

            StudentList.this.set(cursor - 1, student);
        }

        @Override
        public void add(Student student) {
            StudentList.this.add(cursor, student);
            cursor++;
        }
    }

    private class SubList<E> extends AbstractList<Student> {
        private final StudentList root;
        private final SubList<Student> parent;
        private final int offset;
        private int size;

        public SubList(StudentList root, int fromIndex, int toIndex) {
            this.root = root;
            this.parent = null;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
        }

        private SubList(SubList<Student> parent, int fromIndex, int toIndex) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + fromIndex;
            this.size = toIndex - fromIndex;
        }

        public Student set(int index, Student element) {
            indexInRange(index, size - 1);
            Student oldValue = root.students[offset + index];
            root.students[offset + index] = element;
            return oldValue;
        }

        public Student get(int index) {
            indexInRange(index, size - 1);
            return root.students[offset + index];
        }

        public int size() {
            return size;
        }

        public void add(int index, Student element) {
            indexInRange(index, size);
            root.add(offset + index, element);
            updateSize(1);
        }

        public Student remove(int index) {
            indexInRange(index, size - 1);
            Student result = root.remove(offset + index);
            updateSize(-1);
            return result;
        }

        @SuppressWarnings("unchecked")
        private void updateSize(int sizeChange) {
            SubList<Student> slist = (SubList<Student>) this;
            do {
                slist.size += sizeChange;
                slist = slist.parent;
            } while (slist != null);
        }

        public Student[] toArray() {
            return Arrays.copyOfRange(root.students, offset, offset + size);
        }

        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            return (T[]) Arrays.copyOfRange(root.students, offset, offset + size, a.getClass());
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

        @SuppressWarnings("unchecked")
        public List<Student> subList(int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > size) {
                throw new IndexOutOfBoundsException();
            } else if (fromIndex > toIndex) {
                throw new IllegalArgumentException();
            }
            return new SubList<Student>((SubList<Student>) this, fromIndex, toIndex);
        }
    }

}