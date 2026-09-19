/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.baker.project7;

import java.util.List;

/**
 * Data type to represent a group of Contacts.
 * @author Richard Lesh
 */
public class AddressBook {
    private DBO dbo;
    
    public AddressBook(DBO dbo) {
        this.dbo = dbo;
    }
    
    public int size() {
        return dbo.countContacts();
    }
    
    public void add(Contact c) {
        dbo.store(c);
    }
    
    public void remove(Contact c) {
        dbo.delete(c);
    }
    
    public List<Contact> find(String first, String last) {
        return dbo.getContact(first, last);
    }
}
