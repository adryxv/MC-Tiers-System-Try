# ⚔️ Minecraft Tier System Simulator

<div align="center">

![Java](https://img.shields.io/badge/Java-25+-orange?style=for-the-badge&logo=openjdk)
![Course](https://img.shields.io/badge/DAM-1st_Year-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)
![System](https://img.shields.io/badge/System-MCTiers-purple?style=for-the-badge)
![Game](https://img.shields.io/badge/Game-Minecraft-62B447?style=for-the-badge&logo=minecraft)

**🇪🇸 [Español](#-español) · 🇬🇧 [English](#-english)**

</div>

---

## 🇪🇸 Español

Programa interactivo de consola en Java inspirado en el sistema competitivo **MCTiers**. Introduces tu tier en 13 modalidades de Minecraft PvP y el programa calcula tu puntuación, tu **Título**, tu **Rango**, tu perfil de juego, qué mejorar para subir más rápido y, si quieres, simula un combate contra un rival aleatorio.

### ✨ Características

- Entrada de datos con validación (tier 1-5 o 0 si no tienes, y High/Low Tier).
- Resumen por modalidad con colores en degradado (rojo → verde).
- **Título** y **Rango** calculados automáticamente, con tablas de referencia.
- **Perfil de juego** y **Top 3 especializaciones**.
- **Barra de progreso** hacia el siguiente título.
- **Consejos de entrenamiento:** las 3 mejoras de un solo paso que más puntos dan, e indica si alguna llega al siguiente título.
- **Posición estimada** en el ranking global de tu modalidad principal (simulada, no son datos reales).
- **Perks** desbloqueados según el rango.
- **Simulación de combate** contra un rival aleatorio.
- **Versión web** con las mismas reglas (ver más abajo).

### 🖥️ Ejemplo de salida

```text
Enter your player tag: Adry
Sword tier (1-5, 0 = no tier): 2
High or Low tier? (h/l): h
...
--- Tier List Summary: Adry ---
Sword: HT2 (30 pts)
Axe: LT3 (6 pts)
...
Total points: 96
Title: Knight
Rank: Elite

--- Progress to Next Title (Fighter) ---
Points needed: 54 more pts
Progress: [██░░░░░░░░] 22%

💡 Training Advice (best single-step upgrades):
  • Sword: HT2 -> LT1 (+15 pts)
  ...
```

### ⚔️ Modalidades (13)

Sword · Axe · Pot · UHC · NethOP · SMP · Mace · Spear Mace · Vanilla · XbowCart · Cart · Creeper · Bow

### 💯 Puntos por tier

| Tier | High Tier (HT) | Low Tier (LT) |
|:---:|:---:|:---:|
| 1 | 60 | 45 |
| 2 | 30 | 20 |
| 3 | 10 | 6 |
| 4 | 4 | 3 |
| 5 | 2 | 1 |

### 🏆 Títulos

| Título | Puntos |
|:---|:---:|
| Rookie | 0–29 |
| Soldier | 30–79 |
| Knight | 80–149 |
| Fighter | 150–249 |
| Warrior | 250–399 |
| Master | 400–599 |
| Grand Master | 600+ |

### 🎖️ Rangos

| Rango | Requisito | Perks nuevos |
|:---|:---|:---|
| Elite | 1x HT2 o superior | Etiqueta de rango en el chat |
| Elite+ | 2x HT2 o superior | Personalizar armor trim y escudo |
| MVP+ | 1x HT1 | Comando Fly |
| VIP | 2x HT1 | Elytra y fuegos artificiales en el lobby |
| VIP+ | 4x HT1 | Tier tester, Spear con Lunge 3, Wind Charges |

> Los perks son acumulativos: cada rango incluye los de los anteriores. Solo cuentan los **High Tier**.

### 🎮 Simulación de combate

- Se genera un rival con tiers aleatorios en las 13 modalidades.
- Tú eliges 3 modalidades; el rival elige sus 3 mejores (empates al azar).
- Se juegan las 6 modalidades: **FT3** en *Sword, Axe, Bow, Creeper, Vanilla* y **FT1** en el resto.
- Si ambos eligen la misma modalidad, se juega dos veces (una por cada elección).
- Probabilidad de ganar cada ronda: **50 %** con el mismo nivel, **±4 %** por cada nivel de diferencia (mín. 5 %, máx. 95 %).

Los niveles van de `sin tier = 0`, `LT5 = 1`, `HT5 = 2`, … hasta `HT1 = 10`.

### 🗂️ Estructura del código

Todo está en `src/Main.java` (*instance main method* de Java 25):

| Parte | Contenido |
|:---|:---|
| Constantes | Colores ANSI, modalidades (`MODES`), modos FT3 |
| Tipos de datos | `record ModeInfo`, `record ModeResult`, `enum Title`, `enum Rank` |
| Flujo principal | `main()` |
| Entrada | Nombre, tiers, modalidad principal |
| Puntuación | Puntos, niveles, rango, búsqueda de modalidades |
| Salida | Resumen, tablas, perfil, progreso, ranking, perks |
| Simulación | Rival, probabilidades, combate |

Los umbrales de títulos y los requisitos y perks de rangos están definidos **solo** en los `enum`; las tablas de referencia se generan a partir de ellos.

### ▶️ Cómo ejecutarlo

Requiere **Java 25 o superior** (*instance main methods* y clase `IO`).

```bash
git clone https://github.com/adryxv/MC-Tiers-System-Try.git
cd MC-Tiers-System-Try
java src/Main.java
```

También puedes abrirlo en IntelliJ IDEA y ejecutar `Main.java`.

### 🌐 Versión web

Abre `calculator.html` en el navegador: mismo sistema de puntos, títulos y rangos que la versión de consola, con el progreso guardado en `localStorage`.

### 🛣️ Próximos pasos

- [ ] Guardar y cargar perfiles
- [ ] Comparar dos jugadores
- [ ] Torneos de 8 y 16 jugadores
- [ ] ELO y estadísticas por modalidad
- [ ] Separar el código en varias clases

### 🌐 Referencias

Basado en **[MCTiers](https://www.mcpvp.com/tiers)**. El programa genera el enlace de referencia de tu modalidad principal.

### 🎯 Objetivo

Práctica del primer curso de **Desarrollo de Aplicaciones Multiplataforma (DAM)**: colecciones, *records*, *enums*, métodos, aleatoriedad y lógica condicional.

---

## 🇬🇧 English

An interactive Java console app inspired by the **MCTiers** competitive system. Enter your tier in 13 Minecraft PvP game modes and it calculates your score, **Title**, **Rank** and playstyle, tells you what to improve to climb fastest, and can simulate a match against a random rival.

### ✨ Features

- Validated input (tier 1-5, or 0 for none, plus High/Low Tier).
- Per-mode summary color-coded from red to green.
- Automatic **Title** and **Rank**, with reference tables.
- **Playstyle profile** and **Top 3 specializations**.
- **Progress bar** towards the next title.
- **Training advice:** the 3 single-step upgrades that give the most points, flagging any that reach the next title.
- **Estimated position** on the global leaderboard for your main mode (simulated, not real data).
- Unlocked **perks** based on your rank.
- **Match simulation** against a random rival.
- **Web version** with the same rules (see below).

### 🖥️ Sample output

```text
Enter your player tag: Adry
Sword tier (1-5, 0 = no tier): 2
High or Low tier? (h/l): h
...
--- Tier List Summary: Adry ---
Sword: HT2 (30 pts)
Axe: LT3 (6 pts)
...
Total points: 96
Title: Knight
Rank: Elite

--- Progress to Next Title (Fighter) ---
Points needed: 54 more pts
Progress: [██░░░░░░░░] 22%

💡 Training Advice (best single-step upgrades):
  • Sword: HT2 -> LT1 (+15 pts)
  ...
```

### ⚔️ Game Modes (13)

Sword · Axe · Pot · UHC · NethOP · SMP · Mace · Spear Mace · Vanilla · XbowCart · Cart · Creeper · Bow

### 💯 Points per Tier

| Tier | High Tier (HT) | Low Tier (LT) |
|:---:|:---:|:---:|
| 1 | 60 | 45 |
| 2 | 30 | 20 |
| 3 | 10 | 6 |
| 4 | 4 | 3 |
| 5 | 2 | 1 |

### 🏆 Titles

| Title | Points |
|:---|:---:|
| Rookie | 0–29 |
| Soldier | 30–79 |
| Knight | 80–149 |
| Fighter | 150–249 |
| Warrior | 250–399 |
| Master | 400–599 |
| Grand Master | 600+ |

### 🎖️ Ranks

| Rank | Requirement | New perks |
|:---|:---|:---|
| Elite | 1x HT2 or better | Rank tag in chat |
| Elite+ | 2x HT2 or better | Customize armor trim & shield |
| MVP+ | 1x HT1 | Fly command |
| VIP | 2x HT1 | Elytra & fireworks in lobby |
| VIP+ | 4x HT1 | Tier tester, Spear with Lunge 3, Wind Charges |

> Perks are cumulative: each rank includes the previous ones. Only **High Tier** placements count.

### 🎮 Match Simulation

- A rival with random tiers is generated for all 13 modes.
- You pick 3 modes; the rival picks their 3 best (ties broken randomly).
- All 6 modes are played: **FT3** for *Sword, Axe, Bow, Creeper, Vanilla* and **FT1** for the rest.
- If both pick the same mode, it is played twice (once per pick).
- Round win chance: **50%** at equal level, **±4%** per level of difference (min 5%, max 95%).

Levels go from `no tier = 0`, `LT5 = 1`, `HT5 = 2`, … up to `HT1 = 10`.

### 🗂️ Code Structure

Everything lives in `src/Main.java` (Java 25 *instance main method*): constants, data types (`ModeInfo`, `ModeResult`, `Title`, `Rank`), main flow, input, scoring, output and match simulation.

Title thresholds and rank requirements/perks are defined **only** in the enums; the reference tables are generated from them.

### ▶️ How to run

Requires **Java 25 or higher** (instance main methods and the `IO` class).

```bash
git clone https://github.com/adryxv/MC-Tiers-System-Try.git
cd MC-Tiers-System-Try
java src/Main.java
```

You can also open it in IntelliJ IDEA and run `Main.java`.

### 🌐 Web version

Open `calculator.html` in your browser: same points, titles and ranks as the console version, with progress saved in `localStorage`.

### 🛣️ Roadmap

- [ ] Save and load profiles
- [ ] Compare two players
- [ ] 8 and 16 player tournaments
- [ ] Per-mode ELO and stats
- [ ] Split the code into several classes

### 🌐 References

Based on **[MCTiers](https://www.mcpvp.com/tiers)**. The program builds the reference link for your main mode.

### 🎯 Purpose

First-year coursework for **Multiplatform Application Development (DAM)**: collections, records, enums, methods, randomness and conditional logic.