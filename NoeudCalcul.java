import raytracer.Scene;
import raytracer.Image;
import java.rmi.RemoteException;

public class NoeudCalcul implements ServiceNoeudCalcul {

    public Image computeImage(Scene scene, int x, int y, int w, int l) throws RemoteException {
        Image partieImage = scene.compute(x, y, w, l);
        return partieImage;
    }

    public boolean isStillActive() throws RemoteException {
        return true;
    }

}