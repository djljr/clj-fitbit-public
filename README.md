# clj-fitbit-public

A Clojure library designed to access the public Fitbit API. The public API is what you can see using consumer_key only OAuth.

## Usage

Add the dependency to your project:

    [clj-fitbit-public "0.1.0"]

require it

    > (require '[fitbit-public.client :as fitbit])

Each endpoint is a separate function, you need to call the function with at least your OAuth consumer-key, consumer-secret of your [registered app][1] and the user-id of the profile you want to look at. Some also require a date or date range.

    > (def api-key {:consumer-key "..." :consumer-secret "..."})
    > (fitbit/profile api-key "<user-id>")
    {"user" { ... }}

[1]: http://dev.fitbit.com/

## Implemented API

Dates and the base-dates should be of the format "yyyy-MM-dd". A period for the Date-range based ones is either a date to specify the end date or a time period to specify the length of the window (e.g. 1d, 7d, 1w, 3m, 1y, max).

* No additional params
 * profile
* Date based functions, 
    (activities api-key user-id date)
 * activities
 * body
 * food-log
 * sleep
* [Date-range base functions][2], 
    (activities-steps api-key user-id base-date period)
 * foods-log-calories-in
 * foods-log-water
 * activities-calories
 * activities-calories-bmr
 * activities-steps
 * activities-distance
 * activities-floors
 * activities-elevation
 * activities-minutes-sedentary
 * activities-minutes-lightly-active
 * activities-minutes-fairly-active
 * activities-minutes-very-active
 * activities-activity-calories
 * activities-tracker-calories
 * activities-tracker-steps
 * activities-tracker-distance
 * activities-tracker-floors
 * activities-tracker-elevation
 * activities-tracker-minutes-sedentary
 * activities-tracker-minutes-lightly-active
 * activities-tracker-minutes-fairly-active
 * activities-tracker-minutes-very-active
 * activities-tracker-minutes-activity-calories
 * sleep-start-time
 * sleep-time-in-bed
 * sleep-minutes-asleep
 * sleep-awakenings-count
 * sleep-minutes-awake
 * sleep-minutes-to-fall-asleep
 * sleep-minutes-after-wakeup
 * sleep-efficiency
 * body-weight
 * body-bmi
 * body-fat

[2]: https://wiki.fitbit.com/display/API/API-Get-Time-Series

## License

Copyright © 2014 Dennis Lipovsky

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
