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

    List<TimeRange> mandatoryBusyTimes = getBusyTimes(events, request, "mandatory");
    //can maybe move down, dont need to sort now
    Collections.sort(mandatoryBusyTimes, TimeRange.ORDER_BY_START);
    mandatoryBusyTimes = mergeIntervals(mandatoryBusyTimes);
    //mandatoryResult = getAvailableTimes(mandatoryBusyTimes, request.getDuration());

    if (!request.getOptionalAttendees().isEmpty()) {
    System.out.println("-------");  
    System.out.println("mandatory");
    for (TimeRange time: mandatoryBusyTimes) {
        System.out.println(time.toString());
    }

    List<TimeRange> optionalBusyTimes = getBusyTimes(events, request, "optional");
    System.out.println("optional");
    for (TimeRange time: optionalBusyTimes) {
        System.out.println(time.toString());
    }
    List<TimeRange> combinedTimes = new ArrayList<>(optionalBusyTimes);

	combinedTimes.addAll(mandatoryBusyTimes);
    Collections.sort(combinedTimes, TimeRange.ORDER_BY_START);
    //System.out.println(optionalBusyTimes.getClass());
    //System.out.println(mandatoryBusyTimes.getClass());


    combinedTimes = mergeIntervals(combinedTimes);
    System.out.println("combined");
    for (TimeRange time: combinedTimes) {
        System.out.println(time.toString());
    }
    
    List<TimeRange> result =  getAvailableTimes(combinedTimes, request.getDuration());
    System.out.println("result");
        for (TimeRange time: result) {
        System.out.println(time.toString());
        }
    System.out.println("-------");    
    return result;
    }

    
    return getAvailableTimes(mandatoryBusyTimes, request.getDuration());
    
  }

  // gets busy times for all request attendees
  private List<TimeRange> getBusyTimes(Collection<Event> events, MeetingRequest request, String requestType) {
      Collection<String> attendeeType;
    if (requestType.equals("mandatory")) {
        attendeeType = request.getAttendees();
    } else {
        attendeeType= request.getOptionalAttendees();
    }
    List<TimeRange> busyTimes = new ArrayList<>();
    for (Event event : events) {
      for (String attendees : attendeeType) {
        if (event.getAttendees().contains(attendees)) {
          busyTimes.add(event.getWhen());
        }
      }
    }
    return busyTimes;
  }

  // merges intervals of overlapping events
  private List<TimeRange> mergeIntervals(List<TimeRange> intervals) {
    if (intervals == null || intervals.size() <= 1) {
      return intervals;
    }

    List<TimeRange> result = new ArrayList<>();
    TimeRange startInterval = intervals.get(0);
    for (int i = 1; i < intervals.size(); i++) {
      TimeRange currentInterval = intervals.get(i);
      // if events overlap, merge
      if (currentInterval.start() <= startInterval.end()) {
        startInterval = TimeRange.fromStartEnd(startInterval.start(),
            Math.max(currentInterval.end(), startInterval.end()), /*inclusive=*/false);
      } else {
        result.add(startInterval);
        startInterval = currentInterval;
      }
    }
    result.add(startInterval);
    return result;
  }

  // gets available times of correct duration
  private List<TimeRange> getAvailableTimes(List<TimeRange> busyTimes, long duration) {
    List<TimeRange> availableTimes = new ArrayList<>();

    // finds gaps of requested duration time
    int currentStart = TimeRange.START_OF_DAY;
    for (TimeRange time : busyTimes) {
      if (time.start() - currentStart >= duration) {
        availableTimes.add(
            TimeRange.fromStartEnd(currentStart, time.start(), /*inclusive=*/false));
      }
      currentStart = time.end();
    }

    // finds gap of requested duration time between last meeting and end of day
    if (TimeRange.END_OF_DAY - currentStart >= duration) {
      availableTimes.add(
          TimeRange.fromStartEnd(currentStart, TimeRange.END_OF_DAY, /*inclusive=*/true));
    }
    return availableTimes;
  }
}
