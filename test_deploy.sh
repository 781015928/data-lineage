
./gradlew assemble


function stop() {


        echo "stop data-lineage-web"
        ps -ef | grep data-lineage-web | grep -v grep | awk '{print $2}' | xargs kill -9
        echo "stop data-lineage-web success"
        return 0
}
function start() {
    echo "start data-lineage-web"
    nohup java -jar data-lineage-web.jar  --server.port=18080 > data-lineage-web.log 2>&1 &
    echo "start data-lineage-web success"

}



stop
rm -f data-lineage-web.jar
cp data-lineage-web/build/libs/data-lineage-web-1.0.0-SNAPSHOT.jar ./data-lineage-web.jar
start
tail -f data-lineage-web.log