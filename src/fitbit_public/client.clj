(ns fitbit-public.client
	(:require [oauth.client :as oauth]
            [clojure.edn :as edn]
            [clj-http.client :as http]))

(def settings
  (with-open [in (java.io.PushbackReader. (clojure.java.io/reader "settings.edn"))]
    (edn/read in)))

(def auth-urls
  {:request-token "https://api.fitbit.com/oauth/request_token"
   :access-token  "https://api.fitbit.com/oauth/access_token"
   :authorize     "https://www.fitbit.com/oauth/authorize"})

(defn profile [user]
  (let [url (str "http://api.fitbit.com/1/user/" user "/profile.json")
        consumer (oauth/make-consumer 
                   (:consumer-key settings) 
                   (:consumer-secret settings)
                   (:request-token auth-urls)
                   (:access-token auth-urls)
                   (:authorize auth-urls)
                   :hmac-sha1)
        request-token (oauth/request-token consumer)
        credentials (oauth/credentials consumer
                      (:oauth_token request-token)
                      (:oauth_token_secret request-token)
                      :POST url {})
        headers (oauth/build-request credentials)
        _ (println headers)]
    (http/post url headers)))
