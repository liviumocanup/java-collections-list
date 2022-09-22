package com.endava.internship.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class StudentListTest {
    private StudentList studentList;
    private final Student student1 = new Student("Bobby", LocalDate.of(2001, 5, 26), "loser");
    private final Student student2 = new Student("Bob", LocalDate.of(2000, 5, 26), "chad");
    private final Student student3 = new Student("Val", LocalDate.of(2002, 5, 26), "nothing");
    private final Student student4 = new Student("George", LocalDate.of(2000, 5, 26), "learner");
    private final Student student5 = new Student("Gaven", LocalDate.of(2001, 5, 26), "introverted");

    @BeforeEach
    void setUp() {
        studentList = new StudentList();
    }

    @Test
    void testConstructorThrowsExceptionForNegativeCapacity() {
        Throwable exceptionThatWasThrown = assertThrows(IllegalArgumentException.class, () -> new StudentList(-2));
        assertEquals(exceptionThatWasThrown.getMessage(), "Illegal Capacity: " + "-2");
    }

    @Test
    void testConstructorZeroCapacity() {
        studentList = new StudentList(0);
        assertEquals(0, studentList.size());
    }

    @Test
    void testConstructorForCollection() {
        List<Student> s = new ArrayList<>();
        s.add(student1);
        s.add(student2);

        studentList = new StudentList(s);

        assertEquals(2, studentList.size());
        assertArrayEquals(s.toArray(), studentList.toArray());
    }

    @Test
    void testDefaultConstructorSize() {
        assertEquals(0, studentList.size());
    }

    @Test
    void testSizeAfterAdd() {
        studentList.add(student1);
        studentList.add(student1);
        studentList.add(student2);

        assertEquals(3, studentList.size());
    }

    @Test
    void testSizeAfterAddAndRemove() {
        studentList.add(student1);
        studentList.remove(student1);

        assertEquals(0, studentList.size());
    }

    @Test
    void testIsEmptyAfterDefaultConstructor() {
        assertEquals(0, studentList.size());
        assertTrue(studentList.isEmpty());
    }

    @Test
    void testIsEmptyAfterAdd() {
        studentList.add(student1);

        assertFalse(studentList.isEmpty());
    }

    @Test
    void testIsEmptyAfterAddAndRemove() {
        studentList.add(student1);
        studentList.add(student2);
        studentList.remove(student1);
        studentList.remove(student2);

        assertEquals(0, studentList.size());
        assertTrue(studentList.isEmpty());
    }

    @Test
    void testContainsWithNull() {
        assertFalse(studentList.contains(null));
    }

    @Test
    void testContainsOnEmptyList() {
        assertFalse(studentList.contains(student1));
    }

    @Test
    void testContainsOnAddedObjectsWithoutNull() {
        studentList.add(student1);
        studentList.add(student2);

        assertTrue(studentList.contains(student1));
        assertTrue(studentList.contains(student2));
        assertFalse(studentList.contains(null));
    }

    @Test
    void testContainsOnAddedNull() {
        studentList.add(null);

        assertTrue(studentList.contains(null));
    }

    @Test
    void testContainsOnRemovedElement() {
        studentList.add(null);
        studentList.remove(null);
        studentList.add(student1);
        studentList.remove(student1);

        assertFalse(studentList.contains(null));
        assertFalse(studentList.contains(student1));
    }

    @Nested
    class TestInnerClassIterator {

        @Test
        void testHasNextIfNextElementPresent() {
            studentList.add(student1);
            Iterator<Student> iterator = studentList.iterator();

            assertTrue(iterator.hasNext());
        }

        @Test
        void testHasNextIfNoNextElement() {
            Iterator<Student> iterator = studentList.iterator();

            assertFalse(iterator.hasNext());
        }

        @Test
        void testNextIfNoNextElement() {
            Iterator<Student> iterator = studentList.iterator();

            assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        void testNextIfNextElementPresent() {
            studentList.add(student1);
            Iterator<Student> iterator = studentList.iterator();

            assertEquals(student1, iterator.next());
            assertFalse(iterator.hasNext());
        }

        @Test
        void testNextIfMoreThanOneElement() {
            studentList.add(student1);
            studentList.add(student2);
            Iterator<Student> iterator = studentList.iterator();

            assertEquals(student1, iterator.next());
            assertEquals(student2, iterator.next());
            assertFalse(iterator.hasNext());
        }

        @Test
        void testRemoveIfNoElements() {
            Iterator<Student> iterator = studentList.iterator();

            assertThrows(IllegalStateException.class, iterator::remove);
        }

        @Test
        void testRemoveLastObject() {
            studentList.add(student1);
            studentList.add(student2);

            Iterator<Student> iterator = studentList.iterator();
            iterator.next();
            iterator.next();

            iterator.remove();
            assertThat(studentList).hasSize(1)
                    .contains(student1)
                    .doesNotContain(student2);
        }

        @Test
        void testRemoveForObjectInBetween() {
            studentList.add(student1);
            studentList.add(student2);

            Iterator<Student> iterator = studentList.iterator();
            iterator.next();

            iterator.remove();
            assertThat(studentList).hasSize(1)
                    .contains(student2)
                    .doesNotContain(student1);
        }
    }

    @Test
    void testToArrayOnEmptyList() {
        assertArrayEquals(new Student[0], studentList.toArray());
    }

    @Test
    void testToArrayOnTwoElementsArray() {
        Student[] s = new Student[2];
        s[0] = student1;
        s[1] = student2;

        studentList.add(student1);
        studentList.add(student2);
        List<Student> a = new ArrayList<>();
        a.add(student1);
        a.add(student2);

        assertArrayEquals(s, studentList.toArray());
        assertArrayEquals(a.toArray(), studentList.toArray());
    }

    @Test
    void testToArrayWithGenericArrayOnEmptyList() {
        assertArrayEquals(new Student[0], studentList.toArray(new Student[0]));
    }

    @Test
    void testToArrayWithGenericArrayOnTwoElementsArray() {
        Student[] s = new Student[2];
        s[0] = student1;
        s[1] = student2;

        studentList.add(student1);
        studentList.add(student2);
        List<Student> a = new ArrayList<>();
        a.add(student1);
        a.add(student2);

        assertArrayEquals(s, studentList.toArray(new Student[5]));
        assertArrayEquals(a.toArray(), studentList.toArray(new Student[0]));
    }

    @Test
    void testAddOneElementInOneCapacityList() {
        studentList = new StudentList(1);
        studentList.add(student1);

        List<Student> s = new ArrayList<>();
        s.add(student1);

        assertThat(studentList).hasSize(1)
                .contains(student1);
        assertArrayEquals(s.toArray(), studentList.toArray());
    }

    @Test
    void testAddDuplicateAndNull() {
        studentList.add(student1);
        studentList.add(student1);
        studentList.add(null);

        List<Student> s = new ArrayList<>();
        s.add(student1);
        s.add(student1);
        s.add(null);

        assertThat(studentList).hasSize(3)
                .contains(student1)
                .contains((Student) null);
        assertArrayEquals(s.toArray(), studentList.toArray());
    }

    @Test
    void testAddTwoElementsInOneCapacityList() {
        studentList = new StudentList(1);
        studentList.add(student1);
        studentList.add(student2);

        List<Student> s = new ArrayList<>();
        s.add(student1);
        s.add(student2);

        assertThat(studentList).hasSize(2)
                .contains(student1)
                .contains(student2);
        assertArrayEquals(s.toArray(), studentList.toArray());
    }

    @Test
    void testRemoveForPresentElement() {
        studentList.add(student2);

        assertTrue(studentList.remove(student2));
    }

    @Test
    void testRemoveInEmptyList() {
        assertFalse(studentList.remove(student2));
    }

    @Test
    void testRemoveNonPresentElement() {
        studentList.add(student2);

        assertFalse(studentList.remove(student1));
    }

    @Test
    void testRemoveNonPresentNull() {
        studentList.add(student2);

        assertFalse(studentList.remove(null));
    }

    @Test
    void testRemovePresentNull() {
        studentList.add(null);

        assertTrue(studentList.remove(null));
    }

    @Test
    void testRemoveDuplicateAndNull() {
        studentList.add(student1);
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(null);

        assertTrue(studentList.remove(student1));
        assertTrue(studentList.remove(student1));
        assertTrue(studentList.remove(null));
        assertThat(studentList).hasSize(1)
                .contains(student2)
                .doesNotContain(student1)
                .doesNotContain((Student) null);

    }

    @Test
    void testClear() {
        List<Student> a = new ArrayList<>();

        studentList.add(student1);
        a.add(student2);

        a.clear();
        studentList.clear();

        assertThat(studentList).hasSize(0)
                .doesNotContain(student1);
        assertEquals(Arrays.toString(a.toArray()), Arrays.toString(studentList.toArray()));
        assertEquals(Arrays.toString(new Student[0]), Arrays.toString(studentList.toArray()));
    }

    @Test
    void testClearOnEmptyList() {
        List<Student> a = new ArrayList<>();

        a.clear();
        studentList.clear();

        assertTrue(studentList.isEmpty());
        assertEquals(Arrays.toString(a.toArray()), Arrays.toString(studentList.toArray()));
        assertEquals(Arrays.toString(new Student[0]), Arrays.toString(studentList.toArray()));
    }

    @Test
    void testGetOnEmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> studentList.get(0));
    }

    @Test
    void testGetWithNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> studentList.get(-2));
    }

    @Test
    void testGet() {
        studentList.add(student1);
        studentList.add(student2);

        assertEquals(student1, studentList.get(0));
        assertEquals(student2, studentList.get(1));
    }

    @Test
    void testSetOnEmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> studentList.set(0, student1));
    }

    @Test
    void testSetOnOneElementList() {
        studentList.add(student1);

        assertEquals(student1, studentList.set(0, student2));
        assertEquals(student2, studentList.get(0));
    }

    @Test
    void testSetOnTwoElementList() {
        studentList.add(student1);
        studentList.add(student2);

        assertEquals(student1, studentList.set(0, student2));
        assertEquals(student2, studentList.get(0));
        assertFalse(studentList.contains(student1));
    }

    @Test
    void testAddOnIndexZeroInEmptyList() {
        studentList.add(0, student1);

        assertFalse(studentList.isEmpty());
        assertEquals(student1, studentList.get(0));
    }

    @Test
    void testAddOnIndexZeroInOneElementList() {
        studentList.add(student1);
        studentList.add(0, student2);

        assertEquals(student2, studentList.get(0));
        assertEquals(student1, studentList.get(1));
        assertEquals(2, studentList.size());
    }

    @Test
    void testAddOnIndexInTwoElementList() {
        studentList = new StudentList(2);
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(1, student3);

        assertEquals(student1, studentList.get(0));
        assertEquals(student3, studentList.get(1));
        assertEquals(student2, studentList.get(2));
    }

    @Test
    void testRemoveOnIndexZeroInEmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> studentList.remove(0));
    }

    @Test
    void testRemoveOnIndexZeroInOneElementList() {
        studentList.add(student1);

        assertEquals(student1, studentList.remove(0));
        assertTrue(studentList.isEmpty());
    }

    @Test
    void testRemoveOnIndexInTwoElementList() {
        studentList.add(student1);
        studentList.add(student2);


        assertEquals(student1, studentList.remove(0));
        assertEquals(student2, studentList.remove(0));
        assertTrue(studentList.isEmpty());
    }

    @Test
    void testIndexOfInEmptyList() {
        assertEquals(-1, studentList.indexOf(null));
    }

    @Test
    void testIndexOfInOneElementList() {
        studentList.add(student1);

        assertEquals(0, studentList.indexOf(student1));
    }

    @Test
    void testIndexOfInOneElementListWithNoSuchElement() {
        studentList.add(student1);

        assertEquals(-1, studentList.indexOf(student2));
    }

    @Test
    void testIndexOfInTwoElementListWithNull() {
        studentList.add(student1);
        studentList.add(null);

        assertEquals(1, studentList.indexOf(null));
    }

    @Test
    void testIndexOfInTwoElementListWithDuplicates() {
        studentList.add(student1);
        studentList.add(student1);

        assertEquals(0, studentList.indexOf(student1));
    }

    @Test
    void testLastIndexOfInEmptyList() {
        assertEquals(-1, studentList.lastIndexOf(null));
    }

    @Test
    void testLastIndexOfInOneElementList() {
        studentList.add(student1);

        assertEquals(0, studentList.lastIndexOf(student1));
    }

    @Test
    void testLastIndexOfInOneElementListWithNoSuchElement() {
        studentList.add(student1);

        assertEquals(-1, studentList.lastIndexOf(student2));
    }

    @Test
    void testLastIndexOfInTwoElementListWithNull() {
        studentList.add(student1);
        studentList.add(null);

        assertEquals(1, studentList.lastIndexOf(null));
    }

    @Test
    void testLastIndexOfInTwoElementListWithDuplicates() {
        studentList.add(student1);
        studentList.add(student1);
        studentList.add(null);
        studentList.add(null);

        assertEquals(1, studentList.lastIndexOf(student1));
        assertEquals(3, studentList.lastIndexOf(null));
    }

    @Nested
    class TestInnerClassListIterator {

        @Test
        void testConstructorWithIndexForTheEnd() {
            studentList.add(student1);
            assertEquals(student1, studentList.listIterator(0).next());
        }

        @Test
        void testConstructorWithIndexForInBetween() {
            studentList.add(student1);
            studentList.add(student2);
            studentList.add(student3);

            assertEquals(student2, studentList.listIterator(1).next());
        }

        @Test
        void testDefaultConstructor() {
            studentList.add(student1);
            assertEquals(student1, studentList.listIterator().next());
        }

        @Test
        void testHasPreviousInOneElementList() {
            studentList.add(student2);
            ListIterator<Student> listIterator = studentList.listIterator();
            listIterator.next();

            assertTrue(listIterator.hasPrevious());
        }

        @Test
        void testHasPreviousInEmptyList() {
            assertFalse(studentList.listIterator().hasPrevious());
        }

        @Test
        void testPreviousInTwoElementsList() {
            studentList.add(student2);
            studentList.add(student3);
            ListIterator<Student> listIterator = studentList.listIterator();
            listIterator.next();

            assertEquals(student3, listIterator.previous());
        }

        @Test
        void testPreviousInZeroElementsList() {
            assertThrows(NoSuchElementException.class, studentList.listIterator()::previous);
        }

        @Test
        void testNextIndexForOneElementList() {
            studentList.add(student2);
            ListIterator<Student> listIterator = studentList.listIterator();
            listIterator.next();

            assertEquals(1, listIterator.nextIndex());
        }

        @Test
        void testPreviousIndexForOneElementList() {
            studentList.add(student1);
            ListIterator<Student> listIterator = studentList.listIterator();
            listIterator.next();

            assertEquals(0, listIterator.previousIndex());
        }

        @Test
        void testPreviousIndexInZeroElementsList() {
            assertEquals(-1, studentList.listIterator().previousIndex());
        }

        @Test
        void testListIteratorSet() {
            studentList.add(student1);
            studentList.add(student2);
            studentList.add(student3);
            ListIterator<Student> listIterator = studentList.listIterator();
            listIterator.next();
            listIterator.set(student3);
            listIterator.next();
            listIterator.set(null);

            assertEquals(student3, studentList.get(0));
            assertNull(studentList.get(1));
        }

        @Test
        void testListIteratorSetOnEmptyList() {
            ListIterator<Student> listIterator = studentList.listIterator();

            assertThrows(IllegalStateException.class, () -> listIterator.set(student1));
        }

        @Test
        void testListIteratorAdd() {
            ListIterator<Student> listIterator = studentList.listIterator();
            listIterator.add(student1);
            listIterator.add(student2);
            listIterator.add(student3);

            assertEquals(student1, studentList.get(0));
            assertEquals(student2, studentList.get(1));
            assertEquals(student3, studentList.get(2));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class TestInnerClassSubList {
        List<Student> sub;

        @BeforeEach
        void setUp(){
            studentList.add(student1);
            studentList.add(student2);
            studentList.add(student3);
            studentList.add(student4);
            studentList.add(student5);

            sub = studentList.subList(1, 3);
        }

        @Test
        void testConstructorWithOutOfBoundsIndexForLowerBoundary(){
            assertThrows(IndexOutOfBoundsException.class, () -> studentList.subList(-1, 3));
        }

        @Test
        void testConstructorWithOutOfBoundsIndexForUpperBoundary(){
            assertThrows(IndexOutOfBoundsException.class, () -> studentList.subList(1, 12));
        }

        @Test
        void testConstructorWithIllegalArguments(){
            assertThrows(IllegalArgumentException.class, () -> studentList.subList(3, 1));
        }

        @Test
        void testDefaultConstructor(){
            assertArrayEquals(copyListView(1, 3), sub.toArray());
        }

        @Test
        void testSetForOutOfBoundsIndex(){
            assertThrows(IndexOutOfBoundsException.class, () -> sub.set(3, student1));
        }

        @Test
        void testSetReturnAndChangingOriginalListValues(){
            assertEquals(student2, sub.set(0, student1));
            assertEquals(student3, sub.set(1, student5));

            assertArrayEquals(copyListView(1, 3), sub.toArray());
        }

        @Test
        void testAddChangingOriginalListValues(){
            sub.add(0, student5);
            sub.add(1, student4);
            sub.add(4, student1);

            assertEquals(5, sub.size());
            assertArrayEquals(copyListView(1, 6), sub.toArray());
        }

        Student[] copyListView(int startIndex, int endIndex){
            Student[] s = new Student[endIndex-startIndex];
            for(int i=startIndex; i<endIndex; i++){
                s[i-startIndex] = studentList.get(i);
            }
            return s;
        }

        @Test
        void testRemoveChangingOriginalListValues(){
            sub.remove(0);
            sub.remove(0);

            assertEquals(0, sub.size());
            assertEquals(3, studentList.size());
            assertFalse(studentList.contains(student2));
            assertFalse(studentList.contains(student3));
        }

        @Test
        void testGet(){
            assertEquals(student2, sub.get(0));
            assertEquals(student3, sub.get(1));
        }

        @Test
        void testSize(){
            assertEquals(2, sub.size());
        }

        @Test
        void testSizeAfterAdd(){
            sub.add(1, student3);

            assertEquals(3, sub.size());
        }

        @Test
        void testIndexOfInEmptyList() {
            sub = studentList.subList(1,1);
            assertEquals(-1, sub.indexOf(null));
        }

        @Test
        void testIndexOfWithNoSuchElement() {
            assertEquals(-1, sub.indexOf(new Student("", null, "")));
        }

        @Test
        void testIndexOfWithNull() {
            sub.add(1,null);

            assertEquals(1, sub.indexOf(null));
        }

        @Test
        void testIndexOfInTwoElementListWithDuplicates() {
            sub.add(0, student1);
            sub.add(1, student1);

            assertEquals(0, sub.indexOf(student1));
        }

        @Test
        void testLastIndexOfInEmptyList() {
            sub = studentList.subList(1,1);
            assertEquals(-1, sub.lastIndexOf(null));
        }

        @Test
        void testLastIndexOfWithNoSuchElement() {
            assertEquals(-1, sub.lastIndexOf(new Student("", null, "")));
        }

        @Test
        void testLastIndexOfWithNull() {
            sub.add(1,null);

            assertEquals(1, sub.lastIndexOf(null));
        }

        @Test
        void testLastIndexOfInTwoElementListWithDuplicates() {
            sub.add(0, student1);
            sub.add(1, student1);

            assertEquals(1, sub.lastIndexOf(student1));
        }

        @Test
        void testToArray() {
            assertEquals(Arrays.toString(copyListView(1, 3)), Arrays.toString(sub.toArray()));
        }

        @Test
        void testToArrayWithParameter() {
            assertEquals(Arrays.toString(copyListView(1, 3)), Arrays.toString(sub.toArray(new Student[0])));
        }

        @ParameterizedTest
        @MethodSource("provideStudentForContains")
        void testContains(Student student, boolean expected) {
            assertEquals(expected, sub.contains(student));
        }

        private Stream<Arguments> provideStudentForContains() {
            return Stream.of(
                    Arguments.of(student1, false),
                    Arguments.of(student2, true),
                    Arguments.of(student3, true),
                    Arguments.of(student4, false),
                    Arguments.of(student5, false),
                    Arguments.of(null, false));
        }

        @Test
        void testSubListOfSubList(){
            sub = studentList.subList(1, 5);
            List<Student> subsub = sub.subList(1,3);

            subsub.add(1, new Student("", null, ""));

            assertArrayEquals(copyListView(1,6), sub.toArray());
            assertArrayEquals(copyListView(2,5), subsub.toArray());
        }
    }

    @Test
    void testAddAllFiveElementsInZeroCapacityList(){
        List<Student> a = new ArrayList<>();
        Collections.addAll(a, student1, student2, student3, student4, student5);

        studentList = new StudentList(0);
        studentList.addAll(a);

        assertArrayEquals(a.toArray(), studentList.toArray());
    }

    @Test
    void testContainsAllTwoElementsInZeroCapacityList(){
        List<Student> a = new ArrayList<>();
        Collections.addAll(a, student1, student2);

        studentList = new StudentList(a);

        assertTrue(studentList.containsAll(a));
        assertArrayEquals(a.toArray(), studentList.toArray());
    }
}
