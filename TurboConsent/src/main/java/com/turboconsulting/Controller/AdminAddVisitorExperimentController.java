package com.turboconsulting.Controller;

import com.turboconsulting.Entity.Account;
import com.turboconsulting.Entity.Visitor;
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

import java.util.GregorianCalendar;

@Controller
public class AdminAddVisitorExperimentController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userService;

    @GetMapping("/admin/vexp")
    public String adminVisitorExperimentsPage(ModelMap m,
                                              @RequestParam(name="updateSuccess",  required = false) boolean update) {

        m.addAttribute("visitorExperiments", adminService.getAllVisitorExperiments());
        m.addAttribute("updateSuccess", update);
        int aID = getLoggedInAccountID();
        return "admin-vexp";
    }

    @PostMapping("/admin/vexp/add")
    public ModelAndView addAccount(@ModelAttribute("visitorId") int visitorId,
                                   @ModelAttribute("experimentId") int experimentId)  {
        adminService.addVisitorExperiment(visitorId, experimentId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/admin/vexp?updateSuccess=true");
        return mav;
    }

    @PostMapping("/admin/vexp/delete")
    public ModelAndView deleteVisitorExperiment(@ModelAttribute("deleteId") int visitorExperimentId)  {
        adminService.deleteVisitorExperiment(visitorExperimentId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/admin/vexp");
        return mav;
    }


    private int getLoggedInAccountID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUser userDetails = (MyUser)userService.loadUserByUsername(auth.getName());
        return userDetails.getUser().getAccountId();
    }

}
