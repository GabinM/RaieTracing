import raytracer.Scene;
import raytracer.Image;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import raytracer.Disp;

public class RaytracingClient {
    public static void main(String[] args) {
        String hostCoord = "localhost";
        int portCoord    = 1099;

        String fichier_description="simple.txt";
        int largeur = 512, hauteur = 512;

        try {
            // 1) Crée la scène locale (ser- / désérializable)
            Scene scene = new Scene(fichier_description, largeur, hauteur);

            // 2) Lookup du coordinateur dans le registre distant
            Registry registry = LocateRegistry.getRegistry(hostCoord, portCoord);
            ServiceCoordinateur coord = 
                (ServiceCoordinateur) registry.lookup("ServiceCoordinateur");

            System.out.println("[Client] Envoi de la scène (" + largeur + "×" + hauteur + ") au coordinateur...");

            // 3) Appelle computeDistributed(scene) et reçoit une Image complète
            Image imageFinale = coord.computeDistributed(scene, largeur, hauteur);

            // 4) Affiche l’image
            Disp disp = new Disp("Résultat Raytracing Distribué", largeur, hauteur);
            disp.setImage(imageFinale, 0, 0);

            System.out.println("[Client] Image finale reçue et affichée.");

        } catch (Exception e) {
            System.err.println("[Client] Erreur :");
            e.printStackTrace();
        }
    }
}
