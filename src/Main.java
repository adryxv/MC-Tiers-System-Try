import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;

// ===================== CONSTANTS =====================

// ANSI colors & format
final String RESET = "\u001B[0m";
        final String BOLD  = "\u001B[1m";
        final String DIM   = "\u001B[2m";

        final String CYAN   = "\u001B[36m";
        final String PURPLE = "\u001B[35m";
        final String BLUE   = "\u001B[34m";

        // Tier gradient palette (Red -> Orange -> Yellow -> Lime -> Green)
        final String COLOR_TIER_NONE = "\u001B[31m"; // Red
        final String COLOR_TIER_5    = "\u001B[91m"; // Light red / coral
        final String COLOR_TIER_4    = "\u001B[33m"; // Orange / amber
        final String COLOR_TIER_3    = "\u001B[93m"; // Yellow
        final String COLOR_TIER_2    = "\u001B[92m"; // Lime
        final String COLOR_TIER_1    = "\u001B[32m"; // Green

        final Random random = new Random();

        // Modes played first to 3 (the rest are first to 1)
        final Set<String> FT3_MODES = Set.of("sword", "axe", "bow", "creeper", "vanilla");

        // The 13 game modes (urlKey is only used to build the reference URL)
        final ModeInfo[] MODES = {
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

// ===================== DATA TYPES =====================

        record ModeInfo(String name, String urlKey) {}

        record ModeResult(String mode, int tier, boolean isHigh, int points, String urlKey) {}

        // Titles and their point thresholds live ONLY here
        enum Title {
            ROOKIE("Rookie", 0, "Starting tier level"),
            SOLDIER("Soldier", 30, "Basic competitive competence"),
            KNIGHT("Knight", 80, "Intermediate skilled player"),
            FIGHTER("Fighter", 150, "Advanced combat specialist"),
            WARRIOR("Warrior", 250, "High-tier seasoned veteran"),
            MASTER("Master", 400, "Elite global contender"),
            GRAND_MASTER("Grand Master", 600, "Legendary top-tier player");

            final String label;
            final int minPoints;
            final String description;

            Title(String label, int minPoints, String description) {
                this.label = label;
                this.minPoints = minPoints;
                this.description = description;
            }

            static Title of(int points) {
                Title result = ROOKIE;
                for (Title t : values()) {
                    if (points >= t.minPoints) result = t;
                }
                return result;
            }

            Title next() {
                return this == GRAND_MASTER ? null : values()[ordinal() + 1];
            }
        }

        // Ranks, requirements and perks live ONLY here (perks are cumulative)
        enum Rank {
            NONE("No rank", "-", List.of()),
            ELITE("Elite", "1x HT2+", List.of("Rank tag in chat")),
            ELITE_PLUS("Elite+", "2x HT2+", List.of("Access to customize armor trim and shield")),
            MVP_PLUS("MVP+", "1x HT1", List.of("Access to Fly command")),
            VIP("VIP", "2x HT1", List.of("Elytra and fireworks in lobby")),
            VIP_PLUS("VIP+", "4x HT1", List.of(
                    "Can be a tier tester for other users",
                    "Spear with lunge 3",
                    "Wind charges"));

            final String label;
            final String requirement;
            final List<String> perks; // perks unlocked AT this rank

            Rank(String label, String requirement, List<String> perks) {
                this.label = label;
                this.requirement = requirement;
                this.perks = perks;
            }

            static Rank of(int ht1, int ht2OrBetter) {
                if (ht1 >= 4) return VIP_PLUS;
                if (ht1 >= 2) return VIP;
                if (ht1 >= 1) return MVP_PLUS;
                if (ht2OrBetter >= 2) return ELITE_PLUS;
                if (ht2OrBetter >= 1) return ELITE;
                return NONE;
            }

            List<String> allPerks() {
                List<String> all = new ArrayList<>();
                for (Rank r : values()) {
                    if (r.ordinal() <= ordinal()) all.addAll(r.perks);
                }
                return all;
            }
        }

// ===================== MAIN FLOW =====================

        void main() {
            String playerName = readPlayerName();
            List<ModeResult> results = askAllModes();
            int totalPoints = results.stream().mapToInt(ModeResult::points).sum();

            ModeResult mainMode = askMainMode(results);

            printSummary(playerName, results);
            printRankInfoTable();
            printTitleInfoTable();

            Rank rank = getRank(results);
            IO.println(CYAN + "\nTotal points: " + BOLD + totalPoints + RESET);
            IO.println(CYAN + "Title: " + BOLD + COLOR_TIER_3 + Title.of(totalPoints).label + RESET);
            IO.println(CYAN + "Rank: " + BOLD + PURPLE + rank.label + RESET);

            printPlaystyleProfile(results);
            printProgressAndRecommendations(totalPoints, results);
            printLeaderboard(mainMode);
            printPerks(rank);

            IO.print(CYAN + "\nSimulate a match vs a random player? (y/n): " + RESET);
            String answer = IO.readln();
            if (answer != null && answer.trim().equalsIgnoreCase("y")) {
                simulateMatch(playerName, results);
            }
        }

// ===================== INPUT =====================

        String readPlayerName() {
            IO.print(CYAN + "Enter your player tag: " + RESET);
            String name = IO.readln();
            return (name == null || name.isBlank()) ? "Steve_PvP" : name.trim();
        }

        List<ModeResult> askAllModes() {
            List<ModeResult> results = new ArrayList<>();
            for (ModeInfo info : MODES) {
                results.add(askMode(info));
            }
            return results;
        }

        ModeResult askMode(ModeInfo info) {
            int tier = -1;
            while (tier < 0 || tier > 5) {
                IO.print(info.name() + " tier (1-5, 0 = no tier): ");
                String input = IO.readln();
                if (input == null) return newResult(info.name(), 0, false, info.urlKey());

                try {
                    tier = Integer.parseInt(input.trim());
                } catch (NumberFormatException e) {
                    IO.println(COLOR_TIER_NONE + "Enter a valid number." + RESET);
                    continue;
                }
                if (tier < 0 || tier > 5) {
                    IO.println(COLOR_TIER_NONE + "Invalid tier." + RESET);
                }
            }

            if (tier == 0) return newResult(info.name(), 0, false, info.urlKey());

            String level = "";
            while (!level.equalsIgnoreCase("h") && !level.equalsIgnoreCase("l")) {
                IO.print("High or Low tier? (h/l): ");
                String input = IO.readln();
                if (input == null) break;
                level = input.trim();
            }
            return newResult(info.name(), tier, level.equalsIgnoreCase("h"), info.urlKey());
        }

        // Returns null if the player skips it
        ModeResult askMainMode(List<ModeResult> results) {
            List<String> names = new ArrayList<>();
            for (ModeResult r : results) names.add(r.mode());
            IO.println(DIM + "\nAvailable modes: " + String.join(", ", names) + RESET);

            while (true) {
                IO.print(CYAN + "What is your main mode? (press Enter to skip): " + RESET);
                String input = IO.readln();
                if (input == null || input.isBlank()) return null;

                ModeResult found = findMode(results, input);
                if (found != null) return found;

                IO.println(COLOR_TIER_NONE + "Invalid mode name. Please choose one from the list." + RESET);
            }
        }

// ===================== SCORING =====================

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

        ModeResult newResult(String mode, int tier, boolean isHigh, String urlKey) {
            return new ModeResult(mode, tier, isHigh, getTierPoints(tier, isHigh), urlKey);
        }

        // Tier "level": no tier = 0, LT5 = 1, HT5 = 2, LT4 = 3 ... LT1 = 9, HT1 = 10
        int getLevel(ModeResult r) {
            if (r.tier() == 0) return 0;
            return (6 - r.tier()) * 2 - (r.isHigh() ? 0 : 1);
        }

        // Inverse of getLevel
        ModeResult fromLevel(String mode, int level, String urlKey) {
            if (level <= 0) return newResult(mode, 0, false, urlKey);
            int tier = 6 - (level + 1) / 2;
            boolean isHigh = level % 2 == 0;
            return newResult(mode, tier, isHigh, urlKey);
        }

        Rank getRank(List<ModeResult> results) {
            int ht1 = 0;
            int ht2OrBetter = 0;
            for (ModeResult r : results) {
                if (r.isHigh() && r.tier() >= 1 && r.tier() <= 2) {
                    ht2OrBetter++;
                    if (r.tier() == 1) ht1++;
                }
            }
            return Rank.of(ht1, ht2OrBetter);
        }

        // Same lookup everywhere: by mode name, ignoring case and spaces ("spear mace" == "SpearMace")
        String normalize(String s) {
            return s.toLowerCase().replace(" ", "");
        }

        ModeResult findMode(List<ModeResult> list, String input) {
            String query = normalize(input);
            for (ModeResult r : list) {
                if (normalize(r.mode()).equals(query)) return r;
            }
            return null;
        }

// ===================== OUTPUT =====================

        String getTierColor(int tier) {
            return switch (tier) {
                case 1 -> COLOR_TIER_1;
                case 2 -> COLOR_TIER_2;
                case 3 -> COLOR_TIER_3;
                case 4 -> COLOR_TIER_4;
                case 5 -> COLOR_TIER_5;
                default -> COLOR_TIER_NONE;
            };
        }

        String tierLabel(ModeResult r) {
            if (r.tier() == 0) return COLOR_TIER_NONE + "no tier" + RESET;
            return getTierColor(r.tier()) + (r.isHigh() ? "HT" : "LT") + r.tier() + RESET;
        }

        void printSummary(String playerName, List<ModeResult> results) {
            IO.println(PURPLE + BOLD + "\n--- Tier List Summary: " + playerName + " ---" + RESET);
            for (ModeResult r : results) {
                if (r.tier() == 0) {
                    IO.println(r.mode() + ": " + COLOR_TIER_NONE + "no tier" + RESET);
                } else {
                    String tierFormatted = getTierColor(r.tier())
                            + (r.isHigh() ? BOLD + "HT" : DIM + "LT") + r.tier() + RESET;
                    IO.println(r.mode() + ": " + tierFormatted + " (" + r.points() + " pts)");
                }
            }
        }

        void printRankInfoTable() {
            IO.println(CYAN + "\n--- Ranks & Perks Reference Table ---" + RESET);
            IO.println(String.format("%-8s | %-12s | %s", "Rank", "Requirements", "New perks"));
            IO.println("-".repeat(90));
            for (Rank r : Rank.values()) {
                if (r == Rank.NONE) continue;
                IO.println(String.format("%-8s | %-12s | %s", r.label, r.requirement, String.join(", ", r.perks)));
            }
        }

        void printTitleInfoTable() {
            IO.println(CYAN + "\n--- Titles & Points Reference Table ---" + RESET);
            IO.println(String.format("%-15s | %-15s | %s", "Title", "Points Required", "Description"));
            IO.println("-".repeat(71));
            for (Title t : Title.values()) {
                Title next = t.next();
                String range = (next == null)
                        ? t.minPoints + "+ pts"
                        : t.minPoints + " - " + (next.minPoints - 1) + " pts";
                IO.println(String.format("%-15s | %-15s | %s", t.label, range, t.description));
            }
        }

        void printPlaystyleProfile(List<ModeResult> results) {
            long activeModes = results.stream().filter(r -> r.tier() > 0).count();
            String style;

            if (activeModes >= 10) style = "All-Rounder God (Master of All Trades)";
            else if (activeModes >= 6) style = "Versatile Combatant (Flexible Skillset)";
            else if (activeModes >= 3) style = "Focused Specialist (Core Mode Main)";
            else if (activeModes >= 1) style = "One-Trick Pony (Hyper Specialized)";
            else style = "Unranked Challenger";

            IO.println(CYAN + "\n--- Playstyle Profile ---" + RESET);
            IO.println("Active Modes: " + BOLD + activeModes + "/" + results.size() + RESET);
            IO.println("Playstyle: " + BOLD + COLOR_TIER_2 + style + RESET);

            // Top 3 modes by points
            List<ModeResult> sorted = new ArrayList<>(results);
            sorted.sort(Comparator.comparingInt(ModeResult::points).reversed());

            List<String> topModes = new ArrayList<>();
            for (int i = 0; i < Math.min(3, sorted.size()); i++) {
                if (sorted.get(i).points() > 0) {
                    topModes.add(sorted.get(i).mode() + " (" + sorted.get(i).points() + " pts)");
                }
            }
            if (!topModes.isEmpty()) {
                IO.println("Top Specializations: " + BOLD + String.join(", ", topModes) + RESET);
            }
        }

        void printProgressAndRecommendations(int totalPoints, List<ModeResult> results) {
            Title current = Title.of(totalPoints);
            Title next = current.next();

            if (next == null) {
                IO.println(COLOR_TIER_3 + BOLD + "\n🏆 You have reached the maximum title: Grand Master! 🏆" + RESET);
                return;
            }

            int pointsNeeded = next.minPoints - totalPoints;
            int range = next.minPoints - current.minPoints;
            int progress = totalPoints - current.minPoints;
            int filled = (progress * 10) / range;

            StringBuilder bar = new StringBuilder("[");
            for (int i = 0; i < 10; i++) {
                bar.append(i < filled ? "█" : "░");
            }
            bar.append("] ").append((progress * 100) / range).append("%");

            IO.println(CYAN + "\n--- Progress to Next Title (" + next.label + ") ---" + RESET);
            IO.println("Points needed: " + COLOR_TIER_3 + pointsNeeded + " more pts" + RESET);
            IO.println("Progress: " + COLOR_TIER_1 + bar + RESET);

            // Training advice: best single-step upgrade per mode, sorted by points gained
            record Upgrade(String text, int gain) {}
            List<Upgrade> upgrades = new ArrayList<>();
            for (ModeResult r : results) {
                int level = getLevel(r);
                if (level >= 10) continue; // already HT1
                ModeResult up = fromLevel(r.mode(), level + 1, r.urlKey());
                upgrades.add(new Upgrade(
                        BOLD + r.mode() + RESET + ": " + tierLabel(r) + " -> " + tierLabel(up),
                        up.points() - r.points()));
            }
            upgrades.sort(Comparator.comparingInt(Upgrade::gain).reversed());

            IO.println(COLOR_TIER_3 + "\n💡 Training Advice (best single-step upgrades):" + RESET);
            for (int i = 0; i < Math.min(3, upgrades.size()); i++) {
                Upgrade u = upgrades.get(i);
                String extra = u.gain() >= pointsNeeded ? " -> reaches " + BOLD + next.label + RESET : "";
                IO.println("  • " + u.text() + " (+" + u.gain() + " pts)" + extra);
            }
        }

        void printLeaderboard(ModeResult mainMode) {
            if (mainMode == null) return;

            if (mainMode.tier() == 0) {
                IO.println(COLOR_TIER_NONE + "\n[Leaderboard]: Your selected main mode has no tier assigned." + RESET);
                return;
            }

            String referenceUrl = "https://www.mcpvp.com/tiers/" + mainMode.urlKey();
            IO.println(BLUE + "\n[Global Leaderboard Simulation - estimated, not real data]" + RESET);
            IO.println("Main mode: " + mainMode.mode() + " (" + (mainMode.isHigh() ? "HT" : "LT") + mainMode.tier() + ")");
            IO.println("Estimated rank position: " + COLOR_TIER_1 + getWebLeaderboardPosition(mainMode) + RESET);
            IO.println("Official reference: " + CYAN + referenceUrl + RESET);
        }

        String getWebLeaderboardPosition(ModeResult r) {
            boolean high = r.isHigh();
            return switch (r.tier()) {
                case 1 -> high ? "Top 1 - 10 worldwide (Elite Global Tier)" : "Top 11 - 25 worldwide";
                case 2 -> high ? "Top 26 - 60 worldwide" : "Top 61 - 120 worldwide";
                case 3 -> high ? "Top 121 - 300 worldwide" : "Top 301 - 600 worldwide";
                case 4 -> high ? "Top 601 - 1200 worldwide" : "Top 1201 - 2000 worldwide";
                default -> "Beginner Tier / Regional Competitive";
            };
        }

        void printPerks(Rank rank) {
            IO.println(COLOR_TIER_3 + "\nUnlocked Perks:" + RESET);
            List<String> perks = rank.allPerks();
            if (perks.isEmpty()) {
                IO.println("  (none)");
            }
            for (String perk : perks) {
                IO.println("  - " + perk);
            }
        }

// ===================== MATCH SIMULATION =====================

        int getFirstTo(String mode) {
            return FT3_MODES.contains(mode.toLowerCase()) ? 3 : 1;
        }

        List<ModeResult> generateRival(List<ModeResult> template) {
            List<ModeResult> rival = new ArrayList<>();
            for (ModeResult r : template) {
                int tier = random.nextInt(6); // 0-5
                boolean isHigh = tier > 0 && random.nextBoolean();
                rival.add(newResult(r.mode(), tier, isHigh, r.urlKey()));
            }
            return rival;
        }

        List<ModeResult> askThreeModes(List<ModeResult> results) {
            List<ModeResult> picks = new ArrayList<>();
            while (picks.size() < 3) {
                IO.print(CYAN + "Pick mode " + (picks.size() + 1) + "/3: " + RESET);
                String input = IO.readln();
                if (input == null) break;

                ModeResult found = findMode(results, input);
                if (found == null) {
                    IO.println(COLOR_TIER_NONE + "Invalid mode name." + RESET);
                } else if (picks.contains(found)) {
                    IO.println(COLOR_TIER_NONE + "You already picked that mode." + RESET);
                } else {
                    picks.add(found);
                }
            }
            return picks;
        }

        // 50% if equal, +4% per level of difference (min 5%, max 95%)
        double getWinChance(ModeResult mine, ModeResult theirs) {
            int diff = getLevel(mine) - getLevel(theirs);
            double chance = 0.5 + diff * 0.04;
            return Math.max(0.05, Math.min(0.95, chance));
        }

        // Returns true if "mine" wins the mode
        boolean playMode(String myName, ModeResult mine, String theirName, ModeResult theirs) {
            int firstTo = getFirstTo(mine.mode());
            double chance = getWinChance(mine, theirs);

            int myScore = 0, theirScore = 0;
            while (myScore < firstTo && theirScore < firstTo) {
                if (random.nextDouble() < chance) myScore++; else theirScore++;
            }

            boolean iWin = myScore > theirScore;
            IO.println(String.format("  %-11s FT%d | %s %s vs %s %s -> %d-%d | Winner: %s",
                    mine.mode(), firstTo,
                    myName, tierLabel(mine), theirName, tierLabel(theirs),
                    myScore, theirScore,
                    BOLD + (iWin ? myName : theirName) + RESET));
            return iWin;
        }

        void simulateMatch(String playerName, List<ModeResult> playerResults) {
            List<ModeResult> rival = generateRival(playerResults);
            String rivalName = "Rival_" + (100 + random.nextInt(900));

            IO.println(PURPLE + BOLD + "\n--- " + rivalName + " (random tiers) ---" + RESET);
            for (ModeResult r : rival) {
                IO.println(r.mode() + ": " + tierLabel(r));
            }

            IO.println(CYAN + "\nChoose 3 modes for the match:" + RESET);
            List<ModeResult> myPicks = askThreeModes(playerResults);

            // Rival picks his 3 best modes (highest tier level); ties are broken randomly
            List<ModeResult> sortedRival = new ArrayList<>(rival);
            Collections.shuffle(sortedRival, random);
            sortedRival.sort(Comparator.comparingInt(this::getLevel).reversed());
            List<ModeResult> rivalPicks = sortedRival.subList(0, 3);

            int myWins = 0, rivalWins = 0;

            IO.println(PURPLE + BOLD + "\n--- Your modes ---" + RESET);
            for (ModeResult pick : myPicks) {
                ModeResult theirs = findMode(rival, pick.mode());
                if (playMode(playerName, pick, rivalName, theirs)) myWins++; else rivalWins++;
            }

            IO.println(PURPLE + BOLD + "\n--- " + rivalName + "'s modes ---" + RESET);
            for (ModeResult pick : rivalPicks) {
                ModeResult mine = findMode(playerResults, pick.mode());
                if (playMode(playerName, mine, rivalName, pick)) myWins++; else rivalWins++;
            }

            IO.println(CYAN + "\nFinal score: " + BOLD + playerName + " " + myWins + " - " + rivalWins + " " + rivalName + RESET);
            if (myWins > rivalWins) IO.println(COLOR_TIER_1 + BOLD + "You win!" + RESET);
            else if (myWins < rivalWins) IO.println(COLOR_TIER_NONE + BOLD + "You lose." + RESET);
            else IO.println(COLOR_TIER_3 + BOLD + "Draw." + RESET);
        }