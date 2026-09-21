void main (){

    // Points System and ranks MCtiers

    int totalPoints = 0;

    totalPoints += askMode("Sword");
    totalPoints += askMode("Axe");
    totalPoints += askMode("Pot");
    totalPoints += askMode("UHC");
    totalPoints += askMode("NethOP");
    totalPoints += askMode("SMP");
    totalPoints += askMode("Mace");
    totalPoints += askMode("Vanilla");

    IO.println("Total points: " + totalPoints);

    if (totalPoints >= 400) {
        IO.println("Title: Combat Grandmaster");
    } else if (totalPoints >= 250) {
        IO.println("Title: Combat Master");
    } else if (totalPoints >= 100) {
        IO.println("Title: Combat Ace");
    } else if (totalPoints >= 50) {
        IO.println("Title: Combat Specialist");
    } else if (totalPoints >= 20) {
        IO.println("Title: Combat Cadet");
    } else if (totalPoints >= 10) {
        IO.println("Title: Combat Novice");
    } else {
        IO.println("Title: Rookie");
    }
}

int askMode(String mode) {
    int tier;

    while (true) {
        IO.print(mode + " tier (1-5): ");
        tier = Integer.parseInt(IO.readln());

        if (tier >= 1 && tier <= 5) {
            break;
        }

        IO.println("Invalid tier.");
    }

    IO.print("High or Low tier? (h/l): ");
    String level = IO.readln();
    boolean isHigh = level.equals("h");

    return getTierPoints(tier, isHigh);
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