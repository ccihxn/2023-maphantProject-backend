package com.tovelop.maphant.service

import com.tovelop.maphant.dto.UploadLogDTO
import com.tovelop.maphant.mapper.ProfileMapper
import com.tovelop.maphant.mapper.UploadLogMapper
import com.tovelop.maphant.utils.UploadUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.ArrayList

@Service
class UploadLogService(private val uploadUtils: UploadUtils, private val uploadLogMapper: UploadLogMapper) {
    @Transactional
    fun storeUrls(userId:Int, imageUrls: List<String>, files: List<MultipartFile>): ArrayList<UploadLogDTO> {
        val dtoList = ArrayList<UploadLogDTO>()

        for ((index, file) in files.withIndex()) {

            if(uploadUtils.isNotImageFile(file.originalFilename as String))
                throw IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");

            val storeUrlDto = UploadLogDTO(
                user_id = userId,
                file_size = file.size.toInt(),
                upload_date = LocalDateTime.now(),
                url = imageUrls[index]
            )

            /* Mapper 이용해 db 저장 하기 */
            uploadLogMapper.insertUploadLog(
                storeUrlDto.user_id,
                storeUrlDto.file_size,
                storeUrlDto.upload_date,
                storeUrlDto.url);

            dtoList.add(storeUrlDto)
        }

        return dtoList
    }

    @Transactional
    fun storeUrl(userId: Int, imageUrl: String, file: MultipartFile): UploadLogDTO {
        if (uploadUtils.isNotImageFile(file.originalFilename as String))
            throw IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");
        val storeUrlDto = UploadLogDTO(
            user_id = userId,
            file_size = file.size.toInt(),
            upload_date = LocalDateTime.now(),
            url = imageUrl,
        )
        /* TODO Mapper 이용해 db 저장 하기 */
        uploadLogMapper.insertUploadLog(
            storeUrlDto.user_id,
            storeUrlDto.file_size,
            storeUrlDto.upload_date,
            storeUrlDto.url);

        return storeUrlDto
    }

}