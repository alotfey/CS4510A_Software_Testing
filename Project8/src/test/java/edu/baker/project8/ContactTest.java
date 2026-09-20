/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package edu.baker.project8;

import net.jqwik.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Richard Lesh
 */
public class ContactTest {
    private static Contact testObj;
    
    public ContactTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        testObj = new Contact("John", "Doe", "5551234567", "johndoe@baker.edu");
    }
    
    /**
     * Test of toString method, of class Contact.
     */
    @Test
    public void testToString() {
        assertEquals(testObj.toString(), "Doe, John: 5551234567 <johndoe@baker.edu>");
    }

    /**
     * Test of equals method, of class Contact.
     */
    @Test
    public void testEquals() {
        assertEquals(testObj, testObj);
        assertEquals(testObj, new Contact("John", "Doe", "5551234567", "johndoe@baker.edu"));
        assertNotEquals(testObj, "John Doe");
        assertNotEquals(testObj, new Contact("john", "Doe", "5551234567", "johndoe@baker.edu"));
        assertNotEquals(testObj, new Contact("John", "doe", "5551234567", "johndoe@baker.edu"));
        assertNotEquals(testObj, new Contact("John", "Doe", "1111234567", "johndoe@baker.edu"));
        assertNotEquals(testObj, new Contact("John", "Doe", "5551234567", "johndoe@baker.gov"));
    }

    /**
     * Test of hashCode method, of class Contact.
     */
    @Test
    public void testHashCode() {
        assertEquals(testObj.hashCode(), -1108351679);
    }

    /**
     * Test of getFirst method, of class Contact.
     */
    @Test
    public void testGetFirst() {
         assertEquals(testObj.getFirst(), "John");
   }

    /**
     * Test of getLast method, of class Contact.
     */
    @Test
    public void testGetLast() {
         assertEquals(testObj.getLast(), "Doe");
    }

    /**
     * Test of getPhone method, of class Contact.
     */
    @Test
    public void testGetPhone() {
         assertEquals(testObj.getPhone(), "5551234567");
    }

    /**
     * Test of getEmail method, of class Contact.
     */
    @Test
    public void testGetEmail() {
         assertEquals(testObj.getEmail(), "johndoe@baker.edu");
    }
    
    @Provide
    static Arbitrary<Contact> contactProvider() {
        Arbitrary<String> firstnames = Arbitraries.strings().withCharRange('a', 'z')
            .ofMinLength(5).ofMaxLength(20);
        Arbitrary<String> lastnames = Arbitraries.strings().withCharRange('a', 'z')
            .ofMinLength(5).ofMaxLength(20);
        Arbitrary<String> phones = Arbitraries.strings().withCharRange('0', '9')
            .ofMinLength(10).ofMaxLength(10);
        Arbitrary<String> emails = Arbitraries.strings().withCharRange('a', 'z')
            .ofMinLength(5).ofMaxLength(10);
        Arbitrary<String> hosts = Arbitraries.strings().withCharRange('a', 'z')
            .ofMinLength(5).ofMaxLength(10);
        Arbitrary<String> tlds = Arbitraries.of(".com", ".org", ".net", ".gov", ".edu");
        return Combinators.combine(firstnames, lastnames, phones, emails, hosts, tlds)
            .as((firstname, lastname, phone, email, host, tld) -> 
            new Contact(firstname, lastname, phone, email + "@" + host + tld));
    }
}
