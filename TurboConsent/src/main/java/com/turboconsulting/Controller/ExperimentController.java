package com.turboconsulting.Controller;

import com.turboconsulting.Entity.ConsentOption;
import com.turboconsulting.Entity.Experiment;
import com.turboconsulting.Security.MyUser;
import com.turboconsulting.Security.MyUserDetailsService;
import com.turboconsulting.Service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;


@Controller

public class ExperimentController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private ConsentService consentService;

    @GetMapping("/visitors/experiments/experiment")
    public String experimentPage(Model m,
                                 @RequestParam("vID") int vID,
                                 @RequestParam("eID") int eID) throws AccessDeniedException {
        checkAccountID(vID);
        m.addAttribute("visitorExp", consentService.getVisitorExperiment(vID, eID));
        m.addAttribute("visitorName", consentService.getVisitor(vID).getName());
        m.addAttribute("customConsentOptions", consentService.getExperimentsConsentOptions(eID));
        m.addAttribute("vID", vID);
        m.addAttribute("eID", eID);
        return "experiment";
    }

    @PostMapping("/visitor/experiments/experiment/updateConsent")
    public ModelAndView updateConsent(@RequestParam("vID") int vID,
                                      @RequestParam("eID") int eID,
                                      @ModelAttribute("consentLevel") String c) throws AccessDeniedException {
        checkAccountID(vID);
        boolean updateSuccessful = consentService.updateExperimentConsent(vID, new ConsentOption(c.toUpperCase(), "Description"), eID);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/visitors/experiments?vID="+vID+"&update="+updateSuccessful);
        return mav;
    }

    private int getLoggedInAccountID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUser userDetails = (MyUser)userService.loadUserByUsername(auth.getName());
        return userDetails.getUser().getAccountId();
    }

    public void checkAccountID(int vID) throws AccessDeniedException {
        if (getLoggedInAccountID() != consentService.getVisitor(vID).getAccount().getAccountId()) {
            throw new AccessDeniedException("Your account cannot give consent for this visitor.");
        }
    }


}
