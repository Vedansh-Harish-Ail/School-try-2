package com.example

import android.app.Application
import com.example.data.StudentPortalDatabase
import com.example.data.StudentPortalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class StudentPortalApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { StudentPortalDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { StudentPortalRepository(database.studentPortalDao()) }
}
