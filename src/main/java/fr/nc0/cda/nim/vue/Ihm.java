/*
 * Copyright (c) 2024 Lucas Paulo, Younes Ouaammou, Nicolas Paul.
 * Use of this source code is governed by a BSD-style license
 * that can be found in the COPYRIGHT file.
 */

package fr.nc0.cda.nim.vue;

import fr.nc0.cda.nim.modele.Joueur;
import java.util.List;
import java.util.Scanner;

/** Interface de relation entre l'utilisateur et le système. */
public class Ihm {
  /**
   * Demande le nombre de tas pour la partie
   *
   * @return le nombre de tas
   */
  public int demanderNombreTas() {
    Scanner scanner = new Scanner(System.in);
    afficherDemande("Veuillez saisir le nombre de tas (entier >= 1)  pour la partie : ");

    while (true) {
      try {
        return scanner.nextInt();
      } catch (Exception e) {
        afficherErreur(new Exception("La saisie doit être un chiffre entier >= 1"));
      }
    }
  }

  /**
   * Demande le nom du joueur
   *
   * @param numero numéro du joueur
   * @return le nom du joueur
   */
  public String selectNomJoueur(int numero) {
    Scanner scanner = new Scanner(System.in);
    afficherDemande("Veuillez saisir le nom du joueur numéro " + numero + " : ");
    return scanner.next();
  }

  /**
   * Affiche les tas de la partie
   *
   * @param tas liste des tas
   */
  public void afficherTas(List<Integer> tas) {
    String affichage = "Tas\t\tAllumettes\n\n";
    for (int i = 0; i < tas.size(); ++i) affichage += (i + 1) + "\t\t" + tas.get(i) + "\n";
    afficherMessage(affichage);
  }

  /**
   * Demande au joueur de choisir un tas et un nombre d'allumettes
   *
   * @param nom nom du joueur
   * @return un tableau contenant le tas et le nombre d'allumettes, sous la forme {tas, allumettes}.
   */
  public int[] demanderChoix(String nom) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      afficherDemande(nom + ", à vous de jouer un coup (sous la forme 'tas allumettes') : ");

      if (!scanner.hasNextInt()) {
        afficherMessage("Merci de saisir un entier pour le tas à choisir !");
        continue;
      }

      int tas = scanner.nextInt();

      if (!scanner.hasNextInt()) {
        afficherMessage("Merci de saisir un entier pour le nombre d'allumettes à retirer !");
        continue;
      }

      int allumettes = scanner.nextInt();

      scanner.nextLine();
      return new int[] {tas, allumettes};
    }
  }

  /**
   * Demande aux joueurs s'ils veulent rejouer
   *
   * @param gagnant le joueur gagnant
   * @param perdant le joueur perdant
   * @return true si les joueurs veulent rejouer, false sinon
   */
  public boolean demanderRejouer(Joueur gagnant, Joueur perdant) {
    afficherScores(gagnant, perdant);

    afficherDemande("Voulez-vous rejouer une nouvelle partie ? (Oui/Non) : ");
    Scanner scanner = new Scanner(System.in);
    return scanner.next().equalsIgnoreCase("Oui");
  }

  /**
   * Affiche les scores des joueurs
   *
   * @param joueur1 le premier joueur
   * @param joueur2 le second joueur
   */
  private void afficherScores(Joueur joueur1, Joueur joueur2) {
    afficherMessage(
        "Scores : "
            + joueur1.getNom()
            + " -> "
            + joueur1.getNbrPartieGagnee()
            + " V | "
            + joueur2.getNom()
            + " -> "
            + joueur2.getNbrPartieGagnee()
            + " V");
  }

  /**
   * Affiche le gagnant de la partie
   *
   * @param gagnant le joueur gagnant
   * @param perdant le joueur perdant
   * @param exaequo true si les joueurs ont gagné autant de partie
   */
  public void afficherGagnant(Joueur gagnant, Joueur perdant, boolean exaequo) {
    afficherScores(gagnant, perdant);
    if (exaequo) {
      afficherMessage("Ex-aequo !");
      return;
    }
    afficherMessage(gagnant.getNom() + " est le grand gagnant !");
  }

  /**
   * Affiche une question sans retour à la ligne, utilisé pour les saisies d'informations
   *
   * @param question la question
   */
  public void afficherDemande(String question) {
    System.out.print(question);
  }

  /**
   * Affiche un message
   *
   * @param message le message
   */
  public void afficherMessage(String message) {
    System.out.println(message);
  }

  /**
   * Affiche un message d'erreur
   *
   * @param exception l'exception
   */
  public void afficherErreur(Exception exception) {
    System.out.println("Erreur : " + exception.getMessage());
  }
}
