/*
 * Copyright (c) 2024 Lucas Paulo, Younes Ouaammou, Nicolas Paul.
 * Use of this source code is governed by a BSD-style license
 * that can be found in the COPYRIGHT file.
 */

package fr.nc0.cda.nim.modele;

import java.util.ArrayList;
import java.util.List;

/** Représente une partie du jeu de Nim. */
public class Nim {
  /** Constante pour le joueur 1 */
  public static final int JOUEUR_1 = 1;

  /** Constante pour le joueur 2 */
  public static final int JOUEUR_2 = 2;

  /**
   * Liste des tas de la partie, sous forme d'une liste. L'élément i de cette liste correspond au
   * nombre d'allumettes dans le tas i.
   */
  private final List<Integer> tas;

  /** État de la partie. */
  private EtatPartieNim etat = EtatPartieNim.EN_COURS;

  /** Le joueur courant. */
  private int joueurCourant = JOUEUR_1;

  /**
   * Créer une partie et l'initialise avec le nombre de tas donné.
   *
   * @param nombreTas le nombre de tas de la partie.
   */
  public Nim(int nombreTas) {
    if (nombreTas < 1) throw new IllegalArgumentException("Nombre de Tas choisis < 1");

    tas = new ArrayList<>(nombreTas);
    for (int i = 1; i <= nombreTas; ++i) tas.add(2 * i - 1);
  }

  /** Change le joueur courant. */
  private void changerJoueurCourant() {
    joueurCourant = joueurCourant == JOUEUR_1 ? JOUEUR_2 : JOUEUR_1;
  }

  /**
   * Vérifie que le tas donné existe
   *
   * @param tas le numéro du tas
   * @return true si le tas n'existe pas, false sinon
   */
  private boolean verifierNumeroTas(int tas) {
    return tas < 0 || tas >= this.tas.size();
  }

  /**
   * Vérifie que le nombre d'allumettes donné est valide
   *
   * @param tas le tas
   * @param nombre le nombre d'allumettes
   * @return true si le nombre d'allumettes est invalide, false sinon
   */
  private boolean verifierNombre(int tas, int nombre) {
    return nombre <= 0 || nombre > this.tas.get(tas);
  }

  /**
   * Retire le nombre d'allumettes donné du tas donné.
   *
   * @param tasNumber le numéro du tas
   * @param nombre le nombre d'allumettes à retirer
   */
  private void retirerAllumettes(int tasNumber, int nombre) {
    int allumettes = tas.get(tasNumber);
    tas.set(tasNumber, allumettes - nombre);
  }

  private void verifierEtatPartie() {
    for (int allumettes : tas) if (allumettes > 0) return;

    etat =
        joueurCourant == JOUEUR_1
            ? EtatPartieNim.VICTOIRE_JOUEUR_2
            : EtatPartieNim.VICTOIRE_JOUEUR_1;
  }

  /**
   * Récupère la liste des tas de la partie.
   *
   * @return la liste des tas de la partie.
   */
  public List<Integer> getTas() {
    return tas;
  }

  /**
   * Retourne l'état de la partie.
   *
   * @return l'état de la partie.
   */
  public EtatPartieNim getEtat() {
    return etat;
  }

  /**
   * Retourne le joueur courant.
   *
   * @return le joueur courant.
   */
  public int getJoueurCourant() {
    return joueurCourant;
  }

  /**
   * Joue un tour de jeu.
   *
   * @param tas le tas sur lequel retirer des allumettes
   * @param nombre le nombre d'allumettes à retirer
   * @throws IllegalArgumentException si le choix est invalide ou le tas est vide
   * @throws IllegalStateException si la partie est terminée
   */
  public void jouer(int tas, int nombre) {
    if (etat != EtatPartieNim.EN_COURS) throw new IllegalStateException("La partie est terminée");
    if (!verifierNumeroTas(tas)) throw new IllegalArgumentException("Le tas donné est invalide");
    if (!verifierNombre(tas, nombre))
      throw new IllegalArgumentException("Le tas donné ne contient pas assez d'allumettes");

    retirerAllumettes(tas, nombre);
    verifierEtatPartie();
    changerJoueurCourant();
  }
}
