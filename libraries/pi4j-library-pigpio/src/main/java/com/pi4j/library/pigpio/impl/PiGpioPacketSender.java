package com.pi4j.library.pigpio.impl;

import com.pi4j.library.pigpio.PiGpioCmd;
import com.pi4j.library.pigpio.PiGpioException;
import com.pi4j.library.pigpio.PiGpioPacket;
import com.pi4j.library.pigpio.PiGpioStreamsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Functional abstraction with responsibility for sending and receiving {@link PiGpioPacket} instances
 */
public interface PiGpioPacketSender {
    /**
     * Send a packet via the underlying stream
     * @param tx the packet to send
     * @return the response {@link PiGpioPacket}
     */
    PiGpioPacket sendPacket(PiGpioPacket tx);

    default PiGpioPacket sendCommand(PiGpioCmd cmd) {
        return sendPacket(new PiGpioPacket(cmd));
    }

    default PiGpioPacket sendCommand(PiGpioCmd cmd, int p1) {
        return sendPacket(new PiGpioPacket(cmd, p1));
    }

    default PiGpioPacket sendCommand(PiGpioCmd cmd, int p1, int p2) {
        return sendPacket(new PiGpioPacket(cmd, p1, p2));
    }

    /**
     * Default {@link PiGpioPacketSender} implementation sends and receives using streams provided by
     * {@link #streamsProvider}
     */
    class DefaultPacketSender implements PiGpioPacketSender {

        private final Logger logger = LoggerFactory.getLogger(DefaultPacketSender.class);

        /**
         * The component responsible for providing the in/out streams
         */
        private final PiGpioStreamsProvider streamsProvider;

        public DefaultPacketSender(PiGpioStreamsProvider streamsProvider) {
            this.streamsProvider = streamsProvider;
        }

        @Override
        public PiGpioPacket sendPacket(PiGpioPacket tx) {
            streamsProvider.validateReady();
            try {
                // get socket streams
                var in = streamsProvider.getInputStream();
                var out = streamsProvider.getOutputStream();

                // transmit packet
                if (logger.isTraceEnabled()) {
                    logger.trace("[TX] -> {}", tx);
                }
                out.write(PiGpioPacket.encode(tx));
                out.flush();

                // read receive packet
                PiGpioPacket rx = PiGpioPacket.decode(in);
                if (logger.isTraceEnabled()) {
                    logger.trace("[RX] <- {}", rx);
                }
                return rx;
            } catch (IOException se) {
                this.streamsProvider.terminate();
                throw new PiGpioException(se);
            }
        }
    }
}