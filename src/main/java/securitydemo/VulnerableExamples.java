package com.securedevops.employeemanagement.securitydemo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VulnerableExamples {

    public void vulnerableSqlQuery(String employeeId) {

        String query = "SELECT * FROM employee WHERE id = '" + employeeId + "'";

        System.out.println(query);
    }

    public void weakHash(String input) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] hash = md.digest(input.getBytes());

        System.out.println(hash);
    }
}