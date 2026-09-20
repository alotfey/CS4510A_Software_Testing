/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.baker.project8;
        
import java.util.List;

/**
 * Encapsulates interaction with a database.
 * @author Richard Lesh
 */
public interface DBO {
    void store(Contact c);
    void delete(Contact c);
    List<Contact> getContact(String first, String last);
    int countContacts();
}
