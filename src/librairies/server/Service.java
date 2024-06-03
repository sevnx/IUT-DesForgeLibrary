package librairies.server;

import librairies.communication.Protocol;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public abstract class Service extends SocketProtocolLink {
    private boolean needToWait = true;
    private volatile boolean isRunning = false;

    public Service(Socket socket, Class<? extends Protocol> protocol) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        super(socket, protocol);
    }

    public Service(Socket socket) throws IOException {
        super(socket);
    }

    public void stopWaiting() {
        needToWait = false;
    }

    protected abstract List<Class<? extends Component>> componentList();

    @Override
    public void run() {
        isRunning = true;
        try {
            while (isRunning) {
                this.protocol.setupCommunication();
                for(Class<? extends Component> component : componentList()) {
                    component.getConstructor().newInstance().call(this);
                }
                while (this.needToWait);
            }
        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void close() throws IOException {
        this.client.close();
        this.protocol.close();
    }
}
