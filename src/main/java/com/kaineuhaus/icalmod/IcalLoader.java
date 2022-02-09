package com.kaineuhaus.icalmod;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IcalLoader {

    private String filename2load = null;
    private String filename2write = null;
    private static IcalLoader instance;

    private IcalLoader(){};

    public static IcalLoader getInstance() {
        if (instance == null) {
            instance = new IcalLoader();
        }
        return instance;
    }

    public String getFilename2load() {
        return filename2load;
    }

    public void setFilename2load(final String filename2load) {
        this.filename2load = filename2load;
    }

    public Calendar loadCalender() throws ParserException, IOException {
        FileInputStream fin = new FileInputStream(getFilename2load());
        CalendarBuilder builder = new CalendarBuilder();
        return builder.build(fin);
    }

    public void saveCalendar(Calendar calendar) throws IOException {
        FileOutputStream fout = new FileOutputStream(getFilename2write());

        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, fout);

    }

    public String getFilename2write() {
        return filename2write;
    }

    public void setFilename2write(final String filename2write) {
        this.filename2write = filename2write;
    }
}
