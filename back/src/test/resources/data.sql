ALTER TABLE SESSIONS DROP CONSTRAINT IF EXISTS fk_sessions_teacher;
ALTER TABLE PARTICIPATE DROP CONSTRAINT IF EXISTS fk_participate_user;
ALTER TABLE PARTICIPATE DROP CONSTRAINT IF EXISTS fk_participate_session;

DROP TABLE IF EXISTS PARTICIPATE;
DROP TABLE IF EXISTS SESSIONS;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS TEACHERS;

CREATE TABLE TEACHERS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    last_name VARCHAR(40),
    first_name VARCHAR(40),
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE USERS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    last_name VARCHAR(40),
    first_name VARCHAR(40),
    admin BOOLEAN NOT NULL DEFAULT false,
    email VARCHAR(255),
    password VARCHAR(255),
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE SESSIONS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    description VARCHAR(2000),
    date TIMESTAMP(6),
    teacher_id INT,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE PARTICIPATE (
    user_id INT,
    session_id INT,
    PRIMARY KEY (user_id, session_id)
);

ALTER TABLE SESSIONS ADD CONSTRAINT fk_sessions_teacher
    FOREIGN KEY (teacher_id) REFERENCES TEACHERS(id);

ALTER TABLE PARTICIPATE ADD CONSTRAINT fk_participate_user
    FOREIGN KEY (user_id) REFERENCES USERS(id);

ALTER TABLE PARTICIPATE ADD CONSTRAINT fk_participate_session
    FOREIGN KEY (session_id) REFERENCES SESSIONS(id);


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('John', 'Doe', false, 'john.doe@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

