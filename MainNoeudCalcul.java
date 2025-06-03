import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainNoeudCalcul {
    public static void main(String[] args) throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(args[0]);
        System.out.println("reg : " + String.valueOf(registry));
        ServiceDistributeur serveur = (ServiceDistributeur) registry.lookup("tableauBlanc");

        NoeudCalcul noeud = new NoeudCalcul();

        ServiceNoeudCalcul service = (ServiceNoeudCalcul) UnicastRemoteObject.exportObject(noeud, 0);

        serveur.enregistrerClient(service);

    }
}