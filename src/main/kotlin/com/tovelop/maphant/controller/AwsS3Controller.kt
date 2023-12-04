package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.MockupCustomUserToken
import com.tovelop.maphant.configure.security.token.TokenAuthToken
import com.tovelop.maphant.dto.UploadLogDTO
import com.tovelop.maphant.service.AwsS3Service
import com.tovelop.maphant.service.UploadLogService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/image")
class AwsS3Controller(
    private val awsS3Service: AwsS3Service,
    private val uploadLogService: UploadLogService
) {

    @PostMapping
    fun uploadFile(@RequestParam files: List<MultipartFile>): List<UploadLogDTO> {

        if (files.size > 5) {
            // 에러 던지기
            throw MaxUploadSizeExceededException(5)
        }
        val auth = SecurityContextHolder.getContext().authentication!! as TokenAuthToken
        val userId: Int = auth.getUserId()!!

        // 서비스에서 여러 파일 aws에 올려서 url 받기
        val imageUrls: List<String> = awsS3Service.uploadFiles(files)
        //db저장 후 dto 반환
        return uploadLogService.storeUrls(userId, imageUrls, files)
    }


}