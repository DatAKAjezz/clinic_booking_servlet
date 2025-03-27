CREATE DATABASE ClinicBookingDB;
GO

USE ClinicBookingDB;
GO

-- 1. Users
CREATE TABLE Users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('guest', 'patient', 'receptionist', 'doctor')),
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15)
);
GO

-- 2. Patients
CREATE TABLE Patients (
    patient_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    age INT CHECK (age >= 0),
    address NVARCHAR(200),
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- 3. Doctors
CREATE TABLE Doctors (
    doctor_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    specialty NVARCHAR(50),
	experience INT CHECK (experience >= 0),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- 4. Schedules
CREATE TABLE Schedules (
    schedule_id INT IDENTITY(1,1) PRIMARY KEY,
    doctor_id INT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('available', 'booked', 'unavailable')),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
);
GO

update Schedules set status = 'available'

-- 5. Services
CREATE TABLE Services (
    service_id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    price DECIMAL(10,2),
    duration INT
);
GO

-- 6. Appointments
CREATE TABLE Appointments (
    appointment_id INT IDENTITY(1,1) PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    schedule_id INT NOT NULL,
    service_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    reason NVARCHAR(200),
    status VARCHAR(20) NOT NULL CHECK (status IN ('pending', 'confirmed', 'canceled', 'completed')),
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE NO ACTION,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE NO ACTION,
    FOREIGN KEY (schedule_id) REFERENCES Schedules(schedule_id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Services(service_id) ON DELETE NO ACTION
);


-- Assuming you want to filter by a specific status, search for patient and doctor names
-- and want to fetch a specific page of results

SELECT a.appointment_id, 
       a.patient_id, 
       a.doctor_id, 
       a.schedule_id, 
       a.service_id, 
       a.appointment_date, 
       a.reason, 
       a.status, 
       a.note, 
       p.full_name AS patient_name, 
       d.full_name AS doctor_name, 
       s.name AS service_name 
FROM Appointments a 
JOIN Patients p ON a.patient_id = p.patient_id 
JOIN Doctors d ON a.doctor_id = d.doctor_id 
JOIN Services s ON a.service_id = s.service_id 
WHERE 1=1
  AND a.status = 'confirmed'  -- Example status filter
  AND p.full_name LIKE '%%'  -- Example patient name search
  AND d.full_name LIKE '%%'  -- Example doctor name search
ORDER BY a.appointment_date
OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY;


GO

-- 7. MedicalRecords (Đã sửa lỗi)
CREATE TABLE MedicalRecords (
    record_id INT IDENTITY(1,1) PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_id INT NOT NULL,
    visit_date DATE NOT NULL,
    diagnosis NVARCHAR(500),
    prescription NVARCHAR(500),
    notes NVARCHAR(1000),
    attachment VARCHAR(200),
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE NO ACTION,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE NO ACTION, -- Sửa từ displDoctors thành Doctors
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) ON DELETE CASCADE
);
GO

-- 8. Reviews
CREATE TABLE Reviews (
    review_id INT IDENTITY(1,1) PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment NVARCHAR(500),
    review_date DATE NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE NO ACTION,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE NO ACTION
);
GO


select * from Doctors where Doctors.user_id = 10

DROP TABLE Schedules; -- Xóa bảng cũ nếu cần
GO

CREATE TABLE Schedules (
    schedule_id INT IDENTITY(1,1) PRIMARY KEY,
    doctor_id INT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('available', 'booked', 'unavailable')),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
);
GO
DROP TABLE Schedules;
GO

CREATE TABLE Schedules (
    schedule_id INT IDENTITY(1,1) PRIMARY KEY,
    doctor_id INT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('available', 'booked', 'unavailable')),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
);
GO
CREATE TRIGGER LimitAppointmentsPerSchedule
ON Appointments
AFTER INSERT, UPDATE
AS
BEGIN
    DECLARE @schedule_id INT, @doctor_id INT, @appointment_date DATE;

    -- Lấy thông tin từ bản ghi vừa thêm/sửa
    SELECT @schedule_id = schedule_id, 
           @doctor_id = doctor_id, 
           @appointment_date = appointment_date 
    FROM INSERTED;

    -- Đếm số lượng cuộc hẹn cho khung giờ và ngày cụ thể
    IF (SELECT COUNT(*) 
        FROM Appointments 
        WHERE schedule_id = @schedule_id 
          AND appointment_date = @appointment_date) > 10
    BEGIN
        RAISERROR ('Mỗi khung giờ của bác sĩ chỉ được phép có tối đa 10 cuộc hẹn.', 16, 1);
        ROLLBACK TRANSACTION;
    END
END;
GO

select * from Doctors

ALTER TABLE Appointments
ADD isActive BIT NOT NULL DEFAULT 1;



