package com.example.kafkaproducerdemo;

import java.util.*;

public class UserProfileGenerator {

    private static final String[] adjectives = {
        "Curious", "Witty", "Chill", "Brave", "Clever", "Happy", "Calm", "Bold", "Quirky", "Gentle"
    };

    private static final String[] nouns = {
        "Explorer", "Penguin", "Coder", "Nomad", "Dreamer", "Thinker", "Rider", "Writer", "Creator", "Artist"
    };

    private static final String[] bioTemplates = {
        "Lover of %s and %s. Living life one %s at a time.",
        "Always seeking %s and %s. Let’s connect!",
        "Fueled by %s, driven by %s. %s enthusiast.",
        "On a journey through %s and %s. Ask me about %s."
    };

    private static final String[] interests = {
        "coffee", "technology", "books", "travel", "music", "design", "nature", "games", "history", "languages"
    };

    private static final Random random = new Random();

    public static String generateUsername() {
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        int number = 100 + random.nextInt(900); // adds some uniqueness
        return adjective + noun + number;
    }

    public static String generateBio() {
        String template = bioTemplates[random.nextInt(bioTemplates.length)];
        String interest1 = interests[random.nextInt(interests.length)];
        String interest2 = interests[random.nextInt(interests.length)];
        String interest3 = interests[random.nextInt(interests.length)];
        return String.format(template, interest1, interest2, interest3);
    }
}

