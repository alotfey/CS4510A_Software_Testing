package edu.baker.project7;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

/**
 * Property-based tests for {@link AddressBook}.
 * <p>
 * Each property receives a freshly generated list of 2 to 50 distinct
 * contacts and builds a brand new AddressBook backed by a {@link FakeDBO},
 * so no state leaks between runs. Rather than asserting a fixed expected
 * result, each property asserts a relationship that must hold for every
 * possible list: an add raises the match count by exactly one, a remove
 * lowers it by exactly one, and a stored contact is findable by name.
 * <p>
 * Every property also checks size() after each operation, which is why the
 * assignment needs no separate test for size().
 *
 * @author Ahmed Lotfey
 */
public class AddressBookTest {

    /**
     * Adding a contact increases the number of matches for that contact's name
     * by exactly one, and raises the address book size by exactly one.
     *
     * @param contacts a generated list of distinct contacts
     */
    @Property(tries = 1000)
    public void testAdd(@ForAll("listOfContacts") List<Contact> contacts) {
        FakeDBO db = new FakeDBO();
        AddressBook book = new AddressBook(db);

        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);

            int before = book.find(c.getFirst(), c.getLast()).size();
            book.add(c);
            int after = book.find(c.getFirst(), c.getLast()).size();

            assertEquals(before + 1, after);
            assertEquals(i + 1, book.size());
        }

        assertEquals(contacts.size(), book.size());
    }

    /**
     * Removing a contact decreases the number of matches for that contact's
     * name by exactly one, and lowers the address book size by exactly one.
     * Every contact is added first, so the book starts full and ends empty.
     *
     * @param contacts a generated list of distinct contacts
     */
    @Property(tries = 1000)
    public void testRemove(@ForAll("listOfContacts") List<Contact> contacts) {
        FakeDBO db = new FakeDBO();
        AddressBook book = new AddressBook(db);

        for (Contact c : contacts) {
            book.add(c);
        }
        assertEquals(contacts.size(), book.size());

        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);

            int before = book.find(c.getFirst(), c.getLast()).size();
            book.remove(c);
            int after = book.find(c.getFirst(), c.getLast()).size();

            assertEquals(before - 1, after);
            assertEquals(contacts.size() - (i + 1), book.size());
        }

        assertEquals(0, book.size());
    }

    /**
     * A contact that has been added is present in the results of a find() on
     * its own first and last name. Uses the Hamcrest hasItem() matcher, since
     * find() may legitimately return several people sharing a name and the
     * assertion only cares that this contact is among them.
     *
     * @param contacts a generated list of distinct contacts
     */
    @Property(tries = 1000)
    public void testFind(@ForAll("listOfContacts") List<Contact> contacts) {
        FakeDBO db = new FakeDBO();
        AddressBook book = new AddressBook(db);

        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);

            book.add(c);

            assertThat(book.find(c.getFirst(), c.getLast()), hasItem(c));
            assertEquals(i + 1, book.size());
        }

        assertEquals(contacts.size(), book.size());
    }

    /**
     * Supplies lists of 2 to 50 contacts, reusing the single-contact provider
     * from {@link ContactTest} so a generated Contact is defined in one place.
     * <p>
     * uniqueElements() matters: FakeDBO stores contacts in a Set, so adding a
     * duplicate would leave the count unchanged and break the "exactly one
     * more" assertion in testAdd. jqwik's edge case generation would find such
     * a duplicate well within 1000 tries.
     *
     * @return an Arbitrary producing lists of 2 to 50 distinct contacts
     */
    @Provide
    public Arbitrary<List<Contact>> listOfContacts() {
        Arbitrary<Contact> oneContact = new ContactTest().contactProvider();
        return oneContact.list().ofMinSize(2).ofMaxSize(50).uniqueElements();
    }
}