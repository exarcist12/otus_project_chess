CREATE TABLE IF NOT EXISTS players (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    rating INT NOT NULL,
    games_played INT NOT NULL,
    games_won INT NOT NULL,
    password VARCHAR(255) NOT NULL
    );

INSERT INTO players (id, name, rating, games_played, games_won, password)
SELECT 'player1', 'Player One', 1200, 0, 0, 'pwd1'
    WHERE NOT EXISTS (SELECT 1 FROM players WHERE id = 'player1');

INSERT INTO players (id, name, rating, games_played, games_won, password)
SELECT 'player2', 'Player Two', 1200, 0, 0, 'pwd2'
    WHERE NOT EXISTS (SELECT 1 FROM players WHERE id = 'player2');

INSERT INTO players (id, name, rating, games_played, games_won, password)
SELECT 'player3', 'Player Three', 1200, 0, 0, 'pwd3'
    WHERE NOT EXISTS (SELECT 1 FROM players WHERE id = 'player3');

INSERT INTO players (id, name, rating, games_played, games_won, password)
SELECT 'player4', 'Player Four', 1200, 0, 0, 'pwd4'
    WHERE NOT EXISTS (SELECT 1 FROM players WHERE id = 'player4');