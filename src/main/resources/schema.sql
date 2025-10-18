-- Удаление существующих таблиц (если нужно пересоздать)
DROP TABLE IF EXISTS team_reports CASCADE;
DROP TABLE IF EXISTS match_player_statistics CASCADE;
DROP TABLE IF EXISTS tournament_participations CASCADE;
DROP TABLE IF EXISTS match_statistics CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS athletes CASCADE;
DROP TABLE IF EXISTS tournaments CASCADE;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS seasons CASCADE;
DROP TABLE IF EXISTS users CASCADE;


-- Создание таблицы пользователей
CREATE TABLE "users"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "username" VARCHAR(50) NOT NULL UNIQUE,
    "email" VARCHAR(100) NOT NULL UNIQUE,
    "password_hash" VARCHAR(255) NOT NULL,
    "role" VARCHAR(20) NOT NULL,
    "full_name" VARCHAR(100) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "is_active" BOOLEAN NOT NULL DEFAULT TRUE,
    "version" BIGINT NOT NULL DEFAULT 0
);

-- Создание таблицы сезонов
CREATE TABLE "seasons"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" VARCHAR(50) NOT NULL UNIQUE,
    "start_year" INTEGER NOT NULL,
    "end_year" INTEGER NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT FALSE,
    "description" TEXT,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы команд
CREATE TABLE "teams"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" VARCHAR(100) NOT NULL,
    "city" VARCHAR(50) NOT NULL,
    "coach_id" UUID NOT NULL REFERENCES "users"("id"),
    "owner_id" UUID NOT NULL REFERENCES "users"("id"),
    "gender" VARCHAR(10) NOT NULL,
    "year_group" VARCHAR(10),
    "full_display_name" VARCHAR(200),
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "version" BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT "teams_unique_composite" UNIQUE ("name", "city", "gender", "year_group", "coach_id")
);

-- Создание таблицы турниров
CREATE TABLE "tournaments"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "name" VARCHAR(100) NOT NULL,
    "description" TEXT,
    "start_date" DATE,
    "end_date" DATE,
    "location" VARCHAR(100),
    "status" VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    "tournament_gender" VARCHAR(10),
    "age_category" VARCHAR(20),
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "created_by" UUID NOT NULL REFERENCES "users"("id"),
    "owner_id" UUID NOT NULL REFERENCES "users"("id"),
    "tournament_type" VARCHAR(20) NOT NULL DEFAULT 'REGULAR_SEASON',
    "max_teams" INTEGER,
    "format" VARCHAR(50),
    "season_id" UUID NOT NULL REFERENCES "seasons"("id")
);

-- Создание таблицы спортсменов
CREATE TABLE "athletes"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "first_name" VARCHAR(50) NOT NULL,
    "last_name" VARCHAR(50) NOT NULL,
    "birth_date" DATE NOT NULL,
    "team_id" UUID NOT NULL REFERENCES "teams"("id") ON DELETE CASCADE,
    "gender" VARCHAR(10) NOT NULL,
    "jersey_number" INTEGER,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы матчей
CREATE TABLE "matches"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "tournament_id" UUID NOT NULL REFERENCES "tournaments"("id"),
    "home_team_id" UUID NOT NULL REFERENCES "teams"("id"),
    "away_team_id" UUID NOT NULL REFERENCES "teams"("id"),
    "match_date" TIMESTAMPTZ NOT NULL,
    "status" VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    "round" VARCHAR(50) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы командной статистики матча
CREATE TABLE "match_statistics"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "match_id" UUID NOT NULL UNIQUE REFERENCES "matches"("id"),
    "home_team_score" INTEGER NOT NULL DEFAULT 0,
    "away_team_score" INTEGER NOT NULL DEFAULT 0,
    "home_team_fouls" INTEGER NOT NULL DEFAULT 0,
    "away_team_fouls" INTEGER NOT NULL DEFAULT 0,
    "recorded_by" UUID NOT NULL REFERENCES "users"("id"),
    "recorded_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы участия команд в турнирах
CREATE TABLE "tournament_participations"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "tournament_id" UUID NOT NULL REFERENCES "tournaments"("id"),
    "team_id" UUID NOT NULL REFERENCES "teams"("id"),
    "group_name" VARCHAR(20),
    "final_position" INTEGER,
    CONSTRAINT "tournament_participation_unique" UNIQUE ("tournament_id", "team_id")
);

