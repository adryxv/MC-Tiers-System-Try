import java.util.ArrayList;
import java.util.List;

record ModeResult(String mode, int tier, boolean isHigh, int points) {}

void main() {
    // Points System and ranks MCtiers

    String[] modes = {"Sword", "Axe", "Pot", "UHC", "NethOP", "SMP", "Mace",
            "Vanilla", "Spear Mace", "XbowCart", "Cart", "Creeper", "Bow"};

    List<ModeResult> results = new ArrayList<>();
    int totalPoints = 0;

    for (String mode : modes) {
        ModeResult result = askMode(mode);
        results.add(result);
        totalPoints += result.points();
    }

    IO.println("\n--- Summary ---");
    for (ModeResult r : results) {
        if (r.tier() == 0) {
            IO.println(r.mode() + ": no tier");
        } else {
            IO.println(r.mode() + ": " + (r.isHigh() ? "HT" : "LT") + r.tier()
                    + " (" + r.points() + " pts)");
        }
    }

    int rankLevel = getRankLevel(results);

    IO.println("\nTotal points: " + totalPoints);
    IO.println("Title: " + getTitle(totalPoints));
    IO.println("Rank: " + getRankName(rankLevel));

    IO.println("Perks:");
    List<String> perks = getPerks(rankLevel);
    if (perks.isEmpty()) {
        IO.println("  (none)");
    }
    for (String perk : perks) {
        IO.println("  - " + perk);
    }
}

String getTitle(int totalPoints) {
    if (totalPoints >= 600) {
        return "Grand Master";
    } else if (totalPoints >= 400) {
        return "Master";
    } else if (totalPoints >= 250) {
        return "Warrior";
    } else if (totalPoints >= 150) {
        return "Fighter";
    } else if (totalPoints >= 80) {
        return "Knight";
    } else if (totalPoints >= 30) {
        return "Soldier";
    } else {
        return "Rookie";
    }
}

ModeResult askMode(String mode) {
    int tier = -1;

    while (tier < 0 || tier > 5) {
        IO.print(mode + " tier (1-5, 0 = no tier): ");
        try {
            tier = Integer.parseInt(IO.readln().trim());
        } catch (NumberFormatException e) {
            IO.println("Enter a number.");
            continue;
        }
        if (tier < 0 || tier > 5) {
            IO.println("Invalid tier.");
        }
    }

    if (tier == 0) {
        return new ModeResult(mode, 0, false, 0);
    }

    String level = "";
    while (!level.equals("h") && !level.equals("l")) {
        IO.print("High or Low tier? (h/l): ");
        level = IO.readln().trim().toLowerCase();
    }
    boolean isHigh = level.equals("h");

    return new ModeResult(mode, tier, isHigh, getTierPoints(tier, isHigh));
}

int getTierPoints(int tier, boolean isHigh) {
    if (tier == 1) {
        return isHigh ? 60 : 45;
    } else if (tier == 2) {
        return isHigh ? 30 : 20;
    } else if (tier == 3) {
        return isHigh ? 10 : 6;
    } else if (tier == 4) {
        return isHigh ? 4 : 3;
    } else {
        return isHigh ? 2 : 1;
    }
}

// 0 = no rank, 1 = Elite, 2 = Elite+, 3 = MVP+, 4 = VIP, 5 = VIP+
int getRankLevel(List<ModeResult> results) {
    int ht1 = 0;
    int ht2OrBetter = 0; // HT1 also counts here

    for (ModeResult r : results) {
        if (r.isHigh() && r.tier() >= 1 && r.tier() <= 2) {
            ht2OrBetter++;
            if (r.tier() == 1) {
                ht1++;
            }
        }
    }

    if (ht1 >= 4) {
        return 5;
    } else if (ht1 >= 2) {
        return 4;
    } else if (ht1 >= 1) {
        return 3;
    } else if (ht2OrBetter >= 2) {
        return 2;
    } else if (ht2OrBetter >= 1) {
        return 1;
    } else {
        return 0;
    }
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

    if (rankLevel >= 1) {
        perks.add("Rank tag in chat");
    }
    if (rankLevel >= 2) {
        perks.add("Access to customize armor trim and shield");
    }
    if (rankLevel >= 3) {
        perks.add("Access to Fly command");
    }
    if (rankLevel >= 4) {
        perks.add("Elytra and fireworks in lobby");
    }
    if (rankLevel >= 5) {
        perks.add("Can be a tier tester for other users");
        perks.add("Spear with lunge 3");
        perks.add("Wind charges");
    }

    return perks;
}