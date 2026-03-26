package com.mcommings.campaigner.setup.calendar.builders;

import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;

import java.util.UUID;

public class WeekBuilder {

    private int id = CalendarTestConstants.WEEK_ID;
    private String description = CalendarTestConstants.WEEK_DESCRIPTION;
    private UUID campaignUuid = CalendarTestConstants.CAMPAIGN_UUID;
    private int weekNumber = CalendarTestConstants.WEEK_NUMBER;
    private int monthId = CalendarTestConstants.MONTH_ID;

    public static WeekBuilder aWeek() {
        return new WeekBuilder();
    }

    public WeekBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public WeekBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WeekBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public WeekBuilder withWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public WeekBuilder withMonthId(int monthId) {
        this.monthId = monthId;
        return this;
    }

    public Week build() {
        Week week = new Week();
        week.setId(id);
        week.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        week.setCampaign(campaign);

        Month month = new Month();
        month.setId(monthId);
        week.setMonth(month);

        week.setWeekNumber(weekNumber);

        return week;
    }
}
