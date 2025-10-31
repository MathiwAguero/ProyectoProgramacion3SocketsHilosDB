package hospital.Logic;

import hospital.Presentation.ThreadListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Mantiene una conexión global al servidor para recibir mensajes asíncronos
 * y distribuirlos a todos los controladores interesados.
 */
public class GlobalSocket {

    private static GlobalListener listener;
    private static final List<ThreadListener> registeredControllers = new ArrayList<>();

    public static void init() {
        if (listener == null) {
            try {
                // Usa el mismo puerto que el backend (ajusta si no es 5000)
                Socket socket = new Socket("127.0.0.1", 5000);
                listener = new GlobalListener(socket, registeredControllers);
                listener.start();
                System.out.println("✓ Listener global inicializado correctamente.");
            } catch (IOException e) {
                System.err.println("✗ Error al iniciar GlobalSocket: " + e.getMessage());
            }
        } else {
            System.out.println("⚠ Listener global ya estaba inicializado.");
        }
    }

    public static void register(ThreadListener controller) {
        if (!registeredControllers.contains(controller)) {
            registeredControllers.add(controller);
            System.out.println("→ Controller registrado: " + controller.getClass().getSimpleName());
        }
    }

    public static void unregister(ThreadListener controller) {
        registeredControllers.remove(controller);
    }
}
