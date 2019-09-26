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

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

@Controller
public class AdminAddVisitorController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userService;

    @GetMapping("/admin/visitors")
    public String adminVisitorsPage(ModelMap m,
                                    @RequestParam(name="updateSuccess", required = false) boolean update) {
        m.addAttribute("visitors", adminService.getAllVisitors());
        m.addAttribute("updateSuccess", update);
        int aID = getLoggedInAccountID();
        return "admin-visitors";
    }

    @PostMapping("/admin/visitors/add")
    public ModelAndView addAccount(ModelMap m,
                                   @ModelAttribute("accountId") int accountId,
                                   @ModelAttribute("name") String name,
                                   @ModelAttribute("dob") String dateVal)  {
        GregorianCalendar date = new GregorianCalendar();
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(dateVal);
            date. setTime(d);
        } catch (ParseException ex) {
            // do nothing
        }

        adminService.addNewVisitor(new Visitor(name, date), accountId );
        ModelAndView mav = new ModelAndView();

        mav.setViewName("redirect:/admin/visitors?updateSuccess=true");
        return mav;
    }

    @PostMapping("/admin/visitors/delete")
    public ModelAndView deleteVisitor(@ModelAttribute("deleteId") int visitorId)  {
        adminService.deleteVisitor(visitorId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/admin/visitors");
        return mav;
    }

    private int getLoggedInAccountID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUser userDetails = (MyUser)userService.loadUserByUsername(auth.getName());
        return userDetails.getUser().getAccountId();
    }
}
