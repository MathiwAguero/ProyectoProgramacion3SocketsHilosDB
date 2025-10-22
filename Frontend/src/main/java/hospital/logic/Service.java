package hospital.logic;

import hospital.Entities.Entities.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Service{
    private static Service theInstance;
    public static Service instance(){
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }
    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;
    public Service() {
        try {
            s = new Socket(Protocol.SERVER, Protocol.PORT);
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
        } catch (Exception e) { System.exit(-1);}
    }

    private void disconnect() throws Exception {
        os.writeInt(Protocol.DISCONNECT);
        os.flush();
        s.shutdownOutput();
        s.close();
    }

    public void stop() {
        try {
            disconnect();
        } catch (Exception e) {
            System.exit(-1);
        }
    }
}
