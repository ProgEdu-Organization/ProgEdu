FROM mongo

ARG MONGO_USER
ENV MONGO_USER=$MONGO_USER

ARG MONGO_PASSWORD
ENV MONGO_PASSWORD=$MONGO_PASSWORD

ARG MONGO_DATABASE
ENV MONGO_DATABASE=$MONGO_DATABASE

COPY ./docker-entrypoint-initdb.d/init-mongo.sh /docker-entrypoint-initdb.d/.
RUN chmod +x ./docker-entrypoint-initdb.d/init-mongo.sh
CMD ./docker-entrypoint-initdb.d/init-mongo.sh  "'$MONGO_USER'" "'$MONGO_PASSWORD'" "'$MONGO_DATABASE'"