-- Создание таблицы индивидуальной статистики игроков
CREATE TABLE "match_player_statistics"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "match_id" UUID NOT NULL REFERENCES "matches"("id"),
    "athlete_id" UUID NOT NULL REFERENCES "athletes"("id"),
    "points_scored" INTEGER NOT NULL DEFAULT 0,
    "fouls_committed" INTEGER NOT NULL DEFAULT 0,
    "minutes_played" INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT "player_match_unique" UNIQUE ("match_id", "athlete_id")
);

-- Создание таблицы отчетов
CREATE TABLE "team_reports"(
    "id" UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "report_type" VARCHAR(50) NOT NULL,
    "season_name" VARCHAR(50),
    "tournament_name" VARCHAR(100),
    "team_display_name" VARCHAR(200),
    "athlete_name" VARCHAR(100),
    "city_filter" VARCHAR(50),
    "gender_filter" VARCHAR(10),
    "year_group_filter" VARCHAR(10),
    "season_id" UUID NOT NULL REFERENCES "seasons"("id"),
    "tournament_id" UUID REFERENCES "tournaments"("id") ON DELETE SET NULL,
    "team_id" UUID REFERENCES "teams"("id") ON DELETE SET NULL,
    "athlete_id" UUID REFERENCES "athletes"("id") ON DELETE SET NULL,
    "report_data" JSONB NOT NULL,
    "generated_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "generated_by" UUID NOT NULL REFERENCES "users"("id")
);

-- Создание индексов для оптимизации запросов
CREATE INDEX "idx_users_username" ON "users"("username");
CREATE INDEX "idx_users_email" ON "users"("email");
CREATE INDEX "idx_users_role" ON "users"("role");

CREATE INDEX "idx_teams_coach_id" ON "teams"("coach_id");
CREATE INDEX "idx_teams_owner_id" ON "teams"("owner_id");
CREATE INDEX "idx_teams_city_gender" ON "teams"("city", "gender");
CREATE INDEX "idx_teams_name_city" ON "teams"("name", "city");

CREATE INDEX "idx_tournaments_owner_id" ON "tournaments"("owner_id");
CREATE INDEX "idx_tournaments_created_by" ON "tournaments"("created_by");
CREATE INDEX "idx_tournaments_season_id" ON "tournaments"("season_id");
CREATE INDEX "idx_tournaments_status" ON "tournaments"("status");

CREATE INDEX "idx_athletes_team_id" ON "athletes"("team_id");
CREATE INDEX "idx_athletes_birth_date" ON "athletes"("birth_date");
CREATE INDEX "idx_athletes_gender" ON "athletes"("gender");
CREATE INDEX "idx_athletes_name" ON "athletes"("first_name", "last_name");

CREATE INDEX "idx_matches_tournament_id" ON "matches"("tournament_id");
CREATE INDEX "idx_matches_date" ON "matches"("match_date");
CREATE INDEX "idx_matches_status" ON "matches"("status");
CREATE INDEX "idx_matches_teams" ON "matches"("home_team_id", "away_team_id");

CREATE INDEX "idx_tournament_participations_tournament" ON "tournament_participations"("tournament_id");
CREATE INDEX "idx_tournament_participations_team" ON "tournament_participations"("team_id");
CREATE INDEX "idx_tournament_participations_group" ON "tournament_participations"("group_name");

CREATE INDEX "idx_match_player_stats_match" ON "match_player_statistics"("match_id");
CREATE INDEX "idx_match_player_stats_athlete" ON "match_player_statistics"("athlete_id");

CREATE INDEX "idx_team_reports_filters" ON "team_reports"("season_id", "tournament_id", "team_id");
CREATE INDEX "idx_team_reports_generated_by" ON "team_reports"("generated_by");
CREATE INDEX "idx_team_reports_type" ON "team_reports"("report_type");
CREATE INDEX "idx_team_reports_generated_at" ON "team_reports"("generated_at");

