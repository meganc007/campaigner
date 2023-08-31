package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.models.calendar.Month;
import com.mcommings.campaigner.services.calendar.MonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/calendar/months")
public class MonthController {

    private final MonthService monthService;

    @Autowired
    public MonthController(MonthService monthService) {
        this.monthService = monthService;
    }

    @GetMapping
    public List<Month> getMonths() {
        return monthService.getMonths();
    }

    @PostMapping
    public void saveMonth(@RequestBody Month month) {
        monthService.saveMonth(month);
    }

    @DeleteMapping(path = "{monthId}")
    public void deleteMonth(@PathVariable("monthId") int monthId) {
        monthService.deleteMonth(monthId);
    }

    @PutMapping(path = "{monthId}")
    public void updateMonth(@PathVariable("monthId") int monthId, @RequestBody Month month) {
        monthService.updateMonth(monthId, month);
    }
}
