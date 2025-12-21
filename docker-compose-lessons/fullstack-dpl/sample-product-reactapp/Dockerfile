FROM node:lts-alpine3.23 AS builder
WORKDIR /app
COPY package*.json ./
ENV REACT_APP_BASE_URL=http://spring-svc:8080
RUN npm install
COPY . .
RUN npm run build
FROM nginx:mainline-alpine3.23 AS prod
COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]