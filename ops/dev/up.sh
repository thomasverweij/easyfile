cd "$(dirname "$0")"
mvn clean install -f ../../api && docker compose up -d --build "${1}"