CREATE TABLE IF NOT EXISTS games (
                                     id BIGSERIAL PRIMARY KEY,
                                     white_player_id VARCHAR(255) NOT NULL,
    black_player_id VARCHAR(255) NOT NULL,
    winner_id VARCHAR(255),
    moves TEXT,
    started_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS idx_games_white_player ON games(white_player_id);
CREATE INDEX IF NOT EXISTS idx_games_black_player ON games(black_player_id);
CREATE INDEX IF NOT EXISTS idx_games_winner ON games(winner_id);