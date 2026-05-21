package com.example.data

import kotlinx.coroutines.flow.Flow

class StudentPortalRepository(private val dao: StudentPortalDao) {
    fun getAllStudents(): Flow<List<Student>> = dao.getAllStudents()

    fun getStudentById(studentId: String): Flow<Student?> = dao.getStudentById(studentId)

    fun getAbsenceLogs(studentId: String): Flow<List<AbsenceLog>> = dao.getAbsenceLogs(studentId)

    fun getMarkRecords(studentId: String): Flow<List<MarkRecord>> = dao.getMarkRecords(studentId)

    fun getAllNotices(): Flow<List<Notice>> = dao.getAllNotices()

    fun getAllEnquiries(): Flow<List<Enquiry>> = dao.getAllEnquiries()

    suspend fun insertEnquiry(enquiry: Enquiry) {
        dao.insertEnquiry(enquiry)
    }
}
