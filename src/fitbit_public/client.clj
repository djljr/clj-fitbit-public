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

(defn oauth-headers 
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

(defn make-url [user-id]
  (str "http://api.fitbit.com/1/user/" user-id))

(defn api-request [url headers debug]
  (json/read-str (:body (http/get url (merge headers {:throw-exceptions false} (if debug {:debug true}))))))

(defn profile 
  ([user-id] 
    (profile false))
  ([user-id debug]
    (let [url (str (make-url user-id) "/profile.json")
          headers (oauth-headers url)]
      (api-request url headers debug))))

(defn activities 
  ([user-id date]
    (activities user-id date false))
  ([user-id date debug] 
    (let [url (str (make-url user-id) "/activities/date/" date ".json")
          headers (oauth-headers url)]
      (api-request url headers debug))))

(defn body
  ([user-id date]
    (body user-id date false))
  ([user-id date debug]
    (let [url (str (make-url user-id) "/body/date/" date ".json")
          headers (oauth-headers url)]
      (api-request url headers debug))))

(defn food-log
  ([user-id date]
    (food-log user-id date false))
  ([user-id date debug]
    (let [url (str (make-url user-id) "/foods/log/date/" date ".json")
          headers (oauth-headers url)]
      (api-request url headers debug))))

(defn sleep
  ([user-id date]
    (sleep user-id date false))
  ([user-id date debug]
    (let [url (str (make-url user-id) "/sleep/date/" date ".json")
          headers (oauth-headers url)]
      (api-request url headers debug))))

(defn steps-timeseries 
  ([user-id start-date end-date] 
    (steps-timeseries user-id start-date end-date false))
  ([user-id start-date end-date debug]
    (let [url (str (make-url user-id) "/activities/steps/date/" start-date "/" end-date ".json")
          headers (oauth-headers url)]
      (api-request url headers debug))))