package de.iisys.duramentum.document.handler.strategy;

import org.junit.Test;

import static org.junit.Assert.*;

public class SearchWithMachineAndOrderTest {

    @Test
    public void isEntryRequired() throws Exception {
        assertTrue("MachineA".matches("\\b(MachineA){1}\\b"));
        assertFalse("MachineATool1".matches("MachineA(?![A-Za-z0-9])"));
        assertTrue("MachineATool223213".matches("Machine[A-Za-z]{1}Tool[0-9]{6}\\b"));

    }

}