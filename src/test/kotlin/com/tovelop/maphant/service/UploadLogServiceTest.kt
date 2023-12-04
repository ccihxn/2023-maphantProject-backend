package com.tovelop.maphant.service

import com.tovelop.maphant.dto.UploadLogDTO
import com.tovelop.maphant.mapper.UploadLogMapper
import com.tovelop.maphant.utils.UploadUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockMultipartFile
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class UploadLogServiceTest {
    @Mock
    private lateinit var uploadUtils: UploadUtils

    @Mock
    private lateinit var uploadLogMapper: UploadLogMapper

    @InjectMocks
    private lateinit var uploadLogService: UploadLogService

    @Test
    @DisplayName("urls 확장자가 png, jpeg, jpg에 해당하지 않는 url이면 예외를 던진다.")
    fun storeUrlsNotImageThrowException() {
        //given
        val userId = 1;
        val imageUrls = listOf("test1.jpeg","test2.txt")
        val files = listOf(
            MockMultipartFile(
                "test.jpeg", "test.jpeg", "byte",
                "test".toByteArray()
            ),
            MockMultipartFile(
                "file", // 파일 이름
                "test.txt", // 원본 파일 이름
                "text/plain", // 컨텐츠 타입
                "test".toByteArray(StandardCharsets.UTF_8) // 파일의 내용
            )
        )
        whenever(uploadUtils.isNotImageFile(any())).thenReturn(false).thenReturn(true)

        //when
        val exception = assertThrows<IllegalArgumentException> {
            uploadLogService.storeUrls(userId, imageUrls, files)
        }

        //then
        assertEquals("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.", exception.message)
        verify(uploadUtils, times(files.size)).isNotImageFile(any())
    }

    @Test
    @DisplayName("urls 확장자가 png, jpeg, jpg에 해당한다면 uploadLog 테이블에 튜플을 저장한다.")
    fun storeUrlsWithImageInsertData() {
        //given
        val userId = 1;
        val imageUrls = listOf("test1.jpeg","test2.txt")
        val files = listOf(
            MockMultipartFile(
                "test.jpeg", "test.jpeg", "byte",
                "test".toByteArray()
            ),
            MockMultipartFile(
                "test.png", "test.png", "png",
                "test".toByteArray()
            ),
        )
        val expectResult = ArrayList<UploadLogDTO>()
        for ((index, file) in files.withIndex()) {
            expectResult.add(
                UploadLogDTO(
                    user_id = userId,
                    file_size = file.size.toInt(),
                    upload_date = LocalDateTime.now(),
                    url = imageUrls[index]
                )
            )
        }
        whenever(uploadUtils.isNotImageFile(any())).thenReturn(false).thenReturn(false)

        //when
        val result = uploadLogService.storeUrls(userId,imageUrls, files)

        //then
        assert(result.size == files.size)
        for((index, dto) in expectResult.withIndex()) {
            assert(result.get(index).user_id == dto.user_id)
            assert(result.get(index).file_size == dto.file_size)
            assert(result.get(index).url == dto.url)
        }
        verify(uploadLogMapper, times(files.size)).insertUploadLog(any(),any(),any(),any())
    }

    @Test
    @DisplayName("url 확장자가 png, jpeg, jpg에 해당하지 않는 url이면 예외를 던진다.")
    fun storeUrlNotImageThrowException() {
        //given
        val userId = 1;
        val imageUrl = "test1.txt"
        val file = MockMultipartFile(
            "file", // 파일 이름
            "test.txt", // 원본 파일 이름
            "text/plain", // 컨텐츠 타입
            "test".toByteArray(StandardCharsets.UTF_8) // 파일의 내용
        )
        whenever(uploadUtils.isNotImageFile(any())).thenReturn(true)

        //when
        val exception = assertThrows<IllegalArgumentException> {
            uploadLogService.storeUrl(userId, imageUrl, file)
        }

        //then
        assertEquals("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.", exception.message)
        verify(uploadUtils, times(1)).isNotImageFile(any())
    }

    @Test
    @DisplayName("url 확장자가 png, jpeg, jpg에 해당한다면 uploadLog 테이블에 튜플을 저장한다.")
    fun storeUrlWithImageInsertData() {
        //given
        val userId = 1;
        val imageUrl = "test1.jpeg"
        val file = MockMultipartFile(
            "test.jpeg", "test.jpeg", "byte",
            "test".toByteArray()
        )
        whenever(uploadUtils.isNotImageFile(any())).thenReturn(false)

        //when
        val result = uploadLogService.storeUrl(userId,imageUrl, file)

        //then
        assert(result.user_id == userId)
        assert(result.file_size == file.size.toInt())
        assert(result.url == imageUrl)
        verify(uploadLogMapper, times(1)).insertUploadLog(any(),any(),any(),any())
    }

}