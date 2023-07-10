package com.tovelop.maphant.controller

import com.tovelop.maphant.configure.security.PasswordEncoderBcrypt
import com.tovelop.maphant.dto.*
import com.tovelop.maphant.service.UserService
import com.tovelop.maphant.type.response.Response
import com.tovelop.maphant.type.response.ResponseUnit
import com.tovelop.maphant.utils.ValidationHelper
import com.tovelop.maphant.utils.isSuccess
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class SignupController(@Autowired val userService: UserService) {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoderBcrypt

    //이메일 검증 api
    @PostMapping("/validation/email")
    fun validationEmail(@RequestBody validationSignupDTO: ValidationSignupDTO): ResponseEntity<ResponseUnit> {
        if (!userService.isEmailValid(validationSignupDTO.email!!)) {
            return ResponseEntity.badRequest().body(Response.error("형식에 맞지 않는 이메일입니다."))
        }

        if (userService.isDuplicateEmail(validationSignupDTO.email)) {
            return ResponseEntity.badRequest().body(Response.error("형식에 맞지 않는 이메일입니다."))
        }

        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/validation/nickname")
    fun validationNickname(@RequestBody validationSignupDTO: ValidationSignupDTO): ResponseEntity<ResponseUnit> {
        if (!ValidationHelper.isValidNickname(validationSignupDTO.nickName!!)) {
            return ResponseEntity.badRequest().body(Response.error("형식에 맞지 않는 별명입니다."))
        }

        if (userService.isDuplicateNickname(validationSignupDTO.nickName)) {
            return ResponseEntity.badRequest().body(Response.error("이미 사용중인 별명입니다."))
        }

        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/validation/phoneNum")
    fun validationPhonenum(@RequestBody validationSignupDTO: ValidationSignupDTO): ResponseEntity<ResponseUnit> {
        if (!ValidationHelper.isValidPhoneNum(validationSignupDTO.phoneNum!!)) {
            return ResponseEntity.badRequest().body(Response.error("핸드폰 번호를 형식에 맞춰주세요. ex) 010-1234-5678"))
        }
        if (userService.isDuplicatePhoneNum(validationSignupDTO.phoneNum)) {
            return ResponseEntity.badRequest().body(Response.error("이미 사용중인 핸드폰 번호입니다."))
        }

        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/validation/password")
    fun validationPassword(@RequestBody validationSignupDTO: ValidationSignupDTO): ResponseEntity<ResponseUnit> {
        if (!ValidationHelper.isValidPassword(validationSignupDTO.password!!)) {
            return ResponseEntity.badRequest()
                .body(Response.error("비밀번호는 영문 소문자, 대문자, 숫자와 특수문자를 포함하고, 최소 8자로 구성되어야 합니다."))
        }

        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/validation/passwordCheck")
    fun validationPasswordChk(@RequestBody validationSignupDTO: ValidationSignupDTO): ResponseEntity<ResponseUnit> {
        if (validationSignupDTO.password != validationSignupDTO.passwordChk) {
            return ResponseEntity.badRequest().body(Response.error("비밀번호와 비밀번호 확인이 동일하지 않습니다."))
        }

        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/signup")
    fun signup(@RequestBody signup: SignupDTO): ResponseEntity<ResponseUnit> {
        val emailValidation = validationEmail(ValidationSignupDTO(email = signup.email))
        if (!emailValidation.isSuccess()) {
            return emailValidation
        }

        val nicknameValidation = validationNickname(ValidationSignupDTO(nickName = signup.nickname))
        if (!nicknameValidation.isSuccess()) {
            return nicknameValidation
        }

        val phoneNumValidation = validationPhonenum(ValidationSignupDTO(phoneNum = signup.phoneNo))
        if (!phoneNumValidation.isSuccess()) {
            return phoneNumValidation
        }

        val passwordValidation = validationPassword(ValidationSignupDTO(password = signup.password))
        if (!passwordValidation.isSuccess()) {
            return passwordValidation
        }

        val passwordChkValidation =
            validationPasswordChk(ValidationSignupDTO(password = signup.password, passwordChk = signup.passwordChk))
        if (!passwordChkValidation.isSuccess()) {
            return passwordChkValidation
        }

        if (!ValidationHelper.isValidName(signup.name)) {
            return ResponseEntity.badRequest().body(Response.error("이름을 형식에 맞춰주세요. ex) 홍길동"))
        }

        // 이메일 인증 코드 발송 (feat-sendgrid)

        userService.signUp(signup.toUserDTO(passwordEncoder))
        return ResponseEntity.ok(Response.stateOnly(true))
    }

    @PostMapping("/login")
    fun login(@RequestBody login: LoginDTO): ResponseEntity<ResponseUnit> {
        //ID, PW DB 체크
        if (true /*입력받은 이메일, 비밀번호 DB에 있는 정보와 동일한 지 확인 */) {
            return ResponseEntity.badRequest().body(Response.error("이메일, 비밀번호를 확인해주세요."))
        }

        return ResponseEntity.ok(Response.stateOnly(true))
    }


    @PostMapping("/findemail")
    fun findEmail(@RequestBody findEmail: FindEmailDTO): ResponseEntity<Response<Map<String, String>>> {
        //학번, 전화번호 DB 체크
        val email = "a@ks.ac.kr"

        return ResponseEntity.ok(Response.success(mutableMapOf("email" to email)))
    }

    @PostMapping("/changepw")
    fun ChangePw(@RequestBody changePw: ChangePwDTO): ResponseEntity<ResponseUnit> {
        //이메일 DB 체크
        if (true /*이메일이 일치하지 않을 때*/) {
            return ResponseEntity.badRequest().body(Response.error("유효하지 않은 이메일입니다."))
        }

        // You might have to handle signService.sendEmail() based on its implementation and return type.

        return ResponseEntity.ok(Response.stateOnly(true))
        // 여기서 내 이메일을 session을 넘겨줄지, 아니면 data에 email을 넘겨줄지 결정 해야함.
    }

    @PostMapping("/changepw/authenticationcode")
    fun authenticationCode(@RequestBody newPw: NewPwDTO) {
        //인증번호 확인
        // You might have to handle this method based on its implementation and return type.
    }

    @PostMapping("/newpw")
    fun newPw(@RequestBody newPw: NewPwDTO): ResponseEntity<ResponseUnit> {
        //패스워드 입력, 검증
        // You might have to handle userService related function based on its implementation and return type.

        //DB 패스워드 치환
        return ResponseEntity.ok(Response.stateOnly(true))
    }
}