FROM node:12 as build-stage
WORKDIR /src
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build