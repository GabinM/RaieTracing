import java.time.Instant;
import java.time.Duration;

import raytracer.Disp;
import raytracer.Scene;
import raytracer.Image;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";
     
    public static void main(String args[]){

        // Le fichier de description de la scène si pas fournie
        String fichier_description="simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512;
        
        if(args.length > 0){
            fichier_description = args[0];
            if(args.length > 1){
                largeur = Integer.parseInt(args[1]);
                if(args.length > 2)
                    hauteur = Integer.parseInt(args[2]);
            }
        }else{
            System.out.println(aide);
        }

        try {
            // 1) Crée la scène locale (ser- / désérializable)
            Scene scene = new Scene(fichier_description, largeur, hauteur);

            // 2) Lookup du coordinateur dans le registre distant
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServiceCoordinateur coord = 
                (ServiceCoordinateur) registry.lookup("ServiceCoordinateur");

            System.out.println("[Client] Envoi de la scène (" + largeur + "×" + hauteur + ") au coordinateur...");

            // Chronométrage du temps de calcul total
            Instant debutTotal = Instant.now();
            System.out.println("Calcul de l'image en 4 parties :");

            // 3) Appelle computeDistributed(scene) et reçoit une Image complète
            Image imageFinale = coord.computeDistributed(scene, largeur, hauteur);

            // 4) Affiche l’image
            Disp disp = new Disp("Résultat Raytracing Distribué", largeur, hauteur);
            disp.setImage(imageFinale, 0, 0);

            long dureeTotal = Duration.between(debutTotal, Instant.now()).toMillis();
            System.out.println("Image totale calculée en : " + dureeTotal + " ms");

            System.out.println("[Client] Image finale reçue et affichée.");

        } catch (Exception e) {
            System.err.println("[Client] Erreur :");
            e.printStackTrace();
        }
    }	
}
