import java.util.ArrayList;
import java.util.List;

record ModeResult(String mode, int tier, boolean isHigh, int points, String urlKey) {}

void main() {
    // 1. Prompt for Player Tag
    IO.print("Enter your player tag: ");
    String playerName = IO.readln();
    if (playerName == null || playerName.isBlank()) {
        playerName = "Steve_PvP";
    }

    // Definition of the 13 game modes and their reference keys on mcpvp.com
    record ModeInfo(String name, String urlKey) {}

    ModeInfo[] modeConfigs = {
            new ModeInfo("Sword", "sword"),
            new ModeInfo("Axe", "shield"),
            new ModeInfo("Pot", "pot"),
            new ModeInfo("UHC", "uhc"),
            new ModeInfo("NethOP", "late-game"),
            new ModeInfo("SMP", "smp"),
            new ModeInfo("Mace", "mace"),
            new ModeInfo("Spear Mace", "spear"),
            new ModeInfo("Vanilla", "end-game"),
            new ModeInfo("XbowCart", "cart"),
            new ModeInfo("Cart", "cart"),
            new ModeInfo("Creeper", "creeper"),
            new ModeInfo("Bow", "bow")
    };

    List<ModeResult> results = new ArrayList<>();
    int totalPoints = 0;

    for (ModeInfo config : modeConfigs) {
        ModeResult result = askMode(config.name(), config.urlKey());
        results.add(result);
        totalPoints += result.points();
    }

    // 2. Ask for main mode with validation loop (repeats until a valid mode is entered)
    ModeResult mainModeResult = null;
    while (mainModeResult == null) {
        IO.print("\nWhat is your main mode?: ");
        String mainModeInput = IO.readln();

        if (mainModeInput == null || mainModeInput.isBlank()) {
            IO.println("Please enter a mode name.");
            continue;
        }

        String query = mainModeInput.trim().toLowerCase();
        for (ModeResult r : results) {
            if (r.mode().toLowerCase().equals(query) || r.urlKey().toLowerCase().equals(query)) {
                mainModeResult = r;
                break;
            }
        }

        if (mainModeResult == null) {
            IO.println("Invalid mode name. Please choose one from the list.");
        }
    }

    // --- SUMMARY HEADER ---
    IO.println("\n--- Tier List Summary: " + playerName + " ---");
    for (ModeResult r : results) {
        if (r.tier() == 0) {
            IO.println(r.mode() + ": no tier");
        } else {
            IO.println(r.mode() + ": " + (r.isHigh() ? "HT" : "LT") + r.tier()
                    + " (" + r.points() + " pts)");
        }
    }

    // Tabla de referencia colocada tras la lista de modos
    printRankInfoTable();

    // --- RESULTADOS FINALES Y PERKS ---
    int rankLevel = getRankLevel(results);

    IO.println("\nTotal points: " + totalPoints);
    IO.println("Title: " + getTitle(totalPoints));
    IO.println("Rank: " + getRankName(rankLevel));

    // 3. Display estimated leaderboard position & official reference URL
    if (mainModeResult.tier() > 0) {
        String referenceUrl = "https://www.mcpvp.com/tiers/" + mainModeResult.urlKey();
        IO.println("\n[Global Leaderboard Simulation]");
        IO.println("Main mode: " + mainModeResult.mode() + " (" + (mainModeResult.isHigh() ? "HT" : "LT") + mainModeResult.tier() + ")");
        IO.println("Estimated rank position: " + getWebLeaderboardPosition(mainModeResult));
        IO.println("Official reference: " + referenceUrl);
    } else {
        IO.println("\n[Leaderboard]: Your selected main mode has no tier assigned.");
    }

    IO.println("\nUnlocked Perks:");
    List<String> perks = getPerks(rankLevel);
    if (perks.isEmpty()) {
        IO.println("  (none)");
    }
    for (String perk : perks) {
        IO.println("  - " + perk);
    }
}

String getTitle(int totalPoints) {
    if (totalPoints >= 600) return "Grand Master";
    if (totalPoints >= 400) return "Master";
    if (totalPoints >= 250) return "Warrior";
    if (totalPoints >= 150) return "Fighter";
    if (totalPoints >= 80) return "Knight";
    if (totalPoints >= 30) return "Soldier";
    return "Rookie";
}

