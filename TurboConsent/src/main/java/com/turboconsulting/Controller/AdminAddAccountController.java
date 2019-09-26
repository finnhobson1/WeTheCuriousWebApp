package com.turboconsulting.Controller;

import com.turboconsulting.Entity.Account;
import com.turboconsulting.Security.MyUser;
import com.turboconsulting.Security.MyUserDetailsService;
import com.turboconsulting.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminAddAccountController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userService;

    @GetMapping("/admin/accounts")
    public String adminAccountsPage(ModelMap m, @RequestParam(name = "updateSuccess", required = false) boolean update ) {
        m.addAttribute("accounts", adminService.getAllAccounts());
        int aID = getLoggedInAccountID();
        m.addAttribute("updateSuccess", update);
        return "admin-accounts";
    }

    @PostMapping("/admin/accounts/add")
    public ModelAndView addAccount(ModelMap m,
                                   @ModelAttribute("name") String name,
                                   @ModelAttribute("email") String email,
                                   @ModelAttribute("password") String pword)  {
        adminService.addNewAccount(new Account(name, email, bCryptPasswordEncoder.encode(pword)));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/admin/accounts?updateSuccess=true");
        return mav;
    }

    @PostMapping("/admin/accounts/delete")
    public ModelAndView deleteAccount(@ModelAttribute("deleteId") int accountId)  {
        if (accountId != getLoggedInAccountID()) adminService.deleteAccount(accountId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/admin/accounts");
        return mav;
    }


    private int getLoggedInAccountID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUser userDetails = (MyUser)userService.loadUserByUsername(auth.getName());
        return userDetails.getUser().getAccountId();
    }
}
