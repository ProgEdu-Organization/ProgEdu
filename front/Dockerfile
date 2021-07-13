FROM node:12.16.1 as builder

WORKDIR /usr/src/app

COPY package.json package.json

RUN npm i
COPY . .

ARG BACK_END_URL
ARG SPRING_BACK_END_URL

RUN apt-get update \
    && apt-get -y install gettext-base \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    && envsubst < ./src/environments/environment.docker.ts > ./src/environments/environment.prod.ts \
    && npm run build


FROM nginx:1.19.5
COPY ./nginx/default.conf /etc/nginx/conf.d/
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /usr/src/app/dist /usr/share/nginx/html/
CMD ["nginx", "-g", "daemon off;"]

 





