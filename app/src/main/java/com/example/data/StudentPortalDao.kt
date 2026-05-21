package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentPortalDao {
    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
    fun getStudentById(studentId: String): Flow<Student?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<Student>)

    @Query("SELECT * FROM absence_logs WHERE studentId = :studentId")
    fun getAbsenceLogs(studentId: String): Flow<List<AbsenceLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAbsenceLogs(logs: List<AbsenceLog>)

    @Query("SELECT * FROM mark_records WHERE studentId = :studentId")
    fun getMarkRecords(studentId: String): Flow<List<MarkRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkRecords(records: List<MarkRecord>)

    @Query("SELECT * FROM notices")
    fun getAllNotices(): Flow<List<Notice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices: List<Notice>)

    @Query("SELECT * FROM enquiries ORDER BY timestamp DESC")
    fun getAllEnquiries(): Flow<List<Enquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: Enquiry)
}
