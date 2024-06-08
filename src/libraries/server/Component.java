package libraries.server;

/**
 * Represents a component that can call a service
 */
public interface Component {
    void call(Service service);
}
