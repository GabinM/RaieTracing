import java.rmi.Remote;
import java.rmi.RemoteException;
import raytracer.Scene;
import raytracer.Image;

public interface ServiceNoeudCalcul extends Remote {

    boolean isAlreadyUsed() throws RemoteException;

    Image computeImage(Scene scene, int x, int y, int w, int l) throws RemoteException;
}