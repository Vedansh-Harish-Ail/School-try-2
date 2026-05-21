package com.example.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StudentPortalViewModel(
    application: Application,
    private val repository: StudentPortalRepository
) : AndroidViewModel(application) {

    // --- State: Active Student ---
    private val _activeStudentId = MutableStateFlow("st002") // Default to Aarav Sharma
    val activeStudentId: StateFlow<String> = _activeStudentId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeStudent: StateFlow<Student?> = _activeStudentId
        .flatMapLatest { id -> repository.getStudentById(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeStudentAbsences: StateFlow<List<AbsenceLog>> = _activeStudentId
        .flatMapLatest { id -> repository.getAbsenceLogs(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeStudentMarks: StateFlow<List<MarkRecord>> = _activeStudentId
        .flatMapLatest { id -> repository.getMarkRecords(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- State: General Information ---
    val allStudents: StateFlow<List<Student>> = repository.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notices: StateFlow<List<Notice>> = repository.getAllNotices()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val enquiries: StateFlow<List<Enquiry>> = repository.getAllEnquiries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Interaction States ---
    val loginUsername = MutableStateFlow("parent_smith")
    val loginPassword = MutableStateFlow("password123")
    val rememberMe = MutableStateFlow(true)
    val loginError = MutableStateFlow<String?>(null)
    val isLoggedIn = MutableStateFlow(false)

    // Password reset state
    val resetEmail = MutableStateFlow("")
    val resetStatusMessage = MutableStateFlow<String?>(null)

    // Report generation states
    val isReportDownloading = MutableStateFlow(false)
    val isReportReady = MutableStateFlow(false)
    val activeReportName = MutableStateFlow("Aarav_Sharma_Term2_Report.pdf")
    val showShareSheet = MutableStateFlow(false)

    // School support request form states
    val enquiryName = MutableStateFlow("")
    val enquiryTopic = MutableStateFlow("General Enquiry")
    val enquiryMessage = MutableStateFlow("")
    val enquirySentSuccess = MutableStateFlow(false)

    fun setActiveStudent(studentId: String) {
        _activeStudentId.value = studentId
        activeReportName.value = if (studentId == "st001") "Akshayan_Term2_Report.pdf" else "Aarav_Sharma_Term2_Report.pdf"
    }

    fun triggerLogin() {
        if (loginUsername.value.trim() == "parent_smith" && loginPassword.value.trim().isNotEmpty()) {
            loginError.value = null
            isLoggedIn.value = true
        } else {
            loginError.value = "Invalid username or password"
        }
    }

    fun logout() {
        isLoggedIn.value = false
        loginPassword.value = ""
        loginError.value = null
    }

    fun triggerPasswordReset() {
        if (resetEmail.value.trim().isNotEmpty()) {
            resetStatusMessage.value = "Send Reset Link: A reset email has been sent to ${resetEmail.value} successfully!"
        } else {
            resetStatusMessage.value = "Please enter your registered email or username."
        }
    }

    fun startReportDownload() {
        viewModelScope.launch {
            isReportDownloading.value = true
            isReportReady.value = false
            kotlinx.coroutines.delay(1800) // Simulate downloading animation
            isReportDownloading.value = false
            isReportReady.value = true
        }
    }

    fun closeReportReadyDialog() {
        isReportReady.value = false
    }

    fun submitQuickEnquiry() {
        if (enquiryName.value.trim().isEmpty() || enquiryMessage.value.trim().isEmpty()) {
            return
        }
        viewModelScope.launch {
            repository.insertEnquiry(
                Enquiry(
                    fullName = enquiryName.value,
                    topic = enquiryTopic.value,
                    message = enquiryMessage.value
                )
            )
            enquirySentSuccess.value = true
            enquiryName.value = ""
            enquiryMessage.value = ""
            kotlinx.coroutines.delay(3000)
            enquirySentSuccess.value = false
        }
    }
}

class StudentPortalViewModelFactory(
    private val application: Application,
    private val repository: StudentPortalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentPortalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentPortalViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
