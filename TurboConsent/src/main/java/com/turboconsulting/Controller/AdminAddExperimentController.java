package com.turboconsulting.Controller;

import com.turboconsulting.Entity.Account;
import com.turboconsulting.Entity.ConsentOption;
import com.turboconsulting.Entity.Experiment;
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

import java.util.HashSet;
import java.util.List;

@Controller
public class AdminAddExperimentController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userService;

    @GetMapping("/admin/experiments")
    public String adminExperimentsPage(ModelMap m, @RequestParam(name="updateSuccess", required = false) boolean update) {
        m.addAttribute("experiments", adminService.getAllExperiments());
        m.addAttribute("updateSuccess", update);

        int aID = getLoggedInAccountID();
        return "admin-experiments";
    }

    @PostMapping("/admin/experiments/add")
    public String addAccount(ModelMap m,
                                   @ModelAttribute("name") String name,
                                   @ModelAttribute("description") String description,
                                   @RequestParam("consentNames") List<String> consentNames,
                                   @RequestParam("consentDescriptions") List<String> consentDescriptions)  {
        HashSet<ConsentOption> consentOptions = new HashSet<>();
        for(int i = 0; i < consentNames.size(); i++)  consentOptions.add(new ConsentOption(consentNames.get(i),
                                                                                           consentDescriptions.get(i)));
        adminService.addNewExperiment(new Experiment(name, description),  consentOptions);
        return "redirect:/admin/experiments?updateSuccess=true";
    }

    @PostMapping("/admin/experiments/delete")
    public ModelAndView deleteExperiment(@ModelAttribute("deleteId") int experimentsId)  {
        adminService.deleteExperiment(experimentsId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/admin/experiments");
        return mav;
    }

    private int getLoggedInAccountID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUser userDetails = (MyUser)userService.loadUserByUsername(auth.getName());
        return userDetails.getUser().getAccountId();
    }
}
