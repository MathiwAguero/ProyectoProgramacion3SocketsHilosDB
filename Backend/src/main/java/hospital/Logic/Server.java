package hospital.Logic;

import hospital.Entities.Entities.Protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.join;

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
        String sid;
        while (continuar) {
            try {
                s = ss.accept();
                System.out.println("\n>>> Nueva conexión establecida desde: " +
                        s.getInetAddress().getHostAddress());
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                int type = is.readInt();

                switch (type) {
                    case Protocol.SYNC:
                        sid = s.getRemoteSocketAddress().toString();
                        System.out.println("SYNC: " + sid);

                        worker = new Worker(this, s, service, os, is, sid);
                        workers.add(worker);
                        worker.start();

                        os.writeObject(sid); // Enviar Session ID
                        break;

                    case Protocol.ASYNC:
                        sid = (String) is.readObject();
                        System.out.println("ASYNC: " + sid);
                        join(s, os, is, sid);
                        break;
                }

                os.flush();

            } catch (IOException | ClassNotFoundException ex) {
                System.err.println("Error: " + ex.getMessage());
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

    public void join(Socket as, ObjectOutputStream aos, ObjectInputStream ais, String sid) {
        for (Worker w : workers) {
            if (w.sid.equals(sid)) {
                w.setAs(as, aos, ais);
                break;
            }
        }
    }
    public void deliver_message(Worker from, String message) {
        for (Worker w : workers) {
            if (w != from) w.deliver_message(message);
        }
    }
}