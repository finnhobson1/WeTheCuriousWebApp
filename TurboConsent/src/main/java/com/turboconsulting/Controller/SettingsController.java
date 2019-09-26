package com.turboconsulting.Controller;

import com.turboconsulting.Entity.ConsentOption;
import com.turboconsulting.Entity.ConsentOption;
import com.turboconsulting.Security.MyUser;
import com.turboconsulting.Security.MyUserDetailsService;
import com.turboconsulting.Service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class SettingsController {

    @Autowired
    private ConsentService consentService;

    @Autowired
    private MyUserDetailsService userService;

    @GetMapping("/settings")
    public String homePage(ModelMap m,
                           @RequestParam(value = "update", required = false) boolean updateSuccess) {
        int aID = getLoggedInAccountID();
        //m.addAttribute("consentOptions", ConsentOption.values());
        m.addAttribute("visitors", consentService.getAccountsVisitors(aID));
        m.addAttribute("updateSuccess", updateSuccess);
        return "settings";
    }

    @PostMapping("/settings/updateConsent")
    public String updateConsent(ModelMap m,
                                @RequestParam("vSelected") List<Integer> vIDs,
                                @ModelAttribute("dConsentLevel") String c)  {
        boolean updateSuccessful = consentService.updateAccountConsent(vIDs, new ConsentOption(c.toUpperCase(), "Description"));

        return "redirect:/settings?update="+updateSuccessful;
    }

    private int getLoggedInAccountID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUser userDetails = (MyUser)userService.loadUserByUsername(auth.getName());
        return userDetails.getUser().getAccountId();
    }
}
