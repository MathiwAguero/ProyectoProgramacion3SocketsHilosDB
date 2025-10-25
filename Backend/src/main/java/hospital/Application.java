package hospital;

import hospital.Data.HospitalData;
import hospital.Logic.Server;


public class Application {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SISTEMA HOSPITAL - BACKEND SERVER   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        try {
            // 1. Verificar conexión a base de datos
            System.out.println("→ Verificando conexión a base de datos...");
            hospital.Data.Database db = hospital.Data.Database.getDatabase();
            System.out.println("✓ Conexión a BD establecida correctamente");
            System.out.println();

            // 2. Inicializar datos del sistema (si están vacíos)
            System.out.println("→ Inicializando datos del sistema...");
            HospitalData.inicializarDatos();
            System.out.println();

            // 3. Iniciar servidor de sockets
            System.out.println("→ Iniciando servidor de sockets...");
            Server server = new Server();

            // 4. Hook para cierre limpio
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n→ Cerrando servidor...");
                try {
                    db.close();
                    System.out.println("✓ Servidor cerrado correctamente");
                } catch (Exception e) {
                    System.err.println("✗ Error al cerrar: " + e.getMessage());
                }
            }));

            // 5. Ejecutar servidor (loop infinito)
            server.run();

        } catch (Exception e) {
            System.err.println("\n╔════════════════════════════════════════╗");
            System.err.println("║      ✗ ERROR CRÍTICO EN SERVIDOR      ║");
            System.err.println("╚════════════════════════════════════════╝");
            System.err.println("\nDetalles del error:");
            e.printStackTrace();
            System.err.println("\nVerifique:");
            System.err.println("  • MySQL está corriendo");
            System.err.println("  • database.properties tiene credenciales correctas");
            System.err.println("  • La base de datos 'Hospital' existe");
            System.err.println("  • El puerto 5000 está disponible");
            System.exit(-1);
        }
    }
}