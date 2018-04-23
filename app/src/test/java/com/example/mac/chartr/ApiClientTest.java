package com.example.mac.chartr;


import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ApiClientTest {

    @Test
    public void testGetClientInstance() {
        ApiInterface victim = ApiClient.getApiInstance();

        assertNotNull(victim);
    }
}
