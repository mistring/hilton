package com.mstringham.ipgeolocation.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {

    @Test
    fun `Valid Query Strings`() {
        listOf(
            "",
            "1.2.3.4",
            "10.200.13.24",
            "0.0.0.0",
            "255.255.255.255",
            "google.com",
            "www.google.com",
            "domain123.com",
            "domain-info.com",
            "sub.domain.com",
            "sub.domain-plus.com",
            "domain.co.uk",
            "g.co",
            "domain.t.t.co"
        ).forEach {
            assertTrue(Validation.isQueryValid(it))
        }
    }

    @Test
    fun `Invalid Query Strings`() {
        listOf(
            " ",
            "1.2.3.4 ",
            " 10.200.13.24",
            "-1.0.0.0",
            "0...0",
            "253.254.255.256",
            "google,com",
            "google",
            "domain.123",
            ".com",
            "domain.com/users",
            "-domain.com",
            "domain-.com",
            "sub.-domain.com",
            "sub.domain-.com",
            "domain.t.t.c"
        ).forEach {
            println("Test: $it")
            assertEquals(false, Validation.isQueryValid(it))
        }
    }

}