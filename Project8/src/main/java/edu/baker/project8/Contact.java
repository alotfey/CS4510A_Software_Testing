/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.baker.project8;

import java.util.Objects;

/**
 * Data type to represent a contact in an address book.
 * @author Richard Lesh
 */
public class Contact {
    private final String first;
    private final String last;
    private final String phone;
    private final String email;
    
    public Contact(String first, String last, String phone, String email) {
        this.first = first;
        this.last = last;
        this.phone = phone;
        this.email = email;
    }
    
    public String toString() {
        return last + ", " + first + ": " + phone + " <" + email + ">";
    }
    
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (! (o instanceof Contact)) return false;
        Contact c = (Contact)o;
        return last.equals(c.last) && first.equals(c.first) &&
                phone.equals(c.phone) && email.equals(c.email);
    } 
    
    public int hashCode() {
        return Objects.hash(first, last, phone, email);
    }
    
    public String getFirst() { return first; }
    public String getLast() { return last; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
