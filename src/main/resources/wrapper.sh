#!/user/bin/env bash
while ! exec 6<>/dev/tcp/${DATABASE_HOST}/${DATABASE_PORT}; do
    echo "Trying to connect to MySql at ${DATABASE_HOST}:${DATABASE_PORT}..."
    sleep 10
done
echo ">> connected to MySql Database! <<"
java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar stocker.jar
