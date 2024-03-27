/*
 * Copyright (c) 2024 Lucas Paulo, Younes Ouaammou, Nicolas Paul.
 * Use of this source code is governed by a BSD-style license
 * that can be found in the COPYRIGHT file.
 */

package fr.nc0.cda.nim.controleur;

import fr.nc0.cda.nim.modele.EtatPartieNim;
import fr.nc0.cda.nim.modele.Joueur;
import fr.nc0.cda.nim.modele.Nim;
import fr.nc0.cda.nim.vue.Ihm;
import java.util.ArrayList;
import java.util.List;

/** Contrôleur du jeu de Nim. */
public class ControleurJeuNim {
  /** Interface homme-machine. */
  private final Ihm ihm;

  /** Liste des joueurs de la partie. */
  private final List<Joueur> joueurs = new ArrayList<>(2);

  /** Nombre de tas pour les parties. */
  private final int nombreTas;

  /**
   * Créer un contrôleur de jeu de Nim.
   *
   * @param ihm l'interface homme-machine.
   */
  public ControleurJeuNim(Ihm ihm) {
    this.ihm = ihm;
    this.nombreTas = demanderNombreTas();

    joueurs.add(new Joueur(ihm.selectNomJoueur(1)));
    joueurs.add(new Joueur(ihm.selectNomJoueur(2)));
  }

  /**
   * Demande le nombre de tas pour le plateau de la partie.
   *
   * @return le nombre de tas.
   * @throws IllegalArgumentException si le nombre de tas est inférieur à 1.
   */
  private int demanderNombreTas() {
    while (true) {
      try {
        return ihm.demanderNombreTas();
      } catch (IllegalArgumentException e) {
        ihm.afficherErreur(e);
      }
    }
  }

  /**
   * Demande le nombre d'allumettes à supprimer et dans quel tas.
   *
   * @param joueur le joueur qui doit jouer.
   * @param nim le jeu de Nim.
   * @return un tableau contenant le numéro du tas et le nombre d'allumettes à supprimer.
   */
  private int[] recupererChoix(Joueur joueur, Nim nim) {
    while (true) {
      try {
        int[] choix = ihm.demanderChoix(joueur.getNom());
        int tas = choix[0];
        int allumettes = choix[1];

        if (tas < 1 || tas > nombreTas)
          throw new IllegalArgumentException(
              "Le numéro du tas doit être compris entre 1 et " + nombreTas);

        if (allumettes < 1 || allumettes > nim.getTas().get(tas - 1))
          throw new IllegalArgumentException(
              "Le nombre d'allumettes doit être compris entre 1 et " + nim.getTas().get(tas - 1));

        return choix;
      } catch (IllegalArgumentException e) {
        ihm.afficherErreur(e);
      }
    }
  }

  /**
   * Détermine le joueur qui doit jouer.
   *
   * @param nim le jeu de Nim.
   * @return le joueur qui doit jouer.
   */
  private Joueur determinerJoueur(Nim nim) {
    return joueurs.get(nim.getJoueurCourant() - 1);
  }

  /**
   * Retourne le joueur vainqueur de la partie donnée.
   *
   * @param nim le jeu de Nim.
   * @return le joueur vainqueur.
   */
  private Joueur determinerVainqueur(Nim nim) {
    if (nim.getEtat() == EtatPartieNim.VICTOIRE_JOUEUR_1) return joueurs.get(0);
    else return joueurs.get(1);
  }

  /**
   * Retourne le joueur perdant de la partie donnée.
   *
   * @param nim le jeu de Nim.
   * @return le joueur perdant.
   */
  private Joueur determinerPerdant(Nim nim) {
    if (nim.getEtat() == EtatPartieNim.VICTOIRE_JOUEUR_2) return joueurs.get(0);
    else return joueurs.get(1);
  }

  /** Affiche le résumé des parties jouées. */
  private void afficherResume() {
    Joueur joueur1 = joueurs.get(0);
    Joueur joueur2 = joueurs.get(1);

    if (joueur1.getNbrPartieGagnee() > joueur2.getNbrPartieGagnee())
      ihm.afficherGagnant(joueur1, joueur2, false);
    else if (joueur1.getNbrPartieGagnee() < joueur2.getNbrPartieGagnee())
      ihm.afficherGagnant(joueur2, joueur1, false);
    else ihm.afficherGagnant(joueur1, joueur2, true);
  }

  /** Jouer une partie du jeu de Nim. */
  public void jouer() {
    Nim nim = new Nim(nombreTas);

    // Game loop principale
    while (nim.getEtat() == EtatPartieNim.EN_COURS) {
      ihm.afficherTas(nim.getTas());
      Joueur currentPlayer = determinerJoueur(nim);

      int[] choix = recupererChoix(currentPlayer, nim);
      int tas = choix[0];
      int allumettes = choix[1];

      nim.jouer(tas, allumettes);
    }

    // Fin de la partie, il faut calculer et afficher le vainqueur, mettre à jour les statistiques
    // des joueurs, ainsi que de recommencer une nouvelle partie au besoin.
    Joueur gagnant = determinerVainqueur(nim);
    Joueur perdant = determinerPerdant(nim);

    gagnant.ajouterPartieGagnee();

    boolean rejouer = ihm.demanderRejouer(gagnant, perdant);
    if (!rejouer) {
      afficherResume();
      return;
    }

    jouer();
  }
}
