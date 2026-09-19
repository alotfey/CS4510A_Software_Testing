package edu.baker.project7;

import static org.junit.jupiter.api.Assertions.*;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Structural tests for the {@link Contact} class.
 * <p>
 * Every test works against a single shared {@code testObj} that is built once
 * by {@link #setUpClass()}, as required by the assignment. Because Contact is
 * mutable, {@link #resetTestObj()} restores that object to its baseline values
 * after each test so the setter tests cannot influence the tests that follow
 * them, whatever order JUnit chooses to run them in.
 *
 * @author Ahmed Lotfey
 */
public class ContactTest {

    /** Baseline first name for the shared test object. */
    private static final String FIRST = "John";

    /** Baseline last name for the shared test object. */
    private static final String LAST = "Smith";

    /** Baseline phone number for the shared test object. */
    private static final String PHONE = "555-1234";

    /** Baseline email address for the shared test object. */
    private static final String EMAIL = "jsmith@example.com";

    /** Baseline favorite color for the shared test object. */
    private static final String COLOR = "Blue";

    /** The shared Contact under test, created once before all tests. */
    private static Contact testObj;

    /**
     * Creates the single Contact instance shared by every test in this class.
     * Runs once, before any test method.
     */
    @BeforeAll
    public static void setUpClass() {
        testObj = new Contact(FIRST, LAST, PHONE, EMAIL, COLOR);
    }

    /**
     * Restores the shared Contact to its baseline field values after every
     * test. Without this, a setter test would leave the object modified and
     * later tests such as {@link #testToString()} would fail depending on
     * execution order.
     */
    @AfterEach
    public void resetTestObj() {
        testObj.setFirst(FIRST);
        testObj.setLast(LAST);
        testObj.setPhone(PHONE);
        testObj.setEmail(EMAIL);
        testObj.setFavoriteColor(COLOR);
    }

    // ---------- toString() ----------

    /**
     * Verifies that toString() produces the exact documented format:
     * last name, comma, first name, colon, phone, the email in angle
     * brackets, then the favorite color in parentheses. Compared character
     * for character against a literal.
     */
    @Test
    public void testToString() {
        assertEquals("Smith, John: 555-1234 <jsmith@example.com> (Blue)", testObj.toString());
    }

    // ---------- equals() ----------

    /**
     * Covers the first branch of equals(), the {@code o == this} shortcut.
     * An object compared to itself is equal without examining any field.
     */
    @Test
    public void testEqualsSameInstance() {
        assertTrue(testObj.equals(testObj));
    }

    /**
     * Covers the null branch of equals(). A Contact is never equal to null,
     * and the check must return false rather than throw.
     */
    @Test
    public void testEqualsNull() {
        assertFalse(testObj.equals(null));
    }

    /**
     * Covers the type branch of equals(). An object of an unrelated class is
     * rejected by the instanceof check before any field is read.
     */
    @Test
    public void testEqualsDifferentType() {
        assertFalse(testObj.equals("Smith, John"));
    }

    /**
     * Verifies that two distinct Contact instances holding identical field
     * values are equal. This is the only path on which all five field
     * comparisons evaluate to true.
     */
    @Test
    public void testEqualsSameValues() {
        Contact other = new Contact(FIRST, LAST, PHONE, EMAIL, COLOR);
        assertTrue(testObj.equals(other));
    }

    /**
     * Verifies that a difference in the first name alone makes two Contacts
     * unequal. Isolating one field forces the first-name comparison to be the
     * operand that fails, which a test differing in several fields at once
     * would never reach because {@code &&} short-circuits.
     */
    @Test
    public void testEqualsDifferentFirst() {
        Contact other = new Contact("Jane", LAST, PHONE, EMAIL, COLOR);
        assertFalse(testObj.equals(other));
    }

    /**
     * Verifies that a difference in the last name alone makes two Contacts
     * unequal, exercising the last-name operand of the comparison chain.
     */
    @Test
    public void testEqualsDifferentLast() {
        Contact other = new Contact(FIRST, "Jones", PHONE, EMAIL, COLOR);
        assertFalse(testObj.equals(other));
    }

    /**
     * Verifies that a difference in the phone number alone makes two Contacts
     * unequal, exercising the phone operand of the comparison chain.
     */
    @Test
    public void testEqualsDifferentPhone() {
        Contact other = new Contact(FIRST, LAST, "555-9999", EMAIL, COLOR);
        assertFalse(testObj.equals(other));
    }

    /**
     * Verifies that a difference in the email alone makes two Contacts
     * unequal, exercising the email operand of the comparison chain.
     */
    @Test
    public void testEqualsDifferentEmail() {
        Contact other = new Contact(FIRST, LAST, PHONE, "other@example.com", COLOR);
        assertFalse(testObj.equals(other));
    }

    /**
     * Verifies that a difference in the favorite color alone makes two
     * Contacts unequal, exercising the final operand of the comparison chain.
     * This is the operand that only runs when all four original fields match.
     */
    @Test
    public void testEqualsDifferentFavoriteColor() {
        Contact other = new Contact(FIRST, LAST, PHONE, EMAIL, "Green");
        assertFalse(testObj.equals(other));
    }

    // ---------- hashCode() ----------

    /**
     * Verifies that hashCode() is stable across repeated calls on an unchanged
     * object, which the Object contract requires.
     */
    @Test
    public void testHashCodeIsConsistent() {
        assertEquals(testObj.hashCode(), testObj.hashCode());
    }

    /**
     * Verifies the hashCode side of the equals/hashCode contract: two objects
     * that are equal must return the same hash code. Note there is
     * deliberately no test asserting that unequal objects hash differently,
     * because the contract does not require that.
     */
    @Test
    public void testHashCodeMatchesForEqualContacts() {
        Contact other = new Contact(FIRST, LAST, PHONE, EMAIL, COLOR);
        assertEquals(testObj.hashCode(), other.hashCode());
    }

    // ---------- getters ----------

    /** Verifies getFirst() returns the first name given to the constructor. */
    @Test
    public void testGetFirst() {
        assertEquals(FIRST, testObj.getFirst());
    }

    /** Verifies getLast() returns the last name given to the constructor. */
    @Test
    public void testGetLast() {
        assertEquals(LAST, testObj.getLast());
    }

    /** Verifies getPhone() returns the phone number given to the constructor. */
    @Test
    public void testGetPhone() {
        assertEquals(PHONE, testObj.getPhone());
    }

    /** Verifies getEmail() returns the email address given to the constructor. */
    @Test
    public void testGetEmail() {
        assertEquals(EMAIL, testObj.getEmail());
    }

    /** Verifies getFavoriteColor() returns the color given to the constructor. */
    @Test
    public void testGetFavoriteColor() {
        assertEquals(COLOR, testObj.getFavoriteColor());
    }

    // ---------- setters ----------

    /**
     * Verifies setFirst() replaces the stored first name. The change is read
     * back through getFirst() and undone by {@link #resetTestObj()}.
     */
    @Test
    public void testSetFirst() {
        testObj.setFirst("Jane");
        assertEquals("Jane", testObj.getFirst());
    }

    /**
     * Verifies setLast() replaces the stored last name. The change is read
     * back through getLast() and undone by {@link #resetTestObj()}.
     */
    @Test
    public void testSetLast() {
        testObj.setLast("Jones");
        assertEquals("Jones", testObj.getLast());
    }

    /**
     * Verifies setPhone() replaces the stored phone number. The change is read
     * back through getPhone() and undone by {@link #resetTestObj()}.
     */
    @Test
    public void testSetPhone() {
        testObj.setPhone("555-9999");
        assertEquals("555-9999", testObj.getPhone());
    }

    /**
     * Verifies setEmail() replaces the stored email address. The change is read
     * back through getEmail() and undone by {@link #resetTestObj()}.
     */
    @Test
    public void testSetEmail() {
        testObj.setEmail("other@example.com");
        assertEquals("other@example.com", testObj.getEmail());
    }

    /**
     * Verifies setFavoriteColor() replaces the stored color. The change is
     * read back through getFavoriteColor() and undone by
     * {@link #resetTestObj()}.
     */
    @Test
    public void testSetFavoriteColor() {
        testObj.setFavoriteColor("Green");
        assertEquals("Green", testObj.getFavoriteColor());
    }

    // ---------- provider ----------

    /**
     * Supplies randomly generated Contact objects to jqwik property tests.
     * First name, last name and the local part of the email are alphabetic
     * strings of 1 to 10 characters, the phone is exactly ten digits, and the
     * favorite color is drawn from a fixed set of real color names, so every
     * generated Contact has a realistic shape and no sample has to be
     * discarded. AddressBookTest reuses this provider to build its lists, so
     * the definition of a generated Contact lives in one place only.
     *
     * @return an Arbitrary that produces random Contact instances
     */
    @Provide
    public Arbitrary<Contact> contactProvider() {
        Arbitrary<String> firstNames = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(10);
        Arbitrary<String> lastNames = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(10);
        Arbitrary<String> phones = Arbitraries.strings().numeric().ofLength(10);
        Arbitrary<String> emailNames = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(10);
        Arbitrary<String> colors = Arbitraries.of("Red", "Orange", "Yellow", "Green",
                "Blue", "Purple", "Black", "White");
        return Combinators.combine(firstNames, lastNames, phones, emailNames, colors)
                .as((f, l, p, e, c) -> new Contact(f, l, p, e + "@example.com", c));
    }
}