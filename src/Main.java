import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// --- ANSI COLOR & FORMAT CONSTANTS ---
String RESET  = "\u001B[0m";
        String BOLD   = "\u001B[1m";
        String DIM    = "\u001B[2m";

        // Basic colors for UI elements
        String CYAN   = "\u001B[36m";
        String PURPLE = "\u001B[35m";
        String BLUE   = "\u001B[34m";

        // Tier Gradient Palette (Red -> Orange -> Yellow -> Lime -> Bright Green)
        String COLOR_TIER_NONE = "\u001B[31m";      // Red
        String COLOR_TIER_5    = "\u001B[91m";      // Light Red / Coral
        String COLOR_TIER_4    = "\u001B[33m";      // Orange / Amber
        String COLOR_TIER_3    = "\u001B[93m";      // Yellow
        String COLOR_TIER_2    = "\u001B[92m";      // Light Green / Lime
        String COLOR_TIER_1    = "\u001B[32m";      // Green

        record ModeResult(String mode, int tier, boolean isHigh, int points, String urlKey) {}

        void main() {
            // 1. Prompt for Player Tag
            IO.print(CYAN + "Enter your player tag: " + RESET);
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

            // 2. Ask for main mode with validation loop
            ModeResult mainModeResult = null;
            while (mainModeResult == null) {
                IO.print(CYAN + "\nWhat is your main mode?: " + RESET);
                String mainModeInput = IO.readln();

                if (mainModeInput == null || mainModeInput.isBlank()) {
                    IO.println(COLOR_TIER_NONE + "Please enter a mode name." + RESET);
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
                    IO.println(COLOR_TIER_NONE + "Invalid mode name. Please choose one from the list." + RESET);
                }
            }

            // --- SUMMARY HEADER ---
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

            // Reference tables printed after mode results
            printRankInfoTable();
            printTitleInfoTable();

            // --- FINAL RESULTS AND PERKS ---
            int rankLevel = getRankLevel(results);

            IO.println(CYAN + "\nTotal points: " + BOLD + totalPoints + RESET);
            IO.println(CYAN + "Title: " + BOLD + COLOR_TIER_3 + getTitle(totalPoints) + RESET);
            IO.println(CYAN + "Rank: " + BOLD + PURPLE + getRankName(rankLevel) + RESET);

            // --- PLAYSTYLE PROFILE ---
            printPlaystyleProfile(results);

            // --- PROGRESS BAR & TRAINING RECOMMENDATIONS ---
            printProgressAndRecommendations(totalPoints, results);

            // 3. Display estimated leaderboard position & official reference URL
            if (mainModeResult.tier() > 0) {
                String referenceUrl = "https://www.mcpvp.com/tiers/" + mainModeResult.urlKey();
                IO.println(BLUE + "\n[Global Leaderboard Simulation]" + RESET);
                IO.println("Main mode: " + mainModeResult.mode() + " (" + (mainModeResult.isHigh() ? "HT" : "LT") + mainModeResult.tier() + ")");
                IO.println("Estimated rank position: " + COLOR_TIER_1 + getWebLeaderboardPosition(mainModeResult) + RESET);
                IO.println("Official reference: " + CYAN + referenceUrl + RESET);
            } else {
                IO.println(COLOR_TIER_NONE + "\n[Leaderboard]: Your selected main mode has no tier assigned." + RESET);
            }

            IO.println(COLOR_TIER_3 + "\nUnlocked Perks:" + RESET);
            List<String> perks = getPerks(rankLevel);
            if (perks.isEmpty()) {
                IO.println("  (none)");
            }
            for (String perk : perks) {
                IO.println("  - " + perk);
            }
        }

        // Devuelve un color en degradado desde Rojo (Tiers bajos) hasta Verde (Tier 1)
        String getTierColor(int tier) {
            return switch (tier) {
                case 1 -> COLOR_TIER_1; // Pure Green
                case 2 -> COLOR_TIER_2; // Lime / Light Green
                case 3 -> COLOR_TIER_3; // Yellow
                case 4 -> COLOR_TIER_4; // Orange
                case 5 -> COLOR_TIER_5; // Light Red
                default -> COLOR_TIER_NONE;
            };
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

        void printPlaystyleProfile(List<ModeResult> results) {
            long activeModes = results.stream().filter(r -> r.tier() > 0).count();
            String style;

            if (activeModes >= 10) style = "All-Rounder God (Master of All Trades)";
            else if (activeModes >= 6) style = "Versatile Combatant (Flexible Skillset)";
            else if (activeModes >= 3) style = "Focused Specialist (Core Mode Main)";
            else if (activeModes >= 1) style = "One-Trick Pony (Hyper Specialized)";
            else style = "Unranked Challenger";

            IO.println(CYAN + "\n--- Playstyle Profile ---" + RESET);
            IO.println("Active Modes: " + BOLD + activeModes + "/13" + RESET);
            IO.println("Playstyle: " + BOLD + COLOR_TIER_2 + style + RESET);

            // Top 3 best modes by points
            List<ModeResult> sorted = new ArrayList<>(results);
            sorted.sort(Comparator.comparingInt(ModeResult::points).reversed());

            if (!sorted.isEmpty() && sorted.get(0).points() > 0) {
                IO.print("Top Specializations: ");
                List<String> topModes = new ArrayList<>();
                for (int i = 0; i < Math.min(3, sorted.size()); i++) {
                    if (sorted.get(i).points() > 0) {
                        topModes.add(sorted.get(i).mode() + " (" + sorted.get(i).points() + " pts)");
                    }
                }
                IO.println(BOLD + String.join(", ", topModes) + RESET);
            }
        }

        void printProgressAndRecommendations(int totalPoints, List<ModeResult> results) {
            int[] thresholds = {0, 30, 80, 150, 250, 400, 600};
            String[] titles = {"Rookie", "Soldier", "Knight", "Fighter", "Warrior", "Master", "Grand Master"};

            int currentIdx = 0;
            for (int i = 0; i < thresholds.length; i++) {
                if (totalPoints >= thresholds[i]) {
                    currentIdx = i;
                }
            }

            if (currentIdx == thresholds.length - 1) {
                IO.println(COLOR_TIER_3 + BOLD + "\n🏆 You have reached the maximum title: Grand Master! 🏆" + RESET);
                return;
            }

            int currentThreshold = thresholds[currentIdx];
            int nextThreshold = thresholds[currentIdx + 1];
            int pointsNeeded = nextThreshold - totalPoints;
            String nextTitle = titles[currentIdx + 1];

            int progressRange = nextThreshold - currentThreshold;
            int currentProgress = totalPoints - currentThreshold;
            int percent = (currentProgress * 10) / progressRange;

            StringBuilder bar = new StringBuilder("[");
            for (int i = 0; i < 10; i++) {
                bar.append(i < percent ? "█" : "░");
            }
            bar.append("] ").append((currentProgress * 100) / progressRange).append("%");

            IO.println(CYAN + "\n--- Progress to Next Title (" + nextTitle + ") ---" + RESET);
            IO.println("Points needed: " + COLOR_TIER_3 + pointsNeeded + " more pts" + RESET);
            IO.println("Progress: " + COLOR_TIER_1 + bar.toString() + RESET);

            // Smart Training Recommendation
            IO.println(COLOR_TIER_3 + "\n💡 Training Advice (Easiest Way to Rank Up):" + RESET);
            List<String> suggestions = new ArrayList<>();
            for (ModeResult r : results) {
                if (r.tier() == 0) {
                    suggestions.add("Get a basic tier in " + BOLD + r.mode() + RESET + " (e.g. LT5 gives +1 pt, LT3 gives +6 pts).");
                } else if (!r.isHigh()) {
                    suggestions.add("Push " + BOLD + r.mode() + RESET + " from LT" + r.tier() + " to HT" + r.tier() + ".");
                }
                if (suggestions.size() >= 2) break;
            }
            if (suggestions.isEmpty()) {
                suggestions.add("Upgrade any of your current tiers to Tier 1 or Tier 2 to gain huge points!");
            }
            for (String s : suggestions) {
                IO.println("  • " + s);
            }
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
                    IO.println(COLOR_TIER_NONE + "Enter a valid number." + RESET);
                    continue;
                }
                if (tier < 0 || tier > 5) {
                    IO.println(COLOR_TIER_NONE + "Invalid tier." + RESET);
                }
            }

            if (tier == 0) return new ModeResult(modeName, 0, false, 0, urlKey);

            String level = "";
            while (!level.equalsIgnoreCase("h") && !level.equalsIgnoreCase("l")) {
                IO.print("High or Low tier? (h/l): ");
                String input = IO.readln();
                if (input == null) break;
                level = input.trim();
            }
            boolean isHigh = level.equalsIgnoreCase("h");

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
            IO.println(CYAN + "\n--- Ranks & Perks Reference Table ---" + RESET);
            IO.println(String.format("%-10s | %-25s | %-35s", "Rank", "Requirements", "Perks"));
            IO.println("-".repeat(78));
            IO.println(String.format("%-10s | %-25s | %-35s", "Elite", "1x HT2+", "Rank tag in chat"));
            IO.println(String.format("%-10s | %-25s | %-35s", "Elite+", "2x HT2+", "Customize armor trim & shield"));
            IO.println(String.format("%-10s | %-25s | %-35s", "MVP+", "1x HT1", "Access to Fly command"));
            IO.println(String.format("%-10s | %-25s | %-35s", "VIP", "2x HT1", "Elytra & fireworks in lobby"));
            IO.println(String.format("%-10s | %-25s | %-35s", "VIP+", "4x HT1", "Tier tester, Spear lunge 3, Wind charges"));
        }

        void printTitleInfoTable() {
            IO.println(CYAN + "\n--- Titles & Points Reference Table ---" + RESET);
            IO.println(String.format("%-15s | %-15s | %-35s", "Title", "Points Required", "Description"));
            IO.println("-".repeat(71));
            IO.println(String.format("%-15s | %-15s | %-35s", "Rookie", "0 - 29 pts", "Starting tier level"));
            IO.println(String.format("%-15s | %-15s | %-35s", "Soldier", "30 - 79 pts", "Basic competitive competence"));
            IO.println(String.format("%-15s | %-15s | %-35s", "Knight", "80 - 149 pts", "Intermediate skilled player"));
            IO.println(String.format("%-15s | %-15s | %-35s", "Fighter", "150 - 249 pts", "Advanced combat specialist"));
            IO.println(String.format("%-15s | %-15s | %-35s", "Warrior", "250 - 399 pts", "High-tier seasoned veteran"));
            IO.println(String.format("%-15s | %-15s | %-35s", "Master", "400 - 599 pts", "Elite global contender"));
            IO.println(String.format("%-15s | %-15s | %-35s", "Grand Master", "600+ pts", "Legendary top-tier player"));
        }