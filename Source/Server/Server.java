package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    private static final String DEFAULT_IP_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_PORT = 1099;

    private Registry registry;

    public Server() {
        this(DEFAULT_IP_ADDRESS, DEFAULT_PORT);
    }

    public static Server launchWithArgs(String... args) {
        // Extract ip address and port from args
        String ipAddress = DEFAULT_IP_ADDRESS;
        int port = DEFAULT_PORT;

        if (args.length > 0)
            ipAddress = args[0];

        int dividerIndex = ipAddress.indexOf(':');
        if (dividerIndex >= 0) {
            port = Integer.parseInt(ipAddress.substring(dividerIndex + 1));
            ipAddress = ipAddress.substring(0, dividerIndex);
        }
        else if (args.length > 1)
            port = Integer.parseInt(args[1]);
        return new Server(ipAddress, port);
    }

    public Server(String ipAddress) {
        this(ipAddress, DEFAULT_PORT);
    }

    public Server(String ipAddress, int port) {
        try{
            System.setProperty("java.security.policy","file:permissions.policy");
            System.setProperty("java.rmi.server.hostname", ipAddress);
            LocateRegistry.createRegistry(port);
            registry = LocateRegistry.getRegistry(port);

            ShapeListServant shapeList = new ShapeListServant();
            UserListServant userList = new UserListServant();

            registry.bind("ShapeList", shapeList);
            registry.bind("UserList", userList);

            System.out.println("CanvasView server ready");
        }catch(Exception e) {
            System.out.println("CanvasView server exception: " + e.getMessage());
        }
    }

    public static void main(String... args){
        Server.launchWithArgs(args);
    }
}