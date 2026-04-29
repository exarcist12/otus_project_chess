CREATE TABLE IF NOT EXISTS messages (
                                        id BIGSERIAL PRIMARY KEY,
                                        sender VARCHAR(255) NOT NULL,
    content VARCHAR(1000) NOT NULL,
    timestamp VARCHAR(50) NOT NULL,
    room_id VARCHAR(255) NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX idx_messages_room_id ON messages(room_id);
CREATE INDEX idx_messages_received_at ON messages(received_at);