import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import raytracer.Scene;
import raytracer.Image;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;

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

    public Image computeDistributed(Scene scene, int largeur, int hauteur) throws RemoteException {
        int nbNoeuds = noeuds.size();
        if (nbNoeuds == 0) {
            throw new RemoteException("Aucun nœud enregistré");
        }

        System.out.println("[Coordinator] computeDistributed() pour scène " 
                       + largeur + "×" + hauteur 
                       + " sur " + nbNoeuds + " nœuds.");

        int nbParts = nbNoeuds; // Nombre de parties par dimension

        int partieLargeur = largeur / nbParts;
        int partieHauteur = hauteur / nbParts;
        
        Image imageFinale = new Image(largeur, hauteur);
        
        // Création d'un pool de threads pour gérer les calculs en parallèle
        ExecutorService executor = Executors.newFixedThreadPool(nbNoeuds);
        CompletionService<Fragment> completionService = new ExecutorCompletionService<>(executor);
        
        // Distribution des parties aux nœuds disponibles
        int partieIndex = 0;
        for (int i = 0; i < nbParts; i++) {
            for (int j = 0; j < nbParts; j++) {
                int x0 = i * partieLargeur;
                int y0 = j * partieHauteur;
                
                // Trouver un nœud disponible
                ServiceNoeudCalcul noeud = noeuds.get(partieIndex % nbNoeuds);
                partieIndex++;
                
                // Soumettre le calcul de la partie
                completionService.submit(() -> {
                    try {
                        Image img = noeud.computeImage(scene, x0, y0, partieLargeur, partieHauteur);
                        return new Fragment(x0, y0, img);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        
        // Récupération et assemblage des résultats
        try {
            for (int i = 0; i < nbParts * nbParts; i++) {
                Fragment frag = completionService.take().get();
                
                // Copier la partie dans l'image finale
                for (int x = 0; x < partieLargeur; x++) {
                    for (int y = 0; y < partieHauteur; y++) {
                        imageFinale.setPixel(frag.x0 + x, frag.y0 + y, frag.image.getPixel(x, y));
                    }
                }
            }
        } catch (Exception e) {
            throw new RemoteException("Erreur lors du calcul distribué", e);
        } finally {
            executor.shutdown();
        }

        return imageFinale;

    }

    private static class Fragment {
        final int x0, y0;
        final Image image;
        Fragment(int x0, int y0, Image image) {
            this.x0 = x0;
            this.y0 = y0;
            this.image = image;
        }
    }
    
}
