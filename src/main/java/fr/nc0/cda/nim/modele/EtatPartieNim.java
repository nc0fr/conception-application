/*
 * Copyright (c) 2024 Lucas Paulo, Younes Ouaammou, Nicolas Paul.
 * Use of this source code is governed by a BSD-style license
 * that can be found in the COPYRIGHT file.
 */

package fr.nc0.cda.nim.modele;

/** Représente l'état d'une partie du jeu de Nim. */
public enum EtatPartieNim {
  /** La partie est en cours. */
  EN_COURS,
  /** La partie est terminée par une victoire du joueur 1. */
  VICTOIRE_JOUEUR_1,
  /** La partie est terminée par une victoire du joueur 2. */
  VICTOIRE_JOUEUR_2,
}
