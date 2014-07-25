(ns fitbit-public.client
	(:require [oauth.client :as oauth]
            [clojure.edn :as edn]
            [clj-http.client :as http]
            [clojure.data.json :as json]))

(def settings
  (with-open [in (java.io.PushbackReader. (clojure.java.io/reader "settings.edn"))]
    (edn/read in)))

(def endpoints
  {:request-token "https://api.fitbit.com/oauth/request_token"
   :access-token  "https://api.fitbit.com/oauth/access_token"
   :authorize     "https://www.fitbit.com/oauth/authorize"})

(defn- oauth-headers 
  ([url] 
    (oauth-headers url false))
  ([url debug]
    (let [consumer (oauth/make-consumer 
                   (:consumer-key settings) 
                   (:consumer-secret settings)
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

(defn- do-request [user-id path debug]
  (let [url (str (make-url user-id) path)
        headers (oauth-headers url)]
    (api-request url headers debug)))

; single day requests

(defn profile [user-id & {:keys [debug] :or {debug false}}] 
  (do-request user-id "/profile.json" debug))

(defn activities [user-id date & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/date/" date ".json") debug))

(defn body [user-id date & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/body/date/" date ".json") debug))

(defn food-log [user-id date & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/foods/log/date/" date ".json") debug))

(defn sleep [user-id date & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/date/" date ".json") debug))

; timeseries

;; food

(defn timeseries-foods-calories-in [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/foods/log/caloriesIn/date/" start "/" end ".json") debug))

(defn timeseries-foods-water [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/foods/log/water/date/" start "/" end ".json") debug))

;; timeseries activities

(defn timeseries-activities-calories [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/calories/date/" start "/" end ".json") debug))

(defn timeseries-activities-calories-bmr [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/caloriesBMR/date/" start "/" end ".json") debug))

(defn timeseries-activities-steps [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/steps/date/" start "/" end ".json") debug))

(defn timeseries-activities-distance [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/distance/date/" start "/" end ".json") debug))

(defn timeseries-activities-floors [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/floors/date/" start "/" end ".json") debug))

(defn timeseries-activities-elevation [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/elevation/date/" start "/" end ".json") debug))

(defn timeseries-activities-minutes-sedentary [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/minutesSedentary/date/" start "/" end ".json") debug))

(defn timeseries-activities-minutes-lightly-active [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/minutesLightlyActive/date/" start "/" end ".json") debug))

(defn timeseries-activities-minutes-fairly-active [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/minutesFairlyActive/date/" start "/" end ".json") debug))

(defn timeseries-activities-minutes-very-active [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/minutesVeryActive/date/" start "/" end ".json") debug))

(defn timeseries-activities-activity-calories [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/activityCalories/date/" start "/" end ".json") debug))

;; timeseries activities tracker

(defn timeseries-activities-tracker-calories [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/calories/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-steps [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/steps/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-distance [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/distance/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-floors [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/floors/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-elevation [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/elevation/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-minutes-sedentary [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/minutesSedentary/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-minutes-lightly-active [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/minutesLightlyActive/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-minutes-fairly-active [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/minutesFairlyActive/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-minutes-very-active [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/minutesVeryActive/date/" start "/" end ".json") debug))

(defn timeseries-activities-tracker-minutes-activity-calories [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/activities/tracker/activityCalories/date/" start "/" end ".json") debug))

;; sleep

(defn timeseries-sleep-start-time [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/startTime/date/" start "/" end ".json") debug))

(defn timeseries-sleep-time-in-bed [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/timeInBed/date/" start "/" end ".json") debug))

(defn timeseries-sleep-minutes-asleep [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/minutesAsleep/date/" start "/" end ".json") debug))

(defn timeseries-sleep-awakenings-count [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/awakeningsCount/date/" start "/" end ".json") debug))

(defn timeseries-sleep-minutes-awake [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/minutesAwake/date/" start "/" end ".json") debug))

(defn timeseries-sleep-minutes-to-fall-asleep [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/minutesToFallAsleep/date/" start "/" end ".json") debug))

(defn timeseries-sleep-minutes-after-wakeup [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/minutesAfterWakeup/date/" start "/" end ".json") debug))

(defn timeseries-sleep-efficiency [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/sleep/efficiency/date/" start "/" end ".json") debug))

;; body

(defn timeseries-body-weight [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/body/weight/date/" start "/" end ".json") debug))

(defn timeseries-body-bmi [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/body/bmi/date/" start "/" end ".json") debug))

(defn timeseries-body-fat [user-id start end & {:keys [debug] :or {debug false}}]
  (do-request user-id (str "/body/fat/date/" start "/" end ".json") debug))
