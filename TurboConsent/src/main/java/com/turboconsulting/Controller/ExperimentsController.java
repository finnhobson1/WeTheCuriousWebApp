package com.turboconsulting.Controller;

import com.turboconsulting.Entity.ConsentOption;
import com.turboconsulting.Entity.Experiment;
import com.turboconsulting.Entity.VisitorExperiment;
import com.turboconsulting.Security.MyUser;
import com.turboconsulting.Security.MyUserDetailsService;
import com.turboconsulting.Service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExperimentsController {

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private ConsentService consentService;

    @GetMapping("/visitors/experiments")
    public String experimentsPage(ModelMap m,
                                  @RequestParam(value="vID") int vID,
                                  @RequestParam(value="update", required = false) boolean updateSuccess)
                                    throws AccessDeniedException {
        checkAccountID(vID);
        m.addAttribute("visitorExps", consentService.getVisitorExperiments(vID));
        m.addAttribute("visitorName", consentService.getVisitor(vID).getName());
        m.addAttribute("vID", vID);
        m.addAttribute("defaultConsent", consentService.getVisitor(vID).getDefaultConsent());
        m.addAttribute("updateSuccess", updateSuccess);

        return "experiments";
    }

    @PostMapping("/visitors/experiments/updateConsent")
    public String updateConsent(ModelMap m, @RequestParam("vID") int vID,
                                      @RequestParam("selected") List<Integer> eIDs,
                                      @ModelAttribute("consentLevel") String c) throws AccessDeniedException {
        checkAccountID(vID);
        boolean updateSuccessful = consentService.updateBatchExperimentConsents(vID, new ConsentOption(c.toUpperCase(), "Description"), eIDs);
        return "redirect:/visitors/experiments?vID="+vID+"&update="+updateSuccessful;
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

    @PostMapping("visitors/experiments/toReviewed")
    public ModelAndView reviewConsents(@RequestParam("vID") int vID) throws AccessDeniedException {
        ModelAndView mav = new ModelAndView();
        int aID = getLoggedInAccountID();
        checkAccountID(vID);
        boolean updateSuccessful = consentService.moveAllToReviewed(vID);
        mav.setViewName("redirect:/visitors/experiments?vID="+vID+"&update="+updateSuccessful);
        return mav;
    }
}
