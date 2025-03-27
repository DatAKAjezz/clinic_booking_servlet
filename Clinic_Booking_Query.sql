create database test2
GO

USE test2
GO

/****** Object:  Table [dbo].[Users]    Script Date: 3/27/2025 2:28:28 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Users](
	[user_id] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](50) NOT NULL,
	[password] [varchar](100) NOT NULL,
	[role] [varchar](20) NOT NULL,
	[email] [varchar](100) NOT NULL,
	[phone] [varchar](15) NULL,
	[profile_picture] [text] NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [dbo].[Users] ADD  CONSTRAINT [DF_Users_profile_picture]  DEFAULT (NULL) FOR [profile_picture]
GO

ALTER TABLE [dbo].[Users]  WITH CHECK ADD CHECK  (([role]='doctor' OR [role]='receptionist' OR [role]='patient' OR [role]='guest'))
GO

/****** Object:  Table [dbo].[Services]    Script Date: 3/27/2025 2:24:30 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Services](
	[service_id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[price] [decimal](10, 2) NULL,
	[duration] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[service_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[Doctors]    Script Date: 3/27/2025 1:50:45 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Doctors](
	[doctor_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[full_name] [nvarchar](100) NOT NULL,
	[specialty] [nvarchar](50) NULL,
	[experience] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[doctor_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Doctors]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
ON DELETE CASCADE
GO

ALTER TABLE [dbo].[Doctors]  WITH CHECK ADD CHECK  (([experience]>=(0)))
GO

/****** Object:  Table [dbo].[Patients]    Script Date: 3/27/2025 2:09:14 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Patients](
	[patient_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[full_name] [nvarchar](100) NOT NULL,
	[age] [int] NULL,
	[address] [nvarchar](200) NULL,
	[gender] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[patient_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Patients]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
ON DELETE CASCADE
GO

ALTER TABLE [dbo].[Patients]  WITH CHECK ADD CHECK  (([age]>=(0)))
GO

ALTER TABLE [dbo].[Patients]  WITH CHECK ADD CHECK  (([gender]='Other' OR [gender]='Female' OR [gender]='Male'))
GO

/****** Object:  Table [dbo].[Receptionists]    Script Date: 3/27/2025 2:09:31 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Receptionists](
	[receptionist_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[full_name] [nvarchar](100) NOT NULL,
	[experience] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[receptionist_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Receptionists]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
ON DELETE CASCADE
GO

ALTER TABLE [dbo].[Receptionists]  WITH CHECK ADD CHECK  (([experience]>=(0)))
GO

/****** Object:  Table [dbo].[Schedules]    Script Date: 3/27/2025 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Schedules](
	[schedule_id] [int] IDENTITY(1,1) NOT NULL,
	[doctor_id] [int] NOT NULL,
	[start_time] [datetime] NOT NULL,
	[end_time] [datetime] NOT NULL,
	[is_available] [bit] NOT NULL DEFAULT (1),
PRIMARY KEY CLUSTERED 
(
	[schedule_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Schedules]  WITH CHECK ADD FOREIGN KEY([doctor_id])
REFERENCES [dbo].[Doctors] ([doctor_id])
ON DELETE CASCADE
GO

/****** Object:  Table [dbo].[Appointments]    Script Date: 3/27/2025 1:47:06 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Appointments](
	[appointment_id] [int] IDENTITY(1,1) NOT NULL,
	[patient_id] [int] NULL,
	[doctor_id] [int] NOT NULL,
	[schedule_id] [int] NOT NULL,
	[service_id] [int] NOT NULL,
	[appointment_date] [date] NOT NULL,
	[reason] [nvarchar](200) NULL,
	[status] [varchar](20) NOT NULL,
	[note] [nvarchar](500) NULL,
	[guest_name] [nvarchar](100) NULL,
	[guest_phone] [varchar](20) NULL,
	[isActive] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[appointment_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Appointments] ADD  DEFAULT ((1)) FOR [isActive]
GO

ALTER TABLE [dbo].[Appointments]  WITH CHECK ADD FOREIGN KEY([doctor_id])
REFERENCES [dbo].[Doctors] ([doctor_id])
GO

ALTER TABLE [dbo].[Appointments]  WITH CHECK ADD FOREIGN KEY([patient_id])
REFERENCES [dbo].[Patients] ([patient_id])
GO

ALTER TABLE [dbo].[Appointments]  WITH CHECK ADD FOREIGN KEY([service_id])
REFERENCES [dbo].[Services] ([service_id])
GO

ALTER TABLE [dbo].[Appointments]  WITH CHECK ADD  CONSTRAINT [FK_Appointments_Schedules] FOREIGN KEY([schedule_id])
REFERENCES [dbo].[Schedules] ([schedule_id])
ON DELETE CASCADE
GO

ALTER TABLE [dbo].[Appointments] CHECK CONSTRAINT [FK_Appointments_Schedules]
GO

ALTER TABLE [dbo].[Appointments]  WITH CHECK ADD CHECK  (([status]='completed' OR [status]='canceled' OR [status]='confirmed' OR [status]='pending'))
GO

/****** Object:  Table [dbo].[MedicalRecords]    Script Date: 3/27/2025 1:51:59 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[MedicalRecords](
	[record_id] [int] IDENTITY(1,1) NOT NULL,
	[patient_id] [int] NOT NULL,
	[doctor_id] [int] NOT NULL,
	[appointment_id] [int] NOT NULL,
	[visit_date] [date] NOT NULL,
	[diagnosis] [nvarchar](500) NULL,
	[prescription] [nvarchar](500) NULL,
	[notes] [nvarchar](1000) NULL,
	[attachment] [varchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[record_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[MedicalRecords]  WITH CHECK ADD FOREIGN KEY([appointment_id])
REFERENCES [dbo].[Appointments] ([appointment_id])
ON DELETE CASCADE
GO

ALTER TABLE [dbo].[MedicalRecords]  WITH CHECK ADD FOREIGN KEY([doctor_id])
REFERENCES [dbo].[Doctors] ([doctor_id])
GO

ALTER TABLE [dbo].[MedicalRecords]  WITH CHECK ADD FOREIGN KEY([patient_id])
REFERENCES [dbo].[Patients] ([patient_id])
GO