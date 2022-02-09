# icalmod
reformats wastecalendar from normal (6AM) appointments with alarm to whole-day-appointments for importing in Google-Calendar

### example
_SourceFormat:_
```
BEGIN:VEVENT
    UID:15085aca-c694-413e-9e03-934cdd7ebc0c
    DTSTAMP:20220131T115942Z
    SUMMARY;LANGUAGE=de:Restmüll 2 wö.
    DTSTART:20210114T050000Z
    DTEND:20210114T050000Z
    DESCRIPTION:Restmüll 2 wö.
    LOCATION:XXXXXXXXXXXXX
    BEGIN:VALARM
        ACTION:DISPLAY
        TRIGGER;RELATED=START:-PT720M
        DESCRIPTION:Restmüll 2 wö.
    END:VALARM
END:VEVENT
```

_Target:_
```
BEGIN:VEVENT
    DTSTAMP:20220209T194642Z
    DTSTART;VALUE=DATE:20210114
    DURATION:P1D
    SUMMARY:Restmüll 2 wö.
    UID:c0c097a5-31b7-4d07-9a0a-beecdefff0cb
END:VEVENT
```

- alarm removed (Google Calender support own alarm creation in calendar-settings for single and whole-day appointments)
- Set DTStart to Midnight (for creating 1 day appointment)
- Duration instead of DTEND used (Duration 1 DAY)
