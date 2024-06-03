package application.server.services.returns;

import application.server.services.returns.components.ReturnDocumentComponent;
import application.server.services.returns.components.ReturnWelcomeComponent;
import librairies.server.Component;
import librairies.server.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ReturnService extends Service {
    public ReturnService(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    protected List<Class<? extends Component>> componentList() {
        ArrayList<Class<? extends Component>> components = new ArrayList<>();

        components.add(ReturnWelcomeComponent.class);
        components.add(ReturnDocumentComponent.class);

        return components;
    }
}
