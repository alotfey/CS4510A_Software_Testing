/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package edu.baker.project4;

import org.junit.jupiter.api.Test;

/**
 * Smoke test for the Project4 main class so the coverage report
 * covers the whole project, not just Fraction.
 */
public class Project4Test {

    /**
     * main() only prints a banner; it should run without throwing.
     */
    @Test
    public void testMainRunsWithoutError() {
        System.out.println("main");
        Project4.main(new String[0]);
    }
}