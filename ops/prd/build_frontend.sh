cd "$(dirname "$0")" || exit
VERSION="0.1"
docker build -t thodev/easyfile-frontend:latest ../../frontend
docker tag thodev/easyfile-frontend:latest thodev/easyfile-frontend:${VERSION}
docker push thodev/easyfile-frontend:${VERSION}