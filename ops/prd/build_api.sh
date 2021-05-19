cd "$(dirname "$0")" || exit
VERSION="0.1"
mvn clean install -f ../../api
docker build -t thodev/easyfile-api:latest ../../api
docker tag thodev/easyfile-api:latest thodev/easyfile-api:${VERSION}
docker push thodev/easyfile-api:${VERSION}