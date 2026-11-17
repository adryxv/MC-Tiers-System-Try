# ⚔️ Minecraft Tier System Simulator

<div align="center">

![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=openjdk)
![Course](https://img.shields.io/badge/DAM-1st_Year-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)
![System](https://img.shields.io/badge/System-MCTiers-purple?style=for-the-badge)
![Game](https://img.shields.io/badge/Game-Minecraft-62B447?style=for-the-badge&logo=minecraft)

</div>

---

## 🇪🇸 Español

Programa interactivo en consola desarrollado en Java, basado en el sistema competitivo **MCTiers**. Permite analizar el nivel de un jugador en 13 modalidades de Minecraft PvP, calcular su puntuación global, asignarle un **Título** y un **Rango**, descubrir su perfil de juego y consultar los **Perks** desbloqueados.

Incluye también un sistema de progreso con recomendaciones de entrenamiento y una simulación de combates contra rivales aleatorios.

### ⚙️ ¿Cómo funciona?

1. Solicita el nombre del jugador (si se deja vacío usa `Steve_PvP`).
2. Pide el *tier* (1 al 5, o 0 si no tiene) para cada una de las 13 modalidades.
3. Para tiers del 1 al 5, pregunta si es **High Tier (HT)** o **Low Tier (LT)**.
4. Pide una modalidad principal (*Main Mode*, opcional) para estimar su posición en el ranking global.
5. Muestra un resumen completo con:

   - **Resumen por modalidad:** tier y puntos de cada modo, con colores en degradado (rojo → verde).
   - **Tablas de referencia:** rangos con sus perks y títulos con sus puntos.
   - **Total Points:** puntuación acumulada.
   - **Title:** *Rookie*, *Soldier*, *Knight*, *Fighter*, *Warrior*, *Master* o *Grand Master*.
   - **Rank:** *Elite*, *Elite+*, *MVP+*, *VIP* o *VIP+*, según la cantidad de HT1 y HT2.
   - **Playstyle Profile:** estilo de juego según el número de modalidades con tier.
   - **Top Specializations:** las tres modalidades con más puntos.
   - **Progress Bar:** barra de progreso hacia el siguiente título.
   - **Training Advice:** sugerencias para subir de tier y ganar puntos.
   - **Global Leaderboard Simulation:** posición estimada en la modalidad principal y enlace de referencia.
   - **Unlocked Perks:** ventajas desbloqueadas según el rango.

### ⚔️ Modalidades soportadas (13)

Sword · Axe · Pot · UHC · NethOP · SMP · Mace · Spear Mace · Vanilla · XbowCart · Cart · Creeper · Bow

### 💯 Puntos por tier

| Tier | High Tier (HT) | Low Tier (LT) |
|:---:|:---:|:---:|
| 1 | 60 | 45 |
| 2 | 30 | 20 |
| 3 | 10 | 6 |
| 4 | 4 | 3 |
| 5 | 2 | 1 |

### 🏆 Sistema de títulos

| Título | Puntos necesarios |
|:---|:---:|
| Rookie | 0–29 |
| Soldier | 30–79 |
| Knight | 80–149 |
| Fighter | 150–249 |
| Warrior | 250–399 |
| Master | 400–599 |
| Grand Master | 600+ |

### 🎖️ Sistema de rangos

| Rango | Requisito | Perks |
|:---|:---|:---|
| Elite | 1x HT2 o superior | Etiqueta de rango en el chat |
| Elite+ | 2x HT2 o superior | Personalizar armor trim y escudo |
| MVP+ | 1x HT1 | Comando Fly |
| VIP | 2x HT1 | Elytra y fuegos artificiales en el lobby |
| VIP+ | 4x HT1 | Tier tester, Spear con Lunge 3, Wind Charges |

> Los perks son acumulativos: cada rango incluye los de los anteriores.

### 🎮 Simulación de combates

Al final del programa se puede simular un combate contra un rival aleatorio:

- Se genera un rival con tiers aleatorios en las 13 modalidades.
- El jugador elige 3 modalidades y el rival elige automáticamente sus 3 mejores.
- Cada modalidad se juega en formato **FT3** (*Sword, Axe, Bow, Creeper, Vanilla*) o **FT1** (el resto).
- La probabilidad de ganar cada ronda es del **50 %** si los niveles son iguales, y varía **±4 %** por cada nivel de diferencia (mínimo 5 %, máximo 95 %).
- Se muestran los resultados de cada modalidad y el marcador final.

### ▶️ Cómo ejecutarlo

Requiere **Java 25 o superior** (usa *instance main methods* y la clase `IO`).

```bash
git clone https://github.com/adryxv/MC-Tiers-System-Try.git
cd MC-Tiers-System-Try
java src/Main.java
```

También se puede abrir el proyecto en IntelliJ IDEA y ejecutar `Main.java`.

### 🌐 Referencias externas

Se usa **[MCTiers](https://www.mcpvp.com/tiers)** como referencia de las modalidades, y el programa genera el enlace directo a la modalidad principal elegida.

### 🎯 Objetivo

Proyecto desarrollado como práctica de programación durante el primer curso de **Desarrollo de Aplicaciones Multiplataforma (DAM)**, aplicando colecciones, *records*, métodos, aleatoriedad y lógica condicional.

---

## 🇬🇧 English

An interactive console application written in Java, based on the **MCTiers** competitive system. It evaluates a player across 13 Minecraft PvP game modes, calculates an overall score, assigns a **Title** and a **Rank**, builds a playstyle profile and lists the unlocked **Perks**.

It also includes a progression system with training recommendations and a match simulator against random opponents.

### ⚙️ How it works

1. Prompts for the player name (defaults to `Steve_PvP` if left empty).
2. Asks for a tier (1 to 5, or 0 for no tier) in each of the 13 game modes.
3. For tiers 1–5, asks whether it is **High Tier (HT)** or **Low Tier (LT)**.
4. Optionally asks for a main mode to estimate the global leaderboard position.
5. Prints a full summary including:

   - **Per-mode summary:** tier and points for every mode, color-coded from red to green.
   - **Reference tables:** ranks with their perks, and titles with their points.
   - **Total Points:** accumulated score.
   - **Title:** *Rookie*, *Soldier*, *Knight*, *Fighter*, *Warrior*, *Master* or *Grand Master*.
   - **Rank:** *Elite*, *Elite+*, *MVP+*, *VIP* or *VIP+*, based on the number of HT1 and HT2 placements.
   - **Playstyle Profile:** playstyle based on the number of ranked modes.
   - **Top Specializations:** the three highest-scoring modes.
   - **Progress Bar:** progress towards the next title.
   - **Training Advice:** suggestions to improve tiers and earn more points.
   - **Global Leaderboard Simulation:** estimated position in the main mode plus a reference link.
   - **Unlocked Perks:** rewards unlocked according to the rank.

### ⚔️ Supported Game Modes (13)

Sword · Axe · Pot · UHC · NethOP · SMP · Mace · Spear Mace · Vanilla · XbowCart · Cart · Creeper · Bow

### 💯 Points per Tier

| Tier | High Tier (HT) | Low Tier (LT) |
|:---:|:---:|:---:|
| 1 | 60 | 45 |
| 2 | 30 | 20 |
| 3 | 10 | 6 |
| 4 | 4 | 3 |
| 5 | 2 | 1 |

### 🏆 Title System

| Title | Required Points |
|:---|:---:|
| Rookie | 0–29 |
| Soldier | 30–79 |
| Knight | 80–149 |
| Fighter | 150–249 |
| Warrior | 250–399 |
| Master | 400–599 |
| Grand Master | 600+ |

### 🎖️ Rank System

| Rank | Requirement | Perks |
|:---|:---|:---|
| Elite | 1x HT2 or better | Rank tag in chat |
| Elite+ | 2x HT2 or better | Customize armor trim & shield |
| MVP+ | 1x HT1 | Access to Fly command |
| VIP | 2x HT1 | Elytra & fireworks in lobby |
| VIP+ | 4x HT1 | Tier tester, Spear with Lunge 3, Wind Charges |

> Perks are cumulative: each rank includes those of the previous ones.

### 🎮 Match Simulation

At the end of the program you can simulate a match against a random opponent:

- A rival with random tiers is generated for all 13 modes.
- The player picks 3 modes and the rival automatically picks their 3 strongest.
- Each mode is played as **FT3** (*Sword, Axe, Bow, Creeper, Vanilla*) or **FT1** (all the others).
- The chance of winning each round is **50%** at equal levels, shifting **±4%** per level of difference (min 5%, max 95%).
- Individual mode results and the final score are displayed.

### ▶️ How to run

Requires **Java 25 or higher** (uses instance main methods and the `IO` class).

```bash
git clone https://github.com/adryxv/MC-Tiers-System-Try.git
cd MC-Tiers-System-Try
java src/Main.java
```

You can also open the project in IntelliJ IDEA and run `Main.java`.

### 🌐 External References

**[MCTiers](https://www.mcpvp.com/tiers)** is used as a reference for the game modes, and the program generates a direct link to the selected main mode.

### 🎯 Purpose

Developed as a programming coursework project during the first year of the **Multiplatform Application Development (DAM)** degree, applying collections, records, methods, random generation and conditional logic.