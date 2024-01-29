FROM node:lts-alpine
COPY package.json .
RUN npm install
COPY . .
RUN npm run build
EXPOSE 9528
CMD ["npm", "run", "dev"]
