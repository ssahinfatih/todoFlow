CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,

                                token VARCHAR(500) NOT NULL UNIQUE,

                                user_id BIGINT NOT NULL,

                                expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

                                revoked BOOLEAN NOT NULL DEFAULT FALSE,

                                used BOOLEAN NOT NULL DEFAULT FALSE,

                                CONSTRAINT fk_refresh_tokens_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id)
                                        ON DELETE CASCADE
);