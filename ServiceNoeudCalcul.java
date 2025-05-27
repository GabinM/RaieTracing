import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceNoeudCalcul {

    Image computeImage(scene, int x, int y, int w, int l) throws RemoteException;
}