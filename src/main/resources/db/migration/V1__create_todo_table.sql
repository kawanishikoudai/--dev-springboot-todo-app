CREATE TABLE todo (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    category    VARCHAR(100),
    status      VARCHAR(50)  NOT NULL DEFAULT 'PENDING',
    due_date    DATE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
