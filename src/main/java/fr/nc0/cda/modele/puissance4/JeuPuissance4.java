/*
 * Copyright (c) 2024 Lucas Paulo, Younes Ouaammou, Nicolas Paul.
 * Use of this source code is governed by a BSD-style license
 * that can be found in the COPYRIGHT file.
 */

package fr.nc0.cda.modele.puissance4;

import fr.nc0.cda.modele.CoupInvalideException;
import fr.nc0.cda.modele.EtatPartie;
import fr.nc0.cda.modele.EtatPartieException;
import java.util.ArrayList;
import java.util.List;

/** Représente une partie de Puissance 4. */
public class JeuPuissance4 {
  /** La grille de jeu. */
  private PlateauPuissance4 plateauPuissance4;

  /** L'état de la partie */
  private EtatPartie etatPartie = EtatPartie.EN_COURS;

  /** Créer une partie de Puissance 4 et la commence */
  public JeuPuissance4(int longueur, int hauteur) {
    if (longueur < 1) throw new IllegalArgumentException("La longueur doît être supérieure à 0");
    if (hauteur < 1) throw new IllegalArgumentException("La hauteur doît être supérieure à 0");

    this.plateauPuissance4 = new PlateauPuissance4(longueur, hauteur);
  }

  /**
   * Vérifie si la colonne donnée est pleine
   *
   * @param colonne La colonne à vérifier
   * @return true si la colonne est pleine, false sinon
   */
  private boolean colonnePleine(int colonne) {
    return this.plateauPuissance4.get(colonne, 1) != CellulePuissance4.VIDE;
  }

  /**
   * Vérifie si les cellules données sont égales (même couleur)
   *
   * @param cellules la liste des cellules à vérifier
   * @return true si les cellules sont égales, false sinon
   */
  private boolean cellulesEgales(CellulePuissance4... cellules) {
    for (int i = 1; i < cellules.length; i++) {
      if (cellules[i] != cellules[0]) return false;
    }

    return true;
  }

  /**
   * Insère un jeton dans la colonne donnée, en le plaçant au plus bas de la colonne.
   *
   * @param colonne La colonne dans laquelle insérer le jeton, entre 1 et la longueur de la grille
   * @param cellule La cellule à insérer
   * @return la ligne à laquelle le jeton a été inséré, ou -1 si la colonne est pleine
   */
  private int insererCellule(int colonne, CellulePuissance4 cellule) {
    // On parcourt la colonne de bas en haut. Par la gravité, nous savons que si
    // une cellule est vide, alors celles du dessus le sont aussi.
    for (int ligne = this.plateauPuissance4.getHauteur(); ligne > 0; --ligne) {
      if (this.plateauPuissance4.get(colonne, ligne) != CellulePuissance4.VIDE) continue;

      this.plateauPuissance4.set(colonne, ligne, cellule);
      return ligne;
    }

    return -1;
  }

