package Client;

import Client.Models.ClientModel;
import Client.Models.UserServant;
import Client.Views.ApplicationView;
import Framework.Colour;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class CollaborativeCanvas extends javafx.application.Application {

    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 640;
    private static final String WINDOW_TITLE = "Collaborative Canvas";
    private static final String DEFAULT_IP_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_PORT = 1099;

    private String ipAddress;
    private int port;

    @Override
    public void start(Stage stage) {

        // Get Connection Information
        ipAddress = DEFAULT_IP_ADDRESS;
        port = DEFAULT_PORT;
        extractConnectionInformationFromArgs();

        // Build Models
        UserServant currentUser;
        ClientModel clientModel;
        try {
            currentUser = new UserServant(Colour.random());
            clientModel = new ClientModel(ipAddress, port, currentUser);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            return;
        }

        // Build Application
        ApplicationView applicationView = ApplicationBuilder.buildApplication(clientModel);

        // Show View
        Scene scene = new Scene(applicationView, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle(WINDOW_TITLE);
        stage.show();
    }

    private void extractConnectionInformationFromArgs() {
        // Extract ip address and port from args
        List<String> args = getParameters().getRaw();
        if (args.size() > 0)
            ipAddress = args.get(0);

        int dividerIndex = ipAddress.indexOf(':');
        if (dividerIndex >= 0) {
            port = Integer.parseInt(ipAddress.substring(dividerIndex + 1));
            ipAddress = ipAddress.substring(0, dividerIndex);
        }
        else if (args.size() > 1)
            port = Integer.parseInt(args.get(1));
    }

    public static void main(String... args) throws Exception {
        CollaborativeCanvas.launch(args);
    }
}