import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import raytracer.Scene;
import raytracer.Image;

public interface ServiceCoordinateur extends Remote {

    /**
     * Méthode qui va enregistrer un noeud
     * @param noeud
     * @throws RemoteException
     */
    void enregistrerNoeud(ServiceNoeudCalcul noeud) throws RemoteException;

    /**
     * Méthode qui va retourner la liste des noeuds enregistrés
     * @return
     * @throws RemoteException
     */
    List<ServiceNoeudCalcul> listRegisteredNodes() throws RemoteException; 

    /**
     * Méthode qui va découper l'image et la distribuer à chaque noeud
     * @param scene
     * @return
     * @throws RemoteException
     */
    Image computeDistributed(Scene scene) throws RemoteException;
}