  /**
   * Vérifie si une condition de victoire est remplie pour la cellule donnée
   *
   * @param colonne la colonne de la cellule insérée, entre 1 et la longueur de la grille
   * @param ligne la ligne de la cellule insérée, entre 1 et la hauteur de la grille
   * @return true si une condition de victoire est remplie, false sinon
   */
  private boolean celluleVictorieuse(int colonne, int ligne) {
    // ⚪ -> Une cellule, peu importe la couleur
    // 🔴 -> Cellule de la même couleur que la cellule actuelle
    // ⭕️ -> Cellule actuelle

    if (cellulesEgales(
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne + 1),
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne + 2),
        /* ⭕️🔴🔴🔴 */ this.plateauPuissance4.get(colonne, ligne + 3))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne - 1),
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne + 1),
        /* 🔴⭕️🔴🔴 */ this.plateauPuissance4.get(colonne, ligne + 2))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne - 2),
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne - 1),
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne),
        /* 🔴🔴⭕️🔴 */ this.plateauPuissance4.get(colonne, ligne + 1))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne - 3),
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne - 2),
        /* ⚪⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne - 1),
        /* 🔴🔴🔴⭕️ */ this.plateauPuissance4.get(colonne, ligne))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪⭕️ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 1, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 2, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 3, ligne))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne - 1, ligne),
        /* ⚪⚪⚪⭕️ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 1, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 2, ligne))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne - 2, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne - 1, ligne),
        /* ⚪⚪⚪⭕️ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 1, ligne))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne - 3, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne - 2, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne - 1, ligne),
        /* ⚪⚪⚪⭕️ */ this.plateauPuissance4.get(colonne, ligne))) return true;

    if (cellulesEgales(
        /* ⭕️⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪🔴⚪⚪ */ this.plateauPuissance4.get(colonne + 1, ligne + 1),
        /* ⚪⚪🔴⚪ */ this.plateauPuissance4.get(colonne + 2, ligne + 2),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 3, ligne + 3))) return true;

    if (cellulesEgales(
        /* 🔴⚪⚪⚪ */ this.plateauPuissance4.get(colonne - 1, ligne - 1),
        /* ⚪⭕️⚪⚪ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪⚪🔴⚪ */ this.plateauPuissance4.get(colonne + 1, ligne + 1),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 2, ligne + 2))) return true;

    if (cellulesEgales(
        /* 🔴⚪⚪⚪ */ this.plateauPuissance4.get(colonne - 2, ligne - 2),
        /* ⚪🔴⚪⚪ */ this.plateauPuissance4.get(colonne - 1, ligne - 1),
        /* ⚪⚪⭕️⚪ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 1, ligne + 1))) return true;

    if (cellulesEgales(
        /* 🔴⚪⚪⚪ */ this.plateauPuissance4.get(colonne - 3, ligne - 3),
        /* ⚪🔴⚪⚪ */ this.plateauPuissance4.get(colonne - 2, ligne - 2),
        /* ⚪⚪🔴⚪ */ this.plateauPuissance4.get(colonne - 1, ligne - 1),
        /* ⚪⚪⚪⭕️ */ this.plateauPuissance4.get(colonne, ligne))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪⭕️ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪⚪🔴⚪ */ this.plateauPuissance4.get(colonne - 1, ligne + 1),
        /* ⚪🔴⚪⚪ */ this.plateauPuissance4.get(colonne - 2, ligne + 2),
        /* 🔴⚪⚪⚪ */ this.plateauPuissance4.get(colonne - 3, ligne + 3))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 1, ligne - 1),
        /* ⚪⚪⭕️⚪ */ this.plateauPuissance4.get(colonne, ligne),
        /* ⚪🔴⚪⚪ */ this.plateauPuissance4.get(colonne - 1, ligne + 1),
        /* 🔴⚪⚪⚪ */ this.plateauPuissance4.get(colonne - 2, ligne + 2))) return true;

    if (cellulesEgales(
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 2, ligne - 2),
        /* ⚪⚪🔴⚪ */ this.plateauPuissance4.get(colonne + 1, ligne - 1),
        /* ⚪⭕️⚪⚪ */ this.plateauPuissance4.get(colonne, ligne),
        /* 🔴⚪⚪⚪ */ this.plateauPuissance4.get(colonne - 1, ligne + 1))) return true;

    return cellulesEgales(
        /* ⚪⚪⚪🔴 */ this.plateauPuissance4.get(colonne + 3, ligne - 3),
        /* ⚪⚪🔴⚪ */ this.plateauPuissance4.get(colonne + 2, ligne - 2),
        /* ⚪🔴⚪⚪ */ this.plateauPuissance4.get(colonne + 1, ligne - 1),
        /* ⭕️⚪⚪⚪ */ this.plateauPuissance4.get(colonne, ligne));
  }

  /**
   * Vérifie si la grille est pleine en vérifiant si chaque colonne est pleine.
   *
   * @return true si la grille est pleine, false sinon
   */
  private boolean grillePleine() {
    boolean result = true;
    for (int i = 1; i <= this.plateauPuissance4.getLongueur(); ++i) {
      if (!colonnePleine(i)) {
        result = false;
        break;
      }
    }
    return result;
  }

  /**
   * Vérifie l'état de la partie à partir de la grille à partir de la cellule insérée.
   *
   * @param colonne la colonne de la cellule insérée, entre 1 et la longueur de la grille
   * @param ligne la ligne de la cellule insérée, entre 1 et la hauteur de la grille
   */
  private void actualiserEtatPartie(int colonne, int ligne) {
    CellulePuissance4 cellule = this.plateauPuissance4.get(colonne, ligne);
    if (cellule == CellulePuissance4.VIDE) return;

    if (celluleVictorieuse(colonne, ligne))
      etatPartie = cellule == CellulePuissance4.ROUGE ? EtatPartie.VICTOIRE_JOUEUR_1 : EtatPartie.VICTOIRE_JOUEUR_2;
    else if (grillePleine()) etatPartie = EtatPartie.MATCH_NUL;
    else etatPartie = EtatPartie.EN_COURS;
  }

  /** Actualise l'état de la partie en itérant sur la grille. */
  private void actualiserEtatPartie() {
    int victoiresRouges = 0;
    int victoiresJaunes = 0;

    iteration:
    for (int i = 1; i <= this.plateauPuissance4.getLongueur(); i++)
      for (int j = 1; j <= this.plateauPuissance4.getHauteur(); j++) {
        CellulePuissance4 cellule = this.plateauPuissance4.get(i, j);

        if (cellule == CellulePuissance4.VIDE
            || (cellule == CellulePuissance4.ROUGE && victoiresRouges > 0)
            || (cellule == CellulePuissance4.JAUNE && victoiresJaunes > 0)) continue;

        actualiserEtatPartie(i, j);

        if (etatPartie == EtatPartie.VICTOIRE_JOUEUR_1) victoiresRouges = 1;
        else if (etatPartie == EtatPartie.VICTOIRE_JOUEUR_2) victoiresJaunes = 1;

        // Early exit
        if (victoiresRouges > 0 && victoiresJaunes > 0) break iteration;
      }

    if (victoiresRouges > 0 && victoiresJaunes > 0) etatPartie = EtatPartie.MATCH_NUL;
    else if (victoiresRouges > 0) etatPartie = EtatPartie.VICTOIRE_JOUEUR_1;
    else if (victoiresJaunes > 0) etatPartie = EtatPartie.VICTOIRE_JOUEUR_2;
  }

  /**
   * Rotationne la grille de jeu de 90° dans le sens horaire ou anti-horaire, selon la rotation
   * donnée.
   *
   * <p>La rotation est effectuée en appliquant une matrice de rotation sur la grille. Aucune
   * gravité n'est appliquée. Il peut aussi y avoir des pertes de données si la grille n'est pas
   * carrée, ou au contraire des données pouvant être dupliquées.
   *
   * @param rotation la rotation à effectuer
   */
  private void rotationnerGrille(RotationPuissance4 rotation) {
    int longueurOriginale = this.plateauPuissance4.getLongueur();
    int hauteurOriginale = this.plateauPuissance4.getHauteur();

    // On inverse les dimensions pour la rotation
    PlateauPuissance4 plateauPuissance4Rotationnee = new PlateauPuissance4(hauteurOriginale, longueurOriginale);

    // Note : commencer à 1 au lieu de 0 pour suivre la formule mathématique
    // qui indexe à 1.
    for (int i = 1; i <= longueurOriginale; ++i) {
      for (int j = 1; j <= hauteurOriginale; ++j) {
        // Rotation de 90° dans le sens horaire d'une matrice 3x2 vers une
        // matrice 2x3 :
        //
        //                  ┌────────────┐     Matrice M        Matrice M'
        //               ┌──┘┌──────────┐└┐    d'origine :      d'arrivée :
        //           ┌───┼───┘┌────────┐└┐│
        //       ┌───┼───┼────┘ ┌──┬──┐└┐││      * A -> 1,1       * A' -> 1,2
        //     ┌─┴─┬─┴─┬─┴─┐  ┌►│D'│A'│◄┘││      * B -> 1,2       * B' -> 2,2
        //     │ A │ B │ C │ ┌┘ ├──┼──┤ ┌┘│      * C -> 1,3       * C' -> 3,2
        //     ├───┼───┼───┤┌┘┌►│E'│B'│◄┘┌┘      * D -> 2,1       * D' -> 1,1
        //     │ D │ E │ F ││┌┘ ├──┼──┤ ┌┘       * E -> 2,2       * E' -> 2,1
        //     └─┬─┴─┬─┴─┬─┘││┌►│F'│C'│◄┘        * F -> 2,3       * F' -> 3,1
        //       └┐  └┐  └──┼┼┘ └──┴──┘
        //        └┐  └─────┼┘                 ∀ɣ∈M, ∀ɣ'∈M, ɣ'₁ = ɣ₂,
        //         └────────┘                               ɣ'₂ = n - ɣ₁
        if (rotation == RotationPuissance4.HORAIRE)
          plateauPuissance4Rotationnee.set(j, hauteurOriginale - i + 1, plateauPuissance4.get(i, j));

        // Rotation de 90° dans le sens horaire inverse d'une matrice 3x2 vers
        // une matrice 2x3 :
        //
        //                  ┌────────────┐     Matrice M        Matrice M'
        //               ┌──┘┌──────────┐└┐    d'origine :      d'arrivée :
        //           ┌───┼───┘┌────────┐└┐│
        //       ┌───┼───┼────┘ ┌──┬──┐└┐││      * A -> 1,1       * A' -> 3,1
        //     ┌─┴─┬─┴─┬─┴─┐  ┌►│C'│F'│◄┘││      * B -> 1,2       * B' -> 2,1
        //     │ A │ B │ C │ ┌┘ ├──┼──┤ ┌┘│      * C -> 1,3       * C' -> 1,1
        //     ├───┼───┼───┤┌┘┌►│B'│E'│◄┘┌┘      * D -> 2,1       * D' -> 3,2
        //     │ D │ E │ F ││┌┘ ├──┼──┤ ┌┘       * E -> 2,2       * E' -> 2,2
        //     └─┬─┴─┬─┴─┬─┘││┌►│A'│D'│◄┘        * F -> 2,3       * F' -> 1,2
        //       └┐  └┐  └──┼┼┘ └──┴──┘
        //        └┐  └─────┼┘                 ∀ɣ∈M, ∀ɣ'∈M, ɣ'₁ = n - ɣ₂,
        //         └────────┘                               ɣ'₂ = ɣ₁
        //
        else if (rotation == RotationPuissance4.ANTI_HORAIRE)
          plateauPuissance4Rotationnee.set(longueurOriginale - j + 1, i, plateauPuissance4.get(i, j));
      }
    }

    plateauPuissance4 = plateauPuissance4Rotationnee;
  }

  /** Applique la gravité sur la grille de jeu, en déplaçant les cellules vides vers le bas. */
  private void appliquerGravite() {
    // Pour simuler la gravité, il nous suffit pour chaque colonne, d'insérer
    // les cellules non vides dans une file, puis d'y ajouter
    // hauteur - len(file) cases vides.  On défile la file dans l'ordre pour
    // insérer dans la colonne.

    int longueur = this.plateauPuissance4.getLongueur();
    int hauteur = this.plateauPuissance4.getHauteur();
    // Faire une copie de la grille actuelle nous permet de réinitialiser la
    // grille existante et de profiter des fonctions pré-définies pour insérer
    // un jeton.
    PlateauPuissance4 copiePlateauPuissance4 = plateauPuissance4;
    plateauPuissance4 = new PlateauPuissance4(longueur, hauteur);

    List<CellulePuissance4> file = new ArrayList<>();
    for (int colonne = 1; colonne <= hauteur; ++colonne) {
      int tailleFile = 0; // Évite d'appeler file.size() qui requière une boucle

      // On enfile uniquement les jetons colorés dans une file.
      for (int ligne = 1; ligne <= longueur; ++ligne) {
        CellulePuissance4 cellule = copiePlateauPuissance4.get(colonne, ligne);

        if (cellule != null && cellule != CellulePuissance4.VIDE) {
          file.add(cellule);
          ++tailleFile;
        }
      }

      // On comble l'espace restant de vide
      if (tailleFile < hauteur) {
        for (int k = 0; k < tailleFile; ++k) file.add(CellulePuissance4.VIDE);
      }

      // Reste qu'à insérer nos jetons dans la colonne.
      for (CellulePuissance4 cellule : file) insererCellule(colonne, cellule);
      file.clear(); // Permet de réutiliser la file pour la prochaine colonne
    }
  }

  // ===========================================================================
  // Public API
  // ===========================================================================

  /**
   * Retourne la grille de jeu
   *
   * @return La grille de jeu
   */
  public PlateauPuissance4 getPlateauPuissance4() {
    return plateauPuissance4;
  }

  /**
   * Retourne l'état de la partie
   *
   * @return L'état de la partie
   */
  public EtatPartie getEtatPartie() {
    return etatPartie;
  }

  /**
   * Vérifie que le numéro de colonne est valide.
   *
   * <p>Une colonne est considérée valide si elle est comprise entre 1 et la longueur de la grille.
   *
   * @param colonne le numéro de colonne
   * @return true si la colonne est valide
   */
  public boolean colonneInvalide(int colonne) {
    return colonne <= 0 || colonne > this.plateauPuissance4.getLongueur();
  }

  /**
   * Joue un coup dans la colonne donnée
   *
   * @param jeton le jeton à insérer
   * @param colonne la colonne dans laquelle jouer, entre 1 et 7 (inclus)
   * @throws CoupInvalideException si la colonne n'existe pas où est pleine
   * @throws EtatPartieException si la partie est terminée
   */
  public void jouer(CellulePuissance4 jeton, int colonne) throws CoupInvalideException, EtatPartieException {
    if (etatPartie != EtatPartie.EN_COURS) throw new EtatPartieException("La partie est terminée");

    if (colonneInvalide(colonne))
      throw new CoupInvalideException("La colonne doit être comprise entre 1 et 7");

    if (colonnePleine(colonne)) throw new CoupInvalideException("La colonne est pleine");

    int ligne = insererCellule(colonne, jeton);
    actualiserEtatPartie(colonne, ligne);
  }

  /**
   * Rotationne la grille de jeu de 90° dans le sens horaire ou anti-horaire.
   *
   * @param rotation la rotation à effectuer
   * @throws EtatPartieException si la partie est terminée
   */
  public void rotationner(RotationPuissance4 rotation) throws EtatPartieException {
    if (etatPartie != EtatPartie.EN_COURS) throw new EtatPartieException("La partie est terminée");

    rotationnerGrille(rotation);
    appliquerGravite();
    actualiserEtatPartie();
  }
}
