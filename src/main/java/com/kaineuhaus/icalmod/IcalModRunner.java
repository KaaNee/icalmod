package com.kaineuhaus.icalmod;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;

@Component
public class IcalModRunner implements CommandLineRunner {

    private static final IcalLoader loader = IcalLoader.getInstance();
    public static final TemporalAmount DURATION_DAY = Duration.ofDays(1);

    @Override
    public void run(final String... args) throws Exception {

        if (args.length != 2) {
            throw new IllegalArgumentException();
        }

        loader.setFilename2load(args[0]);
        loader.setFilename2write(args[1]);

        Calendar cal = loader.loadCalender();

        ComponentList newComps = new ComponentList();

        cal.getComponents().stream().filter(calendarComponent -> calendarComponent instanceof VEvent).forEach(calendarComponent -> newComps.add(modifyEvent(calendarComponent)));

        Calendar newCal = new Calendar(newComps);
        newCal.getProperties().add(new ProdId(cal.getProductId().getValue()));
        newCal.getProperties().add(CalScale.GREGORIAN);
        newCal.getProperties().add(cal.getVersion());

        loader.saveCalendar(newCal);

    }

    private CalendarComponent modifyEvent(CalendarComponent sourceComponent) {

        if (sourceComponent instanceof VEvent == false) {
            return null;
        }

        VEvent event = (VEvent) sourceComponent;

        // by default the TimeZone is NULL
        DtStart currentStart = event.getStartDate();
        DateTime dt = (DateTime) currentStart.getDate();

        //format to ZonedDateTime with current TimeZone
        final ZonedDateTime zonedDateTime = dt.toInstant().atZone(TimeZone.getDefault().toZoneId());

        // create "old" Date
        final java.util.Date startDateLocal = Date.from(zonedDateTime.toInstant());

        //change the time to midnight
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(startDateLocal);
        cal.set(java.util.Calendar.HOUR, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);

        // important: need to set "net.fortuna.ical4j.timezone.date.floating=true" in ical4j.properties - else UTC TimeZone will be used to create new instance of Date (ical-Date-Type)
        Date newStartDate = new Date(cal.getTime());

        VEvent modEvent = new VEvent(newStartDate, DURATION_DAY, event.getSummary().getValue());

        UidGenerator ug = new RandomUidGenerator();
        modEvent.getProperties().add(ug.generateUid());

        modEvent.validate();
        return modEvent;
    }
}
