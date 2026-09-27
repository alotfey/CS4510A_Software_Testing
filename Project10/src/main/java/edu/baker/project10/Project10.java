/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package edu.baker.project10;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Demo program: encrypts pom.xml to pom.xml.cipher and decrypts it back to
 * pom.xml.clear.
 * @author Richard Lesh
 */
public class Project10 {

    /**
     * Entry point for the demo. Picks one EncryptionMethod and hands it to
     * Encryption, which does all of the file reading and writing.
     * @param args command-line arguments (not used)
     * @throws IOException if a file cannot be read or written
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Project 10!");
        Path cleartextPath = FileSystems.getDefault().getPath("pom.xml");
        Path ciphertextPath = FileSystems.getDefault().getPath("pom.xml.cipher");
        Path cleartextAgainPath = FileSystems.getDefault().getPath("pom.xml.clear");

//        EncryptionMethod method = new CaesarCipher("Bubblegum");
        EncryptionMethod method = new XORCipher("Bubblegum");
        Encryption.encryptFile(cleartextPath, ciphertextPath, method);
        Encryption.decryptFile(ciphertextPath, cleartextAgainPath, method);
    }
}