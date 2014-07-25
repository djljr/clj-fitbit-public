(ns fitbit-public.client
	(:require [oauth.client :as oauth]
            [clojure.edn :as edn]
            [clj-http.client :as http]))

(def settings
  (with-open [in (java.io.PushbackReader. (clojure.java.io/reader "settings.edn"))]
    (edn/read in)))

(def endpoints
  {:request-token "https://api.fitbit.com/oauth/request_token"
   :access-token  "https://api.fitbit.com/oauth/access_token"
   :authorize     "https://www.fitbit.com/oauth/authorize"})

(defn profile [user]
    (let [url (str "http://api.fitbit.com/1/user/" user "/activities/date/2014-07-24.json")
        consumer (oauth/make-consumer 
                   (:consumer-key settings) 
                   (:consumer-secret settings)
                   (:request-token endpoints)
                   (:access-token endpoints)
                   (:authorize endpoints)
                   :plaintext)
        request-token (oauth/request-token consumer)
        credentials (oauth/credentials consumer
                      nil nil
                      :POST url {})
        headers (oauth/build-request (dissoc credentials :oauth_token))]
    (http/post url (merge headers))))
