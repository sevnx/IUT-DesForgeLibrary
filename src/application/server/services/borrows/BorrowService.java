package application.server.services.borrows;

import application.server.services.borrows.components.BorrowDocumentComponent;
import application.server.services.borrows.components.BorrowWelcomeComponent;
import librairies.server.Component;
import librairies.server.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BorrowService extends Service {
    public BorrowService(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    protected List<Class<? extends Component>> componentList() {
        ArrayList<Class<? extends Component>> components = new ArrayList<>();

        components.add(BorrowWelcomeComponent.class);
        components.add(BorrowDocumentComponent.class);

        return components;
    }
}
