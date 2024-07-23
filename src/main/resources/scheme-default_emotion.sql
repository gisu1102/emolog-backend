DROP TABLE IF EXISTS default_emotion;

CREATE TABLE default_emotion
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    emotion VARCHAR(100) NOT NULL,
    red INT NOT NULL,
    green INT NOT NULL,
    blue INT NOT NULL,
    type VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
)