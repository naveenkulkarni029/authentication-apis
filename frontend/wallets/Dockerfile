FROM node:latest
WORKDIR /wallets
COPY package.json ./
RUN npm install
COPY . .
CMD ["npm", "start"]
