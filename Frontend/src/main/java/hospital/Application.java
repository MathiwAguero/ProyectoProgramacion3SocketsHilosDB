package hospital;

import hospital.Presentation.Login.Login;
import hospital.Presentation.Login.ModelUsuarios;
import hospital.Presentation.Login.UsuariosController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Application {

    public static JFrame window;
    public static final Color BACKGROUND_ERROR = new Color(255, 200, 200);

    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            System.err.println("No se pudo cargar Nimbus Look and Feel");
        }

        // Ejecutar en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                initializeApplication();
            } catch (Exception e) {
                mostrarErrorConexion(e);
            }
        });
    }

    private static void initializeApplication() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SISTEMA HOSPITAL - CLIENTE          ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        try {
            // Verificar conexión al servidor
            System.out.println("→ Conectando al servidor...");
            hospital.logic.Service.instance();
            System.out.println("✓ Conexión establecida correctamente");
            System.out.println();

            // Crear ventana principal de login
            window = new JFrame("Hospital - Sistema de Recetas Médicas");

            // Configurar cierre
            window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    cerrarAplicacion();
                }
            });

            // Crear vista de login
            Login loginView = new Login();
            ModelUsuarios loginModel = new ModelUsuarios();
            UsuariosController loginController = new UsuariosController(loginView, loginModel);

            // Configurar ventana
            window.setContentPane(loginView.getLogin());
            window.setSize(650, 450);
            window.setLocationRelativeTo(null);
            window.setResizable(false);

            // Cargar icono (opcional)
            try {
                ImageIcon icon = new ImageIcon(
                        Application.class.getResource("/Images/Hospital.jpg")
                );
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    window.setIconImage(icon.getImage());
                }
            } catch (Exception e) {
                System.err.println("⚠ No se pudo cargar el icono de la aplicación");
            }

            // Mostrar ventana
            window.setVisible(true);

            System.out.println("✓ Interfaz gráfica iniciada");
            System.out.println("\n→ Sistema listo para usar");
            System.out.println("→ Puede iniciar sesión con:");
            System.out.println("   • Administrador: ADM-111 / 1234");
            System.out.println("   • Médico: MED-111 / 111");
            System.out.println("   • Farmaceuta: FAR-111 / 123");
            System.out.println();

        } catch (Exception e) {
            throw new RuntimeException("Error inicializando aplicación", e);
        }
    }

    private static void cerrarAplicacion() {
        int opcion = JOptionPane.showConfirmDialog(
                window,
                "¿Está seguro que desea salir del sistema?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            System.out.println("\n→ Cerrando aplicación...");

            try {
                // Cerrar conexión al servidor
                hospital.logic.Service.instance().stop();
                System.out.println("✓ Conexión cerrada correctamente");
            } catch (Exception e) {
                System.err.println("⚠ Error al cerrar conexión: " + e.getMessage());
            }

            System.out.println("✓ Aplicación cerrada");
            System.exit(0);
        }
    }

    private static void mostrarErrorConexion(Exception e) {
        System.err.println("\n╔════════════════════════════════════════╗");
        System.err.println("║    ✗ ERROR DE CONEXIÓN AL SERVIDOR    ║");
        System.err.println("╚════════════════════════════════════════╝");
        System.err.println("\nDetalles:");
        e.printStackTrace();

        String mensaje =
                "No se pudo conectar al servidor.\n\n" +
                        "Verifique que:\n" +
                        "• El servidor Backend está ejecutándose\n" +
                        "• El servidor está escuchando en localhost:5000\n" +
                        "• No hay firewall bloqueando la conexión\n\n" +
                        "Error: " + e.getMessage();

        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE
        );

        System.exit(-1);
    }
}