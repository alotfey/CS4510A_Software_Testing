package edu.baker.project7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A fake implementation of the {@link DBO} interface, used in place of a real
 * database when testing {@link AddressBook}.
 * <p>
 * This is a fake rather than a stub: it has a real working implementation of
 * every operation in the interface, it just does the job in a far simpler way
 * by holding the contacts in an in-memory Set instead of a database. That
 * keeps the tests fast and completely under the test's control, with no
 * connection, no schema and no cleanup between runs.
 * <p>
 * A Set is used because the assignment specifies one. It also means storing
 * the same Contact twice has no effect on the count, so any test that relies
 * on the count rising by one per add must supply distinct contacts.
 *
 * @author Ahmed Lotfey
 */
public class FakeDBO implements DBO {

    /** The in-memory stand-in for the database table of contacts. */
    private Set<Contact> data = new HashSet<>();

    /**
     * Stores a contact. Adding a contact equal to one already held leaves the
     * set unchanged, which mirrors a table with a uniqueness constraint.
     *
     * @param c the contact to store
     */
    @Override
    public void store(Contact c) {
        data.add(c);
    }

    /**
     * Deletes a contact. Removing a contact that is not held leaves the set
     * unchanged rather than failing.
     *
     * @param c the contact to delete
     */
    @Override
    public void delete(Contact c) {
        data.remove(c);
    }

    /**
     * Finds every stored contact whose first and last name both match the
     * arguments. Two different people can share a name, and the same name can
     * appear with different phone numbers or emails, so this returns a list
     * rather than a single contact. An empty list means no match.
     *
     * @param first the first name to match
     * @param last the last name to match
     * @return the matching contacts, empty if there are none
     */
    @Override
    public List<Contact> getContact(String first, String last) {
        List<Contact> matches = new ArrayList<>();
        for (Contact c : data) {
            if (c.getFirst().equals(first) && c.getLast().equals(last)) {
                matches.add(c);
            }
        }
        return matches;
    }

    /**
     * Reports how many contacts are currently stored.
     *
     * @return the number of stored contacts
     */
    @Override
    public int countContacts() {
        return data.size();
    }
}