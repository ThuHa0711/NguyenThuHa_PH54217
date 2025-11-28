CREATE DATABASE task_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE task_management;

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role TINYINT NOT NULL,       -- 1 = USER, 2 = ADMIN
    status TINYINT NOT NULL      -- 1 = active, 0 = inactive
);

-- ============================================
--  TABLE TASK
-- ============================================
CREATE TABLE task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    deadline DATETIME NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,  -- 1 = Pending, 2 = Completed
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- ============================================
--  INSERT USER DATA
--  Mật khẩu gốc: 123456
-- ============================================

SET @BCRYPT_PASSWORD = '$2a$10$4Bep5M87Akr15uboISV8BesAs8K4XNRK9Z3rXkwMmRTvcQvGrBh3C';
INSERT INTO user (username, full_name, password, email, role, status) VALUES
('admin',        'Nguyễn Thu Hà', @BCRYPT_PASSWORD, 'admin@gmail.com',        2, 1),
('vananguyen01', 'Nguyễn Văn A', @BCRYPT_PASSWORD, 'vana@gmail.com',         1, 1),
('thibnguyen02', 'Trần Thị B', @BCRYPT_PASSWORD, 'thib@gmail.com',         1, 1),
('vancnguyen03', 'Nguyễn Văn C', @BCRYPT_PASSWORD, 'vanc@gmail.com',         1, 1);


-- ============================================
--  INSERT TASK DATA
-- ============================================

INSERT INTO task (task_name, created_at, deadline, status, user_id) VALUES
('Hoàn thành báo cáo tuần', NOW(), '2025-12-01 17:00:00', 1, 2),
('Chuẩn bị thuyết trình dự án', NOW(), '2025-12-05 10:00:00', 1, 2),

('Cập nhật tài liệu kỹ thuật', NOW(), '2025-12-03 09:00:00', 1, 3),
('Sửa lỗi module API', NOW(), '2025-12-06 15:00:00', 1, 3),

('Thiết kế giao diện mới', NOW(), '2025-12-02 14:00:00', 1, 4),
('Tối ưu hiệu năng hệ thống', NOW(), '2025-12-04 18:00:00', 1, 4);


SET SQL_SAFE_UPDATES = 0;

-- XÓA SẠCH toàn bộ user cũ (bắt buộc!)
DELETE FROM user;

-- Reset lại ID về 1 (tùy chọn)
ALTER TABLE user AUTO_INCREMENT = 1;

DELETE FROM task;

-- Reset lại ID về 1 (tùy chọn)
ALTER TABLE task AUTO_INCREMENT = 1;

drop database task_management;


