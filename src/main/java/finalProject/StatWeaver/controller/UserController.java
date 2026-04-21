package finalProject.StatWeaver.controller;

import finalProject.StatWeaver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/sign-up")
    @PreAuthorize("isAnonymous()")
    public String signUpPage(Model model){
        return "sign-up";
    }

    @PostMapping("/registration")
    @PreAuthorize("isAnonymous()")
    public String registration(@RequestParam(name = "user_email") String email,
                               @RequestParam(name = "user_password") String password,
                               @RequestParam(name = "user_repeat_password") String repeatPassword,
                               @RequestParam(name = "user_full_name") String fullName){

        Boolean result = userService.signUp(email, password, repeatPassword, fullName);

        if(result != null){
            if(result){
                return "redirect:/sign-up?success";
            }

            return "redirect:/sign-up?passwordError";
        }

        return "redirect:/sign-up?emailError";
    }

    @GetMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public String changePasswordPage(Model model){
        return "change-password";
    }

    @PostMapping("/save-password")
    @PreAuthorize("isAuthenticated()")
    public String savePassword(@RequestParam(name = "user_old_password") String oldPassword,
                               @RequestParam(name = "user_new_password") String newPassword,
                               @RequestParam(name = "user_repeat_new_password") String repeatNewPassword){

        Boolean result = userService.updatePassword(oldPassword, newPassword, repeatNewPassword);

        if(result != null){
            if(result){
                return "redirect:/change-password?success";
            }

            return "redirect:/change-password?newPasswordError";
        }

        return "redirect:/change-password?oldPasswordError";
    }
}
