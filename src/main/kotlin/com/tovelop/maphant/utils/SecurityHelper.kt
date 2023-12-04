package com.tovelop.maphant.utils

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import org.springframework.security.core.Authentication

class SecurityHelper {
    companion object {
        fun Authentication.isLogged(): Boolean =
            this != null && this is TokenAuthToken && this.isAuthenticated

        fun Authentication.isNotLogged(): Boolean =
            this == null || this !is TokenAuthToken || !this.isAuthenticated
    }
}