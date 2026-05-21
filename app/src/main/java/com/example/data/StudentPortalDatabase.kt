package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Student::class, AbsenceLog::class, MarkRecord::class, Notice::class, Enquiry::class],
    version = 1,
    exportSchema = false
)
abstract class StudentPortalDatabase : RoomDatabase() {
    abstract fun studentPortalDao(): StudentPortalDao

    companion object {
        @Volatile
        private var INSTANCE: StudentPortalDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): StudentPortalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentPortalDatabase::class.java,
                    "student_portal_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.studentPortalDao())
                }
            }
        }

        suspend fun populateDatabase(dao: StudentPortalDao) {
            // 1. Insert Students
            val students = listOf(
                Student(
                    id = "st001",
                    name = "Akshayan",
                    classRoom = "Class 3-A",
                    rollNo = "01",
                    dob = "12 May 2014",
                    parentName = "Mr. Madhusudan Nayar",
                    motherName = "Mrs. Lakshmi Nayar",
                    emergencyContact = "+91 98765 43210",
                    bloodGroup = "O+",
                    address = "123 Greenfield Lane, Kochi, Kerala.",
                    avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDftcCeDkkb0K4V-IlArY6p7uj-drNEgsQBON_AmkXa9YanPbhL8X0uz85jHWc0aYYApp5Vk_MeMNfCr67hlV0gAjzWh1gv65J3BCo2vKKVQRA42OKmbtcIyz4TAm76UcHWjWrBbBccMuZPTqqISJWnDLyAfspqnPGckAiG_2ZAHBT_8UY3L632f5NYS8O3SJ-KRVbi_GJutKkSdk4dXUGA2mp2gaUn4jfyJCBIPKm1h4qvHvFuVwKb9jD3vyEpNLVuhwgGhDCgQ4o",
                    admissionDate = "05 April 2020",
                    attendancePercentage = 92,
                    totalWorkingDays = 22,
                    daysPresent = 20,
                    daysAbsent = 2
                ),
                Student(
                    id = "st002",
                    name = "Aarav Sharma",
                    classRoom = "Class 3-A",
                    rollNo = "02",
                    dob = "18 August 2014",
                    parentName = "Mr. Madhusudan Nayar",
                    motherName = "Mrs. Lakshmi Nayar",
                    emergencyContact = "+91 98765 43210",
                    bloodGroup = "B+",
                    address = "123 Greenfield Lane, Kochi, Kerala.",
                    avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAGPQd1gnh8LWisXMkvnxNg4LfYiqhVzUB3thQTH4VafWqc23xiinw_QI1FnMw5hTS04vSATbmstqDLxeQ505NJ3YosTFuU_Zbyz4idLB5DGWKsJ_7GHiAH6rFKobp0HqnmpTy7Svqge9vu77mAibbxbOo7bQxJGoT7qEl5E-9cO0PY_bqMKtpd1-TMt248eskq9M7YURiXuwqUORUvfGVWwqv4311NTJwvWLLlAdL3u4HljuJCvZMpur1E7weJfhX5KfPvdK5urW_V",
                    admissionDate = "12 June 2020",
                    attendancePercentage = 92,
                    totalWorkingDays = 22,
                    daysPresent = 20,
                    daysAbsent = 2
                )
            )
            dao.insertStudents(students)

            // 2. Insert Absences
            val absences = listOf(
                AbsenceLog(
                    studentId = "st001",
                    date = "14 May 2024",
                    reason = "Medical Leave",
                    isExcused = true,
                    notes = "Documented by Physician Office"
                ),
                AbsenceLog(
                    studentId = "st001",
                    date = "03 May 2024",
                    reason = "Family Event",
                    isExcused = false,
                    notes = "Parental notification received late"
                ),
                AbsenceLog(
                    studentId = "st002",
                    date = "14 May 2024",
                    reason = "Medical Leave",
                    isExcused = true,
                    notes = "Documented by Physician Office"
                ),
                AbsenceLog(
                    studentId = "st002",
                    date = "03 May 2024",
                    reason = "Family Event",
                    isExcused = false,
                    notes = "Parental notification received late"
                )
            )
            dao.insertAbsenceLogs(absences)

            // 3. Insert Mark Records
            val marks = listOf(
                MarkRecord(studentId = "st002", subject = "English", score = 88),
                MarkRecord(studentId = "st002", subject = "Maths", score = 90),
                MarkRecord(studentId = "st002", subject = "Science", score = 85),
                MarkRecord(studentId = "st002", subject = "EVS", score = 80),
                MarkRecord(studentId = "st002", subject = "Computer", score = 78),

                MarkRecord(studentId = "st001", subject = "English", score = 92),
                MarkRecord(studentId = "st001", subject = "Maths", score = 94),
                MarkRecord(studentId = "st001", subject = "Science", score = 88),
                MarkRecord(studentId = "st001", subject = "EVS", score = 85),
                MarkRecord(studentId = "st001", subject = "Computer", score = 82)
            )
            dao.insertMarkRecords(marks)

            // 4. Insert Notices
            val notices = listOf(
                Notice(
                    title = "Annual Day Celebration",
                    content = "Dear Parents, our Annual Day will be celebrated on 25th May with great joy and excitement. All students are requested to be present in their full uniforms.",
                    timeAgo = "2 hours ago",
                    dateStr = "25 May, 2024",
                    timeStr = "",
                    tag = "Annual Day Celebration",
                    colorType = "primary",
                    isUrgent = false,
                    iconName = "campaign"
                ),
                Notice(
                    title = "Holiday Notice",
                    content = "School will remain closed on Monday, 20th May due to regional public holiday. Classes resume Tuesday.",
                    timeAgo = "1 day ago",
                    dateStr = "20 May, 2024",
                    timeStr = "",
                    tag = "Public Holiday",
                    colorType = "tertiary",
                    isUrgent = false,
                    iconName = "holiday_village"
                ),
                Notice(
                    title = "Parent Teacher Meeting",
                    content = "PTM will be held on 30th May to discuss the Term 1 progress. Timing: 10 AM to 1 PM in Room 302.",
                    timeAgo = "2 days ago",
                    dateStr = "30 May, 2024",
                    timeStr = "10:00 AM - 01:00 PM",
                    tag = "Parent Teacher Meeting",
                    colorType = "secondary",
                    isUrgent = false,
                    iconName = "groups"
                ),
                Notice(
                    title = "Fee Payment Reminder",
                    content = "This is a gentle reminder that the deadline for the second installment is 20th May. Late fees apply thereafter.",
                    timeAgo = "3 days ago",
                    dateStr = "20 May",
                    timeStr = "Deadline: 20 May",
                    tag = "Fee Payment Reminder",
                    colorType = "error",
                    isUrgent = false,
                    iconName = "payments"
                )
            )
            dao.insertNotices(notices)
        }
    }
}
