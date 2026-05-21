package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey val id: String,
    val name: String,
    val classRoom: String,
    val rollNo: String,
    val dob: String,
    val parentName: String,
    val motherName: String,
    val emergencyContact: String,
    val bloodGroup: String,
    val address: String,
    val avatarUrl: String,
    val admissionDate: String,
    val attendancePercentage: Int,
    val totalWorkingDays: Int,
    val daysPresent: Int,
    val daysAbsent: Int
)

@Entity(tableName = "absence_logs")
data class AbsenceLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,
    val date: String,
    val reason: String,
    val isExcused: Boolean,
    val notes: String
)

@Entity(tableName = "mark_records")
data class MarkRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: String,
    val subject: String,
    val score: Int,
    val maxScore: Int = 100
)

@Entity(tableName = "notices")
data class Notice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timeAgo: String,
    val dateStr: String,
    val timeStr: String,
    val tag: String,
    val colorType: String, // "primary", "secondary", "tertiary", "error"
    val isUrgent: Boolean,
    val iconName: String
)

@Entity(tableName = "enquiries")
data class Enquiry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val topic: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
