package training.busboard.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import training.busboard.Main;

@Controller
@EnableAutoConfiguration
public class Website {

    @RequestMapping("/")
    ModelAndView home() {
        return new ModelAndView("index");
    }

    @RequestMapping("/busInfo")
    ModelAndView busInfo(@RequestParam("postcode") String postcode) {
        System.out.println(postcode);
        if (Main.ValidatePostcode(postcode)) {
            return new ModelAndView("info", "busInfo", new BusInfo(postcode));
        } else {
//            return new ModelAndView("postcodeError");
            return new ModelAndView("postcodeError", "busInfo", new BusInfo("E111BB"));
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Website.class, args);
    }

}