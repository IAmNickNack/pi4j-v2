package com.pi4j.library.pigpio;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface PiGpioStreamsProvider extends Closeable {

    /**
     * Provide an {@link InputStream}
     */
    InputStream getInputStream();

    /**
     * Provide an {@link OutputStream}
     */
    OutputStream getOutputStream();

    /**
     * Ensure resources are ready for use
     */
    void validateReady();

    /**
     * Dispose resources
     */
    void terminate();

    /**
     * {@link PiGpioStreamsProvider} implementation to for streams derived from a {@link Socket}
     */
    class SocketStreamsProvider implements PiGpioStreamsProvider {

        private final PiGpioSupplier<Socket> socketSupplier;

        private Socket socket;

        public SocketStreamsProvider(PiGpioSupplier<Socket> socketSupplier) {
            this.socketSupplier = socketSupplier;
        }

        /**
         * Platform-specific connection validation
         */
        @Override
        public void validateReady() {
            if (this.socket == null || !this.socket.isConnected()) {
                this.socket = socketSupplier.get();
            }
        }

        /**
         * Check {@link #socket} is not null and connected and close
         */
        @Override
        public void close() throws IOException {
            if (this.socket != null && this.socket.isConnected()) {
                this.socket.close();
            }
        }

        /**
         * This implementation is a platform-specific wrapper for {@link SocketStreamsProvider#close()}
         */
        @Override
        public void terminate() {
            try {
                this.close();
            } catch (IOException e) {
                throw new PiGpioException(e);
            }
        }

        @Override
        public InputStream getInputStream() {
            try {
                return socket.getInputStream();
            } catch (IOException e) {
                throw new PiGpioException(e);
            }
        }

        @Override
        public OutputStream getOutputStream() {
            try {
                return socket.getOutputStream();
            } catch (IOException e) {
                throw new PiGpioException(e);
            }
        }
    }

    /**
     * A {@link java.util.function.Supplier} which can catch or throw exceptions
     * @param <T> the type being supplied
     */
    interface PiGpioSupplier<T> {
        T get() throws PiGpioException;
    }

    /**
     * Default {@link PiGpioSupplier} for {@link Socket} instances
     */
    class DefaultSocketSupplier implements PiGpioSupplier<Socket> {
        private final String host;
        private final int port;

        /**
         * @param host host connect to
         * @param port port to connect to
         */
        public DefaultSocketSupplier(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public Socket get() throws PiGpioException {
            try {
                return new Socket(host, port);
            } catch (IOException e) {
                throw new PiGpioException(e);
            }
        }
    }
}