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
        this.capacity = capacity;
        students = new Student[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Iterator<Student> iterator() {
        return new Itr();
    }

    @Override
    public Student[] toArray() {
        return Arrays.copyOf(students, size);
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return (T[]) Arrays.copyOf(students, size, ts.getClass());
    }

    @Override
    public boolean add(Student student) {
        addCapacity();
        students[size] = student;
        size++;
        return true;
    }

    private void addCapacity(){
        addCapacity(1);
    }
    private void addCapacity(int min){
        if(size+min >= capacity) {
            if ((capacity * 2) > (size + min)) {
                capacity *= 2;
            } else {
                capacity += min;
            }
            students = Arrays.copyOf(students, capacity);
        }
    }

    @Override
    public boolean remove(Object o) {
        int indx = indexOf(o);
        if(indx >= 0){
            remove(indx);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        for(int i=0; i<size; i++){
            students[i] = null;
        }
        size = 0;
    }

    @Override
    public Student get(int i) {
        indexInRange(i);
        return students[i];
    }

    private void indexInRange(int i){
        if(!(i>=0 && i<size))
            throw new IndexOutOfBoundsException();
    }

    @Override
    public Student set(int i, Student student) {
        indexInRange(i);
        Student replacedStudent = students[i];
        students[i] = student;
        return replacedStudent;
    }

    @Override
    public void add(int i, Student student) {
        indexInRange(i);
        addCapacity();

        for(int k=size; k>i; k--){
            students[k] = students[k-1];
        }
        students[i]=student;
        size++;
    }

    @Override
    public Student remove(int i) {
        indexInRange(i);
        Student removedStudent = students[i];

        for(int k=i; k<size-1; k++){
            students[k]=students[k+1];
        }
        students[size-1] = null;
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

    public int indexOfRange(Object o, int start, int end) {
        if(o instanceof Student)
            for(int i=start; i<end; i++){
                if(students[i].equals(o))
                    return i;
            }
        else if(o == null){
            for(int i=start; i<end; i++){
                if(students[i]==null)
                    return i;
            }
        }
        return -1;
    }

    public int lastIndexOfRange(Object o, int start, int end) {
        if(o instanceof Student)
            for(int i=end-1; i>=start; i--){
                if(students[i].equals(o))
                    return i;
            }
        else if(o == null){
            for(int i=end-1; i>=start; i--){
                if(students[i]==null)
                    return i;
            }
        }
        return -1;
    }

    public ListIterator<Student> listIterator() {
        return new ListItr();
    }

    @Override
    public ListIterator<Student> listIterator(int i) {
        indexInRange(i);
        return new ListItr(i);
    }

    @Override
    public List<Student> subList(int i, int i1) {
        if (i < 0 || i1 > size)
            throw new IndexOutOfBoundsException();
        else if(i>i1)
            throw new IllegalArgumentException();
        return new SubList<>(this, i, i1);
    }

    @Override
    public boolean addAll(Collection<? extends Student> collection) {
        Student[] c = collection.toArray(new Student[0]);
        int len = c.length;
        if(len == 0){
            return false;
        }

        addCapacity(len);

        for(int i=size; i < size+len; i++){
            students[i] = c[i-size];
        }
        size += len;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int i, Collection<? extends Student> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "StudentList{" +
                "size=" + size +
                ", capacity=" + capacity +
                ", students=" + Arrays.toString(students) +
                '}';
    }

    private class Itr implements Iterator<Student>{
        int cursor;

        public Itr() {
        }

        @Override
        public boolean hasNext() {
            return cursor<size;
        }

        @Override
        public Student next() {
            if (cursor >= size)
                throw new NoSuchElementException();
            return students[cursor++];
        }

        @Override
        public void remove() {
            if (cursor-1 < 0)
                throw new IllegalStateException();

            StudentList.this.remove(cursor-1);
            cursor--;
        }
    }

    private class ListItr extends Itr implements ListIterator<Student>{
        public ListItr() {
        }

        public ListItr(int index) {
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return cursor>0;
        }

        @Override
        public Student previous() {
            if (cursor-1 < 0)
                throw new NoSuchElementException();
            return students[cursor--];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public void set(Student student) {
            if (cursor-1 < 0)
                throw new IllegalStateException();

            StudentList.this.set(cursor-1, student);
        }

        @Override
        public void add(Student student) {
            StudentList.this.add(cursor, student);
            cursor++;
        }
    }

    private class SubList<E> extends AbstractList<Student>{
        private final StudentList root;
        private final StudentList.SubList<Student> parent;
        private final int offset;
        private int size;

        public SubList(StudentList root, int fromIndex, int toIndex) {
            this.root = root;
            this.parent = null;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
        }

        private SubList(StudentList.SubList<Student> parent, int fromIndex, int toIndex) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + fromIndex;
            this.size = toIndex - fromIndex;
        }

        public Student set(int index, Student element) {
            indexInRange(index);
            Student oldValue = root.students[offset + index];
            root.students[offset + index] = element;
            return oldValue;
        }

        public Student get(int index) {
            indexInRange(index);
            return root.students[offset + index];
        }

        public int size() {
            return size;
        }

        public void add(int index, Student element) {
            indexInRange(index);
            root.add(offset + index, element);
            updateSize(1);
        }

        public Student remove(int index) {
            indexInRange(index);
            Student result = root.remove(offset + index);
            updateSize(-1);
            return result;
        }

        private void updateSize(int sizeChange) {
            StudentList.SubList<Student> slist = (SubList<Student>) this;
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

        public List<Student> subList(int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > size)
                throw new IndexOutOfBoundsException();
            else if(fromIndex>toIndex)
                throw new IllegalArgumentException();
            return new SubList<Student>((SubList<Student>) this, fromIndex, toIndex);
        }
    }
}