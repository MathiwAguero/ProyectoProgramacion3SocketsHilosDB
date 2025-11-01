package hospital.Logic;

import hospital.Entities.Entities.Protocol;
import hospital.Presentation.ThreadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor centralizado de notificaciones del servidor.
 * Un solo SocketListener distribuye mensajes a múltiples controladores.
 */
public class NotificationManager {
    private static NotificationManager instance;
    private SocketListener socketListener;
    private final List<ThreadListener> listeners = new ArrayList<>();
    private boolean initialized = false;

    private NotificationManager() {}

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    /**
     * Inicializa el SocketListener una sola vez
     */
    public synchronized void initialize(String sid) {
        if (initialized) {
            System.out.println("⚠ NotificationManager ya inicializado");
            return;
        }

        try {
            // Crear un ThreadListener que distribuye a todos los registrados
            ThreadListener distributor = message -> {
                System.out.println("📨 Distribuyendo mensaje a " + listeners.size() + " listeners");
                for (ThreadListener listener : listeners) {
                    try {
                        listener.deliver_message(message);
                    } catch (Exception e) {
                        System.err.println("✗ Error entregando mensaje a listener: " + e.getMessage());
                    }
                }
            };

            socketListener = new SocketListener(distributor, sid);
            socketListener.start();
            initialized = true;
            System.out.println("✓ NotificationManager inicializado con SID: " + sid);

        } catch (Exception e) {
            System.err.println("✗ Error inicializando NotificationManager: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Registra un controller para recibir notificaciones
     */
    public synchronized void register(ThreadListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            System.out.println("✓ Listener registrado (" + listeners.size() + " total)");
        }
    }

    /**
     * Desregistra un controller
     */
    public synchronized void unregister(ThreadListener listener) {
        if (listeners.remove(listener)) {
            System.out.println("✓ Listener desregistrado (" + listeners.size() + " restantes)");
        }
    }

    /**
     * Detiene el SocketListener
     */
    public synchronized void shutdown() {
        if (socketListener != null) {
            socketListener.stop();
            socketListener = null;
        }
        listeners.clear();
        initialized = false;
        System.out.println("✓ NotificationManager detenido");
    }

    /**
     * Verifica si está inicializado
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Obtiene el número de listeners registrados
     */
    public int getListenerCount() {
        return listeners.size();
    }
}