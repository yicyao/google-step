// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** Finds possible meeting times for a request for all mandatory participants */
public final class FindMeetingQuery {

    /**
    * @param events List of all known events with times and participants
    * @param request Meeting to be planned
    * @return List of all possible meeting times
    */
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        List<TimeRange> busyTimes = getBusyTimes(events, request);
        Collections.sort(busyTimes, TimeRange.ORDER_BY_START);
        busyTimes = mergeIntervals(busyTimes);
        return getAvailableTimes(busyTimes, request.getDuration());
    }

    // gets busy times for all request attendees
    private ArrayList<TimeRange> getBusyTimes(Collection<Event> events, MeetingRequest request) {
        ArrayList<TimeRange> busyTimes = new ArrayList<>();
        for (Event event: events) {
            for (String attendees: request.getAttendees()) {
                if (event.getAttendees().contains(attendees)) {
                    busyTimes.add(event.getWhen());
                }
            }
        }
        
        return busyTimes;
    }

    // merges overlapping intervals
    private List<TimeRange> mergeIntervals(List<TimeRange> intervals) {
    List<TimeRange> result = new ArrayList<>();
    for (int i = 0; i < intervals.size(); i++) {
        TimeRange currentRange = intervals.get(i);
        while (i + 1 < intervals.size() && currentRange.overlaps(intervals.get(i + 1))) {
        // Combine the two overlapping Timeintervals into a single TimeRange
        currentRange = TimeRange.fromStartEnd(currentRange.start(),
            Math.max(currentRange.end(), intervals.get(i + 1).end()),
            /* inclusive=*/false);
        i++;
        }
        result.add(currentRange);
    }
    return result;
    }

    //gets available times of correct duration
    private List<TimeRange> getAvailableTimes(List<TimeRange> busyTimes, long duration){
    List<TimeRange> availableTimes = new ArrayList<>();

    int currentStart = TimeRange.START_OF_DAY;
    for (TimeRange time: busyTimes) {
        if (time.start() - currentStart >= duration) {
            availableTimes.add(TimeRange.fromStartEnd(currentStart, time.start(), /* inclusive=*/ false));
        }
        currentStart = time.end();
    }

    if (TimeRange.END_OF_DAY - currentStart >= duration) {
        availableTimes.add(TimeRange.fromStartEnd(currentStart, TimeRange.END_OF_DAY, /* inclusive=*/ true));
    }
    return availableTimes;
    }
  
}
