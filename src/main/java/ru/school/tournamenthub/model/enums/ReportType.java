package ru.school.tournamenthub.model.enums;

public enum ReportType {
    TEAM_SEASON_STATS,      // Статистика команды за сезон
    TEAM_TOURNAMENT_STATS,  // Статистика команды в турнире
    PLAYER_SEASON_STATS,    // Статистика игрока за сезон
    PLAYER_TOURNAMENT_STATS,// Статистика игрока в турнире
    TEAM_COMPARISON,        // Сравнение команд за разные сезоны
    TOURNAMENT_SUMMARY,     // Итоги турнира
    SCHOOL_SUMMARY          // Сводный отчет по школе (всем командам)
}
