package com.example.ivan.recoammico;

/**
 * RECOMMENDATION Service for AMMICO application.
 * Compute content-based recommendation of POIs.
 *
 * Usage:
 * 1. From the activity, launch the recommendation service as soon as possible for pre-computations
 * (IntentService's are executed in background) with statements:
 *      Intent intent = new Intent(this, RecommendationService.class);
 *      intent.putExtra("dbPath", DB_PATH);
 *      intent.putExtra("poisContentPath", POIS_CONTENT_PATH);
 *      startService(intent);
 * specifying a database path (DB_PATH) and the xml file with textual description of POIs (POIS_CONTENT_PATH)
 * 2. Retrieve recommended POIs by calling the method getRecommendation().
 * Example:
 *      List<Integer> recommendedPOIs = RecommendationService.getRecommendation(VISITE_ID, MAX_NB_RECO);
 * where:
 *      VISITE_ID (String) is the visitor id (as stored in the DB)
 *      MAX_NB_RECO (Integer) is the maximum number of recommended POIs to retrieve.
 *
 * Note that the returned list can be EMPTY if recommendation failed for any reason (see logs).
 *
 * author: Ivan Keller
 * version: 2.1
 */

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecommendationService extends IntentService {

    public static final int MINIMUM_LIKED_POIS = 1;  //minimum number of previously liked POIs for getting recommendation
    private static List<Integer> allPOIs;
    private static final List EMPTY_LIST = Collections.unmodifiableList(new ArrayList());
    // for debugging:
    private static final String TAG = "reco";

    protected static String dbPath;
    protected static String poisContentPath;
    protected static String tourPath;

    private TourmlParser tourmlParser;

    public RecommendationService() {
        super("RecommandationService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.i(TAG, "RecommendationService onHandleIntent");
        dbPath = intent.getStringExtra("dbPath");
        poisContentPath = intent.getStringExtra("poisContentPath");
        tourPath = intent.getStringExtra("tourPath");

        POIsDBHandler PoisHandler = new POIsDBHandler();
        allPOIs = PoisHandler.getAllIdPOIs();
        int nbPOIs = allPOIs.size();
        Log.i(TAG, "Got all " + nbPOIs + " POI ids.");

        // uncomment the following lines for XML parsing. See TourmlParser.java
        //tourmlParser = new TourmlParser(tourPath);
        //tourmlParser.parseTourmlConnections();
    }

    // Return a list of recommended POI ids.
    // Return an empty list if the number of liked POIs if lower than MINIMUM_LIKED_POIS.
    public static List<Integer> getRecommendation(String visiteID, int nbOfRecommendedPOIs) {
        Log.i(TAG, "RecommendationService getRecommendation(visiteID=" + visiteID + ", nbOfRecommendedPOIs=" + nbOfRecommendedPOIs + ")");

        VisitesDBHandler VisitesHandler = new VisitesDBHandler(visiteID);

        List<Integer> likedIdPOIs = VisitesHandler.getLikedIdPOIs();
        Log.i(TAG, "Got " + likedIdPOIs.size() + " liked POIs");

        List<Integer> visitedIdPOIs = VisitesHandler.getVisitedIdPOIs();
        Log.i(TAG, "Got " + visitedIdPOIs.size() + " visited POIs");

        Integer lastPOI = VisitesHandler.getLastVisitedOrLikedPOI();
        Log.i(TAG, "Last POI: " + lastPOI);

        Log.i(TAG, "visited IdPOIs :");
        for (int idpoi : visitedIdPOIs) {
            Log.i(TAG, Integer.toString(idpoi));
        }

        List<Integer> unvisitedIdPOIs = allPOIs;
        // remove visited POIs:
        if (visitedIdPOIs.size() > 0) {
            unvisitedIdPOIs.removeAll(visitedIdPOIs);
        }
        // remove liked POIs:
        if (likedIdPOIs.size() > 0) {
            unvisitedIdPOIs.removeAll(likedIdPOIs);
        }
        Log.i(TAG, "Got " + unvisitedIdPOIs.size() + " not yet visited nor liked POIs :");

        List recommendedPOIs = EMPTY_LIST;
        if (likedIdPOIs.size() >= MINIMUM_LIKED_POIS) {
            // Première solution basique
            //recommendedPOIs = randomRecommendedPOIs(unvisitedIdPOIs, nbOfRecommendedPOIs);
            // Deuxième solution un peu meilleure pour test 1 à l'EPPPD :
            recommendedPOIs = customEPPPDRecommendedPOIs(lastPOI, visitedIdPOIs, likedIdPOIs, nbOfRecommendedPOIs);
            Log.i(TAG, "Got " + recommendedPOIs.size() + " recommended POIs :");
        } else {
            Log.i(TAG, "Not enough liked POIs to compute recommendation.");
        }
        return recommendedPOIs;
    }


    // Return a list of randomly chosen POI ids among unvisited POIs.
    // The number of POIs is at maximum nbOfRecommendedPOIs.
    private static List<Integer> randomRecommendedPOIs(List<Integer> unvisitedPOIs, int nbOfRecommendedPOIs) {
        int nbOfReco = Math.min(nbOfRecommendedPOIs, unvisitedPOIs.size());
        Collections.shuffle(unvisitedPOIs);
        return unvisitedPOIs.subList(0, nbOfReco);
    }

    // Solution temporaire pour les test 1 à l'EPPPD. POIs à recommander codés en dur.
    private static List<Integer> customEPPPDRecommendedPOIs(Integer lastPOI, List<Integer> visitedPOIs, List<Integer> likedPOIs, int nbOfRecommendedPOIs) {
        List<Integer> recommendedPOIs = EMPTY_LIST;

        // Parcours Libre, srcId (carte) inclus (premier element), extrait à la main du fichier tour-373.xml
        List<Integer> libreDiversite = Arrays.asList(556, 466, 465, 464, 371, 370, 418, 417);
        List<Integer> libreEmigrer = Arrays.asList(558, 449, 345, 391, 433, 389, 555, 410, 447, 388, 467, 393, 406, 435, 450, 436, 347, 390, 432, 434, 348, 408, 422, 346);
        List<Integer> libreEnracinement = Arrays.asList(559, 405, 463, 461, 368, 460, 459, 366, 420, 367, 421, 419);
        List<Integer> libreEtatTerreIci = Arrays.asList(560, 394, 350, 356, 454, 452, 451, 358, 453, 351, 355, 352, 354, 349, 357, 353, 423, 425, 424);
        List<Integer> libreLieux = Arrays.asList(561, 361, 398, 456, 360, 455, 430, 362, 397, 359, 426);
        List<Integer> libreTravail = Arrays.asList(557, 399, 401, 414, 400, 403, 457, 365, 458, 404, 363, 427);
        // Le POI 541 est la carte parcours libre. Non localise
        HashMap<Integer, List<Integer>> parcoursLibre = new HashMap<Integer, List<Integer>>();
        parcoursLibre.put(1, libreDiversite);
        parcoursLibre.put(2, libreEmigrer);
        parcoursLibre.put(3, libreEnracinement);
        parcoursLibre.put(4, libreEtatTerreIci);
        parcoursLibre.put(5, libreLieux);
        parcoursLibre.put(6, libreTravail);

        // Parcours Découverte, srcId (carte) NON inclus, extrait à la main du fichier tour-334.xml. POIs à recommander
        List<Integer> decouverteDiversite = Arrays.asList(466, 465, 464, 371, 370, 418, 417); //tous ajoutés car non présents dans le parcours découverte
        List<Integer> decouverteEmigrer = Arrays.asList(345, 467, 393, 347, 348, 346);
        List<Integer> decouverteEnracinement = Arrays.asList(368, 367);
        List<Integer> decouverteEtatTerreIci = Arrays.asList(350, 356, 358, 351, 355, 354, 349, 357, 353);
        List<Integer> decouverteLieux = Arrays.asList(361, 360, 362, 359);
        List<Integer> decouverteTravail = Arrays.asList(365, 363, 364);
        // Le POI 374 est la carte parcours decouverte. Non localise
        HashMap<Integer, List<Integer>> parcoursDecouverte = new HashMap<Integer, List<Integer>>();
        parcoursDecouverte.put(1, decouverteDiversite);
        parcoursDecouverte.put(2, decouverteEmigrer);
        parcoursDecouverte.put(3, decouverteEnracinement);
        parcoursDecouverte.put(4, decouverteEtatTerreIci);
        parcoursDecouverte.put(5, decouverteLieux);
        parcoursDecouverte.put(6, decouverteTravail);

        // localize zone of lastPOI, default 0 = nowhere
        Integer visitorLoc = 0;
        for (Integer i=1; i<=6; i++) {
            if(parcoursLibre.get(i).contains(lastPOI)) {
                visitorLoc = i;
                break;
            }
        }
        Log.i(TAG, "VisitorLoc = " + visitorLoc);

        // remove visited and liked POIs from surrounding local POIs
        if (visitorLoc != 0) {
            List<Integer> localPOIs = new ArrayList<>(parcoursDecouverte.get(visitorLoc)); //to avoid an UnsupportedOperationException in removeAll method below
            List<Integer> poisToRemove = new ArrayList<>(visitedPOIs);
            poisToRemove.addAll(likedPOIs);
            try {
                localPOIs.removeAll(poisToRemove);
            } catch (Exception e) {
                Log.i(TAG, "error" + e);
            }
            // at this stage localPOIs contains neighbouring remaining non visited nor liked POIs

            int nbOfReco = Math.min(nbOfRecommendedPOIs, localPOIs.size());
            Collections.shuffle(localPOIs);
            if (nbOfReco > 0) {
                recommendedPOIs = localPOIs.subList(0, nbOfReco);
            }
        }
        return recommendedPOIs;
    }
}
