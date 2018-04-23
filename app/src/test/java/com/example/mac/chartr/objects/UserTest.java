package com.example.mac.chartr.objects;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class UserTest {

    @Test
    public void testConstructorEquality() {
        User user1 = new User("email", "name", 4.5f);
        User user2 = new User("email", "name",
                null, null, 4.5f, 0);

        assertEquals(user1, user2);
    }

    @Test
    public void testNonEmptyHash() {
        User user1 = new User("email", "name", 4.5f);

        assertTrue(user1.hashCode() != 0);
    }

    @Test
    public void testConstructorAndSetters() {
        User user1 = new User("email", "name",
                "birthday", "phone", 4.5f, 12);
        User user2 = new User("none", "nope", 0.0f);

        user2.setEmail("email");
        user2.setName("name");
        user2.setPhone("phone");
        user2.setBirthdate("birthday");
        user2.setRating(4.5f);
        user2.setReviewCount(12);

        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getBirthdate(), user2.getBirthdate());
        assertEquals(user1.getPhone(), user2.getPhone());
        assertEquals(user1.getRating(), user2.getRating());
        assertEquals(user1.getReviewCount(), user2.getReviewCount());
    }

}
