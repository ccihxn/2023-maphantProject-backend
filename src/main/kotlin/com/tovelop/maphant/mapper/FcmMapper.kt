package com.tovelop.maphant.mapper

import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface FcmMapper {
    fun saveFcmToken(userId: Int, token: String)

    fun selectTokenById(userId: Int): List<String>
}