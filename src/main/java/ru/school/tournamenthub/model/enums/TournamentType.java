package ru.school.tournamenthub.model.enums;

public enum TournamentType {

    REGULAR_SEASON("Регулярный сезон"),    // Турнир в рамках регулярного сезона
    CHAMPIONSHIP("Чемпионат"),            // Чемпионат города/региона
    CUP("Кубок"),                         // Кубковые соревнования
    FRIENDLY("Товарищеский турнир"),      // Товарищеские встречи
    QUALIFICATION("Квалификация"),        // Квалификационный турнир
    PLAYOFF("Плей-офф");

    private final String description;

    TournamentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
