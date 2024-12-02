package com.denproj.posmanongjaks.session

import com.denproj.posmanongjaks.model.Branch
import com.denproj.posmanongjaks.model.Role
import com.denproj.posmanongjaks.model.User

data class Session(@JvmField var branch: Branch? = null, @JvmField var user: User? = null, var role: Role? = null, var isConnectionReachable: Boolean = false) {

}
