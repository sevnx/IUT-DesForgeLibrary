package librairies.server;

import librairies.communication.Protocol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;

public abstract class Service extends SocketProtocolLink {
    private boolean needToWait = true;
    private Class<? extends Component> component;
    private volatile boolean isRunning = false;

    public Service(Socket socket, Class<? extends Component> component, Class<? extends Protocol> protocol) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        super(socket, protocol);
        this.component = component;
    }

    public Service(Socket socket, Class<? extends Component> component) throws IOException {
        super(socket);
        this.component = component;
    }

    public void stopWaiting() {
        needToWait = false;
    }

    @Override
    public void run() {
        isRunning = true;
        try {
            while (isRunning) {
                this.protocol.setupCommunication();
                component.getConstructor().newInstance().call(this);
                while (this.needToWait) ;
            }
        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
