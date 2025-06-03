import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import raytracer.Scene;
import raytracer.Image;
import java.util.Collections;

public class CoordinateurImpl implements ServiceCoordinateur {

    private final List<ServiceNoeudCalcul> noeuds = Collections.synchronizedList(new ArrayList<>());

    public CoordinateurImpl() {

    }

    @Override
    public void enregistrerNoeud(ServiceNoeudCalcul noeud) throws RemoteException {
        if (!noeuds.contains(noeud)) {
            noeuds.add(noeud);
            System.out.println("[Coordinateur] Nouveau nœud enregistré. Total = " + noeuds.size());
        }
    }

    @Override
    public List<ServiceNoeudCalcul> listRegisteredNodes() throws RemoteException {
        return noeuds;
    }

    public Image computeDistributed(Scene scene) throws RemoteException {
        // TODO : Découper l'image en plusieurs parties et les distribuer à chaque noeud
    }
    
    
}
