cd "$(dirname "$0")" || exit
mvn clean install -f ../../api && docker compose up -d --build 