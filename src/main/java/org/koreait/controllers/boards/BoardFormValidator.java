package org.koreait.controllers.boards;

import lombok.RequiredArgsConstructor;
import org.koreait.commons.MemberUtil;
import org.koreait.commons.validators.PasswordValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class BoardFormValidator implements Validator, PasswordValidator {

    private final MemberUtil memberUtil;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(BoardForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BoardForm form = (BoardForm)target;
        if (!memberUtil.isLogin()) { // 미로그인 상태 -> 비회원 비밀번호 필수
            String guestPw = form.getGuestPw();

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "guestPw", "NotBlank");

            if (StringUtils.hasText(guestPw)) {
                // 비회원 비밀번호는 1자리 이상 알파벳, 숫자 필수
                if (!alphaCheck(guestPw, true) || !numberCheck(guestPw)) {
                    errors.rejectValue("guestPw", "Complexity");
                }

                // 비회원 비밀번호는 4자리 이상
                if (guestPw.length() < 4) {
                    errors.rejectValue("guestPw", "Size");
                }
            }
        }
    }
}
