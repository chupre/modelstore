# --- builder stage ---
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .

# build-time args
ARG VITE_URL
ARG VITE_API_URL

# Vite only uses variables prefixed with VITE_ at build time
ENV VITE_URL=${VITE_URL}
ENV VITE_API_URL=${VITE_API_URL}

RUN VITE_URL=$VITE_URL VITE_API_URL=$VITE_API_URL npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
