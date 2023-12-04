package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.FcmMessageDTO
import com.tovelop.maphant.dto.FcmTokenDTO
import com.tovelop.maphant.service.FcmService
import com.tovelop.maphant.type.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fcm")
class FcmMessageController(private val fcmService: FcmService) {

    @PostMapping("/send")
    fun send(@RequestBody fcmMessageDTO: FcmMessageDTO): ResponseEntity<Any> {
        return try {
            fcmService.send(fcmMessageDTO)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/token")
    fun addUserFcmToken(@RequestBody req: FcmTokenDTO): ResponseEntity<Any> {
        val token = req.token
        val auth = SecurityContextHolder.getContext().authentication as TokenAuthToken

        if(token == null || token.trim() == "") {
            return ResponseEntity.badRequest().body(Response.error<Any>("no token field"))
        } else {
            return try {
                fcmService.saveFcmToken(auth.getUserData().id, token)
                ResponseEntity.ok().build()
            } catch (e: Exception) {
                e.printStackTrace()
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        }
    }
}
