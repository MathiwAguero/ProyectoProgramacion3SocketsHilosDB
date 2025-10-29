package hospital.Logic;

import hospital.Entities.Entities.Protocol;
import hospital.Presentation.ThreadListener;

import javax.management.NotificationListener;
import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketListener {
    ThreadListener listener;
    String sid;
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;

    public SocketListener(ThreadListener listener, String sid) throws Exception {
        this.listener = listener;
        this.sid = sid;

        as = new Socket(Protocol.SERVER, Protocol.PORT);
        aos = new ObjectOutputStream(as.getOutputStream());
        ais = new ObjectInputStream(as.getInputStream());

        aos.writeInt(Protocol.ASYNC);
        aos.writeObject(sid);
        aos.flush();
    }

    boolean condition = true;
    private Thread t;

    public void start() {
        t = new Thread(() -> listen());
        condition = true;
        t.start();
    }

    public void stop() {
        condition = false;
    }

    public void listen() {
        int method;
        while (condition) {
            try {
                method = ais.readInt();

                switch (method) {
                    case Protocol.DELIVER_MESSAGE:
                        String message = (String) ais.readObject();
                        SwingUtilities.invokeLater(new  Runnable(){
                            public void run(){ listener.deliver_message(message); }
                            }
                        );
                        break;
                }
            } catch (Exception ex) {
                condition = false;
            }
        }

        try {
            if (as != null) as.close();
        } catch (Exception e) {}
    }
}