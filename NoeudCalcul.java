import raytracer.Scene;
import raytracer.Image;
import java.rmi.RemoteException;

public class NoeudCalcul implements ServiceNoeudCalcul {

    private boolean alreadyUsed = false;

    public Image computeImage(Scene scene, int x, int y, int w, int l) throws RemoteException {
        System.out.println("[NoeudCalcul] computeImage(" + x + "," + y + "," + w + "," + l + ")");
        return scene.compute(x, y, w, l);
    }

    public boolean isAlreadyUsed() throws RemoteException {
        return alreadyUsed;
    }

}