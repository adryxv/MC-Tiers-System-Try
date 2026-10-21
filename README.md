# 🛡️ Minecraft Tier System

<div align="center">

![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=openjdk)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)
![Course](https://img.shields.io/badge/DAM-1er_Curso-blue?style=for-the-badge)

</div>

---

## 🇪🇸 Español

Programa interactivo en Java basado en el sistema **MCTiers**. Permite calcular los puntos de un jugador, asignarle un **Título** según su puntuación global, determinar su **Rango** según sus mejores marcas (*HT1/HT2*) y mostrar las **Ventajas (Perks)** desbloqueadas.

### ⚙️ ¿Cómo funciona?
1. Solicita el *tier* (1 al 5, o 0 si no tiene) para cada una de las 13 modalidades.
2. Para tiers del 1 al 5, permite elegir si el rango es **High (HT)** o **Low (LT)**.
3. Suma la puntuación total y calcula:
    - **Título:** Basado en la cantidad total de puntos (*Rookie*, *Soldier*, *Knight*, *Fighter*, *Warrior*, *Master*, *Grand Master*).
    - **Rango:** Determinado por la cantidad de modalidades en *HT1* o *HT2* (*Elite*, *Elite+*, *MVP+*, *VIP*, *VIP+*).
    - **Perks:** Recompensas acumulativas según el nivel de rango obtenido.

### ⚔️ Modalidades soportadas (13)
- Sword
- Axe
- Pot
- UHC
- NethOP
- SMP
- Mace
- Vanilla
- Spear Mace
- XbowCart
- Cart
- Creeper
- Bow

### 🎯 Objetivo
Proyecto desarrollado como práctica de programación durante el primer curso de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

---

## 🇬🇧 English

An interactive Java application based on the **MCTiers** competitive framework. It calculates player scores across game modes, assigns a **Title** based on total points, evaluates a **Rank** based on high tiers (*HT1/HT2*), and unlocks special **Perks**.

### ⚙️ How it works?
1. Prompts for the player's tier (1 to 5, or 0 for no tier) across 13 game modes.
2. For tiers 1–5, prompts whether it is **High (HT)** or **Low (LT)**.
3. Computes the total score and determines:
    - **Title:** Assigned via point thresholds (*Rookie* up to *Grand Master*).
    - **Rank:** Evaluated from the number of *HT1* or *HT2* placements (*Elite* up to *VIP+*).
    - **Perks:** Cumulative rewards granted depending on the achieved rank level.

### ⚔️ Supported Game Modes (13)
- Sword
- Axe
- Pot
- UHC
- NethOP
- SMP
- Mace
- Vanilla
- Spear Mace
- XbowCart
- Cart
- Creeper
- Bow

### 🎯 Purpose
Developed as a programming coursework project during the first year of the **Multiplatform Application Development (DAM)** degree.