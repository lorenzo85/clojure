(defproject tiny-web "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :java-source-paths ["src/java"]
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot tiny-web.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
