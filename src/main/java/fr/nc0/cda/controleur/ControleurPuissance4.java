/*
 * Copyright (c) 2024 Lucas Paulo, Younes Ouaammou, Nicolas Paul.
 * Use of this source code is governed by a BSD-style license
 * that can be found in the COPYRIGHT file.
 */

package fr.nc0.cda.controleur;

import fr.nc0.cda.modele.CoupInvalideException;
import fr.nc0.cda.modele.EtatPartie;
import fr.nc0.cda.modele.EtatPartieException;
import fr.nc0.cda.modele.joueur.Joueur;
import fr.nc0.cda.modele.puissance4.*;
import fr.nc0.cda.vue.Ihm;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur du jeu Puissance 4.
 *
 * <p>Cette classe gère le déroulement d'une partie de Puissance 4 en utilisant le modèle Puissance4
 * et l'interface utilisateur Ihm.
 */
public class ControleurPuissance4 extends ControleurTemplate {
  /** La longueur d'une grille de Puissance 4 */
  private static final int LONGUEUR = 7;

  /** La hauteur d'une grille de Puissance 4 */
  private static final int HAUTEUR = 7;

  /** Rotations disponibles par défaut */
  private static final int ROTATIONS_DISPONIBLES_DEFAUT = 4;

  /** La liste des joueurs */
  private final ArrayList<Joueur> lesJoueurs;

  /** Les rotations restantes pour chaque joueur */
  private List<Integer> rotationsRestantes;

  /** Le numéro du joueur dont c'est le tour (1 ou 2) */
  private int joueurCourant;

  /** La partie en cours de Puissance 4 */
  private Puissance4 puissance4;

  /** true si la partie peut se faire avec des rotations. */
  private boolean rotationActivee = false;

  /**
   * Constructeur de la classe ControleurPuissance4.
   *
   * @param ihm L'interface utilisateur pour les interactions avec le jeu.
   */
  public ControleurPuissance4(Ihm ihm) {
    super(ihm);

    lesJoueurs = new ArrayList<>(2);
    lesJoueurs.add(new Joueur(demanderNomJoueur(1)));
    lesJoueurs.add(new Joueur(demanderNomJoueur(2)));
  }

  /**
   * Demande le nom d'un joueur
   *
   * @param numJoueur le numéro du joueur demandé
   * @return le nom entré
   */
  private String demanderNomJoueur(int numJoueur) {
    return ihm.demanderString("Saisissez le nom du joueur " + numJoueur);
  }

  @Override
  String creerAffichagePlateau() {
    return puissance4.getGrille().toString();
  }

  @Override
  EtatPartie getEtatPartie() {
    return puissance4.getEtat();
  }

  @Override
  void initialiserPartie() {
    puissance4 = new Puissance4(LONGUEUR, HAUTEUR);
    rotationActivee =
        ihm.demanderBoolean("Voulez-vous activer la possibilité de rotation de la grille ?");
    rotationsRestantes =
        new ArrayList<>(List.of(ROTATIONS_DISPONIBLES_DEFAUT, ROTATIONS_DISPONIBLES_DEFAUT));
    joueurCourant = 0;
  }

  @Override
  void jouerCoup() throws CoupInvalideException, EtatPartieException {
    Joueur joueur = getJoueur(joueurCourant);

    // Vérifier si le joueur courant peut et veut jouer une rotation.
    boolean avecRotations = rotationActivee;

    if (avecRotations) avecRotations = rotationsRestantes.get(joueurCourant - 1) > 0;

    if (avecRotations) {
      ChoixJouer choix = null;

      while (choix == null) {
        String input =
            ihm.demanderString(joueur.getNom() + ", que voulez-vous faire ? (jouer/rotation)")
                .toLowerCase();

        switch (input) {
          case "jouer", "j":
            choix = ChoixJouer.JOUER;
            break;
          case "rotation", "r":
            choix = ChoixJouer.ROTATION;
            break;
          default:
            ihm.afficherErreur("Veuillez choisir entre \"jouer\" et \"rotation\".");
        }
      }
    }

    // Jouer
    if (avecRotations) {
      // Diminuer les rotations restantes du joueur curant, puis jouer.
      rotationsRestantes.set(joueurCourant - 1, rotationsRestantes.get(joueurCourant - 1) - 1);

      while (true) {
        String choix =
            ihm.demanderString(
                    joueur.getNom()
                        + ", dans quel sens voulez-vous tourner la grille ? (droite/gauche)")
                .toLowerCase();

        switch (choix) {
          case "droite", "d", "horaire":
            puissance4.rotationner(Rotation.ANTI_HORAIRE);
            return;
          case "gauche", "g", "anti-horaire":
            puissance4.rotationner(Rotation.HORAIRE);
            return;
          default:
            ihm.afficherErreur("Veuillez choisir \"droite\" et \"gauche\".");
        }
      }
    } else {
      while (true) {
        int colonne =
            ihm.demanderInt(joueur.getNom() + ", choisissez une colonne (1-" + LONGUEUR + ")");

        if (puissance4.colonneInvalide(colonne)) {
          ihm.afficherErreur("Veuillez choisir une colonne valide.");
          continue;
        }

        Cellule cellule = joueurCourant == 1 ? Cellule.ROUGE : Cellule.JAUNE;
        puissance4.jouer(cellule, colonne);
        break;
      }
    }
  }

  @Override
  Joueur getJoueur(int numeroJoueur) {
    if (numeroJoueur < 1 || numeroJoueur > lesJoueurs.size()) return null;
    return lesJoueurs.get(numeroJoueur - 1);
  }

  @Override
  void changerJoueurCourant() {
    if (joueurCourant == 1) ++joueurCourant;
    else if (joueurCourant == 2) --joueurCourant;
    else joueurCourant = 1; // unreachable.
  }
}
