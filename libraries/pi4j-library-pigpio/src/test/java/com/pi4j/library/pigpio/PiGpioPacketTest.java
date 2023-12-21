package com.pi4j.library.pigpio;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class PiGpioPacketTest {

    @Test
    void i2cri_does_return_p3() throws IOException {
        PiGpioPacket packet = new PiGpioPacket(PiGpioCmd.I2CRI).p3(99);
        ByteArrayInputStream bis = new ByteArrayInputStream(new byte[2]);
        assertEquals(99, PiGpioPacket.bytesToRead(packet, bis));
    }

    @Test
    void i2crd_does_return_p3() throws IOException {
        PiGpioPacket packet = new PiGpioPacket(PiGpioCmd.I2CRD).p3(99);
        ByteArrayInputStream bis = new ByteArrayInputStream(new byte[2]);
        assertEquals(99, PiGpioPacket.bytesToRead(packet, bis));
    }

    @Test
    void nonconfigured_cmd_returns_stream_available() throws IOException {
        PiGpioPacket packet = new PiGpioPacket(PiGpioCmd.BC1).p3(99);
        ByteArrayInputStream bis = new ByteArrayInputStream(new byte[2]);
        assertEquals(2, PiGpioPacket.bytesToRead(packet, bis));
    }
}