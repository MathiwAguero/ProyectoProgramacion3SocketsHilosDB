package hospital.Logic;

import hospital.Presentation.ThreadListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Hilo que escucha mensajes del servidor y los distribuye
 * a todos los controladores registrados.
 */
public class GlobalListener extends Thread {

    private final Socket socket;
    private final List<ThreadListener> listeners;

    public GlobalListener(Socket socket, List<ThreadListener> listeners) {
        this.socket = socket;
        this.listeners = listeners;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("[GLOBAL] Mensaje recibido: " + message);

                // Notificar a todos los controladores registrados
                synchronized (listeners) {
                    for (ThreadListener tl : listeners) {
                        try {
                            tl.deliver_message(message);
                        } catch (Exception e) {
                            System.err.println("✗ Error notificando a " + tl.getClass().getSimpleName() + ": " + e.getMessage());
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("✗ Error en GlobalListener: " + e.getMessage());
        }
    }
}
