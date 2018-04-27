package com.example.mac.chartr.objects;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfirmationEmailTest
{

    @Test
    public void testGetAndSet() {
        ConfirmationEmail email = new ConfirmationEmail();

        email.setDriverEmail("email");
        email.setDriverName("name");
        email.setDriverPhone("+18888888888");
        email.setRiderEmail("email2");
        email.setRiderName("name2");
        email.setRiderPhone("+19999999999");
        email.setTripTime(123456L);

        ConfirmationEmail email1 = new ConfirmationEmail("name", "name2",
                "+18888888888", "+19999999999", "email",
                "email2", 123456L);

        assertEquals("email", email.getDriverEmail());
        assertEquals("name", email.getDriverName());
        assertEquals("+18888888888", email.getDriverPhone());
        assertEquals("email2", email.getRiderEmail());
        assertEquals("name2", email.getRiderName());
        assertEquals("+19999999999", email.getRiderPhone());
        assertEquals(123456L, email.getTripTime());

        assertEquals("email", email1.getDriverEmail());
        assertEquals("name", email1.getDriverName());
        assertEquals("+18888888888", email1.getDriverPhone());
        assertEquals("email2", email1.getRiderEmail());
        assertEquals("name2", email1.getRiderName());
        assertEquals("+19999999999", email1.getRiderPhone());
        assertEquals(123456L, email1.getTripTime());
    }
}
