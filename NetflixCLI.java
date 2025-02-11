import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class User {
    private String name;
    private Map<String, Integer> watchHistory; // Genre to watch time in minutes

    public User(String name) {
        this.name = name;
        this.watchHistory = new HashMap<>();
    }

    public void watchContent(String genre, int duration) {
        watchHistory.put(genre, watchHistory.getOrDefault(genre, 0) + duration);
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getWatchHistory() {
        return watchHistory;
    }
}

class NetflixSystem {
    private List<User> users;
    private Map<String, List<String>> genreMovies;
    private List<String> availableGenres;

    public NetflixSystem() {
        this.users = new ArrayList<>();
        this.genreMovies = new HashMap<>();
        this.availableGenres = new ArrayList<>();
        initializeGenres();
    }

    private void initializeGenres() {
        genreMovies.put("Action", List.of("The Dark Knight", "Mad Max: Fury Road", "John Wick"));
        genreMovies.put("Comedy", List.of("Superbad", "The Hangover", "Bridesmaids"));
        genreMovies.put("Drama", List.of("The Shawshank Redemption", "Forrest Gump", "The Godfather"));
        genreMovies.put("Sci-Fi", List.of("Inception", "Blade Runner 2049", "The Matrix"));
        genreMovies.put("Horror", List.of("The Conjuring", "Hereditary", "Get Out"));

        availableGenres.addAll(genreMovies.keySet());
    }

    public void addUser(String name) {
        users.add(new User(name));
    }

    public void recordWatchHistory(String userName, String genre, int duration) {
        for (User user : users) {
            if (user.getName().equals(userName)) {
                user.watchContent(genre, duration);
                return;
            }
        }
        System.out.println("User not found.");
    }

    public void recommendContent(String userName) {
        User targetUser = null;
        for (User user : users) {
            if (user.getName().equals(userName)) {
                targetUser = user;
                break;
            }
        }

        if (targetUser == null) {
            System.out.println("User not found.");
            return;
        }

        Map<String, Integer> history = targetUser.getWatchHistory();
        String favoriteGenre = "";
        int maxWatchTime = 0;

        for (Map.Entry<String, Integer> entry : history.entrySet()) {
            if (entry.getValue() > maxWatchTime) {
                maxWatchTime = entry.getValue();
                favoriteGenre = entry.getKey();
            }
        }

        if (!favoriteGenre.isEmpty()) {
            List<String> movies = genreMovies.get(favoriteGenre);
            String recommendedMovie = movies.get((int) (Math.random() * movies.size()));
            System.out.println("Recommended genre for " + userName + ": " + favoriteGenre);
            System.out.println("Suggested movie: " + recommendedMovie);
        } else {
            System.out.println("No watch history to make a recommendation.");
        }
    }

    public List<String> getAvailableGenres() {
        return availableGenres;
    }
}

public class NetflixCLI {
    public static void main(String[] args) {
        NetflixSystem netflix = new NetflixSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add User\n2. Record Watch History\n3. Recommend Content\n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter user name:");
                    String name = scanner.nextLine();
                    netflix.addUser(name);
                    break;
                case 2:
                    System.out.println("Enter user name:");
                    String userName = scanner.nextLine();
                    List<String> genres = netflix.getAvailableGenres();
                    System.out.println("Select a genre by number:");
                    for (int i = 0; i < genres.size(); i++) {
                        System.out.println((i + 1) + ". " + genres.get(i));
                    }
                    int genreChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (genreChoice > 0 && genreChoice <= genres.size()) {
                        String selectedGenre = genres.get(genreChoice - 1);
                        System.out.println("Enter watch duration (minutes):");
                        int duration = scanner.nextInt();
                        netflix.recordWatchHistory(userName, selectedGenre, duration);
                    } else {
                        System.out.println("Invalid genre choice.");
                    }
                    break;
                case 3:
                    System.out.println("Enter user name:");
                    String user = scanner.nextLine();
                    netflix.recommendContent(user);
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}