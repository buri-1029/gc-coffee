package org.prgrms.gccoffee.order.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    public void testInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Email("abc"));
    }

    @Test
    public void testValidEmail() {
        Email email = new Email("hello@gmail.com");
        assertThat(email).isEqualTo("hello@gmail.com");
    }

}