-- Триггеры
-- Триггер для автоматического обновления updated_at в users
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON "users"
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Триггер для автоматического обновления updated_at в teams
CREATE TRIGGER update_teams_updated_at BEFORE UPDATE ON "teams"
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Триггер для автоматической генерации full_display_name в teams
CREATE OR REPLACE FUNCTION generate_team_display_name()
RETURNS TRIGGER AS $$
BEGIN
    NEW.full_display_name :=
        CASE NEW.gender
            WHEN 'MALE' THEN 'Юноши'
            ELSE 'Девушки'
        END || ', ' || NEW.name ||
        CASE WHEN NEW.year_group IS NOT NULL THEN ' ' || NEW.year_group ELSE '' END ||
        ', ' || NEW.city;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER generate_team_display_name_trigger BEFORE INSERT OR UPDATE ON "teams"
    FOR EACH ROW EXECUTE FUNCTION generate_team_display_name();

-- Комментарии к таблицам и полям для документации
COMMENT ON TABLE users IS 'Таблица пользователей системы';
COMMENT ON COLUMN users.role IS 'Роль пользователя: ADMIN, COACH';
COMMENT ON COLUMN users.username IS 'Уникальное имя пользователя для входа в систему';
COMMENT ON COLUMN users.email IS 'Электронная почта пользователя';
COMMENT ON COLUMN users.is_active IS 'Флаг активности учетной записи';

COMMENT ON TABLE seasons IS 'Таблица спортивных сезонов';
COMMENT ON COLUMN seasons.is_active IS 'Флаг активного сезона';
COMMENT ON COLUMN seasons.name IS 'Название сезона в формате "2024-2025"';

COMMENT ON TABLE teams IS 'Таблица спортивных команд';
COMMENT ON COLUMN teams.gender IS 'Пол состава: MALE, FEMALE';
COMMENT ON COLUMN teams.year_group IS 'Возрастная группа: 2005, 2006 и т.д.';
COMMENT ON COLUMN teams.full_display_name IS 'Автоматически генерируемое отображаемое имя команды';

COMMENT ON TABLE tournaments IS 'Таблица турниров и соревнований';
COMMENT ON COLUMN tournaments.status IS 'Статус турнира: PLANNED, ONGOING, COMPLETED, CANCELLED';
COMMENT ON COLUMN tournaments.tournament_type IS 'Тип турнира: REGULAR_SEASON, CHAMPIONSHIP, CUP, FRIENDLY, QUALIFICATION, PLAYOFF';
COMMENT ON COLUMN tournaments.tournament_gender IS 'Пол для турнира: MALE, FEMALE';

COMMENT ON TABLE athletes IS 'Таблица спортсменов (игроков)';
COMMENT ON COLUMN athletes.gender IS 'Пол спортсмена: MALE, FEMALE';
COMMENT ON COLUMN athletes.jersey_number IS 'Игровой номер спортсмена';

COMMENT ON TABLE matches IS 'Таблица матчей';
COMMENT ON COLUMN matches.status IS 'Статус матча: SCHEDULED, LIVE, COMPLETED, CANCELLED';
COMMENT ON COLUMN matches.round IS 'Раунд турнира: групповой этап, 1/4 финала, полуфинал, финал';

COMMENT ON TABLE match_statistics IS 'Статистика матчей по командам';
COMMENT ON TABLE match_player_statistics IS 'Индивидуальная статистика игроков по матчам';
COMMENT ON TABLE tournament_participations IS 'Участие команд в турнирах';
COMMENT ON TABLE team_reports IS 'Отчеты и аналитика по командам и игрокам';

-- Вставка тестовых данных (опционально)
INSERT INTO "users" ("id", "username", "email", "password_hash", "role", "full_name", "is_active") VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'admin', 'admin@school.ru', '$2a$10$xyz123', 'ADMIN', 'Администратор Системы', true),
    ('123e4567-e89b-12d3-a456-426614174001', 'coach_ivanov', 'ivanov@school.ru', '$2a$10$xyz124', 'COACH', 'Иванов Иван Иванович', true);

INSERT INTO "seasons" ("id", "name", "start_year", "end_year", "is_active", "description") VALUES
    ('223e4567-e89b-12d3-a456-426614174000', '2024-2025', 2024, 2025, true, 'Основной спортивный сезон 2024-2025');