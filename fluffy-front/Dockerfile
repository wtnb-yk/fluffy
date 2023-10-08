FROM node:18

WORKDIR /fluffy/fluffy-front
COPY . .
RUN npm i -g pnpm

ENV CHOKIDAR_USEPOLLING=true
ENV HOST 0.0.0.0
EXPOSE 10273

RUN pnpm install
CMD [ "/bin/sh", "-c", "pnpm dev --host" ]

#CMD [ "/bin/sh", "-c", "pnpm install && pnpm dev --host" ]
