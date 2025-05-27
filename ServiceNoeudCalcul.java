import java.rmi.RemoteException;
import raytracer.Scene;
import raytracer.Image;

public interface ServiceNoeudCalcul {

    boolean isStillActive() throws RemoteException;

    Image computeImage(Scene scene, int x, int y, int w, int l) throws RemoteException;
}