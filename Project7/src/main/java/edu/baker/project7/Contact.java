/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.baker.project7;

import java.util.Objects;

/**
 * Data type to represent a contact in an address book.
 * @author Richard Lesh
 */
public class Contact {
    private String first;
    private String last;
    private String phone;
    private String email;
    private String favoriteColor;

    public Contact(String first, String last, String phone, String email, String favoriteColor) {
        this.first = first;
        this.last = last;
        this.phone = phone;
        this.email = email;
        this.favoriteColor = favoriteColor;
    }

    public String toString() {
        return last + ", " + first + ": " + phone + " <" + email + "> (" + favoriteColor + ")";
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (! (o instanceof Contact)) return false;
        Contact c = (Contact)o;
        return last.equals(c.last) && first.equals(c.first) &&
                phone.equals(c.phone) && email.equals(c.email) &&
                favoriteColor.equals(c.favoriteColor);
    }

    public int hashCode() {
        return Objects.hash(first, last, phone, email, favoriteColor);
    }

    public String getFirst() { return first; }
    public String getLast() { return last; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getFavoriteColor() { return favoriteColor; }

    /**
     * Sets the first name of this contact.
     * Changing a name field alters the value returned by hashCode(), so a
     * Contact must not be modified while it is stored in a hash-based
     * collection such as the Set inside FakeDBO.
     *
     * @param first the new first name
     */
    public void setFirst(String first) { this.first = first; }

    /**
     * Sets the last name of this contact.
     * Changing a name field alters the value returned by hashCode(), so a
     * Contact must not be modified while it is stored in a hash-based
     * collection such as the Set inside FakeDBO.
     *
     * @param last the new last name
     */
    public void setLast(String last) { this.last = last; }

    /**
     * Sets the phone number of this contact.
     * Changing this field alters the value returned by hashCode(), so a
     * Contact must not be modified while it is stored in a hash-based
     * collection such as the Set inside FakeDBO.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Sets the email address of this contact.
     * Changing this field alters the value returned by hashCode(), so a
     * Contact must not be modified while it is stored in a hash-based
     * collection such as the Set inside FakeDBO.
     *
     * @param email the new email address
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Sets the favorite color of this contact.
     * Changing this field alters the value returned by hashCode(), so a
     * Contact must not be modified while it is stored in a hash-based
     * collection such as the Set inside FakeDBO.
     *
     * @param favoriteColor the new favorite color
     */
    public void setFavoriteColor(String favoriteColor) { this.favoriteColor = favoriteColor; }
}