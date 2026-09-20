/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package edu.baker.project8;

import java.util.Collections;
import java.util.List;

import net.jqwik.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Richard Lesh
 */
public class AddressBookTest {

    public AddressBookTest() {
    }

    /**
     * Adding a Contact reaches the data store exactly once per Contact.
     * <p>
     * The DBO is a Mockito mock, so nothing is really stored. getContact() is
     * stubbed to report an empty result before the add and a one-element list
     * after it, which is what a real database would do, and store() is
     * verified rather than inspected. That is the difference between this
     * assignment and the fake used in Assignment 7: a fake answers from its
     * own state, a mock answers what it was told to answer and records who
     * called it.
     *
     * @param contacts a generated list of distinct contacts
     */
    @Property(tries = 1000)
    public void testAdd(@ForAll("listOfContacts") List<Contact> contacts) {
        DBO dbo = mock(DBO.class);
        AddressBook book = new AddressBook(dbo);

        for (Contact c : contacts) {
            when(dbo.getContact(c.getFirst(), c.getLast())).thenReturn(Collections.EMPTY_LIST);
            List<Contact> before = book.find(c.getFirst(), c.getLast());
            assertTrue(before.isEmpty());

            book.add(c);
            verify(dbo, times(1)).store(c);

            when(dbo.getContact(c.getFirst(), c.getLast())).thenReturn(Collections.singletonList(c));
            List<Contact> after = book.find(c.getFirst(), c.getLast());
            assertThat(after, hasItem(c));
        }

        verify(dbo, times(contacts.size())).store(any(Contact.class));
    }

    /**
     * Removing a Contact reaches the data store exactly once per Contact.
     * <p>
     * Note on ordering: the assignment lists "verify delete() is called once"
     * before "remove the Contact from the AddressBook". Those two steps are
     * transposed in the prompt, since delete() cannot have been called before
     * remove() runs. This method removes first and then verifies, matching the
     * order used for add and store in testAdd().
     *
     * @param contacts a generated list of distinct contacts
     */
    @Property(tries = 1000)
    public void testRemove(@ForAll("listOfContacts") List<Contact> contacts) {
        DBO dbo = mock(DBO.class);
        AddressBook book = new AddressBook(dbo);

        for (Contact c : contacts) {
            book.add(c);
        }

        for (Contact c : contacts) {
            when(dbo.getContact(c.getFirst(), c.getLast())).thenReturn(Collections.singletonList(c));
            List<Contact> before = book.find(c.getFirst(), c.getLast());
            assertThat(before, hasItem(c));

            book.remove(c);
            verify(dbo, times(1)).delete(c);

            when(dbo.getContact(c.getFirst(), c.getLast())).thenReturn(Collections.EMPTY_LIST);
            List<Contact> after = book.find(c.getFirst(), c.getLast());
            assertTrue(after.isEmpty());
        }

        verify(dbo, times(contacts.size())).delete(any(Contact.class));
    }

    /**
     * A Contact that has been added is returned by a find() on its own first
     * and last name. The Hamcrest hasItem() matcher is used because a real
     * getContact() may return several people sharing a name, and the assertion
     * only cares that this Contact is among them.
     *
     * @param contacts a generated list of distinct contacts
     */
    @Property(tries = 1000)
    public void testFind(@ForAll("listOfContacts") List<Contact> contacts) {
        DBO dbo = mock(DBO.class);
        AddressBook book = new AddressBook(dbo);

        for (Contact c : contacts) {
            book.add(c);

            when(dbo.getContact(c.getFirst(), c.getLast())).thenReturn(Collections.singletonList(c));
            List<Contact> matches = book.find(c.getFirst(), c.getLast());
            assertThat(matches, hasItem(c));
        }
    }

    @Provide
    public Arbitrary<List<Contact>> listOfContacts() {
        return ContactTest.contactProvider().list()
                .ofMinSize(2).ofMaxSize(50).uniqueElements();
    }
}