USE ClinicBookingDB;
GO

-- 1. Thêm dữ liệu vào Users (5 patient, 3 doctor, 2 receptionist)
INSERT INTO Users (username, password, role, email, phone) VALUES
    ('patient1', '1', 'patient', 'patient1@example.com', '0901234567'),
    ('patient2', '1', 'patient', 'patient2@example.com', '0912345678'),
    ('patient3', '1', 'patient', 'patient3@example.com', '0923456789'),
    ('patient4', '1', 'patient', 'patient4@example.com', '0934567890'),
    ('patient5', '1', 'patient', 'patient5@example.com', '0945678901'),
    ('doctor1', '1', 'doctor', 'doctor1@example.com', '0901234568'),
    ('doctor2', '1', 'doctor', 'doctor2@example.com', '0912345679'),
    ('doctor3', '1', 'doctor', 'doctor3@example.com', '0923456780'),
    ('recep1', '1', 'receptionist', 'recep1@example.com', '0901234569'),
    ('recep2', '1', 'receptionist', 'recep2@example.com', '0912345680');
GO

-- 2. Thêm dữ liệu vào Patients (5 bệnh nhân)
INSERT INTO Patients (user_id, full_name, age, address, gender) VALUES
    (1, N'Nguyễn Văn A', 30, N'123 Đường Láng, Hà Nội', 'Male'),
    (2, N'Trần Thị B', 25, N'45 Lê Lợi, TP.HCM', 'Female'),
    (3, N'Lê Văn C', 40, N'78 Nguyễn Huệ, Đà Nẵng', 'Male'),
    (4, N'Phạm Thị D', 35, N'12 Trần Phú, Hải Phòng', 'Female'),
    (5, N'Hoàng Văn E', 50, N'56 Hùng Vương, Huế', 'Male');
GO

-- 3. Thêm dữ liệu vào Doctors (3 bác sĩ)
INSERT INTO Doctors (user_id, full_name, specialty, experience) VALUES
    (6, N'Nguyễn Thị X', N'Nội khoa', 8),
    (7, N'Trần Văn Y', N'Nha khoa', 5),
    (8, N'Lê Thị Z', N'X-quang', 10);
GO

-- 4. Thêm dữ liệu vào Services (5 dịch vụ)
INSERT INTO Services (name, price, duration) VALUES
    (N'Khám tổng quát', 150000.00, 30),
    (N'Chụp X-quang', 200000.00, 15),
    (N'Khám răng', 100000.00, 20),
    (N'Xét nghiệm máu', 250000.00, 10),
    (N'Khám chuyên sâu nội khoa', 300000.00, 45);
GO

-- 5. Thêm dữ liệu vào Schedules (10 khung giờ làm việc cho các bác sĩ)
INSERT INTO Schedules (doctor_id, date, start_time, end_time, status) VALUES
    (1, '2025-03-21', '08:00', '08:30', 'available'),
    (1, '2025-03-21', '08:30', '09:00', 'available'),
    (1, '2025-03-21', '09:00', '09:30', 'available'),
    (2, '2025-03-21', '10:00', '10:20', 'available'),
    (2, '2025-03-21', '10:20', '10:40', 'available'),
    (2, '2025-03-22', '14:00', '14:20', 'available'),
    (3, '2025-03-21', '13:00', '13:15', 'available'),
    (3, '2025-03-21', '13:15', '13:30', 'available'),
    (3, '2025-03-22', '08:00', '08:15', 'available'),
    (1, '2025-03-22', '09:00', '09:30', 'available');
GO

-- 6. Thêm dữ liệu vào Appointments (8 lịch hẹn)
INSERT INTO Appointments (patient_id, doctor_id, schedule_id, service_id, appointment_date, appointment_time, reason, status) VALUES
    (1, 1, 1, 1, '2025-03-21', '08:00', N'Đau bụng', 'confirmed'),
    (2, 1, 2, 5, '2025-03-21', '08:30', N'Khó thở', 'pending'),
    (3, 2, 4, 3, '2025-03-21', '10:00', N'Đau răng', 'confirmed'),
    (4, 2, 5, 3, '2025-03-21', '10:20', N'Sâu răng', 'completed'),
    (5, 3, 7, 2, '2025-03-21', '13:00', N'Chụp phổi', 'completed'),
    (1, 3, 8, 2, '2025-03-21', '13:15', N'Chụp xương', 'pending'),
    (2, 2, 6, 3, '2025-03-22', '14:00', N'Nhổ răng', 'confirmed'),
    (3, 1, 10, 1, '2025-03-22', '09:00', N'Kiểm tra sức khỏe', 'pending');
GO

-- Cập nhật trạng thái Schedules sau khi có lịch hẹn
UPDATE Schedules SET status = 'booked' WHERE schedule_id IN (1, 2, 4, 5, 7, 8, 6, 10);
GO

-- 7. Thêm dữ liệu vào MedicalRecords (4 hồ sơ y tế cho các lịch hẹn completed)
INSERT INTO MedicalRecords (patient_id, doctor_id, appointment_id, visit_date, diagnosis, prescription, notes, attachment) VALUES
    (4, 2, 4, '2025-03-21', N'Sâu răng cấp độ 2', N'Uống paracetamol 500mg', N'Cần nhổ nếu tái phát', NULL),
    (5, 3, 5, '2025-03-21', N'Phổi bình thường', N'Không cần thuốc', N'Hình ảnh X-quang ổn', 'xray_phoi_001.jpg'),
    (1, 1, 1, '2025-03-21', N'Viêm dạ dày nhẹ', N'Omeprazole 20mg', N'Ăn uống điều độ', NULL),
    (2, 2, 7, '2025-03-22', N'Răng khôn mọc lệch', N'Nhổ răng', N'Đã nhổ thành công', NULL);
GO

-- 8. Thêm dữ liệu vào Reviews (5 đánh giá cho các bác sĩ)
INSERT INTO Reviews (patient_id, doctor_id, rating, comment, review_date) VALUES
    (1, 1, 4, N'Bác sĩ nhiệt tình, giải thích rõ', '2025-03-21'),
    (4, 2, 5, N'Khám răng rất nhẹ nhàng', '2025-03-21'),
    (5, 3, 3, N'Chụp X-quang nhanh nhưng ít tư vấn', '2025-03-21'),
    (2, 2, 4, N'Nhổ răng không đau, hài lòng', '2025-03-22'),
    (3, 1, 5, N'Bác sĩ rất chuyên nghiệp', '2025-03-22');
GO