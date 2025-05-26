import java.time.Instant;
import java.time.Duration;

import raytracer.Disp;
import raytracer.Scene;
import raytracer.Image;

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
        
   
        // création d'une fenêtre 
        Disp disp = new Disp("Raytracer", largeur, hauteur);
        
        // Initialisation d'une scène depuis le modèle 
        Scene scene = new Scene(fichier_description, largeur, hauteur);
        
        // Calcul de l'image de la scène les paramètres : 
        // - x0 et y0 : correspondant au coin haut à gauche
        // - l et h : hauteur et largeur de l'image calculée
        // Ici on calcule toute l'image (0,0) -> (largeur, hauteur)

        int x = 2; // variable a modifier pour le nombre de parties
        
        int partieLargeur = largeur / x;
        int partieHauteur = hauteur / x;
        
        // Chronométrage du temps de calcul total
        Instant debutTotal = Instant.now();
        System.out.println("Calcul de l'image en 4 parties :");
        
        // Calcul des 4 parties de l'image
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                // Ne calculer que les parties en diagonale (haut-gauche et bas-droite)
                int x0 = i * partieLargeur;
                int y0 = j * partieHauteur;
                
                System.out.println("Calcul de la partie " + (i*x + j + 1) + " :");
                System.out.println(" - Coordonnées : " + x0 + "," + y0);
                System.out.println(" - Taille " + partieLargeur + "x" + partieHauteur);
                
                Instant debut = Instant.now();
                Image partieImage = scene.compute(x0, y0, partieLargeur, partieHauteur);
                Instant fin = Instant.now();
                
                long duree = Duration.between(debut, fin).toMillis();
                System.out.println("Partie calculée en : " + duree + " ms");
                
                // Affichage de la partie calculée
                disp.setImage(partieImage, x0, y0);
            }
        }
        
        long dureeTotal = Duration.between(debutTotal, Instant.now()).toMillis();
        System.out.println("Image totale calculée en : " + dureeTotal + " ms");
    }	
}
