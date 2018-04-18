package com.example.mac.chartr;


import android.text.Editable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MaskWatcherTest {

    @Test
    public void testBeforeChange() {
        MaskWatcher maskWatcher = new MaskWatcher("+####");

        maskWatcher.beforeTextChanged(null, 10, 5, 2);

        assertTrue(maskWatcher.isDeleting());

        maskWatcher.beforeTextChanged(null, 10, 5, 15);
        assertFalse(maskWatcher.isDeleting());
    }

    @Test
    public void testOnTextChange() {
        MaskWatcher maskWatcher = new MaskWatcher("+####");
        boolean isDeleting = maskWatcher.isDeleting();
        boolean isRunning = maskWatcher.isRunning();

        maskWatcher.onTextChanged(null, 10, 5, 2);

        assertEquals(isDeleting, maskWatcher.isDeleting());
        assertEquals(isRunning, maskWatcher.isRunning());
    }

    @Test
    public void testAfterTextChange() {
        MaskWatcher maskWatcher = new MaskWatcher("+####");
        Editable editable = mock(Editable.class);
        when(editable.length()).thenReturn(3);

        maskWatcher.afterTextChanged(editable);

        assertFalse(maskWatcher.isRunning());
    }
}
