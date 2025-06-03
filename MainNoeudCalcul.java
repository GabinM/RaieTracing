import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainNoeudCalcul {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        if (args.length != 2) {
            System.out.println("Usage : java MainNoeudCalcul <hostCoord> <portCoord>");
            System.out.println("Exemple : java MainNoeudCalcul localhost 1099");
            System.exit(0);
        }
        String hostCoord = args[0];
        int portCoord    = Integer.parseInt(args[1]);
        try {
            // 1) Crée un registre RMI local sur un port aléatoire (0)
            Registry localRegistry = LocateRegistry.createRegistry(0);

            // 2) Instancie NoeudCalcul
            NoeudCalcul nodeImpl = new NoeudCalcul();

            // 3) Exporte explicitement l’instance sur un port dynamique (0)
            ServiceNoeudCalcul stubNode = 
                (ServiceNoeudCalcul) UnicastRemoteObject.exportObject(nodeImpl, 0);

            // 4) Bind le stub dans le registre local sous "ServiceNoeudCalcul"
            localRegistry.rebind("ServiceNoeudCalcul", stubNode);

            // 5) Récupère le stub du coordinateur dans son registre distant
            Registry registryCoord = LocateRegistry.getRegistry(hostCoord, portCoord);
            ServiceCoordinateur coord = 
                (ServiceCoordinateur) registryCoord.lookup("ServiceCoordinateur");

            // 6) S’enregistre auprès du coordinateur
            coord.enregistrerNoeud(stubNode);

            System.out.println("[MainNoeudCalcul] NoeudCalcul enregistré auprès du coordinateur.");
            System.out.println("En attente d'appels computeImage(...)...");

        } catch (Exception e) {
            System.err.println("[ComputeNodeServer] Erreur au démarrage :");
            e.printStackTrace();
        }

    }
}