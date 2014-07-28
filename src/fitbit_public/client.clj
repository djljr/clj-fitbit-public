(ns fitbit-public.client
	(:require [oauth.client :as oauth]
            [clojure.edn :as edn]
            [clj-http.client :as http]
            [clojure.data.json :as json]))

(def endpoints
  {:request-token "https://api.fitbit.com/oauth/request_token"
   :access-token  "https://api.fitbit.com/oauth/access_token"
   :authorize     "https://www.fitbit.com/oauth/authorize"})

(defn- oauth-headers 
  ([api-key url] 
    (oauth-headers api-key url false))
  ([api-key url debug]
    (let [consumer (oauth/make-consumer 
                     (:consumer-key api-key) 
                     (:consumer-secret api-key)
                     (:request-token endpoints)
                     (:access-token endpoints)
                     (:authorize endpoints)
                     :plaintext)
        request-token (oauth/request-token consumer)
        credentials (oauth/credentials consumer
                      nil nil
                      :GET url {})]
    (oauth/build-request (dissoc credentials :oauth_token)))))

(defn- make-url [user-id]
  (str "http://api.fitbit.com/1/user/" user-id))

(defn- api-request [url headers debug]
  (json/read-str (:body (http/get url (merge headers {:throw-exceptions false} (if debug {:debug true}))))))

(defn- do-request [api-key user-id path debug]
  (let [url (str (make-url user-id) path)
        headers (oauth-headers api-key url)]
    (api-request url headers debug)))

(defmacro def-request [name path]
  `(defn ~name [api-key# user-id# & {:keys [debug#] :or {debug# false}}]
    (do-request api-key# user-id# ~path debug#)))

(defmacro def-request-date [name path]
  `(defn ~name [api-key# user-id# date# & {:keys [debug#] :or {debug# false}}]
    (do-request api-key# user-id# (str ~path "/date/" date# ".json") debug#)))

(defmacro def-request-timeseries [name path]
  `(defn ~name [api-key# user-id# base-date# period-or-end-date# & {:keys [debug#] :or {debug# false}}]
    (do-request api-key# user-id# (str ~path "/date/" base-date# "/" period-or-end-date# ".json") debug#)))

(def-request profile "/profile.json")

; single day requests

(def-request-date activities "/activities")
(def-request-date body "/body")
(def-request-date food-log "/foods/log")
(def-request-date sleep "/sleep")

; timeseries

;; food

(def-request-timeseries timeseries-foods-calories-in "/foods/log/caloriesIn")
(def-request-timeseries timeseries-foods-water "/foods/log/water")

;; activities

(def-request-timeseries timeseries-activities-calories "/activities/calories")
(def-request-timeseries timeseries-activities-calories-bmr "/activities/caloriesBMR")
(def-request-timeseries timeseries-activities-steps "/activities/steps")
(def-request-timeseries timeseries-activities-distance "/activities/distance")
(def-request-timeseries timeseries-activities-floors "/activities/floors")
(def-request-timeseries timeseries-activities-elevation "/activities/elevation")
(def-request-timeseries timeseries-activities-minutes-sedentary "/activities/minutesSedentary")
(def-request-timeseries timeseries-activities-minutes-lightly-active "/activities/minutesLightlyActive")
(def-request-timeseries timeseries-activities-minutes-fairly-active "/activities/minutesFairlyActive")
(def-request-timeseries timeseries-activities-minutes-very-active "/activities/minutesVeryActive")
(def-request-timeseries timeseries-activities-activity-calories "/activities/activityCalories")

;; activities tracker

(def-request-timeseries timeseries-activities-tracker-calories "/activities/tracker/calories")
(def-request-timeseries timeseries-activities-tracker-steps "/activities/tracker/steps")
(def-request-timeseries timeseries-activities-tracker-distance "/activities/tracker/distance")
(def-request-timeseries timeseries-activities-tracker-floors "/activities/tracker/floors")
(def-request-timeseries timeseries-activities-tracker-elevation "/activities/tracker/elevation")
(def-request-timeseries timeseries-activities-tracker-minutes-sedentary "/activities/tracker/minutesSedentary")
(def-request-timeseries timeseries-activities-tracker-minutes-lightly-active "/activities/tracker/minutesLightlyActive")
(def-request-timeseries timeseries-activities-tracker-minutes-fairly-active "/activities/tracker/minutesFairlyActive")
(def-request-timeseries timeseries-activities-tracker-minutes-very-active "/activities/tracker/minutesVeryActive")
(def-request-timeseries timeseries-activities-tracker-minutes-activity-calories "/activities/tracker/activityCalories")

;; sleep

(def-request-timeseries timeseries-sleep-start-time "/sleep/startTime")
(def-request-timeseries timeseries-sleep-time-in-bed "/sleep/timeInBed")
(def-request-timeseries timeseries-sleep-minutes-asleep "/sleep/minutesAsleep")
(def-request-timeseries timeseries-sleep-awakenings-count "/sleep/awakeningsCount")
(def-request-timeseries timeseries-sleep-minutes-awake "/sleep/minutesAwake")
(def-request-timeseries timeseries-sleep-minutes-to-fall-asleep "/sleep/minutesToFallAsleep")
(def-request-timeseries timeseries-sleep-minutes-after-wakeup "/sleep/minutesAfterWakeup")
(def-request-timeseries timeseries-sleep-efficiency "/sleep/efficiency")

;; body

(def-request-timeseries timeseries-body-weight "/body/weight")
(def-request-timeseries timeseries-body-bmi "/body/bmi")
(def-request-timeseries timeseries-body-fat "/body/fat/date")
