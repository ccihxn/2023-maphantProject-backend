package com.tovelop.maphant.utils

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse

class ResponseJsonWriter {
    companion object {
        private val objMapper = ObjectMapper()

        fun HttpServletResponse.writeJSON(obj: Any) {
            this.contentType = "application/json"
            this.characterEncoding = "UTF-8"
            this.writer.write(objMapper.writeValueAsString(obj))
        }

        fun writeJSON(obj: Any): String {
            return objMapper.writeValueAsString(obj)
        }
    }
}