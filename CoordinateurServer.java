import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe qui va lancer le serveur Coordinateur
 */
public class CoordinateurServer {

    public static void main(String[] args) {
        try{
            // Port par défaut
            int port = 1099;
            
            // Récupération du port dans les arguments si spécifié
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            CoordinateurImpl coordinateur = new CoordinateurImpl();

            ServiceCoordinateur coordinateurExport = (ServiceCoordinateur) UnicastRemoteObject.exportObject(coordinateur, 0);

            // Création ou récupération du registry
            Registry registry = null;
            try {
                // Essaie de créer un nouveau registry
                registry = LocateRegistry.createRegistry(port);
                System.out.println("RMI Registry créé sur le port " + port);
            } catch (Exception e) {
                // Si un registry existe déjà, on le récupère
                System.out.println("RMI Registry existe déjà, récupération...");
                registry = LocateRegistry.getRegistry(port);
            }

            registry.rebind("ServiceCoordinateur", coordinateurExport);

            System.out.println("[CoordinatorServer] Coordinateur prêt sur port 1099 avec le nom 'ServiceCoordinateur'.");
            System.out.println("En attente d’enregistrement de ComputeNodes et de requêtes clients...");
        } catch (Exception e) {
            System.err.println("[CoordinatorServer] Erreur au démarrage :");
            e.printStackTrace();
        }
    }
}

