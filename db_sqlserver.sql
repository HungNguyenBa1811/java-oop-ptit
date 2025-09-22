CREATE TABLE [users] (
  [user_id] varchar(15) UNIQUE PRIMARY KEY,
  [username] nvarchar(255) UNIQUE,
  [password] nvarchar(255),
  [full_name] nvarchar(50),
  [email] varchar(100),
  [role] boolean NOT NULL DEFAULT (0),
  [created_at] timestamp,
  [updated_at] timestamp
)
GO

CREATE TABLE [students] (
  [student_id] varchar(15) UNIQUE PRIMARY KEY,
  [class] varchar(20),
  [major] nvarchar(30),
  [faculty] nvarchar(20),
  [created_at] timestamp,
  [updated_at] timestamp
)
GO

CREATE TABLE [courses] (
  [course_id] varchar(15) UNIQUE PRIMARY KEY,
  [course_name] nvarchar(70),
  [credits] int,
  [faculty] varchar(20),
  [created_at] timestamp,
  [updated_at] timestamp
)
GO

CREATE TABLE [course_offerings] (
  [course_offering_id] varchar(15) UNIQUE PRIMARY KEY,
  [course_id] varchar(15) NOT NULL,
  [instructor] nvarchar(50),
  [semester] int,
  [academic_year] nvarchar(255),
  [max_capacity] int,
  [current_capacity] int DEFAULT (0),
  [created_at] timestamp,
  [updated_at] timestamp
)
GO

CREATE TABLE [course_offerings_schedules] (
  [course_offering_id] varchar(15) PRIMARY KEY NOT NULL,
  [start_date] date NOT NULL,
  [end_date] date NOT NULL,
  [day_of_week] int NOT NULL,
  [start_time] time NOT NULL,
  [end_time] time NOT NULL,
  [created_at] timestamp,
  [updated_at] timestamp
)
GO

CREATE TABLE [registrations] (
  [registration_id] varchar(20) UNIQUE PRIMARY KEY,
  [student_id] varchar(15) NOT NULL,
  [course_offering_id] varchar(15) NOT NULL,
  [registered_at] timestamp DEFAULT (now()),
  [status] enum(success,failed),
  [note] text,
  [created_at] timestamp,
  [updated_at] timestamp
)
GO

EXEC sp_addextendedproperty
@name = N'Column_Description',
@value = '0: student, 1: admin',
@level0type = N'Schema', @level0name = 'dbo',
@level1type = N'Table',  @level1name = 'users',
@level2type = N'Column', @level2name = 'role';
GO

EXEC sp_addextendedproperty
@name = N'Column_Description',
@value = 'Course offering ID',
@level0type = N'Schema', @level0name = 'dbo',
@level1type = N'Table',  @level1name = 'course_offerings',
@level2type = N'Column', @level2name = 'course_offering_id';
GO

EXEC sp_addextendedproperty
@name = N'Column_Description',
@value = '1=Monday, 7=Sunday',
@level0type = N'Schema', @level0name = 'dbo',
@level1type = N'Table',  @level1name = 'course_offerings_schedules',
@level2type = N'Column', @level2name = 'day_of_week';
GO

ALTER TABLE [students] ADD FOREIGN KEY ([student_id]) REFERENCES [users] ([user_id])
GO

ALTER TABLE [course_offerings] ADD FOREIGN KEY ([course_id]) REFERENCES [courses] ([course_id])
GO

ALTER TABLE [course_offerings_schedules] ADD FOREIGN KEY ([course_offering_id]) REFERENCES [course_offerings] ([course_offering_id])
GO

ALTER TABLE [registrations] ADD FOREIGN KEY ([student_id]) REFERENCES [students] ([student_id])
GO

ALTER TABLE [registrations] ADD FOREIGN KEY ([course_offering_id]) REFERENCES [course_offerings] ([course_offering_id])
GO