ModeResult askMode(String modeName, String urlKey) {
    int tier = -1;

    while (tier < 0 || tier > 5) {
        IO.print(modeName + " tier (1-5, 0 = no tier): ");
        String input = IO.readln();
        if (input == null) return new ModeResult(modeName, 0, false, 0, urlKey);

        try {
            tier = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            IO.println("Enter a valid number.");
            continue;
        }
        if (tier < 0 || tier > 5) {
            IO.println("Invalid tier.");
        }
    }

    if (tier == 0) return new ModeResult(modeName, 0, false, 0, urlKey);

    String level = "";
    while (!level.equals("h") && !level.equals("l")) {
        IO.print("High or Low tier? (h/l): ");
        String input = IO.readln();
        if (input == null) break;
        level = input.trim().toLowerCase();
    }
    boolean isHigh = level.equals("h");

    return new ModeResult(modeName, tier, isHigh, getTierPoints(tier, isHigh), urlKey);
}

int getTierPoints(int tier, boolean isHigh) {
    return switch (tier) {
        case 1 -> isHigh ? 60 : 45;
        case 2 -> isHigh ? 30 : 20;
        case 3 -> isHigh ? 10 : 6;
        case 4 -> isHigh ? 4 : 3;
        case 5 -> isHigh ? 2 : 1;
        default -> 0;
    };
}

String getWebLeaderboardPosition(ModeResult r) {
    int tier = r.tier();
    boolean isHigh = r.isHigh();

    if (tier == 1) {
        return isHigh ? "Top 1 - 10 worldwide (Elite Global Tier)" : "Top 11 - 25 worldwide";
    } else if (tier == 2) {
        return isHigh ? "Top 26 - 60 worldwide" : "Top 61 - 120 worldwide";
    } else if (tier == 3) {
        return isHigh ? "Top 121 - 300 worldwide" : "Top 301 - 600 worldwide";
    } else if (tier == 4) {
        return isHigh ? "Top 601 - 1200 worldwide" : "Top 1201 - 2000 worldwide";
    } else {
        return "Beginner Tier / Regional Competitive";
    }
}

int getRankLevel(List<ModeResult> results) {
    int ht1 = 0;
    int ht2OrBetter = 0;

    for (ModeResult r : results) {
        if (r.isHigh() && r.tier() >= 1 && r.tier() <= 2) {
            ht2OrBetter++;
            if (r.tier() == 1) ht1++;
        }
    }

    if (ht1 >= 4) return 5;
    if (ht1 >= 2) return 4;
    if (ht1 >= 1) return 3;
    if (ht2OrBetter >= 2) return 2;
    if (ht2OrBetter >= 1) return 1;
    return 0;
}

String getRankName(int rankLevel) {
    return switch (rankLevel) {
        case 5 -> "VIP+";
        case 4 -> "VIP";
        case 3 -> "MVP+";
        case 2 -> "Elite+";
        case 1 -> "Elite";
        default -> "No rank";
    };
}

List<String> getPerks(int rankLevel) {
    List<String> perks = new ArrayList<>();
    if (rankLevel >= 1) perks.add("Rank tag in chat");
    if (rankLevel >= 2) perks.add("Access to customize armor trim and shield");
    if (rankLevel >= 3) perks.add("Access to Fly command");
    if (rankLevel >= 4) perks.add("Elytra and fireworks in lobby");
    if (rankLevel >= 5) {
        perks.add("Can be a tier tester for other users");
        perks.add("Spear with lunge 3");
        perks.add("Wind charges");
    }
    return perks;
}

void printRankInfoTable() {
    IO.println("\n--- Ranks & Perks Reference Table ---");
    IO.println(String.format("%-10s | %-25s | %-35s", "Rank", "Requirements", "Perks"));
    IO.println("-".repeat(78));
    IO.println(String.format("%-10s | %-25s | %-35s", "Elite", "1x HT2+", "Rank tag in chat"));
    IO.println(String.format("%-10s | %-25s | %-35s", "Elite+", "2x HT2+", "Customize armor trim & shield"));
    IO.println(String.format("%-10s | %-25s | %-35s", "MVP+", "1x HT1", "Access to Fly command"));
    IO.println(String.format("%-10s | %-25s | %-35s", "VIP", "2x HT1", "Elytra & fireworks in lobby"));
    IO.println(String.format("%-10s | %-25s | %-35s", "VIP+", "4x HT1", "Tier tester, Spear lunge 3, Wind charges"));
}