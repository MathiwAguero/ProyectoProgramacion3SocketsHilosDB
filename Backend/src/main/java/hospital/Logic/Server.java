package hospital.Logic;

import hospital.Entities.Entities.Protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    ServerSocket ss;
    List<Worker> workers;
    Service service;

    public Server() {
        try {
            ss = new ServerSocket(Protocol.PORT);
            workers = Collections.synchronizedList(new ArrayList<Worker>());
            service = Service.getInstance();
            System.out.println("===========================================");
            System.out.println("Servidor Hospital iniciado correctamente");
            System.out.println("Puerto: " + Protocol.PORT);
            System.out.println("Esperando conexiones...");
            System.out.println("===========================================");
        } catch (IOException ex) {
            System.err.println("ERROR CRÍTICO: No se pudo iniciar el servidor");
            System.err.println("Causa: " + ex.getMessage());
            System.exit(-1);
        }
    }

    public void run() {
        boolean continuar = true;
        Socket s;
        Worker worker;

        while (continuar) {
            try {
                s = ss.accept();
                System.out.println("\n>>> Nueva conexión establecida desde: " +
                        s.getInetAddress().getHostAddress());

                worker = new Worker(this, s, service);
                workers.add(worker);

                System.out.println(">>> Total de clientes conectados: " + workers.size());

                worker.start();

            } catch (IOException ex) {
                System.err.println("Error aceptando cliente: " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("Error inesperado: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void remove(Worker w) {
        workers.remove(w);
        System.out.println("<<< Cliente desconectado. Quedan: " + workers.size() + " clientes");
    }

    public void stop() {
        try {
            if (ss != null && !ss.isClosed()) {
                ss.close();
                System.out.println("Servidor detenido correctamente");
            }
        } catch (IOException e) {
            System.err.println("Error cerrando servidor: " + e.getMessage());
        }
    }
}